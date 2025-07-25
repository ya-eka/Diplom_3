package browsers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browserName) {
        if ("chrome".equalsIgnoreCase(browserName)) {
            System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            return new ChromeDriver(options);
        } else if ("yandex".equalsIgnoreCase(browserName)) {
            System.setProperty("webdriver.chrome.driver", "drivers\\yandexdriver.exe"); // если у вас exe
            ChromeOptions options = new ChromeOptions();
            return new ChromeDriver(options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
}
