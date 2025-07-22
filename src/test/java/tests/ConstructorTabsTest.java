package tests;

import browsers.BrowserTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageobject.MainPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorTabsTest extends BrowserTestBase {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Test
    @DisplayName("Проверка переходов в разделы Конструктора")
    void checkConstructorTabs() {
        driver.get(BASE_URL);

        MainPage mainPage = new MainPage(driver);

        assertTrue(mainPage.isConstructorVisible(), "Раздел 'Конструктор' не отображается");

        mainPage.clickBunsTab();
        assertTrue(mainPage.isBunsTabActive(), "Вкладка 'Булки' не активна после клика");

        mainPage.clickSaucesTab();
        assertTrue(mainPage.isSaucesTabActive(), "Вкладка 'Соусы' не активна после клика");

        mainPage.clickFillingsTab();
        assertTrue(mainPage.isFillingsTabActive(), "Вкладка 'Начинки' не активна после клика");
    }
}
