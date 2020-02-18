package pageobjects.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;

public class LaborRatesPO extends PageObject {
    //Loading circle
    private static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");

    //Labour rates
    public static final String ID_LABOR_RATE1 = "root.task.classInput.rate1.value";
    public static final String ID_LABOR_RATE2 = "root.task.classInput.rate2.value";
    public static final String ID_LABOR_RATE3 = "root.task.classInput.rate3.value";
    public static final String ID_PAINT_RATE = "root.task.classInput.ratePaint1.value";
    private static final String CONFIRM_LABEL = "confirmLabel";
    private static final By TEXT_MANUF_PRINT_CODE_VALUE = By.className("react-select-root.task.calculationOptions.manufTimePrintCode");
    public static final By NAME_PAINT_METHOD_VALUE = By.name("root.task.classInput.category");
    public static final By NAME_CONTRACTS_VALUE = By.name("root.task.classInput.partnershipId");

    @FindBy(id = "root.task.classInput.partnershipId")
    private WebElement inputPartnership;
    @FindBy(id = "select-field-id-root.task.classInput.partnershipId")
    private WebElement partnership;
    @FindBy(id = ID_LABOR_RATE1)
    private WebElement laborRate1;
    @FindBy(id = ID_LABOR_RATE2)
    private WebElement laborRate2;
    @FindBy(id = ID_LABOR_RATE3)
    private WebElement laborRate3;
    @FindBy(id = "root.task.classInput.category")
    private WebElement inputPaintMethod;
    @FindBy(id = "select-field-id-root.task.classInput.category")
    private WebElement paintMethod;
    @FindBy(id = ID_PAINT_RATE)
    private WebElement paintRate;

    @FindBy(id = "index-absolute-div")
    private WebElement grayMask;
    @FindBy(id = "confirm")
    private WebElement confirmDialog;
    @FindBy(xpath = "//*[@id=\"confirm\"]/div/div/div[3]/button[2]")
    private WebElement btnConfirmYes;

    @FindBy(className = "notification-group-display")
    private WebElement warningMessage;

    //ID Block Code(IDBC)
    public static final String GET_ATTRIBUTE_VALUE = "value";
    @FindBy(id = "root.task.classInput.idbcAddButton")
    private WebElement addIdbcButton;
    @FindBy(id = "root.task.classInput.idbcDeleteButton")
    private WebElement delete;
    @FindBy(css = ".idbc-value")
    private List<WebElement> addedIdbcValue;
    @FindBy(className = "idbc-check-number")
    private List<WebElement> idBlockCode;

    //ID Block Code(IDBC) Dialog
    public static final String ADD_IDBC_BTN = "AddIDBlockCodes";
    @FindBy(id = "AddIDBlockCodes")
    private WebElement addIdbcDialog;
    @FindBy(id = "idbc-08-amount")
    private WebElement idbc08Amount;
    @FindBy(id = "idbc-67-amount")
    private WebElement idbc67Amount;
    @FindBy(id = "idbc-09-amount")
    private WebElement idbc09Amount;
    @FindBy(xpath = "//*[@id=\"AddIDBlockCodes\"]/div/div/div[3]/button[2]")
    private WebElement idbcPopUpSubmitButton;

    //Calculation options
    @FindBy(id = "react-select-root.task.calculationOptions.calculationTitle--value-item")
    private WebElement calculationTitle;
    @FindBy(id="select-field-id-root.task.calculationOptions.manufTimePrintCode")
    private WebElement manufacturerTimePrintCode;
    @FindBy(id="root.task.calculationOptions.manufTimePrintCode")
    private WebElement inputManufTimePrintCode;

    public LaborRatesPO() {
        super();
    }

