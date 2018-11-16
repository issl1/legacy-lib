package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class SchedulePanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -2131806810130481378L;

	private List<ColumnDefinition> columnDefinitions;	
	private SimpleTable<Contract> simpleTable;
	private FinanceCalculationService financeCalculationService;
	private Panel schedulePanel;
	
	/**
	 * 
	 */
	public SchedulePanel() {
		schedulePanel = new Panel();
		schedulePanel.setStyleName(Reindeer.PANEL_LIGHT);
		financeCalculationService = SpringUtils.getBean(FinanceCalculationService.class);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Contract>(this.columnDefinitions);
		simpleTable.setPageLength(5);
		simpleTable.setSizeFull();
		
		schedulePanel.setCaption(I18N.message("schedule"));
		schedulePanel.setContent(simpleTable);
		addComponent(schedulePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("no", I18N.message("no."), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("due.date", I18N.message("due.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("installment", I18N.message("installment"), Amount.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("capital", I18N.message("capital"), Amount.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("interest", I18N.message("interest"), Amount.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("balance", I18N.message("balance"), Amount.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible) {
		schedulePanel.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param schedules
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(double initialPrincipal, List<Schedule> schedules) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		Item item = indexedContainer.addItem(0);
		item.getItemProperty("no").setValue(0);
		item.getItemProperty("balance").setValue(AmountUtils.convertToAmount(initialPrincipal));
		for (Schedule schedule : schedules) {
			item = indexedContainer.addItem(schedule.getN());
			item.getItemProperty("no").setValue(schedule.getN());
			item.getItemProperty("due.date").setValue(schedule.getInstallmentDate());
			item.getItemProperty("installment").setValue(AmountUtils.convertToAmount(schedule.getInstallmentPayment()));
			item.getItemProperty("capital").setValue(AmountUtils.convertToAmount(schedule.getPrincipalAmount()));
			item.getItemProperty("interest").setValue(AmountUtils.convertToAmount(schedule.getInterestAmount()));
			item.getItemProperty("balance").setValue(AmountUtils.convertToAmount(schedule.getBalanceAmount()));
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void createSchedule(Contract contract) {
		Contract con = CONT_SRV.getById(Contract.class, contract.getId());
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setInitialPrincipal(con.getTiFinancedAmount());
		calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(con.getTerm(), con.getFrequency()));
		calculationParameter.setPeriodicInterestRate(con.getInterestRate() / 100d);
		calculationParameter.setFrequency(con.getFrequency());
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(con.getNumberOfPrincipalGracePeriods()));
	
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(con.getStartDate(), con.getFirstDueDate(), calculationParameter);
		List<Schedule> schedules = amortizationSchedules.getSchedules();
		setIndexedContainer(calculationParameter.getInitialPrincipal(), schedules);
		setVisible(ContractUtils.isHoldPayment(con));
	}
}
