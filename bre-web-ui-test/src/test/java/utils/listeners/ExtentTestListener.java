package utils.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.UtilitiesManager;
import utils.webdrivers.WebDriverFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cases.TestBase.testCase;

public class ExtentTestListener extends TestListenerAdapter {

    Configuration jiraIssueProp;

    @Override
    public synchronized void onStart(ITestContext context) {
        String reportsPath = System.getProperty("user.dir") + "/target/extentreports/"
                + context.getSuite().getName();
        UtilitiesManager.createDirectory(reportsPath);
        jiraIssueProp = UtilitiesManager.setPropertiesFile
                (context.getCurrentXmlTest().getLocalParameters().get("jiraIssueList"));
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        //Update Extent report
        testCase.get().pass(MarkupHelper.createLabel("Test passed", ExtentColor.GREEN));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        try {
            //Capture screenshot then add to html report
            String currentUnixTime = String.valueOf(UtilitiesManager.getCurrentUnixTime());
            String fileName = result.getMethod().getConstructorOrMethod().getName() + "-"
                    + result.getTestContext().getCurrentXmlTest().getLocalParameters().get("browser") + "-"
                    + currentUnixTime + ".png";
            String path = "screenshots/" + fileName;

            File destination = new File(System.getProperty("user.dir") + "/target/extentreports/"
                    + result.getTestContext().getSuite().getName() + "/screenshots/" + fileName);
            //create directory if not exist
            if (!destination.getParentFile().exists()) {
                if (!destination.getParentFile().mkdirs()) {
                    throw new IOException("Created new directory failed.");
                }
            }

            // getScreenshotAs will throw exception if there is an alert popping up.
            try {
                FileUtils.copyFile(((TakesScreenshot) WebDriverFactory.getDriver()).getScreenshotAs(OutputType.FILE), destination);
            } catch (UnhandledAlertException e) {
                // Close alert to take screenshot again for debugging
                WebDriverFactory.getDriver().switchTo().alert().accept();
                FileUtils.copyFile(((TakesScreenshot) WebDriverFactory.getDriver()).getScreenshotAs(OutputType.FILE), destination);
            }
            //add screenshot
            String throwableMsg = result.getThrowable().getMessage().replaceAll("([<>])|([<>])\\\\W/g", " ");
            testCase.get().log(Status.FAIL, throwableMsg, MediaEntityBuilder.createScreenCaptureFromPath(path).build());

            String jiraIssueUrl = jiraIssueProp.getString("jiraUrl");
            String jiraNumbers = jiraIssueProp.getString(result.getMethod().getMethodName());
            if (jiraNumbers != null) {
                List<String> jiraList = new ArrayList<>(Arrays.asList(jiraNumbers.split("[,]")));
                if (jiraList != null) {
                    for (String jiraNumber : jiraList) {
                        if (!jiraNumber.isEmpty()) {
                            //add link to existing jira
                            testCase.get().warning("<a href='" + jiraIssueUrl + jiraNumber.trim() + "'>Related JIRA: " + jiraNumber + "</a>");
                        }
                    }
                }
            }
            else {
                testCase.get().fail(MarkupHelper.createLabel("Test failed", ExtentColor.RED));
            }
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        String throwableMsg = result.getThrowable().getMessage().replaceAll("([<>])|([<>])\\\\W/g", " ");
        testCase.get().skip(result.getThrowable());
        testCase.get().log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));
        testCase.get().log(Status.SKIP, MarkupHelper.createCodeBlock(throwableMsg));
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        testCase.get().log(Status.WARNING, MarkupHelper.createLabel("Test Failed But Within Success Percentage", ExtentColor.YELLOW));
    }
}
