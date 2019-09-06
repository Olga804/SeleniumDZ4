import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class JuniteTest {
    private static WebDriver driver;
    private static String baseUrl;

    private String lastName;
    private String firstName;
    private String middleName;

    public JuniteTest(String lastName, String firstName, String middleName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        final String path = System.getProperty("driver"); //"Chrome";

        if (path.equals("firefox")){
            System.setProperty("webdriver.gecko.driver", "drv/geckodriver.exe");
            driver = new FirefoxDriver() ;

        }if(path.equals("chrome")){
            System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
            driver = new ChromeDriver();

        } if (path.equals("ie")){
            System.setProperty("webdriver.ie.driver", "drv/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
        }

        baseUrl = "https://www.rgs.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(baseUrl + "/");
        driver.findElement(By.xpath("//ol[contains(@class,'rgs-menu')]/li/*[@class=\"hidden-xs\"]")).click();
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[contains(text(),'ДМС')]"))));
        driver.findElement(By.xpath("//*[contains(text(),'ДМС')]")).click();

        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.xpath("//*[contains(text(),'Отправить заявку')][contains(@class,'btn')]"))));
        driver.findElement(By.xpath("//*[contains(text(),'Отправить заявку')][contains(@class,'btn')]")).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h4[@class='modal-title']"))));
    }



    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][]data = new Object[][]{
                {"Иванов", "Иван", "Иванович"},{"Петрова", "Анна", "Владимировна"},{"В голове", "Моей", "Опилки"}
        };
        return Arrays.asList(data);
    }



    @Test
    public void testInsurance() throws Exception {

        fillField(By.name("LastName"), lastName);
        fillField(By.name("FirstName"), firstName);
        fillField(By.name("MiddleName"), middleName);
        new Select(driver.findElement(By.name("Region"))).selectByVisibleText("Москва");
        fillField(By.name("Email"), "qwertyqwerty");
        driver.findElement(By.xpath("(//INPUT[@type='text'])[6]")).click();
        fillField(By.xpath("(//INPUT[@type='text'])[6]"), "9876543210");
        fillField(By.name("Comment"), "test");
        driver.findElement(By.cssSelector("input.checkbox")).click();
        driver.findElement(By.id("button-m")).click();


        assertEquals(lastName, driver.findElement(By.name("LastName")).getAttribute("value"));
        assertEquals(middleName, driver.findElement(By.name("MiddleName")).getAttribute("value"));
        assertEquals(firstName, driver.findElement(By.name("FirstName")).getAttribute("value"));
        assertEquals("qwertyqwerty", driver.findElement(By.name("Email")).getAttribute("value"));
        assertEquals("test", driver.findElement(By.name("Comment")).getAttribute("value"));


        assertEquals("+7 (987) 654-32-10", driver.findElement(By.xpath("(//INPUT[@type='text'])[6]")).getAttribute("value"));


        assertEquals("Москва",
                new Select(driver.findElement(By.name("Region"))).getAllSelectedOptions().get(0).getText());
        assertEquals("Введите адрес электронной почты",
                driver.findElement(By.xpath("//*[text()='Эл. почта']/..//span[@class='validation-error-text']")).getText());
    }
    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }

    private void fillField(By locator, String value){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }


}
