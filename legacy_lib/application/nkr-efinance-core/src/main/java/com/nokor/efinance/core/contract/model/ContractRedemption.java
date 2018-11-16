package com.nokor.efinance.core.contract.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_contract_redemption")
public class ContractRedemption extends EntityA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5109249629875654460L;
	
	private Contract contract;
	
	private Date redemptionDate;
	private Long pickupLocationId;
	private Date pickupDate;
	private Double redemptionFee;
	private Double discountFees;
	private Double suspand;
	private Double advances;
	
	
	/**
     * 
     * @return
     */
    public static ContractRedemption createInstance() {
    	ContractRedemption instance = EntityFactory.createInstance(ContractRedemption.class);
        return instance;
    }
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_rde_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the contract
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "con_id")
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
	 * @return the pickupLocationId
	 */
	@Column(name = "con_red_pickup_location_id", nullable = true)
	public Long getPickupLocationId() {
		return pickupLocationId;
	}

	/**
	 * @param pickupLocationId the pickupLocationId to set
	 */
	public void setPickupLocationId(Long pickupLocationId) {
		this.pickupLocationId = pickupLocationId;
	}

	/**
	 * @return the pickupDate
	 */
	@Column(name = "con_red_dt_pickup_date", nullable = true)
	public Date getPickupDate() {
		return pickupDate;
	}

	/**
	 * @param pickupDate the pickupDate to set
	 */
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	/**
	 * @return the redemptionFee
	 */
	@Column(name = "con_red_fee", nullable = true)
	public Double getRedemptionFee() {
		return redemptionFee;
	}

	/**
	 * @param redemptionFee the redemptionFee to set
	 */
	public void setRedemptionFee(Double redemptionFee) {
		this.redemptionFee = redemptionFee;
	}

	/**
	 * @return the discountFees
	 */
	@Column(name = "con_red_discount_fee", nullable = true)
	public Double getDiscountFees() {
		return discountFees;
	}

	/**
	 * @param discountFees the discountFees to set
	 */
	public void setDiscountFees(Double discountFees) {
		this.discountFees = discountFees;
	}

	/**
	 * @return the redemptionDate
	 */
	@Column(name = "con_red_date", nullable = true)
	public Date getRedemptionDate() {
		return redemptionDate;
	}

	/**
	 * @param redemptionDate the redemptionDate to set
	 */
	public void setRedemptionDate(Date redemptionDate) {
		this.redemptionDate = redemptionDate;
	}
	
	/**
	 * @return the suspand
	 */
	@Column(name = "con_red_suspand", nullable = true)
	public Double getSuspand() {
		return suspand;
	}

	/**
	 * @param suspand the suspand to set
	 */
	public void setSuspand(Double suspand) {
		this.suspand = suspand;
	}

	/**
	 * @return the advances
	 */
	@Column(name = "con_red_advances", nullable = true)
	public Double getAdvances() {
		return advances;
	}

	/**
	 * @param advances the advances to set
	 */
	public void setAdvances(Double advances) {
		this.advances = advances;
	}

}
