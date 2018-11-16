package org.seuksa.frmk.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author mov.seng
 * @version $Revision$
 * 
 */
public final class MyEmailUtils {
	
	
	
	// Should be moved into Framework
    private static final String EMAIL_PATTERN_SIMPLE = ".+@.+\\.[a-z]+";
    private static final String EMAIL_PATTERN_COMPLEXE = 
    		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
    		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String EMAIL_PATTERN = EMAIL_PATTERN_SIMPLE;
   
	
    
	/**
	 * Return true if the given email is valid
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmailValid(String email) {
		return isEmailValid(email, EMAIL_PATTERN);
	}
	
	/**
	 * 
	 * @param email
	 * @param sPattern
	 * @return
	 */
	public static boolean isEmailValid(String email, String sPattern) {
    	Pattern pattern = Pattern.compile(sPattern);
    	Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param textEmail
	 * @return
	 */
	public static String unescapeHtmlSubject(String textEmail) {
		
		return textEmail.replace("’", "'"); 
	}
	 
	/**
	 * 
	 * @param textEmail
	 * @return
	 */
	 public static String unescapeHtmlBody(String textEmail) {
		 return textEmail.replace("’", "'");
	 }

}
