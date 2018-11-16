package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.JournalEventComboBox;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;


/**
 * 
 * @author uhout.cheng
 */
public class LockSplitItemLayout extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4358032923701174458L;
	
	private String USD_FORMAT = "###,##0.00";
	
	private JournalEventComboBox cbxJournalEvent;
	private TextField txtLockSplitValue;
	private Button btnMoney;
	private Button btnRemove;
	private double totalAmount;
	private Map<JournalEvent, Double> mapItems;
	private LockSplitItem lockSplitItem; 
	
	/**
	 * 
	 * @param lckSplitPaymentPanel
	 * @param lockSplitItem
	 * @param mapItems
	 * @param index
	 */
	public LockSplitItemLayout(ColLockSplitPaymentPanel lckSplitPaymentPanel, LockSplitItem lockSplitItem, Map<JournalEvent, Double> mapItems, Integer index) {
		this.mapItems = mapItems;
		this.lockSplitItem = lockSplitItem;
		HorizontalLayout allocatToFormLayout = new HorizontalLayout();
		allocatToFormLayout.setSpacing(true);
		allocatToFormLayout.setMargin(new MarginInfo(true, true, false, false));
		allocatToFormLayout.setData(index);
		
		List<JournalEvent> events = new ArrayList<JournalEvent>();
		if (mapItems != null && !mapItems.isEmpty()) {
			for(JournalEvent journalEvent : mapItems.keySet()) {
				events.add(journalEvent);
			}
		}

		cbxJournalEvent = new JournalEventComboBox(events);
		cbxJournalEvent.setWidth(150, Unit.PIXELS);
		cbxJournalEvent.setImmediate(true);
		
		txtLockSplitValue = ComponentFactory.getTextField(30, 80);
		txtLockSplitValue.setImmediate(true);
		
		cbxJournalEvent.setSelectedEntity(lockSplitItem.getJournalEvent());
		txtLockSplitValue.setValue(AmountUtils.format(MyNumberUtils.getDouble(lockSplitItem.getTiAmount())));
		
		totalAmount += MyNumberUtils.getDouble(lockSplitItem.getTiAmount());
		
		btnMoney = ComponentLayoutFactory.getDefaultButton(null, FontAwesome.MONEY, 30);
		btnMoney.addStyleName("btn-icon-padding");
		btnMoney.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -2163308800321289846L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (cbxJournalEvent.getSelectedEntity() != null) {
					txtLockSplitValue.setValue(AmountUtils.format(MyNumberUtils.getDouble(getMapItems().get(cbxJournalEvent.getSelectedEntity()))));
				} else {
					txtLockSplitValue.setValue(AmountUtils.format(0d));
				}
				lckSplitPaymentPanel.displayTotalAmount();
			}
		});
		
		btnRemove = ComponentLayoutFactory.getButtonStyle(null, FontAwesome.TRASH_O, 30, "btn btn-danger button-small");
		btnRemove.addStyleName("btn-icon-padding");
		btnRemove.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -264864448818956748L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				allocatToFormLayout.removeAllComponents();
				removeComponent(allocatToFormLayout);
				lckSplitPaymentPanel.removeItemByIndexed((int) allocatToFormLayout.getData());
				lckSplitPaymentPanel.displayTotalAmount();
			}
		});
		
		cbxJournalEvent.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 6151501738938117707L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				lockSplitItem.setJournalEvent(cbxJournalEvent.getSelectedEntity());
			}
		});
		
		txtLockSplitValue.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 8199784290357776127L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				totalAmount = MyNumberUtils.getDouble(getDouble(txtLockSplitValue));
				lockSplitItem.setTiAmount(totalAmount);
				lckSplitPaymentPanel.displayTotalAmount();
			}
		});
		
		allocatToFormLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("receipt"));
		allocatToFormLayout.addComponent(cbxJournalEvent);
		allocatToFormLayout.addComponent(btnMoney);
		allocatToFormLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("amount"));
		allocatToFormLayout.addComponent(txtLockSplitValue);
		allocatToFormLayout.addComponent(btnRemove);
		addComponent(allocatToFormLayout);
	}
	
	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}
	
	/**
	 * @return the lockSplitItem
	 */
	public LockSplitItem getLockSplitItem() {
		return lockSplitItem;
	}

	/**
	 * @return the mapItems
	 */
	public Map<JournalEvent, Double> getMapItems() {
		return mapItems;
	}
	
	/**
	 * @param mapItems the mapItems to set
	 */
	public void setMapItems(Map<JournalEvent, Double> mapItems) {
		this.mapItems = mapItems;
	}

	/**
	 * @return the cbxJournalEvent
	 */
	public JournalEventComboBox getCbxJournalEvent() {
		return cbxJournalEvent;
	}

	/**
	 * 
	 * @return
	 */
	protected String validation() {
		if (cbxJournalEvent != null) {
			if (cbxJournalEvent.getSelectedEntity() != null) {
				double balance = 0d;
				if (cbxJournalEvent.getSelectedEntity() != null) {
					balance = MyNumberUtils.getDouble(mapItems.get(cbxJournalEvent.getSelectedEntity()));
				}
				if (StringUtils.isNotEmpty(txtLockSplitValue.getValue())) {
					ValidateUtil.checkDoubleField(txtLockSplitValue, "amount");
					double amount = MyNumberUtils.getDouble(getDouble(txtLockSplitValue));
					String desc = cbxJournalEvent.getSelectedEntity().getDescLocale();
					if (balance < amount) {
						ValidateUtil.addError(I18N.message("amount.cannot.higher.than.balance",
								new String[]{ desc + StringUtils.SPACE + MyNumberUtils.formatDoubleToString(amount, USD_FORMAT), 
								MyNumberUtils.formatDoubleToString(balance, USD_FORMAT) }));
					}
				} else {
					ValidateUtil.checkMandatoryField(txtLockSplitValue, "amount");
				}
			} else {
				ValidateUtil.checkMandatorySelectField(cbxJournalEvent, "receipt");
			}
		}
		return ValidateUtil.getErrorMessages();
	}
	
}
