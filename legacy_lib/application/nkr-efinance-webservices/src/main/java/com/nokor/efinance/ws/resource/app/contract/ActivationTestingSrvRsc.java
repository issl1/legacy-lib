package com.nokor.efinance.ws.resource.app.contract;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.share.contract.ContractDTO;


/**
 * 
 * @author uhout.cheng
 */
@Path("/contracts")
public class ActivationTestingSrvRsc extends BaseContractSrvRsc {	
	
	@POST
	@Path("/testing/activate")
	@Consumes(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	@Produces(MediaType.APPLICATION_JSON + SEMI_COLON + CHARSET_UTF8)
	public void activateTest(ContractDTO contractDTO) {
		Contract contract = CONT_SRV.getById(Contract.class, contractDTO.getId());
		Asset asset = contract.getAsset();
		asset.setTaxInvoiceNumber("TAXINV" + asset.getId());
		asset.setChassisNumber("CHASS" + asset.getId());
		asset.setEngineNumber("ENG" + asset.getId());
		contract.setFirstDueDate(contractDTO.getFirstDueDate());
		contract.setStartDate(contractDTO.getContractDate());
		CONT_ACTIVATION_SRV.complete(contract, false, false);
	}
}
