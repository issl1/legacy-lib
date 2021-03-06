package com.nokor.efinance.gui.ui.panel.report.comparison;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.gui.report.xls.GLFComparisonReport;
import com.nokor.efinance.gui.ui.panel.report.AbstractReportPanel;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.efinance.tools.report.Report;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ComparisonReportPanel.NAME)
public class ComparisonReportPanel extends AbstractReportPanel {

	/** */
	private static final long serialVersionUID = 5889791606931294169L;
	public static final String NAME = "comparison.report";
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init();
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractReportPanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchReportPanel createSearchPanel() {
		return new ComparisonReportSearchPanel();
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractReportPanel#getReportClass()
	 */
	@Override
	protected Class<? extends Report> getReportClass() {
		return GLFComparisonReport.class;
	}

}
