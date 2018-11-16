package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class ContractFlagDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = 8438725753165580585L;
	
	private Long id;
	private Date createDate;
	private String createUser;
	private String contractNo;
	private RefDataDTO flag;
	private RefDataDTO location;	
	private Date date;
	private Date actionDate;
	private String comment;
	private String otherValue;
	private String locationDesc;
	private String courtInCharge;
	private boolean completed;
	private UriDTO policeStation;
	private UriDTO province;
	private UriDTO district;
	private UriDTO commune;
	private UriDTO branch;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/**
	 * @return the flag
	 */
	public RefDataDTO getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(RefDataDTO flag) {
		this.flag = flag;
	}

	/**
	 * @return the location
	 */
	public RefDataDTO getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(RefDataDTO location) {
		this.location = location;
	}

	/**
	 * @return the date
	 */
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
	public Date getActionDate() {
		return actionDate;
	}

	/**
	 * @param actionDate the actionDate to set
	 */
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	/**
	 * @return the comment
	 */
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
	 * @return the otherValue
	 */
	public String getOtherValue() {
		return otherValue;
	}

	/**
	 * @param otherValue the otherValue to set
	 */
	public void setOtherValue(String otherValue) {
		this.otherValue = otherValue;
	}

	/**
	 * @return the locationDesc
	 */
	public String getLocationDesc() {
		return locationDesc;
	}

	/**
	 * @param locationDesc the locationDesc to set
	 */
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	/**
	 * @return the courtInCharge
	 */
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
	 * @return the policeStation
	 */
	public UriDTO getPoliceStation() {
		return policeStation;
	}

	/**
	 * @param policeStation the policeStation to set
	 */
	public void setPoliceStation(UriDTO policeStation) {
		this.policeStation = policeStation;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
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
	public UriDTO getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(UriDTO province) {
		this.province = province;
	}

	/**
	 * @return the district
	 */
	public UriDTO getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(UriDTO district) {
		this.district = district;
	}

	/**
	 * @return the commune
	 */
	public UriDTO getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	public void setCommune(UriDTO commune) {
		this.commune = commune;
	}

	/**
	 * @return the branch
	 */
	public UriDTO getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(UriDTO branch) {
		this.branch = branch;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof ContractFlagDTO)) {
			 return false;
		 }
		 ContractFlagDTO contractFlagDTO = (ContractFlagDTO) arg0;
		 return getId() != null && getId().equals(contractFlagDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
