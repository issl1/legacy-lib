package com.nokor.efinance.glf.statistic.model;

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
 * 
 * @author sok.vina
 *
 */
@Entity
@Table(name = "td_statistic_3hours_visitor")
public class Statistic3HoursVisitor extends EntityA {

    private static final long serialVersionUID = -4053267507390718585L;
    
    private Dealer dealer;
	private Integer numberVisitorDealer11;
	private Integer numberVisitorDealer14;
	private Integer numberVisitorDealer17;
    private Integer numberVisitorCompany11;
    private Integer numberVisitorCompany14;
    private Integer numberVisitorCompany17;
    
    private Integer numberVisitorApplied11;
    private Integer numberVisitorApplied14;
    private Integer numberVisitorApplied17;
    
    private Date date; 

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "visto_id", unique = true, nullable = false)
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
	 * @return the numberVisitorDealer11
	 */
	@Column(name = "visto_nu_visitor_dealer_11", nullable = true)
	public Integer getNumberVisitorDealer11() {
		return numberVisitorDealer11;
	}

	/**
	 * @param numberVisitorDealer11 the numberVisitorDealer11 to set
	 */
	public void setNumberVisitorDealer11(Integer numberVisitorDealer11) {
		this.numberVisitorDealer11 = numberVisitorDealer11;
	}

	/**
	 * @return the numberVisitorDealer14
	 */
	@Column(name = "visto_nu_visitor_dealer_14", nullable = true)
	public Integer getNumberVisitorDealer14() {
		return numberVisitorDealer14;
	}

	/**
	 * @param numberVisitorDealer14 the numberVisitorDealer14 to set
	 */
	public void setNumberVisitorDealer14(Integer numberVisitorDealer14) {
		this.numberVisitorDealer14 = numberVisitorDealer14;
	}

	/**
	 * @return the numberVisitorDealer17
	 */
	@Column(name = "visto_nu_visitor_dealer_17", nullable = true)
	public Integer getNumberVisitorDealer17() {
		return numberVisitorDealer17;
	}

	/**
	 * @param numberVisitorDealer17 the numberVisitorDealer17 to set
	 */
	public void setNumberVisitorDealer17(Integer numberVisitorDealer17) {
		this.numberVisitorDealer17 = numberVisitorDealer17;
	}

	/**
	 * @return the numberVisitorCompany11
	 */
	@Column(name = "visto_nu_visitor_company_11", nullable = true)
	public Integer getNumberVisitorCompany11() {
		return numberVisitorCompany11;
	}

	/**
	 * @param numberVisitorCompany11 the numberVisitorCompany11 to set
	 */
	public void setNumberVisitorCompany11(Integer numberVisitorCompany11) {
		this.numberVisitorCompany11 = numberVisitorCompany11;
	}

	/**
	 * @return the numberVisitorCompany14
	 */
	@Column(name = "visto_nu_visitor_company_14", nullable = true)
	public Integer getNumberVisitorCompany14() {
		return numberVisitorCompany14;
	}

	/**
	 * @param numberVisitorCompany14 the numberVisitorCompany14 to set
	 */
	public void setNumberVisitorCompany14(Integer numberVisitorCompany14) {
		this.numberVisitorCompany14 = numberVisitorCompany14;
	}

	/**
	 * @return the numberVisitorCompany17
	 */
	@Column(name = "visto_nu_visitor_company_17", nullable = true)
	public Integer getNumberVisitorCompany17() {
		return numberVisitorCompany17;
	}

	/**
	 * @param numberVisitorCompany17 the numberVisitorCompany17 to set
	 */
	public void setNumberVisitorCompany17(Integer numberVisitorCompany17) {
		this.numberVisitorCompany17 = numberVisitorCompany17;
	}

	/**
	 * @return the numberVisitorApplied11
	 */
	@Column(name = "visto_nu_visitor_applied_11", nullable = true)
	public Integer getNumberVisitorApplied11() {
		return numberVisitorApplied11;
	}

	/**
	 * @param numberVisitorApplied11 the numberVisitorApplied11 to set
	 */
	public void setNumberVisitorApplied11(Integer numberVisitorApplied11) {
		this.numberVisitorApplied11 = numberVisitorApplied11;
	}

	/**
	 * @return the numberVisitorApplied14
	 */
	@Column(name = "visto_nu_visitor_applied_14", nullable = true)
	public Integer getNumberVisitorApplied14() {
		return numberVisitorApplied14;
	}

	/**
	 * @param numberVisitorApplied14 the numberVisitorApplied14 to set
	 */
	public void setNumberVisitorApplied14(Integer numberVisitorApplied14) {
		this.numberVisitorApplied14 = numberVisitorApplied14;
	}

	/**
	 * @return the numberVisitorApplied17
	 */
	@Column(name = "visto_nu_visitor_applied_17", nullable = true)
	public Integer getNumberVisitorApplied17() {
		return numberVisitorApplied17;
	}

	/**
	 * @param numberVisitorApplied17 the numberVisitorApplied17 to set
	 */
	public void setNumberVisitorApplied17(Integer numberVisitorApplied17) {
		this.numberVisitorApplied17 = numberVisitorApplied17;
	}

	/**
	 * @return the date
	 */
	@Column(name = "visto_dt_date", nullable = true)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
