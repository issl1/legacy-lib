package com.nokor.efinance.core.workflow;

import com.nokor.common.app.history.model.EHistoReason;

/**
 * 
 * @author prasnar
 *
 */
public class ContractHistoReason  {
	public final static EHistoReason CONTRACT_0001 = new EHistoReason("CONTRACT_0001", 1); // contract.financed
	public final static EHistoReason CONTRACT_0002 = new EHistoReason("CONTRACT_0002", 2); // contract.closed
	public final static EHistoReason CONTRACT_0003 = new EHistoReason("CONTRACT_0003", 3); // contract.early.settlement
	public final static EHistoReason CONTRACT_LOSS = new EHistoReason("CONTRACT_LOSS", 4); // contract.loss
	public final static EHistoReason CONTRACT_REP = new EHistoReason("CONTRACT_REP", 5); // contract.repossessed
	public final static EHistoReason CONTRACT_THE = new EHistoReason("CONTRACT_THE", 6); // contract.theft
	public final static EHistoReason CONTRACT_ACC = new EHistoReason("CONTRACT_ACC", 7); // contract.accident
	public final static EHistoReason CONTRACT_FRA = new EHistoReason("CONTRACT_FRA", 8); // contract.fraud
	public final static EHistoReason CONTRACT_WRI = new EHistoReason("CONTRACT_WRI", 9); // contract.write.off
	public final static EHistoReason CONTRACT_BACK_FIN = new EHistoReason("CONTRACT_BACK_FIN", 10); // .back.financed
	public final static EHistoReason CONTRACT_SIMUL_EARLY_SETL = new EHistoReason("CONTRACT_SIMUL_EARLY_SETL", 10); // contract simulation early settlement	
}
