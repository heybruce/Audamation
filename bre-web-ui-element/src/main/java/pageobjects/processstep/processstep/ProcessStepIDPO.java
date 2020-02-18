package pageobjects.processstep.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.ReportsPO;
import utils.log.Loggable;

public class ProcessStepIDPO extends ProcessStepPO {

    //Navigation menu on the left
    @FindBy(css = "#toDoListItem_GeneralDetailsID a")
    private WebElement generalDetailsID;
    @FindBy(css = "#toDoListItem_DamageDetailsID a")
    private WebElement damageDetailsID;
    @FindBy(css = "#toDoListItem_AttachmentsID a")
    private WebElement attachmentsID;
    @FindBy(css = "#toDoListItem_LaborRateID a")
    private WebElement laborRateID;
    @FindBy(css = "#toDoListItem_DamageCaptureID a")
    private WebElement damageCaptureID;
    @FindBy(css = "#toDoListItem_ModifySparePartsID a")
    private WebElement modifySparePartsID;
    @FindBy(css = "#toDoListItem_ReportsID a")
    private WebElement reportsID;
    @FindBy(css = "#toDoListItem_CompareID a")
    private WebElement compareID;
    @FindBy(css = "#toDoListItem_CaseHistoryID a")
    private WebElement caseHistoryID;

    private static final By QAPTER_CONTENT = By.id("process-step-content");

    public ProcessStepIDPO() {
        super();
    }

    public ProcessStepIDPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickGeneralDetailsID(){
        openCollapsedMenu();
        this.click(generalDetailsID); }

    @Loggable
    public void clickDamageDetailsID(){
        openCollapsedMenu();
        this.click(damageDetailsID); }

    @Loggable
    public void clickAttachmentsID(){
        openCollapsedMenu();
        this.click(attachmentsID);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(LOADING_INLINE);
    }

    @Loggable
    public void clickLaborRateID(){
        openCollapsedMenu();
        this.click(laborRateID);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickDamageCaptureID(){
        openCollapsedMenu();
        this.click(damageCaptureID);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.attributeContains(QAPTER_CONTENT,"class", "integrated-mode"));
    }

    @Loggable
    public void clickModifySparePartsID(){
        openCollapsedMenu();
        this.click(modifySparePartsID);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickReportsID(){
        openCollapsedMenu();
        this.click(reportsID);
        waitForElementInvisible(LOADING_CIRCLE);
        this.waitForElementPresent(By.cssSelector(ReportsPO.CSS_CUSTOMIZE_BTN));
    }

    @Loggable
    public void clickCompareID(){
        openCollapsedMenu();
        this.click(compareID);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickCaseHistoryID(){
        openCollapsedMenu();
        this.click(caseHistoryID);
        waitForElementInvisible(LOADING_CIRCLE);
    }
}
