package com.nokor.efinance.gui.ui.panel.contract;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.panel.ServicePanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.panel.popup.ContractClosePopupPanel;
import com.nokor.efinance.core.quotation.panel.popup.ContractEarlySettlementPopupPanel;
import com.nokor.efinance.core.quotation.panel.popup.ContractNewRepossessionPopupPanel;
import com.nokor.efinance.core.quotation.panel.popup.ContractRedemptionPopupPanel;
import com.nokor.efinance.core.quotation.panel.popup.ContractTerminatePopupPanel;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.isr.ISRTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.ColPhoneLegalFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.repo.ColReturnRepoTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.history.ColPhoneHistoriesPanel;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetPanel;
import com.nokor.efinance.gui.ui.panel.contract.cashflow.CashflowPanel;
import com.nokor.efinance.gui.ui.panel.contract.document.DocumentPanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanPanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.NotesPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.sms.SMSPopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.summary.SummaryPanel;
import com.nokor.efinance.gui.ui.panel.contract.summary.popup.SummaryFlagPopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.summary.popup.SummaryNotePopupPanel;
import com.nokor.efinance.gui.ui.panel.contract.tranfer.ContractTranferTabPanel;
import com.nokor.efinance.gui.ui.panel.contract.user.UsersPanel;
import com.nokor.efinance.gui.ui.panel.dashboard.BookContractsPanel;
import com.nokor.efinance.gui.ui.panel.dashboard.DashboardPanel;
import com.nokor.efinance.gui.ui.panel.dashboard.ScanContractsPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author buntha.chea
 */
public class ContractFormPanelOld1 extends AbstractFormPanel implements QuotationEntityField, ClickListener, FinServicesHelper, SelectedTabChangeListener {

	private static final long serialVersionUID = -3948949199018017119L;
		
	private Contract contract;
	private SummaryPanel summaryPanel;
	
	private TabSheet accordionPanel;
	private LoanSummaryPanel loanSummaryPanel; 
	private LoanPanel loanPanel;
	private NotesPanel notesPanel;
	private AssetPanel assetPanel;
//	private CollectionPanel collectionPanel;
	private UsersPanel usersPanel;
	private CashflowPanel cashflowPanel; 
	private DocumentPanel documentPanel;
	private ContractTranferTabPanel transferPanel;
		
	//private Button btnSetting;
	private Button btnNotes;
	private Button btnFlags;
	private Button btnSMS;
	private Button btnPrint;
	private MenuBar mnuAfterSales;
	
	private Button btnReceived;
	private Button btnBooked;
	
	private ColPhoneHistoriesPanel phoneHistoriesPanel;
	private ColPhoneLegalFormPanel phoneLegalFormPanel;
	private ISRTabPanel phoneISRTabPanel;
	private ColReturnRepoTabPanel colPhoneReturnRepoTabPanel;
	
	private Button btnRecapView;
	private Button btnMoveNext;
	private Button btnMovePrevious;
	
	private List<Long> contractIds;
	private Long previousId;
	private Long nextId;
	private Label lblCurrentIndexed;
	
	public ContractFormPanelOld1() {
		super.init();
	    setSizeFull();
	}
	
	/**
	 * @return the accordionPanel
	 */
	public TabSheet getAccordionPanel() {
		return accordionPanel;
	}
	
	/**
	 * @return the summaryPanel
	 */
	public SummaryPanel getSummaryPanel() {
		return summaryPanel;
	}

