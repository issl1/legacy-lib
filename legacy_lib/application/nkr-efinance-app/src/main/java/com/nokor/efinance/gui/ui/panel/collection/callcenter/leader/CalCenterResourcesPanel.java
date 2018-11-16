package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.ResourcePanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Call Center Resource Panel
 * @author bunlong.taing
 */
public class CalCenterResourcesPanel extends AbstractTabPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -6115740329859221371L;

	private SimpleTable<SecUser> staffTable;
	
	/**
	 * 
	 */
	public CalCenterResourcesPanel() {
		setStaffTableContainerDataSource(IProfileCode.CAL_CEN_STAFF);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#init()
	 */
	@Override
	public void init() {
		super.init();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
	
		staffTable = new SimpleTable<>(createStaffTableColumnDefinition());
		staffTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = -4485326848127922446L;
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					SecUser selectedUser = (SecUser) event.getItemId();
					ResourcePanel resourcePanel = new ResourcePanel(IProfileCode.CAL_CEN_STAFF);
					resourcePanel.assignValues(selectedUser);
					UI.getCurrent().addWindow(resourcePanel);
				}
			}
		});
			
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(staffTable);		
		return verticalLayout;
	}
	
	/**
	 * Create Staff Table Column Definition
	 * @return
	 */
	private List<ColumnDefinition> createStaffTableColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SecUser.LOGIN, I18N.message("code"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(SecUser.DESC, I18N.message("name"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * Set Staff Table Container Data Source
	 * @param profileCode
	 */
	@SuppressWarnings("unchecked")
	private void setStaffTableContainerDataSource(String profileCode) {
		staffTable.removeAllItems();
		for (SecUser user : COL_SRV.getCollectionUsers(new String[] {profileCode})) {
			Item item = staffTable.addItem(user);
			item.getItemProperty(SecUser.LOGIN).setValue(user != null ? user.getLogin() : "");
			item.getItemProperty(SecUser.DESC).setValue(user != null ? user.getDesc() : "");
		}
	}
}
