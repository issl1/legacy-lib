package com.nokor.efinance.core.stock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_product")
public class Product extends EntityRefA {
	
	private static final long serialVersionUID = -1244256876127622717L;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Column(name = "prd_code", nullable = true, length = 50, unique = true)
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "prd_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * @return <String>
     */
    @Override
    @Column(name = "prd_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }
}
