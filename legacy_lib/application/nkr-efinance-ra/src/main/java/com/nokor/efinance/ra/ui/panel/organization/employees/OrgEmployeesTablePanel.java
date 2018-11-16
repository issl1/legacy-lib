package com.nokor.efinance.ra.ui.panel.organization.employees;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.organization.Employee;
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
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrgEmployeesTablePanel extends AbstractTablePanel<Employee> implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947778344600932119L;
	
	private Long organizationId;
	
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addDefaultNavigation();
		//window = new OrganizationEmployeePopUpPanel(this);
	}
	
	/**
	 * 
	 */
	public void init(String caption) {
		setCaption(I18N.message(caption));
		super.init(I18N.message(caption));
	}
	
	/**
	 * getEntity
	 */
	@Override
	protected Employee getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(OrgStructure.ID).getValue();
		    return ORG_SRV.getById(Employee.class, id);
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	protected PagedDataProvider<Employee> createPagedDataProvider() {
		PagedDefinition<Employee> pagedDefinition = new PagedDefinition<Employee>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(OrgStructure.ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("reference", I18N.message("reference"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("civility.descEn", I18N.message("prefix"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("firstName", I18N.message("firstname"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lastName", I18N.message("lastname"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("gender.descEn", I18N.message("gender"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("birthDate", I18N.message("birth.date"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("jobPosition.descEn", I18N.message("position"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("branch.nameEn", I18N.message("Department"), String.class, Align.LEFT, 150);
		
		EntityPagedDataProvider<Employee> pagedDataProvider = new EntityPagedDataProvider<Employee>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	@Override
	protected AbstractSearchPanel<Employee> createSearchPanel() {
		return new OrgEmployeesSearchPanel(I18N.message("search"), this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		OrgEmployeePopUpPanel empPopupWindown = new OrgEmployeePopUpPanel(this);
		empPopupWindown.assignValues(null, organizationId);
		empPopupWindown.show();
	}
	
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long empId = (Long) selectedItem.getItemProperty(OrgStructure.ID).getValue();
			OrgEmployeePopUpPanel window = new OrgEmployeePopUpPanel(this);
			window.assignValues(empId, organizationId);
			UI.getCurrent().addWindow(window);
		}
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
	 * Assign Values
	 * @param organizationId
	 */
	public void assignValues(Long organizationId) {
		this.organizationId = organizationId;
		((OrgEmployeesSearchPanel) searchPanel).setOrganizationId(organizationId);
	}

	

}
