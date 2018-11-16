package com.nokor.efinance.gui.ui.panel.contract.notes.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.third.finwiz.client.sms.ClientSms;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * SMS pop up panel
 * @author uhout.cheng
 */
public class SMSPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/** */
	private static final long serialVersionUID = -8861198237432410921L;
	
	private ComboBox cbxSendTo;
	private ComboBox cbxMobileNumbers;	
	private TextArea txtMessage;
	
	private Button btnSend;
	private Button btnCancel;
	
	private Contract contract;
	
	private VerticalLayout messagePanel;
	
	private Listener listener = null;
	private List<String> errors;
	
	public interface Listener extends Serializable {
        void onClose(SMSPopupPanel dialog);
    }
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static SMSPopupPanel show(final Contract contract, final Listener listener) {    	
    	SMSPopupPanel smsPopupPanel = new SMSPopupPanel(contract);
    	smsPopupPanel.listener = listener;
    	UI.getCurrent().addWindow(smsPopupPanel);
        return smsPopupPanel;
    }
	
	/**
	 * 
	 * @param contract
	 * @param notesTablePanel
	 */
	private SMSPopupPanel(Contract contract) {
		this.contract = contract;
		setCaption(I18N.message("sms"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		init();
	}
	
	/**
	 */
	private void init() {
		btnSend = new NativeButton(I18N.message("send"));
		btnSend.addClickListener(this);
		btnSend.setIcon(FontAwesome.PAPER_PLANE);
	     
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.addClickListener(this);
		btnCancel.setIcon(FontAwesome.BAN);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSend);
		navigationPanel.addButton(btnCancel);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(SMSPopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(SMSPopupPanel.this);
              }
            }
        };
		
        btnSend.addClickListener(cb);
        
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(createForm());
	    
		setContent(contentLayout);
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		ENTITY_SRV.refresh(contract);
		List<SendTo> sendToList = new ArrayList<>();
		SendTo lessee = new SendTo(SendTo.LESSEE, 0, EApplicantType.C.getDescEn());
		List<PhoneInfo> phonesLessee = new ArrayList<>();
		List<IndividualContactInfo> lesseeContactInfos = contract.getApplicant().getIndividual().getIndividualContactInfos();
		if (lesseeContactInfos != null && !lesseeContactInfos.isEmpty()) {
			for (IndividualContactInfo lesseeContactInfo : lesseeContactInfos) {
				if (ETypeContactInfo.MOBILE.equals(lesseeContactInfo.getContactInfo().getTypeInfo())) {
					phonesLessee.add(new PhoneInfo(lesseeContactInfo.getContactInfo().isPrimary(), lesseeContactInfo.getContactInfo().getValue()));
				}
			}
			lessee.setPhones(phonesLessee);
		}
		sendToList.add(lessee);
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		if (contractApplicants != null  && !contractApplicants.isEmpty()) {
			for (int i = 0; i < contractApplicants.size(); i++) {
				ContractApplicant contractApplicant = contractApplicants.get(i);
				if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
					SendTo guarantor = new SendTo(SendTo.GUARANTOR, i + 1, EApplicantType.G.getDescEn());
					List<PhoneInfo> phonesGuarantor = new ArrayList<>();
					List<IndividualContactInfo> guarantorContactInfos = contractApplicant.getApplicant().getIndividual().getIndividualContactInfos();
					if (guarantorContactInfos != null && !guarantorContactInfos.isEmpty()) {
						for (IndividualContactInfo guarantorContactInfo : guarantorContactInfos) {
							if (ETypeContactInfo.MOBILE.equals(guarantorContactInfo.getContactInfo().getTypeInfo())) {
								phonesGuarantor.add(new PhoneInfo(guarantorContactInfo.getContactInfo().isPrimary(), guarantorContactInfo.getContactInfo().getValue()));
							}
						}
					}
					guarantor.setPhones(phonesGuarantor);
					sendToList.add(guarantor);
				}
			}
		}
		
		List<IndividualReferenceInfo> referenceInfos = contract.getApplicant().getIndividual().getIndividualReferenceInfos();
		if (referenceInfos != null && !referenceInfos.isEmpty()) {
			for (int i = 0; i < referenceInfos.size(); i++) {
				IndividualReferenceInfo referenceInfo = referenceInfos.get(i);
				SendTo reference = new SendTo(SendTo.REFERENCE, (i + 1), I18N.message("reference"));
				List<PhoneInfo> phonesReference = new ArrayList<>();
				List<IndividualReferenceContactInfo> referenceContactInfos = referenceInfo.getIndividualReferenceContactInfos();
				if (referenceContactInfos != null && !referenceContactInfos.isEmpty()) {
					for (IndividualReferenceContactInfo referenceContactInfo : referenceContactInfos) {
						if (ETypeContactInfo.MOBILE.equals(referenceContactInfo.getContactInfo().getTypeInfo())) {
							phonesReference.add(new PhoneInfo(referenceContactInfo.getContactInfo().isPrimary(), referenceContactInfo.getContactInfo().getValue()));
						}
					}
				}
				reference.setPhones(phonesReference);
				sendToList.add(reference);
			}
		}
		setItemsSendTo(sendToList);
	}
	
	/**
	 * @param sendTo
	 */
	private void setItemsSendTo(List<SendTo> sendToList) {
		cbxSendTo.clear();
		for (SendTo sendTo : sendToList) {
			cbxSendTo.addItem(sendTo);
		}
	}
	
	/**
	 * @param sendTo
	 */
	private void setItemMobileNumbers(SendTo sendTo) {
		cbxMobileNumbers.removeAllItems();
		List<PhoneInfo> phoneInfos = sendTo.getPhones();
		for (PhoneInfo phoneInfo : phoneInfos) {
			cbxMobileNumbers.addItem(phoneInfo);
		}
	}
	
	/**
	 * @return
	 */
	private VerticalLayout createForm() {
		cbxSendTo = new ComboBox(I18N.message("send.to"));
		cbxSendTo.setImmediate(true);
		cbxSendTo.setNullSelectionAllowed(true);
		cbxSendTo.setWidth(170, Unit.PIXELS);
		cbxSendTo.addValueChangeListener(new ValueChangeListener() {
		
			/** */
			private static final long serialVersionUID = 2244561588244183287L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxSendTo.getValue() != null) {
					setItemMobileNumbers((SendTo) cbxSendTo.getValue());
				} else {
					cbxMobileNumbers.removeAllItems();
				}
			}
		});
		
		cbxMobileNumbers = new ComboBox(I18N.message("phone.no"));
		cbxMobileNumbers.setImmediate(true);
		cbxMobileNumbers.setNullSelectionAllowed(true);
		cbxMobileNumbers.setRequired(true);
		cbxMobileNumbers.setWidth(170, Unit.PIXELS);
				
		txtMessage = ComponentFactory.getTextArea("message", false, 300, 100);
		txtMessage.setMaxLength(255);
		txtMessage.setRequired(true);
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		frmLayout.addComponent(cbxSendTo);
		frmLayout.addComponent(cbxMobileNumbers);
		frmLayout.addComponent(txtMessage);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		contentLayout.addComponent(frmLayout);
		return contentLayout;
	}

	/**
	 * Validate Add BankAccount 
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;

		if (cbxMobileNumbers.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("phone.no") }));
		}
		
		if (StringUtils.isEmpty(txtMessage.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("message") }));
		}	
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSend) {
			if (validate()) {
				String mobileNo = "";
				try {					
					SecUser user = UserSessionManager.getCurrentUser();
					
					SendTo sendTo = (SendTo) cbxSendTo.getValue();
					PhoneInfo phoneInfo = (PhoneInfo) cbxMobileNumbers.getValue();
					mobileNo = phoneInfo.getPhoneNumber();
					ContractSms contractSms = new ContractSms();
					contractSms.setContract(contract);
					contractSms.setType(sendTo.getType());
					contractSms.setPhoneNumber(mobileNo);
					contractSms.setMessage(txtMessage.getValue());
					contractSms.setUserLogin(user.getLogin());
					contractSms.setSendTo(sendTo.getDesc());
					
					ClientSms.sendSms(mobileNo, txtMessage.getValue(), user.getDesc());
				 
					NOTE_SRV.saveOrUpdateSMS(contractSms);
					ComponentLayoutFactory.displaySuccessfullyMsg();
					close();
				} catch (Exception e) {
					Notification notification = new Notification("", "Error while Send SMS. [" + mobileNo + "]", Notification.Type.WARNING_MESSAGE, true);
					notification.setDelayMsec(3000);
					notification.show(Page.getCurrent());
				}
			}
		}
		if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		cbxSendTo.setValue("");
		cbxMobileNumbers.setValue("");
		txtMessage.setValue("");
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
	/**
	 * @author youhort.ly
	 */
	private class SendTo implements Serializable {
		
		private static final long serialVersionUID = 5844656744202556144L;
		
		public final static int LESSEE = 1;
		public final static int GUARANTOR = 2;
		public final static int REFERENCE = 3;
		
		private int type = LESSEE;
		private int index = 0;
		private String desc;
		private List<PhoneInfo> phones = new ArrayList<>();
		
		/**
		 * @param type
		 * @param desc
		 */
		public SendTo(int type, int index, String desc) {
			this.type = type;
			this.index = index;
			this.desc = desc;
		}
		
		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}
		
		/**
		 * @return the desc
		 */
		public String getDesc() {
			return desc;
		}
		
		/**
		 * @return the phones
		 */
		public List<PhoneInfo> getPhones() {
			return phones;
		}
		/**
		 * @param phones the phones to set
		 */
		public void setPhones(List<PhoneInfo> phones) {
			this.phones = phones;
		}
		
		@Override
		public String toString() {
			return desc + (index > 0 ? (" " + index) : ""); 
		}
	}
	
	/**
	 * @author youhort.ly
	 */
	private class PhoneInfo implements Serializable {
		
		private static final long serialVersionUID = -7839202327612119403L;
		
		private boolean primary;
		private String phoneNumber;
		
		/**
		 * @param primary
		 * @param phoneNumber
		 */
		public PhoneInfo(boolean primary, String phoneNumber) {
			this.primary = primary;
			this.phoneNumber = phoneNumber;
		}
		
		
		/**
		 * @return the phoneNumber
		 */
		public String getPhoneNumber() {
			return phoneNumber;
		}
		
		@Override
		public String toString() {
			return (primary ? I18N.message("primary") : I18N.message("mobile")) + " - " + phoneNumber; 
		}
	}
	
}
