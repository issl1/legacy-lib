package com.nokor.efinance.ra.ui.panel.organization.subsidiairy.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author prasnar
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubsidiairyTablePanel extends AbstractTablePanel<Organization> implements FinServicesHelper, FMEntityField {
	/** */
	private static final long serialVersionUID = -7636989881029676921L;
	
	private BaseOrganizationHolderPanel mainPanel;

	private Long parentOrganizationId;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("subsidiaries"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("subsidiaries"));	
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Organization> createPagedDataProvider() {
		PagedDefinition<Organization> pagedDefinition = new PagedDefinition<Organization>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(NAME_EN, I18N.message("name.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(NAME, I18N.message("name"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(STATUS_RECORD, I18N.message("status"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Organization> pagedDataProvider = new EntityPagedDataProvider<Organization>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Organization getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ORG_SRV.getById(Organization.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected SubsidiairySearchPanel createSearchPanel() {
		return new SubsidiairySearchPanel(this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		mainPanel.addSubsidiaryFormPanel(getParentOrganizationId(), null);
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
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
			mainPanel.addSubsidiaryFormPanel(getParentOrganizationId(), id);
		}
	}

	/**
	 * @return the getParentOrganizationId
	 */
	public Long getParentOrganizationId() {
		return parentOrganizationId;
	}

	/**
	 * @param getParentOrganizationId the getParentOrganizationId to set
	 */
	public void setParentOrganization(Long getParentOrganizationId) {
		this.parentOrganizationId = getParentOrganizationId;
	}
	
	/**
	 * @return the mainPanel
	 */
	public BaseOrganizationHolderPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(BaseOrganizationHolderPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
