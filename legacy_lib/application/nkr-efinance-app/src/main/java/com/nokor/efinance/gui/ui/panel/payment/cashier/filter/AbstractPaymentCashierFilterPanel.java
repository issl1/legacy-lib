package com.nokor.efinance.gui.ui.panel.payment.cashier.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.cashier.PaymentCashierDetailTablePanel;
import com.nokor.efinance.gui.ui.panel.payment.cashier.filter.PaymentCashierFilterPopUpPanel.StoreControlPaymentCashierFilter;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public abstract class AbstractPaymentCashierFilterPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7520282491578981518L;
	
	private Button btnUpdateFilter;
	
	protected StoreControlPaymentCashierFilter storeControlFilter;

	private PaymentCashierDetailTablePanel tablePanel;

	private Label lblFromValue;
	private Label lblToValue;
	private Label lblPaymentMethodValue;
	
	/**
	 * @return the storeControlFilter
	 */
	public StoreControlPaymentCashierFilter getStoreControlFilter() {
		return storeControlFilter;
	}

	/**
	 * @param storeControlFilter the storeControlFilter to set
	 */
	public void setStoreControlFilter(StoreControlPaymentCashierFilter storeControlFilter) {
		this.storeControlFilter = storeControlFilter;
	}
	
	/**
	 * 
	 * @param tablePanel
	 */
	public AbstractPaymentCashierFilterPanel(PaymentCashierDetailTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		
		btnUpdateFilter = ComponentLayoutFactory.getDefaultButton("update", FontAwesome.EDIT, 70);
		btnUpdateFilter.addClickListener(this);
		
		Label lblPaymentDateFrom = ComponentFactory.getLabel(I18N.message("payment.date.from") + " : ");
		Label lblPaymentDateTo = ComponentFactory.getLabel(I18N.message("to") + " : ");
		Label lblPaymentMethod = ComponentFactory.getLabel(I18N.message("payment.method") + " : ");
	
		lblFromValue = ComponentFactory.getHtmlLabel(null);
		lblToValue = ComponentFactory.getHtmlLabel(null);
		lblPaymentMethodValue = ComponentFactory.getHtmlLabel(null);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(btnUpdateFilter);
		horLayout.addComponent(lblPaymentDateFrom);
		horLayout.addComponent(lblFromValue);
		horLayout.addComponent(lblPaymentDateTo);
		horLayout.addComponent(lblToValue);
		horLayout.addComponent(lblPaymentMethod);
		horLayout.addComponent(lblPaymentMethodValue);
		
		horLayout.setComponentAlignment(lblPaymentDateFrom, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblFromValue, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblPaymentDateTo, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblToValue, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblPaymentMethod, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblPaymentMethodValue, Alignment.MIDDLE_LEFT);
		
		Panel filterPanel = new Panel();
		filterPanel.setCaption("<b style=\"color:#3B5998\">" + I18N.message("current.filters") + "</b>");
		filterPanel.setContent(ComponentLayoutFactory.setMargin(horLayout));
		
		storeControlFilter = null;
		assignDefaultValue();
		
		addComponent(filterPanel);
	}
	
	/**
	 * 
	 */
	private void assignDefaultValue() {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		List<EPaymentMethod> paymentMethods = new ArrayList<EPaymentMethod>();
		paymentMethods.add(EPaymentMethod.CHEQUE);
		if (this.tablePanel.isPendingCheque()) {
			restrictions.setWkfStatuses(new EWkfStatus[] { PaymentFileWkfStatus.MATCHED });
			lblPaymentMethodValue.setValue("<b>" + displayPaymentMethodDesc(EPaymentMethod.CHEQUE.getCode()) + "</b>");
		} else {
			restrictions.addCriterion(Restrictions.ne(PaymentFileItem.WKFSTATUS, PaymentFileWkfStatus.ALLOCATED));
			paymentMethods.add(EPaymentMethod.CASH);
			lblPaymentMethodValue.setValue("<b>" + displayPaymentMethodDesc(EPaymentMethod.CASH.getCode()) + 
					" / " + displayPaymentMethodDesc(EPaymentMethod.CHEQUE.getCode()) + "</b>");
		}
		restrictions.setPaymentMethods(paymentMethods);
		tablePanel.refresh(restrictions);
	}
	
	/**
	 * 
	 * @param paymentMethodCode
	 * @return
	 */
	private String displayPaymentMethodDesc(String paymentMethodCode) {
		EPaymentMethod paymentMethod = ENTITY_SRV.getByCode(EPaymentMethod.class, paymentMethodCode);
		if (paymentMethod != null) {
			return paymentMethod.getDescLocale();
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param storeControl
	 */
	private void setNewResult(StoreControlPaymentCashierFilter storeControl) {
		reset();
		if (storeControl != null) {
			lblFromValue.setValue("<b>" + getDateFormat(storeControl.getFrom()) + "</b>");
			lblToValue.setValue("<b>" + getDateFormat(storeControl.getTo()) + "</b>");
			String paymentMethodDesc = storeControl.getPaymentMethod() != null ? storeControl.getPaymentMethod().getDescLocale() : StringUtils.EMPTY;
			if (StringUtils.isEmpty(paymentMethodDesc)) {
				lblPaymentMethodValue.setValue("<b>" + displayPaymentMethodDesc(EPaymentMethod.CASH.getCode()) + 
						" / " + displayPaymentMethodDesc(EPaymentMethod.CHEQUE.getCode()) + "</b>");
			} else {
				lblPaymentMethodValue.setValue("<b>" + paymentMethodDesc + "</b>");
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnUpdateFilter) {
			PaymentCashierFilterPopUpPanel window = PaymentCashierFilterPopUpPanel.show(
					storeControlFilter, tablePanel.isPendingCheque(), new PaymentCashierFilterPopUpPanel.Listener() {
				
				/** */
				private static final long serialVersionUID = -5797257244386440584L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.payment.cashier.filter.PaymentCashierFilterPopUpPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.payment.cashier.filter.PaymentCashierFilterPopUpPanel, com.nokor.efinance.gui.ui.panel.payment.cashier.filter.PaymentCashierFilterPopUpPanel.StoreControlPaymentCashierFilter)
				 */
				@Override
				public void onClose(PaymentCashierFilterPopUpPanel dialog, StoreControlPaymentCashierFilter storeControl) {
					storeControlFilter = storeControl;
					setNewResult(storeControl);
					tablePanel.refresh(getRestrictions());
				}
			});
			UI.getCurrent().addWindow(window);
		}
	}
	
	/**
	 * 
	 */
	private void reset() {
		lblFromValue.setValue(StringUtils.EMPTY);
		lblToValue.setValue(StringUtils.EMPTY);
		lblPaymentMethodValue.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract PaymentFileItemRestriction getRestrictions();

}
