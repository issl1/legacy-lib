package com.ext.testing.app.accounting;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.client.jersey.ClientJournalEventAccount;
import com.nokor.ersys.messaging.share.accounting.JournalEventAccountDTO;

/**
 * 
 * @author prasnar
 *
 */
public class TestWSJournalEventAccount extends TestCase {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestWSJournalEventAccount.class);
	
	private static final String URL = "http://localhost:8080/efinance-ra";
	
	/**
	 * 
	 */
	public void testCreate() {
		try {
			JournalEventAccountDTO entryDTO = createJournalEventAccountDTO(1, 1, true);		
			
			Gson gson = new Gson();
			String strJson = gson.toJson(entryDTO);
			logger.info("JournalEventAccountDTO: ***[" + strJson + "]***");
			
			Response response = ClientJournalEventAccount.createJournalEventAccount(URL, entryDTO);
			
			logger.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				JournalEventAccountDTO eventAccountDTO = response.readEntity(JournalEventAccountDTO.class);
				if (eventAccountDTO != null) {
					logger.info("JournalEventAccount ID :" + eventAccountDTO.getId());
					logger.info("JSON: \r\n" + gson.toJson(eventAccountDTO));
				} else {
					logger.info("No JournalEventAccount returned - an error has occured on the server side.");
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
	 * 
	 * @param eventId
	 * @param accountId
	 * @param isDebit
	 * @return
	 */
	private JournalEventAccountDTO createJournalEventAccountDTO(long eventId, long accountId, boolean isDebit) {
		JournalEventAccountDTO evAccDTO = new JournalEventAccountDTO();
		
		evAccDTO.setEventId(eventId);
		evAccDTO.setAccountId(accountId);
		evAccDTO.setIsDebit(isDebit);
		
		return evAccDTO;
	}

}
