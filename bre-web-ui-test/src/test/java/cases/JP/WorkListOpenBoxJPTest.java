package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.LoginPO;
import pageobjects.PreIntakePO;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.claimdetails.ClaimDetailsJPPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import pageobjects.worklistgrid.WorkListGridInboxPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import steps.*;
import utils.UtilitiesManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class WorkListOpenBoxJPTest extends TestBase {
    private LoginPO loginPO;
    private ProcessStepJPPO processStepJPPO;
    private WorkListGridOpenPO workListGridOpenPO;
    private WorkListGridInboxPO workListGridInboxPO;
    private PreIntakePO preIntakePO;
    private ClaimDetailsJPPO claimDetailsJPPO;
    private ReportsPO reportsPO;
    private AttachmentsRepairerPO attachmentPO;
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(){
        loginPO = (LoginPO)context.getBean("LoginPO");
        loginPO.setWebDriver(getDriver());
        processStepJPPO = (ProcessStepJPPO) context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        workListGridOpenPO= (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        workListGridInboxPO = (WorkListGridInboxPO) context.getBean("WorkListGridInboxPO");
        workListGridInboxPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        claimDetailsJPPO = (ClaimDetailsJPPO) context.getBean("ClaimDetailsJPPO");
        claimDetailsJPPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    @Test(description = "Multiple login and logout")
    public void loginAndLogout() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Dashboard page
        Assert.assertEquals(testData.getString("ins_username").toUpperCase(), workListGridOpenPO.getLoggedUsername());
        testCase.get().log(Status.PASS, "Login successfully");

        //Logout
        workListGridOpenPO.clickLogout();
        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.PASS, "Logout successfully");

        //Login again
        loginPO.enterUserName(testData.getString("ins_username"));
        loginPO.enterPassword(testData.getString("password"));
        loginPO.clickSubmit();

        Assert.assertEquals(testData.getString("ins_username").toUpperCase(), workListGridOpenPO.getLoggedUsername());
        testCase.get().log(Status.PASS, "Login successfully");

        //Logout again
        workListGridOpenPO.clickLogout();
        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.PASS, "Logout successfully");
    }

    @Test(description = "Create case from header right top button")
    public void createNewCaseFromHeader(){
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Work List grid Open
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.clickNewClaimButton();

        //Pre Intake page
        String claimNumber = Long.toString(UtilitiesManager.getCurrentUnixTime());
        preIntakePO.enterClaimNumberTextbox(claimNumber);
        preIntakePO.clickCreateNewCaseButton();
        fluentWait(By.id(ClaimDetailsJPPO.ID_CLAIM_NUMBER));
        testCase.get().log(Status.INFO, "Create a new claim");

        //Check claim is in Open box
        processStepJPPO.clickClaimManager();
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "Claim number " + claimNumber + " is presented in Open box");

        //Logout
        workListGridOpenPO.clickLogout();

        Assert.assertFalse(isAlertPresent());
        testCase.get().log(Status.INFO, "Logout successfully");
    }

    @Test(description = "Change Owner in open box")
    public void changeOwnerInOpenBox() {
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
        String claimNumber = claimDetailsJPPO.getClaimNumber();

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - change owner
        String newOwner = testData.getString("ins_change_owner_username");
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.changeOwnerTo(claimNumber, newOwner);

        //Check the claim is not in open box
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertFalse(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The changed owner claim is not in open box anymore");

        //Logout
        workListGridOpenPO.clickLogout();

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

    @Test(description = "Close the task from work list in open box")
    public void closeTheTaskInOpenBox() {
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
        String claimNumber = claimDetailsJPPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim number: " + claimNumber);

        //Open Box - close claim and verification
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.closeTheTask(claimNumber);

        workListGridOpenPO.sortCreationDate();
        Assert.assertFalse(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The closed claim does not exist in Open box");
        workListGridOpenPO.clickClosedTab();
        workListGridOpenPO.sortCreationDate();
        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.PASS, "The closed claim exists in Close box");
    }

    @Test(description = "Send case with latest calculation and all attachments")
    public void sendCaseWithLatestCalculationAllAttachments() throws URISyntaxException, InterruptedException {
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
        createNewCase.createNewCaseWithAttachmentAndCalculation(testData.getString("benzE_vehicle"));

        String claimNumber = reportsPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

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
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        //Get the last calculation result
        Map<String, String> theLastCalculationResult = calculationList.getCalculationResult(2);

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - send task
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithLastCalculationAllAttachments(claimNumber, receiverOrg);

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

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All Attachments and thumbnails are all found");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        //Display all columns for calculation list
        Map<String, String> calculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultBeforeMerge, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is sent successfully");

        ToolBar toolBar = new ToolBar();
        toolBar.mergeTheTask();

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
        testCase.get().log(Status.PASS, "All Attachments and thumbnails are all found");

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        Map<String, String> calculationResultAfterMerged = calculationList.getCalculationResult(0);
        Assert.assertEquals(calculationResultAfterMerged, theLastCalculationResult);
        testCase.get().log(Status.PASS, "The last calculation is merged successfully");
    }

    @Test(description = "Send case with selected calculation and attachments")
    public void sendCaseWithSelectedCalculationsSelectedAttachments() throws URISyntaxException {
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

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - send task
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithSelectedCalculationSelectedAttachments(claimNumber, receiverOrg);

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

        ToolBar toolBar = new ToolBar();
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
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsFromAndroid() {
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

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - send task
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithSelectedCalculationSelectedAttachments(claimNumber, receiverOrg);

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

        ToolBar toolBar = new ToolBar();
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
    public void sendCaseWithSelectedCalculationsSelectedAttachmentsOnIOS() {
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

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - send task
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithSelectedCalculationSelectedAttachments(claimNumber, receiverOrg);

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

        ToolBar toolBar = new ToolBar();
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

    @Test(description = "Send case with all calculations")
    public void sendCaseWithAllCalculations(){
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
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");

        //Save all calculation results for assertion
        //Get the first calculation results
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResult = calculationList.getCalculationResult(2);

        // Back to Open box
        processStepJPPO.clickClaimManager();
        testCase.get().log(Status.INFO, "Back to open box");

        //Open Box - send task
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.sendTaskWithAllCalculations(claimNumber, receiverOrg);

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

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Calculations page");
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        //Display all columns for calculation list
        Map<String, String> theFirstCalculationResultBeforeMerge = calculationList.getCalculationResult(0);
        Assert.assertEquals(theFirstCalculationResultBeforeMerge, theFirstCalculationResult);
        //Get the second calculation results
        Map<String, String> theSecondCalculationResultBeforeMerge = calculationList.getCalculationResult(1);
        Assert.assertEquals(theSecondCalculationResultBeforeMerge, theSecondCalculationResult);
        //Get the third calculation results
        Map<String, String> theThirdCalculationResultBeforeMerge = calculationList.getCalculationResult(2);
        Assert.assertEquals(theThirdCalculationResultBeforeMerge, theThirdCalculationResult);
        testCase.get().log(Status.PASS, "All calculations are sent successfully");

        // Back to Inbox
        processStepJPPO.clickClaimManager();
        workListGridInboxPO.clickCopiedTab();
        testCase.get().log(Status.INFO, "Back to Inbox");
        worklistToolBar.mergeTheTask(claimNumber);

        //Calculation verification
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Calculations page");
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

    @Test(description = "Change displayed columns of work list")
    public void changeDisplayedColumnsOfWorkListOpenBox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String insurer = testData.getString("ins_username");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer " + insurer);

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //ClaimDetails - Get the claim number
        String claimNumber = claimDetailsJPPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        processStepJPPO.clickClaimManager();
        workListGridOpenPO.clickOpenTab();
        testCase.get().log(Status.INFO, "Back to open box");

        //Un-select first checked item in Customize Menu to remove it from work list column
        String uncheckedColumnName = workListGridOpenPO.clickFirstCheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first checked item on customize menu");
        Assert.assertFalse(workListGridOpenPO.isColumnDisplayedByName(uncheckedColumnName));
        testCase.get().log(Status.PASS, uncheckedColumnName + " column is removed from work list");

        //Select first unchecked item in Customize Menu to add it into work list column
        String checkedColumnName = workListGridOpenPO.clickFirstUncheckedCustomizeItem();
        testCase.get().log(Status.INFO, "Click first unchecked item on customize menu");
        Assert.assertTrue(workListGridOpenPO.isColumnDisplayedByName(checkedColumnName));
        testCase.get().log(Status.PASS, checkedColumnName + " column is added to work list");
    }

    @Test(description = "Search claim by number")
    public void searchCaseByClaimNumberInOpenBox(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        String repairer = testData.getString("rep_username");

        //Login as sender
        Login login = new Login();
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as sender " + repairer);

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        String claimNumber = claimDetailsJPPO.getClaimNumber();
        testCase.get().log(Status.INFO, "Claim " + claimNumber + " is created");

        processStepJPPO.clickClaimManager();
        workListGridOpenPO.clickOpenTab();
        testCase.get().log(Status.INFO, "Back to open box");

        //Search case by claim number
        workListGridOpenPO.sortCreationDate();
        WorklistToolBar worklistToolBar = new WorklistToolBar();
        worklistToolBar.searchByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Search case by claim number");

        Assert.assertTrue(workListGridOpenPO.isClaimNumberExist(claimNumber));
        Assert.assertEquals(workListGridOpenPO.getCurrentNumberOfClaims(), 1);
        testCase.get().log(Status.PASS, claimNumber + " is found in search result");
    }
}
