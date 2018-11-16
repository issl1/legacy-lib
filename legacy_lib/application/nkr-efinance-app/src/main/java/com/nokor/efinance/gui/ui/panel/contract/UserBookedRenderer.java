package com.nokor.efinance.gui.ui.panel.contract;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;


/**
 * 
 * @author uhout.cheng
 */
public class UserBookedRenderer extends EntityColumnRenderer implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6897611378133938335L;

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
	 */
	@Override
	public Object getValue() {
		final Contract contract = (Contract) getEntity();
		
		ContractUserInbox usrInbox = INBOX_SRV.getContractUserInboxed(contract.getId(), 
				new String[] { IProfileCode.CMSTAFF, IProfileCode.CMLEADE });
		if (usrInbox != null && usrInbox.getSecUser() != null) {
			return usrInbox.getSecUser().getDesc();
		}
		return StringUtils.EMPTY;
	}

}
