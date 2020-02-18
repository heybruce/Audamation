package utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;

public class ResponseBuilder {

    public static String getFullResponse(HttpURLConnection con) throws IOException {

        StringBuilder responseBuilder = new StringBuilder();

        responseBuilder
                .append(con.getResponseCode())
                .append(" ")
                .append(con.getResponseMessage())
                .append("\n");

        con.getHeaderFields()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null)
                .forEach(entry -> {
                    responseBuilder.append(entry.getKey()).append(": ");

                    List<String> headerValues = entry.getValue();
                    Iterator<String> it = headerValues.iterator();

                    if (it.hasNext()) {
                        responseBuilder.append(it.next());

                        while (it.hasNext()) {
                            responseBuilder.append(", ").append(it.next());
                        }
                    }

                    responseBuilder.append("\n");
                });

        Reader streamReader = null;
        if (con.getResponseCode() > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        }
        else  {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();

        responseBuilder.append("Response: ").append(content);

        return responseBuilder.toString();
    }
}
