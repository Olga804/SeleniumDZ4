import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class SecondTest {

    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception {

        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");

        driver = new ChromeDriver();
        baseUrl = "https://www.sberbank.ru/ru/person/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testInsurance() throws Exception {////UL[@class='footer__social']
        driver.get(baseUrl + "/");
        driver.findElement(By.xpath("(//SPAN[text()='Москва'][text()='Москва'])[1]")).click();
        driver.findElement(By.xpath("//A[@class='kit-link kit-link_m hd-ft-region__city'][text()='Нижегородская область']")).click();
        assertEquals("Нижегородская область",
                driver.findElement(By.xpath("(//SPAN[text()='Нижегородская область'][text()='Нижегородская область'])[1]")).getText());
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.findElement(By.xpath("//UL[@class='footer__social']")).isEnabled();
        driver.findElement(By.xpath("//UL[@class='footer__social']")).isDisplayed();
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }



}

