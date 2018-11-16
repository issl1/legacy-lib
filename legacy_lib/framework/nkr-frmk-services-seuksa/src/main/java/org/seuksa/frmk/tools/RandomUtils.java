package org.seuksa.frmk.tools;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author prasnar
 *
 */
public final class RandomUtils {
	
	/**
	 * 
	 * @return
	 */
	public static String randomName(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Date randomDate() {
    	int month, year, day;  
        Random call = new Random();  
        month = call.nextInt(11);  
        year = call.nextInt(50) + 1950;  // between 1950 et 2020
        day  = call.nextInt(30);  
    
         GregorianCalendar calendar = new GregorianCalendar(year, month, day);  
         Date dt = calendar.getTime();
         return dt;
    }
	
	
}
