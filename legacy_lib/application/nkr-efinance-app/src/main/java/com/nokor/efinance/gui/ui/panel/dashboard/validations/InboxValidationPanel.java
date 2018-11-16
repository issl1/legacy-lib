package com.nokor.efinance.gui.ui.panel.dashboard.validations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Send payment to AP for CM_Leader
 * @author uhout.cheng
 */
public class InboxValidationPanel extends AbstractControlPanel implements ItemClickListener, SelectedItem, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -2575849585963449672L;
	
	private final static String SELECT = "select";
	
	private final static String TAX_INVOICE_AMOUNT = "tax.invoice.amount";
	private final static String NB_PREPAID_INSTALLMENT = "nb.prepaid.installment";
	private final static String COMMISSION_1 = "commission.1";
	private final static String INSURANCE_FEE = "insurance.fee";
	private final static String CONTRACT_FEE = "contract.fee";
	private final static String DOWN_PAYMENT = "down.payment";
	
	private Button btnSendToAP;
	private SimplePagedTable<Contract> pagedTable;
	private Item selectedItem;
	private TextField txtContractId;
	private Button btnSelect;
	private boolean selectAll = false;
	private AutoDateField dfActivatedDate;
	private AutoDateField dfEndDate;
	private Button btnSearch;
	private Button btnReset;
	
	/**
	 * 
	 */
	public InboxValidationPanel() {
		setSizeFull();
		setMargin(true);
		txtContractId = ComponentFactory.getTextField(false, 60, 130);
		btnSelect = ComponentLayoutFactory.getDefaultButton("select", FontAwesome.CHECK_SQUARE_O, 60);
		btnSendToAP = ComponentLayoutFactory.getDefaultButton("send.to.ap", FontAwesome.SEND, 90);
		
		dfActivatedDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField();
		
		dfActivatedDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
		
		dfActivatedDate.setWidth(90, Unit.PIXELS);
		dfEndDate.setWidth(90, Unit.PIXELS);
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnReset = ComponentLayoutFactory.getButtonReset();
		
		btnSearch.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -8841759317604377633L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				assignValues();
			}
		});
		
		btnReset.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4543550800426206858L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				dfActivatedDate.setValue(DateUtils.getDateAtBeginningOfMonth());
				dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
			}
		});
		
		txtContractId.addFocusListener(new FocusListener() {
						
			/** */
			private static final long serialVersionUID = 1028238828820991980L;

			/**
			 * @see com.vaadin.event.FieldEvents.FocusListener#focus(com.vaadin.event.FieldEvents.FocusEvent)
			 */
			@Override
			public void focus(FocusEvent event) {
				btnSelect.setClickShortcut(KeyCode.ENTER, null);
			}
		});
		
		txtContractId.addBlurListener(new BlurListener() {
			
			/** */
			private static final long serialVersionUID = 4408592551757335148L;

			/**
			 * @see com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
			 */
			@Override
			public void blur(BlurEvent event) {
				btnSelect.removeClickShortcut();
			}
		});
		
		pagedTable = new SimplePagedTable<>(createColumnDefinitions());
		pagedTable.setCaption(null);
		pagedTable.addItemClickListener(this);
		pagedTable.setColumnIcon(SELECT, FontAwesome.CHECK);
		pagedTable.addHeaderClickListener(new HeaderClickListener() {
		
			/** */
			private static final long serialVersionUID = 1679745546762808869L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == SELECT) {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					java.util.Collection<Long> ids = (java.util.Collection<Long>) pagedTable.getItemIds();
					for (Long id : ids) {
						Item item = pagedTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(getSearchLayout());
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        addComponent(contentLayout);
	}
	
	/**
	 * Search Layout for search applicant id
	 * @return
	 */
	private Panel getSearchLayout() {
		btnSendToAP.addClickListener(new ClickListener() {
						
			/** */
			private static final long serialVersionUID = -6184399628666587968L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				List<Long> ids = getSelectedIds(pagedTable);
				if (ids != null && !ids.isEmpty()) {
					ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), I18N.message("confirm.send.payment.to.ap"), I18N.message("confirm"), I18N.message("cancel"),
							new ConfirmDialog.Listener() {				
								
						/** */
						private static final long serialVersionUID = 4653919449097278911L;
						
						/**
						 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
						 */
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								try {
									CONT_ACTIVATION_SRV.sendPaymentToAP(ids);
									assignValues();
									ComponentLayoutFactory.displaySuccessMsg("send.payment.to.ap.success");
								} catch (Exception e) {
									logger.error("Error send Payments to AP", e);
									ComponentLayoutFactory.displayErrorMsg("Error while Send Payment !.");
								}
								dialog.close();
							}
						}
					});
				}
			}
		});
		
		btnSelect.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 7631024674425029342L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@SuppressWarnings("rawtypes")
			@Override
			public void buttonClick(ClickEvent event) {
				for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
				    Long iid = (Long) i.next();
				    Item item = pagedTable.getItem(iid);
				    String conRef = (String) item.getItemProperty(Contract.REFERENCE).getValue();
				    if (txtContractId.getValue().equals(conRef)) {
				    	CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
				    	cbSelect.setValue(true);
				    	pagedTable.setCurrentPage(getNbPage(conRef));
				    	pagedTable.addStyleName("colortable");
				    	pagedTable.addStyleName("table-column-wrap");
				    	pagedTable.setCellStyleGenerator(new CellStyleGenerator() {
						
							/** */
							private static final long serialVersionUID = 6376689924949010015L;

							/**
							 * @see com.vaadin.ui.Table.CellStyleGenerator#getStyle(com.vaadin.ui.Table, java.lang.Object, java.lang.Object)
							 */
							@Override
							public String getStyle(Table source, Object itemId, Object propertyId) {
								Long id = (Long) itemId;
								Payment payment = PAYMENT_SRV.getById(Payment.class, id);
								String conRef = payment.getContract() == null ? StringUtils.EMPTY : payment.getContract().getReference();
								if (txtContractId.getValue().equals(conRef)) {
									return "highligh-lightgreen";
								}
								return null;
							}
						});
				    }
				}
			}
		});
		
		String OPEN_TABLE = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TD = "<td>";
		String CLOSE_TR = "</tr>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusBtnBookLayout = new CustomLayout("xxx");
		String templateBtnBook = OPEN_TABLE;
		templateBtnBook += OPEN_TR;
		templateBtnBook += OPEN_TD;
		templateBtnBook += "<div location =\"btnBook\"/>";
		templateBtnBook += CLOSE_TD;
		templateBtnBook += CLOSE_TR;
		templateBtnBook += CLOSE_TABLE;
		
		cusBtnBookLayout.addComponent(btnSendToAP, "btnBook");
		cusBtnBookLayout.setTemplateContents(templateBtnBook);
		
		HorizontalLayout dateFilterLayout = new HorizontalLayout();
		dateFilterLayout.setSpacing(true);
		dateFilterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("activated.date") + " : "));
		dateFilterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("start.date"));
		dateFilterLayout.addComponent(dfActivatedDate);
		dateFilterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("end.date"));
		dateFilterLayout.addComponent(dfEndDate);
		dateFilterLayout.addComponent(btnSearch);
		dateFilterLayout.addComponent(btnReset);
		dateFilterLayout.setComponentAlignment(btnSearch, Alignment.MIDDLE_LEFT);
		dateFilterLayout.setComponentAlignment(btnReset, Alignment.MIDDLE_LEFT);
		
		CustomLayout cusDateFilterLayout = new CustomLayout("xxx");
		String templateDateFilter = OPEN_TABLE;
		templateDateFilter += OPEN_TR;
		templateDateFilter += OPEN_TD;
		templateDateFilter += "<div location =\"dateFilterLayout\"/>";
		templateDateFilter += CLOSE_TD;
		templateDateFilter += CLOSE_TR;
		templateDateFilter += CLOSE_TABLE;
		
		cusDateFilterLayout.addComponent(dateFilterLayout, "dateFilterLayout");
		cusDateFilterLayout.setTemplateContents(templateDateFilter);
		
		CustomLayout cusSearchLayout = new CustomLayout("xxx");
		String templateSearchLayout = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" align=\"right\">";
		templateSearchLayout += OPEN_TR;
		templateSearchLayout += "<td align=\"right\">";
		templateSearchLayout += "<div location =\"lblContractId\"/>";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"txtContractId\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"btnSelect\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += CLOSE_TR;
		templateSearchLayout += CLOSE_TABLE;
		
		cusSearchLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractId");
		cusSearchLayout.addComponent(txtContractId, "txtContractId");
		cusSearchLayout.addComponent(btnSelect, "btnSelect");
		cusSearchLayout.setTemplateContents(templateSearchLayout);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(cusBtnBookLayout);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(40, Unit.PIXELS));
		horLayout.addComponent(cusDateFilterLayout);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(40, Unit.PIXELS));
		horLayout.addComponent(cusSearchLayout);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(horLayout);
		verLayout.setComponentAlignment(horLayout, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel(verLayout);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return panel;
	}
		
	/**
	 * 
	 * @param conRef
	 * @return
	 */
	private int getContractIndex(String conRef) {
		if (StringUtils.isNotEmpty(conRef)) {
			List<Payment> payments = PAYMENT_SRV.list(getRestrictions());
			for (int i = 0; i < payments.size(); i++) {
				Contract con = payments.get(i).getContract();
				if (conRef.equals(con.getReference())) {
					return i;
				}
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param conRef
	 * @return
	 */
	private int getNbPage(String conRef) {
		int currentContractIndex = getContractIndex(conRef);
		int paymentSize = PAYMENT_SRV.list(getRestrictions()).size();
		int nbPage = paymentSize % pagedTable.getPageLength();
		int totalPage = 0;
		if (nbPage != 0) {
			totalPage = (paymentSize / pagedTable.getPageLength()) + 1;
		} else {
			totalPage = paymentSize / pagedTable.getPageLength();
		} 
		for (int i = 0; i < totalPage; i++) {
			int startIndex = 0;
			if (pagedTable.getPageLength() == 5) {
				startIndex = i * 5;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 4) {
					return i + 1;
				}
			} else if (pagedTable.getPageLength() == 10) {
				startIndex = i * 10;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 9) {
					return i + 1;
				}
			} else if (pagedTable.getPageLength() == 25) {
				startIndex = i * 25;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 24) {
					return i + 1;
				}
			} else if (pagedTable.getPageLength() == 50) {
				startIndex = i * 50;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 49) {
					return i + 1;
				}
			} else if (pagedTable.getPageLength() == 100) {
				startIndex = i * 100;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 99) {
					return i + 1;
				}
			} else if (pagedTable.getPageLength() == 600) {
				startIndex = i * 600;
				if (currentContractIndex >= startIndex && currentContractIndex <= startIndex + 599) {
					return i + 1;
				}
			}
		}
		return 0;
	}
	
	/**
	 *
	 */
	public void assignValues() {
		setIndexedContainer(PAYMENT_SRV.list(getRestrictions()));
	}
	
	/**
	 * @param dealer
	 * @return
	 */
	private PaymentRestriction getRestrictions() {
		PaymentRestriction restrictions = new PaymentRestriction();
		restrictions.getWkfStatusList().add(PaymentWkfStatus.RVA);
		restrictions.setPaymentTypes(new EPaymentType[] { EPaymentType.ORC });
		restrictions.setActivatedDateFrom(dfActivatedDate.getValue());
		restrictions.setActivatedDateTo(dfEndDate.getValue());
		return restrictions;
	}
	
	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Long> getSelectedIds(SimplePagedTable<Contract> pagedTable) {
		List<Long> ids = new ArrayList<>();
		for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = pagedTable.getItem(iid);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(iid);
		    }
		}
		return ids;
	}
	
	/**
	 * @param payments
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Payment> payments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				
				
				
				Item item = indexedContainer.addItem(payment.getId());
				item.getItemProperty(SELECT).setValue(getRenderSelected(payment.getId()));
				item.getItemProperty(Payment.ID).setValue(payment.getId());
				
				Contract contract = payment.getContract();
				
				String conRef = StringUtils.EMPTY; 
				Date activatedDate = null;
				if (contract != null) {
					
					Collection collection = contract.getCollection();
					
					Cashflow commission = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.COMM);
					Cashflow contractFee = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.FEE);
					Cashflow insuranceFee = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.INSFEE);
					double downPayment = MyNumberUtils.getDouble(contract.getTiInvoiceAmount()) - MyNumberUtils.getDouble(contract.getTiFinancedAmount());
					
					conRef = contract.getReference();
					activatedDate = contract.getStartDate();
					
					item.getItemProperty(TAX_INVOICE_AMOUNT).setValue(AmountUtils.format(contract.getTiInvoiceAmount()));
					item.getItemProperty(MContract.TIFINANCEDAMOUNT).setValue(AmountUtils.format(contract.getTiFinancedAmount()));
					item.getItemProperty(DOWN_PAYMENT).setValue(AmountUtils.format(downPayment));
					item.getItemProperty(NB_PREPAID_INSTALLMENT).setValue(collection != null ? collection.getNbInstallmentsPaid() : null);
					item.getItemProperty(COMMISSION_1).setValue(AmountUtils.format(commission != null ? commission.getTiInstallmentAmount() : 0d));
					item.getItemProperty(INSURANCE_FEE).setValue(AmountUtils.format(insuranceFee != null ? insuranceFee.getTiInstallmentAmount() : 0d));
					item.getItemProperty(CONTRACT_FEE).setValue(AmountUtils.format(contractFee != null ? contractFee.getTiInstallmentAmount() : 0d));
				}
			
				item.getItemProperty(Contract.REFERENCE).setValue(conRef);
				item.getItemProperty(Contract.STARTDATE).setValue(activatedDate);
//				item.getItemProperty(Payment.PAYMENTDATE).setValue(payment.getPaymentDate());
				item.getItemProperty(Payment.PAYMENTMETHOD).setValue(payment.getPaymentMethod().getDescLocale());
				item.getItemProperty(FMEntityField.WKF_STATUS).setValue(payment.getWkfStatus().getDescLocale());
				item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
                        MyNumberUtils.getDouble(payment.getTiPaidAmount()), "###,##0.00"));
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param payId
	 * @return
	 */
	private CheckBox getRenderSelected(Long payId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(payId);
		checkBox.setValue(true);
		return checkBox;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30));				
		columnDefinitions.add(new ColumnDefinition(Payment.ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(Contract.REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Contract.STARTDATE, I18N.message("activated.date"), Date.class, Align.LEFT, 80));
//		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.PAYMENTMETHOD, I18N.message("payment.method"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TAX_INVOICE_AMOUNT, I18N.message("tax.invoice.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(MContract.TIFINANCEDAMOUNT, I18N.message("finance.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DOWN_PAYMENT, I18N.message("down.payment"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NB_PREPAID_INSTALLMENT, I18N.message("nb.prepaid.installment"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMMISSION_1, I18N.message("commission.one"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(INSURANCE_FEE, I18N.message("insurance.fee"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_FEE, I18N.message("contract.fee"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(FMEntityField.WKF_STATUS, I18N.message("status"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}	
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + getItemSelectedId());
		}
	}
		
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			String conRef = ((String) this.selectedItem.getItemProperty(Contract.REFERENCE).getValue());
			Contract contra = CONT_SRV.getByReference(conRef);
			return contra == null ? null : contra.getId();
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
}
