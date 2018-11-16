package com.nokor.efinance.core.quotation.panel.popup;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Redemption Pop up Panel
 * @author bunlong.taing
 */
public class ContractRedemptionPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -7703328332330804843L;
	
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	
	public ContractRedemptionPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setWidth(480, Unit.PIXELS);
		setHeight(200, Unit.PIXELS);
		setResizable(false);
		init();
	}
	
	/**
	 * Init components
	 */
	private void init() {
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		
		setContent(verticalLayout);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

}
