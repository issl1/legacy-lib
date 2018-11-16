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
import javax.persistence.Transient;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_asset_arc")
public class AssetArc extends AbstractAsset {
    /** */
	private static final long serialVersionUID = -4227116574519407670L;

	private List<Quotation> quotations;
	private List<Contract> contracts;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ass_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	
	/**
	 * @return the quotations
	 */
	@OneToMany(mappedBy="assetArc", fetch = FetchType.LAZY)
	public List<Quotation> getQuotations() {
		return quotations;
	}


	/**
	 * @param quotations the quotations to set
	 */
	public void setQuotations(List<Quotation> quotations) {
		this.quotations = quotations;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Quotation getMainQuotation() {
		return quotations.get(0);
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void setMainQuotation(Quotation quotation) {
		this.quotations.add(quotation);
	}
	
	/**
	 * @return the contracts
	 */
	@OneToMany(mappedBy="assetArc", fetch = FetchType.LAZY)
	public List<Contract> getContracts() {
		return contracts;
	}


	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public Contract getMainContract() {
		return contracts.get(0);
	}
}
