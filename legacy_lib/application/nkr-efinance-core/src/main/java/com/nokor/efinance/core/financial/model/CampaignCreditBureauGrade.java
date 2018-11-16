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
@Table(name = "tu_campaign_credit_bureau_grade")
public class CampaignCreditBureauGrade extends EntityA {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
		
	private Campaign campaign;
	private String grade;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_crb_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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

	/**
	 * @return the grade
	 */
	@Column(name = "cam_crb_grade", nullable = true)
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}		
}
