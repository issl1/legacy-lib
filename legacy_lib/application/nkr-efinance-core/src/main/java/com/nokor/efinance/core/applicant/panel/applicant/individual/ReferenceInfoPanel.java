package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.applicant.model.EIndividualReferenceType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.eref.ERelationship;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Full detail reference in contact tab
 * @author uhout.cheng
 */	
public class ReferenceInfoPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5676411412125311717L;

	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	//Reference
	private ERefDataComboBox<EIndividualReferenceType> cbxReferenceType;
	private ERefDataComboBox<ERelationship> cbxReferenceRelationship;
	private TextField txtReferenceFirstName;
	private TextField txtReferenceLastName;
	private boolean isAddNewReferenceInfo;
	private ContactInfoPanel contactInfoPanel;
	private Button btnAddReferenceContactInfo;
	private Button btnCloseReferenceContactInfo;
	private VerticalLayout contactInfoLayout;
	private List<ContactInfoPanel> contactInfoPanels;
	
	private ReferenceContactPhonePanel referenceContactPhonePanel;

	/** */
	public ReferenceInfoPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeUndefined();
		contactInfoLayout = new VerticalLayout();
		contactInfoPanel = new ContactInfoPanel();
		contactInfoPanels = new ArrayList<>();
		contactInfoLayout.addComponent(contactInfoPanel);
		contactInfoPanels.add(contactInfoPanel);
		btnAddReferenceContactInfo = new Button(I18N.message("add.contact"));
		btnAddReferenceContactInfo.addClickListener(this);
			
		referenceContactPhonePanel = new ReferenceContactPhonePanel();
		
		verticalLayout.addComponent(createContractInfoPanel());
		verticalLayout.addComponent(referenceContactPhonePanel);
		//verticalLayout.addComponent(contactInfoLayout);
		//verticalLayout.addComponent(btnAddReferenceContactInfo);
		//verticalLayout.setComponentAlignment(btnAddReferenceContactInfo, Alignment.BOTTOM_CENTER);

		return verticalLayout;
	}
	
	/**
	 * 
	 * @param referenceInfoPanels
	 * @param individual
	 * @param listContainListOfContactInfoPanel
	 */
	public void getReferenceInfo(List<ReferenceInfoPanel> referenceInfoPanels, Individual individual, List<List<ContactInfoPanel>> listContainListOfContactInfoPanel){
		List<IndividualReferenceInfo> individualReferenceInfos = individual.getIndividualReferenceInfos();
		int numAddNewReferenceInfo = referenceInfoPanels.size() - individualReferenceInfos.size();
		int index = 0;
		List<IndividualReferenceInfo> individualReferenceInfos2 = new ArrayList<>();
		//Update & add new Reference
		isAddNewReferenceInfo = false;
		for (int i = 0; i<= numAddNewReferenceInfo; i++) {
			if (!isAddNewReferenceInfo) {
				for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
					//Update ApplicantReferenceInfo
					individualReferenceInfo.setReferenceType(referenceInfoPanels.get(index).cbxReferenceType.getSelectedEntity());
					individualReferenceInfo.setRelationship(referenceInfoPanels.get(index).cbxReferenceRelationship.getSelectedEntity());
					individualReferenceInfo.setFirstNameEn(referenceInfoPanels.get(index).txtReferenceFirstName.getValue());
					individualReferenceInfo.setLastNameEn(referenceInfoPanels.get(index).txtReferenceLastName.getValue());
					individualReferenceInfo.setIndividual(individual);
					if (individualReferenceInfo.getReferenceType() != null) {
						entityService.saveOrUpdate(individualReferenceInfo);
					}
					List<ContactInfoPanel> contactInfoPanels = listContainListOfContactInfoPanel.get(index);
					index++;
					List<IndividualReferenceContactInfo> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();
					
					int numAddReferenceContactInfo = contactInfoPanels.size() - individualReferenceContactInfos.size();
					boolean isAddNewContactInfo = false;
					int contactIndex = 0;
					
					for (int j = 0; j <= numAddReferenceContactInfo; j++) {
						if (!isAddNewContactInfo) {
							for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
								ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
								if (contactInfo != null) {
									//Update Contact Info 
									contactInfo.setTypeInfo(contactInfoPanels.get(contactIndex).cbxContactInfoType.getSelectedEntity());
									contactInfo.setTypeAddress(contactInfoPanels.get(contactIndex).cbxContactAddressType.getSelectedEntity());
									contactInfo.setValue(contactInfoPanels.get(contactIndex).txtValue.getValue());
									contactIndex++;
									entityService.saveOrUpdate(contactInfo);
								}
							}
							isAddNewContactInfo = true;
						} else {
							//Add new Contact Info
							ContactInfo contactInfo = new ContactInfo();
							contactInfo.setTypeInfo(contactInfoPanels.get(contactIndex).cbxContactInfoType.getSelectedEntity());
							contactInfo.setTypeAddress(contactInfoPanels.get(contactIndex).cbxContactAddressType.getSelectedEntity());
							contactInfo.setValue(contactInfoPanels.get(contactIndex).txtValue.getValue());
							//index++;
							entityService.saveOrUpdate(contactInfo);
							
							//New ApplicantReferenceContactInfo
							IndividualReferenceContactInfo individualReferenceContactInfo = new IndividualReferenceContactInfo();
							individualReferenceContactInfo.setContactInfo(contactInfo);
							individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
							individualReferenceContactInfos.add(individualReferenceContactInfo);
							entityService.saveOrUpdate(individualReferenceContactInfo);
						}
					}
					individualReferenceInfos2.add(individualReferenceInfo);
				}
				isAddNewReferenceInfo = true;
			}else {
				//Add New ApplicantReferenceInfo
				IndividualReferenceInfo individualReferenceInfo = new IndividualReferenceInfo();
				individualReferenceInfo.setReferenceType(referenceInfoPanels.get(index).cbxReferenceType.getSelectedEntity());
				individualReferenceInfo.setRelationship(referenceInfoPanels.get(index).cbxReferenceRelationship.getSelectedEntity());
				individualReferenceInfo.setFirstNameEn(referenceInfoPanels.get(index).txtReferenceFirstName.getValue());
				individualReferenceInfo.setLastNameEn(referenceInfoPanels.get(index).txtReferenceLastName.getValue());
				individualReferenceInfo.setIndividual(individual);
				if (individualReferenceInfo.getReferenceType() != null) {
					entityService.saveOrUpdate(individualReferenceInfo);
					List<IndividualReferenceContactInfo> individualReferenceContactInfos = new ArrayList<>();
					for (ContactInfoPanel contactInfoPanel : contactInfoPanels) {
						//Add New Contact info 
						ContactInfo contactInfo = new ContactInfo();
						contactInfo.setTypeInfo(contactInfoPanel.cbxContactInfoType.getSelectedEntity());
						contactInfo.setTypeAddress(contactInfoPanel.cbxContactAddressType.getSelectedEntity());
						contactInfo.setValue(contactInfoPanel.txtValue.getValue());
						entityService.saveOrUpdate(contactInfo);
						
						//New ApplicantReferenceContactInfo
						IndividualReferenceContactInfo individualReferenceContactInfo = new IndividualReferenceContactInfo();
						individualReferenceContactInfo.setContactInfo(contactInfo);
						individualReferenceContactInfo.setIndividualReferenceInfo(individualReferenceInfo);
						entityService.saveOrUpdate(individualReferenceContactInfo);
						
						//New List ApplicantReferenceContactInfos
						individualReferenceContactInfos.add(individualReferenceContactInfo);
						
					}
					individualReferenceInfo.setIndividualReferenceContactInfos(individualReferenceContactInfos);
					individualReferenceInfos2.add(individualReferenceInfo);
				}
				index++;	
			}
		}
		individual.setIndividualReferenceInfos(individualReferenceInfos2);
	}
	
	/**
	 * 
	 * @param individual
	 * @param indRefInfo
	 */
	public void setReferenceInfo(Individual individual, IndividualReferenceInfo indRefInfo) {
		indRefInfo.setReferenceType(cbxReferenceType.getSelectedEntity());
		indRefInfo.setRelationship(cbxReferenceRelationship.getSelectedEntity());
		indRefInfo.setFirstNameEn(txtReferenceFirstName.getValue());
		indRefInfo.setLastNameEn(txtReferenceLastName.getValue());
		indRefInfo.setIndividual(individual);
		INDIVI_SRV.saveOrUpdate(indRefInfo);
		INDIVI_SRV.refresh(indRefInfo);
		assignValues(indRefInfo);
	}

	/**
	 * 
	 * @param individualReferenceInfo
	 */
	public void assignValues(IndividualReferenceInfo individualReferenceInfo) {
		cbxReferenceType.setSelectedEntity(individualReferenceInfo.getReferenceType());
		cbxReferenceRelationship.setSelectedEntity(individualReferenceInfo.getRelationship());
		txtReferenceFirstName.setValue(individualReferenceInfo.getFirstNameEn());
		txtReferenceLastName.setValue(individualReferenceInfo.getLastNameEn());
		
		referenceContactPhonePanel.assignValue(individualReferenceInfo);
	}
	
	
	
	/**
	 * 
	 * @param individualReferenceInfo
	 * @return
	 */
	public List<ContactInfoPanel> assignValue(IndividualReferenceInfo individualReferenceInfo) {
		cbxReferenceType.setSelectedEntity(individualReferenceInfo.getReferenceType());
		cbxReferenceRelationship.setSelectedEntity(individualReferenceInfo.getRelationship());
		txtReferenceFirstName.setValue(individualReferenceInfo.getFirstNameEn());
		txtReferenceLastName.setValue(individualReferenceInfo.getLastNameEn());
		
		referenceContactPhonePanel.assignValue(individualReferenceInfo);
		
		contactInfoPanels.clear();
		contactInfoLayout.removeAllComponents();
		List<IndividualReferenceContactInfo> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();
		if (individualReferenceContactInfos != null && !individualReferenceContactInfos.isEmpty()) {
			for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
				ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
				if (contactInfo != null) {
					contactInfoPanel = new ContactInfoPanel();
					contactInfoPanel.assignValue(contactInfo);
					contactInfoLayout.addComponent(contactInfoPanel);
					contactInfoPanels.add(contactInfoPanel);
				}
			}
		}
		return contactInfoPanels;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout createContractInfoPanel(){
		//Reference 
		cbxReferenceType = new ERefDataComboBox<>(EIndividualReferenceType.class);
		cbxReferenceType.setWidth(190, Unit.PIXELS);
		cbxReferenceRelationship = new ERefDataComboBox<>(null, ERelationship.class);
		cbxReferenceRelationship.setWidth(190, Unit.PIXELS);
		txtReferenceFirstName = ComponentFactory.getTextField(60, 190);
		txtReferenceLastName = ComponentFactory.getTextField(60, 190);
				
		String template = "referenceInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout contactCustomLayout = null;
		try {
			contactCustomLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
				
		contactCustomLayout.addComponent(new Label(I18N.message("reference.type")), "lblReferenceType");
		contactCustomLayout.addComponent(cbxReferenceType, "cbxReferenceType");
		contactCustomLayout.addComponent(new Label(I18N.message("reference.relationship")), "lblReferenceRelationship");
		contactCustomLayout.addComponent(cbxReferenceRelationship, "cbxReferenceRelationship");
		contactCustomLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		contactCustomLayout.addComponent(txtReferenceFirstName, "txtReferenceFirstName");
		contactCustomLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		contactCustomLayout.addComponent(txtReferenceLastName, "txtReferenceLastName");
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(contactCustomLayout);	
		return verticalLayout;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		removeErrorsPanel();
		cbxReferenceType.setSelectedEntity(null);
		cbxReferenceRelationship.setSelectedEntity(null);
		txtReferenceFirstName.setValue("");
		txtReferenceLastName.setValue("");
	}
	
	/**
	 * @return
	 */
	public List<String> isValid() {		
		removeErrorsPanel();
		checkMandatorySelectField(cbxReferenceType, "reference.type");
		checkMandatorySelectField(cbxReferenceRelationship, "reference.relationship");
		checkMandatoryField(txtReferenceFirstName, "firstname.en");
		checkMandatoryField(txtReferenceLastName, "lastname.en");
//		for (ContactInfoPanel contactInfoPanel : contactInfoPanels) {
//			List<String> contactInfoErrors = contactInfoPanel.isValid();
//			if (contactInfoErrors != null && !contactInfoErrors.isEmpty()) {
//				errors.addAll(contactInfoPanel.isValid());
//			}
//		}
		return errors;
	}
	
	/**
	 * Click to add Contact info in Reference
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		btnCloseReferenceContactInfo = new Button("X");
		btnCloseReferenceContactInfo.setStyleName(Reindeer.BUTTON_LINK);
//		newContactInfoLayout = new VerticalLayout();
		VerticalLayout newContactInfoLayout = new VerticalLayout();
		ContactInfoPanel contactInfoPanel = new ContactInfoPanel();
		btnCloseReferenceContactInfo.addClickListener(new CancelAddReferenceContactInfo(newContactInfoLayout, contactInfoPanel));
//		contactInfoPanel = new ContactInfoPanel();
		newContactInfoLayout.addComponent(btnCloseReferenceContactInfo);
		newContactInfoLayout.setComponentAlignment(btnCloseReferenceContactInfo, Alignment.MIDDLE_RIGHT);
		newContactInfoLayout.addComponent(contactInfoPanel);
		contactInfoLayout.addComponent(newContactInfoLayout);
		contactInfoPanels.add(contactInfoPanel);
	}
	
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddReferenceContactInfo implements ClickListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7534827815805759534L;
		private VerticalLayout contactInfoLayouts;
		private ContactInfoPanel contactInfoPanel;
		
		public CancelAddReferenceContactInfo(VerticalLayout contactInfoLayout, ContactInfoPanel contactInfoPanel) {
			this.contactInfoLayouts = contactInfoLayout;
			this.contactInfoPanel = contactInfoPanel;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			this.contactInfoPanel.removeErrorsPanel();
			contactInfoLayout.removeComponent(this.contactInfoLayouts);
			contactInfoPanels.remove(this.contactInfoPanel);
		}
	}
}
