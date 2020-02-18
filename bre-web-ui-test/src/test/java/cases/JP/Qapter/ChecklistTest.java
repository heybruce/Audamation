package cases.JP.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.Qapter.Checklist;
import steps.Qapter.ZoneAndLayout;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ChecklistTest extends TestBase {
    private WebDriverWait wait;
    private ProcessStepJPPO processstepJPPO;
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        wait = new WebDriverWait(getDriver(), 10);
        processstepJPPO = (ProcessStepJPPO)context.getBean("ProcessStepJPPO");
        processstepJPPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        Checklist checklist = new Checklist();
        int addedPartsNumber = checklist.consistentAfterQapterResumed(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"),
                vehicleElementData.getString("benzE_zone_rearDoors"),vehicleElementData.getString("benzE_position_1781And1782Door"));
        Assert.assertEquals(addedPartsNumber, 2);

        getDriver().switchTo().defaultContent();
        processstepJPPO.clickCalculationsTab();
        //Switch to Damage Capturing page
        processstepJPPO.clickDamageCaptureTab();
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Checklist
        Checklist checklist = new Checklist();
        checklist.addPositionInChecklist(vehicleElementData.getString("benzE_bonnet_guideNo"));

        //Verification
        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 2);
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("benzE_0471_part_description"));
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("benzE_bonnet_guideNo"));
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Standard position (" + vehicleElementData.getString("benzE_0471_part_description") + ") is added in checklist");
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Checklist
        Checklist checklist = new Checklist();
        checklist.editPositionInChecklist(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));

        //Verification
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("benzE_0471_part_description"));
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Checklist
        Checklist checklist = new Checklist();
        checklist.deletePositionInChecklist(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));

        //Verification
        int checklistNumber = damageCapturingPO.getChecklistNumber();
        Assert.assertEquals(checklistNumber, 0);
        testCase.get().log(Status.PASS, "After deleting, no more item in checklist");
        damageCapturingPO.navigationVehicle();
        Assert.assertFalse(damageCapturingPO.isPartSelected(vehicleElementData.getString("benzE_zone_frontOuter")));
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.openZone(vehicleElementData.getString("benzE_zone_frontOuter"));
        Assert.assertFalse(damageCapturingPO.isPartSelected(vehicleElementData.getString("benzE_position_0471_Bonnet")));
        testCase.get().log(Status.PASS, "After deleting, the zone and position are in normal state and color");
    }
}
