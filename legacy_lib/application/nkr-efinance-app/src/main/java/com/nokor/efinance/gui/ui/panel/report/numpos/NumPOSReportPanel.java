package com.nokor.efinance.gui.ui.panel.report.numpos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
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
 * Report number of POS
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(NumPOSReportPanel.NAME)
public class NumPOSReportPanel extends AbstractTabPanel implements View, DealerEntityField, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = 3445944403267010596L;
	public static final String NAME = "numpos.report";
	private static final String MONTH = "month";
	private static final String NUM_POS_PER_MONTH = "num_pos_per_month";
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Button btnSearch;
	private Button btnReset;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	
	private SimplePagedTable<Dealer> tableDealer;

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
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(getButtonClickListener());	// add button click listener
		btnReset = ComponentFactory.getButton("reset");
		btnReset.addClickListener(getButtonClickListener());
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 2889037263661750301L;
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
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		Panel panelSearch = new Panel();
		panelSearch.setCaption(I18N.message("search"));
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		
		HorizontalLayout horDateLayout = new HorizontalLayout();
		horDateLayout.setSpacing(true);
		horDateLayout.addComponent(new FormLayout(dfStartDate));
		horDateLayout.addComponent(new FormLayout(dfEndDate));
		horDateLayout.addComponent(new FormLayout(cbxDealerType));
		horDateLayout.addComponent(new FormLayout(cbxDealer));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnSearch);
		horizontalLayout.addComponent(btnReset);
		verticalLayout.addComponent(horDateLayout);
		verticalLayout.addComponent(horizontalLayout);
		panelSearch.setContent(verticalLayout);
		
		//--- Dealer table
		tableDealer = new SimplePagedTable<Dealer>(createColumnDefinitions());
		tableDealer.setCaption(I18N.message("dealers"));
		
		//--- add all component to form
		contentLayout.addComponent(panelSearch);
		contentLayout.addComponent(tableDealer);
		contentLayout.addComponent(tableDealer.createControls());
		tabSheet.addTab(contentLayout, I18N.message("numbers.pos"));
		
		return tabSheet;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	/**
	 * Create ColumnDefinition for table
	 * @return List<ColumnDefinition>
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		
		columnDefinitions.add(new ColumnDefinition(MONTH, I18N.message("month"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NUM_POS_PER_MONTH, I18N.message("numbers.pos"), String.class, Align.LEFT, 100));
		
		return columnDefinitions;
	}
	
	/**
	 * Set the container data source to table
	 */
	@SuppressWarnings("unchecked")
	private void setContainerDataSources(List<Dealer> dealers) {
		Container indexedContainer = tableDealer.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		Date startDate = DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue());
		Date endDate = DateUtils.getDateAtEndOfMonth(dfEndDate.getValue());
		int numberOfMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		for (int i = 0; i < numberOfMonth; i++) {
			Date beginMonthSearch = DateUtils.addMonthsDate(startDate, i);
			Date endMonthSearch = DateUtils.getDateAtEndOfMonth(DateUtils.addMonthsDate(startDate, i));
			Integer numPos = 0;
			for (Dealer dealer : dealers) {
				if (DateUtils.isBeforeDay(dealer.getCreateDate(), endMonthSearch)) {
					numPos += 1;
				}
			}
			SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
			Item item = indexedContainer.addItem(i);
			item.getItemProperty(MONTH).setValue(df.format(beginMonthSearch));
			item.getItemProperty(NUM_POS_PER_MONTH).setValue(numPos + " POS");
		}
		tableDealer.refreshContainerDataSource();
	}
	
	/**
	 * Get the restriction of dealer
	 * @return BaseRestrictions<Dealer>
	 */
	public BaseRestrictions<Dealer> getRestrictions(Date dtStartDate, Date dtEndDate, EDealerType dt, Dealer dealer) {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		if (dt != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", dt));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("id", dealer.getId()));
		}
		restrictions.addCriterion(Restrictions.le(CREATE_DATE, DateUtils.getDateAtEndOfMonth(dtEndDate)));
		return restrictions;
	}
	
	/**
	 * Button ClickLitener of Search and Reset button
	 * @return ClickListener
	 */
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = 2293567146215243102L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == btnSearch) {	// Button search is clicked
					onSearch();
				} else {								// Button Reset
					onReset();
				}
			}
		};
	}
	
	/**
	 * Perform search
	 */
	private void onSearch() {
		Date startDate = dfStartDate.getValue();
		Date endDate = dfEndDate.getValue();
		EDealerType dt = cbxDealerType.getSelectedEntity();
		Dealer dealer = cbxDealer.getSelectedEntity();
		List<Dealer> dealers = ENTITY_SRV.list(getRestrictions(startDate, endDate, dt, dealer));
		setContainerDataSources(dealers);
	}
	
	/**
	 * Reset the form
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
