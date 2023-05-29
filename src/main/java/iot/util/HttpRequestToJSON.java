package iot.util;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public class HttpRequestToJSON {
	
	public static JSONObject inputToJson(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		BufferedReader reader = null;
		try {
			reader = request.getReader();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(stringBuffer.toString());

		return jsonObject;
	}
}
