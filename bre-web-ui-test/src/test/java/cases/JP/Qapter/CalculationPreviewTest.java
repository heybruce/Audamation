package cases.JP.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.Qapter.CalculationPreview;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CalculationPreviewTest extends TestBase {
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    @Test(description = "Add some parts to check total loss and calculation preview in Qapter")
    public void totalLossAndCalculationPreview(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCaseBySearchTreeForQapterTest(testData.getString("benzE_vehicle"));

        CalculationPreview calculationPreview = new CalculationPreview();
        calculationPreview.totalLossIndicatorAndCalculationPreview(
                vehicleElementData.getString("benzE_zone_frontOuter"), vehicleElementData.getString("benzE_position_0471_Bonnet"));

        String editedOutput = damageCapturingPO.getCalcPreviewContent();
        Assert.assertTrue(editedOutput.contains("AUDATEX SYSTEM"));
        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("benzE_0471_part_description")));
        Assert.assertTrue(editedOutput.contains("NSP test"));
        testCase.get().log(Status.PASS, "Calculation preview is shown successfully");
    }
}
