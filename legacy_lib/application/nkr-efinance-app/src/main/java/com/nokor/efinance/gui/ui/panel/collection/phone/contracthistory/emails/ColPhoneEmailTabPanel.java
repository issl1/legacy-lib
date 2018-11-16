package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.emails;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.collection.model.MEmail;
import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.Email;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.EmailTemplateComboBox;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneEmailTabPanel extends AbstractControlPanel implements MEmail, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1685150775035677250L;
	
	private ERefDataComboBox<EApplicantType> cbxSendTo;
	private ComboBox cbxMailAddress;
	private AutoDateField dfDate;
	private CheckBox cbNow;
	private TextArea txtMessage;
	private TextField txtSubject;
	private EmailTemplateComboBox cbxEmailTemplate;
	private Button btnSend;
	
	private SimpleTable<Email> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	private Contract contract;
	
	/**
	 * 
	 */
	public ColPhoneEmailTabPanel() {
		setSpacing(true);
		setMargin(true);
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		
		cbxSendTo = new ERefDataComboBox<>(EApplicantType.values());
		cbxSendTo.setWidth(120, Unit.PIXELS);
		cbxSendTo.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = -504177512802837324L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				List<String> mailAddresses = getMailAddresses(cbxSendTo.getSelectedEntity());
				cbxMailAddress.removeAllItems();
				for (String mailAddress : mailAddresses) {
					cbxMailAddress.addItem(mailAddress);
				}
			}
		});
		cbxMailAddress = new ComboBox();
		cbxMailAddress.setWidth(150, Unit.PIXELS);
		dfDate = ComponentFactory.getAutoDateField();
		cbNow = new CheckBox(I18N.message("now"));
		cbNow.addValueChangeListener(new ValueChangeListener() {
		
			/** */
			private static final long serialVersionUID = 8079966399543285406L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbNow.getValue()) {
					dfDate.setEnabled(false);
					dfDate.setValue(DateUtils.today());
				} else {
					dfDate.setEnabled(true);
				}
				
			}
		});
		btnSend = ComponentLayoutFactory.getDefaultButton("send", FontAwesome.SEND, 60);
		btnSend.addClickListener(this);
		txtMessage = ComponentFactory.getTextArea(false, 300, 70);
		txtSubject = ComponentFactory.getTextField(false, 100, 150);
		
		cbxEmailTemplate = new EmailTemplateComboBox(ENTITY_SRV.list(new BaseRestrictions<>(EmailTemplate.class)));
		cbxEmailTemplate.setWidth(150, Unit.PIXELS);
		
		cbxEmailTemplate.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 1821368929225223259L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxEmailTemplate.getSelectedEntity() != null) {
					txtMessage.setValue(getDefaultString(cbxEmailTemplate.getSelectedEntity().getContent()));
				} else {
					txtMessage.setValue(StringUtils.EMPTY);
				}
			}
		});
		
		simpleTable = new SimpleTable<>(getColumnDefinitions());
		simpleTable.setPageLength(5);
		simpleTable.setCaption(I18N.message("mail"));
		
		Label lblSendTo = getLabel("send.to");
		Label lblDate = getLabel("date");
		Label lblComment = getLabel("message");
		Label lblSubject = getLabel("subject");
		Label lblEmailTemplate = getLabel("email.template");
		
		GridLayout gridLayout = new GridLayout(8, 2);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(true, true, false, true));
	
		int iCol = 0;
		gridLayout.addComponent(lblSendTo, iCol++, 0);
		gridLayout.addComponent(cbxSendTo, iCol++, 0);
		gridLayout.addComponent(cbxMailAddress, iCol++, 0);
		gridLayout.addComponent(lblDate, iCol++, 0);
		gridLayout.addComponent(dfDate, iCol++, 0);
		gridLayout.addComponent(cbNow, iCol++, 0);
		gridLayout.addComponent(lblSubject, iCol++, 0);
		gridLayout.addComponent(txtSubject, iCol++, 0);
		
		gridLayout.setComponentAlignment(lblSendTo, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbNow, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblSubject, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(new MarginInfo(false, true, true, true));
		horizontalLayout.addComponent(lblEmailTemplate);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(7, Unit.PIXELS));
		horizontalLayout.addComponent(cbxEmailTemplate);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(lblComment);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(7, Unit.PIXELS));
		horizontalLayout.addComponent(txtMessage);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(btnSend);
		
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		formLayout.addComponent(gridLayout);
		formLayout.addComponent(horizontalLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(ComponentLayoutFactory.getPanel(formLayout, false, false));
		mainLayout.addComponent(simpleTable);
		
		addComponent(mainLayout);
	}
	
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("email.id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(SEND_TO, I18N.message("send.to"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(SUBJECT, I18N.message("subject"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(SEND_DATE, I18N.message("send.on"), Date.class, Align.LEFT, 90));
		return columnDefinitions;
	}
	
	/**
	 * get mail address by applicant type
	 * @param applicantType
	 * @return
	 */
	private List<String> getMailAddresses(EApplicantType applicantType) {
		List<String> mailAddresses = new ArrayList<>();
		
		Map<EApplicantType, Applicant> applicants = new LinkedHashMap<>();
		if (EApplicantType.C.equals(applicantType)) {
			applicants.put(EApplicantType.C, contract.getApplicant());
		} else {
			ContractApplicant conApp = contract.getContractApplicant(EApplicantType.G);
			if (conApp != null) {
				applicants.put(EApplicantType.G, conApp.getApplicant());
			}
		}
		if (!applicants.isEmpty()) {
			for (EApplicantType key : applicants.keySet()) {
				Applicant applicant = applicants.get(key);
				Individual individual = applicant.getIndividual();
				if (individual != null) {
					List<IndividualContactInfo> individualContactInfos = individual.getIndividualContactInfos();
					for (IndividualContactInfo individualContactInfo : individualContactInfos) {
						ContactInfo contactInfo = individualContactInfo.getContactInfo();
						if (ETypeContactInfo.EMAIL.equals(contactInfo.getTypeInfo())) {
							mailAddresses.add(contactInfo.getValue());
						}
					}
				}
			}
		}
		return mailAddresses;
	}
	
	/**
	 * 
	 * @param letters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Container getIndexedContainer(List<Email> emails) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		
		for (Email email : emails) {
			Item item = indexedContainer.addItem(email.getId());
			
			item.getItemProperty(ID).setValue(email.getId());
			if (email.getSendTo() != null) {
				item.getItemProperty(SEND_TO).setValue(email.getSendTo().getDescEn());
			}
			item.getItemProperty(CREATEDATE).setValue(email.getCreateDate());
			item.getItemProperty(SUBJECT).setValue(email.getSubject());
			item.getItemProperty(SEND_DATE).setValue(email.getSendDate());
		}
		return indexedContainer;
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		reset();
		if (contract != null) {
			this.contract = contract;
			List<Email> letters = getEmailsSent();
			simpleTable.setContainerDataSource(getIndexedContainer(letters));
		}
	}
	
	/**
	 * get list of Email sent
	 * @return
	 */
	private List<Email> getEmailsSent() {
		BaseRestrictions<Email> restrictions = new BaseRestrictions<>(Email.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		return ENTITY_SRV.list(restrictions);
	}
	
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		cbxSendTo.setSelectedEntity(null);
		cbxMailAddress.setValue(null);
		cbxEmailTemplate.setSelectedEntity(null);
		dfDate.setValue(null);
		txtSubject.setValue("");
		cbNow.setValue(false);
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		errors.clear();
		
		if (cbxMailAddress.getValue() == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("field.required.1", I18N.message("email.address")), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
			return false;
		}
		return errors.isEmpty();
	}
	
	/**
	 * Label Template
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(caption);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSend) {	
			if (validate()) {
				SecUser user = UserSessionManager.getCurrentUser();
				Email email = new Email();
				email.setContract(contract);
				email.setUserLogin(user.getLogin());
				email.setSendTo(cbxSendTo.getSelectedEntity());
				email.setSendEmail(String.valueOf(cbxMailAddress.getValue()));
				email.setSendDate(dfDate.getValue());
				email.setSubject(txtSubject.getValue());
				email.setMessage(txtMessage.getValue());
				try {
					ENTITY_SRV.saveOrUpdate(email);
					assignValue(contract);
					Notification.show("", I18N.message("msg.info.save.successfully"), Type.HUMANIZED_MESSAGE);
				} catch (DaoException e) {
					logger.error(e.getMessage());
					Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
				}
			}
		}
	}

}
