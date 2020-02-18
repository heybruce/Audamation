package cases.KR;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import jcifs.smb.SmbException;
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
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.ToolBarPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.*;
import utils.UtilitiesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class ReportsTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
    private ReportsPO reportsPO;
    private ToolBarPO toolBarPO;
    private LaborRatesPO laborRatesPO;
    private DamageCapturingPO damageCapturingPO;

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
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO)context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());
        laborRatesPO = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());

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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

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
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        Assert.assertFalse(reportsPO.getCalculationTitle(0).isEmpty());
        Assert.assertFalse(reportsPO.getCalculationDateTime(0).isEmpty());
        Assert.assertFalse(reportsPO.getUserId(0).isEmpty());
        Assert.assertFalse(reportsPO.getGrandTotalWTaxCombined(0).isEmpty());
        Assert.assertFalse(reportsPO.getRepairTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPartsTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getLabourTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPaintTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getAdditionalCost(0).isEmpty());
        testCase.get().log(Status.PASS, "New calculation is added into calculation list");

        String output = reportsPO.getCalculationOutput();
        Assert.assertNotNull(output);
        Assert.assertTrue(output.contains("AUDATEX SYSTEM"));
        testCase.get().log(Status.PASS, "Calculation output is displayed");
    }

    @Test(description = "Print PDF report in calculation list")
    public void printPdfInCalculationList() throws MalformedURLException, SmbException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        String pdfFilePath = downloadPath + File.separator + "printPdf.pdf";
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);

        //Download the PDF report in the list
        reportsPO.clickPrintReportIcon(0);
        testCase.get().log(Status.INFO, "Download PDF report");

        //check the file exists and the file size is not 0
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF template 1 by clicking print PDF button")
    public void printPdfTemplate1ByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Make sure file is not existed in the dir file
        int templateNumber = 1;
        String fileName = "ReportsTest_PDF_Template"+templateNumber;
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";


        reportsPO.clickPrintPdfButton();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        toolBarPO.setFileName(fileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print PDF (template "+templateNumber+") from Print PDF popup in Report page");

        //check file exist and the file size is not 0
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);
        //check the file exists and the file size is not 0
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF template 2 by clicking print PDF button")
    public void printPdfTemplate2ByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Make sure file is not existed in the dir file
        int templateNumber = 2;
        String fileName = "ReportsTest_PDF_Template"+templateNumber;
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";

        reportsPO.clickPrintPdfButton();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        toolBarPO.setFileName(fileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print PDF (template "+templateNumber+") from Print PDF popup in Report page");

        //check file exist and the file size is not 0
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);
        //check the file exists and the file size is not 0
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
    }

    @Test(description = "Print PDF template 3 by clicking print PDF button")
    public void printPdfTemplate3ByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Make sure file is not existed in the dir file
        int templateNumber = 3;
        String fileName = "ReportsTest_PDF_Template"+templateNumber;
        String pdfFilePath = downloadPath + File.separator + fileName + ".pdf";

        reportsPO.clickPrintPdfButton();
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        toolBarPO.setFileName(fileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, "Print PDF (template "+templateNumber+") from Print PDF popup in Report page");

        //check file exist and the file size is not 0
        SmbFile pdfFile = new SmbFile(pdfFilePath, SMB_AUTH);
        //check the file exists and the file size is not 0
        await().atMost(15, SECONDS).until(isSmbFileExisted(pdfFile));
        testCase.get().log(Status.PASS, "PDF report download successfully");
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String LR1_Original, LR2_Original, LR3_Original, PR_Original;
        String LR1_New = "1111", LR2_New = "2222", LR3_New = "3333", PR_New = "4444";
        LR1_Original = laborRatesPO.getLabourRate1().replaceAll(",","");
        LR2_Original = laborRatesPO.getLabourRate2().replaceAll(",","");
        LR3_Original = laborRatesPO.getLabourRate3().replaceAll(",","");
        PR_Original = laborRatesPO.getPaintRate().replaceAll(",","");

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
        testCase.get().log(Status.INFO, "Do the first calculation");
        //The original edited output should contains original labor rate and paint rate
        String output = reportsPO.getCalculationOutput();
        Assert.assertTrue(output.contains(LR1_Original));
        Assert.assertTrue(output.contains(LR2_Original));
        Assert.assertTrue(output.contains(LR3_Original));
        Assert.assertTrue(output.contains(PR_Original));
        testCase.get().log(Status.INFO, "The original edited output contains original labor rate and paint rate");
        Assert.assertFalse(output.contains(LR1_New));
        Assert.assertFalse(output.contains(LR2_New));
        Assert.assertFalse(output.contains(LR3_New));
        Assert.assertFalse(output.contains(PR_New));
        testCase.get().log(Status.INFO, "The original edited output does NOT contain new labor rate and paint rate");

        //Switch to Labour Rate page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        //Update new value
        laborRatesPO.enterLaborRate1Textbox(LR1_New);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterLaborRate2Textbox(LR2_New);
        laborRatesPO.enterLaborRate3Textbox(LR3_New);
        laborRatesPO.enterPaintRateTextbox(PR_New);
        //this is workaround to trigger save text value (issue happens at v19.07 and can't reproduce manually)
        laborRatesPO.clickLaborRate2Textbox();
        testCase.get().log(Status.INFO, "Updated labor rate and paint rate to be new value");

        //Switch to Reports page
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");
        //The new edited output should contains new labor rate and paint rate
        output = reportsPO.getCalculationOutput();
        Assert.assertFalse(output.contains(LR1_Original));
        Assert.assertFalse(output.contains(LR2_Original));
        Assert.assertFalse(output.contains(LR3_Original));
        Assert.assertFalse(output.contains(PR_Original));
        testCase.get().log(Status.PASS, "The new edited output does NOT contain original labor rate and paint rate");
        Assert.assertTrue(output.contains(LR1_New));
        Assert.assertTrue(output.contains(LR2_New));
        Assert.assertTrue(output.contains(LR3_New));
        Assert.assertTrue(output.contains(PR_New));
        testCase.get().log(Status.PASS, "The new edited output contains new labor rate and paint rate");
    }
}