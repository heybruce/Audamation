package utils.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParametersStringBuilder {

    public static String getParametersString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            sb.append("&");
        }

        String paramsString = sb.toString();
        return paramsString.length() > 0 ? paramsString.substring(0, paramsString.length() - 1) : paramsString;
    }
}
