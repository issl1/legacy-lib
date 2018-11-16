package com.nokor.frmk.config.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.frmk.config.dao.SettingConfigDao;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.config.service.SettingService;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Service("settingService")
public class SettingServiceImpl extends BaseEntityServiceImpl implements SettingService {
	/** */
	private static final long serialVersionUID = 2645897443757666078L;

	@Autowired
    private SettingConfigDao dao;
	
	private static List<SettingConfig> mapAppSettingsCache = new ArrayList<>();
	
	private static long UNKNOWN_LONG = -1000987;
	private static int UNKNOWN_INT = -1000987;
    
    /**
     * 
     */
    public SettingServiceImpl() {
    }
    
    @PostConstruct
    public void postConstruct() {
    }
    
    @Override
	public BaseEntityDao getDao() {
		return dao;
	}


	@Override
	public void flushCache() {
		mapAppSettingsCache = new ArrayList<>();
	}

	@Override
	public Map<String, String> getPairValues(String code) {
		try {
			Map<String, String> pairs = new HashMap<String, String>();
	        String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Settings Configuration.");
	            return pairs;
	        }
	        String[] properties = propStr.split(";");
	
	        for (String prop : properties) {
	        	String[] pair = prop.split(":");
	            pairs.put(pair[0], pair[1]);
	        }
	        return pairs;
		} catch (Exception e) {
            String errMsg = "[getPairValues] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public boolean getValueBoolean(String code) {
		try {
			String value = getValue(code);
			if (StringUtils.isEmpty(value)) {
				return false;
			}
			value = value.trim();
			
            return Arrays.asList("true", "1").contains(value);
        } catch (Exception e) {
            String errMsg = "[getValueBoolean] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public long getValueLong(String code) {
		return getValueLong(code, UNKNOWN_LONG);
	}
	
	@Override
	public long getValueLong(String code, long defaultId) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Long.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueLong] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            if (defaultId == UNKNOWN_LONG) {
                throw new IllegalArgumentException(errMsg, e);
            } else {
            	return defaultId;
            }
        }
	}
	
	@Override
	public int getValueInt(String code) {
		return getValueInt(code, UNKNOWN_INT);
	}
	
	@Override
	public int getValueInt(String code, int defaultId) {
		try {
			String value = getValue(code);
            return  StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : 0;
        }
        catch (Exception e) {
            String errMsg = "[ValueInteger] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            if (defaultId == UNKNOWN_LONG) {
                throw new IllegalArgumentException(errMsg, e);
            } else {
            	return defaultId;
            }
        }
	}
	
	@Override
	public BigDecimal getValueBigDecimal(String code) {
		try {
            return BigDecimal.valueOf(getValueDouble(code));
        }
        catch (Exception e) {
            String errMsg = "[ValueBigDecimal] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public Double getValueDouble(String code) {
		try {
			String value = getValue(code);
            return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : 0L;
        } catch (Exception e) {
            String errMsg = "[ValueDouble] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public List<Long> getValuesID(String code) {
		return getValuesLong(code);
	}

	@Override
	public List<Long> getValuesLong(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
	            return new ArrayList<Long>();
	        }
			return MyStringUtils.getValuesLong(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesLong] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	


	@Override
	public List<Integer> getValuesInt(String code) {
		try {
			String propStr = getValue(code);
		    if (StringUtils.isEmpty(propStr)) {
		        logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
		        return new ArrayList<Integer>();
		    }
		
		    return MyStringUtils.getValuesInt(propStr);
		} catch (Exception e) {
            String errMsg = "[getValuesInt] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}
	
	@Override
	public List<String> getValues(String code) {
		try {
			String propStr = getValue(code);
	        if (StringUtils.isEmpty(propStr)) {
	            logger.warn("[" + code + "] is not defined in the Application Advanced Configuration.");
	            return new ArrayList<String>();
	        }
	
	        String[] values = propStr.replace(" ", "").split(MyStringUtils.LIST_SEPARATOR_COMMA);
			return Arrays.asList(values);
		} catch (Exception e) {
            String errMsg = "[getValues] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public String getValue(String code) {
		try {
			SettingConfig appCfg = getSettingConfig(code);
	        if (appCfg == null) {
	            logger.warn("[SettingService.Value[" + code + "] returns NULL - Check its code/value in the [ts_setting_config] table" );
	            return null;
	        }
	        return appCfg.getValue();
		} catch (Exception e) {
            String errMsg = "[getValue] Error for the value [" + code + "] - Check its code/value in the [ts_setting_config] table";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg, e);
        }
	}

	@Override
	public SettingConfig getSettingConfig(String code) {
		List<SettingConfig> cfgDataList = list(false);
        for (SettingConfig cfgData : cfgDataList) {
           if (cfgData.getCode().equals(code)) {
                return cfgData;
            }
        }

        logger.warn("SettingService - Get(" + code + ") = null");
        return null;
	}

	@Override
	public List<SettingConfig> list(boolean refreshCache) {
		
		if (refreshCache) {
            flushCache();
        }
        if (mapAppSettingsCache.isEmpty()) {
        	mapAppSettingsCache = dao.list();
        }

        return mapAppSettingsCache;
	}



}