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
import utils.UserClient;
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
    private String createdUserToken;

    private static final String REGISTER_URL = "https://stellarburgers.nomoreparties.site/register";

    @Step("Открыть страницу регистрации")
    private void openRegisterPage() {
        driver.get(REGISTER_URL);
        registerPage = new RegisterPage(driver);
    }

    @AfterEach
    @Step("Удаление пользователя после теста, если он был создан")
    public void tearDown() {
        if (createdUserToken != null && !createdUserToken.isEmpty()) {
            UserClient.deleteUser(createdUserToken);
            createdUserToken = null;
        }
    }

    @ParameterizedTest(name = "Успешная регистрация нового пользователя в браузере {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Проверка успешной регистрации пользователя с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    void successfulRegistrationTest(String browser) {
        this.browserName = browser;

        openRegisterPage();

        User user = UserGenerator.getValidUser();

        registerPage.register(user);

        createdUserToken = UserClient.loginAndGetToken(user);

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isLoginButtonVisible(), "После регистрации должна открыться форма логина");

        loginPage.login(user);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Оформить заказ']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Личный Кабинет']"))).click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/account"),
                "После логина должен быть переход в профиль, но текущий URL: " + currentUrl);
    }

    @ParameterizedTest(name = "Ошибка при коротком пароле в браузере {0}")
    @ValueSource(strings = {"chrome", "yandex"})
    @DisplayName("Ошибка при вводе короткого пароля")
    @Description("Проверка отображения ошибки при вводе пароля короче 6 символов")
    @Severity(SeverityLevel.NORMAL)
    void shortPasswordErrorTest(String browser) {
        this.browserName = browser;

        openRegisterPage();

        User user = UserGenerator.getValidUser();

        User shortPasswordUser = new User(user.getName(), user.getEmail(), "12345");

        registerPage.register(shortPasswordUser);

        assertTrue(registerPage.isPasswordErrorDisplayed(), "Должна отображаться ошибка о коротком пароле");
    }
}