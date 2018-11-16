package com.nokor.efinance.gui.ui.panel.payment.locksplit;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 *
 */
public class LockSplitTablePanel extends AbstractTablePanel<LockSplit> implements MLockSplit, FinServicesHelper {
	
	/** 
	 */
	private static final long serialVersionUID = -183829701473009115L;
	
	private Button btnExpired;

	public LockSplitTablePanel() {
		setCaption(I18N.message("locksplits"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("locksplits"));
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		
		btnExpired = new NativeButton("Run Expired");
		navigationPanel.addButton(btnExpired);
		btnExpired.addClickListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				LCK_SPL_SRV.updateLockSplitsToExpired();
			}
		});
	}		

	@Override
	protected PagedDataProvider<LockSplit> createPagedDataProvider() {
		PagedDefinition<LockSplit> pagedDefinition = new PagedDefinition<LockSplit>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(CREATEUSER, I18N.message("user.id"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PAYMENTDATE, I18N.message("due.date.from"), Date.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(EXPIRYDATE, I18N.message("due.date.to"), Date.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("paymentChannel.descEn", I18N.message("channel"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("dealer.descEn", I18N.message("dealer"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition(TOTALAMOUNT, I18N.message("total"), Double.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(STATUS + ".descEn", I18N.message("status"), String.class, Align.LEFT, 70);	
		EntityPagedDataProvider<LockSplit> pagedDataProvider = new EntityPagedDataProvider<LockSplit>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected LockSplit getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(LockSplit.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected LockSplitSearchPanel createSearchPanel() {
		return new LockSplitSearchPanel(this);		
	}
	

}
