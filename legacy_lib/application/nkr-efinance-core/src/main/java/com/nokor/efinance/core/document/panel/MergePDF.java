package com.nokor.efinance.core.document.panel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class MergePDF {

    /**
     * Merge multiple pdf into one pdf
     * 
     * @param list of pdf input stream
     * @param outputStream output file output stream
     * @throws DocumentException
     * @throws IOException
     */
    public static void doMerge(List<File> files, String destFileName)
            throws DocumentException, IOException {
    	System.out.println("===>"+ files.size() + ", dest file: "+ destFileName);
    	
    	if(files != null && files.size() ==0 || destFileName == null)
    		return;
    	
    	FileOutputStream outputStream = new FileOutputStream(destFileName);
    	        
    	Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
                
        for (File f : files) {
        	FileInputStream fin = new FileInputStream(f);
            PdfReader reader = new PdfReader(fin);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        }
        
        outputStream.flush();
        document.close();        
        outputStream.close();
    }
    
    
}