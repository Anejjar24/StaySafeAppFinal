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
5. [Getting Started](#getting-started)
6. [Contributing](#contributing)
7. [Future Improvements](#future-improvements)

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



### Prerequisites:
- Git
- Java Development Kit (JDK) 11+
- Maven
- MySQL
- Google Maps API key
## Contributors
- [Anejjar Ihssane](https://github.com/Anejjar24)
- [Bousensar Rajaa](https://github.com/rajaa52)
- [Lachgar Mohamed ](https://www.researchgate.net/profile/Mohamed-Lachgar)


