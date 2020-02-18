package pageobjects.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;

public class ToolBarPO extends PageObject {
    public ToolBarPO() {
        super();
    }
    public ToolBarPO(WebDriver webDriver) {
        super(webDriver);
    }

    private static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");
    private static final String GET_ATTRIBUTE_VALUE = "value";
    private static final String JS_MOVE_TO_ELEMENT = "arguments[0].scrollIntoView();";

    protected static final By NOTIFICATION_POPUP = By.cssSelector(".notification-component");
    private String classString = "class";

    public static final String ID_SEND_ESTIMATE_BTN = "root.task.workflow.changeBusinessStatus.value23.update";
    public static final String ID_SEND_TASK_DIALOG_WITH_CALCULATION = "styled-component-root.task.workflow.sendTask.dialog-calculations";
    
    //Tool bar menu
    @FindBy(id="more-row-icon-toolbar")
    private WebElement btnToolBar;
    @FindBy(id="root.actionButtons.printPDFButton")
    private WebElement btnPrintPDF;
    @FindBy(id="root.task.workflow.sendTask.send")
    private WebElement btnSend;
    @FindBy(id="root.task.workflow.closeTask.close")
    private WebElement btnClose;
    @FindBy(id="root.task.workflow.changeBusinessStatus.value5.update")
    private WebElement btnApprove;
    @FindBy(id="root.task.workflow.changeBusinessStatus.value6.update")
    private WebElement btnReject;
    @FindBy(id="root.task.genericCaseMessages.sendGenericCaseMessage.sendmessage")
    private WebElement btnSendMessage;
    @FindBy(id="root.task.workflow.changeOwner.change")
    private WebElement btnChangeOwner;
    @FindBy(id="root.copyIntoNewCaseComponent.copyButton")
    private WebElement btnCopy;
    @FindBy(id="root.task.workflow.mergeTask.merge")
    private WebElement btnMergeTask;
    @FindBy(id="root.task.workflow.reopenTask.reopen")
    private WebElement btnReopen;
    @FindBy(id="root.actionButtons.commentsButton")
    private WebElement btnNewComment;
    @FindBy(id=ID_SEND_ESTIMATE_BTN)
    private WebElement btnSendEstimate;
    @FindBy(id="root.task.workflow.sendTask.assign")
    private WebElement btnAssign;

