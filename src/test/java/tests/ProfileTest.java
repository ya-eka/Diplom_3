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

@Epic("Переходы")
@Feature("Личный кабинет")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileTest extends BrowserTestBase {

    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API")
    void createTestUser() {
        RestAssured.baseURI = BASE_URL + "/api";
        testUser = UserGenerator.getValidUser();
        accessToken = createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());
    }

    @AfterEach
    @Step("Удаление тестового пользователя")
    void deleteTestUser() {
        deleteUser(accessToken);
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
