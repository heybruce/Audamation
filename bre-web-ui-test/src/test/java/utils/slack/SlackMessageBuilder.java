package utils.slack;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.apache.commons.configuration2.Configuration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import utils.UtilitiesManager;
import utils.testrail.TestrailManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.listeners.TestrailListener.getTestRunId;

public class SlackMessageBuilder {

    String onStartMsg;
    String onFinishMsg;
    String fullMessage;
    private JSONObject testRailInfo = new JSONObject();
    private JSONObject jenkinsInfo = new JSONObject();
    public static Configuration testrailProp = UtilitiesManager.setPropertiesFile("testrail.properties");
    public static Configuration jenkinsProp = UtilitiesManager.setPropertiesFile("jenkins.properties");
    private static final String RAILS_ENGINE_URL = testrailProp.getString("rails_engine_url");
    private static final String TESTRAIL_USERNAME = testrailProp.getString("username");
    private static final String TESTRAIL_PASSWORD = testrailProp.getString("password");

    public TestrailManager testrailManager = new TestrailManager(RAILS_ENGINE_URL,TESTRAIL_USERNAME, TESTRAIL_PASSWORD);

    public void composeOnStartMessage(ITestContext context) {
        JSONObject message = new JSONObject();
        StringBuilder builder = new StringBuilder();
        builder.append("Test run started..." + "\n");
        builder.append("Test suite: " + context.getSuite().getName() + "\n");
        builder.append("Test name: " + context.getCurrentXmlTest().getName() + "\n");
        message.put("text", builder.toString());
        onStartMsg = message.toJSONString();
    }

    public void composeOnStartMessage() {
        JSONObject message = new JSONObject();
        message.put("text", "A new test run is about to start...");
        onStartMsg = message.toJSONString();
    }

    public void composeOnFinishMessage(String runId) {
        JSONObject runInfo = getRunInfo(runId);
        StringBuilder builder = new StringBuilder();
        builder.append("Test Run Name: " + runInfo.get("name").toString() + "\n");
        builder.append("URL: " + runInfo.get("url").toString() + "\n");
        JSONObject attachment = new JSONObject();
        attachment.put("pretext", "Test run finished.");
        attachment.put("title", "Test Run ID: " + runId);
        attachment.put("text", builder.toString());

        List<JSONObject> attachments = new ArrayList<>();
        attachments.add(attachment);
        JSONObject message = new JSONObject();
        message.put("attachments", attachments);
        onFinishMsg = message.toJSONString();
    }

    public void composeFullMessage(ITestContext context) {
        String browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
        String type = context.getCurrentXmlTest().getLocalParameters().get("type");
        String country = context.getCurrentXmlTest().getLocalParameters().get("country");
        String testSuiteName = context.getSuite().getName();
        String testName = context.getCurrentXmlTest().getName();
        String env = context.getCurrentXmlTest().getLocalParameters().get("env");
        String device = context.getCurrentXmlTest().getLocalParameters().get("device");
        String jobName = env + "-" + type + "-" + testSuiteName + "-" + browser;
        if(!device.equalsIgnoreCase("pc")) {
            jobName = env + "-" + type + "-" + testSuiteName + "-" + browser + "-" + device;
        }
        setTestRailInfo(getTestRunId(testName), context);
        setJenkinsInfo(jobName);

        List<JSONObject> attachments = new ArrayList<>();
        attachments.add(testRailInfo);
        attachments.add(jenkinsInfo);
        JSONObject message = new JSONObject();
        message.put("attachments", attachments);
        fullMessage = message.toJSONString();
    }

    public String getOnFinishMsg() {
        return onFinishMsg;
    }

    public String getOnStartMsg() {
        return onStartMsg;
    }

    public String getFullMessage() { return fullMessage; }

    private JSONObject getRunInfo(String runId) {
        JSONParser parser = new JSONParser();
        JSONObject runInfo = new JSONObject();
        try {
            runInfo = (JSONObject) parser.parse(testrailManager.getRun(runId));
        } catch (ParseException pe) {
        }
        return runInfo;
    }

    private void setTestRailInfo(String runId, ITestContext context) {
        int failedCount = context.getFailedTests().size();
        int passedCount = context.getPassedTests().size();
        JSONObject runInfo = getRunInfo(runId);

        testRailInfo.put("pretext", "Test run finished. Please see below summaries:");
        testRailInfo.put("author_name", "TestRail Run ID: " + runId);
        testRailInfo.put("title", runInfo.get("name").toString());
        testRailInfo.put("title_link", runInfo.get("url").toString());
        if (failedCount > 0)
            testRailInfo.put("color", "danger");
        else
            testRailInfo.put("color", "good");

        JSONObject passedField = new JSONObject();
        passedField.put("title", "Passed");
        passedField.put("value", passedCount);
        passedField.put("short", true);
        JSONObject failedField = new JSONObject();
        failedField.put("title", "Failed");
        failedField.put("value", failedCount);
        failedField.put("short", true);
        List<JSONObject> fields = new ArrayList<>();
        fields.add(passedField);
        fields.add(failedField);

        testRailInfo.put("fields", fields);
        testRailInfo.put("footer", "TestRail");
        testRailInfo.put("ts", UtilitiesManager.getCurrentUnixTime());
    }

    private void  setJenkinsInfo(String jobName) {
        try {
            JenkinsServer jenkins = new JenkinsServer(new URI(jenkinsProp.getString("jenkinsUrl")),
                    jenkinsProp.getString("username"), jenkinsProp.getString("password"));
            Map<String, Job> jobs = jenkins.getJobs();
            JobWithDetails job = jobs.get(jobName).details();
            BuildWithDetails details = job.getLastBuild().details();

            jenkinsInfo.put("author_name", "Jenkins Job: ");
            jenkinsInfo.put("title", jobName + " Build #" + details.getNumber());
            jenkinsInfo.put("title_link", details.getUrl());
            jenkinsInfo.put("color", "#0576b9");
            jenkinsInfo.put("footer", "Jenkins");
            jenkinsInfo.put("ts", UtilitiesManager.getCurrentUnixTime());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
