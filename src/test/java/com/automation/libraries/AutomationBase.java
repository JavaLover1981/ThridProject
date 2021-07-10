package com.automation.libraries;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.automation.libraries.AutomationLibrary.Browser;

public class AutomationBase {
	final static Logger logger = Logger.getLogger(AutomationBase.class);

	public static WebDriver driver;
	public static AutomationLibrary autoLibrary;
	ManageJP javaProperty = new ManageJP("src/test/resources/tShirtPageLocators.properties");
	ObjectStorageMap osMap = new ObjectStorageMap("src/test/resources/locators/mainPageLocators.properties");
//	private String browserType;
//	private String hubURL;
	@BeforeClass
	public void beforeAllTests() {
		autoLibrary = new AutomationLibrary(driver);
	//CHECK CHECK-->	autoLibrary = new AutomationLibrary(driver);

		// test suite start time
		String tempTestStartTime = autoLibrary.getCurrentTime();
		ManageJP sessionTimeProp = new ManageJP(
				"src/test/resources/sessionConfig.properties");
		sessionTimeProp.setProperty("sessionTime", tempTestStartTime);

//		String demoModePropValue = javaProperty.readProperty("demoMode");
//		if (demoModePropValue.contains("on")) {
//			autoLibrary.setDemoMode(true);
//		}
	//	browserType = javaProperty.readProperty("browserType");
	//	hubURL = javaProperty.readProperty("hubURL");
	//	logger.info("hubURL is: [" + hubURL + "]");

//		String remoteRun = javaProperty.readProperty("isRemote");
//		if (remoteRun.toLowerCase().contains("on")) {
//			autoLibrary.setIsRemote(true);
//		}
//
//		String headless = javaProperty.readProperty("isHeadless");
//		if (headless.toLowerCase().contains("on")) {
//			autoLibrary.setChromeHeadless(true);
//		}
	}

	@AfterClass
	public void afterAllTests() {
		try {
			if (driver != null) {
				driver.close();

				// if(isBrowserTypeFirefox==true) {
				// }else {
				driver.quit();
				// }
			}
			logger.info("Tests are ended...");
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		
//		ManageJP configProp = new ManageJP("src/test/java/com/object/repository/mainPage.properties");
//		String isSendEmail = configProp.readProperty("sendEmail");
//		if (isSendEmail.toLowerCase().contains("on")) {
//			// Sending emails...
//			EmailManager email = new EmailManager();
//			email.toAddress = "renaamet@gmail.com";
//			email.ccAddress = "mypulum@gmail.com";
//			autoLibrary.errorScreenshots.add("target/logs/log4j-selenium.log");
//			autoLibrary.errorScreenshots.add("target/logs/Selenium-Report.html");
//			email.sendEmail(autoLibrary.errorScreenshots);
//		}
	}
	
	@BeforeMethod
	// this method runs/executes depending on how many tests you
	// have in your test class
	// before each test starts - setting up browser
	public void setup() {
		logger.info("Test started...");

	//	driver=autoLibrary.getDriver();
		driver=autoLibrary.startChromeBrowser();
	//	driver.get(hubURL);
		
//		if (autoLibrary.getIsRemote()) {
//			if (browserType.toLowerCase().contains("chrome")) {
//				driver = autoLibrary.startRemoteBrowser(hubURL, Browser.CHROME);
//			} else if (browserType.toLowerCase().contains("Firefox")) {
//				driver = autoLibrary.startRemoteBrowser(hubURL, Browser.FIREFOX);
//			} else {
//				logger.info("starting default browser as [Chrome].");
//				driver = autoLibrary.startRemoteBrowser(hubURL, Browser.CHROME);
//			}
//		} else {
//			if (browserType.toLowerCase().contains("chrome")) {
//				driver = autoLibrary.startBrowser(Browser.CHROME);
//			} else if (browserType.toLowerCase().contains("Firefox")) {
//				driver = autoLibrary.startBrowser(Browser.FIREFOX);
//			} else {
//				logger.info("starting default browser as [Chrome].");
//				driver = autoLibrary.startBrowser(Browser.CHROME);
//			}
//		}
	}

	@AfterMethod
	// after each test is completed, cleaning up - close the browser
	// capture screenshots only if there is test failure.
	public void close(ITestResult result) {
		try {
			Thread.sleep(5*1000);
			if (ITestResult.FAILURE == result.getStatus()) {
				
				String fileFolder = javaProperty.readProperty("fileFolder");
				LocalDate today = LocalDate.now();
				autoLibrary.captureScreenshot(fileFolder, result.getName() + "_" + today);
				logger.info("ScreenShot taken at" + fileFolder);
				logger.info("========failed=====");
				// test failed, call capture screenshot method
				//autoLibrary.captureScreenshot(result.getName(), "");
			}
			autoLibrary.closeBrowsers();
			logger.info("Test is ended...");
		} catch (Exception e) {
			logger.error("Error: ", e);
			autoLibrary.closeBrowsers();
		}
	}
}
