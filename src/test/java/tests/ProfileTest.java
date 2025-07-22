package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pageobject.LoginPage;
import pageobject.MainPage;
import pageobject.ProfilePage;
import utils.User;
import utils.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Переходы")
@Feature("Личный кабинет")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String API_BASE_PATH = "/api";

    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API")
    void createTestUser() {
        RestAssured.baseURI = BASE_URL + API_BASE_PATH;
        testUser = UserGenerator.getValidUser();
        accessToken = createUser(testUser);
    }

    @AfterEach
    @Step("Удаление тестового пользователя")
    void deleteTestUser() {
        if (accessToken != null && !accessToken.isEmpty()) {
            deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Переход в Личный кабинет авторизованного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    void profileAccessAfterLoginTest() {
        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickProfileButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        mainPage.clickProfileButton();

        ProfilePage profilePage = new ProfilePage(driver);
        assertTrue(profilePage.isProfileHeaderVisible(), "Заголовок 'Профиль' не отображается, переход не удался");
    }

    private String createUser(User user) {
        String jsonBody = String.format(
                "{\"email\":\"%s\", \"password\":\"%s\", \"name\":\"%s\"}",
                user.getEmail(), user.getPassword(), user.getName()
        );

        Response response = given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/auth/register")
                .then()
                .extract()
                .response();

        assertEquals(200, response.getStatusCode(), "Не удалось создать пользователя");
        return response.jsonPath().getString("accessToken");
    }

    private void deleteUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user")
                .then()
                .statusCode(202);
    }
}
