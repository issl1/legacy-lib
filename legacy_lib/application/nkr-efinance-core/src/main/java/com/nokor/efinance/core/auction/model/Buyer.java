package com.nokor.efinance.core.auction.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Buyer model
 * @author bunlong.taing
 */
@Entity
@Table(name = "tu_buyer")
public class Buyer extends EntityRefA {

	/** */
	private static final long serialVersionUID = -558633209326749799L;
	
	private String firstNameEn;
	private String lastNameEn;
	private String telephone;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buy_id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}
	
	/**
	 * @return the firstNameEn
	 */
	@Column(name = "buy_first_name_en", nullable = false)
	public String getFirstNameEn() {
		return firstNameEn;
	}

	/**
	 * @param firstNameEn the firstNameEn to set
	 */
	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}

	/**
	 * @return the lastNameEn
	 */
	@Column(name = "buy_last_name_en", nullable = false)
	public String getLastNameEn() {
		return lastNameEn;
	}

	/**
	 * @param lastNameEn the lastNameEn to set
	 */
	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}

	/**
	 * @return the telephone
	 */
	@Column(name = "buy_telephone", nullable = false)
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Override
	@Transient
	public String getCode() {
		return null;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Override
	@Transient
	public String getDesc() {
		return null;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Override
	@Transient
	public String getDescEn() {
		return getFirstNameEn() + " " + getLastNameEn();
	}

}
