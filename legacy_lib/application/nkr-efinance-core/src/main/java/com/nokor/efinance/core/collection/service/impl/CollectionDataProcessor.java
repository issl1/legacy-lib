package com.nokor.efinance.core.collection.service.impl;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;

public class CollectionDataProcessor implements Runnable, FinServicesHelper {

	private int thread = 0;
	private ConcurrentLinkedDeque<Long> contractIds;
	
	public CollectionDataProcessor(int thread, ConcurrentLinkedDeque<Long> contractIds) {
		this.thread = thread;
		this.contractIds = contractIds;
	}
	
	@Override
	public void run() {
		System.out.println("Start thread = " + thread);
		Long cotraId = null;
		int processed = 0;
		while ((cotraId = contractIds.poll()) != null) {
			System.out.println("Thread " + thread + " = " + contractIds.size());
			Contract contract = CONT_SRV.getById(Contract.class, cotraId);
			CON_OTH_SRV.calculateOtherDataContract(contract);
			processed++;
			if (processed % 100 == 0) {
				CON_OTH_SRV.flush();
				CON_OTH_SRV.clear();
			}
		}
		System.out.println("End thread = " + thread);
	}

}
