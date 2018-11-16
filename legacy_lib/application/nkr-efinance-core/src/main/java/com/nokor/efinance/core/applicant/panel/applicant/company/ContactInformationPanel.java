package com.nokor.efinance.core.applicant.panel.applicant.company;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 * @author uhout.cheng
 */
public class ContactInformationPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 6787332596395470878L;

	private TextField txtCode;
	private TextField txtNameEn;
	private TextField txtName;
	private TextField txtDescEn;
	private TextField txtDesc;
	private TextField txtMobile;

	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout(boolean margin) {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.setMargin(margin);
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField txtField = ComponentFactory.getTextField(caption, false, 60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @param caption
	 * @param horLayout
	 * @return
	 */
	private Panel getPanel(String caption, HorizontalLayout horLayout) {
		Panel mainPanel = new Panel();
		mainPanel.setCaptionAsHtml(true);
		mainPanel.setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;background-color:#F5F5F5;margin:0;\" "
				+ "align=\"center\" >" + I18N.message(caption) + "</h2>");
		mainPanel.setContent(horLayout);
		return mainPanel;
	}
	
	
	/**
	 * 
	 * @param caption
	 */
	public ContactInformationPanel(String caption) {
		txtCode = getTextField("code");
		txtNameEn = getTextField("name.en");
		txtName = getTextField("name");
		txtDescEn = getTextField("desc.en");
		txtDesc = getTextField("desc");
		txtMobile = getTextField("mobile");
		
		Panel mainPanel = getCompanyInfoLayout(caption);
		
		addComponent(mainPanel);
	}

	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Panel getCompanyInfoLayout(String caption) {
		FormLayout frmLayout = getFormLayout(true);
		frmLayout.addComponent(txtCode);
		frmLayout.addComponent(txtNameEn);
		frmLayout.addComponent(txtName);
		frmLayout.addComponent(txtDescEn);
        frmLayout.addComponent(txtDesc);
        frmLayout.addComponent(txtMobile);
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(frmLayout);
		Panel mainPanel = getPanel(caption, horLayout);
		return mainPanel;
	}
	
	/**
	 * 
	 * @param company
	 */
	public void assignValues(Company company) {
		if (company != null) {
			txtCode.setValue(company.getCode());
			txtNameEn.setValue(company.getNameEn());
			txtName.setValue(company.getName());
			txtDescEn.setValue(company.getDescEn());
			txtDesc.setValue(company.getDesc());
			txtMobile.setValue(company.getMobile());
		}
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		txtCode.setValue("");
		txtNameEn.setValue("");
		txtName.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtMobile.setValue("");
	}
}
