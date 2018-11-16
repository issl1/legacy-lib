package org.seuksa.frmk.tools.exception;

import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.ERefDataHelper;

/**
 * @author prasnar
 * @version $Revision$
 */
public enum ErrorCode implements RefDataId {
 	ERR_NONE(0, "None"),
    ERR_SYS(1, "System"),
    ERR_SYS_TIMEOUT(2, "Timeout"),
    ERR_INCORRECT_PARAMETERS(3, "Incorrect parameters");

    private final long id;
	private final String code;

	
	/**
     * 
     */
	private ErrorCode(final long id, final String code) {
		this.id = id;
		this.code = code;
	}


	/**
	 * return code
	 */
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public String getCode() {
		return code;
	}  

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return ERefDataHelper.getDescI18N(this, false);
	}


	@Override
	public String getDescEn() {
		return ERefDataHelper.getDescI18N(this, true);
	}

	

	  
}