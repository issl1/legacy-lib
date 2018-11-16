package com.nokor.common.app.tools.helper;

import com.nokor.common.app.eventlog.model.SecEventAction;
import com.nokor.frmk.helper.FrmkReferenceDataHelper;

/**
 * 
 * @author prasnar
 *
 */
public class AppReferenceDataHelper extends FrmkReferenceDataHelper {

	public static SecEventAction getSecEventActionLOGIN() {
		return ENTITY_SRV.getById(SecEventAction.class, SecEventAction.LOGIN);
	}
	public static SecEventAction getSecEventActionLOGOUT() {
		return ENTITY_SRV.getById(SecEventAction.class, SecEventAction.LOGOUT);
	}

}
