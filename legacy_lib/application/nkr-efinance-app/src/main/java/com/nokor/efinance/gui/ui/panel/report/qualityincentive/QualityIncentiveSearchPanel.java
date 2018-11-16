package com.nokor.efinance.gui.ui.panel.report.qualityincentive;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
public class QualityIncentiveSearchPanel extends AbstractSearchPanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2663311603012223797L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private AutoDateField dfEndDate;
	
	/**
	 * @param tablePanel
	 */
	public QualityIncentiveSearchPanel(QualityIncentiveTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		cbxDealerType = new ERefDataComboBox<EDealerType>(I18N.message("dealer.type"), EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -1121619816068986468L;
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
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		reset();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(cbxDealerType));
		horizontalLayout.addComponent(new FormLayout(cbxDealer));
		horizontalLayout.addComponent(new FormLayout(dfEndDate));
		
		verticalLayout.addComponent(horizontalLayout);
		
		return verticalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("contractStatus", ContractWkfStatus.FIN));
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation(DEALER, "indeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("indeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER, cbxDealer.getSelectedEntity()));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.desc(START_DATE));
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		dfEndDate.setValue(DateUtils.today());
	}

}
