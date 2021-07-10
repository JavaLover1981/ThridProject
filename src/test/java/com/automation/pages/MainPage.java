package com.automation.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.automation.libraries.AutomationBase;
import com.automation.libraries.ManageJP;
import com.automation.libraries.ObjectStorageMap;

public class MainPage extends AutomationBase {
	final static Logger logger = Logger.getLogger(MainPage.class);
	ObjectStorageMap osMap = new ObjectStorageMap("src/test/resources/locators/mainPageLocators.properties");

	public MainPage gotoMainPage() {
		try {
			driver.get("http://automationpractice.com/index.php");
			logger.info("My Store website URL printed");
			String actualWebsiteTitle = driver.getTitle();
			logger.info("Current website title is: " + actualWebsiteTitle);

			String expectedTitle = "My Store";
			assertEquals(actualWebsiteTitle, expectedTitle);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return this;
	}

	public MainPage searchItem() throws Exception {
		autoLibrary.sendKey(osMap.getLocator("searchField"), "Dress");
		logger.info("");
		return this;
	}
	
	public MainPage clickSearchBtn() throws Exception {
		autoLibrary.clickElement(osMap.getLocator("searchSubmitBtn"));
		logger.info("");
		return this;
	}

	public MainPage verifyResult(String text) throws Exception {
		driver.findElement(osMap.getLocator("verifyResult"));
		driver.getPageSource().contains("7 results have been found.");
		logger.info("");
		return this;
	}

	public MainPage clickTSHIRTS() throws Exception {
		autoLibrary.clickElement(osMap.getLocator("clickTSHIRT"));
		logger.info("");
		return this;
	}

	public void navigateToResultPage() throws Exception {
//		autoLibrary.waitForElementVisibility(osMap.getLocator("resultPage"));
//		driver.getPageSource().contains("Faded Short Sleeve T-shirts");
//		logger.info("");
		
		WebElement elem = autoLibrary.waitForElementVisibility(osMap.getLocator("resultPage"));
		String elemText = elem.getText();
		logger.info("The text is :" + elemText);
		assertEquals(elemText, "Faded Short Sleeve T-shirts");
		
		//xpath: //h1[contains(text(),'Faded Short Sleeve T-shirts')]
		//xpath: //h1[contains(text(),'Short')]
		//xpath: //h1[text()='Faded Short Sleeve T-shirts']
		//-->css: h1[itemprop='name']
	}
}