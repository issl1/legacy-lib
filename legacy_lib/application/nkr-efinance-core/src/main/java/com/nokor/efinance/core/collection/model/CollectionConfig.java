package com.nokor.efinance.core.collection.model;

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

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_collection_config")
public class CollectionConfig extends EntityA {

	/** */
	private static final long serialVersionUID = 6326951008621768160L;

	private EColType colType;
	private FinService collectionFee;
	private FinService reposessionFee;
	private Integer extendInDay;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "col_cof_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the colType
	 */
	@Column(name = "col_typ_id", nullable = false)
	@Convert(converter = EColType.class)
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}
	
	/**
	 * @return the extendInDay
	 */
	@Column(name = "col_cof_extend_in_day", nullable = true)
	public Integer getExtendInDay() {
		return extendInDay;
	}

	/**
	 * @param extendInDay the extendInDay to set
	 */
	public void setExtendInDay(Integer extendInDay) {
		this.extendInDay = extendInDay;
	}

	/**
	 * @return the collectionFee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_col_fee_id", nullable = true)
	public FinService getCollectionFee() {
		return collectionFee;
	}

	/**
	 * @param collectionFee the collectionFee to set
	 */
	public void setCollectionFee(FinService collectionFee) {
		this.collectionFee = collectionFee;
	}

	/**
	 * @return the reposessionFee
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_repo_fee_id", nullable = true)
	public FinService getReposessionFee() {
		return reposessionFee;
	}

	/**
	 * @param reposessionFee the reposessionFee to set
	 */
	public void setReposessionFee(FinService reposessionFee) {
		this.reposessionFee = reposessionFee;
	}

}
