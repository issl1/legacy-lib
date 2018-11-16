package com.nokor.efinance.core.callcenter.model;

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

/**
 * @author youhort.ly
 */
@Entity
@Table(name = "td_call_center_history")
public class CallCenterHistory extends EntityA implements MCallCenterHistory {

	private static final long serialVersionUID = -1893813037868210744L;
		
	private Contract contract;	
	private ECallCenterResult result;
	private String comment;
	
	/**
     * @return
     */
    public static CallCenterHistory createInstance() {
    	CallCenterHistory colAction = EntityFactory.createInstance(CallCenterHistory.class);
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
	 * @return the result
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cal_ctr_res_id", nullable = true)
	public ECallCenterResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ECallCenterResult result) {
		this.result = result;
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
}
