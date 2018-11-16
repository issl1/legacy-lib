package com.nokor.common.app.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.content.model.eref.EFileType;
import com.nokor.common.app.content.model.file.FileData;
import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
public class FileDataUtils {
	
	protected static final Logger logger = LoggerFactory.getLogger(FileDataUtils.class);
	
	// Image extenstion
	public static String[] fileImageEx = {"jpeg", "jpg", "png", "gif"};
	// Word extension
	public static String[] fileWordEx = {"doc", "dot", "docx", "docm", "dotx", "dotm"};
	// Excel extension
	public static String[] fileExcelEx = {"xls", "xlsx", "xlt", "xlm", "xlsm", "xltx", "xltm", "xlsb", "xla", "xlam", "xll", "xlw"};
	// Exe extenstion
	public static String[] fileExeEx = {"exe"};

	// File Name
	public final static String FILE_NAME_BANK_RIB = "bankRibFile";
	public final static String FILE_NAME_EMPLOYEE_PHOTO = "employeePhotoFile";
	public final static String FILE_NAME_COMPANY_PHOTO = "companyPhotoFile";
	public final static String FILE_NAME_IMPORTED = "importedFile";
	public final static String FILE_NAME_DATA = "fileData";
	public final static String FILE_NAME_DATA_IMG = "fileDataImg";
	public final static String FILE_NAME_IMAGE = "imageFile";
	public final static String FILE_NAME_PDF = "pdfFile";	

	/**
	 * 
	 */
    private FileDataUtils() {
        super();
    }
    
    /**
     * 
     * @param file
     * @param srcFiles
     * @param fileNames
     * @param typeFile
     * @param prefixPath
     * @return
     * @throws IOException
     */
    public static FileData generateFile(final FileData file, final File srcFile, final String fileName, final EFileType typeFile, final String prefixPath) throws IOException {
    	
    	// Check if varFileName has been defined in jsp file
        if (fileName != null) {
			String destPath = AppConfigFileHelper.getCdnUrlUpload() + prefixPath;
			File destPathFile = new File(destPath);
			if (!destPathFile.exists()) {
				destPathFile.mkdirs();
			}
        	
        	String fileNameFromFullPath = getNameFromFullPath(fileName);
        	
        	if (file != null) {
        		if (file.getName() != null) {
        			deleteExistingFile(file.getId());
        		}
			}
        	
            String convertedFileName = convertName(fileNameFromFullPath);

            if (srcFile != null) {
                File dst = new File(destPath, convertedFileName);
                FileUtils.copyFile(srcFile, dst);
            }

            FileData fileData = file;
            if (fileData == null) {
                fileData = FileData.createInstance();
            }
            
            String desc = getNameWithoutExtension(fileNameFromFullPath);
            
            fileData.setDesc(desc);
            fileData.setName(convertedFileName);
            fileData.setType(typeFile);
            fileData.setSize(srcFile.length());
            
            if (!StringUtils.isEmpty(prefixPath)) {
            	fileData.setUrl(prefixPath + "/" + convertedFileName);
            }
            else {
            	fileData.setUrl(convertedFileName);
            }
            
            return fileData;
        }
        
        return null;
    }
    
    /**
     * 
     * @param fileId
     */
    public static void deleteExistingFile(Long fileId) {
    	
    	FileData fileData = AppServicesHelper.ENTITY_SRV.getById(FileData.class, fileId);
		File updateFile = new File(AppConfigFileHelper.getCdnUrlUpload() + fileData.getUrl());
		if (updateFile.exists()) {
			updateFile.delete();
			logger.info("File [" + fileId + ": " + fileData.getUrl() + "] has been deleted");
		}
    }
    
    /**
     * 
     * @param file
     * @param srcFile
     * @param fileName
     * @param typeFile
     * @param prefixPath
     * @return
     * @throws IOException
     */
    public static FileData changeFile(final FileData file, final File srcFile, final String fileName, final EFileType typeFile, final String prefixPath) throws IOException {
    	deleteFile(file);
        return generateFile(file, srcFile, fileName, typeFile, prefixPath);
    }
    
    /**
     * 
     * @param file
     * @return
     */
    public static FileData deleteFile(final FileData file) {
    	if (file != null) {
        	File oldFile = new File(AppConfigFileHelper.getCdnUrlUpload() + file.getUrl());
			
			if (oldFile.exists()) {
				oldFile.delete();
			}
        }
    	return file;
    }
    
