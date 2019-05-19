import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.xpath;

public class VacuumCleanersOnMarketTest {
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
    public void searchVacuumCleanerOnMarketTest() {
        // step 1
        String welcomePage = "https://market.yandex.ru/";
        String xpathName = "/html/head/meta[5]";
        String expected = "Яндекс.Маркет";

        driver.get(welcomePage);
        assertEquals("Yandex market home page not open", expected,
                driver.findElement(By.xpath(xpathName)).getAttribute("content"));

        //step 2
        String xpathSearch = "//*[@id=\"header-search\"]";
        String input = "пылесосы";
        String xpathSimpleVacuumCleaner = "//div[contains(text(),'Пылесосы')]";
        String xpathCheckVaacuum = "/html/head/meta[13]";

        WebElement search = driver.findElement(xpath(xpathSearch));
        search.sendKeys(input);
        search.submit();

        try {
            driver.findElement(xpath(xpathSimpleVacuumCleaner)).click();
        } catch (NoSuchElementException ignored) {  /* we are initially on the right page */ }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertTrue("Found not vacuum cleaners",
                driver.findElement(xpath(xpathCheckVaacuum))
                        .getAttribute("content")
                        .contains(input));

        // step 3
        String xpathFilters = "//span[contains(text(),'Все фильтры')]";
        String xpathPolaris = "//label[contains(text(),'Polaris')]";
        String xpathVitek = "//label[contains(text(),'VITEK')]";
        String checkbox = "checkbox_checked_yes";

        driver.findElement(xpath(xpathFilters)).click();
        driver.findElement(xpath(xpathPolaris)).click();
        driver.findElement(xpath(xpathVitek)).click();

        assertTrue("Polaris checkbox not checked",
                driver.findElement(xpath(xpathPolaris + "/.."))
                        .getAttribute("class")
                        .contains(checkbox));
        assertTrue("Vitek checkbox not checked",
                driver.findElement(xpath(xpathVitek + "/.."))
                .getAttribute("class")
                .contains(checkbox));

        // step 4
        String xpathBefore = "//span[@sign-title='до']/*/*";
        final String counter = "//div[contains(@class, 'n-filter-panel-counter')]";
        String inputPrice = "6000";

        WebElement before = driver.findElement(xpath(xpathBefore));
        before.sendKeys(inputPrice);

        Boolean isPopUpWindow = new WebDriverWait(driver, 10).until(localDriver ->
                localDriver.findElement(xpath(counter))
                        .getCssValue("display")
                        .equals("block"));

        assertTrue("Pop-up window with the number of suitable products did not appear", isPopUpWindow);

        // step 5
        String show = "//span[contains(text(),'Показать подходящие')]/..";
        String xpathOpenedResults = "//span[@class='n-search-preciser__results-count']";
        driver.findElement(xpath(show)).click();
        assertTrue("The filter results page did not open",
                driver.findElement(xpath(xpathOpenedResults)).isDisplayed());

        // step 6
        String xpathCheckBefore = "//input[@id='glpriceto']";
        assertEquals("The \"price before\" filter is not set",
                driver.findElement(xpath(xpathCheckBefore)).getAttribute("value"), inputPrice);

        String xpathCheckPolaris = "//input[contains(@name, 'Производитель Polaris')]";
        assertEquals("Polaris checkbox not checked", TRUE.toString(),
                driver.findElement(xpath(xpathCheckPolaris)).getAttribute("checked"));

        String xpathCheckVitek = "//input[contains(@name, 'Производитель VITEK')]";
        assertEquals("Vitek checkbox not checked", TRUE.toString(),
                driver.findElement(xpath(xpathCheckVitek)).getAttribute("checked"));
    }
}