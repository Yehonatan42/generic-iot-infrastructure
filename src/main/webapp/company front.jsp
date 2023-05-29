<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONObject" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Company Server</title>
</head>
<body>
    <form method="post" action="CreateCompany">
        <label for="inputText">Enter company name:</label>
        <input type="text" id="company" name="inputText">
        <input type="submit" value="Submit">
    </form>
    <form method="post" action="CreateProduct">
        <label for="inputText">Enter product name:</label>
        <input type="text" id="product" name="inputText">
        <input type="submit" value="Submit">
    </form>
    <%
        String inputCompany = request.getParameter("company");
        if (inputCompany != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("inputText", inputCompany);
            request.setAttribute("jsonObject", jsonObject);
        }
    %>
    <%
        String inputProduct = request.getParameter("product");
        if (inputProduct != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("inputText", inputProduct);
            request.setAttribute("jsonObject", jsonObject);
        }
    %>
</body>
</html>