package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import pageobject.*;
import utils.User;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Login Tests")
@Feature("Authorization")
public class LoginTest extends BrowserTestBase {

    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API")
    void setUpTestUser() {
        RestAssured.baseURI = BASE_URL + "/api";
        testUser = UserGenerator.getValidUser();
        accessToken = createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());
    }

    @AfterEach
    @Step("Удаление тестового пользователя и закрытие браузера")
    void tearDownTestUser() {
        deleteUser(accessToken);
        super.tearDown();
    }

    @Test
    @Order(1)
    @DisplayName("Вход по кнопке 'Войти в аккаунт' на главной")
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
    @DisplayName("Вход через кнопку в форме регистрации")
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
    @DisplayName("Вход через кнопку в форме восстановления пароля")
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

    @Step("Создание пользователя через API")
    private String createUser(String email, String password, String name) {
        Response response = given()
                .contentType("application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                .when()
                .post("/auth/register")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось создать пользователя");
        return response.jsonPath().getString("accessToken");
    }

    @Step("Удаление пользователя через API")
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
