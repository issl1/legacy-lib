package com.nokor.efinance.gui.ui.panel.report.ratio;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Report on the ratio of application approve to total application
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(RatioNumApplicationApproveToTotalApplication.NAME)
public class RatioNumApplicationApproveToTotalApplication extends AbstractTabPanel implements View, QuotationEntityField, FrmkServicesHelper {
	
	/** */
	private static final long serialVersionUID = -6153259598728961685L;
	public static final String NAME = "ratio.num.app.apply.to.num.app";
	private static final String MONTH = "month";
	private static final String RATIO = "ratio";
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private Button btnSearch;
	private Button btnReset;
	
	private SimplePagedTable<Contract> tableRatio;
	
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
			private static final long serialVersionUID = -5422266816011785470L;
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
		tableRatio = new SimplePagedTable<Contract>(createColumnDefinitions());
		tableRatio.setCaption(I18N.message("numbers.application.applied.to.total.application"));
		
		//--- add all component to form
		contentLayout.addComponent(panelSearch);
		contentLayout.addComponent(tableRatio);
		contentLayout.addComponent(tableRatio.createControls());
		tabSheet.addTab(contentLayout, I18N.message("numbers.application.applied.to.total.application"));
		
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
	 * Create the ColumnDefinition
	 * @return List of ColumnDefinition
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition(MONTH, I18N.message("month"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(RATIO, I18N.message("ratio.application.applied"), String.class, Align.LEFT, 300));
		
		return columnDefinitions;
	}
	
	/**
	 * Get the button click listener for the button search and reset
	 * @return Button Click listener
	 */
	private Button.ClickListener getButtonClickListener() {
		return new Button.ClickListener() {
			/** */
			private static final long serialVersionUID = -4929392639207789919L;
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
	@SuppressWarnings("unchecked")
	private void onSearch() {
		Container indexedContainer = tableRatio.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		Date startDate = DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue());
		Date endDate = DateUtils.getDateAtEndOfMonth(dfEndDate.getValue());
		Dealer dealer = cbxDealer.getSelectedEntity();
		EDealerType dt = cbxDealerType.getSelectedEntity();
		int numberOfMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		for (int i = 0; i < numberOfMonth; i++) {
			Date searchDate = DateUtils.addMonthsDate(startDate, i);
			long numApplicationApproved = getNbApplicationsFromBeginOfMonth(searchDate, dealer, dt, true);
			long numApplication = getNbApplicationsFromBeginOfMonth(searchDate, dealer, dt, false);
			
			SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
			Item item = indexedContainer.addItem(i);
			item.getItemProperty(MONTH).setValue(df.format(DateUtils.addMonthsDate(startDate, i)));
			double ratio = numApplication == 0 ? 0 : ((double) numApplicationApproved / numApplication) * 100;
			DecimalFormat decf = new DecimalFormat("0.00");
			item.getItemProperty(RATIO).setValue(decf.format(ratio) + "%");
		}
		
		tableRatio.refreshContainerDataSource();
	}
	
	/**
	 * Get Number of application from the beginning of the month
	 * @param date The date to be searched
	 * @param dealer The dealer to be searched
	 * @param needApproved Whether the application needed is approved
	 * @return The number of application
	 */
	private long getNbApplicationsFromBeginOfMonth(Date date, Dealer dealer, EDealerType dt, boolean needApproved) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.ge("quotationDate", DateUtils.getDateAtBeginningOfMonth(date)));
		restrictions.addCriterion(Restrictions.le("quotationDate", DateUtils.getDateAtEndOfMonth(date)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id", dealer.getId()));	
		}
		if (dt != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", dt));
		}
		if (needApproved) {
			restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.APV));
		}
		List<Quotation> quotations = ENTITY_SRV.list(restrictions);
		if (quotations == null) return 0;
		return quotations.size();
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
