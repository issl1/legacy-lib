package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

/**
 * Resource Pop up Panel
 * @author bunlong.taing
 */
public class ResourceDetailPanel extends AbstractTabPanel implements FinServicesHelper, SaveClickListener {
	
	/**
	 */
	private static final long serialVersionUID = -5586151445544386806L;

	private CheckBox cbEnableAssignContracts;
	
	private SecUser secUser;

	/**
	 * 
	 * @param profileCode
	 */
	public ResourceDetailPanel() {
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		addComponentAsFirst(navigationPanel);
	}
		
	@Override
	protected Component createForm() {
		
		cbEnableAssignContracts = new CheckBox(I18N.message("enable.assign.contracts"));
		cbEnableAssignContracts.setValue(true);
		
		FormLayout controlLayout = new FormLayout();
		controlLayout.setSpacing(true);
		controlLayout.addComponent(cbEnableAssignContracts);
		
		return controlLayout;
	}
		
	/**
	 * Assign control layout values
	 * @param user
	 */
	public void assignValues(SecUser secUser) {
		this.secUser = secUser;
		SecUserDetail userDetail = getUserDetail(secUser);
		if (userDetail == null) {
			cbEnableAssignContracts.setValue(true);
		} else {
			cbEnableAssignContracts.setValue(userDetail.isEnableAssignContracts());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		save();
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (validate()) {
			SecUserDetail userDetail = getUserDetail(secUser);
			if (userDetail == null) {
				userDetail = new SecUserDetail();
				userDetail.setSecUser(secUser);
			}
			userDetail.setEnableAssignContracts(cbEnableAssignContracts.getValue());
			ENTITY_SRV.saveOrUpdate(userDetail);
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Validate
	 */
	private boolean validate() {
		errors.clear();
		return errors.isEmpty();
	}
	
	/**
	 * Get User Detail
	 * @param user
	 * @return
	 */
	private SecUserDetail getUserDetail(SecUser secUser) {
		BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<>(SecUserDetail.class);
		restrictions.addCriterion(Restrictions.eq(SecUserDetail.SECUSER + "." + SecUserDetail.ID, secUser.getId()));
		List<SecUserDetail> userDetails = ENTITY_SRV.list(restrictions);
		if (!userDetails.isEmpty()) {
			return userDetails.get(0);
		}
		return null;
	}
}
