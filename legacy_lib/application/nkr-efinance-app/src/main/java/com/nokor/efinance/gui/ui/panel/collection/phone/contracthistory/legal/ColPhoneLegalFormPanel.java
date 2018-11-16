package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal;

import javax.annotation.PostConstruct;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.court.LegalCourtPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneLegalFormPanel extends AbstractTabPanel implements ClickListener {
	
	/** */
	private static final long serialVersionUID = -5200111518448518294L;
	
	private Label lblSeized;
	private Button btnOpen;
	private LegalFormPanel legalFormPanel;
	private HorizontalLayout openHorizontalLayout;;
	
	private LegalCourtPanel legalCourtPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		setCaption(I18N.message("legal"));
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		lblSeized = ComponentFactory.getHtmlLabel("<b>" + I18N.message("seized") + "</b>");
		lblSeized.setWidth(40, Unit.PIXELS);
		btnOpen = new NativeButton(I18N.message("open"), this);
		btnOpen.setStyleName("btn btn-success button-small");
		btnOpen.setWidth("70px");
		
		openHorizontalLayout = new HorizontalLayout();
		openHorizontalLayout.setSpacing(true);
		openHorizontalLayout.addComponent(lblSeized);
		openHorizontalLayout.addComponent(btnOpen);
		openHorizontalLayout.setComponentAlignment(lblSeized, Alignment.MIDDLE_CENTER);
		
		legalFormPanel = new LegalFormPanel(this);
		legalCourtPanel = new LegalCourtPanel();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(openHorizontalLayout);
		mainLayout.addComponent(legalFormPanel);
		mainLayout.addComponent(legalCourtPanel);
		
		return ComponentLayoutFactory.getPanel(mainLayout, false, true);
	}
	
	/**
	 * Assign Value to Legal
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		if (contract != null) {
			legalFormPanel.assignValues(contract);
			legalCourtPanel.assingValues(contract);
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnOpen) {
			legalFormPanel.hideResultLayout();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		legalFormPanel.reset();
	}
	
	/**
	 * hide header layout
	 */
	public void setVisibleHeaderLayout(boolean visible) {
		openHorizontalLayout.setVisible(visible);
	}

}
