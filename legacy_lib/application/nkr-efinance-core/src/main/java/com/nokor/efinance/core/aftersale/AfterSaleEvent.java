package com.nokor.efinance.core.aftersale;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author vina.sok
 *
 */
@Entity
@Table(name = "tu_after_sale_event")
public class AfterSaleEvent extends EntityRefA {
	
	private static final long serialVersionUID = -6696389166194161054L;
	
	private EAfterSaleEventType afterSaleEventType;
	private Double discountRate;
	
    
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aft_evt_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
	@Override
    @Column(name = "aft_evt_code", nullable = false, length = 10, unique = true)
    public String getCode() {
        return super.getCode();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "aft_evt_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "aft_evt_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }
       

	/**
	 * @return the discountRate
	 */
    @Column(name = "aft_evt_rt_discount", nullable = true)
	public Double getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate the discountRate to set
	 */
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * @return the afterSaleEventType
	 */
    @Column(name = "aft_evt_typ_id", nullable = true)
	@Convert(converter = EAfterSaleEventType.class)
	public EAfterSaleEventType getAfterSaleEventType() {
		return afterSaleEventType;
	}

	/**
	 * @param afterSaleEventType the afterSaleEventType to set
	 */
	public void setAfterSaleEventType(EAfterSaleEventType afterSaleEventType) {
		this.afterSaleEventType = afterSaleEventType;
	}   
    
}
