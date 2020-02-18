package utils.webdrivers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import utils.UtilitiesManager;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static cases.TestBase.*;
import static utils.screenRecorder.DockerVideoRecorder.isEnableVideoRecording;

public class WebDriverFactory {
    private static ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();
    private static OptionManager optionManager = new OptionManager();

    public static synchronized void setDriver(Method method, ITestContext context) {

        String os = context.getCurrentXmlTest().getLocalParameters().get("os");
        String browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
        String device = context.getCurrentXmlTest().getLocalParameters().get("device");
        String mobileLanguage = context.getCurrentXmlTest().getLocalParameters().get("locale").substring(0, 2);
        String mobileLocale = context.getCurrentXmlTest().getLocalParameters().get("locale").substring(3, 5);
        String deviceCoordinates = context.getCurrentXmlTest().getLocalParameters().get("deviceCoordinates");
        String methodName = method.getName();
        String downloadFolder = String.valueOf(UtilitiesManager.getCurrentUnixTime()) + "_" + methodName;

        context.setAttribute(methodName + "_downloadFolder", downloadFolder);
        context.setAttribute(methodName + "_videoPath", downloadFolder + "_" + browser);

        if (browser.equalsIgnoreCase("firefox") && !RUN_ON_GRID) {
            //Run tests on local machine
            switch (UtilitiesManager.getSystem()) {
                case "Windows":
                    System.setProperty("webdriver.gecko.driver", OptionManager.configProp.getString("geckodriverWin"));
                    break;
                case "Mac":
                    System.setProperty("webdriver.gecko.driver", OptionManager.configProp.getString("geckodriverMac"));
                    break;
                case "Linux":
                    System.setProperty("webdriver.gecko.driver", OptionManager.configProp.getString("geckodriverLinux"));
                    break;
            }
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            try {
                webDriverThreadLocal.set(new FirefoxDriver(optionManager.setFirefoxOptions(method, context)));
                webDriverThreadLocal.get().manage().window().maximize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else //Run tests on Selenium Grid
            if (browser.equalsIgnoreCase("firefox") && RUN_ON_GRID) try {
                FirefoxOptions firefoxOptions = optionManager.setFirefoxOptions(method, context);

                //default set to WINDOWS
                firefoxOptions.setCapability("platform", Platform.WINDOWS);

                if (RUN_ON_DOCKER)
                    firefoxOptions.setCapability("platform", Platform.ANY);
                else if (os.equalsIgnoreCase("windows"))
                    firefoxOptions.setCapability("platform", Platform.WINDOWS);
                else if (os.equalsIgnoreCase("mac"))
                    firefoxOptions.setCapability("platform", Platform.MAC);

                //Set test name
                firefoxOptions.setCapability("name", methodName);

                //Set video file name
                firefoxOptions.setCapability("testFileNameTemplate", downloadFolder + "_{browser}");

                //Toggle video recording
                firefoxOptions.setCapability("recordVideo", isEnableVideoRecording());


                webDriverThreadLocal.set(new RemoteWebDriver(new URL(SELENIUM_HUB_URL + "/wd/hub"), firefoxOptions));
                webDriverThreadLocal.get().manage().window().maximize();
            } catch (Exception e) {
                e.printStackTrace();
            }
            else if (browser.equalsIgnoreCase("chrome") && device.equalsIgnoreCase("android")) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("device", "Android");
                capabilities.setCapability("deviceName", "Android");
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("udid", "b24deaa737a8412a");
                capabilities.setCapability("browserName", "Chrome");
                capabilities.setCapability("automationName", "UIAutomator2");
                capabilities.setCapability("language", mobileLanguage);
                capabilities.setCapability("locale", mobileLocale);
                try {
                    CustomDeviceDriver driver = new CustomDeviceDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
                    webDriverThreadLocal.set(driver);
                    //TODO: Workaround to fix high chances hitting ClassCastException when wait for element. AXNASEAN-3642 is filed.
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                } catch (MalformedURLException e) {
                }
            } else if (browser.equalsIgnoreCase("chrome") && !RUN_ON_GRID) {
                //Run tests on local machine
                switch (UtilitiesManager.getSystem()) {
                    case "Windows":
                        System.setProperty("webdriver.chrome.driver", OptionManager.configProp.getString("chromedriverWin"));
                        break;
                    case "Mac":
                        System.setProperty("webdriver.chrome.driver", OptionManager.configProp.getString("chromedriverMac"));
                        break;
                    case "Linux":
                        System.setProperty("webdriver.chrome.driver", OptionManager.configProp.getString("chromedriverLinux"));
                        break;
                }
                try {
                    webDriverThreadLocal.set(new ChromeDriver(optionManager.setChromeOptions(method, context)));
                    webDriverThreadLocal.get().manage().window().maximize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (browser.equalsIgnoreCase("chrome") && RUN_ON_GRID) {
                //Run tests on Selenium Grid
                try {
                    ChromeOptions chromeOptions = optionManager.setChromeOptions(method, context);

                    //Set test name
                    chromeOptions.setCapability("name", methodName);

                    //Set video file name
                    chromeOptions.setCapability("testFileNameTemplate", downloadFolder + "_{browser}");

                    //Toggle video recording
                    chromeOptions.setCapability("recordVideo", isEnableVideoRecording());

                    //Platform default set to ANY
                    if (RUN_ON_DOCKER)
                        chromeOptions.setCapability("platform", Platform.ANY);
                    else if (os.equalsIgnoreCase("mac"))
                        chromeOptions.setCapability("platform", Platform.MAC);
                    else if (os.equalsIgnoreCase("windows"))
                        chromeOptions.setCapability("platform", Platform.WINDOWS);

                    webDriverThreadLocal.set(new RemoteWebDriver(new URL(SELENIUM_HUB_URL + "/wd/hub"), chromeOptions));
                    webDriverThreadLocal.get().manage().window().maximize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (browser.equalsIgnoreCase("safari") && device.equalsIgnoreCase("iPad")) {
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability("platformVersion", "12.3");
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("device", "iPad");
                capabilities.setCapability("deviceName", "iPad");
                capabilities.setCapability("browserName", "Safari");
                capabilities.setCapability("udid", "0c085fcda525e5a199214bcef4dcd62c8ad57f49");
                capabilities.setCapability("xcodeOrgId", "8HAGNV66UG");
                capabilities.setCapability("xcodeSigningId", "iPhone Developer");
                capabilities.setCapability("language", mobileLanguage);
                capabilities.setCapability("locale", mobileLanguage + "_" + mobileLocale.toUpperCase());

                try {
                    CustomDeviceDriver driver = new CustomDeviceDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
                    driver.setCoordinateProperties(deviceCoordinates);
                    webDriverThreadLocal.set(driver);
//                WebDriver driver = new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
//                webDriverThreadLocal.set(driver);
                } catch (MalformedURLException e) {

                }
            }
    }

    public static synchronized WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, 20);
    }

    public static synchronized WebDriver getDriver() {
        return webDriverThreadLocal.get();
    }

    public static synchronized Capabilities getCapabilities() {
        return ((RemoteWebDriver) webDriverThreadLocal.get()).getCapabilities();
    }
}
