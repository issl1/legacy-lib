package com.nokor.efinance.gui.ui.panel.contract.repossess;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.applicant.ApplicantsPanel;
import com.nokor.efinance.gui.ui.panel.contract.CashflowsPanel;
import com.nokor.efinance.gui.ui.panel.contract.ChangeStatusPopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.DetailPanel;
import com.nokor.efinance.gui.ui.panel.contract.OtherInfoPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * Contract re possess form panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContractRepossessFormPanel extends AbstractFormPanel {
	
	/** */
	private static final long serialVersionUID = -5011837298969453941L;

	@Autowired
	private ContractService contractService;
	
	private TabSheet accordionPanel;
	private CashflowsPanel cashflowsPanel;
	private Contract contract;
	private DetailPanel detailPanel;
	private OtherInfoPanel otherInfoPanel;
	private VerticalLayout riskPanel;
	private Panel infoPanel;
	public MenuBar afterSaleMenu;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		afterSaleMenu = new MenuBar();
		final MenuBar.MenuItem afterSaleEvent = afterSaleMenu.addItem(I18N.message("after.sale.event"), null);
		afterSaleEvent.addItem(I18N.message("repossessed"),new ChangeStatusContractCommand(ContractWkfStatus.REP));
		
		infoPanel = new Panel();
		accordionPanel = new TabSheet();
		detailPanel = new DetailPanel();
		cashflowsPanel = new CashflowsPanel();
		otherInfoPanel = new OtherInfoPanel();
		riskPanel = new VerticalLayout();
        accordionPanel.addTab(cashflowsPanel, I18N.message("cashflows"));
        accordionPanel.addTab(otherInfoPanel, I18N.message("other.info"));
        accordionPanel.addTab(riskPanel, I18N.message("risk"));
        contentLayout.addComponent(afterSaleMenu);
        contentLayout.addComponent(infoPanel);
        contentLayout.addComponent(accordionPanel);
		return contentLayout;
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long cotraId) {	
		contract = ENTITY_SRV.getById(Contract.class, cotraId);
		afterSaleMenu.setVisible(contract.getWkfStatus().equals(ContractWkfStatus.FIN));
		accordionPanel.setSelectedTab(detailPanel);
		detailPanel.assignValues(contract);
		otherInfoPanel.assignValues(contract);
		cashflowsPanel.assignValues(contract);
		cashflowsPanel.reset();
				
		displayContractInfo(contract);
	}
	
	/**
	 * @param contract
	 */
	private void displayContractInfo(final Contract contract) {
		Button btnApplicant = new Button(contract.getApplicant().getIndividual().getLastNameEn() + " " + contract.getApplicant().getIndividual().getFirstNameEn());
		btnApplicant.setStyleName(Runo.BUTTON_LINK);
		btnApplicant.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5451254851200324858L;
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + ApplicantsPanel.NAME + "/" + contract.getApplicant().getId());
			}
		});	
		
		String template = "contractInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		inputFieldLayout.setSizeFull();
		inputFieldLayout.addComponent(new Label(I18N.message("applicant")), "lblApplicant");
		inputFieldLayout.addComponent(btnApplicant, "lnkApplicant");
		inputFieldLayout.addComponent(new Label(I18N.message("reference")), "lblReference");
		inputFieldLayout.addComponent(new Label("<b>" + contract.getReference() + "</b>", ContentMode.HTML), "lblReferenceValue");
		inputFieldLayout.addComponent(new Label(I18N.message("startdate") ), "lblStartDate");
		inputFieldLayout.addComponent(new Label("<b>" + DateUtils.date2StringDDMMYYYY_SLASH(contract.getStartDate()) + "</b>", ContentMode.HTML), "lblStartDateValue");
		inputFieldLayout.addComponent(new Label(I18N.message("enddate")), "lblEndDate");
		inputFieldLayout.addComponent(new Label("<b>" + DateUtils.date2StringDDMMYYYY_SLASH(contract.getEndDate()) + "</b>", ContentMode.HTML), "lblEndDateValue");
		inputFieldLayout.addComponent(new Label(I18N.message("penalty.rule")), "lblPenaltyRule");
		inputFieldLayout.addComponent(new Label(contract.getPenaltyRule() != null ? contract.getPenaltyRule().getDescEn() : ""), "txtPenaltyRule");
        
		inputFieldLayout.addComponent(new Label(I18N.message("guarantor")), "lblGuarantor");
		if (contract.getGuarantor() != null) {
			Button btnGuarantor = new Button(contract.getGuarantor().getIndividual().getLastNameEn() + " " + contract.getGuarantor().getIndividual().getFirstNameEn());
			btnGuarantor.setStyleName(Runo.BUTTON_LINK);
			btnGuarantor.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -7808726057921125458L;
				@Override
				public void buttonClick(ClickEvent event) {
					Page.getCurrent().setUriFragment("!" + ApplicantsPanel.NAME + "/" + contract.getGuarantor().getId());
				}
			});
			inputFieldLayout.addComponent(btnGuarantor, "lnkGuarantor");
		} else {
			inputFieldLayout.addComponent(new Label("N/A"), "lnkGuarantor");
		}
		inputFieldLayout.addComponent(new Label(I18N.message("outstanding")), "lblOutstanding");
		inputFieldLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		inputFieldLayout.addComponent(new Label(contract.getWkfStatus().getDesc()), "txtStatus");
		
		Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
		inputFieldLayout.addComponent(new Label(AmountUtils.format(outstanding.getTiAmount())), "txtOutstanding");
		
        VerticalLayout vertical = new VerticalLayout();
        vertical.addComponent(inputFieldLayout);
        infoPanel.setContent(vertical);
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		accordionPanel.removeComponent(detailPanel);
		accordionPanel.removeComponent(cashflowsPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}
	
	/**
	 * @author uhout.cheng
	 */
	public class ChangeStatusContractCommand implements Command {
		private static final long serialVersionUID = -4210325376008120024L;
		private EWkfStatus contractStatus;
		public ChangeStatusContractCommand(EWkfStatus contractStatus) {
			this.contractStatus = contractStatus;
		}
		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(new ChangeStatusPopupPanel(contractStatus, contract));
		}
	}
}
