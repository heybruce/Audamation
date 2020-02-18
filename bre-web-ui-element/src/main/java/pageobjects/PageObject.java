package pageobjects;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.log.LogWriter;
import utils.log.Loggable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class PageObject {
    protected WebDriver webDriver;
    private static final int TIME_OUT = 15;

    private WebElement lastElem = null;
    private String lastBorder = null;

    private String scriptGetElementBorder;
    private String scriptUnhighlightElement;

    public PageObject() {
        try {
            scriptGetElementBorder = FileUtils.readFileToString(new File(this.getClass().getClassLoader()
                    .getResource("SCRIPT_GET_ELEMENT_BORDER.js").toURI()), "UTF-8");
            scriptUnhighlightElement = FileUtils.readFileToString(new File(this.getClass().getClassLoader()
                    .getResource("SCRIPT_UNHIGHLIGHT_ELEMENT.js").toURI()), "UTF-8");
            lastElem = null;
        } catch (IOException | URISyntaxException e) {
            LogWriter.write(this.getClass(), Loggable.Level.ERROR, e.toString());
        }
    }

    public PageObject(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
        lastElem = null;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
        lastElem = null;
    }

    protected void waitForElementInvisible(By locator) {
        try{
            // Wait for the element visible
            WebDriverWait wait = new WebDriverWait(webDriver, 8);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            // Wait for the element invisible
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        }catch(Exception e){
            //LogWriter.write(this.getClass(), Loggable.Level.WARN, "Element is not visible/invisible: " + e.toString());
        }
    }

    protected void waitForElementNotDisplay(By locator) {
        // Wait few seconds to check element is not display as expected
        try{
            new WebDriverWait(webDriver, 3).until(ExpectedConditions.visibilityOfElementLocated(locator));
        }catch (TimeoutException e) {
            LogWriter.write(this.getClass(), Loggable.Level.INFO, "Element is not displayed.");
        }
    }

    public void waitForElementEnabled(By locator) {
        try {
            // Wait for the element visible
            WebDriverWait wait = new WebDriverWait(webDriver, 3);
            wait.until((ExpectedCondition<Boolean>) driver -> webDriver.findElement(locator).isEnabled());
        } catch (Exception e) {
            LogWriter.write(this.getClass(), Loggable.Level.WARN,e + " : " + "Timed out waiting for element: " + locator);
        }
    }

    // wait and get the refreshed element to prevent StaleElementReferenceException
    public WebElement waitForElementRefreshedAndVisible(WebElement element) {
        return new WebDriverWait(webDriver, 15).until(
                ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
    }

    public void click(WebElement element) {
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.elementToBeClickable(element));
        highlightElement(element);
        element.click();
    }

    public void sendKeys(WebElement element, String text){
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.visibilityOf(element));
        highlightElement(element);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element){
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    public String getAttributeValue(By locator, String attributeName){
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.presenceOfElementLocated(locator));
        return  webDriver.findElement(locator).getAttribute(attributeName);
    }

    public void sendKeyboards(WebElement element, Keys keyboard){
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.visibilityOf(element));
        highlightElement(element);
        element.sendKeys(keyboard);
    }

    public void waitForElementPresent(By locator) {
        new WebDriverWait(webDriver, TIME_OUT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    void highlightElement(WebElement elem) {
        unhighlightLast();

        // remember the new element
        lastElem = elem;
        lastBorder = (String)(((JavascriptExecutor) webDriver).executeScript(scriptGetElementBorder, elem));
    }

    void unhighlightLast() {
        if (lastElem != null) {
            try {
                // if there already is a highlighted element, unhighlight it
                ((JavascriptExecutor) webDriver).executeScript(scriptUnhighlightElement, lastElem, lastBorder);
            }
            catch (NoSuchElementException e) {
                LogWriter.write(this.getClass(), Loggable.Level.WARN, String.format("Unable to unhighlight element [%s] because it's not displayed on UI anymore.", lastElem));
            }
            catch (StaleElementReferenceException e) {
                LogWriter.write(this.getClass(), Loggable.Level.WARN, String.format("Unable to unhighlight element [%s] because it's refreshed/redrawn in the DOM.", lastElem));
            }
            catch (Exception e) {
                LogWriter.write(this.getClass(), Loggable.Level.ERROR, e.toString());
            }
            finally {
                // element either restored or wasn't valid, nullify in both cases
                lastElem = null;
            }
        }
    }
}
