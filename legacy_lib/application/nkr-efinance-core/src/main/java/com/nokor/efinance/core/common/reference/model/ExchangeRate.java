package com.nokor.efinance.core.common.reference.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.ersys.core.finance.model.eref.ECurrency;

/**
 * Color
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_exchange_rate")
public class ExchangeRate extends EntityRefA {
	
	private static final long serialVersionUID = -2278207833041846997L;

	private ECurrency from;
	private ECurrency to;
	private Double rate;
	private Date start;
	private Date end;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exc_rat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
	 */
	@Override
	@Transient
	public String getCode() {
		return descEn;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "exc_rat_desc", nullable = false, length=50)
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
    @Column(name = "exc_rat_desc_en", nullable = false, length = 50)
    public String getDescEn() {
        return super.getDescEn();
    }

	/**
	 * @return the from
	 */
    @Column(name = "cur_id_from", nullable = false)
    @Convert(converter = EProductLineType.class)
	public ECurrency getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(ECurrency from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
    @Column(name = "cur_id_to", nullable = false)
    @Convert(converter = EProductLineType.class)
	public ECurrency getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(ECurrency to) {
		this.to = to;
	}

	/**
	 * @return the rate
	 */
	@Column(name = "exc_rat_rt_rate", nullable = false)
	public Double getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}

	/**
	 * @return the start
	 */
	@Column(name = "exc_rat__dt_start", nullable = false)
	public Date getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	@Column(name = "exc_rat_dt_end", nullable = true)
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}
    
}
