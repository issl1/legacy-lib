package com.nokor.efinance.ra.ui.panel.quotation;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Quotation_2 search panel for administration profile
 * @author youhort.ly
 */
public class QuotationSearchOldPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -6404129819897600944L;
	
	private TextField txtQuotationId;
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatus;
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<Province> cbxProvince;
	private TextField txtReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	
	/** */
	public QuotationSearchOldPanel(AbstractTablePanel<Quotation> quotationTablePanel) {
		super(I18N.message("quotation.search"), quotationTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtQuotationId.setValue("");
		cbxDealer.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
		cbxQuotationStatus.setSelectedEntity(null);
		txtReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtReference = ComponentFactory.getTextField(false, 60, 150);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtQuotationId = ComponentFactory.getTextField(false, 20, 150); 
		
		cbxQuotationStatus = new ERefDataComboBox<EWkfStatus>(QuotationWkfStatus.values());
		
        cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
        cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.renderer();
		
		final GridLayout gridLayout = new GridLayout(22, 2);	
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("id")), iCol++, 0);
		gridLayout.addComponent(txtQuotationId, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("quotation.status")), iCol++, 0);
		gridLayout.addComponent(cbxQuotationStatus, iCol++, 0);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
	    gridLayout.addComponent(cbxDealer, iCol++, 0);
	    
	    gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    gridLayout.addComponent(new Label(I18N.message("province")), iCol++, 0);
	    gridLayout.addComponent(cbxProvince, iCol++, 0);
	    iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 1);
		gridLayout.addComponent(txtLastNameEn, iCol++, 1);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 1);
		gridLayout.addComponent(txtFirstNameEn, iCol++, 1);
		
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("reference")), iCol++, 1);
		gridLayout.addComponent(txtReference, iCol++, 1);
		
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);		
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtQuotationId.getValue())) {
			restrictions.addCriterion(Restrictions.eq("id", Long.parseLong(txtQuotationId.getValue())));
		}
		
		if (cbxQuotationStatus.getSelectedEntity() != null) { 
			if (cbxQuotationStatus.getSelectedEntity().equals(QuotationWkfStatus.ACT)) {
				restrictions.addAssociation("contract", "cotra", JoinType.INNER_JOIN);
				restrictions.addCriterion(Restrictions.eq("cotra.contractStatus", ContractWkfStatus.FIN));
				restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
			} else {
				restrictions.addCriterion(Restrictions.eq(WKF_STATUS, cbxQuotationStatus.getSelectedEntity()));
			}
		} else if (!cbxQuotationStatus.getValueMap().values().isEmpty()) {
			restrictions.addCriterion(Restrictions.in(WKF_STATUS, cbxQuotationStatus.getValueMap().values()));
		} else {
			restrictions.addCriterion(Restrictions.isNull(WKF_STATUS));
		}
		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue()) || StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {	
			restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("quoapp.applicantType", EApplicantType.C);
		}
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addAssociation("app.applicantAddresses", "appaddr", JoinType.INNER_JOIN);
			restrictions.addAssociation("appaddr.address", "addr", JoinType.INNER_JOIN);			
			restrictions.addCriterion("quoapp.applicantType", EApplicantType.C);
			restrictions.addCriterion("appaddr.addressType", ETypeAddress.MAIN);
			restrictions.addCriterion("addr.province.id", cbxProvince.getSelectedEntity().getId());
		}
		if (StringUtils.isNotEmpty(txtLastNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtLastNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstNameEn.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.desc(QUOTATION_DATE));
		return restrictions;
	}

}
