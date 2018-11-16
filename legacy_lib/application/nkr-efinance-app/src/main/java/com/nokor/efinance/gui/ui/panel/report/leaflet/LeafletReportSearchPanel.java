package com.nokor.efinance.gui.ui.panel.report.leaflet;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
/**
 * 
 * @author uhout.cheng
 *
 */
public class LeafletReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {

	/** */
	private static final long serialVersionUID = 455763174208036311L;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public LeafletReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	/*
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#reset()
	 */
	@Override
	public void reset() {
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
	}

	/*
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth());
		
		dfStartDate.setWidth(95, Unit.PIXELS);
		dfEndDate.setWidth(95, Unit.PIXELS);
		
		GridLayout gridLayout = new GridLayout(3,1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(new FormLayout(dfStartDate));
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		gridLayout.addComponent(new FormLayout(dfEndDate));
		
		return gridLayout;
	}
	
	/*
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#getReportParameter()
	 */
	@Override
	public ReportParameter getReportParameter() {
		ReportParameter reportParameter = new ReportParameter();
		if (dfStartDate.getValue() != null) { 
			reportParameter.addParameter("dateStartValue", dfStartDate.getValue());	
		}
		if (dfEndDate.getValue() != null) { 
			reportParameter.addParameter("dateEndValue", dfEndDate.getValue());	
		}
		return reportParameter;
	}
	
}
