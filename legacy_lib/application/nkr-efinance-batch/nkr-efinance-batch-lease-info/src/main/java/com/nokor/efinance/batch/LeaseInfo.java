package com.nokor.efinance.batch;

import java.io.Serializable;
import java.util.Date;

public class LeaseInfo implements Serializable {
	private static final long serialVersionUID = 109603725918956450L;

	private Long id;
	private String fullName;
	private String poNo;
	private String lidNo;
	private String businessType;
	private String tel;
	private String village;
	private String commune;
	private String district;
	private String province;
	private String villageKh;
	private String communeKh;
	private String districtKh;
	private String provinceKh;
	private String houseNo;
	private String street;
	private String sex;
	private String dealerName;
	private String assetModel;
	private String coName;
	private Date dateOfContract;
	private Double assetPrice;
	private Integer term;
	private Double rate;
	private Double irrYear;
	private Double irrMonth;
	private Double advPaymentPer;
	private Double insurance;
	private Double registration;
	private Double serviceFee;
	private Double advPayment;
	private Double secondPay;
	private Double downPay;
	private Double loanAmount;
	private Double totalInt;
	private Double intBalance;
	private Double realIntBalance;
	private Double prinBalance;
	private Double realPrinBalance;
	private Double totalReceive;
	private Double realTotalReceive;
	private Double installmentAmount;
	private String poNumber;
	private Date firstInstallmentDate;

	/**
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return
	 */
	public String getPoNo() {
		return poNo;
	}
	
	/**
	 * @param poNo
	 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
	/**
	 * @return
	 */
	public String getLidNo() {
		return lidNo;
	}
	
	/**
	 * @param lidNo
	 */
	public void setLidNo(String lidNo) {
		this.lidNo = lidNo;
	}
	
	/**
	 * @return
	 */
	public String getBusinessType() {
		return businessType;
	}
	/**
	 * @param businessType
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	/**
	 * @return
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	/**
	 * @return the villageKh
	 */
	public String getVillageKh() {
		return villageKh;
	}

	/**
	 * @param villageKh the villageKh to set
	 */
	public void setVillageKh(String villageKh) {
		this.villageKh = villageKh;
	}

	/**
	 * @return the communeKh
	 */
	public String getCommuneKh() {
		return communeKh;
	}

	/**
	 * @param communeKh the communeKh to set
	 */
	public void setCommuneKh(String communeKh) {
		this.communeKh = communeKh;
	}

	/**
	 * @return the districtKh
	 */
	public String getDistrictKh() {
		return districtKh;
	}

	/**
	 * @param districtKh the districtKh to set
	 */
	public void setDistrictKh(String districtKh) {
		this.districtKh = districtKh;
	}

	/**
	 * @return the provinceKh
	 */
	public String getProvinceKh() {
		return provinceKh;
	}

	/**
	 * @param provinceKh the provinceKh to set
	 */
	public void setProvinceKh(String provinceKh) {
		this.provinceKh = provinceKh;
	}

	/**
	 * @return the houseNo
	 */
	public String getHouseNo() {
		return houseNo;
	}

