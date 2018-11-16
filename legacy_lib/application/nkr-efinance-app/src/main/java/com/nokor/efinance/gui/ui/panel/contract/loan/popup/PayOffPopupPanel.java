package com.nokor.efinance.gui.ui.panel.contract.loan.popup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract pay off pop up panel
 * @author uhout.cheng
 */
public class PayOffPopupPanel extends Window {

	/** */
	private static final long serialVersionUID = 1638439104220860646L;

	private AutoDateField dfDate;
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public PayOffPopupPanel() {
		setCaption(I18N.message("pay.off.quotes"));
		setModal(true);
		setResizable(false);
		dfDate = ComponentFactory.getAutoDateField("date", false);
		simpleTable = new SimpleTable<Entity>(getPayOffColumnDefinitions());
		simpleTable.setWidth(800, Unit.PIXELS);;
		simpleTable.setPageLength(10);
		setPayOffIndexedContainer();
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		Button btnConfirm = new NativeButton(I18N.message("confirm"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = -1745772273422135447L;

			public void buttonClick(ClickEvent event) {
				
			}
		});
		btnConfirm.setIcon(FontAwesome.USD);
	     
		Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -5013182869577014870L;

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnCancel.setIcon(FontAwesome.BAN);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnConfirm);
		navigationPanel.addButton(btnCancel);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(getVerLayout());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getVerLayout() {
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.setSpacing(true);
		horLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(dfDate, true));
		
		HorizontalLayout tableLayout = ComponentLayoutFactory.getHorizontalLayout(true, false);
		tableLayout.addComponent(simpleTable);
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(horLayout);
		verLayout.addComponent(tableLayout);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getPayOffColumnDefinitions() {
		List<ColumnDefinition> latestNotesColumnDefinitions = new ArrayList<ColumnDefinition>();
		latestNotesColumnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 130));
		latestNotesColumnDefinitions.add(new ColumnDefinition("principle", I18N.message("principle"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("interest", I18N.message("interest"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("vat", I18N.message("vat"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("total.loan.amount", I18N.message("total.loan.amount"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("discount", I18N.message("discount.title"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("other.fee", I18N.message("other.fee"), Double.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition("select", I18N.message("select"), CheckBox.class, Align.CENTER, 50));
		return latestNotesColumnDefinitions;
	}
	
	/**
	 * 
	 * @param lockSplits
	 */
	@SuppressWarnings("unchecked")
	private void setPayOffIndexedContainer() {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		Item item = indexedContainer.addItem(1);
		item.getItemProperty("date").setValue(DateUtils.today());
		item.getItemProperty("principle").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("interest").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("vat").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("total.loan.amount").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("discount").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("other.fee").setValue(MyNumberUtils.getDouble(0d));
		item.getItemProperty("select").setValue(new CheckBox());
	}
	
}
