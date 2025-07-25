package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage {

    private final WebDriver driver;

    private static final By PROFILE_HEADER = By.xpath("//a[text()='Профиль']");
    private static final By CONSTRUCTOR_BUTTON = By.xpath("//p[text()='Конструктор']");
    private static final By LOGO = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']");
    private static final By LOGOUT_BUTTON = By.xpath("//button[text()='Выход']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProfileHeaderVisible() {
        return driver.findElement(PROFILE_HEADER).isDisplayed();
    }

    public void clickConstructorButton() {
        driver.findElement(CONSTRUCTOR_BUTTON).click();
    }

    public void clickLogo() {
        driver.findElement(LOGO).click();
    }

    public void clickLogoutButton() {
        driver.findElement(LOGOUT_BUTTON).click();
    }
}
