package pageobjects.processstep.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import utils.log.Loggable;

public class ProcessStepJPPO extends ProcessStepPO {
    //Navigation menu on the left
    @FindBy(css = "#toDoListItem_ClaimInfoJP a")
    private WebElement claimInfo;
    @FindBy(css = "#toDoListItem_LaborRateJP a")
    private WebElement labourRates;
    @FindBy(css = "#toDoListItem_AttachmentsJP a")
    private WebElement attachments;
    @FindBy(css = "#toDoListItem_DamageCaptureJP a")
    private WebElement damageCapture;
    @FindBy(css = "#toDoListItem_ModifySparePartsJP a")
    private WebElement modifySpareParts;
    @FindBy(css = "#toDoListItem_CalculationOptionsJP a")
    private WebElement calculationOptions;
    @FindBy(css = "#toDoListItem_CalculationsJP a")
    private WebElement calculations;
    @FindBy(css = "#toDoListItem_CaseInfoJP a")
    private WebElement caseInfo;
    @FindBy(css = "#toDoListItem_CompareJP a")
    private WebElement compare;


    public ProcessStepJPPO() {
        super();
    }

    public ProcessStepJPPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickClaimInfoTab() {
        openCollapsedMenu();
        this.click(claimInfo);
    }

    @Loggable
    public void clickLabourRatesTab() {
        openCollapsedMenu();
        this.click(labourRates);
        this.waitForElementPresent(By.id(LaborRatesPO.ADD_IDBC_BTN));
    }

    @Loggable
    public void clickAttachmentsTab() {
        openCollapsedMenu();
        this.click(attachments);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickDamageCaptureTab(){
        openCollapsedMenu();
        this.click(damageCapture);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.attributeContains(QAPTER_CONTENT,"class", "integrated-mode"));
    }

    @Loggable
    public void clickModifySparePartsTab(){
        openCollapsedMenu();
        this.click(modifySpareParts);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickCalculationsTab(){
        openCollapsedMenu();
        this.click(calculations);
        waitForElementInvisible(LOADING_CIRCLE);
        this.waitForElementPresent(By.cssSelector(ReportsPO.CSS_CUSTOMIZE_BTN));
    }

    @Loggable
    public void clickCaseInfoTab(){
        openCollapsedMenu();
        this.click(caseInfo);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickCompareTab(){
        openCollapsedMenu();
        this.click(compare);
        waitForElementInvisible(LOADING_CIRCLE);
    }
}
