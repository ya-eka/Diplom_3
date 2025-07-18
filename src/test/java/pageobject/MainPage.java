package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private final By profileButton = By.xpath("//p[text()='Личный Кабинет']");
    private final By loginLink = By.xpath("//a[text()='Войти']");

    private final By loginButtonOnMain = By.xpath("//button[contains(@class,'button_button__33qZ0') and text()='Войти в аккаунт']");

    private final By bunsTab = By.xpath("//span[text()='Булки']");
    private final By saucesTab = By.xpath("//span[text()='Соусы']");
    private final By fillingsTab = By.xpath("//span[text()='Начинки']");

    private final By constructorHeader = By.xpath("//h1[text()='Соберите бургер']");

    private final String activeTabClass = "tab_tab_type_current__2BEPc";

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void clickProfileButton() {
        wait.until(ExpectedConditions.elementToBeClickable(profileButton)).click();
    }

    public void clickLoginButtonFromMainPage() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButtonOnMain)).click();
    }

    public boolean isConstructorVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(constructorHeader)).isDisplayed();
    }

    public void clickBunsTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(bunsTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickSaucesTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saucesTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickFillingsTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(fillingsTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private boolean isTabActive(By tabLocator) {
        WebElement tabSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
        WebElement tabDiv = tabSpan.findElement(By.xpath("./.."));
        return wait.until(ExpectedConditions.attributeContains(tabDiv, "class", activeTabClass));
    }

    public boolean isBunsTabActive() {
        return isTabActive(bunsTab);
    }

    public boolean isSaucesTabActive() {
        return isTabActive(saucesTab);
    }

    public boolean isFillingsTabActive() {
        return isTabActive(fillingsTab);
    }
}
