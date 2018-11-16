package com.nokor.efinance.core.contract.panel;

import java.util.List;

import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
/**
 * Legal panel in CM_Profile
 * @author uhout.cheng
 */
public class LegalPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -7235339278242290177L;
	
	private ComboBox cbxType;
	private ERefDataComboBox<EColType> cbxOrigin;
	private ERefDataComboBox<EApplicantType> cbxContactPerson;
	private ERefDataComboBox<ETypeContactInfo> cbxContactNumber;
	private ComboBox cbxTagStatus;
	private ComboBox cbxSuedType;
	private ComboBox cbxResult;
	private TextArea txtRemarks;
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(String caption, List<T> values, boolean required) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(I18N.message(caption), values);
		comboBox.setRequired(required);
		comboBox.setWidth(200, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private ComboBox getComboBox(String caption, boolean required) {
		ComboBox cbx = ComponentFactory.getComboBox(caption, null);
		cbx.setWidth(200, Unit.PIXELS);
		cbx.setRequired(required);
		return cbx;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		return formLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxType = getComboBox("type", true);
		cbxTagStatus = getComboBox("tag.status", false);
		cbxSuedType = getComboBox("sued.type", false);
		cbxResult = getComboBox("result", false);
		cbxOrigin = getERefDataComboBox("origin", EColType.values(), true);
		cbxContactPerson = getERefDataComboBox("contact.person", EApplicantType.values(), true);
		cbxContactNumber = getERefDataComboBox("contact.no", ETypeContactInfo.values(), true);
		txtRemarks = ComponentFactory.getTextArea("remarks", false, 250, 50);
		
		FormLayout frmLayout = getFormLayout();
		frmLayout.addComponent(cbxType);
		frmLayout.addComponent(cbxOrigin);
		frmLayout.addComponent(cbxContactPerson);
		frmLayout.addComponent(cbxContactNumber);
		frmLayout.addComponent(cbxTagStatus);
		frmLayout.addComponent(cbxSuedType);
		frmLayout.addComponent(cbxResult);
		frmLayout.addComponent(txtRemarks);
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(frmLayout);
		
		return mainLayout;
	}

	/**
	 * Reset controls
	 */
	public void reset() {
		cbxType.setValue(null);
		cbxTagStatus.setValue(null);
		cbxResult.setValue(null);
		cbxOrigin.setSelectedEntity(null);
		cbxContactPerson.setSelectedEntity(null);
		cbxContactNumber.setSelectedEntity(null);
		txtRemarks.setValue("");
	}
	
}
