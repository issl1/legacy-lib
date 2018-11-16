package com.nokor.efinance.core.common.reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * SMS template
 * @author youhort
 *
 */
@Entity
@Table(name="tu_sms_template")
public class ESmsTemplate extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 1926023334544670059L;
	
	private String content;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_tep_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    @Override
    @Column(name = "sms_tep_code", nullable = false, length=20, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the asset model's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sms_tep_desc", nullable = false, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset model's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "sms_tep_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }
    
    /**
	 * @return the content
	 */
    @Column(name = "sms_tep_content", nullable = true)
	@Lob
	@Type(type="org.hibernate.type.StringType")
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}    
    	
}