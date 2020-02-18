package cases.SG;

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
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.LaborRatesPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.ToolBarPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import steps.*;
import utils.UtilitiesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class ReportsSGTest extends TestBase{
    private ProcessStepSGPO processStepSGPO;
    private ReportsPO reportsPO;
    private DamageCapturingPO damageCapturingPO;
    private LaborRatesPO laborRatesPO;
    private ToolBarPO toolBarPO;
    //VEHICLE
    private final String VEHICLE = "bmw320_vehicle";
    //Report download
    private final String PDF_EXTENSION = ".pdf";
    private final String PREFIX_TEMPLATE_REPORT_FILE_NAME = "ReportsTest_PDF_Template_SG_";
    private final String LOG_PDF_DOWNLOAD_SUCESS = "PDF report download successfully";
    private final String LOG_PDF_TEMPLATE_SELECT_SUCESS = "Print PDF (template %s) from Print PDF popup in Report page";
    private final String LOG_MERIMEN_REPORT_DOWNLOAD_SUCESS = "Merimen report download successfully";
    private final String LOG_MERIMEN_REPORT_TEMPLATE_SELECT_SUCESS = "Merimen  report (template %s) from Print PDF popup in Report page";

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(Method method, ITestContext testContext) throws IOException {
        processStepSGPO = (ProcessStepSGPO) context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        laborRatesPO  = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
        toolBarPO = (ToolBarPO) context.getBean("ToolBarPO");
        toolBarPO.setWebDriver(getDriver());

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
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString(VEHICLE));

        //Switch to Labour Rate page
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processStepSGPO.clickDamageCaptureSG();
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
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        Assert.assertFalse(reportsPO.getCalculationDateTime(0).isEmpty());
        Assert.assertFalse(reportsPO.getUserId(0).isEmpty());
        Assert.assertFalse(reportsPO.getGrandTotalWithTax(0).isEmpty());
        Assert.assertFalse(reportsPO.getPartsTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getLabourTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getPaintTotal(0).isEmpty());
        Assert.assertFalse(reportsPO.getAdditionalCost(0).isEmpty());
        testCase.get().log(Status.PASS, "New calculation is added into calculation list");

        //GSGN-1084
        Assert.assertFalse(isElementPresent(By.id(reportsPO.ID_CALC_OUTPUT)));
        Assert.assertTrue(isElementPresent(By.cssSelector(reportsPO.CLASS_PDF_CONTAINER)));
        testCase.get().log(Status.PASS, "Calculation output is displayed by PDF");
    }

    @Test(description = "Modified labor rate shows in edited output")
    public void modifiedLaborRateUpdatedInEditedOutput(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("bmw320_vehicle"));

        //Switch to Labour Rate page
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Get the partnership value as original one
        String LR1_Original, PR_Original;
        String LR1_New = "1111", PR_New = "4444";
        LR1_Original = laborRatesPO.getLabourRate1().replaceAll(",","");
        PR_Original = laborRatesPO.getPaintRate().replaceAll(",","");

        //Switch to Damage Capturing page
        processStepSGPO.clickDamageCaptureSG();
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
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the first calculation");
        //The original edited output should contains original labor rate and paint rate
        String output = reportsPO.getCalculationOutput();
        Assert.assertTrue(output.contains(LR1_Original));
        Assert.assertTrue(output.contains(PR_Original));
        testCase.get().log(Status.INFO, "The original edited output contains original labor rate and paint rate");
        Assert.assertFalse(output.contains(LR1_New));
        Assert.assertFalse(output.contains(PR_New));
        testCase.get().log(Status.INFO, "The original edited output does NOT contain new labor rate and paint rate");

        //Switch to Labour Rate page
        processStepSGPO.clickLaborRateSG();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        //Update new value
        laborRatesPO.enterLaborRate1Textbox(LR1_New);
        laborRatesPO.triggerChangeRateDialog();
        laborRatesPO.confirmChangeRate();
        laborRatesPO.enterPaintRateTextbox(PR_New);
        //this is workaround to trigger save text value (issue happens at v19.07 and can't reproduce manually)
        laborRatesPO.clickLaborRate1Textbox();
        testCase.get().log(Status.INFO, "Updated labor rate and paint rate to be new value");

        //Switch to Reports page
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");
        //The new edited output should contains new labor rate and paint rate
        output = reportsPO.getCalculationOutput();
        Assert.assertFalse(output.contains(LR1_Original));
        Assert.assertFalse(output.contains(PR_Original));
        testCase.get().log(Status.PASS, "The new edited output does NOT contain original labor rate and paint rate");
        Assert.assertTrue(output.contains(LR1_New));
        Assert.assertTrue(output.contains(PR_New));
        testCase.get().log(Status.PASS, "The new edited output contains new labor rate and paint rate");

    }
    /**
     * AXNASEAN-3306
     * @throws MalformedURLException
     */
    @Test(description = "Full report - template 1")
    public void printPdfTemplate1ByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString(VEHICLE));

        int templateNumber = 1;
        String templateReportfileName = PREFIX_TEMPLATE_REPORT_FILE_NAME + templateNumber + PDF_EXTENSION;
        String templateReportFilePath = String.join(File.separator,downloadPath, templateReportfileName);

        //Click Print Reports button
        reportsPO.clickPrintPdfButton();

        //Wait for printPdf dialog popped up
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        toolBarPO.setFileName(templateReportfileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, String.format(LOG_PDF_TEMPLATE_SELECT_SUCESS, templateNumber));

        //check file exist and the file size is not 0
        SmbFile smbFile = new SmbFile(templateReportFilePath, SMB_AUTH);
        await().atMost(15, SECONDS).until(isSmbFileExisted(smbFile));
        testCase.get().log(Status.PASS, LOG_PDF_DOWNLOAD_SUCESS);
    }

    /**
     * AXNASEAN-3308
     * @throws MalformedURLException
     */
    @Test(description = "Standard report - template 2")
    public void printPdfTemplate2ByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString(VEHICLE));

        //Click Print Reports button
        reportsPO.clickPrintPdfButton();

        //Wait for printPdf dialog popped up
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        //Select template from drop-down list
        int templateNumber = 2;
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        //Get the default templateReportFileName
        String templateReportFileName = PREFIX_TEMPLATE_REPORT_FILE_NAME + templateNumber + PDF_EXTENSION;
        String templateReportFilePath = String.join(File.separator,downloadPath, templateReportFileName);
        toolBarPO.setFileName(templateReportFileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, String.format(LOG_PDF_TEMPLATE_SELECT_SUCESS, templateNumber));

        //check file exist and the file size is not 0
        SmbFile smbFile = new SmbFile(templateReportFilePath, SMB_AUTH);
        await().atMost(15, SECONDS).until(isSmbFileExisted(smbFile));
        testCase.get().log(Status.PASS, LOG_PDF_DOWNLOAD_SUCESS);
    }

    /**
     * AXNASEAN-3307
     * @throws MalformedURLException
     */
    @Test(description = "Print Merimen Export - template 3")
    public void printMerimenExportTemplateByPrintPdfButton() throws MalformedURLException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString(VEHICLE));

        //Click Print Reports button
        reportsPO.clickPrintPdfButton();

        //Wait for printPdf dialog popped up
        new WebDriverWait(getDriver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(toolBarPO.ID_PRINT_PDF_POPUP)));
        //Select template from drop-down list
        int templateNumber = 3;
        toolBarPO.selectPrintFormatByDropDownMenu(templateNumber-1);
        //Get the default templateReportFileName
        String templateReportFileName = toolBarPO.getFileName();
        String templateReportFilePath = String.join(File.separator,downloadPath, templateReportFileName);
        toolBarPO.clickGeneratePDF();
        testCase.get().log(Status.INFO, String.format(LOG_MERIMEN_REPORT_TEMPLATE_SELECT_SUCESS, templateNumber));

        //check file exist and the file size is not 0
        SmbFile smbFile = new SmbFile(templateReportFilePath, SMB_AUTH);
        await().atMost(15, SECONDS).until(isSmbFileExisted(smbFile));
        testCase.get().log(Status.PASS, LOG_MERIMEN_REPORT_DOWNLOAD_SUCESS);
    }

}

