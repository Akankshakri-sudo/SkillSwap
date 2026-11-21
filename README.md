# 🔄 SkillSwap – Student Skill Exchange & Free Mentoring Platform

> **A modern, responsive, beginner-friendly full-stack web application built with Java 21, Spring Boot 3, Spring Data JPA, MySQL, and modern Vanilla Web Technologies (HTML5, CSS3, JavaScript ES6+, Bootstrap 5).**

---

## 📌 1. Project Overview & Problem Statement

### The Problem
College students frequently want to learn in-demand skills such as **Java, Python, SQL, Web Development, Data Structures & Algorithms (DSA), and Artificial Intelligence / Machine Learning (AI/ML)**. However, commercial bootcamps and tutoring services are often expensive, and college students struggle to discover and connect with peers on campus or across universities who already master those topics.

### The Solution: SkillSwap
**SkillSwap** is a student-to-student peer learning ecosystem that bridges this gap by connecting learners based on complementary skills. The platform features two core learning modes:

1. **🔄 Skill Exchange (Barter Learning)**:
   - *Example*: Student A excels at Java and wants to master SQL. Student B excels at SQL and wants to learn Java. SkillSwap pairs them up for a reciprocal exchange where no money changes hands.
2. **🎯 Free Mentoring (Volunteer Learning)**:
   - Senior or experienced students can volunteer to mentor juniors in specific skills without requiring anything in return. Mentees can submit requests, and mentors can accept, schedule, and mark sessions completed.

---

## 🛠️ 2. Technology Stack

| Layer | Technology | Details |
|---|---|---|
| **Language** | Java 21 (LTS) | Modern Java features, records, pattern matching |
| **Framework** | Spring Boot 3.3.0 | Spring Web, Spring Data JPA, Spring Validation, Spring Security |
| **Security** | Spring Security 6 & BCrypt | Password hashing, session-based cookie authentication, authorization guards |
| **ORM / Persistence** | Hibernate 6 / Spring Data JPA | Entity mappings, derived queries, JPQL, automatic schema updates |
| **Database** | MySQL 8.x | Relational database with foreign keys, unique constraints, and indices |
| **Frontend** | HTML5, CSS3, JavaScript (ES6+), Bootstrap 5 | Responsive UI, asynchronous `fetch()`, modular styling, no Node.js/React needed |
| **Testing** | JUnit 5 & Mockito, MockMvc, H2 Database | Unit and integration testing suites |
| **Build Tool** | Apache Maven | Dependency management and executable JAR packaging |

---

## 🏗️ 3. Architecture & Project Structure

SkillSwap follows the industry-standard **Clean Layered Architecture**, ensuring strict separation of concerns, testability, and maintainability.

