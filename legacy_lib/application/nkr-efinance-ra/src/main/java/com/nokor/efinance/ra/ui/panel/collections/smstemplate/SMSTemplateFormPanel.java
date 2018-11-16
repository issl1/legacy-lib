package com.nokor.efinance.ra.ui.panel.collections.smstemplate;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SMSTemplateFormPanel extends AbstractFormPanel implements ValueChangeListener, ClickListener {

	
	private static final long serialVersionUID = 6815491670858108459L;
	
	private ESmsTemplate eSmsTemplate;
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	
	private TextArea txtContent;
	private ComboBox cbxField;
	private TextField txtField;
	private Button btnAdd;
	
	@javax.annotation.PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected Entity getEntity() {
		eSmsTemplate.setCode(txtCode.getValue());
		eSmsTemplate.setDescEn(txtDescEn.getValue());
		eSmsTemplate.setDesc(txtDesc.getValue());
		eSmsTemplate.setContent(txtContent.getValue());
		return eSmsTemplate;
	}

	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("", false, 60, 200);
		txtDesc = ComponentFactory.getTextField("", false, 60, 200);
		txtDescEn = ComponentFactory.getTextField("", false, 60, 200);
		txtContent = ComponentFactory.getTextArea(false, 456, 150);
		
		cbxField = ComponentFactory.getComboBox();
		cbxField.addItems("Current Date and Time",
						  "Customer ID",
						  "Fist Name",
						  "Last Name",
						  "Branch Name",
						  "Branch Address",
						  "Installment Amount",
						  "Validation Date");
		cbxField.addValueChangeListener(this);
		
		txtField = ComponentFactory.getTextField("", false, 60, 200);
		btnAdd = new Button(I18N.message("add"));
		btnAdd.addClickListener(this);
		
		String template = "letterAndSmsTemplate";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/template/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("code")), "lblCode");
		customLayout.addComponent(txtCode, "txtCode");
		customLayout.addComponent(new Label(I18N.message("desc.en")), "lblDescEn");
		customLayout.addComponent(txtDescEn, "txtDescEn");
		customLayout.addComponent(new Label(I18N.message("desc")), "lblDesc");
		customLayout.addComponent(txtDesc, "txtDesc");
		customLayout.addComponent(new Label(I18N.message("content")), "lblContent");
		customLayout.addComponent(txtContent, "txtContent");
		
		customLayout.addComponent(new Label(I18N.message("field")), "lblField");
		customLayout.addComponent(cbxField, "cbxField");
		customLayout.addComponent(txtField, "txtField");
		customLayout.addComponent(btnAdd, "btnAdd");
	
		return customLayout;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			eSmsTemplate = ENTITY_SRV.getById(ESmsTemplate.class, id);
			txtCode.setValue(eSmsTemplate.getCode());
			txtDescEn.setValue(eSmsTemplate.getDescEn());
			txtDesc.setValue(eSmsTemplate.getDesc());
			txtContent.setValue(eSmsTemplate.getContent());	
		}
	}
	
	/**
	 * reset
	 */
	public void reset() {
		super.reset();
		eSmsTemplate = new ESmsTemplate();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		
		txtContent.setValue("");
		cbxField.setValue(null);
		txtField.setValue("");
		
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkDuplicatedCode(ESmsTemplate.class, txtCode, eSmsTemplate.getId(), "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}

	@Override
	public void buttonClick(ClickEvent event) {
		StringBuffer buf = new StringBuffer(txtContent.getValue());
		buf.insert(txtContent.getCursorPosition(), txtField.getValue());
		txtContent.setValue(String.valueOf(buf));
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().toString() == "Current Date and Time") {
			txtField.setValue("(CURRENT_DATE-TIME)");
		} else if (event.getProperty().toString() == "Customer ID") {
			txtField.setValue("(CUSTOMER_ID)");
		} else if (event.getProperty().toString() == "Fist Name") {
			txtField.setValue("(FULL_NAME)");
		} else if (event.getProperty().toString() == "Last Name") {
			txtField.setValue("(LAST_NAME)");
		} else if (event.getProperty().toString() == "Branch Name") {
			txtField.setValue("(BRANCH_NAME)");
		} else if (event.getProperty().toString() == "Branch Address") {
			txtField.setValue("(BRANCH_ADDRESS)");
		} else if (event.getProperty().toString() == "Installment Amount") {
			txtField.setValue("(INSTALLMENT_AMOUNT)");
		} else if (event.getProperty().toString() == "Validation Date") {
			txtField.setValue("(VALIDATION_DATE)");
		}
		
	}

}
