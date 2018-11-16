package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Contract dues table panel in collection 
 * @author uhout.cheng
 */
public class ColContractDuesTablePanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -5183347172635797150L;

	private TreeTable treeTable;
	
	/**
	 * 
	 */
	public ColContractDuesTablePanel() {
		treeTable = new TreeTable();
		treeTable.setPageLength(10);
		treeTable.setSelectable(true);
		treeTable.setSizeFull();
		treeTable.setImmediate(true);
		treeTable.setColumnCollapsingAllowed(true);
		treeTable.setFooterVisible(true);
		
		setUpColumnDefinitions(treeTable);
		
		treeTable.setColumnFooter(Cashflow.CASHFLOWTYPE, I18N.message("total"));
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), false);
		verLayout.addComponent(treeTable);
		verLayout.setComponentAlignment(treeTable, Alignment.TOP_CENTER);
		
		Panel mainPanel = new Panel(verLayout);
		mainPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("dues") + "</h3>");
		mainPanel.setCaptionAsHtml(true);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(mainPanel);
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(Cashflow.CASHFLOWTYPE, I18N.message("item"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(Cashflow.TIINSTALLMENTAMOUNT, I18N.message("total.amount.pay"), String.class, Align.RIGHT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		setIndexedContainer(contract, 0, 0);
	}
	
	/**
	 * 
	 */
	public void reset() {
		treeTable.removeAllItems();
	}
	
	/**
	 * 
	 * @param contract
	 * @param parent
	 * @param index
	 */
	private void setIndexedContainer(Contract contract, int parent, int index) {
		List<LockSplitRecapVO> lockSplitRecapVOs = new ArrayList<LockSplitRecapVO>();

		lockSplitRecapVOs = LCK_SPL_SRV.getLockSplitRecapVOs(contract.getId(), null);
		int subParent = parent;
		double totalAmount = 0d;
		for (LockSplitRecapVO lockSplitRecapVO : lockSplitRecapVOs) {
			subParent = renderParentRow(lockSplitRecapVO.getDesc(), parent, index, 
					MyNumberUtils.formatDoubleToString(lockSplitRecapVO.getAmountToPay(), "###,##0.00"));
			index++;
			totalAmount += lockSplitRecapVO.getAmountToPay();
			if (lockSplitRecapVO.getSubLockSplitRecap() != null) {
				for (LockSplitRecapVO subLckRecap : lockSplitRecapVO.getSubLockSplitRecap()) {
					Item item = treeTable.addItem(index);
					treeTable.setParent(index, subParent);
					treeTable.setCollapsed(subParent, true);
					treeTable.setChildrenAllowed(index, false);
					index = renderRow(item, index, subLckRecap);
					index++;
				}
			} else {
				treeTable.setChildrenAllowed(subParent, false);
			}	
			parent = index;
		}
		treeTable.setColumnFooter(Cashflow.TIINSTALLMENTAMOUNT, MyNumberUtils.formatDoubleToString(totalAmount, "###,##0.00"));
	
	}
	
	/**
	 * 
	 * @param item
	 * @param index
	 * @param lockSplitRecapVO
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderRow(Item item, int index, LockSplitRecapVO lockSplitRecapVO) {
		if (lockSplitRecapVO != null) {
			item.getItemProperty(Cashflow.CASHFLOWTYPE).setValue(lockSplitRecapVO.getDesc());
			item.getItemProperty(Cashflow.TIINSTALLMENTAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
					lockSplitRecapVO.getAmountToPay(), "###,##0.00"));
		}
		return index;
	}
	
	/**
	 * 
	 * @param key
	 * @param parent
	 * @param index
	 * @param totalAmount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, int parent, int index, String totalAmount) {
		Item item = treeTable.addItem(index);
		
		treeTable.setParent(index, parent);
		treeTable.setCollapsed(parent, true);
		
		item.getItemProperty(Cashflow.CASHFLOWTYPE).setValue(key);
		item.getItemProperty(Cashflow.TIINSTALLMENTAMOUNT).setValue(totalAmount);
		return index;
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
