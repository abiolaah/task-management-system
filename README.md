# Task Management System

## Description
The **Task Management System** is a comprehensive Java application that demonstrates modern enterprise development practices. Built with JavaFX, JDBC, and WebSocket technology, this project provides real-time task management capabilities with secure authentication using OAuth 2.0 and JWT tokens. The system includes automated database migrations with Liquibase and a complete CI/CD pipeline using Jenkins.

- [Task Management System](#task-management-system)
   * [Description](#description)
   * [Purpose](#purpose)
   * [Core Functionality](#core-functionality)
   * [Technologies Used](#technologies-used)
   * [Project Setup](#project-setup)
      + [Prerequisites](#prerequisites)
      + [Steps](#steps)
         - [1. Clone the Repository](#1-clone-the-repository)
         - [2. Set Up Database](#2-set-up-database)
            * [For macOS M1/M2/M3 (Apple Silicon):](#for-macos-m1m2m3-apple-silicon)
            * [For Intel Macs or Windows:](#for-intel-macs-or-windows)
         - [3. Configure Database Connection](#3-configure-database-connection)
         - [4. Install Dependencies](#4-install-dependencies)
         - [5. Run Database Migrations](#5-run-database-migrations)
         - [6. Run the Application](#6-run-the-application)
         - [7. Run Tests](#7-run-tests)
   * [Project Structure](#project-structure)
   * [Architecture Overview](#architecture-overview)
      + [Layer Architecture](#layer-architecture)
      + [Security Flow](#security-flow)
      + [Real-time Update Flow](#real-time-update-flow)
   * [Features](#features)
      + [1. User Authentication](#1-user-authentication)
      + [2. Task Management](#2-task-management)
      + [3. Real-time Collaboration](#3-real-time-collaboration)
      + [4. Database Management](#4-database-management)
      + [5. CI/CD Pipeline](#5-cicd-pipeline)
   * [Test Results](#test-results)
      + [Latest Test Execution](#latest-test-execution)
      + [Test Coverage](#test-coverage)
         - [TaskServiceTest](#taskservicetest)
         - [AuthServiceTest](#authservicetest)
      + [Viewing Test Reports](#viewing-test-reports)
         - [Command Line Execution](#command-line-execution)
         - [IDE Execution (IntelliJ IDEA)](#ide-execution-intellij-idea)
   * [CI/CD Pipeline](#cicd-pipeline)
      + [Jenkins Pipeline Stages](#jenkins-pipeline-stages)
      + [Pipeline Configuration](#pipeline-configuration)
      + [Setting Up Jenkins](#setting-up-jenkins)
   * [Database Schema](#database-schema)
      + [Users Table](#users-table)
      + [Tasks Table](#tasks-table)
   * [API Documentation](#api-documentation)
      + [Authentication Endpoints](#authentication-endpoints)
         - [Login](#login)
         - [Validate Token](#validate-token)
      + [Task Management Endpoints](#task-management-endpoints)
         - [Get All Tasks](#get-all-tasks)
         - [Create Task](#create-task)
         - [Update Task](#update-task)
         - [Delete Task](#delete-task)
      + [WebSocket Endpoints](#websocket-endpoints)
         - [Connect](#connect)
         - [Message Format](#message-format)
   * [Usage Examples](#usage-examples)
      + [Creating a Task via UI](#creating-a-task-via-ui)
      + [Creating a Task Programmatically](#creating-a-task-programmatically)
      + [Validating JWT Token](#validating-jwt-token)
   * [Troubleshooting](#troubleshooting)
      + [Database Connection Issues](#database-connection-issues)
      + [Port Already in Use](#port-already-in-use)
      + [JavaFX Not Loading](#javafx-not-loading)
      + [Tests Failing](#tests-failing)
   * [Performance Considerations](#performance-considerations)
      + [Database Optimization](#database-optimization)
      + [WebSocket Scalability](#websocket-scalability)
      + [UI Responsiveness](#ui-responsiveness)
   * [Security Best Practices](#security-best-practices)
   * [Future Enhancements](#future-enhancements)
   * [Contributing](#contributing)
      + [Coding Standards](#coding-standards)
   * [License](#license)
   * [Contact](#contact)

## Purpose
The primary purpose of this project is to demonstrate:
- Full-stack Java application development
- Real-time collaboration features
- Secure authentication and authorization
- Database management and migrations
- Automated testing and deployment
- Modern enterprise development practices

## Core Functionality
- User authentication with JWT token-based security
- Real-time task updates via WebSocket
- CRUD operations for task management
- Secure data persistence with MSSQL
- Automated database schema management with Liquibase
- Continuous Integration/Continuous Deployment (CI/CD) pipeline
- Comprehensive unit testing with JUnit 5

## Technologies Used
| Technology | Purpose |
|------------|---------|
| Java 11+ | Core programming language |
| JavaFX 17 | Desktop user interface framework |
| JDBC | Database connectivity |
| MSSQL Server | Relational database (Azure SQL Edge for M1/M2/M3 Macs) |
| Liquibase | Database migration and version control |
| WebSocket | Real-time bidirectional communication |
| OAuth 2.0 | Authentication protocol |
| JWT (JSON Web Token) | Secure token-based authentication |
| JUnit 5 | Unit testing framework |
| Mockito | Mocking framework for tests |
| Maven | Dependency management and build tool |
| Jenkins | Continuous Integration/Continuous Deployment |
| Docker | Containerized database deployment |

## Project Setup
To set up and run the project locally, follow these steps:

### Prerequisites
Ensure the following are installed:
- **Java Development Kit (JDK)** 11 or higher
- **Maven** (Apache Maven 3.6.3 or later)
- **Docker Desktop** (for running MSSQL database)
- **Git** (for version control)
- **Jenkins** (optional, for CI/CD pipeline)

### Steps

#### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/task-management-system.git
cd task-management-system
```

#### 2. Set Up Database

##### For macOS M1/M2/M3 (Apple Silicon):
Create `docker-compose.yml` in project root:

```yaml
version: '3.8'

services:
  mssql:
    image: mcr.microsoft.com/azure-sql-edge
    container_name: mssql-server
    hostname: mssql-server
    environment:
      - ACCEPT_EULA=Y
      - MSSQL_SA_PASSWORD=YourStrong@Password123
      - MSSQL_PID=Developer
    ports:
      - "1433:1433"
    volumes:
      - mssql-data:/var/opt/mssql
    restart: unless-stopped

volumes:
  mssql-data:
    driver: local
```

**Start the database:**
```bash
docker-compose up -d
```

**Wait for database to start (30 seconds):**
```bash
sleep 30
```

**Create TaskDB database:**
```bash
docker exec -it mssql-server /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P "YourStrong@Password123" \
  -Q "CREATE DATABASE TaskDB;"
```

##### For Intel Macs or Windows:
Use SQL Server 2022 image instead:
```yaml
image: mcr.microsoft.com/mssql/server:2022-latest
```

#### 3. Configure Database Connection
Update `src/main/java/com/taskapp/config/DatabaseConfig.java` if needed:

```java
private static final String URL = "jdbc:sqlserver://localhost:1433;" +
                                 "databaseName=TaskDB;" +
                                 "encrypt=true;" +
                                 "trustServerCertificate=true";
private static final String USER = "sa";
private static final String PASSWORD = "YourStrong@Password123";
```

#### 4. Install Dependencies
```bash
mvn clean install
```

#### 5. Run Database Migrations
```bash
mvn liquibase:update
```

This creates the necessary tables:
- `users` - User account information
- `tasks` - Task data
- `DATABASECHANGELOG` - Liquibase tracking
- `DATABASECHANGELOGLOCK` - Migration locking

#### 6. Run the Application
```bash
mvn javafx:run
```

#### 7. Run Tests
```bash
mvn test
```

## Project Structure

```text
task-management-system/
│
├── pom.xml                         # Maven configuration
├── Jenkinsfile                     # CI/CD pipeline definition
├── docker-compose.yml              # Database container configuration
├── README.md                       # Project documentation
│
├── src/
│   ├── main/
│   │   ├── java/com/taskapp/
│   │   │   ├── Main.java           # Application entry point
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── DatabaseConfig.java      # Database connection setup
│   │   │   │   └── SecurityConfig.java      # Security configuration
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── TaskController.java      # Task endpoint handlers
│   │   │   │   └── AuthController.java      # Authentication handlers
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── Task.java                # Task entity
│   │   │   │   └── User.java                # User entity
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java      # Database operations
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── TaskService.java         # Business logic for tasks
│   │   │   │   └── AuthService.java         # Authentication logic
│   │   │   │
│   │   │   ├── security/
│   │   │   │   └── JwtUtil.java             # JWT token management
│   │   │   │
│   │   │   ├── websocket/
│   │   │   │   └── TaskWebSocketServer.java # Real-time updates
│   │   │   │
│   │   │   └── ui/
│   │   │       └── TaskUI.java              # JavaFX user interface
│   │   │
│   │   └── resources/
│   │       └── db/changelog/
│   │           └── db.changelog-master.xml  # Database migrations
│   │
│   └── test/
│       └── java/com/taskapp/
│           ├── TaskServiceTest.java         # Task service tests
│           └── AuthServiceTest.java         # Authentication tests
│
└── target/                         # Build output directory
    ├── classes/                    # Compiled classes
    └── surefire-reports/           # Test results
```

## Architecture Overview

### Layer Architecture
```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (JavaFX UI + WebSocket Client)       │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│   (Business Logic + Validation)         │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│      (Database Operations)              │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│         Data Layer                      │
│     (MSSQL Database)                    │
└─────────────────────────────────────────┘
```

### Security Flow
```
User Login → Credentials Validation → JWT Token Generation
     ↓
Protected Request → Token Validation → Access Granted/Denied
```

### Real-time Update Flow
```
User Creates Task → Service Layer → Database
     ↓
WebSocket Broadcast → All Connected Clients
     ↓
UI Auto-Refresh
```

## Features

### 1. User Authentication
- Secure login with username/password validation
- JWT token-based session management
- Password hashing with SHA-256
- 24-hour token expiration
- OAuth 2.0 integration ready

### 2. Task Management
- Create new tasks with title and description
- View all tasks in real-time
- Mark tasks as complete/incomplete
- Delete tasks
- Filter tasks by user
- Task assignment tracking

### 3. Real-time Collaboration
- WebSocket server on port 8080
- Instant updates across all connected clients
- Live task status changes
- Multi-user synchronization

### 4. Database Management
- Automated schema migrations with Liquibase
- Version-controlled database changes
- Rollback capabilities
- Data persistence with MSSQL
- Optimized with indexes

### 5. CI/CD Pipeline
- Automated builds on code commit
- Unit test execution
- Code compilation verification
- Packaging and deployment preparation
- Jenkins integration

## Test Results

### Latest Test Execution
**Statistics:**
- **Total Tests:** 14
- **Success Rate:** 100%
- **Execution Time:** ~2.5s

| Test Class | Tests | Errors | Failures | Skipped | Success Rate | Time |
|------------|-------|--------|----------|---------|--------------|------|
| **Total** | 14 | 0 | 0 | 0 | 100% | 2.5 s |
| TaskServiceTest | 8 | 0 | 0 | 0 | 100% | 1.8 s |
| AuthServiceTest | 6 | 0 | 0 | 0 | 100% | 0.7 s |

### Test Coverage

#### TaskServiceTest
- ✅ `testGetAllTasks()` - Verify retrieval of all tasks
- ✅ `testCreateTask()` - Validate task creation
- ✅ `testUpdateTask()` - Confirm task updates
- ✅ `testDeleteTask()` - Test task deletion
- ✅ `testCreateTaskWithNullTitle()` - Error handling for null titles
- ✅ Repository interaction verification with Mockito
- ✅ Exception handling validation
- ✅ Data integrity checks

#### AuthServiceTest
- ✅ `testSuccessfulLogin()` - Valid credential authentication
- ✅ `testFailedLoginWithShortPassword()` - Password validation
- ✅ `testFailedLoginWithEmptyUsername()` - Username validation
- ✅ `testValidateValidToken()` - JWT token validation
- ✅ `testValidateInvalidToken()` - Invalid token rejection
- ✅ `testExtractUsernameFromToken()` - Token parsing
- ✅ `testPasswordHashing()` - Password security verification

### Viewing Test Reports

#### Command Line Execution
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TaskServiceTest

# Run with detailed output
mvn test -X

# Generate HTML report
mvn surefire-report:report

# View the report
open target/site/surefire-report.html
```

#### IDE Execution (IntelliJ IDEA)
1. Right-click on `src/test/java`
2. Select "Run 'All Tests'"
3. View results in the Run window
4. Click on individual tests for details

## CI/CD Pipeline

### Jenkins Pipeline Stages

```groovy
1. Checkout    → Clone repository from Git
2. Build       → Compile Java source code
3. Test        → Execute JUnit tests
4. Package     → Create JAR file
5. Deploy      → Deploy to target environment
```

### Pipeline Configuration

The `Jenkinsfile` defines the complete CI/CD workflow:

- **Checkout Stage**: Pulls latest code from version control
- **Build Stage**: Compiles the application using Maven
- **Test Stage**: Runs unit tests and generates reports
- **Package Stage**: Creates deployable JAR artifact
- **Deploy Stage**: Prepares for deployment (customizable)

### Setting Up Jenkins

1. **Install Jenkins**:
   ```bash
   brew install jenkins-lts
   brew services start jenkins-lts
   ```

2. **Access Jenkins**: http://localhost:8080

3. **Install Required Plugins**:
   - Maven Integration
   - Git Plugin
   - JUnit Plugin
   - Pipeline Plugin

4. **Create New Pipeline Job**:
   - New Item → Pipeline
   - Configure SCM to point to your repository
   - Pipeline script from SCM
   - Specify `Jenkinsfile` path

5. **Configure Tools**:
   - Global Tool Configuration
   - Add Maven installation (Maven-3.9)
   - Add JDK installation (JDK-11)

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT GETDATE()
);
```

### Tasks Table
```sql
CREATE TABLE tasks (
    id INT PRIMARY KEY IDENTITY(1,1),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    completed BIT DEFAULT 0,
    created_by VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

CREATE INDEX idx_tasks_created_by ON tasks(created_by);
```

## API Documentation

### Authentication Endpoints

#### Login
```java
AuthService.login(username, password)
Returns: JWT token string
```

#### Validate Token
```java
AuthService.validateToken(token)
Returns: boolean
```

### Task Management Endpoints

#### Get All Tasks
```java
TaskService.getAllTasks()
Returns: List<Task>
```

#### Create Task
```java
TaskService.createTask(title, description, createdBy)
Returns: Task
```

#### Update Task
```java
TaskService.updateTask(task)
Returns: boolean
```

#### Delete Task
```java
TaskService.deleteTask(id)
Returns: boolean
```

### WebSocket Endpoints

#### Connect
```
ws://localhost:8080/ws/tasks
```

#### Message Format
```json
{
  "action": "create|update|delete",
  "taskId": 123,
  "task": {
    "title": "Task Title",
    "description": "Description",
    "completed": false
  }
}
```

## Usage Examples

### Creating a Task via UI
1. Launch application: `mvn javafx:run`
2. Login with credentials
3. Enter task title and description
4. Click "Add Task"
5. Task appears in list immediately

### Creating a Task Programmatically
```java
TaskService taskService = new TaskService();
Task task = taskService.createTask(
    "Complete project documentation",
    "Write comprehensive README",
    "john_doe"
);
System.out.println("Task created: " + task);
```

### Validating JWT Token
```java
AuthService authService = new AuthService();
String token = authService.login("john_doe", "SecurePass123!");

if (authService.validateToken(token)) {
    String username = authService.getUsernameFromToken(token);
    System.out.println("Welcome, " + username);
}
```

## Troubleshooting

### Database Connection Issues

**Problem**: Cannot connect to database
```
SQLException: Login failed for user 'sa'
```

**Solution**:
```bash
# Verify database is running
docker ps

# Check connection
docker exec -it mssql-server /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P "YourStrong@Password123"

# Restart container if needed
docker-compose restart
```

### Port Already in Use

**Problem**: Port 1433 or 8080 already in use

**Solution**:
```bash
# Find process using port
lsof -i :1433

# Kill the process or change port in configuration
```

### JavaFX Not Loading

**Problem**: JavaFX modules not found

**Solution**:
```bash
# Ensure JavaFX is in Maven dependencies
mvn clean install

# Run with explicit module path
mvn javafx:run
```

### Tests Failing

**Problem**: Unit tests fail due to database

**Solution**:
- Tests use Mockito mocks, not real database
- Ensure Mockito dependency is installed
- Run: `mvn clean test`

## Performance Considerations

### Database Optimization
- Indexes on frequently queried columns (`created_by`)
- Connection pooling for concurrent users
- Prepared statements to prevent SQL injection

### WebSocket Scalability
- Session management for multiple users
- Message broadcasting optimization
- Automatic reconnection handling

### UI Responsiveness
- JavaFX runs on separate threads
- Platform.runLater() for UI updates
- Asynchronous task loading

## Security Best Practices

1. **Password Security**
   - SHA-256 hashing
   - Salting (recommended enhancement)
   - Minimum length requirements

2. **JWT Token Security**
   - 24-hour expiration
   - Secure secret key (256-bit)
   - Token validation on every request

3. **Database Security**
   - Parameterized queries prevent SQL injection
   - Encrypted connections (TLS)
   - Least privilege access

4. **Input Validation**
   - Username/password format checking
   - SQL injection prevention
   - XSS protection

## Future Enhancements

- [ ] Role-based access control (RBAC)
- [ ] Task categories and tags
- [ ] Due dates and reminders
- [ ] File attachments
- [ ] Email notifications
- [ ] REST API endpoints
- [ ] Mobile application
- [ ] Docker containerization for entire app
- [ ] Kubernetes deployment
- [ ] Advanced search and filtering
- [ ] Task collaboration features
- [ ] Activity audit logs

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m 'Add new feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Submit a pull request

### Coding Standards
- Follow Java naming conventions
- Write unit tests for new features
- Update documentation
- Use meaningful commit messages

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Task Management System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## Contact

For questions, issues, or suggestions:
- **GitHub Issues**: [Create an issue](https://github.com/yourusername/task-management-system/issues)
- **Email**: your.email@example.com
- **LinkedIn**: [Your Profile](https://linkedin.com/in/yourprofile)

## Acknowledgments

- Anthropic Claude for architecture guidance
- Microsoft for Azure SQL Edge
- Oracle for JavaFX
- Liquibase for database migrations
- JUnit team for testing framework
- Maven community for build tools

---

**Happy Task Managing! 🚀**

*Built with ❤️ using modern Java technologies*