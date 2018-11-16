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

import com.nokor.efinance.core.contract.model.Contract;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_contract_operation")
public class ContractOperation extends EntityA implements MContractOperation {
	/**
	 */
	private static final long serialVersionUID = 2830297649359189868L;
	
	private EOperationType operationType;
	private Contract contract;
	private double tiPrice;	

	/**
     * 
     * @return
     */
    public static ContractOperation createInstance() {
    	ContractOperation instance = EntityFactory.createInstance(ContractOperation.class);
        return instance;
    }
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_ope_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}		

	/**
	 * @return the operationType
	 */
	@Column(name = "ope_typ_id", nullable = true)
	@Convert(converter = EOperationType.class)
	public EOperationType getOperationType() {
		return operationType;
	}
	
	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(EOperationType operationType) {
		this.operationType = operationType;
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
	 * @return the tiPrice
	 */
	@Column(name = "con_ope_ti_price", nullable = true)
	public double getTiPrice() {
		return tiPrice;
	}

	/**
	 * @param tiPrice the tiPrice to set
	 */
	public void setTiPrice(double tiPrice) {
		this.tiPrice = tiPrice;
	}	
}
