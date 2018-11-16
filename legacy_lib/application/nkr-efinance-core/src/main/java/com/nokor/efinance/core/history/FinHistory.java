package com.nokor.efinance.core.history;

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

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_fin_history")
public class FinHistory extends EntityA implements MFinHistory {

	/** */
	private static final long serialVersionUID = 807419783431125818L;
	
	private Contract contract;
	private Individual individual;
	private FinHistoryType type;
	private String comment;
	
	/**
     * 
     * @return
     */
    public static FinHistory createInstance() {
    	FinHistory his = EntityFactory.createInstance(FinHistory.class);
        return his;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fin_his_id", unique = true, nullable = false)
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
	 * @return the individual
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ind_id")
	public Individual getIndividual() {
		return individual;
	}

	/**
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	/**
	 * @return the type
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_his_typ_id")
	public FinHistoryType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(FinHistoryType type) {
		this.type = type;
	}    

	/**
     * @return the comment
     */
    @Column(name = "fin_his_comment", nullable = true, length = 255)
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }
}