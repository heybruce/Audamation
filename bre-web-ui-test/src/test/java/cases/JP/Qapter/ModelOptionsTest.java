package cases.JP.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import pageobjects.processstep.processstep.ProcessStepJPPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.Qapter.ModelOptions;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ModelOptionsTest extends TestBase {
    private ClaimDetailsPO claimDetails;
    private WebDriverWait wait;
    private ProcessStepJPPO processstepJPPO;
    private DamageCapturingPO damageCapturingPO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        claimDetails = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
        processstepJPPO = (ProcessStepJPPO)context.getBean("ProcessStepJPPO");
        processstepJPPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    @Test(description = "Subtype model option shown at Startup if the submodel is not selected in claim details page")
    public void modelOptionPopUp(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCase();
        //Vehicle identification
        claimDetails.selectManufacturerBySearching(testData.getString("QT_Manufacturer"));
        claimDetails.selectModelBySearching(testData.getString("QT_Model"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(ClaimDetailsPO.ID_SUB_MODEL)));
        testCase.get().log(Status.INFO, "Vehicle identification by search tree without submodel");

        //Switch to Labour Rate page
        processstepJPPO.clickLabourRatesTab();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");

        //Select labour rate
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Switch to Damage Capturing page
        processstepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.modelOptionPopUp(vehicleElementData.getString("bmw320_zone_frontOuter"));

        getDriver().switchTo().defaultContent();
        processstepJPPO.clickCalculationsTab();
        //Switch to Damage Capturing page again
        processstepJPPO.clickDamageCaptureTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_POPUP_MODEL_OPTION));
        testCase.get().log(Status.PASS, "There is no Model Option pop up shown after restart");
        Assert.assertFalse(getDriver().findElement(damageCapturingPO.ID_MODEL_OPTION_VIEW).isDisplayed());
        testCase.get().log(Status.PASS, "There is no model option view shown after restart");
    }
}
