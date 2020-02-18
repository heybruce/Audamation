package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.ToolBarPO;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkflowIDTest extends TestBase {
    private WorkListGridPO workListGridPO;
    private ClaimDetailsPO claimDetailsPO;
    private ReportsPO reportsPO;
    private ProcessStepIDPO processStepIDPO;
    private DamageCapturingPO damageCapturingPO;
    private ToolBarPO toolBarPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        claimDetailsPO = (ClaimDetailsPO) context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO) context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO) context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO) context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO) context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
    }

    @Test(description = "Part 0: Insurer send claim to Repairer")
    public void insurerSendClaimToRepairer () {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String repairerOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as Insurer " + insurer);

        //Create a new claim
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        String claimNumber = claimDetailsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(repairerOrg);

        //Open box - check claim is not existing in Open box
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Open_After_Send"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Open_After_Send"));

        //Send the claim from worklist
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.sendTask(claimNumber, repairerOrg);

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box after sent again");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Open_After_Send"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Open_After_Send"));

        //Logout
        workListGridPO.clickLogout();

        //Login as repairer
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Go to inbox
        workListGridPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in repairer's inbox");

        //Merge the claim
        worklistTaskActions.mergeTheTask(claimNumber);

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber),testData.getString("status_Created"));
        testCase.get().log(Status.PASS, "Claim status is \"Created\"");
    }

    @Test(description = "Part 1: Repairer is able to select receiver and send claim")
    public void repairerSubmitCreatedTaskToInsurer() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(insurerOrg);

        //Open box - check claim is not existing in Open box
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));

        //Send the claim from worklist
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.submitTaskWithAllAttachments(claimNumber, insurerOrg);

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box after sent again");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Submitted"));
    }

    @Test(description = "Part 2: Insurer assign submitted task to surveyor")
    public void insurerAssignSubmittedTaskToSurveyor () throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String surveyorOrg = testData.getString("svyr_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(insurer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        int submittedTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Submitted"));
        String claimNumber = "";
        if(submittedTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Submitted")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(insurer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as surveyor " + insurer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(submittedTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Submitted")+ " claim in Open box: " + claimNumber);
        }

        //Assign task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.assignTask(claimNumber, surveyorOrg);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspecting"));

        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        ToolBar toolBar = new ToolBar();
        toolBar.assignTask(surveyorOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspecting"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspecting"));
    }

    @Test(description = "Part 3: Surveyor send Inspecting task to insurer")
    public void surveyorReassessAndSendInspectingTaskToInsurer () throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(surveyor , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        int inspectingTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Inspecting"));
        String claimNumber = "";
        if(inspectingTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Inspecting")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithInspectingStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(surveyor , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(inspectingTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Inspecting")+ " claim in Open box: " + claimNumber);
        }

        //Do damage capture
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        processStepIDPO.clickDamageCaptureID();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");

        //Get the last calculation result
        int theLastCalculation = reportsPO.getCalculationNumber()-1;
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(theLastCalculation);

        //Send Assessment from toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.sendAssessmentToInsurer();

        //Check the claim is not existed in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        worklistTaskActions.sendTask(claimNumber, insurerOrg);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspected"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspected"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        theLastCalculation = reportsPO.getCalculationNumber()-1;
        Map<String, String> theLastCalculationResultAfterMerged = calculationList.getCalculationResult(theLastCalculation);
        Assert.assertEquals(theLastCalculationResultAfterMerged, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The re-assessed task is merged successfully");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspected"));
    }

    @Test(description = "Part 4: Insurer assign inspected task to surveyor")
    public void insurerAssignInspectedTaskToSurveyor () throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String surveyorOrg = testData.getString("svyr_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(insurer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int inspectedTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Inspected"));
        String claimNumber = "";
        if(inspectedTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Inspected")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithInspectedStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(insurer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as surveyor " + insurer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(inspectedTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Inspected")+ " claim in Open box: " + claimNumber);
        }

        //Assign task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.assignTask(claimNumber, surveyorOrg);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspecting"));

        //Do damage capture
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        processStepIDPO.clickDamageCaptureID();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");

        //Get the last calculation result
        int theLastCalculation = reportsPO.getCalculationNumber()-1;
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(theLastCalculation);

        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        ToolBar toolBar = new ToolBar();
        toolBar.assignTask();

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspecting"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        theLastCalculation = reportsPO.getCalculationNumber()-1;
        Map<String, String> theLastCalculationResultAfterMerged = calculationList.getCalculationResult(theLastCalculation);
        Assert.assertEquals(theLastCalculationResultAfterMerged, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The re-assessed task is merged successfully");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspecting"));
    }

    @Test(description = "Part 5: Surveyor send inspecting task to insurer")
    public void surveyorSendInspectingTaskToInsurer() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(surveyor , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int inspectingTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Inspecting"));
        String claimNumber = "";
        if(inspectingTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Inspecting")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithInspectingStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(surveyor , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as surveyor " + surveyor);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(inspectingTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Inspecting")+ " claim in Open box: " + claimNumber);
        }

        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Send Assessment from toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.sendAssessmentToInsurer();

        //Check the claim is not existed in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        worklistTaskActions.sendTask(claimNumber, insurerOrg);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspected"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Inspected"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspected"));
    }


    @Test(description = "Part 6: Insurer send estimate inspected task to repairer")
    public void insurerSendEstimateInspectedTaskToRepairer() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");

        //Login
        Login login = new Login();
        login.LoginBRE(insurer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int inspectedTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Inspected"));
        String claimNumber = "";
        if(inspectedTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Inspected")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithInspectedStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(insurer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as insurer " + insurer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(inspectedTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Inspected")+ " claim in Open box: " + claimNumber);
        }

        //Assign task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.sendEstimate(claimNumber);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Pending_Agreement"));

        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        toolBarPO.clickToolBar();
        Assert.assertTrue(getDriver().findElements(By.id(toolBarPO.ID_SEND_ESTIMATE_BTN)).isEmpty());
        testCase.get().log(Status.PASS, "After send estimate, there is no Send Estimate button in tool bar list");

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Pending_Agreement"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Pending_Agreement"));
    }

    @Test(description = "Part 7: Repairer reject pending agreement task to insurer")
    public void repairerRejectTaskToInsurer() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int pendingAgreementTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Pending_Agreement"));
        String claimNumber = "";
        if(pendingAgreementTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Pending_Agreement")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithPendingAgreementStatus(testData.getString("benzS_vehicle"), false, insurer);
            login.LoginBRE(repairer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as repairer " + repairer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(pendingAgreementTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Pending_Agreement")+ " claim in Open box: " + claimNumber);
        }

        //Assign task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.rejectEstimate(claimNumber);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reject claim is still existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Created"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Created"));

        worklistTaskActions.submitTaskWithAllAttachments(claimNumber);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Submitted"));
    }


    @Test(description = "Part 8: Insurer send estimate submitted task to repairer")
    public void insurerSendEstimateSubmittedTaskToRepairer() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");

        //Login
        Login login = new Login();
        login.LoginBRE(insurer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int inspectedTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Submitted"));
        String claimNumber = "";
        if(inspectedTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Submitted")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("benzS_vehicle"), false);
            login.LoginBRE(insurer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as insurer " + insurer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(inspectedTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Submitted")+ " claim in Open box: " + claimNumber);
        }

        //Assign task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.sendEstimate(claimNumber);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Pending_Agreement"));

        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        toolBarPO.clickToolBar();
        Assert.assertTrue(getDriver().findElements(By.id(toolBarPO.ID_SEND_ESTIMATE_BTN)).isEmpty());
        testCase.get().log(Status.PASS, "After send estimate, there is no Send Estimate button in tool bar list");

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Pending_Agreement"));
        worklistTaskActions.mergeTheTask(claimNumber);
        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Pending_Agreement"));
    }

    @Test(description = "Part 9: Repairer accept pending agreement task")
    public void repairerAcceptTask() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Search a submitted task or create one
        workListGridPO.clickCustomOpenTab();
        int pendingAgreementTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Pending_Agreement"));
        String claimNumber = "";
        if(pendingAgreementTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Pending_Agreement")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithPendingAgreementStatus(testData.getString("benzS_vehicle"), false, insurer);
            login.LoginBRE(repairer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as repairer " + repairer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(pendingAgreementTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Pending_Agreement")+ " claim in Open box: " + claimNumber);
        }

        //Open the claim
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Check calculation button in Report page
        processStepIDPO.clickReportsID();
        Assert.assertTrue(getDriver().findElements(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)).isEmpty());
        testCase.get().log(Status.PASS, "There is no calculation button in Report page");

        //Assign task in worklist
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        worklistTaskActions.acceptEstimate(claimNumber);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The approved claim is still existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Repairing"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Repairing"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - check and merge task
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The approved claim appeared in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Repairing"));
        testCase.get().log(Status.PASS, "The approved claim status is: " + testData.getString("status_Repairing"));
    }

    @Test(description = "Part 10: Repairer submit repairing task to insurer to close")
    public void repairerSendRepairingTaskToInsurer() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Search a repairing task or create one
        workListGridPO.clickCustomOpenTab();
        int repairingTaskRow = workListGridPO.findRowOfTheClaimByBizStatus(testData.getString("status_Repairing"));
        String claimNumber = "";
        if(repairingTaskRow < 0){
            testCase.get().log(Status.INFO, "There is no " + testData.getString("status_Repairing")+ " claim in Open box");
            processStepIDPO.clickLogout();
            //Create a new case
            CreateNewCaseID createNewCase = new CreateNewCaseID();
            claimNumber = createNewCase.createCaseWithRepairingStatus(testData.getString("benzS_vehicle"), false, insurer);
            login.LoginBRE(repairer , testData.getString("password"));
            testCase.get().log(Status.INFO, "Login as repairer " + repairer);
            workListGridPO.clickCustomOpenTab();
        }else{
            claimNumber = workListGridPO.getClaimNumberByRow(repairingTaskRow);
            testCase.get().log(Status.INFO, "Found a " + testData.getString("status_Pending_Agreement")+ " claim in Open box: " + claimNumber);
        }

        //Submit task in worklist
        workListGridPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.submitTaskWithAllAttachments(claimNumber);
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The submit claim (supplementary) is not existed in Open box");

        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The submit claim (supplementary) is existing in Sent box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        worklistTaskActions.mergeTheTask(claimNumber);

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Submitted"));

        //Close task - check claim is existing in Closed box
        worklistTaskActions.closeTheTask(claimNumber);
        workListGridPO.clickClosedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Closed box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "Claim status is not changed: " + testData.getString("status_Submitted"));
    }
}
