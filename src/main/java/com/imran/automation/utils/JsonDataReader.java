package com.imran.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imran.automation.constants.FrameworkConstants;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.models.PimEmployeeData;
import com.imran.automation.models.PracticeFormData;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public final class JsonDataReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {
    }

    public static Object[][] getValidLoginTestData() {
        return convertToDataProvider(getLoginTestDataByType("valid"));
    }

    public static Object[][] getInvalidLoginTestData() {
        return convertToDataProvider(getLoginTestDataByType("invalid"));
    }

    public static Object[][] getPracticeFormTestData() {
        try {
            List<PracticeFormData> records = OBJECT_MAPPER.readerForListOf(PracticeFormData.class)
                    .readValue(new File(FrameworkConstants.PRACTICE_FORM_TEST_DATA_PATH));
            return convertPracticeFormDataToDataProvider(records);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read practice form test data.", exception);
        }
    }

    public static Object[][] getPimEmployeeTestData() {
        try {
            List<PimEmployeeData> records = OBJECT_MAPPER.readerForListOf(PimEmployeeData.class)
                    .readValue(new File(FrameworkConstants.PIM_EMPLOYEE_TEST_DATA_PATH));
            return convertPimEmployeeDataToDataProvider(records);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read PIM employee test data.", exception);
        }
    }

    private static List<LoginTestData> getLoginTestDataByType(String scenarioType) {
        try {
            List<LoginTestData> records = OBJECT_MAPPER.readerForListOf(LoginTestData.class)
                    .readValue(new File(FrameworkConstants.LOGIN_TEST_DATA_PATH));
            return records.stream()
                    .filter(record -> scenarioType.equalsIgnoreCase(record.getScenarioType()))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read login test data.", exception);
        }
    }

    private static Object[][] convertToDataProvider(List<LoginTestData> records) {
        Object[][] data = new Object[records.size()][1];
        for (int index = 0; index < records.size(); index++) {
            data[index][0] = records.get(index);
        }
        return data;
    }

    private static Object[][] convertPracticeFormDataToDataProvider(List<PracticeFormData> records) {
        Object[][] data = new Object[records.size()][1];
        for (int index = 0; index < records.size(); index++) {
            data[index][0] = records.get(index);
        }
        return data;
    }

    private static Object[][] convertPimEmployeeDataToDataProvider(List<PimEmployeeData> records) {
        Object[][] data = new Object[records.size()][1];
        for (int index = 0; index < records.size(); index++) {
            data[index][0] = records.get(index);
        }
        return data;
    }
}
