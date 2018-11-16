package com.nokor.efinance.gui.ui.panel.report.numpos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Report on the performance contract
 * @author tha.bunsath
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PerformanceContractReportPanel.NAME)
public class PerformanceContractReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {

	private static final long serialVersionUID = -5129432824367235949L;
	public static final String NAME = "Performances";
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private Button btnSearch;
	private Button btnReset;
	private EntityRefComboBox<Province> cbxProvince;
	private PagedTable<Contract> tableNumContract;
	//private SimplePagedTable<Contract> tableNumContract;
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
		dfStartDate.setDateFormat("yyyy/MM/dd");
		dfStartDate.setValue(DateUtils.minusMonth(DateUtils.today(), 12));
		dfEndDate = ComponentFactory.getAutoDateField("enddate", false);
		dfEndDate.setDateFormat("yyyy/MM/dd");
		dfEndDate.setValue(DateUtils.today());
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.renderer();

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
		
		HorizontalLayout horizontalLayoutProvine = new HorizontalLayout();
		horizontalLayoutProvine.addComponent(new FormLayout(new Label(I18N.message("province"))));
		horizontalLayoutProvine.addComponent(new FormLayout(cbxProvince));
		
		HorizontalLayout horButtonLayout = new HorizontalLayout();
		horButtonLayout.setSpacing(true);
		horButtonLayout.addComponent(btnSearch);
		horButtonLayout.addComponent(btnReset);
		
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.addComponent(horizontalLayoutProvine);
		verticalLayout.addComponent(horButtonLayout);
		panelSearch.setContent(verticalLayout);

		tableNumContract=new PagedTable<Contract>(createColumnDefinitions());
		tableNumContract.setCaption("Performance");
		
		//--- add all component to form
		contentLayout.addComponent(panelSearch);
		contentLayout.addComponent(tableNumContract);
		contentLayout.addComponent(tableNumContract.createControls());
		tabSheet.addTab(contentLayout,"Performance");
		
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
		
		columnDefinitions.add(new ColumnDefinition("rank", I18N.message("rank") , Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("province", I18N.message("province") , String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("dealerType", I18N.message("dealer.type") , String.class, Align.CENTER, 100));
		columnDefinitions.add(new ColumnDefinition("dealer", I18N.message("dealer") , String.class, Align.LEFT, 250));		
		columnDefinitions.add(new ColumnDefinition("count_contract", I18N.message("number.contract") , Long.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("total_fin", I18N.message("portfolio.amount") , Double.class, Align.CENTER, 150));
			
		return columnDefinitions;
	 }
	
	/**
	 * BaseRestrcitions
	 * @return BaseRestrcitions
	 */
	
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		
		restrictions.addAssociation("dealer", "condeal", JoinType.INNER_JOIN);
		restrictions.addAssociation("condeal.dealerAddresses", "dealAddr", JoinType.INNER_JOIN);
		restrictions.addAssociation("dealAddr.address", "dealAddress", JoinType.INNER_JOIN);
		restrictions.addAssociation("dealAddress.province", "dealAddrPro", JoinType.INNER_JOIN);
		restrictions.addCriterion("dealAddr.addressType", ETypeAddress.MAIN);

		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("condeal.id", cbxDealer.getSelectedEntity().getId()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("condeal.dealerType", cbxDealerType.getSelectedEntity()));
		} 
		//else {
//			restrictions.addCriterion(Restrictions.ne("condeal.dealerType", DealerType.OTH));
//		}				
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealAddress.province.id",cbxProvince.getSelectedEntity().getId()));
		}
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("creationDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("creationDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
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
		int cs=contracts.size();		
		int rank=1;
		List <TempingContract> tempContract = new ArrayList<TempingContract>();
		
		for(int k=1;k<=cs;k++){
			String dealer="";
			String dealerType="";
			long count_contract=0;
			long dealer_id=0;
			double total_fin=0;
			String dealer_province="";
		
		for (Contract contract : contracts) {
				if(contract.getDealer().getId()==k){
					dealer_id=contract.getDealer().getId();
					dealer=contract.getDealer().getNameEn();							
					dealerType=contract.getDealer().getDealerType().getDesc();
					Province province = contract.getDealer().getMainAddress().getProvince();
					dealer_province = province.getDescEn(); 
					count_contract +=1;
					total_fin+= contract.getTeFinancedAmount();			
					}	
		      }
				if(dealer_id!=0){
					tempContract.add(new TempingContract(dealer_province, dealer, dealerType, total_fin, count_contract)); 
				}   
		}//close k
		
		Collections.sort(tempContract, SortingContract.DESC);
		for(TempingContract co : tempContract){
			Item item = indexedContainer.addItem(rank);
	 		item.getItemProperty("rank").setValue(rank);
	 		item.getItemProperty("province").setValue(co.getProvince());
			item.getItemProperty("dealer").setValue(co.getDeler());
			item.getItemProperty("dealerType").setValue(co.getDeler_type());
			item.getItemProperty("count_contract").setValue(co.getCount_contra());
			item.getItemProperty("total_fin").setValue(co.getTotal_fin());	
			
			rank += 1;	
		}
		tableNumContract.refreshContainerDataSource();
		}
	/**
	 * Get the button click listener for the search and resbutton
	 * @return Button Click Listener
	 */
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = -774154222891913537L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == btnSearch) { 
					onSearch();
				} else {
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

	/*
	 * create class tempingContract to temp data from Contract
	 * 
	 */
  class TempingContract implements Comparable<TempingContract> {
	  	private String province;
	  	private String deler;
	  	private String deler_type;
	  	private double total_fin;
	  	private long count_contra;
	  
		  @Override
		public int compareTo(TempingContract con) {
				 return Long.compare(count_contra, con.count_contra);
		  }
		public long getCount_contra() {
			return count_contra;
		  }
		public void setCount_contra(long count_contra) {
			  this.count_contra = count_contra;
		  }
		public TempingContract(String province,String deler,String deler_type,double total_contract,long count_contra){
			  this.province=province;
			  this.deler=deler;
			  this.deler_type=deler_type;
			  this.total_fin=total_contract;
			  this.count_contra=count_contra;  
		  }
		public String getProvince() {
			  return province;
		  }
		public void setProvince(String province) {
			  this.province = province;
		  }
		public String getDeler() {
			  return deler;
		  }
		public void setDeler(String deler) {
			 this.deler = deler;
		 }
		public String getDeler_type() {
			return deler_type;
		 }
		public void setDeler_type(String deler_type) {
			this.deler_type = deler_type;
		 }
		public double getTotal_fin() {
			return total_fin;
		 }
		public void setTotal_fin(double total_fin) {
			this.total_fin = total_fin;
		 }
  	}
 /*
  *	create shorting contract 
  */
  class SortingContract implements Comparator<TempingContract> {
		public static final SortingContract ASC =  new SortingContract(1);
		public static final SortingContract DESC = new SortingContract(-1);
		private final int order;
		private SortingContract(int order) {
			this.order = order;
	    }
		
	  @Override
	  public int compare(TempingContract a, TempingContract b) {
		  return order * (Long.compare(a.getCount_contra(), b.getCount_contra()));
	  }
	}
