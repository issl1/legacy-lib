package com.nokor.efinance.core.workflow.model.history;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;


/**
 * 
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_history_process")
public class HistoryProcess extends EntityA {

    private static final long serialVersionUID = -4053267507390718585L;
    
//    private WkfHistoryItem history;
    private EProcessType processType;
	private Date processDate;
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "his_pro_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the history
	 */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "his_id")
//	public WkfHistoryItem getHistory() {
//		return history;
//	}
//
//	/**
//	 * @param history the history to set
//	 */
//	public void setHistory(WkfHistoryItem history) {
//		this.history = history;
//	}

	/**
	 * @return the processType
	 */
    @Column(name = "pro_typ_id", nullable = false)
    @Convert(converter = EProcessType.class)
	public EProcessType getProcessType() {
		return processType;
	}

	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(EProcessType processType) {
		this.processType = processType;	
	}
	
	/**
	 * @return the processDate
	 */
	@Column(name = "his_pro_dt_date", nullable = true)
	public Date getProcessDate() {
		return processDate;
	}

	/**
	 * @param processDate the processDate to set
	 */
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}	
}
