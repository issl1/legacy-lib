package com.nokor.efinance.gui.ui.panel.report.complaints;

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
 * @author buntha.chea
 */
public class ComplaintReportSearchPanel extends AbstractSearchReportPanel {
	private static final long serialVersionUID = -828857098334109695L;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private ValueChangeListener valueChangeListener;
	private EntityService entityService;

	/** */
	public ComplaintReportSearchPanel() {
		super(I18N.message("complaint.reports"));
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#reset()
	 */
	@Override
	public void reset() {
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel#createForm()
	 */
	@Override
	protected Component createForm() {

		dfStartDate = ComponentFactory.getAutoDateField(I18N.message("startdate"), false);
		dfStartDate.setWidth("95px");
		dfEndDate = ComponentFactory.getAutoDateField(I18N.message("enddate"), false);
		dfEndDate.setWidth("95px");


		entityService = SpringUtils.getBean(EntityService.class);
		dfStartDate = ComponentFactory.getAutoDateField("startdate", false);
		dfEndDate = ComponentFactory.getAutoDateField("enddate", false);

		cbxDealer = new DealerComboBox(entityService.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		cbxDealer.setCaption(I18N.message("dealer"));
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 3113344698670781211L;
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
		reset();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(dfStartDate));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayout.addComponent(new FormLayout(dfEndDate));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
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
		if (dfStartDate.getValue() != null) {
			reportParameter.addParameter("startDate", dfStartDate.getValue());
		}
		if (dfEndDate.getValue() != null){
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
