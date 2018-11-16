package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * DashboardPopup Table Panel
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DashboardPopupTablePanel.NAME)
public class DashboardPopupTablePanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "dashboard1";
		
	private SimplePagedTable<Statistic3HoursVisitor> pagedTable;
	private SimplePagedTable<Statistic3HoursVisitor> pagedTableHeader;
	private List<ColumnDefinition> columnDefinitions;
	private NumberField txtdealer11;
	private NumberField txtdealer14;
	private NumberField txtdealer17;
	private NumberField txtcompany11;
	private NumberField txtcompany14;
	private NumberField txtcompany17;
	private Indexed indexedContainer;
	private Dealer dealer;
	
	public DashboardPopupTablePanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public DashboardPopupTablePanel(Dealer dealer) {
		this.dealer = dealer;
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Statistic3HoursVisitor>(this.columnDefinitions);
        pagedTable.setPageLength(31);
        pagedTableHeader = new SimplePagedTable<Statistic3HoursVisitor>(createColumnDefinitionsHeader());
        pagedTableHeader.setHeight("20px");
        contentLayout.addComponent(pagedTableHeader);
        contentLayout.addComponent(pagedTable);
        return contentLayout;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Statistic3HoursVisitor> getRestrictions() {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<>(Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(DateUtils.getDateAtBeginningOfMonth(DateUtils.today()))));
		restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(DateUtils.getDateAtEndOfMonth(DateUtils.today()))));
		restrictions.addOrder(Order.asc("date"));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		}
		return restrictions;
	}

	/**
	 * Search
	 */
	public void search() {
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
	}

	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Statistic3HoursVisitor> statistic3HoursVisitors) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		Date beginningDate = DateUtils.getDateAtBeginningOfMonth(DateUtils.today());
		int numDateInMonth = DateUtils.getNbDaysInMonth(DateUtils.today());
		int index = 0;
		for (int i = 0; i < numDateInMonth; i++) {
			Date date = DateUtils.addDaysDate(beginningDate, i);
			final Item item = indexedContainer.addItem(i);
			txtdealer11 = ComponentFactory.getNumberField();
			txtdealer11.setWidth("60px");
			txtdealer14 = ComponentFactory.getNumberField();
			txtdealer14.setWidth("60px");
			txtdealer17 = ComponentFactory.getNumberField();
			txtdealer17.setWidth("60px");
			txtcompany11 = ComponentFactory.getNumberField();
			txtcompany11.setWidth("60px");
			txtcompany14 = ComponentFactory.getNumberField();
			txtcompany14.setWidth("60px");
			txtcompany17 = ComponentFactory.getNumberField();
			txtcompany17.setWidth("60px");
			//disable all TextField
			txtdealer11.setEnabled(true);
			txtdealer14.setEnabled(true);
			txtdealer17.setEnabled(true);
			txtcompany11.setEnabled(false);
			txtcompany14.setEnabled(false);
			txtcompany17.setEnabled(false);

			if (statistic3HoursVisitors != null && !statistic3HoursVisitors.isEmpty()) {
				Statistic3HoursVisitor statistic3HoursVisitor = findStatistic3HoursVisitor(statistic3HoursVisitors, date);
				if (statistic3HoursVisitor != null) {
					txtdealer11.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorDealer11()));
					txtdealer14.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorDealer14()));
					txtdealer17.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorDealer17()));
					txtcompany11.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorCompany11()));
					txtcompany14.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorCompany14()));
					txtcompany17.setValue(getDefaultString(statistic3HoursVisitor.getNumberVisitorCompany17()));
				}
			}
			//Enable the TextField of the current date
			if (DateUtils.isSameDay(date, DateUtils.today())) {
				txtdealer11.setEnabled(true);
				txtdealer14.setEnabled(true);
				txtdealer17.setEnabled(true);
				txtcompany11.setEnabled(false);
				txtcompany14.setEnabled(false);
				txtcompany17.setEnabled(false);
			}
			//dfPaymentDealerDate.setValue(DateUtils.today());
			item.getItemProperty("index").setValue(index);
			item.getItemProperty("date").setValue(DateUtils.getDateLabel(date, "dd/MM/yyyy"));
			item.getItemProperty("dealer.11am").setValue(txtdealer11);
			item.getItemProperty("dealer.2pm").setValue(txtdealer14);
			item.getItemProperty("dealer.5pm").setValue(txtdealer17);
			item.getItemProperty("company.11am").setValue(txtcompany11);
			item.getItemProperty("company.2pm").setValue(txtcompany14);
			item.getItemProperty("company.5pm").setValue(txtcompany17);
			index++;
		}
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), String.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("dealer.11am", I18N.message("11am"), TextField.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("dealer.2pm", I18N.message("2pm"), TextField.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("dealer.5pm", I18N.message("5pm"), TextField.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("company.11am", I18N.message("11am"), TextField.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("company.2pm", I18N.message("2pm"), TextField.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("company.5pm", I18N.message("5pm"), TextField.class, Align.CENTER, 70));
		
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> createColumnDefinitionsHeader() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition("index.empty", I18N.message("emplty"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition("empty", I18N.message(""), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("visitor.dealer", I18N.message("visitor.dealer"), Amount.class, Align.CENTER, 236));
		columnDefinitions.add(new ColumnDefinition("visitor.company", I18N.message("visitor.company"), Amount.class, Align.CENTER, 236));
		
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Statistic3HoursVisitor> getStatistic3HoursVisitors() {
		int numDateInMonth = DateUtils.getNbDaysInMonth(DateUtils.today());
		List<Statistic3HoursVisitor> statistic3HoursVisitors = ENTITY_SRV.list(getRestrictions());
		if (statistic3HoursVisitors == null) {
			statistic3HoursVisitors = new ArrayList<>();
		}
		for (int i = 0; i < numDateInMonth; i++) {
			Date beginningDate = DateUtils.getDateAtBeginningOfMonth(DateUtils.today());
			Date date = DateUtils.addDaysDate(beginningDate, i);
			Statistic3HoursVisitor statistic3HoursVisitor = findStatistic3HoursVisitor(statistic3HoursVisitors, date);
			if (statistic3HoursVisitor == null) {
				statistic3HoursVisitor = new Statistic3HoursVisitor();
				statistic3HoursVisitors.add(statistic3HoursVisitor);
			}
			final Item item = indexedContainer.getItem(i);
			statistic3HoursVisitor.setDate(date);
			statistic3HoursVisitor.setDealer(dealer);
			statistic3HoursVisitor.setNumberVisitorDealer11(getInteger(item.getItemProperty(
					"dealer.11am").getValue()));
			statistic3HoursVisitor.setNumberVisitorDealer14(getInteger(item.getItemProperty(
					"dealer.2pm").getValue()));
			statistic3HoursVisitor.setNumberVisitorDealer17(getInteger(item.getItemProperty(
					"dealer.5pm").getValue()));
			statistic3HoursVisitor.setNumberVisitorCompany11(getInteger(item.getItemProperty(
					"company.11am").getValue()));
			statistic3HoursVisitor.setNumberVisitorCompany14(getInteger(item.getItemProperty(
					"company.2pm").getValue()));
			statistic3HoursVisitor.setNumberVisitorCompany17(getInteger(item.getItemProperty(
					"company.5pm").getValue()));
		}
		return statistic3HoursVisitors;
	}
	
	/**
	 * Find statistic3HoursVisitor base on date 
	 * @param statistic3HoursVisitors List of statistic3HoursVisitor to be searched
	 * @param date The date for searching
	 * @return Statistic3HoursVisitor if found, else null
	 */
	private Statistic3HoursVisitor findStatistic3HoursVisitor(List<Statistic3HoursVisitor> statistic3HoursVisitors, Date date ) {
		for (Statistic3HoursVisitor statistic3HoursVisitor : statistic3HoursVisitors) {
			Date date1 = DateUtils.getDateWithoutTime(statistic3HoursVisitor.getDate());
			if (DateUtils.isSameDay(date1, date)) {
				return statistic3HoursVisitor;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	private int getInteger(Object obj) {
		if (obj != null && !("").equals(obj.toString())) {
			return Integer.parseInt(obj.toString());
		} else {
			return 0;
		}

	}
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}
	
}
