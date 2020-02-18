package pageobjects.processstep.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.ReportsPO;
import utils.log.Loggable;

public class ProcessStepSGPO extends ProcessStepPO {
    //Navigation menu on the left
    @FindBy(css = "#toDoListItem_GeneralDetailsSG a")
    private WebElement generalDetailsSG;
    @FindBy(css = "#toDoListItem_DamageDetailsSG a")
    private WebElement damageDetailsSG;
    @FindBy(css = "#toDoListItem_AttachmentsSG a")
    private WebElement attachmentsSG;
    @FindBy(css = "#toDoListItem_LaborRateSG a")
    private WebElement laborRateSG;
    @FindBy(css = "#toDoListItem_DamageCaptureSG a")
    private WebElement damageCaptureSG;
    @FindBy(css = "#toDoListItem_ModifySparePartsSG a")
    private WebElement modifySparePartsSG;
    @FindBy(css = "#toDoListItem_ReportsSG a")
    private WebElement reportsSG;
    @FindBy(css = "#toDoListItem_CompareSG a")
    private WebElement compareSG;
    @FindBy(css = "#toDoListItem_CaseHistorySG a")
    private WebElement caseHistorySG;

    public ProcessStepSGPO() {
        super();
    }

    public ProcessStepSGPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickGeneralDetailsSG(){
        openCollapsedMenu();
        this.click(generalDetailsSG);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickDamageDetailsSG(){
        openCollapsedMenu();
        this.click(damageDetailsSG); }

    @Loggable
    public void clickAttachmentsSG(){
        openCollapsedMenu();
        this.click(attachmentsSG);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(LOADING_INLINE);
    }

    @Loggable
    public void clickLaborRateSG(){
        openCollapsedMenu();
        this.click(laborRateSG);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickDamageCaptureSG(){
        openCollapsedMenu();
        this.click(damageCaptureSG);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.attributeContains(QAPTER_CONTENT,"class", "integrated-mode"));
    }

    @Loggable
    public void clickModifySparePartsSG(){
        openCollapsedMenu();
        this.click(modifySparePartsSG);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickReportsSG(){
        openCollapsedMenu();
        this.click(reportsSG);
        waitForElementInvisible(LOADING_CIRCLE);
        this.waitForElementPresent(By.cssSelector(ReportsPO.CSS_CUSTOMIZE_BTN));
    }

    @Loggable
    public void clickCompareSG(){
        openCollapsedMenu();
        this.click(compareSG);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickCaseHistorySG(){
        openCollapsedMenu();
        this.click(caseHistorySG);
        waitForElementInvisible(LOADING_CIRCLE);
    }
}
