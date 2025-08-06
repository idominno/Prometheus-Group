# REST API Testing Framework

A Java-based testing framework for REST API validation, built with JUnit 5 and Gradle. Designed to test the JSONPlaceholder REST API with some CRUD operation coverage.

## Technology Stack

- **Java 21**
- **Gradle**
- **Apache HttpClient 5.x**
- **Jackson**
- **SLF4J with Log4j2**

## Requirements Met

- **Config-driven URLs and Headers**: Simple properties file configuration management
- **Clear Assertions**: JUnit assertions for straightforward test validation
- **Data-Driven Test Methods**: Tests read from external JSON files for maintainability
- **Modular Test Organization**: Tests organized by API endpoint (posts/, users/, todos/)
- **External Configuration**: Properties file for endpoints and status codes, JSON for test data
- **Comprehensive Logging**: Request/response logging
- **Wrapper Class for API Client** - Centralized HTTP client wrapper with automatic header management
- **27 tests** - 9 GET (3 negative), 6 POST (3 negative), 6 PUT (3 negative), 6 DELETE (3 negative)

## Requirements Not Met

- **Data Transfer Objects (DTOs)** - Not implemented for request/response handling; manual JSON parsing using Jackson JsonNode instead
- **C# Project** - Project is Java-based

## Considerations

- **No setup and teardown routines** - Given the nature of the JSONPlaceholder REST API's "fake" updating of resources, I did not implement test data setup and teardown routines but should be considered for real APIs

## Project Structure

```
├── build.gradle                      
├── gradle/wrapper/                   
├── gradlew                          
├── logs/                            
├── src/
│   ├── main/java/
│   │   ├── client/
│   │   │   └── ApiClient.java     
│   │   └── config/
│   │       └── ApiConfiguration.java
│   └── test/
│       ├── java/
│       │   ├── posts/  
│       │   │   ├── GetTests.java
│       │   │   ├── PostTests.java
│       │   │   ├── PutTests.java
│       │   │   └── DeleteTests.java
│       │   ├── users/
│       │   │   ├── GetTests.java
│       │   │   ├── PostTests.java
│       │   │   ├── PutTests.java
│       │   │   └── DeleteTests.java
│       │   ├── todos/
│       │   │   ├── GetTests.java
│       │   │   ├── PostTests.java
│       │   │   ├── PutTests.java
│       │   │   └── DeleteTests.java
│       │   └── util/
│       │       └── TestDataLoader.java
│       └── resources/
│           ├── config.properties
│           ├── test.properties
│           ├── log4j2.xml
│           └── testdata/
│               ├── posts-create.json
│               ├── posts-update.json
│               ├── posts-create-invalid.json
│               ├── posts-update-invalid.json
│               ├── users-create.json
│               ├── users-update.json
│               ├── users-create-invalid.json
│               ├── users-update-invalid.json
│               ├── todos-create.json
│               ├── todos-update.json
│               ├── todos-create-invalid.json
│               └── todos-update-invalid.json
```

## Build Commands

### Build the Project
```bash
./gradlew build
```
Compiles code, runs all tests, generates reports, and creates JAR artifacts

### Clean Build Artifacts
```bash
./gradlew clean
```
Removes all generated files and build directories

### Run Tests Only
```bash
./gradlew test
```
Executes all test suites and generates test reports

### Run Specific Test Categories
```bash
# Run tests for specific endpoints
./gradlew test --tests "posts.*"
./gradlew test --tests "users.*"
./gradlew test --tests "todos.*"

# Run tests by HTTP method across all endpoints
./gradlew test --tests "*GetTests"
./gradlew test --tests "*PostTests"
./gradlew test --tests "*PutTests"
./gradlew test --tests "*DeleteTests"
```

### Generate Code Coverage Report
```bash
./gradlew jacocoTestReport
```
Runs tests and generates detailed code coverage metrics

## Test Results and Reports

### Test Result Locations

#### HTML Test Reports
- All Tests: `/build/reports/tests/test/index.html`

#### XML Test Reports
- All Tests: `/build/test-results/test/`

#### Code Coverage Reports
- HTML Coverage Report: `/build/jacoco/test/html/index.html`
- XML Coverage Report: `/build/jacoco/test/jacocoTestReport.xml`

#### Log Files
- Test Execution Logs: `/logs/api-test.log`
- Console Output: Real-time logging during test execution
