# StaySafe: Personal Security Mobile Application
<p align="center">
  <img src="https://github.com/user-attachments/assets/faf0eb52-aac5-446e-9426-116bf843828e" alt="app" width="400">
</p>

StaySafe is a mobile application designed to enhance personal security by providing real-time geolocation alerts and notifications about high-risk areas. The app integrates user-friendly technology with robust security features, enabling individuals to feel safer and stay connected with their loved ones during critical situations.

## Table of Contents
1. [Description](#description)
2. [Key Features](#key-features)
3. [Software Architecture](#software-architecture)
4. [Backend Project Structure](#backend-project-structure)
5. [Prerequisites](#prerequisites)
6. [Docker Image](#docker-image)
7. [Demonstration Video](#demonstration-video)
8. [Contributors](#contributors)

## Description
StaySafe empowers users to quickly send geolocation alerts to designated contacts in case of danger and receive real-time notifications about potentially hazardous zones. By leveraging geolocation technology, the app enhances personal safety and helps users keep their loved ones informed.

## Key Features
- **Real-Time Geolocation Alerts**: Instantly share your location with trusted contacts in emergencies.
- **Risk Zone Notifications**: Receive alerts about high-risk areas based on your current location.
- **User-Friendly Interface**: Intuitive and secure design for a seamless user experience.

## Software Architecture
The StaySafe architecture employs a modern, modular approach to ensure scalability, maintainability, and robustness.

<p align="center">
  <img src="https://github.com/user-attachments/assets/acdc6564-fd30-40a9-ba15-1ff6493d4ef9" alt="app" width="700">
</p>

The application architecture consists of:

- **Frontend**: Developed using Android (Java/Kotlin).
- **Backend**: Built with Spring Boot, communicating with the frontend through RESTful APIs.
- **Third-Party Services**: Google Maps API for geolocation and mapping.

## Backend Project Structure

The backend code follows a modular and organized structure, leveraging the power of Spring Boot for building a robust and scalable application.

### 1. `com.example.demo`
**Main Application Class**: `DemoApplication.java` serves as the entry point for the Spring Boot application. It includes the main method to bootstrap and start the application.

### 2. `com.example.demo.controller`
**Controller Layer**: This package contains classes responsible for handling incoming HTTP requests. Each controller defines RESTful endpoints for specific features or entities and delegates the request processing to the service layer.

### 3. `com.example.demo.model`
**Entity Layer**: The model package includes classes representing data entities in the application. These classes use JPA annotations to define the structure of the corresponding database tables, ensuring seamless ORM mapping.

### 4. `com.example.demo.repository`
**Repository Layer**: This package contains interfaces extending Spring Data JPA repository interfaces. These provide built-in methods for CRUD operations and enable interaction with the database without requiring boilerplate code.

### 5. `com.example.demo.security`
**Security Configuration**: The security package includes classes for configuring authentication and authorization mechanisms. This might involve defining roles, managing user credentials, and securing endpoints based on roles or permissions.

### 6. `com.example.demo.service`
**Service Layer**: This package contains business logic for the application. Services interact with the repository layer to fetch or modify data and provide processed information to the controller layer.

## Docker Image
```sh
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_staysafe
    environment:
      MYSQL_DATABASE: staysafe_db
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_USER: root
      MYSQL_PASSWORD:
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - staysafe_network

  spring_app:
    build:
      context: ./Backend
      dockerfile: Dockerfile
    container_name: staysafe_backend
    depends_on:
      - mysql
    ports:
      - "8082:8082"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/staysafe_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_JPA_SHOW_SQL: true
      SERVER_PORT: 8082
      JWT_SECRET: dGhpc2lzYXZlcnlzZWN1cmVhbmRzYWZlc2VjcmV0a2V5MTIzIQ==
      JWT_VALIDITY: 3600000
      SPRING_MAIL_HOST: smtp.gmail.com
      SPRING_MAIL_PORT: 587
      SPRING_MAIL_USERNAME: anejjarihssane@gmail.com
      SPRING_MAIL_PASSWORD: vbek rbhl etka lyvd
      SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH: true
      SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE: true
    networks:
      - staysafe_network

  android_build:
    build:
      context: ./FrontEnd
      dockerfile: Dockerfile
      args:
        - MAPS_API_KEY=AIzaSyBeju7DcjbThPgbS9gYxNGvj7_KoVesVQ0
    volumes:
      - android_build:/app/app/build/outputs/apk
    networks:
      - staysafe_network

volumes:
  mysql_data:
  android_build:

networks:
  staysafe_network:
    driver: bridge

```


## Prerequisites:
- Git
- Java Development Kit (JDK) 11+
- Maven
- MySQL
- Google Maps API key
## Demonstration Video
part 1:
part 2:

https://github.com/user-attachments/assets/01940d43-d3df-4cb4-a1db-442bd46ece29

## Contributors
- [Anejjar Ihssane](https://github.com/Anejjar24)
- [Bousensar Rajaa](https://github.com/rajaa52)
- [Lachgar Mohamed ](https://www.researchgate.net/profile/Mohamed-Lachgar)


