# TalkFlow API 🚀

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![Keycloak](https://img.shields.io/badge/Keycloak-26.0.0-blueviolet.svg)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)

## 📖 Overview
**TalkFlow** is a modern, real-time chat and messaging backend API built with Java and Spring Boot 3.5.14. It provides a robust architecture for handling user synchronization, secure communication via WebSockets, and message state management with file upload capabilities. The application is secured using OAuth2 Resource Server configuration with Keycloak integration.

## ✨ Key Features
* **Real-time Messaging:** Powered by Spring WebSocket for instant bi-directional chat communication.
* **Secure Authentication:** OAuth2/JWT authentication using Keycloak with custom JWT converter (`KeycloakJwtAuthenticationConverter`).
* **User Synchronization:** Request filters (`UserSynchronizerFilter` & `UserSynchronizer`) for seamless user mapping and database sync.
* **Message Management:** Full lifecycle support with message states (SENT, DELIVERED, SEEN) and multiple message types.
* **File Upload & Media:** Support for media file uploads (up to 500MB) with configurable storage path.
* **Auditing:** Base auditing entity (`BaseAuditingEntity`) to track creation and modification timestamps automatically.
* **Database Migrations:** Schema management using Flyway for reliable database versioning.
* **Environment Configuration:** Easy .env file management using `spring-dotenv` library.
* **Docker Support:** Docker Compose setup for quick local development environment (PostgreSQL + Keycloak).

## 🛠️ Technology Stack
* **Backend Framework:** Java 17, Spring Boot 3.5.14
* **Core Spring Modules:** Spring Web, Spring Data JPA, Spring Validation, Spring Security
* **Real-time Communication:** Spring WebSocket
* **Authentication & Authorization:** Spring OAuth2 Resource Server, Keycloak 26.0.0
* **Database:** PostgreSQL 15
* **Database Migrations:** Flyway with PostgreSQL dialect
* **Code Generation:** Lombok (automatic getters, setters, constructors)
* **Configuration Management:** spring-dotenv 4.0.0
* **Containerization:** Docker & Docker Compose

## 📂 Project Architecture
```text
api/
├── src/main/java/com/yavuzahmet/talkflow/
│   ├── chat/                # Chat management (entities, services, controllers, mappers)
│   │   ├── Chat.java                    # Chat entity
│   │   ├── ChatRepository.java          # Database access layer
│   │   ├── ChatService.java             # Business logic
│   │   ├── ChatController.java          # REST endpoints
│   │   ├── ChatMapper.java              # DTO mapping
│   │   ├── ChatResponse.java            # Response DTO
│   │   └── ChatConstants.java           # Constants
│   ├── message/             # Message handling (entities, states, file management)
│   │   ├── Message.java                 # Message entity
│   │   ├── MessageRequest.java          # Request DTO
│   │   ├── MessageResponse.java         # Response DTO
│   │   ├── MessageState.java            # Enum (SENT, DELIVERED, SEEN)
│   │   ├── MessageType.java             # Enum (TEXT, MEDIA, etc.)
│   │   ├── MessageService.java          # Business logic
│   │   ├── MessageController.java       # REST endpoints
│   │   ├── MessageMapper.java           # DTO mapping
│   │   ├── MessageRepository.java       # Database access layer
│   │   ├── MessageConstants.java        # Constants
│   │   └── FileService.java             # File upload handling
│   ├── user/                # User management
│   │   ├── User.java                    # User entity
│   │   ├── UserRepository.java          # Database access layer
│   │   ├── UserMapper.java              # DTO mapping
│   │   └── UserConstants.java           # Constants
│   ├── security/            # OAuth2 & Keycloak configuration
│   │   ├── SecurityConfig.java          # Spring Security configuration
│   │   └── KeycloakJwtAuthenticationConverter.java  # JWT token conversion
│   ├── interceptor/         # Request/Response filtering
│   │   ├── UserSynchronizerFilter.java  # Filter for user sync
│   │   └── UserSynchronizer.java        # User sync logic
│   ├── common/              # Shared utilities
│   │   ├── BaseAuditingEntity.java      # Base class with audit fields
│   │   └── StringResponse.java          # Generic response wrapper
│   ├── file/                # File utilities
│   │   ├── FileService.java             # File operations
│   │   └── FileUtils.java               # Utility methods
│   └── TalkflowApiApplication.java      # Main application entry point
├── src/main/resources/
│   └── application.yml                  # Application configuration
├── pom.xml                              # Maven dependencies
└── .mvn/                                # Maven wrapper configuration

docker-compose.yml          # Docker setup (PostgreSQL + Keycloak)
.env-example                # Environment variables template
```

## 🚀 Getting Started

### Prerequisites
* Java 17 or higher
* Maven 3.6+
* Docker & Docker Compose (for local development)
* PostgreSQL 15 (or use Docker Compose)
* Keycloak 26.0.0 (or use Docker Compose)

### Environment Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd TalkFlow
   ```

2. **Create `.env` file from template:**
   ```bash
   cp .env-example .env
   ```

3. **Configure environment variables** (`.env`):
   ```env
   # Database Configuration
   DB_USER=talkflow_user
   DB_PASSWORD=your_secure_password
   DB_NAME=talkflow_db
   
   # Keycloak Configuration
   KC_ADMIN=admin
   KC_ADMIN_PASSWORD=admin_password
   ```

4. **Start Docker Compose services:**
   ```bash
   docker-compose up -d
   ```
   This starts:
   * **PostgreSQL 15** on `localhost:5432`
   * **Keycloak 26.0.0** on `localhost:9090`

5. **Build and run the application:**
   ```bash
   cd api
   mvn clean install
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`

## 📡 API Endpoints

### Chat Management (`/api/v1/chats`)
* **POST** `/api/v1/chats` - Create a new chat between two users
  ```bash
  POST /api/v1/chats?sender-id=USER1&receiver-id=USER2
  Response: { "response": "CHAT_ID" }
  ```
* **GET** `/api/v1/chats` - Get all chats for the authenticated user
  ```bash
  GET /api/v1/chats
  Authorization: Bearer <JWT_TOKEN>
  ```

### Message Management (`/api/v1/messages`)
* **POST** `/api/v1/messages` - Send a text message
  ```bash
  POST /api/v1/messages
  Content-Type: application/json
  {
    "chat-id": "CHAT_ID",
    "content": "Message text",
    "type": "TEXT"
  }
  ```
* **POST** `/api/v1/messages/upload-media` - Upload media file
  ```bash
  POST /api/v1/messages/upload-media?chat-id=CHAT_ID
  Content-Type: multipart/form-data
  file: <binary_file> (max 500MB)
  Authorization: Bearer <JWT_TOKEN>
  ```
* **PATCH** `/api/v1/messages` - Mark messages as seen
  ```bash
  PATCH /api/v1/messages?chat-id=CHAT_ID
  Authorization: Bearer <JWT_TOKEN>
  ```
* **GET** `/api/v1/messages/chat/{chat-id}` - Retrieve chat messages
  ```bash
  GET /api/v1/messages/chat/CHAT_ID
  ```

## 🔐 Security Features

### Authentication & Authorization
* **OAuth2 Resource Server** with JWT tokens from Keycloak
* **JWT Token Validation:** Tokens are validated against Keycloak's issuer-uri (`http://localhost:9090/realms/talkflow`)
* **Custom JWT Converter:** `KeycloakJwtAuthenticationConverter` extracts claims and roles from tokens
* **Automatic User Sync:** `UserSynchronizerFilter` intercepts requests and ensures user data is synced to the database

### Message Security
* All message operations require authentication
* User isolation: Users can only access their own chats and messages
* File upload size limit: 500MB per file

## 📋 Configuration

### Application Configuration (`api/src/main/resources/application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update  # Auto-update schema (use 'validate' in production)
    show-sql: false
    open-in-view: false
    database: postgresql
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9090/realms/talkflow
  servlet:
    multipart:
      max-file-size: 500MB

application:
  file:
    uploads:
      media-output-path: ./upload  # Media storage path
```

### Database Migrations
* **Flyway** is configured but currently **disabled** (`flyway.enabled: false`)
* Schema management is handled by Hibernate (`ddl-auto: update`)
* To enable Flyway migrations: Set `spring.flyway.enabled: true` and add migration files to `db/migration/`

## 🧪 Testing

Run unit tests:
```bash
cd api
mvn test
```

## 📝 Database Schema

### User Table
- `id` - UUID primary key
- `username` - Unique username from Keycloak
- `email` - User email
- `created_at` - Creation timestamp (audited)
- `updated_at` - Last update timestamp (audited)

### Chat Table
- `id` - UUID primary key
- `sender_id` - Foreign key to User
- `receiver_id` - Foreign key to User
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

### Message Table
- `id` - UUID primary key
- `chat_id` - Foreign key to Chat
- `sender_id` - Foreign key to User
- `content` - Message text
- `type` - Message type (TEXT, MEDIA)
- `state` - Message state (SENT, DELIVERED, SEEN)
- `file_path` - Path to uploaded file (if media)
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

## 📦 Dependencies Management

### Maven
All dependencies are specified in `api/pom.xml`. Key dependencies:
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-oauth2-resource-server` - OAuth2 security
- `spring-boot-starter-websocket` - WebSocket support
- `org.postgresql:postgresql` - PostgreSQL driver
- `org.flywaydb:flyway-*` - Database migrations
- `org.projectlombok:lombok` - Code generation
- `me.paulschwarz:spring-dotenv` - .env support

### Update Dependencies
```bash
mvn dependency:update-check
mvn dependency:tree
```
## 👨‍💻 Contributors
- Ahmet YAVUZ