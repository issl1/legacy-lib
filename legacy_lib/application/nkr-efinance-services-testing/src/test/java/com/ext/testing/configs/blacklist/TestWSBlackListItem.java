package com.ext.testing.configs.blacklist;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.common.messaging.share.UriDTO;
import com.nokor.efinance.client.jersey.ClientBlackListItem;
import com.nokor.efinance.share.blacklist.BlackListItemCriteriaDTO;
import com.nokor.efinance.share.blacklist.BlackListItemDTO;
import com.nokor.frmk.testing.tools.NameGenerator;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSBlackListItem extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSBlackListItem.class);

	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
     * Get all blacklist items
     */
    public void xxtestGetBlackListItems() {
    	try {
    		Gson gson = new Gson();
			Response response = ClientBlackListItem.getBlackListItems(URL);
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<BlackListItemDTO> blackListItemDTOs = response.readEntity(new GenericType<List<BlackListItemDTO>>() {});
				for (BlackListItemDTO blackListItemDTO : blackListItemDTOs) {
					logger.info("BlackList ID:" + blackListItemDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(blackListItemDTO));
				}
				logger.info("Nb blacklist items found :" + blackListItemDTOs.size());
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
	
	/**
     * Get blacklist item by id 
     */
    public void xxtestGetBlackListItemById() {
    	try {
    		Gson gson = new Gson();
			
			Response response = ClientBlackListItem.getBlackListItem(URL, 1l);

			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				BlackListItemDTO blackListDTO = response.readEntity(BlackListItemDTO.class);
				logger.info("BlackList ID:" + blackListDTO.getId());
				logger.info("JSON: \r\n" + gson.toJson(blackListDTO));
				logger.info("************SUCCESS**********");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
    
    /**
	 * Search blacklist item by idNumber, FN & LN
	 */
	public void xxtestSearchBlackListItem() {
		try {
			BlackListItemCriteriaDTO criteriaDTO = new BlackListItemCriteriaDTO();
			criteriaDTO.setIdNumber("3-1005-03454-80-3");
			criteriaDTO.setFirstName("First");
			criteriaDTO.setLastName("Last");
		
			logger.info("BlackListItemCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientBlackListItem.searchBlackListItem(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<BlackListItemDTO> blackListItemDTOs = response.readEntity(new GenericType<List<BlackListItemDTO>>() {});
				logger.info("Nb blacklist item found :" + blackListItemDTOs.size());
				for (BlackListItemDTO listItemDTO : blackListItemDTOs) {
					logger.info("BlackList Item ID :" + listItemDTO.getId());
				}
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		
			logger.info("************SUCCESS**********");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Create blacklist item
	 */
	public void xxtestCreateBlackListItem() {
		try {
			BlackListItemDTO blackListItemDTO = getSampleBlackListItem();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(blackListItemDTO);
			logger.info(strJson);
			logger.info("BlackListItemDTO: ***[" + strJson + "]***");
			
			Response response = ClientBlackListItem.createBlackListItem(URL, blackListItemDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				BlackListItemDTO resBlackListItemDTO = response.readEntity(BlackListItemDTO.class);
				if (resBlackListItemDTO != null) {
					logger.info("BlackListItem ID :" + resBlackListItemDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(resBlackListItemDTO));
				} else {
					logger.info("No blacklist item returned - an error has occured on the server side.");
				}
				logger.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				logger.error("Error: " + errMsg);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Create sample blacklist item
	 * @return
	 */
	private BlackListItemDTO getSampleBlackListItem() {
		NameGenerator gen = new NameGenerator();
		Date today = new Date();
		
		BlackListItemDTO blackListItemDTO = new BlackListItemDTO();
		blackListItemDTO.setTypeIdNumber(new UriDTO(1l, ""));
		blackListItemDTO.setIdNumber("3-1005-03454-80-3");
		blackListItemDTO.setFirstName(gen.getName());
		blackListItemDTO.setLastName(gen.getName());
		blackListItemDTO.setPhoneNumber("011-xxx-xxx");
		blackListItemDTO.setBirthDate(today);
		blackListItemDTO.setIssuingDate(today);
		blackListItemDTO.setExpiringDate(today);
		blackListItemDTO.setCivility(new UriDTO(1l, ""));
		blackListItemDTO.setGender(new UriDTO(1l, ""));
		blackListItemDTO.setMaritalStatus(new UriDTO(1l, ""));
		blackListItemDTO.setNationality(new UriDTO(1l, ""));
		blackListItemDTO.setApplicantCateogry(new UriDTO(1l, ""));
		blackListItemDTO.setSource(new UriDTO(1l, ""));
		blackListItemDTO.setReason(new UriDTO(1l, ""));
		
		blackListItemDTO.setDetails("XXXX");
		blackListItemDTO.setRemarks("XXXX");
		return blackListItemDTO;
	}
}
