package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Константы для локаторов - заглавными буквами
    private static final By PROFILE_BUTTON = By.xpath("//p[text()='Личный Кабинет']");
    private static final By LOGIN_BUTTON_ON_MAIN = By.xpath("//button[contains(@class,'button_button__33qZ0') and text()='Войти в аккаунт']");
    private static final By BUNS_TAB = By.xpath("//span[text()='Булки']");
    private static final By SAUCES_TAB = By.xpath("//span[text()='Соусы']");
    private static final By FILLINGS_TAB = By.xpath("//span[text()='Начинки']");
    private static final By CONSTRUCTOR_HEADER = By.xpath("//h1[text()='Соберите бургер']");

    private static final String ACTIVE_TAB_CLASS = "tab_tab_type_current__2BEPc";

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void clickProfileButton() {
        wait.until(ExpectedConditions.elementToBeClickable(PROFILE_BUTTON)).click();
    }

    public void clickLoginButtonFromMainPage() {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON_ON_MAIN)).click();
    }

    public boolean isConstructorVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(CONSTRUCTOR_HEADER)).isDisplayed();
    }

    public void clickBunsTab() {
        clickTab(BUNS_TAB);
    }

    public void clickSaucesTab() {
        clickTab(SAUCES_TAB);
    }

    public void clickFillingsTab() {
        clickTab(FILLINGS_TAB);
    }

    private void clickTab(By tabLocator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(tabLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private boolean isTabActive(By tabLocator) {
        WebElement tabSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
        WebElement tabDiv = tabSpan.findElement(By.xpath("./.."));
        return wait.until(ExpectedConditions.attributeContains(tabDiv, "class", ACTIVE_TAB_CLASS));
    }

    public boolean isBunsTabActive() {
        return isTabActive(BUNS_TAB);
    }

    public boolean isSaucesTabActive() {
        return isTabActive(SAUCES_TAB);
    }

    public boolean isFillingsTabActive() {
        return isTabActive(FILLINGS_TAB);
    }
}
