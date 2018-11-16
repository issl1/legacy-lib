package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.staffgroup;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.widget.EntityRefListSelect;
import com.nokor.efinance.core.widget.SecUserListSelect;
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
 * Staff group tab in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StaffGroupPanel extends AbstractTabPanel implements ClickListener, ValueChangeListener, CollectionEntityField {

	/** */
	private static final long serialVersionUID = -3668636584918291331L;
	
	private EntityService entityService;
	
	private EntityRefComboBox<EColTeam> cbxGroupTeam;
	
	private EntityRefListSelect<EColGroup> lstGroup;
	private SecUserListSelect<SecUser> lstDestination;
	private SecUserListSelect<SecUser> lstStaff;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnStaffAdd;
	private Button btnStaffRemove;
	private Label lblTotalStaff;
	private VerticalLayout messagePanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		cbxGroupTeam = new EntityRefComboBox<EColTeam>(entityService.list(new BaseRestrictions<EColTeam>(EColTeam.class)));
		cbxGroupTeam.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -113729167173532893L;
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<EColGroup> restrictions = new BaseRestrictions<EColGroup>(EColGroup.class);
				if (cbxGroupTeam.getSelectedEntity() != null) {
					BaseRestrictions<EColTeamGroup> restrictionsTeamGroup = new BaseRestrictions<EColTeamGroup>(EColTeamGroup.class);
					restrictionsTeamGroup.addCriterion(Restrictions.eq(TEAM + "." + ID, cbxGroupTeam.getSelectedEntity().getId()));
					List<Long> groupIds = new ArrayList<Long>();
					for (EColTeamGroup teamGroup : entityService.list(restrictionsTeamGroup)) {
						if (teamGroup.getGroup() != null) {
							groupIds.add(teamGroup.getGroup().getId());
						}
					}
					if (!groupIds.isEmpty()) {
						restrictions.addCriterion(Restrictions.in(ID, groupIds));
						lstGroup.setRestrictions(restrictions);
						lstGroup.renderer();
					} else {
						lstGroup.clear();
					}
				} else {
					lstGroup.setRestrictions(restrictions);
					lstGroup.renderer();
				} 
			}
		});
		cbxGroupTeam.setWidth(130, Unit.PIXELS);
		
		btnStaffAdd = ComponentFactory.getButton("<<");
		btnStaffRemove = ComponentFactory.getButton(">>");
		btnStaffAdd.addClickListener(this);
		btnStaffRemove.addClickListener(this);
		
		lstGroup = getListSelect("group", new BaseRestrictions<EColGroup>(EColGroup.class));
		lstGroup.addValueChangeListener(this);
		lstStaff = getSecUserListSelect("staff", new BaseRestrictions<SecUser>(SecUser.class));
		lstStaff.setMultiSelect(true);
		lstStaff.renderer();
		lstDestination = getSecUserListSelect("", null);
		lstDestination.setMultiSelect(true);
		
		lblTotalStaff = new Label(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
		
		VerticalLayout destinationLstedLayout = new VerticalLayout();
		destinationLstedLayout.setSpacing(true);
		destinationLstedLayout.addComponent(lstDestination);
		
		VerticalLayout buttonStaffAddRemoveLayout = new VerticalLayout();
		buttonStaffAddRemoveLayout.setSpacing(true);
		buttonStaffAddRemoveLayout.addComponent(btnStaffAdd);
		buttonStaffAddRemoveLayout.addComponent(btnStaffRemove);
		
		GridLayout gridLayout = new GridLayout(5, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		int iRow = 0;
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("team")), iCol++, iRow);
		gridLayout.addComponent(cbxGroupTeam, iCol++, iRow);
		iCol += 2;
		gridLayout.addComponent(lblTotalStaff, iCol++, iRow);
		
		iRow++;
		iCol = 1;
		gridLayout.addComponent(lstGroup, iCol++, iRow);
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
        mainTab.addTab(mainLayout, I18N.message("staff.groups"));
		
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
			if (!lstGroup.getSelectedEntities().isEmpty()) {
				EColGroup group = lstGroup.getSelectedEntities().get(0);
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
	
	private void initStaffAndDestinationListSelect() {
		messagePanel.setVisible(false);
		lstDestination.clear();
		lstStaff.clear();
		lstStaff.renderer();
		if (!lstGroup.getSelectedEntities().isEmpty()) {
			EColGroup group = lstGroup.getSelectedEntities().get(0);
			lstDestination.addEntities(group.getStaffs());
			lstStaff.removeEntities(group.getStaffs());
		}
	}
	
	/**
	 * Get ListSelect with a caption and the data from the restriction.
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
	 * Get SecUser ListSelect with a caption and the data from the restriction.
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
