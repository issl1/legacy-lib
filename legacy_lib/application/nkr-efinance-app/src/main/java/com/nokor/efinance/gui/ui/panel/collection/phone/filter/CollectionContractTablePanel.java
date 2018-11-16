package com.nokor.efinance.gui.ui.panel.collection.phone.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.collection.service.CollectionAssistRestriction;
import com.nokor.efinance.core.collection.service.CollectionFlagRestriction;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ISRWkfStatus;
import com.nokor.efinance.core.workflow.ReturnWkfStatus;
import com.nokor.efinance.gui.report.xls.GLFRecapContractInfo;
import com.nokor.efinance.gui.ui.panel.collection.MultiApproveAssistFlagLayout;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel;
import com.nokor.efinance.tools.report.Report;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionContractTablePanel extends AbstractControlPanel implements MCollection, ItemClickListener, SelectedItem, FinServicesHelper, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9047202487419926794L;
	
	private final static String SELECT = "select";
	private final static String USD_FORMART = "###,##0.00";
	
	private SimplePagedTable<Entity> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem;
	
	private BaseRestrictions<ContractUserInbox> restriction;
	
	private Button btnRequestFlag;
	private Button btnRequestAssist;
	
	private MultiApproveAssistFlagLayout multiApproveAssistFlagLayout;
	
	private List<Long> selectedIds = new ArrayList<>();
	private boolean selectAll = false;
	
	private Boolean isFlag;
	
	private Button btnPrint;

	/**
	 * @return the selectedIds
	 */
	public List<Long> getSelectedIds() {
		return selectedIds;
	}
	
	/**
	 * 
	 * @param isFlag
	 */
	public CollectionContractTablePanel(Boolean isFlag) {
		this.isFlag = isFlag;
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		btnRequestAssist = ComponentLayoutFactory.getDefaultButton("request", null, 60);
		btnRequestAssist.addClickListener(this);
		btnRequestFlag = ComponentLayoutFactory.getDefaultButton("request", null, 60);
		btnRequestFlag.addClickListener(this);
		
		btnPrint = ComponentLayoutFactory.getDefaultButton(null, FontAwesome.PRINT, 40);
		btnPrint.addClickListener(this);
		btnPrint.addStyleName("btn-icon-padding");
		
		Label lblAssistance = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("assistance") + " : " + "</h4>");
		Label lblFlag = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("flag") + " : " + "</h4>");
		
		HorizontalLayout buttonStaffRequestLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(false, false, true, false), true);
		buttonStaffRequestLayout.setVisible(ProfileUtil.isCollectionStaff());
		
		buttonStaffRequestLayout.addComponent(lblAssistance);
		buttonStaffRequestLayout.addComponent(btnRequestAssist);
		buttonStaffRequestLayout.addComponent(lblFlag);
		buttonStaffRequestLayout.addComponent(btnRequestFlag);
		buttonStaffRequestLayout.addComponent(btnPrint);
		
		buttonStaffRequestLayout.setComponentAlignment(lblAssistance, Alignment.MIDDLE_LEFT);
		buttonStaffRequestLayout.setComponentAlignment(lblFlag, Alignment.MIDDLE_LEFT);
		
		pagedTable = new SimplePagedTable<Entity>(getColumnDefinitions());
		pagedTable.addItemClickListener(this);
		pagedTable.addStyleName("colortable");
		pagedTable.addStyleName("table-column-wrap");
		pagedTable.setCellStyleGenerator(new CellStyleGenerator() {

			/** */
			private static final long serialVersionUID = 8342126812655891730L;

			/**
			 * @see com.vaadin.ui.Table.CellStyleGenerator#getStyle(com.vaadin.ui.Table, java.lang.Object, java.lang.Object)
			 */
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				Item item = pagedTable.getItem(itemId);
				Long conId = (Long) item.getItemProperty(ID).getValue();
				Contract contract = CONT_SRV.getById(Contract.class, conId);
				Collection collection = contract.getCollection();
				double apd = 0d;
				if (collection != null) {
					apd = MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue());
				}
				ContractFlagRestriction restrictions = new ContractFlagRestriction();
				restrictions.setConId(contract.getId());
				restrictions.getWkfStatusList().add(ReturnWkfStatus.RECLO);
				restrictions.getWkfStatusList().add(ISRWkfStatus.ISRCONF);
				List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
				ContractFlag contractFlag = null;
				if (contractFlags != null && !contractFlags.isEmpty()) {
					contractFlag = contractFlags.get(0);
				}
				if (apd == 0 || contractFlag != null) {
					return "highligh-lightgreen";
				}
				return null;
			}
		});
	
		VerticalLayout tableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		tableLayout.addComponent(pagedTable);		
		tableLayout.addComponent(pagedTable.createControls());
		pagedTable.setPageLength(25);	
		

		pagedTable.setColumnIcon(SELECT, FontAwesome.CHECK);
		pagedTable.addHeaderClickListener(new HeaderClickListener() {
			
			/** */
			private static final long serialVersionUID = -1845073176795142097L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == SELECT) {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					java.util.Collection<Long> ids = (java.util.Collection<Long>) pagedTable.getItemIds();
					for (Long id : ids) {
						Item item = pagedTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		
		multiApproveAssistFlagLayout = new MultiApproveAssistFlagLayout(this, isFlag, btnPrint);
		
		addComponent(multiApproveAssistFlagLayout);
		addComponent(buttonStaffRequestLayout);
		setSpacing(false);
		if (this.isFlag == null && !ProfileUtil.isCollectionStaff()) {
			setSpacing(true);
			addComponent(btnPrint);
		}
		addComponent(tableLayout);
	}
	
	/**
	 * 
	 * @param isFlag
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<>();
		/*if (ProfileUtil.isCollectionStaff() || (isFlag != null 
						&& (ProfileUtil.isColLeader() 
							|| ProfileUtil.isColFieldSupervisor()
							|| ProfileUtil.isColInsideRepoSupervisor()
							|| ProfileUtil.isColOASupervisor()))) {
			columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30));
		}*/
		columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACTID, I18N.message("contract.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ASSIGNDATE, I18N.message("assign.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(COLLECTOR, I18N.message("collector"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(LESSESS, I18N.message("lessee"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(BRANCH, I18N.message("branch"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PRIMARYPHONENO, I18N.message("primary.phone.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantors"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DUEDAY, I18N.message("due.day"), Integer.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("first.due.date", I18N.message("first.due.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("last.payment.date", I18N.message("last.payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PI, I18N.message("pi"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(RI, I18N.message("ri"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(ODI, I18N.message("odi"), Integer.class, Align.LEFT, 50));		
		columnDefinitions.add(new ColumnDefinition(ODM, I18N.message("odm"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), String.class, Align.RIGHT, 80));
		
		if (!ProfileUtil.isCallCenter()) {
			columnDefinitions.add(new ColumnDefinition(INSTALLMENT, I18N.message("installment"), String.class, Align.RIGHT, 80));
			columnDefinitions.add(new ColumnDefinition(COLLECTED, I18N.message("collected"), String.class, Align.RIGHT, 80));
			columnDefinitions.add(new ColumnDefinition(ASSIST, I18N.message("assist"), Component.class, Align.LEFT, 40));	
			columnDefinitions.add(new ColumnDefinition(FLAG, I18N.message("flag"), Component.class, Align.LEFT, 40));
		}
		columnDefinitions.add(new ColumnDefinition(LATESTRESULT, I18N.message("latest.result"), Component.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(RESULT, I18N.message("result"), Component.class, Align.LEFT, 80));
		if (ProfileUtil.isColField() || ProfileUtil.isColInsideRepo()) {
			columnDefinitions.add(new ColumnDefinition(AREA, I18N.message("area"), String.class, Align.LEFT, 120));
		}
		columnDefinitions.add(new ColumnDefinition(ACTION, I18N.message("action"), Button.class, Align.LEFT, 40));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<ContractUserInbox> contractUserInboxs) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		long index = 0l;
		if (contractUserInboxs != null && !contractUserInboxs.isEmpty()) {
			for (ContractUserInbox contractUserInbox : contractUserInboxs) {
				Contract contract = contractUserInbox.getContract();
				String primaryPhone = "";
				Boolean isCompleted = null;
				
				Collection collection = contract.getCollection();
				Applicant applicant = contract.getApplicant();
				Individual individual = applicant.getIndividual();
				CollectionFlag  collectionFlag = collection.getLastCollectionFlag();
				CollectionAssist collectionAssist = collection.getLastCollectionAssist();
				CollectionHistory collectionHistory = collection.getLastCollectionHistory();
				CallCenterHistory callCenterHistory = collection.getLastCallCenterHistory();
 				
				if (individual != null) {
					primaryPhone = individual.getIndividualPrimaryContactInfo();
				}
								
				if (collection.getLastAction() != null && collection.getLastAction().getColAction() != null) {
					if (EColAction.NOFURTHER.equals(collection.getLastAction().getColAction())) {
						isCompleted = true;
					} else if (EColAction.SCHEDULE.equals(collection.getLastAction().getColAction())) {
						isCompleted = false;
					}
				}				
				
				if (collection != null) {
					int dpd = MyNumberUtils.getInteger(collection.getNbOverdueInDays());
					int odm = MyNumberUtils.getInteger(collection.getDebtLevel());
					double apd = MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue());
					Integer dueDay = collection.getDueDay();
					int pi = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
					int ri = contract.getTerm() - pi;
					int odi = MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue());
					
					Item item = indexedContainer.addItem(index);
					
					/*if (ProfileUtil.isCollectionStaff() || (isFlag != null 
							&& (ProfileUtil.isColLeader() 
									|| ProfileUtil.isColFieldSupervisor()
									|| ProfileUtil.isColInsideRepoSupervisor()
									|| ProfileUtil.isColOASupervisor()))) {
						item.getItemProperty(SELECT).setValue(getRenderSelected(contract.getId()));
					}*/
					item.getItemProperty(SELECT).setValue(getRenderSelected(contract.getId()));
					
					if (!ProfileUtil.isCallCenter()) {
						
						double collected = 0d;
						List<Payment> payments = PAYMENT_SRV.getListPaymentPaidInCurrentMonth(contract.getId());
						if (payments != null && !payments.isEmpty()) {
							for (Payment payment : payments) {
								collected += MyNumberUtils.getDouble(payment.getTiPaidAmount());
							}
						}
						
						if (collectionFlag != null) {
							item.getItemProperty(FLAG).setValue(new Label(collectionFlag.getRequestStatus().getDescEn()));
						} else {
							item.getItemProperty(FLAG).setValue(new FlagButton(contract));
						}
						
						if (collectionAssist != null) {
							item.getItemProperty(ASSIST).setValue(new Label(collectionAssist.getRequestStatus().getDescEn()));
						} else {
							item.getItemProperty(ASSIST).setValue(new AssistButton(contract));
						}
						if (collectionHistory != null && collectionHistory.getResult() != null) {
							item.getItemProperty(LATESTRESULT).setValue(new Label(collectionHistory.getResult().getCode()));
						}
						
						item.getItemProperty(INSTALLMENT).setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(
								contract.getTiInstallmentAmount()), USD_FORMART));
						item.getItemProperty(COLLECTED).setValue(MyNumberUtils.formatDoubleToString(collected, USD_FORMART));
					} else if (callCenterHistory != null && callCenterHistory.getResult() != null) {
						item.getItemProperty(LATESTRESULT).setValue(new Label(callCenterHistory.getResult().getCode()));
					}
					
					item.getItemProperty(RESULT).setValue(new FollowUpButton(contract));
					
					if (isCompleted != null && isCompleted) {
						item.getItemProperty(RESULT).setValue(new Label(I18N.message("completed")));
					} else {
						item.getItemProperty(RESULT).setValue(new Label(I18N.message("follow.up")));
					}
					
					if (ProfileUtil.isColField() || ProfileUtil.isColInsideRepo()) {
						item.getItemProperty(AREA).setValue(collection.getArea() != null ? collection.getArea().getCode() : "");
					}
								
					item.getItemProperty(ID).setValue(contract.getId());
					item.getItemProperty(CONTRACTID).setValue(contract.getReference());
					item.getItemProperty(ASSIGNDATE).setValue(contractUserInbox.getCreateDate());
					item.getItemProperty(COLLECTOR).setValue(contractUserInbox.getSecUser() != null ? 
							contractUserInbox.getSecUser().getDesc() : StringUtils.EMPTY);
					item.getItemProperty(LESSESS).setValue(applicant.getNameEn());
					item.getItemProperty(PRIMARYPHONENO).setValue(primaryPhone);
					item.getItemProperty(GUARANTOR).setValue(contract.getNumberGuarantors());
					item.getItemProperty(DUEDAY).setValue(dueDay);
					item.getItemProperty("first.due.date").setValue(contract.getFirstDueDate());
					item.getItemProperty("last.payment.date").setValue(collection.getLastPaymentDate());
					item.getItemProperty(PI).setValue(pi);
					item.getItemProperty(RI).setValue(ri);
					item.getItemProperty(ODI).setValue(odi);
					item.getItemProperty(ODM).setValue(odm);
					item.getItemProperty(DPD).setValue(dpd);
					item.getItemProperty(APD).setValue(MyNumberUtils.formatDoubleToString(apd, USD_FORMART));
					item.getItemProperty(ACTION).setValue(new ActionButton(contract));	
					String branchIncharge = "";
					String originBranch = "";
					if (contract.getBranchInCharge() != null) {
						branchIncharge = contract.getBranchInCharge().getNameLocale();
					}
					if (contract.getOriginBranch() != null) {
						originBranch = contract.getOriginBranch().getNameLocale();
					}
					if (StringUtils.isEmpty(originBranch)) {
						item.getItemProperty(BRANCH).setValue(branchIncharge);
					} 
					item.getItemProperty(BRANCH).setValue(StringUtils.isEmpty(originBranch) ?
							branchIncharge : originBranch);
					
					index++;
				}
			}
		}
		return indexedContainer;
	}
	
	/**
	 * @param conId
	 * @return
	 */
	private CheckBox getRenderSelected(Long conId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(conId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -4636741293540271138L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (checkBox.getValue()) {
					selectedIds.add(id);
					Collections.sort(selectedIds, new Comparator<Long>() {
						
						/**
						 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
						 */
						@Override
					    public int compare(Long o1, Long o2) {
					        return o1.compareTo(o2);
					    }
					});
				} else {
					selectedIds.remove(id);
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnRequestFlag) || event.getButton().equals(btnRequestAssist)) {
			List<Contract> contracts = new ArrayList<Contract>();
			if (!selectedIds.isEmpty()) {
				for (int i = 0; i < selectedIds.size(); i++) {
					contracts.add(CONT_SRV.getById(Contract.class, selectedIds.get(i)));
				}	
			}
			if (event.getButton().equals(btnRequestFlag)) {
				if (!contracts.isEmpty()) {	
					List<Long> contFlagIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionFlag flag = getLastColFlag(cont, new ERequestStatus[] { ERequestStatus.APPROVE, ERequestStatus.PENDING });
						if (flag != null) {
							contFlagIds.add(cont.getId());
						}
					}
					if (contFlagIds.isEmpty()) {
						CollectionRequestPopupPanel.show(contracts, "flag", new CollectionRequestPopupPanel.Listener() {
							
							/** */
							private static final long serialVersionUID = -9075456808354961144L;

							/**
							 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
							 */
							@Override
							public void onClose(CollectionRequestPopupPanel dialog) {
								refresh(getRestriction());
								selectedIds.clear();
							}
						});
					} else {
						Notification notification = ComponentLayoutFactory.getNotification(Type.WARNING_MESSAGE);
						notification.setDescription(getErrorIndex(contFlagIds, "already.flagged"));
					}
				}
			} else if (event.getButton().equals(btnRequestAssist)) {
				if (!contracts.isEmpty()) {
					List<Long> contAssistIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionAssist assist = getLastColAssist(cont, new ERequestStatus[] { ERequestStatus.APPROVE, ERequestStatus.PENDING });
						if (assist != null) {
							contAssistIds.add(cont.getId());
						}
					}
					if (contAssistIds.isEmpty()) {
						CollectionRequestPopupPanel.show(contracts, "assist", new CollectionRequestPopupPanel.Listener() {
							
							/** */
							private static final long serialVersionUID = -127360804863667575L;

							/**
							 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
							 */
							@Override
							public void onClose(CollectionRequestPopupPanel dialog) {
								refresh(getRestriction());
								selectedIds.clear();
							}
						});
					} else {
						Notification notification = ComponentLayoutFactory.getNotification(Type.WARNING_MESSAGE);
						notification.setDescription(getErrorIndex(contAssistIds, "already.assisted"));
					}
				}
			}
		} else if (event.getButton().equals(btnPrint)) {
			if (!selectedIds.isEmpty()) {
				Class<? extends Report> reportClass = GLFRecapContractInfo.class;
				ReportParameter reportParameter = new ReportParameter();
				reportParameter.addParameter(Contract.ID, selectedIds);
				String fileName = "";
				try {
					fileName = REPORT_SRV.extract(reportClass, reportParameter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
				DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName);
				UI.getCurrent().addWindow(documentViewver);
			} else {
				ComponentLayoutFactory.displayErrorMsg("msg.select.at.least.one.contract");
			}
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @param requestStatuses
	 * @return
	 */
	public CollectionFlag getLastColFlag(Contract contract, ERequestStatus[] requestStatuses) {
		CollectionFlagRestriction restrictions = new CollectionFlagRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setRequestStatuses(requestStatuses);
		List<CollectionFlag> flags = ENTITY_SRV.list(restrictions);
		if (flags != null && !flags.isEmpty()) {
			return flags.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param contract
	 * @param requestStatuses
	 * @return
	 */
	public CollectionAssist getLastColAssist(Contract contract, ERequestStatus[] requestStatuses) {
		CollectionAssistRestriction restrictions = new CollectionAssistRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setRequestStatuses(requestStatuses);
		List<CollectionAssist> assists = ENTITY_SRV.list(restrictions);
		if (assists != null && !assists.isEmpty()) {
			return assists.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param longs
	 * @param desc
	 * @return
	 */
	private String getErrorIndex(List<Long> longs, String desc) {
		StringBuffer stringBuffer = new StringBuffer(); 
		if (!longs.isEmpty()) {
			stringBuffer.append("Column ID [ ");
			for (Long integer : longs) {
				stringBuffer.append(integer);
				stringBuffer.append(",");
			}
			int lastIndex = stringBuffer.lastIndexOf(",");
			stringBuffer.replace(lastIndex, lastIndex + 1, " ]");
			stringBuffer.append(StringUtils.SPACE);
			stringBuffer.append(I18N.message(desc));
		}
		return stringBuffer.toString();
	}
	
	/**
	 * AssignValue
	 * @param contracts
	 */
	public void assignValues(List<ContractUserInbox> contractUserInboxs) {
		pagedTable.setContainerDataSource(getIndexedContainer(contractUserInboxs));
	}
	
	/**
	 * Refresh
	 * @param restriction
	 */
	public void refresh(BaseRestrictions<ContractUserInbox> restriction) {
		List<ContractUserInbox> contractUserInboxs = CONT_SRV.list(restriction);	
		assignValues(contractUserInboxs);
	}
	
	/**
	 * @author buntha.chea
	 */
	private class ActionButton extends NativeButton {
		
		private static final long serialVersionUID = 5331682229749631545L;

		public ActionButton(Contract contract) {
			super("");
			setIcon(FontAwesome.EXTERNAL_LINK);
			setStyleName(Reindeer.BUTTON_LINK);
			addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -8479467763680332747L;
				
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + contract.getId());
				}
			});
		}
	}
	
	/**
	 * @author buntha.chea
	 * FollowUpButton
	 */
	private class FollowUpButton extends NativeButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2423760096066635012L;

		public FollowUpButton(Contract contract) {
			super("");
			setCaption(I18N.message("update"));
			setStyleName("btn btn-success button-small");
			addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -950334390103001404L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					CollectionFollowUpPopupPanel followUpWindow = CollectionFollowUpPopupPanel.show(contract, new CollectionFollowUpPopupPanel.Listener() {
	
						/** */
						private static final long serialVersionUID = 7213742764603753855L;

						/**
						 * @see com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionFollowUpPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionFollowUpPopupPanel)
						 */
						@Override
						public void onClose(CollectionFollowUpPopupPanel dilog) {
							refresh(getRestriction());
						}
					});
					UI.getCurrent().addWindow(followUpWindow);
				}
			});
		}
	}
	
	/**
	 * @author buntha.chea
	 * FlagButton
	 */
	private class FlagButton extends NativeButton {
		
		private static final long serialVersionUID = 5331682229749631545L;

		public FlagButton(Contract contract) {
			super("");
			setCaption(I18N.message("new"));
			setStyleName("btn btn-success button-small");
			this.setEnabled(ProfileUtil.isCollectionStaff());
			addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 8766734154167225082L;
				
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					List<Contract> contracts = new ArrayList<Contract>();
					contracts.add(contract);
					CollectionRequestPopupPanel.show(contracts, "flag", new CollectionRequestPopupPanel.Listener() {
						
						/** */
						private static final long serialVersionUID = -5092899580137915228L;

						/**
						 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
						 */
						@Override
						public void onClose(CollectionRequestPopupPanel dialog) {
							refresh(getRestriction());
						}
					});
				}
			});
		}
	}
	
	/**
	 * @author buntha.chea
	 * AssistButton
	 */
	private class AssistButton extends NativeButton {
		
		private static final long serialVersionUID = 5331682229749631545L;

		public AssistButton(Contract contract) {
			super("");
			setCaption(I18N.message("new"));
			setStyleName("btn btn-success button-small");
			this.setEnabled(ProfileUtil.isCollectionStaff());
			addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -5702259066855874997L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					List<Contract> contracts = new ArrayList<Contract>();
					contracts.add(contract);
					CollectionRequestPopupPanel.show(contracts, "assist", new CollectionRequestPopupPanel.Listener() {
						
						/** */
						private static final long serialVersionUID = 4179861531737984784L;

						/**
						 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
						 */
						@Override
						public void onClose(CollectionRequestPopupPanel dialog) {
							refresh(getRestriction());
						}
					});
				}
			});
		}
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + getItemSelectedId());
		}
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @return the restriction
	 */
	public BaseRestrictions<ContractUserInbox> getRestriction() {
		return restriction;
	}

	/**
	 * @param restriction the restriction to set
	 */
	public void setRestriction(BaseRestrictions<ContractUserInbox> restriction) {
		this.restriction = restriction;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

}
