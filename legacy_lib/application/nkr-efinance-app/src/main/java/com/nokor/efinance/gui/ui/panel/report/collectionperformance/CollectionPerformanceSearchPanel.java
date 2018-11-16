package com.nokor.efinance.gui.ui.panel.report.collectionperformance;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class CollectionPerformanceSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public CollectionPerformanceSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}

	@Override
	protected Component createForm() {
		
		dfStartDate = ComponentFactory.getAutoDateField("", false);
		dfStartDate.setValue(DateUtils.today());
		
		dfEndDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate.setValue(DateUtils.today());
		
		final GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 0);
		gridLayout.addComponent(dfStartDate, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 0);
		gridLayout.addComponent(dfEndDate, iCol++, 0);
		
		return gridLayout;
	}
	
	@Override
	public ReportParameter getReportParameter() {
		ReportParameter reportParameter = new ReportParameter();
		if (dfStartDate.getValue() != null) { 
			reportParameter.addParameter("start.date", dfStartDate.getValue());	
		} else {
			reportParameter.addParameter("start.date", DateUtils.today());	
		}
		if (dfEndDate.getValue() != null) { 
			reportParameter.addParameter("end.date", dfEndDate.getValue());	
		} else {
			reportParameter.addParameter("end.date", DateUtils.today());	
		}
		return reportParameter;
	}
}
