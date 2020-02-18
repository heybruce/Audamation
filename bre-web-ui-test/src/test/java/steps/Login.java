package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pageobjects.LoginPO;
import pageobjects.worklistgrid.WorkListGridPO;
import utils.webdrivers.CustomDeviceDriver;
import utils.xpath.XPathNodeBuilder;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class Login extends TestBase {
    public void LoginBRE(String username, String password){
        //close pop up for translation
        if (isMobileDevice() && getDeviceName().equalsIgnoreCase("Android"))
            closeAndroidNativePopup();

        //login page
        LoginPO loginPO = (LoginPO)context.getBean("LoginPO");
        loginPO.setWebDriver(getDriver());
        loginPO.enterUserName(username);
        loginPO.enterPassword(password);
        if (isMobileDevice() && getDeviceName().equalsIgnoreCase("iPad"))
            // Wait few seconds before click submit to avoid login failed caused by click too fast
            loginPO.waitAndClickSubmit();
        else
            loginPO.clickSubmit();
        //Dashboard page
        WorkListGridPO workListGridPO =(WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        Assert.assertEquals(workListGridPO.getLoggedUsername(), username.toUpperCase());
        //workaround to load script for trigger react event on tablet
        execCompatibilitiesScript();

        //Close pop up for adding QC to home screen
        if (isMobileDevice() && getDeviceName().equalsIgnoreCase("Android"))
            closeAndroidNativePopup();
    }

    private void closeAndroidNativePopup(){
        // switch context to native
        CustomDeviceDriver driver = (CustomDeviceDriver) getDriver();
        driver.switchContextToApp();

        // click allow button
        XPathNodeBuilder nodeBuilder = new XPathNodeBuilder();
        nodeBuilder.addEqualPair(true, XPathNodeBuilder.ATTRIBUTE_RESOURCE_ID, "com.android.chrome:id/infobar_close_button");
        try {
            WebElement closeButton = driver.findElementByXPathBuilder(nodeBuilder);
            driver.clickNativeElement(closeButton);
        }catch (Exception e) {
            testCase.get().log(Status.DEBUG, "No native pop up");
        }
        driver.switchContextToBrowser();
    }
}
