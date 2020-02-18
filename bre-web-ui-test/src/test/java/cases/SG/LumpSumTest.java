package cases.SG;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class LumpSumTest extends TestBase{
    private ProcessStepSGPO processStepSGPO;
    private ReportsPO reportsPO;
    private DamageCapturingPO damageCapturingPO;
    private WorkListGridPO workListGridPO;
    private ClaimDetailsPO claimDetailsPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepSGPO = (ProcessStepSGPO) context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO) context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        claimDetailsPO = (ClaimDetailsPO) context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver(getDriver());
    }

    @Test(description = "Check lump sum settlement in different status with different role")
    public void lumpSumSettlement(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String repairerOrg = testData.getString("rep_orgname");
        String insurerOrg = testData.getString("ins_orgname");
        String surveyorOrg = testData.getString("svyr_orgname");
        String claimStatus = testData.getString("status_Created");

        //Precondition - insurer create a claim with two calculations
        Login login = new Login();
        login.LoginBRE(insurer , testData.getString("password"));
        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString("bmw320_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created by " + insurer);
        //Second calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");
        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");
        //in Created status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.ALL);
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(repairerOrg);
        claimStatus = testData.getString("status_Open_After_Send");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Open after send status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.ALL);

        //Repairer
        //in Created status before and after merged
        claimStatus = testData.getString("status_Created");
        loginAndOpenClaim(repairer, claimNumber, claimStatus);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        //submit task to surveyor
        toolBar.sendTaskTo(surveyorOrg);
        claimStatus = testData.getString("status_Submitted");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Submitted status
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Surveyor
        // in Submitted status before and after merged
        loginAndOpenClaim(surveyor, claimNumber, claimStatus);
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        //add another part to repair and do the second calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do another calculation");
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.LAST);
        //send back to repairer for estimation
        toolBar.sendEstimate();
        claimStatus = testData.getString("status_Pending_Agreement");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Submitted status
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.NONE);

        //Repairer
        //in Pending agreement status before and after merged
        loginAndOpenClaim(repairer, claimNumber, claimStatus);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        //Repairer reject estimate and submit back to insurer
        toolBar.rejectTheTask();
        toolBar.sendTaskTo(insurerOrg);
        claimStatus = testData.getString("status_Submitted");

        //Insurer
        //in Submitted status before and after merged
        loginAndOpenClaim(insurer, claimNumber, claimStatus);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);
        //assign task to surveyor
        toolBar.assignTask(surveyorOrg);
        claimStatus = testData.getString("status_Inspecting");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Submitted status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);

        //Surveyor
        // in Inspecting status before and after merged
        loginAndOpenClaim(surveyor, claimNumber, claimStatus);
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        //add another part to repair and do the second calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do another calculation");
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.LAST);
        //send claim to insurer
        toolBar.sendTaskTo(insurerOrg);
        claimStatus = testData.getString("status_Inspected");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Inspected status
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.LAST);


        //Insurer
        //in Inspected status before and after merged
        loginAndOpenClaim(insurer, claimNumber, claimStatus);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);
        //send back to repairer for estimation
        toolBar.sendEstimate();
        claimStatus = testData.getString("status_Pending_Agreement");
        openClaimAfterSent(claimNumber, claimStatus);
        //in Pending agreement status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);

        //Repairer
        //in Pending agreement status
        loginAndOpenClaim(repairer, claimNumber, claimStatus);
        //Merge the claim
        toolBar.mergeTheTask();
        toolBar.approveTheTask();
        claimStatus = testData.getString("status_Repairing");
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Logout
        workListGridPO.clickLogout();
        //Login as insurer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);
        //Go to inbox - check status and open claim
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
    }

    @Test(description = "Check approval and lump sum value")
    public void markCalculationAsApproved() throws URISyntaxException, InterruptedException {
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Repairer send claim to insurer and merged by insurer (claim status: Submitted)
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        String claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("bmw320_vehicle"), false);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //Do another damage capturing and calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");
        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");
        reportsPO.inputLumpSumValue(1, "1000");
        testCase.get().log(Status.INFO, "Insurer input lump sum value as '1000'");

        //Send back to repairer
        ToolBar toolBar = new ToolBar();
        toolBar.sendEstimate();

        //Repairer login and check the value in Reports page
        String status = testData.getString("status_Pending_Agreement");
        loginAndOpenClaim(repairer, claimNumber, status);
        toolBar.mergeTheTask();
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Assert.assertTrue(reportsPO.getLumpSum(0).isEmpty());
        Assert.assertEquals(reportsPO.getLumpSum(1), "1000");
        testCase.get().log(Status.PASS, "The last lump sum value which added from insurer is merged");

        //Repairer accept estimate and check Report page
        toolBar.approveTheTask();
        status = testData.getString("status_Repairing");
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), status);
        testCase.get().log(Status.PASS, "The sent claim status is: " + status);
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.getStatus(0).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(1).equals("Agreed"));
        testCase.get().log(Status.PASS, "'Status' in calculation list are correct");
        Assert.assertTrue(reportsPO.getComment(0).isEmpty());
        Assert.assertTrue(reportsPO.getComment(1).contains("Agreed amount = 1000" ));
        testCase.get().log(Status.PASS, "'Comment' in calculation list are correct");

        //Do the third damage capturing and calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");
        Assert.assertTrue(reportsPO.getStatus(2).isEmpty());
        Assert.assertTrue(reportsPO.getComment(2).isEmpty());
        testCase.get().log(Status.PASS, "'Status' and 'Comment' are empty in the third calculation");

        toolBar.sendTaskTo(insurerOrg);
        testCase.get().log(Status.INFO, "Send back to Insurer");
        //Repairer Logout
        workListGridPO.clickLogout();
        //Insurer merge the submitted claim
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer");
        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        testCase.get().log(Status.INFO, "Insurer merge the submitted task");

        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.getStatus(0).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(1).equals("Agreed"));
        Assert.assertTrue(reportsPO.getStatus(2).isEmpty());
        testCase.get().log(Status.PASS, "'Status' in calculation list are correct");
        Assert.assertTrue(reportsPO.getComment(0).isEmpty());
        Assert.assertTrue(reportsPO.getComment(1).contains("Agreed amount = 1000" ));
        Assert.assertTrue(reportsPO.getComment(2).isEmpty());
        testCase.get().log(Status.PASS, "'Comment' in calculation list are correct");

        //Insurer do the fourth damage capturing and calculation
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the fourth calculation");
        Assert.assertTrue(reportsPO.getStatus(3).isEmpty());
        Assert.assertTrue(reportsPO.getComment(3).isEmpty());
        testCase.get().log(Status.PASS, "'Status' and 'Comment' are empty in the fourth calculation");
        reportsPO.inputLumpSumValue(3, "4000");
        testCase.get().log(Status.INFO, "Insurer input lump sum value as '4000'");

        //Send back to repairer
        toolBar.sendEstimate();

        //Repairer login and check the value in Reports page
        status = testData.getString("status_Pending_Agreement");
        loginAndOpenClaim(repairer, claimNumber, status);
        toolBar.mergeTheTask();
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Assert.assertTrue(reportsPO.getLumpSum(0).isEmpty());
        Assert.assertEquals(reportsPO.getLumpSum(1), "1000");
        Assert.assertTrue(reportsPO.getLumpSum(2).isEmpty());
        Assert.assertEquals(reportsPO.getLumpSum(3), "4000");
        testCase.get().log(Status.PASS, "The last lump sum value which added from insurer is merged");
        Assert.assertTrue(reportsPO.getStatus(1).equals("Agreed"));
        Assert.assertTrue(reportsPO.getComment(1).contains("Agreed amount = 1000" ));
        testCase.get().log(Status.PASS, "Only 2nd comment and status have correct values");

        //Repairer accept estimate and check Report page
        toolBar.approveTheTask();
        status = testData.getString("status_Repairing");
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), status);
        testCase.get().log(Status.PASS, "The sent claim status is: " + status);
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.getStatus(0).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(1).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(2).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(3).equals("Agreed"));
        testCase.get().log(Status.PASS, "'Status' in calculation list are correct");
        Assert.assertTrue(reportsPO.getComment(0).isEmpty());
        Assert.assertTrue(reportsPO.getComment(1).contains("Agreed amount = 1000" ));
        Assert.assertTrue(reportsPO.getComment(2).isEmpty());
        Assert.assertTrue(reportsPO.getComment(3).contains("Agreed amount = 4000" ));
        testCase.get().log(Status.PASS, "'Comment' in calculation list are correct");
    }

    private void loginAndOpenClaim(String loginUser, String claimNumber, String claimStatus){
        //Logout
        workListGridPO.clickLogout();
        //Login
        Login login = new Login();
        login.LoginBRE(loginUser, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as: " + loginUser);
        //Go to inbox - check status and open claim
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
    }

    private void lumpSumAssertion(String role, String claimStatus, LumpSumEditable lumpSumEditable){
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();

        int expectedResult = -999;
        String logMessage = "";
        switch(lumpSumEditable) {
            case ALL:
                logMessage = "'Lump sum' fields are editable for all calculations";
                expectedResult = reportsPO.getCalculationNumber();
                break;
            case NONE:
                logMessage = "'Lump sum' fields can NOT be edited for all calculations";
                expectedResult = -1 * reportsPO.getCalculationNumber();
                break;
            case LAST:
                logMessage = "'Lump sum' fields is editable only for the last calculation";
                expectedResult = 999;
                break;
        }

        testCase.get().log(Status.INFO, "=== "+ role +" in '"+ claimStatus +"' status ===");
        Assert.assertTrue(calculationList.isAllLumpSumValueBlankInCalculationList());
        testCase.get().log(Status.PASS,"'Lump Sum' value are blank in calculation list");
        Assert.assertEquals(calculationList.getLumpSumEditableNumber(), expectedResult);
        testCase.get().log(Status.PASS,logMessage);
    }

    private void openClaimAfterSent(String claimNumber, String claimStatus){
        //Sent box - check claim is existing in Sent box
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
    }

    private enum LumpSumEditable{ LAST, ALL, NONE}
}
