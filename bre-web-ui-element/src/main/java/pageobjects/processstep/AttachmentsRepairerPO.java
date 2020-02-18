package pageobjects.processstep;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;

public class AttachmentsRepairerPO extends PageObject {

    public static final String CLAIMS_DOCUMENT_BLOCK = "ClaimsDocument";
    public static final String VEHICLE_BEFORE_REPAIR_BLOCK = "VehicleBeforeRepair";
    public static final String VEHICLE_AFTER_REPAIR_BLOCK = "VehicleAfterRepair";
    public static final String OTHER_BLOCK = "OTHER";
    private static final String CLAIMS_DOCUMENT_UPLOAD_LOCATOR = "ClaimsDocument_upload";
    private static final String VEHICLE_BEFORE_REPAIR_UPLOAD_LOCATOR = "VehicleBeforeRepair_upload";
    private static final String VEHICLE_AFTER_REPAIR_UPLOAD_LOCATOR = "VehicleAfterRepair_upload";
    private static final String OTHER_UPLOAD_LOCATOR = "OTHER_upload";
    public static final String REMOVE_ATTRUBUTE_CLASS_SCRIPT = "arguments[0].removeAttribute('class');";
    private static final String SELECT_ALL_CLAIM_DOCUMENT = "select-ClaimsDocument";
    private static final String SELECT_ALL_VEHICLE_BEFORE_REPAIR = "select-VehicleBeforeRepair";
    private static final String SELECT_ALL_VEHICLE_AFTER_REPAIR = "select-VehicleAfterRepair";
    private static final String SELECT_ALL_OTHER = "select-OTHER";
    private static final String SELECT_ALL = "select-all-link";
    private static final String DOWNLOAD_BUTTON = "batchDownload";
    private static final String CLASSNAME_GROUP_TITLE ="group-title";
    public static final String UNSELECT_ID_PREFIX = "unselect-";
    public static final String CLASSNAME_CLOSE_PREVIEW_BTN = "preview-image-close";

    public static final By NOTIFICATION_POPUP = By.cssSelector(".notification-component");
    public static final String JS_MOVE_TO_ELEMENT_BUTTOM = "arguments[0].scrollIntoView(false);";
    private static final String JS_MOVE_TO_ELEMENT_TOP = "arguments[0].scrollIntoView(true);";

    // ios native elements of uploading file
    public static final String XPATH_PHOTO_LIBARY = "//XCUIElementTypeStaticText[@name=\"Photo Library\"]";
    public static final String XPATH_ALL_PHOTOS = "//XCUIElementTypeCell[@name=\"All Photos\"]";
    public static final String XPATH_DONE = "//XCUIElementTypeButton[@name=\"Done\"]";
    public static final String XPATH_JPG_PHOTO = "(//XCUIElementTypeCell[@name=\"Photo, Landscape, April 24, 3:57 PM\"])[1]";
    public static final String XPATH_JPEG_PHOTO = "(//XCUIElementTypeCell[@name=\"Photo, Landscape, April 24, 3:57 PM\"])[2]";
    public static final String XPATH_PHOTO_LIBARY_KR = "//XCUIElementTypeStaticText[@name=\"사진 보관함\"]";
    public static final String XPATH_ALL_PHOTOS_KR = "//XCUIElementTypeCell[@name=\"모든 사진\"]";
    public static final String XPATH_DONE_KR = "//XCUIElementTypeButton[@name=\"완료\"]";
    public static final String XPATH_JPG_PHOTO_KR = "(//XCUIElementTypeCell[@name=\"사진, 가로 화면 방향, 4월 24일 오후 3:57\"])[1]";
    public static final String XPATH_JPEG_PHOTO_KR = "(//XCUIElementTypeCell[@name=\"사진, 가로 화면 방향, 4월 24일 오후 3:57\"])[2]";

