package pageobjects.processstep;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;


public class ModifySparePartsPO extends PageObject{
    private static final String GET_ATTRIBUTE_VALUE = "value";
    private static final String PART_SUPPLIER_PREFIX = "#altPartSupplier_";
    private static final String PART_NUMBER_PREFIX = "#altPartNumber_";
    private static final String PART_PRICE_PREFIX = "#altPartPrice_";
    public static final String ID_MANURAL_EDITED_PARTS_TABLE = "modify-parts-table-component";
    public static final String ID_ADD_PART_BUTTON = "button[id='root.task.manualEditedParts.addPartButton']";

    //Loading circle
    private static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");

    //Parts Information
    @FindBy(css = "input[id=\"root.task.manualEditedParts.newPartNumber\"]")
    private WebElement newPartNumber;
    @FindBy(css = "input[id=\"root.task.manualEditedParts.newPartDescription\"]")
    private WebElement newDescription;
    @FindBy(css = "input[id=\"root.task.manualEditedParts.newPartPrice\"]")
    private WebElement newPrice;
    @FindBy(css = "input[id=\"root.task.manualEditedParts.newPartSupplier\"]")
    private WebElement newSupplier;
    @FindBy(css = "input[id=\"root.task.manualEditedParts.newPartType\"]")
    private WebElement newPartType;
    @FindBy(id="react-select-root.task.manualEditedParts.newPartType-option-0")
    private WebElement selectionOEMPart;
    @FindBy(id="react-select-root.task.manualEditedParts.newPartType-option-1")
    private WebElement selectionNonOEMPart;
    @FindBy(id="react-select-root.task.manualEditedParts.newPartType-option-2")
    private WebElement selectionUsedPart;
    @FindBy(css=ID_ADD_PART_BUTTON)
    private WebElement btnAddPart;

    //Upload by template
    public static final String ID_BTN_UPLOAD_CSV = "root.task.manualEditedParts.uploadPartsCsvButton_upload";
    @FindBy(id="root.task.manualEditedParts.downloadPartsTemplateCsvButton")
    private WebElement btnDownloadCsvTemplate;
    @FindBy(id="root.task.manualEditedParts.downloadPartsTemplateXlsButton")
    private WebElement btnDownloadXlsTemplate;
    @FindBy(id=ID_BTN_UPLOAD_CSV)
    private WebElement btnUploadCsv;
    @FindBy(id="root.task.manualEditedParts.uploadPartsXlsButton_upload")
    private WebElement btnUploadXls;

    //Added parts table
    @FindBy(id=ID_MANURAL_EDITED_PARTS_TABLE)
    private WebElement addedPartsTable;
    @FindBy(xpath = "//*[@id=\"modify-parts-table-component\"]/div/div[1]/div[2]/div[1]/div[1]/div/div")
    private List<WebElement> partsRow;
    @FindBy(css="label[for=\"total_saving\"]")
    private WebElement totalSaving;
    @FindBy(id="select-field-id-altPartType")
    private List<WebElement> selectAltPartType;
    @FindBy(id="react-select-altPartType-option-0")
    private WebElement selectAltPartOEMPart;
    @FindBy(id="react-select-altPartType-option-1")
    private WebElement selectAltPartNonOEMPart;
    @FindBy(id="react-select-altPartType-option-2")
    private WebElement selectAltPartUsedPart;
    @FindBy(css = ".react-select__option")
    private List<WebElement> selectPartType;
    //row0
    public static final String PART_NUM = "oemPartNumber";
    @FindBy(name = "override")
    private List<WebElement> partCheckbox;
    @FindBy(name="oemGuideNumber")
    private List<WebElement> guideNumber;
    @FindBy(name="oemPartDescription")
    private List<WebElement> partDescription;
    @FindBy(name= PART_NUM)
    private List<WebElement> partNumber;
    @FindBy(name="oemPartPrice")
    private List<WebElement> partPrice;
    @FindBy(xpath="//*[@id=\"select-field-id-altPartType\"]/div/div[1]/div[1]")
    private List<WebElement> partType;
    @FindBy(id="altPartSupplier")
    private List<WebElement> partSupplier;
    @FindBy(id="altPartNumber")
    private List<WebElement> enteredPartNumber;
    @FindBy(id="altPartPrice")
    private List<WebElement> enteredPrice;
//    //row1
//    public static final String PART_NUM_1 = "oemPartNumber_1";
//    @FindBy(id="oemGuideNumber_1")
//    private WebElement guideNumber1;
//    @FindBy(id="oemPartDescription_1")
//    private WebElement partDescription1;
//    @FindBy(id=PART_NUM_1)
//    private WebElement partNumber1;
//    @FindBy(id="oemPartPrice_1")
//    private WebElement partPrice1;
//    @FindBy(css="#altPartType_1 .react-select__single-value")
//    private WebElement partType1;
//    @FindBy(css= "#altPartSupplier_1 input")
//    private WebElement partSupplier1;
//    @FindBy(css="#altPartNumber_1 input")
//    private WebElement enteredPartNumber1;
//    @FindBy(css="#altPartPrice_1 input")
//    private WebElement enteredPrice1;
//    //row2
//    public static final String PART_NUM_2 = "oemPartNumber_2";
//    @FindBy(id="oemGuideNumber_2")
//    private WebElement guideNumber2;
//    @FindBy(id="oemPartDescription_2")
//    private WebElement partDescription2;
//    @FindBy(id=PART_NUM_2)
//    private WebElement partNumber2;
//    @FindBy(id="oemPartPrice_2")
//    private WebElement partPrice2;
//    @FindBy(css="#altPartType_2 .react-select__single-value")
//    private WebElement partType2;
//    @FindBy(css="#altPartSupplier_2 input")
//    private WebElement partSupplier2;
//    @FindBy(css="#altPartNumber_2 input")
//    private WebElement enteredPartNumber2;
//    @FindBy(css="#altPartPrice_2 input")
//    private WebElement enteredPrice2;

