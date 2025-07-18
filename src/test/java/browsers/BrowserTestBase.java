package browsers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class BrowserTestBase {

    protected WebDriver driver;
    protected String browserName = "chrome";

    @BeforeEach
    public void setup() {
        System.out.println("Запуск браузера: " + browserName);
        driver = DriverFactory.createDriver(browserName);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            System.out.println("Закрываем браузер");
            driver.quit();
        }
    }
}
