package com.nokor.efinance.core.quotation.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.third.creditbureau.cbc.model.AccountType;
import com.nokor.efinance.third.creditbureau.cbc.model.EnquiryType;
import com.nokor.efinance.third.creditbureau.cbc.model.ProductType;
import com.nokor.efinance.third.creditbureau.exception.ErrorCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.InvokedCreditBureauException;
import com.nokor.efinance.third.creditbureau.exception.ParserCreditBureauException;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.columngenerator.DateColumnGenerator;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Credit bureau panel
 * @author ly.youhort
 */
public class CreditBureauPanel extends AbstractTabPanel implements QuotationEntityField {

	private static final long serialVersionUID = 754341755217081254L;
	
	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	
	private Quotation quotation;
	
	private ERefDataComboBox<EnquiryType> cbxCbcEnquiryType;
	private ERefDataComboBox<ProductType> cbxCbcProductType;
	private ERefDataComboBox<AccountType> cbxCbcAccountType;
	
	private NavigationPanel navigationPanel;
	
	private SimplePagedTable<QuotationExtModule> pagedTable;
	private TabSheet creditBureauTabSheet;
	private CreditBureauInfoPanel creditBureauInfoPanel;	
	private List<ColumnDefinition> columnDefinitions;
		
	public CreditBureauPanel(final EApplicantType applicantType) {
		super();
		setSizeFull();
		navigationPanel = new NavigationPanel();
		Button btnCreditBureau = new NativeButton(I18N.message("enquiry"));
		btnCreditBureau.setIcon(new ThemeResource("icons/16/credit_report.png"));
		navigationPanel.addButton(btnCreditBureau);
		btnCreditBureau.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4221139206201864574L;
			@Override
			public void buttonClick(ClickEvent event) {
				
				int nbQuotationExtModules = 0;
				if (quotation.getQuotationExtModules() != null) {
					for (QuotationExtModule quotationExtModule : quotation.getQuotationExtModules()) {
						if (quotationExtModule.getApplicantType() == applicantType) {
							nbQuotationExtModules += 1;
						}
					}
				}
				
				int nbMaxEnquiry = AppConfig.getInstance().getConfiguration().getInt("credit-bureau.nb-max-enquiry");
				
				if (nbQuotationExtModules >= nbMaxEnquiry) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("nb.max.enquiry.credit.bureau.reached"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else if (checkValidityFiels()) {
				
					Map<String, Object> parameters = new HashMap<String, Object>();
					EnquiryType enquiryType = cbxCbcEnquiryType.getSelectedEntity();
					ProductType productType = cbxCbcProductType.getSelectedEntity();
					AccountType accountType = cbxCbcAccountType.getSelectedEntity();
					
					parameters.put(EnquiryType.class.toString(), enquiryType);
					parameters.put(ProductType.class.toString(), productType);
					parameters.put(AccountType.class.toString(), accountType);
					
					parameters.put(EApplicantType.class.toString(), applicantType);
					
					try {
						quotationService.invokeCreditBureau(quotation.getId(), parameters);
						assignValues(quotation, applicantType);
					} catch (InvokedCreditBureauException e) {
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "170px", I18N.message("error"),
								MessageBox.Icon.ERROR, I18N.message("cb.error.process", e.getMessage()), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					} catch (ErrorCreditBureauException e) {
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "170px",  I18N.message("error"),
								MessageBox.Icon.ERROR, I18N.message("cb.error.process", e.getMessage()), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					} catch (ParserCreditBureauException e) {
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "170px",  I18N.message("error"),
								MessageBox.Icon.ERROR, I18N.message("cb.error.process", e.getMessage()), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}
					
				}
			}
		});
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.setSpacing(true);
		
		cbxCbcEnquiryType = new ERefDataComboBox<EnquiryType>("Enquiry Type", EnquiryType.list());
		cbxCbcEnquiryType.setSelectedEntity(EnquiryType.NA);
		cbxCbcEnquiryType.setRequired(true);
		
