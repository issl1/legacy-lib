package com.nokor.efinance.gui.ui.panel.report.applicant;

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
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class Applicant2ReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	private EntityService entityService;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<FinProduct> cbxFinancialProduct;
	private ValueChangeListener valueChangeListener;
		
	public Applicant2ReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxFinancialProduct.setSelectedEntity(null);
	}

	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
        cbxDealer = new DealerComboBox(entityService.list(getDealerRestriction()));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4414097012648270160L;
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
		
		cbxFinancialProduct = new EntityRefComboBox<FinProduct>();
		BaseRestrictions<FinProduct> restrictions = new BaseRestrictions<>(FinProduct.class);
		restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(DateUtils.todayH00M00S00())));
		restrictions.addCriterion(Restrictions.ge(END_DATE, DateUtils.todayH00M00S00()));
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		cbxFinancialProduct.setRestrictions(restrictions);		
		cbxFinancialProduct.setImmediate(true);
		cbxFinancialProduct.renderer();
		
		final GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
        int iCol = 0;
        
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
		gridLayout.addComponent(cbxDealerType, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
        
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
		gridLayout.addComponent(cbxDealer, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("financial.product")), iCol++, 0);
		gridLayout.addComponent(cbxFinancialProduct, iCol++, 0);
		
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
			reportParameter.addParameter(DEALER, cbxDealer.getSelectedEntity());
		}
		
		if (cbxFinancialProduct.getSelectedEntity() != null) {
			reportParameter.addParameter("financial.product", cbxFinancialProduct.getSelectedEntity());
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			reportParameter.addParameter("dealer.type", cbxDealerType.getSelectedEntity());
		}
		return reportParameter;
	}
	
}
