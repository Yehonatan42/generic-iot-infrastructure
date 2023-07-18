# Generic IoT Infrastructure

The Generic IoT Infrastructure is a versatile and extensible Java-based server application that serves as a fundamental platform for managing and processing data related to IoT (Internet of Things) devices and companies. It is designed to facilitate the collection, analysis, and management of IoT data by providing a robust set of features and a scalable architecture. The application adopts a comprehensive and object-oriented approach, leveraging modern technologies and design principles to ensure flexibility, maintainability, and efficiency.

## Features

- Create a company: Allows creating a new company in the MongoDB database.
- Create a product: Creates a new product collection within a company in the MongoDB database.
- Create a device: Adds a new device document to a product collection in the MongoDB database.
- Update device: Updates a value of a device in the MongoDB database.

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
6. Send http requests using Postman or a similar API testing tool.
