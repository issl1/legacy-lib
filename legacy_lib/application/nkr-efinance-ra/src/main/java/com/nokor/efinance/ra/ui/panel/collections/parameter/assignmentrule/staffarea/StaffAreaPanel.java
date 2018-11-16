package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.staffarea;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColStaffArea;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.efinance.core.widget.EntityRefListSelect;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
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
public class StaffAreaPanel extends AbstractTabPanel implements ClickListener, ValueChangeListener, CollectionEntityField {

	/** */
	private static final long serialVersionUID = 2256717670409837242L;

	private SecUserComboBox cbxStaff;
	private EntityRefListSelect<Area> lstStaffArea;
	private EntityRefListSelect<Area> lstArea;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnAdd;
	private Button btnRemove;
	private Label lblTotalAreas;
	private VerticalLayout messagePanel;

	@Override
	protected com.vaadin.ui.Component createForm() {
		btnAdd = ComponentFactory.getButton();
		btnAdd.setIcon(FontAwesome.ANGLE_DOUBLE_LEFT);
		btnRemove = ComponentFactory.getButton();
		btnRemove.setIcon(FontAwesome.ANGLE_DOUBLE_RIGHT);
		btnAdd.addClickListener(this);
		btnRemove.addClickListener(this);
		
		VerticalLayout buttonAreaAddRemoveLayout = new VerticalLayout();
		buttonAreaAddRemoveLayout.setSpacing(true);
		buttonAreaAddRemoveLayout.addComponent(btnAdd);
		buttonAreaAddRemoveLayout.addComponent(btnRemove);
		
		lstArea = new EntityRefListSelect<>(I18N.message("area"));
		lstArea.setRestrictions(new BaseRestrictions<>(Area.class));
		lstArea.setMultiSelect(true);
		lstArea.setImmediate(true);
		lstArea.setWidth(120, Unit.PIXELS);
		lstArea.setRows(10);
		lstArea.renderer();
		
		lstStaffArea = new EntityRefListSelect<>(I18N.message("staff.area"));
		lstStaffArea.setImmediate(true);
		lstStaffArea.setMultiSelect(true);
		lstStaffArea.setWidth(160, Unit.PIXELS);
		lstStaffArea.setRows(10);
		
		cbxStaff = new SecUserComboBox(ENTITY_SRV.list(SecUser.class));
		cbxStaff.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -7126274331342059954L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SecUser staff = cbxStaff.getSelectedEntity();
				lstStaffArea.clear();
				lstArea.renderer();
				for (EColStaffArea colStaffArea : getColStaffAreaByStaff(staff)) {
					lstStaffArea.addEntity(colStaffArea.getArea());
					lstArea.removeEntity(colStaffArea.getArea());
				}
				
				lblTotalAreas.setValue(I18N.message("total.areas") + " " + lstArea.getItemIds().size());
			}
		});
		
		lblTotalAreas = new Label(I18N.message("total.areas") + " " + lstArea.getItemIds().size());
		
		GridLayout gridLayout = new GridLayout(5, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		gridLayout.addComponent(new Label(I18N.message("staff")), 0, 0);
		gridLayout.addComponent(cbxStaff, 1, 0);
		gridLayout.addComponent(lblTotalAreas, 3, 0);

		gridLayout.addComponent(lstStaffArea, 1, 1);
		gridLayout.addComponent(buttonAreaAddRemoveLayout, 2, 1);
		gridLayout.setComponentAlignment(buttonAreaAddRemoveLayout, Alignment.MIDDLE_CENTER);
		gridLayout.addComponent(lstArea,3, 1);
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
        mainTab.addTab(mainLayout, I18N.message("staff.areas"));
		
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
	 * display error
	 */
	private boolean validate() {
		this.messagePanel.removeAllComponents();
		errors.clear();
		if (cbxStaff.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("staff") }));
		}	
		
		if (!(errors.isEmpty())) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	private void initStaffAndDestinationListSelect() {
		List<EColStaffArea> colStaffAreas = getColStaffAreaByStaff(cbxStaff.getSelectedEntity());
		messagePanel.setVisible(false);
		lstStaffArea.clear();
		lstArea.clear();
		lstArea.renderer();
		for (EColStaffArea colStaffArea : colStaffAreas) {
			lstStaffArea.addEntity(colStaffArea.getArea());
			lstArea.removeEntity(colStaffArea.getArea());
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
			try {
				if (validate()) {
					List<EColStaffArea> colStaffAreas = getColStaffAreaByStaff(cbxStaff.getSelectedEntity());
					EColStaffArea colStaffArea;
					if (colStaffAreas.size() <= lstStaffArea.getAllEntities().size()) {
						//Update|Create
						for (Area area : lstStaffArea.getAllEntities()) {
							colStaffArea = getColStaffAreaByArea(area);
							if (colStaffArea == null) {
								colStaffArea = new EColStaffArea();
							}
							colStaffArea.setUser(cbxStaff.getSelectedEntity());
							colStaffArea.setArea(area);
							ENTITY_SRV.saveOrUpdate(colStaffArea);
							logger.info("<<<<< Save Successfully >>>>>");	
						}
					} else {
						//Delete
						for (EColStaffArea eColStaffArea : colStaffAreas) {
							if (!lstStaffArea.getAllEntities().contains(eColStaffArea.getArea())) {
								ENTITY_SRV.delete(eColStaffArea);
								logger.info("<<<<< Delete Successfully >>>>>");	
							}
						}
					}
					
					
					displaySuccessMsg();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (event.getButton() == btnCancel) {
			initStaffAndDestinationListSelect();
		}
		lblTotalAreas.setValue(I18N.message("total.areas") + " " + lstArea.getItemIds().size());	
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		initStaffAndDestinationListSelect();
		lblTotalAreas.setValue(I18N.message("total.areas") + " " + lstArea.getItemIds().size());	
	}
	
	/**
	 * 
	 * @param area
	 * @return
	 */
	private EColStaffArea getColStaffAreaByArea(Area area) {
		BaseRestrictions<EColStaffArea> restrictions = new BaseRestrictions<>(EColStaffArea.class);
		restrictions.addCriterion(Restrictions.eq("area", area));
		restrictions.addCriterion(Restrictions.eq("user", cbxStaff.getSelectedEntity()));
		List<EColStaffArea> colStaffAreas = ENTITY_SRV.list(restrictions);
		
		if (!colStaffAreas.isEmpty()) {
			return colStaffAreas.get(0);
		}
		
		return null;
	}
	
	/**
	 * reset
	 */
	public void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		cbxStaff.setSelectedEntity(null);
	}
	
}
