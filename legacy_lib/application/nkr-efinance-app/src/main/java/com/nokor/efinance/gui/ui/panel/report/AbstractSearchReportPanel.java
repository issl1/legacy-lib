package com.nokor.efinance.gui.ui.panel.report;

import com.nokor.efinance.core.shared.report.ReportParameter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 * @param <T>
 */
public abstract class AbstractSearchReportPanel extends Panel {
	
	private static final long serialVersionUID = -6787380793713001375L;
	
	public AbstractSearchReportPanel (final String caption) {
		setCaption(caption);		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setMargin(true);
		containLayout.addComponent(createForm());
		setContent(containLayout);
	}

	public abstract void reset();
	protected abstract Component createForm();
	public abstract ReportParameter getReportParameter();
}
