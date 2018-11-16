package com.nokor.efinance.core.collection.model;

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

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_collection_assist")
public class CollectionAssist extends EntityA implements MCollectionAssist {
	
	/**
	 */
	private static final long serialVersionUID = -5519788735598884231L;
	
	private Contract contract;
	private ERequestStatus requestStatus;
	private EColType colType;
	private String requestedUserLogin;
	private String approvedUserLogin;
	private String rejectedUserLogin;
	private String assistingUserLogin;
	
	private Date requestedDate;
	private Date approvedDate;
	private Date rejectedDate;
	
	private String reason;
	
	/**
     * @return
     */
    public static CollectionAssist createInstance() {
    	CollectionAssist colAssist = EntityFactory.createInstance(CollectionAssist.class);
        return colAssist;
    }

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cnt_col_ass_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	

	/**
	 * @return the contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the requestStatus
	 */
	@Column(name = "req_sta_id", nullable = true)
	@Convert(converter = ERequestStatus.class)
	public ERequestStatus getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(ERequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}	

	/**
	 * @return the colType
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}

	/**
	 * @return the requestedUserLogin
	 */
	@Column(name = "cnt_col_ass_requested_usr_login", nullable = true, length = 50)
	public String getRequestedUserLogin() {
		return requestedUserLogin;
	}

	/**
	 * @param requestedUserLogin the requestedUserLogin to set
	 */
	public void setRequestedUserLogin(String requestedUserLogin) {
		this.requestedUserLogin = requestedUserLogin;
	}

	/**
	 * @return the approvedUserLogin
	 */
	@Column(name = "cnt_col_ass_approved_usr_login", nullable = true, length = 50)
	public String getApprovedUserLogin() {
		return approvedUserLogin;
	}

	/**
	 * @param approvedUserLogin the approvedUserLogin to set
	 */
	public void setApprovedUserLogin(String approvedUserLogin) {
		this.approvedUserLogin = approvedUserLogin;
	}	
	
	/**
	 * @return the rejectedUserLogin
	 */
	@Column(name = "cnt_col_ass_rejected_usr_login", nullable = true, length = 50)
	public String getRejectedUserLogin() {
		return rejectedUserLogin;
	}

	/**
	 * @param rejectedUserLogin the rejectedUserLogin to set
	 */
	public void setRejectedUserLogin(String rejectedUserLogin) {
		this.rejectedUserLogin = rejectedUserLogin;
	}

	/**
	 * @return the assistingUserLogin
	 */
	@Column(name = "cnt_col_ass_assisting_usr_login", nullable = true, length = 50)
	public String getAssistingUserLogin() {
		return assistingUserLogin;
	}

	/**
	 * @param assistingUserLogin the assistingUserLogin to set
	 */
	public void setAssistingUserLogin(String assistingUserLogin) {
		this.assistingUserLogin = assistingUserLogin;
	}
	
	/**
	 * @return the requestedDate
	 */
	@Column(name = "cnt_col_ass_dt_requested", nullable = true)
	public Date getRequestedDate() {
		return requestedDate;
	}

	/**
	 * @param requestedDate the requestedDate to set
	 */
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	/**
	 * @return the approvedDate
	 */
	@Column(name = "cnt_col_ass_dt_approved", nullable = true)
	public Date getApprovedDate() {
		return approvedDate;
	}

	/**
	 * @param approvedDate the approvedDate to set
	 */
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	/**
	 * @return the rejectedDate
	 */
	@Column(name = "cnt_col_ass_dt_rejected", nullable = true)
	public Date getRejectedDate() {
		return rejectedDate;
	}

	/**
	 * @param rejectedDate the rejectedDate to set
	 */
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}

	/**
	 * @return the reason
	 */
	@Column(name = "cnt_col_ass_reason", nullable = true)
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
