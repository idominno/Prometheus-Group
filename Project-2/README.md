# Selenium Web Automation Framework

A Java-based testing framework for web automation, built with JUnit 5 and Gradle.

## Technology Stack

- **Java 21**
- **Gradle 8.4**
- **Selenium WebDriver 4.27.0** (Latest)
- **WebDriverManager 6.2.0** (Latest)
- **JUnit 5.10.1**
- **Log4j 2.21.1**

## Requirements 

- Step 1: Navigate to Google (COMPLETE)
- Step 2: Search for "Prometheus Group" (COMPLETE)
- Step 3: Verify search results contain "Prometheus Group" (COMPLETE)
- Step 4: Click on Careers link and validate 4 accordion dropdowns (NOT COMPLETE)
- Step 5: Click "View All Prometheus Jobs" (NOT COMPLETE)
- Step 6: Validate Senior Software Developer in Test (SDET) posting (NOT COMPLETE)


## Project Structure

```
├── build.gradle                      
├── gradle/wrapper/                  
│   ├── gradle-wrapper.jar            
│   └── gradle-wrapper.properties     
├── gradlew                          
├── logs/                            
│   └── selenium-tests.log            
├── test/                            
│   ├── BaseTest.java                
│   ├── GoogleSearchTest.java        
│   └── log4j2.xml                   
└── README.md                        
```

## Build Commands

### Build the Project
```bash
./gradlew build
```
Compiles code, runs all tests, and validates project structure

### Clean Build Artifacts
```bash
./gradlew clean
```
Removes all generated files and build directories

### Run Tests Only
```bash
./gradlew test
```
Executes test suite with detailed logging

### Compile Tests Only
```bash
./gradlew compileTestJava
```
Compiles test classes without execution

### Run with Verbose Output
```bash
./gradlew test --info
```
Provides detailed build and test execution information

## Test Results and Reports

### Test Result Locations

#### HTML Test Reports
- Test Results: `/build/reports/tests/test/index.html`

#### XML Test Reports
- Test Results: `/build/test-results/test/`

#### Log Files
- Structured Logs: `/logs/selenium-tests.log`
- Console Output: Real-time logging during test execution
