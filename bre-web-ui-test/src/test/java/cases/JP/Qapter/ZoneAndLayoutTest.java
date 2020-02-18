package cases.JP.Qapter;

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
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.Qapter.ZoneAndLayout;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ZoneAndLayoutTest extends TestBase {
    private ProcessStepJPPO processStepJPPO;
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
        processStepJPPO = (ProcessStepJPPO) context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.fastCapturing(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"), false);

        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("benzE_0471_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("benzE_0471_part_description") + ") is added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("benzE_bonnet_guideNo"));
        testCase.get().log(Status.PASS, "Guide no. (" + vehicleElementData.getString("benzE_bonnet_guideNo") + ") display correctly in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), vehicleElementData.getString("benzE_0471_oem_part_number"));
        testCase.get().log(Status.PASS, "Part no. (" + vehicleElementData.getString("benzE_0471_oem_part_number") + ") display correctly in checklist");
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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        String[] one_multi_part = new String[] {vehicleElementData.getString("benzE_0577_part_description")};
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.multipartSelection(vehicleElementData.getString("benzE_zone_frontOuter"),
                vehicleElementData.getString("benzE_position_mp_0577_lIndicatorBulb"), one_multi_part);

        //Verify checklist
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist tab");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("benzE_0577_part_description"));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("benzE_0577_part_description") + ") added successfully");
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("replace_with_oem")));
        testCase.get().log(Status.PASS, "Repair Method is " + vehicleElementData.getString("replace_with_oem") + " in checklist");

        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        Assert.assertTrue(reportsPO.getCalculationOutput().contains(vehicleElementData.getString("benzE_0577_part_description")));
        testCase.get().log(Status.PASS, "Part (" + vehicleElementData.getString("benzE_0577_part_description") + ") shows in calculation output");
    }

    @Test(description = "Pictogram content is folded in More views of Qapter")
    public void pictogramViews() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Pictogram
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.pictogramViews(vehicleElementData.getString("benzE_newPictogram_bodyPainting"));
        testCase.get().log(Status.PASS, "Pictogram is clickable when pictogram content is unfolded");
        Assert.assertEquals(damageCapturingPO.getPictogramNumber(), 14);
        testCase.get().log(Status.PASS, "There are 14 pictogram content");
        Assert.assertTrue(damageCapturingPO.isPictogramWithImage());
        testCase.get().log(Status.PASS, "All pictogram are with image");

        damageCapturingPO.clickMoreViewToClosePictogram();
        new WebDriverWait(getDriver(), 3).until(ExpectedConditions.not(
                ExpectedConditions.elementToBeClickable(By.id(vehicleElementData.getString("benzE_newPictogram_bodyPainting")))));
        testCase.get().log(Status.PASS, "Pictogram is not clickable after pictogram content is folded");
    }

    @Test(description = "Pictogram content is folded in More views of Qapter")
    public void pictogramViewsInZone() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Pictogram
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.pictogramViewsInZone(vehicleElementData.getString("benzE_zone_frontOuter"));
        testCase.get().log(Status.PASS, "There are pictogram of more parts when pictogram content is unfolded");
        Assert.assertTrue(damageCapturingPO.getMorePartsPictogramNumber()>0);
        testCase.get().log(Status.PASS, "There are " + damageCapturingPO.getMorePartsPictogramNumber() + " pictogram content are clickable in More parts");

        damageCapturingPO.clickMoreViewToClosePictogram();
        new WebDriverWait(getDriver(), 3).until(ExpectedConditions.not(
                ExpectedConditions.elementToBeClickable(damageCapturingPO.getMorePartsPictogram().get(0))));
        testCase.get().log(Status.PASS, "Pictogram is not clickable after pictogram content is folded");
    }

    @Test(description = "Open pictogram sheet by selecting one of pictogram content")
    public void openPictogramSheet() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Pictogram
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.openPictogramSheet(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));
        testCase.get().log(Status.PASS, "The pictogram sheet is displayed and the position is clickable");
    }

    @Test(description = "Open a zone by clicking pictogram")
    public void openZoneByPictogram() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Pictogram
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.openZoneByPictogram(
                vehicleElementData.getString("benzE_newPictogram_airbags"), vehicleElementData.getString("benzE_position_4607And4608_sideAirbag"));
        testCase.get().log(Status.PASS, "The zone is loaded and clickable");
    }

    @Test(description = "Add a part by clicking pictogram content")
    public void addPartByPictogram() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Pictogram
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.addPartByPictogram(vehicleElementData.getString("benzE_newPictogram_airbags"), vehicleElementData.getString("benzE_position_4607And4608_sideAirbag"));

        //Verification
        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 1);
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), vehicleElementData.getString("benzE_4608_part_description"));
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), vehicleElementData.getString("benzE_sideAirbag_guideNo"));
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("replace_with_oem"));
        testCase.get().log(Status.PASS, "Standard position (" + vehicleElementData.getString("benzE_4608_part_description") + ") is added in checklist");

    }
}
