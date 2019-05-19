import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.xpath;

public class GoogleToMarketTest {
    private static WebDriver driver;

    @BeforeClass
    public static void beforeTests() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void afterTests() { driver.quit(); }

    @Test
    public void openGoogleGoToMarket() {
        String welcomePage = "https://www.google.ru";
        String input = "яндекс маркет";
        String xpathSearch = "//input[@name='q']";
        String xpathFirstLink = "//*[@id=\"rso\"]/div[1]/div/div/div/div/div[1]/a";
        String expectedOpenMarket = "Яндекс.Маркет — выбор и покупка товаров из проверенных интернет-магазинов";

        // step 1
        driver.get(welcomePage);
        assertEquals("Google home page not open", "Google", driver.getTitle());

        // step 2
        WebElement search = driver.findElement(xpath(xpathSearch));
        search.sendKeys(input);
        search.submit();

        WebElement firstLinkOnPage = driver.findElement(xpath(xpathFirstLink));
        assertEquals("First link on page not Yandex market", "https://market.yandex.ru/", firstLinkOnPage.getAttribute("href"));

        // step 3
        String openedWindow = driver.getWindowHandle();
        driver.findElement(xpath(xpathFirstLink)).click();
        switchWindow(openedWindow);

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        assertEquals("Opened not Yandex market", expectedOpenMarket, driver.getTitle());
    }

    private void switchWindow(String openedWindow) {
        Set<String> newWindows = driver.getWindowHandles();
        newWindows.remove(openedWindow);
        Iterator<String> iterator = newWindows.iterator();
        if (iterator.hasNext()) driver.switchTo().window(iterator.next());
    }
}