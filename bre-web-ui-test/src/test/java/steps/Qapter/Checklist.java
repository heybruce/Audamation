package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;

import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class Checklist extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private RepairPanel repairPanel;

    public Checklist() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        repairPanel = new RepairPanel();
    }

    public void modelOptionTabInChecklist(){
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistModelOptions();
    }

    public int consistentAfterQapterResumed(String zone1, String position1, String zone2, String position2){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToRepair(zone1, position1);
        damageCapturing.addStandardPartToHollowCavitySealing(zone2, position2);
        damageCapturingPO.navigationChecklist();
        int checklistPartsNumber = damageCapturingPO.getChecklistNumber();
        testCase.get().log(Status.INFO, "There are " + checklistPartsNumber + " parts in checklist");
        return checklistPartsNumber;
    }

    public void addPositionInChecklist(String SPGuideNumber){
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistNewPosition();
        damageCapturingPO.inputSPGuideNumber(SPGuideNumber);
        new WebDriverWait(getDriver(), 3)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(DamageCapturingPO.ID_REPAIR_METHOD_REPAIR)));
        DamageCapturing damageCapturing = new DamageCapturing();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPReplaceWithOEMPart();
        damageCapturingPO.waitForQapterLoading();
        damageCapturingPO.clickSPAddAnotherPosition();
        testCase.get().log(Status.INFO, "Add a standard position (" + SPGuideNumber + ") from checklist add position");
        Assert.assertTrue(isElementPresent(damageCapturingPO.ID_ADD_POSITION_MAIN_SECTION));
        testCase.get().log(Status.PASS, "Add position section is still opened after clicking keep adding button");
        damageCapturingPO.clickNonStandardTab();
        damageCapturingPO.clickNSPRepair();
        damageCapturingPO.inputNSPDescription("Automation Test");
        damageCapturingPO.inputNSPSparePart("NSP test");
        if(isMobileDevice()) {
            damageCapturingPO.clickNSPWorkUnits();
            damageCapturingPO.clickKeypadNumber("2");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadConfirm();
        }else{
            damageCapturingPO.inputNSPWorkUnits("20");
        }
        damageCapturingPO.clickNSPAddPosition();
        testCase.get().log(Status.INFO, "Add a non-standard part with Replace with OEM");
    }

    public void editPositionInChecklist(String zone, String position){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToRepair(zone, position);
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistItem(1);
        damageCapturingPO.clickRPRepair();
        testCase.get().log(Status.INFO, "Uncheck repair method: Repair");
        new WebDriverWait(getDriver(), 5).until(
                ExpectedConditions.textToBe(By.cssSelector(damageCapturingPO.CSS_REPAIR_COST), "0"));
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Re-select repair method: replace with OEM");
        damageCapturingPO.waitForQapterLoading();
    }

    public void deletePositionInChecklist(String zone, String position){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToRepair(zone, position);
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistCheckbox(0);
        damageCapturingPO.clickDeletePositionInChecklist();
    }

    public boolean isModelOptionListedInCheckList(String modelOptionCode, String type) {
        Map<String, String> modelOption = damageCapturingPO.getModelOptionsInChecklist(modelOptionCode);
        if (!modelOption.isEmpty() && modelOption.get("moType").equals(type))
            return true;
        else
            return false;
    }

    public boolean isCustomModelOptionListedInCheckList(String description, String type) {
        Map<String, String> modelOption = damageCapturingPO.getCustomModelOptionsInChecklist(description);
        if (!modelOption.isEmpty() && modelOption.get("moType").equals(type))
            return true;
        else
            return false;
    }

    public void verifyModelOptionConsistencies(String removeMO1, String removeMO2, String addMO1, String addMO2) {
        //Open model option tab in Checklist
        modelOptionTabInChecklist();
        int moCountCheckList = damageCapturingPO.getModelOptionNumberInChecklist();
        Assert.assertTrue(moCountCheckList > 0);
        testCase.get().log(Status.PASS, "Model options are shown in checklist");

        //Verification - model options that will be removed later are listed in the table and the type is original model option (provided by VIN)
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(removeMO1 + "_code"), vehicleElementData.getString("original_model_option")));
        testCase.get().log(Status.PASS, "Deleted model option: " + vehicleElementData.getString(removeMO1 + "_code") + " is listed in checklist model option table");
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(removeMO2 + "_code"), vehicleElementData.getString("original_model_option")));
        testCase.get().log(Status.PASS, "Deleted model option: " + vehicleElementData.getString(removeMO2 + "_code") + " is listed in checklist model option table");

        //Open all selected model Option and verify total number is the same as checklist MO number
        ModelOptions modelOptions = new ModelOptions();
        damageCapturingPO.navigationModelOptions();
        int moCountAllSelected = damageCapturingPO.getAllSelectedModelOptionNumber();
        Assert.assertEquals(moCountCheckList, moCountAllSelected);
        testCase.get().log(Status.PASS, "Model options are shown in all selected model option section and the count is the same as in Checklist");

        //Delete 2 model options that provided by VIN
        modelOptions.deleteModelOptionFromAllSelectedMO(vehicleElementData.getString(removeMO1 + "_code"));
        modelOptions.deleteModelOptionFromAllSelectedMO(vehicleElementData.getString(removeMO2 + "_code"));
        //Add 2 model options
        modelOptions.addModelOption(vehicleElementData.getString(addMO1 + "_zone"), vehicleElementData.getString(addMO1 + "_code"));
        modelOptions.addModelOption(vehicleElementData.getString(addMO2 + "_zone"), vehicleElementData.getString(addMO2 + "_code"));
        //Add a customer model option
        modelOptions.addCustomModelOption("Custom MO");

        //Open model option tab in Checklist
        modelOptionTabInChecklist();

        //Verification - deleted model options are listed in the table and the type is dismissed model option
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(removeMO1 + "_code"), vehicleElementData.getString("dismissed_model_option")));
        testCase.get().log(Status.PASS, "Deleted model option: " + vehicleElementData.getString(removeMO1 + "_code") + " is marked as dismissed in checklist model option table");
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(removeMO2 + "_code"), vehicleElementData.getString("dismissed_model_option")));
        testCase.get().log(Status.PASS, "Deleted model option: " + vehicleElementData.getString(removeMO2 + "_code") + " is marked as dismissed in checklist model option table");

        //Verification - added model options are listed in the table and the type is new model option
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(addMO1 + "_code"), vehicleElementData.getString("new_model_option")));
        testCase.get().log(Status.PASS, "Addded model option: " + vehicleElementData.getString(addMO1 + "_code") + " is displayed in checklist model option table");
        Assert.assertTrue(isModelOptionListedInCheckList(vehicleElementData.getString(addMO2 + "_code"), vehicleElementData.getString("new_model_option")));
        testCase.get().log(Status.PASS, "Addded model option: " + vehicleElementData.getString(addMO2 + "_code") + " is displayed in checklist model option table");

        //Verification - added custom model options are listed in the table and the type is custom model option
        Assert.assertTrue(isCustomModelOptionListedInCheckList("Custom MO", vehicleElementData.getString("custom_model_option")));
        testCase.get().log(Status.PASS, "Addded custom model option is displayed in checklist model option table");
    }

    public void openMoreInfoInChecklist(String zone, String position, int row){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(zone, position);
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist");
        damageCapturingPO.openMoreInfoOfChecklistItem(row);
        testCase.get().log(Status.INFO, "Open more info of row " + row + " item in checklist");
        Assert.assertTrue(damageCapturingPO.isMoreInfoOfChecklistItemOpened(row));
        testCase.get().log(Status.PASS, "More info of checklist row " + row + " is opened");
    }

    public void openChecklistFilter(String zone1, String position1, String zone2, String position2) {
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(zone1, position1);
        damageCapturing.addStandardPartToHollowCavitySealing(zone2, position2);
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist");
        damageCapturingPO.openFilterOnChecklist();
        testCase.get().log(Status.PASS, "Checklist filter is opened");
    }

    public void filterReplaceWithOem() {
        String repairMethod = vehicleElementData.getString("replace_with_oem");
        damageCapturingPO.clickRPReplaceWithOEMFilter();
        testCase.get().log(Status.INFO, "Check replace with oem filter item");
        damageCapturingPO.applyFilter();
        testCase.get().log(Status.INFO, "Apply filter selection");
        Assert.assertTrue(damageCapturingPO.isRepairMethodFiltered(repairMethod));
        testCase.get().log(Status.PASS, "Only " + repairMethod + " repair(s) display in checklist");
    }
}
