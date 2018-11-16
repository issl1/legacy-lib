package com.nokor.efinance.ra.ui.panel.organization.branch.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.ra.ui.panel.organization.branch.detail.BranchPopupPanel;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * Branch or Department Table Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BranchTablePanel extends AbstractTablePanel<OrgStructure> implements FinServicesHelper, FMEntityField {

	/** */
	private static final long serialVersionUID = 6710220519145150159L;
	
	private Long organizationId;
	private BranchPopupPanel window;
	private EOrgLevel level;
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addDefaultNavigation();
	}
	
	/**
	 * init
	 */
	public void init(String listCaption, String detailCaption, EOrgLevel level) {
		this.level = level;
		setCaption(I18N.message(listCaption));
		super.init(I18N.message(listCaption));
		window = new BranchPopupPanel(detailCaption, this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<OrgStructure> createPagedDataProvider() {
		PagedDefinition<OrgStructure> pagedDefinition = new PagedDefinition<OrgStructure>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(OrgStructure.ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(OrgStructure.CODE, I18N.message("code"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(OrgStructure.NAMEEN, I18N.message("name.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(OrgStructure.NAME, I18N.message("name"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(OrgStructure.LEVEL + "." + DESC_EN, I18N.message("level"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(OrgStructure.TEL, I18N.message("tel"), String.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(OrgStructure.MOBILE, I18N.message("mobile"), String.class, Align.LEFT, 80);
		if (EOrgLevel.BRANCH.equals(this.getLavel())) {
			pagedDefinition.addColumnDefinition("fax", I18N.message("fax"), String.class, Align.LEFT, 80);
		}
		pagedDefinition.addColumnDefinition(OrgStructure.EMAIL, I18N.message("email"), String.class, Align.LEFT, 150);
		
		EntityPagedDataProvider<OrgStructure> pagedDataProvider = new EntityPagedDataProvider<OrgStructure>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected OrgStructure getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ORG_SRV.getById(OrgStructure.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<OrgStructure> createSearchPanel() {
		return new BranchSearchPanel(this, level);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		window.reset();
		window.show();
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
			window.assignValues(id);
			window.show();
		}
	}
	
	/**
	 * Assign Values
	 * @param organizationId
	 */
	public void assignValues(Long organizationId) {
		this.organizationId = organizationId;
		((BranchSearchPanel) searchPanel).setOrganizationId(organizationId);
	}

	/**
	 * @return the organizationId
	 */
	public Long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * 
	 * @return
	 */
	public EOrgLevel getLavel() {
		return level;
	}

}
