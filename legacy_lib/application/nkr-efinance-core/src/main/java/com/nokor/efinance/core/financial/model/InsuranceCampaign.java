package com.nokor.efinance.core.financial.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_insurance_campaign")
public class InsuranceCampaign extends EntityRefA {	
	/**
	 */
	private static final long serialVersionUID = -909057246405454647L;
	
	private Date startDate;
	private Date endDate;
		
	private Integer nbCoverageInYears;	
	private Double insuranceFee;
	private EChargePoint chargePoint;		
	private Organization insuranceCompany;
	
	private List<InsuranceCampaignDealer> insuranceCampaignDealers;
	
	/**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ins_cam_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "ins_cam_code", nullable = false, length = 10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "ins_cam_desc", nullable = true)
    public String getDesc() {
        return super.getDesc();
    }

    /** 
     * @return <String>
     */
    @Override
    @Column(name = "ins_cam_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }
    
    /**
	 * @return the startDate
	 */
    @Column(name = "ins_cam_dt_start", nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "ins_cam_dt_end", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the nbCoverageInYears
	 */
	@Column(name = "ins_cam_nu_nb_coverage_in_years", nullable = true)
	public Integer getNbCoverageInYears() {
		return nbCoverageInYears;
	}

	/**
	 * @param nbCoverageInYears the nbCoverageInYears to set
	 */
	public void setNbCoverageInYears(Integer nbCoverageInYears) {
		this.nbCoverageInYears = nbCoverageInYears;
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

	/**
	 * @return the insuranceFee
	 */
	@Column(name = "ins_cam_am_insurance_fee", nullable = true)
	public Double getInsuranceFee() {
		return insuranceFee;
	}

	/**
	 * @param insuranceFee the insuranceFee to set
	 */
	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	/**
	 * @return the insuranceCompany
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_id", nullable = true)
	public Organization getInsuranceCompany() {
		return insuranceCompany;
	}

	/**
	 * @param insuranceCompany the insuranceCompany to set
	 */
	public void setInsuranceCompany(Organization insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	/**
	 * @return the insuranceCampaignDealers
	 */
	@OneToMany(mappedBy="insuranceCampaign", fetch = FetchType.LAZY)
	public List<InsuranceCampaignDealer> getInsuranceCampaignDealers() {
		return insuranceCampaignDealers;
	}

	/**
	 * @param insuranceCampaignDealers the insuranceCampaignDealers to set
	 */
	public void setInsuranceCampaignDealers(List<InsuranceCampaignDealer> insuranceCampaignDealers) {
		this.insuranceCampaignDealers = insuranceCampaignDealers;
	}
		
}
