package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.log.Loggable;

public class PreIntakePO extends PageObject {
    protected static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");

    @FindBy(id = "root.task.claimNumber")
    private WebElement claimNumber;
    @FindBy(id = "root.task.basicClaimData.vehicle.vehicleAdmin.plateNumber-plateNumber")
    private WebElement plateNumberKR;
    @FindBy(id = "root.task.basicClaimData.vehicle.vehicleAdmin.plateNumber")
    private WebElement vehicleRegistrationNumber;
    @FindBy(id = "submitButton")
    private WebElement createNewCase;
    @FindBy(id = "root.task.displayName")
    private WebElement repairerReferenceNumber;
    @FindBy(id = "select-field-id-root.task.caseMemberByRole.workProvider.companyName")
    private WebElement company;

    // SG Control Question
    @FindBy(id = "select-field-id-survey.questions.answer1")
    private WebElement controlQuestionAnswerSG;
    @FindBy(id = "react-select-survey.questions.answer1-option-2")
    private WebElement answerNo;

    public PreIntakePO() {
        super();
    }

    public PreIntakePO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void enterClaimNumberTextbox(String textClaimNumber) {
        this.sendKeys(claimNumber, textClaimNumber);
    }

    @Loggable
    public boolean isRepairerReferenceNumberEnable(){ return repairerReferenceNumber.isEnabled(); }

    @Loggable
    public void enterRepairerReferenceNumberTextbox(String textClaimNumber) {
        this.sendKeys(repairerReferenceNumber, textClaimNumber);
    }

    @Loggable
    public void enterPlateNumberKRTextbox(String textPlateNumber) {
        this.sendKeys(plateNumberKR, textPlateNumber);
    }

    @Loggable
    public void enterVehicleRegistrationNumberTextbox(String textPlateNumber) { this.sendKeys(vehicleRegistrationNumber, textPlateNumber); }

    @Loggable
    public void clickCreateNewCaseButton() {
        this.click(createNewCase);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void selectCompany(String value){
        this.click(company);
        webDriver.findElement(By.cssSelector("[id*=\"-option-"+value+"\"]")).click();
    }

    @Loggable
    public void setControlQuestionAnswer(){
        this.click(controlQuestionAnswerSG);
        this.click(answerNo);
    }

}
