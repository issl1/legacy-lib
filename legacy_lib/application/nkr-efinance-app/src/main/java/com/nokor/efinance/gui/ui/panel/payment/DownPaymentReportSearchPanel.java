package com.nokor.efinance.gui.ui.panel.payment;

import java.util.List;

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
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author sok.vina
 *
 */
public class DownPaymentReportSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {

	private static final long serialVersionUID = -8424925526442646308L;
	
	private EntityService entityService;
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ValueChangeListener valueChangeListener;
	
	public DownPaymentReportSearchPanel(DownPaymentReportTablePanel downPaymentReportTablePanel) {
		super(I18N.message("search"), downPaymentReportTablePanel);
	}
	
	@Override
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtContractReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}


	@Override
	protected Component createForm() {
		entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");		
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7901047153006187327L;
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
		
		secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS()
				&& secUserDetail != null
				&& secUserDetail.getDealer() != null
				&& !ProfileUtil.isCreditOfficerMovable()) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealer.setEnabled(false);
			cbxDealerType.setEnabled(false);
			cbxDealerType.removeValueChangeListener(valueChangeListener);
			cbxDealerType.setSelectedEntity(cbxDealer.getSelectedEntity() != null ? cbxDealer.getSelectedEntity().getDealerType() : null);;
			cbxDealerType.addValueChangeListener(valueChangeListener);
		} else {
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
				
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);      
		dfEndDate.setValue(DateUtils.today());

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
        
		return gridLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.ORC));
		restrictions.addCriterion(Restrictions.in(PAYMENT_STATUS, new EWkfStatus[] {PaymentWkfStatus.RVA, PaymentWkfStatus.VAL, PaymentWkfStatus.PAI}));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
				
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
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}		
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}

		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
			userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
			userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));
			userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
			restrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
		}
		
		restrictions.addOrder(Order.desc(PAYMENT_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
}
