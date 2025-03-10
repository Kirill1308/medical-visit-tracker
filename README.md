# Healthcare Visit Tracker

A Spring Boot application for tracking and managing healthcare visits between doctors and patients.

## Features

- Schedule visits between doctors and patients
- Prevent scheduling conflicts (overlapping appointments)
- Support for different timezones
- RESTful API

## Technologies

- Java 17
- Spring Boot 3.4.3
- MySQL Database
- Flyway for database migrations
- Maven build system
- TestContainers for integration testing

## Getting Started

### Prerequisites

- JDK 17 or later
- Docker (for running MySQL in container)
- Maven

### Setup

1. Clone the repository
2. Run docker-compose file
3. Run the application

## Projectt Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/healthcare/visittracker/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── db/migration/    # Flyway migration scripts
│   │       └── application.yml  # Application configuration
│   └── test/
│       ├── java/com/healthcare/visittracker/
│       │   ├── controller/      # Controller tests
│       │   ├── BaseTest.java    # Base test configuration
│       │   ├── TestConfig.java  # Test configuration
│       │   └── TestDataFactory.java  # Test data generation
│       └── resources/
│           ├── application-test.yml  # Test configuration
│           └── init_script.sql       # Test database initialization
├── .env                         # Environment variables
├── docker-compose.yml           # Docker services configuration
└── README.md                    # Project documentation
```

## Database Schema

The application uses the following tables:
- `doctors` - Stores information about healthcare providers
- `patients` - Stores information about patients
- `visits` - Tracks appointments between doctors and patients
