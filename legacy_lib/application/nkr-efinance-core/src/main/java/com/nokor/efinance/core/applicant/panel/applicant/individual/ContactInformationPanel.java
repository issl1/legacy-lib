package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * LESSEE, SPOUSE, GUARANTOR in contact tab
 * layout in Contact panel 
 * @author uhout.cheng
 */
public class ContactInformationPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 1013986597078416443L;

	// Financial layout
	private TextField txtIncomeType;
	private TextField txtCategoryIncome;
	private TextField txtCompanyName;
	private TextField txtJobTitle;
	private TextArea txtCompanyAddress;
	// Personal layout
	private TextField txtTitle;
	private TextField txtPrefix;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtNickName;
	private TextField txtAge;
	private Button btnFullDetail;
	
		
	// Contact layout
	private GridLayout contactGridLayout;
	private CustomLayout customLayout;
	

	/**
	 * @return the btnApplicantFullDetailLink
	 */
	public Button getBtnFullDetail() {
		return btnFullDetail;
	}

	/**
	 * @param btnApplicantFullDetailLink the btnFullDetail to set
	 */
	public void setBtnFullDetail(Button btnFullDetail) {
		this.btnFullDetail = btnFullDetail;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField txtField = ComponentFactory.getTextField(60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button btnButton = ComponentFactory.getButton(caption);
		btnButton.setStyleName(Reindeer.BUTTON_LINK);
		return btnButton;
	} 
	
	/** */
	public ContactInformationPanel(String caption) {
		btnFullDetail = getButton("full.detail");
		
		txtIncomeType = getTextField();
		txtCategoryIncome = getTextField();
		txtCompanyName = getTextField();
		txtJobTitle = getTextField();
		txtCompanyAddress = ComponentFactory.getTextArea(false, 150, 50);
		txtCompanyAddress.setEnabled(false);
		
		txtTitle = getTextField();
		txtPrefix = getTextField();
		txtFirstName = getTextField();
		txtLastName = getTextField();
		txtNickName = getTextField();
		txtAge = getTextField();
		
		Panel mainPanel = getCustomLayoutPanel(caption);
		
		addComponent(mainPanel);
	}

	/**
	 * LESSEE, SPOUSE, GUARANTOR
	 * @param caption
	 * @return
	 */
	private Panel getCustomLayoutPanel(String caption) {
		String template = "contactTabLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/contact/" + template + ".html");
		customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addStyleName("overflow-layout-style");
		customLayout.addComponent(new Label(I18N.message("contact")), "lblContact");
		customLayout.addComponent(new Label(I18N.message("financial")), "lblFinacial");
		customLayout.addComponent(new Label(I18N.message("personal")), "lblPersonal");
		customLayout.addComponent(new Label(I18N.message("income.type")), "lblIncomeType");
		customLayout.addComponent(txtIncomeType, "txtIncomeType");
		customLayout.addComponent(new Label(I18N.message("title")), "lblTitle");
		customLayout.addComponent(txtTitle, "txtTitle");
		customLayout.addComponent(new Label(I18N.message("category.income")), "lblCategoryIncome");
		customLayout.addComponent(txtCategoryIncome, "txtCategoryIncome");
		customLayout.addComponent(new Label(I18N.message("prefix")), "lblPrefix");
		customLayout.addComponent(txtPrefix, "txtPrefix");
		customLayout.addComponent(new Label(I18N.message("company.name")), "lblCompanyName");
		customLayout.addComponent(txtCompanyName, "txtCompanyName");
		customLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		customLayout.addComponent(txtFirstName, "txtFirstName");
		customLayout.addComponent(new Label(I18N.message("job.title")), "lblJobTitle");
		customLayout.addComponent(txtJobTitle, "txtJobTitle");
		customLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		customLayout.addComponent(txtLastName, "txtLastName");
		customLayout.addComponent(new Label(I18N.message("address")), "lblCompanyAddress");
		customLayout.addComponent(txtCompanyAddress, "txtCompanyAddress");
		customLayout.addComponent(new Label(I18N.message("nickname")), "lblNickName");
		customLayout.addComponent(txtNickName, "txtNickName");
		customLayout.addComponent(new Label(I18N.message("age")), "lblAge");
		customLayout.addComponent(txtAge, "txtAge");
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(customLayout);
		mainLayout.addComponent(btnFullDetail);
		mainLayout.setComponentAlignment(btnFullDetail, Alignment.BOTTOM_RIGHT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.addComponent(mainLayout);
		
		Panel panel = ComponentFactory.getPanel(caption);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;background-color:#F5F5F5;margin:0;\" "
				+ "align=\"center\" >" + I18N.message(caption) + "</h2>");
		panel.setContent(horizontalLayout);

		return panel;
	}
	
	/**
	 * @param contract
	 * @param applicant
	 * @param applicantType
	 */
	public void assignValues(Applicant applicant, EApplicantType applicantType) {
		if (applicant != null && applicant.getIndividual() != null) {
			Individual individual = applicant.getIndividual();
			
			txtTitle.setValue(individual.getTitle() != null ? individual.getTitle().getDescEn() : "");
			txtPrefix.setValue(individual.getCivility() != null ? individual.getCivility().getDescEn() : "");
			txtFirstName.setValue(getDefaultString(individual.getFirstNameEn()));
			txtLastName.setValue(getDefaultString(individual.getLastNameEn()));
			txtNickName.setValue(getDefaultString(individual.getNickName()));
			Integer age = null;
			if (individual.getBirthDate() != null) {
				age = DateUtils.getNumberYearOfTwoDates(DateUtils.today(), individual.getBirthDate());
			}
			txtAge.setValue(getDefaultString(age));
			
			Employment employment = individual.getCurrentEmployment();
			if (employment != null) {
				txtIncomeType.setValue(employment.getEmploymentType() != null ? 
						getDefaultString(employment.getEmploymentType().getDescEn()) : "");
				txtCategoryIncome.setValue(employment.getEmploymentCategory() != null ? 
						getDefaultString(employment.getEmploymentCategory().getDescEn()) : "");
				txtCompanyName.setValue(getDefaultString(employment.getEmployerName()));
				txtJobTitle.setValue(individual.getCurrentEmployment() == null ? "" : 
					individual.getCurrentEmployment().getPosition());
				Address empAddress = employment.getAddress();
				if (empAddress != null) {
					setAddress(empAddress, txtCompanyAddress);
				}
			}
			int i = 1;
			List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
			List<IndividualContactInfo> indContactInfos = individual.getIndividualContactInfos();
			int lstContactSize = individualAddresses.size() + indContactInfos.size();
			if (lstContactSize > 0) {
				contactGridLayout = new GridLayout(2, lstContactSize + 2);
				contactGridLayout.setSpacing(true);
			}
			if (individualAddresses != null && !individualAddresses.isEmpty()) {
	        	contactGridLayout.addComponent(new Label("<b>" + I18N.message("contact.address") + "</b>", ContentMode.HTML), 0, 0);
				for (IndividualAddress individualAddress : individualAddresses) {
					Address address = individualAddress.getAddress();
					if (address != null) {
						TextArea txtContactAddress = ComponentFactory.getTextArea(false, 150, 50);
						txtContactAddress.setEnabled(false);
			    		setAddress(address, txtContactAddress);
			    		contactGridLayout.addComponent(new Label(address.getType() != null ? address.getType().getDescEn() : ""), 0, i);
			    		contactGridLayout.addComponent(txtContactAddress, 1, i);
			    		i++;	
					}
				}
			}
			if (indContactInfos != null && !indContactInfos.isEmpty()) {
				if (i == 1) {
					contactGridLayout.addComponent(new Label("<b>" + I18N.message("contact.no") + "</b>", ContentMode.HTML), 0, 
							individualAddresses.size());
				} else {
					i++;
					contactGridLayout.addComponent(new Label("<b>" + I18N.message("contact.no") + "</b>", ContentMode.HTML), 0, 
							individualAddresses.size() + 1);
				}
				for (IndividualContactInfo individualContactInfo : indContactInfos) {
					ContactInfo contactInfo = individualContactInfo.getContactInfo();
					if (contactInfo != null) {
						TextField txtContactPhone = getTextField();
			    		txtContactPhone.setValue(getDefaultString(contactInfo.getValue()));
			    		contactGridLayout.addComponent(new Label(I18N.message(contactInfo.getTypeInfo() != null ? 
			    				contactInfo.getTypeInfo().getDescEn() : "")), 0, i);
			    		contactGridLayout.addComponent(txtContactPhone, 1, i);
			    		i++;	
					}
				}
			}	
			if (contactGridLayout != null) {
				customLayout.addComponent(contactGridLayout, "contactLayout");
			}
		}
	}
	
	/**
	 * 
	 * @param address
	 * @param field
	 */
	private void setAddress(Address address, AbstractTextField field) {
		StringBuffer referenceName = new StringBuffer(); 
		List<String> descriptions = new ArrayList<String>();
		descriptions.add(getDefaultString(address.getHouseNo()));
		descriptions.add(getDefaultString(address.getLine1()));
		descriptions.add(getDefaultString(address.getLine2()));
		descriptions.add(getDefaultString(address.getStreet()));
		descriptions.add(address.getCommune() != null ? address.getCommune().getDescEn() : "");
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDescEn() : "");
		descriptions.add(address.getProvince() != null ? address.getProvince().getDescEn() : "");
		descriptions.add(getDefaultString(address.getPostalCode()));
		for (String string : descriptions) {
			referenceName.append(string);
			if (StringUtils.isNotEmpty(string)) {
				referenceName.append(",");
			}
		}
		int lastIndex = referenceName.lastIndexOf(",");
		referenceName.replace(lastIndex, lastIndex + 1, "");
		field.setValue(referenceName.toString());
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		contactGridLayout = null;
		txtIncomeType.setValue("");
		txtCategoryIncome.setValue("");
		txtCompanyName.setValue("");
		txtJobTitle.setValue("");
		txtCompanyAddress.setValue("");
		txtTitle.setValue("");
		txtPrefix.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtNickName.setValue("");
		txtAge.setValue("");
	}

	/**
	 * @return
	 */
	public boolean isValid() {		
		return errors.isEmpty();
	}
}
