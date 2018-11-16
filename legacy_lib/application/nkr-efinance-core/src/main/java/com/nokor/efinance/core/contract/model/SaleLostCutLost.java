package com.nokor.efinance.core.contract.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * @author poevminea.sann
 */
@Entity
@Table(name = "tu_sale_lost_cut_lost")
public class SaleLostCutLost extends EntityA {

	private static final long serialVersionUID = 8935751397622214350L;
	
	private String scLostCode;
	private String scLostDesc;
	private String scLostDescEn;
	private Date scLostDate;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_lost_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return lost code
	 */
	@Column(name = "sc_lost_code", nullable = true)
	public String getScLostCode() {
		return scLostCode;
	}

	/**
	 * @param scLostCode the scLostCode to set
	 */
	public void setScLostCode(String scLostCode) {
		this.scLostCode = scLostCode;
	}

	/**
	 * @return lost description
	 */
	@Column(name = "sc_lost_desc", nullable = true)
	public String getScLostDesc() {
		return scLostDesc;
	}

	/**
	 * @return
	 */
	@Column(name = "sc_lost_desc_en", nullable = true)
	public String getScLostDescEn() {
		return scLostDescEn;
	}

	/**
	 * @param scLostDescEn
	 */
	public void setScLostDescEn(String scLostDescEn) {
		this.scLostDescEn = scLostDescEn;
	}

	/**
	 * @param scLostDesc the scLostDesc to set
	 */
	public void setScLostDesc(String scLostDesc) {
		this.scLostDesc = scLostDesc;
	}

	/**
	 * @return lost date
	 */
	@Column(name = "sc_lost_date", nullable = true)
	public Date getScLostDate() {
		return scLostDate;
	}

	/**
	 * @param scLostDate the scLostDate to set
	 */
	public void setScLostDate(Date scLostDate) {
		this.scLostDate = scLostDate;
	}
}