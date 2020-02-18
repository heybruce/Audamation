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
import pageobjects.processstep.AttachmentsRepairerPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.Login;
import steps.UploadAttachments;
import utils.UtilitiesManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static utils.webdrivers.WebDriverFactory.getDriver;

public class AttachmentTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
    private AttachmentsRepairerPO attachmentPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(Method method, ITestContext testContext) throws IOException {
        processStepKRPO = (ProcessStepKRPO)context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        attachmentPO = (AttachmentsRepairerPO)context.getBean("AttachmentsRepairerPO");
        attachmentPO.setWebDriver(getDriver());

        //Setup download path
        setupDownloadPath(method);
    }

    @Test(description = "Upload JPG file")
    public void uploadJPGFile() throws URISyntaxException  {
        //Launch browser
        getDriver().get(testData.getString("test_url"));

        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test image path
        File jpg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        String jpg_path = jpg_file.getAbsolutePath();

        //Network drive
       // String jpg_path = UtilitiesManager.getImgPath("IMG_0001.jpg");

        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(jpg_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpg_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(jpg_path);
        attachmentPO.addFilesIntoOther(jpg_path);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file")
    public void uploadJPEGFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));

        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test image path
        File jpeg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
        String jpeg_path = jpeg_file.getAbsolutePath();
        // Upload file
        attachmentPO.addFilesIntoClaimsDocument(jpeg_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        attachmentPO.addFilesIntoOther(jpeg_path);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPG file from Android device")
    public void uploadJPGFileFromAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Android upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        String FileName = "IMG_0001.jpg";
        attachmentPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file from Android device")
    public void uploadJPEGFileFromAndroid() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //android upload image
        UploadAttachments uploadAttachments = new UploadAttachments();
        String FileName = "IMG_0002.jpeg";
        attachmentPO.clickUploadClaimsDocumentImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickVehicleBeforeRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickVehicleAfterRepairImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        attachmentPO.clickOtherImage();
        uploadAttachments.uploadImageFromAndroid(FileName);
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPG file from iOS device")
    public void uploadJPGFileOnIOS() throws InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Android upload image
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

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(description = "Upload JPEG file from iOS device")
    public void uploadJPEGFileOnIOS() throws InterruptedException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Android upload image
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

        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.PASS, "All attachments are added");
    }

    @Test(groups = {"UAT"})
    public void compareDownloadedAttachments(Method method, ITestContext context)
            throws URISyntaxException, MalformedURLException, SmbException {
        //AXNKR-2111
        //Launch browser
        getDriver().get(testData.getString("test_url"));

        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"),
                testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Get all the files in the attachment folder
        URL uploadDir = AttachmentTest.class.getClassLoader().getResource("attachments");
        File[] uploadFiles = new File(uploadDir.toURI()).listFiles();
        int count = 1;

        //Upload files
        for (File file : uploadFiles) {
            String attachment_path = file.getAbsolutePath();
            attachmentPO.addFilesIntoClaimsDocument(attachment_path);
            Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(count, "class").contains("icon"));
            testCase.get().log(Status.INFO, "Upload file: " + file.getName());
            count++;
        }

        testCase.get().log(Status.INFO, "All attachments are added");

        //Select attachments to download
        fluentWait(By.id("select-ClaimsDocument"));
        attachmentPO.clickSelectAllInClaimDocument();
        testCase.get().log(Status.INFO, "All attachments are selected for download");

        //download attachments
        attachmentPO.clickDownloadButton();
        testCase.get().log(Status.INFO, "Attachment downloading...");

        String caseNumber = attachmentPO.getCaseNumber();       //case number will be used for the name of downloaded file
        String zipFile = downloadPath + File.separator + caseNumber + ".zip";
        SmbFile smbFile = new SmbFile(zipFile, SMB_AUTH);


        //verify files are downloaded
        await().atMost(30, SECONDS).until(isSmbFileExisted(smbFile));
        testCase.get().log(Status.INFO, "All attachments downloaded. File name: " + caseNumber + ".zip");

        //unzip files
        UtilitiesManager.unZipOnSmb(context.getAttribute(method.getName() + "_downloadFolder").toString(), caseNumber);

        try {
            String title = attachmentPO.getClaimsDocumentBlockTitle();
            //Get the list of downloaded attachments
            SmbFile[] downloadedFiles = new SmbFile(downloadPath + File.separator
                    + "decompressed-attachments" + File.separator + title + "/", SMB_AUTH).listFiles();

            //verify number of download files
            Assert.assertEquals(downloadedFiles.length, uploadFiles.length);
            testCase.get().log(Status.PASS, "The file count of the upload files and download files are the same");

            //verify content of each download file with its corresponding upload file
            for (SmbFile downloadedFile : downloadedFiles) {
                for (File uploadedFile : uploadFiles) {
                    if (downloadedFile.getName().equals(uploadedFile.getName())) {
                        testCase.get().log(Status.INFO, "Compare file name: " + downloadedFile.getName());

                        Assert.assertTrue(UtilitiesManager.isFilesIdentical(uploadedFile.getPath(), downloadedFile.getPath()));
                        testCase.get().log(Status.PASS, "The file between 2 files are identical");
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(groups = {"UAT"})
    public void compareDownloadedImages(Method method, ITestContext context) throws URISyntaxException, MalformedURLException, SmbException  {
        //AXNKR-2111
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test image path
        File jpeg_file = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        String jpeg_path = jpeg_file.getAbsolutePath();

        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(jpeg_path);
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(1, "class"), "img ");
        attachmentPO.addFilesIntoVehicleBeforeRepair(jpeg_path);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class"), "img ");
        attachmentPO.addFilesIntoVehicleAfterRepair(jpeg_path);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class"), "img ");
        attachmentPO.addFilesIntoOther(jpeg_path);
        Assert.assertEquals(attachmentPO.getOtherAttributeValue(1, "class"), "img ");
        testCase.get().log(Status.INFO, "Upload files");

        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getVehicleAfterRepairAttributeValue(1, "id").isEmpty());
        Assert.assertFalse(attachmentPO.getOtherAttributeValue(1, "id").isEmpty());
        testCase.get().log(Status.INFO, "All attachments are added");

        //Select attachments
        fluentWait(By.id("select-ClaimsDocument"));
        attachmentPO.clickSelectAllInClaimDocument();
        attachmentPO.clickSelectAllAttachments();
        testCase.get().log(Status.INFO, "All attachments are selected for download");
        attachmentPO.clickDownloadButton();
        testCase.get().log(Status.INFO, "Attachment downloading...");


        String caseNumber = attachmentPO.getCaseNumber();       //case number will be used for the name of downloaded file
        String downloadFilePath = downloadPath + File.separator + caseNumber + ".zip";
        SmbFile downloadFile = new SmbFile(downloadFilePath, SMB_AUTH);


        //verify files are downloaded
        await().atMost(30, SECONDS).until(isSmbFileExisted(downloadFile));
        testCase.get().log(Status.INFO, "All attachments downloaded. File name: " + caseNumber + ".zip");

        //unzip files
        UtilitiesManager.unZipOnSmb(context.getAttribute(method.getName() + "_downloadFolder").toString(), caseNumber);

        List<String> titleList = new ArrayList<>();
        titleList.add(attachmentPO.getClaimsDocumentBlockTitle());
        titleList.add(attachmentPO.getVehicleBeforeRepairBlockTitle());
        titleList.add(attachmentPO.getVehicleAfterRepairBlockTitle());
        titleList.add(attachmentPO.getOtherBlockTitle());

        for(String title : titleList) {
            try {
                URL imageUrl1 = new URL(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toString());
//                File path2 = new File(System.getProperty("user.dir")
//                        + "/target/decompressed-images/" + caseNumber + "/" + title + "/IMG_0001.jpg");

                File path2 = new File(context.getAttribute("downloadPath") + File.separator
                        + "decompressed-attachments" + File.separator + title + File.separator + "IMG__0001.jpg");

                URL imageUrl2 = new URL(path2.toURI().toString());
//                URL imageUrl2 = new URL(path2.getPath());
                BufferedImage image1 = ImageIO.read(imageUrl1);
                BufferedImage image2 = ImageIO.read(imageUrl2);
                double diff = UtilitiesManager.getImgDifferencePercent(image1, image2);
                String diffPercentage = String.valueOf(diff);
                if (diff != 0) {
                    testCase.get().log(Status.FAIL,title + " - The pixel difference between 2 images is " + diffPercentage + "%");
                }
                testCase.get().log(Status.PASS,title + " - The pixel difference between 2 images is " + diffPercentage + "%");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test(description = "Upload PDF file")
    public void uploadPDFFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File pdf_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/PrintPdf.pdf").toURI());
        String pdf_path = pdf_file.getAbsolutePath();
        String pdf_fileName = pdf_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(pdf_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(pdf_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(pdf_path);
        attachmentPO.addFilesIntoOther(pdf_path);
        testCase.get().log(Status.INFO, "Upload PDF files");

        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-pdf"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-pdf"));

        //Verify uploaded filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), pdf_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), pdf_fileName);

        testCase.get().log(Status.PASS, "All PDF attachments are added");
    }

    @Test(description = "Upload Word (.doc) file")
    public void uploadWordDocFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File doc_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/doc.doc").toURI());
        String doc_path = doc_file.getAbsolutePath();
        String doc_fileName = doc_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(doc_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(doc_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(doc_path);
        attachmentPO.addFilesIntoOther(doc_path);
        testCase.get().log(Status.INFO, "Upload Word (.doc) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-doc"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-doc"));
        //Verify upload filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), doc_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), doc_fileName);
        testCase.get().log(Status.PASS, "All Word (.doc) attachments are added");
    }

    @Test(description = "Upload Word (.docx) file")
    public void uploadWordDocxFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File docx_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/docx.docx").toURI());
        String docx_path = docx_file.getAbsolutePath();
        String docx_fileName = docx_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(docx_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(docx_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(docx_path);
        attachmentPO.addFilesIntoOther(docx_path);
        testCase.get().log(Status.INFO, "Upload Word (.docx) files");

        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-docx"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-docx"));
        //Verify upload filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), docx_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), docx_fileName);
        testCase.get().log(Status.PASS, "All Word (.docx) attachments are added");
    }

    @Test(description = "Upload Excel (.xls) file")
    public void uploadExcelXlsFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File xls_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/xls.xls").toURI());
        String xls_path = xls_file.getAbsolutePath();
        String xls_fileName = xls_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(xls_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(xls_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(xls_path);
        attachmentPO.addFilesIntoOther(xls_path);
        testCase.get().log(Status.INFO, "Upload Excel (.xls) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-xls"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-xls"));
        //Verify upload filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), xls_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), xls_fileName);
        testCase.get().log(Status.PASS, "All Excel (.xls) attachments are added");
    }

    @Test(description = "Upload Excel (.xlsx) file")
    public void uploadExcelXlsxFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File xlsx_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/xlsx.xlsx").toURI());
        String xlsx_path = xlsx_file.getAbsolutePath();
        String xlsx_fileName = xlsx_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(xlsx_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(xlsx_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(xlsx_path);
        attachmentPO.addFilesIntoOther(xlsx_path);
        testCase.get().log(Status.INFO, "Upload Excel (.xlsx) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-xlsx"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-xlsx"));
        //Verify upload filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), xlsx_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), xlsx_fileName);
        testCase.get().log(Status.PASS, "All Excel (.xlsx) attachments are added");
    }

    @Test(description = "Upload Text (.txt) file")
    public void uploadTextTxtFile() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        // Get test file path
        File txt_file = new File(AttachmentTest.class.getClassLoader().getResource("attachments/textFile.txt").toURI());
        String txt_path = txt_file.getAbsolutePath();
        String txt_fileName = txt_file.getName();
        // Upload file and verify
        attachmentPO.addFilesIntoClaimsDocument(txt_path);
        attachmentPO.addFilesIntoVehicleBeforeRepair(txt_path);
        attachmentPO.addFilesIntoVehicleAfterRepair(txt_path);
        attachmentPO.addFilesIntoOther(txt_path);
        testCase.get().log(Status.INFO, "Upload Text (.txt) files");

        //Verify upload file type icon
        Assert.assertTrue(attachmentPO.getClaimsDocumentAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(1, "class").contains("icon-txt"));
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(1, "class").contains("icon-txt"));
        //Verify upload filename
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), txt_fileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), txt_fileName);
        testCase.get().log(Status.PASS, "All Text (.txt) attachments are added");
    }

    @Test(description = "Rename uploaded attachment file")
    public void renameAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");


        String updateFileName = "_Update.";
        UploadAttachments uploadAttachments = new UploadAttachments();
        //Rename and cancel
        uploadAttachments.RenameFileNameAndCancel(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK, claimDocumentFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK, vehicleBeforeRepairFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK, vehicleAfterRepairFileName.replace(".", updateFileName));
        uploadAttachments.RenameFileNameAndCancel(1, attachmentPO.OTHER_BLOCK, otherFileName.replace(".", updateFileName));
        //Verify the filename are not changed
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), claimDocumentFileName);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), vehicleBeforeRepairFileName);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), vehicleAfterRepairFileName);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), otherFileName);

        //Rename and confirm
        //Due to automation have to wait for the filename of the last block is updated,
        //or the element will be updated and can't be attached to the page document
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK, claimDocumentFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
               ExpectedConditions.textToBe(attachmentPO.getByOfUploadFileName(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK), claimDocumentFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK, vehicleBeforeRepairFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentPO.getByOfUploadFileName(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK), vehicleBeforeRepairFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK, vehicleAfterRepairFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentPO.getByOfUploadFileName(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK), vehicleAfterRepairFileName.replace(".", updateFileName)));
        uploadAttachments.RenameFileNameAndConfirm(1, attachmentPO.OTHER_BLOCK, otherFileName.replace(".", updateFileName));
        new WebDriverWait(getDriver(), 3).until(
                ExpectedConditions.textToBe(attachmentPO.getByOfUploadFileName(1, attachmentPO.OTHER_BLOCK), otherFileName.replace(".", updateFileName)));

        //do verification after switch back from another page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labor rate page");
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch back to Attachment page and do double confirm");
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(1), claimDocumentFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(1), vehicleBeforeRepairFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(1), vehicleAfterRepairFileName.replace(".", updateFileName));
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(1), otherFileName.replace(".", updateFileName));
        testCase.get().log(Status.PASS, "All filename are updated");
    }

    @Test(description = "Copy uploaded attachment file")
    public void copyAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");

        //Copy attachment file and do verification
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttachmentFileName(2), claimDocumentFileName);
        Assert.assertEquals(attachmentPO.getClaimsDocumentAttributeValue(2, "class"), "img ");
        Assert.assertFalse(attachmentPO.getClaimsDocumentAttributeValue(2, "id").isEmpty());
        testCase.get().log(Status.PASS, "File in " + attachmentPO.CLAIMS_DOCUMENT_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileName(2), vehicleBeforeRepairFileName);
        Assert.assertTrue(attachmentPO.getVehicleBeforeRepairAttributeValue(2, "class").contains("icon-docx"));
        testCase.get().log(Status.PASS, "File in " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileName(2), vehicleAfterRepairFileName);
        Assert.assertTrue(attachmentPO.getVehicleAfterRepairAttributeValue(2, "class").contains("icon-xlsx"));
        testCase.get().log(Status.PASS, "File in " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK + " is copied successfully");
        uploadAttachments.CopyUploadedAttachmentFile(1, attachmentPO.OTHER_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 2);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileName(2), otherFileName);
        Assert.assertTrue(attachmentPO.getOtherAttributeValue(2, "class").contains("icon-pdf"));
        testCase.get().log(Status.PASS, "File in " + attachmentPO.OTHER_BLOCK + " is copied successfully");
    }

    @Test(description = "Delete uploaded attachment file")
    public void deleteAttachment() throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithVehicleIdentificationBySearchTree(testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        // Attachment page
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch to Attachment page");

        //Upload files
        String claimDocumentFileName = "IMG_0001.jpg";
        String vehicleBeforeRepairFileName = "docx.docx";
        String vehicleAfterRepairFileName = "xlsx.xlsx";
        String otherFileName = "pdf.pdf";

        File claimDocumentFile = new File(AttachmentTest.class.getClassLoader().getResource("images/"+claimDocumentFileName).toURI());
        String claimDocumentFile_path = claimDocumentFile.getAbsolutePath();
        attachmentPO.addFilesIntoClaimsDocument(claimDocumentFile_path);
        File vehicleBeforeRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleBeforeRepairFileName).toURI());
        String vehicleBeforeRepairFile_path = vehicleBeforeRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleBeforeRepair(vehicleBeforeRepairFile_path);
        File vehicleAfterRepairFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+vehicleAfterRepairFileName).toURI());
        String vehicleAfterRepairFile_path = vehicleAfterRepairFile.getAbsolutePath();
        attachmentPO.addFilesIntoVehicleAfterRepair(vehicleAfterRepairFile_path);
        File otherFile = new File(AttachmentTest.class.getClassLoader().getResource("attachments/"+otherFileName).toURI());
        String otherFile_path = otherFile.getAbsolutePath();
        attachmentPO.addFilesIntoOther(otherFile_path);
        testCase.get().log(Status.INFO, "Upload files into each block");

        //Delete attachment file and do verification
        UploadAttachments uploadAttachments = new UploadAttachments();
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentPO.CLAIMS_DOCUMENT_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK + " is deleted successfully");
        uploadAttachments.DeleteUploadedAttachmentFile(1, attachmentPO.OTHER_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "File in " + attachmentPO.OTHER_BLOCK + " is deleted successfully");

        //do double confirm after switch back from another page
        processStepKRPO.clickLabourRatesKRTab();
        testCase.get().log(Status.INFO, "Switch to Labor rate page");
        processStepKRPO.clickAttachmentsRepairerTab();
        testCase.get().log(Status.INFO, "Switch back to Attachment page and do double confirm");
        Assert.assertEquals(attachmentPO.getClaimDocumentAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.CLAIMS_DOCUMENT_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleBeforeRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_BEFORE_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getVehicleAfterRepairAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.VEHICLE_AFTER_REPAIR_BLOCK);
        Assert.assertEquals(attachmentPO.getOtherAttachmentFileNumber(), 0);
        testCase.get().log(Status.PASS, "There is no attachment in block: " + attachmentPO.OTHER_BLOCK);
    }
}