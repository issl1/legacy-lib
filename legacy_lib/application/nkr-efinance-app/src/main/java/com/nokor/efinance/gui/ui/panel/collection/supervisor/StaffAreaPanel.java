package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColStaffArea;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.AreaListSelect;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.security.model.SecUser;
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
 * @author buntha.chea
 *
 */
public class StaffAreaPanel extends AbstractTabPanel implements ClickListener, FMEntityField {

	/** */
	private static final long serialVersionUID = 2256717670409837242L;

	private AreaListSelect lstStaffArea;
	private AreaListSelect lstArea;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnAdd;
	private Button btnRemove;
	
	private SecUser staff;
	
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict;
	private Button btnRefresh;
	
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
			private static final long serialVersionUID = -572281953646438700L;

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
			private static final long serialVersionUID = -470992549634354195L;

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
		
		lstArea = new AreaListSelect(I18N.message("area"));
		lstArea.setRestrictions(getAreaRestrictions());
		lstArea.setMultiSelect(true);
		lstArea.setImmediate(true);
		lstArea.setWidth(300, Unit.PIXELS);
		lstArea.setRows(10);
		lstArea.renderer();
		
		lstStaffArea = new AreaListSelect(I18N.message("staff.area"));
		lstStaffArea.setImmediate(true);
		lstStaffArea.setMultiSelect(true);
		lstStaffArea.setWidth(300, Unit.PIXELS);
		lstStaffArea.setRows(10);
		
		GridLayout gridLayout = new GridLayout(4, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		gridLayout.addComponent(searchLayout,3, 0);
		
		gridLayout.addComponent(lstStaffArea, 1, 1);
		gridLayout.addComponent(buttonsLayout, 2, 1);
		gridLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
		gridLayout.addComponent(lstArea,3, 1);

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
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(panel);
		
		return mainLayout;
	}

	/**
	 * @param secUser
	 */
	public void assignValues(SecUser secUser) {
		this.staff = secUser;
		//List<Area> areas = new ArrayList<>();
		List<EColStaffArea> colStaffAreas = getColStaffAreaByStaff(secUser);
		lstStaffArea.clear();
		lstArea.clear();
		lstArea.renderer();
		for (EColStaffArea colStaffArea : colStaffAreas) {
			lstStaffArea.addEntity(colStaffArea.getArea());
			//areas.add(colStaffArea.getArea());
		}
		if (ProfileUtil.isColFieldSupervisor()) {
			lstArea.removeEntities(getAreasAssigned());
		} else {
			lstArea.removeEntities(lstStaffArea.getAllEntities());
		}
	}
	
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	private List<EColStaffArea> getColStaffAreaByStaff(SecUser secUser) {
		BaseRestrictions<EColStaffArea> restrictions = new BaseRestrictions<>(EColStaffArea.class);
		restrictions.addCriterion(Restrictions.eq("user", secUser));		
		return ENTITY_SRV.list(restrictions);
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			List<Area> selectedAreas = lstArea.getSelectedEntities();
			lstStaffArea.addEntities(selectedAreas);
			lstArea.removeEntities(selectedAreas);
		} else if (event.getButton() == btnRemove) {
			List<Area> removedAreas = lstStaffArea.getSelectedEntities();
			lstStaffArea.removeEntities(removedAreas);
			lstArea.addEntities(removedAreas);
		} else if (event.getButton() == btnSave) {
			List<EColStaffArea> colStaffAreas = getColStaffAreaByStaff(staff);
			EColStaffArea colStaffArea;
			if (colStaffAreas.size() <= lstStaffArea.getAllEntities().size()) {
				//Update|Create
				for (Area area : lstStaffArea.getAllEntities()) {
					colStaffArea = getColStaffAreaByArea(area);
					if (colStaffArea == null) {
						colStaffArea = new EColStaffArea();
					}
					colStaffArea.setUser(staff);
					colStaffArea.setArea(area);
					ENTITY_SRV.saveOrUpdate(colStaffArea);
				}
			} else {
				//Delete
				for (EColStaffArea eColStaffArea : colStaffAreas) {
					if (!lstStaffArea.getAllEntities().contains(eColStaffArea.getArea())) {
						ENTITY_SRV.delete(eColStaffArea);						}
				}
			}
			displaySuccess();
		} else if (event.getButton() == btnCancel) {
			assignValues(staff);
		} else if (event.getButton() == btnRefresh) {
			lstArea.setRestrictions(getAreaRestrictions());
			lstArea.renderer();
			if (ProfileUtil.isColFieldSupervisor()) {
				lstArea.removeEntities(getAreasAssigned());
			} else {
				lstArea.removeEntities(lstStaffArea.getAllEntities());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Area> getAreaRestrictions() {
		BaseRestrictions<Area> restrictions = new BaseRestrictions<>(Area.class);
		
		if (ProfileUtil.isColFieldSupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.FIELD));
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.INSIDE_REPO));
		} else if (ProfileUtil.isColOASupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.OA));
		}
		
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
	private EColStaffArea getColStaffAreaByArea(Area area) {
		BaseRestrictions<EColStaffArea> restrictions = new BaseRestrictions<>(EColStaffArea.class);
		restrictions.addCriterion(Restrictions.eq("area", area));
		restrictions.addCriterion(Restrictions.eq("user", staff));
		List<EColStaffArea> colStaffAreas = ENTITY_SRV.list(restrictions);
		
		if (!colStaffAreas.isEmpty()) {
			return colStaffAreas.get(0);
		}
		
		return null;
	}
	
	/**
	 * 
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
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(EColStaffArea.class, "staffarea");
		userContractSubCriteria.add(Restrictions.isNotNull("user"));
		userContractSubCriteria.add(Restrictions.isNotNull("area"));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("staffarea.area.id")));
		restrictions.addCriterion(Property.forName(EColStaffArea.ID).in(userContractSubCriteria));
		return ENTITY_SRV.list(restrictions);
	}
	
	
}
