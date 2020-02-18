package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CalculationPreview extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;

    public CalculationPreview() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
    }

    public void totalLossIndicatorAndCalculationPreview(String zone, String position){
        String repairCost_original, repairCost_new;
        repairCost_original= "0";
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToRepair(zone, position);
        repairCost_new = damageCapturingPO.getIncreaseRepairCost(repairCost_original);
        Assert.assertTrue(Double.parseDouble(repairCost_new.replaceAll("\\pP","")) > Double.parseDouble(repairCost_original.replaceAll("\\pP","")));
        testCase.get().log(Status.PASS, "The standard part is added and repair cost is increased");
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistCheckbox(0);
        damageCapturingPO.clickDeletePositionInChecklist();
        damageCapturingPO.navigationVehicle();
        repairCost_new = damageCapturingPO.getIncreaseRepairCost(repairCost_new);
        Assert.assertEquals(repairCost_new, repairCost_original);
        testCase.get().log(Status.PASS, "After deselected the part, total loss indicator is showing 0");
        damageCapturing.addStandardPartToRepair(zone, position);
        damageCapturing.addNonStandardPartReplaceWithOem();
        damageCapturingPO.navigationCalcPreview();
    }

    public boolean displayPrintButtonInCalcPreview(String vehicle){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString(vehicle+ "_zone_frontOuter"),vehicleElementData.getString(vehicle+ "_position_0471_Bonnet"));
        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.INFO, "Navigate to calculation preview");
        return isElementPresent(damageCapturingPO.ID_CALC_PREVIEW_PRINT);
    }

}
