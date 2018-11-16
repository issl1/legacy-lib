package org.seuksa.frmk.tools;

import java.text.DecimalFormat;

/**
 * CurrencyUtils
 * @author kong.phirun
 *
 */
public final class CurrencyUtils {

    private CurrencyUtils() {
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static String euroWithDecimal(final Double value) {
	    DecimalFormat formatter = new DecimalFormat("###,###,###,###.##");
	    String fm = formatter.format(value);
	    return fm + " €";
    }
}
