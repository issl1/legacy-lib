package com.nokor.efinance.gui.ui.panel.contract.dues.loan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
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
public class FeeWindowFormPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 6842731352450843917L;
	
	private ERefDataComboBox<EServiceType> cbxServiceType;
	private AutoDateField dfInstallmentDate;
	private TextField txtAmount;
	private VerticalLayout messagePanel;
	
	/**
	 * 
	 * @param loanFeePenaltyTablePanel
	 * @param contract
	 */
	public FeeWindowFormPanel(LoanFeePenaltyTablePanel loanFeePenaltyTablePanel, Contract contract) {
		setModal(true);
		setResizable(false);
		setCaption(I18N.message("add.fee"));
		
		List<EServiceType> serviceTypes = new ArrayList<EServiceType>();
		serviceTypes.add(EServiceType.FOLLWFEE);
		serviceTypes.add(EServiceType.COLFEE);
		serviceTypes.add(EServiceType.REPOSFEE);
		serviceTypes.add(EServiceType.OPERFEE);
		serviceTypes.add(EServiceType.TRANSFEE);
		serviceTypes.add(EServiceType.PRESSFEE);
		
		cbxServiceType = new ERefDataComboBox<EServiceType>(serviceTypes);
		cbxServiceType.setWidth(150, Unit.PIXELS);
		
		dfInstallmentDate = ComponentFactory.getAutoDateField();
		dfInstallmentDate.setValue(DateUtils.today());
		
		txtAmount = ComponentFactory.getTextField(30, 150);
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = -3052372123845661591L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
        		if (validate()) {
        			FinService service = ENTITY_SRV.getByCode(FinService.class, cbxServiceType.getSelectedEntity().getCode());
            		int nbInstallments = 1;
    				int nbMonths = 0;
    				if (service.getFrequency() != null) {
    					nbMonths = service.getFrequency().getNbMonths();
    					nbInstallments = MyNumberUtils.getInteger(contract.getTerm()) / nbMonths;
    				}
            		ProductLine proLine = contract.getProductLine();
            		Cashflow cashflowFee = CashflowUtils.createCashflow(proLine,
    						null, contract, 0d, ECashflowType.FEE, service.getTreasuryType(), service.getJournalEvent(),
    						proLine.getPaymentConditionFee(),
    						Double.parseDouble(txtAmount.getValue()), 0d, Double.parseDouble(txtAmount.getValue()),
    						dfInstallmentDate.getValue(), contract.getStartDate(), null, nbInstallments);
            		cashflowFee.setService(service);
            		try {
            			CASHFLOW_SRV.create(cashflowFee);
                		ComponentLayoutFactory.displaySuccessfullyMsg();
                		close();
                		loanFeePenaltyTablePanel.assignValues(contract);
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
			private static final long serialVersionUID = 2212974975311889822L;

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
		
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		Label lblServiceType = ComponentLayoutFactory.getLabelCaptionRequired("service.type");
		Label lblInstallmentDate = ComponentLayoutFactory.getLabelCaptionRequired("installment.date");
		Label lblAmount = ComponentLayoutFactory.getLabelCaptionRequired("amount");
		gridLayout.addComponent(lblServiceType);
		gridLayout.addComponent(cbxServiceType);
		gridLayout.addComponent(lblInstallmentDate);
		gridLayout.addComponent(dfInstallmentDate);
		gridLayout.addComponent(lblAmount);
		gridLayout.addComponent(txtAmount);
		
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
		ValidateUtil.checkMandatorySelectField(cbxServiceType, "service.type");
		ValidateUtil.checkMandatoryDateField(dfInstallmentDate, "installment.date");
		ValidateUtil.checkMandatoryField(txtAmount, "amount");
		ValidateUtil.checkDoubleField(txtAmount, "amount");
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
}
