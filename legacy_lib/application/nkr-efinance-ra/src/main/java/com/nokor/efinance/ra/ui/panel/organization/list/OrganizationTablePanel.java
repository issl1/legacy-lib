package com.nokor.efinance.ra.ui.panel.organization.list;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.ra.ui.panel.agent.company.list.AgentCompanySearchPanel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrganizationTablePanel extends AbstractTablePanel<Organization> implements FinServicesHelper, FMEntityField {
	/** */
	private static final long serialVersionUID = -6983649577715703341L;

	/**
	 * Branch Table Panel post contractor
	 */
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addDefaultNavigation();
	}
	
	/**
	 * Init
	 */
	public void init(String caption) {
		super.init(caption);
		setCaption(caption);
	}
	
	/**
	 * Get Item Selected Id
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
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
		if (OrganizationTypes.AGENT.equals(getTypeOrganization())) {
			pagedDefinition.addColumnDefinition(MOrganization.SUBTYPEORGANIZATION + "." + DESC_EN, I18N.message("type"), String.class, Align.LEFT, 110);
		}
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
	protected AbstractSearchPanel<Organization> createSearchPanel() {
		if (OrganizationTypes.AGENT.equals(getTypeOrganization())) {
			return new AgentCompanySearchPanel(this);
		}
		return new OrganizationSearchPanel(this);
	}
	
	/**
	 * Get Type Organization
	 * @return
	 */
	public ETypeOrganization getTypeOrganization() {
		return ((BaseOrganizationHolderPanel) mainPanel).getTypeOrganization();
	}
	
	/**
	 * Get Sub Type Organization
	 * @return
	 */
	public ESubTypeOrganization getSubTypeOrganization() {
		return ((BaseOrganizationHolderPanel) mainPanel).getSubTypeOrganization();
	}

}
