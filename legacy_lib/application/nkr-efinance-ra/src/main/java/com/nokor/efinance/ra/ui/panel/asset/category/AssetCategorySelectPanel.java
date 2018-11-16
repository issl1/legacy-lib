package com.nokor.efinance.ra.ui.panel.asset.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.service.AssetCategoryRestriction;
import com.nokor.efinance.core.dealer.model.DealerAssetCategory;
import com.nokor.efinance.core.financial.model.CampaignAssetCategory;
import com.nokor.efinance.core.financial.model.MCampaignAssetCategory;
import com.nokor.efinance.core.helper.FinServicesHelper;
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
 * Asset Category Select Panel
 * @author bunlong.taing
 */
public class AssetCategorySelectPanel extends Window implements FinServicesHelper, ClickListener, MCampaignAssetCategory {
	
	/**
	 */
	private static final long serialVersionUID = -3424742022337931033L;

	private static final String SELECT = "select";
	
	private TextField txtSearchText;
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	private SimpleTable<AssetCategory> simpleTable;
	
	private Listener selectListener = null;
	
	private Long campaignId;
	private Long dealerId;

	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @param dealerId the dealerId to set
	 */
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public interface Listener extends Serializable {
        void onClose(AssetCategorySelectPanel dialog);
    }
	
	/**
	 * Show a modal ConfirmDialog in a window.
	 * @param selectListener
	 */
    public void show(final Listener selectListener) {    	
    	this.selectListener = selectListener;
    	UI.getCurrent().addWindow(this);
    }
	
	/**
	 * 
	 */
	public AssetCategorySelectPanel() {
		setCaption(I18N.message("asset.categories"));
		center();
		setModal(true);
		setReadOnly(false);
		
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
		Label lblSearchText = ComponentFactory.getLabel("search.text");
		txtSearchText = ComponentFactory.getTextField(100, 200);
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(this);
		btnReset = ComponentFactory.getButton("reset");
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		GridLayout gridLayout = new GridLayout(2, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblSearchText);
		gridLayout.addComponent(txtSearchText);
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
		
		// Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
                if (selectListener != null) {
                    selectListener.onClose(AssetCategorySelectPanel.this);
                }
                UI.getCurrent().removeWindow(AssetCategorySelectPanel.this);
            }
        };
		
        btnSelect.addClickListener(cb);
		
		createTable();
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(btnSelect);
		content.addComponent(simpleTable);
		return content;
	}
	
	/**
	 */
	private void createTable() {
		simpleTable = new SimpleTable<AssetCategory>(createColumnDefinition());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(AssetCategory.ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AssetCategory.DESC, I18N.message("name"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * @param assetCategories
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<AssetCategory> assetCategories) {
		if (assetCategories != null) {
			for (AssetCategory assetCategory : assetCategories) {
				Item item = simpleTable.addItem(assetCategory.getId());
				item.getItemProperty(SELECT).setValue(new CheckBox());
				item.getItemProperty(AssetCategory.ID).setValue(assetCategory.getId());
				item.getItemProperty(AssetCategory.DESC).setValue(assetCategory.getDescLocale());
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtSearchText.setValue("");
		simpleTable.removeAllItems();
	}
	
	/**
	 */
	private void search() {
		simpleTable.removeAllItems();
		setTableDataSource(ENTITY_SRV.list(getRestrictions()));
	}

	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<AssetCategory> getRestrictions() {
		String DOT = ".";
		String CAM_ASS_CAT = "camasscat";
		String DEA_ASS_CAT = "deaasscat";
		
		AssetCategoryRestriction restrictions = new AssetCategoryRestriction();
		restrictions.setSearchText(txtSearchText.getValue());
		if (campaignId != null) {
			DetachedCriteria camAssCatSubCriteria = DetachedCriteria.forClass(CampaignAssetCategory.class, CAM_ASS_CAT);
			camAssCatSubCriteria.add(Restrictions.eq(CAM_ASS_CAT + DOT + CAMPAIGN + DOT + ID, campaignId));
			camAssCatSubCriteria.setProjection(Projections.projectionList().add(Projections.property(CAM_ASS_CAT + DOT + ASSETCATEGORY + DOT + ID)));
			restrictions.addCriterion(Property.forName(ID).notIn(camAssCatSubCriteria));
		} else if (dealerId != null) {
			DetachedCriteria deaAssCatSubCriteria = DetachedCriteria.forClass(DealerAssetCategory.class, DEA_ASS_CAT);
			deaAssCatSubCriteria.add(Restrictions.eq(DEA_ASS_CAT + DOT + DealerAssetCategory.DEALER + DOT + ID, dealerId));
			deaAssCatSubCriteria.setProjection(Projections.projectionList().add(Projections.property(DEA_ASS_CAT + DOT + DealerAssetCategory.DEALER + DOT + ID)));
			restrictions.addCriterion(Property.forName(ID).notIn(deaAssCatSubCriteria));
		}
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			search();
		} else if (event.getButton() == btnReset) {
			txtSearchText.setValue("");
		}
	}
	
	/**
	 * @return
	 */
	public List<Long> getSelectedIds() {
		List<Long> ids = new ArrayList<>();
		for (Object i : simpleTable.getItemIds()) {
		    Long id = (Long) i;
		    Item item = simpleTable.getItem(id);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(id);
		    }
		}
		return ids;
	}
}
