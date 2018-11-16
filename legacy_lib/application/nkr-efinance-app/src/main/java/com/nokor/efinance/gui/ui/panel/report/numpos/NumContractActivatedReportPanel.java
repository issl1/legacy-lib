package com.nokor.efinance.gui.ui.panel.report.numpos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Report on the number of contract activated
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(NumContractActivatedReportPanel.NAME)
public class NumContractActivatedReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = -5129432824367235949L;
	public static final String NAME = "num.contract.activted";
	private static final String MONTH = "month";
	private static final String NUM_CONTRACT_ACTIVATED = "num_constract_activiated";
	private static final String NUM_CONTRACT_REPOSSESSED ="num_constract_repossessed";
	private static final String NUM_CONTRACT_THEFT="num_constract_theft";
	private static final String NUM_CONTRACT_ACCIDENT= "num_constract_accident";
	private static final String NUM_CONTRACT_WRITE_OFF = "num_constract_write_off";
	private static final String NUM_CONTRACT_PAID_OFF = "num_constract_paid_off";
	private static final String NUM_CONTRACT_TERMINATED = "num_constract_terminated";
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private Button btnSearch;
	private Button btnReset;
	
	private SimplePagedTable<Contract> tableNumContract;


	/**
	 * (non-Javadoc)
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		TabSheet tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		//--- Search
		dfStartDate = ComponentFactory.getAutoDateField("startdate", false);
		dfStartDate.setDateFormat("MM/yyyy");
		dfStartDate.setValue(DateUtils.minusMonth(DateUtils.today(), 12));
		dfEndDate = ComponentFactory.getAutoDateField("enddate", false);
		dfEndDate.setDateFormat("MM/yyyy");
		dfEndDate.setValue(DateUtils.today());
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");

		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 3210736844671745372L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(getButtonClickListener());	// add button click listener
		btnReset = ComponentFactory.getButton("reset");
		btnReset.addClickListener(getButtonClickListener());
		
		Panel panelSearch = new Panel();
		panelSearch.setCaption(I18N.message("search"));
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(dfStartDate));
		horizontalLayout.addComponent(new FormLayout(dfEndDate));
		horizontalLayout.addComponent(new FormLayout(cbxDealerType));
		horizontalLayout.addComponent(new FormLayout(cbxDealer));
		
		HorizontalLayout horButtonLayout = new HorizontalLayout();
		horButtonLayout.setSpacing(true);
		horButtonLayout.addComponent(btnSearch);
		horButtonLayout.addComponent(btnReset);
		
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.addComponent(horButtonLayout);
		panelSearch.setContent(verticalLayout);
		
		//--- Dealer table
		tableNumContract = new SimplePagedTable<Contract>(createColumnDefinitions());
		tableNumContract.setCaption(I18N.message("quotations"));
		
		//--- add all component to form
		contentLayout.addComponent(panelSearch);
		contentLayout.addComponent(tableNumContract);
		contentLayout.addComponent(tableNumContract.createControls());
		tabSheet.addTab(contentLayout, I18N.message("activated"));
		
		return tabSheet;
	}
	
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	/**
	 * Create a list of ColumnDefinition
	 * @return List<ColumnDefinition>
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition(MONTH, I18N.message("month"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_ACTIVATED, I18N.message("num.contract.activated"), String.class, Align.LEFT, 200));
		
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_REPOSSESSED, I18N.message("num.contract.repossessed"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_THEFT, I18N.message("num.contract.theft"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_ACCIDENT, I18N.message("num.contract.accident"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_WRITE_OFF, I18N.message("num.contract.write_off"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_PAID_OFF, I18N.message("num.contract.paid_off"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(NUM_CONTRACT_TERMINATED, I18N.message("num.contract.terminated"), String.class, Align.LEFT, 200));
		
		return columnDefinitions;
	}
	
	/**
	 * BaseRestrcitions
	 * @return BaseRestrcitions
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addAssociation("dealer", "condeal", JoinType.INNER_JOIN);
		Date dtStartDate = DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue());
		Date dtEndDate = DateUtils.getDateAtEndOfMonth(dfEndDate.getValue());
		
		restrictions.addCriterion(Restrictions.ge(START_DATE, dtStartDate));
		restrictions.addCriterion(Restrictions.le(START_DATE, dtEndDate));
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer", (Dealer) cbxDealer.getSelectedEntity()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("condeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		return restrictions;
	}
	
	/**
	 * Set the container data source of the table
	 * @param contracts The list of contracts to be set in the container
	 */
	@SuppressWarnings("unchecked")
	private void setContainerDataSources(List<Contract> contracts) {
		Container indexedContainer = tableNumContract.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		Date startDate = DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue());
		Date endDate = DateUtils.getDateAtEndOfMonth(dfEndDate.getValue());
		int numberOfMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		
		java.util.Calendar cal = GregorianCalendar.getInstance();
				
		cal.setTime( dfStartDate.getValue());
		cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		cal.set(GregorianCalendar.SECOND, 0);
		cal.set(GregorianCalendar.MILLISECOND, 0);
		startDate = cal.getTime();
								
		for (int i = 0; i < numberOfMonth; i++) {
			//Date beginMonthSearch = DateUtils.addMonthsDate(dfStartDate.getValue(), i);
			//Date endMonthSearch = DateUtils.getDateAtEndOfMonth(DateUtils.addMonthsDate(dfStartDate.getValue(), i));
			cal.add(GregorianCalendar.MONTH, i==0? 0: 1);			
			cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
						
			int beginSearchYear  = cal.get(GregorianCalendar.YEAR);
			int beginSearchMonth = cal.get(GregorianCalendar.MONTH);
						
			cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));					    
		    					 
			int com_count = 0;
			int fin_count = 0;
			int can_count = 0;
			int ear_count = 0;
			int pen_count = 0;
			int los_count = 0;
			int clo_count = 0;
			int rep_count = 0;
			int the_count = 0;
			int acc_count = 0;
			int fra_count = 0;
			int wri_count = 0;
						
			for (Contract contract : contracts) {
				java.util.Calendar cal2 = GregorianCalendar.getInstance();
				cal2.setTime(contract.getStartDate());
				int contractYear  = cal2.get(GregorianCalendar.YEAR);
				int contractMonth = cal2.get(GregorianCalendar.MONTH);
				
				if ((contractYear == beginSearchYear) && (contractMonth == beginSearchMonth)) {
					
					if (contract.getWkfStatus().equals(ContractWkfStatus.FIN)) {
						fin_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.CAN)) {
						can_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.EAR)) {
						ear_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.PEN)) {
						pen_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.LOS)) {
						los_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.CLO)) {
						clo_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.REP)) {
						rep_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.THE)) {
						the_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.ACC)) {
						acc_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.FRA)) {
						fra_count +=1;
					} else if (contract.getWkfStatus().equals(ContractWkfStatus.WRI)) {
						wri_count +=1;
					}
				}
			}
			SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
			Item item = indexedContainer.addItem(i);
			item.getItemProperty(MONTH).setValue(df.format(DateUtils.addMonthsDate(startDate, i)));
			//item.getItemProperty(NUM_CONTRACT_ACTIVATED).setValue(numContract + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_ACTIVATED).setValue(fin_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_REPOSSESSED).setValue(rep_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_THEFT).setValue(the_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_ACCIDENT).setValue(acc_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_WRITE_OFF).setValue(wri_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_PAID_OFF).setValue(ear_count + " contract(s)");
			item.getItemProperty(NUM_CONTRACT_TERMINATED).setValue(clo_count + " contract(s)");
			
		}
		
		tableNumContract.refreshContainerDataSource();
	}
	
	/**
	 * Get the button click listener for the search and reset button
	 * @return Button Click Listener
	 */
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = -774154222891913537L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == btnSearch) { // Button Search is clicked
					onSearch();
				} else {							 // Button Reset is Clicked
					onReset();
				}
			}
		};
	}
	
	/**
	 * Search
	 */
	private void onSearch() {
		List<Contract> contracts = ENTITY_SRV.list(getRestrictions());
		setContainerDataSources(contracts);
	}
	
	/**
	 * Reset
	 */
	private void onReset() {
		dfStartDate.setValue(DateUtils.minusMonth(DateUtils.today(), 12));
		dfEndDate.setValue(DateUtils.today());
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		onSearch();
	}

	/**
	 * (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		onSearch();
	}

}
