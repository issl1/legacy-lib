package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * Sequence manager
 * @author ly.youhort
 *
 */
public final class SequenceManager implements FinServicesHelper {

	private final static String CONREF = "CONREF";
	private final static String LOCKSPLITREF = "CONREF";
	private final static String APPLICANTREF = "APPREF";
	
	private Logger LOG = LoggerFactory.getLogger(SequenceManager.class);
	
	private Long sequenceContract = 0l;
	private Long sequenceLockSplit = 0l;
	private Long sequenceApplicant = 0l;
			
	private SequenceManager() {}
	
	/** Holder */
	private static class SingletonHolder {
		private final static SequenceManager instance = new SequenceManager();
	}
	 
	/**
	 * @return
	 */
	public static SequenceManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @return the sequenceContract
	 */
	public Long getSequenceContract() {
		synchronized (sequenceContract) {			
			String yearLabel = DateUtils.getDateLabel(DateUtils.today(), "yy");
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, CONREF + yearLabel);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Reference contract running number");
				setting.setReadOnly(false);
				setting.setCode(CONREF + yearLabel);
				sequenceContract = 0l;
			} else {
				sequenceContract = Long.parseLong(setting.getValue());
			}
			sequenceContract++;
			setting.setValue(String.valueOf(sequenceContract));
			LOG.debug(">> saveOrUpdate SettingConfig contract running number");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig contract running number");
			
		}
		return sequenceContract;
	}
	
	/**
	 * @return the sequenceContract
	 */
	public Long getSequenceLockSplit() {
		synchronized (this.sequenceLockSplit) {
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, LOCKSPLITREF);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Lock split running number");
				setting.setReadOnly(false);
				setting.setCode(LOCKSPLITREF);
				setting.setValue(String.valueOf(1));
				sequenceLockSplit = 0l;
			} else {
				sequenceLockSplit = Long.parseLong(setting.getValue());
			}
			sequenceLockSplit++;
			setting.setValue(String.valueOf(sequenceLockSplit));
			LOG.debug(">> saveOrUpdate SettingConfig Lock Split");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig Lock Split");
		}
		return sequenceLockSplit;
	}
	
	/**
	 * @return
	 */
	public Long getSequenceApplicant() {
		synchronized (this.sequenceApplicant) {
			SettingConfig setting = CONT_SRV.getByCode(SettingConfig.class, APPLICANTREF);
			if (setting == null) {
				setting = new SettingConfig();
				setting.setStatusRecord(EStatusRecord.ACTIV);
				setting.setDesc("Applicant running number");
				setting.setReadOnly(false);
				setting.setCode(APPLICANTREF);
				setting.setValue(String.valueOf(1));
				sequenceApplicant = 0l;
			} else {
				sequenceApplicant = Long.parseLong(setting.getValue());
			}
			sequenceApplicant++;
			setting.setValue(String.valueOf(sequenceApplicant));
			LOG.debug(">> saveOrUpdate SettingConfig Applicant");
			CONT_SRV.saveOrUpdate(setting);
			LOG.debug("<< saveOrUpdate SettingConfig Applicant");
		}
		return sequenceApplicant;
	}
	
}
