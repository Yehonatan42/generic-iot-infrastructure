package iot.gatewayserver.commands;

import java.util.function.Function;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CreateDeviceCommand implements Function<JSONObject, JSONObject>{

	@Override
	public JSONObject apply(JSONObject json) {
		String receivedCompany = (String) json.get("company");
		String receivedProduct = (String) json.get("product");
		String receivedSerialNumber = (String) json.get("serial-number");
		
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongoClient.getDatabase(receivedCompany);
		
		MongoCollection<Document> collection = database.getCollection(receivedProduct);
		Document document = new Document();
		document.put("Serial number", receivedSerialNumber);
		collection.insertOne(document);
		
		return null;
	}
}