		cbxCbcProductType = new ERefDataComboBox<ProductType>("Product Type", ProductType.list());
		cbxCbcProductType.setSelectedEntity(ProductType.MTL);
		cbxCbcProductType.setRequired(true);
		
		cbxCbcAccountType = new ERefDataComboBox<AccountType>("Account Type", AccountType.list());
		cbxCbcAccountType.setSelectedEntity(AccountType.S);
		cbxCbcAccountType.setRequired(true);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(new FormLayout(cbxCbcEnquiryType));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(new FormLayout(cbxCbcProductType));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(new FormLayout(cbxCbcAccountType));
				
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<QuotationExtModule>(this.columnDefinitions);
		pagedTable.setPageLength(4);		
		final VerticalLayout pagedVerticalLayout = new VerticalLayout();
		pagedVerticalLayout.setMargin(true);		
		pagedVerticalLayout.addComponent(pagedTable);
		
		creditBureauTabSheet = new TabSheet();
		creditBureauInfoPanel = new CreditBureauInfoPanel();
		
		creditBureauTabSheet.addTab(pagedVerticalLayout, I18N.message("credit.bureau"));
		creditBureauTabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = 289500717149171793L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component selectedTab = event.getTabSheet().getSelectedTab();
				if (selectedTab == pagedVerticalLayout) {
					creditBureauTabSheet.removeComponent(creditBureauInfoPanel);
				}
			}
		});			
		
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long quextId = (Long) event.getItem().getItemProperty(ID).getValue();
					QuotationExtModule quotationExtModule = quotationService.getById(QuotationExtModule.class, quextId);
					creditBureauInfoPanel.assignValues(quotationExtModule);
					creditBureauTabSheet.addTab(creditBureauInfoPanel, I18N.message("detail"));
					creditBureauTabSheet.setSelectedTab(creditBureauInfoPanel);
				}
			}
		});
		
		contentLayout.addComponent(new Panel(horizontalLayout));
		contentLayout.addComponent(creditBureauTabSheet);
		
		return contentLayout;
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 * @param applicantType
	 */
	public void assignValues(Quotation quotation, EApplicantType applicantType) {
		this.quotation = quotation;
		if (quotation.getId() != null) {
			List<QuotationExtModule> quotationExtModules = quotationService.getQuotationExtModules(quotation.getId(), applicantType);
			pagedTable.setContainerDataSource(getIndexedContainer(quotationExtModules));
			this.quotation.setQuotationExtModules(quotationExtModules);
		} else {
			pagedTable.removeAllItems();
		}
		creditBureauTabSheet.removeComponent(creditBureauInfoPanel);
	}
	
	/**
	 * @return navigationPanel
	 */
	public NavigationPanel getNavigationPanel() {
		return this.navigationPanel;
	}
	
	/**
	 * @return
	 */
	public boolean checkValidityFiels() {
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxCbcEnquiryType, "Enquiry Type");
		checkMandatorySelectField(cbxCbcProductType, "Product Type");
		checkMandatorySelectField(cbxCbcAccountType, "Account Type");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<QuotationExtModule> quotationExtModules) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			
			for (QuotationExtModule quotationExtModule : quotationExtModules) {
				Item item = indexedContainer.addItem(quotationExtModule.getId());
				item.getItemProperty(ID).setValue(quotationExtModule.getId());
				item.getItemProperty("processDate").setValue(quotationExtModule.getProcessDate());
				item.getItemProperty("reference").setValue(quotationExtModule.getReference());
				item.getItemProperty("processByUser.desc").setValue(quotationExtModule.getProcessByUser().getDesc());
			}
						
		} catch (DaoException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
		return indexedContainer;
	}
	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("processDate", I18N.message("process.date"), Date.class, Align.LEFT, 120, new DateColumnGenerator(DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH)));
		columnDefinitions.add(new ColumnDefinition("reference", I18N.message("reference"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("processByUser.desc", I18N.message("create.user"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
}
