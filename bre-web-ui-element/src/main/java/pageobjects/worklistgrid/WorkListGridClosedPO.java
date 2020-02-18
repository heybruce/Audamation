package pageobjects.worklistgrid;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.log.Loggable;

public class WorkListGridClosedPO extends WorkListGridPO {

    public WorkListGridClosedPO() {
        super();
    }
    public WorkListGridClosedPO(WebDriver webDriver) {
        super(webDriver);
    }
    //work list
    @FindBy(id="ReopenTask")
    private WebElement btnReopenTasks;
    @FindBy(id="deleteTasks")
    private WebElement btnDeleteTasks;

    //Action Component in each row
    @FindBy(id="ReopenTask")
    private WebElement btnReopenTasksInRow;

    //Reopen dialog
    @FindBy(id="ReopenTaskPopup")
    private WebElement reopenTaskDialog;
    @FindBy(id="root.task.workflow.reopenTask.dialog-comment")
    private WebElement textfeildReopenComment;
    @FindBy(css="#ReopenTaskPopup div div div.modal-footer button:nth-of-type(3)")
    private WebElement btnReopenInDialog;

    @Loggable
    public void clickBtnReopenTasks(){
        this.click(btnReopenTasks);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(reopenTaskDialog, "class", "in"));
    }

    @Loggable
    public void clickBtnDeleteTasks(){ this.click(btnDeleteTasks); }

    @Loggable
    public void enterReopenComment(String comment){ this.sendKeys(textfeildReopenComment, comment);}

    @Loggable
    public void clickBtnReopenInDialog(){
        this.click(btnReopenInDialog);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickReopenTasksInRow(){
        this.click(btnReopenTasksInRow);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.attributeContains(reopenTaskDialog, "class", "in"));
    }
}
