package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage {
    private WebDriver driver;
    private final By profileHeader = By.xpath("//a[text()='Профиль']");
    private final By constructorButton = By.xpath("//p[text()='Конструктор']");
    private final By logo = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']");
    private final By logoutButton = By.xpath("//button[text()='Выход']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProfileHeaderVisible() {
        return driver.findElement(profileHeader).isDisplayed();
    }

    public void clickConstructorButton() {
        driver.findElement(constructorButton).click();
    }

    public void clickLogo() {
        driver.findElement(logo).click();
    }

    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }
}
