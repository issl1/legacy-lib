package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.model.ProfileDefaultQuotationStatus;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationSecUserQueue;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
/**
 * Quotation search panel for front office
 * @author uhout.cheng
 */
public class QuotationSearchPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -635644650780535609L;
		
	private TextField txtReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	
	private EntityRefComboBox<Province> cbxProvince;	
	private DealerComboBox cbxDealer;	
		
	/**
	 * 
	 * @param quotationTablePanel
	 */
	public QuotationSearchPanel(QuotationTablePanel quotationTablePanel) {
		super(I18N.message("quotation.search"), quotationTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {	
		cbxDealer.setSelectedEntity(null);		
		cbxProvince.setSelectedEntity(null);
		txtReference.setValue("");
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtReference = ComponentFactory.getTextField(false, 60, 220);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 220);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 220);
		
        cbxDealer = new DealerComboBox(new ArrayList<Dealer>());                
        cbxDealer.setImmediate(true);
		cbxDealer.setWidth("220px");
				
        cbxProvince = new EntityRefComboBox<>();
		cbxProvince.setRestrictions(new BaseRestrictions<>(Province.class));
		cbxProvince.renderer();
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth("220px");
		
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/quotation/search.html");
		CustomLayout searchLayout = null;
		try {
			searchLayout = new CustomLayout(layoutFile);
			searchLayout.addComponent(new Label(I18N.message("reference")), "lblReference");
			searchLayout.addComponent(txtReference, "txtReference");
			searchLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastNameEn");
			searchLayout.addComponent(txtLastNameEn, "txtLastNameEn");
			searchLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstNameEn");
			searchLayout.addComponent(txtFirstNameEn, "txtFirstNameEn");
			searchLayout.addComponent(new Label(I18N.message("dealer")), "lblDealer");
			searchLayout.addComponent(cbxDealer, "cbxDealer");
			searchLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
			searchLayout.addComponent(cbxProvince, "cbxProvince");
			
		} catch (IOException e) {
		}
		
		return searchLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {
		
		SecUser secUser = UserSessionManager.getCurrentUser();
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
				
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
					 
		List<EWkfStatus> defaultQuotationStatusList = getDefaultQuotationStatus(secUser.getDefaultProfile());
		if (defaultQuotationStatusList != null && !defaultQuotationStatusList.isEmpty()) {
			restrictions.addCriterion(Restrictions.in(WKF_STATUS, defaultQuotationStatusList));
		}
			
		if (cbxProvince.getSelectedEntity() != null || StringUtils.isNotEmpty(txtLastNameEn.getValue())
				|| StringUtils.isNotEmpty(txtFirstNameEn.getValue())) {
			restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
			restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion("quoapp.applicantType", EApplicantType.C);
		}
		
		if (cbxProvince.getSelectedEntity() != null) {
			
			restrictions.addAssociation("app.applicantAddresses", "appaddr", JoinType.INNER_JOIN);
			restrictions.addAssociation("appaddr.address", "addr", JoinType.INNER_JOIN);			
			restrictions.addCriterion("appaddr.addressType", ETypeAddress.MAIN);
			restrictions.addCriterion("addr.province.id", cbxProvince.getSelectedEntity().getId());
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
		
		DetachedCriteria userQuotationSubCriteria = DetachedCriteria.forClass(QuotationSecUserQueue.class, "qusr");
		userQuotationSubCriteria.add(Restrictions.eq("secUser.id", secUser.getId()));
		userQuotationSubCriteria.setProjection(Projections.projectionList().add(Projections.property("qusr.quotation.id")));
		restrictions.addCriterion(Property.forName("id").in(userQuotationSubCriteria));
		
		restrictions.addOrder(Order.desc(QUOTATION_DATE));
		return restrictions;
	}
	
	/**
	 * @param profile
	 * @return
	 */
	private List<EWkfStatus> getDefaultQuotationStatus(SecProfile profile) {
		List<EWkfStatus> quotationStatusList = new ArrayList<EWkfStatus>();
		List<ProfileDefaultQuotationStatus> profileDefaultQuotationStatusList = DataReference.getInstance().getProfileDefaultQuotationStatus();
		if (profileDefaultQuotationStatusList != null && !profileDefaultQuotationStatusList.isEmpty()) {
			for (ProfileDefaultQuotationStatus profileDefaultQuotationStatus : profileDefaultQuotationStatusList) {
				if (profile.getId() == profileDefaultQuotationStatus.getProfile().getId()) {
					quotationStatusList.add(profileDefaultQuotationStatus.getWkfStatus());
				}
			}
		}
		return quotationStatusList;
	}
}
