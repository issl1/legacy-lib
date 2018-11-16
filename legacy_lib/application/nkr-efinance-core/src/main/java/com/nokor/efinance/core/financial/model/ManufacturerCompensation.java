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
@Table(name = "tu_manufacturer_compensation")
public class ManufacturerCompensation extends EntityA {	

	/**
	 */
	private static final long serialVersionUID = -6855361556660811839L;
	
	private AssetMake assetMake;
	private AssetModel assetModel;
	private Double refundPercentage;
	private Integer fromMonth;
	private Integer toMonth;
	
	private Date startDate;
	private Date endDate;
		
	/**
     * 
     * @return
     */
    public static ManufacturerCompensation createInstance() {
    	ManufacturerCompensation instance = EntityFactory.createInstance(ManufacturerCompensation.class);
        return instance;
    }
	
	/**
     * @return
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "man_com_id", unique = true, nullable = false)
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
	 * @return the refundPercentage
	 */
	@Column(name = "man_com_refund_pct", nullable = true)
	public Double getRefundPercentage() {
		return refundPercentage;
	}

	/**
	 * @param refundPercentage the refundPercentage to set
	 */
	public void setRefundPercentage(Double refundPercentage) {
		this.refundPercentage = refundPercentage;
	}	

	/**
	 * @return the fromMonth
	 */
	@Column(name = "man_com_from_month", nullable = true)
	public Integer getFromMonth() {
		return fromMonth;
	}

	/**
	 * @param fromMonth the fromMonth to set
	 */
	public void setFromMonth(Integer fromMonth) {
		this.fromMonth = fromMonth;
	}

	/**
	 * @return the toMonth
	 */
	@Column(name = "man_com_to_month", nullable = true)
	public Integer getToMonth() {
		return toMonth;
	}

	/**
	 * @param toMonth the toMonth to set
	 */
	public void setToMonth(Integer toMonth) {
		this.toMonth = toMonth;
	}

	/**
	 * @return the startDate
	 */
    @Column(name = "man_com_dt_start", nullable = true)
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
	@Column(name = "man_com_dt_end", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}
}
