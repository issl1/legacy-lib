package com.nokor.efinance.gui.ui.panel.loss;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LossPanel.NAME)
public class LossPanel extends AbstractTabPanel implements View, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	public static final String NAME = "loss.after.sale";

	private LossService lossService = SpringUtils.getBean(LossService.class);
	
	private TabSheet tabSheet;
	
	private Button btnLoss;
	private TextField txtApplicant;
	private TextField txtTotalPrincipal;
	private TextField txtTotalInterest;
	private TextField txtInsuranceFee;
	private TextField txtServicingFee;
	private TextField txtTotalOther;
	private TextField txtTotalAmount;
	
	private Contract contract;
	private LossSimulateResponse lossSimulateResponse;
	
	public LossPanel() {
		super();
		setMargin(false);
	}
		
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(9, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		txtTotalPrincipal = ComponentFactory.getTextField(100, 150);
		txtTotalPrincipal.setEnabled(false);
		txtTotalInterest = ComponentFactory.getTextField(100, 150);
		txtTotalInterest.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(100, 150);
		txtInsuranceFee.setEnabled(false);
		txtServicingFee = ComponentFactory.getTextField(100, 150);
		txtServicingFee.setEnabled(false);
		txtTotalOther = ComponentFactory.getTextField(100, 150);
		txtTotalOther.setEnabled(false);
		txtTotalAmount = ComponentFactory.getTextField(100, 150);
		txtTotalAmount.setEnabled(false);
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 0);
        gridLayout.addComponent(txtApplicant, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("total.principal")), iCol++, 1);
        gridLayout.addComponent(txtTotalPrincipal, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("total.interest")), iCol++, 1);
        gridLayout.addComponent(txtTotalInterest, iCol++, 1);        

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
        
		        
        btnLoss = new NativeButton(I18N.message("loss"));
        btnLoss.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
        btnLoss.addClickListener(new ClickListener() {		
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
	    				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.loss"),
	    					new ConfirmDialog.Listener() {
	    						private static final long serialVersionUID = 2380193173874927880L;
	    						public void onClose(ConfirmDialog dialog) {
    				                if (dialog.isConfirmed()) {
    				                	dialog.setModal(true);
    				                	LossValidateRequest request = new LossValidateRequest();
    				                	request.setCotraId(lossSimulateResponse.getCotraId());
    				                	request.setTotalInterest(lossSimulateResponse.getTotalInterest());
    				                	request.setTotalPrincipal(lossSimulateResponse.getTotalPrincipal());
    				                	request.setTotalOther(lossSimulateResponse.getTotalOther());
    				                	request.setInsuranceFee(lossSimulateResponse.getInsuranceFee());
    				                	request.setServicingFee(lossSimulateResponse.getServicingFee());
    				                	request.setCashflows(lossSimulateResponse.getCashflows());
    				                   	lossService.validate(request);
    				                   	Page.getCurrent().setUriFragment("!installments");
    				                }
	    						}
	    				    });
	    				confirmDialog.setWidth("400px");
	    				confirmDialog.setHeight("150px");
					}
            	}
			}
		});
        
        ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnLoss);
        
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
        contentLayout.addComponent(tblButtonsPanel);
        contentLayout.addComponent(gridLayout);
        tabSheet.addTab(contentLayout, I18N.message("loss"));
        
        return tabSheet;
	}
	
	/**
	 * 
	 * @param cotraId
	 * @param installmentDate
	 */
	public void assignValues(Long cotraId) {
		if (cotraId != null) {
			contract = ENTITY_SRV.getById(Contract.class, cotraId);
			LossSimulateRequest request = new LossSimulateRequest();
			request.setCotraId(cotraId);
			request.setEventDate(DateUtils.today());
			lossSimulateResponse = lossService.simulate(request);
			txtTotalPrincipal.setValue(AmountUtils.format(MyNumberUtils.getDouble(lossSimulateResponse.getTotalPrincipal().getTiAmount())));
			txtTotalInterest.setValue(AmountUtils.format(MyNumberUtils.getDouble(lossSimulateResponse.getTotalInterest().getTiAmount())));
			txtTotalOther.setValue(AmountUtils.format(MyNumberUtils.getDouble(lossSimulateResponse.getTotalOther().getTiAmount())));
			txtInsuranceFee.setValue(AmountUtils.format(MyNumberUtils.getDouble(lossSimulateResponse.getInsuranceFee().getTiAmount())));
			txtServicingFee.setValue(AmountUtils.format(MyNumberUtils.getDouble(lossSimulateResponse.getServicingFee().getTiAmount())));
			txtTotalAmount.setValue(AmountUtils.format(lossSimulateResponse.getTotalAmount().getTiAmount()));
			txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
		}
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		reset();
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		assignValues(new Long(cotraId));
	}
}
