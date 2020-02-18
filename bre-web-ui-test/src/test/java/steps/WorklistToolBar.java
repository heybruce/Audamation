package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorklistToolBar extends TestBase {
    private WorkListGridOpenPO workListGridOpenPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private WorkListGridClosedPO workListGridClosedPO;
    private WorkListGridPO workListGridPO;

    public WorklistToolBar(){
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO)context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
        workListGridClosedPO = (WorkListGridClosedPO)context.getBean("WorkListGridClosedPO");
        workListGridClosedPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
    }

    public void sendTaskWithLastCalculationAllAttachments(String claimNumber){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.clickRadioBtnOnlyCurrentCalculation();
        workListGridOpenPO.clickRadioBtnAllAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send claim " + claimNumber + " with last calculation and all attachments");
    }

    public void sendTaskWithLastCalculationAllAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnOnlyCurrentCalculation();
        workListGridOpenPO.clickRadioBtnAllAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send claim " + claimNumber + " with last calculation and all attachments to " + receiver);
    }

    public void sendTaskWithSelectedCalculationSelectedAttachments(String claimNumber, String receiver){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnOnlySelectedCalculations();
        workListGridOpenPO.clickRadioBtnOnlySelectedAttachments();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with last calculation and all attachments \"" + claimNumber + "\" to " + receiver);
    }

    public void sendTaskWithAllCalculations(String claimNumber, String receiver){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickSendTask();
        workListGridOpenPO.sendTaskAdvancedSearch(receiver);
        workListGridOpenPO.clickRadioBtnAllCalculations();
        workListGridOpenPO.clickBtnSendTaskInDialog();
        testCase.get().log(Status.INFO, "Send task claim with all calculations of \"" + claimNumber + "\" to " + receiver);
    }

    public void changeOwnerTo(String claimNumber, String newOwner){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickChangeOwner();
        workListGridOpenPO.changeOwnerAdvancedSearch(newOwner);
        workListGridOpenPO.clickBtnChangeOwnerInDialog();
        testCase.get().log(Status.INFO, "Change owner of claim \"" + claimNumber + "\" to " + newOwner);
    }

    public void closeTheTask(String claimNumber){
        workListGridOpenPO.clickClaimCheckbox(claimNumber);
        workListGridOpenPO.clickCloseTask();
        workListGridOpenPO.enterCloseTaskComment("Automation test - close task");
        workListGridOpenPO.clickBtnCloseInDialog();
        testCase.get().log(Status.INFO, "Close the claim from work list: " + claimNumber);
    }

    public void mergeTheTask(String claimNumber){
        workListGridInboxPO.clickClaimCheckbox(claimNumber);
        workListGridInboxPO.clickMergeTask();
        workListGridInboxPO.enterMergeComment("Automation test - merge task");
        workListGridInboxPO.clickMergeBtnInDialog();
        testCase.get().log(Status.INFO, "Merge the claim from work list: " + claimNumber);
    }

    public void reopenTheTask(String claimNumber){
        workListGridClosedPO.clickClaimCheckbox(claimNumber);
        workListGridClosedPO.clickBtnReopenTasks();
        workListGridClosedPO.enterReopenComment("Automation test - reopen task");
        workListGridClosedPO.clickBtnReopenInDialog();
        testCase.get().log(Status.INFO, "Reopen the closed claim: " + claimNumber);
    }

    public void searchByClaimNumber(String claimNumber) {
        workListGridPO.clickSearchButtonInWorkList();
        workListGridPO.clearSearchDataInDialog();
        workListGridPO.enterClaimNumber(claimNumber);
        workListGridPO.clickBtnSearchInDialog();
        testCase.get().log(Status.INFO, "Search claim: " + claimNumber);
    }
}
