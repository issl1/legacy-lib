package com.nokor.efinance.gui.ui.panel.applicant;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.ApplicantRestriction;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Province search
 * @author youhort.ly
 *
 */
public class ApplicantSearchPanel extends AbstractSearchPanel<Applicant> implements FMEntityField {

	private static final long serialVersionUID = -3274751037886130600L;
	
	private ERefDataComboBox<EApplicantCategory> cbxApplicantCategory;
	
	private TextField txtNickName;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtIdNo;
	private TextField txtCustomerID;
	private TextField txtCompany;
	
	private VerticalLayout content;

	/**
	 * @param customerTablePanel
	 */
	public ApplicantSearchPanel(ApplicantTablePanel customerTablePanel) {
		super(I18N.message("applicant.search"), customerTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Label lblApplicantCategory = ComponentFactory.getLabel("applicant.category");
		cbxApplicantCategory = new ERefDataComboBox<EApplicantCategory>(EApplicantCategory.values());
		cbxApplicantCategory.setNullSelectionAllowed(false);
		
		GridLayout applicantCategoryGridLayout = new GridLayout(2, 1);
		applicantCategoryGridLayout.setSpacing(true);
		applicantCategoryGridLayout.addComponent(lblApplicantCategory);
		applicantCategoryGridLayout.addComponent(cbxApplicantCategory);
		applicantCategoryGridLayout.setComponentAlignment(lblApplicantCategory, Alignment.MIDDLE_LEFT);
		
		GridLayout individualLayout = createIndividualLayout();
		individualLayout.setVisible(false);
		
		GridLayout companyLayout = createCompanyLayout();
		companyLayout.setVisible(false);
		
		cbxApplicantCategory.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -5807775966876839200L;
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				EApplicantCategory selected = cbxApplicantCategory.getSelectedEntity();
				individualLayout.setVisible(EApplicantCategory.INDIVIDUAL.equals(selected) || EApplicantCategory.GLSTAFF.equals(selected));
				companyLayout.setVisible(EApplicantCategory.COMPANY.equals(selected));
			}
		});
		
        content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(applicantCategoryGridLayout);
        content.addComponent(individualLayout);
        content.addComponent(companyLayout);
        
		return content;
	}
	
	/**
	 * Create Inidividual Layout
	 * @return
	 */
	private GridLayout createIndividualLayout() {
		Label lblNickName = ComponentFactory.getLabel("nickname");
		Label lblFirstName = ComponentFactory.getLabel("firstname");
		Label lblLastName = ComponentFactory.getLabel("lastname");
		Label lblIdNo = ComponentFactory.getLabel("id.no");
		Label lblCustomerId = ComponentFactory.getLabel("customer.id");
		
		txtNickName = ComponentFactory.getTextField(60, 150);
		txtFirstName = ComponentFactory.getTextField(60, 150);        
		txtLastName = ComponentFactory.getTextField(60, 150);
		txtIdNo = ComponentFactory.getTextField(60, 150);
		txtCustomerID = ComponentFactory.getTextField(60, 150);
		
		GridLayout individualLayout = new GridLayout(6, 2);
		individualLayout.setSpacing(true);
		
		individualLayout.addComponent(lblFirstName);
		individualLayout.addComponent(txtFirstName);
		individualLayout.addComponent(lblLastName);
		individualLayout.addComponent(txtLastName);
		individualLayout.addComponent(lblNickName);
		individualLayout.addComponent(txtNickName);
		
		individualLayout.addComponent(lblIdNo);
		individualLayout.addComponent(txtIdNo);
		individualLayout.addComponent(lblCustomerId);
		individualLayout.addComponent(txtCustomerID);
		
		individualLayout.setComponentAlignment(lblFirstName, Alignment.MIDDLE_LEFT);
		individualLayout.setComponentAlignment(lblLastName, Alignment.MIDDLE_LEFT);
		individualLayout.setComponentAlignment(lblNickName, Alignment.MIDDLE_LEFT);
		individualLayout.setComponentAlignment(lblIdNo, Alignment.MIDDLE_LEFT);
		individualLayout.setComponentAlignment(lblCustomerId, Alignment.MIDDLE_LEFT);
		
		return individualLayout;
	}
	
	/**
	 * Create Company Layout
	 * @return
	 */
	private GridLayout createCompanyLayout() {
		Label lblCompany = ComponentFactory.getLabel("company");
		txtCompany = ComponentFactory.getTextField(60, 150);
		
		GridLayout companyLayout = new GridLayout(2, 1);
		companyLayout.setSpacing(true);
		companyLayout.addComponent(lblCompany);
		companyLayout.addComponent(txtCompany);
		companyLayout.setComponentAlignment(lblCompany, Alignment.MIDDLE_LEFT);
		
		return companyLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Applicant> getRestrictions() {
		ApplicantRestriction restriction = new ApplicantRestriction();
		restriction.setApplicantCategory(cbxApplicantCategory.getSelectedEntity());
		restriction.setNickName(txtNickName.getValue());
		restriction.setFirstName(txtFirstName.getValue());
		restriction.setLastName(txtLastName.getValue());
		restriction.setIdNumber(txtIdNo.getValue());
		restriction.setApplicantID(txtCustomerID.getValue());
		restriction.setCompanyName(txtCompany.getValue());
		return restriction;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtNickName.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtIdNo.setValue("");
		txtCustomerID.setValue("");
		txtCompany.setValue("");
	}

}
