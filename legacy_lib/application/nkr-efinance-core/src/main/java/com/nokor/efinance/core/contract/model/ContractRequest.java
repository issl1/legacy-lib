package com.nokor.efinance.core.contract.model;

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

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_contract_request")
public class ContractRequest extends EntityA implements MContractRequest {

	/**
	 */
	private static final long serialVersionUID = 8267082659757489425L;
	private Contract contract;
	private ERequestType requestType;
	private String othersValue;
	private String comment;
	private String userLogin;
	private boolean processed;
	
	/**
     * 
     * @return
     */
    public static ContractRequest createInstance() {
    	ContractRequest instance = EntityFactory.createInstance(ContractRequest.class);
        return instance;
    }
	
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_req_id", unique = true, nullable = false)
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
	 * @return the requestType
	 */
	@Column(name = "req_typ_id", nullable = true)
    @Convert(converter = ERequestType.class)
	public ERequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(ERequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the othersValue
	 */
	@Column(name = "con_req_va_others_value", nullable = true, length = 100)
	public String getOthersValue() {
		return othersValue;
	}

	/**
	 * @param othersValue the othersValue to set
	 */
	public void setOthersValue(String othersValue) {
		this.othersValue = othersValue;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "con_req_va_comment", nullable = true, length = 500)
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
	 * @return the userLogin
	 */
	@Column(name = "con_rep_va_usr_login", nullable = true, length = 50)
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}	

	/**
	 * @return the processed
	 */
	@Column(name = "con_req_bl_processed", nullable = true)
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