```
skillswap/
├── pom.xml                                      # Maven dependencies & build setup
├── application.properties.example               # Example DB and server config
├── .gitignore                                   # Git exclusions
├── src/
│   ├── main/
│   │   ├── java/com/skillswap/
│   │   │   ├── SkillSwapApplication.java        # Spring Boot entry point (@SpringBootApplication)
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java          # Spring Security, BCrypt, CORS, filter chain
│   │   │   │   └── DataSeeder.java              # CommandLineRunner to populate default skills
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java          # Registration, login, logout, session status
│   │   │   │   ├── UserController.java          # Profile, student discovery, skill addition
│   │   │   │   ├── SkillController.java         # Global skills catalog
│   │   │   │   └── LearningRequestController.java # Exchange & mentoring request lifecycle
│   │   │   ├── dto/
│   │   │   │   ├── RegisterRequest.java         # Registration input validation
│   │   │   │   ├── LoginRequest.java            # Credentials input validation
│   │   │   │   ├── UpdateProfileRequest.java    # Partial profile updates
│   │   │   │   ├── AddUserSkillRequest.java     # User skill assignment input
│   │   │   │   ├── CreateRequestDto.java        # Exchange & mentoring submission
│   │   │   │   ├── UserResponse.java            # Safe user response (omits password hash)
│   │   │   │   ├── LearningRequestResponse.java # Enriched request details
│   │   │   │   └── DashboardStats.java          # Summary metrics DTO
│   │   │   ├── entity/
│   │   │   │   ├── User.java                    # Student account & profile entity
│   │   │   │   ├── Skill.java                   # Master catalog skill entity
│   │   │   │   ├── UserSkill.java               # Join entity with SkillType (CAN_TEACH / WANTS_TO_LEARN)
│   │   │   │   └── LearningRequest.java         # Exchange / mentoring request & status entity
│   │   │   ├── exception/
│   │   │   │   ├── BadRequestException.java     # 400 Bad Request
│   │   │   │   ├── ResourceNotFoundException.java # 404 Not Found
│   │   │   │   ├── UnauthorizedException.java   # 403 Forbidden
│   │   │   │   └── GlobalExceptionHandler.java  # @RestControllerAdvice centralized error handling
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java          # Custom JPQL discovery & email lookups
│   │   │   │   ├── SkillRepository.java         # Case-insensitive name queries
│   │   │   │   ├── UserSkillRepository.java     # Per-user skill management & duplicate check
│   │   │   │   └── LearningRequestRepository.java # Requests lifecycle & dashboard aggregations
│   │   │   └── service/
│   │   │       ├── UserService.java             # User registration, profile updates, discovery
│   │   │       ├── SkillService.java            # Skill catalog & user-skill associations
│   │   │       └── LearningRequestService.java  # Request creation, accept/reject/complete flows
│   │   └── resources/
│   │       ├── application.properties           # Spring Boot configuration
│   │       └── static/                          # Frontend Static Web Assets
│   │           ├── css/
│   │           │   └── styles.css               # Indigo/purple theme, responsive layout styles
│   │           ├── js/
│   │           │   └── api.js                   # Unified fetch client with cookie auth & toasts
│   │           ├── index.html                   # Landing page (Hero, How It Works, Features)
│   │           ├── register.html                # Student registration page
│   │           ├── login.html                   # Student login page
│   │           ├── dashboard.html               # Main dashboard with real-time stats
│   │           ├── profile.html                 # Profile view, edit modal & skill manager
│   │           ├── discover.html                # Student discovery, skill search & request modal
│   │           ├── student.html                 # Public student profile view
│   │           ├── requests.html                # Incoming & Sent requests with action controls
│   │           └── sessions.html                # Active learning exchanges & completion action
│   └── test/
│       ├── java/com/skillswap/
│       │   ├── controller/
│       │   │   └── AuthControllerTest.java      # MockMvc integration tests for authentication
│       │   └── service/
│       │       ├── UserServiceTest.java         # Unit tests for UserService
│       │       ├── SkillServiceTest.java        # Unit tests for SkillService
│       │       └── LearningRequestServiceTest.java # Unit tests for Request workflows
│       └── resources/
│           └── application-test.properties      # In-memory H2 database configuration for testing
```

---

## 🗄️ 4. Database Design & Entity Relationships

```
┌─────────────────────────────────┐                 ┌─────────────────────────────────┐
│              User               │                 │              Skill              │
├─────────────────────────────────┤                 ├─────────────────────────────────┤
│ id (PK)                         │                 │ id (PK)                         │
│ name, email (UNIQUE), password  │                 │ name (UNIQUE)                   │
│ college, department, year       │                 │ category                        │
│ bio, profileImageUrl            │                 └─────────────────────────────────┘
│ offersFreeMentoring             │                               ▲
│ preferredMode (ONLINE/IN_PERSON)│                               │
│ createdAt                       │                               │
└─────────────────────────────────┘                               │
         │                  ▲                                     │
         │ (1:N)            │ (1:N)                               │
         ▼                  │                                     │
┌───────────────────────────────┐                                 │
│           UserSkill           │                                 │
├───────────────────────────────┤                                 │
│ id (PK)                       │                                 │
│ user_id (FK -> User.id)       ├─────────────────────────────────┘
│ skill_id (FK -> Skill.id)     │
│ skillType (CAN_TEACH /        │
│            WANTS_TO_LEARN)    │
│ UNIQUE(user, skill, type)     │
└───────────────────────────────┘

         ┌────────────────────────────────────────────────────────┐
         │                    LearningRequest                     │
         ├────────────────────────────────────────────────────────┤
         │ id (PK)                                                │
         │ requester_id (FK -> User.id)                           │
         │ receiver_id  (FK -> User.id)                           │
         │ requestType  (EXCHANGE / FREE_MENTORING)               │
         │ skill_to_learn_id (FK -> Skill.id)                     │
         │ skill_to_teach_id (FK -> Skill.id, NULLable)           │
         │ message (TEXT)                                         │
         │ status (PENDING / ACCEPTED / REJECTED / COMPLETED)     │
         │ createdAt, updatedAt                                   │
         └────────────────────────────────────────────────────────┘
```

### Key Relational Constraints & Integrity
- **Unique Email**: `User.email` is decorated with `@Column(unique = true)` and validated prior to insertion.
- **Unique Skill Names**: `Skill.name` is unique to prevent duplicate entries like "Java" and "java".
- **Unique UserSkill**: `uk_user_skill_type` ensures a student cannot add "Java - CAN_TEACH" more than once.
- **Self-Request Prevention**: Handled in `LearningRequestService` so `requester_id != receiver_id`.
- **Duplicate Request Guard**: A student cannot send another pending or accepted request to the same student for the same skill.

