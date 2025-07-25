package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import utils.User;

public class RegisterPage {

    private final WebDriver driver;

    private static final By NAME_INPUT = By.xpath("//label[text()='Имя']/following-sibling::input");
    private static final By EMAIL_INPUT = By.xpath("//label[text()='Email']/following-sibling::input");
    private static final By PASSWORD_INPUT = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private static final By REGISTER_BUTTON = By.xpath("//button[text()='Зарегистрироваться']");
    private static final By PASSWORD_ERROR = By.xpath("//p[contains(text(),'Некорректный пароль')]");
    private static final By LOGIN_LINK = By.xpath("//a[text()='Войти']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Заполнить форму регистрации и нажать Зарегистрироваться")
    public void register(User user) {
        driver.findElement(NAME_INPUT).sendKeys(user.getName());
        driver.findElement(EMAIL_INPUT).sendKeys(user.getEmail());
        driver.findElement(PASSWORD_INPUT).sendKeys(user.getPassword());
        driver.findElement(REGISTER_BUTTON).click();
    }

    @Step("Проверить отображение ошибки о некорректном пароле")
    public boolean isPasswordErrorDisplayed() {
        return !driver.findElements(PASSWORD_ERROR).isEmpty()
                && driver.findElement(PASSWORD_ERROR).isDisplayed();
    }

    @Step("Кликнуть по ссылке Войти")
    public void clickLoginLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginLinkElement = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_LINK));
        loginLinkElement.click();
    }
}
