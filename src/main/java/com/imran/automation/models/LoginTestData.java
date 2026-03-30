package com.imran.automation.models;

public class LoginTestData {

    private String testName;
    private String scenarioType;
    private String username;
    private String password;
    private String expectedUrlKeyword;
    private String expectedErrorMessage;

    public String getTestName() {
        return testName;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExpectedUrlKeyword() {
        return expectedUrlKeyword;
    }

    public String getExpectedErrorMessage() {
        return expectedErrorMessage;
    }
}
