# 🏠 Chatop API

## 📌 Description
Chatop API is the backend of a rental management application. It enables the management of users, rentals, and messages between users.

## 🚀 Technologies Used
![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.9.9-purple)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-green)
![MySQL](https://img.shields.io/badge/MySQL-8.5-darkblue)

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.3
- **Database**: MySQL 8.5
- **Authentication**: JWT (JSON Web Token)
- **API Documentation**: Swagger

---

## ⚙️ Installation and Configuration

### ✅ Prerequisites
- Java 21
- Maven
- MySQL 8.5
- Postman *(optional, for API testing)*

### 🔧 Installation Steps

#### 1️⃣ Clone the Project
```sh
git clone <repository_url>
cd chatop-api
```

#### 2️⃣ Configure the Database
```sh
mysql -u root -p
```
Inside the MySQL shell, run:
```sql
CREATE DATABASE chatop_db;
CREATE USER 'chatop'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON chatop_db.* TO 'chatop'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```
Then, execute the provided SQL script:
```sh
mysql -u root -p chatop_db < 'path_to_the_script'/script.sql
```

#### 3️⃣ Create a Keystore for Security
**(Replace `your_key` and `your_password` with your values.)**
```sh
keytool -genkeypair -alias your_key -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -validity 3650 -keystore chatop.p12 -storetype PKCS12 -storepass "your_password" -keypass "your_password" -dname "CN=chatop-api, OU=IT, O=Acme, L=Paris, ST=IDF, C=FR"
```

#### 4️⃣ Configure Environment-Specific Properties

Inside `src/main/resources/`, create:
- `application-dev.properties`
- `application-prod.properties`

Example for **development (`application-dev.properties`)**:
```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/chatop_db
spring.datasource.username=your_value
spring.datasource.password=your_value

app.keystore.path=chatop.p12
app.keystore.alias=your_value
app.keystore.storepass=your_value
app.keystore.keypass=your_value

chatop.openapi.dev-url=http://localhost:3001
```

Example for **production (`application-prod.properties`)**:
```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/chatop_db
spring.datasource.username=your_value
spring.datasource.password=your_value

app.keystore.path=chatop.p12
app.keystore.alias=your_value
app.keystore.storepass=your_value
app.keystore.keypass=your_value

chatop.openapi.prod-url=your_value
```

---

### 🚀 Start the Application
```sh
mvn spring-boot:run
```
*(Make sure `application.properties` correctly loads the desired profile.)*

To force a **specific profile**, use:
```sh
mvn spring-boot:run --define spring-boot.run.arguments="--spring.profiles.active=prod" 
```
Or set an environment variable before running:
```sh
export SPRING_PROFILES_ACTIVE=dev && mvn spring-boot:run
```

---

## 📡 Available Endpoints
The API follows the specifications provided in the Postman collection.

### 🔑 **Authentication**
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

### 🏠 **Rentals**
- `GET /api/rentals`
- `GET /api/rentals/{id}`
- `POST /api/rentals`
- `PUT /api/rentals/{id}`

### 💬 **Messages**
- `POST /api/messages`

---

## 🔐 Security
All routes require authentication *(except signup and login)*.  
Authentication is done via **JWT**.  
Tokens must be included in the `Authorization` header of requests.

---

## 📷 Image Management
When creating a rental, an **image is required**.  
It is stored on the server, and its **URL is saved in the database**.

---

## 📖 API Documentation
After launching the project, Swagger is available at:
```
http://localhost:3001/swagger-ui.html
```

---

## 🛠️ Testing
The API can be tested with **Postman** by importing the provided collection:
```
rental.postman_collection.json
```

---

## 🤝 Contribution
Contributions are **welcome**!  
Fork the project and submit a **Pull Request (PR)**.

---

## 📝 License
This project is **licensed under the MIT License**.
