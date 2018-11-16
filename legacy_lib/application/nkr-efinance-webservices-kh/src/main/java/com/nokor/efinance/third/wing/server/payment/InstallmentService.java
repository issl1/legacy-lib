package com.nokor.efinance.third.wing.server.payment;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.nokor.efinance.third.wing.server.payment.vo.ConfirmRequestMessage;
import com.nokor.efinance.third.wing.server.payment.vo.ConfirmResponseMessage;
import com.nokor.efinance.third.wing.server.payment.vo.InfoRequestMessage;
import com.nokor.efinance.third.wing.server.payment.vo.InfoResponseMessage;

/**
 * @author ly.youhort
 */
@WebService(targetNamespace = "http://www.nokor-group.com/efinance")
public interface InstallmentService {

	@WebMethod
	InfoResponseMessage getInstallmentInfo(@WebParam(name = "infoRequestMessage") InfoRequestMessage infoRequestMessage);
	
	@WebMethod
	ConfirmResponseMessage confirmReceivePayment(@WebParam(name = "confirmRequestMessage") ConfirmRequestMessage confirmRequestMessage);
}