    /**
     * 
     * @param file
     * @return
     */
    public static boolean isExist(final FileData file) {
    	if (file != null) {
        	File rawFile = new File(AppConfigFileHelper.getCdnUrlUpload() + file.getUrl());
			
			if (rawFile.exists()) {
				return true;
			} else {
				logger.error("FiledData [" + file.getId() + "] content Raw file which does not exist in path [" + AppConfigFileHelper.getCdnUrlUpload() + file.getUrl() + "]");
			}
        }
    	return false;
    }
    
    /**
     * 
     * @param dir
     */
    public static void createDirIfNotExsits(String dir) {
    	File file = new File(dir);
    	
    	if (!file.exists()) {
    		file.mkdirs();
    	}
    }
    
    /**
     * 
     * @param filename
     * @return
     */
    public static Long getSize(String fileName) {
    	Long size = null;
    	File file = new File(AppConfigFileHelper.getCdnUrlUpload() + "/" + fileName);
    	if (file != null && file.exists()) {
    		size = file.length();
    	}
    	return size;
    }
    
    /**
     * 
     * @param fileName
     * @return
     */
    public static String convertName(String fileName) {
    	
		String regex = "[0-9]|[a-z]|[A-Z]|[.]";
		StringBuffer sbFileName = new StringBuffer();
		for (int i = 0, n = fileName.length(); i < n; i++) {
		    char c = fileName.charAt(i);
		    if(!String.valueOf(c).matches(regex)){
		    	c = "_".charAt(0);
		    }
		    sbFileName.append(c);
		}
		
    	return sbFileName.toString();
    }
    
    /**
     * 
     * @param fileName
     * @return
     */
    public static Integer[] getImageDimension(String fileName) {
    	BufferedImage bimg;
    	Integer[] dimesion = new Integer[2];
		try {
			bimg = ImageIO.read(new File(fileName));
			dimesion[0] = bimg.getWidth();
			dimesion[1] = bimg.getHeight();
		} catch (IOException e) {
			dimesion[0] = null;
			dimesion[1] = null;
			e.printStackTrace();
		}
    	return dimesion;
    }
    
    /**
     * 
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
    	
    	if (StringUtils.isEmpty(fileName)) {
    		return "";
    	}
    	
    	return FilenameUtils.getExtension(fileName);
    }
    
    /**
     * 
     * @param extension
     * @return
     */
    public static EFileType getType(String extension) {
    	EFileType typeFile = null;
    	
    	if (Arrays.asList(FileDataUtils.fileWordEx).contains(extension.toLowerCase())) {
    		typeFile = EFileType.WORD;
    	}
    	else if (Arrays.asList(FileDataUtils.fileExcelEx).contains(extension.toLowerCase())) {
    		typeFile = EFileType.EXCEL;
    	}
    	else if (extension.toLowerCase().equals("pdf")) {
    		typeFile = EFileType.PDF;
    	}
    	return typeFile;
    }
    
    /**
     * 
     * @param fileName
     * @return
     */
    public static String getNameWithoutExtension(String fileName) {
    	if (StringUtils.isEmpty(fileName)) {
    		return "";
    	}
    	
    	return FilenameUtils.removeExtension(fileName);
    }
    
    /**
     * 
     * @param fileName
     * @return
     */
    public static String getNameFromFullPath(String fileName) {
    	
    	if (StringUtils.isEmpty(fileName)) {
    		return "";
    	}
    	return FilenameUtils.getName(fileName);
    }
    
    /**
     * 
     * @param letterName
     * @param prefixName
     * @param dateTimeString
     * @return
     */
    public static String getGeneratedLetterFileName(String letterName, String prefixName) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(StringUtils.isNotEmpty(letterName) ? letterName + " " : " ");
    	builder.append(StringUtils.isNotEmpty(prefixName) ? prefixName + " " : " ");
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	String dateTimeString = dateFormat.format(new Date());
    	
    	builder.append(StringUtils.isNotEmpty(dateTimeString) ? StringUtils.replace(dateTimeString,":","") + "" : "");
    	String fileName = convertName(builder.toString());
    	return fileName;
    }
    
}
