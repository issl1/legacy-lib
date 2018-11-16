package com.nokor.ersys.finance.accounting.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_journal")
public class Journal extends EntityRefA implements MJournal {
	/** */
	private static final long serialVersionUID = 7044485459348343270L;

	public final static long GENERAL = 1L; // record non-routine transactions, such as depreciation, bad debts, sale of an asset, etc..
	public final static long SALES = 2L;
	public final static long PURCHASES = 3L;
	public final static long CASH_DISBURSEMENTS = 4L;
	public final static long CASH_RECEIPTS = 5L;

	private List<JournalEvent> events;
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jou_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Override
    @Column(name = "jou_code", nullable = false)
    public String getCode() {
    	return code;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Override
    @Column(name = "jou_desc", nullable = true)
    public String getDesc() {
    	return desc;
    }


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
     */
    @Column(name = "jou_desc_en", nullable = true)
    public String getDescEn() {
    	return descEn;
    }

	/**
	 * @return the events
	 */
	@OneToMany(mappedBy="journal", fetch = FetchType.LAZY)
	public List<JournalEvent> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<JournalEvent> events) {
		this.events = events;
	}

}