    @FindBy(id = CLAIMS_DOCUMENT_UPLOAD_LOCATOR)
    private WebElement uploadClaimDocument;
    @FindBy(id = VEHICLE_BEFORE_REPAIR_UPLOAD_LOCATOR)
    private WebElement uploadVehicleBeforeRepair;
    @FindBy(id = VEHICLE_AFTER_REPAIR_UPLOAD_LOCATOR)
    private WebElement uploadVehicleAfterRepair;
    @FindBy(id = OTHER_UPLOAD_LOCATOR)
    private WebElement uploadOther;
    @FindBy(id = SELECT_ALL_CLAIM_DOCUMENT)
    private WebElement selectAllInClaimDocument;
    @FindBy(id = SELECT_ALL_VEHICLE_BEFORE_REPAIR)
    private WebElement selectAllVehicleBeforeRepair;
    @FindBy(id = SELECT_ALL_VEHICLE_AFTER_REPAIR)
    private WebElement selectAllVehicleAfterRepair;
    @FindBy(id = SELECT_ALL_OTHER)
    private WebElement selectAllOther;
    @FindBy(id = SELECT_ALL)
    private WebElement selectAll;
    @FindBy(id = DOWNLOAD_BUTTON)
    private WebElement downloadButton;
    @FindBy(id = CLAIMS_DOCUMENT_BLOCK)
    private WebElement claimDocumentBlock;
    @FindBy(id = VEHICLE_BEFORE_REPAIR_BLOCK)
    private WebElement vehicleBeforeRepairBlock;
    @FindBy(id = VEHICLE_AFTER_REPAIR_BLOCK)
    private WebElement vehicleAfterRepairBlock;
    @FindBy(id = OTHER_BLOCK)
    private WebElement otherBlock;

    @FindBy(css = ".title-toolbar-action")
    private List<WebElement> toolBarActions;
    @FindBy(css = ".actions-row-container")
    private WebElement toolBarActionRowContainer;
    @FindBy(className = "attachment-button-action-rename")
    private WebElement renameAttachment;
    @FindBy(css = ".rename-component-input > input")
    private WebElement inputAttachmentRenameField;
    @FindBy(css = ".glyphicon-ok")
    private WebElement doRename;
    @FindBy(css = ".glyphicon-remove")
    private WebElement cancelRename;
    @FindBy(className = "attachment-button-action-copy")
    private WebElement copyAttachment;
    @FindBy(className = "attachment-button-action-delete")
    private WebElement deleteAttachment;

    @FindBy(className = CLASSNAME_CLOSE_PREVIEW_BTN)
    private WebElement closePreviewBtn;

    @FindBy(css = ".svg-component.notification-close")
    private WebElement closeNotificationBtn;

    private static final String JS_HIDE_UPLOAD_DIALOG = "HTMLInputElement.prototype.click = function() {if(this.type !== 'file') HTMLElement.prototype.click.call(this);};";

    public AttachmentsRepairerPO() { super(); }

    public AttachmentsRepairerPO(WebDriver webDriver) { super(webDriver); }

