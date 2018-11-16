package com.nokor.efinance.gui.ui.panel.quotation;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class QuotationMgtSearchPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -635644650780535609L;
	
	private EntityService entityService;
		
	private TextField txtReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
			
	public QuotationMgtSearchPanel(QuotationMgtTablePanel quotationTablePanel) {
		super(I18N.message("quotation.search"), quotationTablePanel);
	}
	
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		// restrictions.addCriterion(Restrictions.ne("dealerType", DealerType.OTH));
		return restrictions;
	}

	@Override
	protected Component createForm() {
		txtReference = ComponentFactory.getTextField(false, 60, 150);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		
		entityService = SpringUtils.getBean(EntityService.class);
        cbxDealer = new DealerComboBox(entityService.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		
		/*List<DealerType> dealerTypes = DealerType.list();
		dealerTypes.remove(DealerType.OTH);*/
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -1531508523200192193L;
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
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());
		
		final GridLayout gridLayout = new GridLayout(12, 2);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 0);
        gridLayout.addComponent(txtLastNameEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 0);
        gridLayout.addComponent(txtFirstNameEn, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 1);
        gridLayout.addComponent(cbxDealer, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
		
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(gridLayout);

		return horizontalLayout;
	}
	
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		
		restrictions.addCriterion(Restrictions.eq("stamp", false));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}		
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));		
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())
				|| StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("quoapp.applicantType", EApplicantType.C);
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}/* else {
			restrictions.addCriterion(Restrictions.ne("dea.dealerType", DealerType.OTH));
		}*/
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(ACTIVATION_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(ACTIVATION_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.desc(ACTIVATION_DATE));
		return restrictions;
	}
}
