package steps;

import cases.TestBase;
import com.aventstack.extentreports.Status;
import pageobjects.processstep.ComparePO;
import pageobjects.processstep.ReportsPO;

import java.util.HashMap;
import java.util.Map;

import static utils.webdrivers.WebDriverFactory.getDriver;

public class CalculationList extends TestBase {
    private ReportsPO reportsPO;
    private ComparePO comparePO;

    public CalculationList(){
        reportsPO = (ReportsPO)context.getBean("ReportsPO");
        reportsPO.setWebDriver(getDriver());
        comparePO = (ComparePO)context.getBean("ComparePO");
        comparePO.setWebDriver(getDriver());
    }

    public void displayAllContentInReportCalculationList(){
        reportsPO.clickCustomizeBtn();
        reportsPO.checkAllCustomizeCheckedbox();
        reportsPO.clickBtnApply();
        testCase.get().log(Status.INFO, "Customize to display all content in Calculation list");
    }

    public void displayAllContentInCompareCalculationList(){
        comparePO.clickCustomizeBtn();
        comparePO.checkAllCustomizeCheckedbox();
        comparePO.clickBtnApply();
        testCase.get().log(Status.INFO, "Customize to display all content in Compare Calculation list");
    }

    public Map<String, String> getCalculationResult(int row){
        Map<String, String> calculationResult = new HashMap<String, String>();
        calculationResult.put("userId", reportsPO.getUserId(row));
        calculationResult.put("partsTotal", reportsPO.getPartsTotal(row));
        calculationResult.put("labourTotal", reportsPO.getLabourTotal(row));
        calculationResult.put("paintTotal", reportsPO.getPaintTotal(row));

        //If additional cost is 0, it shows null in report page but "0.00" in compare page
        if(reportsPO.getAdditionalCost(row).equals(""))
            calculationResult.put("additionalCost", "0.00");
        else
            calculationResult.put("additionalCost", reportsPO.getAdditionalCost(row));

        if(getCountry().equals("KR")){
            calculationResult.put("grandTotalWTaxCombined", reportsPO.getGrandTotalWTaxCombined(row));
            calculationResult.put("repairTotal", reportsPO.getRepairTotal(row));
        }else
            calculationResult.put("grandTotalWTax", reportsPO.getGrandTotalWithTax(row));
        testCase.get().log(Status.INFO, "Get calculation in row " + String.valueOf(row+1));
        return calculationResult;
    }

    public Map<String, String> getCompareResult(int row){
        Map<String, String> calculationResult = new HashMap<String, String>();
        calculationResult.put("userId", comparePO.getUserId(row));
        calculationResult.put("partsTotal", comparePO.getPartsTotal(row));
        calculationResult.put("labourTotal", comparePO.getLaborTotal(row));
        calculationResult.put("paintTotal", comparePO.getPaintTotal(row));
        calculationResult.put("additionalCost", comparePO.getAdditionalCost(row));
        if(getCountry().equals("KR")){
            calculationResult.put("grandTotalWTaxCombined", comparePO.getGrandTotalWithVat(row));
            calculationResult.put("repairTotal", comparePO.getRepTotal(row));
        }else
            calculationResult.put("grandTotalWTax", comparePO.getGrandTotalWithVat(row));
        testCase.get().log(Status.INFO, "Get compare result in row " + String.valueOf(row+1));
        return calculationResult;
    }

    /*
        if lumpSumEditableNum = calculationNum -> all lump sum field are editable
        if lumpSumEditableNum = -calculationNum -> all lump sum field are NOT editable
        if lumpSumEditableNum = 999 -> only the last calculation is editable
        if lumpSumEditableNum = -999 -> error behavior
    */
    public int getLumpSumEditableNumber(){
        int lumpSumEditableNum = 0;
        int calculationNumber = reportsPO.getCalculationNumber();
        for(int row=0; row<calculationNumber; row++){
            if(reportsPO.isLumpSumEnable(row)){
                //this is the last calculation and no previous calculation is editable
                if(row==calculationNumber-1 && lumpSumEditableNum<0 ) {
                    // mark lumpSumEditableNum as "999" (example: XXXXO or XO)
                    // for "only can be edited for the last calculation"
                    // or set lumpSumEditableNum as "1" for the only calculation (example: O)
                    if(calculationNumber==1)
                        lumpSumEditableNum = 1;
                    else
                        lumpSumEditableNum = 999;
                    break;
                }else if(row!=calculationNumber-1 && lumpSumEditableNum < -1){
                    // error example: XXXOX
                    lumpSumEditableNum = -999;
                    testCase.get().log(Status.ERROR, "There is a lump sum field not expected to be editable");
                    break;
                }
                lumpSumEditableNum++;
            }else{
                if(lumpSumEditableNum>0) {
                    // mark lumpSumEditableNum as "-999" for error (example: OOX)
                    lumpSumEditableNum = -999;
                    testCase.get().log(Status.ERROR, "There is a lump sum field not editable");
                    break;
                }
                lumpSumEditableNum--;
            }
        }
        return lumpSumEditableNum;
    }

    public boolean isAllLumpSumValueBlankInCalculationList(){
        boolean allBlank = true;
        for(int row=0; row<reportsPO.getCalculationNumber(); row++){
            if(!reportsPO.getLumpSum(row).equals("")) {
                allBlank = false;
                break;
            }
        }
        return allBlank;
    }

    public boolean isAllLumpSumValueTheSameAsGrandTotalWithVAT(){
        boolean allTheSame = true;
        for(int row=0; row<reportsPO.getCalculationNumber(); row++){
            if(!reportsPO.getLumpSum(row).equals(reportsPO.getGrandTotalWithTax(row))) {
                allTheSame = false;
                break;
            }
        }
        return allTheSame;
    }
}
