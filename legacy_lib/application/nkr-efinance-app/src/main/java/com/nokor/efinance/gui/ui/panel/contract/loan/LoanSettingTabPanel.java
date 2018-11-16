package com.nokor.efinance.gui.ui.panel.contract.loan;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class LoanSettingTabPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4525886213743681002L;
	
	private Button btnEdit;
	
	private ERefDataComboBox<EWkfStatus> cbxLoanStatus;
	private ComboBox cbxAssetStatus;
	private ComboBox cbxLesseeStatus;
	private ERefDataComboBox<EWkfStatus> cbxNBCStatus;
	private EntityComboBox<PenaltyRule> cbxPenalty;
	
	private TextField txtGracePeriod;
	private TextField txtPressingFee;
	private TextField txtFollowingFee;
	private TextField txtReposessionFee;
	private TextField txtClosingFee;
	private TextField txtRedemptionFee;
	private TextField txtTranferFee;
	
	private EntityComboBox<OrgStructure> cbxBranchInCharge;
	
	private Contract contract;
	
	public LoanSettingTabPanel() {
		setMargin(true);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 70, "btn btn-success button-small");
		btnEdit.addClickListener(this);
		
		cbxLoanStatus = new ERefDataComboBox<>(ContractWkfStatus.listContractStatus());
		cbxAssetStatus = new ComboBox();
		cbxLesseeStatus = new ComboBox();
		cbxPenalty = new EntityComboBox<>(PenaltyRule.class, "descEn");
		cbxPenalty.renderer();
		
		cbxNBCStatus = new ERefDataComboBox<>(ContractWkfStatus.listNBCStatus());
		cbxNBCStatus.setWidth(370, Unit.PIXELS);
		
		txtGracePeriod = ComponentFactory.getTextField(20, 100);
		txtPressingFee = ComponentFactory.getTextField(20, 100);
		txtFollowingFee = ComponentFactory.getTextField(20, 100);
		txtReposessionFee = ComponentFactory.getTextField(20, 100);
		txtClosingFee = ComponentFactory.getTextField(20, 100);
		txtRedemptionFee = ComponentFactory.getTextField(20, 100);
		txtTranferFee = ComponentFactory.getTextField(20, 100);
		
		cbxBranchInCharge = new EntityComboBox<>(OrgStructure.class, null, "nameEn", "");
		cbxBranchInCharge.setImmediate(true);
		cbxBranchInCharge.renderer();
		cbxBranchInCharge.setWidth(140, Unit.PIXELS);
		
		GridLayout settingGridLayout = ComponentLayoutFactory.getGridLayout(6, 6);
		settingGridLayout.setSpacing(true);
		settingGridLayout.setMargin(true);
		
		Label lblLoanStatus = ComponentLayoutFactory.getLabelCaption("loan.status");
		Label lblAssetStatus = ComponentLayoutFactory.getLabelCaption("asset.status");
		Label lblLesseeStatus = ComponentLayoutFactory.getLabelCaption("leassee.status");
		Label lblNBCStatus = ComponentLayoutFactory.getLabelCaption("nbc.status");
		Label lblPenalty = ComponentLayoutFactory.getLabelCaption("penalty");
		Label lblGracePeriod = ComponentLayoutFactory.getLabelCaption("grace.period");
		Label lblPressingFee = ComponentLayoutFactory.getLabelCaption("pressing.fee");
		Label lblFollowingFee = ComponentLayoutFactory.getLabelCaption("following.fee");
		Label lblReposessinFee = ComponentLayoutFactory.getLabelCaption("reposessing.fee");
		Label lblClosingFee = ComponentLayoutFactory.getLabelCaption("closing.fee");
		Label lblRedemptionFee = ComponentLayoutFactory.getLabelCaption("redemption.fee");
		Label lblTranferFee = ComponentLayoutFactory.getLabelCaption("tranfer.fee");
		Label lblBrandCharge = ComponentLayoutFactory.getLabelCaption("branch.in.charge");
		
		int iCol = 0;
		settingGridLayout.addComponent(lblLoanStatus, iCol++, 0);
		settingGridLayout.addComponent(cbxLoanStatus, iCol++, 0);
		settingGridLayout.addComponent(lblAssetStatus, iCol++, 0);
		settingGridLayout.addComponent(cbxAssetStatus, iCol++, 0);
		settingGridLayout.addComponent(lblLesseeStatus, iCol++, 0);
		settingGridLayout.addComponent(cbxLesseeStatus, iCol++, 0);
		
		iCol = 0;
		settingGridLayout.addComponent(lblNBCStatus, iCol++, 1);
		settingGridLayout.addComponent(cbxNBCStatus, iCol++, 1);
		settingGridLayout.addComponent(lblBrandCharge, iCol++, 1);
		settingGridLayout.addComponent(cbxBranchInCharge, iCol++, 1);
		
		iCol = 0;
		settingGridLayout.addComponent(lblPenalty, iCol++, 2);
		settingGridLayout.addComponent(cbxPenalty, iCol++, 2);
		settingGridLayout.addComponent(lblGracePeriod, iCol++, 2);
		settingGridLayout.addComponent(txtGracePeriod, iCol++, 2);
		
		iCol = 0;
		settingGridLayout.addComponent(lblPressingFee, iCol++, 3);
		settingGridLayout.addComponent(txtPressingFee, iCol++, 3);
		settingGridLayout.addComponent(lblFollowingFee, iCol++, 3);
		settingGridLayout.addComponent(txtFollowingFee, iCol++, 3);
		
		iCol = 0;
		settingGridLayout.addComponent(lblReposessinFee, iCol++, 4);
		settingGridLayout.addComponent(txtReposessionFee, iCol++, 4);
		settingGridLayout.addComponent(lblClosingFee, iCol++, 4);
		settingGridLayout.addComponent(txtClosingFee, iCol++, 4);
		
		iCol = 0;
		settingGridLayout.addComponent(lblRedemptionFee, iCol++, 5);
		settingGridLayout.addComponent(txtRedemptionFee, iCol++, 5);
		settingGridLayout.addComponent(lblTranferFee, iCol++, 5);
		settingGridLayout.addComponent(txtTranferFee, iCol++, 5);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("setting"));
		fieldSet.setContent(settingGridLayout);
		
		Panel formPanl = new Panel(fieldSet);
		formPanl.setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(btnEdit);
		mainLayout.addComponent(formPanl);
		mainLayout.setComponentAlignment(btnEdit, Alignment.TOP_RIGHT);
		
		setEnableToSetting(false);
		addComponent(mainLayout);
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setEnableToSetting(boolean isEnable) {
		cbxLoanStatus.setEnabled(isEnable);
		cbxAssetStatus.setEnabled(false);
		cbxLesseeStatus.setEnabled(false);
		cbxNBCStatus.setEnabled(isEnable);
		cbxPenalty.setEnabled(isEnable);
		txtGracePeriod.setEnabled(isEnable);
		txtPressingFee.setEnabled(isEnable);
		txtFollowingFee.setEnabled(isEnable);
		txtReposessionFee.setEnabled(isEnable);
		txtClosingFee.setEnabled(isEnable);
		txtRedemptionFee.setEnabled(isEnable);
		txtTranferFee.setEnabled(isEnable);
		cbxBranchInCharge.setEnabled(isEnable);
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		cbxNBCStatus.setSelectedEntity(null);
		cbxBranchInCharge.setSelectedEntity(null);
	}
	
	/**
	 * AssignValue
	 */
	public void assignValue(Contract contract) {
		this.contract = contract;
		reset();
		Collection collection = contract.getCollection();
		PenaltyRule penaltyRule = contract.getPenaltyRule();
		
		cbxLoanStatus.setSelectedEntity(contract.getWkfStatus());
		cbxNBCStatus.setSelectedEntity(contract.getWkfSubStatus());
		if (penaltyRule != null) {
			txtGracePeriod.setValue(getDefaultString(penaltyRule.getGracePeriod()));
		}
		txtFollowingFee.setValue(AmountUtils.format(collection == null ? 0d : MyNumberUtils.getDouble(collection.getTiFollowingFeeAmount())));
		txtReposessionFee.setValue(AmountUtils.format(collection == null ? 0d : MyNumberUtils.getDouble(collection.getTiBalanceRepossessionFee())));
		cbxBranchInCharge.setSelectedEntity(contract.getBranchInCharge());
		cbxPenalty.setSelectedEntity(penaltyRule);
	}
	
	private void save() {
		contract.setWkfSubStatus(cbxNBCStatus.getSelectedEntity());
		contract.setBranchInCharge(cbxBranchInCharge.getSelectedEntity());
		contract.setWkfStatus(cbxLoanStatus.getSelectedEntity());
		contract.setPenaltyRule(cbxPenalty.getSelectedEntity());
		try {
			ENTITY_SRV.saveOrUpdate(contract);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			setEnableToSetting(false);
			btnEdit.setCaption(I18N.message("edit"));
			btnEdit.setIcon(FontAwesome.EDIT);
		} catch (Exception e) {
			ComponentLayoutFactory.displayErrorMsg(e.getMessage());
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			if (I18N.message("edit").equals(btnEdit.getCaption())) {
				setEnableToSetting(true);
				btnEdit.setCaption(I18N.message("save"));
				btnEdit.setIcon(FontAwesome.SAVE);
			} else {
				save();
			}
		}
		
	}

}
