package pageobjects.worklistgrid;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PageObject;
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import utils.log.Loggable;

import java.util.List;

public class WorkListGridPO extends PageObject{

    protected static final By LOADING_CIRCLE = By.cssSelector(".loading-component ");
    public static final String NEW_CASE_BTN_ID = "newCaseBtn";
    private static final String ATTRIBUTE_ASC = "z-asc";
    private static final String ATTRIBUTE_DESC = "z-desc";
    public static final String ID_OPEN_TAB = "a[id='view-link-worklistgrid_open']";
    public static final String ID_CLOSED_TAB = "a[id='view-link-worklistgrid_closed']";
    public static final By CLASS_WORKLIST_TOOLBAR = By.cssSelector(".work-list-toolbar");
    private static final String GET_ATTRIBUTE_CLASS = "class";

    @FindBy(id = NEW_CASE_BTN_ID)
    private WebElement newClaim;
    @FindBy(className = "logged-user-name")
    private WebElement loggedUsername;

    //Tabs
    @FindBy(css = "a[id='view-link-worklistgrid_copied']")
    private WebElement copiedTab;
    @FindBy(css = ID_OPEN_TAB)
    private WebElement openTab;
    @FindBy(css = ID_CLOSED_TAB)
    private WebElement closedTab;
    @FindBy(css = "a[id='view-link-worklistgrid_custom_open']")
    private WebElement customOpenTab;
    @FindBy(css = "a[id='view-link-worklistgrid_custom_sent']")
    private WebElement customSentTab;

    //Work list toolbar buttons
    @FindBy(id = "WorklistGridSearchPopupComponent")
    private WebElement btnSearchInWorkList;

    //Menu items on left
    @FindBy(id = "claimManager")
    private WebElement claimManager;
    @FindBy(id = "messagelist")
    private WebElement messages;
    @FindBy(id = "settings")
    private WebElement settings;
    @FindBy(id = "changepassword")
    private WebElement changePassword;
    @FindBy(id = "support")
    private WebElement support;
    @FindBy(id = "logout")
    private WebElement logout;
    @FindBy(id="worklist")
    private WebElement worklist;
    @FindBy(css = ".collapsed-menu button")
    private WebElement collapsedMenu;

    //Table header
    @FindBy(css= ".worklist-grid-component")
    private WebElement workListTable;
    @FindBy(id = "header-title-containercreationDate")
    private WebElement headerCreationDate;
    @FindBy(id = "header-title-containerplateNumber")
    private WebElement headerPlateNumber;
    @FindBy(id = "header-title-containerclaimNumber")
    private WebElement headerClaimNumber;
    @FindBy(id = "header-title-containerlastEditedDateTime")
    private WebElement headerLastEditedDateTime;
    @FindBy(id = "header-title-containerbusinessStatusKind")
    private WebElement headerBusinessStatusKind;
    @FindBy(id = "header-title-containermanufacturerName")
    private WebElement headerManufacturer;
    @FindBy(id = "header-title-containermodelName")
    private WebElement headerModel;
    @FindBy(id = "header-title-containerresponsibleUserLoginId")
    private WebElement headerResponsibleUserLoginId;
    @FindBy(className = "column-selection-open-component")
    private WebElement headerCustomize;
    @FindBy(css = ".z-column-header.z-sortable .z-content .z-header-text")
    private List<WebElement> allDisplayedHeaderNames;
    @FindBy(css = "label[for=\"grid-header-select-all\"]")
    private WebElement selectAll;
    @FindBy(css = "label[for=\"grid-header-deselect-all\"]")
    private WebElement deselectAll;