    public LaborRatesPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void selectPartnerShipByInputValue(String value) {
        this.click(partnership);
        this.sendKeys(inputPartnership, value);
        inputPartnership.sendKeys(Keys.ENTER);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(partnership));
    }

    @Loggable
    public void selectPaintMethodByInputValue(String value) {
        this.click(paintMethod);
        this.sendKeys(inputPaintMethod, value);
        inputPaintMethod.sendKeys(Keys.ENTER);
    }

    @Loggable
    public void clickLaborRate1Textbox(){ this.click(laborRate1); }

    @Loggable
    public void clickLaborRate2Textbox(){ this.click(laborRate2); }

    @Loggable
    public void clickPaintRateTextbox(){ this.click(paintRate); }

    @Loggable
    public void enterLaborRate1Textbox(String textLaborRate1) {
        // will not trigger labor rate change event on Chrome, use backspace to clear labor rate value instead (v19.07)
        clearLaborRate1Textbox();
        this.sendKeys(laborRate1, textLaborRate1);
    }

    @Loggable
    public void enterLaborRate2Textbox(String textLaborRate2) {
        // will not trigger labor rate change event on Chrome, use backspace to clear labor rate value instead (v19.07)
        clearLaborRate2Textbox();
        this.sendKeys(laborRate2, textLaborRate2); }

    @Loggable
    public void enterLaborRate3Textbox(String textLaborRate3) {
        // will not trigger labor rate change event on Chrome, use backspace to clear labor rate value instead (v19.07)
        clearLaborRate3Textbox();
        this.sendKeys(laborRate3, textLaborRate3);
    }

    @Loggable
    public void enterPaintRateTextbox(String textPaintRate) {
        // will not trigger labor rate change event on Chrome, use backspace to clear labor rate value instead (v19.07)
        clearPaintRateTextbox();
        this.sendKeys(paintRate, textPaintRate);
    }

    @Loggable
    public void triggerChangeRateDialog(){ this.click(grayMask); }

    @Loggable
    public void confirmChangeRate(){
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.id(CONFIRM_LABEL)));
        this.click(btnConfirmYes);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOf(confirmDialog));
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clearPaintRateTextbox(){
        //Use BACK_SPACE to clear value since
        // - will not trigger labor rate change event on Chrome
        int length = paintRate.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            paintRate.sendKeys(Keys.BACK_SPACE);
     }

    @Loggable
    public void clearLaborRate1Textbox(){
        //Use BACK_SPACE to clear value since
        // - clear() will not trigger labor rate change event on Chrome
        int length = laborRate1.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            laborRate1.sendKeys(Keys.BACK_SPACE);
    }

    @Loggable
    public void clearLaborRate2Textbox(){
        //Use BACK_SPACE to clear value since
        // - clear() will not trigger labor rate change event on Chrome
        int length = laborRate2.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            laborRate2.sendKeys(Keys.BACK_SPACE);
    }

    @Loggable
    public void clearLaborRate3Textbox(){
        //Use BACK_SPACE to clear value since
        // - clear() will not trigger labor rate change event on Chrome
        int length = laborRate3.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            laborRate3.sendKeys(Keys.BACK_SPACE);
    }

    @Loggable
    public void clickAddIdbcListButton() {
        this.click(addIdbcButton);
        // Waiting for IDBC list dialog visible
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='"+ADD_IDBC_BTN+"'][contains(@style, 'display: block')]")));
    }

    @Loggable
    public void enterIdbcAmount(String idbc, String amount) {
        WebElement idbcAmount = getIdbcAmountElement(idbc);
        this.click(getIDBCCheckboxElementInDialog(idbc));
        // Use BACK_SPACE to clear value since
        // - clear() and ctrl+a+delete is not working on iPad Safari if element has default value
        int length = idbcAmount.getAttribute("value").length();
        for (int i = 0; i < length; i++)
            idbcAmount.sendKeys(Keys.BACK_SPACE);
        //Set value
        this.sendKeys(idbcAmount, amount);
    }

    @Loggable
    public String getIdbcValue(String idbc) {
        return this.getAttributeValue(getIDBCValueId(idbc), GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public void clickAddIdbcButton() { this.click(idbcPopUpSubmitButton); }

    @Loggable
    public void clickIDBCCheckbox(String idbc) {
        this.click(getIDBCCheckboxElementInLaborRate(idbc));
    }

    @Loggable
    public void clickIDBCCheckboxinDialog(String idbc){
        this.click(getIDBCCheckboxElementInDialog(idbc));
    }

    @Loggable
    public void clickDeleteButton(){ this.click(delete); }

    @Loggable
    public String getLabourRate1(){ return this.getAttributeValue(By.id(ID_LABOR_RATE1), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getLabourRate2(){
        return this.getAttributeValue(By.id(ID_LABOR_RATE2), GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getLabourRate3(){
        return this.getAttributeValue(By.id(ID_LABOR_RATE3), GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getPaintRate(){
        return this.getAttributeValue(By.id(ID_PAINT_RATE), GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getContracts(){
        return this.getAttributeValue(NAME_CONTRACTS_VALUE, GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public String getPaintMethods(){
        return this.getAttributeValue(NAME_PAINT_METHOD_VALUE, GET_ATTRIBUTE_VALUE);
    }

    private WebElement getIdbcAmountElement(String idbc) {
        return new WebDriverWait(webDriver, 5).until(ExpectedConditions.presenceOfElementLocated(By.id("idbc-" + idbc + "-amount")));
    }

    private By getIDBCValueId(String idbc) {
        return By.id("root.task.classInput.IDBlockCodes.IDBlockCode" + idbc + ".entry.value");
    }

    private WebElement getIDBCCheckboxElementInLaborRate(String idbc) {
        return new WebDriverWait(webDriver, 5).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for=\"root.task.classInput.IDBlockCodes.IDBlockCode" + idbc + ".iDBlockCode\"]")));
    }

    private WebElement getIDBCCheckboxElementInDialog(String idbc){
        return new WebDriverWait(webDriver, 5).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label[for=\"idbc-"+idbc+"-checkbox\"]")));
    }

    @Loggable
    public String getIdbcValue(int num){
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(addedIdbcValue.get(num)));
        return addedIdbcValue.get(num).getAttribute(GET_ATTRIBUTE_VALUE);
    }

    @Loggable
    public void setIdbcValue(int num, String value){ this.sendKeys(addedIdbcValue.get(num), value); }

    @Loggable
    public String getIdBlockCode(int num){ return idBlockCode.get(num).getText(); }

    @Loggable
    public void selectManufTimePrintCodeByNameDropdown(String name) {
        this.click(manufacturerTimePrintCode);
        this.sendKeys(inputManufTimePrintCode, name);
        inputManufTimePrintCode.sendKeys(Keys.ENTER);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.textToBe(TEXT_MANUF_PRINT_CODE_VALUE, name));
    }

    @Loggable
    public By getIdbcLabelId(String idbcValue){
        return By.id("root.task.classInput.IDBlockCodes.IDBlockCode"+idbcValue+".iDBlockCode");
    }

    @Loggable
    public String getWarningMessage() {
        return warningMessage.getText();
    }

    @Loggable
    public Boolean isSelectPartnershipEnable(){ return inputPartnership.isEnabled(); }
}
