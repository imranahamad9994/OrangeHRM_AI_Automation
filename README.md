# OrangeHRM AI Automation Framework

Enterprise-style UI automation framework built with Java, Selenium WebDriver, TestNG, Page Object Model, Page Factory, JSON-driven test data, Extent reporting, and cross-browser execution. This project demonstrates how modern QA automation can be designed as a scalable test engineering system rather than a collection of standalone scripts.

## Recruiter Snapshot

- Built for real-world web application automation using reusable framework design
- Covers authentication, dashboard validation, logout, invalid login handling, and PIM employee workflows
- Uses data-driven architecture for maintainable test expansion
- Supports headed and headless execution through configuration
- Includes Extent reporting, screenshot-on-failure support, and console-level visibility logs
- Designed for CI/CD execution through Maven and Jenkins-friendly plugin configuration
- Demonstrates AI-assisted test engineering workflow for faster framework design, cleaner abstraction, and stronger maintainability

## Why this project stands out

This repository reflects the kind of work expected from a QA Automation Engineer or SDET working on production-grade test automation:

- Modular automation architecture with clean separation of pages, tests, models, listeners, and utilities
- Resume-aligned stack: Selenium, Java, TestNG, Maven, Page Object Model, Data Driven Framework, Extent Reports, Jenkins-ready execution
- Business-flow validation, not just toy assertions
- Extensible design for adding more OrangeHRM modules like Leave, Admin, Recruitment, and Performance
- AI-assisted implementation mindset focused on productivity, test design quality, and framework scalability

## Technology Stack

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- WebDriverManager
- Page Object Model (POM)
- Page Factory
- JSON Data Provider
- Extent Reports
- Cross-browser support for Chrome, Firefox, and Edge

## Current Functional Coverage

### OrangeHRM

- Valid login
- Invalid login validation
- Dashboard UI visibility validation
- Logout flow
- PIM module navigation
- Add Employee
- Search Employee in Employee List

### Practice Form

- End-to-end form submission validation
- Field-level value verification
- Date validation with browser-compatible normalization

## Framework Capabilities

- Config-driven browser and headless execution
- TestNG parameterization for cross-browser runs
- Extent report generation with automatic open after successful execution
- Screenshot capture on failure using `TakesScreenshot`
- Dashboard-level console and report step logging
- Unique test data generation for employee creation workflows
- Jenkins-friendly Maven plugin setup

## Project Structure

```text
src/main/java/com/imran/automation
|-- constants
|-- factory
|-- models
|-- pages
|   |-- auth
|   |-- dashboard
|   |-- pim
|   |-- practice
|-- utils

src/test/java/com/imran/automation
|-- base
|-- listeners
|-- tests
|   |-- auth
|   |-- pim
|   |-- practice
|-- utils
```

## Configuration

Main execution settings live in:

[`config.properties`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/config/config.properties)

Example:

```properties
baseUrl=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login
practiceFormUrl=https://testautomationpractice.blogspot.com/2018/09/automation-form.html
browser=chrome
headless=false
```

## Test Data

JSON-driven data is stored under:

- [`login-data.json`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/testdata/login-data.json)
- [`pim-employee-data.json`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/testdata/pim-employee-data.json)
- [`practice-form-data.json`](C:/Users/Hp/OneDrive/Documents/New%20project/src/test/resources/testdata/practice-form-data.json)

## How to Run

### Run main OrangeHRM suite

```bash
mvn clean test
```

### Run only dashboard visibility test in headed mode

```bash
mvn clean test "-Dtest=com.imran.automation.tests.auth.LoginTest#verifyDashboardElementsVisible" "-Dheadless=false"
```

### Run PIM test only

```bash
mvn clean test "-Dtest=com.imran.automation.tests.pim.PimTest" "-Dheadless=false"
```

### Run cross-browser suite

```bash
mvn clean test "-DsuiteXmlFile=testngCrossBrowser.xml"
```

### Run practice form suite

```bash
mvn clean test "-DsuiteXmlFile=testng-practice-form.xml"
```

## One-Click Windows Launchers

- [`run-orangehrm-tests.bat`](C:/Users/Hp/OneDrive/Documents/New%20project/run-orangehrm-tests.bat)
- [`run-practice-form-tests.bat`](C:/Users/Hp/OneDrive/Documents/New%20project/run-practice-form-tests.bat)

These allow the framework to be executed by double-clicking a batch file without manually opening a terminal.

## Reporting

Extent HTML report is generated at:

[`target/extent-reports/extent-report.html`](C:/Users/Hp/OneDrive/Documents/New%20project/target/extent-reports/extent-report.html)

Report features include:

- Test-level pass/fail reporting
- Per-step dashboard validation logs
- Screenshot attachments on failure
- Local auto-open after successful execution

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

This project was developed with an AI-assisted engineering approach focused on:

- Faster framework scaffolding
- Cleaner page abstraction and reusable utilities
- Better test design iteration
- Improved debugging through structured reporting and targeted feedback
- Accelerated feature expansion from auth coverage into business-module automation

This is not presented as “AI replacing testing.” It demonstrates the more practical and recruiter-relevant story: using AI as a force multiplier to build automation frameworks faster, cleaner, and with stronger engineering discipline.

## Demo Applications

- [OrangeHRM Demo](https://opensource-demo.orangehrmlive.com/)
- [Test Automation Practice Form](https://testautomationpractice.blogspot.com/2018/09/automation-form.html)

## Next Expansion Ideas

- Edit Employee in PIM
- Delete Employee in PIM
- Leave module automation
- Admin user management flows
- Parallel cross-browser execution
- GitHub Actions or Jenkins pipeline as code
- Allure or advanced reporting integration

## Ideal Use Case

This project is a strong portfolio asset for roles such as:

- QA Automation Engineer
- SDET
- Test Automation Engineer
- Quality Engineer
- Software Development Engineer in Test

It demonstrates both hands-on automation implementation and framework-level engineering thinking.
