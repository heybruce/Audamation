package cases.KR;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.IBOSSearchPO;
import pageobjects.processstep.claimdetails.ClaimDetailsKRPO;
import steps.CreateNewCaseKR;
import steps.Login;
import steps.SelectVehicle;
import utils.UtilitiesManager;

import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class ClaimDetailsTest extends TestBase {
    private ClaimDetailsKRPO claimDetails;
    private IBOSSearchPO IBOSSearchPO;

    @BeforeClass
    @Parameters(value = {"dataFile"})
    public void setup(String dataFile) {
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        claimDetails = (ClaimDetailsKRPO) context.getBean("ClaimDetailsKRPO");
        claimDetails.setWebDriver(getDriver());
        IBOSSearchPO  = (IBOSSearchPO)context.getBean("IBOSSearchPO");
        IBOSSearchPO.setWebDriver(getDriver());
    }

    @Test(description = "Search vehicle by VIN query")
    public void searchVehicleByVin() {
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Claim Details
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCase(testData.getString("plate_number"));

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchByVIN(testData.getString("bmw320_vehicle"));

        //Vin details
        String vinManufacturerCodeExpected, vinModelCodeExpected, vinSubmodelCodeExpected;
        vinManufacturerCodeExpected = testData.getString("bmw320_manufacturer_code");
        vinModelCodeExpected = testData.getString("bmw320_model_code");
        vinSubmodelCodeExpected = testData.getString("bmw320_submodel_code");

        //ClaimDetails - Get the VIN query information
        String vinManufacturerCodeActual, vinModelCodeActual, vinSubmodelCodeActual;
        vinManufacturerCodeActual = claimDetails.getManufacturerCode();
        vinModelCodeActual = claimDetails.getModelCode();
        vinSubmodelCodeActual = claimDetails.getSubModelCode();

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
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCase(testData.getString("plate_number"));

        SelectVehicle selectVehicle = new SelectVehicle();
        selectVehicle.SearchBySearchTree(testData.getString("bmw320_vehicle"));

        String ManufacturerCodeExpected, ModelCodeExpected, SubModelCodeExpected;
        ManufacturerCodeExpected = testData.getString("bmw320_manufacturer_code");
        ModelCodeExpected = testData.getString("bmw320_model_code");
        SubModelCodeExpected = testData.getString("bmw320_submodel_code");

        Assert.assertEquals(claimDetails.getManufacturerCode(), ManufacturerCodeExpected);
        testCase.get().log(Status.PASS, "Manufacturer: " + ManufacturerCodeExpected);
        Assert.assertEquals(claimDetails.getModelCode(), ModelCodeExpected);
        testCase.get().log(Status.PASS, "Model: " + ModelCodeExpected);
        Assert.assertEquals(claimDetails.getSubModelCode(), SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Sub Model: " + SubModelCodeExpected);
        testCase.get().log(Status.PASS, "Vehicle data retrieved from search tree was expected.");
    }

    @Test(description = "Query case via IBOS case search")
    public void queryCaseByIBOS() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("IBOS_username"), testData.getString("password"));
        testCase.get().log(Status.INFO, "Login successfully");

        //Create a new case
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCase(testData.getString("plate_number"));

        //Search for IBOS case
        claimDetails.clickSearchIBOSCase();
        testCase.get().log(Status.INFO, "Open IBOS case search Dialog");

        IBOSSearchPO.selectInsuranceCompany(testData.getString("IBOS_insurance_company"));
        IBOSSearchPO.enterPlateNumber(testData.getString("IBOS_plate_number"));
        IBOSSearchPO.enterTaxId(testData.getString("IBOS_tax_id"));
        IBOSSearchPO.clickSearchButton();
        testCase.get().log(Status.INFO, "Search for IBOS case");

        Assert.assertFalse(isAlertPresent());

        Map<String, String> firstResult = IBOSSearchPO.getTheFirstSearchResult();
        Assert.assertNotNull(firstResult.get("id"));
        Assert.assertNotNull(firstResult.get("WIP"));
        Assert.assertNotNull(firstResult.get("contactMobile"));
        Assert.assertNotNull(firstResult.get("contactPerson"));
        Assert.assertNotNull(firstResult.get("coverageTypeCode"));
        Assert.assertNotNull(firstResult.get("damagedObjectSerialNo"));
        testCase.get().log(Status.PASS, "Get search result from IBOS successfully");

        //Choose the search result
        IBOSSearchPO.chooseTheFirstSearchResult();
        Assert.assertEquals(claimDetails.getClaimNumber(), firstResult.get("WIP") + " " + firstResult.get("coverageTypeCode") + firstResult.get("damagedObjectSerialNo"));
        Assert.assertEquals(claimDetails.getPlateNumber(), testData.getString("IBOS_plate_number"));
        Assert.assertEquals(claimDetails.getTaxNumber(), testData.getString("IBOS_tax_id"));
        testCase.get().log(Status.PASS, "Claim is retrieved from IBOS Search, claim number: " + claimDetails.getClaimNumber());

    }
}
