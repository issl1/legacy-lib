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
@Table(name = "tu_campaign_document")
public class CampaignDocument extends EntityA {
			
	/**
	 */
	private static final long serialVersionUID = 6118641155558901423L;
	
	private Long documentId;
	private Campaign campaign;
	
	/**
     * @return id
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cam_doc_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }			

	/**
	 * @return the documentId
	 */
    @Column(name = "doc_id", nullable = false)
	public Long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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
