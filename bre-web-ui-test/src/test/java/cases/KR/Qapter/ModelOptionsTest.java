package cases.KR.Qapter;

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
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CreateNewCaseKR;
import steps.DamageCapturing;
import steps.Login;
import steps.Qapter.ModelOptions;
import steps.Qapter.ZoneAndLayout;
import steps.SetLaborRate;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ModelOptionsTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
    private DamageCapturingPO damageCapturingPO;
    private ClaimDetailsPO claimDetails;
    private WebDriverWait wait;

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
        claimDetails = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCase(testData.getString("plate_number"));
        //Vehicle identification
        claimDetails.selectManufacturerBySearching(testData.getString("QT_Manufacturer"));
        claimDetails.selectModelBySearching(testData.getString("QT_Model"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(ClaimDetailsPO.ID_SUB_MODEL)));
        testCase.get().log(Status.INFO, "Vehicle identification by search tree without submodel");

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

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.modelOptionPopUp(vehicleElementData.getString("audiA1_zone_frontOuter"));

        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();

        //Switch to Damage Capturing page again
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");
        Assert.assertFalse(isElementPresent(damageCapturingPO.ID_POPUP_MODEL_OPTION));
        testCase.get().log(Status.PASS, "There is no Model Option pop up shown after restart");
        Assert.assertFalse(getDriver().findElement(damageCapturingPO.ID_MODEL_OPTION_VIEW).isDisplayed());
        testCase.get().log(Status.PASS, "There is no model option view shown after restart");
    }

    @Test(description = "Switch back to navigation screen without MO selection")
    public void cancelModelOptionSelection(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCase(testData.getString("plate_number"));
        //Vehicle identification
        claimDetails.selectManufacturerBySearching(testData.getString("QT_Manufacturer"));
        claimDetails.selectModelBySearching(testData.getString("QT_Model"));
        claimDetails.selectSubmodelBySearching(testData.getString("QT_SubModel"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_SUB_MODEL_AXCODE), testData.getString("QT_SubModel")));
        testCase.get().log(Status.INFO, "Vehicle identification by search tree");

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.cancelModelOptionSelection(vehicleElementData.getString("audiA1_zone_engine"), testData.getString("Error_SelectModelOptionToSeeGraphics"));

        //Verify navigation by arrow
        ZoneAndLayout zoneAndLayout = new ZoneAndLayout();
        zoneAndLayout.navigateByRightArrow(By.id(vehicleElementData.getString("audiA1_position_7300_powerSteering")));
        testCase.get().log(Status.PASS, "Can navigate by right arrow after cancel model option selection");

        //Verify navigation by menu
        zoneAndLayout.navigateThroughMenus(By.id(vehicleElementData.getString("audiA1_zone_engine")));
        testCase.get().log(Status.PASS, "Can navigate through menu after cancel model option selection");
    }

    @Test(description = "Custom MOs behave as standard")
    public void customModelOptions() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Add custom model option
        String description = "Custom MO";
        String longDescription = "Custom MO 12&45-7890";
        String updatedDescription = "Custom MO edited";
        String updatedLongDescription = "Custom MO edited Long";
        ModelOptions modelOptions = new ModelOptions();
        damageCapturingPO.navigationModelOptions();
        modelOptions.addCustomModelOption(description);
        modelOptions.verifyAddedCustomModelOptionDescription(0, description);
        testCase.get().log(Status.PASS, "Custom model option is added successfully.");

        //Add a custom model option that description exceeds 19 characters
        modelOptions.addCustomModelOption(longDescription);
        modelOptions.verifyAddedCustomModelOptionDescription(1, longDescription.substring(0, 19));
        testCase.get().log(Status.PASS, "No more than 19 characters are allowed for the custom model option description");

        //Edit custom model option
        modelOptions.editCustomModelOption(0, updatedDescription);
        modelOptions.verifyAddedCustomModelOptionDescription(0, updatedDescription);
        testCase.get().log(Status.PASS, "Custom model option can be edited");

        //Edit custom model option for description exceeds 19 characters
        modelOptions.editCustomModelOption(0, updatedLongDescription);
        modelOptions.verifyAddedCustomModelOptionDescription(0, updatedLongDescription.substring(0, 19));
        testCase.get().log(Status.PASS, "Custom model option can be edited with description no more than 19 characters");

        //Delete custom model option
        modelOptions.deleteCustomModelOption(1);
        testCase.get().log(Status.PASS, "Custom model option is deleted successfully");
    }

    @Test(description = "Remove MO, click \"Yes, continue\" button of the conflict window")
    public void confirmModelOptionRemoveConflictDialog() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        ModelOptions modelOptions = new ModelOptions();
        modelOptions.deleteModelOptionWithDamageCaptured(vehicleElementData.getString("bmw320_zone_rearOuter"),
                vehicleElementData.getString("bmw320_position_3151_rearScreen"),
                vehicleElementData.getString("bmw320_3151_part_description"),
                vehicleElementData.getString("bmw320_w5_code"),
                vehicleElementData.getString("bmw320_3151_part_guideNo"));
    }

    @Test(description = "Edit a standard Model Option")
    public void editModelOption() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseForQapterTest();

        //Edit model option but cancel
        String editedDescription = "Edited MO";
        String modelOptionCode = vehicleElementData.getString("bmw320_w5_code");
        ModelOptions modelOptions = new ModelOptions();
        damageCapturingPO.navigationModelOptions();
        modelOptions.editModelOption(modelOptionCode, editedDescription, false);

        //Edit model option and continue
        modelOptions.editModelOption(modelOptionCode, editedDescription, true);
    }
}
