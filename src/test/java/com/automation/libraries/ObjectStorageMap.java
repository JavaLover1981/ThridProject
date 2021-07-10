package com.automation.libraries;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;

public class ObjectStorageMap {
	Properties properties;

	public ObjectStorageMap(String filePath) {
		properties = new Properties();
		try {
			FileInputStream Master = new FileInputStream(filePath);
			properties.load(Master);
			Master.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public By getLocator(String ElemName) throws Exception {
		String locator = properties.getProperty(ElemName);
		// System.out.println(locator);
		String locatorType = locator.split(":")[0];
		// System.out.println(locatorType);
		String locatorValue = locator.split(":")[1];
		// System.out.println(locatorValue);

		if (locatorType.toLowerCase().equals("id"))
			return By.id(locatorValue);
		else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);

		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return By.tagName(locatorValue);

		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return By.linkText(locatorValue);

		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return By.partialLinkText(locatorValue);

		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return By.cssSelector(locatorValue);

		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);
		else
			throw new Exception("Locator type '" + locatorType + "' not defined!!");
	}
}
