package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitRecapVO;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;


/**
 * Lock split table panel in collection 
 * @author uhout.cheng
 */
public class ColLockSplitRecapTablePanel extends TreeTable implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 9045606490690222887L;
	
	private LockSplit lockSplit;

	/**
	 * 
	 */
	public ColLockSplitRecapTablePanel() {
		setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("recap") + "</h3>");
		setCaptionAsHtml(true);
		setPageLength(10);
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
		columnDefinitions.add(new ColumnDefinition(LockSplit.LOCKSPLITTYPE, I18N.message("item"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(LockSplit.TIAMOUNT, I18N.message("total.amount.pay"), String.class, Align.RIGHT, 230));
		columnDefinitions.add(new ColumnDefinition(LockSplit.TOTALAMOUNT, I18N.message("amount.lock.split"), String.class, Align.RIGHT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		this.lockSplit = lockSplit;
		setIndexedContainer(lockSplit, 0, 0);
	}
	
	/**
	 * 
	 * @param lockSplit
	 * @param parent
	 * @param index
	 */
	private void setIndexedContainer(LockSplit lockSplit, int parent, int index) {
		List<LockSplitRecapVO> lockSplitRecapVOs = new ArrayList<LockSplitRecapVO>();
		if (lockSplit.getContract() != null) {
			lockSplitRecapVOs = LCK_SPL_SRV.getLockSplitRecapVOs(lockSplit.getContract().getId(), lockSplit);
			int subParent = parent;
			double totalAmount = 0d;
			for (LockSplitRecapVO lockSplitRecapVO : lockSplitRecapVOs) {
				subParent = renderParentRow(lockSplitRecapVO.getDesc(), parent, index, 
						getTotalAmountOutstanding(lockSplitRecapVO, index), 
						AmountUtils.format(lockSplitRecapVO.getInLockSplitAmount()));
				index++;
				totalAmount += lockSplitRecapVO.getAmountToPay();
				if (lockSplitRecapVO.getSubLockSplitRecap() != null) {
					for (LockSplitRecapVO subLckRecap : lockSplitRecapVO.getSubLockSplitRecap()) {
						Item item = addItem(index);
						setParent(index, subParent);
						setCollapsed(subParent, true);
						setChildrenAllowed(index, false);
						index = renderRow(item, index, subLckRecap);
						index++;
					}
				} else {
					setChildrenAllowed(subParent, false);
				}	
				parent = index;
			}
			setColumnFooter(LockSplit.TIAMOUNT, AmountUtils.format(totalAmount));
		}
	}

	/**
	 * 
	 * @param lockSplitRecapVO
	 * @param index
	 * @return
	 */
	private String getTotalAmountOutstanding(LockSplitRecapVO lockSplitRecapVO, int index) {
		double totalAmount = 0d;
		if (lockSplitRecapVO != null) {
			totalAmount = MyNumberUtils.getDouble(lockSplitRecapVO.getAmountToPay());
		}
		if (index == 0) {
			double outStandingAmt = 0d;
			Contract con = this.lockSplit.getContract();
			Collection col = null;
			if (con != null) {
				col = con.getCollection();
			}
			if (col != null) {
				outStandingAmt = MyNumberUtils.getDouble(col.getTiBalanceCapital()) + MyNumberUtils.getDouble(col.getTiBalanceInterest());
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append(AmountUtils.format(totalAmount));
			buffer.append(StringUtils.SPACE);
			buffer.append("(");
			buffer.append(I18N.message("total.outstanding"));
			buffer.append(StringUtils.SPACE);
			buffer.append("=");
			buffer.append(StringUtils.SPACE);
			buffer.append(AmountUtils.format(outStandingAmt));
			buffer.append(")");
			return buffer.toString();
		} else {
			return AmountUtils.format(totalAmount);
		}
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
			item.getItemProperty(LockSplit.LOCKSPLITTYPE).setValue(lockSplitRecapVO.getDesc());
			item.getItemProperty(LockSplit.TIAMOUNT).setValue(AmountUtils.format(lockSplitRecapVO.getAmountToPay()));
		}
		return index;
	}
	
	/**
	 * 
	 * @param key
	 * @param parent
	 * @param index
	 * @param totalAmount
	 * @param amountLockSplit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, int parent, int index, String totalAmount, String amountLockSplit) {
		Item item = addItem(index);
		
		setParent(index, parent);
		setCollapsed(parent, true);
		
		item.getItemProperty(LockSplit.LOCKSPLITTYPE).setValue(key);
		item.getItemProperty(LockSplit.TIAMOUNT).setValue(totalAmount);
		item.getItemProperty(LockSplit.TOTALAMOUNT).setValue(amountLockSplit);
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
