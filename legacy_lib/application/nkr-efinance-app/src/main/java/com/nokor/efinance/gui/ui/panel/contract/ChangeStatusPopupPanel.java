package com.nokor.efinance.gui.ui.panel.contract;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.auction.model.ContractAuctionData;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author vina.sok
 */
public class ChangeStatusPopupPanel extends Window {

	private static final long serialVersionUID = -3257822869747884791L;

	private LossService lossService = SpringUtils.getBean(LossService.class);
	
	private AutoDateField dfDate;
	private TextField txtApplicant;
	private TextField txtTotalPrincipal;
	private TextField txtTotalInterest;
	private TextField txtInsuranceFee;
	private TextField txtServicingFee;
	private TextField txtTotalOther;
	private TextField txtTotalAmount;
	
	private LossSimulateResponse lossSimulateResponse;
	
	/**
	 * @param contractStatus
	 * @param contract
	 */
	public ChangeStatusPopupPanel(final EWkfStatus contractStatus, final Contract contract) {
		setModal(true);		
		setCaption(contractStatus.getDesc());
		
		// TODO PYI
		ContractAuctionData cotraAuctionData = null;//contract.getContractAuctionData();
		
		dfDate = ComponentFactory.getAutoDateField();
		dfDate.setRequired(true);
		if(cotraAuctionData != null)
			dfDate.setValue(cotraAuctionData.getRequestRepossessedDate());
		else
			dfDate.setValue(DateUtils.today());
		dfDate.setImmediate(true);
		dfDate.setEnabled(false);
		
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
		
		final GridLayout gridLayout = new GridLayout(9, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("date")), iCol++, 0);
        gridLayout.addComponent(dfDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 0);
        gridLayout.addComponent(txtApplicant, iCol++, 0);
        
        
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
		        
        dfDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6373434239288655515L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				assignValues(contract, dfDate.getValue());
			}
		});
        
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
				
		Button btnSave = new NativeButton(I18N.message("validate"), new Button.ClickListener() {
			private static final long serialVersionUID = 7657693632881547084L;
			public void buttonClick(ClickEvent event) {
				if (dfDate.getValue() != null) {
					LossValidateRequest request = new LossValidateRequest();
					request.setCotraId(lossSimulateResponse.getCotraId());
					request.setWkfStatus(contractStatus);
                	request.setEventDate(lossSimulateResponse.getEventDate());
                	request.setTotalInterest(lossSimulateResponse.getTotalInterest());
                	request.setTotalPrincipal(lossSimulateResponse.getTotalPrincipal());
                	request.setTotalOther(lossSimulateResponse.getTotalOther());
                	request.setInsuranceFee(lossSimulateResponse.getInsuranceFee());
                	request.setServicingFee(lossSimulateResponse.getServicingFee());
                	request.setCashflows(lossSimulateResponse.getCashflows());
					lossService.validate(request);
					Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
					notification.setDescription(I18N.message("success.change.status.contract"));
					notification.setDelayMsec(3000);
					notification.show(Page.getCurrent());
					close();
				
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.select.date"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
            	close();
            }
		});
		
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(gridLayout);
		
		assignValues(contract, dfDate.getValue());
		
		setContent(contentLayout);
	}
	
	/**
	 * @param contract
	 * @param eventDate
	 */
	private void assignValues(Contract contract, Date eventDate) {
		LossSimulateRequest request = new LossSimulateRequest();
		request.setCotraId(contract.getId());
		request.setEventDate(eventDate);
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
