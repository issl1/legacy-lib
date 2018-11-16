package com.nokor.efinance.gui.ui.panel.report.installment;

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
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author sok.vina
 *
 */
public class InstallmentPaymentReportSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {

	private static final long serialVersionUID = -8424925526442646308L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private DealerComboBox cbxDealer;
	private Button btnLinkDealer;
	private SecUserDetail secUserDetail;
	private TextField txtContractReference;
	private AutoDateField dfStartDueDate;
	private AutoDateField dfEndDueDate;
	
	public InstallmentPaymentReportSearchPanel(InstallmentPaymentReportTablePanel installmentPaymentReportTablePanel) {
		super(I18N.message("search"), installmentPaymentReportTablePanel);
	}
	
	@Override
	public void reset() {
		cbxDealer.setValue(null);
		txtContractReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		dfStartDueDate.setValue(DateUtils.today());
		dfEndDueDate.setValue(DateUtils.today());
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		secUserDetail = getSecUserDetail(); 
		if (secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		}
		btnLinkDealer = new Button();
		btnLinkDealer.setIcon(new ThemeResource("../nkr-default/icons/16/lock.png"));
		btnLinkDealer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5609114513268886712L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (cbxDealer.getSelectedEntity() != null) {
					secUserDetail.setDealer(cbxDealer.getSelectedEntity());
					EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
					entityService.saveOrUpdate(secUserDetail);
				}
			}
		});
		
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDueDate = ComponentFactory.getAutoDateField("", false);
		dfStartDueDate.setValue(DateUtils.today());
		dfEndDueDate = ComponentFactory.getAutoDateField("", false);
		dfEndDueDate.setValue(DateUtils.today());
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(btnLinkDealer, iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 0);
        gridLayout.addComponent(txtLastNameEn, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 0);
        gridLayout.addComponent(txtFirstNameEn, iCol++, 0);
                
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("start.due.date")), iCol++, 1);
        gridLayout.addComponent(dfStartDueDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("end.due.date")), iCol++, 1);
        gridLayout.addComponent(dfEndDueDate, iCol++, 1);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		}
		
		if (dfStartDueDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDueDate.getValue())));
		}
		if (dfEndDueDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDueDate.getValue())));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}

		DetachedCriteria userSubCriteria = DetachedCriteria.forClass(Cashflow.class, "cash");
		userSubCriteria.createAlias("cash.contract", "cont", JoinType.INNER_JOIN);
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			userSubCriteria.add(Restrictions.ilike("cont."+ REFERENCE, txtContractReference.getValue(), MatchMode.ANYWHERE));				
		}
		SecUser secUser1 = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SecUserDetail usrDetail1 = entityService.getByField(SecUserDetail.class, "secUser.id", secUser1.getId());
		if (ProfileUtil.isPOS() && usrDetail1.getDealer() != null) {
			userSubCriteria.add(Restrictions.eq("cont."+ DEALER + "." + ID, usrDetail1.getDealer().getId()));
		} else {
			if (cbxDealer.getSelectedEntity() != null) {
				userSubCriteria.add(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
			}	
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
