package com.nokor.efinance.core.dealer.service;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * Sequence manager for dealer
 * @author uhout.cheng
 */
public final class DealerSequenceManager implements FinServicesHelper {

	private final static String DEAHEADCODE = "DEAHEADCODE";
	private final static String DEABRANCHCODE = "DEABRANCHCODE";
	
	private Logger LOG = LoggerFactory.getLogger(DealerSequenceManager.class);
	
	private Integer sequence = 0;
			
	private DealerSequenceManager() {
		
	}
	
	/** Holder */
	private static class SingletonHolder {
		private final static DealerSequenceManager instance = new DealerSequenceManager();
	}
	 
	/**
	 * @return
	 */
	public static DealerSequenceManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the sequenceCampaign
	 */
	public Integer getSequenceDealerHead() {
		synchronized (sequence) {			
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, DEAHEADCODE);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Dealer head running code");
				setting.setReadOnly(false);
				setting.setCode(DEAHEADCODE);
				sequence = 0;
			} else {
				sequence = Integer.parseInt(setting.getValue());
			}
			sequence++;
			setting.setValue(String.valueOf(sequence));
			LOG.debug(">> saveOrUpdate SettingConfig dealer head running number");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig dealer head running number");
			
		}
		return sequence;
	}
	
	/**
	 * @return the sequenceDealerBranch
	 */
	public Integer getSequenceDealerBranch() {
		synchronized (sequence) {			
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, DEABRANCHCODE);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Dealer branch running code");
				setting.setReadOnly(false);
				setting.setCode(DEABRANCHCODE);
				sequence = 0;
			} else {
				sequence = Integer.parseInt(setting.getValue());
			}
			sequence++;
			setting.setValue(String.valueOf(sequence));
			LOG.debug(">> saveOrUpdate SettingConfig dealer branch running number");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig dealer branch running number");
			
		}
		return sequence;
	}
	
}
