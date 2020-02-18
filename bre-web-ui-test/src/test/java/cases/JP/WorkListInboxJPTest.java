package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsJPPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkListInboxJPTest extends TestBase {
    private ProcessStepJPPO processStepJPPO;
    private ClaimDetailsJPPO claimDetails;
    private ToolBarPO toolBarPO;
    private ReportsPO reportsPO;
    private LaborRatesPO laborRatesPO;
    private AttachmentsRepairerPO attachmentPO;
    private ModifySparePartsPO modifySparePartsPO;
    private WorkListGridPO workListGridPO;
    private WorkListGridClosedPO workListGridClosedPO;
    private WorkListGridInboxPO workListGridInboxPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepJPPO = (ProcessStepJPPO) context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsJPPO) context.getBean("ClaimDetailsJPPO");
        claimDetails.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO)context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        laborRatesPO  = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        modifySparePartsPO = (ModifySparePartsPO)context.getBean("ModifySparePartsPO");
        modifySparePartsPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        workListGridClosedPO = (WorkListGridClosedPO)context.getBean("WorkListGridClosedPO");
        workListGridClosedPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
    }

    @Test(description = "Work list - Merge a RFQ Received task")
    public void mergeREQReceivedCase() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzE_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();
        //Get calculation result in Reports page
        CalculationList calculationList = new CalculationList();
        Map<String, String> calculationResult = calculationList.getCalculationResult(0);

        //Back to Claim details page to get the car identification
        processStepJPPO.clickClaimInfoTab();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Get Labor rate values
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Verification before merge
        //Claim details
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertFalse(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct but not able to edit before merge");
        Assert.assertEquals(claimDetails.getManufacturer(), manufacturer);
        testCase.get().log(Status.PASS, "Manufacturer: " + manufacturer);
        Assert.assertEquals(claimDetails.getModel(), model);
        testCase.get().log(Status.PASS, "Model: " + model);
        Assert.assertEquals(claimDetails.getSubModel(), submodel);
        testCase.get().log(Status.PASS, "Sub Model: " + submodel);

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "Attachments are all included");

        //Labor rate verification
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Modify spare parts verification
        processStepJPPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Display all columns for calculation list
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, calculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        //Go to inbox - merge task
        processStepJPPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.mergeTheTask(claimNumber);

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertTrue(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
        Assert.assertEquals(claimDetails.getManufacturerCode(), testData.getString("benzE_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("benzE_manufacturer_code"));
        Assert.assertEquals(claimDetails.getModelCode(), testData.getString("benzE_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("benzE_model_code"));
        Assert.assertEquals(claimDetails.getSubModelCode(), testData.getString("benzE_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("benzE_submodel_code"));

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "Attachments are all included");

        //Labor rate verification
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Switch to calculations page and verify
        processStepJPPO.clickCalculationsTab();
        //Display all columns for calculation list
        testCase.get().log(Status.INFO, "Switch to Calculations page");
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, calculationResult);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Change displayed columns of work list")
    public void changeDisplayedColumnsOfWorkListInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to Inbox
        workListGridInboxPO.clickCopiedTab();
        testCase.get().log(Status.INFO, "Go to Inbox");

        //Un-select first checked item in Customize Menu to remove it from work list column
        String uncheckedColumnName = workListGridInboxPO.clickFirstCheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first checked item on customize menu");
        Assert.assertFalse(workListGridInboxPO.isColumnDisplayedByName(uncheckedColumnName));
        testCase.get().log(Status.PASS, uncheckedColumnName + " column is removed from work list");

        //Select first unchecked item in Customize Menu to add it into work list column
        String checkedColumnName = workListGridInboxPO.clickFirstUncheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first unchecked item on customize menu");
        Assert.assertTrue(workListGridInboxPO.isColumnDisplayedByName(checkedColumnName));
        testCase.get().log(Status.PASS, checkedColumnName + " column is added to work list");
    }

    @Test(description = "Search claim by number")
    public void searchCaseByClaimNumberInInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        String claimNumber = claimDetails.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to Inbox
        workListGridInboxPO.clickCopiedTab();
        testCase.get().log(Status.INFO, "Go to Inbox");

        //Search case by claim number
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.searchByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Search case by claim number");

        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        Assert.assertEquals(workListGridInboxPO.getCurrentNumberOfClaims(), 1);
        testCase.get().log(Status.PASS, claimNumber + " is found in search result");
    }
}
