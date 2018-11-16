package com.nokor.efinance.gui.ui.panel.contract.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetStockTabPanel extends AbstractControlPanel implements MAsset {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2488384777346921489L;

	private Label lblDeliveryStaffIDValue;
	private Label lblDeliveryStaffFullnameValue;
	private Label lblDeliveryStaffPhoneValue;
	
	private Label lblWharehouseValue;
	private Label lblAreaValue;
	private Label lblItemIDValue;
	private Label lblItemStatusValue;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Entity> simplePagedTable;
	
	public AssetStockTabPanel() {
		setMargin(true);
		setSpacing(true);
		
		init();
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponent(createDeliveryDetailPanel());
		vLayout.addComponent(createInventoryDetailPanel());
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("info"));
		fieldSet.setContent(vLayout);
		
		Panel mainPanel = new Panel(fieldSet);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(mainPanel);
	}
	
	/**
	 * init
	 */
	private void init() {
		
		lblDeliveryStaffIDValue = getLabelValue();
		lblDeliveryStaffFullnameValue = getLabelValue();
		lblDeliveryStaffPhoneValue = getLabelValue();
		
		lblWharehouseValue = getLabelValue();
		lblAreaValue = getLabelValue();
		lblItemIDValue = getLabelValue();
		lblItemStatusValue = getLabelValue();
		
		this.columnDefinitions = createColumnDefinitions();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
	}
	
	/**
	 * assignValue
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createDeliveryDetailPanel() {
		GridLayout deliveryGridLayout = ComponentLayoutFactory.getGridLayout(6, 1);
		deliveryGridLayout.setSpacing(true);
		
		Label lblStaffID = getLabel("staff.id");
		Label lblStaffFullname = getLabel("staff.fullname");
		Label lblStaffPhone = getLabel("staff.phone");
		
		int iCol = 0;
		deliveryGridLayout.addComponent(lblStaffID, iCol++, 0);
		deliveryGridLayout.addComponent(lblDeliveryStaffIDValue, iCol++, 0);
		deliveryGridLayout.addComponent(lblStaffFullname, iCol++, 0);
		deliveryGridLayout.addComponent(lblDeliveryStaffFullnameValue, iCol++, 0);
		deliveryGridLayout.addComponent(lblStaffPhone, iCol++, 0);
		deliveryGridLayout.addComponent(lblDeliveryStaffPhoneValue, iCol++, 0);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("delivery.detail"));
		fieldSet.setContent(deliveryGridLayout);
		
		Panel mainPanel = new Panel(fieldSet);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return new VerticalLayout(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createInventoryDetailPanel() {
		GridLayout inventoryGridLayout = new GridLayout(8, 1);
		inventoryGridLayout.setSpacing(true);
		
		Label lblWharehouse = getLabel("wharehouse");
		Label lblArea = getLabel("area");
		Label lblItemID = getLabel("item.id");
		Label lblItemStaus = getLabel("item.status");
		
		int iCol = 0;
		inventoryGridLayout.addComponent(lblWharehouse, iCol++, 0);
		inventoryGridLayout.addComponent(lblWharehouseValue, iCol++, 0);
		inventoryGridLayout.addComponent(lblArea, iCol++, 0);
		inventoryGridLayout.addComponent(lblAreaValue, iCol++, 0);
		inventoryGridLayout.addComponent(lblItemID, iCol++, 0);
		inventoryGridLayout.addComponent(lblItemIDValue, iCol++, 0);
		inventoryGridLayout.addComponent(lblItemStaus, iCol++, 0);
		inventoryGridLayout.addComponent(lblItemStatusValue, iCol++, 0);
		
		FieldSet currentLocationFieldSet = new FieldSet();
		currentLocationFieldSet.setLegend(I18N.message("current.location"));
		currentLocationFieldSet.setContent(inventoryGridLayout);
		
		Panel currentLocationPanel = new Panel(currentLocationFieldSet);
		currentLocationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout movementsTableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		movementsTableLayout.addComponent(simplePagedTable);
		movementsTableLayout.addComponent(simplePagedTable.createControls());
		movementsTableLayout.setWidthUndefined();
		simplePagedTable.setCaption(I18N.message("movements"));
		simplePagedTable.setPageLength(5);
		simplePagedTable.setWidthUndefined();
		
		VerticalLayout inventoryDetailLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		inventoryDetailLayout.addComponent(currentLocationPanel);
		inventoryDetailLayout.addComponent(movementsTableLayout);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("inventory.detail"));
		fieldSet.setContent(inventoryDetailLayout);
		
		Panel mainPanel = new Panel(fieldSet);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return new VerticalLayout(mainPanel);
	}
	
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MOVEMENT_TYPE, I18N.message("movement.type"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(LOCATION, I18N.message("location"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(REMARK, I18N.message("remark"), String.class, Align.LEFT, 350));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	

}
