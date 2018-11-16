package com.nokor.efinance.gui.ui.panel.collection.oa;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OutsourceAgencyTablePanel extends AbstractTablePanel<Organization> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -3539673822853037174L;
	
	private OutsourceAgencyFormPanel formPanel;
	private TabSheet mainTab;
	
	/**
	 * @return the formPanel
	 */
	public OutsourceAgencyFormPanel getFormPanel() {
		return formPanel;
	}

	/**
	 * @param mainTab the mainTab to set
	 */
	public void setMainTab(TabSheet mainTab) {
		this.mainTab = mainTab;
	}

	/**
	 * 
	 */
	public void init() {
		setCaption(I18N.message("outsource.agents"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init(I18N.message("outsource.agents"));
		addDefaultNavigation();
		
		formPanel = new OutsourceAgencyFormPanel(this);
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Organization> createPagedDataProvider() {
		PagedDefinition<Organization> pagedDefinition = new PagedDefinition<Organization>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(NAME_EN, I18N.message("name.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(NAME, I18N.message("name"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(MOrganization.SUBTYPEORGANIZATION + "." + DESC_EN, I18N.message("type"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Organization> pagedDataProvider = new EntityPagedDataProvider<Organization>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Organization getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Organization.class, id);
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected OutsourceAgencySearchPanel createSearchPanel() {
		return new OutsourceAgencySearchPanel(this);		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		formPanel.assignValues(null);
		mainTab.addTab(formPanel);
		mainTab.setSelectedTab(formPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#editButtonClick(com.vaadin.ui.Button.ClickEvent)
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
			formPanel.assignValues(areaId);
			mainTab.addTab(formPanel);
			mainTab.setSelectedTab(formPanel);
		}
	}
}
