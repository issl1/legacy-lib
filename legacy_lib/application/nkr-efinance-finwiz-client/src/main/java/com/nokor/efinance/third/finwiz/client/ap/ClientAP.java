package com.nokor.efinance.third.finwiz.client.ap;

import java.sql.Timestamp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gl.finwiz.share.domain.AP.PaymentOrderBatchApproval;
import com.gl.finwiz.share.domain.AP.PaymentOrderBatchApproval.ApprovalCommand;
import com.gl.finwiz.share.domain.AP.PaymentOrderBatchDTO;
import com.google.gson.Gson;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryDTO;

/**
 * 
 * @author uhout.cheng
 */
public class ClientAP {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientAP.class);
	
	private static final String PAYMENTORDERBATCHES = "/paymentOrderBatches";
	private static final String APPROVALS = "/approvals";
	
	/**
	 * 
	 * @param paymentOrderBatchDTO
	 * @return
	 */
	private static Response getPaymentOrderBatch(PaymentOrderBatchDTO paymentOrderBatchDTO) {
		ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		
		Entity<PaymentOrderBatchDTO> entity = Entity.entity(paymentOrderBatchDTO, MediaType.APPLICATION_JSON);
		
		String url = ThirdAppConfigFileHelper.getAPURL();
		
		Response result = client.target(url + PAYMENTORDERBATCHES)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity, Response.class);

		return result;
	}
	
	/**
	 * 
	 * @param orderBatchDTO
	 */
	public static PaymentOrderBatchDTO createPaymentOrderBatchs(PaymentOrderBatchDTO orderBatchDTO) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			try {	
				Gson gson = new Gson();
				String strJson = gson.toJson(orderBatchDTO);
				LOG.info("PaymentOrderBatchDTO: ***[" + strJson + "]***");
					
				Response response = getPaymentOrderBatch(orderBatchDTO);
					
				LOG.info("Response Status: " + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					LOG.info("************CREATE-SUCCESS************");
					orderBatchDTO = response.readEntity(new GenericType<PaymentOrderBatchDTO>() {});
					return orderBatchDTO;
				} else {
					String errMsg = response.readEntity(String.class);
					LOG.error("Error: " + errMsg);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}	
	
	/**
	 * @param url
	 * @param transactionEntryDTO
	 */
	public static void confirmTransaction(String url, TransactionEntryDTO transactionEntryDTO) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			Gson gson = new Gson();
			String strJson = gson.toJson(transactionEntryDTO);
			LOG.info("TransactionEntryDTO : ***[" + strJson + "]***");
			
			ClientConfig config = new ClientConfig();
			final Client client = ClientBuilder.newClient(config);
			
			Entity<TransactionEntryDTO> entity = Entity.entity(transactionEntryDTO, MediaType.APPLICATION_JSON);
					
			Response response = client.target(url)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(entity, Response.class);
			
			LOG.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.CREATED.getStatusCode()) {
				transactionEntryDTO = response.readEntity(new GenericType<TransactionEntryDTO>() {});
				LOG.info("************POST-SUCCESS************");
				LOG.info("************REF = " + transactionEntryDTO.getId() + "************");
			} else {
				String errMsg = response.readEntity(String.class);
				LOG.error("Error: " + errMsg);
			}
		}
	}
	
	/**
	 * @param paymentOrderBatchId
	 */
	public static void approvePaymentOrderBatch(String createdBy, Long paymentOrderBatchId, boolean isPreApprove) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			
			ClientConfig config = new ClientConfig();
			final Client client = ClientBuilder.newClient(config);
			
			String url = ThirdAppConfigFileHelper.getAPURL();
			
			PaymentOrderBatchApproval paymentOrderBatchApproval = new PaymentOrderBatchApproval();
			if (isPreApprove) {
				paymentOrderBatchApproval.setApprovalCommand(ApprovalCommand.SOURCE_PRE_APPROVE);
			} else {
				paymentOrderBatchApproval.setApprovalCommand(ApprovalCommand.SOURCE_APPROVE);
			}
			paymentOrderBatchApproval.setCreatedBy(createdBy);
			paymentOrderBatchApproval.setCreatedDate(new Timestamp(DateUtils.todayDate().getTime()));
			
			Entity<PaymentOrderBatchApproval> entity = Entity.entity(paymentOrderBatchApproval, MediaType.APPLICATION_JSON);
			
			Response response = client.target(url + PAYMENTORDERBATCHES + "/" + paymentOrderBatchId + APPROVALS)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(entity, Response.class);
			
			LOG.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.CREATED.getStatusCode()) {				
				LOG.info("************POST-SUCCESS************");
			} else {
				String errMsg = response.readEntity(String.class);
				LOG.error("Error: " + errMsg);
			}
		}
	}
}
