package utils.listeners;

import org.openqa.selenium.Cookie;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.webdrivers.WebDriverFactory;

import static utils.screenRecorder.DockerVideoRecorder.setEnableVideoRecording;

public class Retry implements IRetryAnalyzer {

    private int count = 0;
    private static int maxTry = 1; //Run the failed test 2 times
    Cookie cookie = null;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < maxTry) {
                count++;
                iTestResult.setStatus(ITestResult.FAILURE);
                setEnableVideoRecording(true);
                return true;
            }
            else {
                iTestResult.setStatus(ITestResult.FAILURE);

                // Stop video recording when case failed
                setEnableVideoRecording(false);

                // Send signal to Zalenium for test failed
                cookie = new Cookie("zaleniumTestPassed", "false");
                WebDriverFactory.getDriver().manage().addCookie(cookie);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);

            // Stop video recording when case succeed
            setEnableVideoRecording(false);

            // Send signal to Zalenium for test success
            cookie = new Cookie("zaleniumTestPassed", "true");
            WebDriverFactory.getDriver().manage().addCookie(cookie);
        }
        return false;
    }

}