package com.nokor.efinance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 
 * @author buntha.chea
 *
 */
public class ReadFileFromSubDistrictParam {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			 PrintWriter out = new PrintWriter("/home/buntha.chea/WORK/Document/SubDistrict.txt");//Path to put new file (.txt)
			 File excel =  new File ("/home/buntha.chea/WORK/Project/GLF_Thai_New/nkr-efinance-th/trunk/doc/From Customer/Parameters/Parameter & System Config.xlsx");
		        FileInputStream fis = new FileInputStream(excel);
		        XSSFWorkbook wb = new XSSFWorkbook(fis);
		        XSSFSheet ws = wb.getSheet("PARAM_SUBDISTRICT");

		        int rowNum = ws.getLastRowNum() + 1;
		        
		        for (int i = 0; i <rowNum; i++) {
		            XSSFRow row = ws.getRow(i);
		            
		            XSSFCell celldisId = row.getCell(0);
		            XSSFCell cellDesc = row.getCell(2);
		            XSSFCell cellDescEn = row.getCell(3);
		            XSSFCell cellProId = row.getCell(4);
                   
		            String desc = cellDesc.toString();
                    String descEn = cellDescEn.toString();
                    String disId = cellProId.toString().replace(".0", "");
                    String subDisId = celldisId.toString().replace(".0", "");
                    
                    String code = convertCode(i);
                    System.out.println (subDisId + "," + code + "," + desc + "," + descEn + "," + disId);
                    //insert data to file 
                    out.println("(" + subDisId + ", '" + code + "'" + ", '" + desc + "'" + ", '" + descEn + "'" + ", " + disId + ", " + 1 + ", " + 1 + ", now()" + ", 'admin'" + ", now()" + ", 'admin'" + ", 'BI'" + ")" + ",");
		        }
		        out.close();
		       
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String convertCode(int id) {
		int leagth = String.valueOf(id).length();
		String result = "";
		if (leagth == 1) {
			result = "000" + id;
		} else if (leagth == 2) {
			result = "00" + id;
		} if (leagth == 3) {
			result = "0" + id;
		} else {
			result = String.valueOf(id);
		}
		return result;
	}
}
