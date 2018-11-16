package com.nokor.efinance.gui.ui.panel.quotation;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(QuickQuotationPanel.NAME)
public class QuickQuotationPanel extends VerticalLayout implements View {
	private static final long serialVersionUID = -535100062285786472L;
	
	public static final String NAME = "quick.quote";
	
	@Autowired
	protected EntityService entityService;
	
	private EntityRefComboBox<FinProduct> cbxFinancialProduct;
	private TextField txtLeaseAmount;
	private TextField txtInterestRate;
	private TextField txtTerm;
	
	private TextField txtMontlyInstallment;
	
	public QuickQuotationPanel() {
		
	}
	
	@PostConstruct
	public void PostConstruct() {
				
		Panel parametersPanel = ComponentFactory.getPanel("financial.parameters");
		FormLayout formParametersLayout = ComponentFactory.getFormLayout();
				
		cbxFinancialProduct = new EntityRefComboBox<FinProduct>(I18N.message("financial.product"));
		cbxFinancialProduct.setRestrictions(new BaseRestrictions<FinProduct>(FinProduct.class));
		cbxFinancialProduct.setRequired(true);
		cbxFinancialProduct.renderer();
		
		txtLeaseAmount = ComponentFactory.getTextField("interest.rate", true, 10, 120);
		txtInterestRate = ComponentFactory.getTextField("interest.rate", true, 10, 60); 
		txtTerm = ComponentFactory.getTextField("term", true, 3, 60);
		
		formParametersLayout.addComponent(cbxFinancialProduct);
		formParametersLayout.addComponent(txtLeaseAmount);
		formParametersLayout.addComponent(txtInterestRate);
		formParametersLayout.addComponent(txtTerm);
		parametersPanel.setContent(formParametersLayout);
		
		Panel simulationPanel = ComponentFactory.getPanel("financial.simulation");
		FormLayout formSimulationLayout = ComponentFactory.getFormLayout();
		
		txtMontlyInstallment = ComponentFactory.getTextField("montly.installment", false, 50, 120);
		
		formSimulationLayout.addComponent(txtMontlyInstallment);
		simulationPanel.setContent(formSimulationLayout);
		
		addComponent(parametersPanel);
		addComponent(simulationPanel);
    }

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
