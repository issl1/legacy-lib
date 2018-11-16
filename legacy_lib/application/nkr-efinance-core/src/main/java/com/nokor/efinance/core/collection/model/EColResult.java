package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author youhort.ly
 *
 */
@Entity
@Table(name = "tu_col_result")
public class EColResult extends EntityRefA {
	/** */
	private static final long serialVersionUID = -7266873441516436821L;
	
	private EColType colTypes;
	
	/**
     * 
     * @return
     */
    public static EColResult createInstance() {
    	EColResult instance = EntityFactory.createInstance(EColResult.class);
        return instance;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_res_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "col_res_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "col_res_desc", nullable = true, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "col_res_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the colTypes
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getColTypes() {
		return colTypes;
	}

	/**
	 * @param colTypes the colTypes to set
	 */
	public void setColTypes(EColType colTypes) {
		this.colTypes = colTypes;
	}	
}
