package com.nokor.efinance.core.stock.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_product_stock_inventory")
public class ProductStockInventory extends EntityA {
	
	private static final long serialVersionUID = -7849333867244064716L;
	
	private Dealer dealer;
	private Product product;
	private EStockReason stockReason;
	private Date date;
	private int qty;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_sto_inv_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
	 * @return the dealer
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	
	/**
	 * @return the product
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prd_id")
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	
	/**
	 * @return the stockReason
	 */
    @Column(name = "sto_rea_id", nullable = false)
    @Convert(converter = EStockReason.class)
	public EStockReason getStockReason() {
		return stockReason;
	}

	/**
	 * @param stockReason the stockReason to set
	 */
	public void setStockReason(EStockReason stockReason) {
		this.stockReason = stockReason;
	}
	
	/**
	 * @return the date
	 */
	@Column(name = "prd_sto_inv_dt_date", nullable = true)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the qty
	 */
	@Column(name = "prd_sto_inv_nu_qty", nullable = false)
	public int getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}
}
