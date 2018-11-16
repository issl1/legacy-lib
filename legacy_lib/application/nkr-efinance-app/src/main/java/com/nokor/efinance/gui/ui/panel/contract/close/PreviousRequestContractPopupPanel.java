package com.nokor.efinance.gui.ui.panel.contract.close;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
/**
 * 
 * @author buntha.chea
 *
 */
public class PreviousRequestContractPopupPanel extends Window implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5178398539913463318L;
	
	private PreviousRequestContractPanel previousRequestContractPanel;
	
	private Button btnSubmit;
	private Button btnCancel;
	private Label lblTotalValue;
	
	/**
	 * 
	 */
	public PreviousRequestContractPopupPanel() {
		setCaption(I18N.message("previous.request"));
		setModal(true);
		
		init();
		Label lblTotal = getLabel("total");
		HorizontalLayout totalLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		totalLayout.addComponent(lblTotal);
		totalLayout.addComponent(lblTotalValue);
		totalLayout.addComponent(btnCancel);
		totalLayout.addComponent(btnSubmit);
		totalLayout.setComponentAlignment(lblTotal, Alignment.MIDDLE_LEFT);
		totalLayout.setComponentAlignment(lblTotalValue, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		mainLayout.addComponent(previousRequestContractPanel);
		mainLayout.addComponent(totalLayout);
		mainLayout.setComponentAlignment(totalLayout, Alignment.BOTTOM_RIGHT);
		
		setContent(mainLayout);
	}
	
	/**
	 * init
	 */
	private void init() {
		previousRequestContractPanel = new PreviousRequestContractPanel();
		
		btnSubmit = ComponentLayoutFactory.getButtonStyle("submit", FontAwesome.CHECK, 70, "btn btn-success");
		btnSubmit.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		lblTotalValue = getLabelValue();
	}
	
	/**
	 * Assing Value 
	 */
	public void assignValue(Contract contract) {
		previousRequestContractPanel.assignValue(contract);
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}


	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			close();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

}
