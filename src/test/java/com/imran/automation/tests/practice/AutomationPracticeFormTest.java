package com.imran.automation.tests.practice;

import com.imran.automation.base.BaseTest;
import com.imran.automation.models.PracticeFormData;
import com.imran.automation.pages.practice.AutomationPracticeFormPage;
import com.imran.automation.utils.ConfigReader;
import com.imran.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class AutomationPracticeFormTest extends BaseTest {

    @DataProvider(name = "practiceFormData")
    public Object[][] practiceFormData() {
        return JsonDataReader.getPracticeFormTestData();
    }

    @Test(dataProvider = "practiceFormData", description = "Verify the mentor practice form accepts data and can be submitted across browsers.")
    public void verifyFormSubmission(PracticeFormData formData) {
        AutomationPracticeFormPage practiceFormPage = new AutomationPracticeFormPage(getDriver())
                .open(ConfigReader.get("practiceFormUrl"));

        Assert.assertTrue(practiceFormPage.isLoaded(), "Practice form page should be visible.");

        practiceFormPage.fillForm(formData);

        Assert.assertEquals(practiceFormPage.getEnteredName(), formData.getName(), "Name should match the entered value.");
        Assert.assertEquals(practiceFormPage.getEnteredEmail(), formData.getEmail(), "Email should match the entered value.");
        Assert.assertEquals(practiceFormPage.getEnteredPhone(), formData.getPhone(), "Phone should match the entered value.");
        Assert.assertEquals(normalizeLineBreaks(practiceFormPage.getEnteredAddress()), normalizeLineBreaks(formData.getAddress()),
                "Address should match the entered value.");
        Assert.assertTrue(practiceFormPage.isGenderSelected(formData.getGender()), "Selected gender option should remain checked.");
        Assert.assertTrue(practiceFormPage.areDaysSelected(formData.getDays()), "Requested day checkboxes should remain selected.");
        Assert.assertEquals(practiceFormPage.getSelectedCountry(), formData.getCountry(), "Country should match the selected option.");
        Assert.assertTrue(practiceFormPage.getSelectedColors().containsAll(formData.getColors()),
                "All requested colors should remain selected.");
        Assert.assertTrue(practiceFormPage.getSelectedAnimals().containsAll(formData.getAnimals()),
                "All requested animals should remain selected.");
        Assert.assertEquals(practiceFormPage.getDatePickerOneValue(), formData.getDatePicker1(),
                "Date Picker 1 should contain the requested date.");
        Assert.assertEquals(practiceFormPage.getDatePickerTwoValue(), formData.getDatePicker2(),
                "Date Picker 2 should contain the requested date.");
        Assert.assertEquals(practiceFormPage.getStartDateValue(), formData.getStartDate(),
                "Date range start date should contain the requested date.");
        Assert.assertEquals(practiceFormPage.getEndDateValue(), formData.getEndDate(),
                "Date range end date should contain the requested date.");

        practiceFormPage.submitForm();

        Assert.assertTrue(practiceFormPage.isSubmitButtonDisplayed(), "Submit button should still be visible after submission.");
        Assert.assertTrue(isSubmissionStateStable(practiceFormPage, formData),
                "Entered form data should stay stable after clicking submit.");
    }

    private boolean isSubmissionStateStable(AutomationPracticeFormPage practiceFormPage, PracticeFormData formData) {
        boolean noBlockingAlert = !practiceFormPage.isAlertPresent();
        if (!noBlockingAlert) {
            practiceFormPage.consumeAlertText();
        }

        return noBlockingAlert
                && formData.getName().equals(practiceFormPage.getEnteredName())
                && formData.getEmail().equals(practiceFormPage.getEnteredEmail())
                && formData.getPhone().equals(practiceFormPage.getEnteredPhone())
                && normalizeLineBreaks(formData.getAddress()).equals(normalizeLineBreaks(practiceFormPage.getEnteredAddress()))
                && formData.getCountry().equals(practiceFormPage.getSelectedCountry())
                && containsAll(practiceFormPage.getSelectedColors(), formData.getColors())
                && containsAll(practiceFormPage.getSelectedAnimals(), formData.getAnimals());
    }

    private boolean containsAll(List<String> actualValues, List<String> expectedValues) {
        return actualValues.containsAll(expectedValues);
    }

    private String normalizeLineBreaks(String value) {
        return value == null ? "" : value.replace("\r\n", "\n").trim();
    }
}
