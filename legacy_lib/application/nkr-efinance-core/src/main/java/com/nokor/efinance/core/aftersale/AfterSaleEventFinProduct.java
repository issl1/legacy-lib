package com.nokor.efinance.core.aftersale;

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

import com.nokor.efinance.core.financial.model.FinProduct;

/**
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_after_sale_event_fin_product")
public class AfterSaleEventFinProduct extends EntityA {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private FinProduct finProduct;
	private AfterSaleEvent afterSaleEvent;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aft_evt_fpd_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the finProduct
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fpd_id")
	public FinProduct getFinProduct() {
		return finProduct;
	}

	/**
	 * @param finProduct the finProduct to set
	 */
	public void setFinProduct(FinProduct finProduct) {
		this.finProduct = finProduct;
	}

	/**
	 * @return the afterSaleEvent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aft_evt_id")
	public AfterSaleEvent getAfterSaleEvent() {
		return afterSaleEvent;
	}

	/**
	 * @param afterSaleEvent the afterSaleEvent to set
	 */
	public void setAfterSaleEvent(AfterSaleEvent afterSaleEvent) {
		this.afterSaleEvent = afterSaleEvent;
	}	
}