---

## 🚀 5. REST API Documentation

### 5.1 Authentication (`/api/auth`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register student with name, email, password, college |
| `POST` | `/api/auth/login` | Public | Authenticate user, create session, return profile |
| `POST` | `/api/auth/logout` | Authenticated | Invalidate current session |
| `GET` | `/api/auth/me` | Authenticated | Returns current authenticated user overview |

#### Sample Register Request:
```json
POST /api/auth/register
Content-Type: application/json

{
  "name": "Priya Sharma",
  "email": "priya@college.edu",
  "password": "Password@123",
  "college": "IIT Bombay",
  "department": "Computer Science",
  "yearOfStudy": 3
}
```

---

### 5.2 User & Profile Management (`/api/users`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/users/me` | Authenticated | Fetch authenticated student profile with skills |
| `PUT` | `/api/users/me` | Authenticated | Update bio, college, mode, and mentoring toggle |
| `GET` | `/api/users/{id}` | Authenticated | Fetch another student's public profile |
| `GET` | `/api/users/discover` | Authenticated | Discover students (supports `?skill=Java&mode=ONLINE&page=0&size=12`) |
| `POST` | `/api/users/me/skills` | Authenticated | Add a skill (`CAN_TEACH` or `WANTS_TO_LEARN`) |
| `DELETE` | `/api/users/me/skills/{id}` | Authenticated | Remove a skill owned by authenticated student |
| `GET` | `/api/users/me/dashboard` | Authenticated | Fetch aggregated statistics for the student dashboard |

---

### 5.3 Skills Catalog (`/api/skills`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/skills` | Public | List all master skills grouped by category |
| `POST` | `/api/skills` | Authenticated | Add a new skill to the global catalog |

---

### 5.4 Learning Requests & Sessions (`/api/requests`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/requests` | Authenticated | Send a Skill Exchange or Free Mentoring request |
| `GET` | `/api/requests/received`| Authenticated | View incoming requests where student is receiver |
| `GET` | `/api/requests/sent` | Authenticated | View requests sent by student |
| `GET` | `/api/requests/active` | Authenticated | View accepted, ongoing sessions |
| `PATCH`| `/api/requests/{id}/accept` | Receiver Only | Accept a pending request |
| `PATCH`| `/api/requests/{id}/reject` | Receiver Only | Reject a pending request |
| `PATCH`| `/api/requests/{id}/complete`| Participants Only | Mark session as completed |

#### Sample Skill Exchange Request:
```json
POST /api/requests
Content-Type: application/json

{
  "receiverId": 2,
  "requestType": "EXCHANGE",
  "skillToLearnId": 1,
  "skillToTeachId": 7,
  "message": "Hey! I can teach you SQL in exchange for Java mentoring."
}
```

#### Sample Free Mentoring Request:
```json
POST /api/requests
Content-Type: application/json

{
  "receiverId": 2,
  "requestType": "FREE_MENTORING",
  "skillToLearnId": 1,
  "message": "Hi! I saw you offer free Java mentoring. Would love guidance on Spring Boot basics."
}
```

---

## 💻 6. How to Run the Application

### Prerequisites
1. **Java Development Kit (JDK 21 or higher)**
2. **Apache Maven (3.8+)**
3. **MySQL Server (8.0+)**

### Step 1: Database Setup
Start your MySQL server and create the database:
```sql
CREATE DATABASE skillswap_db;
```

