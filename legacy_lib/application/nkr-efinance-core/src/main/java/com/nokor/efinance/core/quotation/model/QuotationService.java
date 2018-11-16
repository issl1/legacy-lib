package com.nokor.efinance.core.quotation.model;

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

import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.EChargePoint;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation_service")
public class QuotationService extends EntityA {

	private static final long serialVersionUID = -8700268747245070026L;
	
	private FinService service;
	private Quotation quotation;
	private double tiPrice;
	private double tePrice;
	private double vatPrice;
	private double vatValue;
	private boolean includeInInstallment;
	private boolean splitWithInstallment;
	private EChargePoint chargePoint;
		
	/**
     * Get quotation service's is.
     * @return The quotation service's id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_fin_srv_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
	/**
	 * @return the service
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_id")
	public FinService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(FinService service) {
		this.service = service;
	}


	/**
	 * @return the quotation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quo_id")
	public Quotation getQuotation() {
		return quotation;
	}

	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}	
	
	/**
	 * @return the tiPrice
	 */
	@Column(name = "quo_fin_srv_am_ti_price", nullable = true)
	public double getTiPrice() {
		return tiPrice;
	}

	/**
	 * @param tiPrice the tiPrice to set
	 */
	public void setTiPrice(double tiPrice) {
		this.tiPrice = tiPrice;
	}

	/**
	 * @return the tePrice
	 */
	@Column(name = "quo_fin_srv_am_te_price", nullable = true)
	public double getTePrice() {
		return tePrice;
	}

	/**
	 * @param tePrice the tePrice to set
	 */
	public void setTePrice(double tePrice) {
		this.tePrice = tePrice;
	}

	/**
	 * @return the vatPrice
	 */
	@Column(name = "quo_fin_srv_am_vat_price", nullable = true)
	public double getVatPrice() {
		return vatPrice;
	}

	/**
	 * @param vatPrice the vatPrice to set
	 */
	public void setVatPrice(double vatPrice) {
		this.vatPrice = vatPrice;
	}
	
	/**
	 * @return the vatValue
	 */
	@Column(name = "quo_fin_srv_rt_vat", nullable = true, columnDefinition="double precision default '0'")
	public double getVatValue() {
		return vatValue;
	}

	/**
	 * @param vatValue the vatValue to set
	 */
	public void setVatValue(double vatValue) {
		this.vatValue = vatValue;
	}

	/**
	 * @return the includeInInstallment
	 */
	@Column(name = "quo_fin_srv_bl_include_in_installment", nullable = true, columnDefinition = "boolean default false")
	public boolean isIncludeInInstallment() {
		return includeInInstallment;
	}

	/**
	 * @param includeInInstallment the includeInInstallment to set
	 */
	public void setIncludeInInstallment(boolean includeInInstallment) {
		this.includeInInstallment = includeInInstallment;
	}

	/**
	 * @return the splitWithInstallment
	 */
	@Column(name = "quo_fin_srv_bl_split_with_installment", nullable = true, columnDefinition = "boolean default false")
	public boolean isSplitWithInstallment() {
		return splitWithInstallment;
	}

	/**
	 * @param splitWithInstallment the splitWithInstallment to set
	 */
	public void setSplitWithInstallment(boolean splitWithInstallment) {
		this.splitWithInstallment = splitWithInstallment;
	}
	
	/**
	 * @return the chargePoint
	 */
	@Column(name = "cha_pnt_id", nullable = true)
    @Convert(converter = EChargePoint.class)
	public EChargePoint getChargePoint() {
		return chargePoint;
	}

	/**
	 * @param chargePoint the chargePoint to set
	 */
	public void setChargePoint(EChargePoint chargePoint) {
		this.chargePoint = chargePoint;
	}
}
