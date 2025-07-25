package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pageobject.*;
import utils.User;
import utils.UserClient;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Login Tests")
@Feature("Authorization")
public class LoginTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создаем тестового пользователя через API")
    public void setUpTestUser() {
        testUser = UserGenerator.getValidUser();
        accessToken = UserClient.createUser(testUser);
    }

    @AfterEach
    @Step("Удаляем тестового пользователя через API и закрываем браузер")
    public void tearDownTestUser() {
        UserClient.deleteUser(accessToken);
        accessToken = null;
        super.tearDown();
    }

    @Test
    @Order(1)
    @DisplayName("Вход через кнопку 'Войти в аккаунт' на главной")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFromMainPage() {
        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginButtonFromMainPage();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        assertTrue(mainPage.isConstructorVisible(), "Не удалось войти на главную после логина");
    }

    @Test
    @Order(2)
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFromProfileButton() {
        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickProfileButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        assertTrue(mainPage.isConstructorVisible(), "Не удалось войти через 'Личный кабинет'");
    }

    @Test
    @Order(3)
    @DisplayName("Вход через ссылку в форме регистрации")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFromRegisterPage() {
        driver.get(BASE_URL + "/register");

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.clickLoginLink();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        MainPage mainPage = new MainPage(driver);
        assertTrue(mainPage.isConstructorVisible(), "Не удалось войти через форму регистрации");
    }

    @Test
    @Order(4)
    @DisplayName("Вход через ссылку в форме восстановления пароля")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFromForgotPasswordPage() {
        driver.get(BASE_URL + "/forgot-password");

        ForgotPasswordPage forgotPage = new ForgotPasswordPage(driver);
        forgotPage.clickLoginLink();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        MainPage mainPage = new MainPage(driver);
        assertTrue(mainPage.isConstructorVisible(), "Не удалось войти через форму восстановления пароля");
    }
}
