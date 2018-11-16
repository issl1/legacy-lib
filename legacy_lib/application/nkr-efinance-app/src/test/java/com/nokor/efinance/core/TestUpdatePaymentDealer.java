package com.nokor.efinance.core;

import java.util.List;

import org.junit.Test;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.testing.BaseTestCase;

public class TestUpdatePaymentDealer extends BaseTestCase {
	
	@Test
	public void test() {
		try {
			BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
			List<Payment> payments = ENTITY_SRV.list(restrictions);
			if (payments != null & !payments.isEmpty()) {
				for (Payment payment : payments) {
					if (payment.getDealer() == null) {
						Cashflow cashflowFirstIndex = payment.getCashflows().get(0);
						Contract contract = cashflowFirstIndex.getContract();
						payment.setDealer(contract.getDealer());
						ENTITY_SRV.saveOrUpdate(payment);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
