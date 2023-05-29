package iot.gatewayserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.json.JSONObject;

import iot.gatewayserver.commands.CreateProductCommand;
import iot.gatewayserver.commands.CreateCompanyCommand;
import iot.gatewayserver.commands.CreateDeviceCommand;
import iot.gatewayserver.commands.UpdateDeviceCommand;
import iot.gatewayserver.threadpool.ThreadPool;
import iot.util.HttpRequestToJSON;

@WebServlet("/gatewayServer")
public class GatewayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ThreadPool<?> threadPool= new ThreadPool<>(8);
	SingletonCommandFactory<String, JSONObject, JSONObject> singletonCommandFactory = SingletonCommandFactory.getInstance();
	
	public GatewayServlet() {
		singletonCommandFactory.add("CreateProduct", new CreateProductCommand());
		singletonCommandFactory.add("CreateCompany", new CreateCompanyCommand());
		singletonCommandFactory.add("CreateDevice", new CreateDeviceCommand());
		singletonCommandFactory.add("UpdateDevice", new UpdateDeviceCommand());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = HttpRequestToJSON.inputToJson(request);
		String key = (String) json.get("request-type");
		sendToThreadpool(key, json);
	}
	
	private void sendToThreadpool(String Key, JSONObject json) {
		Callable<JSONObject> callable = () -> { 
	        return singletonCommandFactory.execute(Key, json);
	    };
	    
	    threadPool.submit(callable);
	}
}
