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
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.applicant.model.EApplicantType;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_email")
public class Email extends EntityA {
	
	/**
	 */
	private static final long serialVersionUID = -2733538254098360086L;
	
	private Contract contract;
	private EApplicantType sendTo;
	private String sendEmail;
	private String message;
	private Date sendDate;
	private String userLogin;
	private String subject;
	
	/**
     * 
     * @return
     */
    public static Email createInstance() {
    	Email instance = EntityFactory.createInstance(Email.class);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mal_id", unique = true, nullable = false)
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
	 * @return the sendEmail
	 */
	@Column(name = "mal_send_email", nullable = true, length = 2000)
	public String getSendEmail() {
		return sendEmail;
	}

	/**
	 * @param sendEmail the sendEmail to set
	 */
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	/**
	 * @return the message
	 */
	@Column(name = "mal_message", nullable = true, length = 2000)
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
	@Column(name = "mal_va_usr_login", nullable = true, length = 50)
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
	@Column(name = "mal_send_date", nullable = true)
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the subject
	 */
	@Column(name = "mal_send_subject", nullable = true)
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
