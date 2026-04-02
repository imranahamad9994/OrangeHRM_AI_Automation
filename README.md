# OrangeHRM AI Automation Framework

Enterprise-style UI automation framework built with Java, Selenium WebDriver, TestNG, Page Object Model, Page Factory, Faker-powered dynamic test data, Extent reporting, and Jenkins-ready Maven execution. This project is designed to showcase scalable test automation engineering for a real HR web application, not just isolated Selenium scripts.

## Recruiter Snapshot

- Built as a modular, production-style automation framework using Java, Selenium, TestNG, Maven, POM, and Page Factory
- Covers business-critical OrangeHRM flows across Authentication, Dashboard, PIM, and Admin modules
- Includes full lifecycle validation for employee management and system user management
- Uses AI-assisted engineering patterns to accelerate framework design, debugging, and maintainable test expansion
- Supports headed and headless execution through configuration
- Includes Extent reporting, screenshot-on-failure support, structured logs, and one-click Windows execution
- Prepared for CI/CD execution with Jenkins-friendly Maven plugin configuration

## Why This Project Stands Out

This repository reflects the kind of framework thinking expected from a QA Automation Engineer or SDET working on enterprise applications:

- Clean separation of concerns across pages, tests, models, listeners, utilities, and configuration
- Strong resume-aligned stack: Selenium, Java, TestNG, Maven, POM, Data Driven Framework, Extent Reports, Jenkins readiness
- Real business workflow automation instead of only basic login assertions
- Dynamic test data generation using Faker for more realistic and repeatable lifecycle scenarios
- AI-assisted implementation approach focused on speed, clarity, reusability, and debugging efficiency

## Technology Stack

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- WebDriverManager
- Page Object Model (POM)
- Page Factory
- Jackson JSON data handling
- Java Faker
- Extent Reports
- Log4j2
- Cross-browser support for Chrome, Firefox, and Edge

## Current Functional Coverage

### Authentication and Dashboard

- Valid login
- Invalid login validation
- Dashboard UI visibility validation
- Logout flow
- Console and Extent step logging for dashboard element checks

### PIM Module

- Add random employee
- Search employee by employee ID
- Edit employee details with new Faker-generated values
- Delete employee
- End-to-end Add + Edit + Delete employee lifecycle validation

### Admin Module

- Create random employee as a dependency for Admin user mapping
- Add random system user linked to that employee
- Search system user by username
- Edit system user role, status, and username
- Delete system user
- End-to-end Add + Edit + Delete system user lifecycle validation

## Framework Capabilities

- Config-driven browser and headed/headless execution
- TestNG parameterization for browser selection
- Extent report generation with a single auto-open action after full suite completion
- Screenshot capture on failure using `TakesScreenshot`
- Log4j2 logging with file output in the `logs` folder
- Faker-based dynamic test data generation for lifecycle workflows
- One-click Windows batch execution
- Jenkins-friendly Maven plugin setup

## Project Structure

```text
src/main/java/com/imran/automation
|-- constants
|-- factory
|-- models
|-- pages
|   |-- admin
|   |-- auth
|   |-- base
|   |-- dashboard
|   |-- leave
|   |-- pim
|-- utils

src/test/java/com/imran/automation
|-- base
|-- listeners
|-- tests
|   |-- admin
|   |-- auth
|   |-- leave
|   |-- pim
|-- utils
```

## Configuration

Main execution settings live in:

[`config.properties`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/config/config.properties)

Example:

```properties
baseUrl=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
browser=chrome
headless=false
```

## Test Data

Structured JSON test data is stored under:

- [`login-data.json`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/testdata/login-data.json)
- [`pim-employee-data.json`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/testdata/pim-employee-data.json)

Dynamic lifecycle data such as employee names and admin usernames is generated at runtime with Faker to keep the flows unique and portfolio-realistic.

## How To Run

### Run the main OrangeHRM regression suite

```bash
mvn clean test
```

### Run the full suite in headed mode

```bash
mvn clean test "-Dheadless=false"
```

### Run only the dashboard visibility test

```bash
mvn clean test "-Dtest=com.imran.automation.tests.auth.LoginTest#verifyDashboardElementsVisible" "-Dheadless=false"
```

### Run only the PIM employee lifecycle test

```bash
mvn clean test "-Dtest=com.imran.automation.tests.pim.PimTest#verifyAddEditAndDeleteEmployeeLifecycle" "-Dheadless=false"
```

### Run only the Admin user lifecycle test

```bash
mvn clean test "-Dtest=com.imran.automation.tests.admin.AdminTest#verifyAddEditAndDeleteUserLifecycle" "-Dheadless=false"
```

### Run the cross-browser suite

```bash
mvn clean test "-DsuiteXmlFile=testngCrossBrowser.xml"
```

## Active Main Suite

The current main TestNG regression suite includes:

- Login Tests
- PIM Tests
- Admin Tests

Leave automation remains in the codebase for future stabilization, but it is temporarily excluded from the main suite to keep regular regression runs reliable.

## One-Click Windows Launcher

- [`run-orangehrm-tests.bat`](C:/Users/Hp/OneDrive/Documents/New%20project/run-orangehrm-tests.bat)

This allows the framework to be executed by double-clicking a batch file without manually opening a terminal.

## Reporting and Logs

Extent HTML report is generated at:

[`target/extent-reports/extent-report.html`](C:/Users/Hp/OneDrive/Documents/New%20project/target/extent-reports/extent-report.html)

Framework execution logs are written to:

[`logs/automation.log`](C:/Users/Hp/OneDrive/Documents/New%20project/logs/automation.log)

Current reporting features include:

- Test-level pass/fail reporting
- Dashboard element pass/fail step logs
- Screenshot attachments on failure
- Single report auto-open at the end of the full suite
- Debug-friendly file logging for driver lifecycle and module workflows

## CI/CD Readiness

This framework is prepared for CI execution through Maven and can be integrated easily with Jenkins pipelines.

Example Jenkins command:

```bash
mvn clean test -Dheadless=true
```

Cross-browser CI command:

```bash
mvn clean test "-DsuiteXmlFile=testngCrossBrowser.xml" -Dheadless=true
```

## AI-Assisted Engineering Value

This project was built using an AI-assisted engineering workflow focused on:

- rapid framework scaffolding
- cleaner abstraction of page objects and utilities
- faster debugging through structured diagnostics
- better iteration on selectors and workflow design
- accelerated expansion from auth testing into business-module lifecycle automation

This project positions AI as an engineering force multiplier, helping produce cleaner and more maintainable automation faster, while still keeping the framework grounded in solid QA and SDET practices.

## Demo Application

- [OrangeHRM Demo](https://opensource-demo.orangehrmlive.com/)

## Next Expansion Ideas

- Stabilize and re-enable Leave module flows in the main suite
- Add Admin negative validations and duplicate-user checks
- Add PIM personal details assertions after edit
- Parallel cross-browser execution
- Add Jenkinsfile or GitHub Actions pipeline
- Add Allure or advanced dashboard reporting

## Ideal Use Case

This project is a strong portfolio asset for roles such as:

- QA Automation Engineer
- SDET
- Test Automation Engineer
- Quality Engineer
- Software Development Engineer in Test

It demonstrates both hands-on automation implementation and framework-level engineering thinking.

