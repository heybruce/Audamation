package utils.listeners;

import com.aventstack.extentreports.Status;
import org.apache.commons.configuration2.Configuration;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import utils.UtilitiesManager;
import utils.screenRecorder.VideoEntityBuilder;
import utils.screenRecorder.VideoRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static cases.TestBase.testCase;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MediaType;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;
import static org.monte.media.VideoFormatKeys.MIME_AVI;

public class VideoRecordingListener extends TestListenerAdapter {
    private VideoRecorder screenRecorder = null;
    private Configuration videoRecordingConfig = null;
    private String targetFolder;
    private String source;
    private String destination;
    private String outputFormat = ".mp4";

    @Override
    public synchronized void onStart(ITestContext context) {
        videoRecordingConfig = UtilitiesManager.setPropertiesFile("screencast.properties");

        //Capture video
        GraphicsConfiguration gc = GraphicsEnvironment//
                .getLocalGraphicsEnvironment()//
                .getDefaultScreenDevice()//
                .getDefaultConfiguration();

        //Create a instance of ScreenRecorder with the required configurations
        try {
            targetFolder = System.getProperty("user.dir") + "/target/extentreports/"+ context.getSuite().getName()+"/videos";

            screenRecorder = new VideoRecorder(gc,
                    gc.getBounds(),
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null,
                    new File(targetFolder));

        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        //Call the stop method of ScreenRecorder to end the recording
        try {
            screenRecorder.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        // Start recording
        try {
            // Set filename
            screenRecorder.setCaseName(result.getMethod().getConstructorOrMethod().getName() + "-"
                    + result.getTestContext().getCurrentXmlTest().getLocalParameters().get("browser") + "-"
                    + String.valueOf(UtilitiesManager.getCurrentUnixTime()));

            // Start video recording
            screenRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        //Call the stop method of ScreenRecorder to end the recording
        try {
            screenRecorder.stop();
            if (!videoRecordingConfig.getBoolean("keepVideoOnSuccess")) {
                screenRecorder.removeMovieFile();
            }
            else {
                convertVideo();
                String path = "videos/" + screenRecorder.getCaseName() + outputFormat;
                testCase.get().log(Status.PASS,"",
                            VideoEntityBuilder.createScreencastFromPath(path).build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        //Call the stop method of ScreenRecorder to end the recording
        try {
            screenRecorder.stop();
            if (!videoRecordingConfig.getBoolean("keepVideoOnFail")) {
                screenRecorder.removeMovieFile();
            }
            else {
                convertVideo();
                String path = "videos/" + screenRecorder.getCaseName() + outputFormat;
                testCase.get().log(Status.FAIL,"",
                        VideoEntityBuilder.createScreencastFromPath(path).build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        //Call the stop method of ScreenRecorder to end the recording
        try {
            screenRecorder.stop();
            if (!videoRecordingConfig.getBoolean("keepVideoOnSkipped")) {
                screenRecorder.removeMovieFile();
            }
            else { //attach video to html report
                convertVideo();
                String path = "videos/" + screenRecorder.getCaseName() + outputFormat;
                testCase.get().log(Status.SKIP,"",
                        VideoEntityBuilder.createScreencastFromPath(path).build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //Call the stop method of ScreenRecorder to end the recording
        try {
            screenRecorder.stop();
            if (!videoRecordingConfig.getBoolean("keepVideoOnSkipped")) {
                screenRecorder.removeMovieFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void convertVideo(){
        source = new File(targetFolder, screenRecorder.getCaseName() + ".avi").getPath();
        destination = source.replace(".avi", outputFormat);
        UtilitiesManager.videoEncoding(source, destination);
    }

    public void attachVideoToReport(Status status){
        String fileName = screenRecorder.getCaseName() + outputFormat;
        String path = "videos/" + fileName;
        try {
            testCase.get().log(status,"",
                    VideoEntityBuilder.createScreencastFromPath(path).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
