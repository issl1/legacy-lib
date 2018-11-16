package com.nokor.common.app.content.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.eref.ELanguage;


/**
 * A content can have several Dependencies
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class AbstractDependency extends EntityA {
	/** */
	private static final long serialVersionUID = 8241649280927735875L;

	private DependencyType type;
	private Date dependencyDate;
	
	private String descKh;
	private String descFr;
	private String descEn;
	
    private Integer sortIndex;

   /**
    * 
    * @return
    */
	public static AbstractDependency createInstance() {
		AbstractDependency docDep = EntityFactory.createInstance(AbstractDependency.class);
        return docDep;
	}


	/**
	 * @return the type
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dep_typ_id", nullable = false)
	public DependencyType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(DependencyType type) {
		this.type = type;
	}


	/**
	 * @return the dependencyDate
	 */
	@Column(name = "cnt_dep_date", nullable = false)
	public Date getDependencyDate() {
		return dependencyDate;
	}


	/**
	 * @param dependencyDate the dependencyDate to set
	 */
	public void setDependencyDate(Date dependencyDate) {
		this.dependencyDate = dependencyDate;
	}


	/**
	 * @return the descKH
	 */
	@Column(name = "cnt_dep_desc_kh", nullable = true, length = 1000)
	public String getDescKh() {
		return descKh;
	}


	/**
	 * @param descKH the descKH to set
	 */
	public void setDescKh(String descKh) {
		this.descKh = descKh;
	}


	/**
	 * @return the descFR
	 */
	@Column(name = "cnt_dep_desc_fr", nullable = true, length = 1000)
	public String getDescFr() {
		return descFr;
	}


	/**
	 * @param descFR the descFR to set
	 */
	public void setDescFr(String descFr) {
		this.descFr = descFr;
	}


	/**
	 * @return the descEN
	 */
	@Column(name = "cnt_dep_desc_en", nullable = true, length = 1000)
	public String getDescEn() {
		return descEn;
	}


	/**
	 * @param descEN the descEN to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 */
	@Transient
	public String getDesc(ELanguage lang) {
		if (lang == ELanguage.KHMER) {
			return descKh;
		} else if (lang == ELanguage.FRENCH) {
			return descFr;
		} else {
			return descEn;
		}
	}	

	/**
	 * @return the sortIndex
	 */
	@Column(name = "sort_index", nullable = false)
	public Integer getSortIndex() {
		return sortIndex;
	}


	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}
}
