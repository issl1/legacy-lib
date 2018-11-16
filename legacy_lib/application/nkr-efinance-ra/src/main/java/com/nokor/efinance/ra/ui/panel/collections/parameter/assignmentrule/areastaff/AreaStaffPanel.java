package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.areastaff;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColArea;
import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.widget.EntityRefListSelect;
import com.nokor.efinance.core.widget.SecUserListSelect;
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
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
/**
 * Area staff tab in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreaStaffPanel extends AbstractTabPanel implements ClickListener, ValueChangeListener, CollectionEntityField {

	/** */
	private static final long serialVersionUID = -6534178521043269014L;

	private EntityRefComboBox<EColGroup> cbxGroup;
	private EntityRefComboBox<EColTeam> cbxTeam;
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict;
	
	private EntityRefListSelect<EColArea> lstAreaCode;
	private SecUserListSelect<SecUser> lstDestination;
	private SecUserListSelect<SecUser> lstStaff;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnStaffAdd;
	private Button btnStaffRemove;
	
	private EntityService entityService;
	private Label lblTotalStaff;
	private VerticalLayout messagePanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		cbxTeam = getEntityRefComboBox(new BaseRestrictions<>(EColTeam.class));
		cbxTeam.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 8119115070531148182L;
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<EColGroup> restrictions = new BaseRestrictions<EColGroup>(EColGroup.class);
				if (cbxTeam.getSelectedEntity() != null) {
					BaseRestrictions<EColTeamGroup> teamRestrictions = new BaseRestrictions<EColTeamGroup>(EColTeamGroup.class);
					teamRestrictions.addCriterion(Restrictions.eq(TEAM + "." + ID, cbxTeam.getSelectedEntity().getId()));
					List<Long> groupIds = new ArrayList<Long>();
					for (EColTeamGroup teamGroup : entityService.list(teamRestrictions)) {
						if (teamGroup.getGroup() != null) {
							groupIds.add(teamGroup.getGroup().getId());
						}
					}
					if (!groupIds.isEmpty()) {
						restrictions.addCriterion(Restrictions.in(ID, groupIds));
						cbxGroup.setRestrictions(restrictions);
						cbxGroup.renderer();
					} else {
						cbxGroup.clear();
					}
				} 
			}
		});
		cbxGroup = getEntityRefComboBox(new BaseRestrictions<>(EColGroup.class));
		cbxGroup.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6240044212187626828L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxGroup.getSelectedEntity() != null) {
					lstStaff.clear();
					lstStaff.addEntities(cbxGroup.getSelectedEntity().getStaffs());
				} else {
					lstStaff.renderer();
				}
				lstStaff.removeEntities(lstDestination.getAllEntities());
				lblTotalStaff.setValue(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
			}
		});
		cbxProvince = getEntityRefComboBox(new BaseRestrictions<>(Province.class));
		cbxProvince.setImmediate(true);
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4046692305082296587L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<District> restrictions = new BaseRestrictions<District>(District.class);
				if (cbxProvince.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, cbxProvince.getSelectedEntity().getId()));
				}
				cbxDistrict.setRestrictions(restrictions);
				cbxDistrict.renderer();
				initAreaListSelect();
			}
		});
		cbxDistrict = getEntityRefComboBox(new BaseRestrictions<>(District.class));
		cbxDistrict.setImmediate(true);
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4624991322381195030L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Commune> restrictions = new BaseRestrictions<Commune>(Commune.class);
				if (cbxDistrict.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, cbxDistrict.getSelectedEntity().getId()));
				}
				cbxSubDistrict.setRestrictions(restrictions);
				cbxSubDistrict.renderer();
				initAreaListSelect();
			}
		});
		cbxSubDistrict = getEntityRefComboBox(new BaseRestrictions<>(Commune.class));
		cbxSubDistrict.setImmediate(true);
		cbxSubDistrict.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 3732272999755658409L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				initAreaListSelect();
			}
		});
		
		btnStaffAdd = ComponentFactory.getButton("<<");
		btnStaffRemove = ComponentFactory.getButton(">>");
		btnStaffAdd.addClickListener(this);
		btnStaffRemove.addClickListener(this);

		lstAreaCode = getListSelect("area.code", new BaseRestrictions<EColArea>(EColArea.class));
		lstAreaCode.addValueChangeListener(this);
		lstStaff = getSecUserListSelect("staff", new BaseRestrictions<SecUser>(SecUser.class));
		lstStaff.setMultiSelect(true);
		lstStaff.renderer();
		lstDestination = getSecUserListSelect("", null);
		lstDestination.setMultiSelect(true);
		
		lblTotalStaff = new Label(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
		
		VerticalLayout buttonStaffAddRemoveLayout = new VerticalLayout();
		buttonStaffAddRemoveLayout.setSpacing(true);
		buttonStaffAddRemoveLayout.addComponent(btnStaffAdd);
		buttonStaffAddRemoveLayout.addComponent(btnStaffRemove);
		
		GridLayout gridLayout = new GridLayout(6, 4);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		int iRow = 0;
		int iCol = 5;
		gridLayout.addComponent(lblTotalStaff, iCol, iRow);
		
		iRow++;
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("province")), iCol++, iRow);
		gridLayout.addComponent(cbxProvince, iCol++, iRow);
		gridLayout.addComponent(new Label(I18N.message("district")), iCol++, iRow);
		gridLayout.addComponent(cbxDistrict, iCol++, iRow);
		gridLayout.addComponent(new Label(I18N.message("team")), iCol++, iRow);
		gridLayout.addComponent(cbxTeam, iCol++, iRow);
		
		iRow++;
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("subdistrict")), iCol++, iRow);
		gridLayout.addComponent(cbxSubDistrict, iCol++, iRow);
		iCol += 2;	// leave two column
		gridLayout.addComponent(new Label(I18N.message("group")), iCol++, iRow);
		gridLayout.addComponent(cbxGroup, iCol++, iRow);
		
		iRow++;
		iCol = 1;
		gridLayout.addComponent(lstAreaCode, iCol++, iRow);
		iCol++;		// leave one column
		gridLayout.addComponent(lstDestination, iCol++, iRow);
		gridLayout.addComponent(buttonStaffAddRemoveLayout, iCol++, iRow);
		gridLayout.setComponentAlignment(buttonStaffAddRemoveLayout, Alignment.MIDDLE_CENTER);
		gridLayout.addComponent(lstStaff, iCol++, iRow);
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		btnCancel.addClickListener(this);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(panel);
		
		TabSheet mainTab = new TabSheet();
        mainTab.addTab(mainLayout, I18N.message("area.staffs"));
		
		return mainTab;
	}
	
	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(130, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnStaffAdd) {
			List<SecUser> selectedStaffs = lstStaff.getSelectedEntities();
			lstDestination.addEntities(selectedStaffs);
			lstStaff.removeEntities(selectedStaffs);
		} else if (event.getButton() == btnStaffRemove) {
			List<SecUser> removedStaffs = lstDestination.getSelectedEntities();
			lstDestination.removeEntities(removedStaffs);
			lstStaff.addEntities(removedStaffs);
		} else if (event.getButton() == btnSave) {
			if (!lstAreaCode.getSelectedEntities().isEmpty()) {
				EColArea group = lstAreaCode.getSelectedEntities().get(0);
				group.setStaffs(lstDestination.getAllEntities());
				entityService.saveOrUpdate(group);
				displaySuccessMsg();
			}
		} else if (event.getButton() == btnCancel) {
			initStaffAndDestinationListSelect();
		}
		lblTotalStaff.setValue(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		initStaffAndDestinationListSelect();
		lblTotalStaff.setValue(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
	}
	
	/**
	 * Init Staff And Destination ListSelect
	 */
	private void initStaffAndDestinationListSelect() {
		messagePanel.setVisible(false);
		lstDestination.clear();
		if (cbxGroup.getSelectedEntity() != null) {
			lstStaff.clear();
			lstStaff.addEntities(cbxGroup.getSelectedEntity().getStaffs());
		} else {
			lstStaff.renderer();
		}
		if (!lstAreaCode.getSelectedEntities().isEmpty()) {
			EColArea area = lstAreaCode.getSelectedEntities().get(0);
			lstDestination.addEntities(area.getStaffs());
			lstStaff.removeEntities(area.getStaffs());
		}
	}
	
	/**
	 * Init Area ListSelect
	 */
	private void initAreaListSelect() {
		BaseRestrictions<EColArea> restrictions = new BaseRestrictions<EColArea>(EColArea.class);
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, cbxProvince.getSelectedEntity().getId()));
		}
		if (cbxDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, cbxDistrict.getSelectedEntity().getId()));
		}
		if (cbxSubDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(COMMUNE + "." + ID, cbxSubDistrict.getSelectedEntity().getId()));
		}
		lstAreaCode.setRestrictions(restrictions);
		lstAreaCode.renderer();
	}
	
	/**
	 * Get ListSelect
	 * @param caption
	 * @param restrictions
	 * @return
	 */
	private <T extends EntityRefA> EntityRefListSelect<T> getListSelect(String caption, BaseRestrictions<T> restrictions) {
		EntityRefListSelect<T> listSelect = new EntityRefListSelect<T>(I18N.message(caption), true);
		listSelect.setRows(10);
		listSelect.setNullSelectionAllowed(false);
		listSelect.setMultiSelect(false);
		listSelect.setImmediate(true);
		listSelect.setWidth(120, Unit.PIXELS);
		listSelect.setRestrictions(restrictions);
		listSelect.renderer();
		return listSelect;
	}
	
	/**
	 * Get SecUser ListSelect
	 * @param caption
	 * @param restrictions
	 * @return
	 */
	private <T extends SecUser> SecUserListSelect<T> getSecUserListSelect(String caption, BaseRestrictions<T> restrictions) {
		SecUserListSelect<T> listSelect = new SecUserListSelect<T>(I18N.message(caption));
		listSelect.setRows(10);
		listSelect.setNullSelectionAllowed(false);
		listSelect.setImmediate(true);
		listSelect.setWidth(120, Unit.PIXELS);
		listSelect.setRestrictions(restrictions);
		return listSelect;
	}
}
