package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.ContractSms;
import com.nokor.efinance.core.contract.service.ContractSmsRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.SMSTemplateComboBox;
import com.nokor.efinance.third.finwiz.client.sms.ClientSms;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Collection phone SMS table panel
 * @author uhout.cheng
 */
public class ColPhoneSMSTablePanel extends AbstractControlPanel implements FinServicesHelper, ClickListener, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = -4372842656327320772L;

	private ComboBox cbxSend;
	private AutoDateField dfDate;
	private CheckBox cbToday;
	private TextArea txtRemark;
	private Button btnSend;
	private SMSTemplateComboBox cbxSmsTemplate;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ColPhoneSMSTablePanel() {
		cbxSend = new ComboBox();
		cbxSend.setWidth(170, Unit.PIXELS);
		txtRemark = ComponentFactory.getTextArea(false, 300, 80);
		dfDate = ComponentFactory.getAutoDateField();
		cbToday = new CheckBox(I18N.message("today"));
		cbToday.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 3302456133184099900L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbToday.getValue()) {
					dfDate.setEnabled(false);
					dfDate.setValue(DateUtils.today());
				} else {
					dfDate.setEnabled(true);
					//dfDate.setValue(null);
				}
			}
		});
		//cbToday.setValue(true);
		btnSend = ComponentLayoutFactory.getDefaultButton("send", FontAwesome.SEND, 60);
		btnSend.addClickListener(this);
		
		cbxSmsTemplate = new SMSTemplateComboBox(ENTITY_SRV.list(new BaseRestrictions<>(ESmsTemplate.class)));
		cbxSmsTemplate.setWidth(170, Unit.PIXELS);
		
		cbxSmsTemplate.addValueChangeListener(new ValueChangeListener() {
		
			/** */
			private static final long serialVersionUID = -8814462745022141044L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxSmsTemplate.getSelectedEntity() != null) {
					txtRemark.setValue(replaceShortCodeSMS(getDefaultString(cbxSmsTemplate.getSelectedEntity().getContent())));
				} else {
					txtRemark.setValue(StringUtils.EMPTY);
				}
			}
		});
		
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.addItemClickListener(this);
		simpleTable.setPageLength(5);
		simpleTable.setCaption(I18N.message("sms"));
		
		addComponent(getResultsPanel());
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		List<SendTo> sendToList = new ArrayList<>();
		SendTo lessee = new SendTo(SendTo.LESSEE, 0, EApplicantType.C.getDescEn());
		List<PhoneInfo> phonesLessee = new ArrayList<>();
		List<IndividualContactInfo> lesseeContactInfos = null;
		List<IndividualReferenceInfo> referenceInfos = null;
		
		Applicant applicant = contract.getApplicant();
		Individual individual = null;
		if (applicant != null) {
			individual = applicant.getIndividual();
		}
		if (individual != null) {
			lesseeContactInfos = individual.getIndividualContactInfos();
			referenceInfos = individual.getIndividualReferenceInfos();
		}
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
					List<IndividualContactInfo> guarantorContactInfos = null;
					Applicant appGurantor = null;
					if (contractApplicant != null) {
						appGurantor = contractApplicant.getApplicant();
					}
					Individual gua = null;
					if (appGurantor != null) {
						gua = appGurantor.getIndividual();
					}
					if (gua != null) {
						guarantorContactInfos = gua.getIndividualContactInfos();
					}
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
		refresh();
	}
	
	/**
	 * @param sendTo
	 */
	private void setItemsSendTo(List<SendTo> sendToList) {
		cbxSend.removeAllItems();
		for (SendTo sendTo : sendToList) {
			List<PhoneInfo> phoneInfos = sendTo.getPhones();
			for (PhoneInfo phoneInfo : phoneInfos) {
				cbxSend.addItem(sendTo.getDesc() + " - " + phoneInfo.getPhoneNumber());
			}
		}
	}
	
	/**
	 * 
	 */
	private void refresh() {
		reset();
		ContractSmsRestriction restrictions = new ContractSmsRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
		setSMSIndexedContainer(CONT_SRV.list(restrictions));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ContractSms.ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(ContractSms.CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(ContractSms.CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ContractSms.SENDTO, I18N.message("send.to"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ContractSms.PHONENUMBER, I18N.message("phone.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ContractSms.MESSAGE, I18N.message("text"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ContractSms.STATUS, I18N.message("status"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ContractSms.ACTION, I18N.message("action"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contractSms
	 */
	@SuppressWarnings("unchecked")
	private void setSMSIndexedContainer(List<ContractSms> contractSms) {
		this.selectedItem = null;
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (contractSms != null && !contractSms.isEmpty()) {
			for (ContractSms sms : contractSms) {
				if (sms != null) {
					Item item = indexedContainer.addItem(sms.getId());
					item.getItemProperty(ContractSms.ID).setValue(sms.getId());
					item.getItemProperty(ContractSms.CREATEDATE).setValue(sms.getCreateDate());
					item.getItemProperty(ContractSms.CREATEUSER).setValue(sms.getUserLogin());
					item.getItemProperty(ContractSms.SENDTO).setValue(sms.getSendTo());
					item.getItemProperty(ContractSms.PHONENUMBER).setValue(sms.getPhoneNumber());
					item.getItemProperty(ContractSms.MESSAGE).setValue(sms.getMessage());
					item.getItemProperty(ContractSms.STATUS).setValue("");
					item.getItemProperty(ContractSms.ACTION).setValue(new CancelSmsButton(sms));
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getResultsPanel() {
		GridLayout gridLayout = new GridLayout(17, 1);
		int iCol = 0;
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("send.to"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxSend, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("date"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbToday, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("sms.template"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxSmsTemplate, iCol++, 0);
		
		GridLayout remarkLayout = new GridLayout(5, 1);
		remarkLayout.addComponent(ComponentLayoutFactory.getLabelCaption("remark"));
		remarkLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		remarkLayout.addComponent(txtRemark);
		remarkLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		remarkLayout.addComponent(btnSend);
		
		VerticalLayout verGridLayout = new VerticalLayout(gridLayout, remarkLayout);
		verGridLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(ComponentLayoutFactory.getPanel(verGridLayout, true, false));
		layout.addComponent(simpleTable);
		
		return layout;
	}
	
	/**
	 * 
	 */
	public void reset() {
		cbxSmsTemplate.setSelectedEntity(null);
		//dfDate.setValue(null);
		cbToday.setValue(true);
		cbxSend.setValue(null);
		txtRemark.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelSmsButton extends NativeButton {
		
		private static final long serialVersionUID = 5331682229749631545L;

		public CancelSmsButton(ContractSms sms) {
			super("");
			setIcon(FontAwesome.BAN);
			setStyleName(Reindeer.BUTTON_LINK);
			addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -8479467763680332747L;
				
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					try {
						ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.delete.sms"),
								new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 2380193173874927880L;
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											ENTITY_SRV.delete(sms);
											refresh();
										} 
									}
							});
							confirmDialog.setWidth("400px");
							confirmDialog.setHeight("150px");
						
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			});
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSend)) {
			String sendTo = "";
			try {					
				SecUser user = UserSessionManager.getCurrentUser();
				
				sendTo = String.valueOf(cbxSend.getValue() != null ? cbxSend.getValue() : "");
				sendTo = sendTo.replaceAll("\\s", "");
				
				if (StringUtils.isEmpty(sendTo)) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
							MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("send")), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					int sendType;
					String[] sendTos = sendTo.split("-");
					
					if ("Customer".equals(sendTos[0])) {
						sendType = 1;
					} else if ("Guaranotr".equals(sendTos[0])) {
						sendType = 2;
					} else {
						sendType = 3;
					}
					
					ContractSms contractSms = new ContractSms();
					contractSms.setContract(contract);
					contractSms.setType(sendType);
					contractSms.setPhoneNumber(sendTos[1]);
					contractSms.setMessage(txtRemark.getValue());
					contractSms.setUserLogin(user.getLogin());
					contractSms.setSendTo(sendTos[0]);
					
					ClientSms.sendSms(contractSms.getPhoneNumber(), txtRemark.getValue(), user.getDesc());
					
					try {
						NOTE_SRV.saveOrUpdateSMS(contractSms);
						ComponentLayoutFactory.displaySuccessfullyMsg();
						refresh();
					} catch (Exception e) {
						logger.error(e.getMessage());
					}	  	
				}
			} catch (Exception e) {
				Notification notification = new Notification("", "Error while Send SMS. [" + sendTo + "]", Notification.Type.WARNING_MESSAGE, true);
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
		}
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	private String replaceShortCodeSMS(String content) {
		content = content.replace("(CUSTOMER_ID)",contract.getReference());
		content = content.replace("(FULL_NAME)", contract.getApplicant().getNameLocale());
		content = content.replace("(INSTALLMENT_AMOUNT)", AmountUtils.format(contract.getTiInstallmentAmount()));
		content = content.replace("(CURRENT_DATE-TIME)", DateUtils.todayFull());
		content = content.replace("(LAST_NAME)", contract.getApplicant().getLastNameLocale());
		content = content.replace("(BRANCH_NAME)", contract.getOriginBranch() != null ? contract.getOriginBranch().getNameLocale() : "");
		content = content.replace("(BRANCH_ADDRESS)", contract.getOriginBranch() != null ? ADDRESS_SRV.getDetailAddress(contract.getOriginBranch().getMainAddress()) : "");
		content = content.replace("(VALIDATION_DATE)", DateUtils.getDateLabel(contract.getStartDate()));
		return content;
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ContractSms.ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
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
