package com.nokor.efinance.core.quotation.model;

import javax.persistence.Column;
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

import com.nokor.frmk.security.model.SecUser;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_quotation_user_queue")
public class QuotationSecUserQueue extends EntityA {

	private static final long serialVersionUID = 59892883030537102L;

	private SecUser secUser;
	private Quotation quotation;
	
	/**
	 * @return
	 */
	public static QuotationSecUserQueue createInstance() {
		QuotationSecUserQueue secUserBackup = EntityFactory.createInstance(QuotationSecUserQueue.class);
        return secUserBackup;
    }
	
	/**
     * @return the id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quo_usr_que_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the secUser
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id")
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
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
	
}
