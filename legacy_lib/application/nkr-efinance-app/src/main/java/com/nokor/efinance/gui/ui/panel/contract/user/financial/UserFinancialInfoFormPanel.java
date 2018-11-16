package com.nokor.efinance.gui.ui.panel.contract.user.financial;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Financial detail information
 * @author uhout.cheng
 */
public class UserFinancialInfoFormPanel extends AbstractFormPanel implements FinServicesHelper, ValueChangeListener, ClickListener {

	/** */
	private static final long serialVersionUID = -150915966525293808L;
	
	private Individual individual;
	private TextField txtFixedIncome;
	private TextField txtOtherIncome;
	private TextField txtNetIncome;
	private TextField txtSource;
	
	private Button btnSave;
	private Button btnEdit;
	/**
	 * 
	 */
	public UserFinancialInfoFormPanel() {
		super.init();
		super.setMargin(false);
		setEnableControls(false);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtFixedIncome = ComponentFactory.getTextField(false, 30, 120);
		txtFixedIncome.setStyleName("mytextfield-caption");
		txtFixedIncome.addValueChangeListener(this);
		
		txtOtherIncome = ComponentFactory.getTextField(false, 30, 120);
		txtOtherIncome.setStyleName("mytextfield-caption");
		txtOtherIncome.addValueChangeListener(this);
		
		txtNetIncome = ComponentFactory.getTextField(false, 30, 120);
		txtNetIncome.setEnabled(false);
		
		txtSource = ComponentFactory.getTextField(false, 30, 120);
		txtSource.setStyleName("mytextfield-caption");
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.setVisible(false);
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setEnabled(true);
		btnEdit.addClickListener(this);
		
		com.vaadin.ui.Label lblFixedIncome = new com.vaadin.ui.Label(I18N.message("fixed.income"));
		com.vaadin.ui.Label lblOtherIncome = new com.vaadin.ui.Label(I18N.message("other.incomes"));
		com.vaadin.ui.Label lblNetIncome = new com.vaadin.ui.Label(I18N.message("net.income"));
		com.vaadin.ui.Label lblSource = new com.vaadin.ui.Label(I18N.message("source"));
		
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(btnEdit);
		buttonLayout.addComponent(btnSave);
		
		GridLayout gridLayout = new GridLayout(16, 1);
		
		int iCol = 0;
		gridLayout.addComponent(lblFixedIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtFixedIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblOtherIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtOtherIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblNetIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtNetIncome, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblSource, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtSource, iCol++, 0);
		
		gridLayout.setComponentAlignment(lblFixedIncome, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblOtherIncome, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblNetIncome, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblSource, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(gridLayout);
		verticalLayout.addComponent(buttonLayout);
		verticalLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		Panel mainPanel = new Panel(new VerticalLayout(verticalLayout));
		mainPanel.setCaption(I18N.message("general"));
		return mainPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		individual.setFixedIncome(getDouble(txtFixedIncome));
		individual.setOtherIncomes(getDouble(txtOtherIncome));
		individual.setDebtSource(txtSource.getValue());
		return individual;
	}
	
	/**
	 * @param indId
	 */
	public void assignValues(Long indId) {
		super.reset();
		if (indId != null) {
			individual = INDIVI_SRV.getById(Individual.class, indId);
			double fixedIncome = MyNumberUtils.getDouble(individual.getFixedIncome());
			double otherIncome = MyNumberUtils.getDouble(individual.getOtherIncomes());
			double netIncome = fixedIncome + otherIncome;
			txtFixedIncome.setValue(AmountUtils.format(fixedIncome));
			txtOtherIncome.setValue(AmountUtils.format(otherIncome));
			txtNetIncome.setValue(MyNumberUtils.formatDoubleToString(netIncome, "###,##0.00"));
			txtSource.setValue(individual.getDebtSource());
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		txtNetIncome.setValue(AmountUtils.format(0d));
		txtOtherIncome.setValue(AmountUtils.format(0d));
		txtFixedIncome.setValue(AmountUtils.format(0d));
		txtSource.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setEnableControls(boolean isEnable) {
		txtOtherIncome.setEnabled(isEnable);
		txtFixedIncome.setEnabled(isEnable);
		txtSource.setEnabled(isEnable);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		errors.clear();
		checkDoubleField(txtFixedIncome, "fixed.income");
		checkDoubleField(txtOtherIncome, "other.incomes");
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		double netIncome = 0d;
		double fixedIncome = MyNumberUtils.getDouble(getDouble(txtFixedIncome));
		double otherIncome = MyNumberUtils.getDouble(getDouble(txtOtherIncome));
		netIncome = fixedIncome + otherIncome;
		txtNetIncome.setValue(MyNumberUtils.formatDoubleToString(netIncome, "###,##0.00"));
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			btnEdit.setVisible(false);
			btnSave.setVisible(true);
			setEnableControls(true);
		} else if (event.getButton() == btnSave) {
			if (this.validate()) {
				ENTITY_SRV.saveOrUpdate(this.getEntity());
				displaySuccess();
				btnSave.setVisible(false);
				btnEdit.setVisible(true);
				setEnableControls(false);
			} else {
				displayErrors();
			}
		}
		
	}
	
}
