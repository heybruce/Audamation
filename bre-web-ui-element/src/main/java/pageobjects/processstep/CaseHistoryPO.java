package pageobjects.processstep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import utils.log.Loggable;

import java.util.List;

public class CaseHistoryPO extends PageObject {

    @FindBy(css=".top-header-title")
    private WebElement pageTitle;
    //Case Data
    @FindBy(css = "#styled-component-root\\.task\\.businessStatusKind > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")
    private WebElement businessStaus;

    @Loggable
    public String getBusinessStatus(){ return this.getText(businessStaus); }

    //Case message
    @FindBy(id="Case_Messages")
    private WebElement caseMessages;
    @FindBy(id="root.task.genericCaseMessages.sendGenericCaseMessage.sendmessage")
    private WebElement btnSendMessage;
    @FindBy(name="createDateTime")
    private List<WebElement> messageCreateDateTime;
    @FindBy(name="subject")
    private List<WebElement> messageSubject;
    @FindBy(name="message")
    private List<WebElement> messageContent;
    @FindBy(name="senderUserId")
    private List<WebElement> messageSenderUserId;
    @FindBy(name="senderOrganization")
    private List<WebElement> messageSenderOrganization;
    @FindBy(name="receiverUserId")
    private List<WebElement> messageReceiverUserId;
    @FindBy(name="receiverOrganization")
    private List<WebElement> messageReceiverOrganization;

    //Case comments
    @FindBy(id="root.actionButtons.commentsButton")
    private WebElement btnCaseComments;
    @FindBy(name = "commentType")
    private List<WebElement> commentType;
    @FindBy(name="author")
    private List<WebElement> commentAuthor;
    @FindBy(name="date")
    private List<WebElement> commentDate;
    @FindBy(name="text")
    private List<WebElement> commentText;
    //Send comment dialog
    @FindBy(id="root.caseComments.newCommentTextarea")
    private WebElement newCommentTextArea;
    @FindBy(css="label[for=\"root.caseComments.isPrivate\"]")
    private WebElement checkedboxPrivacy;
    @FindBy(css="div.modal-footer button:nth-of-type(1)")
    private WebElement btnClearComment;
    @FindBy(css="div.modal-footer button:nth-of-type(2)")
    private WebElement btnCancel;
    @FindBy(xpath="id(\"AddCommentPopupButton\")/div/div/div[3]/button[3]")
    private WebElement btnSaveComment;


    //Case history
    @FindBy(name="eventNote")
    private List<WebElement> eventNote;
    @FindBy(name="eventLogUserId")
    private List<WebElement> eventLogUserId;
    @FindBy(name="eventUserFirstLastName")
    private List<WebElement> eventUserFirstLastName;
    @FindBy(name="eventLogDate")
    private List<WebElement> eventLogDate;


    @Loggable
    public void clickSendMessage(){ this.click(btnSendMessage); }
    @Loggable
    public String getMessageCreateDateTime(int row){ return this.getText(messageCreateDateTime.get(row)); }
    @Loggable
    public String getMessageSubject(int row){ return this.getText(messageSubject.get(row)); }
    @Loggable
    public String getMessageContent(int row){ return this.getText(messageContent.get(row)); }
    @Loggable
    public String getMessageSenderUserId(int row){ return this.getText(messageSenderUserId.get(row)); }
    @Loggable
    public String getMessageSenderOrganization(int row){ return this.getText(messageSenderOrganization.get(row)); }
    @Loggable
    public String getMessageReceiverUserId(int row){ return this.getText(messageReceiverUserId.get(row)); }
    @Loggable
    public String getMessageReceiverOrganization(int row){ return this.getText(messageReceiverOrganization.get(row)); }

    @Loggable
    public void clickBtnComments(){ this.click(btnCaseComments); }
    @Loggable
    public void inputComment(String comments){ this.sendKeys(newCommentTextArea,comments); }
    @Loggable
    public void clickPrivacyCheckedbox(){ this.click(checkedboxPrivacy); }
    @Loggable
    public void clickClearBtn(){ this.click(btnClearComment); }
    @Loggable
    public void clickCancelBtn(){ this.click(btnCancel); }
    @Loggable
    public void clickSaveBtn(){ this.click(btnSaveComment); }
    @Loggable
    public void waitForAddedComment(){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"styled-component-root.caseComments.caseCommentsTable\"]/descendant::div[@index='0']")));
    }
    @Loggable
    public String getCommentType(int row){ return this.getText(commentType.get(row)); }
    @Loggable
    public String getCommentAuthor(int row){ return this.getText(commentAuthor.get(row)).replace("[+]", ""); }
    @Loggable
    public String getCommentDate(int row){ return this.getText(commentDate.get(row)); }
    @Loggable
    public String getCommentText(int row){ return this.getText(commentText.get(row)); }
    @Loggable
    public int getCommentNumber(){ return commentText.size(); }

    @Loggable
    public String getEventNote(int row){ return this.getText(eventNote.get(row)); }
    @Loggable
    public String getEventUserId(int row){ return this.getText(eventLogUserId.get(row)); }
    @Loggable
    public String getEventUserName(int row){ return this.getText(eventUserFirstLastName.get(row)); }
    @Loggable
    public String getEventLogDate(int row){ return this.getText(eventLogDate.get(row)); }
    @Loggable
    public int getEventNumber(){ return eventNote.size(); }
}
