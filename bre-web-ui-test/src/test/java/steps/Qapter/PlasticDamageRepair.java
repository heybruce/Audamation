package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CalculationList;
import steps.DamageCapturing;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class PlasticDamageRepair extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;
    private ProcessStepKRPO processStepKRPO;
    private ReportsPO reportsPO;

    public PlasticDamageRepair() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 5);
        processStepKRPO = (ProcessStepKRPO)context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
    }

    public void repairingWithSeverityType1(String vehicle, double workUnit, double paintRate){
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(0);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Clearcoat severity type (type 1) is selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPSurfacePaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Surface Painting are marked in repair panel");
        damageCapturingPO.clickSurfacePaintingMutations();

        DecimalFormat df1 = new DecimalFormat("0.0");
        String mutationsWUExpected = df1.format(0.56*workUnit);
        String mutationsWUActual = damageCapturingPO.getMutationsWU();

        Assert.assertEquals(df1.format(Double.parseDouble(mutationsWUActual)), mutationsWUExpected);
        testCase.get().log(Status.PASS, "Fixed labor: " + mutationsWUExpected);
        Assert.assertEquals(damageCapturingPO.getMutationsGM(), "0");
        testCase.get().log(Status.PASS, "GM: 0");

        damageCapturingPO.navigationChecklist();
        String checklistLabor = damageCapturingPO.getChecklistLabour(1);
        Assert.assertEquals(df1.format(Double.parseDouble(checklistLabor)), mutationsWUExpected);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("surface_painting"));
        testCase.get().log(Status.PASS, "Part (Front Bumper) is added with surface painting and the WUs is " + mutationsWUExpected);

        damageCapturingPO.navigationCalcPreview();
        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        String paintLaborPrice = String.valueOf(Math.round(paintRate/workUnit*Double.parseDouble(mutationsWUExpected)));
        Assert.assertTrue(editedOutput.contains(paintLaborPrice), "Paint labor rate ("+ paintLaborPrice +") is NOT in calculation.");
        testCase.get().log(Status.PASS, "Paint labor rate ("+ paintLaborPrice +") is in calculation");
        Assert.assertFalse(editedOutput.contains(vehicleElementData.getString("paint_material_cost")), "Paint material price contains in calculation.");
        testCase.get().log(Status.PASS, "There is no paint material price in calculation");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Switch back to Qapter
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page again");

        damageCapturingPO.navigationChecklist();
        checklistLabor = damageCapturingPO.getChecklistLabour(1);
        Assert.assertEquals(df1.format(Double.parseDouble(checklistLabor)), mutationsWUExpected);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("surface_painting"));
        testCase.get().log(Status.PASS, "Part (Front Bumper) is added with surface painting and the WUs is " + mutationsWUExpected);
        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(paintLaborPrice), "Paint labor rate ("+ paintLaborPrice +") is NOT in calculation.");
        testCase.get().log(Status.PASS, "Paint labor rate ("+ paintLaborPrice +") is in calculation");
        Assert.assertFalse(editedOutput.contains(vehicleElementData.getString("paint_material_cost")), "Paint material price contains in calculation.");
        testCase.get().log(Status.PASS, "There is no paint material price in calculation");
    }

    public void repairingWithSeverityType2(String vehicle){
        DamageCapturing damageCapturing = new DamageCapturing();
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(1);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Clearcoat and paint structure severity type (type 2) is selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        damageCapturingPO.navigationChecklist();
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("repair_painting"));
        testCase.get().log(Status.PASS, "Part (Front Bumper) is added with repair painting");

        damageCapturingPO.navigationCalcPreview();
        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_labor_cost")), "Paint labor price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint labor price in calculation");
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_material_cost")), "Paint material price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint material price in calculation");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Switch back to Qapter
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page again");

        damageCapturingPO.navigationChecklist();
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("repair_painting"));
        testCase.get().log(Status.PASS, "Part (Front Bumper) is added with repair painting");
        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_labor_cost")), "Paint labor price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint labor price in calculation");
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_material_cost")), "Paint material price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint material price in calculation");
    }

    public void repairingWithSeverityType3(String vehicle, double paintRate, String damageType, double oemPrice, double workUnit){
        int type = Integer.valueOf(damageType.substring(0, 1));
        double repairTime = Double.valueOf(testData.getString("RepairTime_"+damageType));
        double repairMaterialCost = Double.valueOf(testData.getString("RepairMaterialCost_"+damageType));
        double variableFactor = getVariableFactor(oemPrice);
        DecimalFormat df = new DecimalFormat("0.0");

        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        //search part
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_rearBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        //Click repair and select severity type 3
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(2);
        Assert.assertEquals(damageCapturingPO.getDamageTypeItemNumber(), 4);
        testCase.get().log(Status.PASS, "There are 4 damage types displayed");

        //Select damage type
        damageCapturingPO.selectKRDamageItem(type);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(type));
        damageCapturingPO.inputOemPartPrice(oemPrice);
        testCase.get().log(Status.INFO, "Input OEM price: " + oemPrice);
        testCase.get().log(Status.INFO, "Variable factor should be: " + variableFactor);
        //Verify working units display in repair panel
        double totalWorkingUnitsCalculate = ((repairTime * paintRate) + repairMaterialCost + (oemPrice * variableFactor)) / paintRate * workUnit;
        String totalWorkingUnitsActual = damageCapturingPO.getTotalWorkingUnits();
        //Refactor according to WEBCAP-6361 & AXNASEAN-3196
        //KR Use_Decimal_WUS = false
        int totalWorkingUnitsExpected = (int) Math.ceil(Double.valueOf(df.format(totalWorkingUnitsCalculate)));
        Assert.assertEquals(totalWorkingUnitsActual, String.valueOf(totalWorkingUnitsExpected));
        testCase.get().log(Status.PASS, "Total working units is as expected: " + totalWorkingUnitsExpected);

        damageCapturingPO.clickContinueInRepairParameters();
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        damageCapturingPO.navigationChecklist();
        String checklistLabor = damageCapturingPO.getChecklistLabour(2);
        Assert.assertEquals(checklistLabor, String.valueOf(totalWorkingUnitsExpected));
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("repair_painting"));
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(2), vehicleElementData.getString("repair"));
        testCase.get().log(Status.PASS, "Checklist: Part (Rear Bumper) is added with repair and repair painting and the WUs is " + totalWorkingUnitsExpected);

        damageCapturingPO.navigationCalcPreview();
        int totalCost = (int) Math.ceil(paintRate/workUnit * totalWorkingUnitsExpected);
        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_labor_cost")), "Paint labor price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint labor price in calculation");
        Assert.assertTrue(editedOutput.contains(String.valueOf(totalCost)), "Total cost ("+ totalCost +") not found in calculation. not found in calculation.");
        testCase.get().log(Status.PASS, "Total cost ("+ totalCost +") display in calculation");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Switch back to Qapter
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page again");

        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("paint_labor_cost")), "Paint labor price not found in calculation.");
        testCase.get().log(Status.PASS, "There is paint labor price in calculation");
        Assert.assertTrue(editedOutput.contains(String.valueOf(totalCost)), "Working unit ("+ totalCost +") not found in calculation.");
        testCase.get().log(Status.PASS, "Total cost ("+ totalCost +") display in calculation");
    }

    public void removePart(String vehicle){
        //Add Part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        //Choose severity type 3 and damage type 2
        damageCapturingPO.selectKRSeverityType(2);
        damageCapturingPO.selectKRDamageItem(1);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 2 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Go to check list and verify added parts
        damageCapturingPO.navigationChecklist();
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("repair_painting"));
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(2), vehicleElementData.getString("repair"));
        testCase.get().log(Status.INFO, "Checklist: Part (front Bumper) is added with repair and repair painting.");

        //Remove the parts
        damageCapturingPO.deleteChecklistItem(0);
        testCase.get().log(Status.INFO, "Try to delete part with repair painting.");
        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(damageCapturingPO.KR_REPAIR_ERROR_DIALOG));
        Assert.assertEquals(damageCapturingPO.getRepairErrorMessage(), testData.getString("Error_RemoveFromKoreanRepairPanel"));
        testCase.get().log(Status.PASS, "Error pop up with message: " + testData.getString("Error_RemoveFromKoreanRepairPanel"));
        damageCapturingPO.clickBtnCloseInRepairErrorDialog();

        damageCapturingPO.deleteChecklistItem(1);
        testCase.get().log(Status.INFO, "Try to delete part with repair.");
        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(damageCapturingPO.KR_REPAIR_ERROR_DIALOG));
        Assert.assertEquals(damageCapturingPO.getRepairErrorMessage(), testData.getString("Error_RemoveFromKoreanRepairPanel"));
        testCase.get().log(Status.PASS, "Error pop up with message: " + testData.getString("Error_RemoveFromKoreanRepairPanel"));
        damageCapturingPO.clickBtnCloseInRepairErrorDialog();

        //Remove the parts from Korean repair panel
        damageCapturingPO.clickChecklistItem(2);
        damageCapturingPO.clickRPRepair();
        wait.until(ExpectedConditions.textToBe(By.cssSelector(damageCapturingPO.CSS_REPAIR_COST), "0"));
        Assert.assertFalse(damageCapturingPO.isRPRepairSelected());
        Assert.assertFalse(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are removed in repair panel.");
        Assert.assertEquals(damageCapturingPO.getChecklistNumber(), 0);
        testCase.get().log(Status.PASS, "Parts with Korean repair formula are removed.");
    }

    public void repairingInfoForSeverityType3(String vehicle) {
        //Add Part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        //Choose severity type 3 and damage type 1
        damageCapturingPO.selectKRSeverityType(2);
        damageCapturingPO.selectKRDamageItem(0);
        String oemPartPrice = damageCapturingPO.getOemPartPrice();
        NumberFormat formater = NumberFormat.getInstance();
        String partPriceFormatted = formater.format(Integer.valueOf(oemPartPrice.substring(0, oemPartPrice.indexOf("."))));
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 1 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Verify the information for severity type 3 and damage type 1 in calculation preview
        damageCapturingPO.navigationCalcPreview();
        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType1")), "Repairing info for Severity Type 3 and Damage Type 1 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceFormatted + " " + testData.getString("currency")), "Part price Info for Severity Type 3 not found in calculation.");

        //Go to check list and change damage type 2
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistItem(2);
        damageCapturingPO.clickRepairMutationsOfKoreanRepair();
        damageCapturingPO.selectKRDamageItem(1);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 2 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Verify the information for severity type 3 and damage type 2 in calculation preview
        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType2")), "Repairing info for Severity Type 3 and Damage Type 2 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceFormatted + " " + testData.getString("currency")), "Part price Info for Severity Type 3 not found in calculation.");

        //Go to check list and change damage type 3
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistItem(2);
        damageCapturingPO.clickRepairMutationsOfKoreanRepair();
        damageCapturingPO.selectKRDamageItem(2);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 3 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Verify the information for severity type 3 and damage type 3 in calculation preview
        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType3")), "Repairing info for Severity Type 3 and Damage Type 3 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceFormatted + " " + testData.getString("currency")), "Part price Info for Severity Type 3 not found in calculation.");

        //Go to check list and change damage type 4
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.clickChecklistItem(2);
        damageCapturingPO.clickRepairMutationsOfKoreanRepair();
        damageCapturingPO.selectKRDamageItem(3);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 4 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Verify the information for severity type 3 and damage type 2 in calculation preview
        damageCapturingPO.navigationCalcPreview();
        editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType4")), "Repairing info for Severity Type 3 and Damage Type 4 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceFormatted + " " + testData.getString("currency")), "Part price Info for Severity Type 3 not found in calculation.");
    }

    public void multiPartsRepairingInfoForSeverityType3(String vehicle) {
        //Add Part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        Search search = new Search();

        //Choose severity type 3 and damage type 2
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(2);
        damageCapturingPO.selectKRDamageItem(1);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(1));
        String oemPartPriceType2 = damageCapturingPO.getOemPartPrice();
        NumberFormat formatter = NumberFormat.getInstance();
        String partPriceType2Formatted = formatter.format(Integer.valueOf(oemPartPriceType2.substring(0, oemPartPriceType2.indexOf("."))));
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 1 are selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Choose severity type 3 and damage type 4
        damageCapturingPO.navigationVehicle();
        search.searchPart(vehicleElementData.getString(vehicle+"_rearBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        damageCapturingPO.clickRPRepair();

        damageCapturingPO.selectKRSeverityType(2);
        damageCapturingPO.selectKRDamageItem(3);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(3));
        String oemPartPriceType4 = damageCapturingPO.getOemPartPrice();
        String partPriceType4Formatted = formatter.format(Integer.valueOf(oemPartPriceType4.substring(0, oemPartPriceType4.indexOf("."))));
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 4 are selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Verify the information for severity type 3 and damage type 2 in calculation preview
        damageCapturingPO.navigationCalcPreview();
        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType2")), "Repairing info for Severity Type 3 and Damage Type 2 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceType2Formatted + " " + testData.getString("currency")), "Part price Info (" +partPriceType2Formatted+ ") for Severity Type 3 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_SeverityType3") + " " + testData.getString("Info_DamageType4")), "Repairing info for Severity Type 3 and Damage Type 4 not found in calculation.");
        Assert.assertTrue(editedOutput.contains(testData.getString("Info_PartPrice") + " " + partPriceType4Formatted + " " + testData.getString("currency")), "Part price Info (" +partPriceType4Formatted+ ")for Severity Type 3 not found in calculation.");
        testCase.get().log(Status.PASS, "Two parts repair information are displayed correctly in calculation preview");
    }

    public void addThreePartsWithKoreanRepairFormula(String vehicle){
        //Add the first Part with severity type 1
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        Search search = new Search();
        search.searchPart(vehicleElementData.getString(vehicle+"_frontBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        if(isMobileDevice())
            damageCapturing.useCameraPermission();
        damageCapturingPO.clickRPRepair();
        //Choose severity type 1
        damageCapturingPO.selectKRSeverityType(0);
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 1 are selected.");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPSurfacePaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Surface Painting are marked in repair panel");

        //Add the second part with severity type 2
        damageCapturingPO.navigationVehicle();
        search.searchPart(vehicleElementData.getString(vehicle+"_leftSideSkirt_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        damageCapturingPO.clickRPRepair();
        damageCapturingPO.selectKRSeverityType(1);
        testCase.get().log(Status.INFO, "Severity type 2 are selected.");
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 2 are selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Add the third part with severity type 3 damage type 1
        damageCapturingPO.navigationVehicle();
        search.searchPart(vehicleElementData.getString(vehicle+"_rearBumper_guideNo"));
        damageCapturingPO.clickSearchResult();
        damageCapturingPO.waitForRepairPanelDisplay();
        damageCapturingPO.clickRPRepair();

        damageCapturingPO.selectKRSeverityType(2);
        damageCapturingPO.selectKRDamageItem(0);
        testCase.get().log(Status.INFO, "Select damage type: " + damageCapturingPO.getDamageTypeName(0));
        damageCapturingPO.clickContinueInRepairParameters();
        testCase.get().log(Status.INFO, "Severity type 3 and damage type 1 are selected");
        damageCapturingPO.waitForRepairPanelDisplay();
        Assert.assertTrue(damageCapturingPO.isRPRepairSelected());
        Assert.assertTrue(damageCapturingPO.isRPReapirPaintingSelected());
        testCase.get().log(Status.PASS, "Repair and Repair Painting are marked in repair panel");

        //Go to check list and verify added parts
        damageCapturingPO.navigationChecklist();
        String guideNo = damageCapturingPO.getChecklistGuideNumber(1);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("surface_painting"));
        testCase.get().log(Status.PASS, "Checklist: Part (" +guideNo+ ") is added with surface painting");
        guideNo = damageCapturingPO.getChecklistGuideNumber(2);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(2), vehicleElementData.getString("repair_painting"));
        testCase.get().log(Status.PASS, "Checklist: Part (" +guideNo+ ") is added with repair painting");
        guideNo = damageCapturingPO.getChecklistGuideNumber(3);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(3), vehicleElementData.getString("repair_painting"));
        testCase.get().log(Status.PASS, "Checklist: Part (" +guideNo+ ") is added with repair painting");
        guideNo = damageCapturingPO.getChecklistGuideNumber(4);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(4), vehicleElementData.getString("repair"));
        testCase.get().log(Status.PASS, "Checklist: Part (" +guideNo+ ") is added with repair");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        Assert.assertFalse(reportsPO.getCalculationTitle(0).isEmpty());
        Assert.assertFalse(reportsPO.getCalculationDateTime(0).isEmpty());
        Assert.assertFalse(reportsPO.getUserId(0).isEmpty());
        Assert.assertFalse(reportsPO.getGrandTotalWTaxCombined(0).isEmpty());
        Assert.assertFalse(reportsPO.getRepairTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPartsTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getLabourTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPaintTotal(0).isEmpty());
        testCase.get().log(Status.PASS, "New calculation is added into calculation list");
    }

    private double getVariableFactor(double oemPrice){
        double vf;
        if(oemPrice<=200000)
            vf = Double.valueOf(testData.getString("VF_Less_20W"));
        else if(oemPrice>200000 && oemPrice<=350000)
            vf = Double.valueOf(testData.getString("VF_20W_35W"));
        else if(oemPrice>350000 && oemPrice<=500000)
            vf = Double.valueOf(testData.getString("VF_35W_50W"));
        else
            vf = Double.valueOf(testData.getString("VF_50W_Up"));
        return vf;
    }

}
