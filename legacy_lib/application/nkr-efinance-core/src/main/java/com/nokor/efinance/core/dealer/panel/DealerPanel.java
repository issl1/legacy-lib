package com.nokor.efinance.core.dealer.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.EPlaceInstallment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.eref.EMediaPromoting;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Dealer panel
 * @author ly.youhort
 */
public class DealerPanel extends AbstractTabPanel implements DealerEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = 6849476207051876799L;
		
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private DateField dfQuotationDate;
	private SecUserComboBox cbxCreditOfficer;
	private SecUserComboBox cbxProductionOfficer;
	private ERefDataComboBox<EPlaceInstallment> cbxPlaceInstallment;
	private ERefDataComboBox<EMediaPromoting> cbxWayOfKnowing;	
	private HorizontalLayout contentLayout;
	private ValueChangeListener valueChangeListener;
	
	private ValueChangeListener dealerValueChangeListener;
	
	
	/**
	 * Default constructor
	 */
	public DealerPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * 
	 * @return get dealer combobox
	 */
	public DealerComboBox getDealerComboBox(){	
		return cbxDealer;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		BaseRestrictions<Dealer> res = new BaseRestrictions<Dealer>(Dealer.class);
		res.getStatusRecordList().add(EStatusRecord.ACTIV);
		cbxDealer = new DealerComboBox(ENTITY_SRV.list(res));
		cbxDealer.setWidth("220px");
		cbxDealer.setImmediate(true);
		cbxDealer.setSelectedEntity(null);
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6789482796826178900L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				//restrictions.addCriterion(Restrictions.eq("dealerType", DealerType.OTH));
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		
		List<SecUser> usersByProfileCO = new ArrayList<SecUser>();
		List<SecUser> usersByProfilePO = new ArrayList<SecUser>();
		 if (ProfileUtil.isPOS()) {
			SecUserDetail secUserDetail = entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
			if (secUserDetail != null && secUserDetail.getDealer() != null) {
				cbxDealerType.setEnabled(false);
				cbxDealer.setEnabled(false);
				BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<>(SecUserDetail.class);			
				restrictions.addCriterion(Restrictions.eq("dealer.id", secUserDetail.getDealer().getId()));
				List<SecUserDetail> secUserDetails = entityService.list(restrictions);					
				if (secUserDetails != null) {
					for (SecUserDetail userDetail : secUserDetails) {
						if (ProfileUtil.isProfileExist(FMProfile.CO, userDetail.getSecUser().getProfiles())) {
							usersByProfileCO.add(userDetail.getSecUser());
						}
						if (ProfileUtil.isProfileExist(FMProfile.PO, userDetail.getSecUser().getProfiles())) {
							usersByProfilePO.add(userDetail.getSecUser());
						}
					}
				}
			} else {
				cbxDealerType.setEnabled(true);
				cbxDealer.setEnabled(true);
			}
		}
		 
		
		
		if (ProfileUtil.isProfileExist(FMProfile.CO, secUser.getProfiles())) {
			addUser(usersByProfileCO, secUser);
		}
		
		if (ProfileUtil.isProfileExist(FMProfile.PO, secUser.getProfiles())) {
			addUser(usersByProfilePO, secUser);
		}
				
		if (usersByProfileCO.isEmpty()) {
			usersByProfileCO = DataReference.getInstance().getUsers(FMProfile.CO);
		}
		if (usersByProfilePO.isEmpty()) {
			usersByProfilePO = DataReference.getInstance().getUsers(FMProfile.PO);
		}
		
		cbxCreditOfficer = new SecUserComboBox(usersByProfileCO);
		cbxProductionOfficer = new SecUserComboBox(usersByProfilePO);
		cbxProductionOfficer.setRequired(false);
		
		dfQuotationDate = ComponentFactory.getAutoDateField();
		dfQuotationDate.setWidth(95,Unit.PIXELS);
		cbxPlaceInstallment = new ERefDataComboBox<EPlaceInstallment>(EPlaceInstallment.class);
		cbxWayOfKnowing = new ERefDataComboBox<EMediaPromoting>(EMediaPromoting.class);
		String template = "quotationDealer";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		
		customLayout.addComponent(new Label(I18N.message("dealer.type")), "lblDealerType");
		customLayout.addComponent(cbxDealerType, "cbxDealerType");
		customLayout.addComponent(new Label(I18N.message("dealer")), "lblDealer");
		customLayout.addComponent(cbxDealer, "cbxDealer");
		customLayout.addComponent(new Label(I18N.message("quotation.date")), "lblQuotationDate");
		customLayout.addComponent(dfQuotationDate, "dfQuotationDate");
		customLayout.addComponent(new Label(I18N.message("credit.officer")), "lblCreditOfficer");
		customLayout.addComponent(cbxCreditOfficer, "cbxCreditOfficer");
		customLayout.addComponent(new Label(I18N.message("production.officer")), "lblProductionOfficer");
		customLayout.addComponent(cbxProductionOfficer, "cbxProductionOfficer");
		customLayout.addComponent(new Label(I18N.message("place.installment")), "lblPlaceInstallment");
		customLayout.addComponent(cbxPlaceInstallment, "cbxPlaceInstallment");
		customLayout.addComponent(new Label(I18N.message("way.of.knowing")), "lblWayOfKnowing");
		customLayout.addComponent(cbxWayOfKnowing, "cbxWayOfKnowing");

		contentLayout = new HorizontalLayout();
		contentLayout.setMargin(true);
		contentLayout.addComponent(customLayout);
		
		return contentLayout;
	}
	
	/**
	 * @param users
	 * @param user
	 */
	private void addUser(List<SecUser> users, SecUser user) {
		boolean found = false;
		for (SecUser secUser : users) {
			if (secUser.getId().equals(user.getId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			users.add(user);
		}
	}
	
	/**
	 * Assign value to quotation 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		
		cbxDealer.removeValueChangeListener(dealerValueChangeListener);
				
		cbxDealer.setSelectedEntity(quotation.getDealer());
		cbxDealerType.removeValueChangeListener(valueChangeListener);
		cbxDealerType.setSelectedEntity(cbxDealer.getSelectedEntity() != null ? cbxDealer.getSelectedEntity().getDealerType() : null);
		cbxDealerType.addValueChangeListener(valueChangeListener);
		if (quotation.getQuotationDate() == null) {
			dfQuotationDate.setValue(DateUtils.today());
		} else {
			dfQuotationDate.setValue(quotation.getQuotationDate());
		}
		
		cbxCreditOfficer.addValue(quotation.getCreditOfficer());
		cbxProductionOfficer.addValue(quotation.getProductionOfficer());
		
		cbxCreditOfficer.setSelectedEntity(quotation.getCreditOfficer());
		cbxProductionOfficer.setSelectedEntity(quotation.getProductionOfficer());
		cbxPlaceInstallment.setSelectedEntity(quotation.getPlaceInstallment());
		
		cbxWayOfKnowing.setSelectedEntity(quotation.getWayOfKnowing());
		if (ProfileUtil.isAdmin()) {
			setEnabledDealer(true);
		}else if(ProfileUtil.isCreditOfficerMovable()){
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}else {
			setEnabled(quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) && !quotation.getPreviousWkfStatus().equals(QuotationWkfStatus.APV));
		}
		
		
		cbxDealer.addValueChangeListener(dealerValueChangeListener);
		
	}
	
	/**
	 * Get quotation
	 * @param quotation
	 * @return
	 */
	public Quotation getQuotation(Quotation quotation) {
		quotation.setDealer(cbxDealer.getSelectedEntity());
		quotation.setQuotationDate(dfQuotationDate.getValue());
		quotation.setCreditOfficer(cbxCreditOfficer.getSelectedEntity());
		quotation.setProductionOfficer(cbxProductionOfficer.getSelectedEntity());
		quotation.setPlaceInstallment(cbxPlaceInstallment.getSelectedEntity());
		quotation.setWayOfKnowing(cbxWayOfKnowing.getSelectedEntity());
		// TODO PYT
//		if (dealerChanged && quotation.getWkfStatus().equals(WkfQuotationStatus.QUO) 
//				&& quotation.getAsset() != null && quotation.getAsset().getAsmodIdFk() != null) {
//			AssetModel assetModel = quotationService.getById(AssetModel.class, quotation.getAsset().getAsmodIdFk());
//			if (assetModel !=null && assetModel.getCalculMethod().equals(ECalculMethod.MAT)) {
//				quotation.getAsset().setTiCashPriceUsd(null);
//			}
//		}
		// check sub dealer 
		if (quotation.getDealer().getDealerType() != EDealerType.HEAD) {
			if (quotation.getAsset() != null) {
				quotation.getAsset().setModelUsed(false);
			}
		}
		return quotation;
	}
	
	/**
	 * @param enabled
	 */
	public void setEnabledDealer(boolean enabled) {
		cbxDealerType.setEnabled(enabled);
	    cbxDealer.setEnabled(enabled);
		cbxCreditOfficer.setEnabled(enabled);
		cbxPlaceInstallment.setEnabled(enabled);
		cbxProductionOfficer.setEnabled(enabled);
		cbxWayOfKnowing.setEnabled(enabled);
		dfQuotationDate.setEnabled(enabled);
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxDealer, "dealer");
		checkMandatoryDateField(dfQuotationDate, "quotation.date");
		checkMandatorySelectField(cbxCreditOfficer, "credit.officer");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxDealer, "dealer");
		checkMandatoryDateField(dfQuotationDate, "quotation.date");
		checkMandatorySelectField(cbxCreditOfficer, "credit.officer");
		checkMandatorySelectField(cbxPlaceInstallment, "place.installment");
		checkMandatorySelectField(cbxWayOfKnowing, "way.of.knowing");
		return errors;
	}

	/**
	 * Reset 
	 */
	public void reset() {
		assignValues(new Quotation());
	}
}
