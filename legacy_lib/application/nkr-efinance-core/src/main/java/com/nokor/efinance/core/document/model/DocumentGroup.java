package com.nokor.efinance.core.document.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Document Group
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_document_group")
public class DocumentGroup extends EntityRefA {
	
	private static final long serialVersionUID = -5023898084478773192L;

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
    @Column(name = "doc_grp_code", nullable = false, length = 10, unique = true)
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "doc_grp_desc", nullable = false, length=50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * @return <String>
     */
    @Override
    @Column(name = "doc_grp_desc_en", nullable = false, length=50)
    public String getDescEn() {
        return super.getDescEn();
    }
}
