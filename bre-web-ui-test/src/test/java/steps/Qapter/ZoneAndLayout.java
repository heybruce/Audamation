package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.apache.commons.configuration2.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import steps.DamageCapturing;
import utils.webdrivers.CustomDeviceDriver;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ZoneAndLayout extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private WebDriverWait wait;

    public ZoneAndLayout() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 3);
    }

    public void openZone(String zone){
        if(getDeviceName().equalsIgnoreCase("iPad"))
            clickZoneOnIOS(zone);
        else
            damageCapturingPO.clickZone(zone);
        testCase.get().log(Status.INFO, "Select a zone");
    }

    public void clickPosition(String position, boolean waitForRP){
        if(getDeviceName().equalsIgnoreCase("iPad"))
            clickPartOnIOS(position, waitForRP);
        else {
            damageCapturingPO.clickPosition(position);
            if (waitForRP)
                damageCapturingPO.waitForRepairPanelDisplay();
        }
        testCase.get().log(Status.INFO, "Select a position");
    }

    public void fastCapturing(String zone, String damagePositionElementId, Boolean multiPartSelected, String... multiPartPosition) {
        if (!isElementPresent(damageCapturingPO.ID_VEHICLE_MAIN_VIEW)) {
            damageCapturingPO.navigationVehicle();
            testCase.get().log(Status.INFO, "Back to Vehicle tab main view");
        }
        openZone(zone);
        damageCapturingPO.clickFastCapturing();
        damageCapturingPO.clickSelectRepairMethod();
        damageCapturingPO.clickFcReplaceWithOem();
        damageCapturingPO.clickFastCaptureNoSide();
        clickPosition(damagePositionElementId, false);
        if (multiPartSelected) {
            damageCapturingPO.clickFastCapturingMultipartsPosition(multiPartPosition);
            testCase.get().log(Status.INFO, "Select a multiple part");
        }
        damageCapturingPO.clickDoneInFastCapturing();
        testCase.get().log(Status.INFO, "Click done in fast capturing");
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Navigate to checklist");
    }

    /**
     * To select one or more parts from multiple parts repair panel
     * @param damagePosition All parts are passed by String Array
     */
    public void multipartSelection(String zone, String damagePositionElementId, String[] damagePosition){
        openZone(zone);
        for (String position : damagePosition) {
            clickPosition(damagePositionElementId, true);
            damageCapturingPO.clickMultipartsPosition(position);
            testCase.get().log(Status.INFO, "Select one of multiple parts");
            DamageCapturing damageCapturing = new DamageCapturing();
            if(isMobileDevice())
                damageCapturing.useCameraPermission();
            damageCapturingPO.clickRPReplaceWithOEMPart();
            testCase.get().log(Status.INFO, "Replace with OEM part");
            damageCapturingPO.clickCloseRepairPanel();
            testCase.get().log(Status.INFO, "Close repair panel");
        }
    }

    public boolean isPartOnMultipartPanelWithRepairIcon(String zone, String damagePositionElementId, String partGuideNo){
        damageCapturingPO.navigationVehicle();
        testCase.get().log(Status.INFO, "Back to vehicle main page");
        openZone(zone);
        clickPosition(damagePositionElementId, true);
        return damageCapturingPO.isPartWithRepairIcon(partGuideNo);
    }

    public void pictogramViews(String pictogramId){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        testCase.get().log(Status.INFO, "Click more view to unfold pictogram content");
        wait.until(ExpectedConditions.elementToBeClickable(By.id(pictogramId)));
    }

    public void pictogramViewsInZone(String zone){
        openZone(zone);
        damageCapturingPO.clickMoreViewToOpenPictogram();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(damageCapturingPO.CSS_MORE_PARTS_ACTIONS_PICTOGRAM, 0));
    }

    public void openPictogramSheet(String function, String Position){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        openZone(function);
        testCase.get().log(Status.INFO, "Click more view and pictogram function");
        wait.until(ExpectedConditions.visibilityOfElementLocated(damageCapturingPO.ID_PICTOGRAM_SHEET));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(Position)));
    }

    public void openZoneByPictogram(String function, String Position){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        openZone(function);
        testCase.get().log(Status.INFO, "Click more view and pictogram function");
        wait.until(ExpectedConditions.elementToBeClickable(By.id(Position)));
    }

    public void addPartByPictogram(String function, String Position){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        openZone(function);
        clickPosition(Position, true);
        if(damageCapturingPO.isSelectSidePopUp())
            damageCapturingPO.clickRightSide();
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Add a part from new pictogram with new part painting");
        damageCapturingPO.navigationChecklist();
    }

    private void clickZoneOnIOS(String zone) {
        String country = getCountry();
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        Configuration deviceCoordinatesData = driver.getDeviceCoordinates();
        Point zoneElementLocation = new Point(Integer.valueOf(deviceCoordinatesData.getString(country + "_" + zone + "_x")), Integer.valueOf(deviceCoordinatesData.getString(country + "_" + zone + "_y")));
        // click zone by touch action
        damageCapturingPO.waitForElementPresent(By.id(zone));
        driver.switchContextToApp();
        driver.actionTap(zoneElementLocation);
        driver.switchContextToBrowser();
        damageCapturingPO.waitForQapterLoading();
    }

    private void clickPartOnIOS(String position, boolean waitForRP) {
        String country = getCountry();
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        Configuration deviceCoordinatesData = driver.getDeviceCoordinates();
        String position_x = deviceCoordinatesData.getString(country + "_" + position + "_x");
        String position_y = deviceCoordinatesData.getString(country + "_" + position + "_y");
        Point positionElementLocation = new Point(Integer.valueOf(position_x), Integer.valueOf(position_y));
        // click position by touch action
        damageCapturingPO.waitForElementPresent(By.id(position));
        driver.switchContextToApp();
        driver.actionTap(positionElementLocation);
        driver.switchContextToBrowser();
        // Don't wait for repair panel displays. Just click the part.
        if (!waitForRP)
            return;
        // Wait for repair panel displays, to see if the part clicked correctly
        try {
            damageCapturingPO.waitForRepairPanelDisplay();
        } catch (TimeoutException e) {
        // if there is other tab opened in Safari, need to click new location of the part
            Point positionNewLocation = new Point(Integer.valueOf(position_x), Integer.valueOf(position_y) + 25);
            driver.switchContextToApp();
            driver.actionTap(positionNewLocation);
            driver.switchContextToBrowser();
        }
    }

    public void navigateBetweenZones(String firstZone, String firstZonePosition, String secondZonePosition, String thirdZonePosition) {
        openZone(firstZone);
        Assert.assertTrue(isElementPresent(damageCapturingPO.ARROW_NAVIGATION));
        testCase.get().log(Status.PASS, "Navigation is present");
        Assert.assertTrue(isElementEnabled(damageCapturingPO.ARROW_LEFT));
        testCase.get().log(Status.PASS, "Left arrow is disabled in the first zone");
        navigateByRightArrow(By.id(secondZonePosition));
        Assert.assertFalse(isElementEnabled(damageCapturingPO.ARROW_LEFT));
        testCase.get().log(Status.PASS, "Left arrow is enabled in the second zone");
        damageCapturingPO.clickPosition(secondZonePosition);
        if (damageCapturingPO.isSelectSidePopUp())
            damageCapturingPO.clickRightSide();
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Add a standard part with replace with OEM");
        navigateByRightArrow(By.id(thirdZonePosition));
        testCase.get().log(Status.PASS, "Expected position is present after added part repair and navigated to the right");
        navigateByLeftArrow(By.id(secondZonePosition));
        Assert.assertTrue(damageCapturingPO.isPartSelected(secondZonePosition));
        testCase.get().log(Status.PASS, "Repair position is present and selected");
        damageCapturingPO.navigationChecklist();
        damageCapturingPO.navigationVehicle();
        Assert.assertTrue(isElementPresent(damageCapturingPO.ARROW_NAVIGATION));
        testCase.get().log(Status.PASS, "Navigation is present after back to vehicle from check list");
        navigateByLeftArrow(By.id(firstZonePosition));
        Assert.assertTrue(isElementPresent(By.id(firstZonePosition)));
        Assert.assertTrue(isElementEnabled(damageCapturingPO.ARROW_LEFT));
        testCase.get().log(Status.PASS, "Expected position is present and left arrow is disabled in the first zone");
    }

    public void navigateByRightArrow(By expectedDisplayElement){
        damageCapturingPO.clickZoneNavigationRightArrow();
        wait.until(ExpectedConditions.visibilityOfElementLocated(expectedDisplayElement));
        testCase.get().log(Status.PASS, "Navigate by right arrow");
    }

    public void navigateByLeftArrow(By expectedDisplayElement){
        damageCapturingPO.clickZoneNavigationLeftArrow();
        wait.until(ExpectedConditions.visibilityOfElementLocated(expectedDisplayElement));
        testCase.get().log(Status.PASS, "Navigate by left arrow");
    }

    public void navigateThroughMenus(By expectedDisplayZone) {
        damageCapturingPO.navigationVehicle();
        wait.until(ExpectedConditions.visibilityOfElementLocated(expectedDisplayZone));
        testCase.get().log(Status.PASS, "Navigate to vehicle successfully");

        damageCapturingPO.navigationPhoto();
        testCase.get().log(Status.PASS, "Navigate to photo successfully");

        damageCapturingPO.navigationSearch();
        testCase.get().log(Status.PASS, "Navigate to search successfully");

        damageCapturingPO.navigationModelOptions();
        testCase.get().log(Status.PASS, "Navigate to model options successfully");

        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.PASS, "Navigate to checklist successfully");

        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.PASS, "Navigate to calculation preview successfully");

        damageCapturingPO.navigationSettings();
        testCase.get().log(Status.PASS, "Navigate to settings successfully");
    }

    public void navigationTreeInteraction(String guideNumber1, String position1, String guideNumber2, String position2){
        damageCapturingPO.clickNavigationBreadcrumbs();
        damageCapturingPO.clickNavigationZoneIconFrontOuter(true);
        testCase.get().log(Status.PASS, "Parts are listed after expand navigation tree");
        damageCapturingPO.clickNavigationParts(guideNumber1);
        Assert.assertTrue(damageCapturingPO.isPartMarked(position1));
        testCase.get().log(Status.PASS, "Part is marked after clicked the part text");
        damageCapturingPO.clickNavigationPartsIcon(guideNumber1);
        Assert.assertEquals(damageCapturingPO.getRPGuideNumber(), guideNumber1);
        testCase.get().log(Status.PASS, "After clicked the icon, repair panel of the part is opened");
        damageCapturingPO.clickNavigationPartsIcon(guideNumber2);
        Assert.assertTrue(damageCapturingPO.isPartMarked(position2));
        testCase.get().log(Status.PASS, "Part is marked after clicked another part text");
        damageCapturingPO.clickNavigationZoneIconFrontOuter(false);
        testCase.get().log(Status.PASS, "Part list for certain zone is closed");
    }

    public void selectMultiRepairMethod(String zone, String multiPosition, String positionDescription, String guideNo){
        openZone(zone);
        clickPosition(multiPosition, true);
        damageCapturingPO.clickMultipartsPosition(positionDescription);
        testCase.get().log(Status.INFO, "Select one of multiple parts");
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturingPO.clickRPPaint();
        damageCapturingPO.clickRPSurfPainting();
        testCase.get().log(Status.INFO, "Click surf painting");
        damageCapturingPO.clickRPHollowCavitySealing();
        testCase.get().log(Status.INFO, "Click hollow cavity sealing");
        new WebDriverWait(getDriver(), 2).until(
                ExpectedConditions.textToBe(damageCapturingPO.ID_CENTER_SIDE_COUNT,"2"));
        new WebDriverWait(getDriver(), 2).until(
                ExpectedConditions.textToBePresentInElement(damageCapturingPO.getMultiplePartsOperationCount(guideNo),"2"));
        testCase.get().log(Status.PASS, "There are 2 operations in the dropdown indicates");

        //Verify checklist
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Go to Checklist tab");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), positionDescription);
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(1).contains(vehicleElementData.getString("surface_painting")));
        testCase.get().log(Status.PASS, "Part (" + positionDescription + ") is added with " + vehicleElementData.getString("surface_painting") + " in checklist");
        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(2), positionDescription);
        Assert.assertTrue(damageCapturingPO.getChecklistRepairMethod(2).contains(vehicleElementData.getString("hollow_cavity_sealing")));
        testCase.get().log(Status.PASS, "Part (" + positionDescription + ") is added with " + vehicleElementData.getString("hollow_cavity_sealing") + " in checklist");

        //Verify calculation preview
        damageCapturingPO.navigationCalcPreview();
        testCase.get().log(Status.INFO, "Go to Calculation Preview tab");
        Assert.assertTrue(damageCapturingPO.getCalcPreviewContent().contains(positionDescription));
        testCase.get().log(Status.PASS, "Part (" + positionDescription + ") shows in calculation output");
    }
}