package utils.listeners;

import org.apache.commons.configuration2.Configuration;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;
import utils.UtilitiesManager;
import utils.log.LogWriter;
import utils.log.Loggable;
import utils.slack.SlackMessageBuilder;
import utils.slack.WebAPIClient;

import java.io.IOException;

public class SlackListener extends TestListenerAdapter {

    //Slack
    public static Configuration slackProp = UtilitiesManager.setPropertiesFile("slack.test_automation.properties");
    private static final String WEBHOOK_URL = slackProp.getString("webhook_url");
    public WebAPIClient webAPIClient = new WebAPIClient();
    public SlackMessageBuilder slackMessageBuilder = new SlackMessageBuilder();

    @Override
    public synchronized void onStart(ITestContext context) {
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        //Send Slack notification
        slackMessageBuilder.composeFullMessage(context);
        try {
            String response = webAPIClient.post(WEBHOOK_URL, slackMessageBuilder.getFullMessage());
            LogWriter.write(getClass(), Loggable.Level.DEBUG, response);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
