import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InsuranceTest {
    private WebDriver driver;
    private String baseUrl;


    @Before
    public void setUp() throws Exception {

        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");

        driver = new ChromeDriver();
        baseUrl = "https://www.rgs.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void testInsurance() throws Exception {
        driver.get(baseUrl + "/");
        driver.findElement(By.xpath("//ol[contains(@class,'rgs-menu')]/li/*[contains(text(),'Страхование')]")).click();
        driver.findElement(By.xpath("//*[contains(text(),'ДМС')]")).click();
        assertEquals("ДМС — добровольное медицинское страхование",
                driver.findElement(By.xpath("//H1[@class='content-document-header'][contains(text(),'\n" +
                        "\n" +
                        "\t\t\t\t\t\t\tДМС')]")).getText());

        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.xpath("//*[contains(text(),'Отправить заявку')][contains(@class,'btn')]"))));

        driver.findElement(By.xpath("//*[contains(text(),'Отправить заявку')][contains(@class,'btn')]")).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h4[@class='modal-title']"))));

        assertEquals("Заявка на добровольное медицинское страхование",
                driver.findElement(By.xpath("//h4[@class='modal-title']")).getText());

        fillField(By.name("LastName"), "Иванов");
        fillField(By.name("FirstName"), "Иван");
        fillField(By.name("MiddleName"), "Иванович");

        new Select(driver.findElement(By.name("Region"))).selectByVisibleText("Москва");

        fillField(By.name("Email"), "qwertyqwerty");
        driver.findElement(By.xpath("(//INPUT[@type='text'])[6]")).click();
        fillField(By.xpath("(//INPUT[@type='text'])[6]"), "9876543210");
        fillField(By.name("Comment"), "test");




        driver.findElement(By.cssSelector("input.checkbox")).click();
        driver.findElement(By.id("button-m")).click();

        assertEquals("Иванов", driver.findElement(By.name("LastName")).getAttribute("value"));
        assertEquals("Иванович", driver.findElement(By.name("MiddleName")).getAttribute("value"));
        assertEquals("Иван", driver.findElement(By.name("FirstName")).getAttribute("value"));
        assertEquals("qwertyqwerty", driver.findElement(By.name("Email")).getAttribute("value"));
        assertEquals("test", driver.findElement(By.name("Comment")).getAttribute("value"));
        //Thread.sleep(3000);

        assertEquals("+7 (987) 654-32-10", driver.findElement(By.xpath("(//INPUT[@type='text'])[6]")).getAttribute("value"));


        assertEquals("Москва",
                new Select(driver.findElement(By.name("Region"))).getAllSelectedOptions().get(0).getText());
        assertEquals("Введите адрес электронной почты",
                driver.findElement(By.xpath("//*[text()='Эл. почта']/..//span[@class='validation-error-text']")).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    private void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }


}
