package utils.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class InvokedMethodListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
//        if (method.isTestMethod()) {
//            WebDriver driver = WebDriverFactory.getDriver();
//            if (driver != null) {
//                driver.manage().deleteAllCookies();
//                driver.quit();
//            }
//        }
    }
}
