package com.nokor.efinance.core.common.reference.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.ersys.core.hr.model.organization.BasePerson;

/**
 * BlackList item
 * @author ly.youhort
 */
@Entity
@Table(name = "tu_black_list_item")
public class BlackListItem extends BasePerson implements MBlackListItem {
	
	/** */
	private static final long serialVersionUID = 996585094407640595L;
	
	private EApplicantCategory applicantCategory;
	private EBlackListSource source;
	private EBlackListReason reason;
	
	private String details;
	private String remarks;
	private Long   blacklistOldId;
	
	/**
     * 
     * @return
     */
    public static BlackListItem createInstance() {
    	BlackListItem blackList = EntityFactory.createInstance(BlackListItem.class);
        return blackList;
    }

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bla_lst_ite_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    /**
   	 * @return the applicantCategory
   	 */
   	@Column(name = "app_cat_id", nullable = true)
    @Convert(converter = EApplicantCategory.class)
   	public EApplicantCategory getApplicantCategory() {
   		return applicantCategory;
   	}

   	/**
   	 * @param applicantCategory the applicantCategory to set
   	 */
   	public void setApplicantCategory(EApplicantCategory applicantCategory) {
   		this.applicantCategory = applicantCategory;
   	}

    /**
   	 * @return the source
   	 */
    @Column(name = "bla_lst_ite_sou_id", nullable = true)
    @Convert(converter = EBlackListSource.class)
   	public EBlackListSource getSource() {
   		return source;
   	}

   	/**
   	 * @param source the source to set
   	 */
   	public void setSource(EBlackListSource source) {
   		this.source = source;
   	}
    
    /**
	 * @return the reason
	 */
	@Column(name = "bla_lst_ite_rea_id", nullable = true)
    @Convert(converter = EBlackListReason.class)
	public EBlackListReason getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(EBlackListReason reason) {
		this.reason = reason;
	}	
	
	/**
	 * Can be overridden to false to disable the Work flow
	 * @return
	 */
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}

	/**
	 * @return the details
	 */
	 @Column(name = "bla_lst_ite_details", nullable = true, length = 255)
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the remarks
	 */
	 @Column(name = "bla_lst_ite_remarks", nullable = true, length = 255)
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the blacklistOldId
	 */
	@Column(name = "blacklist_old_id", nullable = true)
	public Long getBlacklistOldId() {
		return blacklistOldId;
	}

	/**
	 * @param blacklistOldId the blacklistOldId to set
	 */
	public void setBlacklistOldId(Long blacklistOldId) {
		this.blacklistOldId = blacklistOldId;
	}
}
