package com.nokor.efinance.ra.ui.panel.asset.range.popup;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.asset.service.AssetRangeRestriction;
import com.nokor.efinance.core.financial.model.CampaignAssetRange;
import com.nokor.efinance.core.financial.model.MCampaignAssetRange;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
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
 * Asset Range Popup Select Panel
 * @author bunlong.taing
 */
public class AssetRangePopupSelectPanel extends Window implements ClickListener, FinServicesHelper, MCampaignAssetRange {
	/** */
	private static final long serialVersionUID = -8208712092029154627L;
	
	private static final String SELECT = "select";
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	private TextField txtSearchText;
	private EntityComboBox<AssetMake> cbxAssetMake;
	
	private SimpleTable<AssetRange> simpleTable;
	private SelectListener selectListener;
	
	private Long entityId;
	
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
	/**
	 */
	public AssetRangePopupSelectPanel() {
		setCaption(I18N.message("asset.ranges"));
		setModal(true);
		setReadOnly(false);
		setWidth(780, Unit.PIXELS);
		setHeight(520, Unit.PIXELS);
		
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
		Label lblBranch = ComponentFactory.getLabel("brand");
		txtSearchText = ComponentFactory.getTextField(100, 200);
		cbxAssetMake = new EntityComboBox<AssetMake>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetMake.renderer();
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(this);
		btnReset = ComponentFactory.getButton("reset");
		btnReset.setIcon(FontAwesome.ERASER);
		btnReset.addClickListener(this);
		
		GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblSearchText);
		gridLayout.addComponent(txtSearchText);
		gridLayout.addComponent(lblBranch);
		gridLayout.addComponent(cbxAssetMake);
		
		gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblBranch, Alignment.MIDDLE_LEFT);
		
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
	 */
	private void createTable() {
		simpleTable = new SimpleTable<AssetRange>(createColumnDefinition());
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(AssetRange.ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AssetRange.DESCLOCAL, I18N.message("model.name"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(AssetRange.ASSETMAKE + "." + AssetMake.DESCLOCALE, I18N.message("brand.name"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * @param brands
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<AssetRange> models) {
		if (models != null) {
			for (AssetRange model : models) {
				CheckBox cbSelect = new CheckBox();
				cbSelect.setValue(true);
				
				Item item = simpleTable.addItem(model.getId());
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty(AssetRange.ID).setValue(model.getId());
				item.getItemProperty(AssetRange.DESCLOCAL).setValue(model.getDescLocale());
				item.getItemProperty(AssetRange.ASSETMAKE + "." + AssetMake.DESCLOCALE).setValue(model.getAssetMake() != null ? model.getAssetMake().getDescLocale() : "");
			}
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtSearchText.setValue("");
		cbxAssetMake.setSelectedEntity(null);
		simpleTable.removeAllItems();
	}
	
	/**
	 */
	private void search() {
		simpleTable.removeAllItems();
		AssetRangeRestriction restriction = new AssetRangeRestriction();
		restriction.setSearchText(txtSearchText.getValue());
		restriction.setAssetMake(cbxAssetMake.getSelectedEntity());
		setTableDataSource(ENTITY_SRV.list(getRestrictions()));
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<AssetRange> getRestrictions() {
		String DOT = ".";
		String CAM_ASS_RANGE = "camassrange";
	
		AssetRangeRestriction restrictions = new AssetRangeRestriction();
		restrictions.setSearchText(txtSearchText.getValue());
		restrictions.setAssetMake(cbxAssetMake.getSelectedEntity());
		if (entityId != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(CampaignAssetRange.class, CAM_ASS_RANGE);
			subCriteria.add(Restrictions.eq(CAM_ASS_RANGE + DOT + CAMPAIGN + DOT + ID, entityId));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property(CAM_ASS_RANGE + DOT + ASSETRANGE + DOT + ID)));
			restrictions.addCriterion(Property.forName(ID).notIn(subCriteria));
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
			cbxAssetMake.setSelectedEntity(null);
		} else if (event.getButton() == btnSelect) {
			select();
		}
	}
	
	/**
	 * Show the Pop up
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 */
	private void select() {
		if (selectListener != null) {
			selectListener.onSelected(getSelectedIds());
			close();
		}
	}
	
	/**
	 * @return
	 */
	private List<Long> getSelectedIds() {
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
	
	/**
	 * @param selectListener the selectListener to set
	 */
	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}
	
	/**
	 * @author bunlong.taing
	 */
	public interface SelectListener {
		/** */
		void onSelected(List<Long> selectedIds);
	}

}
