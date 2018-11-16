package com.nokor.efinance.core.financial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_credit_control_item")
public class CreditControlItem extends EntityA {
			
	private String code;
	private String value1;
	private String value2;
	private String value3;
	
	private CreditControl creditControl;
	
	/** 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 */
	public CreditControlItem() {
		
	}
	
	/**
	 * @param code
	 */
	public CreditControlItem(String code) {
		this.code = code;
	}
	
	/**
	 * 
	 * @param code
	 * @param creditControl
	 */
	public CreditControlItem(String code, CreditControl creditControl) {
		this.code = code;
		this.creditControl = creditControl;
	}
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cre_ctr_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the code
	 */
    @Column(name = "cre_ctr_ite_code", nullable = false, length = 40)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the value1
	 */
	@Column(name = "cre_ctr_ite_value1", nullable = true, length = 100)
	public String getValue1() {
		return value1;
	}

	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	/**
	 * @return the value2
	 */
	@Column(name = "cre_ctr_ite_value2", nullable = true, length = 100)
	public String getValue2() {
		return value2;
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	/**
	 * @return the value3
	 */
	@Column(name = "cre_ctr_ite_value3", nullable = true, length = 100)
	public String getValue3() {
		return value3;
	}

	/**
	 * @param value3 the value3 to set
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}

	/**
	 * @return the creditControl
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cre_ctr_id")
	public CreditControl getCreditControl() {
		return creditControl;
	}

	/**
	 * @param creditControl the creditControl to set
	 */
	public void setCreditControl(CreditControl creditControl) {
		this.creditControl = creditControl;
	}        
	
}
