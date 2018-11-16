package com.nokor.efinance.gui.ui.panel.report.daily;

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

public class DailysReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	private EntityService entityService;
	
	private AutoDateField dfDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	
	public DailysReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		dfDate.setValue(DateUtils.today());
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
	}

	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		dfDate = ComponentFactory.getAutoDateField("date", false);
		dfDate.setValue(DateUtils.today());
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6774641791917653706L;
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
		
		horizontalLayout.addComponent(new FormLayout(dfDate));
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
	
	@Override
	public ReportParameter getReportParameter() {
		ReportParameter reportParameter = new ReportParameter();
		if (dfDate.getValue() != null) { 
			reportParameter.addParameter("dateValue", dfDate.getValue());	
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
