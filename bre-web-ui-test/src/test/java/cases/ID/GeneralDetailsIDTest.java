package cases.ID;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.SelectVehicle;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class GeneralDetailsIDTest extends TestBase{
    private ClaimDetailsPO claimDetailsPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(){
        claimDetailsPO = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetailsPO.setWebDriver((getDriver()));
    }

    @Test(description = "Search a vehicle by vin")
    public void searchVehicleByVin() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchByVIN(testData.getString("benzS_vehicle"));

        //Vin details
        String vinManufacturerCodeExpected, vinModelCodeExpected, vinSubmodelCodeExpected;
        vinManufacturerCodeExpected = testData.getString("benzS_manufacturer_code");
        vinModelCodeExpected = testData.getString("benzS_model_code");
        vinSubmodelCodeExpected = testData.getString("benzS_submodel_code");

        //ClaimDetails - Get the VIN query information
        String vinManufacturerCodeActual, vinModelCodeActual, vinSubmodelCodeActual;
        vinManufacturerCodeActual = claimDetailsPO.getManufacturerCode();
        vinModelCodeActual = claimDetailsPO.getModelCode();
        vinSubmodelCodeActual = claimDetailsPO.getSubModelCode();

        Assert.assertEquals(vinManufacturerCodeActual, vinManufacturerCodeExpected);
        testCase.get().log(Status.PASS, "Manufacturer: " + vinManufacturerCodeActual);
        Assert.assertEquals(vinModelCodeActual, vinModelCodeExpected);
        testCase.get().log(Status.PASS, "Model: " + vinModelCodeActual);
        Assert.assertEquals(vinSubmodelCodeActual, vinSubmodelCodeExpected);
        testCase.get().log(Status.PASS, "Sub Model: " + vinSubmodelCodeActual);
        testCase.get().log(Status.PASS, "Vehicle data retrieved from VIN query was expected.");
    }

    @Test(description = "Search a vehicle by search tree")
    public void searchVehicleBySearchTree(){
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(testData.getString("benzS_vehicle"));

        String ManufacturerCodeExpected, ModelCodeExpected, SubModelCodeExpected;
        ManufacturerCodeExpected = testData.getString("benzS_manufacturer_code");
        ModelCodeExpected = testData.getString("benzS_model_code");
        SubModelCodeExpected = testData.getString("benzS_submodel_code");

        Assert.assertEquals(claimDetailsPO.getManufacturerCode(), ManufacturerCodeExpected);
        testCase.get().log(Status.PASS, "Manufacturer: " + ManufacturerCodeExpected);
        Assert.assertEquals(claimDetailsPO.getModelCode(), ModelCodeExpected);
        testCase.get().log(Status.PASS, "Model: " + ModelCodeExpected);
        Assert.assertEquals(claimDetailsPO.getSubModelCode(), SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Sub Model: " + SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Vehicle data retrieved from search tree was expected.");
    }

}
