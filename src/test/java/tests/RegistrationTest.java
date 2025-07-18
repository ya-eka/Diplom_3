package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.LoginPage;
import pageobject.RegisterPage;
import utils.User;
import utils.UserGenerator;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Stellar Burgers - Регистрация")
@Feature("Регистрация пользователя")
@Owner("QA Team")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest extends BrowserTestBase {

    private RegisterPage registerPage;

    @ParameterizedTest(name = "Регистрация в браузере {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Проверка успешной регистрации пользователя с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Тест: успешная регистрация")
    void successfulRegistrationTest(String browser) {
        this.browserName = browser;

        driver.get("https://stellarburgers.nomoreparties.site/register");
        registerPage = new RegisterPage(driver);

        String uniqueEmail = "test" + System.currentTimeMillis() + "@mail.com";
        User user = new User("TestUser", uniqueEmail, "validPassword123");
        registerPage.register(user.getName(), user.getEmail(), user.getPassword());

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isLoginButtonVisible(), "После регистрации должна открыться форма логина");

        loginPage.login(user);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Оформить заказ']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Личный Кабинет']"))).click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/account"), "После логина должен быть переход в профиль, но текущий URL: " + currentUrl);
    }

    @ParameterizedTest(name = "Ошибка при коротком пароле в браузере {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Ошибка при вводе короткого пароля")
    @Description("Проверка отображения ошибки при вводе пароля короче 6 символов")
    @Severity(SeverityLevel.NORMAL)
    @Step("Тест: ошибка при коротком пароле")
    void shortPasswordErrorTest(String browser) {
        this.browserName = browser;  // **ВАЖНО!**

        driver.get("https://stellarburgers.nomoreparties.site/register");
        registerPage = new RegisterPage(driver);

        User user = UserGenerator.getValidUser();
        registerPage.register(user.getName(), user.getEmail(), "12345");

        assertTrue(registerPage.isPasswordErrorDisplayed(), "Должна отображаться ошибка о коротком пароле");
    }
}
