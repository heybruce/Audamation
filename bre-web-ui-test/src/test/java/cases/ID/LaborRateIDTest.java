package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import steps.*;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class LaborRateIDTest extends TestBase {
    private LaborRatesPO laborRatesPO;
    private ProcessStepIDPO processStepIDPO;
    private ReportsPO reportsPO;
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO) context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    @Test(description = "Select existing contracts")
    public void selectExistingContracts() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        processStepIDPO.clickLaborRateID();
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

        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        processStepIDPO.clickLaborRateID();
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
        laborRatesPO.clearLaborRate1Textbox();
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterLaborRate1Textbox(newRate);
        //Click on paint rate to make sure the mandatory check passed
        laborRatesPO.clickPaintRateTextbox();
        testCase.get().log(Status.INFO, "Update labor rate 1 value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepIDPO.clickDamageCaptureID();
        processStepIDPO.clickLaborRateID();

        String LR1_New=laborRatesPO.getLabourRate1();
        Assert.assertNotEquals(LR1_Original, LR1_New, "Labour rate 1 is not updated");
        Assert.assertEquals(LR1_New, newRate);
        testCase.get().log(Status.PASS, "Labour rate 1 (updated): " + LR1_New);
    }

    @Test(description = "Modify paint rate details")
    public void modifyPaintRateDetails() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        processStepIDPO.clickLaborRateID();
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
        laborRatesPO.clearPaintRateTextbox();
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterPaintRateTextbox(newRate);
        //Click on labor rate 1 to make sure the mandatory check passed
        laborRatesPO.clickLaborRate1Textbox();
        testCase.get().log(Status.INFO, "Update paint rate value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepIDPO.clickDamageCaptureID();
        processStepIDPO.clickLaborRateID();

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
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Labor rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        String LR1_Before, PR_Before;
        LR1_Before = laborRatesPO.getLabourRate1();
        PR_Before = laborRatesPO.getPaintRate();

        // Add one IDBC
        setLaborRate.addIDBC("60", "500");

        // Verified IDBC is added
        Assert.assertEquals(laborRatesPO.getIdbcValue("60"), "500");
        testCase.get().log(Status.PASS, "IDBC 60 is added successfully");

        // Add two IDBC at one time
        setLaborRate.addTwoIDBCs("122", "10", "72", "10000");

        // Verified IDBC are added
        Assert.assertEquals(laborRatesPO.getIdbcValue("122"), "10");
        Assert.assertEquals(laborRatesPO.getIdbcValue("72"), "10,000");
        testCase.get().log(Status.PASS, "IDBC 122 & 72 are added successfully");

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
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Labor rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        // Add IDBC
        setLaborRate.addIDBC("122", "10");
        setLaborRate.addIDBC("72", "10000");

        // Verified IDBC are added
        Assert.assertEquals(laborRatesPO.getIdbcValue("122"), "10");
        Assert.assertEquals(laborRatesPO.getIdbcValue("72"), "10,000");
        testCase.get().log(Status.PASS, "IDBC 122 & 72 are added successfully");

        // Delete IDBC
        laborRatesPO.clickIDBCCheckbox("122");
        laborRatesPO.clickIDBCCheckbox("72");
        laborRatesPO.clickDeleteButton();
        testCase.get().log(Status.INFO, "IDBCs are deleted");

        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("122")));
        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("72")));
        testCase.get().log(Status.PASS, "IDBCs are not present in the list");
    }

    @Test(description = "Apply IDBC value with fixed amount")
    public void applyIDBCValueWithFixedAmount() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Labor rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        //Switch to Damage Capturing page
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzS_zone_frontOuter"), vehicleElementData.getString("benzS_position_0471_Bonnet"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Do calculation - the first time
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the first time
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");

        // Labor rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Add IDBC
        double idbc74Value = 6666;
        double idbc20Value = 20;
        setLaborRate.addIDBC("74", String.valueOf(idbc74Value));
        setLaborRate.addIDBC("122", String.valueOf(idbc20Value));

        //Switch to Calculation Options page
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the second time
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");

        //Check IDBC 74 is applied
        //IDBC 74: Alignment (added in additional cost)
        double laborTotalRow0= Double.valueOf(reportsPO.getAdditionalCost(0).replaceAll(",",""));
        double laborTotalRow1= Double.valueOf(reportsPO.getAdditionalCost(1).replaceAll(",",""));
        Assert.assertEquals(laborTotalRow1 - laborTotalRow0, idbc74Value,
                "Additional cost difference is not equal to "+ idbc74Value);
        testCase.get().log(Status.PASS, "IDBC 74 is applied in calculation result");

        //Check IDBC 122 is applied
        //IDBC 122: Deduction OEM spare part cost
        double partsTotalRow0= Double.valueOf(reportsPO.getPartsTotal(0).replaceAll(",",""));
        double partsTotalRow1= Double.valueOf(reportsPO.getPartsTotal(1).replaceAll(",",""));
        Assert.assertEquals(partsTotalRow1 * 1.0, partsTotalRow0 * (1 + idbc20Value / 100),
                "Parts price is not equal to "+ String.valueOf(partsTotalRow0 * (1 + idbc20Value / 100)));
        testCase.get().log(Status.PASS, "IDBC 122 is applied in calculation result");
    }

    @Test(description = "Check if Labor rates are mandatory fields")
    public void verifyMandatoryLaborRatesFields() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //Try to open Damage Capture page
        try {
            processStepIDPO.clickDamageCaptureID();
        } catch (TimeoutException e) {
            testCase.get().log(Status.PASS, "Damage Capture page cannot be opened");
        }
        Assert.assertEquals(processStepIDPO.getCurrentStepText(), testData.getString("Step_LaborRates"));
        testCase.get().log(Status.PASS, "Page is redirected to Labor Rates page");
        Assert.assertEquals(laborRatesPO.getWarningMessage(), testData.getString("labor_rates_mandatory_field_warning_message"));
        testCase.get().log(Status.PASS, "Mandatory field warning message is displayed");

        //Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select an existing partnership");

        //Switch to Damage Capturing page again
        processStepIDPO.clickDamageCaptureID();
        testCase.get().log(Status.PASS, "Damage Capturing page can be opened");
    }
}
