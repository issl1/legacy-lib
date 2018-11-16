package com.nokor.ersys.vaadin.ui.security.secuser.detail.profilegroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.ersys.vaadin.ui.security.vo.SecControlGroupVO;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlPrivilege;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SecProfileAppFormPopupPanel extends Window implements VaadinServicesHelper {

	/** */
	private static final long serialVersionUID = -4242227198298802629L;

	private Map<String, Object[]> cbControls;
	
	public SecProfileAppFormPopupPanel() {
		super(I18N.message("application.privileges"));		
		setModal(true);
		setResizable(false);
		setWidth(650, Unit.PIXELS);
		setHeight(800, Unit.PIXELS);
	}
	
	/**
	 * 
	 * @param secProfile
	 */
	public void setSelectByProfile(SecProfile secProfile) {
		List<SecControlProfilePrivilege> secControlPrivileges = SECURITY_SRV.listControlsPrivilegesByProfileId(secProfile.getId());
		setSelectControls(secControlPrivileges);
	}
	
	/**
	 * 
	 * @param secControlPrivileges
	 */
	private void setSelectControls(List<SecControlProfilePrivilege> secControlPrivileges) {
		if (secControlPrivileges != null && !secControlPrivileges.isEmpty()) {
			for (SecControlProfilePrivilege secControlPrivilege: secControlPrivileges) {
				String key = secControlPrivilege.getPrivilege().getId() + "-";
				key += secControlPrivilege.getControl().getId();
				key += secControlPrivilege.getControl().getCode();
				
				if (cbControls.containsKey(key)) {
					CheckBox checkBox = (CheckBox) cbControls.get(key)[0];
					checkBox.setValue(true);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param secApplication
	 * @return
	 */
	public List<SecControlGroupVO> loadControls(SecApplication secApplication) {
		cbControls = new HashMap<String, Object[]>();
		
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
	public void renderView(SecApplication secApplication) {
    	
		List<SecControlGroupVO> listGroup = loadControls(secApplication);
		
		final VerticalLayout controlGroupLayout = new VerticalLayout();
		controlGroupLayout.setStyleName("control-group-layout");
		controlGroupLayout.setEnabled(false);
		
    	if(listGroup != null && !listGroup.isEmpty()) {
    		// render the grid header
    		Label lblComponentHeader = ComponentFactory.getHtmlLabel("<b>" + I18N.message("component") + "</b>");
    		lblComponentHeader.setWidth(310, Unit.PIXELS);

    		// render the row for parent control
    		HorizontalLayout colHeader = new HorizontalLayout(lblComponentHeader);
    		colHeader.setStyleName("control-group-header");
    		
    		List<ESecPrivilege> secPrivileges = ENTITY_SRV.list(ESecPrivilege.class);
    		for (ESecPrivilege eSecPrivilege : secPrivileges) {
    			Label lblCheckBoxHeader = ComponentFactory.getHtmlLabel("<b>" + eSecPrivilege.getDesc() + "</b>");
    			lblCheckBoxHeader.setWidth(65, Unit.PIXELS);
        		colHeader.addComponent(lblCheckBoxHeader);
    		}
    		controlGroupLayout.addComponent(colHeader);
			
			int index = 1;
			
			for (SecControlGroupVO group : listGroup) {
				index = renderSubView(controlGroupLayout, group, index);
			}
    	}
    	String appLabel = "<font size='2'><b>" + I18N.message("application") + "</b>: ";
    	appLabel += "<p>" + secApplication.getDescEn() + "</p></font>";

    	final VerticalLayout applicationLayout = new VerticalLayout();
    	applicationLayout.setMargin(true);
    	applicationLayout.addComponent(ComponentFactory.getHtmlLabel(appLabel));
    	applicationLayout.addComponent(controlGroupLayout);
    	
    	setContent(applicationLayout);
	}
	
	/**
	 * 
	 * @param controlGroupLayout
	 * @param group
	 * @param index
	 * @return
	 */
	private int renderSubView(VerticalLayout controlGroupLayout, SecControlGroupVO group, int index) {
		int level = (int) group.getLevel();
		SecControl control = (SecControl) group.getControl();
		
		List<SecControlGroupVO> listSubGroup = group.getSubGroup();
		
		Label lblControl = ComponentFactory.getLabel(control.getDesc());
		lblControl.setWidth(300, Unit.PIXELS);
		lblControl.setStyleName("control-label-level-" + level);

		HorizontalLayout colRow = new HorizontalLayout(lblControl);
		colRow.setStyleName("control-group-row");
		
		List<SecControlPrivilege> controlPrivileges = SECURITY_SRV.listControlsPrivileges(control);
		List<ESecPrivilege> secPrivileges = ENTITY_SRV.list(ESecPrivilege.class);
		for (ESecPrivilege privilege : secPrivileges) {
			boolean hasPrivilege = hasControlPrivilege(controlPrivileges, privilege.getId());
			if (hasPrivilege) {
				final CheckBox cbPrivilege = new CheckBox();
				cbPrivilege.setValue(false);
				colRow.addComponent(cbPrivilege);
				
				String key = privilege.getId() + "-" + control.getId() + control.getCode();
				Object[] values = new Object[] {cbPrivilege, control, privilege.getId()};
				cbControls.put(key, values);
			} else {
				Label lblMinus = ComponentFactory.getLabel();
				lblMinus.setCaptionAsHtml(true);
				lblMinus.setWidth(65, Unit.PIXELS);
				colRow.addComponent(lblMinus);
			}
		}
		controlGroupLayout.addComponent(colRow);
		index++;
		
		if (listSubGroup != null) {
			for (SecControlGroupVO secSubControl : listSubGroup) {
				index = renderSubView(controlGroupLayout, secSubControl, index);
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
