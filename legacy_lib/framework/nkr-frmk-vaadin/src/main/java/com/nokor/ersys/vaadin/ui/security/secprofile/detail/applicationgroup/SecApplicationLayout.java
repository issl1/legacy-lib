package com.nokor.ersys.vaadin.ui.security.secprofile.detail.applicationgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.ersys.vaadin.ui.security.vo.PrivilegeCheckBoxGroupVO;
import com.nokor.ersys.vaadin.ui.security.vo.SecControlGroupVO;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlPrivilege;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class SecApplicationLayout extends VerticalLayout implements VaadinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 287017383185579772L;
	
	private static final int CONTROL_PREVILEGE_WIDTH = 75;
	
	private SecApplication secApplication;
	private Map<String, Object[]> cbControls;
	private Map<String, ValueChangeListener> listeners;
	private CheckBox cbApplication;
	private Map<Long, List<CheckBox>> cbPrivilegeGroupControls;
	private List<CheckBox> cbPrivilegeControls;
	
	/**
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cbApplication.getValue();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControlsCheckedAtLeastOne() {
		if (cbControls != null && !cbControls.isEmpty()) {
			for (Object[] values : cbControls.values()) {
				if (values != null) {
					CheckBox checkBox = (CheckBox) values[0];
					if (checkBox.getValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		setApplicationChecked(checked);
		setControlsChecked(checked);
	}
	
	/**
	 * Set Controls Checked
	 * @param checked
	 */
	public void setControlsChecked(boolean checked) {
		if (cbControls != null && !cbControls.isEmpty()) {
			for (Object[] values : cbControls.values()) {
				if (values != null) {
					CheckBox checkBox = (CheckBox) values[0];
					checkBox.setValue(checked);
				}
			}
		}
	}
	
	/**
	 * Set Application Checked
	 * @param checked
	 */
	public void setApplicationChecked(boolean checked) {
		if (cbApplication != null) {
			cbApplication.setValue(checked);
		}
	}
	
	/**
	 * 
	 * @param secControlPrivileges
	 */
	public void setSelectControls(List<SecControlProfilePrivilege> secControlPrivileges) {
		if (secControlPrivileges != null && !secControlPrivileges.isEmpty()) {
			for (SecControlProfilePrivilege secControlPrivilege: secControlPrivileges) {
				String key = secControlPrivilege.getPrivilege().getId() + "-";
				key += secControlPrivilege.getControl().getId();
				key += secControlPrivilege.getControl().getCode();
				
				if (cbControls.containsKey(key)) {
					CheckBox checkBox = (CheckBox) cbControls.get(key)[0];
					checkBox.removeValueChangeListener(listeners.get(key));
					checkBox.setValue(true);
					checkBox.addValueChangeListener(listeners.get(key));
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SecControlProfilePrivilege> getSelectedControlPrivileges() {
		List<SecControlProfilePrivilege> secControlPrivileges = new ArrayList<SecControlProfilePrivilege>();
		
		if (cbControls != null && !cbControls.isEmpty()) {
			for (Object[] values: cbControls.values()) {
				CheckBox checkBox = (CheckBox) values[0];
				
				if (checkBox.getValue()) {
					SecControlProfilePrivilege controlProfilePrivilege = new SecControlProfilePrivilege();
					controlProfilePrivilege.setStatusRecord(EStatusRecord.ACTIV);
					controlProfilePrivilege.setControl((SecControl) values[1]);
					
					controlProfilePrivilege.setPrivilege(ESecPrivilege.getById((Long) values[2]));
					
					secControlPrivileges.add(controlProfilePrivilege);
				}
			}
		}
		return secControlPrivileges;
	}
	
	/**
	 * 
	 * @return
	 */
	public SecApplication getSecApplication() {
		return secApplication;
	}
	
	/**
	 * 
	 * @param secApplication
	 * @return
	 */
	public List<SecControlGroupVO> loadControls(SecApplication secApplication) {
		cbControls = new HashMap<String, Object[]>();
		listeners = new HashMap<String, ValueChangeListener>();
		
		List<SecControlGroupVO> listControls = new ArrayList<SecControlGroupVO>();
		List<SecControl> mainControls = UserSessionManager.getCurrentUser().isSysAdmin() ?
				SECURITY_SRV.listParentControlsByAppId(secApplication.getId()) :
				SECURITY_SRV.listParentControlsByAppIdAndProfile(secApplication.getId(), UserSessionManager.getCurrentUser().getDefaultProfile().getId());
		
		if (mainControls != null) {
			for (SecControl mainControl : mainControls) {
				int level = 1;
				listControls.add(loadSubControls(mainControl, level));
			}
		}
		return listControls;
	}
	
	/**
	 * 
	 * @param control
	 * @param level
	 * @return
	 */
	public SecControlGroupVO loadSubControls(SecControl control, int level) {
		SecControlGroupVO group = new SecControlGroupVO();
		group.setLevel(level);
		group.setControl(control);
		
		List<SecControl> subControls = UserSessionManager.getCurrentUser().isSysAdmin() ?
				SECURITY_SRV.listControlsByParentId(control.getId()) :
				SECURITY_SRV.listControlsByParentIdAndProfile(control.getId(), UserSessionManager.getCurrentUser().getDefaultProfile().getId());
		if (subControls != null && subControls.size() > 0) {
			List<SecControlGroupVO> listGroup = new ArrayList<SecControlGroupVO>();
			
			for (SecControl subControl : subControls) {
				listGroup.add(loadSubControls(subControl, (level + 1)));
			}
			group.setSubGroup(listGroup);
		}
		return group;
	}
	
	/**
	 * 
	 * @param secApplication
	 */
	public void renderViewControls(SecApplication secApplication) {
		this.secApplication = secApplication;
		cbPrivilegeGroupControls = new HashMap<Long, List<CheckBox>>();
		cbPrivilegeControls = new ArrayList<CheckBox>();
		
		List<SecControlGroupVO> listGroup = loadControls(secApplication);

		final VerticalLayout controlGroupLayout = new VerticalLayout();
		controlGroupLayout.setStyleName("control-group-layout");
		controlGroupLayout.setEnabled(false);
		
    	if(listGroup != null && !listGroup.isEmpty()) {
    		// Check all CheckBox
    		CheckBox cbAll = new CheckBox(I18N.message("check.all"));
    		cbAll.addValueChangeListener(new ValueChangeListener() {
				/** */
				private static final long serialVersionUID = -2281937549032907029L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (cbPrivilegeControls != null) {
						for (CheckBox checkBox : cbPrivilegeControls) {
							checkBox.setValue(cbAll.getValue());
						}
					}
				}
			});
    		controlGroupLayout.addComponent(cbAll);
    		
    		// render the grid header
    		Label lblComponentHeader = ComponentFactory.getHtmlLabel("<b>" + I18N.message("component") + "</b>");
    		lblComponentHeader.setWidth(305, Unit.PIXELS);

    		// render the row for parent control
    		HorizontalLayout colHeader = new HorizontalLayout(lblComponentHeader);
    		colHeader.setStyleName("control-group-header");
    		
    		List<ESecPrivilege> secPrivileges = ENTITY_SRV.list(ESecPrivilege.class);
    		
    		for (ESecPrivilege eSecPrivilege : secPrivileges) {
    			CheckBox cbPrivilege = new CheckBox("<b>" + eSecPrivilege.getDescLocale() + "</b>");
    			cbPrivilege.setCaptionAsHtml(true);
    			cbPrivilege.setWidth(CONTROL_PREVILEGE_WIDTH, Unit.PIXELS);
    			colHeader.addComponent(cbPrivilege);
    			cbPrivilege.addValueChangeListener(new ValueChangeListener() {
					/** */
					private static final long serialVersionUID = 2751625802238914288L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						List<CheckBox> checkBoxs = cbPrivilegeGroupControls.get(eSecPrivilege.getId());
						if (checkBoxs != null && !checkBoxs.isEmpty()) {
							for (CheckBox checkBox : checkBoxs) {
								checkBox.setValue(cbPrivilege.getValue());
							}
						}
					}
				});
    			
    			cbPrivilegeControls.add(cbPrivilege);
    			cbPrivilegeGroupControls.put(eSecPrivilege.getId(), new ArrayList<CheckBox>());
    		}
    		controlGroupLayout.addComponent(colHeader);
    		
			int index = 1;
			for (SecControlGroupVO group : listGroup) {
				PrivilegeCheckBoxGroupVO cur = new PrivilegeCheckBoxGroupVO();
				index = renderSubView(controlGroupLayout, group, index, cur, null);
			}
    	}
    	
    	cbApplication = new CheckBox();
    	cbApplication.addStyleName("margin-left66");
    	cbApplication.setValue(false);
    	cbApplication.setCaption(secApplication.getDescLocale());
    	cbApplication.setData(secApplication);
    	cbApplication.setImmediate(true);
    	
    	// open the grid only when an application is selected
    	cbApplication.addValueChangeListener(new ValueChangeListener() {				
			/** */
			private static final long serialVersionUID = -5949412747027504458L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				controlGroupLayout.setEnabled(cbApplication.getValue());
			}
		});
    	
    	final VerticalLayout applicationLayout = new VerticalLayout();
    	applicationLayout.setMargin(true);
    	applicationLayout.addComponent(cbApplication);
    	applicationLayout.addComponent(new FormLayout(controlGroupLayout));
    	addComponent(new Panel(applicationLayout));
	}
	
	/**
	 * 
	 * @param controlGroupLayout
	 * @param group
	 * @param index
	 * @return
	 */
	private int renderSubView(VerticalLayout controlGroupLayout, SecControlGroupVO group, int index, PrivilegeCheckBoxGroupVO curCheckBoxGroupVO, PrivilegeCheckBoxGroupVO parentCheckBoxGroupVO) {
		int level = (int) group.getLevel();
		SecControl control = (SecControl) group.getControl();
		
		List<SecControlGroupVO> listSubGroup = group.getSubGroup();
		
		Label lblControl = ComponentFactory.getLabel(control.getDesc());
		lblControl.setWidth(300, Unit.PIXELS);
		lblControl.setStyleName("control-label-level-" + level);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(lblControl);

		HorizontalLayout colRow = new HorizontalLayout(verticalLayout);
		colRow.setStyleName("control-group-row");
		
		Map<Long, CheckBox> rowControls = new HashMap<Long, CheckBox>();
		List<SecControlPrivilege> controlPrivileges = SECURITY_SRV.listControlsPrivileges(control);
		
		List<ESecPrivilege> secPrivileges = ENTITY_SRV.list(ESecPrivilege.class);
		for (ESecPrivilege privilege : secPrivileges) {
			boolean hasPrivilege = hasControlPrivilege(controlPrivileges, privilege.getId());
			if (hasPrivilege) {
				final CheckBox cbPrivilege = new CheckBox();
				cbPrivilege.setValue(true);
				cbPrivilege.setWidth(CONTROL_PREVILEGE_WIDTH, Unit.PIXELS);
				
				String key = privilege.getId() + "-" + control.getId() + control.getCode();
				Object[] values = new Object[] {cbPrivilege, control, privilege.getId()};
				cbControls.put(key, values);
				rowControls.put(privilege.getId(), cbPrivilege);
				cbPrivilegeGroupControls.get(privilege.getId()).add(cbPrivilege);
				
				curCheckBoxGroupVO.addCheckBoxs(privilege.getId(), cbPrivilege);
				if (parentCheckBoxGroupVO != null) {
					curCheckBoxGroupVO.setParent(parentCheckBoxGroupVO);
					parentCheckBoxGroupVO.addChildren(curCheckBoxGroupVO);
				}
				ValueChangeListener listener = new ValueChangeListener() {
					/** */
					private static final long serialVersionUID = 5399286171462063403L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						curCheckBoxGroupVO.setValue(privilege.getId(), cbPrivilege.getValue(), true);
					}
				};
				cbPrivilege.addValueChangeListener(listener);
				listeners.put(key, listener);
				curCheckBoxGroupVO.addListeners(privilege.getId(), listener);
				
				VerticalLayout cellLayout = new VerticalLayout();
				cellLayout.addComponent(cbPrivilege);
				colRow.addComponent(cellLayout);
				cellLayout.addLayoutClickListener(new LayoutClickListener() {
					/** */
					private static final long serialVersionUID = 3114904998419435624L;
					@Override
					public void layoutClick(LayoutClickEvent event) {
						cbPrivilege.setValue(!cbPrivilege.getValue());
					}
				});
			} else {
				Label lblMinus = ComponentFactory.getLabel();
				lblMinus.setCaptionAsHtml(true);
				lblMinus.setWidth(CONTROL_PREVILEGE_WIDTH, Unit.PIXELS);
				colRow.addComponent(lblMinus);
			}
		}

		// lblControl click listener
		verticalLayout.addLayoutClickListener(new LayoutClickListener() {
			/** */
			private static final long serialVersionUID = 5967129525131650223L;
			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (rowControls != null) {
					for (CheckBox checkBox : rowControls.values()) {
						checkBox.setValue(!checkBox.getValue());
					}
				}
			}
		});
		controlGroupLayout.addComponent(colRow);
		index++;
		
		if (listSubGroup != null) {
			for (SecControlGroupVO secSubControl : listSubGroup) {
				PrivilegeCheckBoxGroupVO child = new PrivilegeCheckBoxGroupVO();
				index = renderSubView(controlGroupLayout, secSubControl, index, child, curCheckBoxGroupVO);
			}
		}
		
		return index;
	}
	
	/**
	 * 
	 * @param controlPrivileges
	 * @param privilegeId
	 * @return
	 */
	private boolean hasControlPrivilege(List<SecControlPrivilege> controlPrivileges, Long privilegeId) {
		for (SecControlPrivilege secControlPrivilege : controlPrivileges) {
			if (privilegeId.equals(secControlPrivilege.getPrivilege().getId())) {
				return true;
			}
		}
		return false;
	}
	
}
