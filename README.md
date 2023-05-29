# Generic IoT Infrastructure

The IoT Gateway Server is a Java-based server application designed to handle incoming requests and manage data related to IoT devices and companies. It provides a set of commands to create, update, and retrieve information from a MongoDB database.

## Features

- Create a company: Allows creating a new company in the MongoDB database.
- Create a product: Creates a new product collection within a company in the MongoDB database.
- Create a device: Adds a new device document to a product collection in the MongoDB database.
- Update device temperature: Updates the temperature value of a device in the MongoDB database.
- Forward data to the gateway: Sends data to a gateway server for further processing.

## Technologies Used

- Java: The programming language used for implementing the server application.
- MongoDB: The NoSQL database used for storing company, product, and device information.
- Servlets: Used to handle incoming HTTP requests and execute corresponding commands.
- JSON: The data interchange format used for sending and receiving data.
- Maven: The build tool used for managing dependencies and building the project.
- Tomcat: The web server used for hosting the servlets.

## Prerequisites

Before running the IoT Gateway Server, make sure you have the following:

- Java Development Kit (JDK) installed.
- Apache Tomcat or a compatible servlet container installed.
- MongoDB server running locally or accessible from the server.

## Installation and Setup

1. Clone the repository: `git clone https://github.com/your-username/generic-iot-infrastructure.git`
2. Build the project using Maven: `mvn clean install`
3. Deploy the generated WAR file to your Tomcat server.
4. Configure the MongoDB connection URL in the server application code.
5. Start the Tomcat server.

## Usage

- Access the web interface: Open a web browser and navigate to the URL of the deployed application.
- Create a company: Enter the company name and click "Submit" in the corresponding form.
- Create a product: Enter the product name and click "Submit" in the corresponding form.
- Create a device: Enter the company name, product name, and device details in the corresponding form.
- Update device temperature: Enter the company name, product name, device serial number, and temperature in the corresponding form.
- Forward data to the gateway: Use the `ForwardToGateway` class in the `iot.util` package to send JSON data to the gateway server.
