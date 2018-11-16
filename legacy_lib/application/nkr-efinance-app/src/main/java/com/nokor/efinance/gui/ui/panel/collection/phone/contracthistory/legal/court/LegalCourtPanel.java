package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.court;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author uhout.cheng
 */
public class LegalCourtPanel extends AbstractControlPanel implements ClickListener, FMEntityField {

	/** */
	private static final long serialVersionUID = 4016593958218278284L; 
	
	private Button btnOpen;

	private LegalCourtDetailPanel detailPanel;

	/**
	 * @return the btnOpen
	 */
	public Button getBtnOpen() {
		return btnOpen;
	}
	
	/**
	 * 
	 */
	public LegalCourtPanel() {
		Label lblLegalCase = ComponentLayoutFactory.getLabelCaption("<b>" + I18N.message("court") + "</b>");
		lblLegalCase.setContentMode(ContentMode.HTML);
		lblLegalCase.setWidth(40, Unit.PIXELS);
		
		btnOpen = ComponentLayoutFactory.getDefaultButton("open", null, 70);
		btnOpen.addClickListener(this);
		
		HorizontalLayout titleLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		titleLayout.addComponent(lblLegalCase);
		titleLayout.addComponent(btnOpen);
		
		detailPanel = new LegalCourtDetailPanel(this);
		
		setSpacing(true);
		addComponent(titleLayout);
		addComponent(detailPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assingValues(Contract contract) {
		if (contract != null) {
			detailPanel.assignValues(contract);
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnOpen)) {
			btnOpen.setVisible(true);
			detailPanel.displayDetailLayout(null);
		} 
	}
	
}
