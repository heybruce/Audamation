package steps;

import cases.KR.AttachmentTest;
import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.PreIntakePO;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import utils.UtilitiesManager;

import java.io.File;
import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CreateNewCaseKR extends TestBase{
    private WorkListGridOpenPO workListGridOpenPO;
    private PreIntakePO preIntakePO;
    private ProcessStepKRPO processStepKRPO;
    private AttachmentsRepairerPO attachmentPO;
    private DamageCapturingPO damageCapturingPO;
    private ReportsPO reportsPO;

    public CreateNewCaseKR() {
        workListGridOpenPO = (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        processStepKRPO = (ProcessStepKRPO)context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
    }

    public void createNewCase(String plateNumber) {
        //Create a new claim
        //Work List grid Open
        workListGridOpenPO.clickNewClaimButton();

        //Pre Intake page
        String claimNumber = Long.toString(UtilitiesManager.getCurrentUnixTime());
        preIntakePO.enterClaimNumberTextbox(claimNumber);
        preIntakePO.enterPlateNumberKRTextbox(plateNumber);
        preIntakePO.clickCreateNewCaseButton();
        fluentWait(By.id(ClaimDetailsKRPO.ID_CLAIM_NUMBER));
        testCase.get().log(Status.INFO, "Create a new claim");
    }

    public void createNewCaseWithVehicleIdentificationByVIN(String plateNumber, String vehicle) {
        //Create a new case
        createNewCase(plateNumber);

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchByVIN(vehicle);
        testCase.get().log(Status.INFO, "Vehicle identification by VIN");
    }

    public void createNewCaseWithVehicleIdentificationBySearchTree(String plateNumber, String vehicle) {
        //Create a new case
        createNewCase(plateNumber);

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(vehicle);
    }


    public void createNewCaseWithCalculation(String plateNumber, String vehicle) {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(plateNumber, vehicle);

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("bmw320_zone_frontOuter"),vehicleElementData.getString("bmw320_position_0471_Bonnet"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
    }

    public void createNewCaseWithAttachmentAndCalculation(String plateNumber, String vehicle) throws URISyntaxException, InterruptedException {
        //Create a new case
        createNewCaseWithVehicleIdentificationBySearchTree(plateNumber, vehicle);

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
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
            File jpeg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
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
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("bmw320_zone_frontOuter"),vehicleElementData.getString("bmw320_position_0471_Bonnet"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
    }

    public void createNewCaseForQapterTest(){
        createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
    }
}
