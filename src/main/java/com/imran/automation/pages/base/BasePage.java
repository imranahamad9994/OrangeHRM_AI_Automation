package com.imran.automation.pages.base;

import com.imran.automation.constants.FrameworkConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    protected void type(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        element.sendKeys(value);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void clickUsingJs(WebElement element) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", visibleElement);
    }

    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    protected void setValueUsingJs(WebElement element, String value) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", visibleElement, value);
    }

    protected void selectByVisibleText(WebElement element, String visibleText) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOf(element)));
        select.selectByVisibleText(visibleText);
    }

    protected void selectMultipleByVisibleText(WebElement element, List<String> visibleTexts) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOf(element)));
        if (select.isMultiple()) {
            select.deselectAll();
        }

        for (String visibleText : visibleTexts) {
            select.selectByVisibleText(visibleText);
        }
    }

    protected String getInputValue(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute("value").trim();
    }

    protected String getSelectedOption(WebElement element) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOf(element)));
        return select.getFirstSelectedOption().getText().trim();
    }

    protected List<String> getSelectedOptions(WebElement element) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOf(element)));
        return select.getAllSelectedOptions()
                .stream()
                .map(option -> option.getText().trim())
                .collect(Collectors.toList());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
