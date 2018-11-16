package com.nokor.ersys.finance.billing.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.organization.Employee;



/**
 * Use to receive payment from Customer
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_payment_receipt")
public class PaymentReceipt extends EntityA {
	/** */
	private static final long serialVersionUID = 8852746707388835553L;

	private String reference;
	private Integer nbPrints;
	private Payment payment;
	private Double paidAmount;
	private ECurrency currency;
	private Date receiptDate;
	private Employee receivedBy;
	private String receivedFromLastName;
	private String receivedFromFirstName;
	private String receivedFromPosition;

    /**
     * 
     * @return
     */
    public static PaymentReceipt createInstance() {
    	PaymentReceipt rec = EntityFactory.createInstance(PaymentReceipt.class);
		return rec;
    }


    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_rec_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the reference
	 */
    @Column(name = "pay_rec_reference", nullable = false)
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
	 * @return the nbPrints
	 */
    @Column(name = "pay_rec_nb_prints", nullable = true)
	public Integer getNbPrints() {
    	if (nbPrints == null) {
    		nbPrints = 0;
    	}
		return nbPrints;
	}

	/**
	 * @param nbPrints the nbPrints to set
	 */
	public void setNbPrints(Integer nbPrints) {
		this.nbPrints = nbPrints;
	}



	/**
	 * @return the payment
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_id", nullable = true)
	public Payment getPayment() {
		return payment;
	}

	/**
	 * @param payment the payment to set
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	/**
	 * @return the paidAmount
	 */
    @Column(name = "pay_rec_paid_amount", nullable = false)
	public Double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @return the currency
	 */
    @Column(name = "cur_id", nullable = false)
    @Convert(converter = ECurrency.class)
	public ECurrency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(ECurrency currency) {
		this.currency = currency;
	}

	/**
	 * @return the receiptDate
	 */
    @Column(name = "pay_rec_receipt_date", nullable = false)
	public Date getReceiptDate() {
		return receiptDate;
	}

	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	/**
	 * @return the receivedBy
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_emp_id", nullable = false)
	public Employee getReceivedBy() {
		return receivedBy;
	}

	/**
	 * @param receivedBy the receivedBy to set
	 */
	public void setReceivedBy(Employee receivedBy) {
		this.receivedBy = receivedBy;
	}

	/**
	 * @return the receivedFromLastName
	 */
    @Column(name = "pay_rec_received_from_lastname", nullable = false)
	public String getReceivedFromLastName() {
		return receivedFromLastName;
	}

	/**
	 * @param receivedFromLastName the receivedFromLastName to set
	 */
	public void setReceivedFromLastName(String receivedFromLastName) {
		this.receivedFromLastName = receivedFromLastName;
	}

	/**
	 * @return the receivedFromFirstName
	 */
    @Column(name = "pay_rec_received_from_firstname", nullable = false)
	public String getReceivedFromFirstName() {
		return receivedFromFirstName;
	}

	/**
	 * @param receivedFromFirstName the receivedFromFirstName to set
	 */
	public void setReceivedFromFirstName(String receivedFromFirstName) {
		this.receivedFromFirstName = receivedFromFirstName;
	}

	/**
	 * @return the receivedFromPosition
	 */
    @Column(name = "pay_rec_received_from_position", nullable = true)
	public String getReceivedFromPosition() {
		return receivedFromPosition;
	}

	/**
	 * @param receivedFromPosition the receivedFromPosition to set
	 */
	public void setReceivedFromPosition(String receivedFromPosition) {
		this.receivedFromPosition = receivedFromPosition;
	}

	
}
