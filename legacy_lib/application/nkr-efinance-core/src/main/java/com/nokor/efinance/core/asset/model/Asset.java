package com.nokor.efinance.core.asset.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nokor.efinance.core.contract.model.Contract;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_asset")
public class Asset extends AbstractAsset {
	private static final long serialVersionUID = -2870696410691091691L;

	private List<Contract> contracts;
	
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the contracts
	 */
    @OneToMany(mappedBy="asset", fetch = FetchType.LAZY)
	public List<Contract> getContracts() {
		return contracts;
	}

	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}
    
}
