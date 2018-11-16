package com.nokor.efinance.gui.ui.panel.report.outstanding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author mao.heng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OutstandingsReportPanel.NAME)
public class OutstandingsReportPanel extends AbstractTabPanel implements View, ContractEntityField {
	
	private static final long serialVersionUID = 8019051395954081483L;

	public static final String NAME = "outstanding";
	
	private final ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<OutstandingReport> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private TextField txtFamilyName;
	private TextField txtFirstName;
	
	public OutstandingsReportPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(8, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		txtFamilyName = ComponentFactory.getTextField(false, 60, 200);	
		txtFirstName = ComponentFactory.getTextField(false, 60, 200);
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfEndDate = ComponentFactory.getAutoDateField("", false);       

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("startdate")), iCol++, 0);
        gridLayout.addComponent(dfStartDate, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 1);
        gridLayout.addComponent(txtFamilyName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 1);
        gridLayout.addComponent(txtFirstName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("enddate")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<OutstandingReport>(this.columnDefinitions);
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("outstanding"));
        
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (StringUtils.isNotEmpty(txtFamilyName.getValue()) || StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addAssociation("contractApplicants", "contractapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		}
		
		if (StringUtils.isNotEmpty(txtFamilyName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtFamilyName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(START_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(END_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}

	public void reset() {
		cbxDealer.setValue(null);
		txtContractReference.setValue("");
		txtFamilyName.setValue("");
		txtFirstName.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
	
	/**
	 * Search
	 */
	private void search() {
		
		List<Contract> contracts = contractService.getListContract(getRestrictions());
		List<OutstandingReport> outstandingReports = new ArrayList<OutstandingReport>();
		
		for (Contract contract : contracts) {
			OutstandingReport outstanding = new OutstandingReport();
			outstanding.setId(contract.getId());
			outstanding.setContractReference(contract.getReference());
			outstanding.setApplicant(contract.getApplicant());
			Amount outstandingAmt = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
			outstanding.setOutstandingAmount(outstandingAmt.getTiAmount());
			
			outstandingReports.add(outstanding);
		}
		setIndexedContainer(outstandingReports);

	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<OutstandingReport> outstandings) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (OutstandingReport outstanding : outstandings) {
			Item item = indexedContainer.addItem(outstanding.getId());
			item.getItemProperty(REFERENCE).setValue(outstanding.getContractReference());
			item.getItemProperty(LAST_NAME_EN).setValue(outstanding.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(outstanding.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty("outstanding.amount").setValue(outstanding.getOutstandingAmount());

		}						
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.reference"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("outstanding.amount", I18N.message("outstanding.amount"), Double.class, Align.RIGHT, 150));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	/**
	 * @author nora.ky
	 */
	private class OutstandingReport implements Serializable, Entity {
		private static final long serialVersionUID = 8884453724837698785L;
		private Long id;
		private Applicant applicant;
		private String contractReference;
		private Double outstandingAmount;
		
		/**
		 * @return the applicant
		 */
		public Applicant getApplicant() {
			return applicant;
		}
		/**
		 * @param applicant the applicant to set
		 */
		public void setApplicant(Applicant applicant) {
			this.applicant = applicant;
		}
		/**
		 * 
		 * @return
		 */
		public Long getId() {
			return id;
		}
		
		/**
		 * 
		 * @param id
		 */
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * 
		 * @return
		 */
		public String getContractReference() {
			return contractReference;
		}
		/**
		 * 
		 * @param contractReference
		 */
		public void setContractReference(String contractReference) {
			this.contractReference = contractReference;
		}
		
		/**
		 * 
		 * @return
		 */
		public Double getOutstandingAmount() {
			return outstandingAmount;
		}
		/**
		 * 
		 * @param outstandingAmount
		 */
		public void setOutstandingAmount(Double outstandingAmount) {
			this.outstandingAmount = outstandingAmount;
		}
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
