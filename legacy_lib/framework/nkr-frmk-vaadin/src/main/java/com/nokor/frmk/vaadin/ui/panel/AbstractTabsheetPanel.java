package com.nokor.frmk.vaadin.ui.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract panel for entity
 * 
 * @author ly.youhort
 *
 */
public abstract class AbstractTabsheetPanel extends VerticalLayout implements AppServicesHelper {
	private static final long serialVersionUID = -5095697526484655321L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	/**
	 * Build the Tabsheet panel
	 */
	protected void init() {
		tabSheet = initTabSheet();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
				
		verticalLayout.addComponent(tabSheet);
		verticalLayout.setExpandRatio(tabSheet, 1.0f);
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1f);
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			/**	 */
			private static final long serialVersionUID = 6121447787271589219L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component selectedTab = event.getTabSheet().getSelectedTab();
				if (selectedTab != tabSheet.getTablePanel()) {
					if (tabSheet.getTablePanel() != null) {
						Item selectedItem = ((SelectedItem) tabSheet.getTablePanel()).getSelectedItem();
						if (selectedItem == null && !tabSheet.isAdd() && !tabSheet.isForceSelected()) {
							tabSheet.setSelectedTab(0);
							
							MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
									MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
									new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
							return;
						}
					}
				} else {
					tabSheet.removeFormsPanel();
					initSelectedTab(selectedTab);
				}
			}
		});
	}
	
	protected TabSheet initTabSheet() {
		return new TabSheet();
	}
	
	/**
	 * Get tabSheet
	 * @return
	 */
	public TabSheet getTabSheet() {
		return tabSheet;
	}
	
	
	public abstract void onAddEventClick();
	public abstract void onEditEventClick();
	public abstract void initSelectedTab(Component selectedTab);

}


