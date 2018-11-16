package com.nokor.efinance.gui.ui.panel.quotation.followup;

import java.io.IOException;
import java.io.InputStream;

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
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Quotation follow up search panel for front office
 * @author sok.vina
 */
public class QuotationFollowUpSearchPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = 5826304016516938541L;
	
	private EntityService entityService;
	
	private TextField txtReference;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatus;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private EntityRefComboBox<Province> cbxProvince;
	
	private DealerComboBox cbxDealer;
	private ValueChangeListener valueChangeListener;
	
	/**
	 * 
	 * @param quotationTablePanel
	 */
	public QuotationFollowUpSearchPanel(AbstractTablePanel<Quotation> quotationTablePanel) {
		super(I18N.message("search"), quotationTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDealerType.setSelectedEntity(null);
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
		txtReference = ComponentFactory.getTextField(false, 60, 220);
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 220);        
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 220);
		
        cbxQuotationStatus = new ERefDataComboBox<EWkfStatus>(QuotationWkfStatus.values());
        cbxQuotationStatus.setImmediate(true);
        cbxQuotationStatus.setWidth("220px");
        
        entityService = SpringUtils.getBean(EntityService.class);
        cbxDealer = new DealerComboBox(entityService.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		cbxDealer.setImmediate(true);
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -7948873543031759178L;
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
		
        cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.renderer();
		cbxProvince.setWidth("220px");
		cbxProvince.setImmediate(true);
		
		String template = "quotationSearch";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastNameEn");
		customLayout.addComponent(txtLastNameEn, "txtLastNameEn");
		customLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstNameEn");
		customLayout.addComponent(txtFirstNameEn, "txtFirstNameEn");
		customLayout.addComponent(new Label(I18N.message("dealer.type")), "lblDealerType");
		customLayout.addComponent(cbxDealerType, "cbxDealerType");
		
		customLayout.addComponent(new Label(I18N.message("dealer")), "lblDealer");
		customLayout.addComponent(cbxDealer, "cbxDealer");
		customLayout.addComponent(new Label(I18N.message("reference")), "lblReference");
		customLayout.addComponent(txtReference, "txtReference");
		customLayout.addComponent(new Label(I18N.message("quotation.status")), "lblQuotationStatus");
		customLayout.addComponent(cbxQuotationStatus, "cbxQuotationStatus");
		customLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
		customLayout.addComponent(cbxProvince, "cbxProvince");
		
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(customLayout);
                
		return horizontalLayout;
	}
	
	/**
	 * @return
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
	public BaseRestrictions<Quotation> getRestrictions() {
		
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);		
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		if (cbxQuotationStatus.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(WKF_STATUS, cbxQuotationStatus.getSelectedEntity()));
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
				
		restrictions.addOrder(Order.desc(QUOTATION_DATE));
		return restrictions;
	}

}
