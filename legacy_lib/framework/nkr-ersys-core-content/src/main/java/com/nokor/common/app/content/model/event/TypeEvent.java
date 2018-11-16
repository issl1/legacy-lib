package com.nokor.common.app.content.model.event;

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
@Table(name = "ts_cms_type_event")
public class TypeEvent extends EntityRefA {
    /** */
	private static final long serialVersionUID = 1787626914969992379L;

	/**
      * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
      */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typ_eve_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
     */
    @Column(name = "typ_eve_code", nullable = false)
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "typ_eve_desc", nullable = false)
    @Override
    public String getDesc() {
        return desc;
    }

}
