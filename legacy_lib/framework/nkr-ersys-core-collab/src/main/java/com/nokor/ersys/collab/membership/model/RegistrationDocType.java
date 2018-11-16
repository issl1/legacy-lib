package com.nokor.ersys.collab.membership.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_document_type")
public class RegistrationDocType extends EntityRefA {
    /** */
	private static final long serialVersionUID = 3536832752432860545L;

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
     */
    @Column(name = "doc_typ_code", nullable = false)
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "doc_typ_desc", nullable = true)
    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
     */
    @Column(name = "doc_typ_desc_en", nullable = true)
    @Override
    public String getDescEn() {
        return desc;
    }
}
