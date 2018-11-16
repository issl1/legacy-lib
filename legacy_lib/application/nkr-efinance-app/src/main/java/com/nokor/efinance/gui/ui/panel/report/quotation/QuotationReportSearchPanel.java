package com.nokor.efinance.gui.ui.panel.report.quotation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.gui.ui.panel.report.AbstractSearchReportPanel;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class QuotationReportSearchPanel extends AbstractSearchReportPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = -6451271574354015611L;
	private static final String DEALER_TYPE = "dealer.type";
	private EntityService entityService;
	
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatus;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<Province> cbxProvince;
	private ValueChangeListener valueChangeListener;
		
	public QuotationReportSearchPanel() {
		super(I18N.message("report.parameters"));
	}
	
	@Override
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
		cbxQuotationStatus.setSelectedEntity(null);
	
	}

	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		
        cbxQuotationStatus = new ERefDataComboBox<EWkfStatus>(getWkfStatus());
        cbxDealer = new DealerComboBox(entityService.list(getDealerRestriction()));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7723155265814926403L;
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
        cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.renderer();
		
		final GridLayout gridLayout = new GridLayout(11, 1);
		gridLayout.setSpacing(true);
        int iCol = 0;
        
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
		gridLayout.addComponent(cbxDealerType, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
		gridLayout.addComponent(cbxDealer, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("quotation.status")), iCol++, 0);
		gridLayout.addComponent(cbxQuotationStatus, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("province")), iCol++, 0);
		gridLayout.addComponent(cbxProvince, iCol++, 0);
		
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
		if (cbxQuotationStatus.getSelectedEntity() != null) { 
			reportParameter.addParameter(WKF_STATUS, new EWkfStatus[] {cbxQuotationStatus.getSelectedEntity()});
		}
		if (cbxProvince.getSelectedEntity() != null) {
			reportParameter.addParameter(PROVINCE, cbxProvince.getSelectedEntity());
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			reportParameter.addParameter(DEALER_TYPE, cbxDealerType.getSelectedEntity());
		}
		return reportParameter;
	}
	
	/**
     * List of quotation status
     * @return
     */
    private List<EWkfStatus> getWkfStatus() {
    	List<EWkfStatus> quotationStatus  = new ArrayList<EWkfStatus>();
    	quotationStatus.add(QuotationWkfStatus.ACT);
    	quotationStatus.add(QuotationWkfStatus.REJ);
    	quotationStatus.add(QuotationWkfStatus.REU);
    	return quotationStatus;
    }
	
}
