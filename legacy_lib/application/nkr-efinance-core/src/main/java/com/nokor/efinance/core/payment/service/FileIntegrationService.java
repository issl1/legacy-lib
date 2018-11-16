package com.nokor.efinance.core.payment.service;

import java.util.List;

import org.seuksa.frmk.service.MainEntityService;

import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.payment.model.PaymentFileItem;

/**
 * File Integration Service
 * @author bunlong.taing
 */
public interface FileIntegrationService extends MainEntityService {
	
	/**
	 * @param fileName
	 * @param fileFormat
	 */
	void integrateFilePayment(String fileName, EPaymentFileFormat fileFormat);
	
	/**
	 * @param id
	 * @return
	 */
	List<PaymentFileItem> listPaymentFileItemByPaymentFile(Long id);
	
	/**
	 * 
	 * @param fileName
	 * @param fileFormat
	 * @return
	 */
	String validateFileFormat(String fileName, EPaymentFileFormat fileFormat);

}
