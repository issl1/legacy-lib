package com.nokor.efinance.core.financial.model;

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

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.ersys.core.hr.model.organization.Organization;


/**
 * Service Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_insurance_service")
public class InsuranceFinService extends EntityA implements MInsuranceFinService {
	/** */
	private static final long serialVersionUID = -5437524311129532434L;

	private Organization insurance;
	private AssetModel assetModel; 
	private FinService service;
	
	private Double premium1Y;
	private Double premium2Y;
	private Double claimAmount1Y;
	private Double claimAmount2YFirstYear;
	private Double claimAmount2YSecondYear;
	
    public InsuranceFinService() {
    }
    
    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ins_ser_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
   	

	/**
	 * @return the insurance
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
	public Organization getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(Organization insurance) {
		this.insurance = insurance;
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
	 * @return the assetModel
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_mod_id")
	public AssetModel getAssetModel() {
		return assetModel;
	}

	/**
	 * @param assetModel the assetModel to set
	 */
	public void setAssetModel(AssetModel assetModel) {
		this.assetModel = assetModel;
	}
	/**
	 * @return the premium1Y
	 */
	@Column(name = "ins_ser_premium_first_year", nullable = true)
	public Double getPremium1Y() {
		return premium1Y;
	}

	/**
	 * @param premium1y the premium1Y to set
	 */
	public void setPremium1Y(Double premium1y) {
		premium1Y = premium1y;
	}

	/**
	 * @return the premium2Y
	 */
	@Column(name = "ins_ser_premium_second_year", nullable = true)
	public Double getPremium2Y() {
		return premium2Y;
	}

	/**
	 * @param premium2y the premium2Y to set
	 */
	
	public void setPremium2Y(Double premium2y) {
		premium2Y = premium2y;
	}

	/**
	 * @return the claimAmount1Y
	 */
	@Column(name = "ins_ser_claim_amount_first_year", nullable = true)
	public Double getClaimAmount1Y() {
		return claimAmount1Y;
	}

	/**
	 * @param claimAmount1Y the claimAmount1Y to set
	 */
	public void setClaimAmount1Y(Double claimAmount1Y) {
		this.claimAmount1Y = claimAmount1Y;
	}

	/**
	 * @return the claimAmount2YFirstYear
	 */
	@Column(name = "ins_ser_claim_amount_2y_first_year", nullable = true)
	public Double getClaimAmount2YFirstYear() {
		return claimAmount2YFirstYear;
	}

	/**
	 * @param claimAmount2YFirstYear the claimAmount2YFirstYear to set
	 */
	public void setClaimAmount2YFirstYear(Double claimAmount2YFirstYear) {
		this.claimAmount2YFirstYear = claimAmount2YFirstYear;
	}

	/**
	 * @return the claimAmount2YSecondYear
	 */
	@Column(name = "ins_ser_claim_amount_2y_second_year", nullable = true)
	public Double getClaimAmount2YSecondYear() {
		return claimAmount2YSecondYear;
	}

	/**
	 * @param claimAmount2YSecondYear the claimAmount2YSecondYear to set
	 */
	public void setClaimAmount2YSecondYear(Double claimAmount2YSecondYear) {
		this.claimAmount2YSecondYear = claimAmount2YSecondYear;
	}
	
}
