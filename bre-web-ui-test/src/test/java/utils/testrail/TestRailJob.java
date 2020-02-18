package utils.testrail;

import org.apache.commons.configuration2.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.UtilitiesManager;

public class TestRailJob {
    public static Configuration testrailProp = UtilitiesManager.setPropertiesFile("testrail.properties");
    private static String projectId = testrailProp.getString("apac1_bre_project_id");
    private static final String RAILS_ENGINE_URL = testrailProp.getString("rails_engine_url");
    private static final String TESTRAIL_USERNAME = testrailProp.getString("username");
    private static final String TESTRAIL_PASSWORD = testrailProp.getString("password");
    private Configuration testsuiteProp;


    public TestrailManager manager = new TestrailManager(RAILS_ENGINE_URL,TESTRAIL_USERNAME, TESTRAIL_PASSWORD);

    @BeforeMethod
    public void beforeMethod(ITestContext context) {
        String suiteName = context.getSuite().getName();
        if(suiteName.equalsIgnoreCase("qapter")) {
            projectId = testrailProp.getString("apac1_qapter_project_id");
        }
        System.out.println("Project ID: " + projectId);
    }

    @Test
    public void closeAllTestRunsForASpecificPeriod(ITestContext context) {
        try {
            String createdAfter = String.valueOf(UtilitiesManager.getCurrentUnixTime() - 1209600); //14 days prior to current time
            String createdBefore = String.valueOf(UtilitiesManager.getCurrentUnixTime() - 86400); //1 day prior to current time
            JSONArray runs = manager.getRuns(projectId, createdAfter, createdBefore, "0", "");
            runs.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                String id = obj.get("id").toString();
                System.out.println(manager.closeRun(id).toJSONString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTestCaseCountForASpecificPeriod(ITestContext context) {
        try {
            int[] count = {0};
            String testSuiteName = context.getSuite().getName();
            String propFileName = "testsuite." + testSuiteName + ".properties";

            testsuiteProp = UtilitiesManager.setPropertiesFile(propFileName);

            String createdAfter = context.getCurrentXmlTest().getLocalParameters().get("createdAfter");
            String createdBefore = context.getCurrentXmlTest().getLocalParameters().get("createdBefore");
            JSONArray runs = manager.getAllCases("68", testsuiteProp.getString("suiteId"), createdAfter, createdBefore);

            runs.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                String isAutomation = obj.get("custom_candidate_to_automation").toString();
                if (Boolean.valueOf(isAutomation)) {
                    count[0] = count[0] + 1;
                }
            });
            System.out.println("Total automation test cases: " + count[0]);

        } catch (Exception e) {

        }
    }

    @Test
    public void deleteTestRunWithSuccessRateBelowPct(ITestContext context) {
        try {
            String createdAfter = String.valueOf(UtilitiesManager.getCurrentUnixTime() - 1209600); //14 days prior to current time
            String createdBefore = String.valueOf(UtilitiesManager.getCurrentUnixTime());
            JSONArray runs = manager.getRuns(projectId, createdAfter, createdBefore, "1", "");
            runs.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                long totalCount = (long)obj.get("passed_count") + (long)obj.get("failed_count") + (long)obj.get("untested_count");
                double successRate = calculateSuccessRate((long)obj.get("passed_count"),
                        (long)obj.get("failed_count"), (long)obj.get("untested_count"));
                System.out.println("ID: " + obj.get("id") + ", Test run success rate " + successRate + "%");
                if (successRate <= 80 || totalCount == 0) {
                    System.out.println("Delete test run ID: " + obj.get("id"));
                    System.out.println((manager.deleteRun((long)obj.get("id"))).toJSONString());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void printTestCountForASpecificPeriod(ITestContext context) {
        try {
            String createdAfter = context.getCurrentXmlTest().getLocalParameters().get("createdAfter");
            String createdBefore = context.getCurrentXmlTest().getLocalParameters().get("createdBefore");
            String country = context.getCurrentXmlTest().getLocalParameters().get("country");
            String propFileName = "testsuite." + country + ".properties";
            String testSuiteName = context.getSuite().getName();
            if(testSuiteName.equalsIgnoreCase("qapter")) {
                propFileName = "testsuite." + testSuiteName + ".properties";
            }

            testsuiteProp = UtilitiesManager.setPropertiesFile(propFileName);
            JSONArray runs = manager.getRuns(projectId, createdAfter, createdBefore, "", testsuiteProp.getString("suiteId"));

            String nameSubstring = context.getCurrentXmlTest().getLocalParameters().get("env") + "_" +
                    context.getCurrentXmlTest().getLocalParameters().get("type") + "_" +
                    context.getCurrentXmlTest().getLocalParameters().get("country") + "_" +
                    context.getCurrentXmlTest().getLocalParameters().get("browser") + "_" +
                    context.getCurrentXmlTest().getLocalParameters().get("device") + "_*";
            long[] passed = {0};
            long[] failed = {0};
            long[] untested = {0};
            runs.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                if(obj.get("name").toString().matches(nameSubstring.toUpperCase().replace("?", ".?").replace("*", ".*?"))){
                    passed[0] = passed[0] + (long)obj.get("passed_count");
                    failed[0] = failed[0] + (long)obj.get("failed_count");
                    untested[0] = untested[0] + (long)obj.get("untested_count");
                }
            });
            System.out.println(nameSubstring.toUpperCase());
            System.out.println("Passed: " + passed[0]);
            System.out.println("Failed: " + failed[0]);
            System.out.println("Untested: " + untested[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateSuccessRate(long passedCount, long failedCount, long untestedCount) {
        long totalCount = passedCount + failedCount + untestedCount;
        return passedCount * 100 / totalCount;
    }


}
