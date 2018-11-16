package com.ext.testing.app.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientTransactionEntry;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryDTO;

import junit.framework.TestCase;

/**
 * @author bunlong.taing
 */
public class TestWSTransactionEntry extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSTransactionEntry.class);
	
	private static final String URL = "http://localhost:8080/efinance-app";
	
	/**
	 * Create Journal Entry
	 */
	public void testCreateTransactionEntry() {
		try {
			TransactionEntryDTO entryDTO = getSampleTransactionEntry();		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(entryDTO);
			logger.info("TransactionEntryDTO: ***[" + strJson + "]***");
			
			Response response = ClientTransactionEntry.createTransactionEntry(URL, entryDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				TransactionEntryDTO responseTransactionEntryDTO = response.readEntity(TransactionEntryDTO.class);
				if (responseTransactionEntryDTO != null) {
					logger.info("TransactionEntry ID :" + responseTransactionEntryDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(responseTransactionEntryDTO));
				} else {
					logger.info("No TransactionEntry returned - an error has occured on the server side.");
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
	 * Get Sample Journal Entry
	 * @return
	 */
	private TransactionEntryDTO getSampleTransactionEntry() {
		TransactionEntryDTO entryDTO = new TransactionEntryDTO();
		entryDTO.setDesc("TEST transaction account");
		List<JournalEntryDTO> journalEntriesDTO = new ArrayList<>();
		JournalEntryDTO journalEntryDTO1 = new JournalEntryDTO();
		journalEntryDTO1.setDesc("Payment Dealer");
		journalEntryDTO1.setDescEn("Payment Dealer");
		journalEntryDTO1.setJournalEventCode("01");
		journalEntryDTO1.setAmounts(Arrays.asList(new BigDecimal(10000d)));
		journalEntriesDTO.add(journalEntryDTO1);
		
		JournalEntryDTO journalEntryDTO2 = new JournalEntryDTO();
		journalEntryDTO2.setDesc("Payment Commission");
		journalEntryDTO2.setDescEn("Payment Commission");
		journalEntryDTO2.setJournalEventCode("03");
		journalEntryDTO2.setAmounts(Arrays.asList(new BigDecimal(8000d)));
		journalEntriesDTO.add(journalEntryDTO2);
				
		entryDTO.setJournalEntries(journalEntriesDTO);
		return entryDTO;
	}
	
	
}
