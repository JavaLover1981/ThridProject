package com.automation.libraries;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class have all Selenium/WebDriver related wrapper methods and features.
 * 
 * @author renaa Created on 10/24/2020
 */

public class AutomationLibrary {
	final static Logger logger = Logger.getLogger(AutomationLibrary.class);

	private WebDriver driver;
	private boolean isDemoMode = false; // highlighting elements method
	private boolean isChromeHeadless = false;

	public boolean getChromeHeadless() {
		return isChromeHeadless;
	}

	public void setChromeHeadless(boolean _isChromeHeadless) {
		this.isChromeHeadless = _isChromeHeadless;
	}

	private boolean isRemote = false;

	public boolean getIsRemote() {
		return isRemote;
	}

	public void setIsRemote(boolean _isRemote) {
		this.isRemote = _isRemote;
	}

	private boolean isBrowserTypeFirefox;
	public List<String> errorScreenshots;

	// getter removed (isDemoMode)
	public void setDemoMode(boolean isDemoMode) {
		this.isDemoMode = isDemoMode;
	}

	enum Browser {
		IE, FIREFOX, CHROME, SAFARI, EDGE
	}

	public AutomationLibrary() {

	}

	public AutomationLibrary(WebDriver _driver) {
		driver = _driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver _driver) {
		try {
			if (_driver != null) {
				this.driver = _driver;
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	private WebDriver startRemoteChromeBroser(String hubURL) {
		try {
			DesiredCapabilities cap = new DesiredCapabilities();
			ChromeOptions chromeOps = new ChromeOptions();
			if (isChromeHeadless) {
				chromeOps.setHeadless(true);
			}
			chromeOps.merge(cap);
			driver = new RemoteWebDriver(new URL(hubURL), chromeOps);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	private WebDriver startRemoteFirefoxBroser(String hubURL) {
		try {
			DesiredCapabilities cap = new DesiredCapabilities();
			FirefoxOptions firefoxOps = new FirefoxOptions();
			firefoxOps.merge(cap);
			driver = new RemoteWebDriver(new URL(hubURL), firefoxOps);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver startRemoteBrowser(String hubURL, Browser browser) {
		try {
			switch (browser) {

			case CHROME:
				driver = startChromeBrowser();
				break;

			case FIREFOX:
				driver = startFirefoxBrowser();
				break;

			default:
				System.err.println("Currently we are not supporting this browser type");
				System.err.println("Default browser set to Chrome.");
				driver = startChromeBrowser();
				break;
			}

		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver startBrowser(Browser browser) {
		try {
			switch (browser) {

			case CHROME:
				driver = startChromeBrowser();
				break;

			case FIREFOX:
				driver = startFirefoxBrowser();
				break;

			default:
				logger.error("Currently we are not suporting this browser type!");
				logger.error("Default browser set to Chrome.");
				driver = startChromeBrowser();
				break;
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	/***
	 * This method starts/launch a Chrome Browser
	 * 
	 * @return WebDriver
	 */
	public WebDriver startChromeBrowser() {
		try {
			WebDriverManager.chromedriver().setup();
			//System.setProperty("webdriver.chrome.driver", "src/test/resources/browserDrivers/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			logger.debug("maximize the browser");
			driver.manage().window().maximize();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	/***
	 * This method starts/launch a Firefox Browser
	 * 
	 * @return WebDriver
	 */

	private WebDriver startFirefoxBrowser() {
		try {
			logger.debug("make sure, use firefox system variable and path");
		//	System.setProperty("webdriver.gecko.driver", "src/test/resources/browserDrivers/geckodriver.exe");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.manage().window().maximize();
			isBrowserTypeFirefox=true;
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public void equalAssert(By by, String expected) {
		WebElement actualTxt = driver.findElement(by);
		String actualAssert = actualTxt.getText();
		assertEquals(actualTxt, expected);
	}
	
	public void multipleClick(By by, int x) {
		WebElement clickElem = driver.findElement(by);
		for(int i=0; i<x; i++) {
			clickElem.click();
		}
	}
	
	public void multipleClick(WebElement element, int x) {
		WebElement clickElem = element;
		for(int i=0; i<x; i++) {
			clickElem.click();
		}
	}
	
	public void hoverOver(By by) {
		WebElement elem = driver.findElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(elem).build().perform();
	}
	
	public void scrollToElement(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", element);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void scrollUpDown(int pixels) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(0," + pixels + ")");
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void scrollRightLeft(int pixels) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(" + pixels + ",0)");
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void highlightElement(WebElement element) {
		if (isDemoMode == true) {
			try {
				for (int i = 0; i < 4; i++) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
							"color: red; border: 2px solid yellow");
					customWait(0.5);
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
					customWait(0.5);
				}
			} catch (Exception e) {
				logger.error("Error: ", e);
				assertTrue(false);
			}
		}
	}

	public WebElement waitForElementVisibility(By by) {
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return elem;
	}

	public WebElement waitForElementPresence(By by) {
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			elem = wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return elem;
	}

	/**
	 * Explicit wait for an element to be clickable
	 * 
	 * @param by
	 * @return WebElement
	 */
	public WebElement waitForElementToBeClickable(By by) {
		WebElement elem = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			elem = wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return elem;
	}

	public void enterTxt(By by, String inputTxt) {
		try {
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(inputTxt);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void enterTxt(WebElement element, String inputTxt) {
		try {
			element.clear();
			element.sendKeys(inputTxt);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	/*
	 * public void enterTxt(By by) { try { WebElement element = null;
	 * element.clear(); } catch (Exception e) { logger.error("Error: ", e);
	 * assertTrue(false); } }
	 */

	public void clickElement(By by) {
		try {
			WebElement elem = driver.findElement(by);
			elem.click();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void selectDropDown(By by, String optionValue) {
		try {
			WebElement elem = driver.findElement(by);
			Select dropdown = new Select(elem);
			dropdown.selectByVisibleText(optionValue);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void selectDropDown(WebElement element, String optionValue) {
		try {
			Select dropdown = new Select(element);
			dropdown.selectByVisibleText(optionValue);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void customWait(double inSeconds) {
		try {
			Thread.sleep((long) (inSeconds * 1000));
			logger.debug("Thread.sleep does not receive double so we convert it to long");
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}
	
	
	public void sendKey(By by, WebElement element, String input) {
		try {
			WebElement elem = driver.findElement(by);
			elem.clear();
			elem.sendKeys(input);
			
		}catch(Exception e) {
			logger.error("Error:", e);
			assertTrue(false);
		}
	}
	
	public void sendKey(By by, String input) {
		try {
			WebElement elem = driver.findElement(by);
			elem.click();
			elem.clear();
			elem.sendKeys(input);
		}catch(Exception e) {
			logger.error("Error:", e);
			assertTrue(false);
		}
	}
	
	public void sendKey(By by) {
		try {
			WebElement elem = driver.findElement(by);
			elem.click();
			elem.clear();
			elem.sendKeys();
		}catch(Exception e) {
			logger.error("Error:", e);
			assertTrue(false);
		}
	}

	/**
	 * Method returns current timeStamp
	 * 
	 * @return String
	 */
	public String getCurrentTime() {
		String finalTime = null;
		try {
			Date date = new Date();
			String tempTime = new Timestamp(date.getTime()).toString();
			logger.debug("Time: " + tempTime);
			finalTime = tempTime.replace("-", "").replace(" ", "").replace(":", "").replace(".", "");
			logger.info("getCurrentTime() ---> " + finalTime);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return finalTime;
	}

	/**
	 * This method return total number of iframe if they exist, if not it will
	 * return zero
	 * 
	 * @return int
	 */
	public int getAlliframes() {
		int totalIframe = 0;
		try {
			List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
			totalIframe = iframes.size();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return totalIframe;
	}

	public WebDriver switchToIframeByIdex(int index) {
		try {
			driver = driver.switchTo().frame(index);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver switchToBrowserByIndex(int index) {
		int totalBrowsers = 0;
		try {
			Set<String> setBrowsers = driver.getWindowHandles();
			totalBrowsers = setBrowsers.size();
			if (index < totalBrowsers) {
				List<String> listBrowsers = new ArrayList<String>();
				for (String browser : setBrowsers) {
					listBrowsers.add(browser);
				}
				String windowName = listBrowsers.get(index);
	//			driver.close();
				driver = driver.switchTo().window(windowName);
			} else {
				int tempBrowsers = index + 1;
				logger.info("There are only [" + totalBrowsers + "] open browser available, "
						+ "can't switch to browser number [" + tempBrowsers + "], that doesn't exit!");
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public WebDriver switchIframe(String htmlAttributeName, String htmlAttributeValue) {
		try {
			String value = "";
			List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
			for (WebElement element : iframes) {
				if (htmlAttributeName.toLowerCase().contains("class")) {
					value = element.getAttribute("class");
					if (value.contains(htmlAttributeValue)) {
						driver = driver.switchTo().frame(element);
						break;
					}
				} else if (htmlAttributeName.toLowerCase().contains("for")) {
					value = element.getAttribute("for");
					if (value.contains(htmlAttributeValue)) {
						driver = driver.switchTo().frame(element);
						break;
					}
				} else if (htmlAttributeName.toLowerCase().contains("id")) {
					value = element.getAttribute("id");
					if (value.contains(htmlAttributeValue)) {
						driver = driver.switchTo().frame(element);
						break;
					}
				} else if (htmlAttributeName.toLowerCase().contains("name")) {
					value = element.getAttribute("name");
					if (value.contains(htmlAttributeValue)) {
						driver = driver.switchTo().frame(element);
						break;
					}
				} else if (htmlAttributeName.toLowerCase().contains("src")) {
					value = element.getAttribute("src");
					if (value.contains(htmlAttributeValue)) {
						driver = driver.switchTo().frame(element);
						break;
					}
				} else {
					logger.info("Error ---- ");
					logger.info("The parameter html attirbute name [" + htmlAttributeName
							+ "], is not yet supported at this time.");
					logger.info("Please check method [switchIframe()] under 'GlobalSeleniumLibrary' class.");
				}
				logger.info("attribute [" + htmlAttributeName + "], value [" + value + "]");
				logger.info("parameter attribute_value: " + htmlAttributeValue);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}

	public void handleCheckBox(By by, boolean isCheck) {
		// scenarios
		// 1) user wants to check the check-box,
		// - I) check-box is NOT checked by default ==>
		// - II) check-box is already checked by default ==>

		// 2)user wants to un-check the check-box,
		// - III) check-box is NOT checked by default ==>
		// - IV) check-box is already checked by default ==>
		try {
			WebElement checkboxElem = driver.findElement(by);
			handleCheckBox(checkboxElem, isCheck);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void handleCheckBox(WebElement element, boolean isCheck) {
		try {
			boolean checkboxStatus = element.isSelected();
			if (isCheck == true) {
				if (checkboxStatus == false) {
					// scenario 1: ---> click the check-box
					element.click();
					customWait(0.5);
				} else {
					// scenario 2: ---> do nothing
				}
			} else {
				if (checkboxStatus == false) {
					// scenario 3: ---> do nothing
				} else {
					// scenario 4: ---> click the check-box to un-check
					element.click();
					customWait(0.5);
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public void captureScreenshot(String screnshotFileName, String filePath) {
		String finalScreenshotPath = null;
		try {
			String timeStamp = getCurrentTime();
			if (filePath.isEmpty()) {
				checkDirectory("target/screenshots/");
				finalScreenshotPath = "target/screenshots/" + screnshotFileName + "_" + timeStamp + ".png";
			} else {
				checkDirectory(filePath);
				finalScreenshotPath = filePath + screnshotFileName + "_" + timeStamp + ".png";
			}
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(scrFile, new File(finalScreenshotPath));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		String fullPath = getAbsulatePath(finalScreenshotPath);
		logger.info("Screenshot location: " + fullPath);
	}

	private void checkDirectory(String inputPath) {
		File file = new File(inputPath);
		String abPath = file.getAbsolutePath();
		File file2 = new File(abPath);
		try {
			if (!file2.exists()) {
				if (file2.mkdirs()) {
					logger.info("Directories are created...");
				} else {
					logger.info("Directories are NOT created...");
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	private String getAbsulatePath(String inputPath) {
		String abPath = null;
		try {
			File file = new File(inputPath);
			abPath = file.getAbsolutePath();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return abPath;
	}

	public boolean isBrowserTypeFirefox() {
		return isBrowserTypeFirefox;
	}

	public void setBrowserTypeFirefox(boolean isBrowserTypeFirefox) {
		this.isBrowserTypeFirefox = isBrowserTypeFirefox;
	}

	public void uploadFile(String filePath, By by) {
		String absoluteFilePath = null;
		try {
			File file = new File(filePath);
			absoluteFilePath = file.getAbsolutePath();
			WebElement fileUpload = driver.findElement(by);
			fileUpload.sendKeys(absoluteFilePath);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		logger.info("file uploaded: " + absoluteFilePath);
	}

	public void clickElement(WebElement element) {
		try {
			element.click();
		} catch (StaleElementReferenceException stale) {
			System.out.println("Element is stale. Clicking again");
			// element = reInitializeStaleElement(element);
			element.click();
		}
	}

	public void closeBrowsers() {
		if (driver != null) {
			driver.close();
			// if (isBrowserTypeFirefox) { // this line of code is same as below line
			if (isBrowserTypeFirefox == true) {
				// driver.quit();
			} else {
				driver.quit();
			}
		}
	}

	public static void main(String[] args) {
		AutomationLibrary autoLibe = new AutomationLibrary();
		autoLibe.getCurrentTime();
		// difference between mkdir() vs mkdirs()

		// let's assume using mkdir() --> result: only "c:/abc" folder would be created
		autoLibe.checkDirectory("c:/abc/aaa/a1/2/1");

		// let's assume using mkdirs() --> result: it creates full folder and
		// sub-folders like 'c:/abc/aaa/a1/2/1'
		autoLibe.checkDirectory("c:/abc/aaa/a1/2/1");
	}

	
       // check if folder path is exist, not create the path
	/*
	 * String tempInput = screnshotFilePath; String folders = tempInput.substring(0,
	 * tempInput.lastIndexOf("/") + 1); String imageName =
	 * tempInput.substring(tempInput.lastIndexOf("/") + 1, (tempInput.length()));
	 * 
	 * File filePath = new File(folders); if (!filePath.exists()) {
	 * filePath.mkdirs(); }
	 */
}