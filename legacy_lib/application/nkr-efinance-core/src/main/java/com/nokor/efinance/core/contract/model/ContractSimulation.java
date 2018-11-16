package com.nokor.efinance.core.contract.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.aftersale.EAfterSaleEventType;
import com.nokor.efinance.core.applicant.model.Applicant;

@Entity
@Table(name = "td_contract_simulation")
public class ContractSimulation extends EntityWkf implements MContractSimulation {

	private static final long serialVersionUID = -1893813037868210744L;
		
	private Contract contract;
	private EAfterSaleEventType afterSaleEventType;
	private Date eventDate;
	private String externalReference;
	private Date applicationDate;
	private Date approvalDate;
	
	private Applicant applicant;
	private List<ContractSimulationApplicant> contractSimulationApplicants;
	
	/**
     * @return
     */
    public static ContractSimulation createInstance() {
    	ContractSimulation contractSimulation = EntityFactory.createInstance(ContractSimulation.class);
        return contractSimulation;
    }
    
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_sim_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the afterSaleEventType
	 */
    @Column(name = "aft_evt_typ_id", nullable = true)
	@Convert(converter = EAfterSaleEventType.class)
	public EAfterSaleEventType getAfterSaleEventType() {
		return afterSaleEventType;
	}

	/**
	 * @param afterSaleEventType the afterSaleEventType to set
	 */
	public void setAfterSaleEventType(EAfterSaleEventType afterSaleEventType) {
		this.afterSaleEventType = afterSaleEventType;
	}
	
	/**
	 * @return the applicant
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the eventDate
	 */
	@Column(name = "con_sim_event_date", nullable = true)
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	/**
	 * @return the externalReference
	 */
	@Column(name = "con_sim_va_external_reference", unique = true, nullable = true, length = 20)
	public String getExternalReference() {
		return externalReference;
	}

	/**
	 * @param externalReference the externalReference to set
	 */
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	/**
	 * @return the applicationDate
	 */
	@Column(name = "con_sim_application_date", nullable = true)
	public Date getApplicationDate() {
		return applicationDate;
	}

	/**
	 * @param applicationDate the applicationDate to set
	 */
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	/**
	 * @return the approvalDate
	 */
	@Column(name = "con_sim_approval_date", nullable = true)
	public Date getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate the approvalDate to set
	 */
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	/**
	 * @return the contractSimulationApplicants
	 */
	@OneToMany(mappedBy="contractSimulation", fetch = FetchType.LAZY)
	public List<ContractSimulationApplicant> getContractSimulationApplicants() {
		return contractSimulationApplicants;
	}

	/**
	 * @param contractSimulationApplicants the contractSimulationApplicants to set
	 */
	public void setContractSimulationApplicants(List<ContractSimulationApplicant> contractSimulationApplicants) {
		this.contractSimulationApplicants = contractSimulationApplicants;
	}
	
}
