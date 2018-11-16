package com.nokor.efinance.core.quotation.model;

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

import com.nokor.common.app.workflow.model.EWkfStatus;


/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_quotation_support_decision")
public class QuotationSupportDecision extends EntityA {

	private static final long serialVersionUID = -6559919455976817015L;
	
	private Quotation quotation;
	private SupportDecision supportDecision;
	private EWkfStatus quotationStatus;
	private boolean processed;
	
	/**
     * Get quotation applicant's is.
     * @return The quotation applicant's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_sup_dec_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
	 * @return the quotation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quo_id")
	public Quotation getQuotation() {
		return quotation;
	}

	/**
	 * @param quotation the quotation to set
	 */
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
    
	/**
	 * @return the supportDecision
	 */    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sup_dec_id")
	public SupportDecision getSupportDecision() {
		return supportDecision;
	}

	/**
	 * @param supportDecision the supportDecision to set
	 */
	public void setSupportDecision(SupportDecision supportDecision) {
		this.supportDecision = supportDecision;
	}

	/**
	 * @return the quotationStatus
	 */
    @Column(name = "quo_sta_id", nullable = false)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getWkfStatus() {
		return quotationStatus;
	}

	/**
	 * @param quotationStatus the quotationStatus to set
	 */
	public void setWkfStatus(EWkfStatus quotationStatus) {
		this.quotationStatus = quotationStatus;
	}

	/**
	 * @return the processed
	 */
	@Column(name = "quo_sup_dec_bl_processed", nullable = false)
	public boolean isProcessed() {
		return processed;
	}

	/**
	 * @param processed the processed to set
	 */
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
}
