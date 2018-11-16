package com.nokor.efinance.gui.ui.panel.contract.cashflow.locksplit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Lock split pop up window in cash flow
 * @author uhout.cheng
 */
public class CashflowLockSplitPopupWindow extends Window implements FinServicesHelper, ClickListener, CloseListener {
	
	/** */
	private static final long serialVersionUID = 4880337230142173136L;

	private CashflowLockSplitPanel cashflowLockSplitPanel;
	
	private Button btnSave;
	private Button btnDelete;
	private Button btnCancel;
	
	private AutoDateField dfCreationDate;
	private AutoDateField dfPaymentDate;
	private AutoDateField dfExpiryDate;
	private TextField txtReference;
	private TextField txtDepartment;
	private TextField txtUser;
	private TextField txtTotalBalance;
	
	private VerticalLayout contentLayout;
	private LockSplit lockSplit;
	
	private CustomLayout customLayout;
	private double totalAmountOfPayment;
	
	private List<TextField> txtAmounts;
	private List<TextField> txtBalances;
	private List<EntityRefComboBox<JournalEvent>> cbxLockSplitItems;
	private List<TextField> txtPrioritys;
	private VerticalLayout messagePanel;
	private VerticalLayout locksplitItemLayout;
	
	private static final String OPEN_TABLE = "<table cellspacing=\"3\" cellpadding=\"3\" style=\"border-collapse:collapse\" >";
	private static final String OPEN_TR = "<tr>";
	private static final String OPEN_TH = "<th class=\"align-center\" width=\"180px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static final String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
	private static final String CLOSE_TR = "</tr>";
	private static final String CLOSE_TH = "</th>";
	private static final String CLOSE_TD = "</td>";
	private static final String CLOSE_TABLE = "</table>";
	
	/**
	 * 
	 * @param cashflowLockSplitPanel
	 */
	public CashflowLockSplitPopupWindow(CashflowLockSplitPanel cashflowLockSplitPanel) {
		super(I18N.message("lock.split"));
		setModal(true);
		setResizable(false);
		this.cashflowLockSplitPanel = cashflowLockSplitPanel;
		addCloseListener(this);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.BAN);
		btnCancel.addClickListener(this);

		txtBalances = new ArrayList<>();
		txtAmounts = new ArrayList<>();
		cbxLockSplitItems = new ArrayList<>();
		txtPrioritys = new ArrayList<>();
		
		dfCreationDate = ComponentFactory.getAutoDateField("creation.date", false);
		dfCreationDate.setEnabled(false);
		dfPaymentDate = ComponentFactory.getAutoDateField("payment.date", false);
		dfExpiryDate = ComponentFactory.getAutoDateField("deadline", false);
		txtReference = ComponentFactory.getTextField("reference", false, 50, 150);
		txtReference.setEnabled(false);
		txtDepartment = ComponentFactory.getTextField("department", false, 50, 150);
		txtUser = ComponentFactory.getTextField("user", false, 50, 150);
		txtUser.setEnabled(false);
		
