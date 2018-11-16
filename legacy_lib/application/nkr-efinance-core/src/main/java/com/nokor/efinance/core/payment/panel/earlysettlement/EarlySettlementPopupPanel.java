package com.nokor.efinance.core.payment.panel.earlysettlement;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementValidateRequest;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.ManualAutoPaymentMethodComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 * @author buntha.chea (modified on combo box payment method)
 */
public class EarlySettlementPopupPanel extends AbstractTabPanel implements FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	private EarlySettlementService earlySettlementService = SpringUtils.getBean(EarlySettlementService.class);
	
	private Button btnPaid;
	private DealerComboBox cbxDealer;
	private TextField txtApplicant;
	private TextField txtTotalOustanding;
	private TextField txtTotalInterest;
	private TextField txtInsuranceFee;
	private TextField txtServicingFee;
	private TextField txtTotalPenalty;
	private TextField txtTotalOther;
	private TextField txtTotalAmount;
	
	//private EntityRefComboBox<PaymentMethod> cbxPaymentMethod;
	private ManualAutoPaymentMethodComboBox cbxPaymentMethod;
	private Contract contract;
	private ClickListener allocateListener;
	private EarlySettlementSimulateResponse earlySettlementSimulateResponse;
	
	/** */
	public EarlySettlementPopupPanel() {
		super();
		setMargin(false);
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(9, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
        cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		cbxDealer.setRequired(true);
		
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		txtTotalOustanding = ComponentFactory.getTextField(100, 150);
		txtTotalOustanding.setEnabled(false);
		txtTotalInterest = ComponentFactory.getTextField(100, 150);
		txtTotalInterest.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(100, 150);
		txtInsuranceFee.setEnabled(false);
		txtServicingFee = ComponentFactory.getTextField(100, 150);
		txtServicingFee.setEnabled(false);
		txtTotalPenalty = ComponentFactory.getTextField(100, 150);
		txtTotalPenalty.setEnabled(false);
		txtTotalOther = ComponentFactory.getTextField(100, 150);
		txtTotalOther.setEnabled(false);
		txtTotalAmount = ComponentFactory.getTextField(100, 150);
		txtTotalAmount.setEnabled(false);
		
		//cbxPaymentMethod = new EntityRefComboBox<PaymentMethod>();
		//cbxPaymentMethod.setRestrictions(new BaseRestrictions<PaymentMethod>(PaymentMethod.class));
		cbxPaymentMethod = new ManualAutoPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.setWidth("150px");
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 0);
        gridLayout.addComponent(txtApplicant, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("total.principal")), iCol++, 1);
        gridLayout.addComponent(txtTotalOustanding, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("total.interest")), iCol++, 1);
        gridLayout.addComponent(txtTotalInterest, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 2);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("total.penalty")), iCol++, 2);
        gridLayout.addComponent(txtTotalPenalty, iCol++, 2);

        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 3);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 3);
        gridLayout.addComponent(txtServicingFee, iCol++, 3);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("total.other")), iCol++, 4);
        gridLayout.addComponent(txtTotalOther, iCol++, 4);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 4);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 4);
        gridLayout.addComponent(txtTotalAmount, iCol++, 4);
        
		        
        btnPaid = new Button(I18N.message("paid"));
        btnPaid.setDisableOnClick(true);
        btnPaid.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
        btnPaid.setVisible(!ProfileUtil.isCollectionController() && !ProfileUtil.isCollectionSupervisor());
        btnPaid.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
            	if (isValid()) {
					if (getDouble(txtTotalAmount) == 0d) {
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("this.contract.ispaid.off"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					} else {
	    				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.receive.paid.off.payment", txtApplicant.getValue()),
	    					new ConfirmDialog.Listener() {
	    						private static final long serialVersionUID = 2380193173874927880L;
	    						public void onClose(ConfirmDialog dialog) {
    				                if (dialog.isConfirmed()) {
    				                	EarlySettlementValidateRequest request = new EarlySettlementValidateRequest();
    				                	request.setCotraId(earlySettlementSimulateResponse.getCotraId());
    				                	request.setEarlySettlementDate(earlySettlementSimulateResponse.getEventDate());   				                	
    				                	request.setBalanceCapital(earlySettlementSimulateResponse.getBalanceCapital());
    				                	request.setBalanceInterest(earlySettlementSimulateResponse.getBalanceInterest());
    				                	request.setBalanceCollectionFee(earlySettlementSimulateResponse.getBalanceCollectionFee());
    				                	request.setBalanceFollowingFee(earlySettlementSimulateResponse.getBalanceFollowingFee());
    				                	request.setBalanceOperationFee(earlySettlementSimulateResponse.getBalanceOperationFee());
    				                	request.setBalancePenalty(earlySettlementSimulateResponse.getBalancePenalty());
    				                	request.setBalancePressingFee(earlySettlementSimulateResponse.getBalancePressingFee());
    				                	request.setBalanceRepossessionFee(earlySettlementSimulateResponse.getBalanceRepossessionFee());
    				                	request.setBalanceTransferFee(earlySettlementSimulateResponse.getBalanceTransferFee());    	
    				                	
    				                	request.setAdjustmentInterest(earlySettlementSimulateResponse.getAdjustmentInterest());
    				                	request.setCashflows(earlySettlementSimulateResponse.getCashflows());
    				                	request.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
    				                	request.setDealer(cbxDealer.getSelectedEntity());
    				                   	earlySettlementService.validate(request);
    				                	allocateListener.buttonClick(null);
    				                } else {
    				                	btnPaid.setEnabled(true);
    				                }
	    						}
	    				    });
	    				confirmDialog.setWidth("450px");
	    				confirmDialog.setHeight("200px");
	    				confirmDialog.getOkButton().setDisableOnClick(true);
					}
            	}
			}
		});
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        btnLayout.addComponent(ComponentFactory.getSpaceLayout(220, Unit.PIXELS));
        btnLayout.addComponent(btnPaid);
        contentLayout.setSpacing(true);
        contentLayout.addComponent(gridLayout);
        contentLayout.addComponent(btnLayout);

        return contentLayout;
	}
	
	/**
	 * 
	 * @param cotraId
	 * @param earlySettlementDate
	 * @param includePenalty
	 */
	public void assignValues(Long cotraId, Date earlySettlementDate, boolean includePenalty) {
		if (cotraId != null) {
			contract = ENTITY_SRV.getById(Contract.class, cotraId);
			EarlySettlementSimulateRequest request = new EarlySettlementSimulateRequest();
			request.setCotraId(cotraId);
			request.setEventDate(earlySettlementDate);
			request.setIncludePenalty(includePenalty);
			earlySettlementSimulateResponse = earlySettlementService.simulate(request);
			
			SecUserDetail secUserDetail = (SecUserDetail) VaadinSession.getCurrent().getAttribute(SecUserDetail.NAME);
			if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
				cbxDealer.setSelectedEntity(secUserDetail.getDealer());
				cbxDealer.setEnabled(false);
			} else {
				cbxDealer.setSelectedEntity(contract.getDealer());
				cbxDealer.setEnabled(true);
			}
			txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
		}
	}
	
	/**
	 * @param allocateListener the allocateListener to set
	 */
	public void setAllocateListener(ClickListener allocateListener) {
		this.allocateListener = allocateListener;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		reset();
		checkMandatorySelectField(cbxPaymentMethod, "payment.method");
		checkMandatorySelectField(cbxDealer, "dealer");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
			btnPaid.setEnabled(true);
		}
		return errors.isEmpty();
	}
}
