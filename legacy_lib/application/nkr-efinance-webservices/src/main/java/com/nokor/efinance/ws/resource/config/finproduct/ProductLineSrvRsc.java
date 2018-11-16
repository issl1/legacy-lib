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

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.financial.model.EProductLineType;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.productline.ProductLineDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.ersys.core.hr.model.eref.EOptionality;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * Product line web service
 * @author uhout.cheng
 */
@Path("/configs/productlines")
public class ProductLineSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * List all product lines
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {				
			List<ProductLine> productLines = ENTITY_SRV.list(ProductLine.class);
			List<ProductLineDTO> productLineDTOs = new ArrayList<>();
			for (ProductLine productLine : productLines) {
				productLineDTOs.add(toProductLineDTO(productLine));
			}
			return ResponseHelper.ok(productLineDTOs);
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
	 * List product line by id
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("ProductLine - id. [" + id + "]");
		
			ProductLine productLine = ENTITY_SRV.getById(ProductLine.class, id);
			
			if (productLine == null) {
				String errMsg = "ProductLine param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ProductLineDTO productLineDTO = toProductLineDTO(productLine);
			
			return ResponseHelper.ok(productLineDTO);
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
	 * Create product line
	 * @param productLineDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ProductLineDTO productLineDTO) {
		try {
			ProductLine productLine = toProductLine(productLineDTO, null);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.create(productLine);
			productLineDTO.setId(productLine.getId());
			
			return ResponseHelper.ok(productLineDTO);
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
	 * Update product line
	 * @param id
	 * @param productLineDTO
	 * @return
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ProductLineDTO productLineDTO) {
		try {
			LOG.debug("ProductLine - id. [" + id + "]");
			ProductLine productLine = toProductLine(productLineDTO, id);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(productLine);
			
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
	 * Delete product line
	 * @param id
	 * @return
	 */
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("ProductLine - id. [" + id + "]");
			
			ProductLine productLine = ENTITY_SRV.getById(ProductLine.class, id);
			
			if (productLine == null) {
				String errMsg = "ProductLine param [" + id + "] - Not found";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.delete(productLine);
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
	 * Convert from ProductLineDTO to ProductLine
	 * @param productLineDTO
	 * @param id
	 * @return
	 */
	private ProductLine toProductLine(ProductLineDTO productLineDTO, Long id) {
		ProductLine productLine = null;
		if (id != null) {
			productLine = ENTITY_SRV.getById(ProductLine.class, id);
			if (productLine == null) {
				messages.add(FinWsMessage.PRODUCT_LINE_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			productLine = new ProductLine();
		}
		if (StringUtils.isNotEmpty(productLineDTO.getDescEn())) {
			productLine.setDescEn(productLineDTO.getDescEn());
		} else {
			messages.add(FinWsMessage.PRODUCT_LINE_DESC_EN_MANDATORY);
		}
		if (StringUtils.isNotEmpty(productLineDTO.getDesc())) {
			productLine.setDesc(productLineDTO.getDesc());
		} else {
			messages.add(FinWsMessage.PRODUCT_LINE_DESC_MANDATORY);
		}
		if (productLineDTO.getProductLineTypeId() != null) {
			productLine.setProductLineType(EProductLineType.getById(EProductLineType.class, productLineDTO.getProductLineTypeId()));
			if (productLine.getProductLineType() == null) {
				productLine.setProductLineType(EProductLineType.getById(EProductLineType.class, 1L));
			}
		} else {
			messages.add(FinWsMessage.PRODUCT_LINE_TYPE_MANDATORY);
		}
		
		if (productLineDTO.getGuarantorRequirementId() != null) {
			productLine.setGuarantorRequirement(EOptionality.getById(productLineDTO.getGuarantorRequirementId()));
		}
		
		if (productLineDTO.getCollateralRequirementId() != null) {
			productLine.setCollateralRequirement(EOptionality.getById(productLineDTO.getCollateralRequirementId()));
		}
		
		if (productLineDTO.getReferenceRequirementId() != null) {
			productLine.setReferenceRequirement(EOptionality.getById(productLineDTO.getReferenceRequirementId()));
		}
		
		if (productLineDTO.getVatId() != null) {
			productLine.setVatCapital(ENTITY_SRV.getById(Vat.class, productLineDTO.getVatId()));
		} else {
			productLine.setVatCapital(null);
		}
		
		if (productLineDTO.getPenaltyRuleId() != null) {
			productLine.setPenaltyRule(ENTITY_SRV.getById(PenaltyRule.class, productLineDTO.getPenaltyRuleId()));
		} else {
			productLine.setPenaltyRule(null);
		}
		
		if (productLineDTO.getLocksplitRuleId() != null) {
			productLine.setLockSplitRule(ENTITY_SRV.getById(LockSplitRule.class, productLineDTO.getLocksplitRuleId()));
		} else {
			productLine.setLockSplitRule(null);
		}
		
		if (productLineDTO.getRoundingFormatId() != null) {
			productLine.setRoundingFormat(ERoundingFormat.getById(productLineDTO.getRoundingFormatId()));
		} else {
			productLine.setRoundingFormat(null);
		}
		
		return productLine;
	}
	
	/**
	 * Convert to ProductLine data transfer
	 * @param productLine
	 * @return
	 */
	private ProductLineDTO toProductLineDTO(ProductLine productLine) {
		ProductLineDTO productLineDTO = new ProductLineDTO();
		productLineDTO.setId(productLine.getId());
		productLineDTO.setDescEn(productLine.getDescEn());
		productLineDTO.setDesc(productLine.getDesc());
		productLineDTO.setProductLineTypeId(productLine.getProductLineType() != null ? productLine.getProductLineType().getId() : null);
		productLineDTO.setVatId(productLine.getVatCapital() != null ? productLine.getVatCapital().getId() : null);
		productLineDTO.setPenaltyRuleId(productLine.getPenaltyRule() != null ? productLine.getPenaltyRule().getId() : null);
		productLineDTO.setLocksplitRuleId(productLine.getLockSplitRule() != null ? productLine.getLockSplitRule().getId() : null);
		productLineDTO.setGuarantorRequirementId(productLine.getGuarantorRequirement() != null ? productLine.getGuarantorRequirement().getId() : null);
		productLineDTO.setCollateralRequirementId(productLine.getCollateralRequirement() != null ? productLine.getCollateralRequirement().getId() : null);
		productLineDTO.setReferenceRequirementId(productLine.getReferenceRequirement() != null ? productLine.getReferenceRequirement().getId() : null);
		productLineDTO.setRoundingFormatId(productLine.getRoundingFormat() != null ? productLine.getRoundingFormat().getId() : null);
		return productLineDTO;
	}
}
