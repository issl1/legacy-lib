package com.nokor.efinance.gui.ui.panel.contract.expenses;


import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class ExpensesWindowFormPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -5903704796948896035L;

	private AutoDateField dfRequestedDate;
	private TextField txtRequestedBy;
	private TextField txtReference;
	private TextField txtAmount;
	private TextField txtWH;
	private TextField txtVat;
	private VerticalLayout messagePanel;
	
	/**
	 * 
	 * @param expensesTablePanel
	 * @param payment
	 */
	public ExpensesWindowFormPanel(ExpensesTablePanel expensesTablePanel, Payment payment) {
		setModal(true);
		setResizable(false);
		setCaption(I18N.message("payment"));
		
		dfRequestedDate = ComponentFactory.getAutoDateField();
		
		txtRequestedBy = ComponentFactory.getTextField(30, 150);
		txtReference = ComponentFactory.getTextField(30, 150);
		txtAmount = ComponentFactory.getTextField(30, 150);
		txtWH = ComponentFactory.getTextField(30, 150);
		txtVat = ComponentFactory.getTextField(30, 150);
		
		dfRequestedDate.setEnabled(false);
		txtRequestedBy.setEnabled(false);
		txtReference.setEnabled(false);
		
		if (payment.getId() != null) {
			dfRequestedDate.setValue(payment.getCreateDate());
			txtRequestedBy.setValue(payment.getCreateUser());
			txtAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
			txtWH.setValue(AmountUtils.format(MyNumberUtils.getDouble(0d)));
			txtVat.setValue(AmountUtils.format(MyNumberUtils.getDouble(payment.getVatPaidAmount())));
		}
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = -3425042672709710758L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
        		close();
        		if (validate()) {
            		try {
//            			ENTITY_SRV.saveOrUpdate(payment);
//                		ComponentLayoutFactory.displaySuccessfullyMsg();
//                		close();
//                		expensesTablePanel.assignValues(payment.getContract());
					} catch (Exception e) {
						e.printStackTrace();
						ValidateUtil.addError(I18N.message("msg.error.technical"));
						displayAllErrorsPanel();
					}
        		} else {
        			displayAllErrorsPanel();
        		}
        	}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -310559510180935722L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.BAN);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		GridLayout gridLayout = new GridLayout(2, 6);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		Label lblRequestedDate = ComponentFactory.getLabel("requested.date");
		Label lblRequestedBy = ComponentFactory.getLabel("requested.by");
		Label lblReference = ComponentFactory.getLabel("reference");
		Label lblAmount = ComponentFactory.getLabel("amount");
		Label lblWH = ComponentFactory.getLabel("wh");
		Label lblVat = ComponentFactory.getLabel("vat");
		gridLayout.addComponent(lblRequestedDate);
		gridLayout.addComponent(dfRequestedDate);
		gridLayout.addComponent(lblRequestedBy);
		gridLayout.addComponent(txtRequestedBy);
		gridLayout.addComponent(lblReference);
		gridLayout.addComponent(txtReference);
		gridLayout.addComponent(lblAmount);
		gridLayout.addComponent(txtAmount);
		gridLayout.addComponent(lblWH);
		gridLayout.addComponent(txtWH);
		gridLayout.addComponent(lblVat);
		gridLayout.addComponent(txtVat);
		
		gridLayout.setComponentAlignment(lblRequestedDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblRequestedBy, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblReference, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAmount, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblWH, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblVat, Alignment.MIDDLE_LEFT);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(gridLayout);
        
        setContent(contentLayout);
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		ValidateUtil.clearErrors();
		ValidateUtil.checkDoubleField(txtAmount, "amount");
		ValidateUtil.checkDoubleField(txtWH, "wh");
		ValidateUtil.checkDoubleField(txtVat, "vat");
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
}
