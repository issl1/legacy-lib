package com.nokor.efinance.gui.ui.panel.payment.cashier.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;


/**
 * 
 * @author uhout.cheng
 */
public class PaymentCashierFilterPopUpPanel extends Window implements ClickListener, CloseListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -984003476169463653L;

	private Button btnSave;
	private Button btnReset;
	private Button btnCancel;
	
	private AutoDateField dfPaymentDateFrom;
	private AutoDateField dfPaymentDateTo;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	
	private Listener listener = null;
	
	private boolean isPaymentCheque;
	
	/**
	 * 
	 * @author uhout.cheng
	 *
	 */
	public interface Listener extends Serializable {
        void onClose(PaymentCashierFilterPopUpPanel dialog, StoreControlPaymentCashierFilter storeControl);
    }
	
	/**
	 * 
	 * @param isPaymentCheque
	 */
	public PaymentCashierFilterPopUpPanel(boolean isPaymentCheque) {
		this.isPaymentCheque = isPaymentCheque;
		dfPaymentDateFrom = ComponentFactory.getAutoDateField();
		dfPaymentDateTo = ComponentFactory.getAutoDateField();
		List<EPaymentMethod> values = new ArrayList<>();
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CASH.getCode()));
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CHEQUE.getCode()));
		
		cbxPaymentMethod = new EntityRefComboBox<>(values);
		cbxPaymentMethod.setImmediate(true);
		cbxPaymentMethod.setWidth(150, Unit.PIXELS);
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnReset = new NativeButton(I18N.message("reset"), this);
		btnReset.setIcon(FontAwesome.ERASER);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		Button.ClickListener cb = new Button.ClickListener() {
	            
			/** */
			private static final long serialVersionUID = -3793333774569952487L;

			public void buttonClick(ClickEvent event) {
				if (listener != null) {
					StoreControlPaymentCashierFilter storeControl = new StoreControlPaymentCashierFilter(isPaymentCheque);
					listener.onClose(PaymentCashierFilterPopUpPanel.this, storeControl);
				}
				UI.getCurrent().removeWindow(PaymentCashierFilterPopUpPanel.this);
			}
		};
			
	    btnSave.addClickListener(cb);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnReset);
		navigationPanel.addButton(btnCancel);
		
		Label lblFrom = ComponentFactory.getLabel("payment.date.from");
		Label lblTo = ComponentFactory.getLabel("to");
		Label lblPaymentMethod = ComponentFactory.getLabel("payment.method");
		
		GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblFrom);
		gridLayout.addComponent(dfPaymentDateFrom);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		gridLayout.addComponent(lblTo);
		gridLayout.addComponent(dfPaymentDateTo);
		if (!isPaymentCheque) {
			gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
			gridLayout.addComponent(lblPaymentMethod);
			gridLayout.addComponent(cbxPaymentMethod);
			gridLayout.setComponentAlignment(lblPaymentMethod, Alignment.MIDDLE_LEFT);
		}
		
		gridLayout.setComponentAlignment(lblFrom, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblTo, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(gridLayout);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(ComponentLayoutFactory.setMargin(searchLayout));
		
		setModal(true);
		setCaptionAsHtml(true);
		setCaption(I18N.message("filters"));
		setContent(mainLayout);
	}
	
	/**
	 * 
	 * @param storeControl
	 * @param isPaymentCheque
	 * @param listener
	 * @return
	 */
	public static PaymentCashierFilterPopUpPanel show(StoreControlPaymentCashierFilter storeControl, boolean isPaymentCheque, final Listener listener) {   	
		PaymentCashierFilterPopUpPanel paymentFilterPopUpPanel = new PaymentCashierFilterPopUpPanel(isPaymentCheque);
	    paymentFilterPopUpPanel.listener = listener;
	    paymentFilterPopUpPanel.assignValue(storeControl);
	    return paymentFilterPopUpPanel;
	}
	
	/**
	 * 
	 */
	public void reset() {
		dfPaymentDateFrom.setValue(null);
		dfPaymentDateTo.setValue(null);
		cbxPaymentMethod.setSelectedEntity(null);
	}
	
	/**
	 * Assign Value to all control in filter
	 * @param storeControl
	 */
	public void assignValue(StoreControlPaymentCashierFilter storeControl) {
		reset();
		if (storeControl != null) {
			dfPaymentDateFrom.setValue(storeControl.getFrom());
			dfPaymentDateTo.setValue(storeControl.getTo());
			cbxPaymentMethod.setSelectedEntity(storeControl.getPaymentMethod());
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			new StoreControlPaymentCashierFilter(this.isPaymentCheque);
		} else if (event.getButton() == btnReset) {
			reset();
		} else if (event.getButton() == btnCancel) {
			close();
		}	
	}

	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	public class StoreControlPaymentCashierFilter implements Serializable {
		
		/** */
		private static final long serialVersionUID = -7643029629177103070L;
	
		private Date from;
		private Date to;
		private EPaymentMethod paymentMethod;
		
		/**
		 * 
		 */
		public StoreControlPaymentCashierFilter(boolean isPaymentCheque) {
			this.from = dfPaymentDateFrom.getValue();
			this.to = dfPaymentDateTo.getValue();
			if (!isPaymentCheque) {
				this.paymentMethod = cbxPaymentMethod.getSelectedEntity();
			} else {
				this.paymentMethod = ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CHEQUE.getCode());
			}
		}
		
		/**
		 * @return the from
		 */
		public Date getFrom() {
			return from;
		}
		
		/**
		 * @param from the from to set
		 */
		public void setFrom(Date from) {
			this.from = from;
		}
		
		/**
		 * @return the to
		 */
		public Date getTo() {
			return to;
		}
		
		/**
		 * @param to the to to set
		 */
		public void setTo(Date to) {
			this.to = to;
		}

		/**
		 * @return the paymentMethod
		 */
		public EPaymentMethod getPaymentMethod() {
			return paymentMethod;
		}

		/**
		 * @param paymentMethod the paymentMethod to set
		 */
		public void setPaymentMethod(EPaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
		}
		
	}
}
