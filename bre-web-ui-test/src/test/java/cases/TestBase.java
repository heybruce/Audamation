package cases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.UtilitiesManager;
import utils.extentreports.ExtentManager;
import utils.extentreports.ExtentTestManager;
import utils.log.LogWriter;
import utils.log.Loggable;
import utils.webdrivers.OptionManager;
import utils.webdrivers.WebDriverFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class TestBase {

    //Extent report
    public static ThreadLocal<ExtentTest> testSection = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> testCase = new ThreadLocal<>();
    public static final boolean RUN_ON_GRID = Boolean.valueOf(System.getProperty("runOnGrid"));
    public static final boolean RUN_ON_DOCKER = Boolean.valueOf(System.getProperty("runOnDocker"));
    public static final String SELENIUM_HUB_URL = System.getProperty("seleniumGridHub");

    public static final NtlmPasswordAuthentication SMB_AUTH =
            new NtlmPasswordAuthentication(null, "bruce_liao", "12345678");

    // Bean Factory
    protected ApplicationContext context =
            new ClassPathXmlApplicationContext("spring-PlatformUITesting.xml");

    protected static Configuration testData;
    protected static Configuration vehicleElementData;
    protected static ITestContext testContext;

    protected String downloadPath;
    protected SmbFile remotePath;
    private static AtomicBoolean isBrowserInfoSet = new AtomicBoolean(false);

    @BeforeSuite
    public void beforeSuite() {
    }

    @BeforeTest
    public void beforeTest(ITestContext context) {
        try {
            String browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
            String fileName = System.getProperty("user.dir") + "/target/extentreports/"
                    + context.getSuite().getName() + "/" + context.getCurrentXmlTest().getName()
                    + "-" + browser + "-" + UtilitiesManager.getCurrentUnixTime() +".html";

            ExtentManager.createExtentReports(fileName, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public synchronized void beforeClass(ITestContext context) {
        try {
            String testName = context.getCurrentXmlTest().getName();
            ExtentReports extentReports = ExtentManager.getExtentReports(testName);
            String testTitle = getClass().getSimpleName();
            ExtentTest parent = ExtentTestManager.createTest(extentReports, testTitle);
            testSection.set(parent);
            testContext = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public synchronized void beforeMethod (Method method, ITestContext context) {
        try {
            WebDriverFactory.setDriver(method, context);

            //prevent setting multiple browser info
            if (!isBrowserInfoSet.getAndSet(true)) {
                String testName = context.getCurrentXmlTest().getName();
                ExtentReports extentReports = ExtentManager.getExtentReports(testName);
                ExtentManager.setBrowserInfo(extentReports);
            }

            //Selenium grid
            if(RUN_ON_GRID) {
                //Enable Remote Web Driver to upload files from local machine
                ((RemoteWebDriver) getDriver()).setFileDetector(new LocalFileDetector());
                String sessionID = ((RemoteWebDriver) getDriver()).getSessionId().toString();
                LogWriter.write(getClass(), Loggable.Level.DEBUG, "Session ID: " + sessionID);
            }

            //Extent report
            //Create nodes for each test method in class
            ExtentTest child = testSection.get().createNode(method.getName());
            testCase.set(child);
            String testTitle = getClass().getSimpleName();
            testCase.get().assignCategory(testTitle);

            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");
            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "==== " + StringUtils.center("TestMethod ["+ method.getName() + "] started ", 50) + " ====");
            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public synchronized void afterMethod(Method method, ITestContext context, ITestResult result) {
        try {
            WebDriverFactory.getDriver().manage().deleteAllCookies();
            String device = context.getCurrentXmlTest().getLocalParameters().get("device");
            if(device.equalsIgnoreCase("iPad")){
                AppiumDriver driver =(AppiumDriver)WebDriverFactory.getDriver();
                Iterator i = driver.getWindowHandles().iterator();
                while (i.hasNext()){
                    String windowHandle = (String) i.next();
                    driver.switchTo().window(windowHandle);
                    driver.close();
                }
            }
            WebDriverFactory.getDriver().quit();
            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");
            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "==== " + StringUtils.center("TestMethod ["+ method.getName() + "] completed ", 50) + " ====");
            LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");

            if (remotePath != null && remotePath.exists())
                remotePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void afterTest(ITestContext context) {
        try {
            String testName = context.getCurrentXmlTest().getName();
            ExtentManager.getExtentReports(testName).flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void afterSuite(ITestContext context) {
        LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");
        LogWriter.write(getClass(), Loggable.Level.DEBUG,  "==== " + StringUtils.center("TestSuit ["+ context.getName() + "] completed ", 50) + " ====");
        LogWriter.write(getClass(), Loggable.Level.DEBUG,  "============================================================");
    }

    public boolean isElementPresent(By by) {
        try {
            WebDriverFactory.getDriver().findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    //For those element which disabled value keep in class attribute
    public boolean isElementEnabled(By by){
        try {
            boolean isEnable;
            isEnable = WebDriverFactory.getDriver().findElement(by).getAttribute("class").contains("disabled");
            return isEnable;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            WebDriverFactory.getDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean isTextPresent(String text) {
        try {
            boolean isPresent = WebDriverFactory.getDriver().getPageSource().contains(text);
            return isPresent;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<>(WebDriverFactory.getDriver())
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        WebElement webElement = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver webDriver) {
                return WebDriverFactory.getDriver().findElement(locator);
            }
        });
        return webElement;
    }

    public Callable<Boolean> isFileExisted(File downloadFile) {
        return new Callable<Boolean>() {
            public Boolean call() {
                return downloadFile.exists() && downloadFile.length()>0;
            }
        };
    }

    public Callable<Boolean> isSmbFileExisted(SmbFile smbFile) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return smbFile.exists() && smbFile.length() > 0;
            }
        };
    }

    protected void execCompatibilitiesScript() {
        try {
            String js = FileUtils.readFileToString(new File(this.getClass().getClassLoader()
                .getResource("SCRIPT_MOBILE_DEVICE_COMPATIBILITIES.js").toURI()), "UTF-8");
            ((JavascriptExecutor) WebDriverFactory.getDriver()).executeScript(js);
        } catch(Exception e) {
            LogWriter.write(this.getClass(), Loggable.Level.ERROR, e.toString());
        }
    }

    public boolean isMobileDevice(){
        return getDeviceName().equalsIgnoreCase("Android") || getDeviceName().equalsIgnoreCase("iPad");
    }

    public String getDeviceName(){
        return testContext.getCurrentXmlTest().getLocalParameters().get("device");
    }

    public String getCountry(){
        return testContext.getCurrentXmlTest().getLocalParameters().get("country");
    }

    public void setupDownloadPath(Method method) throws IOException {
        downloadPath = OptionManager.configProp.getString("smbPath") + testContext.getAttribute(method.getName() + "_downloadFolder");
        remotePath = new SmbFile(downloadPath + "/", SMB_AUTH);
        remotePath.mkdir();
    }
}