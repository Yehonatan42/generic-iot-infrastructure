package iot.gatewayserver;

import java.io.IOException;
import org.json.JSONObject;

import iot.util.ForwardToGateway;
import iot.util.HttpRequestToJSON;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registerDevice")
public class RegisterDevice extends HttpServlet {

	private static final long serialVersionUID = -3039346777230314429L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {	
		JSONObject json = HttpRequestToJSON.inputToJson(request);
        ForwardToGateway.forwardToGateway(json);
	}
}