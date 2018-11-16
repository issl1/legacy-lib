package com.nokor.efinance.gui.ui.panel.report.collectionperformance;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.gui.report.xls.GLFCollectionPerformance;
import com.nokor.efinance.gui.ui.panel.report.AbstractReportPanel;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.efinance.tools.report.Report;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CollectionPerformanceReportPanel.NAME)
public class CollectionPerformanceReportPanel extends AbstractReportPanel {

	private static final long serialVersionUID = -8173918126351309900L;

	public static final String NAME = "collection.performance.report";
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	@Override
	protected AbstractSearchReportPanel createSearchPanel() {
		return new CollectionPerformanceSearchPanel();
	}

	@Override
	protected Class<? extends Report> getReportClass() {
		return GLFCollectionPerformance.class;
	}

}
