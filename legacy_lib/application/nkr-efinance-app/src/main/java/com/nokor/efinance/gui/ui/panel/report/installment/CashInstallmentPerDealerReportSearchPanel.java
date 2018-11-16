package com.nokor.efinance.gui.ui.panel.report.installment;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author bunlong.taing
 *
 */
public class CashInstallmentPerDealerReportSearchPanel extends AbstractSearchPanel<Payment> implements FMEntityField {
	/** */
	private static final long serialVersionUID = -7362469019644268805L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	/**
	 * 
	 * @param tablePanel
	 */
	public CashInstallmentPerDealerReportSearchPanel(CashInstallmentPerDealerReportTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setWidth("220px");
		cbxDealer.setSelectedEntity(null);
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6856420968639658855L;
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
		dfStartDate = ComponentFactory.getAutoDateField("start.date",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		dfEndDate.setValue(DateUtils.today());
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(new FormLayout(cbxDealerType));
		horizontalLayout.addComponent(new FormLayout(cbxDealer));
		horizontalLayout.addComponent(new FormLayout(dfStartDate));
		horizontalLayout.addComponent(new FormLayout(dfEndDate));
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Payment> getRestrictions() {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addAssociation(Dealer.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));

		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		restrictions.addOrder(Order.desc(DEALER + "." + ID));
		restrictions.addOrder(Order.desc(PAYMENT_DATE));
		return restrictions;
	}
	
	/**
	 * 
	 * @return The start date selected
	 */
	public Date getStartDate () {
		return dfStartDate.getValue();
	}
	
	/**
	 * 
	 * @return The end date selected
	 */
	public Date getEndDate () {
		return dfEndDate.getValue();
	}
	
	/**
	 * 
	 * @return The selected dealer
	 */
	public List<Dealer> getSelectedDealer () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("id", cbxDealer.getSelectedEntity().getId()));
		}
		return entityService.list(restrictions);
	}

}
