package com.nokor.efinance.gui.ui.panel.report.overdue;

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

public class MonthlyOverdueReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	private EntityService entityService;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private AutoDateField dfDate;
	private ValueChangeListener valueChangeListener;
	
	public MonthlyOverdueReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		dfDate.setValue(DateUtils.today());
	}

	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		cbxDealer = new DealerComboBox(null, entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -3397391227607469576L;
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
		
		dfDate = ComponentFactory.getAutoDateField("", false);
		dfDate.setValue(DateUtils.today());
		
		final GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
		gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
		gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 0);
		gridLayout.addComponent(dfDate, iCol++, 0);
		
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
		if (cbxDealer.getSelectedEntity() != null) { 
			reportParameter.addParameter("dealer", cbxDealer.getSelectedEntity());	
		}
		if (dfDate.getValue() != null) { 
			reportParameter.addParameter("endDate", dfDate.getValue());	
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			reportParameter.addParameter("dealer.type", cbxDealerType.getSelectedEntity());
		}
		return reportParameter;
	}
}
