package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.*;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class LabourRateJPTest extends TestBase {
    private ProcessStepJPPO processStepJPPO;
    private LaborRatesPO laborRatesPO;
    private DamageCapturingPO damageCapturingPO;
    private ReportsPO reportsPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepJPPO = (ProcessStepJPPO)context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        laborRatesPO  = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
    }

    @Test(description = "Select existing contracts")
    public void selectExistingContracts() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        String LR1, PR;
        LR1 = laborRatesPO.getLabourRate1();
        PR = laborRatesPO.getPaintRate();

        Assert.assertFalse(LR1.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 1: " + LR1);
        Assert.assertFalse(PR.isEmpty());
        testCase.get().log(Status.PASS, "Paint Rate: " + PR);
    }

    @Test(description = "Modify labour rates details")
    public void modifyLabourRateDetails() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String LR1_Original;
        LR1_Original=laborRatesPO.getLabourRate1();
        testCase.get().log(Status.INFO, "Labour Rate 1 (original): " + LR1_Original);

        //Update the new value 888
        String newRate = "888";
        laborRatesPO.enterLaborRate1Textbox(newRate);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        testCase.get().log(Status.INFO, "Update three labour rate value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepJPPO.clickDamageCaptureTab();
        processStepJPPO.clickLabourRatesTab();

        String LR1_New = laborRatesPO.getLabourRate1();
        Assert.assertNotEquals(LR1_Original, LR1_New, "Labour rate 1 is not updated");
        Assert.assertEquals(LR1_New, newRate);
        testCase.get().log(Status.PASS, "Labour rate 1 (updated): " + LR1_New);
    }

    @Test(description = "Modify paint rate details")
    public void modifyPaintRateDetails() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String PR_Original;
        PR_Original=laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Paint rate (original): " + PR_Original);

        //Update the new value 888
        String newRate = "888";
        laborRatesPO.enterPaintRateTextbox(newRate);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        testCase.get().log(Status.INFO, "Update paint rate value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepJPPO.clickDamageCaptureTab();
        processStepJPPO.clickLabourRatesTab();

        String PR_New = laborRatesPO.getPaintRate();
        Assert.assertNotEquals(PR_Original, PR_New, "Paint rate is not updated");
        Assert.assertEquals(PR_New, newRate);
        testCase.get().log(Status.PASS, "Paint rate (updated): " + PR_New);
    }

    @Test(description = "Add one or more IDBC at one time")
    public void addOneOrMoreIDBCAtOneTime() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        // Labor rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        String LR1_Before, PR_Before;
        LR1_Before = laborRatesPO.getLabourRate1();
        PR_Before = laborRatesPO.getPaintRate();

        // Add one IDBC
        setLaborRate.addIDBC("75", "15");

        // Verified IDBC is added
        Assert.assertEquals(laborRatesPO.getIdbcValue("75"), "15");
        testCase.get().log(Status.PASS, "IDBC 75 is added successfully");

        // Add two IDBC at one time
        setLaborRate.addTwoIDBCs("33", "20", "20", "10");

        // Verified IDBC are added
        Assert.assertEquals(laborRatesPO.getIdbcValue("33"), "20");
        Assert.assertEquals(laborRatesPO.getIdbcValue("20"), "10");
        testCase.get().log(Status.PASS, "IDBC 33 & 20 are added successfully");

        // Verified selected Labor rate are not changed
        String LR1_After, PR_After;
        LR1_After = laborRatesPO.getLabourRate1();
        PR_After = laborRatesPO.getPaintRate();

        Assert.assertEquals(LR1_Before, LR1_After);
        Assert.assertEquals(PR_Before, PR_After);
        testCase.get().log(Status.PASS, "Selected labor rate are not changed after IDBC added");
    }

    @Test(description = "Delete one or more IDBC at one time")
    public void deleteOneOrMoreIDBCAtOneTime() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        // Labor rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        // Add IDBC
        setLaborRate.addIDBC("33", "20");
        setLaborRate.addIDBC("27", "6500");

        // Verified IDBC are added
        Assert.assertTrue(isElementPresent(laborRatesPO.getIdbcLabelId("33")));
        Assert.assertTrue(isElementPresent(laborRatesPO.getIdbcLabelId("27")));
        Assert.assertEquals(laborRatesPO.getIdbcValue("33"), "20");
        Assert.assertEquals(laborRatesPO.getIdbcValue("27"), "6,500");
        testCase.get().log(Status.PASS, "IDBC 33 & 27 are added successfully");

        // Delete IDBC
        laborRatesPO.clickIDBCCheckbox("33");
        laborRatesPO.clickIDBCCheckbox("27");
        laborRatesPO.clickDeleteButton();
        testCase.get().log(Status.INFO, "IDBCs are deleted");

        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("33")));
        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("27")));
        testCase.get().log(Status.PASS, "IDBCs are not present in the list");
    }

    @Test(description = "Apply IDBC value with fixed amount")
    public void applyIDBCValueWithFixedAmount() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        // Labor rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        //Switch to Damage Capturing page
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the first time
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");

        //Display all columns for calculation list
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();

        // Labor rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Add IDBC
        double idbc33Value = 20;
        double idbc20Value = 10;
        setLaborRate.addTwoIDBCs("33", String.valueOf(idbc33Value), "20", String.valueOf(idbc20Value));
        testCase.get().log(Status.INFO, "IDBC 33 & 20 are added successfully");

        //Switch to Calculation Options page
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the second time
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");

        //Check IDBC 33 is applied
        //IDBC 33: Labor deduction on total labor costs in %
        String laborTotalRow0= reportsPO.getLabourTotal(0).replaceAll("\\pP","");
        String laborTotalRow1= reportsPO.getLabourTotal(1).replaceAll("\\pP","");
        long expectedLaborTotalAfterDeduct = Math.round(Double.valueOf(laborTotalRow0) * (1 - idbc33Value / 100));
        Assert.assertEquals(Long.valueOf(laborTotalRow1).longValue(), expectedLaborTotalAfterDeduct);
        testCase.get().log(Status.PASS, "IDBC 33 is applied in calculation result");

        //Check IDBC 20 is applied
        //IDBC 20: Deduction spare part costs in %
        String partsTotalRow0 = reportsPO.getPartsTotal(0).replaceAll("\\pP","");
        String partsTotalRow1 = reportsPO.getPartsTotal(1).replaceAll("\\pP","");
        long expectedPartsTotalAfterDeduct = Math.round(Double.valueOf(partsTotalRow0) * (1 - idbc20Value / 100));
        Assert.assertEquals(Long.valueOf(partsTotalRow1).longValue(), expectedPartsTotalAfterDeduct);
        testCase.get().log(Status.PASS, "IDBC 20 is applied in calculation result");
    }
}
