# TalkFlow API 🚀

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Blue.svg)
![Keycloak](https://img.shields.io/badge/Keycloak-Secured-blueviolet.svg)

## 📖 Overview
**TalkFlow** is a modern, real-time chat and messaging backend API built with Java and Spring Boot. It provides a robust architecture for handling user synchronization, secure communication via WebSockets, and message state management. The application is secured using an OAuth2 Resource Server configuration, specifically tailored for **Keycloak** integration.

## ✨ Key Features
* **Real-time Messaging:** Powered by Spring WebSockets for instant chat capabilities.
* **Robust Security:** OAuth2/JWT authentication using Keycloak (`KeycloakJwtAuthenticationConverter`).
* **User Synchronization:** Interceptors (`UserSynchronizerFilter`) to map and sync authenticated users seamlessly into the local database.
* **Database Management:** PostgreSQL integration with automated schema migrations using **Flyway**.
* **Auditing:** Built-in auditing entities (`BaseAuditingEntity`) to track creation and modification records.
* **Environment Configuration:** Easy environment variable management using `spring-dotenv`.

## 🛠️ Technology Stack
* **Core:** Java 17, Spring Boot (Web, Data JPA, Validation)
* **Real-time Communication:** Spring Boot Starter WebSocket
* **Security:** Spring Security, OAuth2 Resource Server (Keycloak)
* **Database:** PostgreSQL
* **Migrations:** Flyway
* **Utilities:** Lombok, Dotenv

## 📂 Project Architecture
```text
api/src/main/java/com/yavuzahmet/talkflow/
├── chat/          # Chat related constants and core logic
├── common/        # Shared components (e.g., BaseAuditingEntity)
├── interceptor/   # Request filters and user synchronization logic
├── message/       # Message entity, states, types, and constants
├── security/      # Keycloak JWT converters and Spring Security config
└── user/          # User entity, repository, mapper, and constants