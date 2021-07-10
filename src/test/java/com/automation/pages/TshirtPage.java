package com.automation.pages;

import org.openqa.selenium.By;
import org.testng.log4testng.Logger;

import com.automation.libraries.AutomationBase;
import com.automation.libraries.ManageJP;

public class TshirtPage extends AutomationBase{

	final static Logger logger = Logger.getLogger(TshirtPage.class);
	ManageJP javaProperty = new ManageJP("src/test/resources/locators/tShirtPageLocators.properties");
	
	public TshirtPage chooseColor() throws Exception {
		autoLibrary.clickElement(By.cssSelector(javaProperty.readProperty("blueColor")));
		return this;
	}
	public TshirtPage pickSize() throws Exception {
		autoLibrary.selectDropDown(By.id(javaProperty.readProperty("shirtSize")), "2");
		return this;
	}
	
	public TshirtPage addToCart() throws Exception {
		autoLibrary.clickElement(By.id(javaProperty.readProperty("addToCart")));
		return this;
	}
}
