package com.nokor.efinance.gui.ui.panel.contract.queue;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class QueuePanel extends AbstractTabPanel {

	/** **/
	private static final long serialVersionUID = -3089951477242919973L;

	private QueueDetailPanel queuDetailPanel;
	private QueueTablePanel queueTablePanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setMargin(false);
		queueTablePanel = new QueueTablePanel();
		queuDetailPanel = new QueueDetailPanel();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(queueTablePanel);
		verticalLayout.addComponent(queuDetailPanel);
		
		Panel panel = new Panel(verticalLayout);
		panel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("queue") + "</h3>");
		
		return panel;
	}

	
}
