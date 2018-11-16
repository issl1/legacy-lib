package com.nokor.ersys.finance.billing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author kong.phirun
 *
 */
@Entity
@Table(name = "tu_payment_condition")
public class PaymentCondition extends EntityRefA {
	/** */
	private static final long serialVersionUID = -3103763604664219209L;
	
	public static final Long DAY_30_END_MONTH = 1L;
	public static final Long DAY_60_END_MONTH = 2L;
	public static final Long DAY_90_END_MONTH = 3L;
	public static final Long TODAY = 4L;
	public static final Long DAY_30 = 5L;
	public static final Long DAY_60 = 6L;
	
	private Integer nbDays;
	private Boolean isEndMonth;

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
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "pay_con_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "pay_con_desc", nullable = false)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the nbDays
	 */
	@Column(name = "pay_con_nb_days")
	public Integer getNbDays() {
		return nbDays;
	}

	/**
	 * @param nbDays the nbDays to set
	 */
	public void setNbDays(Integer nbDays) {
		this.nbDays = nbDays;
	}

	/**
	 * @return the isEndMonth
	 */
	@Column(name = "pay_con_is_end_month")
	public Boolean getIsEndMonth() {
		return isEndMonth;
	}

	/**
	 * @param isEndMonth the isEndMonth to set
	 */
	public void setIsEndMonth(Boolean isEndMonth) {
		this.isEndMonth = isEndMonth;
	}
    
}
