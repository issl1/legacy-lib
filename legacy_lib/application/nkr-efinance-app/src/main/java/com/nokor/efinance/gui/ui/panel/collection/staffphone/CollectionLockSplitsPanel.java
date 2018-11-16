package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.List;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit.CollectionLockSplitPopup;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Lock Splits panel in collection right panel
 * @author uhout.cheng
 */
public class CollectionLockSplitsPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2524356899918483007L;
	
	private CheckBox cbAfterSales;
	private CheckBox cbInstallments;
	private CheckBox cbFees;
	private AutoDateField dfDueDateFrom;
	private AutoDateField dfDueDateTo;
	private Button btnRefresh;
	
	private Button btnAdd;
	
	private CollectionLockSplitsTablePanel lockSplitsTablePanel;
	private Contract contract;
	
	/**
	 * 
	 */
	public CollectionLockSplitsPanel() {
		setWidth(525, Unit.PIXELS);
		init();
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private CheckBox getCheckBox(String caption) {
		CheckBox checkBox = new CheckBox(I18N.message(caption));
		return checkBox;
	}

	/**
	 * 
	 */
	private void init() {
		cbAfterSales = getCheckBox("after.sales");
		cbInstallments = getCheckBox("installments");
		cbFees = getCheckBox("fees");
		
		dfDueDateFrom = ComponentFactory.getAutoDateField();
		dfDueDateTo = ComponentFactory.getAutoDateField();
		
		btnRefresh = ComponentLayoutFactory.getButtonStyle("refresh", FontAwesome.REFRESH, 80, "btn btn-success button-small");
		btnRefresh.addClickListener(this);
		
		btnAdd = new Button();
		btnAdd.setStyleName(Reindeer.BUTTON_LINK);
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		
		lockSplitsTablePanel = new CollectionLockSplitsTablePanel(this);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		verLayout.addComponent(getTopLayout());
		verLayout.addComponent(btnAdd);
		verLayout.setComponentAlignment(btnAdd, Alignment.TOP_RIGHT);
		verLayout.addComponent(lockSplitsTablePanel);
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption(I18N.message("current.lock.split"));
		mainPanel.setContent(verLayout);
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getTopLayout() {
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(cbAfterSales);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		horLayout.addComponent(cbInstallments);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		horLayout.addComponent(cbFees);
		
		Label lblDueDateFromTitle = ComponentFactory.getLabel("due.date.from");
		Label lblDueDateToTitle = ComponentFactory.getLabel("due.date.to");
		
		GridLayout gridLayout = new GridLayout(10, 1);
		int iCol = 0;
		gridLayout.addComponent(lblDueDateFromTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfDueDateFrom, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDateToTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfDueDateTo, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(btnRefresh, iCol++, 0);
		
		gridLayout.setComponentAlignment(lblDueDateFromTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDueDateToTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verLayout.addComponent(horLayout);
		verLayout.addComponent(gridLayout);
		
		return verLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		lockSplitsTablePanel.assignValues(getLockSplits());
	}

	/**CollectionLockSplitPopup
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			LockSplit lockSplit = LockSplit.createInstance();
			lockSplit.setContract(this.contract);
			CollectionLockSplitPopup popup = new CollectionLockSplitPopup(this);
			popup.assignValues(lockSplit);
			UI.getCurrent().addWindow(popup);
		} else if (event.getButton().equals(btnRefresh)) {
			lockSplitsTablePanel.assignValues(getLockSplits());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<LockSplit> getLockSplits() {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setContractID(this.contract.getReference());
		restrictions.setAfterSales(cbAfterSales.getValue());
		restrictions.setFees(cbFees.getValue());
		restrictions.setInstallments(cbInstallments.getValue());
		restrictions.setDueDateFrom(dfDueDateFrom.getValue());
		restrictions.setDueDateTo(dfDueDateTo.getValue());
		return LCK_SPL_SRV.list(restrictions);
	}
	
}
