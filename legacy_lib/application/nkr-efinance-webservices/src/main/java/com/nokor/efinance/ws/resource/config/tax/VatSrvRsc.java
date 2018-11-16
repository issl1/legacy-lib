package com.nokor.efinance.ws.resource.config.tax;

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

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.share.vat.VatDTO;
import com.nokor.efinance.ws.FinResourceSrvRsc;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author buntha.chea
 *
 */
@Path("/configs/vats")
public class VatSrvRsc extends FinResourceSrvRsc {
	
	/**
	 * GET LIST OF VAT
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list() {		
		try {
			// get all vat						
			List<Vat> vats = ENTITY_SRV.list(Vat.class);
			List<VatDTO> vatDTOs = new ArrayList<>();
			for (Vat vat : vats) {
				vatDTOs.add(toVatDTO(vat));
			}
			return ResponseHelper.ok(vatDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching vats [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		} 
	}
	
	/**
	 * GET VAT BY ID
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam("id") Long id) {
		try {
			LOG.debug("VAT - id. [" + id + "]");
		
			Vat vat = ENTITY_SRV.getById(Vat.class, id);
			
			if (vat == null) {
				String errMsg = "VAT [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			VatDTO vatDTO = toVatDTO(vat);
			vatDTO.setId(vat.getId());
			return ResponseHelper.ok(vatDTO);
		}catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while loading vat - id. [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_UNIQUE_KO, errMsg);
		}
	}
	
	/**
	 * CREATE VAT
	 * @param vatDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(VatDTO vatDTO) {
		try {
			Vat vat = toVat(vatDTO);
			ENTITY_SRV.saveOrUpdate(vat);
			
			vatDTO.setId(vat.getId());
			return ResponseHelper.ok(vatDTO);
		} catch (EntityCreationException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		} catch (EntityAlreadyExistsException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.ALREADY_EXISTS, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (Exception e) {
			String errMsg = "Error while creating VAT";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.CREATION_KO, errMsg);
		}
	}
/**
 * 
 * @param vatDTO
 * @param id
 * @return
 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{id}")
	public Response update(VatDTO vatDTO, @PathParam("id") Long id) {
		try {
			LOG.debug("vat - id. [" + id + "]");
			Vat vat = toVatById(vatDTO, id);
			ENTITY_SRV.update(vat);
			LOG.info("***Update Success***");
			return ResponseHelper.updateSucess();
		} catch (EntityUpdateException e) {
			String errMsg = e.getMessage();
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID,
					errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
		}
	}
	/**
	 * DELETE
	 * @param id
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Path("/{id}")
	public Response delete(@PathParam("id") Long id) {
		try {
			LOG.debug("vat - id. [" + id + "]");
			Vat vat = ENTITY_SRV.getById(Vat.class, id);
			if (vat == null) {
				String errMsg = "VAT [" + id + "]";
				throw new EntityNotFoundException(I18N.messageParameterNotValid(errMsg));
			} 
			ENTITY_SRV.delete(vat);
			LOG.debug("*********Delete-Success*************");
			return ResponseHelper.deleteSucess();
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID,errMsg);
		} catch (Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			this.LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * 
	 * @param vat
	 * @return
	 */
	private VatDTO toVatDTO(Vat vat) {
		VatDTO vatDTO = new VatDTO();
		vatDTO.setId(vat.getId());
		vatDTO.setDescEn(vat.getDescEn());
		vatDTO.setDesc(vat.getDesc());
		vatDTO.setValue(vat.getValue());
		vatDTO.setStartDate(vat.getStartDate());
		vatDTO.setEndDate(vat.getEndDate());
		return vatDTO;
	}
	
	/**
	 * 
	 * @param vatDTO
	 * @return
	 */
	private Vat toVat(VatDTO vatDTO) {
		Vat vat = new Vat();
		vat.setId(vatDTO.getId());
		vat.setDescEn(vatDTO.getDescEn());
		vat.setDesc(vatDTO.getDesc());
		vat.setValue(vatDTO.getValue());
		vat.setStartDate(vatDTO.getStartDate());
		vat.setEndDate(vatDTO.getEndDate());
		return vat;
	}
	
	/**
	 * 
	 * @param vatDTO
	 * @param ide
	 * @return
	 */
	private Vat toVatById(VatDTO vatDTO, Long ide) {
		Vat vat = ENTITY_SRV.getById(Vat.class, ide);
		if (vat == null) {
			throw new EntityNotFoundException(I18N.messageParameterNotValid(ide.toString()));
		} 
		vat.setDescEn(vatDTO.getDescEn());
		vat.setDesc(vatDTO.getDesc());
		vat.setValue(vatDTO.getValue());
		vat.setStartDate(vatDTO.getStartDate());
		vat.setEndDate(vatDTO.getEndDate());
		return vat;
	}
	
}
