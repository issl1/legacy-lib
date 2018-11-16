package com.nokor.efinance.ws.resource.config.finproduct;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.financialproduct.FinProductDTO;
import com.nokor.efinance.share.financialproduct.FinProductServiceDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Financial product web service
 * @author uhout.cheng
 */
@Path("/configs/financialproducts")
public class FinancialProductSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all financial products
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {					
			List<FinProduct> finProducts = ENTITY_SRV.list(FinProduct.class);
			List<FinProductDTO> finProductsDTO = new ArrayList<>();
			for (FinProduct finProduct : finProducts) {
				finProductsDTO.add(toFiancialProductDTO(finProduct));
			}
			return ResponseHelper.ok(finProductsDTO);
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
	 * List financial product by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("Campaign - id. [" + id + "]");
		
			FinProduct finProduct = ENTITY_SRV.getById(FinProduct.class, id);
			
			if (finProduct == null) {
				String errMsg = "Financial product param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			FinProductDTO finProductDTO = toFiancialProductDTO(finProduct);
			
			return ResponseHelper.ok(finProductDTO);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * Create financial product
	 * @param finProductDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(FinProductDTO finProductDTO) {
		try {
			FinProduct finProduct = toFinProduct(finProductDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			FIN_PROD_SRV.createFinProduct(finProduct);
			finProductDTO.setId(finProduct.getId());
			
			return ResponseHelper.ok(finProductDTO);
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
	
	/**
	 * Update financial product
	 * @param id
	 * @param finProductDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, FinProductDTO finProductDTO) {
		try {
			LOG.debug("FinProduct - id. [" + id + "]");
			FinProduct finProduct = toFinProduct(finProductDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(finProduct);
			
			return ResponseHelper.updateSucess();
		} catch (IllegalStateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.KO, errMsg);
		} catch (EntityUpdateException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	
	/**
	 * Delete financial product
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("FinProduct - id. [" + id + "]");
			
			FinProduct finProduct = ENTITY_SRV.getById(FinProduct.class, id);
			
			if (finProduct == null) {
				String errMsg = "FinProduct param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			FIN_PROD_SRV.deleteFinProduct(finProduct);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * Convert from FinancialProductDTO to FinProduct
	 * @param finProductDTO
	 * @return
	 */
	private FinProduct toFinProduct(FinProductDTO finProductDTO, Long id) {
		FinProduct finProduct = null;
		if (id != null) {
			finProduct = ENTITY_SRV.getById(FinProduct.class, id);
			if (finProduct == null) {
				messages.add(FinWsMessage.FIN_PROD_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			finProduct = new FinProduct();
		}
		
		if (StringUtils.isNotEmpty(finProductDTO.getCode())) {
			if (id == null && isAlreadyExist(finProductDTO.getCode())) {
				messages.add(FinWsMessage.DUPLICATE_FIN_PRODUCT_CODE);
			} else {
				finProduct.setCode(finProductDTO.getCode());
			}
		} else {
			messages.add(FinWsMessage.FIN_PRODUCT_CODE_MANDATORY);
		}
		
		if (StringUtils.isNotEmpty(finProductDTO.getDescEn())) {
			finProduct.setDescEn(finProductDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.FIN_PRODUCT_DESC_EN_MANDATORY);
		}
		
		if (finProductDTO.getProductLineId() != null) {
			finProduct.setProductLine(ENTITY_SRV.getById(ProductLine.class, finProductDTO.getProductLineId()));
		} else {
			messages.add(FinWsMessage.PRODUCT_LINE_MANDATORY);
		}
		
		if (finProductDTO.getStartDate() != null) {
			finProduct.setStartDate(finProductDTO.getStartDate());
		} else {
			messages.add(FinWsMessage.FIN_PRODUCT_START_DATE_MANDATORY);
		}
		
		if (finProductDTO.getEndDate() != null) {
			finProduct.setEndDate(finProductDTO.getEndDate());
		} else {
			messages.add(FinWsMessage.FIN_PRODUCT_END_DATE_MANDATORY);
		}		
		
		if (finProductDTO.getFrequencyId() != null) {
			finProduct.setFrequency(EFrequency.getById(finProductDTO.getFrequencyId()));
		}
		
		if (finProductDTO.getGuarantorRequirementId() != null) {
			finProduct.setGuarantor(EOptionality.getById(finProductDTO.getGuarantorRequirementId()));
		}
		
		if (finProductDTO.getCollateralRequirementId() != null) {
			finProduct.setCollateral(EOptionality.getById(finProductDTO.getCollateralRequirementId()));
		}
		
		if (finProductDTO.getReferenceRequirementId() != null) {
			finProduct.setReference(EOptionality.getById(finProductDTO.getReferenceRequirementId()));
		}
		
		if (finProductDTO.getVatId() != null) {
			finProduct.setVat(ENTITY_SRV.getById(Vat.class, finProductDTO.getVatId()));
		} else {
			finProduct.setVat(null);
		}
		
		if (finProductDTO.getPenaltyRuleId() != null) {
			finProduct.setPenaltyRule(ENTITY_SRV.getById(PenaltyRule.class, finProductDTO.getPenaltyRuleId()));
		} else {
			finProduct.setPenaltyRule(null);
		}
		
		if (finProductDTO.getLocksplitRuleId() != null) {
			finProduct.setLockSplitRule(ENTITY_SRV.getById(LockSplitRule.class, finProductDTO.getLocksplitRuleId()));
		} else {
			finProduct.setLockSplitRule(null);
		}
		
		if (finProductDTO.getRoundingFormatId() != null) {
			finProduct.setRepaymentRounding(ERoundingFormat.getById(finProductDTO.getRoundingFormatId()));
		} else {
			finProduct.setRepaymentRounding(null);
		}
				
		List<FinProductService> finProductServices = new ArrayList<>();
		if (finProductDTO.getFinProductServiceDTOs() != null) {
			for (FinProductServiceDTO finProductServiceDTO : finProductDTO.getFinProductServiceDTOs()) {
				FinProductService finProductService = new FinProductService();		
				finProductService.setFinancialProduct(finProduct);
				if (finProductServiceDTO.getFinServiceId() != null) {
					finProductService.setService(ENTITY_SRV.getById(FinService.class, finProductServiceDTO.getFinServiceId()));
				}
				finProductServices.add(finProductService);
			}
		}
		finProduct.setFinancialProductServices(finProductServices);
		return finProduct;
	}
	
	/**
	 * Convert to FinProduct Data transfer
	 * @param finProduct
	 * @return
	 */
	private FinProductDTO toFiancialProductDTO(FinProduct finProduct) {
		FinProductDTO finProductDTO = new FinProductDTO();
		finProductDTO.setId(finProduct.getId());
		finProductDTO.setCode(finProduct.getCode());
		finProductDTO.setDescEn(finProduct.getDescEn());
		finProductDTO.setDesc(finProduct.getDesc());
		finProductDTO.setProductLineId(finProduct.getProductLine() != null ? finProduct.getProductLine().getId() : null);
		finProductDTO.setStartDate(finProduct.getStartDate());
		finProductDTO.setEndDate(finProduct.getEndDate());
		finProductDTO.setMaxFirstPaymentDay(MyNumberUtils.getInteger(finProduct.getMaxFirstPaymentDay()));
		finProductDTO.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(finProduct.getNumberOfPrincipalGracePeriods()));
		finProductDTO.setTerm(MyNumberUtils.getInteger(finProduct.getTerm()));
		finProductDTO.setPeriodicInterestRate(MyNumberUtils.getDouble(finProduct.getPeriodicInterestRate()));
		finProductDTO.setAdvancePaymentPercentage(MyNumberUtils.getDouble(finProduct.getAdvancePaymentPercentage()));
		finProductDTO.setFrequencyId(finProduct.getFrequency() != null ? finProduct.getFrequency().getId() : null);
		finProductDTO.setGuarantorRequirementId(finProduct.getGuarantor() != null ? finProduct.getGuarantor().getId() : null);
		finProductDTO.setCollateralRequirementId(finProduct.getCollateral() != null ? finProduct.getCollateral().getId() : null);
		finProductDTO.setReferenceRequirementId(finProduct.getReference() != null ? finProduct.getReference().getId() : null);
		finProductDTO.setVatId(finProduct.getVat() != null ? finProduct.getVat().getId() : null);
		finProductDTO.setPenaltyRuleId(finProduct.getPenaltyRule() != null ? finProduct.getPenaltyRule().getId() : null);
		finProductDTO.setLocksplitRuleId(finProduct.getLockSplitRule() != null ? finProduct.getLockSplitRule().getId() : null);
		finProductDTO.setMinAdvancePaymentPercentage(MyNumberUtils.getDouble(finProduct.getMinAdvancePaymentPercentage()));
		finProductDTO.setRoundingFormatId(finProduct.getRepaymentRounding() != null ? finProduct.getRepaymentRounding().getId() : null);
		
		List<FinProductServiceDTO> finProductServiceDTOs = new ArrayList<>();
		if (finProduct.getFinancialProductServices() != null && !finProduct.getFinancialProductServices().isEmpty()) {
			for (FinProductService finProductService : finProduct.getFinancialProductServices()) {
				FinProductServiceDTO finProductServiceDTO = new FinProductServiceDTO();
				finProductServiceDTO.setId(finProductService.getId());
				finProductServiceDTO.setFinServiceId(finProductService.getService() != null ? finProductService.getService().getId() : null);
				finProductServiceDTOs.add(finProductServiceDTO);
			}
		}
		finProductDTO.setFinProductServiceDTOs(finProductServiceDTOs);
		return finProductDTO;
	}
	
	/**
	 * @param code
	 * @return
	 */
	private boolean isAlreadyExist(String code) {
		FinProduct finProduct = ENTITY_SRV.getByCode(FinProduct.class, code);
		return finProduct != null;
	}
}
