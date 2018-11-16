package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.financial.model.CampaignAssetModel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetSerieInfoPopupPanel extends Window implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930117592578106725L;
	
	private Label lblBrand;
	private Label lblModel;
	private Label lblCategory;
	
	private Label lblAssetId;
	private Label lblSerie;
	private Label lblYear;
	
	private Label lblCC;
	private Label lblSerieName;
	private Label lblCharacteristic;
	
	private Label lblManufacturingPrice;
	private Label lblStandardFinanceAmount;
	
	/**
	 * 
	 */
	public AssetSerieInfoPopupPanel() {
		setModal(true);
		setCaption(I18N.message("serie"));
		setWidth("600px");
		setHeight("165px");
		
		lblBrand = getLabelValue();
		lblModel = getLabelValue();
		lblCategory = getLabelValue();
		
		lblAssetId = getLabelValue();
		lblSerie = getLabelValue();
		lblYear = getLabelValue();
		
		lblCC = getLabelValue();
		lblSerieName = getLabelValue();
		lblCharacteristic = getLabelValue();
		
		lblManufacturingPrice = getLabelValue();
		lblStandardFinanceAmount = getLabelValue();
		
		GridLayout gridLayout = new GridLayout(12, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("brand")), iCol++, 0);
		gridLayout.addComponent(new Label(":"), iCol++, 0);
		gridLayout.addComponent(lblBrand, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("model")), iCol++, 0);
		gridLayout.addComponent(new Label(":"), iCol++, 0);
		gridLayout.addComponent(lblModel, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("category")), iCol++, 0);
		gridLayout.addComponent(new Label(":"), iCol++, 0);
		gridLayout.addComponent(lblCategory, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("asset.id")), iCol++, 1);
		gridLayout.addComponent(new Label(":"), iCol++, 1);
		gridLayout.addComponent(lblAssetId, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("serie")), iCol++, 1);
		gridLayout.addComponent(new Label(":"), iCol++, 1);
		gridLayout.addComponent(lblSerie, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("year")), iCol++, 1);
		gridLayout.addComponent(new Label(":"), iCol++, 1);
		gridLayout.addComponent(lblYear, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("cc")), iCol++, 2);
		gridLayout.addComponent(new Label(":"), iCol++, 2);
		gridLayout.addComponent(lblCC, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(new Label(I18N.message("serie.name")), iCol++, 2);
		gridLayout.addComponent(new Label(":"), iCol++, 2);
		gridLayout.addComponent(lblSerieName, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(new Label(I18N.message("characteristic")), iCol++, 2);
		gridLayout.addComponent(new Label(":"), iCol++, 2);
		gridLayout.addComponent(lblCharacteristic, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("manufacturing.price")), iCol++, 3);
		gridLayout.addComponent(new Label(":"), iCol++, 3);
		gridLayout.addComponent(lblManufacturingPrice, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(new Label(I18N.message("standard.finance.amount")), iCol++, 3);
		gridLayout.addComponent(new Label(":"), iCol++, 3);
		gridLayout.addComponent(lblStandardFinanceAmount, iCol++, 3);
		
		setContent(gridLayout);
	}
	
	/**
	 * Assign Value
	 * @param name
	 */
	public void assignValue(String name) {
		AssetModel assetModel = getSerieInfoByName(name);
		if (assetModel != null) {
			AssetRange assetRange = assetModel.getAssetRange();
			lblAssetId.setValue(getDescription(assetModel.getCode()));
			lblSerie.setValue(getDescription(assetModel.getSerie()));
			lblYear.setValue(getDescription(assetModel.getYear() != null ? assetModel.getYear().toString() : ""));
			lblCC.setValue(getDescription(assetModel.getEngine() != null ? assetModel.getEngine().getDescLocale() : null));
			lblCharacteristic.setValue(getDescription(assetModel.getCharacteristic()));
			lblSerieName.setValue(getDescription(assetModel.getDescLocale()));
			lblCategory.setValue(getDescription(assetModel.getAssetCategory() != null ? assetModel.getAssetCategory().getDescLocale() : ""));
			lblManufacturingPrice.setValue(getDescription(AmountUtils.format(assetModel.getTiPrice())));
//			lblStandardFinanceAmount.setValue(AmountUtils.format(assetModel.getStandardFinanceAmount()));
			if (assetRange != null) {
				AssetMake assetMake = assetRange.getAssetMake();
				lblModel.setValue(getDescription(assetModel.getDescLocale()));
				if (assetMake != null) {
					lblBrand.setValue(getDescription(assetMake.getDescLocale()));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private AssetModel getSerieInfoByName(String name) {
		BaseRestrictions<AssetModel> restrictions = new BaseRestrictions<>(AssetModel.class);
		restrictions.addCriterion(Restrictions.eq("descEn", name));
		List<AssetModel> assetModels = ENTITY_SRV.list(restrictions);
		if (!assetModels.isEmpty()) {
			return assetModels.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value != null ? value : "");
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	

}
