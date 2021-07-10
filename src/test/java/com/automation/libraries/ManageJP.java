package com.automation.libraries;

import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ManageJP {
	
	final static Logger logger = Logger.getLogger(ManageJP.class);
	
	private String propertiesFile;
	private Properties pr;
	private FileInputStream input;
	private FileOutputStream output;
	//------input is reading(read data from hard drive to computer memory)
	//-----output is writing(write sth from computer memory out to the hard drive)

	public ManageJP(String propertiesFilePath) {
		try {
			if (propertiesFilePath.length() > 0) {
				propertiesFile = propertiesFilePath;
				pr = new Properties();
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}

	public String readProperty(String key) {
		{
			String value = null;
			try {
				input = new FileInputStream(propertiesFile);
				pr.load(input);
				value = pr.getProperty(key);

			} catch (Exception e) {
				logger.error("Error: ", e);
				assertTrue(false);
			} finally {
				try {
					if (input != null) {
						input.close();
					}
				} catch (Exception e) {
					logger.error("Error: ", e);
					assertTrue(false);
				}
			}
			return value;
		}
	}

	public void setProperty(String key, String value) {
		try {
			output = new FileOutputStream(propertiesFile);
			pr.setProperty(key, value);
			pr.store(output, null);
			logger.info("Properties file created---> " + propertiesFile);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				logger.error("Error: ", e);
				assertTrue(false);
			}
		}
	}

	public void setProperty(String key, String value, String comments) {
		try {
			output = new FileOutputStream(propertiesFile);
			pr.setProperty(key, value);
			pr.store(output, comments);
			logger.info("Properties file created---> " + propertiesFile);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				logger.error("Error: ", e);
				assertTrue(false);
			}
		}
	}
	public static void main(String[] args) {
		
//		  JavaPropertiesManager property = new JavaPropertiesManager("src/test/resources/log4j.properties"); 
//		  String data = property.readProperty("log4j.appender.stdout.layout.ConversionPattern"); 
//		  logger.info("data: " + data);
		 
		  ManageJP property = new ManageJP("src/test/resources/config.properties");
		  property.setProperty("browserType", "Firefox");
		  property.setProperty("demoMode", "false", 
				  "Demo mode true enables highlighting WebElement and slows down the test execution.");
	}
}