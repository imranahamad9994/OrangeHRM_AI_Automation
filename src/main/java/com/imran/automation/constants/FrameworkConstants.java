package com.imran.automation.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    public static final String LOGIN_TEST_DATA_PATH = "src/test/resources/testdata/login-data.json";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    public static final String EXTENT_REPORT_PATH = "target/extent-reports/extent-report.html";
    public static final String SCREENSHOT_DIRECTORY = "target/extent-reports/screenshots";
    public static final int EXPLICIT_WAIT_SECONDS = 10;
}
