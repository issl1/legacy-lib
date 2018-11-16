package org.seuksa.frmk.model.eref;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.reflection.MyClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.config.AppConfigFile;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.model.RefDataVO;
import com.nokor.frmk.model.RefTableVO;



/**
 * 
 * @author prasnar
 *
 */
public class ERefDataHelper implements SeuksaServicesHelper {
	private static final Logger logger = LoggerFactory.getLogger(ERefDataHelper.class);

    private static final String I18N_SEP = ".";
    private static final String I18N_EN_SEP = ".en.";
    private static final String XML = ".xml";

   
	/**
	 * 
	 */
	private ERefDataHelper() {
	}
	
	/**
	 * 
	 * @param refDataClazz
	 * @return
	 */
    public static <T extends SimpleERefData> List<T> getRefDatas(Class<T> refDataClazz) {
    	List<T> eRefDatas = new ArrayList<>();
//    	InputStream is = null;
//    	try {
//    		is = loadXmlFile(refDataClazz.getSimpleName() + XML);
//    		JAXBContext jbCtx = JAXBContext.newInstance(RefTableVO.class);
//    		Unmarshaller jbu = jbCtx.createUnmarshaller();
//    		RefTableVO refTable = (RefTableVO) jbu.unmarshal(is);
//    		
//        	T eRefData = null;
//
//     		for (RefDataVO refData : refTable.getRefDatas()) {
//             	eRefData = refDataClazz.newInstance();
//            	eRefData.setId(refData.getId());
//            	eRefData.setCode(refData.getCode());
//            	eRefData.setDesc(refData.getDesc());
//            	eRefData.setDescEn(refData.getDescEn());
//            	eRefData.setSortIndex(refData.getSortIndex());
//            	
//            	eRefDatas.add(eRefData);
//     		}
//     		
//         		         	
//    	} catch (Exception e) {
//    		String errMsg = "Error while loading RefDatas from XML for [" + refDataClazz.getCanonicalName() + "]";
//    		logger.error(errMsg, e);
//    		throw new IllegalStateException(errMsg, e);
//    	} finally {
//    		try {
//    			if (is != null) {
//    				is.close();
//    			}
//    		} catch (Exception e1) {}
//    	}
    	
    	return eRefDatas;
    }
	
    /**
     * 
     * @param xmlFile
     * @return
     */
    private static InputStream loadXmlFile(String xmlFile) {
    	logger.debug("Loading the configuration file [" + xmlFile + "]");
    	
        InputStream in = ClassLoader.getSystemResourceAsStream(xmlFile);
        if (in == null) {
            in = ClassLoader.getSystemResourceAsStream("/" + xmlFile);
        }
        if (in == null) {
            in = ClassLoader.getSystemClassLoader().getResourceAsStream("/" + xmlFile);
        }
        if (in == null) {
            in = AppConfigFile.class.getClassLoader().getResourceAsStream(xmlFile); 
        }
        
        if (in == null) {
            String errMsg = "Can't find " + xmlFile + " in the classpath, check your Configuration";
            logger.error(errMsg);
            throw new IllegalStateException("Can't find [" + xmlFile + "]");
        }
        
        return in;
    }
    
//    
//        try {
//			in.close();
//		} catch (IOException e) {
//		    logger.warn(e.getMessage());
//            // do nothing
//		    
//		}
//		Enumeration eKeys = instance.keys();
//		while(eKeys.hasMoreElements()) {
//			String key = (String) eKeys.nextElement();
//			String value = (String) instance.get(key);
//
//			logger.debug("Configuration: [" +  key + "] = [" + value + "]");
//		}
//		return true;
//    }
    
	
	/**
	 * 
	 * @param eRefData
	 * @param isEnglish
	 * @return
	 */
    public static <T extends RefDataId> String getDescI18N(T eRefData, boolean isEnglish) {
    	return I18N.value(eRefData.getClass().getSimpleName() + (isEnglish ? I18N_EN_SEP : I18N_SEP) + eRefData.getCode(), eRefData.getCode());
    }

	/**
	 * 
	 * @param refDataClazz
	 * @return
	 */
	public static <T extends RefDataId> T getDefault(Class<T> refDataClazz) {
		return null;
	}
	
	
	/**
	 * 
	 * @param refDataClazz
	 * @param mapValues
	 * @return
	 */
	public static <T extends RefDataId> List<T> getStaticValues(Class<T> refDataClazz, Map<String, List<? extends RefDataId>> mapValues) {
		List<? extends RefDataId> lstVal = mapValues.get(refDataClazz.getName());
		if (lstVal != null) {
			return (List<T>) lstVal;
		}
		List<T> lstRes = (List<T>) MyClassUtils.getStaticValues(refDataClazz, refDataClazz);
		mapValues.put(refDataClazz.getName(), lstRes);
		return lstRes;
	}
	
	

}