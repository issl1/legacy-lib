package com.nokor.efinance.core.payment.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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

import org.seuksa.frmk.model.eref.BaseERefEntity;

import com.nokor.efinance.core.financial.model.FinService;

/**
 * Payment Method
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_payment_method")
@AttributeOverrides({
@AttributeOverride(name = "code", column = @Column(name = "pay_met_code")),
@AttributeOverride(name = "desc", column = @Column(name = "pay_met_desc")),
@AttributeOverride(name = "descEn", column = @Column(name = "pay_met_desc_en"))
})
public class EPaymentMethod extends BaseERefEntity implements AttributeConverter<EPaymentMethod, Long> {
	/** */
	private static final long serialVersionUID = -6646611602087148885L;
	
	public final static EPaymentMethod CASH = new EPaymentMethod("CASH", 1); // Cash 
	public final static EPaymentMethod LOSS = new EPaymentMethod("LOSS", 2); // Loss
	public final static EPaymentMethod CHEQUE = new EPaymentMethod("CHEQUE", 3); // Cheque
	public final static EPaymentMethod TRANSFER = new EPaymentMethod("TRANSFER", 4); // Transfer
	public final static EPaymentMethod UNKNOWN  = new EPaymentMethod("UNKNOWN", 5);  // Unknown

	private ECategoryPaymentMethod categoryPaymentMethod;
	private Boolean autoConfirm;
	private FinService service;

	/**
	 * 
	 */
	public EPaymentMethod() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EPaymentMethod(String code, long id) {
		super(code, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EPaymentMethod convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EPaymentMethod arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_met_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
//    /**
//	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getCode()
//	 */
//	@Column(name = "pay_met_code", nullable = false, length = 10)
//	@Override
//	public String getCode() {
//		return super.getCode();
//	}
//
//
//	/**
//	 * @see org.seuksa.frmk.mvc.model.entity.AuditEntityRef#getDesc()
//	 */
//	@Column(name = "pay_met_desc", nullable = true, length = 50)
//	@Override
//    public String getDesc() {
//        return super.getDesc();
//    }
//	
//	/**
//     * Get the asset financial product's name in English.
//     * 
//     * @return <String>
//     */
//    @Override
//    @Column(name = "pay_met_desc_en", nullable = false, length = 50)
//    public String getDescEn() {
//        return super.getDescEn();
//    }
	/**
	 * @return the categoryPaymentMethod
	 */
    @Column(name = "cat_pay_met_id", nullable = false)
    @Convert(converter = ECategoryPaymentMethod.class)
	public ECategoryPaymentMethod getCategoryPaymentMethod() {
		return categoryPaymentMethod;
	}

	/**
	 * @param categoryPaymentMethod the categoryPaymentMethod to set
	 */
	public void setCategoryPaymentMethod(ECategoryPaymentMethod categoryPaymentMethod) {
		this.categoryPaymentMethod = categoryPaymentMethod;
	}

	/**
	 * @return the autoConfirm
	 */
	@Column(name = "pay_met_auto_confirm", nullable = true)
	public Boolean isAutoConfirm() {
		return autoConfirm;
	}

	/**
	 * @param autoConfirm the autoConfirm to set
	 */
	public void setAutoConfirm(Boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
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
	 * 
	 * @return
	 */
	public static List<EPaymentMethod> values() {
		return getValues(EPaymentMethod.class);
	}
	
	/**
	 * @return
	 */
	public static List<EPaymentMethod> valuesForPaymentDealer() {
		List<EPaymentMethod> values = new ArrayList<>();
		values.add(getByCode(EPaymentMethod.class, CHEQUE.getCode()));
		values.add(getByCode(EPaymentMethod.class, TRANSFER.getCode()));
		return values;
	}
}