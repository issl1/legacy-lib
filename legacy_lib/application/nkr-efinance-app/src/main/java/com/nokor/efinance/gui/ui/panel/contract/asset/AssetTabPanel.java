package com.nokor.efinance.gui.ui.panel.contract.asset;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetTabPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288095927524637328L;
	
	private TabSheet tabSheet;
	
	private AssetPanel assetPanel;
	//private AssetForeclosureTabPanel foreclosureTabPanel;
	private AssetStockTabPanel stockTabPanel;
	private AssetConditionsTabPanel conditionsTabPanel;
	private AssetAuctionTabPanel auctionTabPanel;
	
	private Button btnReturnRepo;
	
	private Label lblAssetIDValue;
	private Label lblAssetDescValue;
	private Label lblAssetStatusValue;
	
	private Label lblCCValue;
	private Label lblColorValue;
	private Label lblYearValue;
	private Label lblCategoryValue;
	
	private Label lblEnginNoValue;
	private Label lblChassisValue;
	private Label lblMileageValue;
	private Label lblCharacteristicsValue;
	
	private Label lblTypeValue;
	private Label lblDateValue;
	private Label lblLocationValue;
	private Label lblByValue;
	private Label lblStatusValue;
	
	public AssetTabPanel() {
		setSpacing(true);
		
		btnReturnRepo = ComponentLayoutFactory.getDefaultButton("return.repo", null, 100);
		btnReturnRepo.addClickListener(this);
		
		lblAssetIDValue = getLabelValue();
		lblAssetDescValue = getLabelValue();
		lblAssetStatusValue = getLabelValue();
		lblCCValue = getLabelValue();
		lblColorValue = getLabelValue();
		lblYearValue = getLabelValue();
		lblCategoryValue = getLabelValue();
		lblEnginNoValue = getLabelValue();
		lblChassisValue = getLabelValue();
		lblMileageValue = getLabelValue();
		lblCharacteristicsValue = getLabelValue();
		
		lblTypeValue = getLabelValue();
		lblDateValue = getLabelValue();
		lblLocationValue = getLabelValue();
		lblByValue = getLabelValue();
		lblStatusValue = getLabelValue();
		
		
		tabSheet = new TabSheet();
		
		assetPanel = new AssetPanel();
		stockTabPanel = new AssetStockTabPanel();
		conditionsTabPanel = new AssetConditionsTabPanel();
		auctionTabPanel	 = new AssetAuctionTabPanel();
		
		tabSheet.addTab(assetPanel, I18N.message("info"));
		tabSheet.addTab(stockTabPanel, I18N.message("stock"));
		tabSheet.addTab(conditionsTabPanel, I18N.message("conditions"));
		tabSheet.addTab(auctionTabPanel, I18N.message("auction"));
		
		addComponent(createDetailPanel());
		addComponent(createReturnRepoPanel());
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createDetailPanel() {
		GridLayout detailGridLayout = new GridLayout(8, 3);
		detailGridLayout.setMargin(true);
		detailGridLayout.setSpacing(true);
		
		Label lblAssetID = getLabel("asset.id");
		Label lblAssetDesc = getLabel("asset.desc");
		Label lblAssetStatus = getLabel("asset.status");
		Label lblCC = getLabel("cc");
		Label lblColor = getLabel("color");
		Label lblYear = getLabel("year");
		Label lblCategory = getLabel("category");
		Label lblEnginNo = getLabel("engine.no");
		Label lblChassis = getLabel("chassis");
		Label lblMilage = getLabel("mileage");
		Label lblCharacteristics = getLabel("characteristics");
		
		int iCol = 0;
		detailGridLayout.addComponent(lblAssetID, iCol++, 0);
		detailGridLayout.addComponent(lblAssetIDValue, iCol++, 0);
		detailGridLayout.addComponent(lblAssetDesc, iCol++, 0);
		detailGridLayout.addComponent(lblAssetDescValue, iCol++, 0);
		detailGridLayout.addComponent(lblAssetStatus, iCol++, 0);
		detailGridLayout.addComponent(lblAssetStatusValue, iCol++, 0);
		
		iCol = 0;
		detailGridLayout.addComponent(lblCC, iCol++, 1);
		detailGridLayout.addComponent(lblCCValue, iCol++, 1);
		detailGridLayout.addComponent(lblColor, iCol++, 1);
		detailGridLayout.addComponent(lblColorValue, iCol++, 1);
		detailGridLayout.addComponent(lblYear, iCol++, 1);
		detailGridLayout.addComponent(lblYearValue, iCol++, 1);
		detailGridLayout.addComponent(lblCategory, iCol++, 1);
		detailGridLayout.addComponent(lblCategoryValue, iCol++, 1);
		
		iCol = 0;
		detailGridLayout.addComponent(lblEnginNo, iCol++, 2);
		detailGridLayout.addComponent(lblEnginNoValue, iCol++, 2);
		detailGridLayout.addComponent(lblChassis, iCol++, 2);
		detailGridLayout.addComponent(lblChassisValue, iCol++, 2);
		detailGridLayout.addComponent(lblMilage, iCol++, 2);
		detailGridLayout.addComponent(lblMileageValue, iCol++, 2);
		detailGridLayout.addComponent(lblCharacteristics, iCol++, 2);
		detailGridLayout.addComponent(lblCharacteristicsValue, iCol++, 2);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("detail"));
		fieldSet.setContent(detailGridLayout);
		
		Panel detailPanel = new Panel(fieldSet);
		detailPanel.setStyleName(Reindeer.PANEL_LIGHT);
		return new VerticalLayout(detailPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createReturnRepoPanel() {
		GridLayout returnRepoGridLayout = ComponentLayoutFactory.getGridLayout(6, 2);
		returnRepoGridLayout.setMargin(true);
		returnRepoGridLayout.setSpacing(true);
		
		Label lblType = getLabel("type");
		Label lblDate = getLabel("date");
		Label lblLocation = getLabel("location");
		Label lblBy = getLabel("by");
		Label lblStatus = getLabel("status");
		
		int iCol = 0;
		returnRepoGridLayout.addComponent(lblType, iCol++, 0);
		returnRepoGridLayout.addComponent(lblTypeValue, iCol++, 0);
		returnRepoGridLayout.addComponent(lblDate, iCol++, 0);
		returnRepoGridLayout.addComponent(lblDateValue, iCol++, 0);
		returnRepoGridLayout.addComponent(lblLocation, iCol++, 0);
		returnRepoGridLayout.addComponent(lblLocationValue, iCol++, 0);
		
		iCol = 0;
		returnRepoGridLayout.addComponent(lblBy, iCol++, 1);
		returnRepoGridLayout.addComponent(lblByValue, iCol++, 1);
		returnRepoGridLayout.addComponent(lblStatus, iCol++, 1);
		returnRepoGridLayout.addComponent(lblStatusValue, iCol++, 1);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("return.repo"));
		fieldSet.setContent(returnRepoGridLayout);
		
		Panel returnRepoPanel = new Panel(fieldSet);
		returnRepoPanel.setStyleName(Reindeer.PANEL_LIGHT);
		return new VerticalLayout(returnRepoPanel);
	}

	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	
	/**
	 * AssignValues
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		assetPanel.assignValues(contract);
		stockTabPanel.assignValues(contract);
		String assetStatus = ASS_SRV.getAssetStatus(contract);
		
		Asset asset = contract.getAsset();
		if (asset != null) {
			AssetModel assetModel = asset.getModel();
			lblAssetIDValue.setValue(getDescription(asset.getCode()));
			lblAssetDescValue.setValue(getDescription(asset.getDescEn()));
			lblAssetStatusValue.setValue(getDescription(assetStatus));
			lblYearValue.setValue(getDescription(getDefaultString(asset.getYear())));
			lblColorValue.setValue(getDescription(asset.getColor() == null ? StringUtils.EMPTY : asset.getColor().getDescLocale()));
			lblMileageValue.setValue(getDescription(getDefaultString(asset.getMileage())));
			lblEnginNoValue.setValue(getDescription(asset.getEngineNumber()));
			lblChassisValue.setValue(getDescription(asset.getChassisNumber()));
			if (assetModel != null) {
				lblCCValue.setValue(getDescription(assetModel.getEngine() != null ? assetModel.getEngine().getDescLocale() : null));
				lblCategoryValue.setValue(getDescription(assetModel.getAssetCategory() != null ? assetModel.getAssetCategory().getDescEn() : ""));
				lblCharacteristicsValue.setValue(getDescription(assetModel.getCharacteristic()));
			}
		}
		assignValueReturnRepo(contract);
	}
	
	/**
	 * assignvalue to return repo
	 */
	private void assignValueReturnRepo(Contract contract) {
		ContractFlag contractFlag = getReturnRepo(contract);
		if (contractFlag != null) {
			lblTypeValue.setValue(getDescription(contractFlag.getFlag() == null ? StringUtils.EMPTY : contractFlag.getFlag().getDescLocale()));
			lblDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getDate())));
			lblLocationValue.setValue(getDescription(contractFlag.getLocantion()));
			lblByValue.setValue(getDescription(contractFlag.getUpdateUser()));
			lblStatusValue.setValue(getDescription(contractFlag.isCompleted() ? I18N.message("complete") : I18N.message("incomplete")));
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private ContractFlag getReturnRepo(Contract contract) {
		BaseRestrictions<ContractFlag> restrictions = new BaseRestrictions<>(ContractFlag.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("flag", EFlag.RETURN), Restrictions.eq("flag", EFlag.REPO)));
		restrictions.addOrder(Order.desc("createDate"));
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (!contractFlags.isEmpty()) {
			return contractFlags.get(0);
		}
		return null;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReturnRepo) {
			
		}
	}

}
