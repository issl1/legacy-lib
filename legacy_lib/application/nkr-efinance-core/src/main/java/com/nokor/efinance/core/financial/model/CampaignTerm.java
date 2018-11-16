package com.nokor.efinance.core.financial.model;

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
@Table(name = "tu_campaign_term")
public class CampaignTerm extends EntityA implements MCampaignTerm {
			
	/**
	 */
	private static final long serialVersionUID = 6674903920230749522L;

	private Term term;
	private Campaign campaign;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_ter_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the term
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ter_id")
	public Term getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Term term) {
		this.term = term;
	} 
	
	/**
	 * @return the campaign
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cam_id")
	public Campaign getCampaign() {
		return campaign;
	}

	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

}
