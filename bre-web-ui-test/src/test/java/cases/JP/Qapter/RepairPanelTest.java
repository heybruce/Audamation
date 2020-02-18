package cases.JP.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseJP;
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
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.openRepairPanel(vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"),
                vehicleElementData.getString("benzE_0471_part_description"),vehicleElementData.getString("benzE_position_1401_windscreen"));

        Assert.assertTrue(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("benzE_1401_part_description"));
        testCase.get().log(Status.PASS, "After switching to another position, repair panel is still opened with the right position");
    }

    @Test(description = "The repair pane will be closed if clicking out of repair panel")
    public void closeRepairPanel(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.closeRepiarPanel(vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));

        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        testCase.get().log(Status.PASS, "Repair panel is closed by clicking out of repair panel");
    }
}
