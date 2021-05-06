package utils; /**
 * @author  Mahesh Manjunath
 * Purpose:	To have collective generic functions which can be reused across project.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.HomePageObjects;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GenericFunctions extends DriverFactory {

	protected WebDriverWait wait;
	public static FileReader reader;
	private static final Logger log = LogManager.getLogger(GenericFunctions.class.getName());
	/**
	 * @param driver
	 */
	@SuppressWarnings("static-access")
	public GenericFunctions(WebDriver driver) {
		driver = getDriver();
	}


	// To enter text into the fields
	/**
	 * @param locator
	 * @param input
	 */
	public void enterText(By locator, String input) {
		try {
			if(isElementEnabled(locator)==true)
			{
				driver.findElement(locator).sendKeys(input);
			}

		} catch (Exception e) {
			System.out.println("Not able to enter the text to the locator - " + locator + "\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	// To verify field texts displayed on page
	/**
	 * @param locator
	 * @param message
	 */
	public void verifyText(WebElement locator, String message) {
		try {
			Assert.assertTrue(message.equals(locator.getText().toString()));
			log.info("Verified text with " + message + " and " + locator);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	// To check if element is displayed on page
	/**
	 * @param locator
	 * @return
	 */
	public boolean isElementDisplayed(By locator) {

		if (driver.findElement(locator).isDisplayed()) {
			log.info("Element is displayed ");
			return true;
		} else {
			log.error("Element is not displayed " );
			return false;
		}
	}

	// To wait for the element to be visible
	/**
	 * @param locator
	 */
	public void waitForElement(By locator) {
		try {
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	// To check if the element is enabled to perform actions
	/**
	 * @param locator
	 * @return
	 */
	public boolean isElementEnabled(By locator) {
		if (driver.findElement(locator).isDisplayed() && driver.findElement(locator).isEnabled()) {
			return true;
		} else {
			return false;
		}

	}

	// To target the element and to submit element request
	/**
	 * @param locator
	 */
	public void submitElement(WebElement locator) {
		try {


			Actions action = new Actions(getDriver());
			action.moveToElement(locator);

			locator.submit();

		} catch (ElementNotVisibleException e1) {
			try {

				((JavascriptExecutor) getDriver()).executeScript("arguments[0].submit();", locator);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		catch (ElementClickInterceptedException e2) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].submit();", locator);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	// To click on element using javascript executor
	/**
	 * @param locator
	 */
	public void clickOnElementJs(WebElement locator) {
		try {

			((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", locator);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// To click on element using regular action
	/**
	 * @param locator
	 */
	public void clickOnElement(By locator) {
		try {
			Assert.assertNotNull(locator);
			if(isElementEnabled(locator)==true)
			{
				driver.findElement(locator).click();
			}

			log.info("Clicked on element");

		} catch (ElementNotVisibleException e1) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", locator);
				log.info("Clicked on element using jse");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Failed to click on element");
				throw e;
			}
		}

		catch (ElementClickInterceptedException e2) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", locator);
				log.info("Clicked on element using jse");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Failed to click on " );
				throw e;
			}
		}

	}

	// To select radio button option with regular and javascript executor
	/**
	 * @param locator
	 */
	public void selectRadioButtonOption(WebElement locator) {
		try {
			locator.click();
		} catch (ElementNotVisibleException e1) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].checked=true;", locator);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		catch (ElementClickInterceptedException e2) {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].checked=true;", locator);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	// To switch for new window
	/**
	 * @param driver
	 */
	public void switchToWindow(WebDriver driver) {
		if (driver.getWindowHandles().size() > 0) {
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
		}
	}

	// To gain back the control on webpage
	public void swtichToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	// To perform different actions on alerts
	/**
	 * @param driver
	 * @param action
	 * @param message
	 */
	public void switchToAlert(WebDriver driver, String action, String message) {

		if (action.equalsIgnoreCase("ACCEPT")) {
			driver.switchTo().alert().accept();
		} else if (action.equalsIgnoreCase("DISMISS")) {
			driver.switchTo().alert().dismiss();
		} else if (action.equalsIgnoreCase("GETTEXT")) {
			Alert alert = driver.switchTo().alert();
			Assert.assertEquals(alert.getText().toString().toLowerCase(), message);
		} else if (action.equalsIgnoreCase("SWITCH")) {
			driver.switchTo().alert();
		}
	}

	// To set the environment execution platform
	public static void validateEnvironment() {
		String environment = System.getProperty("env");
		if (environment == null) {
			System.out.println("Please pass the environment variable 'env' in runtime arguments for TestRunner");
			System.exit(0);
		}
	}

	// Method to wait for time
	/**
	 * @param time
	 */
	public void waitWebDriver(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("Method: waitWebDriver :: exception =  " + e.getMessage());

		}
	}
//
//	// To take back up of extent reports generated at every iteration
//	public static void backUpExtentReport() throws IOException {
//		Date timeStamp = new Date();
//		String date = timeStamp.toString().replace(":", "_").replace(" ", "_");
//		File srcFile = new File(System.getProperty("user.dir") + "\\target\\report.html");
//		File destFile = new File(System.getProperty("user.dir") + "\\reports_backup\\" + date.toString() + ".html");
//		FileUtils.copyFile(srcFile, destFile);
//	}
//
//	// To read the data from json file
//	/**
//	 * @param dataset
//	 * @param dataString
//	 * @return
//	 * @throws IOException
//	 */
//	public String getTestData(String dataset, String dataString) throws IOException {
//		ReadConfigFile readConfFile = new ReadConfigFile();
//
//		String filename = Hooks.featureName.replaceAll("src/test/resources/Features/", "").replaceFirst(".feature:(.*)",
//				"");
//		FileReader reader = new FileReader(
//				readConfFile.getJsonDataPath() + "/" + System.getProperty("env") + "/" + filename + ".json");
//
//		try {
//			return JsonHandler.readDataFromJson(reader, dataset, dataString);
//		} catch (NullPointerException e) {
//			reader.close();
//			return "";
//		}
//
//	}
//
//	// To capture screenshot and to embed the captured screenshot into report
//	public static void captureScreenshot() throws IOException, InterruptedException {
//		Reporter.addStepLog("<br>");
//		String base64Screenshot = "data:image/png;base64,"
//				+ ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//		Reporter.addStepLog("<a href='" + base64Screenshot
//				+ "' data-featherlight='image'><img height=200 width=300 class='report-img'" + "src='"
//				+ base64Screenshot + "'/></a>");
//	}

	// To select and click value from drop down using locator as string
	public static void selectValueFromDropdown(WebDriver driver, String locator, String text) {

		WebElement lang = driver.findElement(By.xpath(locator));
		List<WebElement> list = lang.findElements(By.xpath(locator));
		for (WebElement opt : list) {
			String value = opt.getText();
			if (value.equalsIgnoreCase(text)) {
				System.out.println("Value clicked =" + value);
				opt.click();
			}
		}
	}

	// To select and click value from drop down using locator as webelement
	public static void clickDropDownByValue(WebDriver mDriver, WebElement locator, String text) {

		@SuppressWarnings("unused")
		boolean bFlag = false;
		String value = "";
		try {

			List<WebElement> options = locator.findElements(By.tagName("option"));
			for (WebElement option : options) {
				value = option.getText().trim().toLowerCase();
				if (value.endsWith(text.trim().toLowerCase())) {
					option.click();
					// System.out.println(option.getText());
					bFlag = true;
					break;
				}
			}
		} catch (Exception e) {
			log.error("Method: clickDropDownElement :: Exception occured for xpath value = " + locator + "exception = "
					+ e.getMessage() + "");
		}
	}

	// Date incrementer
	public Calendar dateIncrementer(int byDays) {
		@SuppressWarnings("unused")
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date(); // your date
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, byDays); // number of days to add
		// String incrementedDate = formatter.format(c.getTime());
		// System.out.println(incrementedDate);
		return c;
	}
}
