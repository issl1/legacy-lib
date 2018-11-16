package com.nokor.ersys.collab.membership.model;

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
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_document")
public class RegistrationDoc extends EntityA {
    /** */
	private static final long serialVersionUID = -2652892394834185459L;

    private Date validationDate;
    private Boolean neverExpire;
    private Date expirationDate;
    private Date receiptionDate;
    
    private MemberRegistration registration;
    private EMediaReceiption receiptionBy;
    
    /**
     * 
     * @return
     */
    public static RegistrationDoc createInstance() {
        RegistrationDoc doc = EntityFactory.createInstance(RegistrationDoc.class);
        doc.setNeverExpire(Boolean.TRUE);
        
        return doc;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
   
	/**
	 * 
	 */
    @Transient
	public boolean isValidated() {
		return validationDate != null;
	}
    
    /**
	 * @return the validationDate
	 */
    @Column(name = "doc_validation_date", nullable = true)
	public Date getValidationDate() {
		return validationDate;
	}

	/**
	 * @param validationDate the validationDate to set
	 */
	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}

	/**
	 * @return the neverExpire
	 */
    @Column(name = "doc_never_expire", nullable = true)
	public Boolean getNeverExpire() {
		return neverExpire;
	}

	/**
	 * @param neverExpire the neverExpire to set
	 */
	public void setNeverExpire(Boolean neverExpire) {
		this.neverExpire = neverExpire;
	}

	/**
	 * @return the expirationDate
	 */
    @Column(name = "doc_expiration_date", nullable = true)
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the receiptionDate
	 */
    @Column(name = "doc_receiption_date", nullable = true)
	public Date getReceiptionDate() {
		return receiptionDate;
	}

	/**
	 * @param receiptionDate the receiptionDate to set
	 */
	public void setReceiptionDate(Date receiptionDate) {
		this.receiptionDate = receiptionDate;
	}

	
	/**
	 * @return the registration
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_reg_id", nullable = false)
	public MemberRegistration getRegistration() {
		return registration;
	}

	/**
	 * @param registration the registration to set
	 */
	public void setRegistration(MemberRegistration registration) {
		this.registration = registration;
	}

	/**
	 * @return the receiptionBy
	 */
    @Column(name = "med_rec_id", nullable = true)
    @Convert(converter = EMediaReceiption.class)
	public EMediaReceiption getReceiptionBy() {
		return receiptionBy;
	}

	/**
	 * @param receiptionBy the receiptionBy to set
	 */
	public void setReceiptionBy(EMediaReceiption receiptionBy) {
		this.receiptionBy = receiptionBy;
	}

	
    
}
