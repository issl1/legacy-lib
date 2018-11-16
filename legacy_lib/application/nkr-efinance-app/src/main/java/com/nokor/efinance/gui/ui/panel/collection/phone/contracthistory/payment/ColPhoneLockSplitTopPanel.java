package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.gui.ui.panel.payment.ARPaymentPopupWindowPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLockSplitTopPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 4767650447359604720L;
	
	private Button btnImportPayment;
	private Button btnRecordPayment;
	private Button btnNewLockSplit;
	
	private Contract contract;
	private ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitTopPanel(ColPhoneLockSplitTabPanel colPhoneLockSplitTabPanel) {
		this.colPhoneLockSplitTabPanel = colPhoneLockSplitTabPanel;
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		btnImportPayment = new NativeButton(I18N.message("import.payment"), this);
		btnImportPayment.setStyleName("btn btn-success button-small");
		btnImportPayment.setWidth(100, Unit.PIXELS);
		
		btnRecordPayment = new NativeButton(I18N.message("record.payment"), this);
		btnRecordPayment.setStyleName("btn btn-success button-small");
		btnRecordPayment.setWidth(100, Unit.PIXELS);
		
		btnNewLockSplit = new NativeButton(I18N.message("new.locksplit"), this);
		btnNewLockSplit.setStyleName("btn btn-success button-small");
		btnNewLockSplit.setWidth(100, Unit.PIXELS);
		
		GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(btnImportPayment, 0, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 0);
		gridLayout.addComponent(btnRecordPayment, 2, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 0);
		gridLayout.addComponent(btnNewLockSplit, 4, 0);
		
		addComponent(gridLayout);
		
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
	}

	/** 
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnNewLockSplit) {
			Window popupWindow = new Window(I18N.message("new.locksplit"));
			popupWindow.setModal(true);
			
			
			ColLockSplitPaymentPanel colLockSplitPaymentPanel = new ColLockSplitPaymentPanel(popupWindow, colPhoneLockSplitTabPanel);
			LockSplit lockSplit = LockSplit.createInstance();
			lockSplit.setContract(contract);
			colLockSplitPaymentPanel.assignValues(lockSplit);
			popupWindow.setContent(colLockSplitPaymentPanel);
			
			UI.getCurrent().addWindow(popupWindow);
			//NewLockSplitPopupPanel newLockSplitPopupPanel = new NewLockSplitPopupPanel();
			//UI.getCurrent().addWindow(newLockSplitPopupPanel);
		} else if (event.getButton() == btnRecordPayment) {
			ARPaymentPopupWindowPanel window = new ARPaymentPopupWindowPanel(null, null, null, null, true);
			String conRef = this.contract == null ? StringUtils.EMPTY : this.contract.getReference();
			window.assignValues(null);
			window.setLockContractIdControl(conRef);
			UI.getCurrent().addWindow(window);
//			RecordPaymentPopupPanel recordPaymentPopupPanel = new RecordPaymentPopupPanel();
//			UI.getCurrent().addWindow(recordPaymentPopupPanel);
		}
		
	}
}
