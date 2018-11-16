package com.nokor.efinance.gui.ui.panel.payment;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Payment histories in AR
 * @author uhout.cheng
 */
public class PaymentSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -6223163592840947320L;
	
	private static final String ALL = I18N.message("all");
	private static final String SELECT = I18N.message("select");
	
	private TextField txtFullName;
	private TextField txtContractReference;
	private AutoDateField dfDOB;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ComboBox cbxDealer;
	private Button btnSelectDealer;
	private List<Long> dealerIds;
//	private EntityComboBox<Dealer> cbxDealer;
	
	/**
	 * 
	 * @param paymentTablePanel
	 */
	public PaymentSearchPanel(PaymentTablePanel paymentTablePanel) {
		super(I18N.message("search"), paymentTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealer.select(SELECT);
		txtContractReference.setValue("");
		txtFullName.setValue("");
		dfDOB.setValue(null);
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtFullName = ComponentFactory.getTextField(60, 150);	
//		cbxDealer = new EntityComboBox<Dealer>(Dealer.class, Dealer.NAMELOCALE);
//		cbxDealer.renderer(new BaseRestrictions<Dealer>(Dealer.class));
//		cbxDealer.setWidth(150, Unit.PIXELS);
		
		cbxDealer = ComponentFactory.getComboBox();
		cbxDealer.setImmediate(true);
		cbxDealer.setWidth(150, Unit.PIXELS);
		cbxDealer.setNullSelectionAllowed(false);
		cbxDealer.addItem(ALL);
		cbxDealer.addItem(SELECT);
		cbxDealer.select(SELECT);
		
		btnSelectDealer = ComponentLayoutFactory.getDefaultButton("select", null, 60);
		btnSelectDealer.setVisible(cbxDealer.isSelected(SELECT));
		btnSelectDealer.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -2118572539597646498L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				showDealersSelectPanel();
			}
		});
		
		cbxDealer.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 961916766603337615L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				btnSelectDealer.setVisible(false);
				if (SELECT.equals(cbxDealer.getValue())) {
					btnSelectDealer.setVisible(true);
				} else {
					btnSelectDealer.setVisible(false);
				}	
			}
		});
		
		txtContractReference = ComponentFactory.getTextField(60, 150);	
		dfDOB = ComponentFactory.getAutoDateField();
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField();
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
		
		Label lblFullName = ComponentFactory.getLabel("fullname");
		Label lblContractId = ComponentFactory.getLabel("contract.id");
		Label lblDealer = ComponentFactory.getLabel("dealer.ship.name");
		Label lblDOB = ComponentFactory.getLabel("dob");
		Label lblFrom = ComponentFactory.getLabel("payments.from");
		Label lblTo = ComponentFactory.getLabel("to");
		
		final GridLayout gridLayout = new GridLayout(9, 2);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(lblFullName, iCol++, 0);
        gridLayout.addComponent(txtFullName, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(lblContractId, iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(lblDealer, iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(btnSelectDealer, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(lblDOB, iCol++, 1);
        gridLayout.addComponent(dfDOB, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(lblFrom, iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(lblTo, iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        
        gridLayout.setComponentAlignment(lblFullName, Alignment.MIDDLE_RIGHT);
        gridLayout.setComponentAlignment(lblContractId, Alignment.MIDDLE_RIGHT);
        gridLayout.setComponentAlignment(lblDealer, Alignment.MIDDLE_RIGHT);
        gridLayout.setComponentAlignment(lblDOB, Alignment.MIDDLE_RIGHT);
        gridLayout.setComponentAlignment(lblFrom, Alignment.MIDDLE_RIGHT);
        gridLayout.setComponentAlignment(lblTo, Alignment.MIDDLE_RIGHT);
        
		return gridLayout;
	}
	
	/**
	 * Show Dealers select panel
	 */
	private void showDealersSelectPanel() {
		DealersSelectPanel usersSelectPanel = new DealersSelectPanel();
		usersSelectPanel.show(new DealersSelectPanel.Listener() {				
			
			/** */
			private static final long serialVersionUID = -3232325275561037936L;

			/**
			 * @see com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel)
			 */
			@Override
			public void onClose(DealersSelectPanel dialog) {
				dealerIds = dialog.getSelectedIds();
			}
		});
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		PaymentRestriction restrictions = new PaymentRestriction();
		restrictions.getWkfStatusList().add(PaymentWkfStatus.PAI);
		restrictions.setPaymentTypes(new EPaymentType[] { EPaymentType.IRC });
		restrictions.setContractReference(txtContractReference.getValue());
		restrictions.setPaymentFrom(dfStartDate.getValue());
		restrictions.setPaymentTo(dfStartDate.getValue());
		restrictions.setFullName(txtFullName.getValue());
		if (cbxDealer.isSelected(SELECT) && (dealerIds != null && !dealerIds.isEmpty())) {
			restrictions.addCriterion(Restrictions.in("dealer.id", dealerIds));
		}
		return restrictions;
	}
	
}
