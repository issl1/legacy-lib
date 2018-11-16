package com.nokor.common.app.content.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="ts_cms_classification")
public class Classification extends EntityRefA {
	/** */
	private static final long serialVersionUID = -6053504559064402081L;

	private ClassificationType type;
	private Classification parent;
	private String keywordsKh;
	private String keywordsFr;
	private String keywordsEn;
	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "cla_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "cla_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @return the type
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cla_typ_id", nullable = false)
	public ClassificationType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ClassificationType type) {
		this.type = type;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_cla_id", nullable = true)
	public Classification getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Classification parent) {
		this.parent = parent;
	}

	
	/**
	 * @return the keywordsKh
	 */
	@Column(name = "cla_keywordsKh", nullable = true)
	public String getKeywordsKh() {
		return keywordsKh;
	}

	/**
	 * @param keywordsKh the keywordsKh to set
	 */
	public void setKeywordsKh(String keywordsKh) {
		this.keywordsKh = keywordsKh;
	}

	/**
	 * @return the keywordsFr
	 */
	@Column(name = "cla_keywordsFr", nullable = true)
	public String getKeywordsFr() {
		return keywordsFr;
	}

	/**
	 * @param keywordsFr the keywordsFr to set
	 */
	public void setKeywordsFr(String keywordsFr) {
		this.keywordsFr = keywordsFr;
	}

	/**
	 * @return the keywordsEn
	 */
	@Column(name = "cla_keywordsEn", nullable = true)
	public String getKeywordsEn() {
		return keywordsEn;
	}

	/**
	 * @param keywordsEn the keywordsEn to set
	 */
	public void setKeywordsEn(String keywordsEn) {
		this.keywordsEn = keywordsEn;
	}

  
}
