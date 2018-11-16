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
 * Service Class.
 * 
 * @author ly.youhort
 * 
 */
@Entity
@Table(name = "tu_financial_product_service")
public class FinProductService extends EntityA {

	private static final long serialVersionUID = -2109001780570872581L;

	private FinProduct financialProduct;
	private FinService service;
	private boolean mandatory = false;
	private boolean hidden = false;
	
    public FinProductService() {
    }
    
    /**
     * @return The id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fpd_fin_ser_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    

	/**
	 * @return the financialProduct
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fpd_id")
	public FinProduct getFinancialProduct() {
		return financialProduct;
	}

	/**
	 * @param financialProduct the financialProduct to set
	 */
	public void setFinancialProduct(FinProduct financialProduct) {
		this.financialProduct = financialProduct;
	}

	/**
	 * @return the service
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_srv_id")
	public FinService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(FinService service) {
		this.service = service;
	}

	/**
	 * @return the mandatory
	 */
    @Column(name = "fpd_fin_ser_bl_mandatory", nullable = false)
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the hidden
	 */
	@Column(name = "fpd_fin_ser_bl_hidden", nullable = false)
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