    @FindBy(id="delete")
    private WebElement btnDelete;

    public ModifySparePartsPO() {
        super();
    }

    public ModifySparePartsPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void inputPartNumber(String partNum) { this.sendKeys(newPartNumber, partNum); }

    @Loggable
    public void inputPartDescription(String description) { this.sendKeys(newDescription, description); }

    @Loggable
    public void inputPartPrice(String price) { this.sendKeys(newPrice, price); }

    @Loggable
    public void inputPartSupplier(String supplier) { this.sendKeys(newSupplier, supplier); }

    @Loggable
    public void selectPartType(String partType){
        newPartType.click();
        for(WebElement ele: selectPartType) {
            if(ele.getText().equals(partType)) {
                this.click(ele);
                break;
            }
        }
    }

    @Loggable
    public void selectAddedPartType(int row, String partType){
        this.click(selectAltPartType.get(row));
        for(WebElement ele: selectPartType) {
            if(ele.getText().equals(partType)) {
                this.click(ele);
                break;
            }
        }
    }

    @Loggable
    public void clickAddPart(){ this.click(btnAddPart); }

    @Loggable
    public int getAddedPartsNum(){
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.visibilityOf(addedPartsTable));
        return partsRow.size();
    }

    @Loggable
    public By getDeletePartRow(int num){ return By.id(ID_MANURAL_EDITED_PARTS_TABLE+"_"+num); }

    @Loggable
    public String getGuideNumber(int row){ return this.getText(guideNumber.get(row)); }
    @Loggable
    public String getPartDescription(int row){ return this.getText(partDescription.get(row)); }
    @Loggable
    public String getPartNumber(int row){ return this.getText(partNumber.get(row)); }
    @Loggable
    public String getPartPrice(int row){ return this.getText(partPrice.get(row)); }
    @Loggable
    public String getPartType(int row){ return this.getText(partType.get(row)); }
    @Loggable
    public String getPartSupplier(int row){
        return partSupplier.get(row).getAttribute(GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getEnteredPartNumber(int row){
        return enteredPartNumber.get(row).getAttribute(GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getEnteredPrice(int row){
        return enteredPrice.get(row).getAttribute(GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public void clickDelete(){ this.click(btnDelete); }

    @Loggable
    public void clickPartCheckbox(int row){
        WebElement getCheckBoxInRow = partsRow.get(row);
        this.click(getCheckBoxInRow.findElement(By.xpath("div/div/lable")));
    }

    @Loggable
    public void setEnteredPrice(int row, String newPrice){
        WebElement enterPriceInRow = enteredPrice.get(row);
        // Use BACK_SPACE to clear value since
        // - clear() and ctrl+a+delete is not working on iPad Safari if element has default value
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOf(enterPriceInRow));
        int length = enterPriceInRow.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            enterPriceInRow.sendKeys(Keys.BACK_SPACE);
        this.sendKeys(enterPriceInRow, newPrice);
    }

    @Loggable
    public void clickSupplier(int row){ this.click(partSupplier.get(row)); }

    @Loggable
    public String getTotalSavingAmount(String currency){
        String wholeTextOfTotalSaving = this.getText(totalSaving);
        String[] splitText = wholeTextOfTotalSaving.split(":");

        return splitText[1].replaceAll(currency, "").trim();
    }

    @Loggable
    public void uploadCsvToAddParts(String filePath) {
        // remove the hidden attribute for uploading files
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].removeAttribute('class');", btnUploadCsv);
        // wait for the upload button clickable
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(btnUploadCsv));
        btnUploadCsv.sendKeys(filePath);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void uploadXlsToAddParts(String filePath){ this.sendKeys(btnUploadXls, filePath); }

    @Loggable
    public void clickDownloadCsvTemplate(){ this.click(btnDownloadCsvTemplate); }

    @Loggable
    public void clickDownloadXlsTemplate(){ this.click(btnDownloadXlsTemplate); }

}
