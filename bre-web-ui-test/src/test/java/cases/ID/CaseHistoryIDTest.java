package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.CaseHistoryPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CaseHistoryIDTest extends TestBase{
    private ReportsPO reportsPO;
    private ProcessStepIDPO processStepIDPO;
    private CaseHistoryPO caseHistoryPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        caseHistoryPO = (CaseHistoryPO)context.getBean("CaseHistoryPO");
        caseHistoryPO.setWebDriver(getDriver());
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
    }

    @Test(description = "Show case log in case history")
    public void caseHistory() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        //Case History
        processStepIDPO.clickCaseHistoryID();
        testCase.get().log(Status.INFO, "Switch to Case History page");
        Assert.assertTrue(caseHistoryPO.getEventNumber()>0);
        testCase.get().log(Status.PASS, "There are some log in the list (log number: " + caseHistoryPO.getEventNumber() +")");
    }

    @Test(description = "Add public comment from Case History page")
    public void addComment(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Case History
        processStepIDPO.clickCaseHistoryID();
        testCase.get().log(Status.INFO, "Switch to Case History page");

        //Add public comment
        caseHistoryPO.clickBtnComments();
        caseHistoryPO.inputComment("Automation add public comment");
        caseHistoryPO.clickSaveBtn();
        //Comment assertion
        caseHistoryPO.waitForAddedComment();
        Assert.assertEquals(caseHistoryPO.getCommentType(0), "Public");
        Assert.assertEquals(caseHistoryPO.getCommentAuthor(0), testData.getString("rep_username"));
        Assert.assertEquals(caseHistoryPO.getCommentText(0), "Automation add public comment");
        Assert.assertNotNull(caseHistoryPO.getCommentDate(0));
        testCase.get().log(Status.PASS, "Public comment added successfully");
    }

    @Test(description = "Add private comment from Case History page")
    public void addPrivateComment(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Case History
        processStepIDPO.clickCaseHistoryID();
        testCase.get().log(Status.INFO, "Switch to Case History page");

        //Add public comment
        caseHistoryPO.clickBtnComments();
        caseHistoryPO.inputComment("Automation add private comment");
        caseHistoryPO.clickPrivacyCheckedbox();
        caseHistoryPO.clickSaveBtn();
        //Comment assertion
        caseHistoryPO.waitForAddedComment();
        Assert.assertEquals(caseHistoryPO.getCommentType(0), "Private");
        Assert.assertEquals(caseHistoryPO.getCommentAuthor(0), testData.getString("rep_username"));
        Assert.assertEquals(caseHistoryPO.getCommentText(0), "Automation add private comment");
        Assert.assertNotNull(caseHistoryPO.getCommentDate(0));
        testCase.get().log(Status.PASS, "Private comment added successfully");
    }

    @Test(description = "Verify Business Status of Case Data in Case History")
    public void businessStatusOfCaseData() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithPartnership(testData.getString("benzS_vehicle"));

        //Go to Case History
        processStepIDPO.clickCaseHistoryID();
        testCase.get().log(Status.INFO, "Switch to Case History page");

        //Assert Business Status value of Case Data
        Assert.assertEquals(caseHistoryPO.getBusinessStatus(), testData.getString("status_Created"));
        testCase.get().log(Status.PASS,  "Business status displayed on Case Data is " + testData.getString("status_Created"));
    }
}
