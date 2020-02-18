package cases.KR;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.LoginPO;
import pageobjects.PreIntakePO;
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import pageobjects.standalone.DashboardPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import steps.Login;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class DashboardTest extends TestBase {
    private LoginPO loginPO;
    private DashboardPO dashboardPO;
    private PreIntakePO preIntakePO;
    private ClaimDetailsKRPO claimDetailsKRPO;
    private WorkListGridOpenPO workListGridOpenPO;
    private ProcessStepKRPO processStepKRPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        loginPO = (LoginPO)context.getBean("LoginPO");
        loginPO.setWebDriver(getDriver());
        dashboardPO = (DashboardPO)context.getBean("DashboardPO");
        dashboardPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        claimDetailsKRPO = (ClaimDetailsKRPO)context.getBean("ClaimDetailsKRPO");
        claimDetailsKRPO.setWebDriver(getDriver());
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
    }

    @Test(description = "Multiple login and logout")
    public void loginAndLogout() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Dashboard page
        Assert.assertEquals(workListGridOpenPO.getLoggedUsername(), testData.getString("ins_username").toUpperCase());
        testCase.get().log(Status.PASS, "Login successfully");

        //Logout
        processStepKRPO.clickLogout();
        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.PASS, "Logout successfully");

        //Login again
        loginPO.enterUserName(testData.getString("ins_username"));
        loginPO.enterPassword(testData.getString("password"));
        loginPO.clickSubmit();

        new WebDriverWait(getDriver(), 5).until(ExpectedConditions.presenceOfElementLocated(By.id(DashboardPO.ID_NEW_CASE_BTN)));
        Assert.assertEquals(dashboardPO.getCurrentStepText(), testData.getString("Step_Dashboard"));
        testCase.get().log(Status.PASS, "Login successfully");

        //Logout again
        processStepKRPO.clickLogout();
        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.PASS, "Logout successfully");
    }

    @Test(description = "Create a new case from dashboard")
    public void createNewCaseFromDashboard() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Click create case button from dashboard
        dashboardPO.clickCreateCase();

        //Pre Intake page
        String claimNumber = Long.toString(UtilitiesManager.getCurrentUnixTime());
        preIntakePO.enterClaimNumberTextbox(claimNumber);
        preIntakePO.enterPlateNumberKRTextbox(testData.getString("plate_number"));
        preIntakePO.clickCreateNewCaseButton();
        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.PASS, "New case with claim number " + claimNumber + " created");

        //Claim Details process step
        fluentWait(By.id(ClaimDetailsKRPO.ID_CLAIM_NUMBER));

        //Check claim is in Open box
        processStepKRPO.clickClaimManagerIcon();
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim number " + claimNumber + " is presented in Open box");
        workListGridOpenPO.clickLogout();


        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.INFO, "Logout successfully");
    }

    @Test(description = "User can go to work list page from dashboard")
    public void openWorkListFromDashboard(){
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Go to work list
        dashboardPO.clickAudanet();

        //verification
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(workListGridOpenPO.CLASS_WORKLIST_TOOLBAR));
        testCase.get().log(Status.PASS, "Work list open successfully");
    }
}
