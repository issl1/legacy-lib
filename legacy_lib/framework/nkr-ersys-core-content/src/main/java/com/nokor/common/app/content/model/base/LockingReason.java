package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name="ts_cms_locking_reason")
public class LockingReason extends EntityRefA {
	/** */
	private static final long serialVersionUID = -12071271908614595L;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loc_rea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "loc_rea_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "loc_rea_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }
}
