package com.nokor.efinance.gui.ui.panel.collection;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColStaffArea;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * Area code table panel in collection
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ColAreaTablePanel extends AbstractTablePanel<Area> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 2777943503384227541L;	
	
	public void init() {
		setCaption(I18N.message("areas"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("areas"));
		addDefaultNavigation();
	}
	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Area> createPagedDataProvider() {
		PagedDefinition<Area> pagedDefinition = new PagedDefinition<Area>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new AreaRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(STREET, I18N.message("street"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("line2", I18N.message("soi"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("line1", I18N.message("moo"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(POSTAL_CODE, I18N.message("postal.code"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(PROVINCE + "." + DESC_EN, I18N.message("province"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DISTRICT + "." + DESC_EN, I18N.message("district"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(COMMUNE + "." + DESC_EN, I18N.message("subdistrict"), String.class, Align.LEFT, 150);
		
		
		pagedDefinition.addColumnDefinition("assinged", I18N.message("assigned"), String.class, Align.CENTER, 100);
		EntityPagedDataProvider<Area> pagedDataProvider = new EntityPagedDataProvider<Area>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Area getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Area.class, id);
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected ColAreaSearchPanel createSearchPanel() {
		return new ColAreaSearchPanel(this);		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		ColAreaFormPopupPanel areaFormPopupPanel = new ColAreaFormPopupPanel(this);
		areaFormPopupPanel.assignValues(null);
		UI.getCurrent().addWindow(areaFormPopupPanel);
	}
	
	/**
	 * 
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long areaId = (Long) selectedItem.getItemProperty(OrgStructure.ID).getValue();
			ColAreaFormPopupPanel areaFormPopupPanel = new ColAreaFormPopupPanel(this);
			areaFormPopupPanel.assignValues(areaId);
			UI.getCurrent().addWindow(areaFormPopupPanel);
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class AreaRowRenderer implements RowRenderer, FMEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Area area = (Area) entity;
			if (area != null) {
				item.getItemProperty(ID).setValue(area.getId());
				item.getItemProperty(CODE).setValue(area.getCode());
				item.getItemProperty(DESC).setValue(area.getDesc());
				item.getItemProperty(POSTAL_CODE).setValue(area.getPostalCode());
				item.getItemProperty(PROVINCE + "." + DESC_EN).setValue(area.getProvince() != null ? area.getProvince().getDescEn() : "");
				item.getItemProperty(DISTRICT + "." + DESC_EN).setValue(area.getDistrict() != null ? area.getDistrict().getDescEn() : "");
				item.getItemProperty(COMMUNE + "." + DESC_EN).setValue(area.getCommune() != null ? area.getCommune().getDescEn() : "");
				item.getItemProperty(STREET).setValue(area.getStreet());
				item.getItemProperty("line2").setValue(area.getLine2());
				item.getItemProperty("line1").setValue(area.getLine1());
				item.getItemProperty("assinged").setValue(isAreaAssigned(area) ? "x" : "");
			}
		}
		
		/**
		 * 
		 * @param area
		 * @return
		 */
		private boolean isAreaAssigned(Area area) {
			BaseRestrictions<EColStaffArea> restrictions = new BaseRestrictions<>(EColStaffArea.class);
			restrictions.addCriterion(Restrictions.eq("area", area));
			long colStaffAreas = ENTITY_SRV.count(restrictions);
			if (colStaffAreas > 0) {
				return true;
			}
			return false;
		}
	}
}
