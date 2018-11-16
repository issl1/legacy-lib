package com.nokor.efinance.core.contract.panel;

import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Contract Call Center Panel
 * @author bunlong.taing
 */
public class CallCenterPanel extends AbstractControlPanel implements AppServicesHelper {

	/** */
	private static final long serialVersionUID = 1934534148152460526L;
	
	private TextField txtType;
	private TextField txtOrigin;
	private ERefDataComboBox<EApplicantType> cbxContactedPersion;
	private ERefDataComboBox<ETypeContactInfo> cbxContactedNumber;
	
	private EntityRefComboBox<EColResult> cbxWelcomeResult;
	private TextArea txtWelcomeRemark;
	
	private EntityRefComboBox<EColResult> cbxCustomerResult;
	private TextArea txtCustomerRemark;
	
	private EntityRefComboBox<EColResult> cbxOthersResult;
	private TextArea txtOthersRemark;
	private ComboBox cbxAction1;
	
	/**
	 * Contract Call Center Panel Constructor
	 */
	public CallCenterPanel() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		createForm();
	}
	
	/**
	 * Create form
	 */
	private void createForm() {
		txtType = ComponentFactory.getTextField("type", false, 100, 200);
		txtOrigin = ComponentFactory.getTextField("origin", false, 100, 200);
		cbxContactedPersion = new ERefDataComboBox<EApplicantType>(I18N.message("person.contact"), EApplicantType.class);
		cbxContactedPersion.setWidth(200, Unit.PIXELS);
		cbxContactedNumber = new ERefDataComboBox<ETypeContactInfo>(I18N.message("contacted.number"), ETypeContactInfo.class);
		cbxContactedNumber.setWidth(200, Unit.PIXELS);
		
		List<EColResult> results = ENTITY_SRV.list(new BaseRestrictions<>(EColResult.class));
		cbxWelcomeResult = new EntityRefComboBox<EColResult>(I18N.message("result"), results);
		cbxWelcomeResult.setWidth(200, Unit.PIXELS);
		txtWelcomeRemark = ComponentFactory.getTextArea("remarks", false, 300, 100);
		
		cbxCustomerResult = new EntityRefComboBox<EColResult>(I18N.message("result"), results);
		cbxCustomerResult.setWidth(200, Unit.PIXELS);
		txtCustomerRemark = ComponentFactory.getTextArea("remarks", false, 300, 100);
		
		cbxOthersResult = new EntityRefComboBox<EColResult>(I18N.message("result"), results);
		cbxOthersResult.setWidth(200, Unit.PIXELS);
		txtOthersRemark = ComponentFactory.getTextArea("remarks", false, 300, 100);
		cbxAction1 = new ComboBox(I18N.message("action1"));
		cbxAction1.setWidth(200, Unit.PIXELS);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.addComponent(txtType);
		formLayout.addComponent(txtOrigin);
		formLayout.addComponent(cbxContactedPersion);
		formLayout.addComponent(cbxContactedNumber);
		addComponent(formLayout);
		
		addComponent(new Label("<b>" + I18N.message("result.welcome.call") + "</B>", ContentMode.HTML));
		formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.addComponent(cbxWelcomeResult);
		formLayout.addComponent(txtWelcomeRemark);
		addComponent(formLayout);
		
		addComponent(new Label("<b>" + I18N.message("result.customer.relations") + "</B>", ContentMode.HTML));
		formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.addComponent(cbxCustomerResult);
		formLayout.addComponent(txtCustomerRemark);
		addComponent(formLayout);
		
		addComponent(new Label("<b>" + I18N.message("result.others") + "</B>", ContentMode.HTML));
		formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.addComponent(cbxOthersResult);
		formLayout.addComponent(txtOthersRemark);
		formLayout.addComponent(cbxAction1);
		addComponent(formLayout);
	}
}
