package cases.ID.Qapter;

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
import pageobjects.processstep.processstep.ProcessStepIDPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.Qapter.ModelOptions;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ModelOptionsTest extends TestBase {
    private DamageCapturingPO damageCapturingPO;
    private ClaimDetailsPO claimDetailsPO;
    private ProcessStepIDPO processStepIDPO;
    private WebDriverWait wait;

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
        claimDetailsPO = (ClaimDetailsPO) context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 5);
        processStepIDPO = (ProcessStepIDPO) context.getBean("ProcessStepIDPO");
        processStepIDPO.setWebDriver(getDriver());
    }

    @Test(description = "Subtype model option shown at Startup if the submodel is not selected in claim details page")
    public void modelOptionPopUp(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a claim without submodel
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCase();
        claimDetailsPO.selectManufacturerBySearching(testData.getString("QT_Manufacturer"));
        claimDetailsPO.selectModelBySearching(testData.getString("QT_Model"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(ClaimDetailsPO.ID_SUB_MODEL)));
        testCase.get().log(Status.INFO, "Vehicle identification by search tree without submodel");

        //Select labour rate
        processStepIDPO.clickLaborRateID();
        testCase.get().log(Status.INFO, "Switch to Labour Rate page");
        SetLaborRate setLaborRate = new SetLaborRate();
        setLaborRate.SelectPartnership();
        testCase.get().log(Status.INFO, "Select the first of partnership value");

        //Damage Capture
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.modelOptionPopUp(vehicleElementData.getString("audiA1_zone_frontOuter"));

        getDriver().switchTo().defaultContent();
        processStepIDPO.clickReportsID();
        testCase.get().log(Status.INFO, "Switch to Reports page");
        //Switch back to Qapter
        processStepIDPO.clickDamageCaptureID();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch back to Damage Capturing page");

        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_POPUP_MODEL_OPTION));
        testCase.get().log(Status.PASS, "There is no Model Option pop up shown after restart");
        Assert.assertFalse(getDriver().findElement(damageCapturingPO.ID_MODEL_OPTION_VIEW).isDisplayed());
        testCase.get().log(Status.PASS, "There is no model option view shown after restart");
    }
}
