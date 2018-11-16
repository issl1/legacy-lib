package com.nokor.efinance.gui.ui.panel.marketing;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.ResourcePanel;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class EmployeesMarketingPanel extends AbstractControlPanel implements ItemPerPageChangeListener, ClickListener {

	/** */
	private static final long serialVersionUID = 2463913141216154637L;
	
	private EntityPagedTable<Employee> pagedTable;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtReference;
	private ERefDataComboBox<EGender> cbxGender;
	private Button btnSearch;
	private Button btnReset;
	
	/**
	 * 
	 */
	public EmployeesMarketingPanel() {
		setSizeFull();
		setMargin(true);
		txtFirstName = ComponentFactory.getTextField(false, 60, 150);
		txtLastName = ComponentFactory.getTextField(false, 60, 150);
		txtReference = ComponentFactory.getTextField(false, 60, 150);
		cbxGender = new ERefDataComboBox<EGender>(EGender.values());
		cbxGender.setWidth(150, Unit.PIXELS);
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnReset = ComponentLayoutFactory.getButtonReset();
		
		btnSearch.addClickListener(this);
		btnReset.addClickListener(this);
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("firstname"));
		searchLayout.addComponent(txtFirstName);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("lastname"));
		searchLayout.addComponent(txtLastName);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("reference"));
		searchLayout.addComponent(txtReference);
		searchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("gender"));
		searchLayout.addComponent(cbxGender);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout searchVerLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		searchVerLayout.addComponent(searchLayout);
		searchVerLayout.addComponent(buttonLayout);
		searchVerLayout.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER);
		
		pagedTable = new EntityPagedTable<>(createPagedDataProvider());
		pagedTable.buildGrid();
		pagedTable.setCaption(I18N.message("employees"));
		pagedTable.setItemPerPageChangeListener(this);
		
		pagedTable.addItemClickListener(new ItemClickListener() {
			
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Long empId = (Long) event.getItemId();
					
					Employee emp = ENTITY_SRV.getById(Employee.class, empId);
					
					EmployeeMarketingDetailPanel resourcePanel = EmployeeMarketingDetailPanel.show(new EmployeeMarketingDetailPanel.Listener() {
						

						@Override
						public void onClose(EmployeeMarketingDetailPanel dialog) {
							
						}
					});
					resourcePanel.assignValues(emp);
					UI.getCurrent().addWindow(resourcePanel);
				}
			}
		});
		
		setMargin(true);
		setSpacing(true);
		
		Panel filtersPanel = ComponentLayoutFactory.getPanel(searchVerLayout, true, false);
		filtersPanel.setCaption("<b style=\"color:#3B5998\">" + I18N.message("filters") + "</b>");
		
		addComponent(filtersPanel);
        addComponent(pagedTable);
        addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @return
	 */
	private PagedDataProvider<Employee> createPagedDataProvider() {
		PagedDefinition<Employee> pagedDefinition = new PagedDefinition<>(getRestrictions());
		pagedDefinition.addColumnDefinition(Employee.ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(Employee.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.CIVILITY + "." + FMEntityField.DESC_EN, I18N.message("prefix"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.GENDER + "." + FMEntityField.DESC_EN, I18N.message("gender"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.BIRTHDATE, I18N.message("birth.date"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(Employee.JOBPOSITION + "." + FMEntityField.DESC_EN, I18N.message("position"), String.class, Align.LEFT, 150);
		EntityPagedDataProvider<Employee> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * 
	 */
	public void refresh() {
		pagedTable.refresh();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener#onItemPerPageChange(int)
	 */
	@Override
	public void onItemPerPageChange(int itemPerPage) {
		refresh();
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		txtFirstName.setValue(StringUtils.EMPTY);
		txtLastName.setValue(StringUtils.EMPTY);
		txtReference.setValue(StringUtils.EMPTY);
		cbxGender.setSelectedEntity(null);
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Employee> getRestrictions() {
		BaseRestrictions<Employee> restrictions = new BaseRestrictions<>(Employee.class);
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(Employee.FIRSTNAME, txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtLastName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(Employee.LASTNAME, txtLastName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(Employee.REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxGender.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(Employee.GENDER, cbxGender.getSelectedEntity()));
		}
		restrictions.addOrder(Order.desc(Employee.ID));
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSearch)) {
			pagedTable.getPagedDefinition().setRestrictions(getRestrictions());
			refresh();
		} else if (event.getButton().equals(btnReset)) {
			reset();
		}
	}
}
