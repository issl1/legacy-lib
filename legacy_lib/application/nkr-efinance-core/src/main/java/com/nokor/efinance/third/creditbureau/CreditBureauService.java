package com.nokor.efinance.third.creditbureau;

import java.util.Map;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;

public interface CreditBureauService {

	/**
	 * Inquiry data 
	 * @param quotation
	 * @param parameters
	 * @return
	 */
	String enquiry(Quotation quotation, Map<String, Object> parameters) throws InvokedCreditBureauException;
	
	/**
	 * Validate the result of credit bureau
	 * @param response
	 * @throws InvokedCreditBureauException
	 * @throws ErrorCreditBureauException
	 * @throws ParserCreditBureauException
	 */
	boolean validateResponse(String response) throws InvokedCreditBureauException, ErrorCreditBureauException, ParserCreditBureauException;
	
	/**
	 * @param response
	 * @return
	 */
	String getReference(String response) throws InvokedCreditBureauException;
}
