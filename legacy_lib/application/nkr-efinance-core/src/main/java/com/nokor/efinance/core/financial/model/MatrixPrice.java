package com.nokor.efinance.core.financial.model;

import java.util.Date;

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

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.eref.EColor;

/**
 * Matrix Class.
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_matrix_price")
public class MatrixPrice extends EntityA {

	private static final long serialVersionUID = -1428947425056921525L;
	
	private AssetModel assetModel;
	private Dealer dealer;
	private FinService service;
	private EColor color;
	private Integer year;
	
	private Double tiPriceUsd;
	private Double tePriceUsd;
	private Double vatPriceUsd;
	
	private Date date;
	
	/**
     * Get asset model's is.
     * @return The asset model's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asmpr_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the assetModel
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asmod_id")
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
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deler_id")
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
	 * 
	 * @return the service
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_id")
    public FinService getService() {
		return service;
	}
	/**
	 * 
	 * @param service
	 */
	public void setService(FinService service) {
		this.service = service;
	}

	/**
	 * @return the color
	 */
    @Column(name = "col_id", nullable = true)
    @Convert(converter = EColor.class)
	public EColor getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(EColor color) {
		this.color = color;
	}

	/**
	 * @return the year
	 */
	@Column(name = "matri_nu_year", nullable = true)
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the tiPriceUsd
	 */
	@Column(name = "matri_am_ti_price_usd", nullable = true)
	public Double getTiPriceUsd() {
		return tiPriceUsd;
	}

	/**
	 * @param tiPriceUsd the tiPriceUsd to set
	 */
	public void setTiPriceUsd(Double tiPriceUsd) {
		this.tiPriceUsd = tiPriceUsd;
	}

	/**
	 * @return the tePriceUsd
	 */
	@Column(name = "matri_am_te_price_usd", nullable = true)
	public Double getTePriceUsd() {
		return tePriceUsd;
	}

	/**
	 * @param tePriceUsd the tePriceUsd to set
	 */
	public void setTePriceUsd(Double tePriceUsd) {
		this.tePriceUsd = tePriceUsd;
	}

	/**
	 * @return the vatPriceUsd
	 */
	@Column(name = "matri_am_vat_price_usd", nullable = true)
	public Double getVatPriceUsd() {
		return vatPriceUsd;
	}

	/**
	 * @param vatPriceUsd the vatPriceUsd to set
	 */
	public void setVatPriceUsd(Double vatPriceUsd) {
		this.vatPriceUsd = vatPriceUsd;
	}

	/**
	 * @return the date
	 */
	@Column(name = "asmpr_dt_date", nullable = true)
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
