# StaySafe: Personal Security Mobile Application
<p align="center">
  <img src="https://github.com/user-attachments/assets/faf0eb52-aac5-446e-9426-116bf843828e" alt="app" width="400">
</p>


StaySafe is a mobile application designed to enhance personal security by providing real-time geolocation alerts and notifications about high-risk areas. The app integrates user-friendly technology with robust security features, enabling individuals to feel safer and stay connected with their loved ones during critical situations.

## Table of Contents
1. [Description](#description)
2. [Key Features](#key-features)
3. [Software Architecture](#software-architecture)
4. [Getting Started](#getting-started)
5. [Contributing](#contributing)
6. [Future Improvements](#future-improvements)

## Description
StaySafe empowers users to quickly send geolocation alerts to designated contacts in case of danger and receive real-time notifications about potentially hazardous zones. By leveraging geolocation technology, the app enhances personal safety and helps users keep their loved ones informed.

## Key Features
- **Real-Time Geolocation Alerts**: Instantly share your location with trusted contacts in emergencies.
- **Risk Zone Notifications**: Receive alerts about high-risk areas based on your current location.
- **User-Friendly Interface**: Intuitive and secure design for seamless user experience.

## Software Architecture
The StaySafe architecture employs a modern, modular approach to ensure scalability, maintainability, and robustness.
![diagram-export-29-12-2024-04_44_18](https://github.com/user-attachments/assets/acdc6564-fd30-40a9-ba15-1ff6493d4ef9)


### Technologies Used:
- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Spring Boot (Java)
- **Database**: MySQL for secure data storage
- **Geolocation Service**: Google Maps API for location tracking and notifications

### Architecture Overview
1. **Frontend**:
   - Built with HTML, CSS, and JavaScript for a responsive and user-friendly interface.
2. **Backend**:
   - Spring Boot handles API requests, user authentication, and alert notifications.
   - MySQL stores user data, alerts, and risk zone information.
3. **Third-Party Services**:
   - Google Maps API for geolocation and mapping.

## Getting Started
Follow these steps to set up and run the StaySafe application locally:

### Prerequisites:
- Git
- Java Development Kit (JDK) 11+
- Maven
- MySQL
- Google Maps API key

### Setup Instructions:

#### Clone the Repository:
```bash
git clone https://github.com/YourUsername/StaySafe.git
cd StaySafe
```

#### Backend Setup:
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Configure the application properties in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/staysafe
   spring.datasource.username=your_mysql_user
   spring.datasource.password=your_mysql_password
   google.maps.api.key=your_google_maps_api_key
   server.port=8080
   ```
4. Start the backend server:
   ```bash
   mvn spring-boot:run
   ```

#### Frontend Setup:
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Open the `index.html` file in your browser or host it on a local server for testing.

#### Database Setup:
1. Install and start MySQL on your system.
2. Create a database named `staysafe`.
3. Run the SQL scripts provided in the `database` folder to set up the necessary tables and data.

## Contributing
We welcome contributions from the community! To contribute:

1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature description"
   ```
4. Push to your forked repository:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request.

## Future Improvements
- **AI-Powered Risk Detection**: Use machine learning to predict and alert users about emerging risks.
- **Wearable Integration**: Add support for wearable devices to send alerts.
- **Multilingual Support**: Enable the app in multiple languages for wider accessibility.
- **Enhanced Security**: Implement end-to-end encryption for sensitive data.

---
StaySafe is your companion in ensuring safety and peace of mind. Together, we can build a safer world for everyone!
