package com.nokor.efinance.gui.ui.panel.quotation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.panel.IdentificationPanel;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.panel.QuotationTablePanel;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SimulationRepaymentSchedulePanel.NAME)
public class SimulationRepaymentSchedulePanel extends AbstractTabsheetPanel implements View, AssetEntityField {

	private static final long serialVersionUID = -6651024937294153368L;

	public static final String NAME = "simulation.panel";
	
	@Autowired
	private QuotationTablePanel quotationTablePanel;
	@Autowired
	private SimulationRepaymentScheduleFormPanel simulationFormPanel;
	
	@Autowired
	private IdentificationPanel identificationPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		quotationTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(simulationFormPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}
	
	
	@Override
	public void onAddEventClick() {
		
	}

	@Override
	public void onEditEventClick() {
		
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		
	}
	
	/**
	 * Go to quotation table panel
	 */
	public void displayQuotationTablePanel() {
		
	}
	
	/**
	 * Go to quotation form to add a new quotation
	 * @param quotation
	 */
	public void createQuotation(Quotation quotation) {
		/*getTabSheet().removeFormPanel(identificationPanel);
		simulationFormPanel.assignValues(quotation, true);
		
		getTabSheet().addFormPanel(simulationFormPanel);
		getTabSheet().setSelectedTab(simulationFormPanel);*/
	}
	
}
