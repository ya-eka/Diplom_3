package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pageobject.*;
import utils.User;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Login Tests")
@Feature("Authorization")
public class LoginTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String API_BASE_PATH = "/api";
    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создаем тестового пользователя через API")
    void setUpTestUser() {
        RestAssured.baseURI = BASE_URL + API_BASE_PATH;
        testUser = UserGenerator.getValidUser();
        accessToken = createUser(testUser);
    }

    @AfterEach
    @Step("Удаляем тестового пользователя через API и закрываем браузер")
    void tearDownTestUser() {
        deleteUser(accessToken);
        super.tearDown();
    }

    @Test
    @Order(1)
    @DisplayName("Вход через кнопку 'Войти в аккаунт' на главной")
    @Severity(SeverityLevel.CRITICAL)
    void loginFromMainPage() {
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
    void loginFromProfileButton() {
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
    void loginFromRegisterPage() {
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
    void loginFromForgotPasswordPage() {
        driver.get(BASE_URL + "/forgot-password");

        ForgotPasswordPage forgotPage = new ForgotPasswordPage(driver);
        forgotPage.clickLoginLink();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        MainPage mainPage = new MainPage(driver);
        assertTrue(mainPage.isConstructorVisible(), "Не удалось войти через форму восстановления пароля");
    }

    @Step("Создаем пользователя через API")
    private String createUser(User user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/auth/register")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось создать пользователя");
        return response.jsonPath().getString("accessToken");
    }

    @Step("Удаляем пользователя через API")
    private void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isEmpty()) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}
