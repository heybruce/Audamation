package cases.ID;

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
import pageobjects.processstep.processstep.ProcessStepIDPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class LumpSumIDTest extends TestBase{
    private ProcessStepIDPO processStepIDPO;
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
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
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
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created by " + insurer);
        //Second calculation
        processStepIDPO.clickDamageCaptureID();
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
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Verify lump sum in Created status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.ALL);
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(repairerOrg);

        //Verify lump sum in Open after send status
        claimStatus = testData.getString("status_Open_After_Send");
        openClaimAfterSent(claimNumber, claimStatus);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.ALL);

        //Repairer
        //Verify lump sum in Created status before and after merged
        claimStatus = testData.getString("status_Created");
        loginAndOpenClaim(repairer, claimNumber, claimStatus);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        //Merge the claim
        toolBar.mergeTheTask();
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Submit task to insurer
        toolBar.sendTaskTo(insurerOrg);
        claimStatus = testData.getString("status_Submitted");
        openClaimAfterSent(claimNumber, claimStatus);

        //Verify lump sum in Submitted status
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Insurer
        //Verify lump sum in Submitted status before and after merged
        loginAndOpenClaim(insurer, claimNumber, claimStatus);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS,"Only one calculation in the list");
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);

        //assign task to surveyor
        toolBar.assignTask(surveyorOrg);
        claimStatus = testData.getString("status_Inspecting");
        openClaimAfterSent(claimNumber, claimStatus);

        //Verify lump sum in Inspecting status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);

        //Surveyor
        //Verify lump sum in Inspecting status before and after merged
        loginAndOpenClaim(surveyor, claimNumber, claimStatus);
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.NONE);
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS,"Only one calculation in the list");
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.ALL);

        //add another part to repair and do the second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do another calculation");

        //Verify lump sum in Inspecting status
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.LAST);

        //send back to insurer for estimation
        toolBar.sendAssessmentToInsurer();
        claimStatus = testData.getString("status_Inspected");
        openClaimAfterSent(claimNumber, claimStatus);
        //Verify lump sum in Inspected status
        lumpSumAssertion(surveyor, claimStatus, LumpSumEditable.LAST);

        //Insurer
        //Verify lump sum in Inspected status before and after merged
        loginAndOpenClaim(insurer, claimNumber, claimStatus);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS,"Only one calculation in the list");
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.LAST);

        //Insurer send estimate back to repairer
        toolBar.sendEstimate();
        claimStatus = testData.getString("status_Pending_Agreement");
        openClaimAfterSent(claimNumber, claimStatus);

        //Verify lump sum in Pending Agreement status
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);

        //Repairer
        //Verify lump sum in Pending Agreement status before and after merge
        loginAndOpenClaim(repairer, claimNumber, claimStatus);
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS,"Only one calculation in the list");
        //Merge the claim
        toolBar.mergeTheTask();
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Repairer accept estimate
        toolBar.approveTheTask();
        claimStatus = testData.getString("status_Repairing");
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Verify lump sum in Repairing status
        lumpSumAssertion(repairer, claimStatus, LumpSumEditable.NONE);

        //Insurer
        workListGridPO.clickLogout();
        //Login as insurer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);
        //Go to inbox - check status and open claim
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is auto-merged in open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), claimStatus);
        testCase.get().log(Status.PASS, "The sent claim status is: " + claimStatus);
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        lumpSumAssertion(insurer, claimStatus, LumpSumEditable.NONE);
    }

    @Test(description = "Check approval and lump sum value")
    public void markCalculationAsApproved() throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Repairer send claim to insurer and merged by insurer (claim status: Submitted)
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("benzS_vehicle"), false);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //Do another damage capturing and calculation
        processStepIDPO.clickDamageCaptureID();
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
        processStepIDPO.clickReportsID();
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
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
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
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.getStatus(0).isEmpty());
        Assert.assertTrue(reportsPO.getStatus(1).equals("Agreed"));
        testCase.get().log(Status.PASS, "'Status' in calculation list are correct");
        Assert.assertTrue(reportsPO.getComment(0).isEmpty());
        Assert.assertTrue(reportsPO.getComment(1).contains("Agreed amount = 1000" ));
        testCase.get().log(Status.PASS, "'Comment' in calculation list are correct");

        //Do the third damage capturing and calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepIDPO.clickReportsID();
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
        processStepIDPO.clickReportsID();
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
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");
        getDriver().switchTo().defaultContent();
        //Switch to Reports page
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the fourth calculation");
        calculationList.displayAllContentInReportCalculationList();
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
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Assert.assertEquals(reportsPO.getLumpSum(1), "1000");
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
        processStepIDPO.clickReportsID();
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

    private void lumpSumAssertion(String role, String claimStatus, LumpSumEditable lumpSumEditable){
        processStepIDPO.clickReportsID();
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
        Assert.assertTrue(calculationList.isAllLumpSumValueTheSameAsGrandTotalWithVAT());
        testCase.get().log(Status.PASS,"'Lump Sum' values are the same as Grand Total with Tax values");
        Assert.assertEquals(calculationList.getLumpSumEditableNumber(), expectedResult);
        testCase.get().log(Status.PASS,logMessage);
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
