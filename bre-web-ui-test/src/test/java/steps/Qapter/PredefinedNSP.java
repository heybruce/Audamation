package steps.Qapter;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.processstep.DamageCapturingPO;

import java.util.HashMap;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class PredefinedNSP extends TestBase {
    private DamageCapturingPO damageCapturingPO;

    public PredefinedNSP() {
        damageCapturingPO = (DamageCapturingPO)context.getBean("DamageCapturingPO");
        damageCapturingPO.setWebDriver(getDriver());
    }

    public void checkAndPreparePNSP() {
        damageCapturingPO.clickAddNewPosition();
        damageCapturingPO.clickNonStandardTab();
        damageCapturingPO.clickSelectPredefinedNSP();
        if(isElementPresent(damageCapturingPO.ID_NO_PNSP_CONTENT)){
            testCase.get().log(Status.INFO, "There is no predefined NSP");
            damageCapturingPO.clickBackForNoPNSPButton();
            damageCapturingPO.clickNSPReplacewithOEM();
            damageCapturingPO.clickAddAsPNSPCheckbox();
            damageCapturingPO.inputNSPDescription("PNSP Automation");
            damageCapturingPO.inputNSPSparePart("PNSP_AutoTest");
            damageCapturingPO.inputNSPWorkUnits("99");
            damageCapturingPO.inputNSPAmount("876543");
            damageCapturingPO.clickNSPAddPosition();
            testCase.get().log(Status.INFO, "Add a non-standard part as predefined NSP");
            // Remove the added nsp in checklist
            damageCapturingPO.navigationChecklist();
            damageCapturingPO.deleteChecklistItem(0);
            damageCapturingPO.navigationVehicle();
        }else {
            testCase.get().log(Status.INFO, "There is existing predefined NSP");
            damageCapturingPO.clickClosePNSPList();
        }
    }

    public Map<String, String> addPNSPintoChecklist(){
        if(!getDriver().findElement(damageCapturingPO.ID_ADD_POSITION_MAIN_SECTION).isDisplayed())
            damageCapturingPO.clickAddNewPosition();
        damageCapturingPO.clickNonStandardTab();
        damageCapturingPO.clickSelectPredefinedNSP();
        damageCapturingPO.clickPNSPAddIcon(0);
        Map<String, String> pnsp = new HashMap<>();
        pnsp.put("description", damageCapturingPO.getPNSPListPartDescription(0));
        pnsp.put("guideNo", damageCapturingPO.getPNSPListGuideNumber(0));
        pnsp.put("partNo", damageCapturingPO.getPNSPListPartNumber(0));
        pnsp.put("partPrice", damageCapturingPO.getPNSPListPartPrice(0));
        pnsp.put("repairMethod", damageCapturingPO.getPNSPListRepairMethod(0));
        pnsp.put("workUnits", damageCapturingPO.getPNSPListWorkUnits(0));
        damageCapturingPO.clickPNSPListDoneButton();
        damageCapturingPO.navigationChecklist();
        testCase.get().log(Status.INFO, "Add a predefined NSP into Checklist");

        //return the added PNSP for verification
        return pnsp;
    }

    public Map<String, String> editPNSPInChecklist(){
        Map<String, String> pnsp = new HashMap<>();
        pnsp.put("description", "Edit_PNSP_Desc");
        pnsp.put("partNo", "Edit_PNSP_Part");
        pnsp.put("partPrice", "12345");

        //Add a PNSP into Checklist
        addPNSPintoChecklist();
        damageCapturingPO.clickChecklistItem(1);
        damageCapturingPO.clickNSPReplacewithOEM();
        testCase.get().log(Status.INFO, "Uncheck repair method: Replace with OEM");
        damageCapturingPO.clickNSPRepair();
        testCase.get().log(Status.INFO, "Re-select repair method: Repair");
        damageCapturingPO.inputNSPSparePart(pnsp.get("partNo"));
        damageCapturingPO.inputNSPDescription(pnsp.get("description"));
        if(isMobileDevice()){
            damageCapturingPO.clickNSPAmount();
            damageCapturingPO.clickKeypadClearAll();
            damageCapturingPO.clickKeypadNumber("1");
            damageCapturingPO.clickKeypadNumber("2");
            damageCapturingPO.clickKeypadNumber("3");
            damageCapturingPO.clickKeypadNumber("4");
            damageCapturingPO.clickKeypadNumber("5");
            damageCapturingPO.clickKeypadConfirm();
        }else{
            damageCapturingPO.inputNSPAmount(pnsp.get("partPrice"));
        }
        damageCapturingPO.clickNSPAddPosition();
        damageCapturingPO.waitForQapterLoading();
        testCase.get().log(Status.INFO, "Edit a predefined NSP in Checklist");

        //Return the edited PNSP for verification
        return pnsp;
    }
}
