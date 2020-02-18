package cases.KR;

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
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
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
    private ProcessStepKRPO processStepKRPO;
    private ClaimDetailsKRPO claimDetails;
    private ToolBarPO toolBarPO;
    private ReportsPO reportsPO;
    private LaborRatesPO laborRatesPO;
    private DamageCapturingPO damageCapturingPO;
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
    public void methodSetup(Method method, ITestContext testContext) throws IOException {
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsKRPO) context.getBean("ClaimDetailsKRPO");
        claimDetails.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO)context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
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
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());

        //Setup download path
        setupDownloadPath(method);
    }

    @Test(description = "Print PDF report (template 1) from tool bar")
    public void printPdfTemplate1() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        int templateNumber = 1;
        String fileName = "AutoTest_PDF_Template" + templateNumber + ".pdf";
        String pdfFilePath = downloadPath + File.separator + fileName;
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(templateNumber, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF report (template 2) from tool bar")
    public void printPdfTemplate2() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        int templateNumber = 2;
        String fileName = "AutoTest_PDF_Template" + templateNumber + ".pdf";
        String pdfFilePath = downloadPath + File.separator + fileName;
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template1
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(templateNumber, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF report (template 3) from tool bar")
    public void printPdfTemplate3() throws MalformedURLException{
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        int templateNumber = 3;
        String fileName = "AutoTest_PDF_Template" + templateNumber + ".pdf";
        String pdfFilePath = downloadPath + File.separator + fileName;
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Tool bar - Print PDF template3
        ToolBar toolBar = new ToolBar();
        toolBar.printPdfReport(templateNumber, fileName, false);
        //check the file exists and the file size is not 0
        await().atMost(10, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

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

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + sender);

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
        //        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
        //                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
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

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
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

        //Calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertEquals(reportsPO.getCalculationNumber(), 1);
        testCase.get().log(Status.PASS, "There is only one calculation be sent");
        Map<String, String> theFirstCalculationResultAfterSent = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterSent, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is sent successfully");
    }

    @Test(description = "Send case with selected calculation and attachments from toolbar")
    public void sendCaseWithSelectedCalculationsAndAttachmentsFromToolbar() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case with attachment and first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
//        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
//                        vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        CalculationList calculationList = new CalculationList();
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        // Get test image path
        File jpg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        File jpeg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
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
        attachmentPO.selectAttachment(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        // Vehicle Before Repair
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.selectAttachment(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        // Vehicle After Repair
        attachmentPO.addFilesIntoVehicleAfterRepair(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.selectAttachment(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        //Other
        attachmentPO.addFilesIntoOther(jpg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.addFilesIntoOther(jpeg_path);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.selectAttachment(1, attachmentPO.OTHER_BLOCK);
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        testCase.get().log(Status.INFO, "Upload files");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiver);
        testCase.get().log(Status.INFO, "Task is sent");

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Can't verify attachment because of AXNKR-1588
        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Can't verify attachment because of AXNKR-1588
        //Attachment verification after merging the task
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification after merged the task
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Send case with all calculations from toolbar")
    public void sendCaseWithAllCalculationsFromToolbar() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add an Non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a part from pictogram to replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
//        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
//                        vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Save all calculation results for assertion
        //Get the first calculation results
        CalculationList calculationList = new CalculationList();
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(receiver);
        testCase.get().log(Status.INFO, "Task is sent");

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        workListGridInboxPO.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
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
        processStepKRPO.clickReportsTab();
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
        testCase.get().log(Status.INFO, "Merge task");

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.OTHER_BLOCK);

        //Calculation verification after merged the task
        processStepKRPO.clickReportsTab();
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

    @Test(description = "Copy the claim including all calculations from tool bar")
    public void copyTheClaimIncludeAllCalculations() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        String claimNumberOriginal = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumberOriginal + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a Non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.PASS, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a part from pictogram to replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
//        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
//                        vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.PASS, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Save all calculation results for assertion
        //Get the first calculation results
        CalculationList calculationList = new CalculationList();
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - copy the task
        toolBarPO.clickToolBar();
        toolBarPO.clickCopy();
        testCase.get().log(Status.INFO, "Open copy dialog from tool bar");

        //Assert checkbox default value: All selection should be checked except the last calculation
        Assert.assertTrue(toolBarPO.isCheckedIncludeAdministartiveData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeDamageDescription());
        Assert.assertTrue(toolBarPO.isCheckedIncludePolicyData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeVehicleData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeDamageCapture());
        Assert.assertTrue(toolBarPO.isCheckedIncludeAllCalculations());
        Assert.assertTrue(toolBarPO.isCheckedIncludeAttachments());
        Assert.assertFalse(toolBarPO.isCheckedIncludeLastCalculation());
        testCase.get().log(Status.PASS, "Checkbox default value is as expectation in Copy dialog");

        String claimNumberCopy = claimNumberOriginal+"_Copy";
        toolBarPO.enterCopyClaimNumber(claimNumberCopy);
        toolBarPO.clickBtnConfirmCopy();
        testCase.get().log(Status.INFO, "Start to verify in the copy claim");

        //ClaimDetails - Get the claim number
        Assert.assertEquals(claimDetails.getClaimNumber(),claimNumberCopy);
        testCase.get().log(Status.PASS, "Claim number is as expected: " + claimNumberCopy);

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Upload file
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "Attachments are all included");

        //Switch to Labour Rate page
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

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Check the checklist have three items
        Assert.assertTrue(damageCapturing.isChecklistNumberMatched(4));
        testCase.get().log(Status.PASS, "Added part number in Qapter checklist is correct");

        //Switch to Modify Parts page and verify
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 3);
        testCase.get().log(Status.PASS, "Added part number in modify spare parts list is correct");

        //Switch to report page and verify
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultCopied = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultCopied, theFirstCalculationResult);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResultCopied = calculationList.getCalculationResult(1);
        Assert.assertEquals(theSecondCalculationResultCopied, theSecondCalculationResult);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResultCopied = calculationList.getCalculationResult(2);
        Assert.assertEquals(theThirdCalculationResultCopied, theThirdCalculationResult);
        testCase.get().log(Status.PASS, "All calculations are copied into the new claim");
    }

    @Test(description = "Copy the claim including last calculation from tool bar")
    public void copyTheClaimIncludeLastCalculations() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        String claimNumberOriginal = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumberOriginal + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a Non standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.PASS, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a part from pictogram to replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
//        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
//                        vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.PASS, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Get the third calculation results
        CalculationList calculationList = new CalculationList();
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        //Tool bar - copy the task
        toolBarPO.clickToolBar();
        toolBarPO.clickCopy();
        testCase.get().log(Status.INFO, "Open copy dialog from tool bar");

        //Assert checkbox default value: All selection should be checked except the last calculation
        Assert.assertTrue(toolBarPO.isCheckedIncludeAdministartiveData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeDamageDescription());
        Assert.assertTrue(toolBarPO.isCheckedIncludePolicyData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeVehicleData());
        Assert.assertTrue(toolBarPO.isCheckedIncludeDamageCapture());
        Assert.assertTrue(toolBarPO.isCheckedIncludeAllCalculations());
        Assert.assertTrue(toolBarPO.isCheckedIncludeAttachments());
        Assert.assertFalse(toolBarPO.isCheckedIncludeLastCalculation());
        testCase.get().log(Status.PASS, "Checkbox default value is as expectation in Copy dialog");

        // Select last calculation
        toolBarPO.clickCheckboxIncludeLastCalculation();
        Assert.assertTrue(toolBarPO.isCheckedIncludeLastCalculation());
        Assert.assertFalse(toolBarPO.isCheckedIncludeAllCalculations());
        testCase.get().log(Status.PASS, "\"Include last calculation\" is checked and \"Include all calculations\" is unchecked");

        String claimNumberCopy = claimNumberOriginal+"_Copy";
        toolBarPO.enterCopyClaimNumber(claimNumberCopy);
        toolBarPO.clickBtnConfirmCopy();
        testCase.get().log(Status.INFO, "Start to verify in the copy claim");

        //ClaimDetails - Get the claim number
        Assert.assertEquals(claimDetails.getClaimNumber(),claimNumberCopy);
        testCase.get().log(Status.PASS, "Claim number is as expected: " + claimNumberCopy);

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Upload file
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "Attachments are all included");

        //Switch to Labour Rate page
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

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Check the checklist have three items
        Assert.assertTrue(damageCapturing.isChecklistNumberMatched(4));
        testCase.get().log(Status.PASS, "Added part number in Qapter checklist is correct");

        //Switch to Modify Parts page and verify
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 3);
        testCase.get().log(Status.PASS, "Added part number in modify spare parts list is correct");

        //Switch to report page and verify
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Get calculation results
        Map<String, String> calculationResultCopied = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultCopied, theThirdCalculationResult);
        testCase.get().log(Status.PASS, "Last calculation is copied into the new claim");
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetails.getClaimNumber();

        //Tool bar - change owner
        String newOwner = testData.getString("IBOS_username");
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
        workListGridOpenPO.clickClaimManager();
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

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();
        //Get calculation result in Report page
        CalculationList calculationList = new CalculationList();
        Map<String, String> calculationResult = calculationList.getCalculationResult(0);

        //Back to Claim details page to get the car identification
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

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
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
        testCase.get().log(Status.PASS, "Labor rate is disable before merge");
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
        Assert.assertFalse(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculation button is disable before merge");
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, calculationResult);
        testCase.get().log(Status.PASS, "The calculation is sent successfully");

        //Tool bar - merge task
        toolBar.mergeTheTask();

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
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        LR1 = laborRatesPO.getLabourRate1();
        LR2 = laborRatesPO.getLabourRate2();
        LR3 = laborRatesPO.getLabourRate3();
        PR = laborRatesPO.getPaintRate();
        Assert.assertTrue(laborRatesPO.isSelectPartnershipEnable());
        testCase.get().log(Status.PASS, "Partnership is enable after merged");
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
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Assert.assertTrue(reportsPO.isCalculateBtnEnable());
        testCase.get().log(Status.PASS, "Calculation button is enable after merged");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, calculationResult);
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

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

    @Test(description = "Insurer approve the claim which sent by repairer")
    public void insurerApproveTheClaim() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String receiver = testData.getString("ins_username");
        String sender = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
        //Tool bar - merge task
        toolBar.mergeTheTask();
        toolBar.approveTheTask();

        //Work list - check status and send to repairer
        workListGridPO.clickClaimManager();
        workListGridPO.clickOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Approved_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Approved_KR") + "' in insurer's work list");
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithLastCalculationAllAttachments(claimNumber);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + sender);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Approved_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Approved_KR") + "' in repairer's work list");
    }

    @Test(description = "Insurer reject the claim which sent by repairer")
    public void insurerRejectTheClaim() throws URISyntaxException, InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String receiver = testData.getString("ins_username");
        String sender = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender, testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        //ClaimDetails - Get the claim number
        String claimNumber = reportsPO.getClaimNumber();

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithLastCalculationAllAttachments(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);
        //Tool bar - merge task
        toolBar.mergeTheTask();
        toolBar.rejectTheTask();

        //Work list - check status and send to repairer
        workListGridPO.clickClaimManager();
        workListGridPO.clickOpenTab();
        workListGridPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Rejected_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Rejected_KR") + "' in insurer's work list");
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithLastCalculationAllAttachments(claimNumber);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(sender, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + sender);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        workListGridInboxPO.sortCreationDate();
        Assert.assertEquals(workListGridPO.getClaimBizStatus(claimNumber), testData.getString("Rejected_KR"));
        testCase.get().log(Status.PASS, "The status shows as '" + testData.getString("Rejected_KR") + "' in repairer's work list");
    }

    @Test(description = "Send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromToolbarOnAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case with attachment and first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
        //        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
        //                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        CalculationList calculationList = new CalculationList();
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepKRPO.clickAttachmentsRepairerTab();
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
        imgIdClaimsDocument = attachmentPO.getClaimsDocumentAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        // Vehicle Before Repair
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        imgIdVehicleBeforeRepair = attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        // Vehicle After Repair
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        imgIdVehicleAfterRepair = attachmentPO.getVehicleAfterRepairAttributeValue(2, "id");
        attachmentPO.selectAttachment(2, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        //Other
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(jpg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(jpeg_file);
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(2, attachmentPO.OTHER_BLOCK),"class","img "));
        imgIdOther = attachmentPO.getOtherAttributeValue(1, "id");
        attachmentPO.selectAttachment(1, attachmentPO.OTHER_BLOCK);
        testCase.get().log(Status.INFO, "Upload files");

        //Tool bar - send task
        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiver);
        testCase.get().log(Status.INFO, "Task is sent");

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        workListGridInboxPO.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Can't verify attachment because of AXNKR-1588
        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Can't verify attachment because of AXNKR-1588
        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }

    @Test(description = "Send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromToolbarOnIOS() throws InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String sender = testData.getString("ins_username");
        String receiver = testData.getString("rep_username");

        //Login
        Login login = new Login();
        login.LoginBRE(sender , testData.getString("password"));

        //Create a new case with attachment and first calculation
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));
        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        //Second calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Third calculation
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Add a standard part with replace
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"), vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
        //        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
        //                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the third calculation");

        //Select the second calculation
        reportsPO.clickCalculationCheckedbox(1);
        //De-select the third calculation
        reportsPO.clickCalculationCheckedbox(2);

        //Get the second calculation result
        CalculationList calculationList = new CalculationList();
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Go to Attachment page and upload files
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        String imgIdClaimsDocument="", imgIdVehicleBeforeRepair="", imgIdVehicleAfterRepair="", imgIdOther="";
        //Android upload image
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
        toolBar.sendTaskWithSelectedCalculationSelectedAttachments(receiver);
        testCase.get().log(Status.INFO, "Task is sent");

        //Logout
        workListGridOpenPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridInboxPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridInboxPO.sortCreationDate();
        Assert.assertTrue(workListGridInboxPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The sent claim is in receiver's inbox");

        //Open claim
        WorklistTaskActions worklistTaskActions = new WorklistTaskActions();
        worklistTaskActions.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //Can't verify attachment because of AXNKR-1588
        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is sent successfully");

        toolBar.mergeTheTask();

        //Attachment verification
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "id"), imgIdClaimsDocument);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id"), imgIdVehicleBeforeRepair);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id"), imgIdVehicleAfterRepair);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "id"), imgIdOther);
        testCase.get().log(Status.PASS, "Selected attachments are all included");

        //Calculation verification
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> theFirstCalculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultAfterMerged, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "The selected calculation is merged successfully");
    }
}
