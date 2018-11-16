package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

/**
 * Other information panel
 * @author ly.youhort
 */
public class OtherInformationPanel extends AbstractControlPanel {
	
	private static final long serialVersionUID = 8341162309347917428L;
	
	private TextField txtMonthlyPersonalExpenses;
	private TextField txtMonthlyFamilyExpenses;
	private TextField txtTotalMonthlyExpenses;

	private TextField txtValueTotalDisposalIncome;	
	private OptionGroup optDebtOtherSource;
	private TextField txtTotalDebtMonthlyInstallment;
	private TextField txtConvenientVisitTime;
	
	private TextField txtTotalNumberFamilyMember;
	
	private CustomLayout customLayout;

	public OtherInformationPanel() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		
		String template = "applicantOtherInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
				
		txtMonthlyPersonalExpenses = ComponentFactory.getTextField(false, 60, 150);
		txtMonthlyPersonalExpenses.setImmediate(true);
		txtMonthlyFamilyExpenses = ComponentFactory.getTextField(false, 60, 150);
		txtMonthlyFamilyExpenses.setImmediate(true);
		txtTotalMonthlyExpenses = ComponentFactory.getTextField(false, 60, 150);
		txtTotalMonthlyExpenses.setEnabled(false);
		txtTotalMonthlyExpenses.addStyleName("blackdisabled");
		
