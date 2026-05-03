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
* **Backend:** Java 26, Spring Boot, Spring Data JPA, Spring Kafka
* **Frontend:** HTML5, CSS3, Vanilla JavaScript (EventSource API)
* **Database:** MySQL 8.0
* **Infrastructure:** Apache Kafka, Docker, Docker Compose

---

## 🚀 Getting Started

### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine/server.
* Java 26 SDK installed.
* Maven installed.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/kafka-food-order-system.git # Replace with your actual repo URL
    cd kafka-food-order-system
    ```

2.  **Set up Environment Variables:**
    Each microservice uses a `.env` file for sensitive configurations (like database credentials and Kafka server addresses). These files are ignored by Git.

    *   **For `orderservice`:**
        Create a file named `.env` inside the `orderservice/` directory with the following content:
        ```dotenv
        # Database Configuration
        DB_URL=jdbc:mysql://localhost:3306/food_orders?createDatabaseIfNotExist=true
        DB_USERNAME=root # Replace with your MySQL username
        DB_PASSWORD=your_mysql_password # Replace with your MySQL password

        # Kafka Configuration
        KAFKA_SERVERS=localhost:9092
        ```
        **Note:** Make sure `your_mysql_password` matches the password for the `root` user in your MySQL setup or create a dedicated user.

    *   **For `kitchenservice`:**
        Create a file named `.env` inside the `kitchenservice/` directory with the following content:
        ```dotenv
        # Kafka Configuration
        KAFKA_SERVERS=localhost:9092
        ```

### Running the Application

1.  **Start Infrastructure with Docker Compose:**
    Navigate to the root of the project (`kafka-food-order-system/`) and run:
    ```bash
    docker-compose up -d
    ```
    This will start Kafka, Zookeeper, and MySQL containers in the background.

2.  **Build and Run Spring Boot Microservices:**

    *   **Option A: From your IDE (Recommended for Development)**
        Open the project in IntelliJ IDEA (or your preferred IDE).
        Navigate to `orderservice/src/main/java/com/example/orderservice/OrderServiceApplication.java` and run it.
        Navigate to `kitchenservice/src/main/java/com/example/kitchenservice/KitchenServiceApplication.java` and run it.
        **Important:** Ensure the "Working directory" for each run configuration is set to its respective module (e.g., `orderservice/` for `OrderServiceApplication`).

    *   **Option B: Using Maven from Terminal**
        Open two separate terminal windows.

        In the first terminal, navigate to `orderservice/` and run:
        ```bash
        mvn spring-boot:run
        ```

        In the second terminal, navigate to `kitchenservice/` and run:
        ```bash
        mvn spring-boot:run
        ```

3.  **Access the Frontend:**
    Once both services are running, open your web browser and go to:
    ```
    http://localhost:8080
    ```
    You should see the Food Order System UI.

### Testing the Application

1.  **Place an Order:**
    *   In the UI, enter a "Customer Name" and "Food Item" (e.g., "John Doe", "Pepperoni Pizza").
    *   Click "Place Order".
    *   You should see a success message, and the order will appear on the "Live Kitchen Board" with status "PENDING".

2.  **Observe Kitchen Processing:**
    *   The `kitchenservice` console will show logs indicating it received the order, is "preparing" it, and then sends a "READY" signal back to Kafka.
    *   The "Live Kitchen Board" in the UI will automatically update the order's status to "READY".

3.  **Pick Up an Order:**
    *   Note the Order ID from the "Live Kitchen Board".
    *   In the UI, enter the Order ID in the "Ready to Pickup?" section.
    *   Click "Confirm Pickup".
    *   You should see a success message, and the order will disappear from the "Live Kitchen Board".

### Cleaning Up

To stop all Docker containers and remove their networks/volumes:
```bash
docker-compose down
```
To stop the Spring Boot applications, simply stop the processes in your IDE or close the terminal windows.
