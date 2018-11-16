package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
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
 * @author Calvin
 */
public class RepossessedPopupPanel extends Window {

	private static final long serialVersionUID = -3257822869747884791L;
	
	private LossService lossService = SpringUtils.getBean(LossService.class);
	
	private AutoDateField dfDateRequestRepossessed;
	private TextField txtRepossessionFee;
	private TextField txtCollectionFee;   
	private Contract contract;
	private LossSimulateResponse lossSimulateResponse;
		
	/**
	 * @param contractStatus
	 * @param contract
	 */
	public RepossessedPopupPanel(final EWkfStatus contractStatus, final Contract contract) {
		setModal(true);		
		setCaption(contractStatus.getDesc());
		this.contract = contract;
		
		dfDateRequestRepossessed = ComponentFactory.getAutoDateField();
		dfDateRequestRepossessed.setRequired(true);
		dfDateRequestRepossessed.setValue(DateUtils.today());
		dfDateRequestRepossessed.setImmediate(true);
				
		txtRepossessionFee = ComponentFactory.getTextField(100, 150);
		txtRepossessionFee.setEnabled(true);
		txtCollectionFee = ComponentFactory.getTextField(100, 150);
		txtCollectionFee.setEnabled(true);
				
		final GridLayout gridLayout = new GridLayout(9, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("date.repossessed")), iCol++, 0);
        gridLayout.addComponent(dfDateRequestRepossessed, iCol++, 0);
		                    
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("repossession.fee")), iCol++, 1);
        gridLayout.addComponent(txtRepossessionFee, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("collection.fee")), iCol++, 2);
        gridLayout.addComponent(txtCollectionFee, iCol++, 2);        
		  
       	VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		Button btnSave = new NativeButton(I18N.message("validate"), new Button.ClickListener() {
			private static final long serialVersionUID = 7657693632881547084L;
			public void buttonClick(ClickEvent event) {
				LossValidateRequest request = new LossValidateRequest();
				
				if (dfDateRequestRepossessed.getValue() != null) {
					saveEntity();
					assignValues(contract, dfDateRequestRepossessed.getValue());
					request.setCotraId(contract.getId());
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
		assignValues(contract, dfDateRequestRepossessed.getValue());
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
	
		// TODO PYI
//		if (contract.getContractAuctionData() != null) {
//			ContractAuctionData contractAuctionData = contract.getContractAuctionData();
//			if(contractAuctionData.getRequestRepossessedDate() != null)
//				dfDateRequestRepossessed.setValue(contractAuctionData.getRequestRepossessedDate());
//				txtRepossessionFee.setValue(AmountUtils.format(contractAuctionData.getRepossessionFeeUsd()));
//				txtCollectionFee.setValue(AmountUtils.format(contractAuctionData.getCollectionFeeUsd()));	
//		}
	}

	protected Entity getEntity() {
		// TODO PYI
		ContractAuctionData contractAuctionData = null;//contract.getContractAuctionData();
		if (contractAuctionData == null) {
			contractAuctionData = new ContractAuctionData();
			contractAuctionData.setContract(contract);
		}
		
		contractAuctionData.setRequestRepossessedDate(dfDateRequestRepossessed.getValue());
		contractAuctionData.setRepossessionFeeUsd(getDataFromTextField(txtRepossessionFee.getValue()));
		contractAuctionData.setCollectionFeeUsd(getDataFromTextField(txtCollectionFee.getValue()));
		
		return contractAuctionData;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	public void saveEntity() {
		lossService.saveOrUpdate(getEntity());
	}
	
	public Double getDataFromTextField(String data){
		if(data == null || StringUtils.isEmpty(data)){
			return 0d;
		}
		return Double.valueOf(data.replaceAll(",", "").toString());
	}
}



