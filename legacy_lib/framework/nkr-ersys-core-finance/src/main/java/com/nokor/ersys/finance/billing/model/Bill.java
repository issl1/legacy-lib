package com.nokor.ersys.finance.billing.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nokor.ersys.core.finance.model.BankAccount;
import com.nokor.ersys.core.hr.model.address.Address;



/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_bill")
public class Bill extends BaseBill {
	/** */
	private static final long serialVersionUID = -361555352295548826L;

	private Bill origin;	// If type = Quotation 	Then origin is null
							// If type = Order 		Then origin can be null or a Quotation 
							// If type = Invoice 	Then origin can be null or a Quotation or an Order
							// If type = CreditNote Then origin can be null or an Invoice 
	
	private Integer nbPrints;	// How many times the bill has been printed
	private EDiscountType discountType;
	private Double discountAmount;
	private Address address;
	private PaymentCondition paymentCondition;
	private EPaymentMethod paymentMethod;
	private BankAccount recipientBankAccount;

	private List<BillLine> lines;
	private List<Payment> payments;


    /**
     * 
     * @return
     */
    public static Bill createInstance() {
    	Bill invoice = newInstance(Bill.class);
    	invoice.setType(EBillType.INVOICE);
		return invoice;
    }


    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bil_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the nbPrints
	 */
    @Column(name = "bil_nb_prints", nullable = true)
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
	 * @return the discountType
	 */
    @Column(name = "dis_typ_id", nullable = true)
    @Convert(converter = EDiscountType.class)
	public EDiscountType getDiscountType() {
		return discountType;
	}

	/**
	 * @param discountType the discountType to set
	 */
	public void setDiscountType(EDiscountType discountType) {
		this.discountType = discountType;
	}

	/**
	 * @return the discountAmount
	 */
    @Column(name = "bil_discount_amount", nullable = true)
	public Double getDiscountAmount() {
    	if (discountAmount == null) {
    		discountAmount = 0d;
    	}
		return discountAmount;
	}

	/**
	 * @param discountAmount the discountAmount to set
	 */
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * @return the paymentMethod
	 */
    @Column(name = "pay_met_id", nullable = true)
    @Convert(converter = EPaymentMethod.class)
	public EPaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(EPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the recipientBankAccount
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_ban_acc_id", nullable = true)
	public BankAccount getRecipientBankAccount() {
		return recipientBankAccount;
	}

	/**
	 * @param recipientBankAccount the recipientBankAccount to set
	 */
	public void setRecipientBankAccount(BankAccount recipientBankAccount) {
		this.recipientBankAccount = recipientBankAccount;
	}

	/**
	 * @return the paymentCondition
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_con_id", nullable = true)
	public PaymentCondition getPaymentCondition() {
		return paymentCondition;
	}

	/**
	 * @param paymentCondition the paymentCondition to set
	 */
	public void setPaymentCondition(PaymentCondition paymentCondition) {
		this.paymentCondition = paymentCondition;
	}


	/**
	 * @return the origin
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_bil_id", nullable = true)
	public Bill getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Bill origin) {
		this.origin = origin;
	}

	/**
	 * @return the address
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bil_add_id", nullable = false)
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the lines
	 */
	@OneToMany(mappedBy="bill", fetch = FetchType.LAZY)
	public List<BillLine> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<BillLine> lines) {
		this.lines = lines;
	}

