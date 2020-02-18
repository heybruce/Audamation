package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ModelOptions extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;
    private RepairPanel repairPanel;

    public ModelOptions() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
        repairPanel = new RepairPanel();
    }

    public void modelOptionPopUp(String zone){
        String submodel = damageCapturingPO.selectSubModel(0);
        damageCapturingPO.clickModelOptionContinue();
        damageCapturingPO.waitForQapterLoading();
        damageCapturingPO.clickW5Door();
        damageCapturingPO.waitForModelOptionLoading();
        testCase.get().log(Status.INFO, "Select submodel ("+ submodel +") with 5Door in Qapter");
        wait.until(ExpectedConditions.attributeContains(damageCapturingPO.ID_QAPTER_GRAPHICS_NOT_AVAILABLE, "style", "display: none"));
        testCase.get().log(Status.PASS, "Navigation image is shown after choose model option");
        damageCapturingPO.clickCloseModelOptionView();
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.openZone(zone);
        testCase.get().log(Status.PASS, "Zone is clickable");
    }

    public void cancelModelOptionSelection(String engineZone, String warningMsg){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();

        damageCapturingPO.clickZone(engineZone);
        testCase.get().log(Status.INFO, "Open Engine zone");
        Assert.assertEquals(damageCapturingPO.getGraphicsNotAvailableMessage(), warningMsg);
        testCase.get().log(Status.PASS, "\"Select Model Options to see the graphics\" message is displayed");

        //Cancel model option selection
        wait.until(ExpectedConditions.visibilityOfElementLocated(damageCapturingPO.ID_MODEL_OPTION_VIEW));
        damageCapturingPO.clickCloseModelOptionView();
        testCase.get().log(Status.INFO, "Close Model option view");
    }

    public void addCustomModelOption(String description) {
        damageCapturingPO.clickAllSelectedModelOptions();
        int modelOptionNoBeforeAdded = damageCapturingPO.getSelectedModelOptionsCount();
        damageCapturingPO.clickAddCustomModelOptionButton();
        damageCapturingPO.inputCustomModelOptionDescription(description);
        damageCapturingPO.clickCustomModelOptionContinue();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(damageCapturingPO.CSS_MODEL_OPTION_GROUP), modelOptionNoBeforeAdded));
        testCase.get().log(Status.INFO, "Add custom model option");
    }

    public void verifyAddedCustomModelOptionDescription(int index, String expectedDesc ) {
        String addedCustomMODescription = damageCapturingPO.getCustomModelOptionDescription(index);
        Assert.assertTrue(addedCustomMODescription.startsWith(expectedDesc.toUpperCase()));
        testCase.get().log(Status.PASS, "Custom model option description is saved correctly");
        Assert.assertEquals(addedCustomMODescription.substring(addedCustomMODescription.length()-1), "*");
        testCase.get().log(Status.PASS, "Custom model option is marked with '*' ");
    }

    public void verifyEditedModelOptionDescription(String modelOptionCode, String expectedDesc) {
        String editedModelOptionDescription = damageCapturingPO.getModelOptionDescription(modelOptionCode);
        Assert.assertTrue(editedModelOptionDescription.startsWith(modelOptionCode + " - " + expectedDesc.toUpperCase()));
        testCase.get().log(Status.PASS, "Edited model option description is saved correctly");
        Assert.assertEquals(editedModelOptionDescription.substring(editedModelOptionDescription.length()-1), "*");
        testCase.get().log(Status.PASS, "Edited model option is marked with '*' ");
    }

    public void editCustomModelOption(int index, String description) {
        damageCapturingPO.clickEditCustomModelOption(index);
        damageCapturingPO.inputEditedModelOptionDescription(description);
        damageCapturingPO.clickEditedModelOptionContinueButton();
        testCase.get().log(Status.INFO, "Edit custom model option");
    }

    public void deleteCustomModelOption(int index) {
        damageCapturingPO.clickDeleteCustomModelOption(index);
        testCase.get().log(Status.INFO, "Delete custom model option");
    }

    public void addModelOption(String zone, String modelOptionCode) {
        damageCapturingPO.clickModelOptionZone(zone);
        damageCapturingPO.selectModelOption(modelOptionCode);
        testCase.get().log(Status.INFO, "Add a model option");
    }

    public void deleteModelOptionFromAllSelectedMO(String modelOptionCode) {
        damageCapturingPO.clickAllSelectedModelOptions();
        testCase.get().log(Status.INFO, "Select All Selected Options");
        damageCapturingPO.clickDeleteModelOption(modelOptionCode);
        testCase.get().log(Status.INFO, "Delete a model option");
    }

    public void deleteModelOptionWithDamageCaptured(String zone, String position, String positionName, String modelOptionCode, String guideNo) {
        repairPanel.selectPosition(zone, position, positionName);
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Replace a part");
        damageCapturingPO.navigationModelOptions();
        testCase.get().log(Status.INFO, "Go to Model Option");
        deleteModelOptionFromAllSelectedMO(modelOptionCode);
        Assert.assertTrue(damageCapturingPO.isConflictDialogDisplayed());
        testCase.get().log(Status.PASS, "Conflict dialog is displayed");
        Assert.assertTrue(damageCapturingPO.isPartListedOnConflictDialog(guideNo));
        testCase.get().log(Status.PASS, "Guide No. " + guideNo + " is listed on Conflict Dialog");
        damageCapturingPO.confirmConflictDialog();
        testCase.get().log(Status.INFO, "Click continue button on conflict dialog");
        Assert.assertFalse(damageCapturingPO.isPartListedOnModelOption(guideNo));
        testCase.get().log(Status.PASS, "Guide No. " + guideNo + " is not listed on Model Option");
        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.INFO, "Go to Calculation Preview");
        Assert.assertFalse(damageCapturingPO.getCalcPreviewContent().contains(guideNo));
        testCase.get().log(Status.PASS, "Guide No. " + guideNo + " is not listed in Calculation Preview");
    }

    public void editModelOption(String modelOptionCode, String description, Boolean continued) {
        damageCapturingPO.clickAllSelectedModelOptions();
        testCase.get().log(Status.INFO, "Open All Selected Options");
        String originalDescription = damageCapturingPO.getModelOptionDescription(modelOptionCode);
        damageCapturingPO.clickEditModelOption(modelOptionCode);
        testCase.get().log(Status.INFO, "Click edit icon of model code " + modelOptionCode);
        damageCapturingPO.inputEditedModelOptionDescription(description);
        testCase.get().log(Status.INFO, "Input new description");
        if(continued) {
            damageCapturingPO.clickEditedModelOptionContinueButton();
            testCase.get().log(Status.INFO, "Continue editing model option");
            verifyEditedModelOptionDescription(modelOptionCode, description);
        } else {
            damageCapturingPO.clickEditedModelOptionCancelButton();
            testCase.get().log(Status.INFO, "Cancel editing model option");
            Assert.assertEquals(damageCapturingPO.getModelOptionDescription(modelOptionCode), originalDescription);
            testCase.get().log(Status.PASS, "Model option description is not updated");
        }
    }
}
