package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.other.ColContactOtherPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.phones.ColContactPhonePanel;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class ContactInfoDetailPopup extends Window implements FinServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = -9209448258770283305L;
	
	protected final static Logger logger = LoggerFactory.getLogger(ContactInfoDetailPopup.class);

	private ERefDataComboBox<ETypeAddress> cbxContactAddressType;
	private ERefDataComboBox<ETypeContactInfo> cbxContactInfoType;
	private TextField txtValue;
	private TextField txtRemark;
	private CheckBox cbPrimary;
	private CheckBox cbActive;
	private Button btnSave;
	private Button btnCancel;
	
	private Individual individual;
	private IndividualContactInfo indConInfo;
	private IndividualReferenceInfo indRefInfo;
	private IndividualReferenceContactInfo indRefConInfo;
	
	private Company company;
	private CompanyContactInfo comConInfo;
	
	private ColContactPhonePanel contactPhonePanel;
	private ColContactOtherPanel contactOtherPanel;
	
	private ColContractHistoryFormPanel contractHistoriesFormPanel;
	
	/**
	 * @param contractHistoriesFormPanel the contractHistoriesFormPanel to set
	 */
	public void setContractHistoriesFormPanel(ColContractHistoryFormPanel contractHistoriesFormPanel) {
		this.contractHistoriesFormPanel = contractHistoriesFormPanel;
	}

	/**
	 * 
	 * @param contactPhonePanel
	 * @param contactOtherPanel
	 * @param isPhone
	 */
	public ContactInfoDetailPopup(ColContactPhonePanel contactPhonePanel, ColContactOtherPanel contactOtherPanel, boolean isPhone) {
		this.contactPhonePanel = contactPhonePanel;
		this.contactOtherPanel = contactOtherPanel;
		setModal(true);
		setResizable(false);
		setCaption(I18N.message("add.contact"));
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getDefaultButton("cancel", FontAwesome.BAN, 60);
		btnCancel.addClickListener(this);
		
		HorizontalLayout btnLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		btnLayout.addComponent(btnSave);
		btnLayout.addComponent(btnCancel);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, true, true), true);
		verLayout.addComponent(createContractInfoPanel(isPhone));
		verLayout.addComponent(btnLayout);
		verLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_RIGHT);
		
		setContent(verLayout);
	}
	
	/**
	 * 
	 * @param isPhone
	 * @return
	 */
	public VerticalLayout createContractInfoPanel(boolean isPhone){
		VerticalLayout contactInfoContent = new VerticalLayout();
		cbxContactAddressType = new ERefDataComboBox<>(null, ETypeAddress.class);
		cbxContactAddressType.setWidth(150, Unit.PIXELS);
		cbxContactAddressType.setVisible(false);
		
		List<ETypeContactInfo> typeContactInfos = new ArrayList<ETypeContactInfo>();
		if (isPhone) {
			typeContactInfos.add(ETypeContactInfo.LANDLINE);
			typeContactInfos.add(ETypeContactInfo.MOBILE);
		} else {
			for (ETypeContactInfo typConInfo : ETypeContactInfo.values()) {
				if (!ETypeContactInfo.LANDLINE.equals(typConInfo) && !ETypeContactInfo.MOBILE.equals(typConInfo)) {
					typeContactInfos.add(typConInfo);
				}
			}
		}
		
		cbxContactInfoType = new ERefDataComboBox<>(typeContactInfos);
		cbxContactInfoType.setWidth(150, Unit.PIXELS);
		cbxContactInfoType.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 8040949377517284238L;

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
		txtValue = ComponentFactory.getTextField(false, 200, 150);
		txtRemark = ComponentFactory.getTextField(false, 100, 150);
		
		cbPrimary = new CheckBox(I18N.message("primary"));
		cbPrimary.setVisible(isPhone);
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
				
		GridLayout gridLayout = ComponentLayoutFactory.getGridLayout(new MarginInfo(true, true, false, true), 7, 3);
		
		Label lblType = ComponentLayoutFactory.getLabelCaptionRequired("type");
		Label lblValue = ComponentLayoutFactory.getLabelCaptionRequired("value");
		Label lblRemark = ComponentLayoutFactory.getLabelCaption("remark");
		
		HorizontalLayout checkBoxLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		checkBoxLayout.addComponent(cbPrimary);
		checkBoxLayout.addComponent(cbActive);
		
		int iCol = 0;
		gridLayout.addComponent(lblType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(40, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxContactInfoType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxContactAddressType, iCol++, 0);
		gridLayout.addComponent(lblValue, 0, 1);
		gridLayout.addComponent(txtValue, 2, 1);
		gridLayout.addComponent(checkBoxLayout, 4, 1);
		gridLayout.addComponent(lblRemark, 0, 2);
		gridLayout.addComponent(txtRemark, 2, 2);
		
		contactInfoContent.addComponent(gridLayout);
		return contactInfoContent;
	}
	
	/**
	 * 
	 * @param individual
	 * @param indConInfo
	 * @param indRefInfo
	 * @param indRefConInfo
	 * @param company
	 * @param comConInfo
	 */
	public void assignValues(Individual individual, IndividualContactInfo indConInfo,
			IndividualReferenceInfo indRefInfo, IndividualReferenceContactInfo indRefConInfo, 
			Company company, CompanyContactInfo comConInfo) {
		reset();
		this.individual = individual;
		this.company = company;
		this.indConInfo = indConInfo;
		this.indRefInfo = indRefInfo;
		this.indRefConInfo = indRefConInfo;
		this.comConInfo = comConInfo;
	}
	
	/**
	 * 
	 */
	private void reset() {
		this.individual = null;
		this.indConInfo = null;
		this.indRefInfo = null;
		this.indRefConInfo = null;
		this.company = null;
		this.comConInfo = null;
		cbxContactInfoType.setSelectedEntity(null);
		cbxContactAddressType.setSelectedEntity(null);
		cbPrimary.setValue(false);
		cbActive.setValue(true);
		txtValue.setValue(StringUtils.EMPTY);
		txtRemark.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			if (StringUtils.isEmpty(getErrorDetail())) {
				if (this.individual != null) {
					if (this.indConInfo != null) {
						try {
							ContactInfo contactInfo = getContactInfo();
							this.indConInfo.setIndividual(this.individual);
							this.indConInfo.setContactInfo(contactInfo);
							INDIVI_SRV.saveOrUpdateIndividualContactInfo(this.indConInfo);
							displaySuccessfullyMsg();
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}  
				} else if (this.company != null) {
					if (this.comConInfo != null) {
						try {
							ContactInfo contactInfo = getContactInfo();
							this.comConInfo.setCompany(this.company);
							this.comConInfo.setContactInfo(contactInfo);
							COM_SRV.saveOrUpdateCompanyContactInfo(this.comConInfo);
							displaySuccessfullyMsg();
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				} else if (indRefInfo != null && indRefInfo.getId() != null && indRefConInfo != null) {
					try {
						ContactInfo contactInfo = getContactInfo();
						this.indRefConInfo.setIndividualReferenceInfo(indRefInfo);
						this.indRefConInfo.setContactInfo(contactInfo);
						INDIVI_SRV.saveOrUpdateReferenceContactInfo(this.indRefConInfo);
						displaySuccessfullyMsg();
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
				contractHistoriesFormPanel.refereshAfterSaved();
			} else {
				ComponentLayoutFactory.displayErrorMsg(getErrorDetail());
			}
		} else if (event.getButton().equals(btnCancel)) {
			close();
		}
	}
	
	/**
	 * 
	 */
	private void displaySuccessfullyMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
		close();
		if (contactPhonePanel != null) {
			contactPhonePanel.refresh();
		} else if (contactOtherPanel != null) {
			contactOtherPanel.refresh();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private ContactInfo getContactInfo() {
		ContactInfo contactInfo = ContactInfo.createInstance();
		contactInfo.setTypeInfo(cbxContactInfoType.getSelectedEntity());
		contactInfo.setTypeAddress(cbxContactAddressType.getSelectedEntity());
		contactInfo.setValue(txtValue.getValue());
		contactInfo.setPrimary(cbPrimary.getValue());
		contactInfo.setActive(cbActive.getValue());
		contactInfo.setRemark(txtRemark.getValue());
		return contactInfo;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private String getErrorMsgDescription(String caption) {
		return  I18N.message("field.required.1", new String[] {I18N.message(caption)});
	}
	
	/**
	 * @return
	 */
	private String getErrorDetail() {		
		if (cbxContactInfoType.getSelectedEntity() != null) {
			if (cbxContactInfoType.getSelectedEntity().equals(ETypeContactInfo.LANDLINE)) {
				if (cbxContactAddressType.getSelectedEntity() == null) {
					return getErrorMsgDescription("address.type");
				}
			}
		} else {
			return getErrorMsgDescription("contact.info.type");
		}
		if (StringUtils.isEmpty(txtValue.getValue())) {
			return getErrorMsgDescription("contact.info.value");
		}
		return StringUtils.EMPTY;
	}
	
}
