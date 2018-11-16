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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;

/**
 * Journal entry
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_transaction_entry")
public class TransactionEntry extends EntityWkf implements MTransactionEntry {
	/** */
	private static final long serialVersionUID = 3490993190859468386L;

	private String desc;
    private String descEn;
    private List<JournalEntry> journalEntries;
    private String callBackUrl;
    
    /**
     * @return
     */
    public static TransactionEntry createInstance() {
    	TransactionEntry entry = EntityFactory.createInstance(TransactionEntry.class);
        return entry;
    }
        
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tra_en_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @return the desc
	 */
    @Column(name="tra_en_desc", nullable = true)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the descEn
	 */
    @Column(name="tra_en_desc_en", nullable = true)
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}
	
	/**
	 * @return the journalEntries
	 */
    @OneToMany(mappedBy="transactionEntry", fetch = FetchType.LAZY)
	public List<JournalEntry> getJournalEntries() {
		return journalEntries;
	}

	/**
	 * @param journalEntries the journalEntries to set
	 */
	public void setJournalEntries(List<JournalEntry> journalEntries) {
		this.journalEntries = journalEntries;
	}
	
	/**
	 * @return the callBackUrl
	 */
	@Column(name = "tra_en_call_back_url", nullable = true, length = 255)
	public String getCallBackUrl() {
		return callBackUrl;
	}

	/**
	 * @param callBackUrl the callBackUrl to set
	 */
	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}
}
