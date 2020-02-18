package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkListInboxIDTest extends TestBase {
    private ProcessStepIDPO processStepIDPO;
    private ClaimDetailsPO claimDetailsPO;
    private ReportsPO reportsPO;
    private AttachmentsRepairerPO attachmentPO;
    private DamageCapturingPO damageCapturingPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private WorkListGridPO workListGridPO;
    private LaborRatesPO laborRatesPO;
    private ModifySparePartsPO modifySparePartsPO;
    private WorkListGridOpenPO workListGridOpenPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        workListGridPO = (WorkListGridPO) context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
        claimDetailsPO = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO) context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO) context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO) context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO) context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        modifySparePartsPO = (ModifySparePartsPO) context.getBean("ModifySparePartsPO");
        modifySparePartsPO.setWebDriver(getDriver());
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
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzS_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();
        //Get calculation result
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResult_expected = calculationList.getCalculationResult(0);
        testCase.get().log(Status.INFO, "Get last calculation result");

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
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
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Verify claim before merge
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
        //Claim details
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertFalse(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct but not able to edit before merge");
        Assert.assertEquals(claimDetailsPO.getManufacturer(), manufacturer);
        testCase.get().log(Status.PASS, "Manufacturer: " + manufacturer);
        Assert.assertEquals(claimDetailsPO.getModel(), model);
        testCase.get().log(Status.PASS, "Model: " + model);
        Assert.assertEquals(claimDetailsPO.getSubModel(), submodel);
        testCase.get().log(Status.PASS, "Sub Model: " + submodel);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is editable");
        Assert.assertEquals(claimDetailsPO.getManufacturerCode(), testData.getString("benzS_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("benzS_manufacturer_code"));
        Assert.assertEquals(claimDetailsPO.getModelCode(), testData.getString("benzS_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("benzS_model_code"));
        Assert.assertEquals(claimDetailsPO.getSubModelCode(), testData.getString("benzS_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("benzS_submodel_code"));

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithSubmittedStatus(testData.getString("benzS_vehicle"), true);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertFalse(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct but not able to edit before merge");
        Assert.assertEquals(claimDetailsPO.getManufacturer(), manufacturer);
        testCase.get().log(Status.PASS, "Manufacturer: " + manufacturer);
        Assert.assertEquals(claimDetailsPO.getModel(), model);
        testCase.get().log(Status.PASS, "Model: " + model);
        Assert.assertEquals(claimDetailsPO.getSubModel(), submodel);
        testCase.get().log(Status.PASS, "Sub Model: " + submodel);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

//        //Modify spare parts verification (mark for ID hide modify spare part now)
//        processStepIDPO.clickModifySparePartsID();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculate button is visible and enable");
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Insurer merge \"Inspected\" claim")
    public void insurerMergeInspectedClaimFromWorkList() throws URISyntaxException{
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Insure assign a submitted claim to surveyor merged by surveyor (claim status: Inspecting)
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithInspectingStatus(testData.getString("benzS_vehicle"), true);

        //Login as surveyor
        Login login = new Login();
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + surveyor);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertFalse(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct but not able to edit before merge");
        Assert.assertEquals(claimDetailsPO.getManufacturer(), manufacturer);
        testCase.get().log(Status.PASS, "Manufacturer: " + manufacturer);
        Assert.assertEquals(claimDetailsPO.getModel(), model);
        testCase.get().log(Status.PASS, "Model: " + model);
        Assert.assertEquals(claimDetailsPO.getSubModel(), submodel);
        testCase.get().log(Status.PASS, "Sub Model: " + submodel);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

//        //Modify spare parts verification (mark for ID hide modify spare part now)
//        processStepIDPO.clickModifySparePartsID();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculate button is visible and enable");
        Map<String, String> calculationResult_afterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResult_afterMerge, calculationResult_expected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Repairer merge \"Pending agreement\" claim")
    public void repairerMergePendingAgreementClaimFromWorkList() throws URISyntaxException{
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");

        //Surveyor send claim to insurer and merged by insurer (claim status: Inspected)
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithInspectedStatus(testData.getString("benzS_vehicle"), true);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Get calculation result
        processStepIDPO.clickReportsID();
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

        //Login as repairer
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertFalse(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct but not able to edit before merge");
        Assert.assertEquals(claimDetailsPO.getManufacturer(), manufacturer);
        testCase.get().log(Status.PASS, "Manufacturer: " + manufacturer);
        Assert.assertEquals(claimDetailsPO.getModel(), model);
        testCase.get().log(Status.PASS, "Model: " + model);
        Assert.assertEquals(claimDetailsPO.getSubModel(), submodel);
        testCase.get().log(Status.PASS, "Sub Model: " + submodel);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertFalse(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate cannot be edited");
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

//        //Modify spare parts verification (mark for ID hide modify spare part now)
//        processStepIDPO.clickModifySparePartsID();
//        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
//        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
//        testCase.get().log(Status.PASS, "There is an added part in modify spare parts list");

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
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
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Labor rate can be edited");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
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
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

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
        String uncheckedColumnName = workListGridPO.clickFirstCheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first checked item on customize menu");
        Assert.assertFalse(workListGridPO.isColumnDisplayedByName(uncheckedColumnName));
        testCase.get().log(Status.PASS, uncheckedColumnName + " column is removed from work list");

        //Select first unchecked item in Customize Menu to add it into work list column
        String checkedColumnName = workListGridPO.clickFirstUncheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first unchecked item on customize menu");
        Assert.assertTrue(workListGridPO.isColumnDisplayedByName(checkedColumnName));
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
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));
        String claimNumber = claimDetailsPO.getClaimNumber();
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
