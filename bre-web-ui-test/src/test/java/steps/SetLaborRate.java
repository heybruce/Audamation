package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.processstep.LaborRatesPO;
import utils.log.LogWriter;
import utils.log.Loggable;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class SetLaborRate extends TestBase {
    private LaborRatesPO laborRatesPO;

    public SetLaborRate() {
        laborRatesPO = (LaborRatesPO)context.getBean("LaborRatesPO");
        laborRatesPO.setWebDriver(getDriver());
    }

    public void SelectPartnership(){
        laborRatesPO.selectPartnerShipByInputValue("Manufacturer");
        new WebDriverWait(getDriver(), 10).until(
                ExpectedConditions.attributeToBeNotEmpty(getDriver().findElement(By.id(laborRatesPO.ID_LABOR_RATE1)),laborRatesPO.GET_ATTRIBUTE_VALUE));
    }

    public void SelectPartnerShipByName(String name){
        laborRatesPO.selectPartnerShipByInputValue(name);
        new WebDriverWait(getDriver(), 10).until(
                ExpectedConditions.attributeToBeNotEmpty(getDriver().findElement(By.id(laborRatesPO.ID_LABOR_RATE1)),laborRatesPO.GET_ATTRIBUTE_VALUE));
    }

    public void SelectPaintMethodByName(String value){
        laborRatesPO.selectPaintMethodByInputValue(value);
        try {
            laborRatesPO.confirmChangeRate();
        } catch (TimeoutException e) {
            LogWriter.write(this.getClass(), Loggable.Level.WARN, "No labor rate changed confirmation dialog: " + e.toString());
        }
        new WebDriverWait(getDriver(), 5).until(
                ExpectedConditions.textToBePresentInElementValue(laborRatesPO.NAME_PAINT_METHOD_VALUE, value));
        testCase.get().log(Status.INFO, "Select paint method to be '"+ value + "'");
    }

    public void addIDBC(String idbc, String value) {
        laborRatesPO.clickAddIdbcListButton();
        testCase.get().log(Status.INFO, "IDBC List is opened");
        laborRatesPO.enterIdbcAmount(idbc, value);
        laborRatesPO.clickAddIdbcButton();
        try {
            laborRatesPO.confirmChangeRate();
        } catch (TimeoutException e) {
            LogWriter.write(this.getClass(), Loggable.Level.WARN, "No labor rate changed confirmation dialog: " + e.toString());
        }
        testCase.get().log(Status.INFO, "IDBC ("+idbc+") is added");
    }

    public void addTwoIDBCs(String idbc1, String value1, String idbc2, String value2) {
        laborRatesPO.clickAddIdbcListButton();
        testCase.get().log(Status.INFO, "IDBC List is opened");
        laborRatesPO.enterIdbcAmount(idbc1, value1);
        laborRatesPO.enterIdbcAmount(idbc2, value2);
        laborRatesPO.clickAddIdbcButton();
        try {
            laborRatesPO.confirmChangeRate();
        } catch (TimeoutException e) {
            LogWriter.write(this.getClass(), Loggable.Level.WARN, "No labor rate changed confirmation dialog: " + e.toString());
        }
        testCase.get().log(Status.INFO, "IDBCs ("+idbc1+" and "+idbc2+")are added");
    }
}
