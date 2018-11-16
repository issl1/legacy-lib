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

/**
 * @author bunlong.taing
 */
@Entity
@Table(name = "td_installment_checked")
public class InstallmentChecked extends EntityA {
	
	/** */
	private static final long serialVersionUID = 8041379395870651020L;
	private Dealer dealer;
	private Date checkedDate;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ins_chk_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the checkedDate
	 */
	@Column(name = "ins_chk_dt_checked", nullable = true)
	public Date getCheckedDate() {
		return checkedDate;
	}

	/**
	 * @param checkedDate the checkedDate to set
	 */
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

}