    //Print PDF dialog
    public static final String ID_PRINT_PDF_POPUP = "PrintPDFPopup";
    @FindBy(id=ID_PRINT_PDF_POPUP)
    private WebElement printPdfPopup;
    @FindBy(id="select-field-id-root.printPdf.printFormat")
    private WebElement selectPrintFormat;
    @FindBy(css="input[id=\"root.printPdf.calculation\"]")
    private WebElement selectCalculation;
    @FindBy(id = "select-field-id-root.printPdf.storereport")
    private WebElement selectStoreReport;
    @FindBy(id=ID_PDF_FILE_NAME)
    private WebElement textboxFileName;
    @FindBy(css="#PrintPDFPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnGeneratePDF;
    private static final String ID_PDF_FILE_NAME="root.printPdf.filename";

    //Close Dialog
    @FindBy(id="com-audatex-breservices-ui-components-workflow-CloseTaskWorkflowActionComponent")
    private WebElement closeTaskDialog;
    @FindBy(id="root.task.workflow.closeTask.dialog-comment")
    private WebElement textCloseTaskComment;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-CloseTaskWorkflowActionComponent div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnCloseTask;

    //Send Dialog
    @FindBy(id="com-audatex-breservices-ui-components-workflow-SendTaskWorkflowActionComponent")
    private WebElement sendTaskDialog;
    @FindBy(id="root.task.workflow.sendTask.dialog-membersearchfield-advancedSearchButton")
    private WebElement sendTaskAdvancedSearchBtn;
    @FindBy(css = "#AssignToolbarPopup div div div.modal-body div div:nth-child(3) div div button")
    private WebElement assignTaskAdvanceSearch;
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
    @FindBy(css="div[name=\"loginId\"] span[class^=\"text\"]")
    private List<WebElement> searchResultLoginId;
    @FindBy(css="div[name=\"name\"] span[class^=\"text\"]")
    private List<WebElement> searchResultName;
    @FindBy(id="styled-component-memberSearchResults_root.task.workflow.sendTask.dialog-membersearchfield::caseMemberSearch")
    private WebElement searchResultTable;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-SendTaskWorkflowActionComponent div div div.modal-footer button:nth-of-type(3)")
    private WebElement sendTaskBtn;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-SendTaskWorkflowActionComponent div div div.modal-footer button:nth-of-type(2)")
    private WebElement cancelSendTaskDialogBtn;
    @FindBy(id="root.task.workflow.sendTask.dialog-login")
    private WebElement receiverNameSearchResult;
    @FindBy(id="AssignToolbarPopup")
    private WebElement assignPopup;
    @FindBy(css="#AssignToolbarPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement assignTaskBtn;

    //Change Owner Dialog
    @FindBy(id="root.task.workflow.changeOwner.dialog-login")
    private WebElement textNewOwner;
    @FindBy(id="com-audatex-breservices-ui-components-workflow-ChangeTaskOwnerWorkflowActionComponent")
    private WebElement changeOwnerDialog;
    @FindBy(id="root.task.workflow.changeOwner.dialog-membersearchfield-advancedSearchButton")
    private WebElement changeOwnerAdvancedSearchBtn;
    @FindBy(id="styled-component-memberSearchResults_root.task.workflow.changeOwner.dialog-membersearchfield::caseMemberSearch")
    private WebElement changeOwnerSearchResultTable;
    @FindBy(id="root.task.workflow.changeOwner.dialog-membersearchfield-advancedSearchWindow-name_or_loginId")
    private WebElement newOwnerNameField;
    @FindBy(xpath = "id(\"ChangeTaskOwnerWorkflowActionPopup\")/div/div/div[3]/button[3]")
    private WebElement changeOwnerMembersearchBtn;
    @FindBy(xpath="id(\"com-audatex-breservices-ui-components-workflow-ChangeTaskOwnerWorkflowActionComponent\")/div/div/div[3]/button[3]")
    private WebElement changeOwnerBtn;

    //Merge Dialog
    @FindBy(id="com-audatex-breservices-ui-components-workflow-MergeTaskWorkflowActionComponent")
    private WebElement mergeTaskDialog;
    @FindBy(id="root.task.workflow.mergeTask.dialog-comment")
    private WebElement textMergeComment;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-MergeTaskWorkflowActionComponent div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnMergeInDialog;

    //Reopen Dialog
    @FindBy(id="com-audatex-breservices-ui-components-workflow-ReopenTaskWorkflowActionComponent")
    private WebElement reopenTaskDialog;
    @FindBy(id="root.task.workflow.reopenTask.dialog-comment")
    private WebElement textReopenComment;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-ReopenTaskWorkflowActionComponent div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnReopenConfirm;

    //Copy Dialog
    @FindBy(id="root.copyIntoNewCaseComponent.dialog-claimNumber")
    private WebElement textFieldClaimNumber;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeAdministrativeData")
    private WebElement checkboxIncludeAdministartiveData;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeAdministrativeData\"]")
    private WebElement checkboxIncludeAdministartiveDataLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeDamageDescription")
    private WebElement checkboxIncludeDamageDescription;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeDamageDescription\"]")
    private WebElement checkboxIncludeDamageDescriptionLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includePolicyData")
    private WebElement checkboxIncludePolicyData;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includePolicyData\"]")
    private WebElement checkboxIncludePolicyDataLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeVehicleData")
    private WebElement checkboxIncludeVehicleData;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeVehicleData\"]")
    private WebElement checkboxIncludeVehicleDataLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeDamageCapture")
    private WebElement checkboxIncludeDamageCapture;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeDamageCapture\"]")
    private WebElement checkboxIncludeDamageCaptureLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeAllCalculations")
    private WebElement checkboxIncludeAllCalculations;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeAllCalculations\"]")
    private WebElement checkboxIncludeAllCalculationsLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeAttachments")
    private WebElement checkboxIncludeAttachments;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeAttachments\"]")
    private WebElement checkboxIncludeAttachmentsLabel;

    @FindBy(id="root.copyIntoNewCaseComponent.dialog-includeLastCalculation")
    private WebElement checkboxIncludeLastCalculation;
    @FindBy(css="label[for=\"root.copyIntoNewCaseComponent.dialog-includeLastCalculation\"]")
    private WebElement checkboxIncludeLastCalculationLabel;

    @FindBy(id="com-audatex-breservices-ui-components-workflow-CopyIntoNewCaseComponent")
    private WebElement copyCaseDialog;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-CopyIntoNewCaseComponent div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnConfirmCopy;

    @FindBy(id="com-audatex-breservices-ui-components-workflow-ChangeBusinessStatusWorkflowActionComponent-value5")
    private WebElement approveDialog;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-ChangeBusinessStatusWorkflowActionComponent-value5 div div div.modal-footer button:nth-of-type(2)")
    private WebElement btnApproveConfirm;
    @FindBy(css="#com-audatex-breservices-ui-components-workflow-ChangeBusinessStatusWorkflowActionComponent-value6 div div div.modal-footer button:nth-of-type(2)")
    private WebElement btnRejectConfirm;

    @FindBy(css="#com-audatex-breservices-ui-components-workflow-ChangeBusinessStatusWorkflowActionComponent-value23 div div div.modal-footer button:nth-of-type(2)")
    private WebElement btnSendEstimateConfirm;

    //New Comment
    @FindBy(id="NewCommentPopup")
    private WebElement newCommentDialog;
    @FindBy(id="root.caseComments.newCommentTextarea")
    private WebElement textNewComment;
    @FindBy(css="#NewCommentPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnNewCommentConfirm;



    @Loggable
    public void enterCopyClaimNumber(String textNewClaimNumber){ this.sendKeys(textFieldClaimNumber, textNewClaimNumber); }
    @Loggable
    public void clickCheckboxIncludeAdministartiveData(){ this.click(checkboxIncludeAdministartiveDataLabel); }
    @Loggable
    public void clickCheckboxIncludeDamageDescription(){ this.click(checkboxIncludeDamageDescriptionLabel); }
    @Loggable
    public void clickCheckboxIncludePolicyData(){ this.click(checkboxIncludePolicyDataLabel); }
    @Loggable
    public void clickCheckboxIncludeVehicleData(){ this.click(checkboxIncludeVehicleDataLabel); }
    @Loggable
    public void clickCheckboxIncludeDamageCapture(){ this.click(checkboxIncludeDamageCaptureLabel); }
    @Loggable
    public void clickCheckboxIncludeAllCalculations(){ this.click(checkboxIncludeAllCalculationsLabel); }
    @Loggable
    public void clickCheckboxIncludeAttachments(){ this.click(checkboxIncludeAttachmentsLabel); }
    @Loggable
    public void clickCheckboxIncludeLastCalculation(){ this.click(checkboxIncludeLastCalculationLabel); }

    //Toolbar actions
    @Loggable
    public void clickToolBar(){ this.click(btnToolBar); }
    @Loggable
    public void clickPrintPDF(){
        this.click(btnPrintPDF);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(printPdfPopup, classString, "in"));
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(printPdfPopup, "aria-hidden", "false"));
    }
    @Loggable
    public void clickSend(){
        this.click(btnSend);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(sendTaskDialog, classString, "in"));
    }
    @Loggable
    public void clickClose(){
        this.click(btnClose);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(closeTaskDialog, classString, "in"));
    }
    @Loggable
    public void clickApprove(){
        this.click(btnApprove);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(approveDialog, classString, "in"));
    }
    @Loggable
    public void clickReject(){ this.click(btnReject); }
    @Loggable
    public void clickSendMessage(){ this.click(btnSendMessage); }
    @Loggable
    public void clickChangeOwner(){
        this.click(btnChangeOwner);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(changeOwnerDialog, classString, "in"));
    }
    @Loggable
    public void clickCopy(){
        this.click(btnCopy);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(copyCaseDialog, classString, "in"));
    }
    @Loggable
    public void clickMergeTask(){
        this.click(btnMergeTask);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(mergeTaskDialog, classString, "in"));
    }
    @Loggable
    public void clickReopen(){
        this.click(btnReopen);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(reopenTaskDialog, classString, "in"));
    }
    @Loggable
    public void clickNewComment(){
        this.click(btnNewComment);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(newCommentDialog, classString, "in"));
    }
    @Loggable
    public void clickSendEstimate(){ this.click(btnSendEstimate); }
    @Loggable
    public void clickSendEstimateBtnInDialog(){
        this.click(btnSendEstimateConfirm);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }
    @Loggable
    public void clickAssign(){
        this.click(btnAssign);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(assignPopup, classString, "in"));
    }

    //Approve action
    public void clickApproveBtnInDialog(){
        this.click(btnApproveConfirm);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }
    //Reject action
    public void clickRejectBtnInDialog(){
        this.click(btnRejectConfirm);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Print PDF actions
    @Loggable
    public void selectPrintFormatByDropDownMenu(int value) {
        this.click(selectPrintFormat);
        this.click(webDriver.findElement(By.cssSelector("[id*=\"react-select-root.printPdf.printFormat-option-"+value+"\"]")));
    }
    @Loggable
    public String getFileName(){
        return this.getAttributeValue(By.id(ID_PDF_FILE_NAME), GET_ATTRIBUTE_VALUE);
    }
    @Loggable
    public void setFileName(String fileName){
        //workaround for random failed to clear filename on Chrome in sendKeys() function
        textboxFileName.clear();
        this.sendKeys(textboxFileName, fileName);
    }
    @Loggable
    public void clickGeneratePDF(){ this.click(btnGeneratePDF); }
    @Loggable
    public void selectStoreReport(int value){
        this.click(selectStoreReport);
        webDriver.findElement(By.cssSelector("[id*=\"react-select-root.printPdf.storereport-option-"+value+"\"]")).click();
    }
    public void selectCalculation(int value) {
        this.click(selectCalculation);
        webDriver.findElement(By.cssSelector("[id*=\"react-select-root.printPdf.calculation-option-"+value+"\"]")).click();
    }

    //Close task actions
    @Loggable
    public void enterCloseTaskComment(String text){
        this.click(textCloseTaskComment);
        this.sendKeys(textCloseTaskComment, text);
    }
    @Loggable
    public void clickCloseTask(){
        this.click(btnCloseTask);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Send task actions
    @Loggable
    public void sendTaskAdvancedSearch(String receiver) {
        // Open advanced search dialog
        this.click(sendTaskAdvancedSearchBtn);
        // search and select the receiver
        advancedSearch(receiver);
    }

    @Loggable
    public void assignTaskAdvancedSearch(String receiver) {
        // Open advanced search dialog
        this.click(assignTaskAdvanceSearch);
        // search and select the receiver
        advancedSearch(receiver);
    }

    @Loggable
    private void advancedSearch(String receiver) {
        // input receiver in search field
        this.sendKeys(receiverNameField, receiver);
        // search
        this.click(sendTaskMemberSearchBtn);
        // wait for search result table displays
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(searchResultTable));
        // select member if search by member login ID
        if (!selectReceiverFromSearchResult(receiver, searchResultLoginId))
            // select org if search by organization name
            selectReceiverFromSearchResult(receiver, searchResultName);
        // wait for the receiver to be brought into the receiver field
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.attributeContains(receiverNameSearchResult, GET_ATTRIBUTE_VALUE, receiver));
    }

    @Loggable
    public void clickSendTaskBtn() {
        // send task via send task dialog
        this.click(sendTaskBtn);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }
    @Loggable
    public void clickAssignTaskBtn() {
        // send task via send task dialog
        this.click(assignTaskBtn);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Change owner actions
    @Loggable
    public void changeOwnerAdvancedSearch(String newOwner) {
        // Open advanced search dialog
        this.click(changeOwnerAdvancedSearchBtn);
        // input new owner in search field
        this.sendKeys(newOwnerNameField, newOwner);
        // search
        this.click(changeOwnerMembersearchBtn);
        // wait for search result table displays
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(changeOwnerSearchResultTable));
        // select member by login ID
        selectReceiverFromSearchResult(newOwner, searchResultLoginId);
        // Waiting for the new owner is inputted
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.attributeToBe(textNewOwner, GET_ATTRIBUTE_VALUE , newOwner));
    }

    //select receiver from search Result
    @Loggable
    private boolean selectReceiverFromSearchResult(String receiver, List<WebElement> searchResult){
        for (WebElement result : searchResult) {
            if (result.getText().equals(receiver)) {
                String sti = result.getText();
                // scroll to search result
                // note: Actions.MoveToElement is not working in Firefox so using javascript as workaround
                ((JavascriptExecutor) webDriver).executeScript(JS_MOVE_TO_ELEMENT, result);
                // choose the receiver in search result
                this.click(result);
                return true;
            }
        }
        return false;
    }

    @Loggable
    public void clickChangeOwnerBtn() {
        // change owner via change owner dialog
        this.click(changeOwnerBtn);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Merge task actions
    @Loggable
    public void enterMergeComment(String text){ this.sendKeys(textMergeComment, text); }
    @Loggable
    public void clickMergeBtnInDialog(){
        this.click(btnMergeInDialog);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    //Reopen task dialog
    @Loggable
    public void enterReopenComment(String text){ this.sendKeys(textReopenComment, text);}
    @Loggable
    public void clickReopenBtnInDialog(){
        this.click(btnReopenConfirm);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    //Copy task actions
    @Loggable
    public void clickBtnConfirmCopy(){
        this.click(btnConfirmCopy);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForElementInvisible(NOTIFICATION_POPUP);
    }

    @Loggable
    public boolean isCheckedIncludeAdministartiveData(){
        return checkboxIncludeAdministartiveData.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeDamageDescription(){
        return checkboxIncludeDamageDescription.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludePolicyData(){
        return checkboxIncludePolicyData.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeVehicleData(){
        return checkboxIncludeVehicleData.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeDamageCapture(){
        return checkboxIncludeDamageCapture.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeAllCalculations(){
        return checkboxIncludeAllCalculations.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeAttachments(){
        return checkboxIncludeAttachments.isSelected();
    }
    @Loggable
    public boolean isCheckedIncludeLastCalculation(){
        return checkboxIncludeLastCalculation.isSelected();
    }

    //New Comment actions
    @Loggable
    public void enterNewComment(String text){ this.sendKeys(textNewComment, text);}
    @Loggable
    public void clickSaveInNewCommentDialog(){
        this.click(btnNewCommentConfirm);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.not(
                ExpectedConditions.attributeContains(newCommentDialog, classString, "in")));
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
    public void clickCancelSendTaskDialog(){
        this.click(cancelSendTaskDialogBtn);
        new WebDriverWait(webDriver, 3).until(ExpectedConditions.invisibilityOf(sendTaskDialog));
    }
}