    //Table content
    @FindBy(name = "plateNumber")
    private List<WebElement> plateNumber;
    @FindBy(name = "claimNumber")
    private List<WebElement> claimNumber;
    @FindBy(name = "creationDate")
    private List<WebElement> creationDate;
    @FindBy(name = "lastEditedDateTime")
    private List<WebElement> lastEditedDateTime;
    @FindBy(name = "businessStatusKind")
    private List<WebElement> businessStatusKind;
    @FindBy(name = "manufacturerName")
    private List<WebElement> manufacturerName;
    @FindBy(name = "modelName")
    private List<WebElement> modelName;
    @FindBy(name = "responsibleUserLoginId")
    private List<WebElement> responsibleUserLoginId;
    @FindBy(name = "displayName")
    private List<WebElement> repairReferenceNumber;
    @FindBy(name = "senderLoginId")
    private List<WebElement> sender;
    @FindBy(css = "input[data-checked=\"true\"]")
    private List<WebElement> checkedCheckbox;

    //Action Component in each row
    @FindBy(css = ".more-row-icon-wrapper")
    private List<WebElement> btnAction;

    //Items in Column Customize menu
    @FindBy(id = "menu-row-businessStatusKind")
    private WebElement menuBizStatus;
    @FindBy(id = "menu-row-senderLoginId")
    private WebElement menuSender;
    @FindBy(id = "menu-row-responsibleUserLoginId")
    private WebElement menuResponsibleUser;
    @FindBy(xpath = "//*[@id=\"index-absolute-div\"]/div/div[2]/div[1]/span")
    private WebElement closeCustomizeMenu;
    @FindBy(css = ".column-selection-table .z-selected.menu-row")
    private List<WebElement> checkedItems;
    @FindBy(css = ".column-selection-table :not(.z-selected).menu-row")
    private List<WebElement> uncheckedItems;

    //Search dialog
    @FindBy(css = "#GridSearch .modal-footer > button:nth-child(1)")
    private WebElement btnClear;
    @FindBy(id = "root.wfGrid.workList.search.claimNumber")
    private WebElement claimNumberField;
    @FindBy(css = "#GridSearch .modal-footer > button:nth-child(3)")
    private WebElement btnSearch;

    //No case exist
    @FindBy(className="no-items-title")
    private WebElement noCaseMessage;
    private boolean noCaseExistInWorklist;

    protected static final By NOTIFICATION_POPUP = By.cssSelector(".notification-component");


    public WorkListGridPO() { super(); }

    public WorkListGridPO(WebDriver webDriver) {
        super(webDriver);
    }

    @Loggable
    public String getLoggedUsername() { return this.getText(loggedUsername); }

    @Loggable
    public int getCurrentNumberOfClaims() {
        //Return number of records on current page
        return claimNumber.size();
    }

    @Loggable
    public void clickNewClaimButton() {
        this.click(newClaim);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickClaimManager() {
        openCollapsedMenu();
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(claimManager));
        this.click(claimManager);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void clickMessages() { this.click(messages); }

    @Loggable
    public void clickSettings() { this.click(settings); }

    @Loggable
    public void clickChangePassword() { this.click(changePassword); }

    @Loggable
    public void clickSupport() { this.click(support); }

    @Loggable
    public void clickLogout() {
        openCollapsedMenu();
        this.click(logout); }

    @Loggable
    public void clickWorklist(){
        openCollapsedMenu();
        this.click(worklist);
    }

    @Loggable
    public void clickCopiedTab() {
        this.click(copiedTab);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForWorklistTableLoaded();
    }

    @Loggable
    public void clickOpenTab() {
        this.click(openTab);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForWorklistTableLoaded();
    }

    @Loggable
    public void clickCustomOpenTab() {
        this.click(customOpenTab);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForWorklistTableLoaded();
    }

    @Loggable
    public void clickCustomSentTab() {
        this.click(customSentTab);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForWorklistTableLoaded();
    }

    @Loggable
    public void clickClosedTab() {
        this.click(closedTab);
        waitForElementInvisible(LOADING_CIRCLE);
        waitForWorklistTableLoaded();
    }

    @Loggable
    public void clickSearchButtonInWorkList() {
        this.click(btnSearchInWorkList);
        new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOf(claimNumberField));
    }

