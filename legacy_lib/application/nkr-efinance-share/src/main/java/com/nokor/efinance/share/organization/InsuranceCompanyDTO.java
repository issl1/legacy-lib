package com.nokor.efinance.share.organization;

import java.io.Serializable;

import com.nokor.efinance.share.payment.PaymentDetailDTO;
import com.nokor.ersys.messaging.share.organization.OrganizationDTO;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceCompanyDTO extends OrganizationDTO implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 847581828446051677L;
	
	private InsuranceLostDTO insuranceLostDTO;
	private InsuranceAOMDTO insuranceAOMDTO;
	private PaymentDetailDTO paymentDetail;
	
	/**
	 * @return the insuranceLostDTO
	 */
	public InsuranceLostDTO getInsuranceLostDTO() {
		return insuranceLostDTO;
	}
	
	/**
	 * @param insuranceLostDTO the insuranceLostDTO to set
	 */
	public void setInsuranceLostDTO(InsuranceLostDTO insuranceLostDTO) {
		this.insuranceLostDTO = insuranceLostDTO;
	}
	
	/**
	 * @return the insuranceAOMDTO
	 */
	public InsuranceAOMDTO getInsuranceAOMDTO() {
		return insuranceAOMDTO;
	}
	
	/**
	 * @param insuranceAOMDTO the insuranceAOMDTO to set
	 */
	public void setInsuranceAOMDTO(InsuranceAOMDTO insuranceAOMDTO) {
		this.insuranceAOMDTO = insuranceAOMDTO;
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
	
}
