package com.nokor.efinance.gui.ui.panel.report.collectionincentive;

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

import com.nokor.efinance.core.collection.model.CollectionIncentiveReport;
import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTask;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Search panel for Collection Incentive Report
 * @author buntha.chea
 */
public class CollectionIncentiveSearchPanel extends AbstractSearchPanel<CollectionIncentiveReport> {

	/** */
	private static final long serialVersionUID = -4527788882260581871L;
	private EntityService entityService;
	private ValueChangeListener valueChangeListener;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserComboBox cbxCollectionOfficer;
	private ERefDataComboBox<EColTask> cbxCollectionTask;
	private EntityRefComboBox<EColGroup> cbxCollectionGroup;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;


	/**
	 * @param tablePanel
	 */
	public CollectionIncentiveSearchPanel(CollectionIncentiveTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		entityService = SpringUtils.getBean(EntityService.class);
		cbxDealer = new DealerComboBox(I18N.message("dealer"), entityService.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7442302732430560056L;
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
		
		
		
		cbxCollectionTask = new ERefDataComboBox<EColTask>(I18N.message("collection.task"), EColTask.class);
		cbxCollectionGroup = new EntityRefComboBox<EColGroup>(I18N.message("collection.group"));
		cbxCollectionGroup.setRestrictions(new BaseRestrictions<>(EColGroup.class));
		cbxCollectionGroup.renderer();
		
		cbxCollectionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CC, EStatusRecord.ACTIV));
		cbxCollectionOfficer.setCaption(I18N.message("collection.officer"));
	
		dfStartDate = ComponentFactory.getAutoDateField("StartDate", false);
		dfStartDate.setValue(DateUtils.todayH00M00S00());
		dfStartDate.setWidth("95px");
		
		dfEndDate = ComponentFactory.getAutoDateField("EndDate", false);
		dfEndDate.setValue(DateUtils.todayH00M00S00());
		dfEndDate.setWidth("95px");
		
		
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxDealerType);
		formLayout.addComponent(cbxDealer);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(cbxCollectionOfficer);
		formLayout.addComponent(cbxCollectionTask);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(dfStartDate);
		formLayout.addComponent(cbxCollectionGroup);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(dfEndDate);
		horizontalLayout.addComponent(formLayout);
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<CollectionIncentiveReport> getRestrictions() {
		BaseRestrictions<CollectionIncentiveReport> restrictions = new BaseRestrictions<>(CollectionIncentiveReport.class);
		restrictions.addAssociation("payment", "pamet", JoinType.INNER_JOIN);
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("pamet.dealer", "indeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("indeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("pamet.dealer", cbxDealer.getSelectedEntity()));
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("pamet.paymentDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("pamet.paymentDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
					
		if (cbxCollectionOfficer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionOfficer.id", cbxCollectionOfficer.getSelectedEntity().getId()));	
		}
		
		if (cbxCollectionTask.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionTask.id", cbxCollectionTask.getSelectedEntity().getId()));	
		}
		
		if (cbxCollectionGroup.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionGroup.id", cbxCollectionGroup.getSelectedEntity().getId()));
		}
		
		restrictions.addOrder(Order.asc("pamet.paymentDate"));
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxCollectionOfficer.setSelectedEntity(null);
		cbxCollectionGroup.setSelectedEntity(null);
		cbxCollectionTask.setSelectedEntity(null);
	}
	
	/**
	 * Get all Dealer except DealerType = OTH
	 * @return List of Dealers
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
}
