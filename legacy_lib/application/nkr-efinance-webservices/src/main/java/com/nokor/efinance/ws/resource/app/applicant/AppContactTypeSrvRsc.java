package com.nokor.efinance.ws.resource.app.applicant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.AppContactTypeVO;
import com.nokor.efinance.core.applicant.model.ApplicantContactInfoVO;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.applicant.AppContactTypeDTO;
import com.nokor.efinance.ws.resource.app.contract.BaseContractSrvRsc;
import com.nokor.efinance.ws.resource.config.ConfigWsPath;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.ResponseHelper;
import com.nokor.frmk.messaging.ws.WsReponseException;

/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts/appcontacts")
public class AppContactTypeSrvRsc extends BaseContractSrvRsc {
	
	/**
	 * List contract by reference & type contact
	 * @param reference
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public Response get(@QueryParam(ConfigWsPath.CONTRACT_NO) String contractNo, @QueryParam(ConfigWsPath.TYPE) Long type) {
		try {
			
			LOG.debug("ContractNo. [" + contractNo + "]");
			LOG.debug("TypeContact. [" + type + "]");
			
			ApplicantContactInfoVO applicantContactInfoVO = null;
			if (StringUtils.isEmpty(contractNo)) {
				String errMsg = "contractNo is Requried";
				throw new IllegalStateException(I18N.messageFieldEmptyRequired(errMsg));
			} else {
				Contract contract = CONT_SRV.getByReference(contractNo);
				if (contract == null) {
					String errMsg = "Contract Not Found";
					throw new IllegalStateException(I18N.messageObjectNotFound(errMsg));
				} else {
					ETypeContactInfo[] infos = new ETypeContactInfo[] { ETypeContactInfo.LANDLINE, ETypeContactInfo.MOBILE, ETypeContactInfo.EMAIL };
					if (type != null) {
						ETypeContactInfo typeContactInfo = ETypeContactInfo.getById(type);
						if (typeContactInfo != null) {
							infos = new ETypeContactInfo[] { typeContactInfo };
						}
					}
					applicantContactInfoVO = new ApplicantContactInfoVO(contract, infos);
				}
			}
			
			List<AppContactTypeDTO> appContactTypeVoDTOs = new ArrayList<>();
			if (applicantContactInfoVO != null) {
				if (applicantContactInfoVO.getAppContactTypeVOs() != null && !applicantContactInfoVO.getAppContactTypeVOs().isEmpty()) {
					for (AppContactTypeVO contactTypeVO : applicantContactInfoVO.getAppContactTypeVOs()) {
						appContactTypeVoDTOs.add(toAppContactTypeDTO(contactTypeVO));
					}
				}
			}
		
			return ResponseHelper.ok(appContactTypeVoDTOs);
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
	 * @param appContactTypeVO
	 * @return
	 */
	private AppContactTypeDTO toAppContactTypeDTO(AppContactTypeVO appContactTypeVO) {
		AppContactTypeDTO appContactTypeDTO = new AppContactTypeDTO();
		appContactTypeDTO.setCode(appContactTypeVO.getCode());
		appContactTypeDTO.setType(appContactTypeVO.getType());
		appContactTypeDTO.setValue(appContactTypeVO.getValue());
		return appContactTypeDTO;
	}

}
