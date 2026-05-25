# RMS - Restaurant Management System (Backend)

Welcome to the backend repository for the **Restaurant Management System (RMS)**. This is a Spring Boot application designed to handle the core operations of a restaurant, from menu and table management to user authentication, role-based access control, and order/invoice generation.

## 🚀 Project Overview

The RMS backend provides a robust REST API to support a restaurant's day-to-day activities. It features a comprehensive security layer using JWT (JSON Web Tokens) for role and privilege-based access control, allowing precise authorization for different staff members (e.g., Admin, Manager, Waiter).

### Key Features
- **Authentication & Authorization**: Secure login system with JWT. Dynamic role and privilege management (`Role`, `Privilege`, `RolePrivilege`, `RestaurantPrivilege`).
- **Restaurant Profile**: Manage multiple restaurant details and configurations.
- **Menu Management**: Categorized food items with main categories and sub-categories.
- **Table Management**: Manage seating arrangements and track table status.
- **Order Management**: Create and track orders, mapping order items to specific tables.
- **Billing & Invoicing**: Calculate taxes (`Tax`, `OrderTax`), generate order summaries, and create final invoices.
- **Azure Blob Storage Integration**: Handle image uploads (e.g., food images, restaurant logos) directly to Azure Blob Storage.
- **Email Notifications**: Integrated email service for sending notifications and alerts.

## 🛠️ Technology Stack

- **Java Version:** 21
- **Framework:** Spring Boot (Data JPA, WebMVC, Security, Validation, Mail)
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JJWT)
- **Mapping:** MapStruct
- **Boilerplate Reduction:** Lombok
- **Cloud Storage:** Azure Storage Blob
- **Build Tool:** Maven

## 📁 Project Structure

```
src/main/java/com/restaurent/RMS
├── config/              # Configuration classes (Security, Azure, etc.)
├── controllers/         # REST API Controllers (Endpoints)
├── dtos/                # Data Transfer Objects for API requests/responses
├── entities/            # JPA Database Entities
├── enums/               # Enumerations used across the project
├── exceptionHandlers/   # Global Exception Handling logic
├── mappers/             # MapStruct interfaces for DTO <-> Entity conversion
├── repositories/        # Spring Data JPA Repositories
├── security/            # JWT Filters and Security Services
├── services/            # Business logic implementation
├── specification/       # JPA Specifications for advanced querying
└── utils/               # Utility and Helper classes
```

## ⚙️ Prerequisites

Before you begin, ensure you have the following installed on your local machine:
- **Java Development Kit (JDK) 21** or higher.
- **Maven** (Optional, as the project includes the Maven Wrapper `mvnw`).
- **MySQL Server** (Running locally or accessible remotely).
- **Azure Blob Storage Account** (For image uploads).

## 🔧 Configuration

The application uses profiles for configuration. The default active profile is `dev`, which looks for the `application-dev.properties` file in `src/main/resources/`.

Update `src/main/resources/application-dev.properties` with your local database credentials and JWT/Azure secrets:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/rms
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JWT Configuration
jwt.secret=YOUR_SUPER_SECRET_JWT_KEY
jwt.expiration=86400000 # 24 hours

# Azure Storage Configuration
azure.storage.connection-string=YOUR_AZURE_CONNECTION_STRING
azure.storage.container-name=restaurant-images
```
*Note: Make sure to create a database named `rms` in your MySQL server before starting the application.*

## ▶️ Running the Application

You can run the application using the included Maven wrapper.

1. **Open a terminal** and navigate to the project root directory (`Restaurant-Back-End`).
2. **Execute the following command**:
   ```bash
   # On Windows
   ./mvnw.cmd spring-boot:run
   
   # On macOS/Linux
   ./mvnw spring-boot:run
   ```
3. The server will start on port `8089` (as defined in the dev properties).

## 📄 API Documentation

If Swagger/SpringDoc is configured, once the application is running, you can access the interactive API documentation at:
- **Swagger UI:** `http://localhost:8089/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8089/api-docs`
