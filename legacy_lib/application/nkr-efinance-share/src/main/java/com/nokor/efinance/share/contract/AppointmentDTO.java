package com.nokor.efinance.share.contract;

import java.io.Serializable;
import java.util.Date;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;

/**
 * 
 * @author uhout.cheng
 */
public class AppointmentDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -6846743582316254513L;
	
	private Long id;
	private Date creationDate;
	private String contractNo;
	private RefDataDTO location;
	private RefDataDTO applicantType;	
	private UriDTO branch;	
	private Date startDate;
	private String remark;
	
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	 * @return the applicantType
	 */
	public RefDataDTO getApplicantType() {
		return applicantType;
	}

	/**
	 * @param applicantType the applicantType to set
	 */
	public void setApplicantType(RefDataDTO applicantType) {
		this.applicantType = applicantType;
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
	 * @return the startDate
	 */
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
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof AppointmentDTO)) {
			 return false;
		 }
		 AppointmentDTO appointmentDTO = (AppointmentDTO) arg0;
		 return getId() != null && getId().equals(appointmentDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
