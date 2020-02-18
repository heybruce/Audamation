package utils.screenRecorder;

import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.MediaType;

import java.io.IOException;

public class VideoEntityBuilder {

    private static ThreadLocal<Media> media;

    private VideoEntityBuilder() {
    }

    private static VideoEntityBuilder getInstance() {
        return VideoBuilderInstance.INSTANCE;
    }

    public static VideoEntityBuilder createScreencastFromPath(String path, String title) throws IOException {
        if (path == null || path.isEmpty())
            throw new IOException("Screencast path cannot be null or empty.");

        return createScreenCast(path, title, false);
    }

    public static VideoEntityBuilder createScreencastFromPath(String path) throws IOException {
        return createScreencastFromPath(path, null);
    }

    public static VideoEntityBuilder createScreencastFromBase64String(String base64String) throws IOException {
        if (base64String == null || base64String.trim().equals(""))
            throw new IOException("Base64 string cannot be null or empty.");

        return createScreenCast(base64String, null, true);
    }

    private static VideoEntityBuilder createScreenCast(String pathOrBase64String, String title, boolean isBase64String) {
        Screencam sc = new Screencam();
        sc.setMediaType(MediaType.VID);
        if (isBase64String)
            sc.setBase64String(pathOrBase64String);
        else
            sc.setPath(pathOrBase64String);

        if (title != null)
            sc.setName(title);

        media = new ThreadLocal<Media>();
        media.set(sc);

        return getInstance();
    }

    public MediaEntityModelProvider build() {
        return new MediaEntityModelProvider(media.get());
    }

    private static class VideoBuilderInstance {
        static final VideoEntityBuilder INSTANCE = new VideoEntityBuilder();

        private VideoBuilderInstance() {
        }
    }

}
