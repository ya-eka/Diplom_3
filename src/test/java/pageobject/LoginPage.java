package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.User;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By emailLocator = By.xpath("//label[text()='Email']/following-sibling::input");
    private By passwordLocator = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private By loginButtonLocator = By.xpath("//button[text()='Войти']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailLocator));
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordLocator));
        wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
    }

    @Step("Вводим email: {email}")
    public void setEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailLocator));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    @Step("Вводим пароль: {password}")
    public void setPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordLocator));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    @Step("Нажимаем кнопку 'Войти'")
    public void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonLocator));
        loginButton.click();
    }

    @Step("Выполняем вход: {email}, {password}")
    public void login(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    @Step("Выполняем вход через объект User")
    public void login(User user) {
        login(user.getEmail(), user.getPassword());
    }

    @Step("Проверяем, что форма логина отображается")
    public boolean isLoginFormVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emailLocator));
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordLocator));
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonLocator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoginButtonVisible() {
        try {
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonLocator));
            return loginButton.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
