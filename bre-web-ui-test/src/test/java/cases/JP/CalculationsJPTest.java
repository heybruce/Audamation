package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.ToolBarPO;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.*;
import utils.UtilitiesManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import jcifs.smb.SmbFile;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class CalculationsJPTest extends TestBase {
    private ProcessStepJPPO processStepJPPO;
    private ReportsPO reportsPO;
    private DamageCapturingPO damageCapturingPO;
    private LaborRatesPO laborRatesPO;
    private ToolBarPO toolBarPO;
    private AttachmentsRepairerPO attachmentPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(Method method, ITestContext testContext)  throws IOException {
        processStepJPPO = (ProcessStepJPPO) context.getBean("ProcessStepJPPO");
        processStepJPPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO) context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());

        //Setup download path
        setupDownloadPath(method);
    }

    @Test(description = "Calculation output")
    public void calculationOutput() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Switch to Labour Rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        //Damage Capturing - add standard part
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");


        //Display all columns for calculation list
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Check all customize items");
        Assert.assertFalse(reportsPO.getCalculationDateTime(0).isEmpty());
        Assert.assertFalse(reportsPO.getGrandTotalWithTax(0).isEmpty());
        Assert.assertFalse(reportsPO.getPartsTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getLabourTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPaintTotal(0).isEmpty());
        testCase.get().log(Status.PASS, "New calculation is added into calculation list");

        String output = reportsPO.getCalculationOutput();
        Assert.assertNotNull(output);
        Assert.assertTrue(output.contains("AUDATEX SYSTEM"));
        testCase.get().log(Status.PASS, "Calculation output is displayed");
    }

    @Test(description = "Modified labor rate shows in edited output")
    public void modifiedLaborRateUpdatedInEditedOutput(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzE_vehicle"));

        //Switch to Labour Rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String LR1_Original, PR_Original;
        LR1_Original = laborRatesPO.getLabourRate1().replaceAll(",","");
        PR_Original = laborRatesPO.getPaintRate().replaceAll(",","");

        //Switch to Damage Capturing page
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - add standard part
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("benzE_zone_rearDoors"), vehicleElementData.getString("benzE_position_1781And1782Door"));

        //Check calculation preview in Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        String qapterOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(qapterOutput.contains(LR1_Original));
        Assert.assertTrue(qapterOutput.contains(PR_Original));
        testCase.get().log(Status.INFO, "Qapter calculation preview contains original labor rate and paint rate");

        //Switch to Calculation Option page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the first calculation");

        //The original calculation output should contains original labor rate and paint rate
        String output = reportsPO.getCalculationOutput();
        Assert.assertTrue(output.contains(LR1_Original));
        Assert.assertTrue(output.contains(PR_Original));
        testCase.get().log(Status.INFO, "The calculation output contains original labor rate and paint rate");

        //Switch to Labour Rate page
        processStepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Update new value
        String LR1_New = "3333", PR_New = "6666";
        laborRatesPO.enterLaborRate1Textbox(LR1_New);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterPaintRateTextbox(PR_New);
        //this is workaround to trigger save text value (issue happens at v19.07 and can't reproduce manually)
        laborRatesPO.clickLaborRate1Textbox();
        testCase.get().log(Status.INFO, "Update labor rate and paint rate to be new value " + LR1_New + " & " + PR_New);

        //Switch to Damage Capturing page
        processStepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Check calculation preview in Qapter
        damageCapturingPO.navigationCalcPreview();
        qapterOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertFalse(qapterOutput.contains(LR1_Original));
        Assert.assertFalse(qapterOutput.contains(PR_Original));
        testCase.get().log(Status.PASS, "Qapter calculation preview does NOT contain original labor rate and paint rate");
        Assert.assertTrue(qapterOutput.contains(LR1_New));
        Assert.assertTrue(qapterOutput.contains(PR_New));
        testCase.get().log(Status.PASS, "Qapter calculation preview contains new labor rate and paint rate");

        //Switch to Calculation Option page
        getDriver().switchTo().defaultContent();
        processStepJPPO.clickCalculationsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //The new calculation output should contains new labor rate and paint rate
        output = reportsPO.getCalculationOutput();
        Assert.assertFalse(output.contains(LR1_Original));
        Assert.assertFalse(output.contains(PR_Original));
        testCase.get().log(Status.PASS, "The new calculation output does NOT contain original labor rate and paint rate");
        Assert.assertTrue(output.contains(LR1_New));
        Assert.assertTrue(output.contains(PR_New));
        testCase.get().log(Status.PASS, "The new calculation output contains new labor rate and paint rate");
    }

    @Test(description = "Print PDF report by Print PDF button with store report")
    public void printPdfByPrintPdfButtonWithStoreReport() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

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

        int calculationNumber = 1;
        int templateNumber = 1;
        int storeReportNumber = 2;
        String fileName = "AutoTest_PDF_With_Store_Report";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";

        //Click Print Reports button
        reportsPO.clickPrintPdfButton();
        testCase.get().log(Status.INFO, "Click Print PDF button");

        //Wait for printPdf dialog popped up
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectCalculation(calculationNumber-1);
        testCase.get().log(Status.INFO, "Select calculation");
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        testCase.get().log(Status.INFO, "Select print format");
        toolBarPO.setFileName(fileName);
        testCase.get().log(Status.INFO, "Set filename");

        //Store report to Claims Document
        toolBarPO.selectStoreReport(storeReportNumber-1);
        testCase.get().log(Status.INFO, "Select store report");
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print PDF report from Print PDF button in Report page");

        //check file exist and the file size is not 0
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");

        //Check attachments
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), fileName+".pdf");
        testCase.get().log(Status.PASS, "The report file is stored in Attachment");
    }

    @Test(description = "Print PDF report by Print PDF button without store report")
    public void printPdfByPrintPdfButtonWithoutStoreReport() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseWithCalculation(testData.getString("benzE_vehicle"));

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

        int calculationNumber = 1;
        int templateNumber = 1;
        int storeReportNumber = 1;
        String fileName = "AutoTest_PDF_Without_Store_Report";
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";

        //Click Print Reports button
        reportsPO.clickPrintPdfButton();
        testCase.get().log(Status.INFO, "Click Print PDF button");

        //Wait for printPdf dialog popped up
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectCalculation(calculationNumber-1);
        testCase.get().log(Status.INFO, "Select calculation");
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        testCase.get().log(Status.INFO, "Select print format");
        toolBarPO.setFileName(fileName);
        testCase.get().log(Status.INFO, "Set filename");

        //Select Do Not Store
        toolBarPO.selectStoreReport(storeReportNumber-1);
        testCase.get().log(Status.INFO, "Select Do Not Store in Store Report");
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print PDF report from Print PDF button in Report page");

        //check file exist and the file size is not 0
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");

        //Check attachments
        processStepJPPO.clickAttachmentsTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "The report file is Not stored in Attachment");
    }
}
