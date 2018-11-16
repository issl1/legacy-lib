package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.common.app.eref.ELanguage;

@Entity
@Table(name="ts_cms_content_category")
public class ContentCategory extends EntityRefA implements MContentCategory {
	/** */
	private static final long serialVersionUID = 8729208434007123543L;

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_cat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "con_cat_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "con_cat_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }

	
	@Column(name = "con_cat_desc_en", nullable = true, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }
	
	@Column(name = "con_cat_desc_fr", nullable = true, length = 255)
	@Override
    public String getDescFr() {
        return descFr;
    }
	
    
    @Transient
	public String getDesc(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return desc;
		} else if (lang == ELanguage.FRENCH) {
			return descFr;
		} else {
			return descEn;
		}
	}
    
}
