package com.nokor.efinance.gui.ui.panel.report.weekly;

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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
/**
 * Weekly report
 * @author uhout.cheng
 */
public class WeeklyReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {

	/** */
	private static final long serialVersionUID = -3997742522183772809L;
	
	private EntityService entityService;
	
	private AutoDateField dfStartMonth;
	private AutoDateField dfEndMonth;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	
	/**	 */
	public WeeklyReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#reset()
	 */
	@Override
	public void reset() {
		dfStartMonth.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfEndMonth.setValue(DateUtils.getDateAtEndOfMonth());
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		
		dfStartMonth = ComponentFactory.getAutoDateField(I18N.message("start.date"), false);
		dfStartMonth.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfStartMonth.setImmediate(true);
		//dfStartMonth.setResolution(Resolution.MONTH);
		//dfStartMonth.setDateFormat("MM/yyyy");
		
		dfEndMonth = ComponentFactory.getAutoDateField(I18N.message("end.date"), false);
		dfEndMonth.setValue(DateUtils.getDateAtEndOfMonth());
		dfEndMonth.setImmediate(true);
		//dfEndMonth.setResolution(Resolution.MONTH);
		//dfEndMonth.setDateFormat("MM/yyyy");
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7717749513651242930L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(entityService.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
	
		horizontalLayout.addComponent(new FormLayout(dfStartMonth));
		horizontalLayout.addComponent(new FormLayout(dfEndMonth));
		horizontalLayout.addComponent(new FormLayout(cbxDealerType));
		horizontalLayout.addComponent(new FormLayout(cbxDealer));
		
		return horizontalLayout;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#getReportParameter()
	 */
	@Override
	public ReportParameter getReportParameter() {
		ReportParameter reportParameter = new ReportParameter();
		if (dfStartMonth.getValue() != null){
			reportParameter.addParameter("startDateValue", dfStartMonth.getValue());
		}
		if (dfEndMonth.getValue() != null){
			reportParameter.addParameter("endDateValue", dfEndMonth.getValue());
		}
		if (cbxDealer.getSelectedEntity() != null) {
			reportParameter.addParameter("dealer", cbxDealer.getSelectedEntity());
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			reportParameter.addParameter("dealerType", cbxDealerType.getSelectedEntity());
		}
		return reportParameter;
	}
	
}
