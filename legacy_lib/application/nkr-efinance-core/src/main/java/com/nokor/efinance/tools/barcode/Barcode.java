package com.nokor.efinance.tools.barcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

/**
 * @author bunlong.taing
 */
public class Barcode {
	
	private static final String PATH_CONFIG = "barcode-cfg.xml";
	
	private static Configuration cfg;

	/**
	 * @param msg
	 * @param path
	 */
	public static void create(String msg, File file) {
		BarcodeGenerator gen;
		try {
			gen = BarcodeUtil.getInstance().createBarcodeGenerator(getConfig());
			OutputStream out = new FileOutputStream(file);
			BitmapCanvasProvider provider = new BitmapCanvasProvider(
			    out, "image/x-png", 300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
			gen.generateBarcode(provider, msg);
			provider.finish();
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Error barcode configuration", e);
		} catch (BarcodeException e) {
			throw new IllegalStateException("Error generate barcode", e);
		} catch (SAXException e) {
			throw new IllegalStateException("Error SAXException", e);
		} catch (IOException e) {
			throw new IllegalStateException("IOException", e);
		}
	}
	
	/**
	 * @return
	 * @throws ConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Configuration getConfig() throws ConfigurationException, SAXException, IOException {
		if (cfg == null) {
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
			Resource resource = SpringUtils.getAppContext().getResource("classpath:" + PATH_CONFIG);
			cfg = builder.build(resource.getInputStream());
		}
		return cfg;
	}
	
}
