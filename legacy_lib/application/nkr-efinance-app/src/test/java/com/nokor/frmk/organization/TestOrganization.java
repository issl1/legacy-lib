package com.nokor.frmk.organization;

import java.util.List;

import org.seuksa.frmk.tools.exception.EntityUpdateException;

import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.frmk.messaging.ws.EResponseStatus;
import com.nokor.frmk.messaging.ws.WsReponseException;
import com.nokor.frmk.testing.BaseTestCase;


/**
 * @author prasnar
 * 
 */
public class TestOrganization extends BaseTestCase {
	/**
     * 
     */
    public TestOrganization() {
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
    public void testOrgLevel() {
    	try {

    		logger.info("************SUCCESS**********");
    		List<EOrgLevel> lstLev = EOrgLevel.values();
    		for (EOrgLevel lev : lstLev) {
				logger.debug("- " + lev.getCode()  + " - " + lev.getDesc() + " - " + lev.getLevel());
			}
        	
    	} catch (Exception e) {
			String errMsg = e.getMessage();
			logger.error(errMsg, e);
    	}
    }
        
	/**
     * 
     */
    public void xtestCreate() {
    	try {
    		
    		Organization mainCom = Organization.getMainOrganization();
    		String subComName = "Tanaban";
    		Organization subCom = ENTITY_SRV.getByField(Organization.class, MOrganization.NAME, subComName);
    		if (subCom == null) {
	    		subCom = Organization.createInstance();
	    		subCom.setTypeOrganization(OrganizationTypes.MAIN);
	    		subCom.setName(subComName);
	    		subCom.setNameEn(subCom.getName());
	    		ENTITY_SRV.create(subCom);
    		}

    		OrgStructure orgStrParent = ENTITY_SRV.getByField(OrgStructure.class, OrgStructure.NAME, mainCom.getName());
    		if (orgStrParent == null) {
	    		// Structure creation
	    		orgStrParent = OrgStructure.createInstance();
	    		orgStrParent.setOrganization(mainCom);
	    		orgStrParent.setName(mainCom.getName());
	    		orgStrParent.setParent(null);
	    		ENTITY_SRV.create(orgStrParent);
//	
//	    		OrgStructure orgStr = OrgStructure.createInstance();
//	    		orgStr.setCompany(subCom);
//	    		orgStr.setName(subCom.getName());
//	    		orgStr.setParent(orgStrParent);
//	    		ENTITY_SRV.create(orgStr);
    		}
    		
    		String departmentName = "Department A";
    		OrgStructure orgStrDept = ENTITY_SRV.getByField(OrgStructure.class, OrgStructure.NAME, departmentName);
    		if (orgStrDept == null) {
	    		orgStrDept = OrgStructure.createInstance();
	    		orgStrDept.setOrganization(mainCom);
	    		orgStrDept.setName(departmentName);
	    		orgStrDept.setParent(orgStrParent);
	    		ENTITY_SRV.create(orgStrDept);
    		}

    		
    		logger.debug("Main Org: " + orgStrParent.getName());
    		logger.debug("Main Org: Parent: " + orgStrParent.getParent());
    		
    		List<OrgStructure> children = orgStrParent.getChildren();
    		for (OrgStructure orgStr : children) {
        		logger.debug("Child Org: " + orgStr.getName());
           		logger.debug("Child Org: Parent class: " + orgStr.getParent().getClass().getTypeName());
           		logger.debug("Child Org: Parent class: " + (orgStr.getParent() instanceof OrgStructure));
           		logger.debug("Child Org: Parent: " + ((OrgStructure) orgStr.getParent()).getName());
           		logger.debug("Child Org: Parent: " + ((OrgStructure) orgStr).getParent().getName());
			}
    		
        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (EntityUpdateException e) {
			String errMsg = e.getMessage();
			logger.error(errMsg, e);
			throw new WsReponseException(EResponseStatus.UPDATE_KO, errMsg);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		throw new WsReponseException(EResponseStatus.UPDATE_KO, e.getMessage());
    	}
    }
    
 

}
