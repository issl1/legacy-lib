package com.nokor.efinance.core.financial.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_manufacturer_subsidy")
public class ManufacturerSubsidy extends EntityA {	

	/**
	 */
	private static final long serialVersionUID = -6855361556660811839L;
	
	private AssetMake assetMake;
	private AssetModel assetModel;
	private Double subsidyAmount;
	private Integer monthFrom;
	private Integer monthTo;
	
	private Date startDate;
	private Date endDate;
		
	/**
     * 
     * @return
     */
    public static ManufacturerSubsidy createInstance() {
    	ManufacturerSubsidy instance = EntityFactory.createInstance(ManufacturerSubsidy.class);
        return instance;
    }
	
	/**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "man_sub_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
	 * @return the assetMake
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ass_mak_id")
	public AssetMake getAssetMake() {
		return assetMake;
	}

	/**
	 * @param assetMake the assetMake to set
	 */
	public void setAssetMake(AssetMake assetMake) {
		this.assetMake = assetMake;
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
	 * @return the subsidyAmount
	 */
	@Column(name = "man_sub_subsidy_amount", nullable = true)
	public Double getSubsidyAmount() {
		return subsidyAmount;
	}

	/**
	 * @param subsidyAmount the subsidyAmount to set
	 */
	public void setSubsidyAmount(Double subsidyAmount) {
		this.subsidyAmount = subsidyAmount;
	}

	/**
	 * @return the startDate
	 */
    @Column(name = "man_sub_dt_start", nullable = true)
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
	@Column(name = "man_sub_dt_end", nullable = true)
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
	 * @return the monthFrom
	 */
	@Column(name = "man_sub_month_from", nullable = true)
	public Integer getMonthFrom() {
		return monthFrom;
	}

	/**
	 * @param monthFrom the monthFrom to set
	 */
	public void setMonthFrom(Integer monthFrom) {
		this.monthFrom = monthFrom;
	}

	/**
	 * @return the monthTo
	 */
	@Column(name = "man_sub_month_to", nullable = true)
	public Integer getMonthTo() {
		return monthTo;
	}

	/**
	 * @param monthTo the monthTo to set
	 */
	public void setMonthTo(Integer monthTo) {
		this.monthTo = monthTo;
	}

}