		txtTotalBalance = ComponentFactory.getTextField(50, 120);
		txtTotalBalance.setEnabled(false);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnDelete);
		navigationPanel.addButton(btnCancel);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtDepartment);
		formLayout.addComponent(dfPaymentDate);
		formLayout.addComponent(dfExpiryDate);
		horLayout.addComponent(formLayout);
		
		formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		formLayout.addComponent(dfCreationDate);
		formLayout.addComponent(txtUser);
		horLayout.addComponent(formLayout);
		
		locksplitItemLayout = new VerticalLayout();
		locksplitItemLayout.setMargin(true);
		
		contentLayout = new VerticalLayout();
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(new VerticalLayout(horLayout));
		contentLayout.addComponent(locksplitItemLayout);
		
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValue(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
		setVisibleButton(lockSplit);
		txtReference.setValue(lockSplit.getReference());
		txtDepartment.setValue("");
		dfPaymentDate.setValue(lockSplit.getFrom());
		dfExpiryDate.setValue(lockSplit.getTo());
		dfCreationDate.setValue(lockSplit.getCreateDate());
		txtUser.setValue(getUserName());
		List<LockSplitItem> lockSplitItems = lockSplit.getItems();
		if (!lockSplitItems.isEmpty()) {
			Collections.sort(lockSplitItems, new Comparator<LockSplitItem>() {
				
				/**
				 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
				 */
				@Override
				public int compare(LockSplitItem o1, LockSplitItem o2) {
					if (o1.getPriority() == null) return 1;
					if (o2.getPriority() == null) return -1;
					if (o1.getPriority() == o2.getPriority()) return 0;
					if (o1.getPriority() > o2.getPriority()) {
						return 1;
					} else {
						return -1;
					}
				}
			});
		}
		buildLockSplitItemTable(lockSplitItems);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	private void setVisibleButton(LockSplit lockSplit) {
		SecUser secUser = UserSessionManager.getCurrentUser();
		boolean isVisible = lockSplit.getCreateUser().equals(secUser.getLogin());
		btnSave.setVisible(isVisible);
		btnDelete.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param lockSplit
	 * @return
	 */
	private SecUser getSecUser(LockSplit lockSplit) {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addCriterion(Restrictions.eq(SecUser.LOGIN, lockSplit.getCreateUser()));
		List<SecUser> secUsers = LCK_SPL_SRV.list(restrictions);
		if (secUsers != null && !secUsers.isEmpty()) {
			return secUsers.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getUserName() {
		SecUser secUser = getSecUser(lockSplit);
		StringBuffer buffer = new StringBuffer();
		if (secUser != null) {
			buffer.append(secUser.getLogin());
			buffer.append(" (");
			buffer.append(secUser.getDesc());
			buffer.append(")");
		}
		return buffer.toString();
	}
	
	/**
	 * Build Lock Split Item Table
	 * @param lockSplitItems
	 */
	private void buildLockSplitItemTable(List<LockSplitItem> lockSplitItems){
		lockSplitItemClear();
		
		totalAmountOfPayment = 0d;
		
		customLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TH;
		template += "<div location =\"lblItem\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblBalance\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblAmount\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblPaymentPriority\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		
		int i = 0;
		for (LockSplitItem item : lockSplitItems) {
			template += OPEN_TR;
			template += OPEN_TD;
			template += "<div location =\"item" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"txtBlance" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"txtAmount" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"cbxPeymentPriority" + i + "\" />";
			template += CLOSE_TD;
			template += CLOSE_TR;
			
			JournalEventRestriction journalEventRestriction = new JournalEventRestriction();
			journalEventRestriction.setJournalId(JournalEvent.RECEIPTS);
			EntityRefComboBox<JournalEvent> cbxLockSplitType = getEntityRefComboBox(journalEventRestriction);
			cbxLockSplitType.setSelectedEntity(item.getJournalEvent());
			cbxLockSplitItems.add(cbxLockSplitType);
			
			TextField txtAmount = ComponentFactory.getTextField(10, 120);
			txtAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(item.getTiAmount())));
			txtAmounts.add(txtAmount);
			txtAmount.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = 1460019461241753984L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					totalAmountOfPayment = 0d;
					for (int j = 0; j < txtAmounts.size(); j++) {
						totalAmountOfPayment += MyNumberUtils.getDouble(txtAmounts.get(j).getValue(), 0d);
					}
				}
			});
			
			TextField txtBalance = ComponentFactory.getTextField(10, 120);
			txtBalance.setValue("0.00");
			txtBalances.add(txtBalance);
			txtBalance.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = -973665562812307890L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					double updateTotalBalance = 0d;
					for (int j = 0; j < txtBalances.size(); j++) {
						updateTotalBalance += MyNumberUtils.getDouble(txtBalances.get(j).getValue(), 0d);
					}
					txtTotalBalance.setValue(AmountUtils.format(MyNumberUtils.getDouble(updateTotalBalance)));
				}
			});
			
			TextField txtPriority = ComponentFactory.getTextField(5, 70);
			txtPriority.setValue(item.getPriority() != null ? String.valueOf(item.getPriority()) : "");
			txtPrioritys.add(txtPriority);
			
			totalAmountOfPayment += MyNumberUtils.getDouble(item.getTiAmount());
			
			customLayout.addComponent(cbxLockSplitType, "item" + i);
			customLayout.addComponent(txtBalance, "txtBlance" + i);
			customLayout.addComponent(txtAmount, "txtAmount" + i);
			customLayout.addComponent(txtPriority, "cbxPeymentPriority" + i);
			i++;
		}
		
		template += OPEN_TR;
		template += "<td class=\"align-right\" >";
		template += "<div location =\"lblTotalBalance\" />";
		template += CLOSE_TD;
		template += "<td class=\"align-center\" >";
		template += "<div location =\"txtTotalBalance\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		customLayout.addComponent(ComponentFactory.getLabel("item"), "lblItem");
		customLayout.addComponent(ComponentFactory.getLabel("balance"), "lblBalance");
		customLayout.addComponent(ComponentFactory.getLabel("amount.in.vat"), "lblAmount");
		customLayout.addComponent(ComponentFactory.getLabel("payment.priority"), "lblPaymentPriority");
		customLayout.addComponent(ComponentFactory.getLabel("total.balance"), "lblTotalBalance");
		customLayout.addComponent(txtTotalBalance, "txtTotalBalance");
		
		template += CLOSE_TABLE;
		customLayout.setTemplateContents(template);
		locksplitItemLayout.addComponent(customLayout);
	
		txtTotalBalance.setValue(AmountUtils.format(0d));
	}
	
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			saveLockSplit();
		} else if (event.getButton() == btnCancel) {
			close();
		} else if (event.getButton() == btnDelete) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {lockSplit.getReference()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -3602954328220480537L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						LCK_SPL_SRV.deleteLockSplit(lockSplit);
						getNotificationDesc("item.deleted.successfully");
						close();
					}
				}
			});
		} 
	}
	
	/**
	 * 
	 * @param description
	 * @return
	 */
	private Notification getNotificationDesc(String description) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ lockSplit.getReference() }));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		return notification;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(130, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * Save Lock Split
	 */
	private void saveLockSplit() {
		lockSplit.setFrom(dfPaymentDate.getValue());
		lockSplit.setTo(dfExpiryDate.getValue());
		lockSplit.setTotalAmount(totalAmountOfPayment);
		getLockSplitItemValue(lockSplit);
		LCK_SPL_SRV.saveLockSplit(lockSplit);
		close();
	}
	
	/**
	 * 
	 */
	private void lockSplitItemClear() {
		cbxLockSplitItems.clear();
		txtBalances.clear();
		txtAmounts.clear();
		txtPrioritys.clear();
	}
	
	/**
	 * Get Lock Split Item Value
	 */
	private void getLockSplitItemValue(LockSplit lockSplit) {
		int index = 0;
		for (LockSplitItem lockSplitItem : lockSplit.getItems()) {
			if (lockSplitItem != null) {
				lockSplitItem.setLockSplit(lockSplit);
				lockSplitItem.setJournalEvent(cbxLockSplitItems.get(index).getSelectedEntity());
				lockSplitItem.setTiAmount(MyNumberUtils.getDouble(txtAmounts.get(index).getValue(), 0));
				lockSplitItem.setPriority(MyNumberUtils.getInteger(txtPrioritys.get(index).getValue(), 0));
				index++;
			}
		}
	}

	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		cashflowLockSplitPanel.refresh();
	}
	
}
