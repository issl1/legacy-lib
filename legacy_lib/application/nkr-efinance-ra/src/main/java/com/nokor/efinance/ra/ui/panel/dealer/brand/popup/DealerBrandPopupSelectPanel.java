package com.nokor.efinance.ra.ui.panel.dealer.brand.popup;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.service.AssetMakeRestriction;
import com.nokor.efinance.core.dealer.model.DealerAssetMake;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Dealer brand pop up Select Panel
 * @author uhout.cheng
 */
public class DealerBrandPopupSelectPanel extends Window implements FinServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = 2398791550483015563L;

	private static final String SELECT = "select";
	
	private TextField txtSearchBrandName;
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	private SimpleTable<Entity> simpleTable;
	private SelectListener selectListener;
	
	/**
	 * 
	 */
	public DealerBrandPopupSelectPanel() {
		setCaption(I18N.message("brands"));
		setModal(true);
		setReadOnly(false);
		setWidth(780, Unit.PIXELS);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(createSearchPanel());
		content.addComponent(createForm());
		setContent(content);
	}
	
	/**
	 * @return
	 */
	private Component createSearchPanel() {
		Label lblSearchText = ComponentLayoutFactory.getLabelCaption("brand.name");
		txtSearchBrandName = ComponentFactory.getTextField(100, 200);
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.addClickListener(this);
		btnSearch.setClickShortcut(KeyCode.ENTER, null); 
		btnReset = ComponentFactory.getButton("reset");
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		GridLayout gridLayout = new GridLayout(2, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblSearchText);
		gridLayout.addComponent(txtSearchBrandName);
		gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnSearch);
		horizontalLayout.addComponent(btnReset);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(gridLayout);
		content.addComponent(horizontalLayout);
		content.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel(I18N.message("search"));
		panel.setContent(content);
		return panel;
	}
	
	/**
	 * @return
	 */
	private Component createForm() {
		btnSelect = ComponentFactory.getButton("select");
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);
		btnSelect.addClickListener(this);
		createTable();
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(btnSelect);
		content.addComponent(simpleTable);
		
		return content;
	}
	
	/**
	 * 
	 */
	private void createTable() {
		simpleTable = new SimpleTable<Entity>(createColumnDefinition());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ASSETMAKE + DealerAssetMake.ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ASSETMAKE, I18N.message("brand.name"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ASSETCATEGORY + DealerAssetMake.ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ASSETCATEGORY, I18N.message("asset.category"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * @param brands
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<AssetMake> brands) {
		simpleTable.removeAllItems();
		if (brands != null && !brands.isEmpty()) {
			int i = 0;
			for (AssetMake brand : brands) {
				Item item = null;
				List<AssetCategory> categories = ASS_SRV.list(new BaseRestrictions<AssetCategory>(AssetCategory.class));
				if (categories != null && !categories.isEmpty()) {
					for (AssetCategory category : categories) {
						item = simpleTable.addItem(i);
						item.getItemProperty(SELECT).setValue(new CheckBox());
						item.getItemProperty(DealerAssetMake.ASSETMAKE + DealerAssetMake.ID).setValue(brand.getId());
						item.getItemProperty(DealerAssetMake.ASSETMAKE).setValue(brand.getDescLocale());
						item.getItemProperty(DealerAssetMake.ASSETCATEGORY + DealerAssetMake.ID).setValue(category.getId());
						item.getItemProperty(DealerAssetMake.ASSETCATEGORY).setValue(category.getDescLocale());
						i++;
					}
				} else {
					item = simpleTable.addItem(i);
					item.getItemProperty(SELECT).setValue(new CheckBox());
					item.getItemProperty(DealerAssetMake.ASSETMAKE + DealerAssetMake.ID).setValue(brand.getId());
					item.getItemProperty(DealerAssetMake.ASSETMAKE).setValue(brand.getDescLocale());
					i++;
				}
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtSearchBrandName.setValue("");
		simpleTable.removeAllItems();
	}
	
	/**
	 */
	private void search() {
		AssetMakeRestriction restriction = new AssetMakeRestriction();
		restriction.setSearchText(txtSearchBrandName.getValue());
		setTableDataSource(ENTITY_SRV.list(restriction));
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			search();
		} else if (event.getButton() == btnReset) {
			txtSearchBrandName.setValue("");
		} else if (event.getButton() == btnSelect) {
			select();
		}
	}
	
	/**
	 * 
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 * 
	 */
	private void select() {
		if (selectListener != null) {
			List<Long> brandIds = new ArrayList<Long>();
			List<Long> categoryIds = new ArrayList<Long>();
			for (Object i : simpleTable.getItemIds()) {
			    Item item = simpleTable.getItem(i);
			    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
			    if (cbSelect.getValue()) {
			    	Long brandId = (Long) item.getItemProperty(DealerAssetMake.ASSETMAKE + DealerAssetMake.ID).getValue();
			    	Long categoryId = (Long) item.getItemProperty(DealerAssetMake.ASSETCATEGORY + DealerAssetMake.ID).getValue();
			    	brandIds.add(brandId);
			    	if (categoryId != null) {
			    		categoryIds.add(categoryId);
			    	}
			    }
			}
			selectListener.onSelected(brandIds, categoryIds);
			close();
		}
	}

	/**
	 * @param selectListener the selectListener to set
	 */
	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}

	/**
	 * 
	 * @author uhout.cheng
	 */
	public interface SelectListener {
		
		/**
		 * 
		 * @param selectedBrandIds
		 * @param selectedCategoryIds
		 */
		void onSelected(List<Long> selectedBrandIds, List<Long> selectedCategoryIds);
	}

}
