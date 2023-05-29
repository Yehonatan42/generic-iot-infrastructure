package iot.util;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;

public class ForwardToGateway {
	public static int forwardToGateway(JSONObject json) throws IOException {
		URL url = new URL("http://localhost:8080/IoT/gatewayServer");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonString = json.toString();
        
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(jsonString.getBytes());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        
        in.close();
        
		return responseCode;
	}
}
