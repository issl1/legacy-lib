package com.nokor.efinance.share.dealer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.efinance.share.applicant.ContactInfoDTO;
import com.nokor.efinance.share.payment.PaymentDetailDTO;

public class DealerDTO implements Serializable {

	/** */
	private static final long serialVersionUID = 7198386867919501133L;

	private Long id;
	private Long headerId;
	private String name;
	private String nameEn;
	private String code;
	private RefDataDTO dealerType;
	private DealerGroupDTO dealerGroup;
	private Date openingDate;
	private String commercialNo;
	private String description;
	private String managerFirstName;
	private String managerLastName;
	private String managerPhone;
	private String ownerFirstName;
	private String ownerLastName;
	private String ownerPhone;
	private String homePage;
	
	private Integer monthlyTargetSales;
	private Date registrationDate;
	private String registrationPlace;
	private Double registrationCost;
	
	private List<UriDTO> addresses;
	private List<ContactInfoDTO> contactInfos;
	private PaymentDetailDTO paymentDetail;
	private List<DealerAttributeDTO> attributes;
	
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
	 * @return the headerId
	 */
	public Long getHeaderId() {
		return headerId;
	}
	
	/**
	 * @param headerId the headerId to set
	 */
	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	/**
	 * @return the openingDate
	 */
	public Date getOpeningDate() {
		return openingDate;
	}
	
	/**
	 * @param openingDate the openingDate to set
	 */
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}
	
	/**
	 * @return the dealerType
	 */
	public RefDataDTO getDealerType() {
		return dealerType;
	}

	/**
	 * @param dealerType the dealerType to set
	 */
	public void setDealerType(RefDataDTO dealerType) {
		this.dealerType = dealerType;
	}	

	/**
	 * @return the dealerGroup
	 */
	public DealerGroupDTO getDealerGroup() {
		return dealerGroup;
	}

	/**
	 * @param dealerGroup the dealerGroup to set
	 */
	public void setDealerGroup(DealerGroupDTO dealerGroup) {
		this.dealerGroup = dealerGroup;
	}

	/**
	 * @return the commercialNo
	 */
	public String getCommercialNo() {
		return commercialNo;
	}
	
	/**
	 * @param commercialNo the commercialNo to set
	 */
	public void setCommercialNo(String commercialNo) {
		this.commercialNo = commercialNo;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}	

	/**
	 * @return the contactInfos
	 */
	public List<ContactInfoDTO> getContactInfos() {
		return contactInfos;
	}
	
	/**
	 * @param contactInfos the contactInfos to set
	 */
	public void setContactInfos(List<ContactInfoDTO> contactInfos) {
		this.contactInfos = contactInfos;
	}
	
	/**
	 * @return the paymentDetail
	 */
	public PaymentDetailDTO getPaymentDetail() {
		return paymentDetail;
	}

	/**
	 * @param paymentDetail the paymentDetail to set
	 */
	public void setPaymentDetail(PaymentDetailDTO paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	/**
	 * @return the addresses
	 */
	public List<UriDTO> getAddresses() {
		return addresses;
	}
	
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<UriDTO> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the managerFirstName
	 */
	public String getManagerFirstName() {
		return managerFirstName;
	}

	/**
	 * @param managerFirstName the managerFirstName to set
	 */
	public void setManagerFirstName(String managerFirstName) {
		this.managerFirstName = managerFirstName;
	}

	/**
	 * @return the managerLastName
	 */
	public String getManagerLastName() {
		return managerLastName;
	}

	/**
	 * @param managerLastName the managerLastName to set
	 */
	public void setManagerLastName(String managerLastName) {
		this.managerLastName = managerLastName;
	}

	/**
	 * @return the homePage
	 */
	public String getHomePage() {
		return homePage;
	}

	/**
	 * @param homePage the homePage to set
	 */
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}	

	/**
	 * @return the managerPhone
	 */
	public String getManagerPhone() {
		return managerPhone;
	}

	/**
	 * @param managerPhone the managerPhone to set
	 */
	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	/**
	 * @return the ownerFirstName
	 */
	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	/**
	 * @param ownerFirstName the ownerFirstName to set
	 */
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	/**
	 * @return the ownerLastName
	 */
	public String getOwnerLastName() {
		return ownerLastName;
	}

	/**
	 * @param ownerLastName the ownerLastName to set
	 */
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	/**
	 * @return the ownerPhone
	 */
	public String getOwnerPhone() {
		return ownerPhone;
	}

	/**
	 * @param ownerPhone the ownerPhone to set
	 */
	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}
	
	/**
	 * @return the monthlyTargetSales
	 */
	public Integer getMonthlyTargetSales() {
		return monthlyTargetSales;
	}

	/**
	 * @param monthlyTargetSales the monthlyTargetSales to set
	 */
	public void setMonthlyTargetSales(Integer monthlyTargetSales) {
		this.monthlyTargetSales = monthlyTargetSales;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the registrationPlace
	 */
	public String getRegistrationPlace() {
		return registrationPlace;
	}

	/**
	 * @param registrationPlace the registrationPlace to set
	 */
	public void setRegistrationPlace(String registrationPlace) {
		this.registrationPlace = registrationPlace;
	}
	
	/**
	 * @return the registrationCost
	 */
	public Double getRegistrationCost() {
		return registrationCost;
	}

	/**
	 * @param registrationCost the registrationCost to set
	 */
	public void setRegistrationCost(Double registrationCost) {
		this.registrationCost = registrationCost;
	}

	/**
	 * @return the attributes
	 */
	public List<DealerAttributeDTO> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<DealerAttributeDTO> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) {
			 return true;
		 }
		 if (arg0 == null || !(arg0 instanceof DealerDTO)) {
			 return false;
		 }
		 DealerDTO dealerDTO = (DealerDTO) arg0;
		 return getId() != null && getId().equals(dealerDTO.getId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() == null ? -1 : getId().intValue();
	}
}
