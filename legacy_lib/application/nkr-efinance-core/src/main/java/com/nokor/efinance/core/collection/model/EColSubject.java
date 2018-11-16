package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
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
@Table(name = "tu_col_subject")
public class EColSubject extends EntityRefA {
	/** */
	private static final long serialVersionUID = -7266873441516436821L;
		
	/**
     * 
     * @return
     */
    public static EColSubject createInstance() {
    	EColSubject instance = EntityFactory.createInstance(EColSubject.class);
        return instance;
    }
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_suj_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "col_suj_code", nullable = false, length = 10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "col_suj_desc", nullable = true, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "col_suj_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }	
}
