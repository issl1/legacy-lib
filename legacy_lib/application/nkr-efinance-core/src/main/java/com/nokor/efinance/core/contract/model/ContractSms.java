package com.nokor.efinance.core.contract.model;

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

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_contract_sms")
public class ContractSms extends EntityA implements MContractSms {

	/**
	 */
	private static final long serialVersionUID = -8345302237539947978L;
	
	private Contract contract;
	private int type;
	private String sendTo;
	private String phoneNumber;
	private String message;
	private String userLogin;
	
	/**
     * 
     * @return
     */
    public static ContractSms createInstance() {
    	ContractSms instance = EntityFactory.createInstance(ContractSms.class);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_sms_id", unique = true, nullable = false)
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
	 * @return the type
	 */
	@Column(name = "con_sms_type", nullable = true)
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the sendTo
	 */
	@Column(name = "con_sms_send_to", nullable = true, length = 100)
	public String getSendTo() {
		return sendTo;
	}

	/**
	 * @param sendTo the sendTo to set
	 */
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	/**
	 * @return the phoneNumber
	 */
	@Column(name = "con_sms_phone_number", nullable = true, length = 50)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the message
	 */
	@Column(name = "con_sms_message", nullable = true, length = 255)
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the userLogin
	 */
	@Column(name = "con_sms_va_usr_login", nullable = true, length = 50)
	public String getUserLogin() {
		return userLogin;
	}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}	

}
