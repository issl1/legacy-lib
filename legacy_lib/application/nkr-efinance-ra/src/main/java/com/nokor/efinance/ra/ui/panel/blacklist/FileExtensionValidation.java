package com.nokor.efinance.ra.ui.panel.blacklist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate file extension 
 * @author uhout.cheng
 */
public class FileExtensionValidation {
 
	private static Pattern fileExtnPtrn = Pattern.compile("([^\\s]+(\\.(?i)(csv))$)");
     
	/**
	 * 
	 * @param fileName
	 * @return
	 */
    public static boolean validateFileExtn(String fileName){
        Matcher mtch = fileExtnPtrn.matcher(fileName);
        if(mtch.matches()){
            return true;
        }
        return false;
    }
}
