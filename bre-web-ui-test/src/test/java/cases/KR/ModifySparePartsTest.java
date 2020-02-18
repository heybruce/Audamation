package cases.KR;

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
import pageobjects.processstep.ModifySparePartsPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import pageobjects.worklistgrid.WorkListGridPO;
import steps.*;
import utils.UtilitiesManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class ModifySparePartsTest extends TestBase{
    private ProcessStepKRPO processStepKRPO;
    private DamageCapturingPO damageCapturingPO;
    private ModifySparePartsPO modifySparePartsPO;
    private ReportsPO reportsPO;
    private WorkListGridPO workListGridPO;

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
        modifySparePartsPO = (ModifySparePartsPO)context.getBean("ModifySparePartsPO");
        modifySparePartsPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        workListGridPO = (WorkListGridPO) context.getBean("WorkListGridPO");
        workListGridPO.setWebDriver(getDriver());

        //Setup download path
        setupDownloadPath(method);
    }

    @Test(description = "Manually add parts")
    public void manuallyAddParts() {
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

        //Switch to Modify Parts
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        //Add OEM parts
        ModifySpareParts modifySpareParts = new ModifySpareParts();
        modifySpareParts.addPart(testData.getString("PartNumber_OEM"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_OEM"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 1));
        testCase.get().log(Status.INFO, "Add a OEM part manually");

        //Add non-OEM parts
        modifySpareParts.addPart(testData.getString("PartNumber_NonOEM"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_NonOEM"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 2));
        testCase.get().log(Status.INFO, "Add a Non-OEM part manually");

        //Add used parts
        modifySpareParts.addPart(testData.getString("PartNumber_Used"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_Used"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 3));
        testCase.get().log(Status.INFO, "Add a Used part manually");

        //Check added parts in below table - row 0
        Assert.assertEquals(modifySparePartsPO.getPartNumber(0), testData.getString("PartNumber_OEM"));
        Assert.assertEquals(modifySparePartsPO.getPartDescription(0), testData.getString("Part_Description"));
        Assert.assertEquals(modifySparePartsPO.getPartPrice(0), testData.getString("Part_Price"));
        Assert.assertEquals(modifySparePartsPO.getPartSupplier(0), testData.getString("Part_Supplier"));
        Assert.assertEquals(modifySparePartsPO.getPartType(0).substring(0, 3), testData.getString("PartType_OEM"));
        testCase.get().log(Status.PASS, "OEM part is listed in the table");
        //Check added parts in below table - row 1
        Assert.assertEquals(modifySparePartsPO.getPartNumber(1), testData.getString("PartNumber_NonOEM"));
        Assert.assertEquals(modifySparePartsPO.getPartDescription(1), testData.getString("Part_Description"));
        Assert.assertEquals(modifySparePartsPO.getPartPrice(1), testData.getString("Part_Price"));
        Assert.assertEquals(modifySparePartsPO.getPartSupplier(1), testData.getString("Part_Supplier"));
        Assert.assertEquals(modifySparePartsPO.getPartType(1), testData.getString("PartType_NonOEM"));
        testCase.get().log(Status.PASS, "Non-OEM part is listed in the table");
        //Check added parts in below table - row 2
        Assert.assertEquals(modifySparePartsPO.getPartNumber(2), testData.getString("PartNumber_Used"));
        Assert.assertEquals(modifySparePartsPO.getPartDescription(2), testData.getString("Part_Description"));
        Assert.assertEquals(modifySparePartsPO.getPartPrice(2), testData.getString("Part_Price"));
        Assert.assertEquals(modifySparePartsPO.getPartSupplier(2), testData.getString("Part_Supplier"));
        Assert.assertEquals(modifySparePartsPO.getPartType(2), testData.getString("PartType_Used"));
        testCase.get().log(Status.PASS, "Used part is listed in the table");

        //Check Qapter check list
        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        //Launch Qapter
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        testCase.get().log(Status.INFO, "Qapter launched");
        //Check the checklist have three items
        Assert.assertTrue(damageCapturing.isChecklistNumberMatched(3));
        testCase.get().log(Status.PASS, "There are three items in checklist");
    }

    @Test(description = "Delete added parts")
    public void deleteAddedParts(){
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

        //Switch to Modify Parts
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        //Add OEM parts
        ModifySpareParts modifySpareParts = new ModifySpareParts();
        modifySpareParts.addPart(testData.getString("PartNumber_OEM"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_OEM"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 1));
        testCase.get().log(Status.INFO, "Add a OEM part manually");

        //Add non-OEM parts
        modifySpareParts.addPart(testData.getString("PartNumber_NonOEM"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_NonOEM"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 2));
        testCase.get().log(Status.INFO, "Add a Non-OEM part manually");

        //Add used parts
        modifySpareParts.addPart(testData.getString("PartNumber_Used"), testData.getString("Part_Description"), testData.getString("Part_Price"), testData.getString("Part_Supplier"), testData.getString("PartType_Used"));
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), 3));
        testCase.get().log(Status.INFO, "Add a Used part manually");

        int addedPartsNumOriginal = modifySparePartsPO.getAddedPartsNum();
        for(int i=addedPartsNumOriginal-1; i>=0; i--){
            modifySparePartsPO.clickDelete();
            testCase.get().log(Status.INFO, "Delete one of the parts in the table" );
            new WebDriverWait(getDriver(), 10).until(ExpectedConditions.numberOfElementsToBe(By.name(modifySparePartsPO.PART_NUM), i));
            testCase.get().log(Status.PASS, i + " more parts left" );
        }

        //Check Qapter check list
        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        //Launch Qapter
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        testCase.get().log(Status.INFO, "Qapter launched");
        //Check the checklist have no items
        Assert.assertTrue(damageCapturing.isChecklistNumberMatched(0));
        testCase.get().log(Status.PASS, "There is no item in checklist");
    }

    @Test(description = "Repair parts which added from Qapter should display in the list")
    public void repairPartsFromQapterDisplayInModifyPart(){
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

        damageCapturingPO.navigationChecklist();
        //Get part details in check list
        String partDescription, partPrice, partNumber, partGuideNumber;
        partDescription = damageCapturingPO.getChecklistPartDescription(1);
        partPrice = damageCapturingPO.getChecklistPrice(1).replaceAll("\\pP","");
        partNumber = damageCapturingPO.getChecklistPartNumber(1).replaceAll(" ", "");
        partGuideNumber = damageCapturingPO.getChecklistGuideNumber(1);

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Modify Parts page and verify
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        Assert.assertEquals(modifySparePartsPO.getAddedPartsNum(), 1);
        testCase.get().log(Status.PASS, "There is a added part in the list");
        String mspPartDescription, mspPartPrice, mspPartNumber, mspPartGuideNumber;
        mspPartDescription = modifySparePartsPO.getPartDescription(0);
        mspPartGuideNumber = modifySparePartsPO.getGuideNumber(0);
        mspPartNumber = modifySparePartsPO.getPartNumber(0).replaceAll(" ", "");
        mspPartPrice = modifySparePartsPO.getPartPrice(0).replaceAll("\\pP","");

        Assert.assertEquals(mspPartGuideNumber, partGuideNumber);
        testCase.get().log(Status.PASS, "Part guide number (" + mspPartGuideNumber + ") are the same in Qapter and Modify parts");
        Assert.assertEquals(mspPartDescription, partDescription);
        testCase.get().log(Status.PASS, "Part description (" + mspPartDescription + ") are the same in Qapter and Modify parts");
        Assert.assertEquals(mspPartNumber, partNumber);
        testCase.get().log(Status.PASS, "Part number (" + mspPartNumber + ") are the same in Qapter and Modify parts");
        Assert.assertEquals(mspPartPrice, partPrice);
        testCase.get().log(Status.PASS, "Part price (" + mspPartPrice + ") are the same in Qapter and Modify parts");
        Assert.assertEquals("", modifySparePartsPO.getPartSupplier(0));
        testCase.get().log(Status.PASS, "Part supplier is empty");
        Assert.assertEquals(testData.getString("PartType_OEM"), modifySparePartsPO.getPartType(0).substring(0, 3));
        testCase.get().log(Status.PASS, "Part type is OEM parts as default value");
    }

    @Test(description = "Modify the price less than original price")
    public void modifyThePriceLessThanOriginalPrice() {
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

        damageCapturingPO.navigationChecklist();
        //Get part details in check list
        String partPrice;
        partPrice = damageCapturingPO.getChecklistPrice(1).replaceAll("\\pP","");
        double PartPriceOriginal = Double.parseDouble(partPrice);
        int PartPriceNew = Integer.parseInt(testData.getString("Entered_New_Price"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Modify Parts page and edit the price
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        modifySparePartsPO.setEnteredPrice(0, testData.getString("Entered_New_Price"));
        modifySparePartsPO.clickSupplier(0);
        testCase.get().log(Status.INFO, "Entered a new price: " + testData.getString("Entered_New_Price"));
        double totalSavingExpected = PartPriceOriginal - PartPriceNew;
        double totalSavingActual = Double.parseDouble(modifySparePartsPO.getTotalSavingAmount(testData.getString("currency")));
        Assert.assertEquals(totalSavingActual, totalSavingExpected);
        testCase.get().log(Status.PASS, "Total saving is show as expected");

        //Switch to Reports page
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports tab");

        //Do calculation and assertion
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        String partsTotalExpected = Integer.toString((int)(PartPriceNew * 1.02));
        String partsTotalActual = reportsPO.getPartsTotal(0).replaceAll("\\pP","");
        Assert.assertEquals(partsTotalActual, partsTotalExpected);
        testCase.get().log(Status.PASS, "Parts price is the updated price in calculation");
    }

    @Test(description = "Add parts by uploading CSV template")
    public void addPartsByUploadingCSV() throws URISyntaxException {
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

        //Switch to Modify Parts
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        // Get template file path
        File csvTemplate = new File(ModifySparePartsTest.class.getClassLoader().getResource("template/PartsUploaderTest_KR.csv").toURI());
        String csvTemplatePath = csvTemplate.getAbsolutePath();
        //Upload file
        fluentWait(By.id(ModifySparePartsPO.ID_BTN_UPLOAD_CSV));
        modifySparePartsPO.uploadCsvToAddParts(csvTemplatePath);
        testCase.get().log(Status.INFO, "Upload a CSV file to add parts");

        int csvUnitPrice = Integer.parseInt(testData.getString("CSV_UnitPrice"));
        int csvAmount = Integer.parseInt(testData.getString("CSV_Amount"));
        String DescriptionExpected = testData.getString("CSV_Description") + " X(" +csvAmount+")";
        String PartNumberExpected = testData.getString("CSV_PartNumber");
        String PartPriceExpected = Integer.toString((int)csvUnitPrice*csvAmount);

        Assert.assertNotNull(modifySparePartsPO.getGuideNumber(0));
        testCase.get().log(Status.PASS, "Guide number has default value: " + modifySparePartsPO.getGuideNumber(0));
        Assert.assertEquals(modifySparePartsPO.getPartDescription(0), DescriptionExpected);
        testCase.get().log(Status.PASS, "Part description is as expected: " + DescriptionExpected);
        Assert.assertEquals(modifySparePartsPO.getPartNumber(0), PartNumberExpected);
        testCase.get().log(Status.PASS, "Part number is as expected: " + PartNumberExpected);
        Assert.assertEquals(modifySparePartsPO.getPartPrice(0), PartPriceExpected);
        testCase.get().log(Status.PASS, "Part price is as expected: " + PartPriceExpected);
        Assert.assertEquals(modifySparePartsPO.getPartSupplier(0), "");
        testCase.get().log(Status.PASS, "Supplier is null");
        Assert.assertEquals(modifySparePartsPO.getEnteredPartNumber(0),PartNumberExpected);
        testCase.get().log(Status.PASS, "Entered part number is as expected: " + PartNumberExpected);
        Assert.assertEquals(modifySparePartsPO.getEnteredPrice(0), PartPriceExpected);
        testCase.get().log(Status.PASS, "Entered Price is as expected: " + PartPriceExpected);
    }

    @Test(description = "Download parts uploader template")
    public void downloadPartsUploaderTemplate() throws InterruptedException, MalformedURLException {
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

        //Make sure file is not existed in the dir file
//        String csvFilePath = System.getProperty("user.dir")+"/PartsUploaderTemplate.csv";
//        String xlsFilePath = System.getProperty("user.dir")+"/PartsUploaderTemplate.xls";
//        File csvFileOld = new File(csvFilePath);
//        File xlsFileOld = new File(xlsFilePath);
//        if(csvFileOld.exists()) {
//            csvFileOld.delete();
//            testCase.get().log(Status.INFO, "Delete existing CSV template before test");
//        }
//        if(xlsFileOld.exists()) {
//            xlsFileOld.delete();
//            testCase.get().log(Status.INFO, "Delete existing XLS template before test");
//        }
        String csvFilePath = downloadPath + File.separator + "PartsUploaderTemplate.csv";
        String xlsFilePath = downloadPath + File.separator + "PartsUploaderTemplate.xls";


        //Switch to Modify Parts
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        modifySparePartsPO.clickDownloadCsvTemplate();
        testCase.get().log(Status.INFO, "Download CSV template");
        //check file exist and the file size is not 0
//        File csvFileNew = new File(csvFilePath);
        SmbFile csvFileNew = new SmbFile(csvFilePath, SMB_AUTH);
        await().atMost(10, SECONDS).until(isSmbFileExisted(csvFileNew));
        testCase.get().log(Status.PASS, "CSV template download successfully");

//        modifySparePartsPO.clickDownloadXlsTemplate();
//        testCase.get().log(Status.INFO, "Download XLS template");
//        Thread.sleep(2000);
//        //check file exist and the file size is not 0
//        File xlsFileNew = new File(xlsFilePath);
//        if(xlsFileNew.exists()) {
//            Assert.assertTrue(xlsFileNew.length()>0);
//            testCase.get().log(Status.PASS, "XLS template download successfully");
//            xlsFileNew.delete();
//            testCase.get().log(Status.INFO, "Delete XLS template after test");
//        } else {Assert.fail("XLS template does not exist");}
    }

    @Test(description = "Modify the price higher than original price")
    public void modifyThePriceHigherThanOriginalPrice() throws InterruptedException {
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

        damageCapturingPO.navigationChecklist();
        //Get part details in check list
        String partPrice;
        partPrice = damageCapturingPO.getChecklistPrice(1).replaceAll("\\pP","");
        double PartPriceOriginal = Double.parseDouble(partPrice);
        double PartPriceNew = PartPriceOriginal * 1.2;

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Modify Parts page and edit the price
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");

        modifySparePartsPO.setEnteredPrice(0, String.valueOf(PartPriceNew));
        modifySparePartsPO.clickSupplier(0);
        testCase.get().log(Status.INFO, "Entered a new price: " + testData.getString("Entered_New_Price"));
        double totalSavingExpected = PartPriceOriginal - PartPriceNew;
        double totalSavingActual = Double.parseDouble(modifySparePartsPO.getTotalSavingAmount(testData.getString("currency")));
        Assert.assertEquals(totalSavingActual, totalSavingExpected);
        testCase.get().log(Status.PASS, "Total saving is show as expected");

        //Switch to Reports page
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports tab");

        //Do calculation and assertion
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        String partsTotalExpected = Integer.toString((int)(PartPriceNew * 1.02));
        String partsTotalActual = reportsPO.getPartsTotal(0).replaceAll("\\pP","");
        Assert.assertEquals(partsTotalActual, partsTotalExpected);
        testCase.get().log(Status.PASS, "Parts price is the updated price in calculation");
    }

    @Test(description = "Modify the part which added from Qapter to OEM parts in Modify Spare Parts page")
    public void modifyPartTypeToOemParts(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Switch to Modify Parts page
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        //Select Part type and click checkbox for added part
        String oemPartPrice = modifySparePartsPO.getPartPrice(0);
        modifySparePartsPO.selectAddedPartType(0, testData.getString("PartType_OEM"));
        testCase.get().log(Status.INFO, "Select added part type to be OEM part");

        //Switch to Reports page
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports tab");
        String calculationOutput_original = reportsPO.getCalculationOutput();
        testCase.get().log(Status.INFO, "Get the original edited output");
        //Do calculation and assertion
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation and get the new edited output");
        String calculationOutput_new = reportsPO.getCalculationOutput();
        Assert.assertFalse(calculationOutput_original.contains(oemPartPrice+"I"));
        testCase.get().log(Status.PASS, "There is no \"I\" besides the part price in the original calculation output");
        Assert.assertTrue(calculationOutput_new.contains(oemPartPrice+"I"));
        testCase.get().log(Status.PASS, "There is an \"I\" besides the part price in calculation output which modified part type to be OEM parts");
    }

    @Test(description = "Send calculation with edited price which modified in modify spare parts page")
    public void sendCaseWithModifySparePartsCalculation(){
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

        //Damage Capturing - add two standard parts
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.selectDoorsNumberIfModelOptionPopup();
        damageCapturing.addStandardPartToReplaceWithOem(
                vehicleElementData.getString("bmw320_zone_frontOuter"),vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Modify Parts page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickModifySparePartsTab();
        testCase.get().log(Status.INFO, "Switch to Modify Parts page");
        //Set a new price to added parts
        double newPartPrice = Double.valueOf(modifySparePartsPO.getPartPrice(0))*0.8;
        modifySparePartsPO.setEnteredPrice(0, String.valueOf(newPartPrice));
        modifySparePartsPO.clickSupplier(0);
        testCase.get().log(Status.INFO, "Entered a new price: " + newPartPrice);

        //Go to Reports page and do calculation
        processStepKRPO.clickReportsTab();
        String claimNumber = reportsPO.getClaimNumber();
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do calculation");
        int partsTotalExpected = (int)Math.round(newPartPrice * 1.02);
        int partsTotalActual = Integer.parseInt(reportsPO.getPartsTotal(0).replaceAll("\\pP",""));
        Assert.assertEquals(partsTotalActual, partsTotalExpected);
        testCase.get().log(Status.INFO, "Parts price is the updated price in calculation");

        ToolBar toolBar = new ToolBar();
        toolBar.sendTaskWithAllCalculations(receiver);

        //Logout
        workListGridPO.clickLogout();

        //Login as receiver
        login.LoginBRE(receiver, testData.getString("password"));
        testCase.get().log(Status.INFO, "Login as receiver " + receiver);

        //Go to inbox
        workListGridPO.clickClaimManager();
        workListGridPO.clickCopiedTab();

        //Check claim is in receiver's inbox
        workListGridPO.sortCreationDate();
        Assert.assertTrue(workListGridPO.isClaimNumberExist(claimNumber));
        testCase.get().log(Status.INFO, "The sent claim is in receiver's inbox");

        //Open claim
        workListGridPO.openClaimByClaimNumber(claimNumber);
        testCase.get().log(Status.INFO, "Open claim " + claimNumber);

        //check part price before merge
        processStepKRPO.clickReportsTab();
        calculationList.displayAllContentInReportCalculationList();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        int partsTotalActual_received = Integer.parseInt(reportsPO.getPartsTotal(0).replaceAll("\\pP",""));
        Assert.assertEquals(partsTotalActual_received, partsTotalExpected);
        testCase.get().log(Status.PASS, "Updated parts price is sent to receiver");

        //merge the claim and do verification
        toolBar.mergeTheTask();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        int partsTotalActual_merged = Integer.parseInt(reportsPO.getPartsTotal(0).replaceAll("\\pP",""));
        Assert.assertEquals(partsTotalActual_merged, partsTotalExpected);
        testCase.get().log(Status.PASS, "Updated parts price is merged in the claim");
    }
}
