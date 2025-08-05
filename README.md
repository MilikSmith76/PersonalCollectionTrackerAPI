# PersonalCollectionTrackerAPI

A Spring Boot GraphQL API for managing records of collectable inventory.

## Features

- Manage collectable items (CRUD operations)

- GraphQL endpoint for flexible queries and mutations

- SQL database integration

- Built with Java and Gradle

## Getting Started

### Prerequisites

- Java 24.0.1+

- Gradle 8.14.3+

- SQL database (MySQL)

### Setup

1. Clone the repository

2. Copy content of [application.properties.example](`src/main/resources/application.properties.example`) to `src/main/resources/application.properties`.

3. Configure your database credentials in `application.properties`.

4. Build the application: `./gradlew build`

5. Run the application: `./gradlew bootRun`

6. Access the GraphQL endpoint at `http://localhost:8080/graphql`.

### Usage

- Use GraphQL queries and mutations to manage your collection.

### Linting

- Check linting with: `./gradlew SpotlessCheck`

- Apply linting fixes: `./gradlew SpotlessApply`

### Testing

- Run tests: `./gradlew test`

## Managing Database

This application uses a MySQL database to store collectable items and additional information.
A connection to the database is required for the application to function properly.

### Database Configuration

- Ensure your MySQL server is running.

- Update the `application.properties` file with your database connection details.

### Database Schema

The database schema is managed by Flyway migrations.
The schema is defined by migration files in the [migration](`src/main/resources/db/migration`) directory.

### Running Migrations

- Run migration: `./gradlew flywayMigrate`

### Docker Database

If an existing database is not going to be used, a docker database is provided by this project.

- Run docker database: `docker compose up -d`

## GraphQL

### GraphQL Schema

The GraphQL schemas can be found in the [schema](`src/main/resources/schema`) directory.
These schemas include input types, queries, and mutations.

- The schemas for queries can be found in [query.graphql](`src/main/resources/schema/query.graphql`).

- The schemas for mutations can be found in [mutation.graphql](`src/main/resources/schema/mutation.graphql`).

### GraphQL Request Handling

GraphQL requests are handled by the respective Data Fetcher in the [dataFetchers](`src/main/java/app/dataFetchers`) directory.