    //Search actions
    @Loggable
    public void clearSearchDataInDialog() {
        this.click(btnClear);
    }

    @Loggable
    public void enterClaimNumber(String claimNumber) {
        this.sendKeys(claimNumberField, claimNumber);
    }

    @Loggable
    public void clickBtnSearchInDialog() {
        this.click(btnSearch);
        waitForElementInvisible(LOADING_CIRCLE);
    }

    @Loggable
    public void sortCreationDate(){
        sortingByHeader(headerCreationDate);
    }

    @Loggable
    public void sortLastUpdatedDate(){
        sortingByHeader(headerLastEditedDateTime);
    }

    private void sortingByHeader(WebElement header){
        //If there is no any case exists, do nothing
        if (noCaseExistInWorklist)
            return;
        //Get the class attribute of grand parent element of header-creationDate
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(header));
        String grandParentAttribute = header.findElement(By.xpath("../..")).getAttribute("class");
        //Check if the header-creationDate has never been sorted, click twice to sort by desc
        if (!grandParentAttribute.contains(ATTRIBUTE_ASC) && !grandParentAttribute.contains(ATTRIBUTE_DESC)) {
            this.click(header);
            waitForElementInvisible(LOADING_CIRCLE);
            this.click(header);
            waitForElementInvisible(LOADING_CIRCLE);
        } else if (grandParentAttribute.contains(ATTRIBUTE_ASC)) {
            //Check if the header-creationDate has been sorted and is order by asc, click header to sort by desc
            this.click(header);
            waitForElementInvisible(LOADING_CIRCLE);
        }
    }

    @Loggable
    public boolean isClaimNumberExist(String findClaimNumber){
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().equals(findClaimNumber))
                return true;
        }
        return false;
    }

    @Loggable
    public boolean isRepairReferenceNumberExist(String findDisplayName){
        for(WebElement ele: repairReferenceNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().equals(findDisplayName))
                return true;
        }
        return false;
    }

    @Loggable
    public void openClaimByClaimNumber(String findClaimNumber){
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().equals(findClaimNumber)) {
                new Actions(webDriver).doubleClick(ele).perform();
                this.waitForElementPresent(By.id(ClaimDetailsKRPO.ID_CLAIM_NUMBER));
                return;
            }
        }
    }

    @Loggable
    public void openClaimByClaimNumberOnIOS(String findClaimNumber){
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().equals(findClaimNumber)) {
                this.click(ele);
                this.waitForElementPresent(By.id(ClaimDetailsKRPO.ID_CLAIM_NUMBER));
                return;
            }
        }
    }

    @Loggable
    public void openClaimByRow(int row){
        new Actions(webDriver).doubleClick(claimNumber.get(row)).perform();
        this.waitForElementPresent(By.id(ClaimDetailsKRPO.ID_CLAIM_NUMBER));
    }

    @Loggable
    public int findRowOfTheClaimByClaimNumber(String findClaimNumber){
        int row = 0;
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().contains(findClaimNumber)){
                return row;
            }else{
                row++;
            }
        }
        return row;
    }

    @Loggable
    public int findRowOfTheClaimByBizStatus(String bizStatus){
        int row = 0;
        for(WebElement ele: businessStatusKind) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().contains(bizStatus)){
                return row;
            }else{
                row++;
            }
        }
        if(row==businessStatusKind.size())
            row=-1;
        return row;
    }

    @Loggable
    public int findRowOfTheClaimByRepairReferenceNumber(String findDisplayName){
        int row = 0;
        for(WebElement ele: repairReferenceNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().contains(findDisplayName)){
                return row;
            }else{
                row++;
            }
        }
        return row;
    }

    private WebElement getCssOfClaimCheckbox(int row){ return webDriver.findElement(By.cssSelector("label[for=\"grid-row-"+row+"\"]")); }

    @Loggable
    public void clickClaimCheckbox(String claimNumber){
        // Click select all box twice to uncheck all checked checkboxes left by previous failed tests
        if (checkedCheckbox.size() > 0){
            this.click(selectAll);
            this.click(deselectAll);
        }
        this.click(getCssOfClaimCheckbox(findRowOfTheClaimByClaimNumber(claimNumber)));
    }

    @Loggable
    public String getClaimBizStatus(String findClaimNumber){
        // display business status column if it is hidden
        if(businessStatusKind.size() == 0){
            clickCustomizeItem(menuBizStatus);
        }
        int row = 0;
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().contains(findClaimNumber)){
                return businessStatusKind.get(row).getText();
            }else{
                row++;
            }
        }
        return null;
    }

    @Loggable
    private void clickCustomizeItem(WebElement element){
        this.click(headerCustomize);
        // To avoid scrolling into view failure on Firefox
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", element);
        new Actions(webDriver).moveToElement(element).click().perform();
        this.click(closeCustomizeMenu);
    }

    @Loggable
    private String clickCustomizeItemByOrder(List<WebElement> items, int order) {
        this.click(headerCustomize);
        String itemName = items.get(order).getText();
        // To avoid scrolling into view failure on Firefox
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", items.get(order));
        new Actions(webDriver).moveToElement(items.get(order)).click().perform();
        this.click(closeCustomizeMenu);
        // Return clicked item name which is also the column name displayed on work list
        return itemName;
    }

    @Loggable
    public String clickFirstCheckedCustomizeItem() {
        return clickCustomizeItemByOrder(checkedItems, 0);
    }

    @Loggable
    public String clickFirstUncheckedCustomizeItem() {
        return clickCustomizeItemByOrder(uncheckedItems, 0);
    }

    @Loggable
    public boolean isColumnDisplayedByName(String columnName) {
        for (WebElement ele: allDisplayedHeaderNames) {
            if (ele.getText().equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    //Action Component
    @Loggable
    public void clickActionBtnInRow(String claimNumber){
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", btnAction.get(findRowOfTheClaimByClaimNumber(claimNumber)));
        this.click(btnAction.get(findRowOfTheClaimByClaimNumber(claimNumber)));
    }

    @Loggable
    public String getClaimNumberByRow(int row){ return claimNumber.get(row).getText(); }

    @Loggable
    public boolean isCollapsedMenuHidden(){
        boolean isHidden = false;
        if(collapsedMenu.getAttribute(GET_ATTRIBUTE_CLASS).contains("menu-hidden"))
            isHidden = true;
        return isHidden;
    }

    @Loggable
    public void openCollapsedMenu(){
        if(isCollapsedMenuHidden()) {
            try {
                this.click(collapsedMenu);
                new WebDriverWait(webDriver, 5).until(ExpectedConditions.attributeContains(collapsedMenu, GET_ATTRIBUTE_CLASS, "menu-open"));
            } catch (TimeoutException e) {
                //Retry click
                this.click(collapsedMenu);
            }
        }
    }

    @Loggable
    public String getSenderName(String findClaimNumber){
        // display business status column if it is hidden
        if(sender.size() == 0){
            clickCustomizeItem(menuSender);
        }
        int row = 0;
        for(WebElement ele: claimNumber) {
            String getClaimNumber = ele.getText();
            if(getClaimNumber.trim().contains(findClaimNumber)){
                return sender.get(row).getText();
            }else{
                row++;
            }
        }
        return null;
    }

    @Loggable
    private void waitForWorklistTableLoaded() {
        try {
            new WebDriverWait(webDriver, 10).until(ExpectedConditions.visibilityOf(workListTable));
            noCaseExistInWorklist = false;
        } catch (TimeoutException e) {
            new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOf(noCaseMessage));
            // no case exist
            noCaseExistInWorklist = true;
        }
    }
}
