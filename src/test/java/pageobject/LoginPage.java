package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.User;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Константы локаторов
    private static final By EMAIL_LOCATOR = By.xpath("//label[text()='Email']/following-sibling::input");
    private static final By PASSWORD_LOCATOR = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private static final By LOGIN_BUTTON_LOCATOR = By.xpath("//button[text()='Войти']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForPageLoad();
    }

    @Step("Ожидаем загрузку страницы логина")
    private void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_LOCATOR));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_LOCATOR));
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON_LOCATOR));
    }

    @Step("Вводим email: {email}")
    public void setEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_LOCATOR));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    @Step("Вводим пароль: {password}")
    public void setPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_LOCATOR));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    @Step("Нажимаем кнопку 'Войти'")
    public void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON_LOCATOR));
        loginButton.click();
    }

    @Step("Выполняем вход с email: {email} и паролем")
    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    @Step("Выполняем вход через объект User")
    public void login(User user) {
        login(user.getEmail(), user.getPassword());
    }

    @Step("Проверяем видимость формы логина")
    public boolean isLoginFormVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_LOCATOR));
            wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_LOCATOR));
            wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_BUTTON_LOCATOR));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Проверяем видимость кнопки входа")
    public boolean isLoginButtonVisible() {
        try {
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_BUTTON_LOCATOR));
            return loginButton.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
