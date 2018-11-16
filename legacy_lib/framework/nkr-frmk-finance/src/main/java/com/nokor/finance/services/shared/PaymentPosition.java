package com.nokor.finance.services.shared;

import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MyDataId;

/**
 * Payment position
 * @author ly.youhort
 *
 */
public enum PaymentPosition implements MyDataId {
	
	BEGIN_PERIOD(1, "begin.period"),
	END_PERIOD(2, "end.period");
    
	private final long id;
	private final String code;

	
	/**
     * 
     */
	private PaymentPosition(final long id, final String code) {
		this.id = id;
		this.code = code;
	}
	

	@Override
	public Long getId() {
		return id;
	}



	/**
	 * return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return I18N.value(this.getClass().getSimpleName() + "." + getCode());
	}
    
    /**
     * List of frequencies
     * @return
     */
    public static List<PaymentPosition> list() {
    	return Arrays.asList(PaymentPosition.values());
    }


    
}
