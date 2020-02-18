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
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        //Running total
        RunningTotal runningTotal = new RunningTotal();
        runningTotal.displayRunningTotal(testData.getString("QT_VehicleValue"),
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"),
                vehicleElementData.getString("benzE_zone_rearDoors"),vehicleElementData.getString("benzE_position_1781And1782Door"));

        Assert.assertTrue(damageCapturingPO.isRepairCostAlert());
        testCase.get().log(Status.PASS, "The total cost is higher than vehicle value, so the alert is shown");
        Assert.assertTrue(damageCapturingPO.getWarningMessage().contains(testData.getString("QT_WarningMessage")));
        testCase.get().log(Status.PASS, "Warning message shows correctly: " + testData.getString("QT_WarningMessage"));
    }
}
