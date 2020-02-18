package cases.ID.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseID;
import steps.Login;
import steps.Qapter.PredefinedNSP;
import utils.UtilitiesManager;

import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class PositionTest extends TestBase {
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

    @Test(description = "NSP - Display predefined NSPs")
    public void displayPNSP(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Position
        PredefinedNSP pnsp = new PredefinedNSP();
        pnsp.checkAndPreparePNSP();
        Map<String, String> addedPNSP = pnsp.addPNSPintoChecklist();

        String PnspPrice = addedPNSP.get("partPrice").replace(vehicleElementData.getString("ID_currency_symbol"), "");

        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), addedPNSP.get("description"));
        Assert.assertEquals(damageCapturingPO.getChecklistGuideNumber(1), addedPNSP.get("guideNo"));
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), addedPNSP.get("partNo"));
        Assert.assertEquals(damageCapturingPO.getChecklistPrice(1), PnspPrice);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), addedPNSP.get("repairMethod"));
        Assert.assertEquals(damageCapturingPO.getChecklistLabour(1), addedPNSP.get("workUnits"));
        testCase.get().log(Status.PASS, "Added predefined NSP is displayed in Checklist");
    }

    @Test(description = "NSP - Edit predefined NSP in Checklist")
    public void editPNSP() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseID createNewCase = new CreateNewCaseID();
        createNewCase.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Position
        PredefinedNSP pnsp = new PredefinedNSP();
        pnsp.checkAndPreparePNSP();
        Map<String, String> editedPnsp = pnsp.editPNSPInChecklist();

        String PnspPrice = editedPnsp.get("partPrice");

        Assert.assertEquals(damageCapturingPO.getChecklistPartDescription(1), editedPnsp.get("description"));
        Assert.assertEquals(damageCapturingPO.getChecklistPartNumber(1), editedPnsp.get("partNo"));
        Assert.assertEquals(damageCapturingPO.getChecklistPrice(1), PnspPrice);
        Assert.assertEquals(damageCapturingPO.getChecklistRepairMethod(1), vehicleElementData.getString("repair"));
        testCase.get().log(Status.PASS, "Changes of Predefined NSP is saved in Checklist");
    }
}
