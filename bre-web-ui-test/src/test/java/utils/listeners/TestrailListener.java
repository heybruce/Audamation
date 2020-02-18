package utils.listeners;

import org.apache.commons.configuration2.Configuration;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.UtilitiesManager;
import utils.testrail.TestrailManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestrailListener extends TestListenerAdapter {

    //setup TestRail
    public static Configuration testrailProp = UtilitiesManager.setPropertiesFile("testrail.properties");
    private Configuration testsuiteProp;
    private static String projectId = testrailProp.getString("apac1_bre_project_id");
    private static final String RAILS_ENGINE_URL = testrailProp.getString("rails_engine_url");
    private static final String TESTRAIL_USERNAME = testrailProp.getString("username");
    private static final String TESTRAIL_PASSWORD = testrailProp.getString("password");
    private static final int PASSED = 1;
    private static final int SKIPPED = 3;
    private static final int FAILED = 5;
    private static Map<String, String> testRunIdMap = new HashMap<>();
    private static String testSuiteName;

    public TestrailManager manager = new TestrailManager(RAILS_ENGINE_URL,TESTRAIL_USERNAME, TESTRAIL_PASSWORD);

    @Override
    public synchronized void onStart(ITestContext context) {
        //Look up corresponding testsuite properties file
        testSuiteName = context.getSuite().getName();
        setTestsuiteProp(testSuiteName);
        //Create a RAT test run
        Map<String, Object> testRunMap = new HashMap<>();
        if (testSuiteName.equalsIgnoreCase("Qapter")) {
            projectId = testrailProp.getString("apac1_qapter_project_id");

            //look up
            List<String> caseIdList = new ArrayList<>();
            for (ITestNGMethod testMethod : context.getAllTestMethods()) {
                String testName = testMethod.getMethodName();
                String caseId = testsuiteProp.getString(testName + "_caseId");
                caseIdList.add(caseId);
            }
            testRunMap.put("qapterCaseList", caseIdList);
        }
        testRunMap.put("project_id", projectId);
        testRunMap.put("rat_type_id", testrailProp.getString("rat_type_id"));
        testRunMap.put("reg_type_id", testrailProp.getString("reg_type_id"));
        testRunMap.put("suite_id", testsuiteProp.getString("suiteId"));
        testRunMap.put("test_context", context);

        try {
            //Add a new test run based on project id, suite id, and type id. If no type id is specified,
            // all test cases will be included in the test run by default.
            JSONObject testRunInfo = (JSONObject) JSONValue.parse(manager.addTestRun(testRunMap));
            String testRunId = testRunInfo.get("id").toString();
            String testName = context.getCurrentXmlTest().getName();
            testRunIdMap.put(testName, testRunId);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {

        try {
            String testName = context.getCurrentXmlTest().getName();
            manager.closeRun(getTestRunId(testName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        String testName = result.getTestContext().getCurrentXmlTest().getName();
        String methodName = result.getMethod().getMethodName();
        String comment = methodName + " - Test Passed.";
        String caseId = testsuiteProp.getString(methodName + "_caseId");
        try {
            //add results to test run
            manager.addResultForTestCase(getTestRunId(testName), caseId, PASSED, comment);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        String testName = result.getTestContext().getCurrentXmlTest().getName();
        String methodName = result.getMethod().getMethodName();
        String comment = result.getThrowable().getMessage();
        String caseId = testsuiteProp.getString(methodName + "_caseId");
        try {
            //add result to test run
            manager.addResultForTestCase(getTestRunId(testName), caseId, FAILED, comment);
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        String testName = result.getTestContext().getCurrentXmlTest().getName();
        String methodName = result.getMethod().getMethodName();
        String caseId = testsuiteProp.getString(methodName + "_caseId");
        try {
            //add results to test run
            manager.addResultForTestCase(getTestRunId(testName), caseId, SKIPPED, "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTestRunId(String testName) {
        return testRunIdMap.get(testName);
    }

    private void setTestsuiteProp(String name) {
        String propFileName = "testsuite." + name + ".properties";
        try {
            testsuiteProp = UtilitiesManager.setPropertiesFile(propFileName);
        } catch (Exception e) {
            throw new RuntimeException("Error on setting test suite properties file.");
        }
    }
}
