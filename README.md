# 🚀 E-Wallet System (Java 17 + Spring Boot 3)

A high-concurrency, ACID-compliant e-wallet backend system built using **Java 17**, **Spring Boot 3**, and **PostgreSQL**.  
Designed to handle **massively parallel credit/debit transactions** while ensuring **atomicity, isolation, and consistency**—as required in production-grade financial systems.

This project includes:
- Transaction-safe wallet operations (credit/debit)
- Pessimistic locking for concurrency control
- Global exception handling with custom error codes
- Comprehensive unit tests including concurrency tests
- Clean layered architecture (Controller → Service → Repository)
- Professional logging & auditing support

---

## ✨ Features

### 🔐 High-Concurrency Transaction Engine
- Safe **balance updates** using `SELECT … FOR UPDATE`
- Prevents race conditions, lost updates, and double spending
- Fully compliant with financial ACID rules

### 💳 Wallet Operations
- **Credit (Deposit)**
- **Debit (Withdrawal)** with insufficient funds validation
- Transaction history stored in a normalized, query-efficient schema

### 🧪 Testing & Reliability
- Concurrency stress tests (credit + debit)
- API integration tests using Spring Boot Test
- Mock & real DB compatibility (PostgreSQL / H2)
- Load test examples using Gatling & JMeter

### 🛡️ Robust Error Handling
- Custom `AppException` mapped to structured `ResponseDto`
- Clear business error codes (e.g., `80010`, `80011`, `80012`)
- Global exception handler with consistent API responses

---

## 🏗️ Architecture Overview

┌───────────────────────────────┐
│ REST Controller │
│ - Transaction          │
└──────────────┬────────────────┘
│
┌──────────────▼────────────────┐
│ Service Layer │
│ - Transaction logic    │
│ - Concurrency handling │
│ - Validation           │
└──────────────┬────────────────┘
│
┌──────────────▼────────────────┐
│ Repository Layer │
│ - UserRepository        │
│ - TransactionRepository │
└──────────────┬────────────────┘
│
┌──────────────▼────────────────┐
│ PostgreSQL Database │
│ - users        │
│ - transactions │
└───────────────────────────────┘

## 🗂️ Database Schema

### `users`

sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0,

    -- Audit Fields (auto-filled by JPA)
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

### `transactions`
sql
Salin kode
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    amount DECIMAL(15, 2) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('CREDIT', 'DEBIT')),

    -- Audit Fields (auto-filled by JPA)
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

---

## 🔧 Tech Stack
Layer	Technology
Language	Java 17
Framework	Spring Boot 3
Database	PostgreSQL
ORM	Hibernate / JPA
Testing	JUnit 5, Spring Boot Test
Load Testing	JMeter, Gatling
Build Tool	Maven
Logging	SLF4J + Logback
Locking	PESSIMISTIC_WRITE

## 📌 API Endpoints
POST /api/transactions/credit
{
  "user_id": 123,
  "amount": 100.00
}

POST /api/transactions/debit
{
  "user_id": 123,
  "amount": 50.00
}


Example success response:

{
  "status": "success",
  "transaction_id": 457,
  "new_balance": 450.00
}


Example error response:

{
  "status": "error",
  "message": "Insufficient funds"
}

## 🧪 Testing: Concurrency & Reliability
✅ Credit Concurrency Test

100 threads

100 transactions per thread

Each deposit = 1000

Expected final balance = 10,000,000

✅ Debit Concurrency Test

Prevents double spending

Ensures atomic validation of balance

## ▶️ Running the Application
1. Clone repository
git clone https://github.com/username/ewallet-system.git
cd ewallet-system

2. Build & run
mvn clean install
mvn spring-boot:run

3. Run tests
mvn test

## 📦 Folder Structure
src/
 ├── main/
 │   ├── java/com/ewallet_system
 │   │    ├── controller/
 │   │    ├── service/
 │   │    ├── repository/
 │   │    ├── dto/
 │   │    └── exception/
 │   └── resources/
 │        ├── application.yml
 └── test/
      ├── unit/
      ├── integration/
      └── concurrency/

## 👤 Author

Addyani Fajar
Backend Engineer | Java & Go | Distributed Systems | Tech Entusias
GitHub: https://github.com/addyani
LinkedIn: https://www.linkedin.com/addyani