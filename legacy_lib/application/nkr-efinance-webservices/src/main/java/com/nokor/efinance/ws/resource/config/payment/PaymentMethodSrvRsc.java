package com.nokor.efinance.ws.resource.config.payment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.share.payment.PaymentMethodDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Payment method web service
 * @author uhout.cheng
 */
@Path("/configs/paymentmethods")
public class PaymentMethodSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all payment methods
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			BaseRestrictions<EPaymentMethod> restrictions = new BaseRestrictions<EPaymentMethod>(EPaymentMethod.class);
			
			List<EPaymentMethod> paymentMethods = ENTITY_SRV.list(restrictions);
			List<PaymentMethodDTO> paymentMethodDTOs = new ArrayList<>();
			for (EPaymentMethod paymentMethod : paymentMethods) {
				paymentMethodDTOs.add(toPaymentMethodDTO(paymentMethod));
			}
			return ResponseHelper.ok(paymentMethodDTOs);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * List Payment methods by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Payment Method - id. [" + id + "]");
		
			EPaymentMethod paymentMethod = ENTITY_SRV.getById(EPaymentMethod.class, id);
			
			if (paymentMethod == null) {
				String errMsg = "Payment Method [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			
			PaymentMethodDTO paymentMethodDTO = toPaymentMethodDTO(paymentMethod);
			
			return ResponseHelper.ok(paymentMethodDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param paymentMethod
	 * @return
	 */
	private PaymentMethodDTO toPaymentMethodDTO(EPaymentMethod paymentMethod) {
		PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
		paymentMethodDTO.setId(paymentMethod.getId());
		paymentMethodDTO.setCode(paymentMethod.getCode());
		paymentMethodDTO.setDesc(paymentMethod.getDesc());
		paymentMethodDTO.setDescEn(paymentMethod.getDescEn());
		paymentMethodDTO.setPaymentMethodCategory(paymentMethod.getCategoryPaymentMethod() != null ? toRefDataDTO(paymentMethod.getCategoryPaymentMethod()) : null);
		paymentMethodDTO.setAutoConfirm(paymentMethod.isAutoConfirm());
		return paymentMethodDTO;
	}
	
}
