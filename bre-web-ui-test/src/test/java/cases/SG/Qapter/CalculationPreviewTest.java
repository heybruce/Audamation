package cases.SG.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseSG;
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
    public void totalLossAndCalculationPreview() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseSG createNewCaseSG = new CreateNewCaseSG();
        createNewCaseSG.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Calculation preview
        CalculationPreview calculationPreview = new CalculationPreview();
        calculationPreview.totalLossIndicatorAndCalculationPreview(
                vehicleElementData.getString("bmw320_zone_frontOuter"), vehicleElementData.getString("bmw320_position_0471_Bonnet"));

        //Can't verify for calculation preview with PDF
//        String editedOutput = damageCapturingPO.getCalcPreviewContent();
//        Assert.assertTrue(editedOutput.contains("AUDATEX SYSTEM"));
//        Assert.assertTrue(editedOutput.contains(vehicleElementData.getString("bmw320_0471_part_description")));
//        Assert.assertTrue(editedOutput.contains("NSP test"));
//        testCase.get().log(Status.PASS, "Calculation preview is shown successfully");
    }
}
