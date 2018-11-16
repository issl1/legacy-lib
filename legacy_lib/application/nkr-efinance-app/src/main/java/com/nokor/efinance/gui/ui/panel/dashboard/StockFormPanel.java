package com.nokor.efinance.gui.ui.panel.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.stock.model.ProductStock;
import com.nokor.efinance.core.stock.service.StockService;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ButtonFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.nokor.frmk.vaadin.util.exporter.ExcelExporter;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StockFormPanel.NAME)
public class StockFormPanel extends AbstractTabPanel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	public static final String NAME = "stock";
	
	@Autowired
	private StockService stockService;
	
	private Button btnSave;
	private Button btnExcel;
	private SimpleTable<DealerStock> pagedTable; 
	
	/**
	 * @param secUserDetail
	 */
	@PostConstruct
	public void PostConstruct() {		
		assignValues();
	}
	
	/**
	 * @return
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		TabSheet tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		pagedTable = new SimpleTable<>(createColumnDefinitions());
				
		btnExcel = new NativeButton();
		btnExcel.setCaption(I18N.message("excel"));
		btnExcel.setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
		btnExcel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -345128294252780849L;
			public void buttonClick(ClickEvent event) {
	        	final ExcelExporter excelExp = new ExcelExporter();
	            excelExp.setContainerToBeExported(pagedTable.getContainerDataSource());
	            excelExp.sendConvertedFileToUser(getUI());
	        }
	    });
				
		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		
		//---- button save is not added to form if Senior CO log in ---
		if (!ProfileUtil.isSeniorCO()) {
			btnSave = ButtonFactory.getSaveButton();

			tblButtonsPanel.addButton(btnSave);

			btnSave.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1717161084451001316L;

				@Override
				public void buttonClick(ClickEvent event) {
					saveStock();
				}
			});
		}
		tblButtonsPanel.addButton(btnExcel);
		
		contentLayout.addComponent(tblButtonsPanel, 0);
		contentLayout.addComponent(pagedTable);
		
		tabSheet.addTab(contentLayout, I18N.message("stock"));
		
		return tabSheet;
	}
	
	/**
	 * Create columns definition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("dealer.type", I18N.message("dealer.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("dealer", I18N.message("dealer"), String.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("stock.old.leaflet", I18N.message("stock.old.leaflet"), NumberField.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition("stock.new.leaflet", I18N.message("stock.new.leaflet"), NumberField.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition("stock.current.tshirt", I18N.message("stock.current.tshirt"), NumberField.class, Align.RIGHT, 130));
		return columnDefinitions;
	}
	
	@SuppressWarnings("unchecked")
	public void setContainerDataSource(List<DealerStock> dealerStocks) {
		pagedTable.setPageLength(15);
		Container indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (DealerStock dealerStock : dealerStocks) {			
			Item item = indexedContainer.addItem(dealerStock.getId());
			item.getItemProperty("id").setValue(dealerStock.getId());
			item.getItemProperty("dealer.type").setValue(dealerStock.getDealerType());
			item.getItemProperty("dealer").setValue(dealerStock.getDealerName());
			NumberField txtStockOldLeaflet = ComponentFactory.getNumberField();
			NumberField txtStockNewLeaflet = ComponentFactory.getNumberField();
			NumberField txtStockCurrentTShirt = ComponentFactory.getNumberField();

			
			txtStockOldLeaflet.setValue("" + dealerStock.getStockOldLeaflet());
			txtStockNewLeaflet.setValue("" + dealerStock.getStockNewLeaflet());
			txtStockCurrentTShirt.setValue("" + dealerStock.getStockCurrentTshirt());
			
			item.getItemProperty("stock.old.leaflet").setValue(txtStockOldLeaflet);
			item.getItemProperty("stock.new.leaflet").setValue(txtStockNewLeaflet);
			item.getItemProperty("stock.current.tshirt").setValue(txtStockCurrentTShirt);
			
			//--- disable if log in as Senior CO
			if (ProfileUtil.isSeniorCO()) {
				txtStockOldLeaflet.setEnabled(false);
				txtStockNewLeaflet.setEnabled(false);
				txtStockCurrentTShirt.setEnabled(false);
			}
		}
	}
	
	/**	
	 */
	private void saveStock() {
		List<Dealer> dealers = DataReference.getInstance().getDealers();
		for (Dealer dealer : dealers) {
			Item item = pagedTable.getItem(dealer.getId());
			NumberField stockOldLeaflet = (NumberField) item.getItemProperty("stock.old.leaflet").getValue();
			NumberField stockNewLeaflet = (NumberField) item.getItemProperty("stock.new.leaflet").getValue();
			NumberField stockCurrentTShirt = (NumberField) item.getItemProperty("stock.current.tshirt").getValue();			
			stockService.saveStock(dealer, "LEAFL", getInteger(stockOldLeaflet), getInteger(stockNewLeaflet));
			stockService.saveStock(dealer, "TSHIRT", 0, getInteger(stockCurrentTShirt));
		}
		displaySuccess();
	}
	
	private void assignValues() {
		List<Dealer> dealers = DataReference.getInstance().getDealers();
		List<DealerStock> dealerStocks = new ArrayList<>();
		for (Dealer dealer : dealers) {
			DealerStock dealerStock = new DealerStock();
			dealerStock.setId(dealer.getId());
			dealerStock.setDealerName(dealer.getNameEn());
			dealerStock.setDealerType(dealer.getDealerType().getDesc());
			ProductStock productStockLeaflet = stockService.getProductStock(dealer, "LEAFL");
			if (productStockLeaflet != null) {
				dealerStock.setStockOldLeaflet(productStockLeaflet.getInitialQty());
				dealerStock.setStockNewLeaflet(productStockLeaflet.getQty());
			}
			ProductStock productStockTShirt = stockService.getProductStock(dealer, "TSHIRT");
			if (productStockTShirt != null) {
				dealerStock.setStockCurrentTshirt(productStockTShirt.getQty());
			}
			dealerStocks.add(dealerStock);
		}
		setContainerDataSource(dealerStocks);
	}
		
	@Override
	public void enter(ViewChangeEvent event) {		
	}
	
	/**
	 * @author ly.youhort
	 */
	private class DealerStock implements Serializable, Entity {
		private static final long serialVersionUID = -7538992672538026064L;
		
		private Long id;
		private String dealerName;
		private String dealerType;
		private int stockOldLeaflet;
		private int stockNewLeaflet;
		private int stockCurrentTshirt;
		
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * @return the dealerName
		 */
		public String getDealerName() {
			return dealerName;
		}
		/**
		 * @param dealerName the dealerName to set
		 */
		public void setDealerName(String dealerName) {
			this.dealerName = dealerName;
		}		
		/**
		 * @return the stockOldLeaflet
		 */
		public int getStockOldLeaflet() {
			return stockOldLeaflet;
		}
		/**
		 * @param stockOldLeaflet the stockOldLeaflet to set
		 */
		public void setStockOldLeaflet(int stockOldLeaflet) {
			this.stockOldLeaflet = stockOldLeaflet;
		}		
		/**
		 * @return the stockNewLeaflet
		 */
		public int getStockNewLeaflet() {
			return stockNewLeaflet;
		}
		/**
		 * @param stockNewLeaflet the stockNewLeaflet to set
		 */
		public void setStockNewLeaflet(int stockNewLeaflet) {
			this.stockNewLeaflet = stockNewLeaflet;
		}
		/**
		 * @return the stockCurrentTshirt
		 */
		public int getStockCurrentTshirt() {
			return stockCurrentTshirt;
		}
		/**
		 * @param stockCurrentTshirt the stockCurrentTshirt to set
		 */
		public void setStockCurrentTshirt(int stockCurrentTshirt) {
			this.stockCurrentTshirt = stockCurrentTshirt;
		}
		
		/**
		 * @return the dealerType
		 */
		public String getDealerType() {
			return dealerType;
		}
		/**
		 * @param dealerType the dealerType to set
		 */
		public void setDealerType(String dealerType) {
			this.dealerType = dealerType;
		}		
	}
}
