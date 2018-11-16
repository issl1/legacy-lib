package com.nokor.efinance.ra.ui.panel.dealer.attribute;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAttribute;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.dealer.model.ELadderOption;
import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EChargePoint;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Dealer Attribute Popup Panel
 * @author bunlong.taing
 */
public class DealerAttributePopupPanel extends Window implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = 370723109112491389L;
	
	private Button btnSave;
	private Button btnCancel;
	
	private VerticalLayout messagePanel;
	
	private EntityComboBox<AssetMake> cbxAssetMake;
	private EntityComboBox<AssetCategory> cbxAssetCategory;
	private ComboBox cbxInsuranceCoverageDuration;
	private TextField txtContractFee;
	private EntityRefComboBox<EChargePoint> cbxContractFeeChargePoint;
	
	private TextField txtCommission1Amount;
	private EntityRefComboBox<EChargePoint> cbxCommission1ChargePoint;
	
	private CheckBox cbCommission2Enabled;
	private Label lblLadderOption;
	private ERefDataComboBox<ELadderOption> cbxLadderOption;
	private Label lblLadderType;
	private EntityComboBox<LadderType> cbxLadderType;
	
	private Long attributeId;
	private DealerAttributeFormPanel formPanel;
	
	private Label lblInsuranceFeeChargePoint;
	private CheckBox cbChargeInsuranceFee;
	private ERefDataComboBox<EChargePoint> cbxInsuranceFeeChargePoint;
	
	private EntityComboBox<Organization> cbxInsuranceCompanies;
	
	/**
	 * 
	 * @param formPanel
	 */
	public DealerAttributePopupPanel(DealerAttributeFormPanel formPanel) {
		setCaption(I18N.message("attribute"));
		setModal(true);
		setResizable(false);
		this.formPanel = formPanel;
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"), new ClickListener() {
			/** */
			private static final long serialVersionUID = 4037790288094711541L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.setVisible(false);
				if (validate()) {
					DealerAttribute attribute = getEntity();
					DEA_SRV.saveOrUpdate(attribute);
					close();
					formPanel.refresh();
				} else {
					displayAllErrorsPanel();
				}
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
		
		btnCancel = new NativeButton(I18N.message("cancel"), new ClickListener() {
			/** */
			private static final long serialVersionUID = 8118559000849589240L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(messagePanel);
		content.addComponent(createForm());
		setContent(content);
	}
	
	/**
	 * Create form
	 * @return
	 */
	private Component createForm() {
		cbxAssetMake = new EntityComboBox<>(AssetMake.class, AssetMake.DESCLOCALE);
		cbxAssetMake.renderer();
		cbxAssetMake.setWidth(150, Unit.PIXELS);
		
		cbxAssetCategory = new EntityComboBox<AssetCategory>(AssetCategory.class, AssetMake.DESCLOCALE);
		cbxAssetCategory.renderer();
		cbxAssetCategory.setWidth(150, Unit.PIXELS);
		
		cbxInsuranceCoverageDuration = ComponentFactory.getComboBox();
		cbxInsuranceCoverageDuration.setWidth(150, Unit.PIXELS);
		cbxInsuranceCoverageDuration.addItem(1);
		cbxInsuranceCoverageDuration.addItem(2);
		
		txtContractFee = ComponentFactory.getTextField(100, 150);
		cbxContractFeeChargePoint = new EntityRefComboBox<EChargePoint>(EChargePoint.values());
		cbxContractFeeChargePoint.setWidth(150, Unit.PIXELS);
		
		txtCommission1Amount = ComponentFactory.getTextField(100, 150);
		cbxCommission1ChargePoint = new EntityRefComboBox<EChargePoint>(EChargePoint.values());
		cbxCommission1ChargePoint.setWidth(150, Unit.PIXELS);
		
		cbCommission2Enabled = new CheckBox(I18N.message("commission.2.enabled"));
		cbCommission2Enabled.setImmediate(true);
		
		lblLadderOption = ComponentLayoutFactory.getLabelCaptionRequired("ladder.option");
		lblLadderOption.setVisible(false);
		
		cbxLadderOption = new ERefDataComboBox<ELadderOption>(ELadderOption.values());
		cbxLadderOption.setVisible(false);
		cbxLadderOption.setImmediate(true);
		cbxLadderOption.setWidth(150, Unit.PIXELS);
		
		lblLadderType = ComponentLayoutFactory.getLabelCaptionRequired("ladder.type");
		lblLadderType.setVisible(false);
		
		cbxLadderType =  new EntityComboBox<>(LadderType.class, LadderType.DESCEN);
		cbxLadderType.renderer();
		cbxLadderType.setVisible(false);
		cbxLadderType.setWidth(150, Unit.PIXELS);
		
		cbCommission2Enabled.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 7217642645033604564L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				lblLadderOption.setVisible(cbCommission2Enabled.getValue());
				cbxLadderOption.setVisible(cbCommission2Enabled.getValue());
				if (!cbCommission2Enabled.getValue()) {
					cbxLadderOption.setSelectedEntity(null);
					cbxLadderType.setSelectedEntity(null);
				}
				
			}
		});
		
		cbxLadderOption.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = -1240957885386945849L;
			
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbCommission2Enabled.getValue() && cbxLadderOption.getSelectedEntity() != null && cbxLadderOption.getSelectedEntity().equals(ELadderOption.UNIQUE)) {
					cbxLadderType.setVisible(true);
					lblLadderType.setVisible(true);
				} else {
					cbxLadderType.setVisible(false);
					cbxLadderType.setSelectedEntity(null);
					lblLadderType.setVisible(false);
				}
			}
		});
		
		lblInsuranceFeeChargePoint = ComponentLayoutFactory.getLabelCaption("insurance.fee.charge.point");
		cbChargeInsuranceFee = new CheckBox(I18N.message("charge.insurance.fee"));
		cbChargeInsuranceFee.setImmediate(true);
		cbxInsuranceFeeChargePoint = new ERefDataComboBox<>(EChargePoint.values());
		cbxInsuranceFeeChargePoint.setWidth(150, Unit.PIXELS);
		cbChargeInsuranceFee.addValueChangeListener(new ValueChangeListener() {
			/**
			 */
			private static final long serialVersionUID = 931593382831440377L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				lblInsuranceFeeChargePoint.setVisible(cbChargeInsuranceFee.getValue());
				cbxInsuranceFeeChargePoint.setVisible(cbChargeInsuranceFee.getValue());
				if (!cbCommission2Enabled.getValue()) {
					cbxInsuranceFeeChargePoint.setSelectedEntity(null);
				}
			}
		});
		
		cbxInsuranceCompanies = new EntityComboBox<Organization>(Organization.class, Organization.NAMELOCALE);
        OrganizationRestriction organizationRestriction = new OrganizationRestriction();
        organizationRestriction.setTypeOrganization(ETypeOrganization.INSURANCE);
        cbxInsuranceCompanies.renderer(organizationRestriction);
        cbxInsuranceCompanies.setWidth(150, Unit.PIXELS);
		
		Label lblAssetCategory = ComponentLayoutFactory.getLabelCaptionRequired("asset.category");
		Label lblAssetMake = ComponentLayoutFactory.getLabelCaptionRequired("asset.make");
		Label lblContractFee = ComponentLayoutFactory.getLabelCaptionRequired("contract.fee");
		Label lblContractFeeChargePoint = ComponentLayoutFactory.getLabelCaptionRequired("contract.fee.charge.point");
		Label lblCommission1 = ComponentLayoutFactory.getLabelCaptionRequired("commission.1");
		Label lblCommission1ChargePoint = ComponentLayoutFactory.getLabelCaptionRequired("commission.1.charge.point");
		Label lblInsuranceCoverageDuration = ComponentLayoutFactory.getLabelCaptionRequired("insurance.coverage.duration");
		Label lblInsuranceCompanies = ComponentLayoutFactory.getLabelCaptionRequired("insurance.companies");
		
		HorizontalLayout ladderOptionLayout = new HorizontalLayout();
		ladderOptionLayout.setSpacing(true);
		ladderOptionLayout.addComponent(lblLadderOption);
		ladderOptionLayout.addComponent(cbxLadderOption);
		
		GridLayout gridLayout = new GridLayout(4, 8);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(false, true, true, true));
		
		int iCol = 0;
		gridLayout.addComponent(lblAssetCategory, iCol++, 0);
		gridLayout.addComponent(cbxAssetCategory, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblAssetMake, iCol++, 1);
		gridLayout.addComponent(cbxAssetMake, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblContractFee, iCol++, 2);
		gridLayout.addComponent(txtContractFee, iCol++, 2);
		gridLayout.addComponent(lblContractFeeChargePoint, iCol++, 2);
		gridLayout.addComponent(cbxContractFeeChargePoint, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(lblCommission1, iCol++, 3);
		gridLayout.addComponent(txtCommission1Amount, iCol++, 3);
		gridLayout.addComponent(lblCommission1ChargePoint, iCol++, 3);
		gridLayout.addComponent(cbxCommission1ChargePoint, iCol++, 3);

		iCol = 0;
		gridLayout.addComponent(cbCommission2Enabled, iCol++, 4);
		gridLayout.addComponent(ladderOptionLayout, iCol++, 4);
		gridLayout.addComponent(lblLadderType, iCol++, 4);
		gridLayout.addComponent(cbxLadderType, iCol++, 4);
		
		iCol = 0;
		gridLayout.addComponent(lblInsuranceCoverageDuration, iCol++, 5);
		gridLayout.addComponent(cbxInsuranceCoverageDuration, iCol++, 5);
		
		iCol = 0;
		gridLayout.addComponent(lblInsuranceCompanies, iCol++, 6);
		gridLayout.addComponent(cbxInsuranceCompanies, iCol++, 6);
		
		iCol = 0;
		gridLayout.addComponent(cbChargeInsuranceFee, iCol++, 7);
		gridLayout.addComponent(lblInsuranceFeeChargePoint, iCol++, 7);
		gridLayout.addComponent(cbxInsuranceFeeChargePoint, iCol++, 7);

		gridLayout.setComponentAlignment(cbCommission2Enabled, Alignment.MIDDLE_LEFT);
		
		return gridLayout;
	}
	
	/**
	 * Assign values to controls
	 * @param attributeId
	 */
	public void assignValue(Long attributeId) {
		reset();
		if (EDealerType.HEAD.equals(formPanel.getDealer().getDealerType())) {
			cbxLadderOption.assignValueMap(Arrays.asList(ELadderOption.GROUP, ELadderOption.UNIQUE));
		} else {
			cbxLadderOption.assignValueMap(ELadderOption.values());
		}
		if (attributeId != null) {
			this.attributeId = attributeId;
			DealerAttribute attribute = DEA_SRV.getById(DealerAttribute.class, attributeId);
			
			cbxAssetMake.setSelectedEntity(attribute.getAssetMake());
			cbxAssetCategory.setSelectedEntity(attribute.getAssetCategory());
			cbxInsuranceCoverageDuration.setValue(attribute.getInsuranceCoverageDuration() != null ? attribute.getInsuranceCoverageDuration() : null);
			txtContractFee.setValue(AmountUtils.format(attribute.getTiContractFeeAmount()));
			cbxContractFeeChargePoint.setSelectedEntity(attribute.getContractFeeChargePoint());
			txtCommission1Amount.setValue(AmountUtils.format(attribute.getTiCommission1Amount()));
			cbxCommission1ChargePoint.setSelectedEntity(attribute.getCommission1ChargePoint());

			cbCommission2Enabled.setValue(attribute.isCommission2Enabled());
			cbxLadderOption.setSelectedEntity(attribute.getLadderOption());
			cbxLadderType.setSelectedEntity(attribute.getLadderType());
			
			cbxInsuranceCompanies.setSelectedEntity(attribute.getInsuranceCompany());
			cbChargeInsuranceFee.setValue(attribute.isInsuranceFeeEnabled());
			cbxInsuranceFeeChargePoint.setSelectedEntity(attribute.getInsuranceFeeChargePoint());
		}
	}
	
	/**
	 * Get Entity
	 * @return
	 */
	private DealerAttribute getEntity() {
		DealerAttribute attribute = null;
		if (attributeId == null) {
			attribute = new DealerAttribute();
			attribute.setDealer(formPanel.getDealer());
		} else {
			attribute = DEA_SRV.getById(DealerAttribute.class, attributeId);
		}
		attribute.setAssetMake(cbxAssetMake.getSelectedEntity());
		attribute.setAssetCategory(cbxAssetCategory.getSelectedEntity());
		attribute.setInsuranceCoverageDuration((Integer) cbxInsuranceCoverageDuration.getValue());
		attribute.setTiContractFeeAmount(MyNumberUtils.getDouble(txtContractFee.getValue(), 0d));
		attribute.setContractFeeChargePoint(cbxContractFeeChargePoint.getSelectedEntity());
		attribute.setTiCommission1Amount(MyNumberUtils.getDouble(txtCommission1Amount.getValue(), 0d));
		attribute.setCommission1ChargePoint(cbxCommission1ChargePoint.getSelectedEntity());
		attribute.setCommission2Enabled(cbCommission2Enabled.getValue());
		attribute.setLadderOption(cbxLadderOption.getSelectedEntity());
		attribute.setLadderType(cbxLadderType.getSelectedEntity());
		attribute.setInsuranceFeeChargePoint(cbxInsuranceFeeChargePoint.getSelectedEntity());
		attribute.setInsuranceCompany(cbxInsuranceCompanies.getSelectedEntity());
		attribute.setInsuranceFeeEnabled(cbChargeInsuranceFee.getValue());
		return attribute;
	}
	
	/**
	 * Validate data
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		ValidateUtil.clearErrors();
		ValidateUtil.checkMandatorySelectField(cbxAssetCategory, "asset.category");
		ValidateUtil.checkMandatorySelectField(cbxAssetMake, "asset.make");
		ValidateUtil.checkMandatoryField(txtContractFee, "contract.fee");
		ValidateUtil.checkDoubleField(txtContractFee, "contract.fee");
		ValidateUtil.checkMandatorySelectField(cbxContractFeeChargePoint, "contract.fee.charge.point");
		
		ValidateUtil.checkMandatoryField(txtCommission1Amount, "commission.1");
		ValidateUtil.checkDoubleField(txtCommission1Amount, "commission.1");
		ValidateUtil.checkMandatorySelectField(cbxCommission1ChargePoint, "commission.1.charge.point");
		
		if (cbCommission2Enabled.getValue()) {
			ValidateUtil.checkMandatorySelectField(cbxLadderOption, "ladder.option");
			if (ELadderOption.GROUP.equals(cbxLadderOption.getSelectedEntity())) {
				if (formPanel.getDealer().getDealerGroup() == null) {
					ValidateUtil.addError(I18N.message("field.required.1", I18N.message("dealer.dealer.group")));
				}
			} else if (ELadderOption.HEAD.equals(cbxLadderOption.getSelectedEntity())) {
				Dealer parent = formPanel.getDealer().getParent();
				if (parent.getDealerGroup() == null) {
					ValidateUtil.addError(I18N.message("field.required.1", I18N.message("main.dealer.dealer.group")));
				}
			} else if (ELadderOption.UNIQUE.equals(cbxLadderOption.getSelectedEntity())) {
				ValidateUtil.checkMandatorySelectField(cbxLadderType, "ladder.type");
			}
		}
		
		ValidateUtil.checkMandatorySelectField(cbxInsuranceCoverageDuration, "insurance.coverage.duration");
		ValidateUtil.checkMandatorySelectField(cbxInsuranceCompanies, "insurance.companies");
		ValidateUtil.checkMandatorySelectField(cbxInsuranceFeeChargePoint, "charge.insurance.fee");
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Reset form
	 */
	public void reset() {
		attributeId = null;
		cbxAssetMake.setSelectedEntity(null);
		cbxAssetCategory.setSelectedEntity(null);
		cbxInsuranceCoverageDuration.setValue("");
		txtContractFee.setValue("");
		cbxContractFeeChargePoint.setSelectedEntity(null);		
		txtCommission1Amount.setValue("");
		cbxCommission1ChargePoint.setSelectedEntity(null);
		cbCommission2Enabled.setValue(false);
		cbxLadderOption.setSelectedEntity(null);
		cbxLadderType.setSelectedEntity(null);
		messagePanel.setVisible(false);
		
		cbChargeInsuranceFee.setValue(true);
		cbxInsuranceFeeChargePoint.setSelectedEntity(null);
		cbxInsuranceCompanies.setSelectedEntity(null);
	}

}
