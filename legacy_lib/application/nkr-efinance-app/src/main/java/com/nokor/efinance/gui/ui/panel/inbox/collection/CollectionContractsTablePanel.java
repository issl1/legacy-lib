package com.nokor.efinance.gui.ui.panel.inbox.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.collection.service.CollectionAssistRestriction;
import com.nokor.efinance.core.collection.service.CollectionFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionContractsTablePanel extends VerticalLayout implements FinServicesHelper, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8175776486322653133L;
	
	private final static String USD_FORMART = "###,##0.00";
	
	private SimpleTable<Contract> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	private boolean selectAll = false;
	private List<Long> selectedIds = new ArrayList<>();
	private Item selectedItem;
	
	private Button btnAssist;
	private Button btnFlag;
	private Button btnExtend;
	private Button btnPrintReport;
	
	private BaseRestrictions<ContractUserInbox> restriction;
	
	/**
	 * 
	 */
	public CollectionContractsTablePanel(boolean isAssistance) {
		this.columnDefinitions = getColumnDefinitions();
		simpleTable = new SimpleTable<>(this.columnDefinitions);
		simpleTable.setPageLength(25);
		simpleTable.setSizeFull();
		
		simpleTable.setColumnIcon("select", FontAwesome.CHECK);
		simpleTable.addHeaderClickListener(new HeaderClickListener() {
			
			/** */
			private static final long serialVersionUID = -1845073176795142097L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == "select") {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					java.util.Collection<Long> ids = (java.util.Collection<Long>) simpleTable.getItemIds();
					for (Long id : ids) {
						Item item = simpleTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		simpleTable.addItemClickListener(new ItemClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6031495413652324271L;
			
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				Long conId = (Long) selectedItem.getItemProperty("id").getValue();
				if (event.isDoubleClick()) {
					Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + conId);
				}
				
			}
		});
		
		btnAssist = ComponentLayoutFactory.getButtonStyle("request.assist", null, 100, "btn btn-success");
		btnAssist.removeClickShortcut();
		btnAssist.addClickListener(this);
		btnAssist.setVisible(!ProfileUtil.isColOAStaff());
		
		btnFlag = ComponentLayoutFactory.getButtonStyle("request.flag", null, 100, "btn btn-success");
		btnFlag.removeClickShortcut();
		btnFlag.addClickListener(this);
		btnFlag.setVisible(!ProfileUtil.isColOAStaff());
		
		btnExtend = ComponentLayoutFactory.getButtonStyle("extend", null, 100, "btn btn-success");
		btnExtend.removeClickShortcut();
		btnExtend.addClickListener(this);
		btnExtend.setVisible(ProfileUtil.isColInsideRepoStaff() || ProfileUtil.isColOAStaff());
		
		btnPrintReport = ComponentLayoutFactory.getButtonStyle("print.report", null, 100, "btn btn-success");
		btnPrintReport.removeClickShortcut();
		btnPrintReport.addClickListener(this);
		btnPrintReport.setVisible(ProfileUtil.isColInsideRepoStaff() || ProfileUtil.isColFieldStaff() || ProfileUtil.isColOAStaff());
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		
		if (isAssistance) {
			if (!ProfileUtil.isColOAStaff() && !ProfileUtil.isColInsideRepoStaff()) {
				buttonLayout.addComponent(btnExtend);
			}
			
			if (ProfileUtil.isColFieldStaff() || ProfileUtil.isColOAStaff() || ProfileUtil.isColInsideRepoStaff()) {
				buttonLayout.addComponent(btnPrintReport);
			}
			btnFlag.setVisible(true);
			buttonLayout.addComponent(btnFlag);
		} else {
			buttonLayout.addComponent(btnExtend);
			buttonLayout.addComponent(btnPrintReport);
			buttonLayout.addComponent(btnAssist);
			buttonLayout.addComponent(btnFlag);
		}
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(simpleTable);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		addComponent(mainLayout);
	}
	
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		if (ProfileUtil.isCollectionStaff()) {
			columnDefinitions.add(new ColumnDefinition("select", StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30));
		}
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70, false));
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("fullname", I18N.message("fullname"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("first.dd", I18N.message("first.dd"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("primary.phone", I18N.message("primary.phone.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("due.day", I18N.message("due.day"), Integer.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("lpd", I18N.message("lpd"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("odi", I18N.message("odi"), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("pi.term", I18N.message("pi.term"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("apd", I18N.message("apd"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("penalty", I18N.message("penalty"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("other.due", I18N.message("other.due"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("total.due", I18N.message("total.due"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("total.collected", I18N.message("total.collected"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("promised.date", I18N.message("promised.date"), Date.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("last.result", I18N.message("last.result"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param restriction
	 */
	public void refresh(BaseRestrictions<ContractUserInbox> restriction) {
		List<ContractUserInbox> contractUserInboxs = CONT_SRV.list(restriction);	
		assignValues(contractUserInboxs);
	}
	
	/**
	 * 
	 * @param contractUserInboxs
	 */
	public void assignValues(List<ContractUserInbox> contractUserInboxs) {
		setTableIndexedContainer(contractUserInboxs);
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<ContractUserInbox> contractUserInboxs) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		for (ContractUserInbox contractUserInbox : contractUserInboxs) {
			Contract contract = contractUserInbox.getContract();
			if (contract != null) {
				Item item = indexedContainer.addItem(contract.getId());
				item.getItemProperty("id").setValue(contract.getId());
				if (ProfileUtil.isCollectionStaff()) {
					item.getItemProperty("select").setValue(getRenderSelected(contract.getId()));
				}
				
				item.getItemProperty("contract.id").setValue(contract.getReference());
				item.getItemProperty("status").setValue(contract.getWkfStatus() != null ? contract.getWkfStatus().getDescLocale() : "");
				item.getItemProperty("first.dd").setValue(contract.getFirstDueDate());
				item.getItemProperty("due.day").setValue(contract.getCollection().getDueDay());
				
				Applicant applicant = contract.getApplicant();
				if (applicant != null) {
					item.getItemProperty("fullname").setValue(applicant.getNameLocale());
					item.getItemProperty("primary.phone").setValue(applicant.getIndividual().getIndividualPrimaryContactInfo());
				}
				
				Collection collection = contract.getCollection();
				if (collection != null) {
					String piTerm = MyNumberUtils.getDouble(collection.getPartialPaidInstallment()) + "/" + contract.getTerm();
					item.getItemProperty("lpd").setValue(collection.getLastPaymentDate());
					item.getItemProperty("odi").setValue(MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue()));
					item.getItemProperty("pi.term").setValue(piTerm);
					item.getItemProperty("apd").setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue()), "###,##0.00"));
					item.getItemProperty("penalty").setValue(AmountUtils.format(collection.getTiPenaltyAmount()));
					item.getItemProperty("total.due").setValue(AmountUtils.format(collection.getTiTotalAmountInOverdue()));
					CollectionHistory collectionHistory = collection.getLastCollectionHistory();
					if (collectionHistory != null) {
						item.getItemProperty("last.result").setValue(collectionHistory.getResult().getCode());	
					}
				}
				
				double collected = 0d;
				List<Payment> payments = PAYMENT_SRV.getListPaymentPaidInCurrentMonth(contract.getId());
				if (payments != null && !payments.isEmpty()) {
					for (Payment payment : payments) {
						collected += MyNumberUtils.getDouble(payment.getTiPaidAmount());
					}
				}
				item.getItemProperty("total.collected").setValue(MyNumberUtils.formatDoubleToString(collected, USD_FORMART));
				
				LockSplitRestriction lockSplitRestriction = new LockSplitRestriction();
				lockSplitRestriction.setContractID(contract.getReference());
				lockSplitRestriction.getWkfStatusList().add(LockSplitWkfStatus.LNEW);
				lockSplitRestriction.addOrder(Order.desc(LockSplit.ID));
				LockSplit lckSplit = null;
				if (LCK_SPL_SRV.list(lockSplitRestriction) != null && !LCK_SPL_SRV.list(lockSplitRestriction).isEmpty()) {
					lckSplit = LCK_SPL_SRV.list(lockSplitRestriction).get(0);
				}
				
				if (lckSplit != null) {
					Date promiseDate = null;
					if (lckSplit.getTo() != null) {
						promiseDate = lckSplit.getTo();
					}
					item.getItemProperty("promised.date").setValue(promiseDate);
				}
				
			}
		}
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
	 * 
	 * @param contract
	 * @return
	 */
	private CollectionFlag getLastColFlag(Contract contract) {
		CollectionFlagRestriction restrictions = new CollectionFlagRestriction();
		restrictions.setConId(contract.getId());
		List<CollectionFlag> flags = ENTITY_SRV.list(restrictions);
		if (flags != null && !flags.isEmpty()) {
			return flags.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private CollectionAssist getLastColAssist(Contract contract) {
		CollectionAssistRestriction restrictions = new CollectionAssistRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setRequestStatuses(new ERequestStatus[] { ERequestStatus.APPROVE, ERequestStatus.PENDING });
		List<CollectionAssist> assists = ENTITY_SRV.list(restrictions);
		if (assists != null && !assists.isEmpty()) {
			return assists.get(0);
		}
		return null;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnFlag) || event.getButton().equals(btnAssist)) {
			List<Contract> contracts = new ArrayList<Contract>();
			if (!selectedIds.isEmpty()) {
				for (int i = 0; i < selectedIds.size(); i++) {
					contracts.add(CONT_SRV.getById(Contract.class, selectedIds.get(i)));
				}	
			}
			if (event.getButton().equals(btnFlag)) {
				if (!contracts.isEmpty()) {	
					List<Long> contFlagIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionFlag flag = getLastColFlag(cont);
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
			} else if (event.getButton().equals(btnAssist)) {
				if (!contracts.isEmpty()) {
					List<Long> contAssistIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionAssist assist = getLastColAssist(cont);
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
		} else if (event.getButton() == btnExtend) {
			
		} else if (event.getButton() == btnPrintReport) {
			
		}
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
}
