package com.nokor.efinance.core.shared.cashflow;

import com.nokor.efinance.core.shared.FMEntityField;

/**
 * @author ly.youhort
 */
public interface CashflowEntityField extends FMEntityField {
	String CASHFLOW_TYPE = "cashflowType";
	String TREASURY_TYPE = "treasuryType";
	String CONTRACT = "contract";
	String NUM_INSTALLMENT = "numInstallment";
	String TI_INSTALLMENT_USD = "tiInstallmentUsd";
	String TE_INSTALLMENT_USD = "teInstallmentUsd";
	String VAT_INSTALLMENT_USD = "vatInstallmentUsd";
	String TI_PAID_USD = "tiPaidUsd";
	String TI_UNPAID_USD = "tiUnpaidUsd";
	String SERVICE = "service";
}
