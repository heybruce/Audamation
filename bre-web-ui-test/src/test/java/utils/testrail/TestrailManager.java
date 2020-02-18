package utils.testrail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import utils.UtilitiesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestrailManager {

    private APIClient client;

    public TestrailManager(String testrailUrl, String username, String password) {
        client = new APIClient(testrailUrl);
        client.setUser(username);
        client.setPassword(password);
    }

    // TestRail API wrapper methods
    public String addResultForTestCase(String testRunId, String testCaseId, int status, String comment) throws IOException, APIException {
        Map data = new HashMap();
        data.put("status_id", status);
        data.put("comment", comment);
        try {
            JSONObject response = (JSONObject) client.sendPost("add_result_for_case/" + testRunId + "/" + testCaseId, data);
            return response.toJSONString();
        } catch(APIException e) {
            return e.toString();
        }
    }

    public String addTestRun(String projectId, String suiteId, ITestContext context) throws IOException, APIException {
        String testType = context.getCurrentXmlTest().getLocalParameters().get("type");
        String browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
        String testSuiteName = context.getSuite().getName();
        Map data = new HashMap();
        data.put("suite_id", suiteId);
        data.put("name", testSuiteName + "_" + testType + "_" + browser.toUpperCase() + "_" + UtilitiesManager.getCurrentDataTimeString());
        try {
            JSONObject response = (JSONObject)client.sendPost("add_run/" + projectId, data);
            return response.toJSONString();
        } catch (APIException e) {
            return e.toString();
        }
    }

    public String addTestRun(Map<String, Object> testRunMap) throws IOException, APIException, ParseException {
        String testType = ((ITestContext)testRunMap.get("test_context")).getCurrentXmlTest().getLocalParameters().get("type");
        String browser = ((ITestContext)testRunMap.get("test_context")).getCurrentXmlTest().getLocalParameters().get("browser");
        String environment = ((ITestContext)testRunMap.get("test_context")).getCurrentXmlTest().getLocalParameters().get("env");
        String country = ((ITestContext)testRunMap.get("test_context")).getCurrentXmlTest().getLocalParameters().get("country");
        String device = ((ITestContext)testRunMap.get("test_context")).getCurrentXmlTest().getLocalParameters().get("device");
        String projectId = testRunMap.get("project_id").toString();
        String suiteName = ((ITestContext)testRunMap.get("test_context")).getSuite().getName();

        Map data = new HashMap();
        data.put("suite_id", testRunMap.get("suite_id"));
        data.put("name", environment + "_" + testType + "_" + country + "_" + browser.toUpperCase() + "_" + device.toUpperCase() + "_"
                + UtilitiesManager.getCurrentDataTimeString());
        data.put("include_all", false);

        if (suiteName.equalsIgnoreCase("Qapter")) {
            data.put("case_ids", testRunMap.get("qapterCaseList"));
        }
        else {
            //The list of test cases to be added to test run
            List<Object> caseList = new ArrayList<>();
            JSONArray caseListByType = new JSONArray();

            if (testType.equalsIgnoreCase("RAT")) {
                caseListByType = getCasesByType(projectId, testRunMap.get("suite_id").toString(), testRunMap.get("rat_type_id").toString());
            }
            if (testType.equalsIgnoreCase("Regression")) {
                caseListByType = getCasesByType(projectId, testRunMap.get("suite_id").toString(), testRunMap.get("reg_type_id").toString());
            }
            for(Object o : caseListByType){
                JSONObject item = (JSONObject)o;
                //If a test case is marked as "Automated", add it to test run
                if (item.get("custom_automated_yesno").toString().equalsIgnoreCase("true")) {
                    caseList.add(item.get("id"));
                }
            }

            data.put("case_ids", caseList);
        }

        try {
            JSONObject response = (JSONObject)client.sendPost("add_run/" + projectId, data);
            return response.toJSONString();
        } catch (APIException e) {
            return e.toString();
        }
    }

    public JSONArray getAllCases(String projectId, String suiteId, String createdAfter, String createdBefore) throws IOException, APIException {
        JSONArray response = new JSONArray();
        try {
            response = (JSONArray) client.sendGet("get_cases/" + projectId + "&suite_id=" + suiteId
                    + "&created_after=" + createdAfter + "&created_before=" + createdBefore);
            return response;
        } catch(APIException e) {
            e.printStackTrace();
        }
        return response;
    }

    public JSONArray getCasesByType(String projectId, String suiteId, String typeId) throws IOException, APIException {
        JSONArray response = (JSONArray) client.sendGet("get_cases/" + projectId + "&suite_id=" + suiteId + "&type_id=" + typeId);
        return response;
    }

    public String getRun(String runId) {
        try {
            JSONObject response = (JSONObject) client.sendGet("get_run/" + runId);
            return response.toJSONString();
        } catch (APIException e) {
            return e.toString();
        } catch (IOException ioe) {
            return ioe.toString();
        }
    }

    public JSONArray getRuns(String projectId, String createdAfter, String createdBefore, String isCompleted, String suiteId) {
        JSONArray response = new JSONArray();
        try {
            if (isCompleted.equals("0") || isCompleted.equals("1")) {
                response = (JSONArray) client.sendGet
                        ("get_runs/" + projectId + "&suite_id=" + suiteId.trim() + "&created_after=" + createdAfter
                                + "&created_before=" + createdBefore + "&is_completed=" + isCompleted);
            }
            else {
                response = (JSONArray) client.sendGet
                        ("get_runs/" + projectId + "&suite_id=" + suiteId.trim() + "&created_after=" + createdAfter
                                + "&created_before=" + createdBefore);
            }
        } catch (APIException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return response;
    }

    public JSONObject closeRun(String id) {
        JSONObject response = new JSONObject();
        try {
            response = (JSONObject) client.sendPost("close_run/" + id, " ");
        } catch (APIException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return response;
    }

    public JSONObject deleteRun(long id) {
        JSONObject response = new JSONObject();
        try {
            response = (JSONObject) client.sendPost("delete_run/" + id, " ");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
