package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.ContractDetail;
import com.nokor.efinance.core.collection.service.UserContractDetail;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractUserSimulInboxRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.ConfirmAssignmentPopupPanel.ConfirmListener;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Phone Unassigned ODM Panel
 * @author bunlong.taing
 */
public class PhoneUnassignedOdmPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {
	/** */
	private static final long serialVersionUID = 1415858305352188280L;
	
	private static final String CURRENT_CONTRACT = "current.contract";
	private static final String NEW_CONTRACT = "new.contract";
	private static final String ADJUSTED_CONTRACT = "adjusted.contract";
	private static final String GUARANTEED = "guaranteed";
	private static final String NON_GUARANTEED = "non.guaranteed";
	private static final String ASSISTED = "assisted";
	private static final String FLAGGED = "flagged";
	private static final String DD1_5 = "dd1-5";
	private static final String DD6_10 = "dd6-10";
	private static final String DD11_15 = "dd11-15";
	private static final String DD16_20 = "dd16-20";
	private static final String TOTAL_CONTRACT = "total.contract";
	private static final String AUTO = "Auto";
	private static final String MANUAL = "Manual";
	
	private SimpleTable<SecUser> staffTable;
	
	private Label lblNumberOfContracts;
	private Label lblNumberOfStaffs;
	private Label lblAveragePerStaff;
	private Label lblAveragePerArea;
	
	private NativeButton btnDisableResources;
	private NativeButton btnAssign;
	private NativeButton btnReset;
	private NativeButton btnConfirm;
	private TextField txtNbContract;
	private ComboBox cbxMove;
	
	private ConfirmAssignmentPopupPanel confirmAssignmentPopup;
	
	private SecUser selectedStaff;
	private Integer debtLevel;
	private EColType colType;
	private List<ContractUserSimulInbox> contractUserSimulInboxs;
	private Map<SecUser, UserContractDetail> contractsByStaff;
	
	private PhoneUnassignedOdmFilterPanel filterPanel;
	
	private List<Long> areaIds;
	private Integer nbContractArea;
	
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaIds(List<Long> areaIds) {
		this.areaIds = areaIds;
	}

	/**
	 * 
	 * @param debtLevel
	 */
	public PhoneUnassignedOdmPanel(Integer debtLevel, EColType colType) {
		this.debtLevel = debtLevel;
		this.colType = colType;
		addComponent(createForm());
	}
	
	/**
	 * Create form
	 * @return
	 */
	private VerticalLayout createForm() {
		confirmAssignmentPopup = new ConfirmAssignmentPopupPanel();
		
		VerticalLayout content = new VerticalLayout() ;
		content.setMargin(true);
		content.setSpacing(true);
		content.addComponent(createTablePanel());
		content.addComponent(createDetailLayout());
		
		return content;
	}
	
