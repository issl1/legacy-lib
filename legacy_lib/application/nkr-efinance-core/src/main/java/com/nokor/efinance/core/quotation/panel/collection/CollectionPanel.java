package com.nokor.efinance.core.quotation.panel.collection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.panel.AddressPanel;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionConfig;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * Collection tab in CM_Profile
 * @author uhout.cheng
 */
public class CollectionPanel extends AbstractTabPanel implements ValueChangeListener, ClickListener, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = 2106235209548212101L;

	private final static String OPEN_TABLE = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:0;float:left;\" >";
	private final static String OPEN_TR = "<tr>";
	private final static String OPEN_TD = "<td align=\"left\" >";
	private final static String CLOSE_TH = "</th>";
	private final static String CLOSE_TR = "</tr>";
	private final static String CLOSE_TD = "</td>";
	private final static String CLOSE_TABLE = "</table>";
	
	private CollectionHistory collectionHistory;
	private TextField txtPaidTerms;
	private TextField txtRemainningTerms;
	private AutoDateField dfNextDueDate;
	private TextField txtPenaltyBalance;
	private TextField txtFollowingFeeBalance;
	private TextField txtCurrentTerms;
	private TextField txtOverdueTerms;
	private TextField txtOverdueDays;
	private TextField txtOverdueAmount;
	private TextField txtOverdueMonths;
	private TextField txtPaymentPattern;
	private TextField txtDebtLevel;
	private SimpleTable<Entity> collectionDetailTable;
	private ERefDataComboBox<EColType> cbxOrigin;
	private ERefDataComboBox<EApplicantType> cbxContactPerson;
	private ERefDataComboBox<ETypeContactInfo> cbxContactNumber;
	private EntityRefComboBox<EColResult> cbxResult;
	private TextArea txtRemark;
	private TextField txtContactNumber;
	private Button btnAdd;
	private Label lblOverdue30Value;
	private Label lblOverdue60Value;
	private Label lblOverdue90Value;
	private Label lblTotalValue;
	private ERefDataComboBox<ETypeAddress> cbxContactAddress;
	private AddressPanel addressPanel;
	private VerticalLayout messagePanel;
	private List<String> lstErrors;
	private Contract contract;
	
	private Button btnRequestAssist;
	private Button btnRequestFlag;
	// private Button btnRequestTeam;
	
	private Button btnRequestExtend;
	private Button btnRequestAssistStatus;
	
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField txtField = getTextField();
		txtField.setCaption(I18N.message(caption));
		return txtField;
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField() {
		TextField txtField = ComponentFactory.getTextField(60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(200, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(200, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.setStyleName("myform-align-left");
		formLayout.setSizeUndefined();
		return formLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> createSimpleTable(String caption, List<ColumnDefinition> columnDefinitions) {
		List<ColumnDefinition> list = columnDefinitions;
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(list);
		simpleTable.setPageLength(5);
		simpleTable.setCaption(I18N.message(caption));
		return simpleTable;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtPaidTerms = getTextField("paid.terms");
		txtRemainningTerms = getTextField("remainning.terms");
		txtPenaltyBalance = getTextField("balance.penalty");
		txtFollowingFeeBalance = getTextField("balance.following.fee");
		txtCurrentTerms = getTextField("current.terms");
		txtOverdueTerms = getTextField("overdue.terms");
		txtOverdueDays = getTextField("overdue.days");
		txtOverdueAmount = getTextField("overdue.amount");
		txtOverdueMonths = getTextField("overdue.months");
		txtPaymentPattern = getTextField();
		txtDebtLevel = getTextField();
		dfNextDueDate = ComponentFactory.getAutoDateField("next.due.date", false);
		collectionDetailTable = createSimpleTable(null, getCollectionDetailColumnDefinitions());
		cbxOrigin = getERefDataComboBox(EColType.values());
		cbxContactPerson = getERefDataComboBox(EApplicantType.values());
		cbxContactNumber = getERefDataComboBox(ETypeContactInfo.values());
		cbxResult = getEntityRefComboBox(new BaseRestrictions<>(EColResult.class));
		cbxContactAddress = getERefDataComboBox(ETypeAddress.values());
		cbxContactAddress.setCaption(I18N.message("contact.address"));
		cbxContactAddress.addValueChangeListener(this);
		cbxContactNumber.addValueChangeListener(this);
		cbxOrigin.addValueChangeListener(this);
		txtContactNumber = getTextField();
		txtContactNumber.setEnabled(true);
		txtContactNumber.setVisible(false);
		txtRemark = ComponentFactory.getTextArea(null, false, 200, 80);
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAdd.setWidth(70, Unit.PIXELS);
		btnAdd.setHeight(30, Unit.PIXELS);
		lblOverdue30Value = new Label();
		lblOverdue60Value = new Label();
		lblOverdue90Value = new Label();
		lblTotalValue = new Label();
		addressPanel = new AddressPanel();
		
		messagePanel = new VerticalLayout();
		messagePanel.setSizeUndefined();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		lstErrors = new ArrayList<String>();
		
		btnRequestAssist = new NativeButton(I18N.message("request.assist"));
		btnRequestAssist.addClickListener(this);
		btnRequestAssist.setIcon(FontAwesome.FLAG);
		
		
		btnRequestFlag = new NativeButton(I18N.message("request.flag"));
		btnRequestFlag.addClickListener(this);
		btnRequestFlag.setIcon(FontAwesome.FLAG);
		
		/*btnRequestTeam = new NativeButton(I18N.message("request.team"));
		btnRequestTeam.addClickListener(this);*/
		
		btnRequestExtend = new NativeButton(I18N.message("request.extend"));
		btnRequestExtend.addClickListener(this);
		btnRequestExtend.setIcon(FontAwesome.FLAG);
		
		btnRequestAssistStatus = new NativeButton(I18N.message("request.assist.status"));
		btnRequestAssistStatus.addClickListener(this);
		btnRequestAssistStatus.setIcon(FontAwesome.FLAG);
		
	        
	    NavigationPanel navigationPanel = new NavigationPanel();
	    navigationPanel.addButton(btnRequestAssist);
	    navigationPanel.addButton(btnRequestFlag);
	    // navigationPanel.addButton(btnRequestTeam);
	    navigationPanel.addButton(btnRequestExtend);
	    navigationPanel.addButton(btnRequestAssistStatus);
	    		
		btnAdd.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 7372947809398252287L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				removeAllErrorsPanel();
				if (cbxOrigin.getSelectedEntity() != null) {
					Address address = null;
					if (!cbxOrigin.getSelectedEntity().getId().equals(EColType.PHONE.getId())) {
						if (cbxContactAddress.getSelectedEntity() != null) {
							address = new Address();
							address.setType(cbxContactAddress.getSelectedEntity());
							if (validateAddressControls()) {
								logger.debug("[>> saveOrUpdateAddress]");
								
								ENTITY_SRV.saveOrUpdate(addressPanel.getAddress(address));
								logger.debug("[>> saveOrUpdateAddress]");
								
								saveOrUpdateCollectionHistory(address);
							} else {
								displayAllErrorsPanel();
							}
						} else {
							saveOrUpdateCollectionHistory(address);
						}
					} else {
						saveOrUpdateCollectionHistory(address);
					}
				} else {
					if (!validateSelectedControls()) {
						displayAllErrorsPanel();
					}
				}
				setCollectionHistoryIndexedContainer(getCollectionHistories(contract));
			}
		});
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(getTopLayout());
		mainLayout.addComponent(collectionDetailTable);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(getColContactInfoAddressLayout());
		return mainLayout;
	}
	
	/**
	 * 
	 * @param address
	 */
	private void saveOrUpdateCollectionHistory(Address address) {
		logger.debug("[>> saveOrUpdateCollectionHistory]");
		
		ENTITY_SRV.saveOrUpdate(getEntity(address));
	
		logger.debug("[>> saveOrUpdateCollectionHistory]");
		removeAllErrorsPanel();
		displaySuccessMsg();
		resetControls();
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbxOrigin)) {
			if (cbxOrigin.getSelectedEntity() != null) {
				if (cbxOrigin.getSelectedEntity().getId().equals(EColType.PHONE.getId())) {
					cbxContactAddress.setVisible(false);
					addressPanel.setVisible(false);
				} else {
					cbxContactAddress.setSelectedEntity(null);
					cbxContactAddress.setVisible(true);
				}
			} else {
				cbxContactAddress.setVisible(false);
				addressPanel.setVisible(false);
			}
		} else if (event.getProperty().equals(cbxContactNumber)) {
			if (cbxContactNumber.getSelectedEntity() != null) {
				txtContactNumber.setVisible(true);
			} else {
				txtContactNumber.setVisible(false);
			}
		} else if (event.getProperty().equals(cbxContactAddress)) {
			if (cbxContactAddress.getSelectedEntity() != null) {
				addressPanel.setVisible(true);
			} else {
				addressPanel.setVisible(false);
			}
		} 
	}
	
	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		for (String error : lstErrors) {
			Label messageLabel = new Label(error);
			messageLabel.addStyleName("error");
			messagePanel.addComponent(messageLabel);
		}
		messagePanel.setVisible(true);
	}
	
	/**
	 * Remove error message panel
	 */
	public void removeAllErrorsPanel() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		lstErrors.clear();
	}
	
	/**
	 * Reset collection history controls
	 */
	private void resetControls() {
		cbxOrigin.setSelectedEntity(null);
		cbxContactPerson.setSelectedEntity(null);
		cbxContactNumber.setSelectedEntity(null);
		cbxResult.setSelectedEntity(null);
		txtContactNumber.setValue("");
		cbxContactAddress.setSelectedEntity(null);
		txtRemark.setValue("");
		addressPanel.reset();
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getColContactAddressLayout() {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setSpacing(true);
		FormLayout fLayout = new FormLayout(cbxContactAddress);
		fLayout.setWidth(140, Unit.PIXELS);
		fLayout.setStyleName("myform-align-left");
		verLayout.addComponent(fLayout);
		verLayout.addComponent(addressPanel);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CollectionHistory getEntity(Address address) {
		collectionHistory = new CollectionHistory();
		collectionHistory.setContract(contract);
		collectionHistory.setOrigin(cbxOrigin.getSelectedEntity());
		collectionHistory.setContactedPerson(cbxContactPerson.getSelectedEntity());
		collectionHistory.setContactedTypeInfo(cbxContactNumber.getSelectedEntity());
		collectionHistory.setContactedInfoValue(txtContactNumber.getValue());
		collectionHistory.setResult(cbxResult.getSelectedEntity());
		collectionHistory.setComment(txtRemark.getValue());
		if (address != null) {
			collectionHistory.setAddress(address);	
		}
		return collectionHistory;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			this.contract = contract;
			Collection collection = contract.getCollection();
			if (collection != null) {
				txtPaidTerms.setValue(getDefaultString(collection.getNbInstallmentsPaid()));
				int currentTerm = MyNumberUtils.getInteger(collection.getCurrentTerm());
				if (currentTerm > 1) {
					txtRemainningTerms.setValue(getDefaultString((contract.getTerm() - (currentTerm - 1))));
				} else if (currentTerm == 1) {
					txtRemainningTerms.setValue(getDefaultString(contract.getTerm()));
				} 
				
				dfNextDueDate.setValue(collection.getNextDueDate());
				txtPenaltyBalance.setValue(AmountUtils.format(collection.getTiPenaltyAmount()));
				txtFollowingFeeBalance.setValue(AmountUtils.format(collection.getTiFollowingFeeAmount()));
				txtCurrentTerms.setValue(getDefaultString(currentTerm));
				txtOverdueTerms.setValue(getDefaultString(collection.getNbInstallmentsInOverdue()));
				txtOverdueDays.setValue(getDefaultString(collection.getNbOverdueInDays()));
				txtOverdueAmount.setValue(getDefaultString(collection.getTiTotalAmountInOverdue()));
				txtOverdueMonths.setValue("");
				txtPaymentPattern.setValue("");
				txtDebtLevel.setValue(getDefaultString(collection.getDebtLevel()));
				lblOverdue30Value.setValue(collection.getNbInstallmentsInOverdue0030() != null ? 
						collection.getNbInstallmentsInOverdue0030().toString() : "0");
				lblOverdue60Value.setValue(collection.getNbInstallmentsInOverdue3160() != null ? 
						collection.getNbInstallmentsInOverdue3160().toString() : "0");
				lblOverdue90Value.setValue(collection.getNbInstallmentsInOverdue6190() != null ? 
						collection.getNbInstallmentsInOverdue6190().toString() : "0");
				int tolalOverdue = MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue0030())
								 + MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue3160())
								 + MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue6190());
				lblTotalValue.setValue(getDefaultString(tolalOverdue));
				
				btnRequestExtend.setVisible(true);
				btnRequestFlag.setVisible(true);
				btnRequestAssistStatus.setVisible(true);
				btnRequestAssist.setVisible(true);
				if (ERequestStatus.PENDING.equals(collection.getRequestExtendStatus())) {
					btnRequestExtend.setVisible(false);
				}
				
				/*if (ERequestStatus.REQUEST.equals(collection.getRequestFlagStatus())) {
					btnRequestFlag.setVisible(false);
				}
				
				if (ERequestStatus.REJECT.equals(collection.getRequestAssistStatus())) {
					btnRequestAssistStatus.setVisible(false);
				}
				
				if (ERequestStatus.REQUEST.equals(collection.getRequestAssistStatus())) {
					btnRequestAssist.setVisible(false);
				}*/
			}
			setCollectionHistoryIndexedContainer(getCollectionHistories(contract));
			cbxContactAddress.setVisible(false);
			addressPanel.setVisible(false);
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<CollectionHistory> getCollectionHistories(Contract contract) {
		BaseRestrictions<CollectionHistory> restrictions = new BaseRestrictions<>(CollectionHistory.class);	
		restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		return ENTITY_SRV.list(restrictions);
	}
	
	/** */
	private VerticalLayout getTopLayout() {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(getTopLeftLayout());
		horLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horLayout.addComponent(getTopMiddleLayout());
		horLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horLayout.addComponent(getTopRightCustomLayout());
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setStyleName("overflow-layout-style");
		verLayout.addComponent(horLayout);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getCollectionDetailColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("staff.code", I18N.message("staff.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("origin", I18N.message("origin"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("contact.no", I18N.message("contact.no"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("contact.person", I18N.message("contact.person"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("contact.address", I18N.message("contact.address"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition("results", I18N.message("results"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("remarks", I18N.message("remarks"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}

	/**
	 * 
	 * @param collectionHistories
	 */
	@SuppressWarnings("unchecked")
	private void setCollectionHistoryIndexedContainer(List<CollectionHistory> collectionHistories) {
		Container indexedContainer = collectionDetailTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (!collectionHistories.isEmpty()) {
			for (CollectionHistory collectionHistory : collectionHistories) {
				Item item = indexedContainer.addItem(collectionHistory.getId());
				item.getItemProperty("date").setValue(collectionHistory.getCreateDate());
				item.getItemProperty("staff.code").setValue(collectionHistory.getCreateUser());
				item.getItemProperty("origin").setValue(collectionHistory.getOrigin() != null ? 
						collectionHistory.getOrigin().getDescEn() : "");
				item.getItemProperty("contact.no").setValue(collectionHistory.getContactedInfoValue());
				item.getItemProperty("contact.person").setValue(collectionHistory.getContactedPerson() != null ?
						collectionHistory.getContactedPerson().getDescEn() : "");
				item.getItemProperty("results").setValue(collectionHistory.getResult() != null ?
						collectionHistory.getResult().getDescEn() : "");
				item.getItemProperty("remarks").setValue(collectionHistory.getComment());
				if (collectionHistory.getAddress() != null) {
					item.getItemProperty("contact.address").setValue(getAddress(collectionHistory.getAddress()));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private String getAddress(Address address) {
		StringBuffer referenceName = new StringBuffer(); 
		List<String> descriptions = new ArrayList<>();
		descriptions.add(getDefaultString(address.getHouseNo()));
		descriptions.add(getDefaultString(address.getLine1()));
		descriptions.add(getDefaultString(address.getLine2()));
		descriptions.add(getDefaultString(address.getStreet()));
		descriptions.add(address.getCommune() != null ? address.getCommune().getDescEn() : "");
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDescEn() : "");
		descriptions.add(address.getProvince() != null ? address.getProvince().getDescEn() : "");
		descriptions.add(getDefaultString(address.getPostalCode()));
		for (String string : descriptions) {
			referenceName.append(string);
			if (StringUtils.isNotEmpty(string)) {
				referenceName.append(",");
			}
		}
		int lastIndex = referenceName.lastIndexOf(",");
		referenceName.replace(lastIndex, lastIndex + 1, "");
		return referenceName.toString();
	}
	
	/** */
	private FormLayout getTopLeftLayout() {
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtPaidTerms);
		formLayout.addComponent(txtRemainningTerms);
		formLayout.addComponent(dfNextDueDate);
		formLayout.addComponent(txtPenaltyBalance);
		formLayout.addComponent(txtFollowingFeeBalance);
		return formLayout;
	}
	
	/** */
	private FormLayout getTopMiddleLayout() {
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtCurrentTerms);
		formLayout.addComponent(txtOverdueTerms);
		formLayout.addComponent(txtOverdueDays);
		formLayout.addComponent(txtOverdueAmount);
		formLayout.addComponent(txtOverdueMonths);
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getTopRightCustomLayout() {
		String OPEN_TH = "<th bgcolor=\"#e1e1e1\" class=\"align-center\" width=\"36\" style=\"border:1px solid black; border-collapse:collapse;\" >";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblPaymentPattern\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtPaymentPattern\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblNumberOverdue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
			List<String> locations = new ArrayList<String>();
			locations.add("<div location =\"lblOverdue30\" />");
			locations.add("<div location =\"lblOverdue60\" />");
			locations.add("<div location =\"lblOverdue90\" />");
			locations.add("<div location =\"lblTotal\" />");
			template += "<table style=\"border:1px solid black; border-collapse:collapse;\" cellspacing=\"3\" cellpadding=\"0\" >";
			template += "<tr>";
			for (String string : locations) {
				template += OPEN_TH;
				template += string;
				template += CLOSE_TH;
			}
			template += CLOSE_TR;
			locations = new ArrayList<String>();
			locations.add("<div location =\"lblOverdue30Value\" />");
			locations.add("<div location =\"lblOverdue60Value\" />");
			locations.add("<div location =\"lblOverdue90Value\" />");
			locations.add("<div location =\"lblTotalValue\" />");
			template += "<tr>";
			for (String string : locations) {
				template += "<td style=\"border:1px solid black; border-collapse:collapse;\" >";
				template += string;
				template += CLOSE_TD;
			}
			template += CLOSE_TR;
			template += CLOSE_TABLE;
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblDebtLevel\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtDebtLevel\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("payment.pattern")), "lblPaymentPattern");
		cusLayout.addComponent(new Label(I18N.message("number.of.overdue")), "lblNumberOverdue");
		cusLayout.addComponent(new Label(I18N.message("overdue30")), "lblOverdue30");
		cusLayout.addComponent(new Label(I18N.message("overdue60")), "lblOverdue60");
		cusLayout.addComponent(new Label(I18N.message("overdue90")), "lblOverdue90");
		cusLayout.addComponent(new Label(I18N.message("total")), "lblTotal");
		cusLayout.addComponent(new Label(I18N.message("debt.level")), "lblDebtLevel");
		
		cusLayout.addComponent(txtPaymentPattern, "txtPaymentPattern");
		cusLayout.addComponent(lblOverdue30Value, "lblOverdue30Value");
		cusLayout.addComponent(lblOverdue60Value, "lblOverdue60Value");
		cusLayout.addComponent(lblOverdue90Value, "lblOverdue90Value");
		cusLayout.addComponent(lblTotalValue, "lblTotalValue");
		cusLayout.addComponent(txtDebtLevel, "txtDebtLevel");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * Collection contact info 
	 * @return
	 */
	private CustomLayout getColContactInfoLayout(){
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += "<td align=\"left\" width=\"140\" >";
		template += "<div location =\"lblOrigin\" class =\"inline-block\"></div>";
		template += "<div class=\"inline-block requiredfield\">&nbsp;*</div>";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"cbxOrigin\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblContactPerson\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"cbxContactPerson\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblContactNumber\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"cbxContactNumber\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtContactNumber\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblResult\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"cbxResult\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblRemark\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtRemark\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD + CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"btnAdd\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		cusLayout.addComponent(new Label(I18N.message("origin")), "lblOrigin");
		cusLayout.addComponent(new Label(I18N.message("contact.person")), "lblContactPerson");
		cusLayout.addComponent(new Label(I18N.message("contact.no")), "lblContactNumber");
		cusLayout.addComponent(new Label(I18N.message("result")), "lblResult");
		cusLayout.addComponent(new Label(I18N.message("remark")), "lblRemark");
		cusLayout.addComponent(cbxOrigin, "cbxOrigin");
		cusLayout.addComponent(cbxContactPerson, "cbxContactPerson");
		cusLayout.addComponent(cbxContactNumber, "cbxContactNumber");
		cusLayout.addComponent(txtContactNumber, "txtContactNumber");
		cusLayout.addComponent(cbxResult, "cbxResult");
		cusLayout.addComponent(txtRemark, "txtRemark");
		cusLayout.addComponent(btnAdd, "btnAdd");
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * Collection contact info address
	 * @return
	 */
	private VerticalLayout getColContactInfoAddressLayout(){
		Panel leftPanel = new Panel(getColContactInfoLayout());
		leftPanel.addStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout colAddressLayout = getColContactAddressLayout();
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(leftPanel);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(100, Unit.PIXELS));
		horLayout.addComponent(colAddressLayout);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addStyleName("overflow-layout-style");
		verLayout.addComponent(horLayout);
		return verLayout;
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private List<String> checkMandatorySelectedField(AbstractSelect field, String messageKey) {
		if (field.getValue() == null) {
			lstErrors.add(I18N.message("field.required.1", new String[] { I18N.message(messageKey) }));
		}
		return lstErrors;
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private List<String> checkMandatoryTextField(AbstractTextField field, String messageKey) {
		if (StringUtils.isEmpty(field.getValue())) {
			lstErrors.add(I18N.message("field.required.1", new String[] { I18N.message(messageKey) }));
		}
		return lstErrors;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validateSelectedControls() {
		lstErrors = checkMandatorySelectedField(cbxOrigin, I18N.message("origin"));
		return lstErrors.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validateAddressControls() {
		lstErrors = checkMandatoryTextField(addressPanel.getTxtBuilding(), I18N.message("number.building"));
		lstErrors = checkMandatoryTextField(addressPanel.getTxtStreet(), I18N.message("street"));
		lstErrors = checkMandatoryTextField(addressPanel.getTxtPostalCode(), I18N.message("postal.code"));
		lstErrors = checkMandatorySelectedField(addressPanel.getCbxProvince(), I18N.message("province"));
		lstErrors = checkMandatorySelectedField(addressPanel.getCbxDistrict(), I18N.message("district"));
		lstErrors = checkMandatorySelectedField(addressPanel.getCbxSubDistrict(), I18N.message("subdistrict"));
		return lstErrors.isEmpty();
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		messagePanel.setVisible(false);
		lstErrors.clear();
		txtPaidTerms.setValue("");
		txtRemainningTerms.setValue("");
		dfNextDueDate.setValue(null);
		txtPenaltyBalance.setValue("");
		txtFollowingFeeBalance.setValue("");
		txtCurrentTerms.setValue("");
		txtOverdueTerms.setValue("");
		txtOverdueDays.setValue("");
		txtOverdueAmount.setValue("");
		txtOverdueMonths.setValue("");
		txtPaymentPattern.setValue("");
		txtDebtLevel.setValue("");
	}
	
	/**
	 * confirm request extend
	 */
	private void confirmRequestExtend() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.extend"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Collection collection = contract.getCollection();
							CollectionConfig collectionConfig = getExtendInDay(collection);
							if (collectionConfig != null) {
								collection.setRequestExtendStatus(ERequestStatus.PENDING);
								collection.setExtendInDay(collectionConfig.getExtendInDay());
								ENTITY_SRV.update(collection);
								Notification.show("",I18N.message("request.extend.success"),Notification.Type.HUMANIZED_MESSAGE);
								btnRequestExtend.setVisible(false);
							} else {
								MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
										MessageBox.Icon.INFO, I18N.message("collection.config.is.null"),
										new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
								mb.setWidth("300px");
								mb.setHeight("150px");
								mb.show();
							}
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * Confirm Request Assist Request
	 */
	private void confirmRequestAssist() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.assist"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2831516023203612933L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Collection collection = contract.getCollection();
							// collection.setRequestAssistStatus(ERequestStatus.REQUEST);
							ENTITY_SRV.update(collection);
							Notification.show("",I18N.message("request.flag.success"),Notification.Type.HUMANIZED_MESSAGE);
							btnRequestAssist.setVisible(false);
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * Confirm Request Assist status Reject
	 */
	private void confirmRequestAssistStatus() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.assist"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2831516023203612933L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Collection collection = contract.getCollection();
							// collection.setRequestAssistStatus(ERequestStatus.REJECT);
							ENTITY_SRV.update(collection);
							Notification.show("",I18N.message("request.assist.success"),Notification.Type.HUMANIZED_MESSAGE);
							btnRequestAssistStatus.setVisible(false);
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * Confirm Request Flag
	 */
	private void confirmRequestFlag() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.flag"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							Collection collection = contract.getCollection();
							//collection.setRequestFlagStatus(ERequestStatus.REQUEST);
							ENTITY_SRV.update(collection);
							Notification.show("",I18N.message("request.flag.success"),Notification.Type.HUMANIZED_MESSAGE);
							btnRequestFlag.setVisible(false);
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	
	/**
	 * show message when contract don't have collection
	 */
	private void showMessage() {
		MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
				MessageBox.Icon.INFO, I18N.message("contract.no.collection"),
				new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.setWidth("300px");
		mb.setHeight("150px");
		mb.show();
	}
	
	private CollectionConfig getExtendInDay(Collection collection) {
		BaseRestrictions<CollectionConfig> restrictions	= new BaseRestrictions<>(CollectionConfig.class);
		restrictions.addCriterion(Restrictions.eq("colType", getTeam()));
		List<CollectionConfig> collectionConfigs = ENTITY_SRV.list(restrictions);
		if (!collectionConfigs.isEmpty()) {
			return collectionConfigs.get(0);
		}
		return null;
	}
	
	/**
	 * get login user profile 
	 * @return
	 */
	private EColType getTeam() {		
		SecUser secUser = UserSessionManager.getCurrentUser();
		if (ProfileUtil.isColPhone(secUser)) {
			return EColType.PHONE;
		} else if (ProfileUtil.isColField(secUser)) {
			return EColType.FIELD;
		} else if (ProfileUtil.isColInsideRepo(secUser)) {
			return EColType.INSIDE_REPO;
		} else if (ProfileUtil.isColOA()) {
			return EColType.OA;
		} 
		return null;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (contract.getCollection() != null) {
			if (event.getButton() == btnRequestAssist) {
				/*RequestAssistPopupPanel window = new RequestAssistPopupPanel(I18N.message("request.assist"));
				UI.getCurrent().addWindow(window);
				window.assingValuses(contract);*/
				confirmRequestAssist();
			} else if (event.getButton() == btnRequestFlag) {
				confirmRequestFlag();
			/*} else if (event.getButton() == btnRequestTeam) {
				RequestTeamPopupPanel window = new RequestTeamPopupPanel(I18N.message("request.team"));
				UI.getCurrent().addWindow(window);
				window.assingValuses(contract);*/
			} else if (event.getButton() == btnRequestExtend) {
				confirmRequestExtend();
			} else if (event.getButton() == btnRequestAssistStatus) {
				confirmRequestAssistStatus();
			}
		} else {
			this.showMessage();
		}
	}
}
