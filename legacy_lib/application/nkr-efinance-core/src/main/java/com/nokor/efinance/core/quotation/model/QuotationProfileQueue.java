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

import com.nokor.frmk.security.model.SecProfile;

/**
 * @author ly.youhort
 *
 */
@Entity
@Table(name = "td_quotation_profile_queue")
public class QuotationProfileQueue extends EntityA {
	/**
	 */
	private static final long serialVersionUID = -5966202003805184534L;
	
	private SecProfile profile;
	private Quotation quotation;
	
	/**
	 * @return
	 */
	public static QuotationProfileQueue createInstance() {
		QuotationProfileQueue secUserBackup = EntityFactory.createInstance(QuotationProfileQueue.class);
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
	 * @return the profile
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_pro_id")
	public SecProfile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(SecProfile profile) {
		this.profile = profile;
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
