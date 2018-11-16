package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.widget.SecUserListSelect;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
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
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamStaffPanel extends AbstractTabPanel implements ClickListener, ValueChangeListener, CollectionEntityField {

	/** */
	private static final long serialVersionUID = 2256717670409837242L;
	
	private SecUserListSelect<SecUser> lstDestination;
	private SecUserListSelect<SecUser> lstStaff;
	private EntityRefComboBox<EColTeam> cbxTeam;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnStaffAdd;
	private Button btnStaffRemove;
	private Label lblTotalStaff;
	private VerticalLayout messagePanel;

	@Override
	protected com.vaadin.ui.Component createForm() {
		btnStaffAdd = ComponentFactory.getButton();
		btnStaffAdd.setIcon(FontAwesome.ANGLE_DOUBLE_LEFT);
		btnStaffRemove = ComponentFactory.getButton();
		btnStaffRemove.setIcon(FontAwesome.ANGLE_DOUBLE_RIGHT);
		btnStaffAdd.addClickListener(this);
		btnStaffRemove.addClickListener(this);
		
		VerticalLayout buttonStaffAddRemoveLayout = new VerticalLayout();
		buttonStaffAddRemoveLayout.setSpacing(true);
		buttonStaffAddRemoveLayout.addComponent(btnStaffAdd);
		buttonStaffAddRemoveLayout.addComponent(btnStaffRemove);
		
		lstStaff = getSecUserListSelect("staff", new BaseRestrictions<SecUser>(SecUser.class));
		lstStaff.setMultiSelect(true);
		lstStaff.renderer();
		lstDestination = getSecUserListSelect("team.staff", null);
		lstDestination.setMultiSelect(true);
		
		cbxTeam = new EntityRefComboBox<>();
		cbxTeam.setRestrictions(new BaseRestrictions<>(EColTeam.class));
		cbxTeam.setImmediate(true);
		cbxTeam.renderer();
		cbxTeam.addValueChangeListener(this);
		
		lblTotalStaff = new Label(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());
		
		GridLayout gridLayout = new GridLayout(5, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		gridLayout.addComponent(new Label(I18N.message("team")), 0, 0);
		gridLayout.addComponent(cbxTeam, 1, 0);
		gridLayout.addComponent(lblTotalStaff, 3, 0);

		gridLayout.addComponent(lstDestination, 1, 1);
		gridLayout.addComponent(buttonStaffAddRemoveLayout, 2, 1);
		gridLayout.setComponentAlignment(buttonStaffAddRemoveLayout, Alignment.MIDDLE_CENTER);
		gridLayout.addComponent(lstStaff,3, 1);
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
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
        mainTab.addTab(mainLayout, I18N.message("team.staff"));
		
		return mainTab;
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
	 * display error
	 */
	private void displayError() {
		errors.add(I18N.message("field.required.1",new String[] { I18N.message("collection.type") }));
		this.messagePanel.removeAllComponents();
		if (!(errors.isEmpty())) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}
	
	/**
	 * 
	 */
	private void initStaffAndDestinationListSelect() {
		messagePanel.setVisible(false);
		lstDestination.clear();
		lstStaff.clear();
		lstStaff.renderer();
		if (cbxTeam.getSelectedEntity() != null) {
			EColTeam colTeam = cbxTeam.getSelectedEntity();
			lstDestination.addEntities(colTeam.getStaffs());
			lstStaff.removeEntities(colTeam.getStaffs());
		}
	}
	

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
			if (cbxTeam.getSelectedEntity() != null) {
				EColTeam colTeam = cbxTeam.getSelectedEntity();
				colTeam.setStaffs(lstDestination.getAllEntities());
				ENTITY_SRV.saveOrUpdate(colTeam);
				displaySuccessMsg();
			} else {
				this.displayError();
			}
		} else if (event.getButton() == btnCancel) {
			initStaffAndDestinationListSelect();
		}
		lblTotalStaff.setValue(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());	
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		initStaffAndDestinationListSelect();
		lblTotalStaff.setValue(I18N.message("total.staff") + " " + lstStaff.getAllEntities().size());	
	}
	

}
