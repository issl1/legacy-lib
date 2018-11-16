package com.nokor.efinance.core.collection.model;

import java.util.Date;

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

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_reminder")
public class Reminder extends EntityA implements MReminder {
	
	/**
	 */
	private static final long serialVersionUID = -6980118861317608631L;

	private Contract contract;
	private String comment;
	private Date date;
	private boolean dismiss;
	private SecUser secUser;
	
	/**
     * 
     * @return
     */
    public static Reminder createInstance() {
    	Reminder instance = EntityFactory.createInstance(Reminder.class);
        return instance;
    }
	
    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rem_id", unique = true, nullable = false)
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
	 * @return the comment
	 */
	@Column(name = "rem_comment", nullable = true)
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
	 * @return the date
	 */
	@Column(name = "rem_date", nullable = true)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the dismiss
	 */
	@Column(name = "rem_bl_dismiss", nullable = true, columnDefinition = "boolean default false")
	public boolean isDismiss() {
		return dismiss;
	}

	/**
	 * @param dismiss the dismiss to set
	 */
	public void setDismiss(boolean dismiss) {
		this.dismiss = dismiss;
	}

	/**
	 * @return the secUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sec_usr_id")
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
}
