package cases.KR;

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
import pageobjects.processstep.processstep.ProcessStepKRPO;
import steps.CalculationList;
import steps.CreateNewCaseKR;
import steps.DamageCapturing;
import steps.Login;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CompareTest extends TestBase {
    private ProcessStepKRPO processStepKRPO;
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
        processStepKRPO = (ProcessStepKRPO) context.getBean("ProcessStepKRPO");
        processStepKRPO.setWebDriver(getDriver());
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
        CreateNewCaseKR createNewCase = new CreateNewCaseKR();
        createNewCase.createNewCaseWithCalculation(
                testData.getString("plate_number"), testData.getString("bmw320_vehicle"));

        //Switch to Damage Capturing page
        processStepKRPO.clickDamageCapturingTab();
        damageCapturingPO.switchToQapterIframe();
        testCase.get().log(Status.INFO, "Switch to Damage Capturing page");

        //Damage Capturing - the second time
        //Add a standard part with replace
        DamageCapturing damageCapturing = new DamageCapturing();
        damageCapturing.addNonStandardPartReplaceWithOem();
        damageCapturing.addOldPictogramToReplaceWithOem(
                vehicleElementData.getString("bmw320_oldPictogram_AirBags"),vehicleElementData.getString("bmw320_oldPictogram_7445DriverAirBag"));
        //KR new pictogram
        //        damageCapturing.addPartsFromNewPictogramToSurfacePainting(
        //                vehicleElementData.getString("bmw320_newPictogram_bodyPainting"),vehicleElementData.getString("bmw320_newPictogram_0910_engineCompartment"));
        damageCapturing.addStandardPartToHollowCavitySealing(
                vehicleElementData.getString("bmw320_zone_rearOuter"),vehicleElementData.getString("bmw320_position_2711_rearPanelCPL"));

        //Check calculation preview before exit Qapter
        damageCapturing.waitForCalcPreviewGenerated();
        testCase.get().log(Status.INFO, "Calculation preview success");

        //Switch to Reports page
        getDriver().switchTo().defaultContent();
        processStepKRPO.clickReportsTab();
        testCase.get().log(Status.INFO, "Switch to Reports page");

        //Do calculation - the second time
        reportsPO.clickCalculateAlternative();
        testCase.get().log(Status.INFO, "Do the second calculation");

        //Price for the 1st and 2nd calculation
        String firstGrandTotalWTaxCombined = (reportsPO.getGrandTotalWTaxCombined(0));
        String firstRepairTotal= reportsPO.getRepairTotal(0);
        String firstPartsTotal= reportsPO.getPartsTotal(0);
        String firstLabourTotal= reportsPO.getLabourTotal(0);
        String firstPaintTotal= reportsPO.getPaintTotal(0);

        String secondGrandTotalWTaxCombined = reportsPO.getGrandTotalWTaxCombined(1);
        String secondRepairTotal = reportsPO.getRepairTotal(1);
        String secondPartsTotal = reportsPO.getPartsTotal(1);
        String secondLabourTotal = reportsPO.getLabourTotal(1);
        String secondPaintTotal = reportsPO.getPaintTotal(1);

        //Switch to Compare page
        processStepKRPO.clickCompareTab();
        testCase.get().log(Status.INFO, "Switch to Compare page");

        //Verify calculations in Compare page are the same as in Report page
        //The first calculation
        CalculationList calculationList = new CalculationList();
        calculationList.displayAllContentInCompareCalculationList();
        Assert.assertEquals(comparePO.getGrandTotalWithVat(0), firstGrandTotalWTaxCombined);
        Assert.assertEquals(comparePO.getRepTotal(0), firstRepairTotal);
        Assert.assertEquals(comparePO.getPartsTotal(0), firstPartsTotal);
        Assert.assertEquals(comparePO.getLaborTotal(0), firstLabourTotal);
        Assert.assertEquals(comparePO.getPaintTotal(0), firstPaintTotal);

        //The second calculation
        Assert.assertEquals(comparePO.getGrandTotalWithVat(1), secondGrandTotalWTaxCombined);
        //Not: Need to verify additional cost here, there is a issue in NewUI
        Assert.assertEquals(comparePO.getRepTotal(1), secondRepairTotal);
        Assert.assertEquals(comparePO.getPartsTotal(1), secondPartsTotal);
        Assert.assertEquals(comparePO.getLaborTotal(1), secondLabourTotal);
        Assert.assertEquals(comparePO.getPaintTotal(1), secondPaintTotal);
        testCase.get().log(Status.PASS, "Calculation are the same are in Report page");

        //Choose calculations for compare
        comparePO.chooseCalculation(0);
        testCase.get().log(Status.INFO, "Choose the first calculation as base");
        comparePO.chooseCalculation(1);
        testCase.get().log(Status.INFO, "Choose the second calculation as compare");
        comparePO.clickCompareButton();
        testCase.get().log(Status.INFO, "Compare calculations");
        comparePO.showCompareResult();
        testCase.get().log(Status.INFO, "Open compare result");

        //Check if the second calculation are tagged as difference
        //KR is not compared the paint price
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(secondRepairTotal));
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(secondPartsTotal));
        Assert.assertTrue(comparePO.isValueExistingInDiffersRecord(secondLabourTotal));
        testCase.get().log(Status.PASS, "Checked difference items");
    }
}