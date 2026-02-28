# Brand Management System

A full-stack web application for managing brands and their associated companies (chains). Built with a **Spring Boot** REST API backend and a **React + Vite** frontend styled with Tailwind CSS.

---

## Features

- **Brand CRUD** — Create, view, edit, and soft-delete brands
- **Company (Chain) Management** — Filter brands by parent company
- **Search** — Real-time client-side search across brand names and companies
- **Soft Delete** — Brands are deactivated rather than permanently removed; deletion is blocked when a brand is linked to an active Zone
- **Toast Notifications** — Instant feedback for all user actions
- **RESTful API** — Clean JSON API with validation and centralized error handling

---

## Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.11 |
| Spring Data JPA | – |
| Spring Validation | – |
| MySQL | 8+ |
| Lombok | 1.18.36 |
| Maven | 3.9+ |

### Frontend
| Technology | Version |
|---|---|
| React | 19 |
| Vite | 7 |
| Tailwind CSS | 4 |
| Axios | 1.x |
| React Router DOM | 7 |
| React Hot Toast | 2.x |

---

## Project Structure

```
Brand Management System/
├── brand-management-backend/       # Spring Boot application
│   └── src/main/java/com/brandmanagement/
│       ├── controller/             # REST Controllers (BrandController, ChainController)
│       ├── service/                # Service interfaces + implementations
│       ├── repository/             # Spring Data JPA repositories
│       ├── entity/                 # JPA entities (Brand, Chain, Zone)
│       ├── dto/                    # Request / Response DTOs
│       ├── exception/              # Custom exceptions + GlobalExceptionHandler
│       └── config/                 # CORS configuration
│
└── brand-management-frontend/      # React application
    └── src/
        ├── pages/                  # Dashboard, AddBrand, EditBrand
        ├── components/             # Navbar
        └── api/                    # Axios API client (brandApi.js)
```

---

## Prerequisites

- **Java 21** or later
- **Maven 3.9+**
- **MySQL 8+** running locally
- **Node.js 18+** and **npm**

---

## Getting Started

### 1. Database Setup

Create the MySQL database (the app will auto-create tables on first run):

```sql
CREATE DATABASE IF NOT EXISTS brand_management_db;
```

Update credentials in `brand-management-backend/src/main/resources/application.properties` if your MySQL username/password differ from the defaults (`root`/`root`):

```properties
spring.datasource.username=root
spring.datasource.password=root
```

---

### 2. Run the Backend

```bash
cd brand-management-backend
mvn spring-boot:run
```

The API will start on **http://localhost:8080**.

Seed data (5 companies and 5 brands) is loaded automatically on startup via `data.sql`.

---

### 3. Run the Frontend

```bash
cd brand-management-frontend
npm install
npm run dev
```

The React app will be available at **http://localhost:5173**.

---

## API Reference

Base URL: `http://localhost:8080`

### Brands

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/brands` | Get all active brands |
| `GET` | `/api/brands?chainId={id}` | Filter brands by company |
| `GET` | `/api/brands/{id}` | Get a single brand by ID |
| `POST` | `/api/brands` | Create a new brand |
| `PUT` | `/api/brands/{id}` | Update an existing brand |
| `DELETE` | `/api/brands/{id}` | Soft-delete a brand |

### Chains (Companies)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/chains` | Get all active companies |

### Example Request — Create Brand

```json
POST /api/brands
Content-Type: application/json

{
  "brandName": "My Brand",
  "chainId": 1
}
```

### Example Response

```json
{
  "brandId": 6,
  "brandName": "My Brand",
  "chainId": 1,
  "chainName": "Alpha Group",
  "active": true
}
```

---

## Environment Configuration

All backend configuration lives in `application.properties`:

| Property | Default | Description |
|---|---|---|
| `server.port` | `8080` | Backend server port |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/brand_management_db` | MySQL connection URL |
| `spring.datasource.username` | `root` | MySQL username |
| `spring.datasource.password` | `root` | MySQL password |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema auto-update strategy |

---

## Error Handling

The API returns structured error responses for all failure cases:

| Scenario | HTTP Status |
|---|---|
| Resource not found | `404 Not Found` |
| Duplicate brand name | `409 Conflict` |
| Brand linked to active Zone | `409 Conflict` |
| Validation failure | `400 Bad Request` |

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## License

This project is open-source and available under the [MIT License](LICENSE).
