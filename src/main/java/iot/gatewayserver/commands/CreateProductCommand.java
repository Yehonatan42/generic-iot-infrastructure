package iot.gatewayserver.commands;

import java.util.function.Function;

import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class CreateProductCommand implements Function<JSONObject, JSONObject>{

	@Override
	public JSONObject apply(JSONObject json) {
		String receivedCompany = (String) json.get("company");
		String receivedProduct = (String) json.get("product");
		
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase(receivedCompany);
		database.createCollection(receivedProduct);
		
		return null;
	}
}
