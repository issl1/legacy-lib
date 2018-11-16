package com.nokor.efinance.third.finwiz.client.common;

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
import com.nokor.efinance.share.locksplit.LockSplitDTO;
import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author uhout.cheng
 */
public class ClientCallBack {
	
	protected final static Logger LOG = LoggerFactory.getLogger(ClientCallBack.class);
	
	/**
	 * @param url
	 * @param lockSplitDTO
	 */
	public static void callBackLockSplit(String url, LockSplitDTO lockSplitDTO) {
		if (ThirdAppConfigFileHelper.isFinwizEnable()) {
			Gson gson = new Gson();
			String strJson = gson.toJson(lockSplitDTO);
			LOG.info("LockSplitDTO : ***[" + strJson + "]***");
			
			ClientConfig config = new ClientConfig();
			final Client client = ClientBuilder.newClient(config);
			
			Entity<LockSplitDTO> entity = Entity.entity(lockSplitDTO, MediaType.APPLICATION_JSON);
					
			Response response = client.target(url)
					.request(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(entity, Response.class);
			
			LOG.info("Response Status: " + response.getStatus());
			if (response.getStatus() == Status.CREATED.getStatusCode()) {
				lockSplitDTO = response.readEntity(new GenericType<LockSplitDTO>() {});
				LOG.info("************POST-SUCCESS************");
				LOG.info("************REF = " + lockSplitDTO.getId() + "************");
			} else {
				String errMsg = response.readEntity(String.class);
				LOG.error("Error: " + errMsg);
			}
		}
	}
}
