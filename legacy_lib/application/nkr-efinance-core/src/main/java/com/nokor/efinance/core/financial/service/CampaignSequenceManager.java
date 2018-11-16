package com.nokor.efinance.core.financial.service;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * Sequence manager
 * @author ly.youhort
 *
 */
public final class CampaignSequenceManager implements FinServicesHelper {

	private final static String MKTCAMCODE = "MKTCAMCODE";
	
	private Logger LOG = LoggerFactory.getLogger(CampaignSequenceManager.class);
	
	private Integer sequenceCampaign = 0;
			
	private CampaignSequenceManager() {
		
	}
	
	/** Holder */
	private static class SingletonHolder {
		private final static CampaignSequenceManager instance = new CampaignSequenceManager();
	}
	 
	/**
	 * @return
	 */
	public static CampaignSequenceManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the sequenceCampaign
	 */
	public Integer getSequenceCampaign() {
		synchronized (sequenceCampaign) {			
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, MKTCAMCODE);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Campagin running code");
				setting.setReadOnly(false);
				setting.setCode(MKTCAMCODE);
				sequenceCampaign = 0;
			} else {
				sequenceCampaign = Integer.parseInt(setting.getValue());
			}
			sequenceCampaign++;
			setting.setValue(String.valueOf(sequenceCampaign));
			LOG.debug(">> saveOrUpdate SettingConfig campaign running number");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig campaign running number");
			
		}
		return sequenceCampaign;
	}
	
}
