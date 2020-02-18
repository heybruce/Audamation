package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.apache.commons.configuration2.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;
import utils.webdrivers.CustomDeviceDriver;
import utils.xpath.XPathNodeBuilder;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class DamageCapturing extends TestBase {
    private DamageCapturingPO damageCapturingPO;

    public DamageCapturing() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    public void launchQapter(){
        damageCapturingPO.clickQapterIcon();
    }

    public void addStandardPartToRepair(String bodyWorkElementId, String damagePositionElementId) {
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(bodyWorkElementId, damagePositionElementId);
        }else {
            damageCapturingPO.clickZone(bodyWorkElementId);
            damageCapturingPO.clickPosition(damagePositionElementId);
        }
        if(damageCapturingPO.isSelectSidePopUp())
            damageCapturingPO.clickRightSide();
        if(isMobileDevice()) {
            useCameraPermission();
            damageCapturingPO.clickRPRepair();
            damageCapturingPO.clickMutationsTextField();
            damageCapturingPO.clickKeypadNumber("3");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadConfirm();
        }else {
            damageCapturingPO.clickRPRepair();
            damageCapturingPO.inputMutations("30");
        }
        damageCapturingPO.clickContinue();
        testCase.get().log(Status.INFO, "Add a standard part with repair");
        goToVehicleView();
    }

    public void addStandardPartToReplaceWithOem(String bodyWorkElementId, String damagePositionElementId) {
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(bodyWorkElementId, damagePositionElementId);
        }else {
            damageCapturingPO.clickZone(bodyWorkElementId);
            damageCapturingPO.clickPosition(damagePositionElementId);
        }
        if(damageCapturingPO.isSelectSidePopUp())
            damageCapturingPO.clickRightSide();
        if(isMobileDevice())
            useCameraPermission();
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Add a standard part with replace with OEM");
        goToVehicleView();
    }

    public void addStandardPartToHollowCavitySealing(String bodyWorkElementId, String damagePositionElementId) {
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(bodyWorkElementId, damagePositionElementId);
        }else {
            damageCapturingPO.clickZone(bodyWorkElementId);
            damageCapturingPO.clickPosition(damagePositionElementId);
        }
        if(damageCapturingPO.isSelectSidePopUp())
            damageCapturingPO.clickRightSide();
        if(isMobileDevice())
            useCameraPermission();
        damageCapturingPO.clickRPHollowCavitySealing();
        testCase.get().log(Status.INFO, "Add a standard part with hollow cavity sealing");
        goToVehicleView();
    }

    public void addNonStandardPartReplaceWithOem() {
        damageCapturingPO.clickAddNewPosition();
        damageCapturingPO.clickNonStandardTab();
        damageCapturingPO.clickNSPReplacewithOEM();
        damageCapturingPO.inputNSPDescription("Automation Test");
        damageCapturingPO.inputNSPSparePart("NSP test");
        if(isMobileDevice()) {
            damageCapturingPO.clickNSPWorkUnits();
            damageCapturingPO.clickKeypadNumber("2");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadConfirm();
            damageCapturingPO.clickNSPAmount();
            damageCapturingPO.clickKeypadNumber("1");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadNumber("0");
            damageCapturingPO.clickKeypadConfirm();
        }else{
            damageCapturingPO.inputNSPWorkUnits("20");
            damageCapturingPO.inputNSPAmount("1000");
        }
        damageCapturingPO.clickNSPAddPosition();
        testCase.get().log(Status.INFO, "Add a non-standard part with Replace with OEM");
        goToVehicleView();
    }

    public void addOldPictogramToReplaceWithOem(String function, String position) {
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(function, position);
        }else {
            damageCapturingPO.clickZone(function);
            damageCapturingPO.clickPosition(position);
        }
        if(isMobileDevice())
            useCameraPermission();
        damageCapturingPO.clickRPReplaceWithOEMPart();
        testCase.get().log(Status.INFO, "Add a part from old pictogram with painting");
        goToVehicleView();
    }

    public void addPartsFromNewPictogramToSurfacePainting(String function, String position){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(function, position);
        }else {
            damageCapturingPO.clickZone(function);
            damageCapturingPO.clickPosition(position);
        }
        damageCapturingPO.clickRPSurfPainting();
        testCase.get().log(Status.INFO, "Add a part from new pictogram with surf painting");
        goToVehicleView();
    }

    public void addPartsFromNewPictogramToNewPartPainting(String function, String position){
        damageCapturingPO.clickMoreViewToOpenPictogram();
        if(getDeviceName().equalsIgnoreCase("iPad")) {
            clickZoneAndPartOnIOS(function, position);
        }else {
            damageCapturingPO.clickZone(function);
            damageCapturingPO.clickPosition(position);
        }
        damageCapturingPO.clickRPNewPartPainting();
        testCase.get().log(Status.INFO, "Add a part from new pictogram with new part painting");
        goToVehicleView();
    }

    public void goToVehicleView(){
        while(!damageCapturingPO.isTopOfVehicleView())
            damageCapturingPO.navigationVehicle();
    }

    public void waitForCalcPreviewGenerated(){
        damageCapturingPO.navigationCalcPreview();
        if(isElementPresent(By.id(damageCapturingPO.ID_CALCU_PREVIEW_TEXT))){
            String editedOutput = damageCapturingPO.getCalcPreviewContent();
            Assert.assertTrue(editedOutput.contains("AUDATEX SYSTEM"));
        }else
            Assert.assertTrue(isElementPresent(By.id(damageCapturingPO.ID_CALCU_PREVIEW_PDF)));
    }

    public boolean isChecklistNumberMatched(int expectedNumber) {
        damageCapturingPO.navigationChecklist();
        int items = damageCapturingPO.getChecklistNumber();
        if(expectedNumber==items)
            return true;
        else
            return false;
    }

    public void selectDoorsNumberIfModelOptionPopup(){
        if(getDriver().findElement(damageCapturingPO.ID_MODEL_OPTION_VIEW).isDisplayed()){
            testCase.get().log(Status.INFO, "Model option pop up");
            damageCapturingPO.clickW5Door();
            damageCapturingPO.waitForModelOptionLoading();
            testCase.get().log(Status.INFO, "Select 5Door in Qapter");
            new WebDriverWait(getDriver(), 3).until(ExpectedConditions.attributeContains(damageCapturingPO.ID_QAPTER_GRAPHICS_NOT_AVAILABLE, "style", "display: none"));
            damageCapturingPO.clickCloseModelOptionView();
        }
    }

    public void useCameraPermission(){
        if(getDeviceName().equalsIgnoreCase("Android")){
            // switch context to native
            CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
            driver.switchContextToApp();

            // click allow button
            XPathNodeBuilder nodeBuilder = new XPathNodeBuilder();
            nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "android:id/button1");
            try {
                WebElement allowButton = driver.findElementByXPathBuilder(nodeBuilder);
                driver.clickNativeElement(allowButton);
            }catch (Exception e) {
                testCase.get().log(Status.DEBUG, "No need to click Allow button");
            }

            // click the second allow button
            nodeBuilder = new XPathNodeBuilder();
            nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "com.android.packageinstaller:id/permission_allow_button");
            try {
                WebElement allowButton2 = driver.findElementByXPathBuilder(nodeBuilder);
                driver.clickNativeElement(allowButton2);
            }catch (Exception e) {
                testCase.get().log(Status.DEBUG, "No need to click Allow button");
            }
            driver.switchContextToBrowser();
        }
        else if (getDeviceName().equalsIgnoreCase("iPad")) {
            // switch context to native
            CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
            driver.switchContextToApp();

            // click allow button
            try {
                new WebDriverWait(getDriver(), 3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(damageCapturingPO.XPATH_CAMERA_ALLOW_BTN)));
                WebElement allowBtn = driver.findElementByXPath(damageCapturingPO.XPATH_CAMERA_ALLOW_BTN);
                // tap on allow button
                driver.actionTap(driver.getElementCenterPoint(allowBtn));
            }catch (Exception e) {
                testCase.get().log(Status.DEBUG, "No need to click Allow button");
            }
            driver.switchContextToBrowser();
        }
    }

    private void clickZoneAndPartOnIOS(String zone, String position) {
        String country = getCountry();
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        Configuration deviceCoordinatesData = driver.getDeviceCoordinates();
        Point zoneElementLocation = new Point(Integer.valueOf(deviceCoordinatesData.getString(country + "_" + zone + "_x")), Integer.valueOf(deviceCoordinatesData.getString(country + "_" + zone + "_y")));
        Point positionElementLocation = new Point(Integer.valueOf(deviceCoordinatesData.getString(country + "_" + position + "_x")), Integer.valueOf(deviceCoordinatesData.getString(country + "_" + position + "_y")));
        // click zone by touch action
        damageCapturingPO.waitForElementPresent(By.id(zone));
        driver.switchContextToApp();
        driver.actionTap(zoneElementLocation);
        driver.switchContextToBrowser();
        // click position by touch action
        damageCapturingPO.waitForElementPresent(By.id(position));
        driver.switchContextToApp();
        driver.actionTap(positionElementLocation);
        driver.switchContextToBrowser();
        damageCapturingPO.waitForRepairPanelDisplay();
    }
}
