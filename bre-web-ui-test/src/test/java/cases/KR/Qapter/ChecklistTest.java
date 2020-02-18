package cases.KR.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.DamageCapturing;
import steps.Login;
import steps.Qapter.Checklist;
import steps.Qapter.ModelOptions;
import steps.Qapter.ZoneAndLayout;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static java.util.Arrays.asList;
import java.util.List;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ChecklistTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;

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
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
    }

    @Test(description = "Layout test for model option in checklist")
    public void modelOptionTabInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Qapter checklist
        Checklist checklist = new Checklist();
        checklist.modelOptionTabInChecklist();

        testCase.get().log(Status.PASS, "Model options table is shown in checklist");
        Assert.assertTrue(damageCapturingPO.getModelOptionNumberInChecklist()>0);
        testCase.get().log(Status.PASS, "Model options number: " + damageCapturingPO.getModelOptionNumberInChecklist());
    }

    @Test(description = "Check added parts are consistent after Qapter re-launched")
    public void consistentAfterQapterResumed(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        Checklist checklist = new Checklist();
        int addedPartsNumber = checklist.consistentAfterQapterResumed(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        Assert.assertEquals(addedPartsNumber, 2);

        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        //Switch to Damage Capturing page again
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        damageCapturingPO.navigationChecklist();

        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), addedPartsNumber);
        testCase.get().log(Status.PASS, "There are still " + addedPartsNumber + " parts after Qapter resumed");
    }

    @Test(description = "Add standard and non-standard position from checklist")
    public void addPositionInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Checklist
        Checklist checklist = new Checklist();
        checklist.addPositionInChecklist(vehicleElementData.getString("bmw320_frontRoof_guideNo"));

        //Verification
        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 2);
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("replace_with_oem"));
        testCase.get().log(Status.PASS, "Standard position (" + vehicleElementData.getString("bmw320_2385_part_description") + ") is added in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(2), "Automation Test");
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(2), "1000");
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(2), vehicleElementData.getString("repair"));
        testCase.get().log(Status.PASS, "Non-Standard position is added in checklist");
    }

    @Test(description = "Edit added parts in checklist")
    public void editPositionInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Checklist
        Checklist checklist = new Checklist();
        checklist.editPositionInChecklist(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        //Verification
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_0471_part_description"));
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair method update successfully in checklist");
    }

    @Test(description = "Delete added part in checklist")
    public void deletePositionInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Checklist
        Checklist checklist = new Checklist();
        checklist.deletePositionInChecklist(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        //Verification
        int checklistNumber = damageCapturingPO.getChecklistNumber();
        Assert.assertEquals(checklistNumber, 0);
        testCase.get().log(Status.PASS, "After deleting, no more item in checklist");
        damageCapturingPO.navigationVehicle();
        Assert.assertFalse(damageCapturingPO.isPartSelected(vehicleElementData.getString("bmw320_zone_frontOuter")));
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.openZone(vehicleElementData.getString("bmw320_zone_frontOuter"));
        Assert.assertFalse(damageCapturingPO.isPartSelected(vehicleElementData.getString("bmw320_position_0471_Bonnet")));
        testCase.get().log(Status.PASS, "After deleting, the zone and position are in normal state and color");
    }

    @Test(description = "Added Model Options are shown")
    public void addedModelOptionDisplaysInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Model Option - Select model options
        ModelOptions modelOptions = new ModelOptions();
        damageCapturingPO.navigationModelOptions();
        modelOptions.addModelOption(vehicleElementData.getString("bmw320_e2_zone"), vehicleElementData.getString("bmw320_e2_code"));
        modelOptions.addModelOption(vehicleElementData.getString("bmw320_k5_zone"), vehicleElementData.getString("bmw320_k5_code"));
        modelOptions.addModelOption(vehicleElementData.getString("bmw320_n3_zone"), vehicleElementData.getString("bmw320_n3_code"));

        //Open model option tab in Checklist
        Checklist checklist = new Checklist();
        checklist.modelOptionTabInChecklist();

        //Verification - added model options are listed in the table
        Assert.assertTrue(checklist.isModelOptionListedInCheckList(vehicleElementData.getString("bmw320_e2_code"), vehicleElementData.getString("new_model_option")));
        testCase.get().log(Status.PASS, "Addded model option: " + vehicleElementData.getString("bmw320_e2_code") + " is displayed in checklist model option table");
        Assert.assertTrue(checklist.isModelOptionListedInCheckList(vehicleElementData.getString("bmw320_k5_code"), vehicleElementData.getString("new_model_option")));
        testCase.get().log(Status.PASS, "Addded model option: " + vehicleElementData.getString("bmw320_k5_code") + " is displayed in checklist model option table");
        Assert.assertTrue(checklist.isModelOptionListedInCheckList(vehicleElementData.getString("bmw320_n3_code"), vehicleElementData.getString("new_model_option")));
        testCase.get().log(Status.PASS, "Addded model option: " + vehicleElementData.getString("bmw320_n3_code") + " is displayed in checklist model option table");
    }

    @Test(description = "MO consistencies - Historic model options")
    public void modelOptionConsistencies(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case with VIN query
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationByVIN(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();

        //Verify model option consistencies
        Checklist checklist = new Checklist();
        checklist.verifyModelOptionConsistencies("bmw320_e2", "bmw320_p7", "bmw320_k5", "bmw320_n3");
    }

    @Test(description = "Check more info in checklist")
    public void checkMoreInfoInChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Check more info in checklist
        Checklist checklist = new Checklist();
        checklist.openMoreInfoInChecklist(
                vehicleElementData.getString("bmw320_zone_frontOuter"),
                vehicleElementData.getString("bmw320_position_0471_Bonnet"), 1);
    }

    @Test(description = "Filter checklist")
    public void filterChecklist(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Filter items name for each category
        List<String> positionsItemList = asList(vehicleElementData.getString("all"),
                vehicleElementData.getString("standart_position"),
                vehicleElementData.getString("nonStandard_positions"),
                vehicleElementData.getString("correction_entries"));
        List<String> repairMethodsItemList = asList(vehicleElementData.getString("all"),
                vehicleElementData.getString("replace_with_oem"),
                vehicleElementData.getString("hollow_cavity_sealing"));
        List<String> zonesItemList = asList(vehicleElementData.getString("all"),
                vehicleElementData.getString("front_outer"),
                vehicleElementData.getString("auda_hail"),
                vehicleElementData.getString("rear_outer"));
        List<String> commentsAndPhotosItemList = asList(vehicleElementData.getString("all"),
                vehicleElementData.getString("only_with_comments"),
                vehicleElementData.getString("only_with_photos"));

        //Open checklist filter
        Checklist checklist = new Checklist();
        checklist.openChecklistFilter(
                vehicleElementData.getString("bmw320_zone_frontOuter"),
                vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_zone_rearOuter"),
                vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        Assert.assertEquals(positionsItemList, damageCapturingPO.getPositionsFilterItems());
        testCase.get().log(Status.PASS, "Position filter has " + positionsItemList + " items available to choose");
        Assert.assertEquals(repairMethodsItemList, damageCapturingPO.getRepairMethodsFilterItems());
        testCase.get().log(Status.PASS, "Repair methods filter has " + repairMethodsItemList + " items available to choose");
        Assert.assertEquals(zonesItemList, damageCapturingPO.getZonesFilterItems());
        testCase.get().log(Status.PASS, "Zones filter has " + zonesItemList + " items available to choose");
        Assert.assertEquals(commentsAndPhotosItemList, damageCapturingPO.getCommentsAndPhotosFilterItems());
        testCase.get().log(Status.PASS, "Comments and photos filter has " + commentsAndPhotosItemList + " items available to choose");

        //Select repair methods filter
        checklist.filterReplaceWithOem();
    }
}
