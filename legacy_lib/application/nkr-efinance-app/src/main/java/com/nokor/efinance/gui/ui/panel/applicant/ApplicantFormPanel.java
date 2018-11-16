package com.nokor.efinance.gui.ui.panel.applicant;
import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.panel.EmploymentPanel;
import com.nokor.efinance.core.applicant.panel.IdentityPanel;
import com.nokor.efinance.core.applicant.panel.OtherInformationPanel;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Province form panel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ApplicantFormPanel extends AbstractFormPanel implements QuotationEntityField {

	private static final long serialVersionUID = -2022165013967292038L;
		
	private Applicant applicant;
    private IdentityPanel identityPanel;
	private AddressPanel addressFormPanel;
	private TabSheet applicantTabSheet;
	private VerticalLayout identityTab;
	private VerticalLayout employmentTab;
	private EmploymentPanel employmentPanel;
	private OtherInformationPanel otherInformationFormPanel;
	private QuotationsPanel quotationsPanel;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
	}
	
	@Override
	protected Applicant getEntity() {
		applicant = identityPanel.getApplicant(applicant);
		Address address = applicant.getIndividual().getMainAddress();
		if (address == null) {
			address = new Address();
			applicant.getIndividual().setAddress(address, ETypeAddress.MAIN);
		}
		addressFormPanel.getAddress(address);
		employmentPanel.getEmployments(applicant);
		otherInformationFormPanel.getApplicant(applicant);
		return applicant;
	}
	
	@Override
	public void saveEntity() {
		//applicant = applicantService.saveOrUpdateApplicant(getEntity());
		//assignValues(applicant.getId());
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		applicantTabSheet = new TabSheet();
		identityTab = new VerticalLayout();
		identityTab.setMargin(true);
		employmentTab = new VerticalLayout();
		identityPanel = new IdentityPanel("applicantIdentity");		
		final Panel addressPanel = new Panel(I18N.message("current.address"));
        addressFormPanel = new AddressPanel(true, ETypeAddress.HOME, "addressHorizontal");
        addressFormPanel.setMargin(true);
        addressPanel.setContent(addressFormPanel);        
        
        employmentPanel = new EmploymentPanel();
                
        otherInformationFormPanel = new OtherInformationPanel();        
        identityTab.addComponent(identityPanel);
        identityTab.addComponent(addressPanel);
        employmentTab.addComponent(employmentPanel);
               
        quotationsPanel = new QuotationsPanel();

        applicantTabSheet.addTab(identityTab, I18N.message("identity"));
        applicantTabSheet.addTab(employmentTab, I18N.message("employment"));
        applicantTabSheet.addTab(otherInformationFormPanel, I18N.message("other.information"));
        applicantTabSheet.addTab(quotationsPanel, I18N.message("quotations"));
        
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);        
        contentLayout.addComponent(applicantTabSheet);       
        return contentLayout;
	}

	/**
	 * @param asmakId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			applicant = ENTITY_SRV.getById(Applicant.class, id);
			identityPanel.assignValues(applicant);
			Address address = applicant.getIndividual().getMainAddress();
			if (address == null) {
				address = new Address();
			}
			addressFormPanel.assignValues(address);
		}
		employmentPanel.assignValues(applicant);
		otherInformationFormPanel.assignValues(applicant);
		quotationsPanel.assignValues(applicant);
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		applicant = new Applicant();
		identityPanel.reset();
		addressFormPanel.reset();
		employmentPanel.reset();
		otherInformationFormPanel.reset();
		quotationsPanel.reset();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		errors.addAll(otherInformationFormPanel.validate());
		errors.addAll(identityPanel.validate());
		return errors.isEmpty();
	}
}
