package pageobjects.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;

public class ReportsPO extends PageObject {
    //Loading circle
    private static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");
    private static final String CALCULATION_CHECKEDBOX_CSS_LOCATOR = "label[for=\"checked_checkIdN\"]";
    public static final String ID_CALCULATION_ALTERNATIVE = "root.task.calculationOptions.calculateAlternative";
    public static final String CSS_CUSTOMIZE_BTN = ".table-customize-header-container";

    @FindBy(id=ID_CALCULATION_ALTERNATIVE)
    private WebElement btnCalculateAlternative;
    @FindBy(id = "root.actionButtons.printPDFButton")
    private WebElement btnPrintPdfButton;

    @FindBy(css=".claim-identification-item-value")
    private WebElement textClaimNumber;

    //Calculation list
    @FindBy(name = "calculationTitle")
    private List<WebElement> calculationTitle;
    @FindBy(name = "calculationDateTime")
    private List<WebElement> calculationDateTime;
    @FindBy(name = "userId")
    private List<WebElement> userId;
    @FindBy(name = "grandTotalWTaxCombined")
    private List<WebElement> grandTotalWTaxCombined;
    @FindBy(name = "FCRepTot")
    private List<WebElement> repairTotal;
    @FindBy(name = "FCPartTot")
    private List<WebElement> partsTotal;
    @FindBy(name = "FCLaborTot")
    private List<WebElement> labourTotal;
    @FindBy(name = "FCPaintTotOverAll")
    private List<WebElement> paintTotal;
    @FindBy(name = "FCAdditionalCostTot")
    private List<WebElement> additionalCostTotal;
    @FindBy(name="printCalculationButton")
    private List<WebElement> btnPrintCalculation;
    @FindBy(css = ".calculation-report-component-icon")
    private List<WebElement> printReportIconEye;
    @FindBy(name = "grandTotalWithTax")
    private List<WebElement> grandTotalWithTax;
    @FindBy(name = "lumpSum")
    private List<WebElement> lumpSum;
    @FindBy(name = "calculationStatus")
    private List<WebElement> calculationStatus;
    @FindBy(name = "comment")
    private List<WebElement> comment;

    // Calculation Output
    public static final String ID_CALC_OUTPUT = "root.task.calculationList.calculationOutput";
    @FindBy(id=ID_CALC_OUTPUT)
    private WebElement calculationOutput;
    public static final String CLASS_PDF_CONTAINER = ".pdf-container";

    //Customize
    @FindBy(css=CSS_CUSTOMIZE_BTN)
    private WebElement btnCustomizeInCalculations;
    @FindBy(css=".table-customize-component-row")
    private List<WebElement> listOfCustomizeCheckedbox;
    @FindBy(css=".table-customize-component-save")
    private WebElement btnApplyInCalculation;

    public ReportsPO() {
        super();
    }

    public ReportsPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickCalculateAlternative() {
        this.click(btnCalculateAlternative);
        waitForElementInvisible(LOADING_CIRCLE);
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.elementToBeClickable(btnCalculateAlternative));
    }

    @Loggable
    public Boolean isCalculateBtnEnable(){ return btnCalculateAlternative.isEnabled(); }

    @Loggable
    public void clickPrintPdfButton(){ this.click(btnPrintPdfButton); }

    @Loggable
    public void clickCalculationCheckedbox(int row){
        this.click(webDriver.findElement(By.cssSelector(CALCULATION_CHECKEDBOX_CSS_LOCATOR.replace("N", String.valueOf(row)))));
    }

    @Loggable
    public int getCalculationNumber(){ return calculationDateTime.size(); }

    @Loggable
    public String getCalculationTitle(int row){ return this.getText(calculationTitle.get(row)); }

    @Loggable
    public String getCalculationDateTime(int row){ return this.getText(calculationDateTime.get(row)); }

    @Loggable
    public String getUserId(int row){ return this.getText(userId.get(row)); }

    @Loggable
    public String getGrandTotalWTaxCombined(int row){ return this.getText(grandTotalWTaxCombined.get(row)); }

    @Loggable
    public String getRepairTotal(int row){ return this.getText(repairTotal.get(row)); }

    @Loggable
    public String getPartsTotal(int row){ return this.getText(partsTotal.get(row)); }

    @Loggable
    public String getLabourTotal(int row){ return this.getText(labourTotal.get(row)); }

    @Loggable
    public String getPaintTotal(int row){ return this.getText(paintTotal.get(row)); }

    @Loggable
    public String getAdditionalCost(int row){ return this.getText(additionalCostTotal.get(row)); }

    @Loggable
    public String getGrandTotalWithTax(int row){ return this.getText(grandTotalWithTax.get(row)); }

    @Loggable
    public String getLumpSum(int row){
        return lumpSum.get(row).findElement(By.xpath("div/div/input")).getAttribute("value");
    }

    @Loggable
    public Boolean isLumpSumEnable(int row){ return lumpSum.get(row).findElement(By.xpath("div/div/input")).isEnabled(); }

    @Loggable
    public void inputLumpSumValue(int row, String value){ this.sendKeys(lumpSum.get(row).findElement(By.xpath("//*[@id=\"lumpSum_" + row + "\"]")), value);}

    @Loggable
    public String getStatus(int row){ return this.getText(calculationStatus.get(row)); }

    @Loggable
    public String getComment(int row){ return this.getText(comment.get(row)); }

    @Loggable
    public void clickPrintCalculation(int row){ this.click(btnPrintCalculation.get(row)); }

    @Loggable
    public void clickPrintReportIcon(int row){ this.click(printReportIconEye.get(row)); }

    @Loggable
    public String getCalculationOutput() { return this.getText(calculationOutput); }

    @Loggable
    public String getClaimNumber(){ return this.getText(textClaimNumber); }

    @Loggable
    public void clickCustomizeBtn(){ this.click(btnCustomizeInCalculations); }

    @Loggable
    public void checkAllCustomizeCheckedbox(){
        for(WebElement calculationCustomize: listOfCustomizeCheckedbox) {
            if(!calculationCustomize.getAttribute("class").contains("z-selected")){
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", calculationCustomize);
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", calculationCustomize);
            }
        }
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, 0)", "");
    }

    @Loggable
    public void clickBtnApply(){
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", btnApplyInCalculation);
    }
}
