package com.nokor.common.app.workflow.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.slf4j.LoggerFactory;

import com.nokor.common.app.history.model.BaseHistoryItem;

/**
 * Possibility to historize the fields before switching fromStatus->toStatus in WkfFlowItem.
 * 
 * @author prasnar
 * 
 */
@MappedSuperclass
public abstract class WkfBaseHistoryItem extends BaseHistoryItem implements MWkfBaseHistoryItem {
	/** */
	private static final long serialVersionUID = -1808018271208167742L;

	private WkfBaseHistory wkfHistory;
	private Long processTime;

	/**
	 * 
	 * @param hisItemClazz
	 * @param wkfHistory
	 * @return
	 */
	public static <T extends WkfBaseHistory, I extends WkfBaseHistoryItem> I createInstance(Class<I> hisItemClazz, T wkfHistory) {
    	I entry = (I) BaseHistoryItem.createInstance(hisItemClazz);
    	entry.setWkfHistory(wkfHistory);
        return entry;
    }
	
	/**
	 * @return the wkfHistory
	 */
	@Transient
	public WkfBaseHistory getWkfHistory() {
		return wkfHistory;
	}


	/**
	 * @param wkfHistory the wkfHistory to set
	 */
	public void setWkfHistory(WkfBaseHistory wkfHistory) {
		this.wkfHistory = wkfHistory;
	}

	/**
	 * @return the processTime
	 */
    @Column(name = "wkf_his_process_time", nullable = true)
	public Long getProcessTime() {
		return processTime;
	}

	/**
	 * @param processTime the processTime to set
	 */
	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

	/**
	 * @return the wkfStatus
	 */
	@Transient
	public EWkfStatus getWkfCurrentStatus() {
		try {
			if (getNewValue() != null) {
				return EWkfStatus.getByCode(getNewValue());
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Error while converting id into EWkfStatus", e.getMessage());
		}
		return null;
	}
	
	/**
	 * @return the wkfPreviousStatus
	 */
	@Transient
	public EWkfStatus getWkfPreviousStatus() {
		try {
			if (getOldValue() != null) {
				return EWkfStatus.getByCode(getOldValue());
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Error while converting id into EWkfStatus", e.getMessage());
		}
		return null;
	}
	

}
