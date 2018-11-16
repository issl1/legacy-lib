package com.nokor.efinance.core.payment.model;

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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.frmk.security.model.SecUser;

/**
 * The installment receipt
 * 
 * @author bunlong.taing
 * 
 */
@Entity
@Table(name = "td_installment_receipt")
public class InstallmentReceipt extends EntityA {

	/** */
	private static final long serialVersionUID = 5256454172509987820L;

	private Dealer dealer;
	private SecUser userUpload;
	private String path;
	private Date uploadDate;

	/**
	 * Installment receipt id
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ins_rec_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer
	 *            the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the userUpload
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getUserUpload() {
		return userUpload;
	}

	/**
	 * @param userUpload
	 *            the userUpload to set
	 */
	public void setUserUpload(SecUser userUpload) {
		this.userUpload = userUpload;
	}

	/**
	 * @return the path
	 */
	@Column(name = "ins_rec_path", nullable = true)
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the uplaodDate
	 */
	@Column(name = "ins_rec_dt_upload", nullable = true)
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uplaodDate
	 *            the uplaodDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

}