    @Loggable
    public void addFilesIntoClaimsDocument(String filePath){
        this.waitForElementPresent(By.id(CLAIMS_DOCUMENT_UPLOAD_LOCATOR));
        // remove the hidden attribute for uploading files
        ((JavascriptExecutor) webDriver).executeScript(REMOVE_ATTRUBUTE_CLASS_SCRIPT, uploadClaimDocument);
        ((JavascriptExecutor) webDriver).executeScript(JS_HIDE_UPLOAD_DIALOG);
        // wait for the upload button clickable
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(uploadClaimDocument));
        this.uploadClaimDocument.sendKeys(filePath);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void addFilesIntoVehicleBeforeRepair(String filePath) {
        this.waitForElementPresent(By.id(VEHICLE_BEFORE_REPAIR_UPLOAD_LOCATOR));
        // remove the hidden attribute for uploading files
        ((JavascriptExecutor) webDriver).executeScript(REMOVE_ATTRUBUTE_CLASS_SCRIPT, uploadVehicleBeforeRepair);
        ((JavascriptExecutor) webDriver).executeScript(JS_HIDE_UPLOAD_DIALOG);
        // wait for the upload button clickable
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(uploadVehicleBeforeRepair));
        this.uploadVehicleBeforeRepair.sendKeys(filePath);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void addFilesIntoVehicleAfterRepair(String filePath) {
        this.waitForElementPresent(By.id(VEHICLE_AFTER_REPAIR_UPLOAD_LOCATOR));
        // remove the hidden attribute for uploading files
        ((JavascriptExecutor) webDriver).executeScript(REMOVE_ATTRUBUTE_CLASS_SCRIPT, uploadVehicleAfterRepair);
        ((JavascriptExecutor) webDriver).executeScript(JS_HIDE_UPLOAD_DIALOG);
        // wait for the upload button clickable
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(uploadVehicleAfterRepair));
        this.uploadVehicleAfterRepair.sendKeys(filePath);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void addFilesIntoOther(String filePath) {
        this.waitForElementPresent(By.id(OTHER_UPLOAD_LOCATOR));
        // remove the hidden attribute for uploading files
        ((JavascriptExecutor) webDriver).executeScript(REMOVE_ATTRUBUTE_CLASS_SCRIPT, uploadOther);
        ((JavascriptExecutor) webDriver).executeScript(JS_HIDE_UPLOAD_DIALOG);
        // wait for the upload button clickable
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(uploadOther));
        this.uploadOther.sendKeys(filePath);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public String getClaimsDocumentAttributeValue(int index, String attribute){
        String value;
        try {
            value = webDriver.findElement(getXpathOfUploadedFile(index, CLAIMS_DOCUMENT_BLOCK)).getAttribute(attribute);
        } catch (StaleElementReferenceException e) {
            // wait for image loaded after image uploaded successfully or switch to attachment page
            value = waitForElementRefreshedAndVisible(webDriver.findElement(getXpathOfUploadedFile(index, CLAIMS_DOCUMENT_BLOCK))).getAttribute(attribute);
        }
        return value;
    }

    @Loggable
    public String getVehicleBeforeRepairAttributeValue(int index, String attribute){
        String value;
        try {
            value = webDriver.findElement(getXpathOfUploadedFile(index, VEHICLE_BEFORE_REPAIR_BLOCK)).getAttribute(attribute);
        } catch (StaleElementReferenceException e) {
            // wait for image loaded after image uploaded successfully or switch to attachment page
            value = waitForElementRefreshedAndVisible(webDriver.findElement(getXpathOfUploadedFile(index, VEHICLE_BEFORE_REPAIR_BLOCK))).getAttribute(attribute);
        }
        return value;
    }

    @Loggable
    public String getVehicleAfterRepairAttributeValue(int index, String attribute){
        String value;
        try {
            value = webDriver.findElement(getXpathOfUploadedFile(index, VEHICLE_AFTER_REPAIR_BLOCK)).getAttribute(attribute);
        } catch (StaleElementReferenceException e) {
            // wait for image loaded after image uploaded successfully or switch to attachment page
            value = waitForElementRefreshedAndVisible(webDriver.findElement(getXpathOfUploadedFile(index, VEHICLE_AFTER_REPAIR_BLOCK))).getAttribute(attribute);
        }
        return value;
    }

    @Loggable
    public String getOtherAttributeValue(int index, String attribute){
        String value;
        try {
            value = webDriver.findElement(getXpathOfUploadedFile(index, OTHER_BLOCK)).getAttribute(attribute);
        } catch (StaleElementReferenceException e) {
            // wait for image loaded after image uploaded successfully or switch to attachment page
            value = waitForElementRefreshedAndVisible(webDriver.findElement(getXpathOfUploadedFile(index, OTHER_BLOCK))).getAttribute(attribute);
        }
        return value;
    }

