package com.nokor.efinance.gui.ui.panel.contract.repossess;

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

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Contract re possess search panel
 * @author uhout.cheng
 */
public class ContractRepossessSearchPanel extends AbstractSearchPanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -8620575071500944602L;

	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private TextField txtReference;
	private TextField txtFamilyNameEn;
	private TextField txtFirstNameEn;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<Province> cbxProvince;
	
	public ContractRepossessSearchPanel(AbstractTablePanel<Contract> contractRepossessTablePanel) {
		super(I18N.message("search"), contractRepossessTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtReference.setValue("");
		txtFamilyNameEn.setValue("");
		txtFirstNameEn.setValue("");
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtReference = ComponentFactory.getTextField(I18N.message("reference"), false, 60, 220);
		
		txtFamilyNameEn = ComponentFactory.getTextField(I18N.message("lastname.en"), false, 60, 220);	
		txtFirstNameEn = ComponentFactory.getTextField(I18N.message("firstname.en"), false, 60, 220);
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setImmediate(true);
		cbxDealer.setWidth("220px");

		cbxDealerType = new ERefDataComboBox<EDealerType>(I18N.message("dealer.type"), EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 4743882085175723521L;
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
        cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.renderer();
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth("220px");
        
        final GridLayout gridLayout = new GridLayout(5, 1);	
		
		FormLayout formLayoutLeft = new FormLayout();
		formLayoutLeft.addComponent(txtFamilyNameEn);
		formLayoutLeft.addComponent(cbxDealerType);
		
		FormLayout formLayoutMiddle = new FormLayout();
		formLayoutMiddle.addComponent(txtFirstNameEn);
		formLayoutMiddle.addComponent(cbxDealer);
		
		FormLayout formLayoutRight = new FormLayout();
		formLayoutRight.addComponent(txtReference);
		formLayoutRight.addComponent(cbxProvince);
		
		int iCol = 0;
		gridLayout.addComponent(formLayoutLeft, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(formLayoutMiddle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(formLayoutRight, iCol++, 0);
       
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
		
		restrictions.addCriterion(Restrictions.eq(CONTRACT_STATUS, ContractWkfStatus.FIN));
		restrictions.addCriterion(Restrictions.eq("requestRepossess", Boolean.TRUE));
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (cbxProvince.getSelectedEntity() != null || StringUtils.isNotEmpty(txtFamilyNameEn.getValue()) 
				|| StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {	
			
			restrictions.addAssociation("contractApplicants", "contractapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		}
		
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addAssociation("app.applicantAddresses", "appaddr", JoinType.INNER_JOIN);
			restrictions.addAssociation("appaddr.address", "addr", JoinType.INNER_JOIN);			
			restrictions.addCriterion("appaddr.addressType", ETypeAddress.MAIN);
			restrictions.addCriterion("addr.province.id", cbxProvince.getSelectedEntity().getId());
		}
		
		if (StringUtils.isNotEmpty(txtFamilyNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtFamilyNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
}
