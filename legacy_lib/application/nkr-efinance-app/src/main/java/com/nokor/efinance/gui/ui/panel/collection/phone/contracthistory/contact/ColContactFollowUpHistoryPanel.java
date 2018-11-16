package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.address.panel.AddressComboBox;
import com.nokor.efinance.core.applicant.model.AppContactTypeVO;
import com.nokor.efinance.core.applicant.model.ApplicantAddressInfoVO;
import com.nokor.efinance.core.applicant.model.ApplicantContactInfoVO;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColSubject;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.EContactPerson;
import com.nokor.efinance.core.common.reference.model.ESmsTemplate;
import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.EmailTemplateComboBox;
import com.nokor.efinance.core.widget.LabelFormCustomLayout;
import com.nokor.efinance.core.widget.SMSTemplateComboBox;
import com.nokor.efinance.third.finwiz.client.sms.ClientSms;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class ColContactFollowUpHistoryPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, ValueChangeListener {
	
	/** */
	private static final long serialVersionUID = -4561587957359498018L;
	
	private static final String SMS = ECallType.SMS.getDescLocale();
	private static final String CALL = ECallType.CALL.getDescLocale();
	private static final String FIELD = ECallType.FIELD.getDescLocale();
	private static final String MAIL = ECallType.MAIL.getDescLocale();
	
	private static final String IN = I18N.message("in");
	private static final String OUT = I18N.message("out");
	private static final String OTHER = I18N.message("other");
	
	private OptionGroup optionGroupType;
	
	private ComboBox cbxPhone;
	private AddressComboBox cbxAddress;
	
	private EntityRefComboBox<EColResult> cbxAnswer;
	private SMSTemplateComboBox cbxSmsTemplate;
	private EmailTemplateComboBox cbxEmailTemplate;
	private EntityComboBox<EColSubject> cbxSubject;
	private ERefDataComboBox<EContactPerson> cbxContactWith;
	
	private ComboBox cbxCallOption;
	private TextArea txtRemark;
	private Button btnAdd;
	
	private VerticalLayout mainLayout;
	
	private CollectionHistory colHistory;
	private Contract contract;
	private TextField txtOther;
	
	private boolean isCallIn;
	
	private Label lblLastContactDate;
	private Label lblLastContactInfo;
	
	private ColContactFollowUpHistoryTable tablePanel;
	private ColContactAutoEmailTable colContactAutoEmailTable;
	private ColContactAutoSMSTable colContactAutoSMSTable;
	
	private HorizontalLayout resultLayoutField;
	private HorizontalLayout resultLayoutSMS;
	private HorizontalLayout resultLayoutEmail;
	
	private TextField txtPhoneOtherValue;
	private Label lblPhoneOtherValue;
	
	private Button btnNewSMS;
	private Button btnNewEmail;
	private Button btnNewField;
	private Button btnNewCall;
	
	private Panel mainPanel;
	private TextField txtAnswer;
	
	/**
	 * @return the btnAdd
	 */
	public Button getBtnAdd() {
		return btnAdd;
	}
	
	/**
	 * 
	 * @param isCallIn
	 */
	public ColContactFollowUpHistoryPanel(boolean isCallIn) {
		this.isCallIn = isCallIn;
		init();
		
		mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(optionGroupType);
		
		HorizontalLayout detailLayout = new HorizontalLayout();
		detailLayout.setSpacing(true);
		
		detailLayout.addComponent(getLabel("type"));
		detailLayout.addComponent(cbxCallOption);
		detailLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("no.add.mail"));
		detailLayout.addComponent(cbxPhone);
		detailLayout.addComponent(lblPhoneOtherValue);
		detailLayout.addComponent(txtPhoneOtherValue);
		detailLayout.addComponent(cbxAddress);
		detailLayout.addComponent(getLabel("contact.with"));
		detailLayout.addComponent(cbxContactWith);
		detailLayout.addComponent(txtOther);
		
		resultLayoutSMS = getResultLayout("sms.template", cbxSmsTemplate, false);
	
		if (ProfileUtil.isCMProfile()) {
			resultLayoutField = getResultLayout("answer", txtAnswer, true);
		} else {
			resultLayoutField = getResultLayout("answer", cbxAnswer, true);
		}
		
		resultLayoutEmail = getResultLayout("email.template", cbxEmailTemplate, false);
		
		cbxPhone.setVisible(true);
		cbxAddress.setVisible(false);
		
		resultLayoutField.setVisible(true);
		resultLayoutSMS.setVisible(false);
		resultLayoutEmail.setVisible(false);
		
		HorizontalLayout resultLayout = new HorizontalLayout();
		resultLayout.setSpacing(true);
		resultLayout.addComponent(getLabel("subject"));
		resultLayout.addComponent(cbxSubject);
		resultLayout.addComponent(resultLayoutSMS);
		resultLayout.addComponent(resultLayoutField);
		resultLayout.addComponent(resultLayoutEmail);
		
		VerticalLayout middleLayout = new VerticalLayout();
		middleLayout.setSpacing(true);
		middleLayout.addComponent(detailLayout);
		middleLayout.addComponent(resultLayout);
		
		mainLayout.addComponent(middleLayout);
		
		HorizontalLayout remarkLayout = new HorizontalLayout();
		remarkLayout.setSpacing(true);
		
		Label lblRemark = ComponentLayoutFactory.getLabelCaptionRequired("remark");
		
		remarkLayout.addComponent(lblRemark);
		remarkLayout.addComponent(txtRemark);
		remarkLayout.addComponent(btnAdd);
		remarkLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		remarkLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		mainLayout.addComponent(remarkLayout);
		
		HorizontalLayout menuButtonLayout = getButtonMenuLayout();
		
		mainPanel = new Panel(mainLayout);
		mainPanel.setVisible(false);
		
		colContactAutoEmailTable = new ColContactAutoEmailTable();
		colContactAutoSMSTable = new ColContactAutoSMSTable();
		tablePanel = new ColContactFollowUpHistoryTable();
		
		Panel autoEmailPanel = new Panel(colContactAutoEmailTable);
		autoEmailPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("auto.email") + "</h3>");
		autoEmailPanel.setCaptionAsHtml(true);
		
		Panel autoSMSPanel = new Panel(colContactAutoSMSTable);
		autoSMSPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("auto.sms") + "</h3>");
		autoSMSPanel.setCaptionAsHtml(true);
		
		Panel historyPanel = new Panel(tablePanel);
		historyPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("history") + "</h3>");
		historyPanel.setCaptionAsHtml(true);
		
		setMargin(true);
		setSpacing(true);
		addComponent(menuButtonLayout);
		addComponent(mainPanel);	
		addComponent(autoEmailPanel);
		addComponent(autoSMSPanel);
		addComponent(historyPanel);
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param isRequried
	 * @return
	 */
	private HorizontalLayout getResultLayout(String caption, Component component, boolean isRequried) {
		HorizontalLayout resultLayout = new HorizontalLayout();
		resultLayout.setSpacing(true);
		if (isRequried) {
			resultLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired(caption));
		} else {
			resultLayout.addComponent(getLabel(caption));
		}
		resultLayout.addComponent(component);
		return resultLayout;
	}
	
	/**
	 * 
	 */
	private void init() {
		lblLastContactDate = getLabelValue();
		lblLastContactInfo = getLabelValue();
		
		List<String> callOptions = Arrays.asList(new String[] { IN, OUT });
		cbxCallOption = ComponentFactory.getComboBox();
		cbxCallOption.setImmediate(true);
		cbxCallOption.setWidth(50, Unit.PIXELS);
		cbxCallOption.setNullSelectionAllowed(false);
		cbxCallOption.addItems(callOptions);
		cbxCallOption.addValueChangeListener(this);
		
		if (isCallIn) {
			cbxCallOption.select(IN);
		} else {
			cbxCallOption.select(OUT);
		}
		
		cbxPhone = new ComboBox();
		cbxPhone.setWidth(100, Unit.PIXELS);
		cbxPhone.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = 5931028998555615385L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxPhone.isSelected(OTHER)) {
					txtPhoneOtherValue.setVisible(true);
					lblPhoneOtherValue.setVisible(true);
					txtPhoneOtherValue.focus();
				} else {
					txtPhoneOtherValue.setVisible(false);
					lblPhoneOtherValue.setVisible(false);
				}
				
			}
		});
		
		cbxAddress = new AddressComboBox();
		cbxAddress.setImmediate(true);
		cbxAddress.setWidth(150, Unit.PIXELS);
		
		cbxAnswer = new EntityRefComboBox<EColResult>();
		cbxAnswer.setRestrictions(getResults(EColType.PHONE, null));
		cbxAnswer.getRestrictions().addOrder(Order.asc("descEn"));
		cbxAnswer.setCaptionRenderer(new Function<EColResult, String>() {
			/**
			 * @see java.util.function.Function#apply(java.lang.Object)
			 */
			@Override
			public String apply(EColResult t) {
				return t.getCode() + " - " + (I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc());
			}
		});
		cbxAnswer.renderer();
		cbxAnswer.setWidth(130, Unit.PIXELS);
		
		txtAnswer = ComponentFactory.getTextField();
		
		cbxSmsTemplate = new SMSTemplateComboBox(ENTITY_SRV.list(new BaseRestrictions<>(ESmsTemplate.class)));
		cbxSmsTemplate.setWidth(130, Unit.PIXELS);
		
		cbxEmailTemplate = new EmailTemplateComboBox(ENTITY_SRV.list(new BaseRestrictions<>(EmailTemplate.class)));
		cbxEmailTemplate.setWidth(130, Unit.PIXELS);
		
		cbxSubject = new EntityComboBox<EColSubject>(EColSubject.class, EColSubject.DESCEN);
		cbxSubject.renderer(new BaseRestrictions<EColSubject>(EColSubject.class));
		cbxSubject.setWidth(100, Unit.PIXELS);
		
		List<String> optionTypes = Arrays.asList(new String[] { SMS, CALL, FIELD, MAIL });
		optionGroupType = ComponentLayoutFactory.getOptionGroup(optionTypes);
