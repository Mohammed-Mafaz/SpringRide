# 🚗 SpringRide

SpringRide is a backend-only **ride-sharing application** built with **Spring Boot**, designed to enable seamless ride requests and acceptances between Riders and Drivers. It features JWT-based authentication, spatial data management with PostGIS, and real-time routing using the OSRM API.

---

## 🏗️ Architecture Overview

SpringRide follows a layered **Spring Boot REST API** architecture:

```
springRide/
├── src/
│   ├── main/
│   │   ├── java/com/example/springride/
│   │   │   ├── config/          # Security and ModelMapper configuration
│   │   │   ├── controller/      # REST Controllers (Rider, Driver, Ride, Wallet)
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # JPA Entities (User, Ride, Wallet, etc.)
│   │   │   ├── repository/     # Spring Data JPA Repositories
│   │   │   ├── security/       # JWT Filters & Custom UserDetailsService
│   │   │   ├── service/        # Business Logic Layer
│   │   │   └── util/          # Utility classes (GeometryUtil, Distance, etc.)
│   │   └── resources/
│   │       ├── application.properties  # DB & app configuration
│   │       └── data.sql                # Sample data (if any)
│   └── test/                           # Unit and integration tests
├── pom.xml
└── README.md
```

---

## ✨ Features

- ✅ **JWT-based Authentication** — Secure endpoints for Riders & Drivers.
- 📍 **PostGIS Integration** — Efficient spatial data handling for ride locations.
- 🧭 **OSRM API Integration** — Accurate road distance calculation for optimal routes.
- 🧾 **Wallet Service** — Support for multiple payment strategies (cash & wallet).
- 🧰 **Layered Architecture** — Controllers → Services → Repositories → Entities.
- 🧪 **Spring Boot Test Support** — Modular testing setup.

---

## 🛠 Tech Stack

| Component              | Technology                         |
|-------------------------|-------------------------------------|
| Language                | Java                               |
| Framework               | Spring Boot                        |
| Authentication          | Spring Security + JWT              |
| Persistence             | Spring Data JPA                    |
| Database                | PostgreSQL                         |
| Mapping                 | ModelMapper                        |
| Routing / Distance      | OSRM API (via DistanceService)     |
| Build Tool              | Maven                              |

---

## ⚡ Getting Started

### 1️⃣ Clone the repository
```bash
git clone https://github.com/Mohammed-Mafaz/SpringRide.git
cd SpringRide
```

### 2️⃣ Configure PostgreSQL + PostGIS
Create a database and enable the PostGIS extension:
```sql
CREATE DATABASE springride;
\c springride;
CREATE EXTENSION postgis;
```

Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/springride
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Build and run the application
```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint                     | Description                     | Auth Required |
|--------|-------------------------------|----------------------------------|---------------|
| POST   | `/auth/register`             | Register new Rider or Driver    | ❌           |
| POST   | `/auth/login`                | Login and get JWT               | ❌           |
| GET    | `/rides/available`          | Fetch nearby available rides   | ✅           |
| POST   | `/rides/request`            | Rider requests a new ride      | ✅           |
| POST   | `/rides/accept/{rideId}`    | Driver accepts a ride          | ✅           |
| GET    | `/wallet/balance`          | Get wallet balance             | ✅           |
| POST   | `/wallet/topup`            | Top up user wallet             | ✅           |

You can import the Postman collection JSON from [`postman_collection.json`](./postman_collection.json) to quickly test these endpoints.

---

## 🧠 Future Enhancements

- ⏱ Real-time ride tracking with WebSockets
- 📅 Scheduled rides and recurring bookings
- ⭐ Ride rating and feedback system
- 🌐 Integration with mapping SDKs for mobile clients

---

## 👤 Author

**Mohammed Mafaz**  
[GitHub](https://github.com/Mohammed-Mafaz)
