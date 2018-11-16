package com.nokor.efinance.core.stock.model;

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

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_product_stock")
public class ProductStock extends EntityA {
	
	private static final long serialVersionUID = -1244256876127622717L;
	
	private Dealer dealer;
	private Product product;
	private int initialQty;
	private int qty;
	
	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prd_sto_id", unique = true, nullable = false)
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
	 * @return the qty
	 */
	@Column(name = "prd_sto_nu_qty", nullable = false)
	public int getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * @return the initialQty
	 */
	@Column(name = "prd_sto_nu_initial_qty", nullable = false)
	public int getInitialQty() {
		return initialQty;
	}

	/**
	 * @param initialQty the initialQty to set
	 */
	public void setInitialQty(int initialQty) {
		this.initialQty = initialQty;
	}
}
