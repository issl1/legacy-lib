package com.nokor.efinance.core.collection.model;

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

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_collection_history")
public class CollectionHistory extends EntityA implements MCollectionHistory {

	private static final long serialVersionUID = -1893813037868210744L;
		
	private Contract contract;
	private Address address;
	
	private EColType origin;
	private EApplicantType contactedPerson;
	private EContactPerson reachedPerson;
	private ECallType callType;
	private String other;
	private String otherContact;
	
	private boolean callIn;
	
	private ETypeContactInfo contactedTypeInfo;
	private String contactedInfoValue;
	
	private EColSubject subject;
	private EColResult result;
	private String comment;
	private String cmResult;
	
	/**
     * 
     * @return
     */
    public static CollectionHistory createInstance() {
    	CollectionHistory colAction = EntityFactory.createInstance(CollectionHistory.class);
        return colAction;
    }
		
	/**
	 * @return id
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_his_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
        
	/**
	 * @return the contract
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_id", nullable = false)
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
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id", nullable = true)
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	/**
	 * @return the origin
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(EColType origin) {
		this.origin = origin;
	}

	/**
	 * @return the contactedPerson
	 */
	@Column(name = "app_typ_id", nullable = true)
	@Convert(converter = EApplicantType.class)
	public EApplicantType getContactedPerson() {
		return contactedPerson;
	}

	/**
	 * @param contactedPerson the contactedPerson to set
	 */
	public void setContactedPerson(EApplicantType contactedPerson) {
		this.contactedPerson = contactedPerson;
	}
	
	/**
	 * @return the reachedPerson
	 */
	@Column(name = "con_per_id", nullable = true)
	@Convert(converter = EContactPerson.class)
	public EContactPerson getReachedPerson() {
		return reachedPerson;
	}

	/**
	 * @param reachedPerson the reachedPerson to set
	 */
	public void setReachedPerson(EContactPerson reachedPerson) {
		this.reachedPerson = reachedPerson;
	}

	/**
	 * @return the callType
	 */
	@Column(name = "cal_typ_id", nullable = true)
	@Convert(converter = ECallType.class)
	public ECallType getCallType() {
		return callType;
	}

	/**
	 * @param callType the callType to set
	 */
	public void setCallType(ECallType callType) {
		this.callType = callType;
	}

	/**
	 * @return the contactedTypeInfo
	 */
	@Column(name = "typ_cnt_inf_id", nullable = true)
    @Convert(converter = ETypeContactInfo.class)
	public ETypeContactInfo getContactedTypeInfo() {
		return contactedTypeInfo;
	}

	/**
	 * @param contactedTypeInfo the contactedTypeInfo to set
	 */
	public void setContactedTypeInfo(ETypeContactInfo contactedTypeInfo) {
		this.contactedTypeInfo = contactedTypeInfo;
	}
	
	/**
	 * @return the subject
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_suj_id", nullable = true)
	public EColSubject getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(EColSubject subject) {
		this.subject = subject;
	}

	/**
	 * @return the result
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "col_res_id", nullable = true)
	public EColResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(EColResult result) {
		this.result = result;
	}	
	
	/**
	 * @return the contactedInfoValue
	 */
	@Column(name = "col_his_va_contacted_info_value", nullable = true, length = 30)
	public String getContactedInfoValue() {
		return contactedInfoValue;
	}

	/**
	 * @param contactedInfoValue the contactedInfoValue to set
	 */
	public void setContactedInfoValue(String contactedInfoValue) {
		this.contactedInfoValue = contactedInfoValue;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "col_his_va_comment", nullable = true, length = 500)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the other
	 */
	@Column(name = "col_his_va_other", nullable = true, length = 500)
	public String getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the otherContact
	 */
	@Column(name = "col_his_va_other_phone", nullable = true, length = 500)
	public String getOtherContact() {
		return otherContact;
	}

	/**
	 * @param otherContact the otherContact to set
	 */
	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	/**
	 * @return the callIn
	 */
	@Column(name = "col_bl_call_in", nullable = true, columnDefinition = "boolean default false")
	public boolean isCallIn() {
		return callIn;
	}

	/**
	 * @param callIn the callIn to set
	 */
	public void setCallIn(boolean callIn) {
		this.callIn = callIn;
	}

	/**
	 * @return the cmResult
	 */
	@Column(name = "col_his_cm_result", nullable = true)
	public String getCmResult() {
		return cmResult;
	}

	/**
	 * @param cmResult the cmResult to set
	 */
	public void setCmResult(String cmResult) {
		this.cmResult = cmResult;
	}
	
	
}
