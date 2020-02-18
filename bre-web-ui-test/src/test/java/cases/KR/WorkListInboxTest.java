package cases.KR;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkListInboxTest extends TestBase{
    private ProcessStepKRPO processStepKRPO;
    private ClaimDetailsKRPO claimDetails;
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
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsKRPO) context.getBean("ClaimDetailsKRPO");
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

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();
        //Get calculation result in Reports page
        CalculationList calculationList = new CalculationList();
        Map<String, String> calculationResult = calculationList.getCalculationResult(0);

        //Claim details page to get the car identification
        processStepKRPO.clickClaimDetailsTab();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Labor rate verification
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        String LR1, LR2, LR3, PR;
        LR1 = laborRatesPO.getLabourRate1();
        LR2 = laborRatesPO.getLabourRate2();
        LR3 = laborRatesPO.getLabourRate3();
        PR = laborRatesPO.getPaintRate();

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Verify claim before merge
        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
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
        processStepKRPO.clickAttachmentsRepairerTab();
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
        processStepKRPO.clickLabourRatesKRTab();
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), LR1);
        Assert.assertEquals(laborRatesPO.getLabourRate2(), LR2);
        Assert.assertEquals(laborRatesPO.getLabourRate3(), LR3);
        Assert.assertEquals(laborRatesPO.getPaintRate(), PR);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Modify spare parts verification
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Report calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultBeforeMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerged, calculationResult);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");

        //Go to inbox - merge task
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.mergeTheTask(claimNumber);

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertTrue(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
        Assert.assertEquals(claimDetails.getManufacturerCode(), testData.getString("bmw320_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("bmw320_manufacturer_code"));
        Assert.assertEquals(claimDetails.getModelCode(), testData.getString("bmw320_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("bmw320_model_code"));
        Assert.assertEquals(claimDetails.getSubModelCode(), testData.getString("bmw320_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("bmw320_submodel_code"));

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
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
        processStepKRPO.clickLabourRatesKRTab();
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), LR1);
        Assert.assertEquals(laborRatesPO.getLabourRate2(), LR2);
        Assert.assertEquals(laborRatesPO.getLabourRate3(), LR3);
        Assert.assertEquals(laborRatesPO.getPaintRate(), PR);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Modify spare parts verification
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Report calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, calculationResult);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Repairer merge Approved claim in Inbox")
    public void repairerMergeApprovedClaim(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Create a new case first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(insurer);

        //Logout
        workListGridPO.clickLogout();

        //Login as insurer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        //Merge claim from inbox
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.mergeTheTask(claimNumber);
        //Approve the task from tool bar
        toolBar.approveTheTask();
        toolBar.sendTaskWithAllCalculations();

        //Logout
        workListGridPO.clickLogout();

        //Login as repairer
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Approved_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Approved_KR") + "' in repairer's work list");

        //Merge the claim from inbox
        worklistToolBar.mergeTheTask(claimNumber);

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertTrue(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
        Assert.assertEquals(claimDetails.getManufacturerCode(), testData.getString("bmw320_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("bmw320_manufacturer_code"));
        Assert.assertEquals(claimDetails.getModelCode(), testData.getString("bmw320_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("bmw320_model_code"));
        Assert.assertEquals(claimDetails.getSubModelCode(), testData.getString("bmw320_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("bmw320_submodel_code"));

        //Labor rate verification
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        String LR1, LR2, LR3, PR;
        LR1 = laborRatesPO.getLabourRate1();
        LR2 = laborRatesPO.getLabourRate2();
        LR3 = laborRatesPO.getLabourRate3();
        PR = laborRatesPO.getPaintRate();

        Assert.assertFalse(LR1.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 1: " + LR1);
        Assert.assertFalse(LR2.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 2: " + LR2);
        Assert.assertFalse(LR3.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 3: " + LR3);
        Assert.assertFalse(PR.isEmpty());
        testCase.get().log(Status.PASS, "Paint Rate: " + PR);

        //Modify spare parts verification
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Report calculation verification
        processStepKRPO.clickReportsTab();
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS, "There is a calculation in merged claim");

        //Go to open box and check the status
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickOpenTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Approved_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Approved_KR") + "' in repairer's work list");
    }

    @Test(description = "Repairer merge Rejected claim in Inbox")
    public void repairerMergeRejectedClaim(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Create a new case first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(insurer);

        //Logout
        workListGridPO.clickLogout();

        //Login as insurer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        //Merge claim from inbox
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.mergeTheTask(claimNumber);
        //Reject the task from tool bar
        toolBar.rejectTheTask();
        toolBar.sendTaskWithAllCalculations();

        //Logout
        workListGridPO.clickLogout();

        //Login as repairer
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Rejected_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Rejected_KR") + "' in repairer's work list");

        //Merge the claim from inbox
        worklistToolBar.mergeTheTask(claimNumber);

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertTrue(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
        Assert.assertEquals(claimDetails.getManufacturerCode(), testData.getString("bmw320_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("bmw320_manufacturer_code"));
        Assert.assertEquals(claimDetails.getModelCode(), testData.getString("bmw320_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("bmw320_model_code"));
        Assert.assertEquals(claimDetails.getSubModelCode(), testData.getString("bmw320_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("bmw320_submodel_code"));

        //Labor rate verification
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        String LR1, LR2, LR3, PR;
        LR1 = laborRatesPO.getLabourRate1();
        LR2 = laborRatesPO.getLabourRate2();
        LR3 = laborRatesPO.getLabourRate3();
        PR = laborRatesPO.getPaintRate();

        Assert.assertFalse(LR1.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 1: " + LR1);
        Assert.assertFalse(LR2.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 2: " + LR2);
        Assert.assertFalse(LR3.isEmpty());
        testCase.get().log(Status.PASS, "Labour Rate 3: " + LR3);
        Assert.assertFalse(PR.isEmpty());
        testCase.get().log(Status.PASS, "Paint Rate: " + PR);

        //Modify spare parts verification
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Report calculation verification
        processStepKRPO.clickReportsTab();
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS, "There is a calculation in merged claim");

        //Go to open box and check the status
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickOpenTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Rejected_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Rejected_KR") + "' in repairer's work list");
    }

    @Test(description = "Change displayed columns of work list")
    public void changeDisplayedColumnsOfWorkListInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to Inbox
        workListGridPO.clickClaimManager();
        workListGridPO.clickCopiedTab();
        testCase.get().log(Status.INFO, "Go to Inbox");

        //Un-select first checked item in Customize Menu to remove it from work list column
        String uncheckedColumnName = workListGridClosedPO.clickFirstCheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first checked item on customize menu");
        Assert.assertFalse(workListGridClosedPO.isColumnDisplayedByName(uncheckedColumnName));
        testCase.get().log(Status.PASS, uncheckedColumnName + " column is removed from work list");

        //Select first unchecked item in Customize Menu to add it into work list column
        String checkedColumnName = workListGridClosedPO.clickFirstUncheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first unchecked item on customize menu");
        Assert.assertTrue(workListGridClosedPO.isColumnDisplayedByName(checkedColumnName));
        testCase.get().log(Status.PASS, checkedColumnName + " column is added to work list");
    }

    @Test(description = "Search claim by number")
    public void searchCaseByClaimNumberInInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = claimDetails.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to Inbox
        workListGridPO.clickClaimManager();
        workListGridPO.clickCopiedTab();
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
