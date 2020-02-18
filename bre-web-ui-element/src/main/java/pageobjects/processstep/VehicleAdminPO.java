package pageobjects.processstep;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageobjects.PageObject;
import utils.log.Loggable;

public class VehicleAdminPO extends PageObject {

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleAdmin.plateNumber")
    private WebElement plateNumberCN;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleAdmin.plateNumber-optionalRegexTextField")
    private WebElement plateNumberKR;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleAdmin.vehicleOwner.lastName")
    private WebElement ownerLastName;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleAdmin.vehicleOwner.communicationDetailList.phone1")
    private WebElement ownerPhone;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleAdmin.vehicleOwner.communicationDetailList.mobile")
    private WebElement ownerMobile;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleEngineering.mileage")
    private WebElement mileage;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.vehicleEngineering.color")
    private WebElement vehicleColor;

    @FindBy(id = "BREForm_root.task.basicClaimData.vehicle.reducedMarketValue.expertValue.value-value")
    private WebElement vehicleValuation;

    public VehicleAdminPO() {
        super();
    }

    public VehicleAdminPO(WebDriver driver) {
        super(driver);
    }

    @Loggable
    public void enterPlateNumberKR(String plateNumberKR) {
        this.plateNumberKR.sendKeys(plateNumberKR);
    }

    @Loggable
    public void enterPlateNumberCN(String plateNumberCN) {
        this.plateNumberCN.sendKeys(plateNumberCN);
    }

    @Loggable
    public void enterOwnerLastName(String ownerLastName) { this.ownerLastName.sendKeys(ownerLastName); }

    @Loggable
    public void enterOwnerPhone(String ownerPhone) { this.ownerPhone.sendKeys(ownerPhone); }

    @Loggable
    public void enterOwnerMobile(String ownerMobile) { this.ownerMobile.sendKeys(ownerMobile); }

    @Loggable
    public void enterMileage(String mileage) { this.mileage.sendKeys(mileage); }

    @Loggable
    public void enterVehicleColor(String vehicleColor) { this.vehicleColor.sendKeys(vehicleColor); }

    @Loggable
    public void enterVehicleValuation(String vehicleValuation) { this.vehicleValuation.sendKeys(vehicleValuation); }

}
