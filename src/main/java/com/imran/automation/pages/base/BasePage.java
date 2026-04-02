package com.imran.automation.pages.base;

import com.imran.automation.constants.FrameworkConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        visibleElement.click();
        visibleElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        visibleElement.sendKeys(Keys.DELETE);
        visibleElement.clear();
        visibleElement.sendKeys(value);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void clickUsingJs(WebElement element) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", visibleElement);
    }

    protected void clickUsingJs(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});"
                        + "arguments[0].click();",
                element
        );
    }

    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    protected void setValueUsingJs(WebElement element, String value) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                visibleElement,
                value
        );
    }

    protected void setValueUsingJs(By locator, String value) {
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                visibleElement,
                value
        );
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

    protected boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception exception) {
            return false;
        }
    }

    protected Object executeScript(String script, Object... arguments) {
        return ((JavascriptExecutor) driver).executeScript(script, arguments);
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
