package org.seuksa.frmk.tools.http;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.LoggerFactory;

/**
 * Command line program to download data from URLs and save it to local files. 
 * 
 * @author prasnar
 */
public class FileDownload {

	
	/**
	 * Download the URL into the localFileName
	 * @param address URL
	 * @param localFileName output local file
	 */
	public static boolean download(String address, String localFileName) {
		LoggerFactory.getLogger(FileDownload.class).debug("Trying to download [" + address + "]..");
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		
		try {
			
			URL url = new URL(address);
			out = new BufferedOutputStream(new FileOutputStream(localFileName));
			conn = url.openConnection();
			in = conn.getInputStream();

			LoggerFactory.getLogger(FileDownload.class).debug("Download finished...now saving into [" + localFileName + "]..");
			
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			LoggerFactory.getLogger(FileDownload.class).debug("Saved into [" + localFileName + "], nb read:" + numWritten);
			
			return true;
			
		} catch (Exception e) {
			String errMsg = "Error while downloading [" + address + "] and saving into [" + localFileName + "].";
			LoggerFactory.getLogger(FileDownload.class).error(errMsg + "\n" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
		return false;
	}

	
	/**
	 * Main class for testing purpose
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			download(args[i], "myoutputfile");
		}
	}
}