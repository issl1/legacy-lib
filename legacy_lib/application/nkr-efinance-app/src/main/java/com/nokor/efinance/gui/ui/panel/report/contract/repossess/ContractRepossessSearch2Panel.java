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
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.workflow.model.EWkfStatus;
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
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Search Panel for Collection Officer
 * @author bunlong.taing
 */
public class ContractRepossessSearch2Panel extends AbstractSearchPanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 2442390998502051867L;
	
	private EntityService entityService;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private SecUserComboBox cbxCreditOfficer;
	private TextField txtContractReference;
	private ERefDataComboBox<EWkfStatus> cbxCollectionStatus;
	//private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtFamilyNameEn;
	private TextField txtFirstNameEn;
	
	private ValueChangeListener valueChangeListener;

	/**
	 * @param tablePanel
	 */
	public ContractRepossessSearch2Panel(FOContractRepossessTablePanel tablePanel) {
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
		txtContractReference = ComponentFactory.getTextField("contract.reference", false, 20, 150);
		txtFamilyNameEn = ComponentFactory.getTextField("lastname.en", false, 60, 150);
		txtFirstNameEn = ComponentFactory.getTextField("firstname.en", false, 60, 150);
		cbxCollectionStatus = new ERefDataComboBox<EWkfStatus>(I18N.message("collection.status"), EWkfStatus.class);
		
		cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"));
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.setImmediate(true);
		cbxPaymentMethod.setWidth("150px");
		cbxPaymentMethod.renderer();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxDealerType);
		formLayout.addComponent(cbxDealer);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(txtFamilyNameEn);
		formLayout.addComponent(txtFirstNameEn);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(cbxCreditOfficer);
		formLayout.addComponent(cbxCollectionStatus);
		horizontalLayout.addComponent(formLayout);
		
		formLayout = new FormLayout();
		formLayout.addComponent(txtContractReference);
		formLayout.addComponent(cbxPaymentMethod);
		horizontalLayout.addComponent(formLayout);
		
		return horizontalLayout;
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

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {
		
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("contractStatus", ContractWkfStatus.REP));
		restrictions.addCriterion(Restrictions.eq("auctionStatus", AuctionWkfStatus.EVA));
		restrictions.addCriterion(Restrictions.eq("requestRepossess", Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq("overdue", Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq("collectionOfficer.id", secUser.getId()));
		
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
		
		if (StringUtils.isNotEmpty(txtContractReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("reference", txtContractReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFamilyNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("contractApplicants", "contractapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		}
		
		if (StringUtils.isNotEmpty(txtFamilyNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtFamilyNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxCollectionStatus.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("collectionStatus", cbxCollectionStatus.getSelectedEntity()));			
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
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		cbxCreditOfficer.setSelectedEntity(null);
		txtContractReference.setValue("");
		cbxCollectionStatus.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		txtFamilyNameEn.setValue("");
		txtFirstNameEn.setValue("");
	}

}
