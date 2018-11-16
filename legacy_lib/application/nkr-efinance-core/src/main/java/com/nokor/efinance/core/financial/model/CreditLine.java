package com.nokor.efinance.core.financial.model;

import java.util.Date;

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
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "tu_credit_line")
public class CreditLine  extends EntityA {

	private static final long serialVersionUID = -4042702218504416936L;
	
	private ProductLine productLine;
	
	private String reference;
	private Date startDate;
	private Date endDate;
		
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cre_lin_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
	 * @return the productLine
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pro_lin_id")
	public ProductLine getProductLine() {
		return productLine;
	}

	/**
	 * @param productLine the productLine to set
	 */
	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
	}

	/**
	 * @return the reference
	 */
    @Column(name = "cre_lin_va_reference", unique = true, nullable = true, length = 20)
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "cre_lin_dt_start", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "cre_lin_dt_end", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
		
}
