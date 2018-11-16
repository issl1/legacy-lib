package com.nokor.ersys.finance.accounting.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * A source event which triggers entries (see JournalEventAccount) in a type of journal
 *  
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_journal_event")
public class JournalEvent extends EntityRefA implements MJournalEvent {
	/** */
	private static final long serialVersionUID = -3942643526868795061L;

	public static final long GENERAL = 1;
	public static final long SALES = 2;
	public static final long PURCHASES = 3;
	public static final long PAYMENTS = 4;
	public static final long RECEIPTS = 5;
	
	private Journal journal;
	private EJournalEventGroup eventGroup;
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jou_eve_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Override
    @Column(name = "jou_eve_code", nullable = false)
    public String getCode() {
    	return code;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Override
    @Column(name = "jou_eve_desc", nullable = true)
    public String getDesc() {
    	return desc;
    }


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
     */
    @Column(name = "jou_eve_desc_en", nullable = true)
    public String getDescEn() {
    	return descEn;
    }

	/**
	 * @return the journal
	 */
    @ManyToOne
    @JoinColumn(name="jou_id", nullable = false)
	public Journal getJournal() {
		return journal;
	}

	/**
	 * @param journal the journal to set
	 */
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	/**
	 * @return the eventGroup
	 */
	@Column(name="jou_eve_grp_id", nullable = true)
    @Convert(converter = EJournalEventGroup.class)
	public EJournalEventGroup getEventGroup() {
		return eventGroup;
	}

	/**
	 * @param eventGroup the eventGroup to set
	 */
	public void setEventGroup(EJournalEventGroup eventGroup) {
		this.eventGroup = eventGroup;
	}

	
}
