package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;

import java.util.List;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class RepairPanel extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;
    private ZoneAndLayout zoneAndLayout;

    public RepairPanel() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
        zoneAndLayout = new ZoneAndLayout();
    }

    public void selectPosition(String zone, String position, String positionName) {
        zoneAndLayout.openZone(zone);
        zoneAndLayout.clickPosition(position, true);
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), positionName);
        testCase.get().log(Status.PASS, "Repair panel of " + positionName + " is opened");
    }

    public void openRepairPanel(String zone1, String position1, String positionName1, String position2){
        selectPosition(zone1, position1, positionName1);
        DamageCapturing damageCapturing = new DamageCapturing();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        testCase.get().log(Status.PASS, "Repair panel is opened with the right position");
        zoneAndLayout.clickPosition(position2, true);
        damageCapturingPO.waitForQapterLoading();
    }

    public void closeRepiarPanel(String zone, String position){
        zoneAndLayout.openZone(zone);
        zoneAndLayout.clickPosition(position, true);
        DamageCapturing damageCapturing = new DamageCapturing();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickOutOfRepairPanel();
    }

    public void uploadPhotoFromRepairPanel(String zone, String position, String positionName, String filePath, boolean repairNeeded) {
        damageCapturingPO.navigationVehicle();
        selectPosition(zone, position, positionName);
        if (repairNeeded) {
            //Replace part
            damageCapturingPO.clickRPReplaceWithOEMPart();
            testCase.get().log(Status.INFO, "Replace a part");
        }
        damageCapturingPO.uploadPhotosOnRepairPanel(filePath);
        testCase.get().log(Status.PASS, "Photo is uploaded from repair panel");
    }

    public void verifyPartCompositionData(String zone, String position, String positionName, String currency, Boolean withExtraPart) {
        damageCapturingPO.navigationVehicle();
        selectPosition(zone, position, positionName);
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Replace a part");
        damageCapturingPO.clickReplaceMutations();
        testCase.get().log(Status.PASS, "Open replace mutation option successfully");
        damageCapturingPO.clickPartsComposition();
        testCase.get().log(Status.PASS, "Switch to parts composition view successfully");
        Assert.assertEquals(damageCapturingPO.getMainPartName(), positionName);
        testCase.get().log(Status.PASS, "Main part " + positionName + " is displayed");
        if (withExtraPart) {
            Assert.assertTrue(damageCapturingPO.isExtraPartDisplayed());
            testCase.get().log(Status.PASS, "Extra Part is displayed");
            Assert.assertTrue(damageCapturingPO.isAllExtraPartSelected());
            testCase.get().log(Status.PASS, "All extra parts are selected");
        }
        Assert.assertTrue(isWorkCompositionWithWuAndPrice(currency));
        testCase.get().log(Status.PASS, "All parts under work composition have WU and price value");
    }

    private boolean isWorkCompositionWithWuAndPrice(String currency) {
        Map<String, List> workCompositionData = damageCapturingPO.getWorkCompositionData();
        for (Map.Entry<String, List> entry : workCompositionData.entrySet()) {
            if (!(entry.getValue().get(0).toString().matches("\\d+") && entry.getValue().get(1).toString().matches(currency + "\\d+.\\d+"))) {
                return false;
            }
        }
        return true;
    }
}
