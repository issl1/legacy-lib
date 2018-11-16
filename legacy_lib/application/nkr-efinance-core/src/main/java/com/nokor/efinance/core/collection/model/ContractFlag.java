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

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "td_contract_flag")
public class ContractFlag extends EntityWkf implements MContractFlag {

	/** */
	private static final long serialVersionUID = 6326951008621768160L;

	private EFlag flag;
	private Contract contract;
	
	private ELocation location;
	private String otherLocationValue;
	private String comment;
	private Date date;
	
	private Date actionDate;
	private boolean completed;
	
	private Province province;
	private District district;
	private Commune commune;
	private String locantion;
	private PoliceStation policeStation;
	private String courtInCharge;
	
	private OrgStructure branch;
	private Dealer dealer;
	private Address address;
	private String resultRemark;
	
	/**
     * 
     * @return
     */
    public static ContractFlag createInstance() {
    	ContractFlag instance = EntityFactory.createInstance(ContractFlag.class);
        return instance;
    }

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_fla_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}	

	/**
	 * @return the flag
	 */
	@Column(name = "fla_id", nullable = true)
	@Convert(converter = EFlag.class)
	public EFlag getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(EFlag flag) {
		this.flag = flag;
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
	@Column(name = "loc_id", nullable = true)
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
	 * @return the otherLocationValue
	 */
	@Column(name = "con_fla_other_location_value", nullable = true)
	public String getOtherLocationValue() {
		return otherLocationValue;
	}

	/**
	 * @param otherLocationValue the otherLocationValue to set
	 */
	public void setOtherLocationValue(String otherLocationValue) {
		this.otherLocationValue = otherLocationValue;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "con_fla_comment", nullable = true)
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
	@Column(name = "con_fla_date", nullable = true)
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
	 * @return the actionDate
	 */
	@Column(name = "con_fla_act_date", nullable = true)
	public Date getActionDate() {
		return actionDate;
	}

	/**
	 * @return the completed
	 */
	@Column(name = "con_fla_act_bl_completed", nullable = true, columnDefinition = "boolean default false")
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param actionDate the actionDate to set
	 */
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the province
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_id", nullable = true)
	public Province getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(Province province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dis_id", nullable = true)
	public District getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(District district) {
		this.district = district;
	}

	/**
	 * @return the commune
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_id", nullable = true)
	public Commune getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	public void setCommune(Commune commune) {
		this.commune = commune;
	}

	/**
	 * @return the locantion
	 */
	@Column(name = "con_fla_location", nullable = true)
	public String getLocantion() {
		return locantion;
	}

	/**
	 * @param locantion the locantion to set
	 */
	public void setLocantion(String locantion) {
		this.locantion = locantion;
	}

	/**
	 * @return the policeStation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pol_sta_id", nullable = true)
	public PoliceStation getPoliceStation() {
		return policeStation;
	}

	/**
	 * @param policeStation the policeStation to set
	 */
	public void setPoliceStation(PoliceStation policeStation) {
		this.policeStation = policeStation;
	}

	/**
	 * @return the courtInCharge
	 */
	@Column(name = "con_fla_court_in_charge", nullable = true)
	public String getCourtInCharge() {
		return courtInCharge;
	}

	/**
	 * @param courtInCharge the courtInCharge to set
	 */
	public void setCourtInCharge(String courtInCharge) {
		this.courtInCharge = courtInCharge;
	}

	/**
	 * @return the branch
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = true)
	public OrgStructure getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(OrgStructure branch) {
		this.branch = branch;
	}

	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dea_id", nullable = true)
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the address
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "add_id", nullable = true)
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the resultRemark
	 */
	@Column(name = "con_fla_result_remark", nullable = true)
	public String getResultRemark() {
		return resultRemark;
	}

	/**
	 * @param resultRemark the resultRemark to set
	 */
	public void setResultRemark(String resultRemark) {
		this.resultRemark = resultRemark;
	}

}
