package utils.screenRecorder;

import com.aventstack.extentreports.model.Screencast;

public class Screencam extends Screencast {
    private static final long serialVersionUID = -3413285738443448335L;

    public Screencam() {
    }

    @Override
    public String getSource() {
        return "<video id='video' width='50%' controls><source src='" + this.getPath()
                + "' type=\"video/mp4\">Your browser does not support the video tag.</video>";
    }

    public String getSourceWithIcon() {
        return this.getSource();
    }
}
