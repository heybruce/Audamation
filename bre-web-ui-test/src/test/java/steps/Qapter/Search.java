package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.processstep.DamageCapturingPO;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class Search  extends TestBase {
    private DamageCapturingPO damageCapturingPO;

    public Search() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    //Search for guide number or description (Automatic search)
    public void searchPart(String query) {
        damageCapturingPO.navigationSearch();
        damageCapturingPO.inputSearch(query);
        testCase.get().log(Status.INFO, "Search part by guide number or description: " + query);
    }

    //Search for OEM part number (Manually triggered)
    public void searchOemPart(String partNo) {
        damageCapturingPO.navigationSearch();
        damageCapturingPO.inputSearchOem(partNo);
        damageCapturingPO.clickSearchBtn();
        testCase.get().log(Status.INFO, "Search part by OEM part number: " + partNo);
    }
}
