package browsers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import io.qameta.allure.Step;

public class BrowserTestBase {

    protected WebDriver driver;
    protected String browserName = "chrome";

    @BeforeEach
    @Step("Запуск браузера {browserName}")
    public void setup() {
        System.out.println("Запуск браузера: " + browserName);
        driver = DriverFactory.createDriver(browserName);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterEach
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            System.out.println("Закрываем браузер");
            driver.quit();
        }
    }
}
