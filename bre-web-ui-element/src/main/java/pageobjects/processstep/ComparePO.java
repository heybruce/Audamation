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

public class ComparePO extends PageObject {
    private static final String ID_MODIFICATION_ASIDE = "ModificationAside";

    //List
    @FindBy(name = "base")
    private List<WebElement> baseRadioBtn;
    @FindBy(name = "compare")
    private List<WebElement> compareCheckbox;
    @FindBy(name = "userId")
    private List<WebElement> userId;
    @FindBy(name = "grandTotalWithTax")
    private List<WebElement> grandTotalWVat;
    @FindBy(name = "FCRepTot")
    private List<WebElement> repairTotal;
    @FindBy(name = "FCPartTot")
    private List<WebElement> partsTotal;
    @FindBy(name = "FCLaborTot")
    private List<WebElement> labourTotal;
    @FindBy(name = "FCPaintTotOverAll")
    private List<WebElement> paintTotal;
    @FindBy(name = "FCAdditionalCostTot")
    private List<WebElement> additionalCost;

    @FindBy(id="root.task.newModifications.comparison")
    private WebElement btnCompare;

    @FindBy(xpath = "id(\"newmodifications-legend\")/a[1]")
    private WebElement showResultLink;
    @FindBy(css=".calculations-to-compare")
    private WebElement calculationToCompare;
    private static final By comparisonTable = By.id("comparisonTable");

    // Compare result
    @FindBy(id="ConfigurableTabs-tab-Summary Changes")
    private WebElement summaryTab;
    @FindBy(xpath = ".//td[@class=\"numberColumn differsRecord\"]")
    private List<WebElement> differsRecord;

    //Customize
    @FindBy(id="table-customize-header-container-BRE-Compare-root-task-newModifications-calculationList")
    private WebElement btnCustomizeInCompare;
    @FindBy(css=".table-customize-component-row")
    private List<WebElement> listOfCustomizeCheckedbox;
    @FindBy(css=".table-customize-component-save")
    private WebElement btnApplyInCompare;

    public ComparePO() {
        super();
    }

    public ComparePO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickCompareButton(){
        this.click(btnCompare);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.numberOfElementsToBeMoreThan(comparisonTable, 0));
    }

    @Loggable
    public String getUserId(int row){ return this.getText(userId.get(row)); }

    @Loggable
    public String getRepTotal(int row){ return this.getText(repairTotal.get(row)); }

    @Loggable
    public String getPartsTotal(int row){ return this.getText(partsTotal.get(row)); }

    @Loggable
    public String getLaborTotal(int row){ return this.getText(labourTotal.get(row)); }

    @Loggable
    public String getPaintTotal(int row){ return this.getText(paintTotal.get(row)); }

    @Loggable
    public String getAdditionalCost(int row){ return this.getText(additionalCost.get(row)); }

    @Loggable
    public String getGrandTotalWithVat(int row){ return this.getText(grandTotalWVat.get(row)); }

    // New Version - choose 2 or more calculation
    @Loggable
    public void chooseCalculation(int row) {
        WebElement checkbox = new WebDriverWait(webDriver, 5).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".z-row[index=\"" + row + "\"] label")));
        this.click(checkbox);
    }

    // Old Version - choose base and compare calculation
    @Loggable
    public void clickBaseRadioBtn(int row){ this.click(baseRadioBtn.get(row)); }

    @Loggable
    public void clickCompareCheckbox(int row){
        this.click(compareCheckbox.get(row).findElement(By.cssSelector("label")));
    }

    @Loggable
    public void showCompareResult(){
        this.click(showResultLink);
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='"+ID_MODIFICATION_ASIDE+"'][contains(@style, 'display: block')]")));
    }

    @Loggable
    public boolean isValueExistingInDiffersRecord(String value) {
        value = value.replace(".00", "");
        for (WebElement e : differsRecord) {
            if (e.getText().replace(".00","").equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Loggable
    public void clickCustomizeBtn(){ this.click(btnCustomizeInCompare); }

    @Loggable
    public void checkAllCustomizeCheckedbox(){
        for(WebElement compareCustomize: listOfCustomizeCheckedbox) {
            if(!compareCustomize.getAttribute("class").contains("z-selected")){
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", compareCustomize);
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", compareCustomize);
            }
        }
    }

    @Loggable
    public void clickBtnApply(){
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", btnApplyInCompare);
    }
}
