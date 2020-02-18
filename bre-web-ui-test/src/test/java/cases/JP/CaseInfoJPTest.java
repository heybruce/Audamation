package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.CaseHistoryPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.ToolBar;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CaseInfoJPTest extends TestBase{
    private ProcessStepJPPO processStepJPPO;
    private CaseHistoryPO caseInfoPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepJPPO = (ProcessStepJPPO)context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        caseInfoPO = (CaseHistoryPO) context.getBean("CaseHistoryPO");
        caseInfoPO.setWebDriver(getDriver());
    }

    @Test(description = "Show case log in Case Info page")
    public void caseInfo() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Switch to Labour Rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Case Info
        processStepJPPO.clickCaseInfoTab();
        testCase.get().log(Status.INFO, "Switch to Case Info page");
        Assert.assertTrue(caseInfoPO.getEventNumber()>0);
        testCase.get().log(Status.PASS, "There are some log in the list (log number: " + caseInfoPO.getEventNumber() +")");
    }

    @Test(description = "Added comment from tool bar will show in Case Info page")
    public void addCommentFromToolbar() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Add a comment form tool bar
        ToolBar toolBar = new ToolBar();
        toolBar.addComment();

        //Verify in CaseIfo page
        processStepJPPO.clickCaseInfoTab();
        Assert.assertTrue(caseInfoPO.getCommentNumber()>0);
        Assert.assertEquals(caseInfoPO.getCommentAuthor(0), testData.getString("ins_username"));
        Assert.assertEquals(caseInfoPO.getCommentText(0), "Automation test - Add comment");
        Assert.assertNotNull(caseInfoPO.getCommentDate(0));
        testCase.get().log(Status.PASS, "Public comment added successfully");
    }

    @Test(description = "Add public comment from Case Info page")
    public void addPublicComment() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Case Info
        processStepJPPO.clickCaseInfoTab();
        testCase.get().log(Status.INFO, "Switch to Case Info page");

        //Add public comment
        caseInfoPO.clickBtnComments();
        caseInfoPO.inputComment("Automation add public comment");
        caseInfoPO.clickSaveBtn();
        //Comment assertion
        caseInfoPO.waitForAddedComment();
        Assert.assertEquals(caseInfoPO.getCommentType(0), "Public");
        Assert.assertEquals(caseInfoPO.getCommentAuthor(0), testData.getString("ins_username"));
        Assert.assertEquals(caseInfoPO.getCommentText(0), "Automation add public comment");
        Assert.assertNotNull(caseInfoPO.getCommentDate(0));
        testCase.get().log(Status.PASS, "Public comment added successfully");
    }

    @Test(description = "Add private comment from Case Info page")
    public void addPrivateComment() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Case Info
        processStepJPPO.clickCaseInfoTab();
        testCase.get().log(Status.INFO, "Switch to Case Info page");

        //Add public comment
        caseInfoPO.clickBtnComments();
        caseInfoPO.inputComment("Automation add private comment");
        caseInfoPO.clickPrivacyCheckedbox();
        caseInfoPO.clickSaveBtn();
        //Comment assertion
        caseInfoPO.waitForAddedComment();
        Assert.assertEquals(caseInfoPO.getCommentType(0), "Private");
        Assert.assertEquals(caseInfoPO.getCommentAuthor(0), testData.getString("ins_username"));
        Assert.assertEquals(caseInfoPO.getCommentText(0), "Automation add private comment");
        Assert.assertNotNull(caseInfoPO.getCommentDate(0));
        testCase.get().log(Status.PASS, "Private comment added successfully");
    }
}
