package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import steps.CreateNewCaseID;
import steps.DamageCapturing;
import steps.Login;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class DamageCapturingIDTest extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private ProcessStepIDPO processStepIDPO;

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
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    @Test(description = "Damage capturing in Qapter")
    public void damageCapturingInQapter(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //Switch to Labour Rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToRepair(
                vehicleElementData.getString("benzS_zone_frontOuter"), vehicleElementData.getString("benzS_position_0471_Bonnet"));

        //Verify repairCost_added > repairCost_original
        String repairCost_original, repairCost_new;
        repairCost_original= "0";
        repairCost_new = damageCapturingPO.getIncreaseRepairCost(repairCost_original);
        Assert.assertTrue(Double.parseDouble(repairCost_new.replaceAll("\\pP","")) > Double.parseDouble(repairCost_original.replaceAll("\\pP","")));
        testCase.get().log(Status.PASS, "The standard part is added and repair cost is increased");

        //Damage Capturing - add non-standard part
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Verify repairCost_added > repairCost_original
        repairCost_original = repairCost_new;
        repairCost_new = damageCapturingPO.getIncreaseRepairCost(repairCost_original);
        Assert.assertTrue(Double.parseDouble(repairCost_new.replaceAll("\\pP","")) > Double.parseDouble(repairCost_original.replaceAll("\\pP","")));
        testCase.get().log(Status.PASS, "The non-standard part is added and repair cost is increased");

        //Damage Capturing - add part from pictogram
        damageCapturing.goToVehicleView();
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        //Verify repairCost_added > repairCost_original
        repairCost_original = repairCost_new;
        repairCost_new = damageCapturingPO.getIncreaseRepairCost(repairCost_original);
        Assert.assertTrue(Double.parseDouble(repairCost_new.replaceAll("\\pP","")) > Double.parseDouble(repairCost_original.replaceAll("\\pP","")));
        testCase.get().log(Status.PASS, "The part is added from pictogram and repair cost is increased");

        //Check the checklist have three items
        Assert.assertTrue(damageCapturing.isChecklistNumberMatched(3));
        testCase.get().log(Status.PASS, "There are three items in checklist");
    }
}