	/**
	 * Create table Panel
	 * @return
	 */
	private VerticalLayout createTablePanel() {
		staffTable = new SimpleTable<>(createStaffTableColumnDefinition());
		staffTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = -6410163213475834830L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedStaff = (SecUser) event.getItemId();
			}
		});
		
		btnDisableResources = new NativeButton(I18N.message("disable.resources"));
		btnDisableResources.addStyleName("btn btn-success");
		btnDisableResources.addClickListener(this);
		
		txtNbContract = ComponentFactory.getTextField(100, 100);
		cbxMove = new ComboBox();
		cbxMove.addItem(AUTO);
		cbxMove.addItem(MANUAL);
		
		btnAssign = new NativeButton(I18N.message("assign"));
		btnAssign.addStyleName("btn btn-success");
		btnAssign.addClickListener(this);
		
		Label lblRandomSelect = ComponentLayoutFactory.getLabelCaption("random.select");
		
		HorizontalLayout randomSelectLayout = new HorizontalLayout();
		randomSelectLayout.setSpacing(true);
		randomSelectLayout.addComponent(lblRandomSelect);
		randomSelectLayout.addComponent(txtNbContract);
		randomSelectLayout.addComponent(cbxMove);
		randomSelectLayout.addComponent(btnAssign);
		
		randomSelectLayout.setComponentAlignment(lblRandomSelect, Alignment.MIDDLE_LEFT);
		randomSelectLayout.setComponentAlignment(txtNbContract, Alignment.MIDDLE_LEFT);
		randomSelectLayout.setComponentAlignment(cbxMove, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.addComponent(btnDisableResources);
		horizontalLayout.addComponent(randomSelectLayout);
		horizontalLayout.setComponentAlignment(btnDisableResources, Alignment.BOTTOM_LEFT);
		horizontalLayout.setComponentAlignment(randomSelectLayout, Alignment.BOTTOM_RIGHT);
		
		filterPanel = new PhoneUnassignedOdmFilterPanel(this);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(filterPanel);
		content.addComponent(staffTable);
		content.addComponent(horizontalLayout);
		
		return content;
	}
	
	/**
	 * Create Detail Layout
	 * @return
	 */
	private Panel createDetailLayout() {
		btnReset = new NativeButton(I18N.message("reset"));
		btnReset.addStyleName("btn btn-success");
		btnReset.addClickListener(this);
		btnConfirm = new NativeButton(I18N.message("confirm.assignment"));
		btnConfirm.addStyleName("btn btn-success");
		btnConfirm.addClickListener(this);
		
		lblNumberOfContracts = ComponentFactory.getLabel();
		lblNumberOfStaffs = ComponentFactory.getLabel();
		lblAveragePerStaff = ComponentFactory.getLabel();
		lblAveragePerArea = ComponentFactory.getLabel();
		
		String table = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\">"
				+ "<th><b>" + I18N.message("number.of.contracts") + "</b></th>"
				+ "<th><b>" + I18N.message("number.of.staffs") + "</b></th>"
				+ "<th><b>" + I18N.message("average.per.staff") + "</b></th>";
		if (ProfileUtil.isColFieldSupervisor() || ProfileUtil.isColInsideRepoSupervisor()) {		
			table += "<th><b>" + I18N.message("average.per.area") + "</b></th>";
		}
		table += "</tr>";
		
		table += "<tr>"
				+ "<td location=\"lblNumberOfContracts\"></td>"
				+ "<td location=\"lblNumberOfStaffs\"></td>"
				+ "<td location=\"lblAveragePerStaff\"></td>";
		if (ProfileUtil.isColFieldSupervisor() || ProfileUtil.isColInsideRepoSupervisor()) {	
			table += "<td location=\"lblAveragePerArea\"></td>";
		}
		table += "</tr></table>";
		
		CustomLayout tableLayout = null;
		try {
			tableLayout = new CustomLayout(new ByteArrayInputStream(table.getBytes()));
		} catch (IOException e) {
			Notification.show("Could not create custom layout", e.getMessage(), Type.ERROR_MESSAGE);
		}
		tableLayout.addComponent(lblNumberOfContracts, "lblNumberOfContracts");
		tableLayout.addComponent(lblNumberOfStaffs, "lblNumberOfStaffs");
		tableLayout.addComponent(lblAveragePerStaff, "lblAveragePerStaff");
		if (ProfileUtil.isColFieldSupervisor() || ProfileUtil.isColInsideRepoSupervisor()) {
			tableLayout.addComponent(lblAveragePerArea, "lblAveragePerArea");
		}
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnReset);
		buttonLayout.addComponent(btnConfirm);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(tableLayout);
		content.addComponent(buttonLayout);
		content.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		return new Panel(content);
	}
	
	/**
	 */
	public void assignValues() {
		setStaffTableContainerDataSource(true);
		calculateDetailData();
	}
	
	/**
	 * Calculate Detail Data
	 */
	public void calculateDetailData() {
		int nbOfStaffs = contractsByStaff.size();
		int nbArea = getNbAreas(contractUserSimulInboxs);
		int nbOfContracts = 0;
		
		
		for (SecUser user : contractsByStaff.keySet()) {
			nbOfContracts += contractsByStaff.get(user).getNbNewContract();
		}
		nbContractArea = nbArea != 0 ? nbOfContracts / nbArea : 0;
		
		lblNumberOfContracts.setValue(String.valueOf(nbOfContracts));
		lblNumberOfStaffs.setValue(String.valueOf(nbOfStaffs));
		lblAveragePerStaff.setValue(String.valueOf(nbOfStaffs != 0 ? nbOfContracts / nbOfStaffs : 0));
		if (ProfileUtil.isColFieldSupervisor() || ProfileUtil.isColInsideRepoSupervisor()) {
			lblAveragePerArea.setValue(String.valueOf(nbArea != 0 ? nbOfContracts / nbArea : 0));
		}
	}
	
	/**
	 * Create staff table column definition
	 * @return
	 */
	private List<ColumnDefinition> createStaffTableColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SecUser.LOGIN, I18N.message("staff"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(CURRENT_CONTRACT, I18N.message("current"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(NEW_CONTRACT, I18N.message("new"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(ADJUSTED_CONTRACT, I18N.message("adjusted"), Long.class, Align.LEFT, 50));
		
		columnDefinitions.add(new ColumnDefinition(GUARANTEED, I18N.message("guaranteed"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(NON_GUARANTEED, I18N.message("non.guaranteed"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ASSISTED, I18N.message("assisted"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(FLAGGED, I18N.message("flagged"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(DD1_5, I18N.message("dd1-5"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(DD6_10, I18N.message("dd6-10"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(DD11_15, I18N.message("dd11-15"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(DD16_20, I18N.message("dd16-20"), Long.class, Align.LEFT, 50));
		
		columnDefinitions.add(new ColumnDefinition(TOTAL_CONTRACT, I18N.message("total"), Long.class, Align.LEFT, 60));
		return columnDefinitions;
	}
	
	/**
	 * Set Staff Table Container Data Source
	 */
	@SuppressWarnings("unchecked")
	public void setStaffTableContainerDataSource(boolean reset) {
		if (reset) {
			ContractUserSimulInboxRestriction restrictions = new ContractUserSimulInboxRestriction();
			restrictions.setDebtLevels(new Integer[] {debtLevel});
			restrictions.setUserNotNull(true);
			restrictions.setColType(colType);
			
			restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
			restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);
			if (filterPanel.getCbxDueDateFrom().getValue() != null && filterPanel.getCbxDueDateTo().getValue() != null) {
				int from = Integer.valueOf(filterPanel.getCbxDueDateFrom().getValue().toString());
				int to =  Integer.valueOf(filterPanel.getCbxDueDateTo().getValue().toString());
				
				restrictions.addCriterion(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, from), 
						Restrictions.le("col." + Collection.DUEDAY, to)));
			}
			
			if (filterPanel.getCbFirstInstallment().getValue().equals(Boolean.TRUE)) {
				DetachedCriteria cfwCriteria = DetachedCriteria.forClass(Cashflow.class, "cfw");
				cfwCriteria.add(Restrictions.between("cfw." + Cashflow.NUMINSTALLMENT, 1, 3));
				cfwCriteria.add(Restrictions.eq("cfw." + Cashflow.PAID, false));
				cfwCriteria.add(Restrictions.in("cfw." + Cashflow.CASHFLOWTYPE, new ECashflowType[] { ECashflowType.IAP, ECashflowType.CAP }));
				cfwCriteria.setProjection(Projections.projectionList().add(Projections.property("cfw." + Cashflow.CONTRACT + ".id")));
				restrictions.addCriterion(Property.forName("con." + Contract.ID).in(cfwCriteria));
			}
			
			if (filterPanel.getCbxContractStatus().getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq("con." + MContract.WKFSTATUS, filterPanel.getCbxContractStatus().getSelectedEntity()));
			}
			
			if (filterPanel.getCbxOriginalBrach().getSelectedEntity() != null) {
				restrictions.addCriterion(Restrictions.eq("con.originBranch", filterPanel.getCbxOriginalBrach().getSelectedEntity()));
			}
			
			if (areaIds != null && !areaIds.isEmpty()) {
				restrictions.addCriterion(Restrictions.in("col." + Collection.AREA + "." + Area.ID, areaIds));
			}
			
			contractUserSimulInboxs = COL_SRV.list(restrictions);
		}
		contractsByStaff = groupContractsByUser(contractUserSimulInboxs);
		
		selectedStaff = null;
		staffTable.removeAllItems();
		for (SecUser user : contractsByStaff.keySet()) {
			SecUserDetail secUserDetail = ENTITY_SRV.getByField(SecUserDetail.class, "secUser", user);
			if (secUserDetail != null) {
				if (secUserDetail.isEnableAssignContracts()) {
					UserContractDetail userContractDetail = contractsByStaff.get(user);
					Item item = staffTable.addItem(user);
					item.getItemProperty(SecUser.LOGIN).setValue(user != null ? user.getLogin() : "");
					item.getItemProperty(CURRENT_CONTRACT).setValue(userContractDetail.getNbCurrentContracts());
					item.getItemProperty(NEW_CONTRACT).setValue(userContractDetail.getNbNewContract());
					item.getItemProperty(ADJUSTED_CONTRACT).setValue(userContractDetail.getNbAdjustedContracts());
					item.getItemProperty(GUARANTEED).setValue(userContractDetail.getNbGuaranteedContracts());
					item.getItemProperty(NON_GUARANTEED).setValue(userContractDetail.getNbNonGuaranteedContracts());
					item.getItemProperty(ASSISTED).setValue(userContractDetail.getNbAssistedContracts());
					item.getItemProperty(FLAGGED).setValue(userContractDetail.getNbFlaggedContracts());
					item.getItemProperty(DD1_5).setValue(userContractDetail.getNbDD1to5());
					item.getItemProperty(DD6_10).setValue(userContractDetail.getNbDD6to10());
					item.getItemProperty(DD11_15).setValue(userContractDetail.getNbDD11to15());
					item.getItemProperty(DD16_20).setValue(userContractDetail.getNbDD16to20());
					
					item.getItemProperty(TOTAL_CONTRACT).setValue(userContractDetail.getTotalConracts());
				}
			}
		}
	}
	
	/**
	 * @param contractUserSimulInboxs
	 * @return
	 */
	private Map<SecUser, UserContractDetail> groupContractsByUser(List<ContractUserSimulInbox> contractUserSimulInboxs) {
		Map<SecUser, UserContractDetail> contractsByStaff = new HashMap<>();
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			Contract contract = contractUserSimulInbox.getContract();
			Collection collection = contract.getCollection();
			UserContractDetail userContractDetail = contractsByStaff.get(contractUserSimulInbox.getSecUser());
			if (userContractDetail == null) {
				SecUser user = contractUserSimulInbox.getSecUser();
				userContractDetail = new UserContractDetail(contractUserSimulInbox.getSecUser());
				userContractDetail.setNbCurrentContracts(INBOX_SRV.countCurrentContractByUser(user));
				
				contractsByStaff.put(contractUserSimulInbox.getSecUser(), userContractDetail);
			}
			ContractDetail contractDetail = new ContractDetail(contract);
			contractDetail.setArea(contractUserSimulInbox.getArea());
			userContractDetail.addNewContract(contractDetail);
			if (contract.getNumberGuarantors() > 0) {
				userContractDetail.setNbGuaranteedContracts(userContractDetail.getNbGuaranteedContracts() + 1);
			} else {
				userContractDetail.setNbNonGuaranteedContracts(userContractDetail.getNbNonGuaranteedContracts() + 1);
			}
			if (collection.getLastCollectionAssist() != null) {
                userContractDetail.setNbAssistedContracts(userContractDetail.getNbAssistedContracts() + 1);
            }
			if (collection.getLastCollectionFlag() != null) {
                userContractDetail.setNbFlaggedContracts(userContractDetail.getNbFlaggedContracts() + 1);
            }
			if (collection.getDueDay() <= 5) {
				userContractDetail.setNbDD1to5(userContractDetail.getNbDD1to5() + 1);
			} else if (collection.getDueDay() <= 10) {
				userContractDetail.setNbDD6to10(userContractDetail.getNbDD6to10() + 1);
			} else if (collection.getDueDay() <= 15) {
				userContractDetail.setNbDD11to15(userContractDetail.getNbDD11to15() + 1);
			} else if (collection.getDueDay() <= 20) {
				userContractDetail.setNbDD16to20(userContractDetail.getNbDD16to20() + 1);
			}
		}
		
		// Load staff with no contract
		List<SecUser> staffs = getStaffByColType();
		for (SecUser staff : staffs) {
			if (!contractsByStaff.containsKey(staff)) {
				UserContractDetail userContractDetail = new UserContractDetail(staff);
				userContractDetail.setNbCurrentContracts(INBOX_SRV.countCurrentContractByUser(staff));
				contractsByStaff.put(staff, userContractDetail);
			}
		}
		
		// Calculate Adjusted
		if (!filterPanel.isSearchAction()) {
			if (this.contractsByStaff != null) {
				for (SecUser staff : this.contractsByStaff.keySet()) {
					if (contractsByStaff.containsKey(staff)) {
						UserContractDetail newUserContractDetail = contractsByStaff.get(staff);
						UserContractDetail oldUserContractDetail = this.contractsByStaff.get(staff);
						long adjusted = newUserContractDetail.getNbNewContract() - oldUserContractDetail.getNbNewContract();
						adjusted += oldUserContractDetail.getNbAdjustedContracts();
						newUserContractDetail.setNbAdjustedContracts(adjusted);
					}
				}
			}
		}
		
		return contractsByStaff;
	}
	
	/**
	 * @param contractUserSimulInboxs
	 * @return
	 */
	private int getNbAreas(List<ContractUserSimulInbox> contractUserSimulInboxs) {
		Map<Long, Area> areas = new HashMap<>();
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			Area area = contractUserSimulInbox.getArea();
			if (area != null && !areas.containsKey(area.getId())) {
				areas.put(area.getId(), area);
			}
		}
		return areas.size();
	}
	
	/**
	 * Get Staff By DebtLevel
	 * @return
	 */
	private List<SecUser> getStaffByColType() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		if (EColType.PHONE.equals(colType)) {
			restrictions.addCriterion(Restrictions.eq("pro.code", IProfileCode.COL_PHO_STAFF));
		} else if (EColType.FIELD.equals(colType)) {
			restrictions.addCriterion(Restrictions.eq("pro.code", IProfileCode.COL_FIE_STAFF));
		} else if (EColType.INSIDE_REPO.equals(colType)) {
			restrictions.addCriterion(Restrictions.eq("pro.code", IProfileCode.COL_INS_STAFF));
		} else if (EColType.OA.equals(colType)) {
			restrictions.addCriterion(Restrictions.eq("pro.code", IProfileCode.COL_OA_STAFF));
		}
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(SecUserDeptLevel.class, "secdeb");
		subCriteria.add(Restrictions.eq(SecUserDeptLevel.DEBTLEVEL, debtLevel));
		subCriteria.setProjection(Projections.projectionList().add(Projections.property("secdeb." + SecUserDeptLevel.SECUSER + "." + SecUser.ID)));
		
		restrictions.addCriterion(Property.forName(SecUser.ID).in(subCriteria));
		return INBOX_SRV.list(restrictions);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnDisableResources) {
			if (selectedStaff == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.info.deactivate.staff.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.deactivate.contract.user.inbox"), new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -5033520112470958188L;
					/**
					 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
					 */
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							reassign(-1, null, false);
							setStaffTableContainerDataSource(false);
							calculateDetailData();
						}
					}
				});
				confirmDialog.setWidth(400, Unit.PIXELS);
				confirmDialog.setHeight(150, Unit.PIXELS);
			}
		} else if (event.getButton() == btnAssign) {
			if (validateAssign()) {
				int nbContract = getInteger(txtNbContract);
				if (AUTO.equals(cbxMove.getValue())) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.reassigned.contract.user.inbox", String.valueOf(nbContract)), new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = 4942531753829938995L;
						/**
						 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
						 */
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								reassign(nbContract, null, false);
								setStaffTableContainerDataSource(false);
								calculateDetailData();
								txtNbContract.setValue("");
								cbxMove.setValue(null);
							}
						}
					});
					confirmDialog.setWidth(400, Unit.PIXELS);
					confirmDialog.setHeight(150, Unit.PIXELS);
				} else if (MANUAL.equals(cbxMove.getValue())) {
					UnassignedManualPanel manualPopup = new UnassignedManualPanel(this, colType);
					manualPopup.show();
					manualPopup.assignValues(nbContract, selectedStaff);
					txtNbContract.setValue("");
					cbxMove.setValue(null);
				}
			}
		} else if (event.getButton() == btnConfirm) {
			int nbOfContracts = 0;
			for (SecUser user : contractsByStaff.keySet()) {
				nbOfContracts += contractsByStaff.get(user).getNbNewContract();
			}
			if (nbOfContracts > 0) {
				confirmAssignmentPopup.setNbContracts(nbOfContracts);
				confirmAssignmentPopup.setConfirmListener(new ConfirmListener() {
					/** */
					private static final long serialVersionUID = -3911615265628838374L;
					@Override
					public void onConfirmAssignment(int nbContracts) {
						if (contractsByStaff != null && !contractsByStaff.isEmpty()) {
							int count = 0;
							for (SecUser user : contractsByStaff.keySet()) {
								int assignSize = contractsByStaff.get(user).getNewContracts().size();
								if (count == nbContracts) {
									break;
								}
								if (count + assignSize <= nbContracts) {
									INBOX_SRV.addContractDetailsToInbox(user, contractsByStaff.get(user).getNewContracts(), colType);
									count += assignSize;
								} else if (count < nbContracts) {
									List<ContractDetail> assignContracts = contractsByStaff.get(user).getNewContracts().subList(0, nbContracts - count);
									INBOX_SRV.addContractDetailsToInbox(user, assignContracts, colType);
									count = nbContracts;
								}
							}
							setStaffTableContainerDataSource(true);
							calculateDetailData();
						}
					}
				});
				confirmAssignmentPopup.show();
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("msg.info.no.contract.confirm.assignment"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		} else if (event.getButton() == btnReset) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.reset"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = 3970281516362541207L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						setStaffTableContainerDataSource(true);
						calculateDetailData();
					}
				}
			});
			confirmDialog.setWidth(400, Unit.PIXELS);
			confirmDialog.setHeight(150, Unit.PIXELS);
		}
	}
	
	/**
	 * Validate Assign
	 * @return
	 */
	private boolean validateAssign() {
		if (!ValidateUtil.checkMandatoryField(txtNbContract, "")) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("number.contract")), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (!ValidateUtil.checkIntegerField(txtNbContract, "")) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("field.value.incorrect.1", I18N.message("number.contract")), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (getInteger(txtNbContract) < 1) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("number.contract.must.greater.then.zero"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (selectedStaff == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.reassigned.staff.not.seelcted"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (getInteger(txtNbContract) > contractsByStaff.get(selectedStaff).getNbNewContract()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.error.nb.contract.over.selected.staff.contract"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else if (cbxMove.getValue() == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("reassign.type")), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * Reassign
	 * @param nbContract
	 */
	public void reassign(long nbContract, Map<SecUser, Integer> reassignStaffs, boolean manual) {
		List<ContractUserSimulInbox> reassignSimulInboxs = new ArrayList<ContractUserSimulInbox>();
		List<ContractUserSimulInbox> newSimulInboxs = new ArrayList<ContractUserSimulInbox>();
		Map<SecUser, UserContractDetail> conGroupByStaff = groupContractsByUser(contractUserSimulInboxs);
		if (nbContract < 0) {
			nbContract = conGroupByStaff.get(selectedStaff).getNbNewContract();
		}
		
		int count = 0;
		for (ContractUserSimulInbox simulInbox : contractUserSimulInboxs) {
			if (simulInbox.getSecUser().getId().equals(selectedStaff.getId()) && count < nbContract) {
				reassignSimulInboxs.add(simulInbox);
				count++;
			} else {
				newSimulInboxs.add(simulInbox);
			}
		}
		List<SecUser> staffs = getStaffByColType();
		if (!staffs.isEmpty()) {
			staffs.remove(selectedStaff);
		}
		
		// Reassigned
		int nbStaff = staffs.size();
		if (!manual && nbStaff > 0) {
			int index = 0;
			for (ContractUserSimulInbox simulInbox : reassignSimulInboxs) {
				if (index > nbStaff - 1) {
					index = 0;
				}
				simulInbox.setSecUser(staffs.get(index++));
				newSimulInboxs.add(simulInbox);
			}
			contractUserSimulInboxs = newSimulInboxs;
		} else {
			int index = 0;
			for (SecUser user : reassignStaffs.keySet()) {
				for (int i = 0; i < reassignStaffs.get(user); i++) {
					if (index < reassignSimulInboxs.size()) {
						ContractUserSimulInbox simulInbox = reassignSimulInboxs.get(index++);
						simulInbox.setSecUser(user);
						newSimulInboxs.add(simulInbox);
					}
				}
			}
		}
	}
	
	/**
	 * Get ContractsByStaff
	 * @return
	 */
	public Map<SecUser, UserContractDetail> getContractsByStaff() {
		return contractsByStaff;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNbContractPerArea() {
		assignValues();
		return nbContractArea;
	}

}
