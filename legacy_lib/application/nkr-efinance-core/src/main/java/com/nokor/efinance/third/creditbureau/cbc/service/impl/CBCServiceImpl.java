package com.nokor.efinance.third.creditbureau.cbc.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.JiBXException;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.third.conf.ModuleConfig;
import com.nokor.efinance.third.creditbureau.cbc.XmlBinder;
import com.nokor.efinance.third.creditbureau.cbc.model.AccountType;
import com.nokor.efinance.third.creditbureau.cbc.model.Cid;
import com.nokor.efinance.third.creditbureau.cbc.model.Cid3;
import com.nokor.efinance.third.creditbureau.cbc.model.EnquiryType;
import com.nokor.efinance.third.creditbureau.cbc.model.Header;
import com.nokor.efinance.third.creditbureau.cbc.model.ProductType;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Cadr;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Ccnt;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Cdob;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Cemp;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Cnam;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Consumer;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Cplb;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Eadr;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Enquiry;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Message;
import com.nokor.efinance.third.creditbureau.cbc.model.request.Request;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Error;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Item;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Response;
import com.nokor.efinance.third.creditbureau.cbc.service.CBCService;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.config.model.SettingConfig;

/**
 * CBC Service
 * @author ly.youhort
 */
@Service("cbcService")
public class CBCServiceImpl implements CBCService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EntityService entityService;
	
	/**
	 * Inquiry data 
	 * @param quotation
	 * @param parameters
	 * @return
	 */
	public String enquiry(Quotation quotation, Map<String, Object> parameters) throws InvokedCreditBureauException {
		
		EnquiryType enquiryType = (EnquiryType) parameters.get(EnquiryType.class.toString());
		ProductType productType = (ProductType) parameters.get(ProductType.class.toString());
		AccountType accountType = (AccountType) parameters.get(AccountType.class.toString());
		
		Configuration config = ModuleConfig.getInstance().getConfiguration();
		
		SettingConfig cbsRunNO = entityService.getByCode(SettingConfig.class, "cbc_run_no");
		if (cbsRunNO == null) {
			String message = "Could not process CBC interface, please contact administrator !";
			logger.error(message + " [CBC_RUN_NO is not well configured in table TU_PARAMETER]");
			throw new InvokedCreditBureauException(message);			
		}
		
		String runNo = leftPad("0000000" + cbsRunNO.getValue(), 7);
		
		logger.debug("[CBC - enquiryType] : " + enquiryType);
		logger.debug("[CBC - productType] : " + productType);
		logger.debug("[CBC - accountType] : " + accountType);
		logger.debug("[CBC - runNo] : " + runNo);

		EApplicantType applicantType = parameters.containsKey(EApplicantType.class.toString()) ? (EApplicantType) parameters.get(EApplicantType.class.toString()) : EApplicantType.C;
		
		boolean isGuarantor = applicantType == EApplicantType.G;
		
		Request request = new Request();
		request.setService(Request.SERVICE);
		request.setAction(Request.ACTION);
		
		// Generate header
		Header header = new Header();
		request.setHeader(header);
		header.setMemberId(config.getString("creditbureau.cbc.member_id"));
		header.setUserId(config.getString("creditbureau.cbc.user_id"));
		header.setRunNo(runNo);
		header.setTotItems(1);
		
		// Generate message
		Message message = new Message();
		request.setMessage(message);
		Enquiry enquiry = new Enquiry();
		message.setEnquiry(enquiry);
		enquiry.setEnquiryType(enquiryType.toString());
		enquiry.setProductType(productType.toString());
		enquiry.setNoOfApplicant(1);
		enquiry.setAccountType(accountType.toString());
		enquiry.setEnquiryReference("GLF" + runNo);
		enquiry.setAmount(MyNumberUtils.getDouble(quotation.getTiFinanceAmount()));
		enquiry.setCurrency(config.getString("creditbureau.cbc.currency"));
		Consumer consumer = new Consumer();
		enquiry.setConsumer(consumer);
		
		Applicant applicant = isGuarantor ? quotation.getGuarantor() : quotation.getApplicant();
		Individual customer = applicant.getIndividual();
		
		List<QuotationDocument> documents = quotation.getQuotationDocuments(applicantType);
		Collections.sort(documents, new DocumentComparatorBySortIndex());
		List<Cid> cids = new ArrayList<Cid>();
		int maxDoc = documents.size() <= 3 ? documents.size() : 3;
		for (int i = 0; i < maxDoc; i++) {
			QuotationDocument document = documents.get(i);
			if (document.getDocument().isSubmitCreditBureau()) {
				Cid cid = new Cid();
				cid.setCid1(document.getDocument().getCode());
				cid.setCid2(document.getReference());
				if (document.getExpireDate() != null) {
					Cid3 cid3 = new Cid3();
					cid.setCid3(cid3);
					cid3.setCid3d(leftPad("0" + DateUtils.getDay(document.getExpireDate()), 2));
					cid3.setCid3m(leftPad("0" + DateUtils.getMonth(document.getExpireDate()), 2));
					cid3.setCid3y(leftPad("0" + DateUtils.getYear(document.getExpireDate()), 4));
				}
				cids.add(cid);
			}
		}
		
		Cdob cdob = new Cdob();		
		cdob.setCdbd(leftPad("0" + DateUtils.getDay(customer.getBirthDate()), 2));
		cdob.setCdbm(leftPad("0" + DateUtils.getMonth(customer.getBirthDate()), 2));
		cdob.setCdby(leftPad("0" + DateUtils.getYear(customer.getBirthDate()), 4));
				
		Cplb cplb = new Cplb();
		cplb.setCplbc(customer.getPlaceOfBirth().getCountry().getCode());
		cplb.setCplbp(customer.getPlaceOfBirth().getCode());
		
		Cnam cnam = new Cnam();
		cnam.setCnmfa(customer.getLastName());
		cnam.setLang("kh");
		cnam.setCnm1a(customer.getFirstName());
		cnam.setCnmfe(customer.getLastNameEn());
		cnam.setCnm1e(customer.getFirstNameEn());
		
		Address customerAddress = customer.getMainAddress();
		Cadr cadr = new Cadr();
		cadr.setCadt("RESID");
		cadr.setCadpr(customerAddress.getProvince().getCode());
		
		if (customerAddress.getDistrict() != null && !"OTH".equals(customerAddress.getDistrict().getCode())) {
			cadr.setCadds(customerAddress.getDistrict().getCode());
		}		
		if (customerAddress.getCommune() != null && !"OTH".equals(customerAddress.getCommune().getCode())) {
			cadr.setCadcm(customerAddress.getCommune().getCode());
		}
		if (customerAddress.getVillage() != null && !"OTH".equals(customerAddress.getVillage().getCode())) {
			cadr.setCadvl(customerAddress.getVillage().getCode());
		}
		cadr.setCad1e(customerAddress.getResumeAddress());
		cadr.setCad8e(customerAddress.getProvince().getDescEn());
		cadr.setCad9(customerAddress.getCountry().getCode());
		
		Ccnt ccnt = new Ccnt();
		ccnt.setCcn1("M");
		ccnt.setCcn2("855");
		// TODO YLY
		// ccnt.setCcn4(customer.getMobilePhone());
		
		Employment employment = customer.getCurrentEmployment();
		Address employmentAddress = employment.getAddress();
		Cemp cemp = new Cemp();
		cemp.setEtyp("C");
		cemp.setEslf(employment.getEmploymentStatus().getCode());
		cemp.setEoce(employment.getPosition());
		int lengthOfServiceInMonths = (MyNumberUtils.getInteger(employment.getTimeWithEmployerInYear()) * 12 
				+ MyNumberUtils.getInteger(employment.getTimeWithEmployerInMonth())); 
		cemp.setElen(lengthOfServiceInMonths);
		cemp.setEtms(employment.getRevenue());
		cemp.setEcurr(config.getString("creditbureau.cbc.currency"));
		cemp.setEnme(employment.getEmployerName());
		
		Eadr eadr = new Eadr();
		eadr.setEadt("WORK");
		eadr.setEadp(employmentAddress.getProvince().getCode());
		
		if (employmentAddress.getDistrict() != null && !"OTH".equals(employmentAddress.getDistrict().getCode())) {
			eadr.setEadd(employmentAddress.getDistrict().getCode());
		}
		if (employmentAddress.getCommune() != null && !"OTH".equals(employmentAddress.getCommune().getCode())) {
			eadr.setEadc(employmentAddress.getCommune().getCode());
		}
		if (employmentAddress.getVillage() != null && !"OTH".equals(employmentAddress.getVillage().getCode())) {
			eadr.setEadv(employmentAddress.getVillage().getCode());
		}
		eadr.setEad1e(employmentAddress.getResumeAddress());
		eadr.setEad8e(employmentAddress.getProvince().getDescEn());
		eadr.setEad9(employmentAddress.getCountry().getCode());
		cemp.setEadr(eadr);
		
		consumer.setCapl("P");
		consumer.setCids(cids);
		consumer.setCdob(cdob);
		consumer.setCplb(cplb);
		consumer.setCgnd(customer.getGender().toString());
		consumer.setCmar(customer.getMaritalStatus().toString());
		consumer.setCnat(customer.getNationality().getCode());
		consumer.setCnam(cnam);
		consumer.setCadr(cadr);
		consumer.setCcnt(ccnt);
		consumer.setCemp(cemp);
		
		String xmlResponse = null;
		try {
			xmlResponse = enquiry(request);
			validateResponse(xmlResponse);
			// Increase Run No and update into database
			cbsRunNO.setValue(String.valueOf(Integer.parseInt(cbsRunNO.getValue()) + 1));
			entityService.saveOrUpdate(cbsRunNO);			
		} catch (ErrorCreditBureauException e) {
			logger.error("ErrorCreditBureauException", e);
			throw new InvokedCreditBureauException(e.getMessage(), e);
		} catch (ParserCreditBureauException e) {
			logger.error("ParserCreditBureauException", e);
			throw new InvokedCreditBureauException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new InvokedCreditBureauException(e.getMessage(), e);
		}		
		return xmlResponse;
	}
	
	/**
	 * @param request
	 * @return
	 */
	private String enquiry(Request request) throws IOException {
		try {
			String xmlRequest = XmlBinder.marshal(request);
			logger.debug("[CBC Request] : " + xmlRequest);
			String xmlResponse = doHttpPost(xmlRequest);
			logger.debug("[CBC Response] : " + xmlResponse);
			return xmlResponse;
		} catch (JiBXException e) {
			logger.error("[CBC Error] : ", e);
			throw new IllegalArgumentException("[CBC ERROR] Could not process to CBC interface, please contact administrator !");
		}
	}
	
	/**
	 * Do http post 
	 * @param url
	 * @param xmlRequest
	 * @return
	 * @throws IOException
	 */
	private String doHttpPost(String xmlRequest) throws IOException {
		Configuration config = ModuleConfig.getInstance().getConfiguration();
		String url = config.getString("creditbureau.cbc.url");
		if (StringUtils.isEmpty(url)) {
			String message = "URL of CBC is NULL, please contact administrator !";
			logger.error(message + " [efinance-module-config.xml]");
			throw new IllegalArgumentException(message);
		}
		
		PostMethod post = new PostMethod(url);
		post.setDoAuthentication(true);
		// Set Request content
		RequestEntity entity = new StringRequestEntity(xmlRequest, null, null);
		post.setRequestEntity(entity);		
		
		int port = config.getInt("creditbureau.cbc.security.port");
		String username = config.getString("creditbureau.cbc.security.username");
		String password = config.getString("creditbureau.cbc.security.password");
		
		logger.debug(" [CBC Url] = " + url);
		logger.debug(" [CBC Port] = " + port);
		logger.debug(" [CBC Username] = " + username);
		logger.debug(" [CBC password] = ******");
		
		// Get HTTP client
		HttpClient httpclient = new HttpClient();
		httpclient.getState().setCredentials(
				new AuthScope(null, port, null),
				new UsernamePasswordCredentials(username, password));
		// Execute request
		httpclient.executeMethod(post);
		// Throw an HttpException for status other than success
		if (post.getStatusCode() / 100 != 2) {
			throw new HttpException(post.getStatusText());
		}
		String response = getStringFromInputStream(post.getResponseBodyAsStream());
		// Release current connection to the connection pool once you are done
		post.releaseConnection();
		return response;
	}
	
	/**
	 * Get string from input stream
	 * @param is
	 * @return
	 */
	private String getStringFromInputStream(InputStream is) {		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder(); 
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			} 
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("IOException", e);
				}
			}
		} 
		return sb.toString();
 
	}
	
	/**
	 * @param value
	 * @param size
	 * @return
	 */
	private String leftPad(String value, int size) {
		return value == null ? null : value.substring(value.length() - size);
	}
	
	
	/**
	 * Validate the result of credit bureau
	 * @param response
	 * @throws InvokedCreditBureauException
	 * @throws ErrorCreditBureauException
	 * @throws ParserCreditBureauException
	 */
	public boolean validateResponse(String response) throws InvokedCreditBureauException, ErrorCreditBureauException, ParserCreditBureauException {
		try {
			Response cbResponse = XmlBinder.unmarshal(response);
			
			com.nokor.efinance.third.creditbureau.cbc.model.response.Message message = cbResponse.getMessage(); 
			
			if (message.getParser() != null) {
				String error = "[RSP_MSG]" + message.getParser().getRspmsg() + "[/RSP_MSG]";
				error += "[REQ_DATA]" + message.getParser().getReqdata() + "[/REQ_DATA]";
				throw new ParserCreditBureauException(error);
			} else {
				Item item = message.getItems().get(0);
				if (item.getParser() != null) {
					String error = "[RSP_MSG]" + item.getParser().getRspmsg() + "[/RSP_MSG]";
					error += "[REQ_DATA]" + item.getParser().getReqdata() + "[/REQ_DATA]";
					throw new ParserCreditBureauException(error);
				} else if (item.getErrors() != null) {
					List<Error> errors = item.getErrors();
					String errorDesc = "";  
					for (Error error : errors) {
						errorDesc += "[ERROR]";
						errorDesc += "[FIELD]" + error.getField() + "[/FIELD]";
						errorDesc += "<RSP_MSG>" + error.getRspmsg() + "[/RSP_MSG]";
						errorDesc += "[DATA]" + error.getData() + "[/DATA]";
						errorDesc += "[/ERROR]";
					}
					throw new ErrorCreditBureauException(errorDesc);
				}
			}
		} catch (JiBXException e) {
			throw new InvokedCreditBureauException(e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * @param response
	 * @return
	 */
	public String getReference(String response) throws InvokedCreditBureauException {
		String reference = "";
		try {
			Response cbResponse = XmlBinder.unmarshal(response);
			reference = cbResponse.getMessage().getItems().get(0).getEnquiryReference();
		} catch (JiBXException e) {
			throw new InvokedCreditBureauException(e.getMessage(), e);
		}
		return reference;
	}
	
	/**
	 * @author ly.youhort
	 */
	private class DocumentComparatorBySortIndex implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			QuotationDocument c1 = (QuotationDocument) o1;
			QuotationDocument c2 = (QuotationDocument) o2;
			if (c1 == null || c1.getDocument().getSortIndex() == null) {
				if (c2 == null || c2.getDocument().getSortIndex() == null) {
					return 0;
				}
				return 1;
			}
			if (c2 == null || c2.getDocument().getSortIndex() == null) {
				return -1;
			}
			return c1.getDocument().getSortIndex().compareTo(c2.getDocument().getSortIndex());
		}
	}
}
