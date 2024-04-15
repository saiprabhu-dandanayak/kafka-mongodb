# Kafka-MongoDB Integration Project

🚀 This repository demonstrates an efficient integration between Apache Kafka and MongoDB, showcasing a seamless workflow for message production, consumption, and data storage.

## 🎯 Features

- **📡 Kafka Message Production**: Dynamically produces messages to a Kafka topic.
- **📥 Kafka Message Consumption**: Efficiently consumes messages from Kafka.
- **💾 Data Persistence**: Stores the consumed messages into MongoDB for scalable data management.

## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them:

### 🛠️ Installation Instructions

#### Apache Kafka Installation
Kafka requires Java to run, so make sure Java is installed on your system. Follow the steps below to install Kafka:

1. **Download Kafka**: Go to the [Apache Kafka Downloads](https://kafka.apache.org/downloads) page and download the latest release of Kafka.
2. **Extract the Archive**: Once downloaded, extract the archive using a tool like `tar` on Unix/Linux or an archive manager on Windows.
    ```bash
    tar -xzf kafka_2.13-2.8.0.tgz
    cd kafka_2.13-2.8.0
    ```
3. **Start the Kafka Environment**:
    - Start the ZooKeeper service:
      ```bash
      🐘 bin/zookeeper-server-start.sh config/zookeeper.properties
      ```
    - Open another terminal session and start the Kafka broker service:
      ```bash
      🚀 bin/kafka-server-start.sh config/server.properties
      ```

#### MongoDB Installation
MongoDB provides a flexible schema and powerful features, making it a popular choice for many types of applications. Here’s how to install MongoDB:

1. **Download MongoDB**: Go to the [MongoDB Community Download Page](https://www.mongodb.com/try/download/community) and choose the appropriate version for your operating system.
2. **Install MongoDB**:
    - **On Windows**:
      - Run the installer (.msi file) you downloaded.
      - Follow the installation wizard, and make sure to check the option to install MongoDB as a service.
    - **On macOS**:
      - You can use Homebrew to install MongoDB:
        ```bash
        🍺 brew tap mongodb/brew
        🍺 brew install mongodb-community@5.0
        ```
    - **On Linux**:
      - Each Linux distribution has specific instructions for the installation of MongoDB. Refer to the official [MongoDB Manual](https://docs.mongodb.com/manual/administration/install-on-linux/) for detailed steps.
3. **Start MongoDB**:
    - **On Windows**, use the Services app to start the MongoDB service.
    - **On macOS and Linux**, you can start MongoDB using the `mongod` command if it’s not set to auto-start:
      ```bash
      🚀 mongod --config /usr/local/etc/mongod.conf
      ```

#### Verifying Installations
- **For Kafka**: Ensure that both the ZooKeeper and Kafka server are running by checking the terminal logs indicating that the services are up and accepting connections.
- **For MongoDB**: Connect to your MongoDB instance using the MongoDB Shell or a GUI like MongoDB Compass to verify it's running.






