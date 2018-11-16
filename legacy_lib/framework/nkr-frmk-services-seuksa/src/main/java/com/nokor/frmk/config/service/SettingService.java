package com.nokor.frmk.config.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.service.BaseEntityService;

import com.nokor.frmk.config.model.SettingConfig;

/**
 * Settings service functionality to manage settings configuration Default,
 * Application settings
 * 
 * @author prasnar
 */
public interface SettingService extends BaseEntityService {
	
	void flushCache();

	Map<String, String> getPairValues(String code);

	boolean getValueBoolean(String code);

	int getValueInt(String code);
	
	long getValueLong(String code);
	
	BigDecimal getValueBigDecimal(String code);

	Double getValueDouble(String code);

	List<Long> getValuesID(String code);

	List<Integer> getValuesInt(String code);

	List<Long> getValuesLong(String code);

	List<String> getValues(String code);

	String getValue(String code);

	SettingConfig getSettingConfig(String code);

	List<SettingConfig> list(boolean refreshCache);

	long getValueLong(String code, long defaultId);

	int getValueInt(String code, int defaultId);
	

}
