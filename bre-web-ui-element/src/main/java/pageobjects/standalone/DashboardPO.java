package pageobjects.standalone;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import utils.log.Loggable;

public class DashboardPO extends ProcessStepKRPO {

    @FindBy(css = "div.dashboard-item:nth-child(1)")
    private WebElement createCase;
    @FindBy(css = "div.dashboard-item:nth-child(2)")
    private WebElement audanet;
    @FindBy(css = "div.dashboard-item:nth-child(3)")
    private WebElement partsInfoChecker;
    @FindBy(css = "div.dashboard-item:nth-child(4)")
    private WebElement csa;
    @FindBy(id = ID_NEW_CASE_BTN)
    private WebElement newClaimButton;

    //Loading circle
    public static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");
    public static final String ID_NEW_CASE_BTN = "newCaseBtn";

    public DashboardPO() {
        super();
    }

    public DashboardPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickCreateCase() { this.click(createCase); }

    @Loggable
    public void clickAudanet(){
        this.click(audanet);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickNewClaimButton() {
        this.click(newClaimButton);
    }
}
