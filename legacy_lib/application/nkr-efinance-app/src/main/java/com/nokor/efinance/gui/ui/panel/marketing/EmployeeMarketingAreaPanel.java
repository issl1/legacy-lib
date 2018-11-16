package com.nokor.efinance.gui.ui.panel.marketing;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.marketing.model.EmployeeArea;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.AreaListSelect;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class EmployeeMarketingAreaPanel extends AbstractTabPanel implements ClickListener, FMEntityField {

	/** */
	private static final long serialVersionUID = 4417228059811992836L;

	private AreaListSelect lstEmpAreas;
	private AreaListSelect lstAreas;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnAdd;
	private Button btnRemove;
	
	private Employee employee;
	
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict;
	private Button btnRefresh;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnAdd = ComponentFactory.getButton();
		btnAdd.setIcon(FontAwesome.ANGLE_DOUBLE_LEFT);
		btnRemove = ComponentFactory.getButton();
		btnRemove.setIcon(FontAwesome.ANGLE_DOUBLE_RIGHT);
		btnAdd.addClickListener(this);
		btnRemove.addClickListener(this);
		
		VerticalLayout buttonsLayout = new VerticalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnAdd);
		buttonsLayout.addComponent(btnRemove);
		
		cbxProvince = getEntityRefComboBox(null, new BaseRestrictions<>(Province.class));
		cbxProvince.setWidth("140px");
		cbxDistrict = getEntityRefComboBox(null, null);
		cbxDistrict.setWidth("140px");
		cbxSubDistrict = getEntityRefComboBox(null, null);
		cbxSubDistrict.setWidth("140px");
		
		btnRefresh = ComponentLayoutFactory.getButtonStyle("refresh", FontAwesome.REFRESH, 65, "btn btn-success button-small");
		btnRefresh.addClickListener(this);
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = -3181047335455522479L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					Province province = cbxProvince.getSelectedEntity();
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, province.getId()));
					cbxDistrict.setRestrictions(restrictions);
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
				}
				cbxSubDistrict.clear();
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -8756628607605904954L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					District district = cbxDistrict.getSelectedEntity();
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, district.getId()));
					cbxSubDistrict.setRestrictions(restrictions);
					cbxSubDistrict.renderer();
					
					
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		Label lblProvince = ComponentFactory.getLabel("province");
		Label lblDistrict = ComponentFactory.getLabel("district");
		Label lblSubDistrict = ComponentFactory.getLabel("commune");
		
		GridLayout searchLayout = new GridLayout(8, 2);
		searchLayout.setSpacing(true);
		searchLayout.setMargin(true);
		int iCol = 0;
		searchLayout.addComponent(lblProvince, iCol++, 0);
		searchLayout.addComponent(cbxProvince, iCol++, 0);
		searchLayout.addComponent(lblDistrict, iCol++, 0);
		searchLayout.addComponent(cbxDistrict, iCol++, 0);
		
		iCol = 0;
		searchLayout.addComponent(lblSubDistrict, iCol++, 1);
		searchLayout.addComponent(cbxSubDistrict, iCol++, 1);
		searchLayout.addComponent(btnRefresh, 3, 1);
		
		searchLayout.setComponentAlignment(lblProvince, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblDistrict, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblSubDistrict, Alignment.MIDDLE_LEFT);
		
		lstAreas = new AreaListSelect(I18N.message("area"));
		lstAreas.setRestrictions(getAreaRestrictions());
		lstAreas.setMultiSelect(true);
		lstAreas.setImmediate(true);
		lstAreas.setWidth(300, Unit.PIXELS);
		lstAreas.setRows(10);
		lstAreas.renderer();
		
		lstEmpAreas = new AreaListSelect(I18N.message("employee.area"));
		lstEmpAreas.setImmediate(true);
		lstEmpAreas.setMultiSelect(true);
		lstEmpAreas.setWidth(300, Unit.PIXELS);
		lstEmpAreas.setRows(10);
		
		GridLayout gridLayout = new GridLayout(4, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		gridLayout.addComponent(searchLayout,3, 0);
		
		gridLayout.addComponent(lstEmpAreas, 1, 1);
		gridLayout.addComponent(buttonsLayout, 2, 1);
		gridLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
		gridLayout.addComponent(lstAreas,3, 1);

		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
	
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(panel);
		
		return mainLayout;
	}

	/**
	 * 
	 * @param employee
	 */
	public void assignValues(Employee employee) {
		this.employee = employee;
		List<EmployeeArea> colStaffAreas = getEmployeeAreaByEmployee(employee);
		lstEmpAreas.clear();
		lstAreas.clear();
		lstAreas.renderer();
		for (EmployeeArea employeeArea : colStaffAreas) {
			lstEmpAreas.addEntity(employeeArea.getArea());
		}
		if (ProfileUtil.isColFieldSupervisor()) {
			lstAreas.removeEntities(getAreasAssigned());
		} else {
			lstAreas.removeEntities(lstEmpAreas.getAllEntities());
		}
	}
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	private List<EmployeeArea> getEmployeeAreaByEmployee(Employee employee) {
		BaseRestrictions<EmployeeArea> restrictions = new BaseRestrictions<>(EmployeeArea.class);
		restrictions.addCriterion(Restrictions.eq("employee", employee));		
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			List<Area> selectedAreas = lstAreas.getSelectedEntities();
			lstEmpAreas.addEntities(selectedAreas);
			lstAreas.removeEntities(selectedAreas);
		} else if (event.getButton() == btnRemove) {
			List<Area> removedAreas = lstEmpAreas.getSelectedEntities();
			lstEmpAreas.removeEntities(removedAreas);
			lstAreas.addEntities(removedAreas);
		} else if (event.getButton() == btnSave) {
			List<EmployeeArea> employeeAreas = getEmployeeAreaByEmployee(employee);
			EmployeeArea employeeArea;
			if (employeeAreas.size() <= lstEmpAreas.getAllEntities().size()) {
				//Update|Create
				for (Area area : lstEmpAreas.getAllEntities()) {
					employeeArea = getEmployeeAreaByArea(area);
					if (employeeArea == null) {
						employeeArea = new EmployeeArea();
					}
					employeeArea.setEmployee(employee);
					employeeArea.setArea(area);
					ENTITY_SRV.saveOrUpdate(employeeArea);
				}
			} else {
				//Delete
				for (EmployeeArea empAarea : employeeAreas) {
					if (!lstEmpAreas.getAllEntities().contains(empAarea.getArea())) {
						ENTITY_SRV.delete(empAarea);						}
				}
			}
			displaySuccess();
		} else if (event.getButton() == btnCancel) {
			assignValues(employee);
		} else if (event.getButton() == btnRefresh) {
			lstAreas.setRestrictions(getAreaRestrictions());
			lstAreas.renderer();
			if (ProfileUtil.isColFieldSupervisor()) {
				lstAreas.removeEntities(getAreasAssigned());
			} else {
				lstAreas.removeEntities(lstEmpAreas.getAllEntities());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Area> getAreaRestrictions() {
		BaseRestrictions<Area> restrictions = new BaseRestrictions<>(Area.class);
		
		restrictions.addCriterion(Restrictions.eq("colType", EColType.MKT));
		
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("province", cbxProvince.getSelectedEntity()));
		}
		if (cbxDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("district", cbxDistrict.getSelectedEntity()));
		} 
		if (cbxSubDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("commune", cbxSubDistrict.getSelectedEntity()));
		}	
		return restrictions;
	}
	
	/**
	 * 
	 * @param area
	 * @return
	 */
	private EmployeeArea getEmployeeAreaByArea(Area area) {
		BaseRestrictions<EmployeeArea> restrictions = new BaseRestrictions<>(EmployeeArea.class);
		restrictions.addCriterion(Restrictions.eq("area", area));
		restrictions.addCriterion(Restrictions.eq("employee", employee));
		List<EmployeeArea> employeeAreas = ENTITY_SRV.list(restrictions);
		
		if (!employeeAreas.isEmpty()) {
			return employeeAreas.get(0);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param caption
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(String caption, BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>(I18N.message(caption));
		comboBox.setWidth(200, Unit.PIXELS);
		if (restrictions != null) {
			comboBox.setRestrictions(restrictions);
			comboBox.renderer();
		}
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Area> getAreasAssigned() {
		BaseRestrictions<Area> restrictions = new BaseRestrictions<>(Area.class);
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(EmployeeArea.class, "emparea");
		userContractSubCriteria.add(Restrictions.isNotNull("employee"));
		userContractSubCriteria.add(Restrictions.isNotNull("area"));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("emparea.area.id")));
		restrictions.addCriterion(Property.forName(EmployeeArea.ID).in(userContractSubCriteria));
		return ENTITY_SRV.list(restrictions);
	}
	
	
}