	/**
	 * 
	 * @return
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="td_invoice_payment",
				joinColumns = { @JoinColumn(name = "bil_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "pay_id") })
	public List<Payment> getPayments() {
		return payments;
	}

	/**
	 * @param payments the payments to set
	 */
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	

//	@Transient
//	public String getFormattedNumber() {
//		String strInvNum = StringUtils.leftPad(getNumber() != null ? getNumber() : "NA", AppConfigUtil.INV_NUMBER_LENGHT, '0');
//		String mmyy = "";
//		if (getInvoiceDate() != null) {
//			mmyy = DateUtils.getDateLabel(getInvoiceDate(), "MMyy");
//		}
//		//return strInvNum + " " + mmyy;
//		return strInvNum + mmyy;
//	}
//
//	@Transient
//	public String getFormattedNumberAndCompanyName(){
//		return getFormattedNumber() + (company != null && company.getId() != null? " - " + company.getNameWithInternalCode() : "");
//	}
//
//	
//
//	/**
//	 * @param amountVatExcludeEuro the amountVatExcludeEuro to set
//	 */
//	public void setAmountVatExcludeEuro(Double amountVatExcludeEuro) {
//		this.amountVatExcludeEuro = amountVatExcludeEuro;
//	}
//
//	
//	@Transient
//	public Double getAmountVatIncludeEuro() {
//		return NumberUtils.formatDouble(getAmountAfterDiscountVatExcludeEuro() + getAmountVat());
//	}
//
//	
//	@Transient
//	public Double getAmountVat() {
//		return NumberUtils.formatDouble(getAmountAfterDiscountVatExcludeEuro() * getTaxRateDivBy100());
//	}
//	
//	@Transient
//	public Double getAmountAfterDiscountVatExcludeEuro() {
//		if (invoiceDiscountType != null && invoiceDiscountType.getId().equals(InvoiceDiscountType.AMOUNT)) {
//			return getAmountVatExcludeEuro() - getDiscountAmount();
//		} else {
//			return getAmountVatExcludeEuro() - (getAmountVatExcludeEuro() * getDiscountAmount())/100;
//		}
//	}
//
//	@Transient
//	public List<Payment> getPaymentsExceptAvoirs() {
//		List<Payment> lstPaymentExceptAvoirs = new ArrayList<Payment>();
//		for (Payment pay : getPayments()) {
//    		if (pay.getStatusRecord().equals(StatusRecord.ACTIV)) {
//				if (BooleanUtils.isNotTrue(PaymentMode.AVOIR.equals(pay.getPaymentMode().getId()))) {
//					lstPaymentExceptAvoirs.add(pay);
//				}
//    		}
//		}
//		return lstPaymentExceptAvoirs;
//	}
//
//	@Transient
//	public List<InvoiceHisto> getInvoiceHistoByAvoir (Long avoirId) {
//		if (this.invoiceHistories == null || this.invoiceHistories.size() == 0) {
//			return null;
//		}
//
//		List<InvoiceHisto> invoiceHistoriesByAvoir = new ArrayList<InvoiceHisto>();
//
//		for(InvoiceHisto histo : invoiceHistories) {
//			if (histo.getAvoir() != null && histo.getAvoir().getId() == avoirId) {
//				invoiceHistoriesByAvoir.add(histo);
//			}
//		}
//
//		return invoiceHistoriesByAvoir;
//	}
//
//	@Transient
//	public List<InvoiceHisto> getInvoiceHistoByPayment (Long paymentId) {
//		if (this.invoiceHistories == null || this.invoiceHistories.size() == 0) {
//			return null;
//		}
//
//		List<InvoiceHisto> invoiceHistoriesByPayment = new ArrayList<InvoiceHisto>();
//
//		for(InvoiceHisto histo : invoiceHistories) {
//			if (histo.getPayment() != null && histo.getPayment().getId() == paymentId) {
//				invoiceHistoriesByPayment.add(histo);
//			}
//		}
//
//		return invoiceHistoriesByPayment;
//	}
//
//	
//	@Transient
//	public List<Invoice> getAvoirsInvoiceCorrection() {
//		List<Invoice> lstAvoirsCorrection = new ArrayList<Invoice>();
//		for (Invoice avoir : getAvoirs()) {
//			if (BooleanUtils.isNotTrue(avoir.getIsDisbursementAvoir())) {
//				lstAvoirsCorrection.add(avoir);
//			}
//		}
//		return lstAvoirsCorrection;
//	}
//
//
//
//	@Transient
//	public String getPaymentModeDesc() {
//		String desc = "" ;
//		if (payments != null && !payments.isEmpty()) {
//			Payment payment = payments.get(0);
//			desc = payment.getPaymentMode().getDesc();
//		} else {
//			if (paymentMode != null) {
//				desc = paymentMode.getDesc();
//			}
//		}
//
//		return desc;
//	}
//
//	
//	
//
//    /**
//	 * It calculates the remaining due the real payments (deducting the Avoir as payment mode)
//     * @return
//     */
//    @Transient
//	public double getRemainingPaymentDueAmount () {
//		double totalPaymentAmount = getTotalPaymentAmount();
//		double totalAvoirAmount = getTotalAvoirsAmount();
//		double balance = getAmountVatIncludeEuro() - totalPaymentAmount - totalAvoirAmount;
//		
//		return NumberUtils.formatDouble(balance);
//	}
//    
//    @Transient
//    public double getTotalPaymentAmount() {
//    	double totalPaymentAmount = 0d;
//    	for (Payment pay : getPayments()) {
//    		if (pay.getStatusRecord().equals(StatusRecord.ACTIV)) {
//				if (BooleanUtils.isTrue(PaymentMode.AVOIR.equals(pay.getPaymentMode().getId()))) {
//					totalPaymentAmount -= pay.getPayAmount().doubleValue();
//				} else {
//					totalPaymentAmount += pay.getPayAmount().doubleValue();
//				}
//    		}
//		}
//    	
//    	return totalPaymentAmount;
//    }
//    	
//    @Transient
//    public double getTotalAvoirsAmount() {
//		double totalAvoirAmount = 0d;
//
//    	for (Invoice avoir : getAvoirs()) {
//			totalAvoirAmount += avoir.getAmountVatIncludeEuro().doubleValue();
//		}
//    	
//    	return totalAvoirAmount;
//    }
}
