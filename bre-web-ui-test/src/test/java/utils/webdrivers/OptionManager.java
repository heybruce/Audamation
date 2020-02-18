package utils.webdrivers;

import org.apache.commons.configuration2.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.ITestContext;
import utils.UtilitiesManager;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import static cases.TestBase.RUN_ON_DOCKER;
import static cases.TestBase.RUN_ON_GRID;

public class OptionManager {

    public static Configuration configProp = UtilitiesManager.setPropertiesFile("driver.properties");

    public ChromeOptions setChromeOptions(Method method, ITestContext context) {

        ChromeOptions chromeOptions = new ChromeOptions();

        String downloadPath = setDownloadDir(method, context);

        Map<String, Object> preferences = new Hashtable<>();
        preferences.put("profile.default_content_settings.popups", 0);
        preferences.put("download.prompt_for_download", "false");
        preferences.put("download.default_directory", downloadPath);
        preferences.put("plugins.plugins_disabled", new String[]{
                "Adobe Flash Player", "Chrome PDF Viewer"});
        preferences.put("plugins.always_open_pdf_externally",true);

        chromeOptions.addArguments("--window-size=1440,900");
        chromeOptions.addArguments("start-maximized");
        if (Boolean.valueOf(configProp.getString("is_headless")))
            chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--lang=" + context.getCurrentXmlTest().getLocalParameters().get("locale"));
        chromeOptions.addArguments("use-fake-ui-for-media-stream");
        chromeOptions.setExperimentalOption("prefs", preferences);

        return chromeOptions;
    }

    public FirefoxOptions setFirefoxOptions(Method method, ITestContext context) {

        FirefoxOptions firefoxOptions = new FirefoxOptions();

        String downloadPath = setDownloadDir(method, context);

        firefoxOptions.setHeadless(Boolean.valueOf(configProp.getString("is_headless")));
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("intl.accept_languages", context.getCurrentXmlTest().getLocalParameters().get("locale"));
        firefoxProfile.setPreference("browser.download.dir", downloadPath);
        firefoxProfile.setPreference("browser.download.folderList",2);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk"
                ,"application/csv,charset=utf-16,text/csv,application/vnd.ms-excel,application/pdf,application/json,images/jpg,application/zip,application/octet-stream,text/plain");
        firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
        firefoxProfile.setPreference("pdfjs.disabled", true);
        firefoxProfile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf,application/vnd.adobe.xfdf,application/vnd.fdf,application/vnd.adobe.xdp+xml");
        firefoxProfile.setPreference("pdfjs.enabledCache.state", false);
        // Disable firefox update
        firefoxProfile.setPreference("app.update.enabled", false);
        firefoxProfile.setPreference("app.update.service.enabled", false);
        firefoxProfile.setPreference("app.update.auto", false);
        firefoxProfile.setPreference("app.update.staging.enabled", false);
        firefoxProfile.setPreference("app.update.silent", false);

        firefoxProfile.setPreference("media.gmp-provider.enabled", false);

        firefoxProfile.setPreference("extensions.update.autoUpdate", false);
        firefoxProfile.setPreference("extensions.update.autoUpdateEnabled", false);
        firefoxProfile.setPreference("extensions.update.enabled", false);
        firefoxProfile.setPreference("extensions.update.autoUpdateDefault", false);

        firefoxProfile.setPreference("lightweightThemes.update.enabled", false);
        firefoxProfile.setPreference("media.navigator.streams.fake", true);

        // SetProfile for FirefoxOptions
        firefoxOptions.setProfile(firefoxProfile);

        return firefoxOptions;
    }

    private String setDownloadDir(Method method, ITestContext context) {
        String downloadPath;
        if (RUN_ON_DOCKER){
            downloadPath = configProp.getString("downloadPathDocker");
        }
        else
        if (RUN_ON_GRID) {
            downloadPath = configProp.getString("downloadPathWin");

            //use parameter in xml to determine expected OS on nodes
            if (context.getCurrentXmlTest().getLocalParameters().get("os").equalsIgnoreCase("mac")) {
                downloadPath = configProp.getString("downloadPathMac");
            }
        }
        else { //if run on local machine, we should not use "os" parameter, instead we use UtilitiesManager to determine
            //the OS on local machine
            if (UtilitiesManager.getSystem().equalsIgnoreCase("windows")) {
                downloadPath = configProp.getString("downloadPathWin");
            } else {
                downloadPath = configProp.getString("downloadPathMac");
            }
        }

        downloadPath = downloadPath + context.getAttribute(method.getName() + "_downloadFolder");
        context.setAttribute(method.getName() + "_downloadPath", downloadPath);

        return downloadPath;
    }
}
