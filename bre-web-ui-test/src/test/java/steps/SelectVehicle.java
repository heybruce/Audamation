package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.claimdetails.ClaimDetailsPO;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class SelectVehicle extends TestBase {
    private ClaimDetailsPO claimDetails;
    private WebDriverWait wait;

    public SelectVehicle(){
        claimDetails = (ClaimDetailsPO)context.getBean("ClaimDetailsPO");
        claimDetails.setWebDriver(getDriver());
        wait = new WebDriverWait(getDriver(), 10);
    }

    public void SearchBySearchTree(String vehicle) {
        claimDetails.selectManufacturerBySearching(testData.getString(vehicle+"_manufacturer_code"));
        claimDetails.selectModelBySearching(testData.getString(vehicle+"_model_code"));
        claimDetails.selectSubmodelBySearching(testData.getString(vehicle+"_submodel_code"));

        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_MANUFACTURER_AXCODE), testData.getString(vehicle+"_manufacturer_code")));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_MODEL_AXCODE), testData.getString(vehicle+"_model_code")));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_SUB_MODEL_AXCODE), testData.getString(vehicle+"_submodel_code")));
        testCase.get().log(Status.INFO, "Vehicle identification by search tree");
    }

    public void SearchByVIN(String vehicle){
        claimDetails.enterVin(testData.getString(vehicle+"_vin"));
        testCase.get().log(Status.INFO, "Enter VIN: " + testData.getString(vehicle+"_vin"));

        claimDetails.clickVinQuery();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(ClaimDetailsPO.ID_ERROR_NOTIFICATION));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_MANUFACTURER_AXCODE), testData.getString(vehicle+"_manufacturer_code")));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_MODEL_AXCODE), testData.getString(vehicle+"_model_code")));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.name(ClaimDetailsPO.NAME_SUB_MODEL_AXCODE), testData.getString(vehicle+"_submodel_code")));
        testCase.get().log(Status.INFO, "Vehicle identification by VIN");
    }
}
