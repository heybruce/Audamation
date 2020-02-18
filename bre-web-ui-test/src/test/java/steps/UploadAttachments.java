package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.AttachmentsRepairerPO;
import utils.webdrivers.CustomDeviceDriver;
import utils.xpath.XPathNodeBuilder;

import java.util.ArrayList;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class UploadAttachments extends TestBase {
    private AttachmentsRepairerPO attachmentPO;

    public UploadAttachments(){
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
    }

    public void uploadImageFromAndroid(String fileName){
        // switch context to native
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        driver.switchContextToApp();

        // click allow button
        XPathNodeBuilder nodeBuilder = new XPathNodeBuilder();
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "com.android.packageinstaller:id/permission_allow_button");
        try {
            WebElement allowButton = driver.findElementByXPathBuilder(nodeBuilder);
            driver.clickNativeElement(allowButton);
            testCase.get().log(Status.DEBUG, "Allow permission");
        }catch (Exception e) { }

        // click files button
        // For system locale, select xpath node instead of "Files" button
        XPathNodeBuilder parentNodeBuilder = new XPathNodeBuilder();
        parentNodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "android:id/resolver_list");
        XPathNodeBuilder parentNodeBuilder2 = new XPathNodeBuilder(XPathNodeBuilder.CLASS_LINEAR_LAYOUT);
        parentNodeBuilder2.addIndex(0);
        nodeBuilder = new XPathNodeBuilder(XPathNodeBuilder.CLASS_LINEAR_LAYOUT);
        nodeBuilder.addIndex(2);
        ArrayList<XPathNodeBuilder> nodeBuilders = new ArrayList<>();
        nodeBuilders.add(parentNodeBuilder);
        nodeBuilders.add(parentNodeBuilder2);
        nodeBuilders.add(nodeBuilder);
        WebElement FilesButton = driver.findElementByXPathBuilder(nodeBuilders);
        driver.clickNativeElement(FilesButton);

        //Click Tool bar
        parentNodeBuilder = new XPathNodeBuilder();
        parentNodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "com.android.documentsui:id/toolbar");
        nodeBuilder = new XPathNodeBuilder(XPathNodeBuilder.CLASS_IMAGE_BUTTON);
        nodeBuilder.addIndex(0);
        nodeBuilders = new ArrayList<>();
        nodeBuilders.add(parentNodeBuilder);
        nodeBuilders.add(nodeBuilder);
        WebElement toolBarButton = driver.findElementByXPathBuilder(nodeBuilders);
        driver.clickNativeElement(toolBarButton);

        //For system locale, select the first linear layout instead of "Images" folder
        parentNodeBuilder = new XPathNodeBuilder();
        parentNodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "com.android.documentsui:id/roots_list");
        nodeBuilder = new XPathNodeBuilder(XPathNodeBuilder.CLASS_LINEAR_LAYOUT);
        nodeBuilder.addIndex(0);
        XPathNodeBuilder nodeBuilder2 = new XPathNodeBuilder(XPathNodeBuilder.CLASS_LINEAR_LAYOUT);
        nodeBuilder2.addIndex(1);
        nodeBuilders = new ArrayList<>();
        nodeBuilders.add(parentNodeBuilder);
        nodeBuilders.add(nodeBuilder);
        nodeBuilders.add(nodeBuilder2);
        WebElement ImageSelection = driver.findElementByXPathBuilder(nodeBuilders);
        driver.clickNativeElement(ImageSelection);

        //Select "Automation" folder which contains two testing image files
        nodeBuilder = new XPathNodeBuilder();
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_CLASS, XPathNodeBuilder.CLASS_TEXT_VIEW);
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_TEXT, "Automation");
        WebElement automationFolder = driver.findElementByXPathBuilder(nodeBuilder);
        driver.clickNativeElement(automationFolder);

        nodeBuilder = new XPathNodeBuilder();
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_CLASS, XPathNodeBuilder.CLASS_TEXT_VIEW);
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_TEXT, fileName);
        WebElement imageButton = driver.findElementByXPathBuilder(nodeBuilder);
        driver.clickNativeElement(imageButton);

        driver.switchContextToBrowser();
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
        // Workaround of unable to detect invisibility of notification popup after chrome upgrade to 76.0.3809.132
        attachmentPO.closeFileUploadedNotification();
    }

    public void RenameFileNameAndConfirm(int index, String block, String NewFileName){
        attachmentPO.clickAttachmentActionToolBar(index, block);
        attachmentPO.clickRenameButton();
        attachmentPO.inputNewAttachmentFileName(NewFileName);
        attachmentPO.clickDoRenameIcon();
        testCase.get().log(Status.INFO, "Rename filename to be '" + NewFileName + "' in block '" + block +"'");
    }

    public void RenameFileNameAndCancel(int index, String block, String NewFileName){
        attachmentPO.clickAttachmentActionToolBar(index, block);
        attachmentPO.clickRenameButton();
        attachmentPO.inputNewAttachmentFileName(NewFileName);
        attachmentPO.clickCancelRenameIcon();
        testCase.get().log(Status.INFO, "Rename filename and cancel in block '" + block +"'");
    }

    public void CopyUploadedAttachmentFile(int index, String block){
        attachmentPO.clickAttachmentActionToolBar(index, block);
        attachmentPO.clickCopyButton();
        new WebDriverWait(getDriver(), 5).until(
                ExpectedConditions.presenceOfElementLocated(attachmentPO.getByOfUploadFileName(index+1, block)));
        testCase.get().log(Status.INFO, "Copy uploaded attachment file in block '" + block +"'");
    }

    public void DeleteUploadedAttachmentFile(int index, String block){
        attachmentPO.clickAttachmentActionToolBar(index, block);
        attachmentPO.clickDeleteButton();
        new WebDriverWait(getDriver(), 5).until(
                ExpectedConditions.invisibilityOfElementLocated(attachmentPO.getByOfUploadFileName(index, block)));
        new WebDriverWait(getDriver(), 10).until(
                ExpectedConditions.invisibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
        testCase.get().log(Status.INFO, "Delete uploaded attachment file in block '" + block +"'");
    }

    public void uploadJPGFromIOS(){
        // switch context to native
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();

        // click upload button
        driver.switchContextToApp();
        clickPhotoLibraryOnIOS();
        clickAllPhotosOnIOS();
        selectJPGPhotoOnIOS();
        clickDoneOnIOS();

        driver.switchContextToBrowser();
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
    }

    public void clickUploadClaimsDocumentImageFromiOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        ((JavascriptExecutor) driver).executeScript(attachmentPO.JS_MOVE_TO_ELEMENT_BUTTOM, driver.findElement(By.id(attachmentPO.CLAIMS_DOCUMENT_BLOCK)));
        //tap the center of the block
        Point elementCenterPoint = driver.getElementCenterPoint(driver.findElementByXPath("//form[@id=\""+attachmentPO.CLAIMS_DOCUMENT_BLOCK+"\"]"));
        driver.actionTap(elementCenterPoint);
    }

    public void clickVehicleBeforeRepairImageFromiOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        ((JavascriptExecutor) driver).executeScript(attachmentPO.JS_MOVE_TO_ELEMENT_BUTTOM, driver.findElement(By.id(attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK)));
        //tap the center of the block
        Point elementCenterPoint = driver.getElementCenterPoint(driver.findElementByXPath("//form[@id=\""+attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK+"\"]"));
        driver.actionTap(elementCenterPoint);
    }

    public void clickVehicleAfterRepairImageFromiOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        ((JavascriptExecutor) driver).executeScript(attachmentPO.JS_MOVE_TO_ELEMENT_BUTTOM, driver.findElement(By.id(attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK)));
        //tap the center of the block
        Point elementCenterPoint = driver.getElementCenterPoint(driver.findElementByXPath("//form[@id=\""+attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK+"\"]"));
        driver.actionTap(elementCenterPoint);
    }

    public void clickOtherImageFromiOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        ((JavascriptExecutor) driver).executeScript(attachmentPO.JS_MOVE_TO_ELEMENT_BUTTOM, driver.findElement(By.id(attachmentPO.OTHER_BLOCK)));
        //tap the center of the block
        Point elementCenterPoint = driver.getElementCenterPoint(driver.findElementByXPath("//form[@id=\""+attachmentPO.OTHER_BLOCK+"\"]"));
        driver.actionTap(elementCenterPoint);
    }

    private void clickPhotoLibraryOnIOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        if (getCountry().equals("KR"))
            driver.findElementByXPath(attachmentPO.XPATH_PHOTO_LIBARY_KR).click();
        else
            driver.findElementByXPath(attachmentPO.XPATH_PHOTO_LIBARY).click();
    }

    private void clickAllPhotosOnIOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        if (getCountry().equals("KR"))
            driver.findElementByXPath(attachmentPO.XPATH_ALL_PHOTOS_KR).click();
        else
            driver.findElementByXPath(attachmentPO.XPATH_ALL_PHOTOS).click();
    }

    private void selectJPGPhotoOnIOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        if (getCountry().equals("KR"))
            driver.findElementByXPath(attachmentPO.XPATH_JPG_PHOTO_KR).click();
        else
            driver.findElementByXPath(attachmentPO.XPATH_JPG_PHOTO).click();
    }

    private void selectJPEGPhotoOnIOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        if (getCountry().equals("KR"))
            driver.findElementByXPath(attachmentPO.XPATH_JPEG_PHOTO_KR).click();
        else
            driver.findElementByXPath(attachmentPO.XPATH_JPEG_PHOTO).click();
    }

    private void clickDoneOnIOS(){
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        if (getCountry().equals("KR"))
            driver.findElementByXPath(attachmentPO.XPATH_DONE_KR).click();
        else
            driver.findElementByXPath(attachmentPO.XPATH_DONE).click();
    }

    public void uploadJPEGFromIOS(){
        // switch context to native
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();

        // click upload button
        driver.switchContextToApp();
        clickPhotoLibraryOnIOS();
        clickAllPhotosOnIOS();
        selectJPEGPhotoOnIOS();
        clickDoneOnIOS();
        driver.switchContextToBrowser();
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(attachmentPO.NOTIFICATION_POPUP));
    }

    public void selectAttachmentOnIOS(int index, String block) {
        // wait for checkbox displays
        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.presenceOfElementLocated(attachmentPO.getXpathOfUploadedFileCheckboxInput(index, block)));

        // Tap the center of checkbox
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        Point elementCenterPoint = driver.getElementCenterPoint(driver.findElement(attachmentPO.getXpathOfUploadedFileCheckboxOnIOS(index, block)));
        driver.actionTap(elementCenterPoint);

        // wait for the unselect link display
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id(attachmentPO.UNSELECT_ID_PREFIX + block)));
    }
}
