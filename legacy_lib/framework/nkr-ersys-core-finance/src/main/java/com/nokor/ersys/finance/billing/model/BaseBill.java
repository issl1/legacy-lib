package com.nokor.ersys.finance.billing.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.partner.model.Partner;



/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseBill  extends EntityA {
	/** */
	private static final long serialVersionUID = -5487089081865935274L;

	private String label;
	private String reference;
	private String referenceFormated;
	private EBillType type;					// Quotation/Invoice/Order/CreditNote
	private EBillReason reason;

	private EDirection direction;			// Organization->Partner or Partner->Organization
	private Organization organization;		// the main company
	private Partner partner;				// customer or supplier
	private String partnerName;
	private String partnerContactName;
	private String partnerReference;

	
	private Employee issuer;
	private ECurrency currency;				// official currency

	private Double totalAmountVatExcluded;		// Invoice: total amount calculated from invoice lines
												// CreditNote: not calculated
	
	private Double amountVat;					// VAT amount
	
	private Double totalAmountVatExcludedCur2Dis;	// other currency for display only 
	private Double amountVatCur2Dis;				// other currency for display only 

	private Date billDate;
	private Date dueDate;
	
	/**
     * 
     * @return
     */
    protected static <T extends BaseBill> T newInstance(Class<T> paymentNoteClass) {
    	T note = EntityFactory.createInstance(paymentNoteClass);
        note.setBillDate(new Date());
        note.setIssuer(AppServicesHelper.APP_SESSION_MNG.getCurrentEmployee());
        note.setCurrency(ECurrency.getDefault());
//        note.setNumber(ACC_SRV.generateLastNumber()); // ??? here OR at the Service.createProcess 
    	
		return note;
    }

	/**
	 * @return the label
	 */
	@Column(name = "bil_label", nullable = false)
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
	 * @return the reference
	 */
	@Column(name = "bil_reference", nullable = true)
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
	 * @return the referenceFormated
	 */
	@Column(name = "bil_reference_formatted", nullable = true)
	public String getReferenceFormated() {
		return referenceFormated;
	}

	/**
	 * @param referenceFormated the referenceFormated to set
	 */
	public void setReferenceFormated(String referenceFormated) {
		this.referenceFormated = referenceFormated;
	}

	/**
	 * @return the type
	 */
    @Column(name = "bil_typ_id", nullable = false)
    @Convert(converter = EBillType.class)
	public EBillType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EBillType type) {
		this.type = type;
	}

	
	/**
	 * @return the reason
	 */
    @Column(name = "bil_rea_id", nullable = true)
    @Convert(converter = EBillReason.class)
	public EBillReason getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(EBillReason reason) {
		this.reason = reason;
	}

	/**
	 * @return the direction
	 */
    @Column(name = "bil_dir_id", nullable = false)
    @Convert(converter = EDirection.class)
	public EDirection getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(EDirection direction) {
		this.direction = direction;
	}

	/**
	 * @return the organization
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the partner
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id", nullable = false)
	public Partner getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	/**
	 * @return the issuer
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuer_emp_id", nullable = false)
	public Employee getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(Employee issuer) {
		this.issuer = issuer;
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
	 * @return the amountVatExcluded
	 */
	@Column(name = "bil_total_amount_vat_excluded", nullable = false)
	public Double getTotalAmountVatExcluded() {
		return totalAmountVatExcluded;
	}

	/**
	 * @param amountVatExcluded the amountVatExcluded to set
	 */
	public void setTotalAmountVatExcluded(Double amountVatExcluded) {
		this.totalAmountVatExcluded = amountVatExcluded;
	}

	/**
	 * @return the amountVat
	 */
	@Column(name = "bil_amount_vat", nullable = false)
	public Double getAmountVat() {
		return amountVat;
	}

	/**
	 * @param amountVat the amountVat to set
	 */
	public void setAmountVat(Double amountVat) {
		this.amountVat = amountVat;
	}

	/**
	 * @return the amountVatExcludedCur2Dis
	 */
	@Column(name = "bil_total_amount_vat_excluded_cur2display", nullable = true)
	public Double getTotalAmountVatExcludedCur2Dis() {
		return totalAmountVatExcludedCur2Dis;
	}

	/**
	 * @param amountVatExcludedCur2Dis the amountVatExcludedCur2Dis to set
	 */
	public void setTotalAmountVatExcludedCur2Dis(Double amountVatExcludedCur2Dis) {
		this.totalAmountVatExcludedCur2Dis = amountVatExcludedCur2Dis;
	}

	/**
	 * @return the amountVatCur2Dis
	 */
	@Column(name = "bil_amount_vat_cur2display", nullable = true)
	public Double getAmountVatCur2Dis() {
		return amountVatCur2Dis;
	}

	/**
	 * @param amountVatCur2Dis the amountVatCur2Dis to set
	 */
	public void setAmountVatCur2Dis(Double amountVatCur2Dis) {
		this.amountVatCur2Dis = amountVatCur2Dis;
	}

	/**
	 * @return the billDate
	 */
	@Column(name = "bil_when", nullable = false)
	public Date getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	/**
	 * @return the dueDate
	 */
	@Column(name = "bil_due_date", nullable = true)
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the partnerName
	 */
	@Column(name = "bil_partner_name", nullable = false)
	public String getPartnerName() {
		return partnerName;
	}

	/**
	 * @param partnerName the partnerName to set
	 */
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	/**
	 * @return the partnerContactName
	 */
	@Column(name = "bil_partner_contact_name", nullable = true)
	public String getPartnerContactName() {
		return partnerContactName;
	}

	/**
	 * @param partnerContactName the partnerContactName to set
	 */
	public void setPartnerContactName(String partnerContactName) {
		this.partnerContactName = partnerContactName;
	}

	/**
	 * @return the partnerReference
	 */
	@Column(name = "bil_partner_reference", nullable = true)
	public String getPartnerReference() {
		return partnerReference;
	}

	/**
	 * @param partnerReference the partnerReference to set
	 */
	public void setPartnerReference(String partnerReference) {
		this.partnerReference = partnerReference;
	}

    
}
