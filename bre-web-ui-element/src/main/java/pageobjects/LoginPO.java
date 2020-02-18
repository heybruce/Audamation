package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import utils.log.Loggable;

public class LoginPO extends PageObject{
    protected static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");

    @FindBy(id = "ssousername")
    private WebElement userName;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(how = How.CLASS_NAME, using = "btn-submit")
    private WebElement submit;

    public LoginPO() { super(); }

    public LoginPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void enterUserName(String textName) {
        this.sendKeys(userName, textName);
    }

    @Loggable
    public void enterPassword(String textPassword) {
        this.sendKeys(password, textPassword);
    }

    @Loggable
    public void clickSubmit() {
        this.click(submit);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void waitAndClickSubmit() {
        waitForElementNotDisplay(By.id("msg"));
        this.click(submit);
        waitForElementInvisible(LOADING_CIRCLE);
    }
}
