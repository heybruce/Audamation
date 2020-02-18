package cases.KR.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.Login;
import steps.Qapter.Photos;
import utils.UtilitiesManager;

import java.net.URISyntaxException;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class PhotosTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
}

    @Test(description = "Upload stored photos from device")
    public void uploadStoredPhotosFromDevice () throws URISyntaxException {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Upload a photo from device storage
        Photos photos = new Photos();
        photos.uploadPhotosFromCenterBtn();

        //Upload a photo after select a zone and open a part
        photos.uploadPhotosFromRepairPanel(testData.getString("bmw320_vehicle"));

        //Upload a photo after select a zone and renew a part
        photos.uploadPhotosFromRepairPanelAfterRenew(testData.getString("bmw320_vehicle"));
    }
}
