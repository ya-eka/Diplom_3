package tests;

import browsers.BrowserTestBase;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.LoginPage;
import utils.User;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;

@Epic("Переходы")
@Feature("Личный кабинет")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogoutTest extends BrowserTestBase {

    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User testUser;

    @BeforeEach
    void prepareTestData() {
        testUser = new User("Ягодарова", "yagodarova_46@gmail.com", "12345678");
    }

    @Test
    @DisplayName("Выход из аккаунта по кнопке 'Выход'")
    @Severity(SeverityLevel.CRITICAL)
    void logoutTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Войти в аккаунт']"))).click();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testUser);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Оформить заказ']")));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Личный Кабинет']"))).click();

        wait.until(ExpectedConditions.urlContains("/account/profile"));

        By logoutButton = By.xpath("//button[contains(text(),'Выход')]");
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Войти']")));

        assertTrue(driver.findElement(By.xpath("//button[text()='Войти']")).isDisplayed(),
                "Кнопка 'Войти' не отображается — пользователь не вышел из аккаунта");
    }
}
