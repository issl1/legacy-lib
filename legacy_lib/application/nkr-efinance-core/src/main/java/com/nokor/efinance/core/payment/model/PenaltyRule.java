package com.nokor.efinance.core.payment.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_penalty_rule")
public class PenaltyRule extends EntityRefA {
	
	private static final long serialVersionUID = 7436541688657222465L;

	private Integer gracePeriod;
	private Double tiPenaltyAmounPerDaytUsd;
	private Double penaltyRate;
	private EPenaltyCalculMethod penaltyCalculMethod;
	private EDayPassDueCalculationMethod dayPassDueCalculationMethod; 
	private String formula;
	
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pen_rul_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Transient
	public String getCode() {
		return "XXX";
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "pen_rul_desc", nullable = true, length = 50)
	@Override
    public String getDesc() {
        return super.getDesc();
    }
	
	/**
     * @return <String>
     */
    @Override
    @Column(name = "pen_rul_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the gracePeriod
	 */
    @Column(name = "pen_rul_nu_grace_period", nullable = true)
	public Integer getGracePeriod() {
		return gracePeriod;
	}

	/**
	 * @param gracePeriod the gracePeriod to set
	 */
	public void setGracePeriod(Integer gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	/**
	 * @return the tiPenaltyAmounPerDaytUsd
	 */
	@Column(name = "pen_rul_am_ti_penalty_amount_per_day", nullable = true)
	public Double getTiPenaltyAmounPerDaytUsd() {
		return tiPenaltyAmounPerDaytUsd;
	}

	/**
	 * @param tiPenaltyAmounPerDaytUsd the tiPenaltyAmounPerDaytUsd to set
	 */
	public void setTiPenaltyAmounPerDaytUsd(Double tiPenaltyAmounPerDaytUsd) {
		this.tiPenaltyAmounPerDaytUsd = tiPenaltyAmounPerDaytUsd;
	}

	/**
	 * @return the penaltyRate
	 */
	@Column(name = "pen_rul_nu_penalty_rate", nullable = true)
	public Double getPenaltyRate() {
		return penaltyRate;
	}

	/**
	 * @param penaltyRate the penaltyRate to set
	 */
	public void setPenaltyRate(Double penaltyRate) {
		this.penaltyRate = penaltyRate;
	}

	/**
	 * @return the penaltyCalculMethod
	 */
	@Column(name = "pen_cal_met_id", nullable = false)
	@Convert(converter = EPenaltyCalculMethod.class)
	public EPenaltyCalculMethod getPenaltyCalculMethod() {
		return penaltyCalculMethod;
	}

	/**
	 * @param penaltyCalculMethod the penaltyCalculMethod to set
	 */
	public void setPenaltyCalculMethod(EPenaltyCalculMethod penaltyCalculMethod) {
		this.penaltyCalculMethod = penaltyCalculMethod;
	}	
	
	/**
	 * @return the dayPassDueCalculationMethod
	 */
	@Column(name = "dpd_cal_met_id", nullable = false)
	@Convert(converter = EDayPassDueCalculationMethod.class)
	public EDayPassDueCalculationMethod getDayPassDueCalculationMethod() {
		return dayPassDueCalculationMethod;
	}

	/**
	 * @param dayPassDueCalculationMethod the dayPassDueCalculationMethod to set
	 */
	public void setDayPassDueCalculationMethod(
			EDayPassDueCalculationMethod dayPassDueCalculationMethod) {
		this.dayPassDueCalculationMethod = dayPassDueCalculationMethod;
	}

	/**
	 * @return the formula
	 */
	@Column(name = "pen_rul_va_formula", nullable = true, length = 100)
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}	
}

