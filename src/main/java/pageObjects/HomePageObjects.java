package pageObjects; /**
 * @author Mahesh Manjunath
 * @date 04-May-2021
 * Purpose:	To have collective page locator for Home page
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverFactory;
import utils.GenericFunctions;

public class HomePageObjects extends DriverFactory {

    By challengeButton = By.xpath("//span[contains(text(),'Render the Challenge')]");
    By answerSubmitButtonOne = By.xpath("//input[@data-test-id='submit-1']");
    By answerSubmitButtonTwo = By.xpath("//input[@data-test-id='submit-2']");
    By answerSubmitButtonThree = By.xpath("//input[@data-test-id='submit-3']");
    By answerSubmitButtonFour = By.xpath("//input[@data-test-id='submit-4']");
    By challengeTable = By.xpath("//input[@data-test-id='submit-1']//preceding::table[1]");
    By challengeTableColumn = By.xpath("//input[@data-test-id='submit-1']//preceding::table[1]//tr[1]/td");
    By challengeTableRow = By.xpath("//input[@data-test-id='submit-1']//preceding::table[1]//tr[1]");
    By submitAnswerButton = By.xpath("//span[contains(text(),'Submit Answers')]");

    private static final Logger log = LogManager.getLogger(HomePageObjects.class.getName());
    GenericFunctions genericFunctions = PageFactory.initElements(driver, GenericFunctions.class);


    public void clickChallengeButton() {
        try {
            genericFunctions.isElementDisplayed(challengeButton);
            genericFunctions.clickOnElement(challengeButton);
            System.out.println("Successfully clicked Challenge Button");

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    //solves the challenge and submit the answer
    public void solveChallengeAndSubmitAnswer() {
        try {
            genericFunctions.isElementDisplayed(answerSubmitButtonOne);
            genericFunctions.enterText(answerSubmitButtonOne, readTableArray(1));
            genericFunctions.enterText(answerSubmitButtonTwo, readTableArray(2));
            genericFunctions.enterText(answerSubmitButtonThree, readTableArray(3));
            genericFunctions.enterText(answerSubmitButtonFour, "Mahesh Manjunath");
            genericFunctions.clickOnElement(submitAnswerButton);
            System.out.println("Submitted the challenge ");

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public String readTableArray(int rowCount) {
        int columnCount = driver.findElements(challengeTableColumn).size();
        int arr[] = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            arr[i] = Integer.parseInt(driver.findElements(By.xpath("//input[@data-test-id='submit-1']//preceding::table[1]//tr[" + rowCount + "]/td")).get(i).getText());
        }
        String answer = findElementWithinArray(arr, columnCount);
        System.out.println("Answer" + rowCount + " Index is" + answer + " & Value is" + arr[Integer.parseInt(answer)]);
        return answer;
    }

    public String findElementWithinArray(int arr[], int n) {
        // Forming prefix sum array from 0
        int[] prefixSum = new int[n];
        prefixSum[0] = arr[0];
        for (int i = 1; i < n; i++)
            prefixSum[i] = prefixSum[i - 1] + arr[i];

        // Forming suffix sum array from n-1
        int[] suffixSum = new int[n];
        suffixSum[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--)
            suffixSum[i] = suffixSum[i + 1] + arr[i];

        // Find the point where prefix and suffix
        // sums are same.
        for (int i = 1; i < n - 1; i++)
            if (prefixSum[i] == suffixSum[i])
                return String.valueOf(i);

        return "-1";
    }

}
