package pageobjects.worklistgrid;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.log.Loggable;

import java.util.List;

public class WorkListGridOpenPO extends WorkListGridPO {

    public static final String ID_SEND_TASK_DIALOG_WITH_CALCULATION = "styled-component-root.task.workflow.sendTask.dialog-calculations";

    //Action Bar on worklist
    @FindBy(id = "CloseTask")
    private WebElement closeTask;
    @FindBy(id = "SendTask")
    private WebElement sendTask;
    @FindBy(id = "ChangeTaskOwner")
    private WebElement changeTaskOwner;
    @FindBy(id = "ExportToExcel")
    private WebElement exportToExcel;
    @FindBy(id = "KRSendToExternal")
    private WebElement sendToIbos;

    //Action Component in each row
    @FindBy(id="openTask")
    private WebElement btnOpenTaskInRow;
    @FindBy(id="CloseTask")
    private WebElement btnCloseTaskInRow;
    @FindBy(id="ChangeTaskOwner")
    private WebElement btnChangeOwnerInRow;
    @FindBy(id="workflowactionInlineContainer_PrintPdf")
    private WebElement btnPrintPdfInRow;
    @FindBy(id="SubmitTask")
    private WebElement btnSubmitTaskInRow;
    @FindBy(id="workflowactionInlineContainer_SendTask")
    private WebElement btnSendTaskInRow;
    @FindBy(id="AssignTask")
    private WebElement btnAssignTaskInRow;
    @FindBy(id="ChangeBusinessStatusValue23")
    private WebElement btnSendEstimateInRow;
    @FindBy(id="RejectApproval")
    private WebElement btnRejectEstimateInRow;
    @FindBy(id="ApproveTask")
    private WebElement btnAcceptEstimateInRow;

