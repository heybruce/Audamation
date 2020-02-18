package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class RunningTotal extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;

    public RunningTotal() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
    }

    public void displayRunningTotal(String vehicleValue, String zone1, String position1, String zone2, String position2){
        damageCapturingPO.navigationSettings();
        damageCapturingPO.inputVehicleValue(vehicleValue);
        damageCapturingPO.navigationVehicle();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(zone1, position1);
        Assert.assertFalse(damageCapturingPO.isRepairCostAlert());
        testCase.get().log(Status.PASS, "The total cost is not higher than vehicle value, so the alert is not shown");
        damageCapturing.addStandardPartToReplaceWithOem(zone2, position2);
    }
}
