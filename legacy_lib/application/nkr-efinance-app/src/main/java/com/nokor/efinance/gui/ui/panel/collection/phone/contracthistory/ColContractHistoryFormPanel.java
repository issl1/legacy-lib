package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.collection.service.ReminderRestriction;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.field.staff.report.ReportContractInformationField;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColContactInfosPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColPhoneContactPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColPhoneContactResultPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.emails.ColPhoneEmailTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.isr.ISRTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.ColPhoneLegalFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.letters.ColPhoneLettersFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment.ColLockSplitPaymentPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption.RedemptionPopupPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.reminder.CurrentReminderWindowPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.repo.ColReturnRepoTabPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.sms.ColPhoneSMSTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.transactions.TransactionPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.history.ColPhoneHistoriesPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.ContractOperationsPanel;
import com.nokor.efinance.gui.ui.panel.contract.close.CloseContractPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.appointment.AppointmentFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.notes.request.ColRequestFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.tranfer.ContractTranferTabPanel;
import com.nokor.efinance.gui.ui.panel.dashboard.DashboardPanel;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
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
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Collection contract history form panel
 * @author uhout.cheng
 */
public class ColContractHistoryFormPanel extends VerticalLayout implements ClickListener, FinServicesHelper, SelectedTabChangeListener {
		
	/** */
	private static final long serialVersionUID = 1341364924110183447L;
	
	private ReportService reportService = (ReportService) SecApplicationContextHolder.getContext().getBean("reportService");
	
	private ColSummaryPanel summaryPanel;
	
	private Button btnMoveNext;
	private Button btnMovePrevious;
	private Button btnReminder;
	private Button btnPrint;
	
	private HorizontalLayout topLayout;
	
	private ColPhoneContactResultPanel colPhoneResultPanel;
	private ColPhoneContactPanel phoneContactPanel;
	private ColPhoneSMSTablePanel phoneSMSTablePanel;
	private ISRTabPanel phoneISRTabPanel;
	private TransactionPanel transactionPanel;
	private ColRequestFormPanel requestFormPanel;
	//private ColPhoneLockSplitPaymentsPanel phoneLockSplitsPanel;
	private ColLockSplitPaymentPanel colLockSplitPaymentPanel;
	private ColPhoneLegalFormPanel phoneLegalFormPanel;
	//private ColLegalPanel colLegalPanel;
	private ColPhoneLettersFormPanel colPhoneLettersFormPanel;
	private ColReturnRepoTabPanel colPhoneReturnRepoTabPanel;
	private ColPhoneEmailTabPanel colPhoneEmailTabPanel;
	private ContractOperationsPanel contractOperationsPanel;
//	private ColPhonePayOffTabPanel colPhonePayOffTabPanel;
	
	private ColContactInfosPanel contactInfosPanel;
	private ColContractDuesTablePanel contractDuesTablePanel;
	
	private TabSheet accordionPanel;
	
	private ColPhoneHistoriesPanel phoneHistoriesPanel;
	
	private HorizontalLayout topRightLayout;
	
	private List<Long> contractIds;
	private Long previousId;
	private Long nextId;
	
	private Label lblCurrentIndexed;
	
	private Contract contract;

	private AppointmentFormPanel appointmentFormPanel;
	private ContractTranferTabPanel tranferTabPanel;
	
	private HorizontalLayout alertMsgLayout;
	private CloseContractPanel closeContractPanel;
	
	//private ColContractDealerDetailPanel colContractDealerDetailPanel;
	private Button btnReDeem;