    //Close Dialog
    @FindBy(id="CloseTaskPopup")
    private WebElement closeTaskPopup;
    @FindBy(id="root.task.workflow.closeTask.dialog-comment")
    private WebElement textCloseTaskComment;
    @FindBy(css="#CloseTaskPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnCloseTask;

    //Change Owner Dialog
    @FindBy(id="root.task.workflow.changeOwner.dialog-login")
    private WebElement textNewOwner;
    @FindBy(id="ChangeTaskOwnerPopup")
    private WebElement changeOwnerPopup;
    @FindBy(id="ChangeTaskOwnerWorkflowActionPopup")
    private WebElement changeOwnerMemberSearchPopUp;
    @FindBy(id="root.task.workflow.changeOwner.dialog-membersearchfield-advancedSearchButton")
    private WebElement changeOwnerAdvancedSearchBtn;
    @FindBy(id="styled-component-memberSearchResults_root.task.workflow.changeOwner.dialog-membersearchfield::caseMemberSearch")
    private WebElement changeOwnerSearchResultTable;
    @FindBy(id="root.task.workflow.changeOwner.dialog-membersearchfield-advancedSearchWindow-name_or_loginId")
    private WebElement newOwnerNameField;
    @FindBy(xpath = "id(\"ChangeTaskOwnerWorkflowActionPopup\")/div/div/div[3]/button[3]")
    private WebElement changeOwnerMembersearchBtn;
    @FindBy(xpath="id(\"ChangeTaskOwnerPopup\")/div/div/div[3]/button[3]")
    private WebElement btnChangeOwner;

    //Send task dialog
    @FindBy(id="SendTaskPopup")
    private WebElement sendTaskPopup;
    @FindBy(id="SubmitTaskPopup")
    private WebElement submitTaskPopup;
    @FindBy(id="AssignTaskPopup")
    private WebElement assignTaskPopup;
    @FindBy(id="ChangeBusinessStatusValue23Popup")
    private WebElement sendEstimatePopup;
    @FindBy(id="root-task-workflow-sendTaskSendTaskWorkflowActionMemberSearchPopup")
    private WebElement memberSearchPopup;
    @FindBy(id="root.task.workflow.sendTask.dialog-membersearchfield-advancedSearchButton")
    private WebElement sendTaskAdvancedSearchBtn;
    @FindBy(id="root.task.workflow.sendTask.dialog-membersearchfield-advancedSearchWindow-name_or_loginId")
    private WebElement receiverNameField;
    @FindBy(css="#root-task-workflow-sendTaskSendTaskWorkflowActionMemberSearchPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement sendTaskMemberSearchBtn;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-calculations_onlySelectedCalculations\"]")
    private WebElement radionOnlySelectedCalculations;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-calculations_allCalculations\"]")
    private WebElement radionAllCalculations;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-calculations_noCalculations\"]")
    private WebElement radionNoCalculations;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-calculations_onlyCurrentCalculation\"]")
    private WebElement radionOnlyCurrentCalculation;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-attachments_onlySelectedAttachments\"]")
    private WebElement radionOnlySelectedAttachments;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-attachments_allAttachments\"]")
    private WebElement radionAllAttachments;
    @FindBy(css="label[for=\"root.task.workflow.sendTask.dialog-attachments_noAttachments\"]")
    private WebElement radionNoAttachments;
    @FindBy(css = "#SendTaskPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnSendTask;
    @FindBy(css="#SubmitTaskPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnSubmitTask;
    @FindBy(css="#SubmitTaskPopup div div div.modal-footer button:nth-of-type(2)")
    private WebElement cancelSubmitTaskDialogBtn;
    @FindBy(xpath="id(\"AssignTaskPopup\")/div/div/div[3]/button[3]")
    private WebElement btnAssignTask;
    @FindBy(css="#ChangeBusinessStatusValue23Popup div div div.modal-footer button:nth-of-type(2)")
    private WebElement btnSendEstimate;
    @FindBy(id="root.task.workflow.sendTask.dialog-login")
    private WebElement receiverNameSearchResult;
    @FindBy(css="div[name=\"loginId\"] span[class^=\"text\"]")
    private List<WebElement> searchResultLoginId;
    @FindBy(css="div[name=\"name\"] span[class^=\"text\"]")
    private List<WebElement> searchResultName;
    @FindBy(id="styled-component-memberSearchResults_root.task.workflow.sendTask.dialog-membersearchfield::caseMemberSearch")
    private WebElement searchResultTable;

    //Reject Estimate
    @FindBy(id="RejectApprovalPopup")
    private WebElement rejectEstimatePopup;
    @FindBy(xpath="id(\"RejectApprovalPopup\")/div/div/div[3]/button[2]")
    private WebElement btnRejectEstimate;

    //Approve Estimate
    @FindBy(id="ApproveTaskPopup")
    private WebElement acceptEstimatePopup;
    @FindBy(xpath="id(\"ApproveTaskPopup\")/div/div/div[3]/button[2]")
    private WebElement btnAcceptEstimate;

    private String classString = "class";
    private static final String JS_MOVE_TO_ELEMENT = "arguments[0].scrollIntoView();";
    private static final String GET_ATTRIBUTE_VALUE = "value";
    
    public WorkListGridOpenPO() {
        super();
    }
    public WorkListGridOpenPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public void clickCloseTask(){
        this.click(closeTask);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(closeTaskPopup, classString, "in"));
    }
    @Loggable
    public void clickSendTask(){
        this.click(sendTask);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(sendTaskPopup, classString, "in"));
    }
    @Loggable
    public void clickChangeOwner(){
        this.click(changeTaskOwner);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(changeOwnerPopup, classString, "in"));
    }
    @Loggable
    public void clickExportToExcel(){ this.click(exportToExcel); }
    @Loggable
    public void clickSendToIbos(){ this.click(sendToIbos); }

    //Action Component
    @Loggable
    public void clickOpenClaimInRow(){ this.click(btnOpenTaskInRow); }
    @Loggable
    public void clickCloseClaimInRow(){ this.click(btnCloseTaskInRow); }
    @Loggable
    public void clickChangeOwnerInRow(){
        this.click(btnChangeOwnerInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(changeOwnerPopup, classString, "in"));
    }
    @Loggable
    public void clickPrintPdfInRow(){ this.click(btnPrintPdfInRow); }
    @Loggable
    public void clickSubmitTaskInRow(){
        this.click(btnSubmitTaskInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(submitTaskPopup, classString, "in"));
    }
    @Loggable
    public void clickAssignTaskInRow(){
        this.click(btnAssignTaskInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(assignTaskPopup, classString, "in"));
    }

    @Loggable
    public void clickSendEstimateInRow(){
        this.click(btnSendEstimateInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(sendEstimatePopup, classString, "in"));
    }

    @Loggable
    public void clickSendTaskInRow(){
        this.click(btnSendTaskInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(sendTaskPopup, classString, "in"));
    }

    @Loggable
    public void clickRejectEstimateInRow(){
        this.click(btnRejectEstimateInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(rejectEstimatePopup, classString, "in"));
    }

    @Loggable
    public void clickAcceptEstimateInRow(){
        this.click(btnAcceptEstimateInRow);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(acceptEstimatePopup, classString, "in"));
    }

    //Close task actions
    @Loggable
    public void enterCloseTaskComment(String text){
        this.click(textCloseTaskComment);
        this.sendKeys(textCloseTaskComment, text);
    }
    @Loggable
    public void clickBtnCloseInDialog(){
        this.click(btnCloseTask);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Change owner actions
    @Loggable
    public void changeOwnerAdvancedSearch(String newOwner) {
        // Open advanced search dialog
        this.click(changeOwnerAdvancedSearchBtn);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(changeOwnerMemberSearchPopUp, classString, "in"));
        // input new owner in search field
        this.sendKeys(newOwnerNameField, newOwner);
        // search
        this.click(changeOwnerMembersearchBtn);
        // scroll to search result
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(changeOwnerSearchResultTable));
        // select member if search by member login ID
        selectReceiverFromSearchResult(newOwner, searchResultLoginId);
        // Waiting for the new owner is inputted
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.attributeToBe(textNewOwner, GET_ATTRIBUTE_VALUE , newOwner));
    }
    @Loggable
    public void clickBtnChangeOwnerInDialog(){
        this.click(btnChangeOwner);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Send task actions
    @Loggable
    public void sendTaskAdvancedSearch(String receiver) {
        // Open advanced search dialog
        this.click(sendTaskAdvancedSearchBtn);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(memberSearchPopup, classString, "in"));
        // input receiver in search field
        this.sendKeys(receiverNameField, receiver);
        // search
        this.click(sendTaskMemberSearchBtn);
        // scroll to search result
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(searchResultTable));
        // select member if search by member login ID
        if (!selectReceiverFromSearchResult(receiver, searchResultLoginId))
            // select org if search by organization name
            selectReceiverFromSearchResult(receiver, searchResultName);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(receiverNameSearchResult, GET_ATTRIBUTE_VALUE, receiver));
    }

    //select receiver from search Result
    @Loggable
    private boolean selectReceiverFromSearchResult(String receiver, List<WebElement> searchResult){
        for (WebElement result : searchResult) {
            if (result.getText().equals(receiver)) {
                // scroll to search result and choose the receiver in search result
                // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
                ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, result);
                this.click(result);
                return true;
            }
        }
        return false;
    }

    @Loggable
    public void clickBtnSendTaskInDialog() {
        // send task via send task dialog
        // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnSendTask);
        this.click(btnSendTask);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickBtnSubmitTaskInDialog() {
        // send task via send task dialog
        // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnSubmitTask);
        this.click(btnSubmitTask);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickBtnAssignTaskInDialog() {
        // assign task via send task dialog
        // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnAssignTask);
        this.click(btnAssignTask);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickBtnSendEstimateInDialog() {
        // assign task via send task dialog
        // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnSendEstimate);
        this.click(btnSendEstimate);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickBtnRejectEstimateInDialog(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnRejectEstimate);
        this.click(btnRejectEstimate);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickBtnApproveEstimateInDialog(){
        ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, btnAcceptEstimate);
        this.click(btnAcceptEstimate);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public void clickRadioBtnOnlySelectedCalculations() { this.click(radionOnlySelectedCalculations); }
    @Loggable
    public void clickRadioBtnAllCalculations() { this.click(radionAllCalculations); }
    @Loggable
    public void clickRadioBtnNoCalculations() { this.click(radionNoCalculations); }
    @Loggable
    public void clickRadioBtnOnlyCurrentCalculation() { this.click(radionOnlyCurrentCalculation); }
    @Loggable
    public void clickRadioBtnOnlySelectedAttachments() { this.click(radionOnlySelectedAttachments); }
    @Loggable
    public void clickRadioBtnAllAttachments() { this.click(radionAllAttachments); }
    @Loggable
    public void clickRadioBtnNoAttachments() { this.click(radionNoAttachments); }
    @Loggable
    public void clickCancelSubmitTaskDialog(){ this.click(cancelSubmitTaskDialogBtn); }
}
