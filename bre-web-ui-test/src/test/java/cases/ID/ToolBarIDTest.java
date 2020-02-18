package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import jcifs.smb.SmbFile;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.LoginPO;
import pageobjects.PreIntakePO;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class ToolBarIDTest extends TestBase{
    private LoginPO loginPO;
    private WorkListGridOpenPO workListGridOpenPO;
    private PreIntakePO preIntakePO;
    private ProcessStepIDPO processStepIDPO;
    private ClaimDetailsPO claimDetailsPO;
    private ReportsPO reportsPO;
    private AttachmentsRepairerPO attachmentPO;
    private DamageCapturingPO damageCapturingPO;
    private WorkListGridPO workListGridPO;
    private LaborRatesPO laborRatesPO;
    private ModifySparePartsPO modifySparePartsPO;
    private ToolBarPO toolBarPO;
    private WorkListGridInboxPO workListGridInboxPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(Method method, ITestContext testContext) throws IOException {
        loginPO = (LoginPO) context.getBean("LoginPO");
        loginPO.setWebDriver(getDriver());
        workListGridOpenPO = (WorkListGridOpenPO) context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO) context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
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
        workListGridPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO) context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        modifySparePartsPO = (ModifySparePartsPO) context.getBean("ModifySparePartsPO");
        modifySparePartsPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO) context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());

        //Setup download path
        setupDownloadPath(method);
    }

    @Test(description = "Print PDF report (Standard) from tool bar")
    public void printPdfStandard() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        int templateNumber = 1;
        String fileName = "AutoTest_PDF_Template_Standard";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(templateNumber, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF report (Full Report) from tool bar")
    public void printPdfFullReport() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        int templateNumber = 2;
        String fileName = "AutoTest_PDF_Template_FullReport";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(templateNumber, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Insurer send case with latest calculation and all attachments")
    public void sendCaseWithLatestCalculationAllAttachmentsFromToolbar() throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Get the last calculation result
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

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
        testCase.get().log(Status.PASS, "Attachments and thumbnails are all found");

        //Calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is sent successfully");

        toolBar.mergeTheTask();

        // Enter repairer reference number
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

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
        testCase.get().log(Status.PASS, "Attachments and thumbnails are all found");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is merged successfully");
    }

    @Test(description = "Close the task from tool bar")
    public void closeTheTask() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsPO.getClaimNumber();

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");

        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The closed claim is in close box");
    }

    @Test(description = "Change Owner from tool bar")
    public void changeOwner() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsPO.getClaimNumber();

        //Tool bar - change owner
        String newOwner = testData.getString("rep_change_owner_username");
        ToolBar toolBar = new ToolBar();
        toolBar.changeOwnerTo(newOwner);

        //Check the claim is not in open box
        workListGridOpenPO.clickCustomOpenTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertFalse(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The changed owner claim is not in open box anymore");

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(newOwner, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as the new owner " + newOwner);

        //Go to open box
        workListGridOpenPO.clickCustomOpenTab();

        //Check claim is in the new owner's open box
        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The changed owner claim is in new owner's open box");
    }

    @Test(description = "Reopen closed claim from tool bar")
    public void reopenTaskForClosedClaimFromToolBar(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsPO.getClaimNumber();

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        //Close box - open the claim
        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");
        workListGridPO.sortCreationDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Tool bar - reopen the task
        toolBar.reopenTheTask();
        workListGridPO.sortCreationDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reopen claim does not exist in Close box");

        workListGridPO.clickCustomOpenTab();
        testCase.get().log(Status.INFO, "Switch to Open box");

        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reopen claim exists in Open box");
    }

    @Test(description = "Insurer merge 'Submitted' claim")
    public void insurerMergeSubmittedClaim() throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer, testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");
        //Get Calculation result in Reports page
        CalculationList calculationList = new CalculationList();
        Map<String, String> calculationResultExpected = calculationList.getCalculationResult(0);

        //Back to Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();
        testCase.get().log(Status.INFO, "Get vehicle details");

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(insurerOrg);

        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + insurer);

        //Go to inbox
        workListGridPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Verification before merge
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

        //Report calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "There is no calculation button in Report page");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, calculationResultExpected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Tool bar - merge task
        toolBar.mergeTheTask();

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
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

        //Report calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, calculationResultExpected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Insurer send estimate to repairer")
    public void sendEstimationFromToolBar() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("rep_username");
        String receiver = testData.getString("ins_username");
        String receiverOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox - merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.mergeTheTask(claimNumber);

        //Switch to Reports page
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports tab");

        //Do calculation and assertion
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");

        //Get the last (#2) calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Send the new estimation to sender
        toolBar.sendEstimate();
        //User will redirect to worklist after send estimate
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertFalse(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "After sent estimation, the claim is not existed in Open box");
        workListGridPO.clickCustomSentTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "After sent estimation, the claim is existed in Sent box");
        //check if send estimate button is still exist
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        toolBarPO.clickToolBar();
        Assert.assertEquals(0, getDriver().findElements(By.id(toolBarPO.ID_SEND_ESTIMATE_BTN)).size());
        testCase.get().log(Status.PASS, "After sent estimation, Send Estimate button is not existed in tool bar");

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Check the claim in Copied box
        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in sender's Copied box");

        //Merge the claim from worklist
        worklistTaskActions.mergeTheTask(claimNumber);

        //Do verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theSecondCalculationResultAfterSent = calculationList.getCalculationResult(1);
        Assert.assertEquals(theSecondCalculationResultAfterSent, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The estimation is sent successfully");
    }

    @Test(description = "Repairer accept estimate from tool bar")
    public void acceptEstimateFromToolBar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");

        //Create a pending agreement task
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithPendingAgreementStatus(testData.getString("benzS_vehicle"), true, insurer);
        testCase.get().log(Status.INFO, claimNumber + " pending agreement task is created ");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);
        workListGridOpenPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();

        //Open the claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
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
        Map<String, String> calculationResultExpected = calculationList.getCalculationResult(0);

        //Accept estimate from toolbar
        ToolBar toolBar = new ToolBar();
        toolBar.approveTheTask();

        //Verify in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The approved claim is still existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Repairing"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Repairing"));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to Open box to check accepted claim
        workListGridOpenPO.clickCustomOpenTab();
        workListGridOpenPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The accepted claim is sent silently to insurer and auto-merged");
        Assert.assertEquals(workListGridOpenPO.getClaimBizStatus(claimNumber), testData.getString("status_Repairing"));
        testCase.get().log(Status.PASS, "The accepted claim status is: " + testData.getString("status_Repairing"));

        //Open claim
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim is editable");

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
        testCase.get().log(Status.PASS, "All Attachments and thumbnails are all found");

        //Labor rate verification
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResultAfterSent = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterSent, calculationResultExpected);
        testCase.get().log(Status.PASS, "The accepted calculation is merged successfully");
    }

    @Test(description = "Repairer reject estimate from tool bar")
    public void rejectEstimateFromToolBar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");
        String insurer = testData.getString("ins_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Create a pending agreement task
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithPendingAgreementStatus(testData.getString("benzS_vehicle"), true, insurer);
        testCase.get().log(Status.INFO, claimNumber + " pending agreement task is created ");

        //Login
        Login login = new Login();
        login.LoginBRE(repairer , testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as repairer " + repairer);
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();

        //Open the claim and reject from tool bar
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
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
        Map<String, String> calculationResultExpected = calculationList.getCalculationResult(0);

        ToolBar toolBar = new ToolBar();
        toolBar.rejectTheTask();

        //Verify in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reject claim is still existing in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Created"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Created"));
        worklistTaskActions.submitTaskWithAllAttachments(claimNumber, insurerOrg);
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Go to inbox - check and merge task
        workListGridPO.clickCopiedTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is existing in Copied box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Submitted"));
        worklistTaskActions.mergeTheTask(claimNumber);

        //Claim details verification
        Assert.assertEquals(claimDetailsPO.getClaimNumber(), claimNumber);
        testCase.get().log(Status.PASS, "Claim number is correct");
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim is editable");

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
        testCase.get().log(Status.PASS, "All Attachments and thumbnails are all found");

        //Labor rate verification
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        Assert.assertEquals(laborRatesPO.getContracts(), contracts);
        Assert.assertEquals(laborRatesPO.getLabourRate1(), labourRate1);
        Assert.assertEquals(laborRatesPO.getPaintMethods(), paintMethod);
        Assert.assertEquals(laborRatesPO.getPaintRate(), paintRate);
        testCase.get().log(Status.PASS, "Labor rate values are correct");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResultAfterSent = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterSent, calculationResultExpected);
        testCase.get().log(Status.PASS, "The accepted calculation is merged successfully");

        //Open box - check claim is existing in Open box
        workListGridPO.clickWorklist();
        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortLastUpdatedDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim is merged and displayed in Open box");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Submitted"));
        testCase.get().log(Status.PASS, "Claim status is: " + testData.getString("status_Submitted"));
    }

    @Test(description = "Insurer send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromToolbar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        // Get test image path
        File jpg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        File jpeg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
        String jpg_path = jpg_file.getAbsolutePath();
        String jpeg_path = jpeg_file.getAbsolutePath();
        // Upload files and select file
        // Claims Document
        attachmentPO.addFilesIntoClaimsDocument(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        attachmentPO.addFilesIntoClaimsDocument(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        // Vehicle Before Repair
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        // Vehicle After Repair
        attachmentPO.addFilesIntoVehicleAfterRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        //Other
        attachmentPO.addFilesIntoOther(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.addFilesIntoOther(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.OTHER_BLOCK);
        testCase.get().log(Status.INFO, "Upload files");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Insurer send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromToolbarOnAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        //Android upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        String jpg_file = "IMG_0001.jpg";
        String jpeg_file = "IMG_0002.jpeg";
        // Upload files and select file
        // Claims Document
        attachmentPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        attachmentPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        attachmentPO.selectAttachment(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        // Vehicle Before Repair
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.selectAttachment(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        // Vehicle After Repair
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.selectAttachment(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        //Other
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.selectAttachment(1, attachmentPO.OTHER_BLOCK);
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        testCase.get().log(Status.INFO, "Upload files");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Insurer send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromToolbarOnIOS() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        //iOS upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        // Upload files and select file
        // Claims Document
        uploadAttachments.clickUploadClaimsDocumentImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        uploadAttachments.clickUploadClaimsDocumentImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        uploadAttachments.selectAttachmentOnIOS(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        // Vehicle Before Repair
        uploadAttachments.clickVehicleBeforeRepairImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        uploadAttachments.clickVehicleBeforeRepairImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        uploadAttachments.selectAttachmentOnIOS(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        // Vehicle After Repair
        uploadAttachments.clickVehicleAfterRepairImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        uploadAttachments.clickVehicleAfterRepairImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        uploadAttachments.selectAttachmentOnIOS(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        //Other
        uploadAttachments.clickOtherImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        uploadAttachments.clickOtherImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        uploadAttachments.selectAttachmentOnIOS(1, attachmentPO.OTHER_BLOCK);
        testCase.get().log(Status.INFO, "Upload files");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Insurer send case to repairer with all calculation")
    public void sendCaseWithAllCalculationsFromToolbar() throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Save all calculation results for assertion
        //Get the first calculation results
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(receiverOrg);

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification before merge
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.OTHER_BLOCK);

        //Calculation verification before merge
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theFirstCalculationResult);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResultBeforeMerge = calculationList.getCalculationResult(1);
        Assert.assertEquals(theSecondCalculationResultBeforeMerge, theSecondCalculationResult);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResultBeforeMerge = calculationList.getCalculationResult(2);
        Assert.assertEquals(theThirdCalculationResultBeforeMerge, theThirdCalculationResult);
        testCase.get().log(Status.PASS, "All calculations are sent successfully");

        toolBar.mergeTheTask();

        // Enter repairer reference number
        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

        //Attachment verification after merge
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.OTHER_BLOCK);

        //Calculation verification after merge
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterMerged, theFirstCalculationResult);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResultAfterMerged = calculationList.getCalculationResult(1);
        Assert.assertEquals(theSecondCalculationResultAfterMerged, theSecondCalculationResult);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResultAfterMerged = calculationList.getCalculationResult(2);
        Assert.assertEquals(theThirdCalculationResultAfterMerged, theThirdCalculationResult);
        testCase.get().log(Status.PASS, "All calculations are merged successfully");
    }

    @Test(description = "Repairer submit task to insurer with selected attachments from toolbar")
    public void submitTaskWithSelectedAttachmentsFromToolbar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("rep_username");
        String receiver = testData.getString("ins_username");
        String receiverOrg = testData.getString("ins_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a non standard part with replace
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Launch Qapter");

        //Add a standard part with replace
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("benzS_newPictogram_bodyPainting"),vehicleElementData.getString("benzS_newPictogram_polishVehicle"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzS_zone_rearDoors"),vehicleElementData.getString("benzS_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the last calculation result
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(2);

        //Go to Attachment page and upload files
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        // Get test image path
        File jpg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        File jpeg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
        String jpg_path = jpg_file.getAbsolutePath();
        String jpeg_path = jpeg_file.getAbsolutePath();
        // Upload files and select file
        // Claims Document
        attachmentPO.addFilesIntoClaimsDocument(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        attachmentPO.addFilesIntoClaimsDocument(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        // Vehicle Before Repair
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        // Vehicle After Repair
        attachmentPO.addFilesIntoVehicleAfterRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        //Other
        attachmentPO.addFilesIntoOther(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.addFilesIntoOther(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.OTHER_BLOCK);
        testCase.get().log(Status.INFO, "Upload files");

        //Verify there is no calculation selection for repairer
        toolBarPO.clickToolBar();
        toolBarPO.clickSend();
        Assert.assertFalse(isElementPresent(By.id(toolBarPO.ID_SEND_TASK_DIALOG_WITH_CALCULATION)));
        testCase.get().log(Status.PASS, "There is no Calculations selection could be selected");
        toolBarPO.clickCancelSendTaskDialog();

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedAttachments(receiverOrg);
        testCase.get().log(Status.INFO, "Task is sent");

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
        Assert.assertFalse(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is not editable");

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        calculationList.displayAllContentInReportCalculationList();
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is sent successfully");

        toolBar.mergeTheTask();
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is editable");

        //Attachment verification
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterMerged, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is merged successfully");
    }

    @Test(description = "Repairer merge 'Created' claim")
    public void repairMergeCreatedClaim() throws URISyntaxException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzS_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");
        //Get Calculation result in Reports page
        CalculationList calculationList = new CalculationList();
        Map<String, String> calculationResultExpected = calculationList.getCalculationResult(0);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();
        testCase.get().log(Status.INFO, "Get vehicle details");

        //Get Labor rate values
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor Rates page");
        String contracts = laborRatesPO.getContracts();
        String labourRate1 = laborRatesPO.getLabourRate1();
        String paintMethod = laborRatesPO.getPaintMethods();
        String paintRate = laborRatesPO.getPaintRate();
        testCase.get().log(Status.INFO, "Get labor rate values");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskTo(receiverOrg);

        workListGridPO.clickCustomSentTab();
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("status_Created"));
        testCase.get().log(Status.PASS, "The sent claim status is: " + testData.getString("status_Created"));

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Verification before merge
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

        //Report calculation verification
        processStepIDPO.clickReportsID();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALCULATION_ALTERNATIVE)));
        testCase.get().log(Status.PASS, "There is no calculation button in Report page");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, calculationResultExpected);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Tool bar - merge task
        toolBar.mergeTheTask();

        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
        Assert.assertEquals(claimDetailsPO.getManufacturerCode(), testData.getString("benzS_manufacturer_code"));
        testCase.get().log(Status.PASS, "Manufacturer: " + testData.getString("benzS_manufacturer_code"));
        Assert.assertEquals(claimDetailsPO.getModelCode(), testData.getString("benzS_model_code"));
        testCase.get().log(Status.PASS, "Model: " + testData.getString("benzS_model_code"));
        Assert.assertEquals(claimDetailsPO.getSubModelCode(), testData.getString("benzS_submodel_code"));
        testCase.get().log(Status.PASS, "Sub Model: " + testData.getString("benzS_submodel_code"));

        claimDetailsPO.enterRepairReferenceNumber(claimNumber);

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

        //Report calculation verification
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculation button is enable after merged");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, calculationResultExpected);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Surveyor merge 'Inspecting' claim")
    public void surveyorMergeInspectingClaim() throws URISyntaxException{
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
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();
        testCase.get().log(Status.INFO, "Get vehicle details");

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
        ToolBar toolBar = new ToolBar();
        toolBar.assignTask(surveyorOrg);

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
        //Verification before merge
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

        //Tool bar - merge task
        toolBar.mergeTheTask();
        //Claim details verification
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");
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

    @Test(description = "Insurer merge 'Inspected' claim")
    public void insurerMergeInspectedClaim() throws URISyntaxException{
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String insurerOrg = testData.getString("ins_orgname");

        //Insure assign a submitted claim to surveyor and merged by surveyor (claim status: Inspecting)
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithInspectingStatus(testData.getString("benzS_vehicle"), true);

        //Login as surveyor
        Login login = new Login();
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + surveyor);
        workListGridPO.clickCustomOpenTab();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        processStepIDPO.clickGeneralDetailsID();
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();
        testCase.get().log(Status.INFO, "Get vehicle details");

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
        ToolBar toolBar = new ToolBar();
        toolBar.sendAssessmentToInsurer();

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

        //Tool bar - merge task
        toolBar.mergeTheTask();
        //Claim details verification - check main field is editable
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        Assert.assertTrue(claimDetailsPO.isClaimNumberEnable());
        testCase.get().log(Status.PASS, "Claim number is correct and is able to edit");
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

    @Test(description = "Repairer merge 'Pending agreement' claim")
    public void repairerMergePendingAgreementClaim() throws URISyntaxException{
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String repairerOrg = testData.getString("rep_orgname");

        //Surveyor merged a Inspecting claim and send back to insurer to merged it(claim status: Inspected)
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        String claimNumber = createNewCase.createCaseWithInspectedStatus(testData.getString("benzS_vehicle"), true);

        //Login as insurer
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as " + insurer);
        workListGridPO.clickCustomOpenTab();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);

        //Claim details page to get the car identification
        String manufacturer, model, submodel;
        manufacturer = claimDetailsPO.getManufacturer();
        model = claimDetailsPO.getModel();
        submodel = claimDetailsPO.getSubModel();
        testCase.get().log(Status.INFO, "Get vehicle details");

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

        //Toolbar - send estimate to repairer
        ToolBar toolBar = new ToolBar();
        toolBar.sendEstimate();

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

        //merge task from toolbar
        toolBar.mergeTheTask();
        Assert.assertEquals(claimNumber, claimDetailsPO.getClaimNumber());
        testCase.get().log(Status.PASS, "Claim number is correct");

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
}
