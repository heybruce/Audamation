package cases.KR.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseKR;
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.openRepairPanel(vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_0471_part_description"),vehicleElementData.getString("bmw320_position_1401_windscreen"));

        Assert.assertTrue(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("bmw320_1401_part_description"));
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Repair panel
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.closeRepiarPanel(vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_REPAIR_PANEL_MAIN_SECTION));
        testCase.get().log(Status.PASS, "Repair panel is closed by clicking out of repair panel");
    }

    @Test(description = "Verify that the \"Part composition\" data is correct")
    public void verifyPartCompositionData(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Verify part composition without extra part
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.verifyPartCompositionData(vehicleElementData.getString("bmw320_zone_frontOuter"),
                vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_0471_part_description"), vehicleElementData.getString("KR_currency_symbol"),false);

        //Verify part composition with extra part
        repairPanel.verifyPartCompositionData(vehicleElementData.getString("bmw320_zone_frontOuter"),
                vehicleElementData.getString("bmw320_position_1401_windscreen"),
                vehicleElementData.getString("bmw320_1401_part_description"), vehicleElementData.getString("KR_currency_symbol"),true);
    }
}
