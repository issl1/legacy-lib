package com.nokor.efinance.gui.ui.panel.payment;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

/**
 * 
 * @author sok.vina
 *
 */
public class SecondPaymentSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {

	private static final long serialVersionUID = -8424925526442646308L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ComboBox cbxSortBy;
	private OptionGroup rdbtnShortBy;
	private SecondPaymentTablePanel tablePanel;
	public SecondPaymentSearchPanel(SecondPaymentTablePanel secondPaymentTablePanel) {
		super(I18N.message("search"), secondPaymentTablePanel);
		tablePanel = secondPaymentTablePanel;
	}
	
	@Override
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		txtContractReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate.setValue(DateUtils.today());
		rdbtnShortBy.setValue(null);
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -3113776495908827145L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(entityService.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.addMonthsDate(DateUtils.today(), -2));
		dfEndDate = ComponentFactory.getAutoDateField("", false);      
		dfEndDate.setValue(DateUtils.today());
		
		cbxSortBy = new ComboBox();
		cbxSortBy.addItem(I18N.message("date"));
		rdbtnShortBy = new OptionGroup();
		rdbtnShortBy.setStyleName("horizontal");
		rdbtnShortBy.setMultiSelect(false);
		rdbtnShortBy.addItem(I18N.message("Asc"));
		rdbtnShortBy.addItem(I18N.message("Desc"));
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
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
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("sort.by")), iCol++, 1);
        gridLayout.addComponent(cbxSortBy, iCol++, 1);
        gridLayout.addComponent(rdbtnShortBy, iCol++, 1);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.ORC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.VAL));
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		}
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue()) || cbxDealer.getSelectedEntity() != null) {
			DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
			userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
			if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
				userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));				
			}
			if (cbxDealer.getSelectedEntity() != null) {
				userSubCriteria.add(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
			}
			userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
			restrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("dealer", "paydeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("paydeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		if (cbxSortBy.getValue() != null) {
			if (rdbtnShortBy.getValue() != null) {
				
				if (rdbtnShortBy.getValue().equals("Desc")) {
					restrictions.addOrder(Order.desc(PAYMENT_DATE));
				}
				if (rdbtnShortBy.getValue().equals("Asc")) {
					restrictions.addOrder(Order.asc(PAYMENT_DATE));
				}
				
			}
		}
		tablePanel.resetSelectPaid();
		return restrictions;
	}
}
