package com.nokor.efinance.core.collection.model;

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
@Table(name = "tu_storage_location")
public class StorageLocation extends EntityRefA {
	
	private static final long serialVersionUID = -2785763960967734456L;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sto_loc_id", unique = true, nullable = false)
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
	@Column(name = "costa_desc", nullable = false, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
    @Override
    @Column(name = "costa_desc_en", nullable = false, length=50)
    public String getDescEn() {
        return super.getDescEn();
    }
}
