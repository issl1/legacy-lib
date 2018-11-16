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
@Table(name = "td_collection_action")
public class CollectionAction extends EntityA implements MCollectionAction {

	/**
	 */
	private static final long serialVersionUID = 4863378713393021825L;
	
	private Contract contract;
	private EColAction colAction;
	private Date nextActionDate;
	private String userLogin;
	private String comment;
	
	/**
     * 
     * @return
     */
    public static CollectionAction createInstance() {
    	CollectionAction colAction = EntityFactory.createInstance(CollectionAction.class);
        return colAction;
    }

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cnt_col_act_id", unique = true, nullable = false)
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
	 * @return the colAction
	 */
	@Column(name = "col_act_id", nullable = true)
	@Convert(converter = EColAction.class)
	public EColAction getColAction() {
		return colAction;
	}

	/**
	 * @param colAction the colAction to set
	 */
	public void setColAction(EColAction colAction) {
		this.colAction = colAction;
	}
	
	/**
	 * @return the nextActionDate
	 */
	@Column(name = "cnt_col_act_dt_next_action", nullable = true)
	public Date getNextActionDate() {
		return nextActionDate;
	}

	/**
	 * @param nextActionDate the nextActionDate to set
	 */
	public void setNextActionDate(Date nextActionDate) {
		this.nextActionDate = nextActionDate;
	}	
	
	/**
	 * @return the userLogin
	 */
	@Column(name = "cnt_col_act_va_usr_login", nullable = true, length = 50)
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
	 * @return the comment
	 */
	@Column(name = "cnt_col_act_va_comment", nullable = true, length = 500)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
