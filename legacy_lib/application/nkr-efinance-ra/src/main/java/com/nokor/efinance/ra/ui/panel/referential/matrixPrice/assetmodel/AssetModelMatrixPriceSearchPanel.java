package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.assetmodel;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class AssetModelMatrixPriceSearchPanel extends AbstractSearchPanel<AssetMatrixPrice> implements FMEntityField {

	private static final long serialVersionUID = -4478589367053917211L;
	
	private DealerComboBox cbxDealer;
	private EntityRefComboBox<AssetModel> cbxAssetModel;
	private ERefDataComboBox<EColor> cbxColor;
	private TextField txtAssetYear;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public AssetModelMatrixPriceSearchPanel(AssetModelMatrixPriceTablePanel matrixPriceTablePanel) {
		super(I18N.message("search"), matrixPriceTablePanel);
	}
	
	@Override
	protected void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxAssetModel.setSelectedEntity(null);
		cbxColor.setSelectedEntity(null);
		txtAssetYear.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}


	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(8, 2);
		
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField(); 
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		
		cbxAssetModel = new EntityRefComboBox<AssetModel>();
		cbxAssetModel.setRestrictions(new BaseRestrictions<AssetModel>(AssetModel.class));
		cbxAssetModel.renderer();
		
		cbxColor = new ERefDataComboBox<EColor>(EColor.class);
		txtAssetYear = ComponentFactory.getTextField(false, 4, 50);		
		
        gridLayout.setSpacing(true);
        
        int iCol = 0;
		
		gridLayout.addComponent(new Label(I18N.message("asset.model")), iCol++, 0);
		gridLayout.addComponent(cbxAssetModel, iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
		gridLayout.addComponent(cbxDealer, iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("startdate")), iCol++, 0);
		gridLayout.addComponent(dfStartDate, iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("enddate")), iCol++, 0);
		gridLayout.addComponent(dfEndDate, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("year")), iCol++, 1);
		gridLayout.addComponent(txtAssetYear, iCol++, 1);
		
		gridLayout.addComponent(new Label(I18N.message("color")), iCol++, 1);
		gridLayout.addComponent(cbxColor, iCol++, 1);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<AssetMatrixPrice> getRestrictions() {
		
		BaseRestrictions<AssetMatrixPrice> restrictions = new BaseRestrictions<AssetMatrixPrice>(AssetMatrixPrice.class);
		if (cbxAssetModel.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(ASSET_MODEL + "." + ID, cbxAssetModel.getSelectedEntity().getId()));
		}
		
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (cbxColor.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(COLOR + "." + ID, cbxColor.getSelectedEntity().getId()));
		}
		
		if (StringUtils.isNotEmpty(txtAssetYear.getValue())) {
			restrictions.addCriterion(Restrictions.eq(YEAR, txtAssetYear.getValue()));
		}
		
		restrictions.addCriterion(Restrictions.isNull(SERVICE));
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		
		restrictions.addOrder(Order.desc(DATE));
		return restrictions;		
		
	}

}
