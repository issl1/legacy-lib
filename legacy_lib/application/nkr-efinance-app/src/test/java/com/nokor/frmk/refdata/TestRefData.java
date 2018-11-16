package com.nokor.frmk.refdata;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.testing.BaseTestCase;


/**
 * @author prasnar
 * 
 */
public class TestRefData extends BaseTestCase {
	/**
     * 
     */
    public TestRefData() {
    }
    
    @Override
    protected boolean isRequiredAuhentication() {
    	return false;
    }
   
    @Override
    protected void setAuthentication() {
        login = "admin";
        password = "admin@EFIN";
    }
    
    /**
     * 
     */
    public void testRefDataCustomizedFields() {
    	try {
    		List<ETypeOrganization> typeOrganizations = ETypeOrganization.values();
    		for (ETypeOrganization typeOrg : typeOrganizations) {
				logger.debug("Type Org [" + typeOrg.getCode() + "]");
			}
    		List<ESubTypeOrganization> subTypeOrganizations = ESubTypeOrganization.values();
    		for (ESubTypeOrganization subTypeOrg : subTypeOrganizations) {
				logger.debug("Sub Type Org [" + subTypeOrg.getCode() + "] [" + (subTypeOrg.getTypeOrganization() != null ? subTypeOrg.getTypeOrganization().getCode() : "<NULL>") + "]");
			}

    		List<EOrgLevel> orgLevels = EOrgLevel.values();
    		for (EOrgLevel level : orgLevels) {
				logger.debug("EOrgLevel [" + level.getCode() + "] [" + level.getLevel() + "]");
			}

    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		throw new IllegalStateException(e);
    	}
    }
    
	/**
     * 
     */
    public void xxtestUpdateRefData() {
    	try {
    		
    		String refDataClazzName = EColor.class.getCanonicalName();
    		String code = "BLA";
    		RefData refData = REFDATA_SRV.getValueByCode(refDataClazzName, code);
    		logger.debug("Data before: [" + refData.getCode() + "] - " + refData.getDescEn());
    		
    		refData.setDescEn("BLA -" + DateUtils.todayFullNoSeparator());
    		refData.setCode("BLA");
    		REFDATA_SRV.updateRefData(refData);
    		
    		logger.debug("Data after: [" + refData.getCode() + "] - " + refData.getDescEn());
        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
    
    /**
     * 
     */
    public void xxtestDeleteRefData() {
    	String tableShortName = "colors";
		Long ide = 26L;
    	try {
			RefTable table = REFDATA_SRV.getTableByShortName(tableShortName);
			if (table == null) {
				throw new EntityNotFoundException(I18N.messageParameterNotValid(tableShortName));
			}
			
			logger.debug("RefData - refDataTableName [" + tableShortName + "] id [" + (ide != null ? ide : "NULL") + "]");
			REFDATA_SRV.deleteRefData(table.getCode(), ide);
			logger.info("************SUCCESS**********");
			
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
    
  /**
     * 
     */
    public void xxtestListRefData() {
    	try {
    		
    		String refDataClazzName = EColor.class.getCanonicalName();
    		List<RefData> refDataLst = REFDATA_SRV.getValues(refDataClazzName);
    		
    		for (RefData refData : refDataLst) {
				logger.debug("Data: [" + refData.getCode() + "] - " + refData.getDescEn());
			}
        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
   
    /**
     * 
     */
    public void xxtestListEColor() {
    	try {
    		
    		for (EColor color : EColor.values()) {
				logger.debug("Color: [" + color.getCode() + "] - " + color.getDescEn());
			}
        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
   

}
