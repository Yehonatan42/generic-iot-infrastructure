package iot.companyservlet;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import iot.util.ForwardToGateway;
import iot.util.HttpRequestToJSON;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registerProduct")
public class RegisterProduct extends HttpServlet {

	private static final long serialVersionUID = -6880014437671273900L;
	private final String url = "jdbc:mysql://localhost:3306/companies";
	private final String username = "root";
	private final String password = "10071990";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {	
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		JSONObject json = HttpRequestToJSON.inputToJson(request);

		String receivedCompany = (String) json.get("company");
		String receivedProduct = (String) json.get("product");
		
		String query = "INSERT INTO " + receivedCompany + " VALUES ('" + receivedProduct + "');";
	    Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ForwardToGateway.forwardToGateway(json);
	}
}
