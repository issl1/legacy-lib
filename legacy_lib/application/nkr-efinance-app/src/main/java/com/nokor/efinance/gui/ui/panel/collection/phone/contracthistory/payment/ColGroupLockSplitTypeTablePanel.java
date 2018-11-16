package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.collection.model.LockSplitTypeRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitUnPaidDetailVO;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;


/**
 * 
 * @author uhout.cheng
 */
public class ColGroupLockSplitTypeTablePanel extends TreeTable implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3465861858534696722L;

	/**
	 * 
	 */
	public ColGroupLockSplitTypeTablePanel() {
		setPageLength(3);
		setSelectable(true);
		setSizeFull();
		setImmediate(true);
		setColumnCollapsingAllowed(true);
		setUpColumnDefinitions(this);
		setFooterVisible(true);
		setColumnFooter(LockSplit.LOCKSPLITTYPE, I18N.message("total"));
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(LockSplit.LOCKSPLITTYPE, I18N.message("item"), String.class, Align.LEFT, 300));
		columnDefinitions.add(new ColumnDefinition(LockSplit.TIAMOUNT, I18N.message("amount.promised"), String.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIINSTALLMENTAMOUNT, I18N.message("amount.dues"), String.class, Align.RIGHT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(Contract contract) {
		this.removeAllItems();
		setIndexedContainer(contract, 0, 0);
	}
	
	/**
	 * 
	 * @param lockSplit
	 * @param parent
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(Contract contract, int parent, int index) {
		if (contract != null) {
			List<EWkfStatus> lckSplitStatues = new ArrayList<EWkfStatus>();
			lckSplitStatues.add(LockSplitWkfStatus.LNEW);
			List<LockSplit> lockSplits = LCK_SPL_SRV.getLockSplits(contract.getReference(), lckSplitStatues);
			List<Cashflow> cashflows = CASHFLOW_SRV.getCashflowsToPaidLessThanToday(contract.getId(), DateUtils.today());
			
			double totalAmountToPay = 0d;
			double totalAmountDue = 0d;
			
			if (lockSplits != null && !lockSplits.isEmpty()) {
				
				LockSplitUnPaidDetailVO unPaidDetailVO = new LockSplitUnPaidDetailVO(lockSplits, cashflows);
				
				Map<String, List<Double>> amtPromiseds = unPaidDetailVO.getMapAmountPromisedLckSplitTypes();
				Map<String, List<Double>> amtDues = unPaidDetailVO.getMapAmountDueLckSplitTypes();
				
				if (amtPromiseds == null) {
					amtPromiseds = new HashMap<String, List<Double>>();
				}
				if (amtDues == null) {
					amtDues = new HashMap<String, List<Double>>();
				}
				
				List<ELockSplitType> lockSplitTypes = getLockSplitTypes();
				if (lockSplitTypes != null && !lockSplitTypes.isEmpty()) {
					for (ELockSplitType lockSplitType : lockSplitTypes) {
						if (amtPromiseds.containsKey(lockSplitType) || amtDues.containsKey(lockSplitType)) {
							Item item = addItem(index);
					
							setParent(index, parent);
							setCollapsed(parent, true);
							setChildrenAllowed(index, false);
							
							List<Double> items = amtPromiseds.get(lockSplitType);
							double amountToPay = 0d;
							if (items != null && !items.isEmpty()) {
								for (Double amtPromised : items) {
									amountToPay += amtPromised;
								}
								totalAmountToPay += amountToPay;
							}
							
							List<Double> itemDues = amtDues.get(lockSplitType);
							double amountDue = 0d;
							if (itemDues != null && !itemDues.isEmpty()) {
								for (Double amtDue : itemDues) {
									amountDue += amtDue;
								}
								totalAmountDue += amountDue;
							}
							item.getItemProperty(LockSplit.LOCKSPLITTYPE).setValue(lockSplitType.getDescLocale());
							item.getItemProperty(LockSplit.TIAMOUNT).setValue(MyNumberUtils.formatDoubleToString(amountToPay, "###,##0.00"));
							item.getItemProperty(Cashflow.TIINSTALLMENTAMOUNT).setValue(MyNumberUtils.formatDoubleToString(amountDue, "###,##0.00"));
							index++;
						}
					}
				}
			}
			setColumnFooter(LockSplit.TIAMOUNT, MyNumberUtils.formatDoubleToString(totalAmountToPay, "###,##0.00"));
			setColumnFooter(Cashflow.TIINSTALLMENTAMOUNT, MyNumberUtils.formatDoubleToString(totalAmountDue, "###,##0.00"));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ELockSplitType> getLockSplitTypes() {
		LockSplitTypeRestriction restrictions = new LockSplitTypeRestriction();
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Set Up Column Definitions
	 * @param table
	 */
	private void setUpColumnDefinitions(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.removeContainerProperty(columnDefinition.getPropertyId());
		}
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.addContainerProperty(
				columnDefinition.getPropertyId(),
				columnDefinition.getPropertyType(),
				null,
				columnDefinition.getPropertyCaption(),
				null,
				columnDefinition.getPropertyAlignment());
			table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
		}
	}
	
}
