package com.nokor.efinance.core.asset.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_asset_plate_number")
public class AssetPlateNumber extends EntityRefA {
	
	private static final long serialVersionUID = -2785763960967734456L;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_pla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Override
	@Transient
	public String getCode() {
		return null;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "ass_pla_desc", nullable = false, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
    @Override
    @Column(name = "ass_pla_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
}
