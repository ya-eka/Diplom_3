package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pageobject.*;
import utils.User;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Переходы")
@Feature("Конструктор из Личного кабинета")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConstructorTest extends BrowserTestBase {

    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API и запуск браузера")
    void setUp() {
        testUser = UserGenerator.getValidUser();
        accessToken = createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());

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
        deleteUser(accessToken);
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

    // === API ===
    private String createUser(String email, String password, String name) {
        io.restassured.response.Response response = io.restassured.RestAssured.given()
                .contentType("application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                .when()
                .post(BASE_URL + "/api/auth/register")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось создать пользователя");
        return response.jsonPath().getString("accessToken");
    }

    private void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isEmpty()) {
            io.restassured.RestAssured.given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(BASE_URL + "/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}