    private String idEqual = "//*[@id=\"";
    private String divBlock = "\"]/div[2]/div";
    public By getXpathOfUploadedFile(int index, String block) { return By.xpath(idEqual+block+divBlock+"["+index+"]/div[2]/a/div[1]"); }
    public By getXpathOfUploadedFileCheckbox(int index, String block) { return By.xpath(idEqual+block+divBlock+"["+index+"]/div[2]/div/label/span"); }
    public By getXpathOfUploadedFileCheckboxOnIOS(int index, String block) { return By.xpath(idEqual+block+divBlock+"["+index+"]/div[2]/label/span"); }
    public By getXpathOfUploadedFileCheckboxInput(int index, String block) { return By.xpath(idEqual+block+divBlock+"["+index+"]/div[2]/label/input"); }

    public void selectAttachment(int index, String block) {
        // hover on the image
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_BUTTOM, webDriver.findElement(getByOfUploadFileName(index, block)));
        new Actions(webDriver).moveToElement(webDriver.findElement(getXpathOfUploadedFile(index, block))).perform();
        // click the checkbox
        WebElement checkbox = new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(getXpathOfUploadedFileCheckbox(index, block)));
        this.click(checkbox);
        // wait for the checkbox to be checked
        try {
            // wait for element refreshed and to be checked
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.refreshed(
                    ExpectedConditions.attributeToBe(getXpathOfUploadedFileCheckboxInput(index, block), "data-checked", "true")));
        } catch (TimeoutException e) {
            // element had been refreshed. verify if it is checked
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeToBe(getXpathOfUploadedFileCheckboxInput(index, block), "data-checked", "true"));
        }
        // wait for the unselect link display
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id(UNSELECT_ID_PREFIX + block)));
        // wait few seconds to prevent checkbox is not saved after go to other page (randomly happens on Chrome)
        waitForElementNotDisplay(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickUploadClaimsDocumentImage(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_BUTTOM, webDriver.findElement(By.id(CLAIMS_DOCUMENT_BLOCK)));
        this.click(this.getElementOfAttachmentUploadLayout(CLAIMS_DOCUMENT_BLOCK));
    }
    @Loggable
    public void clickVehicleBeforeRepairImage(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_BUTTOM, webDriver.findElement(By.id(VEHICLE_BEFORE_REPAIR_BLOCK)));
        this.click(this.getElementOfAttachmentUploadLayout(VEHICLE_BEFORE_REPAIR_BLOCK));
    }
    @Loggable
    public void clickVehicleAfterRepairImage(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_BUTTOM, webDriver.findElement(By.id(VEHICLE_AFTER_REPAIR_BLOCK)));
        this.click(this.getElementOfAttachmentUploadLayout(VEHICLE_AFTER_REPAIR_BLOCK));
    }
    @Loggable
    public void clickOtherImage(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_BUTTOM, webDriver.findElement(By.id(OTHER_BLOCK)));
        this.click(this.getElementOfAttachmentUploadLayout(OTHER_BLOCK));
    }

    public WebElement getElementOfAttachmentUploadLayout(String block) { return webDriver.findElement(By.xpath("//form[@id=\""+block+"\"]")); }

    public By getByOfUploadFileName(int index, String block){ return By.xpath("//*[@id=\""+block+divBlock+"["+index+"]/div[3]/div[1]/span"); }
    public WebElement getElementOfUploadFileName(int index, String block){ return webDriver.findElement(getByOfUploadFileName(index, block)); }

    @Loggable
    public String getClaimsDocumentAttachmentFileName(int index){ return getElementOfUploadFileName(index, CLAIMS_DOCUMENT_BLOCK).getText(); }
    @Loggable
    public String getVehicleBeforeRepairAttachmentFileName(int index){ return getElementOfUploadFileName(index, VEHICLE_BEFORE_REPAIR_BLOCK).getText(); }
    @Loggable
    public String getVehicleAfterRepairAttachmentFileName(int index){ return getElementOfUploadFileName(index, VEHICLE_AFTER_REPAIR_BLOCK).getText(); }
    @Loggable
    public String getOtherAttachmentFileName(int index){ return getElementOfUploadFileName(index, OTHER_BLOCK).getText(); }


    @Loggable
    public int getClaimDocumentAttachmentFileNumber(){ return webDriver.findElements(By.xpath(idEqual+CLAIMS_DOCUMENT_BLOCK+divBlock)).size()-1; }
    @Loggable
    public int getVehicleBeforeRepairAttachmentFileNumber(){ return webDriver.findElements(By.xpath(idEqual+VEHICLE_BEFORE_REPAIR_BLOCK+divBlock)).size()-1; }
    @Loggable
    public int getVehicleAfterRepairAttachmentFileNumber(){ return webDriver.findElements(By.xpath(idEqual+VEHICLE_AFTER_REPAIR_BLOCK+divBlock)).size()-1; }
    @Loggable
    public int getOtherAttachmentFileNumber(){ return webDriver.findElements(By.xpath(idEqual+OTHER_BLOCK+"\"]/div[2]/div")).size()-1; }

    @Loggable
    public void clickSelectAllInClaimDocument() {
        selectAllInClaimDocument.click();
    }

    @Loggable
    public void clickSelectAllInVehicleBeforeRepair() {
        selectAllVehicleBeforeRepair.click();
    }

    @Loggable
    public void clickSelectAllInVehicleAfterRepair() {
        selectAllVehicleAfterRepair.click();
    }

    @Loggable
    public void clickSelectAllInOther() {
        selectAllOther.click();
    }

    @Loggable
    public void clickSelectAllAttachments() {
        selectAll.click();
    }

    @Loggable
    public void clickDownloadButton() {
        this.click(downloadButton);
    }

    @Loggable
    public String getClaimsDocumentBlockTitle() {
        return claimDocumentBlock.findElement(By.className(CLASSNAME_GROUP_TITLE)).getText();
    }

    @Loggable
    public String getVehicleBeforeRepairBlockTitle() {
        return vehicleBeforeRepairBlock.findElement(By.className(CLASSNAME_GROUP_TITLE)).getText();
    }

    @Loggable
    public String getVehicleAfterRepairBlockTitle() {
        return vehicleAfterRepairBlock.findElement(By.className(CLASSNAME_GROUP_TITLE)).getText();
    }

    @Loggable
    public String getOtherBlockTitle() {
        return otherBlock.findElement(By.className(CLASSNAME_GROUP_TITLE)).getText();
    }

    @Loggable
    public String getCaseNumber() {
        return webDriver.findElement(By.className("claim-identification-item-value")).getText();
    }

    @Loggable
    public void scrollToTheBlock(String lastBlock){
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", webDriver.findElement(getXpathOfUploadedFile(1, lastBlock)));
    }

    @Loggable
    public void clickAttachmentActionToolBar(int index, String block){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT_TOP, webDriver.findElement(By.id(block)));
        WebElement actionToolBar = getBlockActionToolBarElement(index, block);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(actionToolBar));
        this.click(actionToolBar);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(toolBarActionRowContainer));
    }
    private WebElement getBlockActionToolBarElement(int index, String block){
        List<WebElement> attachmentToolBar = webDriver.findElements(By.xpath("//*[@id='"+block+"']/descendant::div[@class='more-row-icon-wrapper']"));
        return attachmentToolBar.get(index-1);
    }

    @Loggable
    public void clickRenameButton(){ this.click(renameAttachment); }

    @Loggable
    public void inputNewAttachmentFileName(String updatedName){ this.sendKeys(inputAttachmentRenameField, updatedName); }

    @Loggable
    public void clickDoRenameIcon(){ this.click(doRename); }

    @Loggable
    public void clickCancelRenameIcon(){ this.click(cancelRename); }

    @Loggable
    public void clickCopyButton(){ this.click(copyAttachment); }

    @Loggable
    public void clickDeleteButton(){ this.click(deleteAttachment); }

    @Loggable
    public void closeImagePreview(){ this.click(closePreviewBtn); }

    @Loggable
    public void closeFileUploadedNotification(){ this.click(closeNotificationBtn); }
}
