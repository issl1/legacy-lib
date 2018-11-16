package com.nokor.efinance.gui.ui.panel.contract;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;

/**
 * 
 * @author uhout.cheng
 */
public class ReceivedDateRenderer extends EntityColumnRenderer implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -803771443600546208L;

	/** 
	 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
	 */
	@Override
	public Object getValue() {
		Contract contract = (Contract) getEntity();
		ContractUserInbox usrInbox = INBOX_SRV.getContractUserInboxed(contract.getId(), 
				new String[] { IProfileCode.CMSTAFF, IProfileCode.CMLEADE });
		if (usrInbox != null && usrInbox.getSecUser() != null) {
			return usrInbox.getCreateDate();
		}
		return null;
	}

}
