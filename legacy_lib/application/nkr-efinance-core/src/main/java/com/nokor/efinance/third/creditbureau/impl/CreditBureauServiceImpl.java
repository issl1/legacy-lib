package com.nokor.efinance.third.creditbureau.impl;

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.third.conf.ModuleConfig;
import com.nokor.efinance.third.creditbureau.CreditBureauService;
import com.nokor.efinance.third.creditbureau.cbc.service.CBCService;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;

/**
 * CBC Service
 * @author ly.youhort
 */
@Service("creditBureauService")
public class CreditBureauServiceImpl implements CreditBureauService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CBCService cbcService;
	
	/**
	 * Call credit bureau enquiry service
	 * @param quotation
	 * @param parameters
	 * @return
	 */
	public String enquiry(Quotation quotation, Map<String, Object> parameters) throws InvokedCreditBureauException {
		Configuration config = ModuleConfig.getInstance().getConfiguration();
		String strategy = config.getString("creditbureau.strategy");
		logger.debug("Strategy used by Credit Bureau [" + strategy + "]");
		String response = "";
		if ("CBC".equals(strategy)) {
			response = cbcService.enquiry(quotation, parameters);
		}
		return response;
	}	
	
	/**
	 * Validate the result of credit bureau
	 * @param response
	 * @throws InvokedCreditBureauException
	 * @throws ErrorCreditBureauException
	 * @throws ParserCreditBureauException
	 */
	public boolean validateResponse(String response) throws InvokedCreditBureauException, ErrorCreditBureauException, ParserCreditBureauException {
		return cbcService.validateResponse(response);
	}

	@Override
	public String getReference(String response) throws InvokedCreditBureauException {
		return cbcService.getReference(response);
	}
}