	/**
	 * @return the assetPanel
	 */
	public AssetPanel getAssetPanel() {
		return assetPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Contract getEntity() {		
		return contract;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		accordionPanel = new TabSheet();
		summaryPanel = new SummaryPanel();
		loanSummaryPanel = new LoanSummaryPanel();
		loanPanel = new LoanPanel();
		notesPanel = new NotesPanel();
		assetPanel = new AssetPanel();
		
		usersPanel = new UsersPanel(summaryPanel);
		cashflowPanel = new CashflowPanel();
		documentPanel = new DocumentPanel();
		transferPanel = new ContractTranferTabPanel();
		
		btnReceived = getButton("received", FontAwesome.CHECK_CIRCLE, 100);
		btnReceived.setVisible(false);
		btnBooked = getButton("booked", FontAwesome.BOOKMARK_O, 100);
		btnBooked.setVisible(false);
		phoneHistoriesPanel = new ColPhoneHistoriesPanel();
		phoneLegalFormPanel = new ColPhoneLegalFormPanel();
		phoneISRTabPanel = new ISRTabPanel();
		colPhoneReturnRepoTabPanel = new ColReturnRepoTabPanel();
		
		accordionPanel.addTab(loanSummaryPanel, I18N.message("summary"));
		accordionPanel.addTab(loanPanel, I18N.message("loan"));
		accordionPanel.addTab(usersPanel, I18N.message("users"));
		accordionPanel.addTab(assetPanel, I18N.message("asset"));
		accordionPanel.addTab(notesPanel, I18N.message("notes"));
        accordionPanel.addTab(documentPanel, I18N.message("document"));
        accordionPanel.addTab(cashflowPanel, I18N.message("cashflow"));
        accordionPanel.addTab(transferPanel, I18N.message("transfer"));
        accordionPanel.addTab(phoneHistoriesPanel, I18N.message("history"));
        
        accordionPanel.addTab(phoneLegalFormPanel, I18N.message("legal"));
        accordionPanel.addTab(phoneISRTabPanel, I18N.message("isr"));
        accordionPanel.addTab(colPhoneReturnRepoTabPanel, I18N.message("return.repo"));
        
       /* if (ProfileUtil.isCollection()) {
	        collectionPanel = new CollectionPanel();
	        accordionPanel.addTab(collectionPanel, I18N.message("collection"));
        }*/
        
        accordionPanel.addSelectedTabChangeListener(this);
                
        Panel feeAmountPanel = new Panel();
        feeAmountPanel.setContent(new ServicePanel());
        
        btnRecapView = new Button();
		btnRecapView.setStyleName(Reindeer.BUTTON_LINK);
		btnRecapView.addClickListener(this);
		btnRecapView.setCaption(I18N.message("recap.view"));
		btnRecapView.setVisible(ProfileUtil.isCallCenter() || ProfileUtil.isCollection());
		
		btnMoveNext = getButton(FontAwesome.FORWARD);
		btnMoveNext.addStyleName("btn-icon-padding");
		btnMovePrevious = getButton(FontAwesome.BACKWARD);
        
		lblCurrentIndexed = ComponentFactory.getLabel();
		lblCurrentIndexed.setCaptionAsHtml(true);
		
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(btnReceived);
        buttonLayout.addComponent(btnBooked);
        
        HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(false, true, false, false), true);
		horLayout.setSizeUndefined();
		horLayout.addComponent(new Panel(summaryPanel));
		horLayout.addComponent(buttonLayout);
		// horLayout.addComponent(getButtonsPanel());
		
		Panel topPanel = new Panel();
		topPanel.setStyleName(Reindeer.PANEL_LIGHT);
		topPanel.setContent(new VerticalLayout(horLayout));
		
		 Button btnGoInbox = new Button();
		 btnGoInbox.setCaption(I18N.message("go.inbox"));
		 btnGoInbox.setStyleName(Reindeer.BUTTON_LINK);
		 btnGoInbox.addClickListener(new ClickListener() {
			
			 /** */
			 private static final long serialVersionUID = 8580548804223281291L;

			 /**
			  * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			  */
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + DashboardPanel.NAME);
			}
		});
		btnGoInbox.setVisible(ProfileUtil.isCMProfile());
		
		HorizontalLayout topRightLayout = new HorizontalLayout();
	    topRightLayout.setSpacing(true);
	    topRightLayout.addComponent(btnRecapView);
	    topRightLayout.addComponent(btnGoInbox);
	    topRightLayout.addComponent(lblCurrentIndexed);
	    topRightLayout.addComponent(btnMovePrevious);
	    topRightLayout.addComponent(btnMoveNext);
	    topRightLayout.setComponentAlignment(btnRecapView, Alignment.MIDDLE_LEFT);
	    topRightLayout.setComponentAlignment(btnGoInbox, Alignment.MIDDLE_LEFT);
		
        VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(topRightLayout);
		contentLayout.setComponentAlignment(topRightLayout, Alignment.MIDDLE_RIGHT);
		contentLayout.addComponent(topPanel);
        contentLayout.addComponent(accordionPanel);
        
        if (ProfileUtil.isCollection()) {
        	contentLayout.addComponent(feeAmountPanel, 2);
        }
                
        return contentLayout;
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(Resource icon) {
		Button btn = ComponentLayoutFactory.getDefaultButton(null, icon, 40);
		btn.addClickListener(this);
		return btn;
	} 
	
	/**
	 * 
	 * @return
	 */
	protected Panel getButtonsPanel() {
		
        //btnSetting = getButton("setting", FontAwesome.COG, 80);
		btnNotes = getButton("notes", FontAwesome.COMMENT, 80);
		btnFlags = getButton("flags", FontAwesome.FLAG, 80);
		btnSMS = getButton("sms", FontAwesome.FONT, 80);
		btnPrint = getButton("print", FontAwesome.PRINT, 80);
		mnuAfterSales = getAfterSalesButtons();
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		//horLayout.addComponent(btnSetting);
		horLayout.addComponent(btnReceived);
		horLayout.addComponent(btnBooked);
		horLayout.addComponent(btnNotes);
		horLayout.addComponent(btnSMS);
		horLayout.addComponent(btnFlags);
		verLayout.addComponent(horLayout);
		
		horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(btnPrint);
		verLayout.addComponent(horLayout);
		
		horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(mnuAfterSales);
		verLayout.addComponent(horLayout);
		
		Panel panel = new Panel(verLayout);
		return panel;
	}
	
	/** 
	 * @param caption
	 * @param icon
	 * @param width
	 * @return
	 */
	private Button getButton(String caption, Resource icon, float width) {
		Button button = ComponentLayoutFactory.getButtonStyle(caption, icon, width, "btn btn-success button-small");
		button.addClickListener(this);
		return button;
	} 

	/**
	 * 
	 * @param contractId
	 * @param firstTabSelect
	 */
	public void assignValues(Long contractId, boolean firstTabSelect) {
		if (contractId != null) {
			assignValues(CONT_SRV.getById(Contract.class, contractId), firstTabSelect);
		} else {
			btnMovePrevious.setEnabled(false);
			btnMoveNext.setEnabled(false);
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + 0 + "/" + 0 + "</b>");
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @param firstTabSelect
	 */
	public void assignValues(Contract contract, boolean firstTabSelect) {
		this.reset();
		this.contract = contract;
		setUpNavigation(contract.getId());
		boolean contractAssigned = INBOX_SRV.isContractAssigned(contract.getId());
		summaryPanel.assignValues(contract);
		loanSummaryPanel.assignValues(contract);
		loanPanel.assignValues(contract, contractAssigned);
		usersPanel.assignValues(contract);
		cashflowPanel.assignValues(contract);
		notesPanel.assignValues(contract);
		assetPanel.assignValues(contract);
		documentPanel.assignValues(contract);
		transferPanel.assignValues(contract);
		/*if (ProfileUtil.isCollection()) {
			collectionPanel.assignValues(contract);
		}*/
		// manageAccess(contract);
		
		if (firstTabSelect) {
			accordionPanel.setSelectedTab(loanSummaryPanel);
		}
		btnReceived.setVisible(!contractAssigned && !btnRecapView.isVisible());
		if (contractAssigned) {
			boolean contractAssignedToUser = INBOX_SRV.isContractAssignedToUser(contract.getId(), UserSessionManager.getCurrentUser().getId());
			btnBooked.setVisible(!contractAssignedToUser && !btnRecapView.isVisible());
		}
		
		phoneHistoriesPanel.assignValues(contract, true);
		phoneLegalFormPanel.assignValue(contract);
		phoneISRTabPanel.assignValues(contract);
		colPhoneReturnRepoTabPanel.assignValue(contract);
	}
	
	/**
	 * Setting up navigation
	 * @param contractId
	 */
	private void setUpNavigation(Long contractId) {
		previousId = null;
		nextId = null;
		if (contractIds == null) {
			contractIds = getContractIds();
		}
		if (isExistContractId(contractId, contractIds)) {
			int size = contractIds.size();
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + (size == 0 ? 0 : getCurrentIndexed(contractId)) + "/" + size + "</b>");
			for (Long id : contractIds) {
				if (id < contractId) {
					previousId = (previousId != null && previousId > id) ? previousId : id;
				} else if (id > contractId) {
					nextId = (nextId != null && nextId < id) ? nextId : id;
				}
			}
			btnMovePrevious.setEnabled(previousId != null);
			btnMoveNext.setEnabled(nextId != null);
		} else {
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + 1 + "/" + 1 + "</b>");
			btnMoveNext.setEnabled(false);
			btnMovePrevious.setEnabled(false);
		}
	}
	
	/**
	 * 
	 * @param currentContractId
	 * @param contractIds
	 * @return
	 */
	private boolean isExistContractId(Long currentContractId, List<Long> contractIds) {
		if (contractIds != null && !contractIds.isEmpty()) {
			for (Long contraId : contractIds) {
				if (contraId == currentContractId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get Contract Central Queue Ids
	 * @return
	 */
	private List<Long> getContractIds() {
		SecUser userLogin = UserSessionManager.getCurrentUser();
		List<ContractUserInbox> contractUsers = new ArrayList<ContractUserInbox>();
		if (ProfileUtil.isCMLeader()) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.CMSTAFF);
		} else if (ProfileUtil.isCMStaff()) {
			contractUsers = INBOX_SRV.getContractUserInboxByUser(userLogin.getId());
		}
		List<Long> ids = new ArrayList<Long>();
		if (contractUsers != null && !contractUsers.isEmpty()) {
			for (ContractUserInbox contractUserInbox : contractUsers) {
				Contract con = contractUserInbox.getContract();
				if (con != null) {
					ids.add(con.getId());
				}
			}
		} 
		return ids;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private long getCurrentIndexed(Long id) {
		List<Long> conIds = getContractIds();
		if (conIds != null && !conIds.isEmpty()) {
			long currentIndexed = 1;
			for (Long conId : conIds) {
				if (id == conId) {
					return currentIndexed;
				}
				currentIndexed++;
			}
		}
		return 0l;
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void manageAccess(Contract contract) {
		EWkfStatus contractWkfStatus = contract.getWkfStatus();
		mnuAfterSales.setVisible(contractWkfStatus.equals(ContractWkfStatus.FIN));
		btnFlags.setVisible(ContractWkfStatus.FIN.equals(contract.getWkfStatus()));
		btnPrint.setVisible(ContractWkfStatus.FIN.equals(contract.getWkfStatus()));
	}

	/**
	 * confirm Received Contract
	 */
	private void confirmReceivedContract() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), I18N.message("confirm.received.applications"), I18N.message("confirm"), I18N.message("cancel"),
				new ConfirmDialog.Listener() {				
					/**
					 */
					private static final long serialVersionUID = 8385185545686540623L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_SRV.receiveContract(contract.getId());
							dialog.close();
							Page.getCurrent().setUriFragment("!" + ScanContractsPanel.NAME);
			            }
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * Confirm Book contract
	 */
	private void confirmBookedContract() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), I18N.message("confirm.booked.applications"), I18N.message("confirm"), I18N.message("cancel"),
				new ConfirmDialog.Listener() {				
					/**
					 */
					private static final long serialVersionUID = 8385185545686540623L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_SRV.bookContract(contract.getId());
							dialog.close();
							Page.getCurrent().setUriFragment("!" + BookContractsPanel.NAME);
			            }
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		contract = Contract.createInstance();
		summaryPanel.reset();
		loanPanel.reset();
		if (assetPanel != null) {
			assetPanel.reset();
		}
		/*if (collectionPanel != null) {
			collectionPanel.reset();
		}*/
		/*if (contactPanel != null) {
			contactPanel.reset();
		}*/
		
		phoneHistoriesPanel.reset();
		phoneLegalFormPanel.reset();
		phoneISRTabPanel.reset();
		colPhoneReturnRepoTabPanel.reset();
	}
	
	/**
	 * Create Action MenuBar
	 * @return
	 */
	private MenuBar getAfterSalesButtons() {
		MenuBar actionMenu = new MenuBar();
		actionMenu.addStyleName("small-menubar-style");
		
		MenuItem item = actionMenu.addItem(I18N.message("after.sale.event"), FontAwesome.FLASH, null);
		
		item.addItem(I18N.message("close.contract"), new Command() {
			/** */
			private static final long serialVersionUID = -6736212247909029335L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				ContractClosePopupPanel window = new ContractClosePopupPanel("close.contract");
				UI.getCurrent().addWindow(window);
				window.assignValue(contract);
			}
		});
		
		item.addItem(I18N.message("early.settlement"), new Command() {
			/** */
			private static final long serialVersionUID = 9053348987568419976L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				ContractEarlySettlementPopupPanel window = new ContractEarlySettlementPopupPanel("early.settlement");
				UI.getCurrent().addWindow(window);
				window.assignValue(contract);
			}
		});
		
		item.addItem(I18N.message("redemption"), new Command() {
			/** */
			private static final long serialVersionUID = 2901399358224109090L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().addWindow(new ContractRedemptionPopupPanel("redemption"));
			}
		});
		item.addItem(I18N.message("new.repossession"), new Command() {
			/** */
			private static final long serialVersionUID = 2901399358224109090L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().addWindow(new ContractNewRepossessionPopupPanel("new.repossession"));
			}
		});
		item.addItem(I18N.message("terminate.contract"), new Command() {
			/** */
			private static final long serialVersionUID = -5246276950843254431L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
				ContractTerminatePopupPanel window = new ContractTerminatePopupPanel("terminate.contract");
				UI.getCurrent().addWindow(window);
				window.assignValue(contract);
			}
		});
		
		return actionMenu;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		boolean valid = true;
		if (!loanSummaryPanel.validateForActivated().isEmpty()) {
			valid = false;
			errors.addAll(loanSummaryPanel.validateForActivated());
			accordionPanel.setSelectedTab(loanPanel);
		} else if (!assetPanel.validateForActivated().isEmpty()) {
			valid = false;
			errors.addAll(assetPanel.validateForActivated());
			accordionPanel.setSelectedTab(assetPanel);
		}
		return valid;
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (accordionPanel.getSelectedTab().equals(loanPanel)) {
			loanPanel.cleanMessage();
			loanSummaryPanel.removeErrorsPanel();
			loanSummaryPanel.assignValues(contract);
			loanPanel.getUpdateDataPanel().assignValue(contract);
		} /*else if (accordionPanel.getSelectedTab().equals(collectionPanel)) {
			collectionPanel.removeAllErrorsPanel();
		}*/ else if (accordionPanel.getSelectedTab().equals(assetPanel)) {
			assetPanel.assignValues(contract);
		} 
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSMS) {
			SMSPopupPanel smsPopupPanel = SMSPopupPanel.show(contract, new SMSPopupPanel.Listener() {
				private static final long serialVersionUID = -2684811234982078364L;
				@Override
				public void onClose(SMSPopupPanel dialog) {
				}
			});
			smsPopupPanel.assignValues(contract);
		} else if (event.getButton() == btnFlags) {
			UI.getCurrent().addWindow(new SummaryFlagPopupPanel(contract));
		} else if (event.getButton() == btnNotes) {
			UI.getCurrent().addWindow(new SummaryNotePopupPanel(contract, loanSummaryPanel));
		} else if (event.getButton() == btnPrint) {
			CONT_SRV.printContract(contract.getId());
		} else if (event.getButton() == btnReceived) {
			confirmReceivedContract();
		} else if (event.getButton() == btnBooked) {
			confirmBookedContract();
		} else if (event.getButton() == btnRecapView) {
			Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + contract.getId());
		} else if (event.getButton() == btnMoveNext) {
			assignValues(nextId, true);
		} else if (event.getButton() == btnMovePrevious) {
			assignValues(previousId, true);
		}
	}
}
