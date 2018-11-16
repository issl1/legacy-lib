package com.ext.testing.app.locksplit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientLockSplit;
import com.nokor.efinance.share.locksplit.LockSplitCriteriaDTO;
import com.nokor.efinance.share.locksplit.LockSplitDTO;
import com.nokor.efinance.share.locksplit.LockSplitItemDTO;
import com.nokor.efinance.share.locksplit.LockSplitStatus;

import junit.framework.TestCase;

/**
 * 
 * @author uhout.cheng
 */
public class TestWSLockSplit extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSLockSplit.class);

	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * Search lock split
	 */
	public void xxxtestSearchApplicant() {
		try {
			LockSplitCriteriaDTO criteriaDTO = new LockSplitCriteriaDTO();
			criteriaDTO.setLockSplitNo("0000000003");
				
			logger.info("LockSplitCriteriaDTO: ***[" + criteriaDTO + "]***");
			
			Response response = ClientLockSplit.searchLockSplit(URL, criteriaDTO);

			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<LockSplitDTO> lockSplitDTOs = response.readEntity(new GenericType<List<LockSplitDTO>>() {});
				logger.info("Nb lock split found :" + lockSplitDTOs.size());
				for (LockSplitDTO lockSplitDTO : lockSplitDTOs) {
					logger.info("LockSplit ID :" + lockSplitDTO.getId());
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
	 * Create lock split
	 */
	public void testCreateLockSplit() {
		try {
			LockSplitDTO lockSplitDTO = getSampleLockSplit();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(lockSplitDTO);
			System.out.println(strJson);
			logger.info("LockSplitDTO: ***[" + strJson + "]***");
			
			Response response = ClientLockSplit.createLockSplit(URL, lockSplitDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				LockSplitDTO responseLockSplitDTO = response.readEntity(LockSplitDTO.class);
				if (responseLockSplitDTO != null) {
					logger.info("LockSplit ID :" + responseLockSplitDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseLockSplitDTO));
				} else {
					logger.info("No lock split returned - an error has occured on the server side.");
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
	 * Create sample lock split
	 * @return
	 */
	private LockSplitDTO getSampleLockSplit() {
		Date today = new Date();
		LockSplitDTO lockSplitDTO = new LockSplitDTO();
		lockSplitDTO.setContractID("0115000003");
		lockSplitDTO.setFrom(today);
		lockSplitDTO.setTo(today);
		lockSplitDTO.setTotalAmount(100d);
		lockSplitDTO.setTotalVatAmount(100d);
		LockSplitItemDTO lockSplitItemDTO = new LockSplitItemDTO();
		lockSplitItemDTO.setReceiptCode("01");
		lockSplitItemDTO.setAmount(200d);
		lockSplitItemDTO.setVatAmount(10d);
		lockSplitItemDTO.setPriority(12);
		lockSplitItemDTO.setStatus(LockSplitStatus.NEW);
		List<LockSplitItemDTO> lockSplitItemDTOs = new ArrayList<>();
		lockSplitItemDTOs.add(lockSplitItemDTO);
		lockSplitDTO.setLockSplitItemDTOs(lockSplitItemDTOs);
		return lockSplitDTO;
	}
}
