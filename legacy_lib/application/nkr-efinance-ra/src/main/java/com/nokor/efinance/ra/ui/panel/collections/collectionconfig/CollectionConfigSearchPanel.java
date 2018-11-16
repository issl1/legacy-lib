package com.nokor.efinance.ra.ui.panel.collections.collectionconfig;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.collection.model.CollectionConfig;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionConfigSearchPanel extends AbstractSearchPanel<CollectionConfig> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = -3010417083411566019L;
	
	private ERefDataComboBox<EColType> cbxColType;
	private EntityRefComboBox<FinService> cbxService;
	private StatusRecordField statusRecordField;

	public CollectionConfigSearchPanel(CollectionConfigTablePanel collectionFeeTablePanel) {
		super(I18N.message("search"), collectionFeeTablePanel);
	}

	@Override
	protected Component createForm() {
		cbxColType = new ERefDataComboBox<>(I18N.message("collection.type"), EColType.values());
		cbxService = new EntityRefComboBox<>();
		cbxService.setCaption(I18N.message("service"));
		cbxService.setImmediate(true);
		cbxService.setRestrictions(getFinserviceCollectionFee());
		cbxService.renderer();
		
		statusRecordField = new StatusRecordField();
		
		GridLayout gridLayout = new GridLayout(3, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(new FormLayout(cbxColType), 0, 0);
		gridLayout.addComponent(new FormLayout(cbxService), 1, 0);
		gridLayout.addComponent(new FormLayout(statusRecordField), 2, 0);
		return gridLayout;
	}

	@Override
	public BaseRestrictions<CollectionConfig> getRestrictions() {
		BaseRestrictions<CollectionConfig> restrictions = new BaseRestrictions<>(CollectionConfig.class);
		if (cbxColType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq("colType", cbxColType.getSelectedEntity()));
		} 
		
		if (cbxService.getSelectedEntity() != null) {
			//restrictions.addCriterion(Restrictions.eq("service", cbxService.getSelectedEntity()));
		}
		
		if (statusRecordField.isInactiveAllValues()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (statusRecordField.getActiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		return restrictions;
	}

	@Override
	protected void reset() {
		cbxColType.setSelectedEntity(null);
		cbxService.setSelectedEntity(null);
		statusRecordField.clearValues();
	}
	
	/**
	 * get service that servicetype = collection fee
	 * @return
	 */
	private BaseRestrictions<FinService> getFinserviceCollectionFee() {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		restrictions.addCriterion(Restrictions.or(Restrictions.eq("serviceType", EServiceType.COLFEE), Restrictions.eq("serviceType", EServiceType.REPOSFEE)));
		return restrictions;
	}
}
