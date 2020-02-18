package cases.KR;

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
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.*;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class LabourRatesTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
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
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO) context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO) context.getBean("ReportsPO");
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        String LR1, LR2, LR3, PR;
        LR1 = laborRatesPO.getLabourRate1();
        LR2 = laborRatesPO.getLabourRate2();
        LR3 = laborRatesPO.getLabourRate3();
        PR = laborRatesPO.getPaintRate();

        Assert.assertFalse(LR1.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 1: " + LR1);
        Assert.assertFalse(LR2.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 2: " + LR2);
        Assert.assertFalse(LR3.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 3: " + LR3);
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String LR1_Original, LR2_Original, LR3_Original;
        LR1_Original = laborRatesPO.getLabourRate1();
        testCase.get().log(Status.INFO, "Labour Rate 1 (original): " + LR1_Original);
        LR2_Original = laborRatesPO.getLabourRate2();
        testCase.get().log(Status.INFO, "Labour Rate 2 (original): " + LR2_Original);
        LR3_Original = laborRatesPO.getLabourRate3();
        testCase.get().log(Status.INFO, "Labour Rate 3 (original): " + LR3_Original);

        //Update the new value 888
        String newRate = "888";
        laborRatesPO.enterLaborRate1Textbox(newRate);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterLaborRate2Textbox(newRate);
        laborRatesPO.enterLaborRate3Textbox(newRate);
        //this is workaround to trigger save text value (issue happens at v19.07 and can't reproduce manually)
        laborRatesPO.clickLaborRate2Textbox();
        testCase.get().log(Status.INFO, "Update three labour rate value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepKRPO.clickModifySparePartsTab();
        processStepKRPO.clickLabourRatesKRTab();

        String LR1_New, LR2_New, LR3_New;
        LR1_New = laborRatesPO.getLabourRate1();
        LR2_New = laborRatesPO.getLabourRate2();
        LR3_New = laborRatesPO.getLabourRate3();

        Assert.assertNotEquals(LR1_Original, LR1_New, "Labour rate 1 is not updated");
        Assert.assertEquals(LR1_New, newRate);
        testCase.get().log(Status.PASS, "Labour rate 1 (updated): " + LR1_New);
        Assert.assertNotEquals(LR2_Original, LR2_New, "Labour rate 2 is not updated");
        Assert.assertEquals(LR2_New, newRate);
        testCase.get().log(Status.PASS, "Labour rate 2 (updated): " + LR2_New);
        Assert.assertNotEquals(LR3_Original, LR3_New, "Labour rate 3 is not updated");
        Assert.assertEquals(LR3_New, newRate);
        testCase.get().log(Status.PASS, "Labour rate 3 (updated): " + LR3_New);
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String PR_Original;
        PR_Original = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Paint rate (original): " + PR_Original);

        //Update the new value 888
        String newRate = "888";
        laborRatesPO.enterPaintRateTextbox(newRate);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        testCase.get().log(Status.INFO, "Update paint rate value to 888");

        //Switch to another tab and go back to labour rate tab
        processStepKRPO.clickModifySparePartsTab();
        processStepKRPO.clickLabourRatesKRTab();

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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Labor rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        String LR1_Before, LR2_Before, LR3_Before, PR_Before;
        LR1_Before = laborRatesPO.getLabourRate1();
        LR2_Before = laborRatesPO.getLabourRate2();
        LR3_Before = laborRatesPO.getLabourRate3();
        PR_Before = laborRatesPO.getPaintRate();

        // Add one IDBC
        setLaborRate.addIDBC("88", "10");

        // Verified IDBC is added
        Assert.assertEquals(laborRatesPO.getIdbcValue("88"), "10");
        testCase.get().log(Status.PASS, "IDBC 88 is added successfully");

        // Add two IDBC at one time
        setLaborRate.addTwoIDBCs("08", "800", "67", "700");

        // Verified IDBC are added
        Assert.assertEquals(laborRatesPO.getIdbcValue("08"), "800");
        Assert.assertEquals(laborRatesPO.getIdbcValue("67"), "700");
        testCase.get().log(Status.PASS, "IDBC 08 & 67 are added successfully");

        // Verified selected Labor rate are not changed
        String LR1_After, LR2_After, LR3_After, PR_After;
        LR1_After = laborRatesPO.getLabourRate1();
        LR2_After = laborRatesPO.getLabourRate2();
        LR3_After = laborRatesPO.getLabourRate3();
        PR_After = laborRatesPO.getPaintRate();

        Assert.assertEquals(LR1_Before, LR1_After);
        Assert.assertEquals(LR2_Before, LR2_After);
        Assert.assertEquals(LR3_Before, LR3_After);
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Labor rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        // Add IDBC 08 & 67
        setLaborRate.addIDBC("08", "800");
        setLaborRate.addIDBC("67", "700");

        // Verified IDBC are added
        Assert.assertTrue(isElementPresent(laborRatesPO.getIdbcLabelId("08")));
        Assert.assertTrue(isElementPresent(laborRatesPO.getIdbcLabelId("67")));
        Assert.assertEquals(laborRatesPO.getIdbcValue("08"), "800");
        Assert.assertEquals(laborRatesPO.getIdbcValue("67"), "700");
        testCase.get().log(Status.PASS, "IDBC 08 & 67 are added successfully");

        // Delete IDBC 08 & 67
        laborRatesPO.clickIDBCCheckbox("08");
        laborRatesPO.clickIDBCCheckbox("67");
        laborRatesPO.clickDeleteButton();
        testCase.get().log(Status.INFO, "IDBCs are deleted");

        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("08")));
        Assert.assertFalse(isElementPresent(laborRatesPO.getIdbcLabelId("67")));
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Labor rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership");

        // Add IDBC 07 & 67
        double idbc07Value = 10;
        int idbc67Value = 700;
        setLaborRate.addIDBC("07", String.valueOf(idbc07Value));
        setLaborRate.addIDBC("67", String.valueOf(idbc67Value));
        testCase.get().log(Status.INFO, "IDBC 07 & 67 are added successfully");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing
        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");

        //Check IDBC 67 is applied
        //IDBC 67: Polishing (fixed amount) (added in additional cost)
        String additionalCost = reportsPO.getAdditionalCost(0);
        Assert.assertEquals(additionalCost, String.valueOf(idbc67Value));
        testCase.get().log(Status.PASS, "IDBC 67 is applied in calculation result");

        //Check IDBC 07 is applied
        //IDBC 07: Excess % deduction after the VAT calculation
        String repairTotal = reportsPO.getRepairTotal(0).replaceAll("\\pP", "");
        String grandTotal = reportsPO.getGrandTotalWTaxCombined(0).replaceAll("\\pP", "");
        double krTax = 0.1;
        double expectedGrandTotalWithTaxAfterDeduct = Math.ceil(Double.valueOf(repairTotal) * (1 + krTax) * (1 - (idbc07Value / 100)));
        Assert.assertEquals(Double.valueOf(grandTotal), expectedGrandTotalWithTaxAfterDeduct);
        testCase.get().log(Status.PASS, "IDBC 07 is applied in calculation result");
    }

    @Test(description = "Modify IDBC default value")
    public void modifyIdbcDefaultValue() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Labor rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("IDBC");

        String defaultValue1, defaultValue2, defaultValue3;
        defaultValue1 = laborRatesPO.getIdbcValue(0);
        defaultValue2 = laborRatesPO.getIdbcValue(1);
        defaultValue3 = laborRatesPO.getIdbcValue(2);

        for (int num = 0; num < 3; num++) {
            String newValue = String.valueOf((num + 1) * 1000);
            String idbc = laborRatesPO.getIdBlockCode(num);
            laborRatesPO.setIdbcValue(num, newValue);
            testCase.get().log(Status.INFO, "Set (" + idbc + ") value to " + newValue);
        }

        // click paint rate field to make sure idbc new value is finished inputting by automation
        laborRatesPO.clickPaintRateTextbox();

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Switch to Damage Capturing page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch back to Labour rate page");

        Assert.assertNotEquals(laborRatesPO.getIdbcValue(0), defaultValue1, "IDBC " + laborRatesPO.getIdBlockCode(0) + " value is not updated");
        Assert.assertNotEquals(laborRatesPO.getIdbcValue(1), defaultValue2, "IDBC " + laborRatesPO.getIdBlockCode(1) + " value is not updated");
        Assert.assertNotEquals(laborRatesPO.getIdbcValue(2), defaultValue3, "IDBC " + laborRatesPO.getIdBlockCode(2) + " value is not updated");
        testCase.get().log(Status.PASS, "The default IDBC value are all be modified");

        for (int num = 0; num < 3; num++) {
            String newValue = String.valueOf((num + 1) * 1000);
            String idbc = laborRatesPO.getIdBlockCode(num);
            Assert.assertEquals(laborRatesPO.getIdbcValue(num).replaceAll(",", ""), newValue);
            testCase.get().log(Status.PASS, "IDBC" + idbc + " new value (" + newValue + ") is updated successfully");
        }
    }

    @Test(description = "IDBC51 is triggered after select AZT paint method")
    public void triggerIdbc51ByAztPaintMethod(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Labor rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        // Select a partnership
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select a existing partnership with manufacturer paint method");
        setLaborRate.SelectPaintMethodByName("AZT");

        // Verified IDBC are added
        Assert.assertTrue(isElementPresent(laborRatesPO.getIdbcLabelId("51")));
        Assert.assertEquals(laborRatesPO.getIdbcValue("51"), "100");
        testCase.get().log(Status.PASS, "IDBC 51 is added automatically with paint method AZT");
    }
}
