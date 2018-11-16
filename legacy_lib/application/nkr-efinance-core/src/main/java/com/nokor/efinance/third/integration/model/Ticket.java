package com.nokor.efinance.third.integration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_ticket")
public class Ticket extends EntityA {

    private static final long serialVersionUID = -4053267507390718585L;
   
    private String uuid;
    private Contract contract;
    private String reference;
    private EThirdParty thirdParty;
    private Date installmentDate;
    private String username;
    private double paidAmount;
    private String status;
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tik_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the uuid
	 */
    @Column(name = "tik_va_uuid", unique = true, nullable = false, length = 100)
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	
	
	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotra_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the reference
	 */
	@Column(name = "tik_va_reference", nullable = false, length = 15)
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
	 * @return the installmentDate
	 */
	@Column(name = "tik_dt_installment", nullable = true)
	public Date getInstallmentDate() {
		return installmentDate;
	}

	/**
	 * @param installmentDate the installmentDate to set
	 */
	public void setInstallmentDate(Date installmentDate) {
		this.installmentDate = installmentDate;
	}

	/**
	 * @return the thirdParty
	 */
    @Column(name = "thi_id", nullable = true)
    @Convert(converter = EThirdParty.class)
	public EThirdParty getThirdParty() {
		return thirdParty;
	}

	/**
	 * @param thirdParty the thirdParty to set
	 */
	public void setThirdParty(EThirdParty thirdParty) {
		this.thirdParty = thirdParty;
	}

	/**
	 * @return the paidAmount
	 */
	@Column(name = "tik_am_paid", nullable = true)
	public double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @return the username
	 */
	@Column(name = "tik_va_username", nullable = true,  length = 50)
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the status
	 */
	@Column(name = "sta_code", nullable = true, length = 5)
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
