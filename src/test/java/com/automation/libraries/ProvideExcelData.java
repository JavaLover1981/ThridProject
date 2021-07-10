package com.automation.libraries;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProvideExcelData {
	final static Logger logger = Logger.getLogger(ProvideExcelData.class);

//	private static String filePath;
	XSSFWorkbook wb;
//	private static Sheet sh;

	public ProvideExcelData() throws IOException {
		try {
			File src = new File("./TestData/AutomationTestCase.xlsx");
			FileInputStream fis = new FileInputStream(src);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}
	
	public String getStringData(int sheetIndex, int row, int column) {
		return wb.getSheetAt(sheetIndex).getRow(row).getCell(column).getStringCellValue();
	}

	public String getStringData(String sheetName, int row, int column) {
		return wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
	}

	public double getNumericData(String sheetName, int row, int column) {
		return wb.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
	}
}