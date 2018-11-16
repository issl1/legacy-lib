package org.seuksa.frmk.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class MyStringUtils {
	public static Logger logger = LoggerFactory.getLogger(MyStringUtils.class);
	public static String LIST_SEPARATOR_COMMA = ",";

	/**
	 * Default separator is comma
	 * @param srcValues
	 * @return
	 */
	public static List<Long> getValuesLong(String srcValues) {
		return getValuesLong(srcValues, LIST_SEPARATOR_COMMA);
	}
	
	/**
	 * Default separator is comma
	 * @param srcValues
	 * @return
	 */
	public static List<Integer> getValuesInt(String srcValues) {
		return getValuesInt(srcValues, LIST_SEPARATOR_COMMA);
	}
		
	/**
	 * Default separator is comma
	 * @param srcValues
	 * @param sep
	 * @return
	 */
	public static List<Long> getValuesLong(String srcValues, String sep) {

        List<Long> lstIds = new ArrayList<Long>();
		if (srcValues == null) {
			return lstIds;
		}
		
		String sepString = null;
		if (StringUtils.isEmpty(sep)) {
			sepString = LIST_SEPARATOR_COMMA;
		} else {
			sepString = sep;
		}
		
        String[] values = srcValues.replace(" ", "").split(sepString);
        for (String val : values) {
        	Long nb = 0L;
            try {
                nb = Long.valueOf(val);
                if (nb > 0) {
                    lstIds.add(nb);
                } else {
                    // Warn but no add
                    logger.warn("The string [" + srcValues+ "] shall contains a list of Long (> 0) separated by \",\". Check in the Application Advanced Configuration.");
                }
            } catch (Exception e) {
                // Warn but no add
                logger.warn("The string [" + srcValues+ "] shall contains a list of Long (> 0) separated by \",\". Check in the Application Advanced Configuration.");
            }
        }
        return lstIds;
	}
	
	/**
	 * Default separator is comma
	 * @param srcValues
	 * @param sep
	 * @return
	 */
	public static List<Integer> getValuesInt(String srcValues, String sep) {

        List<Integer> lstIds = new ArrayList<Integer>();
		if (srcValues == null) {
			return lstIds;
		}
		
		String sepString = null;
		if (StringUtils.isEmpty(sep)) {
			sepString = LIST_SEPARATOR_COMMA;
		} else {
			sepString = sep;
		}
		
        String[] values = srcValues.replace(" ", "").split(sepString);
        for (String val : values) {
        	Integer nb = 0;
            try {
                nb = Integer.valueOf(val);
                if (nb > 0) {
                    lstIds.add(nb);
                } else {
                    // Warn but no add
                    logger.warn("The string [" + srcValues+ "] shall contains a list of Integer (> 0) separated by \",\". Check in the Application Advanced Configuration.");
                }
            } catch (Exception e) {
                // Warn but no add
                logger.warn("The string [" + srcValues+ "] shall contains a list of Integer (> 0) separated by \",\". Check in the Application Advanced Configuration.");
            }
        }
        return lstIds;
	}
}
