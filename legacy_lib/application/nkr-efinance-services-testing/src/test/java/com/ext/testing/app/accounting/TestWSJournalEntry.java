package com.ext.testing.app.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientJournalEntry;
import com.nokor.ersys.messaging.share.accounting.AccountLedgerDTO;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;

import junit.framework.TestCase;

/**
 * @author bunlong.taing
 */
public class TestWSJournalEntry extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSJournalEntry.class);
	
	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * Create Journal Entry
	 */
	public void xxtestCreateJournalEntry() {
		try {
			JournalEntryDTO entryDTO = getSampleJournalEntry();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(entryDTO);
			logger.info("JournalEntryDTO: ***[" + strJson + "]***");
			
			Response response = ClientJournalEntry.createJournalEntry(URL, entryDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				JournalEntryDTO responseJournaleEntryDTO = response.readEntity(JournalEntryDTO.class);
				if (responseJournaleEntryDTO != null) {
					logger.info("JournalEntry ID :" + responseJournaleEntryDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseJournaleEntryDTO));
				} else {
					logger.info("No JournalEntry returned - an error has occured on the server side.");
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
	 * Reconcile Journal Entry
	 */
	public void xxtestReconcileJournalEntry() {
		try {
			Gson gson = new Gson();

			Response response = ClientJournalEntry.listJournalEntries(URL);
			List<JournalEntryDTO> lstDTOs = response.readEntity(new GenericType<List<JournalEntryDTO>>() {});
			if (lstDTOs != null && lstDTOs.size() > 0) {
				JournalEntryDTO entryDTO = lstDTOs.get(0);
				response = ClientJournalEntry.reconcileJournalEntry(URL, entryDTO.getId());
				
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					logger.info("************CREATE-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					logger.error("Error: " + errMsg);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 */
	public void xxtestPostJournalEntryIntoLedger() {
		try {
			Response response = ClientJournalEntry.listJournalEntries(URL);
			List<JournalEntryDTO> lstDTOs = response.readEntity(new GenericType<List<JournalEntryDTO>>() {});
			if (lstDTOs != null && lstDTOs.size() > 0) {
				JournalEntryDTO entryDTO = lstDTOs.get(0);
				Gson gson = new Gson();
				Long journalEntryId = entryDTO.getId();
				logger.info("JournalEntryId: ***[" + journalEntryId + "]***");
				
				response = ClientJournalEntry.postJournalEntryIntoLedger(URL, journalEntryId);
				
				logger.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					ArrayList<AccountLedgerDTO> responseAccountLedgerDTOs = response.readEntity(new GenericType<ArrayList<AccountLedgerDTO>>(){});
					if (responseAccountLedgerDTOs != null) {
						logger.info("JSON: \r\n" + gson.toJson(responseAccountLedgerDTOs));
					} else {
						logger.info("No AccountLedgers returned - an error has occured on the server side.");
					}
					logger.info("************PostJournalEntryIntoLedger-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					logger.error("Error: " + errMsg);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 
	 */
	public void testListJournalEntries() {
		try {
			Response response = ClientJournalEntry.listJournalEntries(URL);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				List<JournalEntryDTO> lstDTOs = response.readEntity(new GenericType<List<JournalEntryDTO>>() {});
				if (lstDTOs != null) {
					for (JournalEntryDTO entryDTO : lstDTOs) {
						Gson gson = new Gson();
						logger.info("\r\n=>JSON: \r\n" + gson.toJson(entryDTO));
					}
				
					logger.info("************testListJournalEntries-SUCCESS************");
				} else {
					String errMsg = response.readEntity(String.class);
					logger.error("Error: " + errMsg);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	/**
	 * Get Sample Journal Entry
	 * @return
	 */
	private JournalEntryDTO getSampleJournalEntry() {
		String eventCode = "0705";
		JournalEntryDTO entryDTO = new JournalEntryDTO();
		
		entryDTO.setDesc("JournalEntry:" + new Date());
		entryDTO.setDescEn(entryDTO.getDesc());
		entryDTO.setReference(entryDTO.getDesc());
		entryDTO.setInfo(entryDTO.getDesc());
		entryDTO.setOtherInfo(entryDTO.getDesc());
		entryDTO.setJournalEventCode(eventCode);
		entryDTO.setWhen(new Date());
		entryDTO.setAmounts(Arrays.asList(new BigDecimal(0)));
		
		return entryDTO;
	}
	
	
}
