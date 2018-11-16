package com.nokor.efinance.core.collection.panel;


import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class FollowingResultPanel extends AbstractTabPanel{
	//Result
	private static final long serialVersionUID = -1812896520644286975L;
	private ERefDataComboBox<ETypeIdNumber> cbxResultType;
	private ERefDataComboBox<ETypeIdNumber> cbxResultOrigin;
	private ERefDataComboBox<ETypeIdNumber> cbxResultContactNo;
	private ERefDataComboBox<ETypeIdNumber> cbxResultContactPerson;
	private ERefDataComboBox<ETypeIdNumber> cbxResult;
	private ERefDataComboBox<ETypeIdNumber> cbxResultAction1;
	private ERefDataComboBox<ETypeIdNumber> cbxResultAction2;
	private TextArea txtResultRemarks;
	//Requests
	private ERefDataComboBox<ETypeIdNumber> cbxRequestOrigin;
	private ERefDataComboBox<ETypeIdNumber> cbxRequestContactNo;
	private ERefDataComboBox<ETypeIdNumber> cbxRequestContactPerson;
	private ERefDataComboBox<ETypeIdNumber> cbxRequest;
	private TextArea txtRequestRemarks;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		//Result
		cbxResultType = new ERefDataComboBox<>(I18N.message("type"), ETypeIdNumber.class);
		cbxResultType.setRequired(true);
		cbxResultOrigin = new ERefDataComboBox<>(I18N.message("origin"), ETypeIdNumber.class);
		cbxResultOrigin.setRequired(true);
		cbxResultContactNo = new ERefDataComboBox<>(I18N.message("contact.no"), ETypeIdNumber.class);
		cbxResultContactPerson = new ERefDataComboBox<>(I18N.message("contact.person"), ETypeIdNumber.class);
		cbxResultContactPerson.setRequired(true);
		cbxResult = new ERefDataComboBox<>(I18N.message("result"), ETypeIdNumber.class);
		cbxResult.setRequired(true);
		cbxResultAction1 = new ERefDataComboBox<>(I18N.message("action1"), ETypeIdNumber.class);
		cbxResultAction2 = new ERefDataComboBox<>(I18N.message("action2"), ETypeIdNumber.class);
		txtResultRemarks = ComponentFactory.getTextArea(I18N.message("remarks"), false, 230, 100);
		//Request
		cbxRequestOrigin = new ERefDataComboBox<>(I18N.message("origin"), ETypeIdNumber.class);
		cbxRequestOrigin.setRequired(true);
		cbxRequestContactNo = new ERefDataComboBox<>(I18N.message("contact.no"), ETypeIdNumber.class);
		cbxRequestContactPerson = new ERefDataComboBox<>(I18N.message("contact.person"), ETypeIdNumber.class);
		cbxRequestContactPerson.setRequired(true);
		cbxRequest = new ERefDataComboBox<>(I18N.message("request"), ETypeIdNumber.class);
		cbxRequest.setRequired(true);
		txtRequestRemarks = ComponentFactory.getTextArea(I18N.message("remarks"), false, 230, 100);
		
		VerticalLayout followingResultLayout = new VerticalLayout();
		followingResultLayout.setSpacing(true);
		followingResultLayout.setMargin(true);
		followingResultLayout.addComponent(createResultPanel());
		if (ProfileUtil.isColPhone() || ProfileUtil.isColBill()) {
			followingResultLayout.addComponent(createRequestPanel());
		}
		return followingResultLayout;
	}
	
	/**
	 * Save Following Result
	 */
	public void getFollowingResult() {
		
	}
	
	/**
	 * Take Following Result data to Form
	 */
	public void assignValue() {
		
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		cbxResultType.setSelectedEntity(null);
		cbxResultOrigin.setSelectedEntity(null);
		cbxResultContactNo.setSelectedEntity(null);
		cbxResultContactPerson.setSelectedEntity(null);
		cbxResult.setSelectedEntity(null);
		cbxResultAction1.setSelectedEntity(null);
		cbxResultAction2.setSelectedEntity(null);
		txtResultRemarks.setValue("");
		
		cbxRequestOrigin.setSelectedEntity(null);
		cbxRequestContactNo.setSelectedEntity(null);
		cbxRequestContactPerson.setSelectedEntity(null);
		cbxRequest.setSelectedEntity(null);
		txtRequestRemarks.setValue("");
		
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxResultType, "result.type");
		checkMandatorySelectField(cbxResultOrigin, "result.origin");
		checkMandatorySelectField(cbxResultContactPerson, "result.contact.person");
		checkMandatorySelectField(cbxResult, "result");
		
		checkMandatorySelectField(cbxRequestOrigin, "request.origin");
		checkMandatorySelectField(cbxRequestContactPerson, "request.contact.person");
		checkMandatorySelectField(cbxRequest, "request");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public Panel createResultPanel() {
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxResultType);
		formLayout.addComponent(cbxResultOrigin);
		formLayout.addComponent(cbxResultContactNo);
		formLayout.addComponent(cbxResultContactPerson);
		formLayout.addComponent(cbxResult);
		formLayout.addComponent(cbxResultAction1);
		formLayout.addComponent(cbxResultAction2);
		formLayout.addComponent(txtResultRemarks);
		
		Panel resultPanel = new Panel();
		resultPanel.setCaption(I18N.message("result"));
		resultPanel.setContent(formLayout);
		return resultPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	public Panel createRequestPanel() {
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxRequestOrigin);
		formLayout.addComponent(cbxRequestContactNo);
		formLayout.addComponent(cbxRequestContactPerson);
		formLayout.addComponent(cbxRequest);
		formLayout.addComponent(txtRequestRemarks);
		
		Panel requestPanel = new Panel();
		requestPanel.setCaption(I18N.message("request"));
		requestPanel.setContent(formLayout);
		return requestPanel;
	}

}
