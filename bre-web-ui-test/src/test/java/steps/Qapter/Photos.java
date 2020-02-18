package steps.Qapter;

import cases.KR.AttachmentTest;
import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import pageobjects.processstep.DamageCapturingPO;

import java.io.File;
import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class Photos extends TestBase {
    private DamageCapturingPO damageCapturingPO;

    public Photos() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    public void uploadPhotosFromCenterBtn() throws URISyntaxException {
        //Upload photo for the first time
        String photoID;
        File file1 = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0001.jpg").toURI());
        String photo_1_path = file1.getAbsolutePath();
        damageCapturingPO.navigationPhoto();
        testCase.get().log(Status.INFO, "Navigate to Photo tab");
        damageCapturingPO.clickUploadPhotosCenterBtn(photo_1_path);
        testCase.get().log(Status.PASS, "First photo is uploaded");
        Assert.assertTrue(damageCapturingPO.isAtViewAll());
        testCase.get().log(Status.PASS, "Photo View is at All tab");
        photoID = damageCapturingPO.getPhotoIDAtViewAll(1);
        Assert.assertTrue(isPhotoFoundInCategoryAtViewByZone(photoID, testData.getString("View_Category_Navigation_Area")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_Navigation_Area") + " category at By Zone view");
        Assert.assertTrue(isPhotoFoundInCategoryAtViewByPart(photoID, testData.getString("View_Category_Unassigned")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_Unassigned") + " category category at By Part view");
    }

    public void uploadPhotosFromRepairPanel(String vehicle) throws URISyntaxException {
        String photoID;
        File file2 = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0002.jpeg").toURI());
        String photo_2_path = file2.getAbsolutePath();
        damageCapturingPO.navigationVehicle();
        testCase.get().log(Status.INFO, "Navigate to Vehicle tab");
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.uploadPhotoFromRepairPanel(vehicleElementData.getString(vehicle + "_zone_frontOuter"),
                vehicleElementData.getString(vehicle + "_position_0471_Bonnet"),
                vehicleElementData.getString(vehicle + "_0471_part_description"), photo_2_path, false);
        photoID = damageCapturingPO.getAllPhotoIDOnRepairPanel().get(0);
        testCase.get().log(Status.PASS, "Photo is found on repair panel");

        damageCapturingPO.navigationPhoto();
        testCase.get().log(Status.INFO, "Navigate to Photo tab");
        Assert.assertTrue(damageCapturingPO.isAtViewAll());
        testCase.get().log(Status.PASS, "Photo View is at All tab");

        Assert.assertTrue(isPhotoFoundInCategoryAtViewByZone(photoID, testData.getString("View_Category_FrontOuter")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_FrontOuter") + " category at By Zone view");
        Assert.assertTrue(isPhotoFoundInCategoryAtViewByPart(photoID, testData.getString("View_Category_Bonnet")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_Bonnet") + " category at By Part view");

    }

    public void uploadPhotosFromRepairPanelAfterRenew(String vehicle) throws URISyntaxException {
        String photoID;
        File file3 = new File(AttachmentTest.class.getClassLoader().getResource("images/IMG_0003.jpg").toURI());
        String photo_3_path = file3.getAbsolutePath();
        damageCapturingPO.navigationVehicle();
        testCase.get().log(Status.INFO, "Navigate to Vehicle tab");
        RepairPanel repairPanel = new RepairPanel();
        repairPanel.uploadPhotoFromRepairPanel(vehicleElementData.getString(vehicle + "_zone_frontOuter"),
                vehicleElementData.getString(vehicle + "_position_1401_windscreen"),
                vehicleElementData.getString(vehicle + "_1401_part_description"), photo_3_path, true);
        photoID = damageCapturingPO.getAllPhotoIDOnRepairPanel().get(0);
        testCase.get().log(Status.PASS, "Photo is found in repair panel");

        damageCapturingPO.navigationPhoto();
        testCase.get().log(Status.INFO, "Navigate to Photo tab");
        Assert.assertTrue(damageCapturingPO.isAtViewAll());
        testCase.get().log(Status.PASS, "Photo View is at All tab");

        Assert.assertTrue(isPhotoFoundInCategoryAtViewByZone(photoID, testData.getString("View_Category_FrontOuter")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_FrontOuter") + " category at By Zone view");
        Assert.assertTrue(isPhotoFoundInCategoryAtViewByPart(photoID, testData.getString("View_Category_Windscreen")));
        testCase.get().log(Status.PASS, "Photo is sorted in " + testData.getString("View_Category_Windscreen") + " category at By Part view");
    }

    private boolean isPhotoFoundInCategoryAtViewByZone(String photoID, String categoryTitle) {
        damageCapturingPO.clickViewByZone();
        testCase.get().log(Status.INFO, "Switch to By Zone view");
        Assert.assertTrue(damageCapturingPO.isAtViewByZone());
        testCase.get().log(Status.PASS, "Photo View is at By Zone");
        return damageCapturingPO.isPhotoFoundInCategory(photoID, categoryTitle);
    }

    private boolean isPhotoFoundInCategoryAtViewByPart(String photoID, String categoryTitle) {
        damageCapturingPO.clickViewByPart();
        testCase.get().log(Status.INFO, "Switch to By Part view");
        Assert.assertTrue(damageCapturingPO.isAtViewByPart());
        testCase.get().log(Status.PASS, "Photo View is at By Part");
        return damageCapturingPO.isPhotoFoundInCategory(photoID, categoryTitle);
    }
}