		txtMonthlyPersonalExpenses.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1522323948219241644L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				txtTotalMonthlyExpenses.setValue(getDefaultString(getTotalMonthlyExpenses()));
			}
		});
		txtMonthlyFamilyExpenses.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2996851767138046538L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				txtTotalMonthlyExpenses.setValue(getDefaultString(getTotalMonthlyExpenses()));
			}
		});
		
		
		
		txtValueTotalDisposalIncome = ComponentFactory.getTextField(false, 60, 150);
		txtValueTotalDisposalIncome.setEnabled(false);
		txtValueTotalDisposalIncome.addStyleName("blackdisabled");
		
		txtTotalNumberFamilyMember = ComponentFactory.getTextField(false, 50, 100); 
		
		optDebtOtherSource = new OptionGroup();
		optDebtOtherSource.addItem(1);
		optDebtOtherSource.setItemCaption(1, I18N.message("yes"));
		optDebtOtherSource.addItem(0);
		optDebtOtherSource.setItemCaption(0, I18N.message("no"));
		optDebtOtherSource.select(0);
		optDebtOtherSource.addStyleName("horizontal");
				
		txtTotalDebtMonthlyInstallment = ComponentFactory.getTextField(false, 60, 100);
		
		txtConvenientVisitTime = ComponentFactory.getTextField(false, 60, 150);
		txtConvenientVisitTime.setWidth("350px");
				
		customLayout.addComponent(new Label(I18N.message("monthly.personal.expenses")), "lblMonthlyPersonalExpenses");
		customLayout.addComponent(txtMonthlyPersonalExpenses, "txtMonthlyPersonalExpenses");
		customLayout.addComponent(new Label(I18N.message("monthly.family.expenses")), "lblMonthlyFamilyExpenses");
		customLayout.addComponent(txtMonthlyFamilyExpenses, "txtMonthlyFamilyExpenses");
		customLayout.addComponent(new Label(I18N.message("total.expenses")), "lblTotalMonthlyExpenses");
		customLayout.addComponent(txtTotalMonthlyExpenses, "txtTotalMonthlyExpenses");		
		
		customLayout.addComponent(new Label(I18N.message("disposal.income.member")), "lblDisposalIncome");
				
		customLayout.addComponent(new Label(I18N.message("debt.from.other.source")), "lblDebtOtherSource");
		customLayout.addComponent(optDebtOtherSource, "optDebtOtherSource");
				
		customLayout.addComponent(new Label(I18N.message("total.debt.monthly.installment")), "lblTotalDebtMonthlyInstallment");
		customLayout.addComponent(txtTotalDebtMonthlyInstallment, "txtTotalDebtMonthlyInstallment");
		
		customLayout.addComponent(new Label(I18N.message("convenient.time.for.visit")), "lblConvenientVisitTime");
		customLayout.addComponent(txtConvenientVisitTime, "txtConvenientVisitTime");
		customLayout.addComponent(new Label(I18N.message("total.accumulate.Household.income")), "lblTotalDisposalIncome");
				
		customLayout.addComponent(txtValueTotalDisposalIncome, "txtValueTotalDisposalIncome");
		
		
		customLayout.addComponent(new Label(I18N.message("total.family.member")), "lblTotalFamilyMember");
		customLayout.addComponent(txtTotalNumberFamilyMember, "txtTotalNumberFamilyMember");
				
		addComponent(customLayout);
	}

	/**
	 * Get total monthly expenses
	 * @return
	 */
	private double getTotalMonthlyExpenses() {
		double monthlyPersonalExpenses = getDouble(txtMonthlyPersonalExpenses, 0.0);
		double monthlyFamilyExpenses = getDouble(txtMonthlyFamilyExpenses, 0.0);
		return monthlyPersonalExpenses + monthlyFamilyExpenses;
	}

	/**
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		Individual individual = applicant.getIndividual();
		individual.setMonthlyPersonalExpenses(getDouble(txtMonthlyPersonalExpenses));
		individual.setConvenientVisitTime(txtConvenientVisitTime.getValue());
		
		individual.setMonthlyFamilyExpenses(getDouble(txtMonthlyFamilyExpenses));
		
		individual.setTotalFamilyMember(getInteger(txtTotalNumberFamilyMember));
		
		if (optDebtOtherSource.getValue() != null) {
			individual.setDebtFromOtherSource(Integer.parseInt(optDebtOtherSource.getValue().toString()) == 1);
			individual.setTotalDebtInstallment(getDouble(txtTotalDebtMonthlyInstallment));
		}
		
		return applicant;
	}
	
	/**
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {
		Individual individual = applicant.getIndividual();
		txtMonthlyPersonalExpenses.setValue(AmountUtils.format(individual.getMonthlyPersonalExpenses()));
		txtMonthlyFamilyExpenses.setValue(AmountUtils.format(individual.getMonthlyFamilyExpenses()));
		txtTotalMonthlyExpenses.setValue(AmountUtils.format(MyNumberUtils.getDouble(individual.getMonthlyPersonalExpenses()) + MyNumberUtils.getDouble(individual.getMonthlyFamilyExpenses())));
		txtConvenientVisitTime.setValue(getDefaultString(individual.getConvenientVisitTime()));		
		optDebtOtherSource.setValue(BooleanUtils.toBoolean(individual.isDebtFromOtherSource()) ? 1 : 0);	
		txtTotalDebtMonthlyInstallment.setValue(AmountUtils.format(individual.getTotalDebtInstallment()));
		
		txtTotalNumberFamilyMember.setValue(getDefaultString(individual.getTotalFamilyMember()));

	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Applicant());
	}
	
	/**
	 * @return
	 */
	public List<String> validate() {
		super.reset();
		checkDoubleField(txtTotalDebtMonthlyInstallment, "total.debt.monthly.installment");
		return errors;
	}

	/**
	 * @return
	 */
	public List<String> fullValidate() {
		super.reset();
		checkMandatoryField(txtMonthlyFamilyExpenses, "monthly.family.expenses");
		checkMandatoryField(txtConvenientVisitTime, "convenient.time.for.visit");
		checkMandatorySelectField(optDebtOtherSource, "debt.from.other.source");
		checkMandatoryField(txtTotalNumberFamilyMember, "total.family.member");

		if (optDebtOtherSource.getValue() != null) {
			if (Integer.parseInt(optDebtOtherSource.getValue().toString()) == 1) {
				checkMandatoryField(txtTotalDebtMonthlyInstallment, "total.debt.monthly.installment");
			}
		}
		return errors;
	}
}
