package steps;

import cases.ID.AttachmentsIDTest;
import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PreIntakePO;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import pageobjects.worklistgrid.WorkListGridPO;
import utils.UtilitiesManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Random;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CreateNewCaseID extends TestBase {
    private WorkListGridOpenPO workListGridOpenPO;
    private PreIntakePO preIntakePO;
    private AttachmentsRepairerPO attachmentPO;
    private ReportsPO reportsPO;
    private DamageCapturingPO damageCapturingPO;
    private WorkListGridPO workListGridPO;
    private ClaimDetailsPO claimDetails;
    private ToolBar toolBar;
    private Login login;
    private WorklistTaskActions worklistTaskActions;
    private ProcessStepIDPO processStepIDPO;

    public CreateNewCaseID(){
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO)context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsPO) context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        toolBar = new ToolBar();
        login = new Login();
        worklistTaskActions = new WorklistTaskActions();
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
    }

    public void createNewCase(){
        //Work List grid Open
        workListGridOpenPO.clickCustomOpenTab();
        workListGridOpenPO.clickNewClaimButton();

        //Pre Intake page
        //appends random number to claim number to make ID claim number be unique when running with multi-thread
        Random random = new Random();
        String claimNumber = Long.toString(UtilitiesManager.getCurrentUnixTime()) + random.nextInt(1000);
        preIntakePO.selectCompany("0");
        preIntakePO.enterClaimNumberTextbox(claimNumber);
        if(preIntakePO.isRepairerReferenceNumberEnable())
            preIntakePO.enterRepairerReferenceNumberTextbox(claimNumber);
        preIntakePO.enterVehicleRegistrationNumberTextbox(testData.getString("plate_number"));
        preIntakePO.clickCreateNewCaseButton();
        fluentWait(By.id(ClaimDetailsPO.ID_CLAIM_NUMBER));
        testCase.get().log(Status.INFO, "Create a new claim: " + claimNumber);
    }

    public void createNewCaseWithVehicleIdentificationBySearchTree(String vehicle) {
        createNewCase();
        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(vehicle);
    }

    public void createNewCaseWithCalculation(String vehicle) {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString(vehicle + "_zone_frontOuter"),vehicleElementData.getString(vehicle+"_position_0471_Bonnet"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the first time
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");
    }

    public void createNewCaseWithAttachmentAndCalculation(String vehicle) throws URISyntaxException {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        fluentWait(By.id(AttachmentsRepairerPO.CLAIMS_DOCUMENT_BLOCK));
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        if(isMobileDevice()){
            UploadAttachments uploadAttachments = new UploadAttachments();
            if(getDeviceName().equalsIgnoreCase("Android")){
                //Android upload image
                String FileName = "IMG_0001.jpg";
                attachmentPO.clickUploadClaimsDocumentImage();
                uploadAttachments.uploadImageFromAndroid(FileName);
                attachmentPO.clickVehicleBeforeRepairImage();
                uploadAttachments.uploadImageFromAndroid(FileName);
                attachmentPO.clickVehicleAfterRepairImage();
                uploadAttachments.uploadImageFromAndroid(FileName);
                attachmentPO.clickOtherImage();
                uploadAttachments.uploadImageFromAndroid(FileName);
            }else if (getDeviceName().equalsIgnoreCase("iPad")){
                //Unable to click none button or link on Safari, tap upload block by coordinates instead.
                uploadAttachments.clickUploadClaimsDocumentImageFromiOS();
                uploadAttachments.uploadJPGFromIOS();
                uploadAttachments.clickVehicleBeforeRepairImageFromiOS();
                uploadAttachments.uploadJPGFromIOS();
                uploadAttachments.clickVehicleAfterRepairImageFromiOS();
                uploadAttachments.uploadJPGFromIOS();
                uploadAttachments.clickOtherImageFromiOS();
                uploadAttachments.uploadJPGFromIOS();
            }
        }else{
            // Get test image path
            File jpeg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
            String jpeg_path = jpeg_file.getAbsolutePath();
            // Upload file and verify
            attachmentPO.addFilesIntoClaimsDocument(jpeg_path);
            attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
            attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
            attachmentPO.addFilesIntoOther(jpeg_path);
        }
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK),"class","img "));
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK),"class","img "));
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK),"class","img "));
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.attributeToBe(attachmentPO.getXpathOfUploadedFile(1, attachmentPO.OTHER_BLOCK),"class","img "));
        testCase.get().log(Status.INFO, "Upload files");

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString(vehicle + "_zone_frontOuter"),vehicleElementData.getString(vehicle+"_position_0471_Bonnet"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the first time
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Calculate");
    }

    public String createCaseWithSubmittedStatus(String vehicle, Boolean addAttachments) throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String repairer = testData.getString("rep_username");
        String insurerOrg = testData.getString("ins_orgname");

        getDriver().get(testData.getString("test_url"));

        //Repairer create a claim and submit it
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as Repairer");

        if(addAttachments){
            createNewCaseWithAttachmentAndCalculation(vehicle);
        }else{
            createNewCaseWithCalculation(vehicle);
        }
        String claimNumber = reportsPO.getClaimNumber();

        toolBar.sendTaskTo(insurerOrg);
        testCase.get().log(Status.INFO, "Submit task to Insurer");

        //Repairer Logout
        workListGridPO.clickLogout();

        //Insurer merge the submitted claim
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as insurer");

        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        testCase.get().log(Status.INFO, "Insurer merge the submitted task");

        //Insurer logout
        processStepIDPO.clickLogout();

        return claimNumber;
    }

    public String createCaseWithInspectingStatus(String vehicle, Boolean addAttachments) throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");
        String surveyorOrg = testData.getString("svyr_orgname");

        //Insurer merged a submitted claim
        String claimNumber = createCaseWithSubmittedStatus(vehicle, addAttachments);

        getDriver().get(testData.getString("test_url"));

        //Insure assign a submitted claim to surveyor
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as Insurer");

        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.assignTask(claimNumber, surveyorOrg);
        testCase.get().log(Status.INFO, "Insurer assign task to Surveyor");

        //Insurer logout
        workListGridPO.clickLogout();

        //Surveyor merge the Inspecting claim
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as surveyor");

        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        testCase.get().log(Status.INFO, "Surveyor merge the Inspecting task");

        //Surveyor logout
        processStepIDPO.clickLogout();

        return claimNumber;
    }

    public String createCaseWithInspectedStatus(String vehicle, Boolean addAttachments) throws URISyntaxException {
        String insurer = testData.getString("ins_username");
        String surveyor = testData.getString("svyr_username");

        //Surveyor merged a Inspecting claim
        String claimNumber = createCaseWithInspectingStatus(vehicle, addAttachments);

        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Surveyor send assessment to insurer
        login.LoginBRE(surveyor, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as Surveyor");

        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.sendAssessmentToInsurer(claimNumber);
        testCase.get().log(Status.INFO, "Surveyor send assessment to Insurer");

        //Surveyor logout
        workListGridPO.clickLogout();

        //Insurer merge the Inspected claim
        login.LoginBRE(insurer, testData.getString("password"));

        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        testCase.get().log(Status.INFO, "Insurer merge the Inspected task");

        //Insurer logout
        processStepIDPO.clickLogout();

        return claimNumber;
    }

    public String createCaseWithPendingAgreementStatus(String vehicle, Boolean addAttachments, String insurer) throws URISyntaxException {
        String repairer = testData.getString("rep_username");

        //Insurer merged a Submitted claim
        String claimNumber = createCaseWithSubmittedStatus(vehicle, addAttachments);

        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Insurer send estimate to repairer
        login.LoginBRE(insurer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as: " + insurer);

        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.sendEstimate(claimNumber);
        testCase.get().log(Status.INFO, insurer + " send estimate to Repairer");

        //Insurer logout
        workListGridPO.clickLogout();

        //Repairer merge the Pending Agreement claim
        login.LoginBRE(repairer, testData.getString("password"));

        workListGridPO.clickCopiedTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.mergeTheTask(claimNumber);
        testCase.get().log(Status.INFO, "Repairer merge the Pending Agreement task");

        //Repairer logout
        processStepIDPO.clickLogout();

        return claimNumber;
    }

    public String createCaseWithRepairingStatus(String vehicle, Boolean addAttachments, String insurer) throws URISyntaxException {
        String repairer = testData.getString("rep_username");

        //Insurer merged a Inspected claim
        String claimNumber = createCaseWithPendingAgreementStatus(vehicle, addAttachments, insurer);

        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Repairer accept estimate
        login.LoginBRE(repairer, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as Repairer");

        workListGridPO.clickCustomOpenTab();
        workListGridPO.sortCreationDate();
        worklistTaskActions.acceptEstimate(claimNumber);
        testCase.get().log(Status.INFO, "Repairer accept estimate");

        //Repairer logout
        workListGridPO.clickLogout();

        return claimNumber;
    }

    public void createNewCaseBySearchTreeForQapterTest(){
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
    }

    public void createNewCaseWithPartnership(String vehicle) {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        //Switch to Labour Rate page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");
    }
}
