package com.nokor.efinance.gui.ui.panel.report.brandknowladge;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class BrandKnowledgeSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	private EntityService entityService;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private ValueChangeListener valueChangeListener;
		
	public BrandKnowledgeSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -1));
		dfEndDate.setValue(DateUtils.today());
	}

	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		dfStartDate = ComponentFactory.getAutoDateField("", false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -1));
		dfEndDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate.setValue(DateUtils.today());
		
		cbxDealer = new DealerComboBox(null, entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 416121858907727116L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(entityService.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		final GridLayout gridLayout = new GridLayout(11, 1);
		gridLayout.setSpacing(true);
        int iCol = 0;
        
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 0);
		gridLayout.addComponent(dfStartDate, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 0);
		gridLayout.addComponent(dfEndDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
		gridLayout.addComponent(cbxDealerType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
		gridLayout.addComponent(cbxDealer, iCol++, 0);
		
		return gridLayout;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	@Override
	public ReportParameter getReportParameter() {
		ReportParameter reportParameter = new ReportParameter();
		if (dfStartDate.getValue() != null) { 
			reportParameter.addParameter("startDate", dfStartDate.getValue());	
		}
		if (dfEndDate.getValue() != null) { 
			reportParameter.addParameter("endDate", dfEndDate.getValue());	
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			reportParameter.addParameter("dealer", cbxDealer.getSelectedEntity());
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			reportParameter.addParameter("dealer.type", cbxDealerType.getSelectedEntity());
		}
		
		return reportParameter;
	}
}
