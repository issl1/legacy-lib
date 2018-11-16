package com.nokor.efinance.client.jersey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;

/**
 * @author bunlong.taing
 */
public class ClientJournalEntry extends FinWsClient {
	
	private static final String RSC_LIST = "/accounting/journalentries";
	private static final String RSC_CREATE = "/accounting/journalentries";
	private static final String RSC_GET = "/accounting/journalentries/{id}";
	private static final String RSC_RECONCILE = "/accounting/journalentries/{id}/reconcile";
	private static final String RSC_POST_JOURNAL_ENTRY_INTO_LEDGER = "/accounting/journalentries/{id}";

	/**
	 * @param url
	 * @param path
	 * @param id
	 * @return
	 */
	private static String getServiceURL(String url, String path, Long id) {
		String serviceURL = getServiceURL(url, path); 
		
		if (id != null) {
			serviceURL = serviceURL.replace("{id}", String.valueOf(id));
		}
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	
	/**
	 * @param url
	 * @param path
	 * @return
	 */
	private static String getServiceURL(String url, String path) {
		String serviceURL = url + SERVICE_PREFIX + path;
		
		logger.debug("Service URL: [" + serviceURL + "]");
		
		return serviceURL;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static Response listJournalEntries(String url) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Response result = client.target(getServiceURL(url, RSC_LIST, null))
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(Response.class);
		
		return result;
	}
	
	/**
	 * Create Journal Entry
	 * @param url
	 * @param journalEntryDTO
	 * @return
	 */
	public static Response createJournalEntry(String url, JournalEntryDTO journalEntryDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<JournalEntryDTO> entityJournalEntry = Entity.entity(journalEntryDTO, MediaType.APPLICATION_JSON);
		
		Response result = client.target(getServiceURL(url, RSC_CREATE, null))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entityJournalEntry, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param journalEntryId
	 * @return
	 */
	public static Response getJournalEntry(String url, long journalEntryId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config).register(JacksonFeature.class);
		
		Response result = client.target(getServiceURL(url, RSC_GET + "/", journalEntryId))
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(Response.class); 
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param journalEntryId
	 * @return
	 */
	public static Response reconcileJournalEntry(String url, long journalEntryId) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Response result = client.target(getServiceURL(url, RSC_RECONCILE, journalEntryId))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(null, Response.class);

		return result;
	}
	
	/**
	 * Post JournalEntryIntoLedger
	 * @param url
	 * @param id
	 * @return
	 */
	public static Response postJournalEntryIntoLedger(String url, Long id) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Response result = client.target(getServiceURL(url, RSC_POST_JOURNAL_ENTRY_INTO_LEDGER, id))
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(null, Response.class);
		return result;
	}

}
