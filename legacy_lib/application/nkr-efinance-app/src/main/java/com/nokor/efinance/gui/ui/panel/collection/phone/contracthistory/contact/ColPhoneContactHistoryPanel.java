package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.EContactPerson;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * Collection phone contact history panel
 * @author uhout.cheng
 */
public class ColPhoneContactHistoryPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -7387511662057461762L;
	
//	private static final String INBOUND = ECallType.INBOUND.getDescLocale();
//	private static final String OUTBOUND = ECallType.OUTBOUND.getDescLocale();
	
	private OptionGroup optionGroupType;
	
	private OptionGroup optionGroupColType;
	private ComboBox cbxSendTo;
	private EntityRefComboBox<EColResult> cbxResult;
	private ERefDataComboBox<EContactPerson> cbxPersonAnswerThePhone;
	private TextArea txtRemark;
	private Button btnAdd;
	
	private VerticalLayout mainLayout;
	
	private Label lblRemarkTitle;
	
	/**
	 * @return the btnAdd
	 */
	public Button getBtnAdd() {
		return btnAdd;
	}

	private CollectionHistory colHistory;
	private Contract contract;
	private TextField txtOther;
	
	private ECallType callType;
	
	/**
	 * 
	 * @param callType
	 */
	public ColPhoneContactHistoryPanel(ECallType callType) {
		this.callType = callType;
		init();
		
		Label lblResultTitle = ComponentLayoutFactory.getLabelCaption("result");
		Label lblContactedPerson = ComponentLayoutFactory.getLabelCaption("contacted.person");
		Label lblPersonAnswerThePhone = ComponentLayoutFactory.getLabelCaption("person.answer.the.phone");
		
		lblRemarkTitle = ComponentFactory.getLabel("remark");
		
		mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		
		mainLayout.addComponent(getDetailLayout(new Label(), optionGroupType));
		mainLayout.addComponent(getDetailLayout(new Label(), optionGroupColType));
		mainLayout.addComponent(getDetailLayout(lblContactedPerson, cbxSendTo));
		mainLayout.addComponent(getDetailLayout(lblPersonAnswerThePhone, cbxPersonAnswerThePhone));
		mainLayout.addComponent(getDetailLayout(new Label(), txtOther));
		mainLayout.addComponent(getDetailLayout(lblResultTitle, cbxResult));
		mainLayout.addComponent(getDetailLayout(lblRemarkTitle, txtRemark));
		mainLayout.addComponent(getDetailLayout(new Label(), btnAdd));
		
		mainLayout.getComponent(4).setVisible(false);
		
		setHideControlsCallCenterStaff(ProfileUtil.isCallCenterStaff());
		
		Panel mainPanel = new Panel(mainLayout);
		mainPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("contact.history") + "</h3>");
		mainPanel.setCaptionAsHtml(true);
		setMargin(true);
		addComponent(mainPanel);
		
	}
	
	/**
	 * @param lblTitle
	 * @param comSecond
	 * @return
	 */
	private HorizontalLayout getDetailLayout(Label lblTitle, Component comSecond) {
		HorizontalLayout layout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		lblTitle.setWidth(140, Unit.PIXELS);
		lblTitle.addStyleName("align-right");
		layout.addComponent(lblTitle);
		layout.addComponent(comSecond);
		if (lblRemarkTitle.getValue().equals(lblTitle.getValue())) {
			layout.setComponentAlignment(lblRemarkTitle, Alignment.MIDDLE_RIGHT);
		}
		if (btnAdd.getCaption().equals(comSecond.getCaption())) {
			layout.setWidth(100, Unit.PERCENTAGE);
			layout.setComponentAlignment(comSecond, Alignment.MIDDLE_RIGHT);
		}
		return layout;
	}
	
	/**
	 * 
	 * @param isCallCenter
	 */
	private void setHideControlsCallCenterStaff(boolean isCallCenter) {
		mainLayout.getComponent(0).setVisible(isCallCenter);
		mainLayout.getComponent(1).setVisible(!isCallCenter);
		mainLayout.getComponent(5).setVisible(!isCallCenter);
	}

	/**
	 * 
	 * @param appType
	 * @param index
	 * @param app
	 * @return
	 */
	private SendTo getSendTo(EApplicantType appType,int index, Applicant app) {
		SendTo sendTo = new SendTo(index, appType.getCode());
		List<PhoneInfo> phoneInfos = new ArrayList<>();
		List<IndividualContactInfo> indContactInfos = new ArrayList<IndividualContactInfo>();
		if (app != null) {
			Individual indApp = app.getIndividual();
			if (indApp != null) {
				indContactInfos = indApp.getIndividualContactInfos();
			}
		}
		if (!indContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : indContactInfos) {
				if (ETypeContactInfo.LANDLINE.equals(indContactInfo.getContactInfo().getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(indContactInfo.getContactInfo().getTypeInfo())) {
					phoneInfos.add(new PhoneInfo(indContactInfo.getContactInfo().getTypeInfo().getCode(), indContactInfo.getContactInfo().getValue()));
				}
			}
			sendTo.setPhones(phoneInfos);
		}
		return sendTo;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assingValues(Contract contract) {
		reset();
		this.contract = contract;

		List<SendTo> sendToList = new ArrayList<>();
		sendToList.add(getSendTo(EApplicantType.C, 0, contract.getApplicant()));
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		if (contractApplicants != null  && !contractApplicants.isEmpty()) {
			for (int i = 0; i < contractApplicants.size(); i++) {
				ContractApplicant contractApplicant = contractApplicants.get(i);
				if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
					sendToList.add(getSendTo(EApplicantType.G, (i + 1), contractApplicant.getApplicant()));
				}
			}
		}
		
		List<IndividualReferenceInfo> referenceInfos = new ArrayList<IndividualReferenceInfo>();
		Applicant appRef = contract.getApplicant();
		if (appRef != null) {
			Individual indRef = appRef.getIndividual();
			if (indRef != null) {
				referenceInfos = indRef.getIndividualReferenceInfos();
			}
		}
		if (!referenceInfos.isEmpty()) {
			for (int i = 0; i < referenceInfos.size(); i++) {
				IndividualReferenceInfo referenceInfo = referenceInfos.get(i);
				SendTo reference = new SendTo((i + 1), EApplicantType.R.getCode());
				List<PhoneInfo> phonesReference = new ArrayList<>();
				List<IndividualReferenceContactInfo> referenceContactInfos = referenceInfo.getIndividualReferenceContactInfos();
				if (referenceContactInfos != null && !referenceContactInfos.isEmpty()) {
					for (IndividualReferenceContactInfo referenceContactInfo : referenceContactInfos) {
						if (ETypeContactInfo.LANDLINE.equals(referenceContactInfo.getContactInfo().getTypeInfo())
								|| ETypeContactInfo.MOBILE.equals(referenceContactInfo.getContactInfo().getTypeInfo())) {
							phonesReference.add(new PhoneInfo(referenceContactInfo.getContactInfo().getTypeInfo().getCode(), referenceContactInfo.getContactInfo().getValue()));
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			save();
		}
	}
	
	/**
	 * Save
	 */
	public void save() {
		// Save result
		if (cbxResult.getSelectedEntity() != null || StringUtils.isNotEmpty(txtRemark.getValue())) {
			if (!checkMandatorySelectField(cbxResult, "") && !ProfileUtil.isCallCenterStaff()) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("result")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				this.colHistory = CollectionHistory.createInstance();
				this.colHistory.setContract(this.contract);
				this.colHistory.setResult(cbxResult.getSelectedEntity());
				
//				if (INBOUND.equals(optionGroupType.getValue())) {
//					this.colHistory.setCallType(ECallType.INBOUND);
//				} else {
//					this.colHistory.setCallType(ECallType.OUTBOUND);
//				}
				
				this.colHistory.setComment(txtRemark.getValue());
				if (optionGroupColType.getValue().equals(EColType.PHONE.getDescLocale())) {
					this.colHistory.setOrigin(EColType.PHONE);
				} else {
					this.colHistory.setOrigin(EColType.FIELD);
				}
				
				String sendTo = String.valueOf(cbxSendTo.getValue() != null ? cbxSendTo.getValue() : "");
				if (StringUtils.isNotEmpty(sendTo)) {
					sendTo = sendTo.replaceAll("\\s", "");
					EApplicantType appType = EApplicantType.C;
					String[] sendTos = sendTo.split("-");
					
					if (EApplicantType.C.getCode().equals(sendTos[0])) {
						appType = EApplicantType.C;
					} else if (EApplicantType.G.getCode().equals(sendTos[0])) {
						appType = EApplicantType.G;
					} else if (EApplicantType.R.getCode().equals(sendTos[0])) {
						appType = EApplicantType.R;
					}
					
					ETypeContactInfo typeCnt = ETypeContactInfo.MOBILE;
					
					if ("L".equals(sendTos[1])) {
						typeCnt = ETypeContactInfo.LANDLINE;
					} else if ("M".equals(sendTos[1])) {
						typeCnt = ETypeContactInfo.MOBILE;
					}
					
					this.colHistory.setReachedPerson(cbxPersonAnswerThePhone.getSelectedEntity());
					this.colHistory.setOther(txtOther.getValue());
					this.colHistory.setContactedPerson(appType);
					this.colHistory.setContactedTypeInfo(typeCnt);
					this.colHistory.setContactedInfoValue(sendTos[2]);
				}
				
				try {
					COL_SRV.saveOrUpdateLatestColHistory(this.colHistory);
					displaySuccessfullyMsg();
				} catch (Exception e) {
					ComponentLayoutFactory.displayErrorMsg(e.getMessage());
				}
				
			}
		}
	}
	
	/**
	 * 
	 */
	public void displaySuccessfullyMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
		reset();
	}
	
	/**
	 * 
	 */
	protected void reset() {
		this.colHistory = null;
		optionGroupColType.select(EColType.PHONE.getDescLocale());
		cbxSendTo.setValue(null);
		cbxResult.setSelectedEntity(null);
		cbxPersonAnswerThePhone.setSelectedEntity(null);
		txtRemark.setValue("");
		optionGroupType.select(callType.getDescLocale());
	}
	
	/**
	 * 
	 */
	private void init() {
		List<String> options = Arrays.asList(new String[] {EColType.PHONE.getDescLocale(), EColType.FIELD.getDescLocale()});
		
		cbxSendTo = new ComboBox();
		cbxSendTo.setWidth(170, Unit.PIXELS);
		
		cbxResult = new EntityRefComboBox<EColResult>();
		cbxResult.setRestrictions(getResults(EColType.PHONE, null));
		cbxResult.getRestrictions().addOrder(Order.asc("descEn"));
		cbxResult.setCaptionRenderer(new Function<EColResult, String>() {
			/**
			 * @see java.util.function.Function#apply(java.lang.Object)
			 */
			@Override
			public String apply(EColResult t) {
				return t.getCode() + " - " + (I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc());
			}
		});
		cbxResult.renderer();
		cbxResult.setWidth(170, Unit.PIXELS);
		
//		List<String> optionTypes = Arrays.asList(new String[] {INBOUND, OUTBOUND});
//		optionGroupType = ComponentLayoutFactory.getOptionGroup(optionTypes);
		optionGroupType.select(callType.getDescLocale());
		optionGroupType.setWidth("170px");
		
		optionGroupColType = ComponentLayoutFactory.getOptionGroup(options);
		optionGroupColType.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 2202683050529943154L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (ProfileUtil.isColField()) {
					if (optionGroupColType.isSelected(EColType.PHONE.getDescLocale())) {
						cbxResult.setRestrictions(getResults(EColType.FIELD, "PF"));
						cbxResult.renderer();
					} else {
						cbxResult.setRestrictions(getResults(EColType.FIELD, "F"));
						cbxResult.renderer();
					}
				} else {
					if (optionGroupColType.isSelected(EColType.PHONE.getDescLocale())) {
						cbxResult.setRestrictions(getResults(EColType.PHONE, null));
						cbxResult.renderer();
					} else {
						cbxResult.setRestrictions(getResults(EColType.FIELD, null));
						cbxResult.renderer();
					}
				}
			}
		});
		optionGroupColType.select(EColType.PHONE.getDescLocale());
		
		cbxPersonAnswerThePhone = new ERefDataComboBox<>(EContactPerson.values());
		cbxPersonAnswerThePhone.setWidth(170, Unit.PIXELS);
		cbxPersonAnswerThePhone.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -3522927862159965542L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (EContactPerson.OTHER.equals(cbxPersonAnswerThePhone.getSelectedEntity())) {
//					txtOther.setVisible(true);
					mainLayout.getComponent(4).setVisible(true);
					txtOther.focus();
				} else {
//					txtOther.setVisible(false);
					mainLayout.getComponent(4).setVisible(false);
				}
				
			}
		});
		
		
		
		txtOther = ComponentFactory.getTextField(200, 170);
