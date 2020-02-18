package cases.KR.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.DamageCapturing;
import steps.Login;
import steps.Qapter.PlasticDamageRepair;
import steps.Qapter.Search;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class PlasticDamageRepairTest extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private ProcessStepKRPO processStepKRPO;
    private LaborRatesPO laborRatesPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        processStepKRPO = (ProcessStepKRPO)context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver((getDriver()));
    }

    @Test(description = "Add parts with Korean repair formula")
    public void addPartsWithKoreanRepairFormula(){
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

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.addThreePartsWithKoreanRepairFormula(testData.getString("bmw320_vehicle"));
    }

    @Test(description = "[Manufacturer][10WU] Repairing with severity type 1")
    public void manufacturerAnd10WURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[Manufacturer][12WU] Repairing with severity type 1")
    public void manufacturerAnd12WURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmwX3_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 12;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[Manufacturer][100TU] Repairing with severity type 1")
    public void manufacturerAnd100TURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("audiA6_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");
        laborRatesPO.selectManufTimePrintCodeByNameDropdown(testData.getString("CalOpt_Print_Code_By_Manuf"));
        testCase.get().log(Status.INFO, "Select print code by manufacturer time basis");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("audiA6_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[Manufacturer][1HR] Repairing with severity type 1")
    public void manufacturerAnd1HRRepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");
        laborRatesPO.selectManufTimePrintCodeByNameDropdown(testData.getString("CalOpt_Print_Code_By_Manuf"));
        testCase.get().log(Status.INFO, "Select print code by manufacturer time basis");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[AZT][10WU] Repairing with severity type 1")
    public void aztAnd10WURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("AZT");
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        int paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with AZT setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        int workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[AZT][12WU] Repairing with severity type 1")
    public void aztAnd12WURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("bmwX3_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("AZT");
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with AZT setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[AZT][100TU] Repairing with severity type 1")
    public void aztAnd100TURepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("audiA6_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("AZT");
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with AZT setting");
        laborRatesPO.selectManufTimePrintCodeByNameDropdown(testData.getString("CalOpt_Print_Code_By_Manuf"));
        testCase.get().log(Status.INFO, "Select print code by manufacturer time basis");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("audiA6_vehicle"), workUnit, paintRate);
    }

    @Test(description = "[AZT][1HR] Repairing with severity type 1")
    public void aztAnd1HRRepairWithType1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("AZT");
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with AZT setting");
        laborRatesPO.selectManufTimePrintCodeByNameDropdown(testData.getString("CalOpt_Print_Code_By_Manuf"));
        testCase.get().log(Status.INFO, "Select print code by manufacturer time basis");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType1(testData.getString("benzS400_vehicle"), workUnit, paintRate);
    }

    @Test(description = "Repairing with severity type 2")
    public void repairWithType2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType2(testData.getString("benzS400_vehicle"));
    }

    @Test(description = "[Manufacturer][12WU] Repairing with severity type 3 and damage type 1 ")
    public void manufacturerAnd12WURepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 661800;
        double workUnit = 12;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("bmw320_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[AZT][100TU] Repairing with severity type 3 and damage type 1 ")
    public void aztAnd100TURepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("audiA6_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnerShipByName("AZT");
        String manufacturerPaintRate = "50000"; //laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with AZT setting");
        laborRatesPO.selectManufTimePrintCodeByNameDropdown(testData.getString("CalOpt_Print_Code_By_Manuf"));
        testCase.get().log(Status.INFO, "Select print code by manufacturer time basis");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 654200;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("audiA6_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "Error handling for labor rate is not set")
    public void errorHandlingForNotSetLaborRate(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("bmw320_vehicle"));

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();

        //search part
        Search search = new Search();
        search.searchPart(vehicleElementData.getString("bmw320_rearBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        //Click repair and select severity type 3
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(2);
        Assert.assertEquals(damageCapturingPO.getDamageTypeItemNumber(), 4);
        testCase.get().log(Status.PASS, "There are 4 damage types displayed");

        //Select damage type
        damageCapturingPO.selectKRDamageItem(3);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(3));
        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(damageCapturingPO.KR_REPAIR_ERROR_DIALOG));
        Assert.assertEquals(damageCapturingPO.getRepairErrorMessage(), testData.getString("Error_LaborRateMissing"));
        testCase.get().log(Status.PASS, "Error pop up with message: " + testData.getString("Error_LaborRateMissing"));
        damageCapturingPO.clickBtnCloseInRepairErrorDialog();
        Assert.assertTrue(damageCapturingPO.getTotalWorkingUnits().isEmpty());
        testCase.get().log(Status.PASS, "There is no value for total working units");
        Assert.assertFalse(getDriver().findElement(By.id(damageCapturingPO.ID_BTN_CONTINUE_IN_REPAIR_PARAMETERS)).isEnabled());
        testCase.get().log(Status.PASS, "Continue button is disable in Repair parameters");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //search part
        search.searchPart(vehicleElementData.getString("bmw320_rearBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();

        //Click repair and select severity type 3
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(2);
        Assert.assertEquals(damageCapturingPO.getDamageTypeItemNumber(), 4);
        testCase.get().log(Status.PASS, "There are 4 damage types displayed");

        //Select damage type
        damageCapturingPO.selectKRDamageItem(3);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(3));
        Assert.assertFalse(damageCapturingPO.getTotalWorkingUnits().isEmpty());
        testCase.get().log(Status.PASS, "The total working unit is displayed");
        Assert.assertTrue(getDriver().findElement(By.id(damageCapturingPO.ID_BTN_CONTINUE_IN_REPAIR_PARAMETERS)).isEnabled());
        testCase.get().log(Status.PASS, "Continue button is enable in Repair parameters");
    }

    @Test(description = "[Manufacturer][10WU][500000<] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice50wUpRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();

        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 893000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000=] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice50wRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 500000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000<;<500000] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice35w50wRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 375000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000=] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice35wRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 350000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200000<;<350,000] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice20w35wRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 250000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000=] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice20wRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 200000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000<] Repairing with severity type 3 and damage type 1 ")
    public void manufacturer10wuPrice20wLessRepairWithS3AndDT1(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 185000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Small"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000<] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice50wUpRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 893000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000=] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice50wRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 500000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000<;<500000] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice35w50wRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 375000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000=] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice35wRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 350000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200000<;<350,000] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice20w35wRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 250000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000=] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice20wRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 200000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000<] Repairing with severity type 3 and damage type 2 ")
    public void manufacturer10wuPrice20wLessRepairWithS3AndDT2(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 185000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Flat_Panel_Large"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000<] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice50wUpRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 893000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000=] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice50wRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 500000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000<;<500000] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice35w50wRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 375000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000=] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice35wRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 350000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200000<;<350,000] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice20w35wRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 250000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000=] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice20wRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 200000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000<] Repairing with severity type 3 and damage type 3 ")
    public void manufacturer10wuPrice20wLessRepairWithS3AndDT3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 185000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000<] Repairing with severity type 3 and damage type 4 ")
    public void manufacturer10wuPrice50wUpRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 893000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_Curved_Panel"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][500000=] Repairing with severity type 3 and damage type 4 ")
    public void manufacturer10wuPrice50wRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 500000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000<;<500000] Repairing with severity type 3 and damage type 4 ")
    public void manufacturer10wuPrice35w50wRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 375000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][350,000=] Repairing with severity type 3 and damage type 4 ")
    public void manufacturer10wuPrice35wRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 350000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200000<;<350,000] Repairing with severity type 3 and damage type 4 ")
    public void manufacturer10wuPrice20w35wRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 250000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000=] Repairing with severity type 3 and damage type 4")
    public void manufacturer10wuPrice20wRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 200000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "[Manufacturer][10WU][200,000<] Repairing with severity type 3 and damage type 4")
    public void manufacturer10wuPrice20wLessRepairWithS3AndDT4(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        double oemPrice = 185000;
        double workUnit = 10;
        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingWithSeverityType3(
                testData.getString("benzS400_vehicle"), paintRate, testData.getString("DamageType_On_Pressline"), oemPrice, workUnit);
    }

    @Test(description = "Remove Korean repair formula parts")
    public void removeKoreanRepairFormulaPart(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.removePart(testData.getString("benzS400_vehicle"));
    }

    @Test(description = "Repairing information of severity type 3 in calculation")
    public void repairingInfoForSeverityType3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("benzS400_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.repairingInfoForSeverityType3(testData.getString("benzS400_vehicle"));
    }

    @Test(description = "Multiple parts repairing information of severity type 3 in calculation")
    public void multiPartsRepairingInfoForSeverityType3(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                 testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        String manufacturerPaintRate = laborRatesPO.getPaintRate();
        double paintRate = Integer.parseInt(manufacturerPaintRate.replaceAll("\\pP",""));
        testCase.get().log(Status.INFO, "Select partnership value with Manufacturer setting");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Plastic damage repair
        PlasticDamageRepair plasticDamageRepair = new PlasticDamageRepair();
        plasticDamageRepair.multiPartsRepairingInfoForSeverityType3(testData.getString("bmw320_vehicle"));
    }
}
