package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.AreaRestriction;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CreditControl;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignGeneralFormPanel extends AbstractFormPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -2210417207240720248L;
	
	private CampaignFormPanel campaignFormPanel;
	private Campaign campaign;
	private TextField txtCampaignCode;
	private TextField txtFlatRate;
	private TextField txtMaxFlatRate;
	private TextField txtCampaignDesc;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;	
	private CheckBox cbActive;
	private EntityComboBox<Area> cbxZoneOfMarketing;
	
	private EntityComboBox<CreditControl> cbxCreditControl;
	
	/**
	 * 
	 * @param marketingCampaignsPanel
	 */
	public CampaignGeneralFormPanel(CampaignFormPanel campaignFormPanel) {
		 super.init();
		 this.campaignFormPanel = campaignFormPanel;
		 NavigationPanel navigationPanel = addNavigationPanel();
		 navigationPanel.addSaveClickListener(this);
	}
	 
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		campaign.setCode(txtCampaignCode.getValue());
		campaign.setDescEn(txtCampaignDesc.getValue());
		campaign.setDesc(txtCampaignDesc.getValue());
		campaign.setStartDate(dfStartDate.getValue());
		campaign.setEndDate(dfEndDate.getValue());
		campaign.setFlatRate(getDouble(txtFlatRate));
		campaign.setMaxFlatRate(getDouble(txtMaxFlatRate));
		campaign.setCreditControl(cbxCreditControl.getSelectedEntity());
		campaign.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		campaign.setArea(cbxZoneOfMarketing.getSelectedEntity());
		return campaign;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Campaign campaign = (Campaign) getEntity();
		CAM_SRV.saveOrUpdateCampagin(campaign);
		displaySuccess();
		campaignFormPanel.assignValues(campaign.getId()); 
		campaignFormPanel.setNeedRefresh(true);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCampaignCode = ComponentFactory.getTextField(70, 170);
		txtCampaignCode.setEnabled(false);
		txtFlatRate = ComponentFactory.getTextField(70, 170);
		txtMaxFlatRate = ComponentFactory.getTextField(70, 170);
		txtCampaignDesc = ComponentFactory.getTextField(255, 200);
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField();
		
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbxCreditControl = new EntityComboBox<>(CreditControl.class, "descEn");
        cbxCreditControl.setImmediate(true);
        cbxCreditControl.renderer();
		
        cbxZoneOfMarketing = new EntityComboBox<>(Area.class, Area.DESCEN);
        cbxZoneOfMarketing.setImmediate(true);
        AreaRestriction restrictions = new AreaRestriction();
        restrictions.setColType(EColType.MKT);
        cbxZoneOfMarketing.renderer(restrictions);
        
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(getGeneralPanel());
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getGeneralPanel() {
		CustomLayout customLayout = initCustomLayout("/VAADIN/themes/efinance/layouts/", "marketingcampaign/marketingCampaignLayout.html");
		customLayout.addComponent(new Label(I18N.message("campaign.id")), "lblCampaignCode");
		customLayout.addComponent(txtCampaignCode, "txtCampaignCode");
		customLayout.addComponent(new Label(I18N.message("start.date")), "lblStartDate");
		customLayout.addComponent(dfStartDate, "dfStartDate");
		customLayout.addComponent(new Label(I18N.message("end.date")), "lblEndDate");
		customLayout.addComponent(dfEndDate, "dfEndDate");
		customLayout.addComponent(new Label(I18N.message("flat.rate")), "lblFlatRate");
		customLayout.addComponent(txtFlatRate, "txtFlatRate");
		customLayout.addComponent(new Label(I18N.message("max.flat.rate")), "lblMaxFlatRate");
		customLayout.addComponent(txtMaxFlatRate, "txtMaxFlatRate");
		customLayout.addComponent(cbActive, "cbActive");
		
		customLayout.addComponent(new Label(I18N.message("desc")), "lblCampaignDesc");
		customLayout.addComponent(txtCampaignDesc, "txtCampaignDesc");
		customLayout.addComponent(new Label(I18N.message("credit.control")), "lblCreditControl");
		customLayout.addComponent(cbxCreditControl, "cbxCreditControl");
		customLayout.addComponent(new Label(I18N.message("zone.of.marketing")), "lblZoneOfMarketing");
		customLayout.addComponent(cbxZoneOfMarketing, "cbxZoneOfMarketing");
		
		Panel panel = new Panel();
		panel.setContent(customLayout);
		return panel;
	}
	
	/**
	 * 
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		reset();
		if (campaignId != null) {
			campaign = ENTITY_SRV.getById(Campaign.class, campaignId);
			txtCampaignCode.setValue(campaign.getCode());
			txtCampaignDesc.setValue(campaign.getDescEn());
			dfStartDate.setValue(campaign.getStartDate());
			dfEndDate.setValue(campaign.getEndDate());
			txtFlatRate.setValue(AmountUtils.format(campaign.getFlatRate()));
			txtMaxFlatRate.setValue(AmountUtils.format(campaign.getMaxFlatRate()));
			cbxCreditControl.setSelectedEntity(campaign.getCreditControl());
			cbxZoneOfMarketing.setSelectedEntity(campaign.getArea());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		campaign = new Campaign();
		txtCampaignCode.setValue("");
		txtFlatRate.setValue("");
		txtMaxFlatRate.setValue("");
		txtCampaignDesc.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxCreditControl.setSelectedEntity(null);
		cbxZoneOfMarketing.setSelectedEntity(null);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
//		checkMandatoryField(txtCampaignCode, "code");
		checkDuplicatedCode(Campaign.class, txtCampaignCode, campaign.getId(), "code");
		checkMandatoryField(txtCampaignDesc, "name");	
		checkMandatoryDateField(dfStartDate, "start.date");
		checkMandatoryField(txtFlatRate, "flat.rate");
		checkMandatoryField(txtMaxFlatRate, "max.flat.rate");
		checkDoubleField(txtFlatRate, "flat.rate");
		checkDoubleField(txtMaxFlatRate, "max.flat.rate");
		checkMandatorySelectField(cbxCreditControl, "credit.control");
		return errors.isEmpty();
	}
}

