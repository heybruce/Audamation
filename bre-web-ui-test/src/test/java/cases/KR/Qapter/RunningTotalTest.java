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
import steps.Qapter.RunningTotal;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class RunningTotalTest extends TestBase {
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

    @Test(description = "Display warning message if the repair cost is higher than vehicle value")
    public void displayRunningTotal(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Running total
        RunningTotal runningTotal = new RunningTotal();
        runningTotal.displayRunningTotal(testData.getString("QT_VehicleValue"),
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"),
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));

        Assert.assertTrue(damageCapturingPO.isRepairCostAlert());
        testCase.get().log(Status.PASS, "The total cost is higher than vehicle value, so the alert is shown");
        Assert.assertTrue(damageCapturingPO.getWarningMessage().contains(testData.getString("QT_WarningMessage")));
        testCase.get().log(Status.PASS, "Warning message shows correctly: " + testData.getString("QT_WarningMessage"));
    }
}
