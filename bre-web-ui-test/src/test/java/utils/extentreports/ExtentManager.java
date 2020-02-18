package utils.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.ITestContext;
import utils.UtilitiesManager;
import utils.webdrivers.WebDriverFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static Map<String, ExtentReports> extentReportsMap = new HashMap<>();

    public static synchronized ExtentReports getExtentReports(String testName) {
        return extentReportsMap.get(testName);
    }

    private static synchronized ExtentHtmlReporter createHtmlReporter(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        //Make the charts visible on report open
        htmlReporter.config().setChartVisibilityOnOpen(false);
        htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir") + "/extent-config.xml"));
        return htmlReporter;
    }

    public static synchronized ExtentReports createExtentReports(String fileName, ITestContext context) {
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(createHtmlReporter(fileName));
        setEnvInfo(extent, context);

        String testName = context.getCurrentXmlTest().getName();
        extentReportsMap.put(testName, extent);
        return extent;
    }

    private static synchronized void setEnvInfo(ExtentReports extentReports, ITestContext context) {
        extentReports.setSystemInfo("Name", context.getCurrentXmlTest().getName());
        extentReports.setSystemInfo("Country", context.getCurrentXmlTest().getLocalParameters().get("country"));
        extentReports.setSystemInfo("Env", context.getCurrentXmlTest().getLocalParameters().get("env"));
        extentReports.setSystemInfo("URL", UtilitiesManager.setPropertiesFile(
                context.getCurrentXmlTest().getLocalParameters().get("dataFile")).getString("test_url"));
        extentReports.setSystemInfo("Device", context.getCurrentXmlTest().getLocalParameters().get("device"));
    }

    public static synchronized void setBrowserInfo(ExtentReports extentReports) {
        extentReports.setSystemInfo("Browser", WebDriverFactory.getCapabilities().getBrowserName());
        extentReports.setSystemInfo("Browser Version", WebDriverFactory.getCapabilities().getVersion());
    }
}
