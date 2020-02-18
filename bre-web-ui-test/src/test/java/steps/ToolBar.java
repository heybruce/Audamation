package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.processstep.ToolBarPO;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ToolBar extends TestBase{
    private ToolBarPO toolBarPO;

    public ToolBar(){
        toolBarPO = (ToolBarPO)context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
    }

    public void printPdfReport(int templateNum, String textFileName, boolean isStoreReport){
        toolBarPO.clickToolBar();
        toolBarPO.clickPrintPDF();
        toolBarPO.selectPrintFormatByDropDownMenu(templateNum-1);
        if(isStoreReport==true) {
            toolBarPO.selectStoreReport(1);
            testCase.get().log(Status.INFO, "Select to store the PDF in attachment");
        }
        toolBarPO.setFileName(textFileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print the PDF from tool bar");
    }

    public void sendTaskTo(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.sendTaskAdvancedSearch(receiver);
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send claim to " + receiver);
    }

    public void sendTaskWithLastCalculationAllAttachments(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.sendTaskAdvancedSearch(receiver);
        toolBarPO.clickRadioBtnOnlyCurrentCalculation();
        toolBarPO.clickRadioBtnAllAttachments();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send claim with last calculation and all attachments to " + receiver);
    }

    public void sendTaskWithSelectedCalculationSelectedAttachments(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.sendTaskAdvancedSearch(receiver);
        toolBarPO.clickRadioBtnOnlySelectedCalculations();
        toolBarPO.clickRadioBtnOnlySelectedAttachments();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send task claim with selected calculation and attachments to " + receiver);
    }

    public void sendTaskWithAllCalculations(){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.clickRadioBtnAllCalculations();
        toolBarPO.clickRadioBtnNoAttachments();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send the claim with all calculations");
    }

    public void sendTaskWithAllCalculations(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.sendTaskAdvancedSearch(receiver);
        toolBarPO.clickRadioBtnAllCalculations();
        toolBarPO.clickRadioBtnNoAttachments();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send the claim with all calculations to " + receiver);
    }

    public void changeOwnerTo(String newOwner){
        toolBarPO.clickToolBar();
        toolBarPO.clickChangeOwner();
        toolBarPO.changeOwnerAdvancedSearch(newOwner);
        toolBarPO.clickChangeOwnerBtn();
        testCase.get().log(Status.INFO, "Change owner to: " + newOwner);
    }

    public void closeTheTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickClose();
        toolBarPO.enterCloseTaskComment("Automation test - Close Task");
        toolBarPO.clickCloseTask();
        testCase.get().log(Status.INFO, "Close the claim from tool bar");
    }

    public void mergeTheTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickMergeTask();
        toolBarPO.enterMergeComment("Automation test - Merge Task");
        toolBarPO.clickMergeBtnInDialog();
        testCase.get().log(Status.INFO, "Merge the claim from tool bar");
    }

    public void reopenTheTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickReopen();
        toolBarPO.enterReopenComment("Automation test - Reopen Task");
        toolBarPO.clickReopenBtnInDialog();
        testCase.get().log(Status.INFO, "Reopen the closed claim from tool bar");
    }

    public void approveTheTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickApprove();
        toolBarPO.clickApproveBtnInDialog();
        testCase.get().log(Status.INFO, "The claim is approved from tool bar");
    }

    public void rejectTheTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickReject();
        toolBarPO.clickRejectBtnInDialog();
        testCase.get().log(Status.INFO, "The claim is rejected from tool bar");
    }

    public void addComment(){
        toolBarPO.clickToolBar();
        toolBarPO.clickNewComment();
        toolBarPO.enterNewComment("Automation test - Add comment");
        toolBarPO.clickSaveInNewCommentDialog();
        testCase.get().log(Status.INFO, "Add a new comment from tool bar");
    }

    public void sendEstimate(){
        toolBarPO.clickToolBar();
        toolBarPO.clickSendEstimate();
        toolBarPO.clickSendEstimateBtnInDialog();
        testCase.get().log(Status.INFO, "Send the estimation back to sender");
    }

    public void assignTask(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickAssign();
        toolBarPO.assignTaskAdvancedSearch(receiver);
        toolBarPO.clickAssignTaskBtn();
        testCase.get().log(Status.INFO, "Assign claim to: " + receiver);
    }

    public void assignTask(){
        toolBarPO.clickToolBar();
        toolBarPO.clickAssign();
        toolBarPO.clickAssignTaskBtn();
        testCase.get().log(Status.INFO, "Assign claim back to surveyor");
    }

    public void sendTaskWithSelectedAttachments(String receiver){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.sendTaskAdvancedSearch(receiver);
        toolBarPO.clickRadioBtnOnlySelectedAttachments();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send claim with selected attachments to: " + receiver);
    }

    public void sendAssessmentToInsurer(){
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        toolBarPO.clickSendTaskBtn();
        testCase.get().log(Status.INFO, "Send the assessment back to insurer");
    }
}
