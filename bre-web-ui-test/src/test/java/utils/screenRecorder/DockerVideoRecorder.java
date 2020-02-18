package utils.screenRecorder;

public class DockerVideoRecorder {

    private static boolean enableVideoRecording = false;

    public static boolean isEnableVideoRecording() {
        return enableVideoRecording;
    }

    public static void setEnableVideoRecording(boolean enableVideoRecording) {
        enableVideoRecording = enableVideoRecording;
    }
}
