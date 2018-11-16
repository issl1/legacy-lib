package com.nokor.common.messaging.ws.resource.cfg.refdata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.BaseERefData;

import com.nokor.common.messaging.share.refdata.RefDataDTO;
import com.nokor.common.messaging.ws.resource.cfg.BaseConfigSrvRsc;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.helper.SeuksaServicesHelper;

/**
 * @author prasnar
 *
 */
public abstract class AbstractRefDataSrvRsc extends BaseConfigSrvRsc implements SeuksaServicesHelper {


	/**
	 * 
	 * @param refTable
	 * @param refDataDTO
	 * @return
	 */
	protected RefData toRefData(RefTable refTable, RefDataDTO refDataDTO) {
		return toRefData(refTable, refDataDTO, null);
	}
	
	/**
	 * @param refTable
	 * @param refDataDTO
	 * @param ide
	 * @return
	 */
	protected RefData toRefData(RefTable refTable, RefDataDTO refDataDTO, Long ide) {
		RefData refData = null;
		if (ide == null) { // Create new
			refData = new RefData();
			refData.setTable(refTable);
		} else {
			refData = REFDATA_SRV.getValueById(refTable.getCode(), ide);
			if (refData == null) {
				throw new EntityNotFoundException(I18N.messageParameterNotValid(ide.toString()));
			}
		}
		// DO NOT set Ide here
		// refData.setIde(xxx);
		refData.setCode(refDataDTO.getCode());
		refData.setDesc(refDataDTO.getDesc());
		refData.setDescEn(refDataDTO.getDescEn());
		refData.setActive(refDataDTO.getIsActive());
		refData.setSortIndex(refDataDTO.getSortIndex());
		return refData;
	}
	
	/**
	 * 
	 * @param refData
	 * @return
	 */
	protected RefDataDTO toRefDataDTO(RefData refData) {
		if (refData != null) {
			RefDataDTO refDataDTO = new RefDataDTO();
			refDataDTO.setId(refData.getIde());
			refDataDTO.setCode(refData.getCode());
			refDataDTO.setDesc(refData.getDesc());
			refDataDTO.setDescEn(refData.getDescEn());
			refDataDTO.setIsActive(refData.isActive());
			refDataDTO.setSortIndex(refData.getSortIndex());
			if (StringUtils.isNotEmpty(refData.getFieldValue1())) {
				refDataDTO.setParentRefDataUri(getParamsUriDTO(refData.getTable().getCode(), Long.valueOf(refData.getFieldValue1())));
			}
			
			return refDataDTO;
		}
		return null;
	}
	
	/**
	 * 
	 * @param refData
	 * @return
	 */
	protected static RefDataDTO toRefDataDTO(BaseERefData refData) {
		if (refData != null) {
			RefDataDTO refDataDTO = new RefDataDTO();
			refDataDTO.setId(refData.getId());
			refDataDTO.setCode(refData.getCode());
			refDataDTO.setDesc(refData.getDesc());
			refDataDTO.setDescEn(refData.getDescEn());
			refDataDTO.setIsActive(refData.isActive());
			refDataDTO.setSortIndex(refData.getSortIndex());
			return refDataDTO;
		}
		return null;
	}
	
	/**
	 * 
	 * @param refDataLst
	 * @return
	 */
	protected List<RefDataDTO> toRefDataDTOs(List<RefData> refDataLst) {
		List<RefDataDTO> refDataDTOLst = new ArrayList<RefDataDTO>();
		for (RefData refData : refDataLst) {
			refDataDTOLst.add(toRefDataDTO(refData));
		}
		return refDataDTOLst;
	}
	
	
}
