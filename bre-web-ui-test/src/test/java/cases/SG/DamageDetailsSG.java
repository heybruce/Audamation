package cases.SG;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageDetailsPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import steps.CreateNewCaseSG;
import steps.Login;
import steps.SelectVehicle;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class DamageDetailsSG extends TestBase {
    private ProcessStepSGPO processStepSGPO;
    private DamageDetailsPO damageDetailsPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepSGPO = (ProcessStepSGPO)context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        damageDetailsPO = (DamageDetailsPO)context.getBean("DamageDetailsPO");
        damageDetailsPO.setWebDriver(getDriver());
    }

    @Test(description = "Add damage areas")
    public void addDamageAreas() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(testData.getString("bmw320_vehicle"));

        // Damage details page
        processStepSGPO.clickDamageDetailsSG();
        testCase.get().log(Status.INFO, "Switch to Damage Details page");

        //Add damage by image
        damageDetailsPO.clickImgFrontMiddle();
        testCase.get().log(Status.INFO, "Add damage by Image: Front Middle");

        Assert.assertTrue(damageDetailsPO.isCheckboxFrontMiddleChecked());
        testCase.get().log(Status.PASS, "Checkbox Front Middle is checked");

        //Add damage by checkbox
        damageDetailsPO.clickCheckboxCenterLeft();
        testCase.get().log(Status.INFO, "Add damage by Checkbox: Center Left");

        Assert.assertTrue(damageDetailsPO.isImgCenterLeftDamaged());
        testCase.get().log(Status.PASS, "Image Center Left is marked as damaged");
    }
}
