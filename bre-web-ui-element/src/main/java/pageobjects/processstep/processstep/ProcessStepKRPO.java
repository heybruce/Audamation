package pageobjects.processstep.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ModifySparePartsPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.worklistgrid.WorkListGridPO;
import utils.log.Loggable;

public class ProcessStepKRPO extends ProcessStepPO {

    @FindBy(xpath = "/html/body/div[1]/div/div[2]/div[2]/div[1]/nav/div/div[1]/div[2]")
    private WebElement currentStep;

    //Icon menu
    @FindBy(xpath = "/html/body/div/div/div[2]/div[2]/div[1]/nav/div/div[1]/div[1]/button")
    private WebElement navigationActions;
    @FindBy(id = "home")
    private WebElement home;
    @FindBy(id = "claimManager")
    private WebElement claimManager;
    @FindBy(id = "settings")
    private WebElement settings;

    //Navigation menu on the left
    @FindBy(css = "#toDoListItem_Claim_Details a")
    private WebElement claimDetails;
    @FindBy(css = "#toDoListItem_Attachments_Repairer a")
    private WebElement attachmentsRepairer;
    @FindBy(css = "#toDoListItem_DamageCapturing a")
    private WebElement damageCapturing;
    @FindBy(css = "#toDoListItem_Modify_spare_parts a")
    private WebElement modifyParts;
    @FindBy(css = "#toDoListItem_Calculations_RC a")
    private WebElement reports;
    @FindBy(css = "#toDoListItem_Compare a")
    private WebElement compare;
    @FindBy(css = "#toDoListItem_Labor_rates_KR a")
    private WebElement labourRatesKR;
    @FindBy(css = "#toDoListItem_Case_History_KR a")
    private WebElement caseHistoryKR;

    public ProcessStepKRPO() {
        super();
    }

    public ProcessStepKRPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickAttachmentsRepairerTab() {
        openCollapsedMenu();
        this.click(attachmentsRepairer);
        waitForElementInvisible(LOADING_CIRCLE);
        // wait for attachment categories loading circle
        waitForElementInvisible(LOADING_INLINE);
        // there are total 4 loading circles for category, wait for all loading circle to be invisible
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.numberOfElementsToBe(LOADING_INLINE,0));
    }

    @Loggable
    public void clickClaimDetailsTab() {
        openCollapsedMenu();
        this.click(claimDetails);
    }


    @Loggable
    public void clickLabourRatesKRTab() {
        openCollapsedMenu();
        this.click(labourRatesKR);
        this.waitForElementPresent(By.id(LaborRatesPO.ADD_IDBC_BTN));
    }

    @Loggable
    public void clickDamageCapturingTab(){
        openCollapsedMenu();
        this.click(damageCapturing);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.attributeContains(QAPTER_CONTENT,"class", "integrated-mode"));
    }

    @Loggable
    public void clickModifySparePartsTab(){
        openCollapsedMenu();
        this.click(modifyParts);
        this.waitForElementPresent(By.id(ModifySparePartsPO.ID_MANURAL_EDITED_PARTS_TABLE));
    }

    @Loggable
    public void clickReportsTab(){
        openCollapsedMenu();
        this.click(reports);
        waitForElementInvisible(LOADING_CIRCLE);
        this.waitForElementPresent(By.cssSelector(ReportsPO.CSS_CUSTOMIZE_BTN));
    }

    @Loggable
    public void clickCompareTab(){
        openCollapsedMenu();
        this.click(compare);
    }

    @Loggable
    public void clickCaseHistoryKRTab(){
        openCollapsedMenu();
        this.click(caseHistoryKR);
        waitForElementInvisible(LOADING_CIRCLE);}

    @Loggable
    public void clickClaimManagerIcon() {
        openCollapsedMenu();
        try {
            this.click(claimManager);
            new WebDriverWait(webDriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(WorkListGridPO.ID_OPEN_TAB)));
        }catch (TimeoutException e) {
            //Retry click
            openCollapsedMenu();
            this.click(claimManager);
        }
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickSettingsIcon() {
        openCollapsedMenu();
        this.click(settings);
    }

    @Loggable
    public void clickNavigationActions() {
        openCollapsedMenu();
        this.click(navigationActions);
    }


}
