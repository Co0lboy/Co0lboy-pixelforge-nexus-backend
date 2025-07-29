<div align="center">
  <h1>PixelForge Nexus - Backend</h1>
  <p>
    <b>The secure, robust, and scalable backend service for the PixelForge Nexus application.</b>
    <br />
    Built with Java Spring Boot, this service provides a complete RESTful API for managing users, projects, and more.
  </p>
</div>

<hr>

## ✨ Features

* 🔐 **Secure Authentication:** Uses Spring Security with JSON Web Tokens (JWT) for secure, stateless authentication.
* 🛡️ **Role-Based Access Control (RBAC):** Granular permissions for different user roles (`ADMIN`, `PROJECT_LEAD`, `DEVELOPER`).
* 🎮 **Project Management:** Endpoints for creating, viewing, and managing game projects.
* 👥 **Team Assignment:** Allows project leads to assign developers to specific projects.
* 📄 **Document Management:** Supports file uploads associated with projects.

## 🚀 Technology Stack

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring" alt="Spring Boot 3.x">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/JWT-Authentication-purple?style=for-the-badge&logo=jsonwebtokens" alt="JWT">
  <img src="https://img.shields.io/badge/Maven-3.9-red?style=for-the-badge&logo=apachemaven" alt="Maven">
</p>

---

## 🛠️ Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* **Java JDK 21:** Make sure you have Java 21 installed.
* **Maven:** The project uses Maven for building. It's typically bundled with IDEs like IntelliJ IDEA.
* **MySQL:** A running instance of MySQL server.
* **DBeaver (or other DB client):** For managing the database.
* **IntelliJ IDEA:** The recommended IDE for this project.

### Local Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/pixelforge-nexus-backend.git](https://github.com/your-username/pixelforge-nexus-backend.git)
    cd pixelforge-nexus-backend
    ```

2.  **Create the Database:**
    * Open DBeaver or your preferred MySQL client.
    * Create a new database named `pixelforge_db`.

3.  **Configure the Application:**
    * Navigate to `src/main/resources/`.
    * Open the `application.properties` file.
    * Update the following properties to match your local MySQL setup:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/pixelforge_db
        spring.datasource.username=your_mysql_username
        spring.datasource.password=your_mysql_password
        ```
    * **Important:** Set a strong, unique secret for JWT signing:
        ```properties
        app.jwt.secret=your-super-strong-and-long-secret-key
        ```

4.  **Run the Application:**
    * Open the project in IntelliJ IDEA. The IDE will automatically handle the Maven dependencies.
    * Find the `NexusApplication.java` file in `src/main/java/com/pixelforge/nexus/`.
    * Right-click on the file and select "Run 'NexusApplication'".
    * The server will start on `http://localhost:8080`.

---

## 🔌 API Endpoints

The application exposes the following RESTful API endpoints.

### Authentication (`/api/auth`)

| Method | Endpoint    | Description                             | Access       |
| :----- | :---------- | :-------------------------------------- | :----------- |
| `POST` | `/login`    | Authenticates a user and returns a JWT. | Public       |
| `POST` | `/register` | Registers a new user.                   | `ADMIN` only |

### Projects (`/api/projects`)

| Method | Endpoint         | Description                             | Access        |
| :----- | :--------------- | :-------------------------------------- | :------------ |
| `POST` | `/`              | Creates a new project.                  | `ADMIN`       |
| `GET`  | `/`              | Retrieves a list of all projects.       | Authenticated |
| `GET`  | `/{id}`          | Retrieves details for a single project. | Authenticated |
| `PUT`  | `/{id}/complete` | Marks a project as completed.           | `ADMIN`       |

### Team Management (`/api/projects/{projectId}/team`)

| Method | Endpoint             | Description                              | Access         |
| :----- | :------------------- | :--------------------------------------- | :------------- |
| `POST` | `/assign/{username}` | Assigns a user to the specified project. | `PROJECT_LEAD` |

### Document Management (`/api/projects/{projectId}/documents`)

| Method | Endpoint | Description                                | Access                  |
| :----- | :------- | :----------------------------------------- | :---------------------- |
| `POST` | `/`      | Uploads a new document for the project.    | `ADMIN`, `PROJECT_LEAD` |
| `GET`  | `/`      | Retrieves a list of documents for the project. | Authenticated           |
