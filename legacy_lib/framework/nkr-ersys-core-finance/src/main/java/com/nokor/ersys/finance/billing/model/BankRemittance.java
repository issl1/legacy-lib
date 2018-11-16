package com.nokor.ersys.finance.billing.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.content.model.file.FileData;
import com.nokor.ersys.core.finance.model.Bank;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_bank_remittance")
public class BankRemittance extends EntityA {
	/** */
	private static final long serialVersionUID = -4890168819532522581L;
	
	private String desc;
	private Bank bank;
    private Date transferDate;
    private Date startDate;
    private Date endDate;
    private Double totalAmount;
    private FileData file;
   
    /**
	 * 
	 * @return
	 */
	public static BankRemittance createInstance(FileData fileData) {
		BankRemittance banRem = EntityFactory.createInstance(BankRemittance.class);
		banRem.setDesc(I18N.message("message.remittance.desc.default"));
		banRem.setFile(fileData);
		banRem.setTransferDate(new Date());

        return banRem;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_rem_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }


	/**
	 * @return the desc
	 */
	@Column(name = "ban_rem_desc", nullable = false)
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
	 * @return the bank
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = false)
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
	 * @return the transferDate
	 */
	@Column(name = "ban_rem_dt_transfer", nullable = false)
	public Date getTransferDate() {
		return transferDate;
	}


	/**
	 * @param transferDate the transferDate to set
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}


	/**
	 * @return the startDate
	 */
	@Column(name = "ban_rem_dt_start", nullable = false)
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	@Column(name = "ban_rem_dt_end", nullable = false)
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the totalAmount
	 */
	@Column(name = "ban_rem_amt_total", nullable = false)
	public Double getTotalAmount() {
		return totalAmount;
	}


	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	/**
	 * @return the file
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fil_id", nullable = true)
	public FileData getFile() {
		return file;
	}


	/**
	 * @param file the file to set
	 */
	public void setFile(FileData file) {
		this.file = file;
	}



}
