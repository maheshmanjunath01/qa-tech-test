
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import pageObjects.HomePageObjects;
import utils.DriverFactory;

public class DockerTest extends DriverFactory {
    HomePageObjects homePage = PageFactory.initElements(driver, HomePageObjects.class);
    private static final Logger log = LogManager.getLogger(DockerTest.class.getName());

    @Test
    public void dockerTest() {
        try {
            driver.get(DriverFactory.url);
            homePage.clickChallengeButton();
            homePage.solveChallengeAndSubmitAnswer();
        } catch (NoSuchElementException e) {
            System.out.println("Could not load the page");
            e.printStackTrace();
        }
    }
}