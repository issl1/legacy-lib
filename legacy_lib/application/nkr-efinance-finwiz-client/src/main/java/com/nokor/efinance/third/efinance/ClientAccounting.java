package com.nokor.efinance.third.efinance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;

/**
 * 
 * @author uhout.cheng
 */
public class ClientAccounting {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientAccounting.class);
	
	private static final String RSC_JOURNAL_CREATE = "/accounting/journalentries";
	
	/**
	 * @param contract
	 */
	public static JournalEntryDTO createPayment(JournalEntryDTO journalEntryDTO) throws Exception {
		if (false) {
			Gson gson = new Gson();
			String strJson = gson.toJson(journalEntryDTO);
			LOG.info("PaymentDTO : ***[" + strJson + "]***");
			
			ClientConfig config = new ClientConfig();
			final Client client = ClientBuilder.newClient(config);
			
			Entity<JournalEntryDTO> entity = Entity.entity(journalEntryDTO, MediaType.APPLICATION_JSON);
			
			String url = ThirdAppConfigFileHelper.getEFinanceApplicationURL();
			
			Response response = client.target(url + RSC_JOURNAL_CREATE)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(entity, Response.class);
			
			LOG.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode()) {
				journalEntryDTO = response.readEntity(new GenericType<JournalEntryDTO>() {});
				LOG.info("************CREATE-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				LOG.error("Error: " + errMsg);
			}
			return journalEntryDTO;
		}
		return null;
	}	
	
	/**
	 * @param reference
	 * @param startDate
	 * @param downPaymentAmount
	 * @return
	 * @throws Exception
	 */
	public static JournalEntryDTO createPayment(String reference, String desc, Date startDate, String eventCode, BigDecimal... amounts) throws Exception {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			JournalEntryDTO journalEntryDTO = new JournalEntryDTO();
			journalEntryDTO.setReference(reference);
			journalEntryDTO.setWhen(startDate);
			journalEntryDTO.setJournalEventCode(eventCode);
			journalEntryDTO.setDescEn(desc);
			journalEntryDTO.setDesc(desc);
			journalEntryDTO.setAmounts(Arrays.asList(amounts));
			return createPayment(journalEntryDTO);
		}
		return null;
	}	
	
	/**
	 * @param reference
	 * @param startDate
	 * @param downPaymentAmount
	 * @return
	 * @throws Exception
	 */
	public static JournalEntryDTO createPayment(AccountingDTO accountingDTO) throws Exception {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			JournalEntryDTO journalEntryDTO = new JournalEntryDTO();
			journalEntryDTO.setReference(accountingDTO.getReference());
			journalEntryDTO.setDesc(accountingDTO.getDesc());
			journalEntryDTO.setDescEn(accountingDTO.getDesc());			
			journalEntryDTO.setWhen(accountingDTO.getStartDate());
			journalEntryDTO.setJournalEventCode(accountingDTO.getEventCode());
			journalEntryDTO.setAmounts(accountingDTO.getAmounts());
			return createPayment(journalEntryDTO);
		}
		return null;
	}	
	
	/**
	 * @author youhort.ly
	 *
	 */
	public static class AccountingDTO {
		private String reference;
		private Date startDate;
		private String eventCode;
		private String desc;
		private List<BigDecimal> amounts;
		
		public AccountingDTO(String eventCode) {
			this.eventCode = eventCode;
		}
		
		/**
		 * @param reference
		 * @param startDate
		 * @param eventCode
		 * @param teAmount
		 * @param vatAmount
		 * @param tiAmount
		 */
		public AccountingDTO(String reference, String desc, Date startDate, String eventCode, List<BigDecimal> amounts) {
			this.reference = reference;
			this.desc = desc;
			this.startDate = startDate;
			this.eventCode = eventCode;
			this.amounts = amounts;
		}

		/**
		 * @return the reference
		 */
		public String getReference() {
			return reference;
		}

		/**
		 * @param reference the reference to set
		 */
		public void setReference(String reference) {
			this.reference = reference;
		}

		/**
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}

		/**
		 * @param desc the desc to set
		 */
		public void setDesc(String desc) {
			this.desc = desc;
		}

		/**
		 * @return the startDate
		 */
		public Date getStartDate() {
			return startDate;
		}

		/**
		 * @param startDate the startDate to set
		 */
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		/**
		 * @return the eventCode
		 */
		public String getEventCode() {
			return eventCode;
		}

		/**
		 * @param eventCode the eventCode to set
		 */
		public void setEventCode(String eventCode) {
			this.eventCode = eventCode;
		}

		/**
		 * @return the amounts
		 */
		public List<BigDecimal> getAmounts() {
			return amounts;
		}

		/**
		 * @param amounts the amounts to set
		 */
		public void setAmounts(List<BigDecimal> amounts) {
			this.amounts = amounts;
		}
		
		/**
		 * @param amount
		 */
		public void addAmount(BigDecimal amount) {
			if (this.amounts == null) {
				this.amounts = new ArrayList<>();
			}
			this.amounts.add(amount);
		}
		
		/**
		 * @param index
		 * @param amount
		 */
		public void addAmount(int index, BigDecimal amount) {
			amounts.set(index, amounts.get(index).add(amount));
		}
	}
}
