package com.nokor.efinance.core.financial.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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

import com.nokor.efinance.core.address.model.Area;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_campaign")
public class Campaign extends EntityRefA implements MCampaign {

	private static final long serialVersionUID = 3344239267196568225L;
		
	private Date startDate;
	private Date endDate;
	
	private Double flatRate;
	private Double maxFlatRate;
	private boolean validForAllAssetMakes;
	private boolean validForAllDealers;
	
	private List<CampaignTerm> terms;
	private List<CampaignAssetCategory> assetCategories;
	private List<CampaignAssetMake> assetMakes;
	private List<CampaignAssetRange> assetRanges;
	private List<CampaignAssetModel> assetModels;
	private List<CampaignDealer> dealers;
	private List<CampaignCreditBureauGrade> grades;
	private List<CampaignDocument> documents;
	
	private CreditControl creditControl;
	
	private Area area;
	
	/**
     * Get financial product's is.
     * @return The financial product's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "cam_code", nullable = false, length=10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset financial product's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "cam_desc", nullable = true)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset financial product's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "cam_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the startDate
	 */
    @Column(name = "cam_dt_start", nullable = false)
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
	@Column(name = "cam_dt_end", nullable = true)
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
	 * @return the flatRate
	 */
	@Column(name = "cam_rt_flat", nullable = true)
	public Double getFlatRate() {
		return flatRate;
	}

	/**
	 * @param flatRate the flatRate to set
	 */
	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}	
	
	/**
	 * @return the maxFlatRate
	 */
	@Column(name = "cam_rt_max_flat", nullable = true)
	public Double getMaxFlatRate() {
		return maxFlatRate;
	}

	/**
	 * @param maxFlatRate the maxFlatRate to set
	 */
	public void setMaxFlatRate(Double maxFlatRate) {
		this.maxFlatRate = maxFlatRate;
	}

	
	/**
	 * @return the terms
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignTerm> getTerms() {
		return terms;
	}

	/**
	 * @param terms the terms to set
	 */
	public void setTerms(List<CampaignTerm> terms) {
		this.terms = terms;
	}	

	/**
	 * @return the assetCategories
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignAssetCategory> getAssetCategories() {
		return assetCategories;
	}

	/**
	 * @param assetCategories the assetCategories to set
	 */
	public void setAssetCategories(List<CampaignAssetCategory> assetCategories) {
		this.assetCategories = assetCategories;
	}

	/**
	 * @return the assetMakes
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignAssetMake> getAssetMakes() {
		return assetMakes;
	}

	/**
	 * @param assetMakes the assetMakes to set
	 */
	public void setAssetMakes(List<CampaignAssetMake> assetMakes) {
		this.assetMakes = assetMakes;
	}	
	
	/**
	 * @return the assetRanges
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignAssetRange> getAssetRanges() {
		return assetRanges;
	}

	/**
	 * @param assetRanges the assetRanges to set
	 */
	public void setAssetRanges(List<CampaignAssetRange> assetRanges) {
		this.assetRanges = assetRanges;
	}

	/**
	 * @return the assetModels
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignAssetModel> getAssetModels() {
		return assetModels;
	}

	/**
	 * @param assetModels the assetModels to set
	 */
	public void setAssetModels(List<CampaignAssetModel> assetModels) {
		this.assetModels = assetModels;
	}

	/**
	 * @return the dealers
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignDealer> getDealers() {
		return dealers;
	}

	/**
	 * @param dealers the dealers to set
	 */
	public void setDealers(List<CampaignDealer> dealers) {
		this.dealers = dealers;
	}

	/**
	 * @return the grades
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignCreditBureauGrade> getGrades() {
		return grades;
	}

	/**
	 * @param grades the grades to set
	 */
	public void setGrades(List<CampaignCreditBureauGrade> grades) {
		this.grades = grades;
	}

	/**
	 * @return the documents
	 */
	@OneToMany(mappedBy="campaign", fetch = FetchType.LAZY)
	public List<CampaignDocument> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<CampaignDocument> documents) {
		this.documents = documents;
	}	
	
	/**
	 * @return the validForAllAssetMakes
	 */
	@Column(name = "cam_bl_valid_for_all_asset_makes", nullable = true, columnDefinition = "boolean default false")
	public boolean isValidForAllAssetMakes() {
		return validForAllAssetMakes;
	}

	/**
	 * @param validForAllAssetMakes the validForAllAssetMakes to set
	 */
	public void setValidForAllAssetMakes(boolean validForAllAssetMakes) {
		this.validForAllAssetMakes = validForAllAssetMakes;
	}

	/**
	 * @return the validForAllDealers
	 */
	@Column(name = "cam_bl_valid_for_all_dealers", nullable = true, columnDefinition = "boolean default false")
	public boolean isValidForAllDealers() {
		return validForAllDealers;
	}

	/**
	 * @param validForAllDealers the validForAllDealers to set
	 */
	public void setValidForAllDealers(boolean validForAllDealers) {
		this.validForAllDealers = validForAllDealers;
	}

	/**
	 * @return the creditControl
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cre_ctr_id")
	public CreditControl getCreditControl() {
		return creditControl;
	}

	/**
	 * @param creditControl the creditControl to set
	 */
	public void setCreditControl(CreditControl creditControl) {
		this.creditControl = creditControl;
	}
	
	/**
	 * @return the area
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "are_id")
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}
	
}
