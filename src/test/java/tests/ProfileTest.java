package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pageobject.LoginPage;
import pageobject.MainPage;
import pageobject.ProfilePage;
import utils.User;
import utils.UserClient;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Переходы")
@Feature("Личный кабинет")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private User testUser;
    private String accessToken;

    @BeforeEach
    @Step("Создание тестового пользователя через API")
    public void createTestUser() {
        testUser = UserGenerator.getValidUser();
        accessToken = UserClient.createUser(testUser);
    }

    @AfterEach
    @Step("Удаление тестового пользователя")
    public void deleteTestUser() {
        UserClient.deleteUser(accessToken);
        accessToken = null;
    }

    @Test
    @DisplayName("Переход в Личный кабинет авторизованного пользователя")
    @Severity(SeverityLevel.CRITICAL)
    public void profileAccessAfterLoginTest() {
        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);
        mainPage.clickProfileButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        mainPage.clickProfileButton();

        ProfilePage profilePage = new ProfilePage(driver);
        assertTrue(profilePage.isProfileHeaderVisible(), "Заголовок 'Профиль' не отображается, переход не удался");
    }
}
