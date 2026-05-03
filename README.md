# 🍔 Event-Driven Food Order System

A real-time, event-driven microservices application built with **Spring Boot**, **Apache Kafka**, and **Docker**. This project simulates a restaurant's ordering system, featuring a live web dashboard that updates automatically as orders are processed in the kitchen.

## 🏗 Architecture Overview

The system consists of two independent microservices communicating asynchronously via Kafka:

1. **Order Service (Producer/Consumer & Frontend Host)**
   * Provides a REST API for customers to place and pick up orders.
   * Saves order state to a **MySQL** database with automated audit timestamps.
   * Publishes new orders to a Kafka topic.
   * Serves a Vanilla JS/HTML frontend.
   * Uses **Server-Sent Events (SSE)** to push real-time updates to the web dashboard.
   * Listens for "Ready" events and Dead Letter Queue (DLQ) alerts to update the UI.

2. **Kitchen Service (Consumer/Producer)**
   * Listens for new orders from the Order Service.
   * Simulates preparation time.
   * Publishes a "Ready" event back to Kafka upon completion.
   * Implements robust error handling (e.g., rejecting invalid items) and routes failed messages to a **Dead Letter Topic (DLT)**.

## ✨ Key Features
* **Real-Time UI:** The web dashboard updates instantly without polling, powered by SSE.
* **Event-Driven:** Decoupled architecture using Kafka as a message broker.
* **Resiliency:** Configured with Spring Kafka Error Handling, Retries, and a Dead Letter Queue (DLQ) for failed processes.
* **Security:** Database connects via a dedicated, non-root MySQL user.
* **Containerized:** Fully deployable using Docker and Docker Compose.

## 🛠 Tech Stack
* **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring Kafka
* **Frontend:** HTML5, CSS3, Vanilla JavaScript (EventSource API)
* **Database:** MySQL 8.0
* **Infrastructure:** Apache Kafka, Docker, Docker Compose

---

## 🚀 Getting Started (Local & Server Deployment)

Because the entire infrastructure is containerized, deploying this project on a local machine or a live Ubuntu server requires the exact same steps.

### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine/server.

### Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/YOUR_USERNAME/kafka-food-order-system.git](https://github.com/YOUR_USERNAME/kafka-food-order-system.git)
   cd kafka-food-order-system
