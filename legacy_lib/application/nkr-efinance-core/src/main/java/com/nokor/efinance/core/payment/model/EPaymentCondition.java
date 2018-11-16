package com.nokor.efinance.core.payment.model;

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
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_payment_condition")
public class EPaymentCondition extends EntityRefA {
	
	private static final long serialVersionUID = -5607356749493808097L;
	
	private EPaymentMethod paymentMethod;
	
	private Integer delay;
	private boolean endOfMonth; 
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_con_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "pay_con_code", nullable = false, length = 10)
	@Override
	public String getCode() {
		return super.getCode();
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "pay_con_desc", nullable = false, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * Get the asset financial product's name in English.
     * 
     * @return <String>
     */
    @Override
    @Column(name = "pay_con_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the paymentMethod
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_met_id", nullable = true)
	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the delay
	 */
	@Column(name = "pay_con_nu_delay", nullable = true)
	public Integer getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	/**
	 * @return the endOfMonth
	 */
	@Column(name = "pay_con_bl_end_of_month", nullable = true)
	public boolean isEndOfMonth() {
		return endOfMonth;
	}

	/**
	 * @param endOfMonth the endOfMonth to set
	 */
	public void setEndOfMonth(boolean endOfMonth) {
		this.endOfMonth = endOfMonth;
	} 
    
}
