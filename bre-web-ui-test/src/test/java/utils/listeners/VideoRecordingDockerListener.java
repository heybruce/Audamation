package utils.listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.screenRecorder.VideoEntityBuilder;

import java.io.IOException;

import static cases.TestBase.RUN_ON_DOCKER;
import static cases.TestBase.testCase;

public class VideoRecordingDockerListener extends TestListenerAdapter {
    private String videoFilePath = null;
    private String outputFormat = ".mp4";

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        ITestContext testContext = result.getTestContext();
        
        if (RUN_ON_DOCKER) {
            try {
                videoFilePath = System.getProperty("seleniumGridHub") + "/dashboard/" + testContext.getAttribute(result.getName() + "_videoPath").toString() + outputFormat;
                testCase.get().log(Status.FAIL, "",
                        VideoEntityBuilder.createScreencastFromPath(videoFilePath).build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
