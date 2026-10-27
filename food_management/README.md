# 🍽️ Food Management System

A backend application built using **Java, Spring Boot, Microservices Architecture, and Apache Kafka** to manage food items, categories, customer orders, and inventory.

The project demonstrates REST API development, secure authentication, inter-service communication, asynchronous messaging, and order processing in a distributed application.

## 🚀 Project Overview

The Food Management System is designed to organize food data and handle order processing through backend services. It uses JWT-based authentication and role-based authorization to secure API access.

The application uses Apache Kafka for asynchronous, event-driven communication between services, helping decouple service interactions.

## 🛠️ Technologies Used

- **Language:** Java
- **Framework:** Spring Boot
- **Architecture:** Microservices
- **Messaging:** Apache Kafka
- **Security:** Spring Security, JWT
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **API Testing:** Thunder Client
- **Build Tool:** Maven
- **Version Control:** Git, GitHub

## ✨ Features

### 1. User Authentication and Authorization
- User registration and login.
- JWT-based authentication.
- Role-based access control using Spring Security.
- Protection of administrative endpoints.

### 2. Food Category Management
- Create and manage food categories.
- Organize food items through categories and subcategories.
- Maintain relationships between categories and subcategories.

### 3. Subcategory Management
- Manage subcategories associated with food categories.
- Organize food items into a structured hierarchy.

### 4. Food Item Management
- Create, retrieve, update, and delete food items.
- Associate food items with their respective subcategories.
- Maintain food item stock information.

### 5. Order Management
- Place and manage customer orders.
- Maintain order details and associated order items.
- Calculate the total order amount.
- Update food stock during order processing.

### 6. Microservices Architecture
- Organize application functionality into independent services.
- Enable communication between services.
- Separate responsibilities to improve modularity and maintainability.

### 7. Apache Kafka Integration
- Use Kafka for asynchronous messaging between services.
- Support event-driven communication.
- Reduce direct dependencies between services where Kafka-based messaging is used.

### 8. Validation and Exception Handling
- Validate incoming requests.
- Handle invalid requests and missing resources.
- Provide structured error responses for application failures.

## 🏗️ Architecture Overview

The application combines microservices, REST APIs, database persistence, and asynchronous messaging.

```text
                 Client
                   |
                   v
             REST API Layer
                   |
                   v
          Food Management Services
             |            |
             |            |
             v            v
          MySQL       Apache Kafka
                          |
                          v
                  Kafka Consumers
                  / Other Services
```

*Note: This is a conceptual overview. Adapt the diagram to your actual service names and Kafka producer/consumer flow.*

## 🗄️ Main Entities

| Entity | Purpose |
|---|---|
| User | Stores user information and roles |
| FoodCategory | Represents food categories |
| SubCategory | Represents food subcategories |
| FoodItem | Stores food item information and stock |
| OrderEntity | Stores order details |
| OrderItem | Represents individual items within an order |

The entities use JPA mappings to represent relationships between categories, food items, users, and orders.

## 🔐 Authentication Flow

1. A user registers with the application.
2. The user logs in using their credentials.
3. The application authenticates the credentials.
4. A JWT token is generated.
5. The client includes the token in subsequent protected API requests.
6. The JWT filter validates the token.
7. Spring Security checks the user's authorities before allowing access.

## ⚙️ Prerequisites

Install the following tools before running the project:

- Java JDK compatible with the project
- Maven
- MySQL Server
- Apache Kafka and its required runtime components
- Git
- IntelliJ IDEA or Eclipse
- Thunder Client or another REST API testing tool

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd <your-project-directory>
```

Replace the placeholders with your actual GitHub repository URL and project directory.

### 2. Configure MySQL

Create the database used by your application. For example:

```sql
CREATE DATABASE food_management;
```

Use the actual database name configured in your project.

### 3. Configure application properties

Update the relevant `application.properties` or `application.yml` files with your database connection and other required settings.

Example MySQL configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_management
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Configure Kafka bootstrap servers and any required service-specific properties according to your implementation.

**Security note:** Keep database credentials, JWT secrets, and other sensitive values out of your public repository.

### 4. Start Kafka

Start Kafka and its required runtime components using the setup appropriate for your environment. Ensure the configured Kafka broker is reachable by the application services.

### 5. Build the application

From each Spring Boot service directory, if the services have separate Maven projects, run:

```bash
mvn clean install
```

### 6. Start the services

Run the required Spring Boot services using your IDE or Maven:

```bash
mvn spring-boot:run
```

Repeat for each service that has its own application.

### 7. Test the APIs

Use Thunder Client to test the available endpoints for:

- Registration and login
- JWT-protected requests
- Food categories and subcategories
- Food item management
- Order creation and retrieval
- Stock updates
- Kafka-driven interactions, where applicable

*Exact service names, ports, endpoints, and Kafka topic names depend on the project configuration.*

## 🧪 Testing Status

Backend APIs have been tested using Thunder Client for the implemented functionality.

Testing areas include:

- Authentication and authorization
- Food category and subcategory operations
- Food item management
- Order processing
- Stock management
- Request validation and exception handling

## 📌 Planned Enhancements

The following tasks remain planned for later completion:

- [ ] Swagger / OpenAPI API documentation
- [ ] Docker configuration
- [ ] Docker Compose setup
- [ ] Containerized application testing
- [ ] Additional automated unit and integration tests, where needed

These items are planned enhancements and are not claimed as completed features.

## 📚 Key Learning Outcomes

- Building REST APIs with Spring Boot.
- Designing backend services using microservices architecture.
- Implementing JWT authentication and role-based authorization.
- Managing relational data with Spring Data JPA and Hibernate.
- Integrating Apache Kafka for asynchronous communication.
- Implementing order processing and stock management.
- Validating requests and handling application exceptions.
- Testing APIs using Thunder Client.

## 👨‍💻 Author

**Your Name**

Java | Spring Boot | Microservices | Apache Kafka | Spring Security | MySQL

## 📄 License

This project was developed for learning and educational purposes.