//		optionGroupType.select(CALL);
		optionGroupType.addValueChangeListener(this);
		
		cbxContactWith = new ERefDataComboBox<>(EContactPerson.values());
		cbxContactWith.setWidth(100, Unit.PIXELS);
		cbxContactWith.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 7212968411396899495L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (EContactPerson.OTHER.equals(cbxContactWith.getSelectedEntity())) {
					txtOther.setVisible(true);
					txtOther.focus();
				} else {
					txtOther.setVisible(false);
				}
				
			}
		});
		
		cbxAnswer.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 8428614164580206031L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxAnswer.getSelectedEntity() != null) {
					txtRemark.setValue(cbxAnswer.getSelectedEntity().getDescLocale());
				} else {
					txtRemark.setValue(StringUtils.EMPTY);
				}
			}
		});
		
		cbxSmsTemplate.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 5746601078836511195L;

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
		
		cbxEmailTemplate.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -2950308971783296227L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxEmailTemplate.getSelectedEntity() != null) {
					txtRemark.setValue(getDefaultString(cbxEmailTemplate.getSelectedEntity().getContent()));
				} else {
					txtRemark.setValue(StringUtils.EMPTY);
				}
			}
		});
		
		txtOther = ComponentFactory.getTextField(150, 150);
		txtOther.setVisible(false);
		
		txtPhoneOtherValue = ComponentFactory.getTextField(150, 150);
		txtPhoneOtherValue.setVisible(false);
		
		lblPhoneOtherValue = getLabel("other");
		lblPhoneOtherValue.setVisible(false);
		
		txtRemark = ComponentFactory.getTextArea(false, 200, 60);
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		btnNewCall = new NativeButton(I18N.message("new.call"), this);
		btnNewCall.setStyleName("btn btn-success button-small");
		btnNewCall.setWidth(70, Unit.PIXELS);
		
		btnNewSMS = new NativeButton(I18N.message("new.sms"), this);
		btnNewSMS.setStyleName("btn btn-success button-small");
		btnNewSMS.setWidth(70, Unit.PIXELS);
		
		btnNewField = new NativeButton(I18N.message("new.field"), this);
		btnNewField.setStyleName("btn btn-success button-small");
		btnNewField.setWidth(70, Unit.PIXELS);
		
		btnNewEmail = new NativeButton(I18N.message("new.email"), this);
		btnNewEmail.setStyleName("btn btn-success button-small");
		btnNewEmail.setWidth(70, Unit.PIXELS);
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(optionGroupType)) {
			cbxAnswer.setSelectedEntity(null);
			cbxSmsTemplate.setSelectedEntity(null);
			cbxEmailTemplate.setSelectedEntity(null);
			cbxPhone.setVisible(!optionGroupType.isSelected(FIELD));
			cbxAddress.setVisible(optionGroupType.isSelected(FIELD));
			resultLayoutSMS.setVisible(optionGroupType.isSelected(SMS));
			resultLayoutEmail.setVisible(optionGroupType.isSelected(MAIL));
			resultLayoutField.setVisible(optionGroupType.isSelected(FIELD) || optionGroupType.isSelected(CALL));
			if (optionGroupType.isSelected(SMS)) {
				refreshContactValue(new ETypeContactInfo[] { ETypeContactInfo.MOBILE });
			} else if (optionGroupType.isSelected(MAIL)) {
				refreshContactValue(new ETypeContactInfo[] { ETypeContactInfo.EMAIL });
			} else if (optionGroupType.isSelected(CALL)) {
				refreshContactValue(new ETypeContactInfo[] { ETypeContactInfo.LANDLINE, ETypeContactInfo.MOBILE });
			} else if (optionGroupType.isSelected(FIELD)) {
				refreshAddressValue(new EApplicantType[] { EApplicantType.C, EApplicantType.G });
			}
			
			if (ProfileUtil.isCollection()) {
				if (ProfileUtil.isColField()) {
					if (optionGroupType.isSelected(CALL)) {
						cbxAnswer.setRestrictions(getResults(EColType.FIELD, "PF"));
						cbxAnswer.renderer();
					} else if (optionGroupType.isSelected(FIELD)) {
						cbxAnswer.setRestrictions(getResults(EColType.FIELD, "F"));
						cbxAnswer.renderer();
					}
				} else {
					if (optionGroupType.isSelected(CALL)) {
						cbxAnswer.setRestrictions(getResults(EColType.PHONE, null));
						cbxAnswer.renderer();
					} else if (optionGroupType.isSelected(FIELD)) {
						cbxAnswer.setRestrictions(getResults(EColType.FIELD, null));
						cbxAnswer.renderer();
					}
				}
			} else if (ProfileUtil.isCallCenter()) {
				cbxAnswer.setRestrictions(getResults(EColType.CALL, null));
				cbxAnswer.renderer();
			} 
			
		 
		} else if (event.getProperty().equals(cbxCallOption)) {
			if (cbxCallOption.isSelected(IN)) {
				this.isCallIn = true;
			} else {
				this.isCallIn = false;
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
	 * 
	 * @return
	 */
	private HorizontalLayout getLastContactDetailLayout() {
		HorizontalLayout contactLayout = new HorizontalLayout();
		contactLayout.setSpacing(true);
		contactLayout.addComponent(new LabelFormCustomLayout("last.contact.date", lblLastContactDate.getValue()));
		contactLayout.addComponent(new LabelFormCustomLayout("last.contact", lblLastContactInfo.getValue()));		
		return contactLayout;
	}
	
	/**
	 * 
	 * @return menuButtonLayout
	 */
	private HorizontalLayout getButtonMenuLayout() {
		HorizontalLayout menuButtonLayout = new HorizontalLayout();
		menuButtonLayout.setSpacing(true);
		menuButtonLayout.addComponent(btnNewCall);
		menuButtonLayout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		menuButtonLayout.addComponent(btnNewField);
		menuButtonLayout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		menuButtonLayout.addComponent(btnNewSMS);
		menuButtonLayout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		menuButtonLayout.addComponent(btnNewEmail);
		return menuButtonLayout;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label(StringUtils.EMPTY, ContentMode.HTML);
		return label;
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void assignLastContactValue(Contract contract) {
		lblLastContactDate.setValue(StringUtils.EMPTY);
		lblLastContactInfo.setValue(StringUtils.EMPTY);
		if (contract != null) {
			Collection col = contract.getCollection();
			if (col != null) {
				CollectionHistory colHisto = col.getLastCollectionHistory();
				if (colHisto != null) {
					lblLastContactDate.setValue(getDateFormat(colHisto.getCreateDate()));
					String type = colHisto.isCallIn() ? IN : OUT;
					String contactWith = colHisto.getReachedPerson() != null ? colHisto.getReachedPerson().getDescLocale() : StringUtils.EMPTY;
					String subject = colHisto.getSubject() != null ? colHisto.getSubject().getDescLocale() : StringUtils.EMPTY;
					String contactType = colHisto.getContactedTypeInfo() != null ? colHisto.getContactedTypeInfo().getCode() : StringUtils.EMPTY;
					String contactValue = colHisto.getContactedInfoValue() != null ? colHisto.getContactedInfoValue() : colHisto.getOtherContact() ;
					String phone = contactType + "-" + contactValue;
					String answer = colHisto.getComment();
					
					String lastConatactDetail = type + " - " + contactWith + " - " + subject + " - " + phone + " - " + answer;
					lblLastContactInfo.setValue(lastConatactDetail);
				}
			}
		}
		HorizontalLayout detailLayout = getLastContactDetailLayout();
		if (mainLayout.getComponentCount() == 4) {
			mainLayout.removeComponent(mainLayout.getComponent(0));
		} 
		mainLayout.addComponent(detailLayout, 0);
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = ComponentLayoutFactory.getLabelCaption(caption);
		return label;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assingValues(Contract contract) {
		reset();
		this.contract = contract;

		assignLastContactValue(contract);
		
		refreshContactValue(new ETypeContactInfo[] { ETypeContactInfo.MOBILE });
		
		tablePanel.assignValues(contract);
	} 
	
	/**
	 * 
	 * @param applicantTypes
	 */
	private void refreshAddressValue(EApplicantType[] applicantTypes) {
		ApplicantAddressInfoVO appAddress = new ApplicantAddressInfoVO(contract, applicantTypes);
		cbxAddress.removeAllItems();
		if (!appAddress.getAddresses().isEmpty()) {
			cbxAddress.setEntities(appAddress.getAddresses());
		}
	}
	
	/**
	 * 
	 * @param typeContactInfos
	 */
	private void refreshContactValue(ETypeContactInfo[] typeContactInfos) {
		ApplicantContactInfoVO appContactInfoVO = new ApplicantContactInfoVO(contract, typeContactInfos);
		cbxPhone.removeAllItems();
		if (!appContactInfoVO.getAppContactTypeVOs().isEmpty()) {
			for (AppContactTypeVO data : appContactInfoVO.getAppContactTypeVOs()) {
				cbxPhone.addItem(data);
				cbxPhone.setItemCaption(data, data.toString());
			}
		}
		if (optionGroupType.isSelected(CALL) || optionGroupType.isSelected(SMS)) {
			cbxPhone.removeItem(OTHER);
			cbxPhone.addItem(OTHER);
		}
	}
	
	/**
	 * 
	 * @param item
	 */
	private void setVisibleOptionGroupt(String item) {
		optionGroupType.removeAllItems();
		optionGroupType.addItem(item);
		optionGroupType.select(item);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		setEnabledButton();
		if (event.getButton().equals(btnAdd)) {
			save();
		}
		if (event.getButton().equals(btnNewCall)) {
			btnNewCall.setEnabled(false);
			mainPanel.setVisible(true);
			setVisibleOptionGroupt(CALL);
		}
		if (event.getButton().equals(btnNewField)) {
			btnNewField.setEnabled(false);
			mainPanel.setVisible(true);
			setVisibleOptionGroupt(FIELD);
		}
		if (event.getButton().equals(btnNewSMS)) {
			btnNewSMS.setEnabled(false);
			mainPanel.setVisible(true);
			setVisibleOptionGroupt(SMS);
		}
		if (event.getButton().equals(btnNewEmail)) {
			btnNewEmail.setEnabled(false);
			mainPanel.setVisible(true);
			setVisibleOptionGroupt(MAIL);
		}
	}
	
	/**
	 * set enable button
	 */
	private void setEnabledButton() {
		btnNewCall.setEnabled(true);
		btnNewSMS.setEnabled(true);
		btnNewField.setEnabled(true);
		btnNewEmail.setEnabled(true);
	}
	
	/**
	 * Save
	 */
	public void save() {
		if (isValid()) {
			SecUser userLogin = UserSessionManager.getCurrentUser();
			this.colHistory = CollectionHistory.createInstance();
			this.colHistory.setContract(this.contract);
			
			if (ProfileUtil.isCMProfile()) {
				this.colHistory.setCmResult(txtAnswer.getValue());
			} else { 
				this.colHistory.setResult(cbxAnswer.getSelectedEntity());
			}
		
			this.colHistory.setComment(txtRemark.getValue());
			
			this.colHistory.setCallIn(this.isCallIn);
			if (optionGroupType.isSelected(SMS)) {
				this.colHistory.setCallType(ECallType.SMS);
			} else if (optionGroupType.isSelected(CALL)) {
				this.colHistory.setCallType(ECallType.CALL);
			} else if (optionGroupType.isSelected(FIELD)) {
				this.colHistory.setCallType(ECallType.FIELD);
			} else if (optionGroupType.isSelected(MAIL)) {
				this.colHistory.setCallType(ECallType.MAIL);
			}
			
			this.colHistory.setSubject(cbxSubject.getSelectedEntity());
			this.colHistory.setReachedPerson(cbxContactWith.getSelectedEntity());
			this.colHistory.setOther(txtOther.getValue());
			this.colHistory.setOtherContact(txtPhoneOtherValue.getValue());
			
			if (!cbxPhone.isSelected(OTHER)) {
				AppContactTypeVO appContactTypeVO = (AppContactTypeVO) cbxPhone.getValue();
				if (!optionGroupType.isSelected(FIELD)) {
					this.colHistory.setContactedPerson(EApplicantType.getByCode(appContactTypeVO.getCode()));
					this.colHistory.setContactedTypeInfo(ETypeContactInfo.getByCode(appContactTypeVO.getType()));
					this.colHistory.setContactedInfoValue(appContactTypeVO.getValue());
				} else {
					Address address = Address.createInstance();
					address = AddressUtils.copy(cbxAddress.getSelectedEntity(), address);
					this.colHistory.setAddress(address);
				}
				if (optionGroupType.isSelected(SMS)) {
					try {
						ClientSms.sendSms(appContactTypeVO.getValue(), txtRemark.getValue(), userLogin.getLogin());
						try {
							COL_SRV.saveOrUpdateLatestColHistory(this.colHistory);
							displaySuccessfullyMsg();
						} catch (Exception e) {
							ComponentLayoutFactory.displayErrorMsg(e.getMessage());
						}
					} catch (Exception e) {
						ComponentLayoutFactory.displayErrorMsg("Error while Send SMS. [" + appContactTypeVO.toString() + "]");
					}
				} else {
					try {
						COL_SRV.saveOrUpdateLatestColHistory(this.colHistory);
						displaySuccessfullyMsg();
					} catch (Exception e) {
						ComponentLayoutFactory.displayErrorMsg(e.getMessage());
					}
				}
			} else {
				if (optionGroupType.isSelected(SMS)) {
					try {
						ClientSms.sendSms(txtPhoneOtherValue.getValue(), txtRemark.getValue(), userLogin.getLogin());
						try {
							COL_SRV.saveOrUpdateLatestColHistory(this.colHistory);
							displaySuccessfullyMsg();
						} catch (Exception e) {
							ComponentLayoutFactory.displayErrorMsg(e.getMessage());
						}
					} catch (Exception e) {
						ComponentLayoutFactory.displayErrorMsg("Error while Send SMS. [" + txtPhoneOtherValue.getValue() + "]");
					}
				} else {
					try {
						COL_SRV.saveOrUpdateLatestColHistory(this.colHistory);
						displaySuccessfullyMsg();
					} catch (Exception e) {
						ComponentLayoutFactory.displayErrorMsg(e.getMessage());
					}
				}
			}
		} else {
			ComponentLayoutFactory.displayErrorMsg(errors.get(0).toString());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isValid() {
		errors.clear();
		if (optionGroupType.isSelected(FIELD)) {
			checkMandatorySelectField(cbxAddress, "no.add.mail");
		} else {
			checkMandatorySelectField(cbxPhone, "no.add.mail");
		}
		if (optionGroupType.isSelected(CALL) || optionGroupType.isSelected(FIELD)) {
			checkMandatorySelectField(cbxAnswer, "answer");
		}
		checkMandatoryField(txtRemark, "remark");
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	private void displaySuccessfullyMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
		reset();
		assignLastContactValue(this.contract);
		tablePanel.assignValues(this.contract);
	}
	
	/**
	 * 
	 */
	protected void reset() {
		this.colHistory = null;
		cbxPhone.setValue(null);
		cbxAnswer.setSelectedEntity(null);
		cbxSmsTemplate.setSelectedEntity(null);
		cbxEmailTemplate.setSelectedEntity(null);
		cbxContactWith.setSelectedEntity(null);
		cbxSubject.setSelectedEntity(null);
		txtRemark.setValue(StringUtils.EMPTY);
//		optionGroupType.select(CALL);
		txtPhoneOtherValue.setValue(StringUtils.EMPTY);
		if (isCallIn) {
			cbxCallOption.select(IN);
		} else {
			cbxCallOption.select(OUT);
		}
	}
	
	/**
	 * Get ColResut to combo box by colType
	 * @param colType
	 * @return
	 */
	private BaseRestrictions<EColResult> getResults(EColType colType, String codeStart) {
		BaseRestrictions<EColResult> restrictions = new BaseRestrictions<>(EColResult.class);
		if (colType != null) {
			restrictions.addCriterion(Restrictions.eq("colTypes", colType));
		}
		if (codeStart != null) {
			restrictions.addCriterion(Restrictions.like("code", codeStart, MatchMode.START));
		}
		restrictions.addOrder(Order.asc(EColResult.ID));
		return restrictions;
	}

}