package org.seuksa.frmk.model.eref;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.reflection.MyClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class ColERefData implements Serializable {
	/** */
	private static final long serialVersionUID = -167426788413718792L;

	private static final Logger logger = LoggerFactory.getLogger(BaseERefData.class);

    private static Map<String, Map<Long, ? extends RefDataId>> mainMapERefData; // Map of eRefData's collection indexed by className
																				// The collection id a  eRefData's map indexed by Id)

    /**
     * 
     */
    public ColERefData() {
		mainMapERefData = new HashMap<>();
    }
    

	/**
	 * 
	 * @param refDataClazz
	 * @return
	 */
	public static <T extends RefDataId> Map<Long, ? extends RefDataId> getMapERefData(String refDataClazzName) {
		return mainMapERefData.get(refDataClazzName);
	}
	

	/**
	 * 
	 * @param refDataClazzName
	 */
	public static void initMapERefData(String refDataClazzName) {
		mainMapERefData.put(refDataClazzName, new HashMap<>());
	}
	
	/**
	 * 
	 * @param mapValues
	 */
	public static void setMainMapERefData(Map<String, Map<Long, ? extends RefDataId>> mapValues) {
		mainMapERefData = mapValues;
	}
	
	/**
	 * 
	 * @param refData
	 */
	public <T extends RefDataId> void addERefData(T refData) {
		Map<Long, T> mapVal = (Map<Long, T>) getMapERefData(refData.getClass().getName());
		if (mapVal == null) {
			initMapERefData(refData.getClass().getName());
			mapVal = (Map<Long, T>) getMapERefData(refData.getClass().getName());
		}
		mapVal.put(refData.getId(), refData);
	}
	
	/**
	 * 
	 * @param refDataClazz
	 */
	public <T extends SimpleERefData> void addERefDatas(Class<T> refDataClazz) {
		List<T> refDatas = ERefDataHelper.getRefDatas(refDataClazz);
		for (T refData : refDatas) {
			addERefData(refData);
		}
	}
	
    /**
	 * 
	 * @param refDataClazzName
	 * @param id
	 * @return
	 */
	public <T extends RefDataId> T getById(String refDataClazzName, Long id) {
		if (id == null || id.longValue() <= 0) {
			return null;//throw new IllegalArgumentException("The param [id] must be greater to 0.");
		}

		try {
			return (T) getMapERefData(refDataClazzName).get(id);
		} catch (NullPointerException e) {
			String errMsg = "getById - The RefData [" + refDataClazzName + "] is not in the cache yet.";
			logger.warn(errMsg);
			initCacheRefDataStaticValues(refDataClazzName);
			try {
				return (T) getMapERefData(refDataClazzName).get(id);
			} catch (NullPointerException e2) {
				errMsg = "getById - The RefData [" + refDataClazzName + "] can not loaded the static values.";
				logger.warn(errMsg);
				throw new IllegalStateException(errMsg, e2);
			}
		}
	}
	
	   /**
     * 
     * @param refDataClazzName
     * @param code
     * @return
     */
    public <T extends RefDataId> T getByCode(String refDataClazzName, String code) {
		if (code == null) {
			return null;//throw new IllegalArgumentException("The param [id] must be greater to 0.");
		}
		Map<Long, T> mapVal = (Map<Long, T>) getMapERefData(refDataClazzName);
		if (mapVal == null) {
			String errMsg = "getByCode - The RefData [" + refDataClazzName + "] is not in the cache yet.";
			logger.warn(errMsg);
			initCacheRefDataStaticValues(refDataClazzName);
			mapVal = (Map<Long, T>) getMapERefData(refDataClazzName);
			if (mapVal == null) {
				errMsg = "getByCode - The RefData [" + refDataClazzName + "] can not loaded the static values.";
				logger.warn(errMsg);
				throw new IllegalStateException(errMsg);
			}
		}
		Iterator<Long> itIds = mapVal.keySet().iterator();
		while (itIds.hasNext()) {
			Long id = itIds.next();
			T refData = mapVal.get(id);
			if (refData.getCode().equals(code)) {
				return refData;
			}
		}
		return null;
    }

    /**
     * 
     * @param refDataClazzName
     * @return
     */
    public <T extends RefDataId> List<T> getValues(String refDataClazzName) {
		try {
			return new ArrayList<T>((Collection<? extends T>) mainMapERefData.get(refDataClazzName).values());
		} catch (NullPointerException e) {
			String errMsg = "getValues - The RefData [" + refDataClazzName + "] is not in the cache yet.";
			logger.warn(errMsg);
			initCacheRefDataStaticValues(refDataClazzName);
			return new ArrayList<T>((Collection<? extends T>) mainMapERefData.get(refDataClazzName).values());
		}
	}

	/**
	 * 
	 * @param refDataClazzName
	 */
	public <T extends RefDataId> void initCacheRefDataStaticValues(String refDataClazzName) {
		Class<T> refDataClazz;
     	try {
     		refDataClazz = (Class<T>) Class.forName(refDataClazzName);
     		initCacheRefDataStaticValues(refDataClazz);
    	} catch (Exception e) {
    		String errMsg = e.getMessage();
    	    logger.error(errMsg, e);
    	    throw new IllegalStateException(errMsg, e);
    	}
	}
	
	/**
	 * 
	 * @param refDataClazz
	 */
	public <T extends RefDataId> void initCacheRefDataStaticValues(Class<T> refDataClazz) {
		logger.debug("Caching [" + refDataClazz + "]");
		List<T> eRefDataLst = (List<T>) MyClassUtils.getStaticValues(refDataClazz, refDataClazz); 
		for (T eRefData : eRefDataLst) {
			addERefData(eRefData);
		}
		if (eRefDataLst == null || eRefDataLst.isEmpty()) {
			initMapERefData(refDataClazz.getName());
		}
	}


 
}
