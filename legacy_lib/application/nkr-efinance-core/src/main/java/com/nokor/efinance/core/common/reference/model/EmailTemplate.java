package com.nokor.efinance.core.common.reference.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * Email Template
 * @author uhout.cheng
 */
@Entity
@Table(name="tu_email_template")
public class EmailTemplate extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 1179766032164125296L;
	
	private String content;
	
	/**
     * 
     * @return
     */
    public static EmailTemplate createInstance() {
    	EmailTemplate instance = EntityFactory.createInstance(EmailTemplate.class);
        return instance;
    }
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mal_tep_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
    @Override
    @Column(name = "mal_tep_code", nullable = false, length=20, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * Get the email template's description in locale language.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "mal_tep_desc", nullable = false, length=255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * Get the email template's description in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "mal_tep_desc_en", nullable = false, length=255)
    public String getDescEn() {
        return super.getDescEn();
    }
    
    /**
	 * @return the content
	 */
    @Column(name = "mal_tep_content", nullable = true)
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