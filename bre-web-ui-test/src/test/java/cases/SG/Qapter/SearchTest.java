package cases.SG.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageobjects.processstep.DamageCapturingPO;
import steps.CreateNewCaseSG;
import steps.Login;
import steps.Qapter.Search;
import utils.UtilitiesManager;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class SearchTest extends TestBase {
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

    @Test(description = "Search Guide number to display MOs with visible parts")
    public void searchGuideNo() {
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseSG createNewCaseSG = new CreateNewCaseSG();
        createNewCaseSG.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Search
        Search search = new Search();
        search.searchPart(vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getSearchResultPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultSheetDescription(), vehicleElementData.getString("bmw320_2385_sheet_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultGuideNo(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getSearchResultModelOptions(), vehicleElementData.getString("bmw320_2385_model_options"));
        testCase.get().log(Status.PASS, "Part search result is displayed");

        damageCapturingPO.clickSearchResultPartDescription();
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getRPGuideNumber(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getRPPartNumber(), vehicleElementData.getString("bmw320_2385_oem_part_number"));
        Assert.assertTrue(damageCapturingPO.isIsolatedPartPreviewDisplayed());
        testCase.get().log(Status.PASS, "Repair panel is opened and part is displayed isolated");
    }

    @Test(description = "Search Description to display MOs with visible parts")
    public void searchDescription(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseSG createNewCaseSG = new CreateNewCaseSG();
        createNewCaseSG.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Search
        Search search = new Search();
        search.searchPart(vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultSheetDescription(), vehicleElementData.getString("bmw320_2385_sheet_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultGuideNo(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getSearchResultModelOptions(), vehicleElementData.getString("bmw320_2385_model_options"));
        testCase.get().log(Status.PASS, "Part search result is displayed");

        damageCapturingPO.clickSearchResultPartDescription();
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getRPGuideNumber(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getRPPartNumber(), vehicleElementData.getString("bmw320_2385_oem_part_number"));
        Assert.assertTrue(damageCapturingPO.isIsolatedPartPreviewDisplayed());
        testCase.get().log(Status.PASS, "Repair panel is opened and part is displayed isolated");
    }

    @Test(description = "Search OEM code to display MOs with visible parts")
    public void searchOemCode(){
        //Launch browser
        getDriver().get(testData.getString("test_url"));
        testCase.get().log(Status.INFO, "Browser launched successfully");

        //Login
        Login login = new Login();
        login.LoginBRE(testData.getString("rep_username"), testData.getString("password"));

        //Create a new claim
        CreateNewCaseSG createNewCaseSG = new CreateNewCaseSG();
        createNewCaseSG.createNewCaseBySearchTreeForQapterTest();

        //Qapter - Search
        Search search = new Search();
        search.searchOemPart(vehicleElementData.getString("bmw320_2385_oem_part_number"));
        Assert.assertEquals(damageCapturingPO.getSearchResultPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultSheetDescription(), vehicleElementData.getString("bmw320_2385_sheet_description"));
        Assert.assertEquals(damageCapturingPO.getSearchResultGuideNo(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getOemPartSearchResultOemCode(), vehicleElementData.getString("bmw320_2385_oem_part_number"));
        Assert.assertEquals(damageCapturingPO.getOemPartSearchResultModelAndSubModel(), vehicleElementData.getString("bmw320_2385_model_submodel"));
        //Not verify model options since Qapter are showing the CLASS model options rather than the MOs in Qapter data when performing OEM part search
        //Please refer to WEBCAP-7531 for more detail
        testCase.get().log(Status.PASS, "Part search result is displayed");

        damageCapturingPO.clickSearchResultPartDescription();
        Assert.assertEquals(damageCapturingPO.getRPPartDescription(), vehicleElementData.getString("bmw320_2385_part_description"));
        Assert.assertEquals(damageCapturingPO.getRPGuideNumber(), vehicleElementData.getString("bmw320_frontRoof_guideNo"));
        Assert.assertEquals(damageCapturingPO.getRPPartNumber(), vehicleElementData.getString("bmw320_2385_oem_part_number"));
        Assert.assertTrue(damageCapturingPO.isIsolatedPartPreviewDisplayed());
        testCase.get().log(Status.PASS, "Repair panel is opened and part is displayed isolated");

    }
}
