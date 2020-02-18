package pageobjects.processstep.claimdetails;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

public class ClaimDetailsPO extends PageObject {
    //Claim Details
    private static final String ID_DISPLAY_NAME = "root.task.displayName";
    public static final String ID_CLAIM_NUMBER = "root.task.claimNumber";
    public static final String ID_PLATE_NUMBER = "root.task.basicClaimData.vehicle.vehicleAdmin.plateNumber-plateNumber";
    public static final String ID_TAX_NUMBER = "root.task.basicClaimData.repairDetail.repairer.taxNumber";
    public static final String NAME_MANUFACTURER_AXCODE = "root.task.basicClaimData.vehicle.vehicleIdentification.manufacturerCodeAX";
    public static final String NAME_MODEL_AXCODE = "root.task.basicClaimData.vehicle.vehicleIdentification.modelCodeAX";
    public static final String ID_MODEL_AXCODE = "root.task.basicClaimData.vehicle.vehicleIdentification.modelCodeAX";
    public static final String NAME_SUB_MODEL_AXCODE = "root.task.basicClaimData.vehicle.vehicleIdentification.subModelCodeAX";
    public static final String ID_SUB_MODEL_AXCODE = "root.task.basicClaimData.vehicle.vehicleIdentification.subModelCodeAX";
    public static final String ID_MANUFACTURER = "select-field-id-root.task.basicClaimData.vehicle.vehicleIdentification.manufacturerCodeAX";
    public static final String ID_MODEL = "select-field-id-root.task.basicClaimData.vehicle.vehicleIdentification.modelCodeAX";
    public static final String ID_SUB_MODEL = "select-field-id-root.task.basicClaimData.vehicle.vehicleIdentification.subModelCodeAX";
    public static final String ID_VIN = "root.task.basicClaimData.vehicle.vehicleIdentification.VINQuery-VIN";
    public static final By ID_ERROR_NOTIFICATION = By.id("error-pre");

    @FindBy(id = ID_CLAIM_NUMBER)
    private WebElement claimNumber;
    @FindBy(id = "root.task.basicClaimData.accidentData.accidentDateTime")
    private WebElement accidentDate;
    public static final String GET_ATTRIBUTE_VALUE = "value";
    @FindBy(id=ID_DISPLAY_NAME)
    private WebElement repairReferenceNumber;

    //Vehicle Details
    @FindBy(id = ID_VIN)
    private WebElement vin;
    @FindBy(id = "root.task.basicClaimData.vehicle.vehicleIdentification.VINQuery-VINQueryButton")
    private WebElement vinQuery;
    @FindBy(id = ID_MANUFACTURER)
    private WebElement manufacturer;
    @FindBy(id = ID_MODEL)
    private WebElement model;
    @FindBy(id = ID_SUB_MODEL)
    private WebElement subModel;
    @FindBy(name = NAME_MANUFACTURER_AXCODE)
    private WebElement manufacturerCode;
    @FindBy(name = NAME_MODEL_AXCODE)
    private WebElement modelCode;
    @FindBy(name = NAME_SUB_MODEL_AXCODE)
    private WebElement subModelCode;
    @FindBy(id =  NAME_MANUFACTURER_AXCODE)
    private WebElement manufacturerInputField;
    @FindBy(id = NAME_MODEL_AXCODE)
    private WebElement modelInputField;
    @FindBy(id = NAME_SUB_MODEL_AXCODE)
    private WebElement submodelInputField;


    public ClaimDetailsPO() {
        super();
    }

    public ClaimDetailsPO(WebDriver webDriver) {
        super(webDriver);
    }


    @Loggable
    public String getRepairReferenceNumber(){ return this.getAttributeValue(By.id(ID_DISPLAY_NAME), GET_ATTRIBUTE_VALUE);}

    @Loggable
    public void enterRepairReferenceNumber(String claimNumber){ this.sendKeys(repairReferenceNumber, claimNumber); }

    @Loggable
    public String getClaimNumber(){ return this.getAttributeValue(By.id(ID_CLAIM_NUMBER), GET_ATTRIBUTE_VALUE);}

    @Loggable
    public String getPlateNumber(){ return this.getAttributeValue(By.id(ID_PLATE_NUMBER), GET_ATTRIBUTE_VALUE);}

    @Loggable
    public String getTaxNumber(){ return this.getAttributeValue(By.id(ID_TAX_NUMBER), GET_ATTRIBUTE_VALUE);}

    @Loggable
    public String getManufacturer() { return this.getText(manufacturer); }

    @Loggable
    public String getModel() {
        return this.getText(model);
    }

    @Loggable
    public String getSubModel() {
        return this.getText(subModel);
    }

    @Loggable
    public String getManufacturerCode() { return this.getAttributeValue(By.name(NAME_MANUFACTURER_AXCODE), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getModelCode() { return this.getAttributeValue(By.name(NAME_MODEL_AXCODE), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getSubModelCode() { return this.getAttributeValue(By.name(NAME_SUB_MODEL_AXCODE), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public String getVinCode(){ return  this.getAttributeValue(By.id(ID_VIN), GET_ATTRIBUTE_VALUE); }

    @Loggable
    public void enterClaimNumberTextbox(String textClaimNumber) { this.sendKeys(claimNumber, textClaimNumber); }

    @Loggable
    public void enterAccidentDate(String testAccidentDate) { this.sendKeys(accidentDate, testAccidentDate); }

    @Loggable
    public void enterVin(String vinNumber) { this.sendKeys(vin, vinNumber); }

    @Loggable
    public void selectManufacturerByText(String text) {
        this.click(manufacturer);
        webDriver.findElement(getCssOfSelectionByText(text)).click();
    }

    @Loggable
    public void selectManufacturerBySearching(String text){
        this.click(manufacturer);
        this.sendKeys(manufacturerInputField, "["+text+"]");
        manufacturerInputField.sendKeys(Keys.ENTER);
    }

    @Loggable
    public void clickVinQuery() {
        this.click(vinQuery);
        new WebDriverWait(webDriver, 20).until(
                ExpectedConditions.elementToBeClickable(By.id(ClaimDetailsPO.ID_MANUFACTURER)));
    }

    @Loggable
    public void selectModelByText(String text) {
        waitForElementPresent(By.id(ID_MODEL_AXCODE));
        this.click(model);
        webDriver.findElement(getCssOfSelectionByText(text)).click();
    }

    @Loggable
    public void selectModelBySearching(String text){
        waitForElementEnabled(By.id(ID_MODEL_AXCODE));
        this.click(model);
        this.sendKeys(modelInputField, "["+text+"]");
        modelInputField.sendKeys(Keys.ENTER);
    }

    @Loggable
    public void selectSubmodelByText(String text) {
        waitForElementPresent(By.id(ID_SUB_MODEL_AXCODE));
        this.click(subModel);
        webDriver.findElement(getCssOfSelectionByText(text)).click();
    }

    @Loggable
    public void selectSubmodelBySearching(String text){
        waitForElementEnabled(By.id(ID_SUB_MODEL_AXCODE));
        this.click(subModel);
        this.sendKeys(submodelInputField, text);
        submodelInputField.sendKeys(Keys.ENTER);
    }

    private By getCssOfSelectionByText(String selection){
        return By.cssSelector("[aria-label*=\""+selection+"\"]");
    }

    @Loggable
    public boolean isVinQueryEnable(){ return vin.isEnabled(); }

    @Loggable
    public boolean isClaimNumberEnable(){ return claimNumber.isEnabled(); }

}
