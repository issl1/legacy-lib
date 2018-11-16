package com.nokor.efinance.core.shared.payment;

import com.nokor.efinance.core.shared.FMEntityField;

/**
 * @author ly.youhort
 */
public interface PaymentEntityField extends FMEntityField {

	String INTERNAL_CODE = "Code";
	String EXTERNAL_CODE = "externalCode";
	
	String INSTALLMENT_DATE = "installmentDate";
}
