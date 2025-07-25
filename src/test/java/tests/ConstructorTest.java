package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pageobject.*;
import utils.User;
import utils.UserClient;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Переходы")
@Feature("Конструктор из Личного кабинета")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConstructorTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API и запуск браузера")
    void setUp() {
        User testUser = UserGenerator.getValidUser();
        accessToken = UserClient.createUser(testUser);

        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickProfileButton();

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isLoginFormVisible(), "Форма логина не загрузилась");
        loginPage.login(testUser);

        mainPage.clickProfileButton();
    }

    @AfterEach
    @Step("Удаление пользователя и закрытие браузера")
    public void tearDown() {
        UserClient.deleteUser(accessToken);
        super.tearDown();
    }

    @Test
    @Order(1)
    @DisplayName("Переход из Личного кабинета в Конструктор по кнопке 'Конструктор'")
    @Severity(SeverityLevel.CRITICAL)
    void transitionToConstructorViaButton() {
        ProfilePage profilePage = new ProfilePage(driver);
        assertTrue(profilePage.isProfileHeaderVisible(), "Мы не в Личном кабинете");

        profilePage.clickConstructorButton();

        MainPage mainPage = new MainPage(driver);
        assertTrue(mainPage.isConstructorVisible(), "Переход в Конструктор не произошел по кнопке 'Конструктор'");
    }

    @Test
    @Order(2)
    @DisplayName("Переход из Личного кабинета в Конструктор по клику на логотип Stellar Burgers")
    @Severity(SeverityLevel.CRITICAL)
    void transitionToConstructorViaLogo() {
        ProfilePage profilePage = new ProfilePage(driver);
        assertTrue(profilePage.isProfileHeaderVisible(), "Мы не в Личном кабинете");

        profilePage.clickLogo();

        MainPage mainPage = new MainPage(driver);
        assertTrue(mainPage.isConstructorVisible(), "Переход в Конструктор не произошел по клику на логотип");
    }
}