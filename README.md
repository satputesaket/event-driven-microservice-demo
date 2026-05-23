# Event-Driven Microservices with Spring Boot, Axon & Saga Pattern

A distributed microservices system built using **Spring Boot**, **Spring Cloud**, and **Axon Framework** demonstrating:

- Event-Driven Architecture
- CQRS
- Event Sourcing
- Saga Pattern
- Distributed Transactions
- API Gateway
- Service Discovery
- Compensation Transactions

---

## 📌 Overview

This project demonstrates how to build scalable and transactional distributed microservices using modern event-driven design principles.

The application showcases:

- Communication between distributed services using events and commands
- Service discovery and API gateway routing
- CQRS (Command Query Responsibility Segregation)
- Event Sourcing with Axon Framework
- Distributed transactions using the Saga pattern
- Compensation transactions and rollback handling

---

## 🏗️ Architecture

The project consists of the following microservices:

```text
API Gateway
    |
    ├── Orders Service
    ├── Product Service
    ├── Payment Service
    ├── Users Service
    └── Discovery Server
```

### Modules

| Service | Description |
|---|---|
| `apigateway` | API Gateway for routing requests |
| `discoveryserver` | Eureka Discovery Server |
| `orders` | Handles order creation and Saga orchestration |
| `product` | Manages product inventory and reservations |
| `payments` | Processes payments |
| `users` | User and payment details |
| `core` | Shared commands, events, queries, DTOs |

---

# 🚀 Technologies Used

- Java 17+
- Spring Boot
- Spring Cloud
- Axon Framework
- Axon Server
- Spring Cloud Gateway
- Eureka Discovery Server
- Maven
- Docker
- CQRS
- Event Sourcing
- Saga Pattern

---

# 📚 Concepts Covered

This project demonstrates the following concepts from the ground up:

## Microservices Fundamentals

- Creating Spring Boot Microservices
- REST APIs
- Running multiple microservice instances
- API Gateway
- Service Discovery

## Event-Driven Architecture

- Commands
- Events
- Queries
- Event Bus
- Distributed Messaging

## Axon Framework

- Aggregates
- Command Handlers
- Event Sourcing Handlers
- Query Handlers
- Sagas

## CQRS & Event Sourcing

- Separating write and read operations
- Storing events instead of current state
- Rebuilding aggregate state from events

## Distributed Transactions

- Saga orchestration
- Compensation transactions
- Rollback flows across services

---

# 🔄 Saga Flow

## Positive Flow

```text
Create Order
    ↓
Reserve Product
    ↓
Process Payment
    ↓
Approve Order
```

---

## Compensation / Failure Flow

```text
Create Order
    ↓
Reserve Product
    ↓
Payment Failure
    ↓
Cancel Product Reservation
    ↓
Reject Order
```

---

# ⚙️ Running the Project

## 1. Start Axon Server

```bash
docker run -d \
--platform linux/amd64 \
--name axonserver \
-p 8024:8024 \
-p 8124:8124 \
axoniq/axonserver
```

Axon Dashboard:

```text
http://localhost:8024
```

---

## 2. Start Discovery Server

Run:

```text
discoveryserver
```

---

## 3. Start Microservices

Run the following services:

```text
users
product
payments
orders
apigateway
```

---

# ⚙️ Axon Configuration

Each service should include:

```properties
axon.axonserver.servers=localhost:8124
```

---

# 🧪 Testing Saga Transactions

## Successful Transaction

- Create an order
- Product gets reserved
- Payment gets processed
- Order gets approved

---

## Failed Transaction / Compensation

Simulate payment failure inside payment service:

```java
if(paymentFailed){
    throw new RuntimeException("Payment failed");
}
```

Expected flow:

```text
Payment Failed
    ↓
Cancel Product Reservation
    ↓
Reject Order
```

---

# 📂 Project Structure

```text
event-driven-microservice-demo
│
├── apigateway
├── core
├── discoveryserver
├── orders
├── payments
├── product
└── users
```

---

# 📖 Learning Outcomes

By completing this project, you will understand:

- How distributed microservices communicate
- How CQRS and Event Sourcing work
- How Axon Framework simplifies event-driven development
- How Saga orchestrates distributed transactions
- How to implement rollback logic using compensation transactions

---

# 🛠️ Future Improvements

- Kafka integration
- Distributed tracing
- Kubernetes deployment
- Authentication & Authorization
- Monitoring & Observability
- Retry mechanisms
- Circuit breakers

---

# 🙌 Acknowledgements

This project is based on learning event-driven microservices using:

- Spring Boot
- Spring Cloud
- Axon Framework
- CQRS
- Event Sourcing
- Saga Pattern

---

# 📜 License

This project is for learning and educational purposes.
