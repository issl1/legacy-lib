package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitTablePanel extends AbstractControlPanel implements MPayment, FinServicesHelper, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = -4668948044483562113L;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private Label lblCreateDateValue;
	private Label lblValidFromValue;
	private Label lblValidToValue;
	private Label lblTotalValue;
	private Label lblStatusValue;
	private Label lblSendEmailValue;
	private Label lblSendSMSValue;
	private Label lblPromiseValue;
	
	private Panel detailLockSplitPanel;
	private DetailLockSplitItemTable lockSplitItemTable;
	
	private ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel;
	
	/**
	 * 
	 * @param colPhoneLockSplitTabPanel
	 */
	public ColPhoneLockSplitTablePanel(ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel) {
		this.colPhoneLockSplitTabPanel = colPhoneLockSplitTabPanel;
		init();
	}
	
	/**
	 * 
	 * @param isPaymentCheque
	 */
	public ColPhoneLockSplitTablePanel(boolean isPaymentCheque) {
		init();
		if (isPaymentCheque) {
			simpleTable.removeItemClickListener(this);
			detailLockSplitPanel.setVisible(false);
		}
	}
	
	/**
	 * init
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		simpleTable.addItemClickListener(this);
		setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		detailLockSplitPanel = getLockSplitDetailLayout();
		
		mainLayout.addComponent(simpleTable);
		mainLayout.addComponent(detailLockSplitPanel);
		addComponent(mainLayout);
	}

	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(LockSplit.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(VALIDFROM, I18N.message("valid.from"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(VALIDTO, I18N.message("valid.to"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TOTAL, I18N.message("total"), String.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<LockSplit> lockSplits) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		for (LockSplit lockSplit : lockSplits) {
			Item item = indexedContainer.addItem(lockSplit.getId());
			item.getItemProperty(ID).setValue(lockSplit.getId());
			item.getItemProperty(LockSplit.REFERENCE).setValue(lockSplit.getReference());
			item.getItemProperty(CREATEDATE).setValue(lockSplit.getCreateDate());
			item.getItemProperty(VALIDFROM).setValue(lockSplit.getFrom());
			item.getItemProperty(VALIDTO).setValue(lockSplit.getTo());
			item.getItemProperty(TOTAL).setValue(AmountUtils.format(lockSplit.getTotalAmount()));
			item.getItemProperty(STATUS).setValue(lockSplit.getWkfStatus() != null ? lockSplit.getWkfStatus().getDescLocale() : "");
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setContractID(contract.getReference());
		restrictions.addCriterion(Restrictions.eq(LockSplitItem.WKFSTATUS, LockSplitWkfStatus.LNEW));
		restrictions.addOrder(Order.desc(LockSplit.ID));
		List<LockSplit> lockSplits = LCK_SPL_SRV.list(restrictions);
		detailLockSplitPanel.setVisible(false);
		setTableIndexedContainer(lockSplits);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	private void assignDetailLockSplitValue(LockSplit lockSplit) {
		this.reset();
		if (lockSplit != null) {
			detailLockSplitPanel.setVisible(true);
			lblCreateDateValue.setValue(getDescription(DateUtils.getDateLabel(lockSplit.getCreateDate())));
			lblValidFromValue.setValue(getDescription(DateUtils.getDateLabel(lockSplit.getFrom())));
			lblValidToValue.setValue(getDescription(DateUtils.getDateLabel(lockSplit.getTo())));
			lblTotalValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(lockSplit.getTotalAmount()), "###,##0.00")));
			lblStatusValue.setValue(getDescription(lockSplit.getWkfStatus() != null ? lockSplit.getWkfStatus().getDescLocale() : StringUtils.EMPTY));
			if (lockSplit.isSendEmail()) {
				lblSendEmailValue.setIcon(FontAwesome.CHECK);
			}
			if (lockSplit.isSendSms()) {
				lblSendSMSValue.setIcon(FontAwesome.CHECK);
			}
			if (lockSplit.isPromise()) {
				lblPromiseValue.setIcon(FontAwesome.CHECK);
			}
			lockSplitItemTable.assignValues(lockSplit.getItems());
		}
	}
	
	/**
	 * 
	 * @return gridLayout
	 */
	private Panel getLockSplitDetailLayout() {
		lblCreateDateValue = getLabelValue();
		lblValidFromValue = getLabelValue();
		lblValidToValue = getLabelValue();
		lblTotalValue = getLabelValue();
		lblStatusValue = getLabelValue();
		lblSendEmailValue = getLabelValue();
		lblSendSMSValue = getLabelValue();
		lblPromiseValue = getLabelValue();
		
		Label lblCreateDateTitle = getLabel("create.date");
		Label lblValidFromTitle = getLabel("valid.from");
		Label lblValidToTitle = getLabel("valid.to");
		Label lblTotalTitle = getLabel("total");
		Label lblStatusTitle = getLabel("status");
		Label lblSendEmailTitle = getLabel("send.email");
		Label lblSendSMSTitle = getLabel("send.sms");
		Label lblPromiseTitle = getLabel("promise");
		
		GridLayout gridLayout = new GridLayout(8, 2);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(lblCreateDateTitle, iCol++, 0);
		gridLayout.addComponent(lblCreateDateValue, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblValidFromTitle, iCol++, 0);
		gridLayout.addComponent(lblValidFromValue, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblValidToTitle, iCol++, 0);
		gridLayout.addComponent(lblValidToValue, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblTotalTitle, iCol++, 1);
		gridLayout.addComponent(lblTotalValue, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblStatusTitle, iCol++, 1);
		gridLayout.addComponent(lblStatusValue, iCol++, 1);
		
		lockSplitItemTable = new DetailLockSplitItemTable();
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(lblSendEmailTitle);
		horLayout.addComponent(lblSendEmailValue);
		horLayout.addComponent(lblSendSMSTitle);
		horLayout.addComponent(lblSendSMSValue);
		horLayout.addComponent(lblPromiseTitle);
		horLayout.addComponent(lblPromiseValue);
		
		HorizontalLayout detailHorLayout = new HorizontalLayout();
		detailHorLayout.setSpacing(true);
		detailHorLayout.addComponent(gridLayout);
		detailHorLayout.addComponent(horLayout);
		
		VerticalLayout vLayout = new VerticalLayout();	
		vLayout.setMargin(new MarginInfo(true, false, false, false));
		vLayout.addComponent(detailHorLayout);
		vLayout.addComponent(lockSplitItemTable);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("detail.of.locksplit"));
		fieldSet.setContent(vLayout);
		
		Panel detailLockSplitPanel = new Panel();
		detailLockSplitPanel.setStyleName(Reindeer.PANEL_LIGHT);
		detailLockSplitPanel.setContent(fieldSet);
		
		return detailLockSplitPanel;
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		lblCreateDateValue.setValue(StringUtils.EMPTY);
		lblValidFromValue.setValue(StringUtils.EMPTY);
		lblValidToValue.setValue(StringUtils.EMPTY);
		lblTotalValue.setValue(StringUtils.EMPTY);
		lblStatusValue.setValue(StringUtils.EMPTY);
		lblSendEmailValue.setIcon(FontAwesome.TIMES);
		lblSendSMSValue.setIcon(FontAwesome.TIMES);
		lblPromiseValue.setIcon(FontAwesome.TIMES);
		lockSplitItemTable.reset();
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		LockSplit lockSplit = LCK_SPL_SRV.getById(LockSplit.class, getItemSelectedId());
		assignDetailLockSplitValue(lockSplit);
		if (event.isDoubleClick()) {
			Window popupWindow = new Window(I18N.message("update.locksplit", new String[] { lockSplit.getReference()}));
			popupWindow.setModal(true);
			ColLockSplitPaymentPanel colLockSplitPaymentPanel = new ColLockSplitPaymentPanel(popupWindow, colPhoneLockSplitTabPanel);
			colLockSplitPaymentPanel.assignValues(lockSplit);
			popupWindow.setContent(colLockSplitPaymentPanel);
			UI.getCurrent().addWindow(popupWindow);
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
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
}
