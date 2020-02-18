package steps;

import cases.JP.AttachmentJPTest;
import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PreIntakePO;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.claimdetails.ClaimDetailsJPPO;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import utils.UtilitiesManager;

import java.io.File;
import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CreateNewCaseJP extends TestBase {
    private WorkListGridOpenPO workListGridOpenPO;
    private PreIntakePO preIntakePO;
    private ProcessStepJPPO processstepJPPO;
    private DamageCapturingPO damageCapturingPO;
    private AttachmentsRepairerPO attachmentPO;
    private ClaimDetailsPO claimDetails;
    private ReportsPO reportsPO;
    private WebDriverWait wait;

    public CreateNewCaseJP() {
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        processstepJPPO = (ProcessStepJPPO)context.getBean("ProcessStepJPPO");
        processstepJPPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        claimDetails = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
    }

    public void createNewCase(){
        //Work List grid Open
        workListGridOpenPO.clickOpenTab();
        workListGridOpenPO.clickNewClaimButton();

        //Pre Intake page
        String claimNumber = Long.toString(UtilitiesManager.getCurrentUnixTime());
        preIntakePO.enterClaimNumberTextbox(claimNumber);
        preIntakePO.clickCreateNewCaseButton();
        fluentWait(By.id(ClaimDetailsJPPO.ID_CLAIM_NUMBER));
        testCase.get().log(Status.INFO, "Create a new claim");
    }

    public void createNewCaseWithVehicleIdentificationBySearchTree(String vehicle) {
        //Create a new case
        createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(vehicle);
    }

    public void createNewCaseWithCalculation(String vehicle){
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        //Switch to Labour Rate page
        processstepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processstepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processstepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        //Display all columns for calculation list
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
    }

    public void createNewCaseWithAttachmentAndCalculation(String vehicle) throws URISyntaxException, InterruptedException {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        //Select labour rate
        processstepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        // Attachment page
        processstepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        UploadAttachments uploadAttachments = new UploadAttachments();
        if(isMobileDevice()){
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
            File jpeg_file = new File(AttachmentJPTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
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

        //Switch to Damage Capturing page
        processstepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processstepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        //Display all columns for calculation list
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
    }


    public void createNewCaseBySearchTreeForQapterTest(String vehicle){
        createNewCaseWithVehicleIdentificationBySearchTree(vehicle);

        //Switch to Labour Rate page
        processstepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processstepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
    }
}
