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
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * @author ly.youhort
 */
@Entity
@Table(name = "td_appointment")
public class Appointment extends EntityA {
	
	/**
	 */
	private static final long serialVersionUID = -2293930151925368636L;	
	private Contract contract;
	private ELocation location;
	private EApplicantType between1; // between applicant type
	private OrgStructure between2; // and
	private Date startDate;
	private String remark;
	
	/**
     * 
     * @return
     */
    public static Appointment createInstance() {
    	Appointment instance = EntityFactory.createInstance(Appointment.class);
        return instance;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apo_id", unique = true, nullable = false)
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
	 * @return the location
	 */
	@Column(name = "loc_id", nullable = false)
    @Convert(converter = ELocation.class)
	public ELocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(ELocation location) {
		this.location = location;
	}
	
	/**
	 * @return the between1
	 */
	@Column(name = "app_typ_id_between_1", nullable = false)
	@Convert(converter = EApplicantType.class)
	public EApplicantType getBetween1() {
		return between1;
	}

	/**
	 * @param between1 the between1 to set
	 */
	public void setBetween1(EApplicantType between1) {
		this.between1 = between1;
	}

	/**
	 * @return the between2
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_str_id_between_2")
	public OrgStructure getBetween2() {
		return between2;
	}

	/**
	 * @param between2 the between2 to set
	 */
	public void setBetween2(OrgStructure between2) {
		this.between2 = between2;
	}
	
	/**
	 * @return the startDate
	 */
	@Column(name = "apo_dt_start", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the remark
	 */
	@Column(name = "apo_va_remark", nullable = true, length = 1000)
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}	
}
