package cases.JP;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.PreIntakePO;
import pageobjects.processstep.claimdetails.ClaimDetailsJPPO;
import pageobjects.worklistgrid.WorkListGridOpenPO;
import steps.CreateNewCaseJP;
import steps.Login;
import steps.SelectVehicle;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ClaimInfoJPTest extends TestBase{
    private WorkListGridOpenPO workListGridOpenPO;
    private PreIntakePO preIntakePO;
    private ClaimDetailsJPPO claimDetailsJPPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup(){
        workListGridOpenPO= (WorkListGridOpenPO)context.getBean("WorkListGridOpenPO");
        workListGridOpenPO.setWebDriver(getDriver());
        preIntakePO = (PreIntakePO)context.getBean("PreIntakePO");
        preIntakePO.setWebDriver(getDriver());
        claimDetailsJPPO = (ClaimDetailsJPPO) context.getBean("ClaimDetailsJPPO");
        claimDetailsJPPO.setWebDriver(getDriver());
    }

    @Test(description = "Search a vehicle by search tree")
    public void searchVehicleBySearchTree(){
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(testData.getString("benzE_vehicle"));

        String ManufacturerCodeExpected, ModelCodeExpected, SubModelCodeExpected;
        ManufacturerCodeExpected = testData.getString("benzE_manufacturer_code");
        ModelCodeExpected = testData.getString("benzE_model_code");
        SubModelCodeExpected = testData.getString("benzE_submodel_code");

        Assert.assertEquals(claimDetailsJPPO.getManufacturerCode(), ManufacturerCodeExpected);
        testCase.get().log(Status.PASS, "Manufacturer: " + ManufacturerCodeExpected);
        Assert.assertEquals(claimDetailsJPPO.getModelCode(), ModelCodeExpected);
        testCase.get().log(Status.PASS, "Model: " + ModelCodeExpected);
        Assert.assertEquals(claimDetailsJPPO.getSubModelCode(), SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Sub Model: " + SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Vehicle data retrieved from search tree was expected.");
    }

    @Test(description = "Search a vehicle by vin")
    public void searchVehicleByVin() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Claim Details
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseJP createNewCase = new CreateNewCaseJP();
        createNewCase.createNewCase();

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchByVIN(testData.getString("benzE_vehicle"));

        //Vin details
        String vinManufacturerCodeExpected, vinModelCodeExpected, vinSubmodelCodeExpected;
        vinManufacturerCodeExpected = testData.getString("benzE_manufacturer_code");
        vinModelCodeExpected = testData.getString("benzE_model_code");
        vinSubmodelCodeExpected = testData.getString("benzE_submodel_code");

        //ClaimDetails - Get the VIN query information
        String vinManufacturerCodeActual, vinModelCodeActual, vinSubmodelCodeActual;
        vinManufacturerCodeActual = claimDetailsJPPO.getManufacturerCode();
        vinModelCodeActual = claimDetailsJPPO.getModelCode();
        vinSubmodelCodeActual = claimDetailsJPPO.getSubModelCode();

        Assert.assertEquals(vinManufacturerCodeActual, vinManufacturerCodeExpected);
        testCase.get().log(Status.PASS, "Manufacturer: " + vinManufacturerCodeActual);
        Assert.assertEquals(vinModelCodeActual, vinModelCodeExpected);
        testCase.get().log(Status.PASS, "Model: " + vinModelCodeActual);
        Assert.assertEquals(vinSubmodelCodeActual, vinSubmodelCodeExpected);
        testCase.get().log(Status.PASS, "Sub Model: " + vinSubmodelCodeActual);
        testCase.get().log(Status.PASS, "Vehicle data retrieved from VIN query was expected.");
    }

}