//		txtOther.setVisible(false);
		
		txtRemark = ComponentFactory.getTextArea(false, 170, 50);
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
	}
	
	/**
	 * @param sendTo
	 */
	private void setItemsSendTo(List<SendTo> sendToList) {
		cbxSendTo.removeAllItems();
		for (SendTo sendTo : sendToList) {
			List<PhoneInfo> phoneInfos = sendTo.getPhones();
			for (PhoneInfo phoneInfo : phoneInfos) {
				cbxSendTo.addItem(sendTo.getCode() + " - " + phoneInfo.getPhoneNumber());
			}
		}
	}
	
	/**
	 * Get ColResut to combo box by colType
	 * @param colType
	 * @return
	 */
	private BaseRestrictions<EColResult> getResults(EColType colType, String codeStart) {
		BaseRestrictions<EColResult> restrictions = new BaseRestrictions<>(EColResult.class);
		restrictions.addCriterion(Restrictions.eq("colTypes", colType));
		if (codeStart != null) {
			restrictions.addCriterion(Restrictions.like("code", codeStart, MatchMode.START));
		}
		return restrictions;
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class SendTo implements Serializable {
		
		/** */
		private static final long serialVersionUID = -5253521142375702378L;
		
		private int index = 0;
		private String code;
		private List<PhoneInfo> phones = new ArrayList<>();
		
		/**
		 * 
		 * @param index
		 * @param code
		 */
		public SendTo(int index, String code) {
			this.index = index;
			this.code = code;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
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
		
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return code + (index > 0 ? (StringUtils.SPACE + index) : StringUtils.EMPTY); 
		}
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class PhoneInfo implements Serializable {
		
		/** */
		private static final long serialVersionUID = 8121870892278288074L;
		
		private String type;
		private String phoneNumber;
		
		/**
		 * 
		 * @param type
		 * @param phoneNumber
		 */
		public PhoneInfo(String type, String phoneNumber) {
			this.type = type;
			this.phoneNumber = phoneNumber;
		}
		
		/**
		 * @return the phoneNumber
		 */
		public String getPhoneNumber() {
			String typeCnt = "";
			if (ETypeContactInfo.LANDLINE.getCode().equals(type)) {
				typeCnt = "L";
			} else if (ETypeContactInfo.MOBILE.getCode().equals(type)) {
				typeCnt = "M";
			}
			return typeCnt + " - " + phoneNumber;
		}
	}
	
}
