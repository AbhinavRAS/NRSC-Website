/*
  * To change this license header, choose License Headers in Project
Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
package ehiring.action;

/**
  *
  * @author SaiKalpana
  */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteToExcel {

	public static void main(String[] args) throws IOException {

		ArrayList<String> arrayList = new ArrayList<String>();
		Object[] objectList = arrayList.toArray();
		String[] stringArray = Arrays.copyOf(objectList, objectList.length, String[].class);
		writeFileUsingPOI();
	}

	public static void writeFileUsingPOI() throws IOException {
		// create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Country");

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		data.add(new String[] { "Country", "Capital", "Population" });
		data.add(new Object[] { "India", "Delhi", 10000 });
		data.add(new Object[] { "France", "Paris", 40000 });
		data.add(new Object[] { "Germany", "Berlin", 20000 });
		data.add(new Object[] { "England", "London", 30000 });

		// Iterate over data and write to sheet
		int rownum = 0;
		for (Object[] countries : data) {
			Row row = sheet.createRow(rownum++);

			int cellnum = 0;
			for (Object obj : countries) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if (obj instanceof Double) {
					cell.setCellValue((Double) obj);
				} else if (obj instanceof Integer) {
					cell.setCellValue((Integer) obj);
				}
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File("j://CountriesDetails.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println(CurrentDateTime.dateTime()+":"+"CountriesDetails.xlsx has been created successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public  void writeFileApplicant(ArrayList app,String flName) throws IOException {
		// create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Applicants");

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		for (int i = 0; i < app.size(); ++i) {
			ArrayList ls = (ArrayList) app.get(i);
			Object[] obj = ls.toArray();
			data.add(obj);
		}

		// Iterate over data and write to sheet
		int rownum = 0;
		for (Object[] applicant : data) {
			Row row = sheet.createRow(rownum++);

			int cellnum = 0;
			for (Object obj : applicant) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if (obj instanceof Double) {
					cell.setCellValue((Double) obj);
				} else if (obj instanceof Integer) {
					cell.setCellValue((Integer) obj);
				}
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(flName));
			workbook.write(out);
			out.close();
			System.out.println(CurrentDateTime.dateTime()+":"+"Applicants.xlsx has been created successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
