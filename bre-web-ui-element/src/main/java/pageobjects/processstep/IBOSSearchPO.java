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

import java.util.HashMap;
import java.util.Map;

public class IBOSSearchPO extends PageObject {

    @FindBy(id = "select-field-id-root.task.IBOSSocket.companyList")
    private WebElement insuranceCompany;
    @FindBy(id = "root.task.IBOSSocket.companyList")
    private  WebElement inputInsuranceCompany;

    @FindBy(id = "root.task.IBOSSocket.plateNumber")
    private WebElement plateNumber;

    @FindBy(id = "root.task.IBOSSocket.taxId")
    private WebElement taxId;

    @FindBy(xpath = "id(\"CaseSearchPopup\")/div/div/div[3]/button[2]")
    private WebElement submitButton;

    @FindBy(xpath = "id(\"caseIbos-row0\")/td[1]")
    private WebElement firstSearchResult;

    public IBOSSearchPO() {
        super();
    }

    public IBOSSearchPO(WebDriver driver) {
        super(driver);
    }

    @Loggable
    public void selectInsuranceCompany(String insuranceCompanyName){
        this.click(insuranceCompany);
        this.sendKeys(inputInsuranceCompany, insuranceCompanyName);
        inputInsuranceCompany.sendKeys(Keys.ENTER);
    }

    @Loggable
    public void enterPlateNumber(String textPlateNumber){ this.sendKeys(plateNumber, textPlateNumber); }

    @Loggable
    public void enterTaxId(String textTaxId){ this.sendKeys(taxId, textTaxId); }

    @Loggable
    public void clickSearchButton(){ this.click(submitButton); }

    @Loggable
    public Map<String, String> getTheFirstSearchResult(){
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.visibilityOf(firstSearchResult));
        Map<String, String> result = new HashMap<>();
        result.put("id", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[1]")).getText());
        result.put("WIP", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[2]")).getText());
        result.put("contactMobile", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[3]")).getText());
        result.put("contactPerson", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[4]")).getText());
        result.put("coverageTypeCode", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[5]")).getText());
        result.put("damagedObjectSerialNo", this.firstSearchResult.findElement(By.xpath("id(\"caseIbos-row0\")/td[6]")).getText());

        return result;
    }

    @Loggable
    public void chooseTheFirstSearchResult(){
        this.click(firstSearchResult);
        this.click(submitButton);
    }
}
