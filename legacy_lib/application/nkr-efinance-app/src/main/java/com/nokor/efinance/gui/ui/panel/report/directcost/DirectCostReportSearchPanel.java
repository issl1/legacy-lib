package com.nokor.efinance.gui.ui.panel.report.directcost;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
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
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
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
 * @author buntha.chea
 *
 */
public class DirectCostReportSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {

	private static final long serialVersionUID = -8424925526442646308L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	//private ERefDataComboBox<ServiceType> directCostName;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private EntityRefComboBox<FinService> cbxDirectCost;
	
	/**
	 * 
	 * @param installmentPaymentReportTablePanel
	 */
	public DirectCostReportSearchPanel(DirectCostReportTablePanel directCostReportTablePanel) {
		super(I18N.message("search"), directCostReportTablePanel);
	}
	
	@Override
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setWidth("220px");
		secUserDetail = getSecUserDetail(); 
		if (secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		}
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6774641791917653706L;
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
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);      
		dfEndDate.setValue(DateUtils.today());
		
		cbxDirectCost = new EntityRefComboBox<FinService>();
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		List<EServiceType> serviceTypes = EServiceType.listDirectCosts();
		restrictions.addCriterion(Restrictions.in("serviceType", serviceTypes) );
		cbxDirectCost.setRestrictions(restrictions);		
		cbxDirectCost.setImmediate(true);
		cbxDirectCost.renderer();
		cbxDirectCost.setSelectedEntity(null);
		 
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("direct.cost")), iCol++, 0);
        gridLayout.addComponent(cbxDirectCost, iCol++, 0);
        
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 1);
        gridLayout.addComponent(cbxDealer, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("payment.start.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("payment.end.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.DCO));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		

		DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
		userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
		userSubCriteria.createAlias("cash.service", "servi", JoinType.INNER_JOIN);
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));				
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (cbxDirectCost.getSelectedEntity() != null) {
			userSubCriteria.add(Restrictions.eq("servi.id", cbxDirectCost.getSelectedEntity().getId()));
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation(DEALER, "dea", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cash.payment.id")));
		restrictions.addCriterion(Property.forName("id").in(userSubCriteria) );
	
		restrictions.addOrder(Order.desc(PAYMENT_DATE));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<SecUserDetail>(SecUserDetail.class);			
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("secUser.id", secUser.getId()));
		restrictions.setCriterions(criterions);
		EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
		List<SecUserDetail> usrDetails = entityService.list(restrictions);
		if (!usrDetails.isEmpty()) {
			return usrDetails.get(0);
		}
		SecUserDetail usrDetail = new SecUserDetail();
		usrDetail.setSecUser(secUser);
		return usrDetail;
	}
}
