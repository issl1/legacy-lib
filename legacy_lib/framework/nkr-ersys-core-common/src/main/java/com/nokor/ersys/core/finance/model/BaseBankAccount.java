package com.nokor.ersys.core.finance.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.eref.ETypeBankAccount;

/**
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseBankAccount extends EntityA  implements MBaseBankAccount {
	/** */
	private static final long serialVersionUID = 5210598434771402357L;

	private ETypeBankAccount typeBankAccount;
	private Bank bank;
	private String branch;
	private String accountHolder;
	private String accountNumber;
	private String comment;
	private Long externalId;
	 

	/**
	 * @return the typeBankAccount
	 */
    @Column(name = "typ_ban_acc_id", nullable = false)
    @Convert(converter = ETypeBankAccount.class)
	public ETypeBankAccount getTypeBankAccount() {
		return typeBankAccount;
	}

	/**
	 * @param typeBankAccount the typeBankAccount to set
	 */
	public void setTypeBankAccount(ETypeBankAccount typeBankAccount) {
		this.typeBankAccount = typeBankAccount;
	}

	/**
	 * @return the bank
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = true)
	public Bank getBank() {
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	/**
	 * @return the branch
	 */
	@Column(name = "ban_branch", nullable = true, length = 100)
	public String getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

	/**
	 * @return the accountHolder
	 */
	@Column(name = "ban_acc_holder", nullable = true, length = 100)
	public String getAccountHolder() {
		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	/**
	 * @return the accountNumber
	 */
	@Column(name = "ban_acc_number", nullable = true, length = 100)
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "ban_acc_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the externalId
	 */
	public Long getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId the externalId to set
	 */
	@Column(name = "ban_external_id", nullable = true)
	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}
}
