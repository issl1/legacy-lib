package com.nokor.efinance.ws.resource.app.contract.flag;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityCreationException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;
import org.seuksa.frmk.tools.exception.EntityUpdateException;
import org.springframework.dao.DataIntegrityViolationException;

import com.nokor.common.messaging.share.UriDTO;
import com.nokor.common.messaging.ws.resource.BaseWsPath;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.common.FinWsMessage;
import com.nokor.efinance.share.contract.ContractFlagDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.app.contract.ContractWsPath;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path(ContractWsPath.CONTRACTS + ContractWsPath.FLAGS)
public class ContractFlagSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List all flags by contract reference & flag type
	 * @param contractNo
	 * @param type
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response list(@QueryParam(ContractWsPath.REFERENCE) String contractNo, @QueryParam(ConfigWsPath.TYPE) Long type) {
		try {
			LOG.debug("Contract - Ref. [" + contractNo + "]");
			LOG.debug("EFlag - TypeID. [" + type + "]");
			
			ContractFlagRestriction restrictions = new ContractFlagRestriction();
			
			if (StringUtils.isNotEmpty(contractNo)) {
				Contract contra = CONT_SRV.getByReference(contractNo);
				if (contra != null) {
					restrictions.setConId(contra.getId());
				} else {
					String errMsg = "Contract [" + contractNo + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				}
			}
			
			EFlag flagType = null;
			if (type != null) {
				flagType = EFlag.getById(type);
				if (flagType == null) {
					String errMsg = "Type [" + type + "]";
					throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));	
				} else {
					restrictions.setFlags(new EFlag[] { flagType });
				}
			}
			
			List<ContractFlag> flags = ENTITY_SRV.list(restrictions);
			List<ContractFlagDTO> flagDTOs= toContractFlagDTOs(flags);
			
			return ResponseHelper.ok(flagDTOs);
		} catch (Exception e) {
			String errMsg = "Error while searching flag [" + e.getMessage() + "]";
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.GET_LIST_KO, errMsg);
		}
	}
	
	/**
	 * List flag by id
	 * @param id
	 * @return
	 */
	@GET
	@Path(BaseWsPath.PATH_ID)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractFlag - id. [" + id + "]");
			
			ContractFlag flag = ENTITY_SRV.getById(ContractFlag.class, id);
			if (flag == null) {
				String errMsg = "ContractFlag [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));			
			}
			ContractFlagDTO flagDTO = toContractFlagDTO(flag);
			
			return ResponseHelper.ok(flagDTO);
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
	 * Create a flag
	 * @param flagDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response create(ContractFlagDTO flagDTO) {
		try {
			if (flagDTO == null) {
				throw new EntityNotValidParameterException(I18N.messageMandatoryField("ContractFlagDTO"));
			}
			ContractFlag flag = toContractFlag(flagDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new EntityNotValidParameterException(I18N.messageObjectNotFound(errMsg));
			}
			ENTITY_SRV.create(flag);
			
			return ResponseHelper.ok(toContractFlagDTO(flag));
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
	 * Update a flag by id
	 * @param id
	 * @param flagDTO
	 * @return
	 */
	@PUT
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response update(@PathParam("id") Long id, ContractFlagDTO flagDTO) {
		try {
			LOG.debug("ContractFlag - id. [" + id + "]");
			flagDTO.setId(id);
			ContractFlag flag = toContractFlag(flagDTO);
			if (!messages.isEmpty()) {
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			} 
			
			ENTITY_SRV.update(flag);
			
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
	 * Delete a flag by id
	 * @param id
	 * @return
	 */
	@DELETE
	@Path(BaseWsPath.PATH_ID)
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response delete(@PathParam(BaseWsPath.ID) Long id) {
		try {
			LOG.debug("ContractFlag [" + (id != null ? id : NULL) + "]");
			
			ContractFlag flag = ENTITY_SRV.getById(ContractFlag.class, id);
			if (flag == null) {
				String errMsg = "ContractFlag [" + id + "]";
				throw new EntityNotFoundException(I18N.messageObjectNotFound(errMsg));
			}
			
			ENTITY_SRV.delete(flag);
			
			return ResponseHelper.deleteSucess();
		} catch (EntityNotFoundException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.NOT_FOUND, errMsg);
		} catch (EntityNotValidParameterException e) {
			String errMsg = e.getMessage();
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.PARAMETER_NOT_VALID, errMsg);
		} catch (DataIntegrityViolationException e) {
			String errMsg = I18N.messageObjectCanNotDelete("" + id);
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		} catch(Exception e) {
			String errMsg = I18N.messageUnexpectedException(e.getMessage());
			LOG.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.DELETE_KO, errMsg);
		}
	}
	
	/**
	 * @param flag
	 * @return
	 */
	private ContractFlagDTO toContractFlagDTO(ContractFlag flag) {
		ContractFlagDTO flagDTO = new ContractFlagDTO();
		flagDTO.setId(flag.getId());
		flagDTO.setCreateDate(flag.getCreateDate());
		flagDTO.setCreateUser(flag.getCreateUser());
		flagDTO.setContractNo(flag.getContract() != null ? flag.getContract().getReference() : StringUtils.EMPTY);
		flagDTO.setFlag(flag.getFlag() != null ? toRefDataDTO(flag.getFlag()) : null);
		flagDTO.setLocation(flag.getLocation() != null ? toRefDataDTO(flag.getLocation()) : null);
		flagDTO.setProvince(flag.getProvince() != null ? getProvinceUriDTO(flag.getProvince().getId()) : null);
		flagDTO.setDistrict(flag.getDistrict() != null ? getDistrictUriDTO(flag.getDistrict().getId()) : null);
		flagDTO.setCommune(flag.getCommune() != null ? getSubDistrictUriDTO(flag.getCommune().getId()) : null);
		flagDTO.setDate(flag.getDate());
		flagDTO.setActionDate(flag.getActionDate());
		flagDTO.setComment(flag.getComment());
		flagDTO.setOtherValue(flag.getOtherLocationValue());
		flagDTO.setLocationDesc(flag.getLocantion());
		flagDTO.setCourtInCharge(flag.getCourtInCharge());
		flagDTO.setPoliceStation(flag.getPoliceStation() != null ? getPoliceStationsDTO(flag.getPoliceStation().getId()) : null);
		flagDTO.setCompleted(flag.isCompleted());
		flagDTO.setBranch(new UriDTO(flag.getBranch() != null ? flag.getBranch().getId() : null, null));
		flagDTO.setBranch(flag.getBranch() != null ? getBranchesDTO(flag.getBranch().getId()) : null);
		return flagDTO;
	}
	
	/**
	 * @param flags
	 * @return
	 */
	private List<ContractFlagDTO> toContractFlagDTOs(List<ContractFlag> flags) {
		List<ContractFlagDTO> contractFlagDTOs = new ArrayList<ContractFlagDTO>();
		for (ContractFlag flag : flags) {
			contractFlagDTOs.add(toContractFlagDTO(flag));
		}
		return contractFlagDTOs;
	}
	
	/**
	 * @param flagDTO
	 * @return
	 */
	private ContractFlag toContractFlag(ContractFlagDTO flagDTO) {
		ContractFlag flag = null;
		if (flagDTO.getId() != null) {
			flag = ENTITY_SRV.getById(ContractFlag.class, flagDTO.getId());
			if (flag == null) {
				messages.add(FinWsMessage.CONTRACT_FLAG_NOT_FOUND);
				String errMsg = messages.get(0).getDesc();
				throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
			}
		} else {
			 flag = ContractFlag.createInstance();
		}
		
		Contract contract = null;
		if (StringUtils.isEmpty(flagDTO.getContractNo())) {
			messages.add(FinWsMessage.CONTRACT_MANDATORY);
		} else {
			contract = CONT_SRV.getByReference(flagDTO.getContractNo());
			if (contract == null) {
				messages.add(FinWsMessage.CONTRACT_NOT_FOUND);
			}
		}
		flag.setContract(contract);
		
		if (flagDTO.getFlag() != null) {
			flag.setFlag(EFlag.getById(flagDTO.getFlag().getId()));
			if (flag.getFlag() == null) {
				messages.add(FinWsMessage.FLAG_TYPE_NOT_FOUND);
			}
		} else {
			messages.add(FinWsMessage.FLAG_TYPE_MANDATORY);
		}
		
		if (flagDTO.getLocation() != null) {
			flag.setLocation(ELocation.getById(flagDTO.getLocation().getId()));
			if (flag.getLocation() == null) {
				messages.add(FinWsMessage.LOCATION_NOT_FOUND);
			}
		} 
		
		if (flagDTO.getProvince() != null) {
			flag.setProvince(ENTITY_SRV.getById(Province.class, flagDTO.getProvince().getId()));
			if (flag.getProvince() == null) {
				messages.add(FinWsMessage.PROVINCE_NOT_FOUND);
			}
		}
		
		if (flagDTO.getDistrict() != null) {
			flag.setDistrict(ENTITY_SRV.getById(District.class, flagDTO.getDistrict().getId()));
			if (flag.getDistrict() == null) {
				messages.add(FinWsMessage.DISTRICT_NOT_FOUND);
			}
		}
		
		if (flagDTO.getCommune() != null) {
			flag.setCommune(ENTITY_SRV.getById(Commune.class, flagDTO.getCommune().getId()));
			if (flag.getCommune() == null) {
				messages.add(FinWsMessage.SUB_DISTRICT_NOT_FOUND);
			}
		}
		
		if (flagDTO.getCommune() != null) {
			flag.setCommune(ENTITY_SRV.getById(Commune.class, flagDTO.getCommune().getId()));
			if (flag.getCommune() == null) {
				messages.add(FinWsMessage.SUB_DISTRICT_NOT_FOUND);
			}
		}
		
		if (flagDTO.getBranch() != null) {
			flag.setBranch(CONT_SRV.getById(OrgStructure.class, flagDTO.getBranch().getId()));
		}
		
		if (flagDTO.getPoliceStation() != null) {
			flag.setPoliceStation(ENTITY_SRV.getById(PoliceStation.class, flagDTO.getPoliceStation().getId()));
		}
		
		flag.setDate(flagDTO.getDate());
		flag.setActionDate(flagDTO.getActionDate());
		flag.setComment(flagDTO.getComment());
		flag.setOtherLocationValue(flagDTO.getOtherValue());
		flag.setLocantion(flagDTO.getLocationDesc());
		flag.setCourtInCharge(flagDTO.getCourtInCharge());
		flag.setCompleted(flagDTO.isCompleted());
		return flag;
	}

}
