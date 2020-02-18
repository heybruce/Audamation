package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import jcifs.smb.SmbFile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.*;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import pageobjects.worklistgrid.WorkListGridClosedPO;
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

public class ToolBarTest extends TestBase{
    private ProcessStepJPPO processStepJPPO;
    private ToolBarPO toolBarPO;
    private WorkListGridPO workListGridPO;
    private WorkListGridClosedPO workListGridClosedPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private WorkListGridOpenPO workListGridOpenPO;
    private AttachmentsRepairerPO attachmentPO;
    private ClaimDetailsPO claimDetails;
    private ReportsPO reportsPO;
    private LaborRatesPO laborRatesPO;
    private DamageCapturingPO damageCapturingPO;
    private ModifySparePartsPO modifySparePartsPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(Method method, ITestContext testContext) throws IOException {
        processStepJPPO = (ProcessStepJPPO) context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO)context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        workListGridClosedPO = (WorkListGridClosedPO)context.getBean("WorkListGridClosedPO");
        workListGridClosedPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        laborRatesPO  = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        modifySparePartsPO = (ModifySparePartsPO)context.getBean("ModifySparePartsPO");
        modifySparePartsPO.setWebDriver(getDriver());
        //Setup download path
        setupDownloadPath(method);

    }

    @Test(description = "Print PDF from tool bar and also store the report in attachment")
    public void printPdfWithStoreReport() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String fileName = "AutoTest_PDF";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(1, fileName, true);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");

        // Attachment page
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), fileName+".pdf");
        testCase.get().log(Status.PASS, "The report file is stored in Attachment page");

    }

    @Test(description = "Print PDF from tool bar and not to store the report in attachment")
    public void printPdfWithoutStoreReport() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String fileName = "AutoTest_PDF";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(1, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");

        // Attachment page
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "The report file is not stored in Attachment page");
    }

    @Test(description = "Close the task from tool bar")
    public void closeTheTask() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetails.getClaimNumber();

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");

        workListGridClosedPO.sortCreationDate();
        Assert.assertTrue(workListGridClosedPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The closed claim is in close box");
    }

    @Test(description = "Send case with latest calculation and all attachments from toolbar")
    public void sendCaseWithLatestCalculationAllAttachmentsFromToolbar() throws URISyntaxException, InterruptedException {
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
        String claimNumber = reportsPO.getClaimNumber();

        //Second calculation
        //Add a non standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        damageCapturing.addPartsFromNewPictogramToNewPartPainting(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        //Get the last calculation result
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(receiverOrg);

        //Logout
        workListGridPO.clickLogout();

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

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Calculations page");
        calculationList.displayAllContentInReportCalculationList();
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS, "There is only one calculation be sent");
        Map<String, String> theFirstCalculationResultAfterSent = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterSent, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is sent successfully");
    }

    @Test(description = "Change Owner from tool bar")
    public void changeOwner() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetails.getClaimNumber();

        //Tool bar - change owner
        String newOwner = testData.getString("ins_change_owner_username");
        ToolBar toolBar = new ToolBar();
        toolBar.changeOwnerTo(newOwner);

        //Check the claim is not in open box
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertFalse(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The changed owner claim is not in open box anymore");

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(newOwner, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as the new owner " + newOwner);

        //Go to open box
        workListGridOpenPO.clickOpenTab();

        //Check claim is in the new owner's open box
        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The changed owner claim is in new owner's open box");
    }

    @Test(description = "Merge task for inbox claim")
    public void mergeTaskForInboxClaim() throws URISyntaxException, InterruptedException {
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
        //Get calculation result in Report page
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

        //Tool bar - merge task
        toolBar.mergeTheTask();

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
        testCase.get().log(Status.INFO, "Switch to Calculations page");
        Map<String, String> calculationResultAfterMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerge, calculationResult);
        testCase.get().log(Status.PASS, "The calculation is merged successfully");
    }

    @Test(description = "Reopen closed claim from tool bar")
    public void reopenTaskForClosedClaimFromToolBar(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = claimDetails.getClaimNumber();

        //Tool bar - close the task
        ToolBar toolBar = new ToolBar();
        toolBar.closeTheTask();

        //Close box - open the claim
        workListGridPO.clickClosedTab();
        testCase.get().log(Status.INFO, "Switch to Close box");
        workListGridClosedPO.sortCreationDate();
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        //Tool bar - reopen the task
        toolBar.reopenTheTask();
        workListGridClosedPO.sortCreationDate();
        Assert.assertFalse(workListGridClosedPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reopen claim does not exist in Close box");

        workListGridPO.clickOpenTab();
        testCase.get().log(Status.INFO, "Switch to Open box");

        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The reopen claim exists in Open box");
    }

    @Test(description = "Send case with all calculations from toolbar")
    public void sendCaseWithAllCalculationsFromToolbar() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case first calculation
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add an Non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a part from pictogram to replace
        damageCapturing.addPartsFromNewPictogramToNewPartPainting(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Save all calculation results for assertion
        //Get the first calculation results
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(receiverOrg);
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

        //Merge claim
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.mergeTheTask(claimNumber);

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Calculations page");
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
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

    @Test(description = "Send case with selected calculation and attachments from toolbar")
    public void sendCaseWithSelectedCalculationsAndAttachmentsFromToolbar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");
        String receiverOrg = testData.getString("rep_orgname");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case first calculation
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add an Non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        damageCapturing.addPartsFromNewPictogramToNewPartPainting(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";

        // Get test image path
        File jpg_file = new File(AttachmentJPTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        File jpeg_file = new File(AttachmentJPTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
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
        workListGridInboxPO.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Display all columns for calculation list
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Send case with selected calculation and attachments")
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

        //Create a new case with attachment and first calculation
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        damageCapturing.addPartsFromNewPictogramToNewPartPainting(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the last calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepJPPO.clickAttachmentsTab();
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
        workListGridInboxPO.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Display all columns for calculation list
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Send case with selected calculation and attachments")
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

        //Create a new case with attachment and first calculation
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        //Add a standard part with replace
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        damageCapturing.addPartsFromNewPictogramToNewPartPainting(
                vehicleElementData.getString("benzE_newPictogram_bodyPainting"), vehicleElementData.getString("benzE_newPictogram_bodyShell"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Calculation Options page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the last calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepJPPO.clickAttachmentsTab();
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

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Display all columns for calculation list
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Attachment verification
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }
}
