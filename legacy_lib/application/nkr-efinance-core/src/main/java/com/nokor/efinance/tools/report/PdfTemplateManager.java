package com.nokor.efinance.tools.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 
 * @author kong.phirun
 *
 */
public class PdfTemplateManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
    public PdfTemplateManager() {
	}
    
    public File createPdf(Map<String, String> values, String templatePath, String outputPath, String prefix, String suffix) {
    	PdfReader reader = null;
    	PdfStamper stamper = null;
    	FileOutputStream fos = null;
    	File pdfFile = null;
    	
    	try {
    		reader = new PdfReader(templatePath);
    		String uuid = UUID.randomUUID().toString().replace("-", "");
    		String fileName = outputPath + "/tmp_" + prefix + uuid + "." + suffix;
    		pdfFile = new File(fileName);
    		fos = new FileOutputStream(pdfFile);
    		stamper = new PdfStamper(reader, fos);
    		AcroFields form = stamper.getAcroFields();
    		Map<String, Item> fields = form.getFields();
    		Iterator<String> iterator = fields.keySet().iterator();
    		while (iterator.hasNext()) {
    			String fieldName = iterator.next();
    			String value = "";
    			if (values.containsKey(fieldName)) {
    				value = values.get(fieldName);
    			}
	            if (value != null) {	            	
            		form.setField(fieldName, value);	            	
	            }
    		}
    		stamper.setFormFlattening(true);
    		stamper.close();
    	} catch (Exception e) {
    		logger.error("Exception", e);
    	} finally {
    		if (stamper != null) {
    			try {
    				stamper.close();
    			} catch (DocumentException e) {
    				logger.error("DocumentException", e);
    			} catch (IOException e) {
    				logger.error("IOException", e);
    			}
    		}
    		if (fos != null) {
    			try {
    				fos.flush();
    				fos.close();
    			} catch (IOException e) {
    				logger.error("IOException", e);
    			}
    		}
    	}
    	return pdfFile;
    }	
}
