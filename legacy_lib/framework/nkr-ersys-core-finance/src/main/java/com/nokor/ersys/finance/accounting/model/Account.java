package com.nokor.ersys.finance.accounting.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_account")
public class Account extends EntityA implements MAccount {
	/** */
	private static final long serialVersionUID = 6139271816120300493L;

	private String code;
	private String name;
	private String nameEn;
	private String reference;
	private BigDecimal startingBalance;
    private String info;
    private String otherInfo;
	
   private List<AccountLedger> accountEntries;

	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the code
	 */
	@Column(name = "acc_code", nullable = false)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	@Column(name = "acc_name", nullable = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameEn
	 */
	@Column(name = "acc_name_en", nullable = true)
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	/**
	 * @return
	 */
	@Transient
	public String getNameLocale() {
		if (I18N.isEnglishLocale()) {
			return getNameEn();
		} else {
			return getName();
		}
	}

	/**
	 * @return the reference
	 */
	@Column(name = "acc_reference", nullable = true)
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
	 * @return the info
	 */
    @Column(name="acc_info", nullable = true)
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
    @Column(name="acc_other_info", nullable = true)
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
	 * @return the startingBalance
	 */
	@Column(name = "acc_start_balance", nullable = false)
	public BigDecimal getStartingBalance() {
		return startingBalance;
	}

	/**
	 * @param startingBalance the startingBalance to set
	 */
	public void setStartingBalance(BigDecimal startingBalance) {
		this.startingBalance = startingBalance;
	}

	/**
	 * @return the accountEntries
	 */
	@OneToMany(mappedBy="account", fetch = FetchType.LAZY)
	public List<AccountLedger> getAccountEntries() {
		return accountEntries;
	}

	/**
	 * @param accountEntries the accountEntries to set
	 */
	public void setAccountEntries(List<AccountLedger> accountEntries) {
		this.accountEntries = accountEntries;
	}

}
