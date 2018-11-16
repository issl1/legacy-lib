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
 * Letter template
 * 
 * @author youhort
 */
@Entity
@Table(name="tu_letter_template")
public class ELetterTemplate extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 1926023334544670059L;
	
	private String content;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "let_tep_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    @Override
    @Column(name = "let_tep_code", nullable = true, length=20, unique = false)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the eletter template model's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "let_tep_desc", nullable = true, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the asset model's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "let_tep_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the content
	 */
    @Column(name = "let_tep_content", nullable = true)
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