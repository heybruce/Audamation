package cases.KR.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.DamageCapturing;
import steps.Login;
import steps.Qapter.ZoneAndLayout;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ZoneAndLayoutTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
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
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
    }

    @Test(description = "Zone - Fast Capturing")
    public void fastCapturing(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.fastCapturing(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"), false);

        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_0471_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("bmw320_0471_part_description") + ") is added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("bmw320_bonnet_guideNo"));
        testCase.get().log(Status.PASS, "Guide no. (" + vehicleElementData.getString("bmw320_bonnet_guideNo") + ") display correctly in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), vehicleElementData.getString("bmw320_0471_oem_part_number"));
        testCase.get().log(Status.PASS, "Part no. (" + vehicleElementData.getString("bmw320_0471_oem_part_number") + ") display correctly in checklist");
    }

    @Test(description = "Add part of multipart selection")
    public void multipartSelection(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        String[] one_multi_part = new String[] {vehicleElementData.getString("bmw320_0543_part_description")};
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.multipartSelection(vehicleElementData.getString("bmw320_zone_frontOuter"),
                vehicleElementData.getString("bmw320_position_mp_0543_lDrivingLampBulb"), one_multi_part);

        //Verify checklist
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist tab");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_0543_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("bmw320_0543_part_description") + ") added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");

        //Verify calculation preview
        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.INFO, "Go to Calculation Preview tab");
        Assert.assertTrue(damageCapturingPO.getCalcPreviewContent().contains(vehicleElementData.getString("bmw320_0543_part_description")));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("bmw320_0543_part_description") + ") shows in calculation output");
    }

    @Test(description = "ZONE - Navigation between zones with arrows")
    public void navigateZonesWithArrows(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.navigateBetweenZones(vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_position_1566_outerHandle"), vehicleElementData.getString("bmw320_position_2353_Roof"));
    }

    @Test(description = "ZONE - Navigation tree interaction: marking the part in Repair panel")
    public void navigationTreeInteraction() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.navigationTreeInteraction(vehicleElementData.getString("bmw320_frontBumper_guideNo"), vehicleElementData.getString("bmw320_position_0283_frontBumper"),
                vehicleElementData.getString("bmw320_bonnet_guideNo"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));
    }

    @Test(description = "Add the second part from multipart panel")
    public void multipartSelectSecondPart(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Add two parts from multiple parts panel
        String zone = vehicleElementData.getString("bmw320_zone_frontOuter");
        String multiplepart_position = vehicleElementData.getString("bmw320_position_mp_0543_lDrivingLampBulb");
        String multiplepart_part_1 = vehicleElementData.getString("bmw320_0543_part_description");
        String multiplepart_part_2 = vehicleElementData.getString("bmw320_0544_part_description");
        String[] two_multi_parts = new String[] {multiplepart_part_1, multiplepart_part_2};
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.multipartSelection(zone, multiplepart_position, two_multi_parts);

        //Verify repair icon on repair panel
        Assert.assertTrue(zoneAndLayout.isPartOnMultipartPanelWithRepairIcon(zone, multiplepart_position,
                vehicleElementData.getString("bmw320_0543_part_guideNo")));
        testCase.get().log(Status.PASS, "Part " + multiplepart_part_1 + " has repairing icon next to it on multiple parts panel");
        Assert.assertTrue(zoneAndLayout.isPartOnMultipartPanelWithRepairIcon(zone, multiplepart_position,
                vehicleElementData.getString("bmw320_0544_part_guideNo")));
        testCase.get().log(Status.PASS, "Part " + multiplepart_part_2 + " has repairing icon next to it on multiple parts panel");

        //Verify checklist
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist tab");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), multiplepart_part_1);
        testCase.get().log(Status.PASS, "Part (" + multiplepart_part_1 + ") added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(2), multiplepart_part_2);
        testCase.get().log(Status.PASS, "Part (" + multiplepart_part_2 + ") added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(2).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");

        //Verify calculation preview
        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.INFO, "Go to Calculation Preview tab");
        Assert.assertTrue(damageCapturingPO.getCalcPreviewContent().contains(multiplepart_part_1));
        testCase.get().log(Status.PASS, "Part (" + multiplepart_part_1 + ") shows in calculation output");
        Assert.assertTrue(damageCapturingPO.getCalcPreviewContent().contains(multiplepart_part_2));
        testCase.get().log(Status.PASS, "Part (" + multiplepart_part_2 + ") shows in calculation output");
    }

    @Test(description = "Select multiple repair method for multiple parts")
    public void multipartSelectMultiRepairMethod(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("audiA6_vehicle"));

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

        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.selectMultiRepairMethod(vehicleElementData.getString("audiA6_zone_centreOuter"), vehicleElementData.getString("audiA6_position_mp_2117_sillPanel"),
                vehicleElementData.getString("audiA6_2117_part_description"), vehicleElementData.getString("audiA6_2117_part_guideNo"));
    }

    @Test(description = "Select a part with no side and multipart")
    public void fastCapturingNoSideAndMultipart(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        // Select a multipart in fast capturing
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.fastCapturing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),
                vehicleElementData.getString("bmw320_position_mp_9594_rearWiring"), true,
                vehicleElementData.getString("bmw320_9594_part_description"));

        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 1);
        testCase.get().log(Status.PASS, "Only 1 part is selected");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_9594_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("bmw320_9594_part_description") + ") is added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("bmw320_9594_part_guideNo"));
        testCase.get().log(Status.PASS, "Guide no. (" + vehicleElementData.getString("bmw320_9594_part_guideNo") + ") display correctly in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), vehicleElementData.getString("bmw320_9594_oem_part_number"));
        testCase.get().log(Status.PASS, "Part no. (" + vehicleElementData.getString("bmw320_9594_oem_part_number") + ") display correctly in checklist");
    }


    @Test(description = "Unselect a damaged part with no side and multipart")
    public void unselectFastCapturingNoSideAndMultipart(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        // Select a multipart in fast capturing
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.fastCapturing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),
                vehicleElementData.getString("bmw320_position_mp_9594_rearWiring"), true,
                vehicleElementData.getString("bmw320_9594_part_description"));

        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 1);
        testCase.get().log(Status.PASS, "Only 1 part is selected");

        // Unselect selected multiple part and select another one in fast capturing
        damageCapturingPO.navigationVehicle();
        testCase.get().log(Status.INFO, "Go to Vehicle tab");

        zoneAndLayout.fastCapturing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),
                vehicleElementData.getString("bmw320_position_mp_9594_rearWiring"), true,
                vehicleElementData.getString("bmw320_9594_part_description"),
                vehicleElementData.getString("bmw320_9615_part_description"));

        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 1);
        testCase.get().log(Status.PASS, "Only 1 part is selected");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("bmw320_9615_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("bmw320_9615_part_description") + ") is added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("bmw320_9615_part_guideNo"));
        testCase.get().log(Status.PASS, "Guide no. (" + vehicleElementData.getString("bmw320_9615_part_guideNo") + ") display correctly in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), vehicleElementData.getString("bmw320_9615_oem_part_number"));
        testCase.get().log(Status.PASS, "Part no. (" + vehicleElementData.getString("bmw320_9615_oem_part_number") + ") display correctly in checklist");
    }
}