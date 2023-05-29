package iot.companyservlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;

import iot.util.ForwardToGateway;
import iot.util.HttpRequestToJSON;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registerCompany")
public class RegisterCompany extends HttpServlet {

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
		String query = "INSERT INTO company_list VALUES ('" + receivedCompany + "');";
	    Statement statement1;
		try {
			statement1 = connection.createStatement();
			statement1.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		query = "CREATE TABLE " + receivedCompany + " (product varchar(30));";
		Statement statement2;
		try {
			statement2 = connection.createStatement();
			statement2.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Entry already exists");
		}
        
        ForwardToGateway.forwardToGateway(json);
	}
	
}
