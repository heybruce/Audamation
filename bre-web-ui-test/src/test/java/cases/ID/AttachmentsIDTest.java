package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.processstep.ProcessStepIDPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.UploadAttachments;
import utils.UtilitiesManager;

import java.io.File;
import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class AttachmentsIDTest extends TestBase {
    private AttachmentsRepairerPO attachmentsPO;
    private ProcessStepIDPO processStepIDPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        attachmentsPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentsPO.setWebDriver(getDriver());
        processStepIDPO = (ProcessStepIDPO)context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
    }

    @Test(description = "Upload JPG file")
    public void uploadJPGFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        // Get test image path
        File jpeg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        String jpg_path = jpeg_file.getAbsolutePath();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(jpg_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(jpg_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(jpg_path);
        attachmentsPO.addFilesIntoOther(jpg_path);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file")
    public void uploadJPEGFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        // Get test image path
        File jpeg_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
        String jpeg_path = jpeg_file.getAbsolutePath();
        // Upload file
        attachmentsPO.addFilesIntoClaimsDocument(jpeg_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        attachmentsPO.addFilesIntoOther(jpeg_path);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPG file")
    public void uploadJPGFileFromAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        //android upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        String FileName = "IMG_0001.jpg";
        attachmentsPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file")
    public void uploadJPEGFileFromAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");
        //android upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        String FileName = "IMG_0002.jpeg";
        attachmentsPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentsPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPG file from iOS device")
    public void uploadJPGFileOnIOS() throws InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //iOS upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.clickUploadClaimsDocumentImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        uploadAttachments.clickVehicleBeforeRepairImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        uploadAttachments.clickVehicleAfterRepairImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        uploadAttachments.clickOtherImageFromiOS();
        uploadAttachments.uploadJPGFromIOS();
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file from iOS device")
    public void uploadJPEGFileOnIOS() throws InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //iOS upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.clickUploadClaimsDocumentImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        uploadAttachments.clickVehicleBeforeRepairImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        uploadAttachments.clickVehicleAfterRepairImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        uploadAttachments.clickOtherImageFromiOS();
        uploadAttachments.uploadJPEGFromIOS();
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentsPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentsPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload PDF file")
    public void uploadPDFFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File pdf_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/PrintPdf.pdf").toURI());
        String pdf_path = pdf_file.getAbsolutePath();
        String pdf_fileName = pdf_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(pdf_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(pdf_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(pdf_path);
        attachmentsPO.addFilesIntoOther(pdf_path);
        testCase.get().log(Status.INFO, "Upload PDF files");

        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-pdf"));
        //Verify uploaded filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), pdf_fileName);
        testCase.get().log(Status.PASS, "All PDF attachments are added");
    }

    @Test(description = "Upload Word (.doc) file")
    public void uploadWordDocFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File doc_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/doc.doc").toURI());
        String doc_path = doc_file.getAbsolutePath();
        String doc_fileName = doc_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(doc_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(doc_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(doc_path);
        attachmentsPO.addFilesIntoOther(doc_path);
        testCase.get().log(Status.INFO, "Upload Word (.doc) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-doc"));
        //Verify upload filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), doc_fileName);
        testCase.get().log(Status.PASS, "All Word (.doc) attachments are added");
    }

    @Test(description = "Upload Word (.docx) file")
    public void uploadWordDocxFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File docx_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/docx.docx").toURI());
        String docx_path = docx_file.getAbsolutePath();
        String docx_fileName = docx_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(docx_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(docx_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(docx_path);
        attachmentsPO.addFilesIntoOther(docx_path);
        testCase.get().log(Status.INFO, "Upload Word (.docx) files");

        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-docx"));
        //Verify upload filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), docx_fileName);
        testCase.get().log(Status.PASS, "All Word (.docx) attachments are added");
    }

    @Test(description = "Upload Excel (.xls) file")
    public void uploadExcelXlsFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File xls_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/xls.xls").toURI());
        String xls_path = xls_file.getAbsolutePath();
        String xls_fileName = xls_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(xls_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(xls_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(xls_path);
        attachmentsPO.addFilesIntoOther(xls_path);
        testCase.get().log(Status.INFO, "Upload Excel (.xls) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-xls"));
        //Verify upload filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), xls_fileName);
        testCase.get().log(Status.PASS, "All Excel (.xls) attachments are added");
    }

    @Test(description = "Upload Excel (.xlsx) file")
    public void uploadExcelXlsxFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File xlsx_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/xlsx.xlsx").toURI());
        String xlsx_path = xlsx_file.getAbsolutePath();
        String xlsx_fileName = xlsx_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(xlsx_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(xlsx_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(xlsx_path);
        attachmentsPO.addFilesIntoOther(xlsx_path);
        testCase.get().log(Status.INFO, "Upload Excel (.xlsx) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-xlsx"));
        //Verify upload filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), xlsx_fileName);
        testCase.get().log(Status.PASS, "All Excel (.xlsx) attachments are added");
    }

    @Test(description = "Upload Text (.txt) file")
    public void uploadTextTxtFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File txt_file = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/textFile.txt").toURI());
        String txt_path = txt_file.getAbsolutePath();
        String txt_fileName = txt_file.getName();
        // Upload file and verify
        attachmentsPO.addFilesIntoClaimsDocument(txt_path);
        attachmentsPO.addFilesIntoVehicleBeforeRepair(txt_path);
        attachmentsPO.addFilesIntoVehicleAfterRepair(txt_path);
        attachmentsPO.addFilesIntoOther(txt_path);
        testCase.get().log(Status.INFO, "Upload Text (.txt) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentsPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(1, "class").contains("icon-txt"));
        //Verify upload filename
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), txt_fileName);
        testCase.get().log(Status.PASS, "All Text (.txt) attachments are added");
    }

    @Test(description = "Rename uploaded attachment file")
    public void renameAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentsPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentsPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");

        String updateFileName = "_Update.";
        UploadAttachments uploadAttachments = new UploadAttachments();
        //Rename and cancel
        uploadAttachments.RenameFileNameAndCancel(1, attachmentsPO.CLAIMS_DOCUMENT_BLOCK, claimDocumentFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK, vehicleBeforeRepairFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK, vehicleAfterRepairFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentsPO.OTHER_BLOCK, otherFileName.replace(".", updateFileName));
        //Verify the filename are not changed
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), claimDocumentFileName);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), vehicleBeforeRepairFileName);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), vehicleAfterRepairFileName);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), otherFileName);

        //Rename and confirm
        //Due to automation have to wait for the filename of the last block is updated,
        //or the element will be updated and can't be attached to the page document
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentsPO.CLAIMS_DOCUMENT_BLOCK, claimDocumentFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentsPO.getByOfUploadFileName(1, attachmentsPO.CLAIMS_DOCUMENT_BLOCK), claimDocumentFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK, vehicleBeforeRepairFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentsPO.getByOfUploadFileName(1, attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK), vehicleBeforeRepairFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK, vehicleAfterRepairFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentsPO.getByOfUploadFileName(1, attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK), vehicleAfterRepairFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentsPO.OTHER_BLOCK, otherFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentsPO.getByOfUploadFileName(1, attachmentsPO.OTHER_BLOCK), otherFileName.replace(".", updateFileName)));

        //do verification after switch back from another page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor rate page");
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch back to Attachment page and do double confirm");
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(1), claimDocumentFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(1), vehicleBeforeRepairFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(1), vehicleAfterRepairFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(1), otherFileName.replace(".", updateFileName));
        testCase.get().log(Status.PASS, "All filename are updated");
    }

    @Test(description = "Copy uploaded attachment file")
    public void copyAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentsPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentsPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");

        //Copy attachment file and do verification
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentsPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentsPO.getClaimDocumentAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttachmentFileName(2), claimDocumentFileName);
        Assert.assertEquals(attachmentsPO.getClaimsDocumentAttributeValue(2, "class"), "img ");
        Assert.assertFalse(attachmentsPO.getClaimsDocumentAttributeValue(2, "id").isEmpty());
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.CLAIMS_DOCUMENT_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileName(2), vehicleBeforeRepairFileName);
        Assert.assertTrue(attachmentsPO.getVehicleBeforeRepairAttributeValue(2, "class").contains("icon-docx"));
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileName(2), vehicleAfterRepairFileName);
        Assert.assertTrue(attachmentsPO.getVehicleAfterRepairAttributeValue(2, "class").contains("icon-xlsx"));
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentsPO.OTHER_BLOCK);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileName(2), otherFileName);
        Assert.assertTrue(attachmentsPO.getOtherAttributeValue(2, "class").contains("icon-pdf"));
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.OTHER_BLOCK + " is copied successfully");
    }

    @Test(description = "Delete uploaded attachment file")
    public void deleteAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("benzS_vehicle"));

        // Attachment page
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentsPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentsPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentsIDTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentsPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");

        //Delete attachment file and do verification
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentsPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentsPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.CLAIMS_DOCUMENT_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentsPO.OTHER_BLOCK);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentsPO.OTHER_BLOCK + " is deleted successfully");

        //do double confirm after switch back from another page
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labor rate page");
        processStepIDPO.clickAttachmentsID();
        testCase.get().log(Status.INFO, "Switch back to Attachment page and do double confirm");
        Assert.assertEquals(attachmentsPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentsPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentsPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentsPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentsPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentsPO.OTHER_BLOCK);
    }
}
