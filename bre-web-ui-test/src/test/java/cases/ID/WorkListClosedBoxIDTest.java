package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkListClosedBoxIDTest extends TestBase {
    private WorkListGridPO workListGridPO;
    private WorkListGridClosedPO workListGridClosedPO;
    private ClaimDetailsPO claimDetailsPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) { testData = UtilitiesManager.setPropertiesFile(dataFile); }

    @BeforeMethod
    public void methodSetup() {
        claimDetailsPO = (ClaimDetailsPO) context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver((getDriver()));
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        workListGridClosedPO = (WorkListGridClosedPO)context.getBean("WorkListGridClosedPO");
        workListGridClosedPO.setWebDriver(getDriver());
    }

    @Test(description = "Reopen the closed claim from work list")
    public void reopenTaskForClosedClaim(){
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String displayName = claimDetailsPO.getRepairReferenceNumber();
        testCase.get().log(Status.INFO, "Display Name: " + displayName);

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");

        workListGridClosedPO.sortCreationDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.reopenTheTask(displayName);
        workListGridClosedPO.sortCreationDate();
        Assert.assertFalse(workListGridClosedPO.isRepairReferenceNumberExist(displayName));
        testCase.get().log(Status.PASS, "The reopen claim does not exist in Close box");

        workListGridPO.clickCustomOpenTab();
        testCase.get().log(Status.INFO, "Switch to Open box");

        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isRepairReferenceNumberExist(displayName));
        testCase.get().log(Status.PASS, "The reopen claim exists in Open box");
    }

    @Test(description = "Change displayed columns of work list")
    public void changeDisplayedColumnsOfWorkListCloseBox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim Number: " + claimNumber);

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");

        //Un-select first checked item in Customize Menu to remove it from work list column
        String uncheckedColumnName = workListGridClosedPO.clickFirstCheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first checked item on customize menu");
        Assert.assertFalse(workListGridClosedPO.isColumnDisplayedByName(uncheckedColumnName));
        testCase.get().log(Status.PASS, uncheckedColumnName + " column is removed from work list");

        //Select first unchecked item in Customize Menu to add it into work list column
        String checkedColumnName = workListGridClosedPO.clickFirstUncheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first unchecked item on customize menu");
        Assert.assertTrue(workListGridClosedPO.isColumnDisplayedByName(checkedColumnName));
        testCase.get().log(Status.PASS, checkedColumnName + " column is added to work list");
    }

    @Test(description = "Search claim by number")
    public void searchCaseByClaimNumberInCloseBox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim Number: " + claimNumber);

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");

        //Search case by claim number
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.searchByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Search case by claim number");

        Assert.assertTrue(workListGridClosedPO.isClaimNumberExist(claimNumber));
        Assert.assertEquals(workListGridClosedPO.getCurrentNumberOfClaims(), 1);
        testCase.get().log(Status.PASS, claimNumber + " is found in search result");
    }
}