	/**
	 * @param houseNo the houseNo to set
	 */
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	/**
	 * @return
	 */
	public String getVillage() {
		return village;
	}
	/**
	 * @param village
	 */
	public void setVillage(String village) {
		this.village = village;
	}
	/**
	 * @return
	 */
	public String getCommune() {
		return commune;
	}
	/**
	 * @param commune
	 */
	public void setCommune(String commune) {
		this.commune = commune;
	}
	/**
	 * @return
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return
	 */
	public String getDealerName() {
		return dealerName;
	}
	/**
	 * @param dealerName
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	/**
	 * @return
	 */
	public String getAssetModel() {
		return assetModel;
	}
	/**
	 * @param assetModel
	 */
	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
	}
	/**
	 * @return
	 */
	public String getCoName() {
		return coName;
	}
	/**
	 * @param coName
	 */
	public void setCoName(String coName) {
		this.coName = coName;
	}
	/**
	 * @return
	 */
	public Date getDateOfContract() {
		return dateOfContract;
	}
	/**
	 * @param dateOfContract
	 */
	public void setDateOfContract(Date dateOfContract) {
		this.dateOfContract = dateOfContract;
	}
	/**
	 * @return
	 */
	public Double getAssetPrice() {
		return assetPrice;
	}
	/**
	 * @param assetPrice
	 */
	public void setAssetPrice(Double assetPrice) {
		this.assetPrice = assetPrice;
	}
	/**
	 * @return
	 */
	public Integer getTerm() {
		return term;
	}
	/**
	 * @param term
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}
	/**
	 * @return
	 */
	public Double getRate() {
		return rate;
	}
	/**
	 * @param rate
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	/**
	 * @return
	 */
	public Double getIrrYear() {
		return irrYear;
	}
	/**
	 * @param irrYear
	 */
	public void setIrrYear(Double irrYear) {
		this.irrYear = irrYear;
	}
	/**
	 * @return
	 */
	public Double getIrrMonth() {
		return irrMonth;
	}
	/**
	 * @param irrMonth
	 */
	public void setIrrMonth(Double irrMonth) {
		this.irrMonth = irrMonth;
	}
	/**
	 * @return
	 */
	public Double getAdvPaymentPer() {
		return advPaymentPer;
	}
	/**
	 * @param advPaymentPer
	 */
	public void setAdvPaymentPer(Double advPaymentPer) {
		this.advPaymentPer = advPaymentPer;
	}
	/**
	 * @return
	 */
	public Double getInsurance() {
		return insurance;
	}
	/**
	 * @param insurance
	 */
	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}
	/**
	 * @return
	 */
	public Double getRegistration() {
		return registration;
	}
	/**
	 * @param registration
	 */
	public void setRegistration(Double registration) {
		this.registration = registration;
	}
	/**
	 * @return
	 */
	public Double getServiceFee() {
		return serviceFee;
	}
	/**
	 * @param serviceFee
	 */
	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}
	/**
	 * @return
	 */
	public Double getAdvPayment() {
		return advPayment;
	}
	/**
	 * @param advPayment
	 */
	public void setAdvPayment(Double advPayment) {
		this.advPayment = advPayment;
	}
	/**
	 * @return
	 */
	public Double getSecondPay() {
		return secondPay;
	}
	/**
	 * @param secondPay
	 */
	public void setSecondPay(Double secondPay) {
		this.secondPay = secondPay;
	}
	/**
	 * @return
	 */
	public Double getDownPay() {
		return downPay;
	}
	/**
	 * @param downPay
	 */
	public void setDownPay(Double downPay) {
		this.downPay = downPay;
	}
	/**
	 * @return
	 */
	public Double getLoanAmount() {
		return loanAmount;
	}
	/**
	 * @param loanAmount
	 */
	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}
	/**
	 * @return
	 */
	public Double getTotalInt() {
		return totalInt;
	}
	/**
	 * @param totalInt
	 */
	public void setTotalInt(Double totalInt) {
		this.totalInt = totalInt;
	}
	
	/**
	 * @return
	 */
	public Double getIntBalance() {
		return intBalance;
	}
	/**
	 * @param intBalance
	 */
	public void setIntBalance(Double intBalance) {
		this.intBalance = intBalance;
	}
	/**
	 * @return the realIntBalance
	 */
	public Double getRealIntBalance() {
		return realIntBalance;
	}

	/**
	 * @param realIntBalance the realIntBalance to set
	 */
	public void setRealIntBalance(Double realIntBalance) {
		this.realIntBalance = realIntBalance;
	}

	/**
	 * @return
	 */
	public Double getPrinBalance() {
		return prinBalance;
	}
	/**
	 * @param prinBalance
	 */
	public void setPrinBalance(Double prinBalance) {
		this.prinBalance = prinBalance;
	}	
	
	/**
	 * @return the realPrinBalance
	 */
	public Double getRealPrinBalance() {
		return realPrinBalance;
	}

	/**
	 * @param realPrinBalance the realPrinBalance to set
	 */
	public void setRealPrinBalance(Double realPrinBalance) {
		this.realPrinBalance = realPrinBalance;
	}

	/**
	 * @return
	 */
	public Double getTotalReceive() {
		return totalReceive;
	}
	/**
	 * @param totalReceive
	 */
	public void setTotalReceive(Double totalReceive) {
		this.totalReceive = totalReceive;
	}
	
	/**
	 * @return the realTotalReceive
	 */
	public Double getRealTotalReceive() {
		return realTotalReceive;
	}

	/**
	 * @param realTotalReceive the realTotalReceive to set
	 */
	public void setRealTotalReceive(Double realTotalReceive) {
		this.realTotalReceive = realTotalReceive;
	}

	/**
	 * @return the installmentAmount
	 */
	public Double getInstallmentAmount() {
		return installmentAmount;
	}
	/**
	 * @param installmentAmount the installmentAmount to set
	 */
	public void setInstallmentAmount(Double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	/**
	 * @return the poNumber
	 */
	public String getPoNumber() {
		return poNumber;
	}

	/**
	 * @param poNumber the poNumber to set
	 */
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	/**
	 * @return the firstInstallmentDate
	 */
	public Date getFirstInstallmentDate() {
		return firstInstallmentDate;
	}

	/**
	 * @param firstInstallmentDate the firstInstallmentDate to set
	 */
	public void setFirstInstallmentDate(Date firstInstallmentDate) {
		this.firstInstallmentDate = firstInstallmentDate;
	}
}