package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorklistTaskActions extends TestBase {
    private WorkListGridPO workListGridPO;
    private WorkListGridOpenPO workListGridOpenPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private WorkListGridClosedPO workListGridClosedPO;

    public WorklistTaskActions(){
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO)context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
        workListGridClosedPO = (WorkListGridClosedPO)context.getBean("WorkListGridClosedPO");
        workListGridClosedPO.setWebDriver(getDriver());
    }

    public void changeOwnerTo(String claimNumber, String newOwner){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickChangeOwnerInRow();
        workListGridOpenPO.changeOwnerAdvancedSearch(newOwner);
        workListGridOpenPO.clickBtnChangeOwnerInDialog();
        testCase.get().log(Status.INFO, "Change owner of claim \"" + claimNumber + "\" to " + newOwner);
    }

    public void closeTheTask(String displayName){
        workListGridOpenPO.clickActionBtnInRow(displayName);
        workListGridOpenPO.clickCloseClaimInRow();
        workListGridOpenPO.enterCloseTaskComment("Automation test - close task");
        workListGridOpenPO.clickBtnCloseInDialog();
        testCase.get().log(Status.INFO, "Close the claim from work list");
    }

    public void reopenTheTask(String displayName){
        workListGridClosedPO.clickActionBtnInRow(displayName);
        workListGridClosedPO.clickReopenTasksInRow();
        workListGridClosedPO.enterReopenComment("Automation test - reopen task");
        workListGridClosedPO.clickBtnReopenInDialog();
        testCase.get().log(Status.INFO, "Reopen the closed claim");
    }

    public void mergeTheTask(String claimNumber){
        workListGridInboxPO.clickActionBtnInRow(claimNumber);
        workListGridInboxPO.clickMergeTasksInRow();
        workListGridInboxPO.enterMergeComment("Automation test - merge task");
        workListGridInboxPO.clickMergeBtnInDialog();
        testCase.get().log(Status.INFO, "Merge the claim from work list");
    }

    public void sendTaskWithLastCalculationAllAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendTaskInRow();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnOnlyCurrentCalculation();
        workListGridOpenPO.clickRadioBtnAllAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with last calculation and all attachments \"" + claimNumber + "\" to " + receiver);
    }

    public void sendTaskWithSelectedCalculationSelectedAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnOnlySelectedCalculations();
        workListGridOpenPO.clickRadioBtnOnlySelectedAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with selected calculation and selected attachments \"" + claimNumber + "\" to " + receiver);
    }

    public void sendTask(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim \"" + claimNumber + "\" to " + receiver);
    }

    public void submitTaskWithAllAttachments(String claimNumber){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSubmitTaskInRow();
        workListGridOpenPO.clickRadioBtnAllAttachments();
        workListGridOpenPO.clickBtnSubmitTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with last calculation and all attachments");
    }

    public void submitTaskWithAllAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSubmitTaskInRow();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnAllAttachments();
        workListGridOpenPO.clickBtnSubmitTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with last calculation and all attachments \"" + claimNumber + "\" to " + receiver);
    }

    public void sendTaskWithAllCalculations(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnAllCalculations();
        workListGridOpenPO.clickRadioBtnNoAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with all calculations and no attachment \"" + claimNumber + "\" to " + receiver);
    }

    public void submitTaskWithSelectedAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSubmitTaskInRow();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnOnlySelectedAttachments();
        workListGridOpenPO.clickBtnSubmitTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with selected attachments \"" + claimNumber + "\" to " + receiver);
    }

    public void assignTask(String claimNumber, String receiver){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickAssignTaskInRow();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickBtnAssignTaskInDialog();
        testCase.get().log(Status.INFO, "Assign task claim \"" + claimNumber + "\" to " + receiver);
    }

    public void sendEstimate(String claimNumber){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendEstimateInRow();
        workListGridOpenPO.clickBtnSendEstimateInDialog();
        testCase.get().log(Status.INFO, "Send Estimate claim: " + claimNumber);
    }

    public void rejectEstimate(String claimNumber){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickRejectEstimateInRow();
        workListGridOpenPO.clickBtnRejectEstimateInDialog();
        testCase.get().log(Status.INFO, "Reject estimate claim: " + claimNumber);
    }

    public void acceptEstimate(String claimNumber){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickAcceptEstimateInRow();
        workListGridOpenPO.clickBtnApproveEstimateInDialog();
        testCase.get().log(Status.INFO, "Approve estimate claim: " + claimNumber);
    }

    public void openClaimByClaimNumber(String claimNumber){
        if(isMobileDevice() && getDeviceName().equalsIgnoreCase("iPad"))
            // Open claim on Pad by using click
            workListGridPO.openClaimByClaimNumberOnIOS(claimNumber);
        else
            // Open claim on PC and Android by using double-click
            workListGridPO.openClaimByClaimNumber(claimNumber);
    }

    public void sendAssessmentToInsurer(String claimNumber){
        workListGridOpenPO.clickActionBtnInRow(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send assessment: " + claimNumber + " to insurer");
    }
}
