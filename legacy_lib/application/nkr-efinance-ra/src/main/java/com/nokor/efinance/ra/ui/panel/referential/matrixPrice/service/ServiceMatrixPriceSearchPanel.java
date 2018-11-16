package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.service;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class ServiceMatrixPriceSearchPanel extends AbstractSearchPanel<AssetMatrixPrice> implements FMEntityField {
	
	private static final long serialVersionUID = 7931833274735253020L;
	
	private EntityRefComboBox<AssetModel> cbxAssetModel;
	private EntityRefComboBox<FinService> cbxService;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public ServiceMatrixPriceSearchPanel(ServiceMatrixPriceTablePanel matrixPriceTablePanel) {
		super(I18N.message("search"), matrixPriceTablePanel);
	}
	
	@Override
	protected void reset() {
		cbxAssetModel.setSelectedEntity(null);
		cbxService.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}


	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(8, 1);
		
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField(); 
		
		cbxAssetModel = new EntityRefComboBox<AssetModel>();
		cbxAssetModel.setRestrictions(new BaseRestrictions<AssetModel>(AssetModel.class));
		cbxAssetModel.renderer();
		
		cbxService = new EntityRefComboBox<FinService>();
		cbxService.setRestrictions(new BaseRestrictions<FinService>(FinService.class));
		cbxService.renderer();
		
        gridLayout.setSpacing(true);
        
        int iCol = 0;
		
		gridLayout.addComponent(new Label(I18N.message("asset.model")), iCol++, 0);
		gridLayout.addComponent(cbxAssetModel, iCol++, 0);
				
		gridLayout.addComponent(new Label(I18N.message("service")), iCol++, 0);
		gridLayout.addComponent(cbxService, iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("startdate")), iCol++, 0);
		gridLayout.addComponent(dfStartDate, iCol++, 0);
		
		gridLayout.addComponent(new Label(I18N.message("enddate")), iCol++, 0);
		gridLayout.addComponent(dfEndDate, iCol++, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<AssetMatrixPrice> getRestrictions() {
		
		BaseRestrictions<AssetMatrixPrice> restrictions = new BaseRestrictions<AssetMatrixPrice>(AssetMatrixPrice.class);
		if (cbxAssetModel.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(ASSET_MODEL + "." + ID, cbxAssetModel.getSelectedEntity().getId()));
		}
		
		restrictions.addCriterion(Restrictions.isNull(DEALER));
		
		if (cbxService.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(SERVICE + "." + ID, cbxService.getSelectedEntity().getId()));
		}
		
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
