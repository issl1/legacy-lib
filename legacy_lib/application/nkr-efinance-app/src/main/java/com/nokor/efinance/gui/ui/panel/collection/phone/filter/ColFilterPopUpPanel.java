package com.nokor.efinance.gui.ui.panel.collection.phone.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.UsersSelectPanel;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationSubTypes;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColFilterPopUpPanel extends Window implements ClickListener, CloseListener, MCollection, FinServicesHelper {

	private static final long serialVersionUID = 4360916217881189754L;
	
	private TextField txtContractId;
	
	public ComboBox cbxDueDayFrom;
	public ComboBox cbxDueDayTo;
	
	private AutoDateField dfAssignDateFrom;
	private AutoDateField dfAssignDateTo;
	
	public ComboBox cbxGuarantor;
	private ComboBox cbxStage;
	private ComboBox cbxStatus;
	private ComboBox cbxAmountCollect;
	
	private EntityComboBox<Organization> cbxOA;
	private ComboBox cbxClaims;
	private ERefDataComboBox<EWkfStatus> cbxLockSplit;
	
	private NativeButton btnSave;
	private NativeButton btnReset;
	private NativeButton btnCancel;
	
	private Listener listener = null;
	private List<String> errors;
	private VerticalLayout messagePanel;
	
	private ComboBox cbxStaff;
	private NativeButton btnSelect;
	private List<SecUser> selectUsers;
	private Label selectUsersLabel;
	
	private StaffComboBoxItem allItem;
	private StaffComboBoxItem selectItem;
	private SecUser secUser;
	
	private EntityComboBox<Area> cbxArea;
	private EntityComboBox<OrgStructure> cbxOriginalBrach;
	
	private NumberField txtODM;

	
	/**
	 * 
	 * @author buntha.chea
	 * interface
	 */
	public interface Listener extends Serializable {
        void onClose(ColFilterPopUpPanel dialog, StoreControl storeControl);
    }
	
	public ColFilterPopUpPanel() {
		init();
	}

	/**
	 * Init
	 */
	private void init() {
		secUser = UserSessionManager.getCurrentUser();
	
		setCaption(I18N.message("filters"));
		setModal(true);
		setWidth("650px");
		
		txtContractId = ComponentFactory.getTextField(150, 150);
		
		cbxDueDayFrom = getComboBoxDueDate();
		cbxDueDayTo = getComboBoxDueDate();

		dfAssignDateFrom = ComponentFactory.getAutoDateField();
		dfAssignDateTo = ComponentFactory.getAutoDateField();
		
		cbxGuarantor = new ComboBox();
		cbxGuarantor.setWidth("50px");
		cbxGuarantor.addItem("0");
		cbxGuarantor.addItem("1+");
		
		cbxStage = new ComboBox();
		cbxStage.addItem(FIRST_INSTALLMENT);
		cbxStage.addItem(SECOND_INSTALLMENT);
		cbxStage.addItem(THIRD_INSTALLMENT);
		
		cbxStatus = new ComboBox();
		cbxStatus.addItem(FOLLOW_UP);
		cbxStatus.addItem(PROCESSED);
		
		cbxAmountCollect = new ComboBox();
		cbxAmountCollect.addItem("0");
		cbxAmountCollect.addItem("1");
		
		cbxClaims = new ComboBox();
		cbxClaims.addItem(LOST);
		cbxClaims.addItem(DAMAGED);
		
		cbxLockSplit = new ERefDataComboBox<>(EWkfStatus.class);
		cbxLockSplit.assignValueMap(LockSplitWkfStatus.listLockSplitStatus());
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnReset = new NativeButton(I18N.message("reset"), this);
		btnReset.setIcon(FontAwesome.ERASER);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		cbxOriginalBrach = new EntityComboBox<>(OrgStructure.class, null, "nameEn", "");
		cbxOriginalBrach.setImmediate(true);
		cbxOriginalBrach.renderer();
		cbxOriginalBrach.setWidth(140, Unit.PIXELS);
		
		Button.ClickListener cb = new Button.ClickListener() {
			
			private static final long serialVersionUID = 3525060915814334881L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (errors.isEmpty()) {
					if (listener != null) {
						StoreControl storeControl = new StoreControl();
						listener.onClose(ColFilterPopUpPanel.this, storeControl);
					}
					UI.getCurrent().removeWindow(ColFilterPopUpPanel.this);
				}
			}
		};
			
	    btnSave.addClickListener(cb);
	    
	    messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		cbxStaff = new ComboBox();
		allItem = new StaffComboBoxItem("ALL", I18N.message("all"));
		selectItem = new StaffComboBoxItem("SEL", I18N.message("select"));
		
		cbxStaff.addItem(allItem);
		cbxStaff.addItem(selectItem);
		cbxStaff.setNullSelectionAllowed(false);
		cbxStaff.setValue(allItem);
		cbxStaff.addValueChangeListener(new ValueChangeListener() {			
			/**
			 */
			private static final long serialVersionUID = 7719144447997706712L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				selectUsersLabel = new Label();
				btnSelect.setVisible(false);
				if (cbxStaff.getValue() != null) {
					StaffComboBoxItem staffComboBoxItem = (StaffComboBoxItem) cbxStaff.getValue();
					if ("SEL".equals(staffComboBoxItem.getCode())) {
						btnSelect.setVisible(true);
					} else {
						selectUsers = null;
						selectUsersLabel.setCaption("");
					}					
				}
			}
		});
		btnSelect = new NativeButton(I18N.message("select"));
		btnSelect.setVisible(false);
		btnSelect.addStyleName("btn btn-success button-small");
		btnSelect.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -911158149367454852L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (ProfileUtil.isColPhoneLeader(secUser) || ProfileUtil.isColPhoneSupervisor(secUser)) {
					showUsersSelectPanel(IProfileCode.COL_PHO_STAFF);
				} else if (ProfileUtil.isColFieldLeader() || ProfileUtil.isColFieldSupervisor(secUser)) {
					showUsersSelectPanel(IProfileCode.COL_FIE_STAFF);
				} else if (ProfileUtil.isColInsideRepoLeader() || ProfileUtil.isColInsideRepoSupervisor()) {
					showUsersSelectPanel(IProfileCode.COL_INS_STAFF);
				} else if (ProfileUtil.isCallCenterLeader()) {
					showUsersSelectPanel(IProfileCode.CAL_CEN_STAFF);
				}
				
			}
		});
		
		cbxArea = new EntityComboBox<>(Area.class, "line2");
		cbxArea.setImmediate(true);
		if (ProfileUtil.isColField() || ProfileUtil.isColInsideRepo()) {
			cbxArea.renderer();
		}
		
		cbxOA = new EntityComboBox<>(Organization.class, Organization.NAMELOCALE);
		cbxOA.setWidth(160, Unit.PIXELS);
		
		if (ProfileUtil.isColOASupervisor()) {
			OrganizationRestriction restrictions = new OrganizationRestriction();
			List<ESubTypeOrganization> subTypeOrganizations = new ArrayList<ESubTypeOrganization>();
			subTypeOrganizations.add(OrganizationSubTypes.OUTSOURCE_AGENT);
			restrictions.setSubTypeOrganizations(subTypeOrganizations);
			cbxOA.renderer(restrictions);
		}
		
		txtODM = ComponentFactory.getNumberField(10, 50);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnReset);
		navigationPanel.addButton(btnCancel);
		
		String template = "collectionFilter";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/collection/phone/" + template + ".html");
		CustomLayout collectionFilterLayout = null;
		try {
			collectionFilterLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("contract.id"), "lblContractId");
		collectionFilterLayout.addComponent(txtContractId, "txtContractId");
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("due.day"), "lblDueDate");
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("from"), "lblFrom");
		collectionFilterLayout.addComponent(cbxDueDayFrom, "cbxDueDateFrom");
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("to"), "lblTo");
		collectionFilterLayout.addComponent(cbxDueDayTo, "cbxDueDateTo");
		
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("assign.date"), "lblAssignDate");
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("from"), "lblAssignDateFrom");
		collectionFilterLayout.addComponent(dfAssignDateFrom, "dfAssignDateFrom");
		collectionFilterLayout.addComponent(ComponentFactory.getLabel("to"), "lblAssignDateTo");
		collectionFilterLayout.addComponent(dfAssignDateTo, "dfAssignDateTo");
		
		collectionFilterLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
		collectionFilterLayout.addComponent(cbxGuarantor, "cbxGuarantor");
		collectionFilterLayout.addComponent(new Label(I18N.message("stage")), "lblStage");
		collectionFilterLayout.addComponent(cbxStage, "cbxStage");
		collectionFilterLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		collectionFilterLayout.addComponent(cbxStatus, "cbxStatus");
		
		collectionFilterLayout.addComponent(new Label(I18N.message("claims")), "lblClaims");
		collectionFilterLayout.addComponent(cbxClaims, "cbxClaims");
		collectionFilterLayout.addComponent(new Label(I18N.message("lock.split")), "lblLockSplit");
		collectionFilterLayout.addComponent(cbxLockSplit, "cbxLockSplit");
		collectionFilterLayout.addComponent(new Label(I18N.message("amount.collect")), "lblAmountCollect");
		collectionFilterLayout.addComponent(cbxAmountCollect, "cbxAmountCollect");
		
		if (ProfileUtil.isColField() || ProfileUtil.isColInsideRepo()) {
			collectionFilterLayout.addComponent(new Label(I18N.message("area")), "lblArea");
			collectionFilterLayout.addComponent(cbxArea, "cbxArea");
		}
		
		if (ProfileUtil.isColPhoneLeader(secUser) 
				|| ProfileUtil.isColPhoneSupervisor(secUser) 
				|| ProfileUtil.isColFieldLeader(secUser) 
				|| ProfileUtil.isColFieldSupervisor(secUser)
				|| ProfileUtil.isColInsideRepoLeader()
				|| ProfileUtil.isColInsideRepoSupervisor()
				|| ProfileUtil.isCallCenterLeader()) {
			
			collectionFilterLayout.addComponent(new Label(I18N.message("staff")), "lblStaff");
			collectionFilterLayout.addComponent(cbxStaff, "cbxStaff");
			collectionFilterLayout.addComponent(btnSelect, "btnSelect");
		}
		
		if (ProfileUtil.isColOASupervisor()) {
			collectionFilterLayout.addComponent(ComponentFactory.getLabel("oa"), "lblOA");
			collectionFilterLayout.addComponent(cbxOA, "cbxOA");
		}
		
		if (ProfileUtil.isColPhone()) {
			collectionFilterLayout.addComponent(ComponentFactory.getLabel("origination.brach"), "lblOriginationBrach");
			collectionFilterLayout.addComponent(cbxOriginalBrach, "cbxOriginalBrach");
		}
		
		collectionFilterLayout.addComponent(new Label(I18N.message("odm")), "lblODM");
		collectionFilterLayout.addComponent(txtODM, "txtODM");
		
		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.addComponent(navigationPanel);
		filterLayout.addComponent(messagePanel);
		filterLayout.addComponent(collectionFilterLayout);
		
		setContent(filterLayout);
	}
	/**
	 * Show Users select panel
	 */
	private void showUsersSelectPanel(String profileCode) {
		UsersSelectPanel usersSelectPanel = new UsersSelectPanel();
		usersSelectPanel.show(new String[] {profileCode}, new UsersSelectPanel.Listener() {				
			private static final long serialVersionUID = 7757863260440994161L;
			@Override
			public void onClose(UsersSelectPanel dialog) {
				selectUsers = dialog.getSelectedIds();
				String selectUsersDesc = "";
				if (selectUsers != null && !selectUsers.isEmpty()) {
					for (SecUser user : selectUsers) {
						if (!selectUsersDesc.isEmpty()) {
							selectUsersDesc += " - ";
						}
						selectUsersDesc += user.getLogin();
					}									
				}
				selectUsersLabel.setCaption(selectUsersDesc);
			}
		});
	}
	
	
	/**
	 * 
	 * @param listener
	 * @return
	 */
	public static ColFilterPopUpPanel show(StoreControl storeControl,final Listener listener) {   	
		ColFilterPopUpPanel colFilterPopUpPanel = new ColFilterPopUpPanel();
	    colFilterPopUpPanel.listener = listener;
	    colFilterPopUpPanel.assignValue(storeControl);
	    return colFilterPopUpPanel;
	}
	
	/**
	 * Get Combobox Duedate with data from 1 to 20
	 * @param caption
	 * @return
	 */
	private ComboBox getComboBoxDueDate() {
		ComboBox comboBox = new ComboBox();
		comboBox.setWidth("50px");
		for (int items = 1; items <= 20; items++) {
			comboBox.addItem(String.valueOf(items));
		}
		return comboBox;
	}
	
	/**
	 * Reset All Control in Popup Panel
	 */
	public void reset() {
		txtContractId.setValue(StringUtils.EMPTY);
		cbxDueDayFrom.setValue(null);
		cbxDueDayTo.setValue(null);
		cbxGuarantor.setValue(null);
		cbxStage.setValue(null);
		cbxStatus.setValue(null);
		cbxAmountCollect.setValue(null);
		
		dfAssignDateFrom.setValue(null);
		dfAssignDateTo.setValue(null);
		
		cbxClaims.setValue(null);
		cbxLockSplit.setSelectedEntity(null);
		cbxStaff.setValue(allItem);
		cbxArea.setSelectedEntity(null);
		cbxOA.setSelectedEntity(null);
		
		cbxOriginalBrach.setSelectedEntity(null);
		txtODM.setValue("");
		
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}
	
	/**
	 * Assign Value to all control in filter popup
	 * @param storeControl
	 */
	public void assignValue(StoreControl storeControl) {
		reset();
		if (storeControl != null) {
			txtContractId.setValue(storeControl.getContractId());
			cbxDueDayFrom.setValue(storeControl.getDueDateFrom());
			cbxDueDayTo.setValue(storeControl.getDueDateTo());
			dfAssignDateFrom.setValue(storeControl.getAssignDateFrom());
			dfAssignDateTo.setValue(storeControl.getAssignDateTo());
			cbxGuarantor.setValue(storeControl.getGuarantor());
			cbxStage.setValue(storeControl.getStage());
			cbxStatus.setValue(storeControl.getStatus());
			cbxClaims.setValue(storeControl.getClaims());
			cbxAmountCollect.setValue(storeControl.getAmountCollecte());
			cbxOA.setSelectedEntity(storeControl.getOutsourceAgent());
			
			cbxLockSplit.setSelectedEntity(storeControl.getLockSplitStatus());
			cbxStaff.setValue(storeControl.getLblStaffSelected() != null ? selectItem : allItem);
			cbxArea.setSelectedEntity(storeControl.getArea());
			cbxOriginalBrach.setSelectedEntity(storeControl.getBrand());
			txtODM.setValue(storeControl.getOdm() != null ? String.valueOf(storeControl.getOdm()) : "");
		}
	}
	
	/**
	 * Validate Before Filter
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;
		
		if (cbxDueDayFrom.getValue() == null && cbxDueDayTo.getValue() != null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("from") }));
		} else if (cbxDueDayFrom.getValue() != null && cbxDueDayTo.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("to") }));
		} else if (cbxDueDayFrom.getValue() != null && cbxDueDayTo != null) {
			if (converterObj(cbxDueDayFrom) > converterObj(cbxDueDayTo)) {
				errors.add(I18N.message("field.due.date.from.should.less.than.field.due.date.to"));
			}
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
	 * Button click listener
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				new StoreControl();
			}
		} else if (event.getButton() == btnReset) {
			reset();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}
	
	/**
	 * convert obj from cbx duedate form and to
	 * @param value
	 * @return
	 */
	private Integer converterObj(Object value) {
		if (value != null) {
			return Integer.valueOf(String.valueOf(value));
		}
		return null;
	}

	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
	/**
	 * Store All control in Filter Popup Panel
	 * @author buntha.chea
	 *
	 */
	public class StoreControl {
		
		private String contractId;
		private String dueDateFrom;
		private String dueDateTo;
		private Date assignDateFrom;
		private Date assignDateTo;
		private String guarantor;
		private String stage;
		private String status;
		private Organization outsourceAgent;
		private String claims;
		private EWkfStatus lockSplitStatus;
		private String amountCollecte;
		private Label lblStaffSelected;
		private List<SecUser> secUsers;
		private Area area;
		private OrgStructure brand;
		private Integer odm;
		
		public StoreControl() {
			this.contractId = txtContractId.getValue();
			this.dueDateFrom = cbxDueDayFrom.getValue() != null ? String.valueOf(cbxDueDayFrom.getValue()) : "";
			this.dueDateTo = cbxDueDayTo.getValue() != null ? String.valueOf(cbxDueDayTo.getValue()) : "";
			this.assignDateFrom = dfAssignDateFrom.getValue();
			this.assignDateTo = dfAssignDateTo.getValue();
			this.guarantor = cbxGuarantor.getValue() != null ? String.valueOf(cbxGuarantor.getValue()) : "";
			this.stage = cbxStage.getValue() != null ? String.valueOf(cbxStage.getValue()) : "";
			this.status = cbxStatus.getValue() != null ? String.valueOf(cbxStatus.getValue()) : "";
			this.outsourceAgent = cbxOA.getSelectedEntity();
			this.claims = cbxClaims.getValue() != null ? String.valueOf(cbxClaims.getValue()) : "";
			this.lockSplitStatus = cbxLockSplit.getSelectedEntity();
			this.setAmountCollecte(cbxAmountCollect.getValue() != null ? String.valueOf(cbxAmountCollect.getValue()) : "");
			this.setLblStaffSelected(selectUsersLabel);
			this.setSecUsers(selectUsers);
			this.setArea(cbxArea.getSelectedEntity());
			this.setBrand(cbxOriginalBrach.getSelectedEntity());
			this.odm = StringUtils.isEmpty(txtODM.getValue())  ? null : Integer.valueOf(txtODM.getValue());
		}

		/**
		 * @return the contractId
		 */
		public String getContractId() {
			return contractId;
		}

		/**
		 * @return the dueDateFrom
		 */
		public String getDueDateFrom() {
			return dueDateFrom;
		}

		/**
		 * @return the dueDateTo
		 */
		public String getDueDateTo() {
			return dueDateTo;
		}

		/**
		 * @return the assignDateFrom
		 */
		public Date getAssignDateFrom() {
			return assignDateFrom;
		}

		/**
		 * @return the assignDateTo
		 */
		public Date getAssignDateTo() {
			return assignDateTo;
		}

		/**
		 * @return the guarantor
		 */
		public String getGuarantor() {
			return guarantor;
		}

		/**
		 * @return the outsourceAgent
		 */
		public Organization getOutsourceAgent() {
			return outsourceAgent;
		}

		/**
		 * @param outsourceAgent the outsourceAgent to set
		 */
		public void setOutsourceAgent(Organization outsourceAgent) {
			this.outsourceAgent = outsourceAgent;
		}

		/**
		 * @param contractId the contractId to set
		 */
		public void setContractId(String contractId) {
			this.contractId = contractId;
		}

		/**
		 * @param assignDateFrom the assignDateFrom to set
		 */
		public void setAssignDateFrom(Date assignDateFrom) {
			this.assignDateFrom = assignDateFrom;
		}

		/**
		 * @param assignDateTo the assignDateTo to set
		 */
		public void setAssignDateTo(Date assignDateTo) {
			this.assignDateTo = assignDateTo;
		}

		/**
		 * @return the claims
		 */
		public String getClaims() {
			return claims;
		}

		/**
		 * @return the lockSplitStatus
		 */
		public EWkfStatus getLockSplitStatus() {
			return lockSplitStatus;
		}

		/**
		 * @param dueDateFrom the dueDateFrom to set
		 */
		public void setDueDateFrom(String dueDateFrom) {
			this.dueDateFrom = dueDateFrom;
		}

		/**
		 * @param dueDateTo the dueDateTo to set
		 */
		public void setDueDateTo(String dueDateTo) {
			this.dueDateTo = dueDateTo;
		}

		/**
		 * @param guarantor the guarantor to set
		 */
		public void setGuarantor(String guarantor) {
			this.guarantor = guarantor;
		}

		/**
		 * @param claims the claims to set
		 */
		public void setClaims(String claims) {
			this.claims = claims;
		}

		/**
		 * @param lockSplitStatus the lockSplitStatus to set
		 */
		public void setLockSplitStatus(EWkfStatus lockSplitStatus) {
			this.lockSplitStatus = lockSplitStatus;
		}

		/**
		 * @return the stage
		 */
		public String getStage() {
			return stage;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param stage the stage to set
		 */
		public void setStage(String stage) {
			this.stage = stage;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the amountCollecte
		 */
		public String getAmountCollecte() {
			return amountCollecte;
		}

		/**
		 * @param amountCollecte the amountCollecte to set
		 */
		public void setAmountCollecte(String amountCollecte) {
			this.amountCollecte = amountCollecte;
		}

		/**
		 * @return the lblStaffSelected
		 */
		public Label getLblStaffSelected() {
			return lblStaffSelected;
		}

		/**
		 * @param lblStaffSelected the lblStaffSelected to set
		 */
		public void setLblStaffSelected(Label lblStaffSelected) {
			this.lblStaffSelected = lblStaffSelected;
		}

		/**
		 * @return the secUsers
		 */
		public List<SecUser> getSecUsers() {
			return secUsers;
		}

		/**
		 * @param secUsers the secUsers to set
		 */
		public void setSecUsers(List<SecUser> secUsers) {
			this.secUsers = secUsers;
		}

		/**
		 * @return the area
		 */
		public Area getArea() {
			return area;
		}

		/**
		 * @param area the area to set
		 */
		public void setArea(Area area) {
			this.area = area;
		}

		/**
		 * @return the brand
		 */
		public OrgStructure getBrand() {
			return brand;
		}

		/**
		 * @param brand the brand to set
		 */
		public void setBrand(OrgStructure brand) {
			this.brand = brand;
		}

		/**
		 * @return the odm
		 */
		public Integer getOdm() {
			return odm;
		}

		/**
		 * @param odm the odm to set
		 */
		public void setOdm(Integer odm) {
			this.odm = odm;
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class StaffComboBoxItem implements Serializable {
		/** 
		 */
		private static final long serialVersionUID = 4487943333257237511L;
		private String code;
		private String label;
		public StaffComboBoxItem(String code, String label) {
			this.code = code;
			this.label = label;
		}		
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		@Override
		public String toString() {
			return label;
		}
	}
	
}
