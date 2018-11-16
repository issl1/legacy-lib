package com.nokor.efinance.gui.ui.panel.report.comparison;

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
 * Comparison report
 * @author bunlong.taing
 */
public class ComparisonReportSearchPanel extends AbstractSearchReportPanel {

	/** */
	private static final long serialVersionUID = -2064415125180533100L;
	
	private EntityService entityService;
	private AutoDateField dfSearchDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;

	/** */
	public ComparisonReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#reset()
	 */
	@Override
	public void reset() {
		dfSearchDate.setValue(DateUtils.today());
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		dfSearchDate = ComponentFactory.getAutoDateField("date", false);
		//dfSearchDate.setResolution(Resolution.MONTH);
		//dfSearchDate.setDateFormat("MM/yyyy");
		cbxDealer = new DealerComboBox(I18N.message("dealer"), entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 2193594685314667589L;
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
		reset();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(dfSearchDate));
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
		if (dfSearchDate.getValue() != null) {
			reportParameter.addParameter("date", dfSearchDate.getValue());
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
