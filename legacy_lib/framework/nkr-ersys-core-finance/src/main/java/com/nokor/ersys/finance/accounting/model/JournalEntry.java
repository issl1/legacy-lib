package com.nokor.ersys.finance.accounting.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * Journal entry
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_journal_entry")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(value="B")
public class JournalEntry extends EntityWkf implements MJournalEntry {
	/** */
	private static final long serialVersionUID = 3490993190859468386L;

	private String reference;
    private String desc;
    private String descEn;
	private JournalEvent journalEvent;
    private Date when;
    private Organization organization;
    private BigDecimal amount;
    private BigDecimal amount2;
    private BigDecimal amount3;
    private BigDecimal amount4;
    private BigDecimal amount5;
    private BigDecimal amount6;   
    private BigDecimal amount7;
    private BigDecimal amount8;
    private BigDecimal amount9;
            
    private ECurrency currency;
    private String info;
    private String otherInfo;
    private TransactionEntry transactionEntry;
    private List<AccountLedger> accountLedger;
    
    /**
     * 
     * @return
     */
    public static JournalEntry createInstance() {
    	JournalEntry entry = EntityFactory.createInstance(JournalEntry.class);
        return entry;
    }
    
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jou_en_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @return the journal
	 */
    @Transient
	public Journal getJournal() {
		return journalEvent.getJournal();
	}

	
	/**
	 * @return the journalEvent
	 */
    @ManyToOne
    @JoinColumn(name="jou_eve_id", nullable = false)
	public JournalEvent getJournalEvent() {
		return journalEvent;
	}

	/**
	 * @param journalEvent the journalEvent to set
	 */
	public void setJournalEvent(JournalEvent journalEvent) {
		this.journalEvent = journalEvent;
	}

	/**
	 * @return the reference
	 */
    @Column(name = "jou_en_reference", nullable = true)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the desc
	 */
    @Column(name="jou_en_desc", nullable = true)
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
    @Column(name="jou_en_desc_en", nullable = true)
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
	 * Get Description by Locale
	 */
	@Transient
	public String getDescLocale() {
		if (I18N.isEnglishLocale()) {
    		return getDescEn();
    	} else {
    		return getDesc();
    	}
	}

	/**
	 * @return the when
	 */
    @Column(name="jou_en_when", nullable = false)
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when the when to set
	 */
	public void setWhen(Date when) {
		this.when = when;
	}

	/**
	 * @return the amount
	 */
    @Column(name="jou_en_amount", nullable = false)
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the amount2
	 */
	@Column(name="jou_en_amount_2", nullable = true)
	public BigDecimal getAmount2() {
		return amount2;
	}

	/**
	 * @param amount2 the amount2 to set
	 */
	public void setAmount2(BigDecimal amount2) {
		this.amount2 = amount2;
	}

	/**
	 * @return the amount3
	 */
	@Column(name="jou_en_amount_3", nullable = true)
	public BigDecimal getAmount3() {
		return amount3;
	}

	/**
	 * @param amount3 the amount3 to set
	 */
	public void setAmount3(BigDecimal amount3) {
		this.amount3 = amount3;
	}

	/**
	 * @return the amount4
	 */
	@Column(name="jou_en_amount_4", nullable = true)
	public BigDecimal getAmount4() {
		return amount4;
	}

	/**
	 * @param amount4 the amount4 to set
	 */
	public void setAmount4(BigDecimal amount4) {
		this.amount4 = amount4;
	}

	/**
	 * @return the amount5
	 */
	@Column(name="jou_en_amount_5", nullable = true)
	public BigDecimal getAmount5() {
		return amount5;
	}

	/**
	 * @param amount5 the amount5 to set
	 */
	public void setAmount5(BigDecimal amount5) {
		this.amount5 = amount5;
	}

	/**
	 * @return the amount6
	 */
	@Column(name="jou_en_amount_6", nullable = true)
	public BigDecimal getAmount6() {
		return amount6;
	}


	/**
	 * @param amount6 the amount6 to set
	 */
	public void setAmount6(BigDecimal amount6) {
		this.amount6 = amount6;
	}


	/**
	 * @return the amount7
	 */
	@Column(name="jou_en_amount_7", nullable = true)
	public BigDecimal getAmount7() {
		return amount7;
	}

	/**
	 * @param amount7 the amount7 to set
	 */
	public void setAmount7(BigDecimal amount7) {
		this.amount7 = amount7;
	}

	/**
	 * @return the amount8
	 */
	@Column(name="jou_en_amount_8", nullable = true)
	public BigDecimal getAmount8() {
		return amount8;
	}

	/**
	 * @param amount8 the amount8 to set
	 */
	public void setAmount8(BigDecimal amount8) {
		this.amount8 = amount8;
	}

	/**
	 * @return the amount9
	 */
	@Column(name="jou_en_amount_9", nullable = true)
	public BigDecimal getAmount9() {
		return amount9;
	}

	/**
	 * @param amount9 the amount9 to set
	 */
	public void setAmount9(BigDecimal amount9) {
		this.amount9 = amount9;
	}

	/**
	 * @return the currency
	 */
    @Column(name = "cur_id", nullable = true)
    @Convert(converter = ECurrency.class)
	public ECurrency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(ECurrency currency) {
		this.currency = currency;
	}

	/**
	 * @return the organization
	 */
    @ManyToOne
    @JoinColumn(name="org_id", nullable = false)
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the info
	 */
    @Column(name="jou_en_info", nullable = true)
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the otherInfo
	 */
    @Column(name="jou_en_other_info", nullable = true)
	public String getOtherInfo() {
		return otherInfo;
	}

	/**
	 * @param otherInfo the otherInfo to set
	 */
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	/**
	 * @return the accountLedger
	 */
	@OneToMany(mappedBy="journalEntry", fetch = FetchType.LAZY)
	public List<AccountLedger> getAccountLedger() {
		return accountLedger;
	}

	/**
	 * @param accountLedger the accountLedger to set
	 */
	public void setAccountLedger(List<AccountLedger> accountLedger) {
		this.accountLedger = accountLedger;
	}


	/**
	 * @return the transactionEntry
	 */
	@ManyToOne
    @JoinColumn(name="tra_en_id", nullable = true)
	public TransactionEntry getTransactionEntry() {
		return transactionEntry;
	}


	/**
	 * @param transactionEntry the transactionEntry to set
	 */
	public void setTransactionEntry(TransactionEntry transactionEntry) {
		this.transactionEntry = transactionEntry;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public List<BigDecimal> getAmounts() {
		List<BigDecimal> amounts = new ArrayList<>();
		if (getAmount() != null) {
			amounts.add(getAmount());
		}
		if (getAmount2() != null) {
			amounts.add(getAmount2());
		}
		if (getAmount3() != null) {
			amounts.add(getAmount3());
		}
		if (getAmount4() != null) {
			amounts.add(getAmount4());
		}
		if (getAmount5() != null) {
			amounts.add(getAmount5());
		}
		if (getAmount6() != null) {
			amounts.add(getAmount6());
		}
		if (getAmount7() != null) {
			amounts.add(getAmount7());
		}
		if (getAmount8() != null) {
			amounts.add(getAmount8());
		}
		if (getAmount9() != null) {
			amounts.add(getAmount9());
		}
		return amounts;
	}
}
