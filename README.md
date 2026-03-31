# Selenium Automation Framework

This project is a UI automation framework for web practice applications using:

- Java 17
- Selenium WebDriver
- TestNG
- Page Object Model (POM)
- Page Factory
- Data-Driven Testing with JSON
- Maven
- WebDriverManager

## Why this project helps your resume

This framework showcases the same skills highlighted in your resume:

- Reusable automation framework design
- Selenium + Java + TestNG implementation
- POM and Page Factory usage
- Data-driven testing approach
- Maven-based execution
- Clean separation of pages, tests, config, and utilities

## Current scope

- Automation Practice form submission coverage
- OrangeHRM login coverage already present in the project
- JSON-based test data provider
- Centralized browser and environment configuration
- Cross-browser execution for Chrome, Firefox, and Edge

## How to run the practice form suite

```bash
mvn clean test -DsuiteXmlFile=testng-practice-form.xml
```

## How to run the cross-browser suite

```bash
mvn clean test -DsuiteXmlFile=testng-practice-form-cross-browser.xml
```

## Demo applications

- `https://testautomationpractice.blogspot.com/2018/09/automation-form.html`
- `https://opensource-demo.orangehrmlive.com/`
