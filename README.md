# Movie Database API

This project is a RESTful API for managing a movie database, including entities like movies, actors, and genres. Built with Spring Boot, it allows users to create, update, delete, and fetch information about movies, actors, and genres. The API supports input validation, error handling, and uses a relational database for data persistence.

## Setup and Installation Instructions

To set up and run the Movies API locally, follow these steps:

### Prerequisites

- **Java Development Kit (JDK) 21**: Ensure you have JDK 21 installed on your machine. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html) or use a package manager like Homebrew (for macOS) or SDKMAN (for Linux).

- **Maven**: This project uses Maven for dependency management. You can download Maven from [Apache's official website](https://maven.apache.org/download.cgi) or use a package manager like Homebrew or Chocolatey.

- **SQLite**: Ensure that you have SQLite installed on your machine, as it is used as the database for this project.

- **Postman**: To test, install Postman. It's a popular API client that allows you to test and interact with API endpoints easily.
- **An IDE**: Presence of an IDE helps to build and run the program (optional).

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
- [Error Handling](#error-handling)
- [Testing](#testing)
- [Notes](#notes)

## Features

- **Movies**: Create, update, fetch, and delete movies, with support for managing associated actors and genres.
- **Actors**: Manage actors, including creating, updating, deleting, and listing movies they appear in.
- **Genres**: Manage genres with options to link movies to genres.
- **Error Handling**: Custom error handling using `@ControllerAdvice` and `@ExceptionHandler`.
- **Validation**: Input validation using Bean Validation annotations.

## Technologies Used

- **Java**: Language used for API development.
- **Spring Boot**: Framework for building and running the API.
- **Spring Data JPA**: For database interactions.
- **H2/SQLite/MySQL**: Relational database for data persistence.
- **Postman**: For testing and API collections.

## Setup

1. **Clone the Repository**
   ```bash
    git clone https://github.com/Pablo9298/kmdb.git
   ```
   
    ```bash
    cd kmdb
    ```

2. **Configure the Database**

- Configure the application.properties file to set up your database connection (e.g., MySQL, SQLite, H2).

3. **Run the Application**

    ```bash
   ./mvnw spring-boot:run
   ```
- The API will be available at http://localhost:8080.

## API Endpoints

### Actors

- **Create Actor**: `POST /api/actors`
- **Get All Actors**: `GET /api/actors`
- **Get Actor by ID**: `GET /api/actors/{id}`
- **Update Actor**: `PATCH /api/actors/{id}`
- **Delete Actor**: `DELETE /api/actors/{id}` (with optional `force` parameter to override restrictions)

### Movies

- **Create Movie**: `POST /api/movies`
- **Get All Movies**: `GET /api/movies`
- **Get Movie by ID**: `GET /api/movies/{id}`
- **Update Movie**: `PATCH /api/movies/{id}` (can update actors and genres)
- **Delete Movie**: `DELETE /api/movies/{id}`

### Genres

- **Create Genre**: `POST /api/genres`
- **Get All Genres**: `GET /api/genres`
- **Get Genre by ID**: `GET /api/genres/{id}`
- **Update Genre**: `PATCH /api/genres/{id}`
- **Delete Genre**: `DELETE /api/genres/{id}` (with optional `force` parameter to override restrictions)

### Special Queries
- **Find Movies by Genre**: `GET /api/movies?genre={genreId}`
- **Find Movies by Actor**: `GET /api/movies?actor={actorId}`
- **Find Movies by Year**: `GET /api/movies?year={year}`
- **Find Actors by Name**: `GET /api/actors?name={actorName}`

## Error Handling

Errors are managed globally through GlobalExceptionHandler using @ControllerAdvice. Common errors:

- **404 Not Found**: Returned when a requested resource (e.g., actor, movie, genre) does not exist.
- **400 Bad Request**: For validation errors or incorrect input format.
- **409 Conflict**: When attempting to delete a resource with existing relationships without using `force=true`.
- **500 Internal Server Error**: Catches any unexpected server errors.

## Testing

Use [Postman](https://www.postman.com/downloads/) to test API requests. A Postman collection named "Movie Database API" exists with organized requests for easy testing. The collection includes:

- Basic CRUD operations for Movies, Actors, and Genres.
- Validation for adding and removing actors in a movie.
- Input validation tests to ensure correct response codes and messages.

## Notes
- **Input Validation**: Bean Validation annotations (e.g., `@NotNull`, `@Size`, `@Pattern`) ensure data integrity. Validation errors are caught and formatted by the global exception handler.
- **Database Relationships**: Movies can be associated with multiple actors and genres. Deleting an entity will check for relationships, and deletion is restricted unless `force=true` is used.
- **Status Codes**: The API returns appropriate HTTP status codes (`200 OK`, `201 Created`, `204 No Content`, etc.) for each operation, along with meaningful error messages.