	/**
	 * 
	 * @param isCallIn
	 */
	public ColContractHistoryFormPanel(boolean isCallIn) {
		summaryPanel = new ColSummaryPanel();
		//colContractDealerDetailPanel = new ColContractDealerDetailPanel();
		
		btnMoveNext = getButton(FontAwesome.FORWARD);
		btnMoveNext.addStyleName("btn-icon-padding");
		btnMovePrevious = getButton(FontAwesome.BACKWARD);
		btnPrint = getButton(FontAwesome.PRINT);
		btnPrint.addClickListener(this);
		
		btnReDeem = ComponentLayoutFactory.getDefaultButton("redeem", null, 70);
		btnReDeem.addClickListener(this);
		
        accordionPanel = new TabSheet();
        accordionPanel.addSelectedTabChangeListener(this);
        accordionPanel.setHeight(315, Unit.PIXELS);
        phoneContactPanel = new ColPhoneContactPanel(isCallIn);

		phoneSMSTablePanel = new ColPhoneSMSTablePanel();
		requestFormPanel = new ColRequestFormPanel(null);
	    
	    transactionPanel = new TransactionPanel();
       phoneLegalFormPanel = new ColPhoneLegalFormPanel();
	    //colLegalPanel = new ColLegalPanel();
        phoneISRTabPanel = new ISRTabPanel();
        //phoneLockSplitsPanel = new ColPhoneLockSplitPaymentsPanel();
        colLockSplitPaymentPanel = new ColLockSplitPaymentPanel();
        colPhoneLettersFormPanel = new ColPhoneLettersFormPanel();
  		colPhoneReturnRepoTabPanel = new ColReturnRepoTabPanel();
  		colPhoneEmailTabPanel = new ColPhoneEmailTabPanel();
  		contractOperationsPanel = new ContractOperationsPanel();
//  		colPhonePayOffTabPanel = new ColPhonePayOffTabPanel();
  		appointmentFormPanel = new AppointmentFormPanel("");
  		tranferTabPanel = new ContractTranferTabPanel();
  		
  		closeContractPanel = new CloseContractPanel();
  		
  		colPhoneResultPanel = new ColPhoneContactResultPanel();
  		
  		
		accordionPanel.addTab(phoneContactPanel, I18N.message("contact"));
		
		if (!ProfileUtil.isCallCenter()) {
			accordionPanel.addTab(colPhoneResultPanel, I18N.message("task"));
		}
		
	    accordionPanel.addTab(phoneSMSTablePanel, I18N.message("sms"));
	    accordionPanel.addTab(colPhoneEmailTabPanel, I18N.message("email"));
	    accordionPanel.addTab(colPhoneLettersFormPanel, I18N.message("letter"));
	    accordionPanel.addTab(transactionPanel, I18N.message("transaction"));
	    accordionPanel.addTab(ComponentLayoutFactory.setMargin(requestFormPanel), I18N.message("request"));
	    accordionPanel.addTab(contractOperationsPanel, I18N.message("operation"));
	    accordionPanel.addTab(phoneLegalFormPanel, I18N.message("legal"));
	    accordionPanel.addTab(phoneISRTabPanel, I18N.message("isr"));
	    accordionPanel.addTab(colLockSplitPaymentPanel, I18N.message("payment"));
	    accordionPanel.addTab(colPhoneReturnRepoTabPanel, I18N.message("return.repo"));
	    accordionPanel.addTab(tranferTabPanel, I18N.message("tranfer"));
	   
	    
//        if (ProfileUtil.isCallCenterStaff() || ProfileUtil.isCallCenterLeader()) {
	    VerticalLayout appointmentLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
	    appointmentLayout.addComponent(appointmentFormPanel);
	    accordionPanel.addTab(appointmentLayout, I18N.message("appointment"));
	    accordionPanel.addTab(closeContractPanel, I18N.message("close.contract"));
//        } 
       
//        accordionPanel.addTab(colPhonePayOffTabPanel, I18N.message("pay.off"));
        
        // Right part
        contactInfosPanel = new ColContactInfosPanel(this);
        contractDuesTablePanel = new ColContractDuesTablePanel();
        
        topLayout = new HorizontalLayout();
        topLayout.setWidth(100, Unit.PERCENTAGE);
        
        phoneHistoriesPanel = new ColPhoneHistoriesPanel(this);
        
        lblCurrentIndexed = ComponentFactory.getLabel();
        lblCurrentIndexed.setCaptionAsHtml(true);
      
        Button btnGoInbox = new Button();
        btnGoInbox.setCaption(I18N.message("go.inbox"));
        btnGoInbox.setStyleName(Reindeer.BUTTON_LINK);
        btnGoInbox.addClickListener(new ClickListener() {
			
        	/** */
			private static final long serialVersionUID = 185904153415553594L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + DashboardPanel.NAME);
			}
		});
        
        topRightLayout = new HorizontalLayout();
        topRightLayout.setSpacing(true);
        topRightLayout.addComponent(btnGoInbox);
        topRightLayout.addComponent(lblCurrentIndexed);
        topRightLayout.addComponent(btnMovePrevious);
        topRightLayout.addComponent(btnMoveNext);
        if (ProfileUtil.isColField()) {
        	 topRightLayout.addComponent(btnPrint);
        }
        topRightLayout.setComponentAlignment(btnGoInbox, Alignment.MIDDLE_LEFT);
        
        Label lblSummary = ComponentFactory.getHtmlLabel("<h1 style=\"color:#449D44; margin:0\">" + I18N.message("summary") + "</h1>");
       // topLayout.addComponent(lblSummary);
        topLayout.addComponent(topRightLayout);
        //topLayout.setComponentAlignment(lblSummary, Alignment.TOP_LEFT);
        topLayout.setComponentAlignment(topRightLayout, Alignment.MIDDLE_RIGHT);
        
        alertMsgLayout = new HorizontalLayout();
       
        VerticalLayout summaryContractLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, false), true);
        summaryContractLayout.addComponent(lblSummary);
        summaryContractLayout.addComponent(summaryPanel);
		summaryContractLayout.addComponent(alertMsgLayout);
		summaryContractLayout.addComponent(btnReDeem);
		summaryContractLayout.addComponent(accordionPanel);
        summaryContractLayout.addComponent(phoneHistoriesPanel);
        
        summaryContractLayout.setComponentAlignment(lblSummary, Alignment.TOP_LEFT);
        summaryContractLayout.setComponentAlignment(btnReDeem, Alignment.BOTTOM_RIGHT);
        
        VerticalLayout contactLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
        contactLayout.addComponent(contactInfosPanel);
        contactLayout.addComponent(contractDuesTablePanel);
        
        HorizontalSplitPanel horSplitPanel = new HorizontalSplitPanel(summaryContractLayout, contactLayout);
        horSplitPanel.setLocked(true);
		horSplitPanel.setWidth(100, Unit.PERCENTAGE);
        horSplitPanel.addStyleName(Reindeer.SPLITPANEL_SMALL);
		horSplitPanel.setSplitPosition(72, Unit.PERCENTAGE);

		VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(topLayout);
        mainLayout.addComponent(horSplitPanel);
        
        setMargin(true);
        setSpacing(true);
        addComponent(mainLayout);
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
	 */
	public void assignReminders() {
		if (ProfileUtil.isColField()) {
			if (topRightLayout.getComponentCount() == 6) {
				topRightLayout.removeComponent(topRightLayout.getComponent(5));
			}
		} else {
			if (topRightLayout.getComponentCount() == 5) {
				topRightLayout.removeComponent(topRightLayout.getComponent(4));
			}
		}
		long countReminders = (long) REMINDER_SRV.count(getReminderRestrictions());
		btnReminder = ComponentLayoutFactory.getDefaultButton(
				"<b style=\"background-color:red; border-radius: 5px; position: absolute; padding: 2px; right: -2px;"
				+ "top: -10px;\">" + String.valueOf(countReminders) + "</b>", FontAwesome.BELL_O, 40);
		btnReminder.addStyleName("btn-icon-padding");
		btnReminder.setCaptionAsHtml(true);
		btnReminder.addClickListener(this);
		topRightLayout.addComponent(btnReminder);
	}
	
	/**
	 * 
	 * @return
	 */
	public ReminderRestriction getReminderRestrictions() {
		ReminderRestriction restrictions = new ReminderRestriction();
//		restrictions.setDate(DateUtils.today());
		restrictions.setConId(this.contract.getId());
		return restrictions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void refereshAfterSaved() {
		phoneContactPanel.assingValues(this.contract);
		if (ProfileUtil.isCallCenterStaff() || ProfileUtil.isCallCenterLeader()) {
			phoneSMSTablePanel.assignValues(this.contract);
		} else {
			summaryPanel.assignValues(this.contract);
			colPhoneEmailTabPanel.assignValue(this.contract);
			phoneSMSTablePanel.assignValues(this.contract);
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @param firstTabSelect
	 */
	private void assignValues(Contract contract, boolean firstTabSelect) {
		this.reset();
		this.contract = contract;
		assignReminders();
		setUpNavigation(contract.getId());
		summaryPanel.assignValues(contract);
		contactInfosPanel.assignValues(contract);
		contractDuesTablePanel.assignValues(contract);
		
		displayAlertMessage();
		
		if (firstTabSelect) {
			phoneContactPanel.assingValues(contract);
			accordionPanel.setSelectedTab(phoneContactPanel);
			phoneHistoriesPanel.assignValues(contract, firstTabSelect);
		}
//		btnReDeem.setVisible(contract.isRedemptionEnable());
//		colContractDealerDetailPanel.assignValue(contract);
	}
	
	/**
	 * If Return/REPO Confirmed alert message 
	 */
	public void displayAlertMessage() {
		Label lblAlertMsg = ComponentFactory.getHtmlLabel("");
		Button btnAction = new Button();
		btnAction.setIcon(FontAwesome.EDIT);
		btnAction.setStyleName(Reindeer.BUTTON_LINK);
		TextField txtMessage = ComponentFactory.getTextField(false, 100, 200);
		alertMsgLayout.setVisible(StringUtils.isNotEmpty(getAlertMsg()));
		Collection collection = contract.getCollection();
		if (collection != null) {
			if (collection.getMessage() != null) {
				lblAlertMsg.setValue("<h2 style=\"color:red; margin: 0\">" + collection.getMessage() + "</h2>");
			} else {
				lblAlertMsg.setValue("<h2 style=\"color:red; margin: 0\">" + getAlertMsg() + "</h2>");
			}
		}
		if (StringUtils.isNotEmpty(getAlertMsg())) {
			alertMsgLayout.removeAllComponents();
			alertMsgLayout.addComponent(lblAlertMsg);
			alertMsgLayout.addComponent(txtMessage);
			alertMsgLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
			alertMsgLayout.addComponent(btnAction);
			alertMsgLayout.setComponentAlignment(lblAlertMsg, Alignment.MIDDLE_LEFT);
			alertMsgLayout.setComponentAlignment(btnAction, Alignment.MIDDLE_CENTER);
			txtMessage.setVisible(false);
			summaryPanel.getAssetStatus(contract);
			btnAction.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 2760293814088677734L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					if (!txtMessage.isVisible()) {
						txtMessage.setVisible(true);
						lblAlertMsg.setVisible(false);
						btnAction.setIcon(FontAwesome.SAVE);
					} else {
							if (StringUtils.isNotEmpty(txtMessage.getValue())) {
								if (collection != null) {
									collection.setMessage(txtMessage.getValue());
									COL_SRV.saveOrUpdate(collection);
								}
								ComponentLayoutFactory.displaySuccessfullyMsg();
								txtMessage.setVisible(false);
								lblAlertMsg.setVisible(true);
								lblAlertMsg.setValue("<h2 style=\"color:red; margin: 0\">" + collection.getMessage() + "</h2>");
								btnAction.setIcon(FontAwesome.EDIT);
							}	
					}
					
				}
			});
		} else {
			alertMsgLayout.removeAllComponents();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private String getAlertMsg() {
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setConId(this.contract.getId());
		restrictions.setCompleted(true);
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			ContractFlag contractFlag = contractFlags.get(0);
			if (contractFlag != null) {
				if (EFlag.RETURN.equals(contractFlag.getFlag())) {
					return I18N.message("motorbike.returned");
				} else if (EFlag.REPO.equals(contractFlag.getFlag())) {
					return I18N.message("motorbike.repo");
				} else if (EFlag.DEAD.equals(contractFlag.getFlag())) {
					return I18N.message("lessee.dead");
				} else if (EFlag.SEIZED.equals(contractFlag.getFlag())) {
					return I18N.message("motorbike.seized");
				} else if (EFlag.LOST.equals(contractFlag.getFlag())) {
					return I18N.message("motorbike.lost");
				} else if (EFlag.DAMAGE.equals(contractFlag.getFlag())) {
					return I18N.message("motorbike.damaged");
				}
			}
		}
		return StringUtils.EMPTY;
	}
		
	/**
	 * 
	 */
	public void reset() {
		summaryPanel.reset();
		contactInfosPanel.reset();
		contractDuesTablePanel.reset();
		phoneContactPanel.reset();
		phoneSMSTablePanel.reset();
		colPhoneEmailTabPanel.reset();
		phoneISRTabPanel.reset();
		colPhoneLettersFormPanel.reset();
		requestFormPanel.reset();
		//phoneLockSplitsPanel.reset();
		contractOperationsPanel.reset();
		colPhoneReturnRepoTabPanel.reset();
		appointmentFormPanel.reset();
		phoneHistoriesPanel.reset();
		colPhoneResultPanel.reset();
		phoneLegalFormPanel.reset();
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (accordionPanel.getSelectedTab().equals(colPhoneResultPanel)) {
			colPhoneResultPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(phoneSMSTablePanel)) {
			phoneSMSTablePanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(colPhoneEmailTabPanel)) {
			colPhoneEmailTabPanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(colPhoneLettersFormPanel)) {
			colPhoneLettersFormPanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(requestFormPanel)) {
			requestFormPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(colLockSplitPaymentPanel)) {
			LockSplit lockSplit = LockSplit.createInstance();
			lockSplit.setContract(contract);
			colLockSplitPaymentPanel.assignValues(lockSplit);
		} else if (accordionPanel.getSelectedTab().equals(phoneLegalFormPanel)) {
			phoneLegalFormPanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(phoneISRTabPanel)) {
			phoneISRTabPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(contractOperationsPanel)) {
			contractOperationsPanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(transactionPanel)) {
			transactionPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(colPhoneReturnRepoTabPanel)) {
			colPhoneReturnRepoTabPanel.assignValue(contract);
		} else if (accordionPanel.getSelectedTab().equals(appointmentFormPanel)) {
			appointmentFormPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(tranferTabPanel)) {
			tranferTabPanel.assignValues(contract);
		} else if (accordionPanel.getSelectedTab().equals(closeContractPanel)) {
			closeContractPanel.assignValue(contract);
		}/* else if (accordionPanel.getSelectedTab().equals(colPhonePayOffTabPanel)) {
			colPhonePayOffTabPanel.assignValue(contract);
		}*/
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
		List<ContractUserInbox> contractUsers = INBOX_SRV.getContractUserInboxByUser(userLogin.getId());
		if (ProfileUtil.isColPhoneLeader(userLogin) || ProfileUtil.isColPhoneSupervisor(userLogin)) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_PHO_STAFF);
		} else if (ProfileUtil.isColFieldLeader(userLogin) || ProfileUtil.isColFieldSupervisor(userLogin)) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_FIE_STAFF);
		} else if (ProfileUtil.isColInsideRepoLeader()) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_INS_STAFF);
		} else if (ProfileUtil.isCallCenterLeader()) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.CAL_CEN_STAFF);
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnMoveNext) {
			assignValues(nextId, true);
		} else if (event.getButton() == btnMovePrevious) {
			assignValues(previousId, true);
		} else if (event.getButton() == btnReminder) {
			List<Reminder> reminders = REMINDER_SRV.list(getReminderRestrictions());
			CurrentReminderWindowPanel windowPanel = CurrentReminderWindowPanel.show(reminders, this);
			UI.getCurrent().addWindow(windowPanel);
		} else if (event.getButton() == btnPrint) {
			Class<? extends Report> reportClass = ReportContractInformationField.class;
			ReportParameter reportParameter = new ReportParameter();
			reportParameter.addParameter("contract", contract);	
			String	fileName = "";
			try {
				fileName = reportService.extract(reportClass, reportParameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
			DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
			UI.getCurrent().addWindow(documentViewver);
		} else if (event.getButton() == btnReDeem) {
			RedemptionPopupPanel redemptionPopupPanel = new RedemptionPopupPanel(contract);
			UI.getCurrent().addWindow(redemptionPopupPanel);
		}
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
	
}
