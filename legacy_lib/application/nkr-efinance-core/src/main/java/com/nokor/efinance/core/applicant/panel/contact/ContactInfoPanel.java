package com.nokor.efinance.core.applicant.panel.contact;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Contact Info Panel
 * @author buntha.chea
 * */
public class ContactInfoPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	//Contact Info 
	public ERefDataComboBox<ETypeAddress> cbxContactAddressType;
	public ERefDataComboBox<ETypeContactInfo> cbxContactInfoType;
	public TextField txtValue;
	public TextField txtRemark;
	public CheckBox cbPrimary;
	private VerticalLayout contactInfoContent;
	private boolean isNewContactInfo;
	private List<IndividualContactInfo> individualContactInfos;
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	private DealerEmployeeContactInfo dealerEmployeeContactInfo;
	public CheckBox cbActive;
 	
	/** */
	public ContactInfoPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(createContractInfoPanel());
		return verticalLayout;
	}
	
	/**
	 * 
	 * @param contactInfo
	 * @return
	 */
	public ContactInfo getContactInfomation(ContactInfo contactInfo) {
		contactInfo.setTypeInfo(cbxContactInfoType.getSelectedEntity());
		contactInfo.setTypeAddress(cbxContactAddressType.getSelectedEntity());
		contactInfo.setValue(txtValue.getValue());
		contactInfo.setPrimary(cbPrimary.getValue());
		contactInfo.setStatusRecord(cbActive.getValue());
		contactInfo.setRemark(txtRemark.getValue());
		return contactInfo;
	}
	
	/**
	 * 
	 * @param contactInfoPanels
	 * @param applicant
	 */
	public void getContactInfo(List<ContactInfoPanel> contactInfoPanels, Applicant applicant){
		individualContactInfos = applicant.getIndividual().getIndividualContactInfos();
		int numAddNewContactInfo = contactInfoPanels.size() - individualContactInfos.size();
		int index = 0;
		isNewContactInfo = false;
		for (int i=0; i<= numAddNewContactInfo; i++) {
			if (!isNewContactInfo) {
				//Update All contactInfo
				for (IndividualContactInfo individualContactInfo : individualContactInfos) {
					ContactInfo contactInfo = individualContactInfo.getContactInfo();
					if (contactInfo != null) {
						contactInfo.setTypeInfo(contactInfoPanels.get(index).cbxContactInfoType.getSelectedEntity());
						contactInfo.setTypeAddress(contactInfoPanels.get(index).cbxContactAddressType.getSelectedEntity());
						contactInfo.setValue(contactInfoPanels.get(index).txtValue.getValue());
						contactInfo.setValue(contactInfoPanels.get(index).txtRemark.getValue());
						if (contactInfo.getTypeInfo() != null) {
							entityService.saveOrUpdate(contactInfo);
						}
					}
					index++;
				}
				isNewContactInfo = true;
			} else {
				//Add New Contact Address
				ContactInfo contactInfo = new ContactInfo();
				contactInfo.setTypeInfo(contactInfoPanels.get(index).cbxContactInfoType.getSelectedEntity());
				contactInfo.setTypeAddress(contactInfoPanels.get(index).cbxContactAddressType.getSelectedEntity());
				contactInfo.setValue(contactInfoPanels.get(index).txtValue.getValue());
				contactInfo.setValue(contactInfoPanels.get(index).txtRemark.getValue());
				
				IndividualContactInfo newIndividualContactInfo = new IndividualContactInfo();
				newIndividualContactInfo.setIndividual(applicant.getIndividual());
				newIndividualContactInfo.setContactInfo(contactInfo);
				if (contactInfo.getTypeInfo() != null) {
					entityService.saveOrUpdate(contactInfo);
					entityService.saveOrUpdate(newIndividualContactInfo);
					individualContactInfos.add(newIndividualContactInfo);
				}
				index++;
			}
		}
	}
	
	/**
	 * 
	 * @param contactInfo
	 */
	public void assignValue(ContactInfo contactInfo) {
		cbxContactInfoType.setSelectedEntity(contactInfo.getTypeInfo());
		cbxContactAddressType.setSelectedEntity(contactInfo.getTypeAddress());
		txtValue.setValue(contactInfo.getValue());
		cbPrimary.setValue(contactInfo.isPrimary());
		cbActive.setValue(EStatusRecord.isActive(contactInfo.getStatusRecord()));
		txtRemark.setValue(contactInfo.getRemark());
	}
	
	/**
	 * AssignValue DelerEmployee ContactInfo
	 * @param contactInfo
	 */
	public void assignValueDealerEmployeeContactInfo(DealerEmployeeContactInfo contactInfo) {
		cbxContactInfoType.setSelectedEntity(contactInfo.getTypeInfo());
		cbxContactAddressType.setSelectedEntity(contactInfo.getTypeAddress());
		txtValue.setValue(contactInfo.getValue() != null ? contactInfo.getValue() : "");
		cbPrimary.setValue(contactInfo.isPrimary());
		cbActive.setValue(EStatusRecord.isActive(contactInfo.getStatusRecord()));
		txtRemark.setValue(contactInfo.getRemark() != null ? contactInfo.getRemark() : "");
		setDealerEmployeeContactInfo(contactInfo);
	}
	
	/**
	 * set value to dealer employeeContactInfo
	 * @param contactInfo
	 * @return
	 */
	public DealerEmployeeContactInfo getDealerEmployeeContactInfo(DealerEmployeeContactInfo contactInfo) {
		contactInfo.setTypeInfo(cbxContactInfoType.getSelectedEntity());
		contactInfo.setTypeAddress(cbxContactAddressType.getSelectedEntity());
		contactInfo.setValue(txtValue.getValue());
		contactInfo.setPrimary(cbPrimary.getValue());
		contactInfo.setStatusRecord(cbActive.getValue());
		contactInfo.setRemark(txtRemark.getValue());
		return contactInfo;
	}
	
	/** */
	public VerticalLayout createContractInfoPanel(){
		contactInfoContent = new VerticalLayout();
		cbxContactAddressType = new ERefDataComboBox<>(null, ETypeAddress.class);
		cbxContactAddressType.setWidth("150px");
		cbxContactAddressType.setVisible(false);
		cbxContactInfoType = new ERefDataComboBox<>(null, ETypeContactInfo.class);
		cbxContactInfoType.setWidth("130px");
		cbxContactInfoType.setRequired(true);
		cbxContactInfoType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6004761053215189076L;
	
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxContactInfoType.getSelectedEntity() != null) {
					if (cbxContactInfoType.getSelectedEntity().equals(ETypeContactInfo.LANDLINE)) {
						cbxContactAddressType.setVisible(true);
					} else {
						cbxContactAddressType.setVisible(false);
						cbxContactAddressType.setSelectedEntity(null);
					}
					
					if (ETypeContactInfo.LANDLINE.equals(cbxContactInfoType.getSelectedEntity()) || ETypeContactInfo.MOBILE.equals(cbxContactInfoType.getSelectedEntity())) {
						txtValue.setMaxLength(10);
					} else {
						txtValue.setMaxLength(60);
					}
				}
			}
		});
		txtValue = ComponentFactory.getTextField();
		txtValue.setWidth("130px");
		
		txtRemark = ComponentFactory.getTextField();
		txtRemark.setWidth("130px");
		
		cbPrimary = new CheckBox(I18N.message("primary"));
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
		
		HorizontalLayout checkBoxLayout = new HorizontalLayout();
		checkBoxLayout.setSpacing(true);
		checkBoxLayout.addComponent(cbPrimary);
		checkBoxLayout.addComponent(cbActive);
				
		GridLayout gridLayout = new GridLayout(5, 3);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(new Label(I18N.message("add.contact")), 0, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(40, Unit.PIXELS), 1, 0);
		gridLayout.addComponent(cbxContactInfoType, 2, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), 3, 0);
		gridLayout.addComponent(cbxContactAddressType, 4, 0);
		gridLayout.addComponent(txtValue, 2, 1);
		gridLayout.addComponent(checkBoxLayout, 4, 1);
		gridLayout.addComponent(txtRemark, 2, 2);
		
		contactInfoContent.addComponent(gridLayout);
		return contactInfoContent;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		removeErrorsPanel();
		cbxContactAddressType.setSelectedEntity(null);
		cbxContactInfoType.setSelectedEntity(null);
		txtValue.setValue("");
		txtRemark.setValue("");
		cbPrimary.setValue(false);
		cbActive.setValue(true);
	}
	
	/**
	 * @return
	 */
	public List<String> isValid() {		
		removeErrorsPanel();
		if (cbxContactInfoType.getSelectedEntity() != null) {
			if (cbxContactInfoType.getSelectedEntity().equals(ETypeContactInfo.LANDLINE)) {
				if (cbxContactAddressType.getSelectedEntity() == null) {
					checkMandatorySelectField(cbxContactAddressType, "address.type");
				}
			}
		} else {
			checkMandatorySelectField(cbxContactInfoType, "contact.info.type");
		}
		checkMandatoryField(txtValue, "contact.info.value");
		return errors;
	}
	
	
	/**
	 * @return the dealerEmployeeContactInfo
	 */
	public DealerEmployeeContactInfo getDealerEmployeeContactInfo() {
		return dealerEmployeeContactInfo;
	}

	/**
	 * @param dealerEmployeeContactInfo the dealerEmployeeContactInfo to set
	 */
	public void setDealerEmployeeContactInfo(DealerEmployeeContactInfo dealerEmployeeContactInfo) {
		this.dealerEmployeeContactInfo = dealerEmployeeContactInfo;
	}
}
