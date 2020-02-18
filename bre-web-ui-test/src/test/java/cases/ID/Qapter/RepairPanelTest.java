package cases.ID.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.Qapter.RepairPanel;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class RepairPanelTest extends TestBase {
    private DamageCapturingPO damageCapturingPO;

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
    }

    @Test(description = "Open repair panel by clicking a position")
    public void openRepairPanel(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.openRepairPanel(vehicleElementData.getString("benzS_zone_frontOuter"), vehicleElementData.getString("benzS_position_0471_Bonnet"),
                vehicleElementData.getString("benzS_0471_part_description"),vehicleElementData.getString("benzS_position_1401_windscreen"));

        Assert.assertTrue(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("benzS_1401_part_description"));
        testCase.get().log(Status.PASS, "After switching to another position, repair panel is still opened with the right position");
    }

    @Test(description = "The repair pane will be closed if clicking out of repair panel")
    public void closeRepairPanel(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.closeRepiarPanel(vehicleElementData.getString("benzS_zone_frontOuter"), vehicleElementData.getString("benzS_position_0471_Bonnet"));

        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        testCase.get().log(Status.PASS, "Repair panel is closed by clicking out of repair panel");
    }
}
