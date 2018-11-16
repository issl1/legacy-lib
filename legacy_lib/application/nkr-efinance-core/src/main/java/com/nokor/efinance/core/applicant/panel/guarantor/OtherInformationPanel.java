package com.nokor.efinance.core.applicant.panel.guarantor;

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
	
	private OptionGroup optDebtOtherSource;
	private TextField txtTotalDebtMonthlyInstallment;
	
	private OptionGroup optGuarantorOtherLoan;	
	private TextField txtConvenientVisitTime;
	
	public OtherInformationPanel() {
		setSizeFull();
		
		String template = "guarantorOtherInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
				
		optDebtOtherSource = new OptionGroup();
		optDebtOtherSource.addItem(1);
		optDebtOtherSource.setItemCaption(1, I18N.message("yes"));
		optDebtOtherSource.addItem(0);
		optDebtOtherSource.setItemCaption(0, I18N.message("no"));
		optDebtOtherSource.select(0);
		optDebtOtherSource.addStyleName("horizontal");
		txtTotalDebtMonthlyInstallment = ComponentFactory.getTextField(false, 60, 100);
		
		optGuarantorOtherLoan = new OptionGroup();
		optGuarantorOtherLoan.addItem(1);
		optGuarantorOtherLoan.setItemCaption(1, I18N.message("yes"));
		optGuarantorOtherLoan.addItem(0);
		optGuarantorOtherLoan.setItemCaption(0, I18N.message("no"));
		optGuarantorOtherLoan.select(0);
		optGuarantorOtherLoan.addStyleName("horizontal");
		
		txtConvenientVisitTime = ComponentFactory.getTextField(false, 60, 150);		
		txtConvenientVisitTime.setWidth("350px");
		
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
		
		customLayout.addComponent(new Label(I18N.message("monthly.personal.expenses")), "lblMonthlyPersonalExpenses");
		customLayout.addComponent(txtMonthlyPersonalExpenses, "txtMonthlyPersonalExpenses");
		customLayout.addComponent(new Label(I18N.message("monthly.family.expenses")), "lblMonthlyFamilyExpenses");
		customLayout.addComponent(txtMonthlyFamilyExpenses, "txtMonthlyFamilyExpenses");
		customLayout.addComponent(new Label(I18N.message("total.expenses")), "lblTotalMonthlyExpenses");
		customLayout.addComponent(txtTotalMonthlyExpenses, "txtTotalMonthlyExpenses");
			
		customLayout.addComponent(new Label(I18N.message("debt.from.other.source")), "lblDebtOtherSource");
		customLayout.addComponent(optDebtOtherSource, "optDebtOtherSource");
		
		customLayout.addComponent(new Label(I18N.message("total.debt.monthly.installment")), "lblTotalDebtMonthlyInstallment");
		customLayout.addComponent(txtTotalDebtMonthlyInstallment, "txtTotalDebtMonthlyInstallment");
		
		customLayout.addComponent(new Label(I18N.message("guarantor.other.loan")), "lblGuarantorOtherLoan");
		customLayout.addComponent(optGuarantorOtherLoan, "optGuarantorOtherLoan");
		
		customLayout.addComponent(new Label(I18N.message("convenient.time.for.visit")), "lblConvenientVisitTime");
		customLayout.addComponent(txtConvenientVisitTime, "txtConvenientVisitTime");
		
		addComponent(customLayout);
	}

	
	
	/**
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		Individual individual = applicant.getIndividual();
		individual.setMonthlyPersonalExpenses(getDouble(txtMonthlyPersonalExpenses));
		individual.setMonthlyFamilyExpenses(getDouble(txtMonthlyFamilyExpenses));
		if (optDebtOtherSource.getValue() != null) {
			individual.setDebtFromOtherSource(Integer.parseInt(optDebtOtherSource.getValue().toString()) == 1);
			individual.setTotalDebtInstallment(getDouble(txtTotalDebtMonthlyInstallment));
		}		
		if (optGuarantorOtherLoan.getValue() != null) {
			individual.setGuarantorOtherLoan(Integer.parseInt(optGuarantorOtherLoan.getValue().toString()) == 1);
		}		
		individual.setConvenientVisitTime(txtConvenientVisitTime.getValue());
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
		optGuarantorOtherLoan.setValue(BooleanUtils.toBoolean(individual.isGuarantorOtherLoan()) ? 1 : 0);
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
		checkMandatoryField(txtMonthlyPersonalExpenses, "monthly.personal.expenses");
		checkMandatoryField(txtMonthlyFamilyExpenses, "monthly.family.expenses");
		checkMandatorySelectField(optDebtOtherSource, "debt.from.other.source");
		if (optDebtOtherSource.getValue() != null) {
			if (Integer.parseInt(optDebtOtherSource.getValue().toString()) == 1) {
				checkMandatoryField(txtTotalDebtMonthlyInstallment, "total.debt.monthly.installment");
			}
		}
		checkMandatorySelectField(optGuarantorOtherLoan, "guarantor.other.loan");
		checkMandatoryField(txtConvenientVisitTime, "convenient.time.for.visit");
		return errors;
	}
}
