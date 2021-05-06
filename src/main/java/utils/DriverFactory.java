package utils;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
    public static WebDriver driver;
    public static String url = "http://localhost:3000"; // for docker instead of localhost need to set "http://host.docker.internal:3000";

    @BeforeClass
    public void setUp() throws MalformedURLException {
        driver = DriverFactory.build(System.getProperty("browser"));
    }


    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    public static WebDriver build(String browser) throws MalformedURLException {

        String type = System.getProperty("type");
        if (type.equals("local")) {
            switch (browser) {
                case "chrome":
                    return buildChromeDriver();
                case "firefox":
                    return buildFirefoxDriver();
                default:
                    throw new IllegalStateException("Unexpected value: " + browser);
            }
        } else if (type.equals("remote")) {
            return buildRemoteWebDriver(browser);
        } else {
            throw new IllegalArgumentException(String.format("%s is invalid.", type));
        }
    }

    public static WebDriver buildRemoteWebDriver(String browser) throws MalformedURLException {
        url = "http://host.docker.internal:3000";
        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
                chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model
//                chromeOptions.addArguments("disable-gpu");
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
                driver.manage().deleteAllCookies();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
                firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxOptions);
                driver.manage().deleteAllCookies();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser);
        }

        return driver;
    }

    public static WebDriver buildChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver buildFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver.exe");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }


}