### Step 2: Configure Credentials
Open `src/main/resources/application.properties` (or copy from `application.properties.example`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/skillswap_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Build & Run
From the `skillswap` project directory:
```bash
# Clean and compile
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

*Alternatively, open the project in **IntelliJ IDEA** or **Eclipse / VS Code** as a Maven project and run `SkillSwapApplication.java`.*

### Step 4: Open in Browser
Once Tomcat starts on port `8080`, navigate to:
```
http://localhost:8080
```
- **Landing Page**: `http://localhost:8080/index.html`
- **Registration**: `http://localhost:8080/register.html`
- **Login**: `http://localhost:8080/login.html`
- **Dashboard**: `http://localhost:8080/dashboard.html`
- **Discover**: `http://localhost:8080/discover.html`

*(Note: DataSeeder automatically seeds 24 essential skills such as Java, Python, SQL, DSA, AI/ML, and UI/UX on startup).*

---

## 🧪 7. Postman API Testing Walkthrough

To test the application in Postman:

1. **Register Student A**:
   - `POST http://localhost:8080/api/auth/register`
   - Body: `{"name":"Aarav Patel","email":"aarav@college.edu","password":"password123","college":"IIT Bombay"}`
   - *Expected*: `201 Created`

2. **Login Student A**:
   - `POST http://localhost:8080/api/auth/login`
   - Body: `{"email":"aarav@college.edu","password":"password123"}`
   - *Expected*: `200 OK` (Postman automatically stores the `JSESSIONID` cookie)

3. **Add Skills for Student A**:
   - `POST http://localhost:8080/api/users/me/skills`
   - Body: `{"skillId": 1, "skillType": "CAN_TEACH"}` (e.g. Java)
   - `POST http://localhost:8080/api/users/me/skills`
   - Body: `{"skillId": 7, "skillType": "WANTS_TO_LEARN"}` (e.g. SQL)

4. **Register and Login Student B**:
   - `POST http://localhost:8080/api/auth/register`
   - Body: `{"name":"Priya Sharma","email":"priya@college.edu","password":"password123","college":"IIT Bombay"}`
   - Login as Priya, add skill 7 (SQL) as `CAN_TEACH` and enable mentoring via `PUT /api/users/me`.

5. **Student B sends Skill Exchange Request to Student A**:
   - `POST http://localhost:8080/api/requests`
   - Body: `{"receiverId": 1, "requestType": "EXCHANGE", "skillToLearnId": 1, "skillToTeachId": 7, "message": "Let's swap Java and SQL!"}`
   - *Expected*: `201 Created`

6. **Student A views and accepts the request**:
   - Login as Student A.
   - `GET http://localhost:8080/api/requests/received` -> note the request `id`.
   - `PATCH http://localhost:8080/api/requests/{id}/accept` -> *Expected*: `status: ACCEPTED`.

7. **Both students see Active Session and Complete it**:
   - `GET http://localhost:8080/api/requests/active`
   - `PATCH http://localhost:8080/api/requests/{id}/complete` -> *Expected*: `status: COMPLETED`.

---

## 🎓 8. Important Java & Spring Boot Concepts (Interview Guide)

This project illustrates core software engineering concepts frequently discussed in technical interviews:

### 1. Inversion of Control (IoC) & Dependency Injection (DI)
- Instead of using `new UserService()`, Spring's IoC container instantiates beans and injects them where needed.
- We utilize **Constructor Injection** via Lombok's `@RequiredArgsConstructor`. This makes components immutable (`final` fields) and easy to unit test by passing mock objects without requiring Spring's reflection.

### 2. Spring Data JPA & Derived Query Methods
- Extending `JpaRepository<User, Long>` gives standard CRUD methods out of the box.
- Methods such as `findByEmail(String email)` and `existsByEmail(String email)` are automatically translated by Spring Data into SQL statements at runtime.
- Complex searches use JPQL (Java Persistence Query Language) in `@Query` to perform joins between entities independently of database-specific SQL dialect.

### 3. DTO Pattern & Entity Decoupling
- Entities represent the database table structure, while DTOs (Data Transfer Objects) represent the API contract.
- Using DTOs prevents **mass assignment vulnerabilities**, prevents recursive JSON serialization bugs with bidirectional relationships, and ensures confidential fields (such as `password`) are never exposed in responses.

### 4. Bean Validation (`jakarta.validation`)
- Declarative validation using annotations like `@NotBlank`, `@Email`, and `@Size`.
- Using `@Valid` in `@RestController` parameters triggers automated validation before method execution, seamlessly routing validation issues to `@RestControllerAdvice`.

### 5. Centralized Error Handling (`@RestControllerAdvice`)
- Rather than sprinkling `try-catch` blocks across controllers, exceptions propagate to `GlobalExceptionHandler`.
- This ensures the frontend receives predictable, structured error payloads (`timestamp`, `status`, `error`, `message`, `errors`).

### 6. BCrypt Password Hashing & Security
- BCrypt incorporates an automated salt and slow hashing algorithm (work factor 10 = $2^{10}$ iterations) to protect passwords against rainbow table lookups and brute-force dictionary attacks.

---

## 🔮 9. Future Enhancements
- 💬 **Real-time In-App Chat**: WebSockets (STOMP) for direct messaging between exchange partners.
- ⭐️ **Ratings & Reviews**: Student feedback ratings after completing learning sessions.
- 🔔 **Email Notifications**: Spring Mail notifications when requests are received, accepted, or completed.
- 📅 **Session Scheduling**: Google Calendar integration to schedule video calls directly.
