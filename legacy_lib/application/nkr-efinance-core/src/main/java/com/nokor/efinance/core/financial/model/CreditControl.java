package com.nokor.efinance.core.financial.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_credit_control")
public class CreditControl extends EntityA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5917663084617901780L;
	
	private String desc;
	private String descEn;

	private List<CreditControlItem> creditControlItems;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cre_ctr_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the desc
	 */
	@Column(name = "cre_ctr_desc", length = 255)
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the descEn
	 */
	@Column(name = "cre_ctr_desc_en", length = 255)
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * @return the creditControlItems
	 */
	@OneToMany(mappedBy="creditControl", fetch = FetchType.LAZY)
	public List<CreditControlItem> getCreditControlItems() {
		return creditControlItems;
	}

	/**
	 * @param creditControlItems the creditControlItems to set
	 */
	public void setCreditControlItems(List<CreditControlItem> creditControlItems) {
		this.creditControlItems = creditControlItems;
	}	
}
