package com.nokor.ersys.finance.billing.model;

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
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_bill_line")
public class BillLine extends EntityA {
	/** */
	private static final long serialVersionUID = 4235144763234244504L;

	private Bill bill;
	
	private String label;
	private Double unitPrice;
	private EUnit unit;
	private Double quantity;
	
	/**
     * 
     */
    public static BillLine createInstance(Bill bill) {
    	BillLine billLine = EntityFactory.createInstance(BillLine.class);
    	billLine.setBill(bill);
    	return billLine;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bil_lin_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the bill
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bil_id", nullable = false)
	public Bill getBill() {
		return bill;
	}

	/**
	 * @param bill the bill to set
	 */
	public void setBill(Bill bill) {
		this.bill = bill;
	}

	/**
	 * @return the label
	 */
	@Column(name = "bil_lin_label_text")
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the unit
	 */
    @Column(name = "par_typ_id", nullable = false)
    @Convert(converter = EUnit.class)
	public EUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(EUnit unit) {
		this.unit = unit;
	}

	/**
	 * @return the unitPrice
	 */
	@Column(name = "bil_lin_unit_price")
	public Double getUnitPrice() {
		if (unitPrice == null) {
			unitPrice = 0d;
		}
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the quantity
	 */
	@Column(name = "bil_lin_quantity")
	public Double getQuantity() {
		if (quantity == null) {
			quantity = 0d;
		}
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	
	@Transient
	public Double getPrice() {
		return getQuantity() * getUnitPrice();
	}
}
