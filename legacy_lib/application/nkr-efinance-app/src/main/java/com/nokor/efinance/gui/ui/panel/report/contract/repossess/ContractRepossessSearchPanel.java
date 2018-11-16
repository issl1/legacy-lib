package com.nokor.efinance.gui.ui.panel.report.contract.repossess;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTask;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.AuctionWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Search panel for Collection Supervisor
 * @author bunlong.taing
 */
public class ContractRepossessSearchPanel extends AbstractSearchPanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -4527788882260581871L;
	private EntityService entityService;
	private ValueChangeListener valueChangeListener;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserComboBox cbxCreditOfficer;
	private SecUserComboBox cbxCollectionOfficer;
	private TextField txtContractReference;
	private ERefDataComboBox<EWkfStatus> cbxCollectionStatus;
	private ERefDataComboBox<EColTask> cbxCollectionTask;
	private EntityRefComboBox<EColGroup> cbxCollectionGroup;
	//private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private NumberField txtOverdueFrom;
	private NumberField txtOverdueTo;

	/**
	 * @param tablePanel
	 */
	public ContractRepossessSearchPanel(FOContractRepossessTablePanel tablePanel) {
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
		
		cbxCreditOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CO, EStatusRecord.ACTIV));
		cbxCreditOfficer.setCaption(I18N.message("credit.officer"));
		cbxCollectionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CC, EStatusRecord.ACTIV));
		cbxCollectionOfficer.setCaption(I18N.message("collection.officer"));
		txtContractReference = ComponentFactory.getTextField("contract.reference", false, 20, 150);
		
		cbxCollectionStatus = new ERefDataComboBox<EWkfStatus>(I18N.message("collection.status"), EWkfStatus.class);
		cbxCollectionTask = new ERefDataComboBox<EColTask>(I18N.message("collection.task"), EColTask.class);
		cbxCollectionGroup = new EntityRefComboBox<EColGroup>(I18N.message("collection.group"));
		cbxCollectionGroup.setRestrictions(new BaseRestrictions<>(EColGroup.class));
		cbxCollectionGroup.renderer();
		
		cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"));
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.setImmediate(true);
		cbxPaymentMethod.renderer();
		
		txtOverdueFrom = ComponentFactory.getNumberField("overdue.from", false, 100, 150);
		txtOverdueTo = ComponentFactory.getNumberField("overdue.to", false, 100, 150);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxDealerType);
		formLayout.addComponent(cbxDealer);
		formLayout.addComponent(txtOverdueFrom);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(cbxCreditOfficer);
		formLayout.addComponent(cbxCollectionOfficer);
		formLayout.addComponent(txtOverdueTo);
		horizontalLayout.addComponent(formLayout);

		formLayout = new FormLayout();
		formLayout.addComponent(txtContractReference);
		formLayout.addComponent(cbxCollectionStatus);
		formLayout.addComponent(cbxPaymentMethod);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(cbxCollectionTask);
		formLayout.addComponent(cbxCollectionGroup);
		horizontalLayout.addComponent(formLayout);
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("contractStatus", ContractWkfStatus.REP));
		restrictions.addCriterion(Restrictions.eq("auctionStatus", AuctionWkfStatus.EVA));
		restrictions.addCriterion(Restrictions.eq("requestRepossess", Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq("overdue", Boolean.TRUE));
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer", cbxDealer.getSelectedEntity()));
		}
		
		if (cbxCreditOfficer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("creditOfficer.id", cbxCreditOfficer.getSelectedEntity().getId()));
		}
		
		if (cbxCollectionOfficer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionOfficer.id", cbxCollectionOfficer.getSelectedEntity().getId()));
		}
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("reference", txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtOverdueFrom.getValue()) || StringUtils.isNotEmpty(txtOverdueTo.getValue())) {
			restrictions.addAssociation("contractOtherData", "cod", JoinType.INNER_JOIN);
		}
		
		if (StringUtils.isNotEmpty(txtOverdueFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("cod.nbOverdueInDays", Integer.valueOf(txtOverdueFrom.getValue())));
		}
		
		if (StringUtils.isNotEmpty(txtOverdueTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("cod.nbOverdueInDays", Integer.valueOf(txtOverdueTo.getValue())));
		}
		
		if (cbxCollectionStatus.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionStatus.id", cbxCollectionStatus.getSelectedEntity().getId()));
		}
		
		if (cbxCollectionTask.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionTask.id", cbxCollectionTask.getSelectedEntity().getId()));
		}
		
		if (cbxCollectionGroup.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionGroup.id", cbxCollectionGroup.getSelectedEntity().getId()));
		}
		
		if (cbxPaymentMethod.getSelectedEntity() != null) { 
			restrictions.addAssociation("contractOtherData", "cod", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("cod.lastPaidPaymentMethod.id", cbxPaymentMethod.getSelectedEntity().getId()));
		}
		
		restrictions.addOrder(Order.asc(START_DATE));
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxCreditOfficer.setSelectedEntity(null);
		cbxCollectionOfficer.setSelectedEntity(null);
		txtContractReference.setValue("");
		cbxCollectionGroup.setSelectedEntity(null);
		cbxCollectionStatus.setSelectedEntity(null);
		cbxCollectionTask.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		txtOverdueFrom.setValue("");
		txtOverdueTo.setValue("");
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
