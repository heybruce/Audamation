package cases.SG;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsSGPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import pageobjects.worklistgrid.WorkListGridClosedPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorklistInboxSGTest extends TestBase {
    private ProcessStepSGPO processStepSGPO;
    private ClaimDetailsSGPO claimDetails;
    private ToolBarPO toolBarPO;
    private ReportsPO reportsPO;
    private LaborRatesPO laborRatesPO;
    private AttachmentsRepairerPO attachmentPO;
    private ModifySparePartsPO modifySparePartsPO;
    private WorkListGridPO workListGridPO;
    private WorkListGridClosedPO workListGridClosedPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private WorkListGridOpenPO workListGridOpenPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }
    @BeforeMethod
    public void methodSetup() {
        processStepSGPO = (ProcessStepSGPO) context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsSGPO) context.getBean("ClaimDetailsSGPO");
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
        workListGridOpenPO = (WorkListGridOpenPO) context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
    }

    @Test(description = "Insurer merges \"Submitted\" claim from work list")
    public void insurerMergeSubmittedClaimFromWorkList() throws URISyntaxException {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String receiver = testData.getString("ins_username");
        String sender = testData.getString("rep_username");
        String receiverOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("bmw320_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();

        //Get the last calculation result
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_expected = calculationList.getCalculationResult(0);
        testCase.get().log(Status.INFO, "Get last calculation result");

        //General details page to get the car identification
        processStepSGPO.clickGeneralDetailsSG();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Get Labor rate values
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Work list - send task
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.submitTaskWithAllAttachments(claimNumber, receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Verify claim before merge
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
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
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "Calculate button is invisible");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_beforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_beforeMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Go to inbox - merge task
        workListGridPO.clickWorklist();
        workListGridPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        Assert.assertTrue(claimDetails.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is editable");
        Assert.assertEquals(claimDetails.getManufacturerCode(), testData.getString("bmw320_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("bmw320_manufacturer_code"));
        Assert.assertEquals(claimDetails.getModelCode(), testData.getString("bmw320_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("bmw320_model_code"));
        Assert.assertEquals(claimDetails.getSubModelCode(), testData.getString("bmw320_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("bmw320_submodel_code"));

        //Modify spare parts verification (mark for SG hide modify spare part now)
//        processStepSGPO.clickModifySparePartsSG();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculate button is visible and enable");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Surveyor merge \"Inspecting\" claim from work list")
    public void surveyorMergeInspectingClaimFromWorkList() throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String surveyorOrg = testData.getString("svyr_orgname");

        //Repairer send claim to insurer and merged by insurer (claim status: Submitted)
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        String claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("bmw320_vehicle"), true);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //General details page to get the car identification
        processStepSGPO.clickGeneralDetailsSG();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Get Labor rate values
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_expected = calculationList.getCalculationResult(0);
        testCase.get().log(Status.INFO, "Get calculation result");

        //Work list - send task
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.assignTask(claimNumber, surveyorOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + surveyor);

        //Verify claim before merge
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspecting"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspecting"));
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
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "Calculate button is invisible");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_beforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_beforeMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Go to inbox - merge task
        workListGridPO.clickWorklist();
        workListGridPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Modify spare parts verification (mark for SG hide modify spare part now)
//        processStepSGPO.clickModifySparePartsSG();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculate button is visible and enable");
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Insurer merge \"Inspected\" claim")
    public void insurerMergeInspectedClaimFromWorkList() throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Insure assign a submitted claim to surveyor merged by surveyor (claim status: Inspecting)
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        String claimNumber = createNewCase.createCaseWithInspectingStatus(testData.getString("bmw320_vehicle"), true);

        //Login as surveyor
        Login login = new Login();
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + surveyor);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //General details page to get the car identification
        processStepSGPO.clickGeneralDetailsSG();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Get Labor rate values
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_expected = calculationList.getCalculationResult(0);
        testCase.get().log(Status.INFO, "Get calculation result");

        //Work list - send task
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.sendAssessmentToInsurer(claimNumber);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as insurer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);

        //Verify claim before merge
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Inspected"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Inspected"));
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
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "Calculate button is invisible");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_beforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_beforeMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Go to inbox - merge task
        workListGridPO.clickWorklist();
        workListGridPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Modify spare parts verification (mark for SG hide modify spare part now)
//        processStepSGPO.clickModifySparePartsSG();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculate button is visible and enable");
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Repairer merge \"Pending agreement\" claim")
    public void repairerMergePendingAgreementClaimFromWorkList() throws URISyntaxException, InterruptedException {
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String repairerOrg = testData.getString("rep_orgname");

        //Surveyor send claim to insurer and merged by insurer (claim status: Inspected)
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        String claimNumber = createNewCase.createCaseWithInspectedStatus(testData.getString("bmw320_vehicle"), true);

        //Login as surveyor
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //General details page to get the car identification
        processStepSGPO.clickGeneralDetailsSG();
        String manufacturer, model, submodel;
        manufacturer = claimDetails.getManufacturer();
        model = claimDetails.getModel();
        submodel = claimDetails.getSubModel();

        //Get Labor rate values
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_expected = calculationList.getCalculationResult(0);
        testCase.get().log(Status.INFO, "Get calculation result");

        //Work list - send task
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.sendEstimate(claimNumber);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as insurer
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + repairer);

        //Verify claim before merge
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Pending_Agreement"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Pending_Agreement"));
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
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "Calculate button is invisible");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_beforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_beforeMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Go to inbox - merge task
        workListGridPO.clickWorklist();
        workListGridPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        Assert.assertEquals(claimNumber, claimDetails.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

        //Modify spare parts verification (mark for SG hide modify spare part now)
//        processStepSGPO.clickModifySparePartsSG();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepSGPO.clickAttachmentsSG();
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
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "Calculate button is invisible for pending agreement status");
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Change displayed columns of work list")
    public void changeDisplayedColumnsOfWorkListInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("rep_username");
        String receiver = testData.getString("ins_username");
        String receiverOrg = testData.getString("ins_orgname");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("bmw320_vehicle"));

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to Inbox
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

    @Test(description = "Search claim by claim number")
    public void searchCaseByClaimNumberInInbox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("rep_username");
        String receiver = testData.getString("ins_username");
        String receiverOrg = testData.getString("ins_orgname");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("bmw320_vehicle"));
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
