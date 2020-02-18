package utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import utils.http.ParametersStringBuilder;
import utils.http.ResponseBuilder;
import utils.webdrivers.OptionManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilitiesManager {

    private static Configurations configs = new Configurations();
    private static PropertiesConfiguration propConfig = null;

    public static Configuration setPropertiesFile(String fileName) {
        try {
            FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, "UTF-8");

            propConfig = configs.properties(UtilitiesManager.class.getClassLoader().getResource(fileName));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return propConfig;
    }

    public static long getCurrentUnixTime() {
        Date date = new Date();
        return date.getTime() / 1000;
    }

    public static String getCurrentDataTimeString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "Windows";
        } else if (os.contains("nux") || os.contains("nix")) {
            return "Linux";
        } else if (os.contains("mac")) {
            return "Mac";
        } else if (os.contains("sunos")) {
            return "Solaris";
        } else {
            return "Other";
        }
    }

    public static String getImgPath(String fileName) {
        String path = null;
        switch (getSystem()) {
                case "Windows":
                    path = System.getProperty("win.img.path") + File.separator + fileName;
                    break;
                case "Mac":
                    path = System.getProperty("mac.img.path") + File.separator + fileName;
                    break;
        }
        return path;
    }

    public static String getAtchPath(String fileName) {
        String path = null;
        switch (getSystem()) {
            case "Windows":
                path = System.getProperty("win.atch.path") + File.separator + fileName;
                break;
            case "Mac":
                path = System.getProperty("mac.atch.path") + File.separator + fileName;
                break;
        }
        return path;
    }

//    public static void killDriverServers () {
//        if (getSystem().equals("Windows")) {
//            try {
//                Runtime.getRuntime().exec(String.format("taskkill /F /IM geckodriver-%s-win64.exe /T"
//                        , propConfig.getString("geckoDriverVersion")));
//                Runtime.getRuntime().exec("taskkill /F /IM chromedriver-2.38.exe /T");
//                Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe /T");
//            } catch (IOException e) {
//                System.out.println("Cannot kill ChromeDriver_GeckoDriver_IEDriverserver");
//                e.printStackTrace();
//            }
//        }
//        else {
//            if (getSystem().equals("Mac")){
//                try {
//                    Runtime.getRuntime().exec("killall -KILL firefox-bin");
//                    Runtime.getRuntime().exec(String.format("killall -KILL geckodriver-%s-macos"
//                            , propConfig.getString("geckoDriverVersion")));
//                } catch (IOException e) {
//                    System.out.println("Cannot kill ChromeDriver_GeckoDriver_IEDriverserver");
//                    e.printStackTrace();
//                }
//            }
//            if (getSystem().equals("Linux")){
//                try {
//                    // Runtime.getRuntime().exec("ps auxm | grep PlatformUIAutomation | awk '{system(\"kill -9 \" $2)}'");
//                    Runtime.getRuntime().exec("killall -KILL firefox");
//                    Runtime.getRuntime().exec(String.format("killall -KILL geckodriver-%s-linux64"
//                            , propConfig.getString("geckoDriverVersion")));
//                } catch (IOException e) {
//                    System.out.println("Cannot kill ChromeDriver_GeckoDriver_IEDriverserver");
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public static void createDirectory(String dirPath) {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void videoEncoding(String source, String destination) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(new String[]{"ffmpeg", "-i", source, "-pix_fmt", "yuv420p", destination});
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
            System.out.println("Video converted successfully!");
            in.close();
            deleteFile(source);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);

        try {
            Files.deleteIfExists(path);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    public static double getImgDifferencePercent(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)"
                    , width, height, width2, height2));
        }

        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;

        return 100.0 * diff / maxDiff;
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }

    public static void unZipOnSmb(String downloadFolder, String zipFileName) {

        String zipFile;
        String outputFolder;
        if (UtilitiesManager.getSystem().equalsIgnoreCase("windows")) {
            zipFile = OptionManager.configProp.getString("downloadPathWin") + downloadFolder
                    + File.separator + zipFileName + ".zip";
            outputFolder = OptionManager.configProp.getString("downloadPathWin") + downloadFolder
                    + File.separator + "decompressed-attachments";
        }
        else {
            zipFile = OptionManager.configProp.getString("downloadPathMac") + downloadFolder
                    + File.separator + zipFileName + ".zip";
            outputFolder = OptionManager.configProp.getString("downloadPathMac") + downloadFolder
                    + File.separator + "decompressed-attachments";
        }

        byte[] buffer = new byte[1024];

        try {
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getPath());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                File file = new File(outputFolder + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(file);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isFilesIdentical(String file1Path, String file2Path) throws IOException {
        FileChannel ch1 = new RandomAccessFile(file1Path, "r").getChannel();
        FileChannel ch2 = new RandomAccessFile(file2Path, "r").getChannel();
        if (ch1.size() != ch2.size()) {
            System.out.println("Files have different length");
            return false;
        }
        long size = ch1.size();
        ByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        ByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);
        for (int pos = 0; pos < size; pos++) {
            if (m1.get(pos) != m2.get(pos)) {
                System.out.println("Files differ at position " + pos);
                return false;
            }
        }
        System.out.println("Files are identical.");
        return true;
    }

    public static List<String> getFileNamesFromFolder(String path) {
        List<String> results = new ArrayList<>();

        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static boolean isFileDownloadable(String link) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpRequestBase request = new HttpHead(link);
        HttpResponse response = httpClient.execute(request);
        String contentType = response.getFirstHeader("Content-Type").getValue();
        int contentLength = Integer.parseInt(response.getFirstHeader("Content-Length").getValue());

        return contentType.equals("application/octet-stream") && contentLength != 0;
    }

    public static void isValidDownloadRequest() throws IOException{

        Map<String, String> parameters = new HashMap<>();
        parameters.put("attachmentGroupIds", "ClaimsDocument,VehicleBeforeRepair,VehicleAfterRepair,OTHER");
        parameters.put("attachmentIds", "1C875884-4704-624D-394F-8C97996BAA8E,5717F906-0504-6BBA-051C-1DF211EFB558,EEFE0EA6-694A-0FC8-CB4E-C76D9A18F402,091AC9F9-B95E-CB2D-4825-3C308408E88C");
        parameters.put("CSRF-TOKEN", "$2a$10$DuQP0Q.3EiQ9IzyqZR4VU.cE57JJkqICqqNv6xL7p0WcKv08auVN.");
        parameters.put("dataObjectItemId", "D43F7F1B-2D3A-E37F-C51F-8444F99F25D0");
        parameters.put("debugMode", "false");
        parameters.put("location", "GLOBAL");
        parameters.put("locationHandler", "TaskAttachmentsLocationHandler");
        parameters.put("process", "BRE");
        parameters.put("rnd", "1559554648");
        parameters.put("step", "Attachments Repairer");
        parameters.put("taskId", "D43F7F1B-2D3A-E37F-C51F-8444F99F25D0");

        URL url = new URL("https://www-int2.audatex.sg/breservices/v1.0/attachment/zip");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParametersStringBuilder.getParametersString(parameters));
        out.flush();
        out.close();

        String fullResponse = ResponseBuilder.getFullResponse(con);

        String contentType = con.getHeaderField("Content-Type");
        String contentDisposition = con.getHeaderField("Content-Disposition");
    }
}

