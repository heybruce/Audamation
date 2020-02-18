package cases.SG;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.ComparePO;
import pageobjects.processstep.DamageCapturingPO;
import pageobjects.processstep.ReportsPO;
import pageobjects.processstep.processstep.ProcessStepSGPO;
import steps.CalculationList;
import steps.CreateNewCaseSG;
import steps.DamageCapturing;
import steps.Login;
import utils.UtilitiesManager;

import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CompareSGTest extends TestBase {
    private ProcessStepSGPO processStepSGPO;
    private DamageCapturingPO damageCapturingPO;
    private ReportsPO reportsPO;
    private ComparePO comparePO;

    @BeforeClass
    @Parameters(value = {"dataFile", "vehicleElement"})
    public void setup(String dataFile, String vehicleElement) {
        vehicleElementData = UtilitiesManager.setPropertiesFile(vehicleElement);
        testData = UtilitiesManager.setPropertiesFile(dataFile);
    }

    @BeforeMethod
    public void methodSetup() {
        processStepSGPO = (ProcessStepSGPO) context.getBean("ProcessStepSGPO");
        processStepSGPO.setWebDriver(getDriver());
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        comparePO = (ComparePO)context.getBean("ComparePO");
        comparePO.setWebDriver(getDriver());
    }

    @Test(description = "Compare different calculation")
    public void compareDifferentCalculation() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("ins_username"), testData.getString("password"));

        //Create a new case
        CreateNewCaseSG createNewCase = new CreateNewCaseSG();
        createNewCase.createNewCaseWithCalculation(testData.getString("bmw320_vehicle"));

        //Switch to Damage Capturing page
        processStepSGPO.clickDamageCaptureSG();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - the second time
        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));
        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepSGPO.clickReportsSG();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the second time
        CalculationList calculationList = new CalculationList();
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Price for the 1st and 2nd calculation
        //Get the last calculation result
        Map<String, String> theFirstCalculationResult = calculationList.getCalculationResult(0);
        Map<String, String> theSecondCalculationResult = calculationList.getCalculationResult(1);

        //Switch to Compare page
        processStepSGPO.clickCompareSG();
        testCase.get().log(Status.INFO, "Switch to Compare page");

        //Verify calculations in Compare page are the same as in Report page
        //The first calculation
        Map<String, String> theFirstCompareResult = calculationList.getCompareResult(0);
        Assert.assertEquals(theFirstCompareResult, theFirstCalculationResult);
        //The second calculation
        Map<String, String> theSecondCompareResult = calculationList.getCompareResult(1);
        Assert.assertEquals(theSecondCompareResult, theSecondCalculationResult);
        testCase.get().log(Status.PASS, "Calculation are the same are in Report page");

        //Choose calculations for compare
        comparePO.clickBaseRadioBtn(0);
        testCase.get().log(Status.INFO, "Choose the first calculation as base");
        comparePO.clickCompareCheckbox(1);
        testCase.get().log(Status.INFO, "Choose the second calculation as compare");
        comparePO.clickCompareButton();
        testCase.get().log(Status.INFO, "Compare calculations");

        //Check if the second calculation are tagged as difference
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(theSecondCalculationResult.get("paintTotal")));
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(theSecondCalculationResult.get("partsTotal")));
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(theSecondCalculationResult.get("labourTotal")));
        testCase.get().log(Status.PASS, "Checked difference items");
    }
}
