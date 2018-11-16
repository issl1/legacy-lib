package com.nokor.frmk.vaadin.ui.layout;

import java.io.InputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.vaadin.util.VaadinConfigHelper;
import com.vaadin.ui.CustomLayout;

/**
 * 
 * @author prasnar
 *
 */
public class LayoutHelper implements Serializable {
	/** */
	private static final long serialVersionUID = -726298270197052196L;

	private static final Logger logger = LoggerFactory.getLogger(LayoutHelper.class);

	private static final String LAYOUT_PATH = VaadinConfigHelper.getLayoutFolder();
	private static final String SLASH = "/";
	private static final String HTML_SUFFIX = ".html";

	/**
	 * 
	 */
	private LayoutHelper() {
	}
	
	/**
	 * 
	 * @param templatePath
	 * @return
	 */
	public static CustomLayout createCustomLayout(String templatePath) {
		String templateFullPath = "";
		CustomLayout formLayout = null;
		try {
			templateFullPath = getTemplateFullPath(templatePath);
			InputStream layoutFile = LayoutHelper.class.getResourceAsStream(templateFullPath);
			formLayout = new CustomLayout(layoutFile);
		} catch (Exception e) {
			String errMsg = "Could not locate template [" + templateFullPath + "]";
			logger.error(errMsg, e);
		}
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getTemplateFullPath(String templatePath) {
		return LAYOUT_PATH  + templatePath + HTML_SUFFIX;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getLayoutFolder() {
		return LAYOUT_PATH;
	}
}
