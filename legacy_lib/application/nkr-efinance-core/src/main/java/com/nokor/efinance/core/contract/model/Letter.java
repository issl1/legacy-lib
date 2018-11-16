package com.nokor.efinance.core.contract.model;

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

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.common.reference.model.ELetterTemplate;
import com.nokor.efinance.core.workflow.LetterWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_letter")
public class Letter extends EntityWkf {
	
	/**
	 */
	private static final long serialVersionUID = -2733538254098360086L;
	
	private Contract contract;
	private EApplicantType sendTo;
	private ELetterTemplate letterTemplate;
	private Address sendAddress;
	private String message;
	private Date sendDate;
	private String userLogin;
	
	/**
	 * 
	 * @return
	 */
	public static Letter createInstance() {
		Letter instance = EntityFactory.createInstance(Letter.class);
		instance.setWkfStatus(LetterWkfStatus.LENEW);
		return instance;
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "let_id", unique = true, nullable = false)
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
	 * @return the sendTo
	 */
	@Column(name = "app_typ_id", nullable = true)
	@Convert(converter = EApplicantType.class)
	public EApplicantType getSendTo() {
		return sendTo;
	}

	/**
	 * @param sendTo the sendTo to set
	 */
	public void setSendTo(EApplicantType sendTo) {
		this.sendTo = sendTo;
	}

	/**
	 * @return the sendAddress
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_id")
	public Address getSendAddress() {
		return sendAddress;
	}

	/**
	 * @param sendAddress the sendAddress to set
	 */
	public void setSendAddress(Address sendAddress) {
		this.sendAddress = sendAddress;
	}

	/**
	 * @return the letterTemplate
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "let_tep_id")
	public ELetterTemplate getLetterTemplate() {
		return letterTemplate;
	}

	/**
	 * @param letterTemplate the letterTemplate to set
	 */
	public void setLetterTemplate(ELetterTemplate letterTemplate) {
		this.letterTemplate = letterTemplate;
	}

	/**
	 * @return the message
	 */
	@Column(name = "let_message", nullable = true, length = 255)
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
	@Column(name = "let_va_usr_login", nullable = true, length = 50)
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
	 * @return the sendDate
	 */
	@Column(name = "let_send_date", nullable = true)
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
}
