package cases.SG;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.CaseHistoryPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import steps.CreateNewCaseSG;
import steps.Login;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CaseHistorySGTest extends TestBase {
    private ProcessStepSGPO processStepSGPO;
    private ReportsPO reportsPO;
    private CaseHistoryPO caseHistoryPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepSGPO = (ProcessStepSGPO)context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        caseHistoryPO = (CaseHistoryPO)context.getBean("CaseHistoryPO");
        caseHistoryPO.setWebDriver(getDriver());
    }

    @Test(description = "Show case log in case history")
    public void caseHistory() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString("bmw320_vehicle"));

        //Case History
        processStepSGPO.clickCaseHistorySG();
        testCase.get().log(Status.INFO, "Switch to Case History page");
        Assert.assertTrue(caseHistoryPO.getEventNumber()>0);
        testCase.get().log(Status.PASS, "There are some log in the list (log number: " + caseHistoryPO.getEventNumber() +")");
    }

    @Test(description = "Add public comment")
    public void addPublicComment(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("bmw320_vehicle"));

        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select Manufacturer partnership value");

        //Case History
        processStepSGPO.clickCaseHistorySG();
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

    @Test(description = "Add private comment")
    public void addPrivateComment(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("bmw320_vehicle"));

        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select Manufacturer partnership value");

        //Case History
        processStepSGPO.clickCaseHistorySG();
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
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithPartnership(testData.getString("bmw320_vehicle"));

        //Go to Case History
        processStepSGPO.clickCaseHistorySG();
        testCase.get().log(Status.INFO, "Switch to Case History page");

        //Assert Business Status value of Case Data
        Assert.assertEquals(caseHistoryPO.getBusinessStatus(), testData.getString("status_Created"));
        testCase.get().log(Status.PASS,  "Business status displayed on Case Data is " + testData.getString("status_Created"));
    }
}
