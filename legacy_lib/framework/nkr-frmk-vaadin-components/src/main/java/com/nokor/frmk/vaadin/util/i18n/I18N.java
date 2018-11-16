package com.nokor.frmk.vaadin.util.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seuksa.frmk.tools.vo.ValuePair;

import com.vaadin.ui.UI;

/**
 * @author ly.youhort
 */
public final class I18N  {

	private static Map<Locale, Map<String, String>> resources = new HashMap<>();

	/**
	 * @return
	 */
	public static char groupingSepator() {
		final String gs = message("grouping_separator");
		return gs.charAt(gs.length() - 1);
	}

	/**
	 * 
	 * @return
	 */
	public static char decimalSepator() {
		final String gs = message("decimal_separator");
		return gs.charAt(gs.length() - 1);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String message(final String code) {
		Map<String, String> bundle = resources.get(getLocale());
		if (bundle != null && bundle.containsKey(code)) {
			return bundle.get(code);
		} else {
			return org.seuksa.frmk.i18n.I18N.message(code);
		}
	}

	/**
	 * 
	 * @param key
	 * @param vals
	 * @return
	 */
	public static String message(final String code, final String... vals) {
		String message = message(code);
		try {
			for (int i = 0; i < vals.length; i++) {
				message = message.replaceAll("\\{" + i + "\\}", vals[i]);
			}
		} catch (final Throwable t) {
		}
		return message;
	}

	/**
	 * @param code
	 * @return
	 */
	public static String value(final String code) {
		return message(code);
	}

	/**
	 * 
	 * @param messages
	 */
	public static void initBundle(final ValuePair[] messages, Locale locale) {
		Map<String, String> bundle = new HashMap<>();
		for (final ValuePair val : messages) {
			bundle.put(val.getCode(), val.getValue());
		}
		resources.put(locale, bundle);
	}

	private static Locale getLocale() {
		UI currentUI = UI.getCurrent();
		Locale locale = (currentUI == null ? null : currentUI.getLocale());
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		return locale;
	}
}