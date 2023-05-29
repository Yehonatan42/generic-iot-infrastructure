package iot.gatewayserver.commands;

import java.util.function.Function;

import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class CreateCompanyCommand implements Function<JSONObject, JSONObject>{

	@Override
	public JSONObject apply(JSONObject json) {
		String receivedCompany = (String) json.get("company");
		
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		mongoClient.getDatabase(receivedCompany);
		
		return null;
	}
}
