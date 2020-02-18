package utils.webdrivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.configuration2.Configuration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.UtilitiesManager;
import utils.xpath.XPathBuilder;
import utils.xpath.XPathNodeBuilder;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

public class CustomDeviceDriver extends AppiumDriver {

    private static final String CONTEXT_NATIVE_APP = "NATIVE_APP";
    private static final String CONTEXT_CHROME_BROWSER = "CHROMIUM";
    private static TouchAction action;
    private static Configuration deviceCoordinates;


    public CustomDeviceDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        action = new TouchAction(this);
    }

    public void setCoordinateProperties(String deviceCoordinatesFile) {
        deviceCoordinates = UtilitiesManager.setPropertiesFile(deviceCoordinatesFile);
    }

    public void switchContextToApp() {
        this.switchContext(CONTEXT_NATIVE_APP);
    }

    public void switchContextToBrowser() {
        Set<String> contexts = this.getContextHandles();
        for (String context : contexts) {
            if (!context.equals(CONTEXT_NATIVE_APP)) {
                switchContext(context);
            }
        }
    }

    public void switchContext(String context) {
        this.context(context);
    }

    public WebElement findElementByXPathBuilder(XPathNodeBuilder nodeBuilder) {
        XPathBuilder builder = new XPathBuilder(nodeBuilder);
        return new WebDriverWait(this, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(builder.getXPath())));
    }

    public WebElement findElementByXPathBuilder(ArrayList<XPathNodeBuilder> nodeBuilders) {
        XPathBuilder builder = new XPathBuilder(nodeBuilders);
        return new WebDriverWait(this, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(builder.getXPath())));
    }

    public void clickNativeElement(WebElement element) {
        element.click();
    }

    private PointOption pointOption(int x, int y) {
        PointOption pointOption = new PointOption();
        pointOption.withCoordinates(x, y);
        return pointOption;
    }

    private WaitOptions waitOption(int waitSeconds) {
        WaitOptions waitOption = new WaitOptions().withDuration(Duration.ofMillis(waitSeconds));
        return waitOption;
    }

    public void actionTap(int x, int y) {
        action.tap(pointOption(x, y)).perform();
    }

    public void actionTap(Point location) {
        action.tap(pointOption(location.x, location.y)).perform();
    }

    public void tapCloseKeyBoard() {
        int x = deviceCoordinates.getInt("hide_keyboard_btn_x");
        int y = deviceCoordinates.getInt("hide_keyboard_btn_y");
        actionTap(x, y);
    }

    public void tapDelOnKeyBoard() {
        int x = deviceCoordinates.getInt("del_btn_x");
        int y = deviceCoordinates.getInt("del_btn_y");
        actionTap(x, y);
    }

    public void tapSymbolMode() {
        int x = deviceCoordinates.getInt("symbol_mode_x");
        int y = deviceCoordinates.getInt("symbol_mode_y");
        actionTap(x, y);
    }

    public void tapAllowBtn() {
        // Allow Camera Permission on IOS
        int x = deviceCoordinates.getInt("camera_permission_allow_btn_x");
        int y = deviceCoordinates.getInt("camera_permission_allow_btn_y");
        actionTap(x, y);
    }

    public Configuration getDeviceCoordinates() {
        return deviceCoordinates;
    }

    public Point getElementCenterPoint(WebElement element){
        Point point = element.getLocation();
        Dimension size = element.getSize();
        return new Point(point.getX() + size.width/2, point.getY() + size.height/2);
    }
}
