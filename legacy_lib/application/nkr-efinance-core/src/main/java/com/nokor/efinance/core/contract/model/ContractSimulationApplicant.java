package com.nokor.efinance.core.contract.model;

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

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_contract_simulation_applicant")
public class ContractSimulationApplicant extends EntityA implements MContractSimulationApplicant {
	
	private static final long serialVersionUID = 8253696265755380685L;
	
	private Applicant applicant;
	private ContractSimulation contractSimulation;
	private EApplicantType applicantType;
	
	/**
	 * 
	 * @param applicantType
	 * @return
	 */
    public static ContractSimulationApplicant createInstance(EApplicantType applicantType) {
    	ContractSimulationApplicant instance = EntityFactory.createInstance(ContractSimulationApplicant.class);
    	instance.setApplicantType(applicantType);
        return instance;
    }
	
	/**
     * Get quotation applicant's is.
     * @return The quotation applicant's is.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_app_id", unique = true, nullable = false)
    public Long getId() {
        return id;
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
	 * @return the contractSimulation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "con_sim_id")
	public ContractSimulation getContractSimulation() {
		return contractSimulation;
	}

	/**
	 * @param contractSimulation the contractSimulation to set
	 */
	public void setContractSimulation(ContractSimulation contractSimulation) {
		this.contractSimulation = contractSimulation;
	}

	/**
	 * @return the applicantType
	 */
    @Column(name = "app_typ_id", nullable = false)
    @Convert(converter = EApplicantType.class)
	public EApplicantType getApplicantType() {
		return applicantType;
	}

	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(EApplicantType applicantType) {
		this.applicantType = applicantType;
	}	
}
