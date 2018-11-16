package com.nokor.efinance.ra.ui.panel.collections.collectionconfig;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.collection.model.CollectionConfig;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollectionConfigFormPanel extends AbstractFormPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4491595819568891346L;
	
	private CollectionConfig collectionConfig;
	private ERefDataComboBox<EColType> cbxColType;
	private EntityRefComboBox<FinService> cbxCollectionFee;
	private EntityRefComboBox<FinService> cbxReposessionFee;
	private TextField txtExtendInDay;
    private CheckBox cbActive;
    
    @PostConstruct
  	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("collection.config"));
        NavigationPanel navigationPanel = addNavigationPanel();
  		navigationPanel.addSaveClickListener(this);
  	}

	@Override
	protected Component createForm() {
		cbxColType = new ERefDataComboBox<>(I18N.message("collection.type"), EColType.values());
		cbxColType.setRequired(true);
		
		cbxCollectionFee = new EntityRefComboBox<>();
		cbxCollectionFee.setCaption(I18N.message("collection.fee"));
		cbxCollectionFee.setImmediate(true);
		cbxCollectionFee.setRestrictions(getFinserviceCollectionFee());
		cbxCollectionFee.renderer();
		
		cbxReposessionFee = new EntityRefComboBox<>();
		cbxReposessionFee.setCaption(I18N.message("reposession.fee"));
		cbxReposessionFee.setImmediate(true);
		cbxReposessionFee.setRestrictions(getFinserviceReposessionFee());
		cbxReposessionFee.renderer();
		
		txtExtendInDay = ComponentFactory.getTextField("extend.in.day", false, 20, 120);
		
		cbActive = new CheckBox(I18N.message("active"));
		cbActive.setValue(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxColType);
		formLayout.addComponent(cbxCollectionFee);
		formLayout.addComponent(cbxReposessionFee);
		formLayout.addComponent(txtExtendInDay);
		formLayout.addComponent(cbActive);
		
		return formLayout;
	}

	@Override
	protected Entity getEntity() {
		collectionConfig.setColType(cbxColType.getSelectedEntity());
		collectionConfig.setCollectionFee(cbxCollectionFee.getSelectedEntity());
		collectionConfig.setReposessionFee(cbxReposessionFee.getSelectedEntity());
		collectionConfig.setExtendInDay(getInteger(txtExtendInDay));
		collectionConfig.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return collectionConfig;
	}
	
	/**
	 * AssignValues
	 * @param minReturnRateId
	 */
	public void assignValues(Long colFeeId) {
		super.reset();
		if (colFeeId != null) {
			collectionConfig = ENTITY_SRV.getById(CollectionConfig.class, colFeeId);
			cbxColType.setSelectedEntity(collectionConfig.getColType());
			cbxCollectionFee.setSelectedEntity(collectionConfig.getCollectionFee());
			cbxReposessionFee.setSelectedEntity(collectionConfig.getReposessionFee());
			txtExtendInDay.setValue(getDefaultString(collectionConfig.getExtendInDay()));
			cbActive.setValue(collectionConfig.getStatusRecord().equals(EStatusRecord.ACTIV) );
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		collectionConfig = new CollectionConfig();
		cbxColType.setSelectedEntity(null);
		cbxCollectionFee.setSelectedEntity(null);
		cbxReposessionFee.setSelectedEntity(null);
		txtExtendInDay.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * Validate
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxColType, "collection.type");
		checkIntegerField(txtExtendInDay, "extend.in.day");
		return errors.isEmpty();
	}
	
	
	/**
	 * get service that servicetype = collection fee
	 * @return
	 */
	private BaseRestrictions<FinService> getFinserviceCollectionFee() {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		//restrictions.addCriterion(Restrictions.or(Restrictions.eq("serviceType", EServiceType.COLFEE), Restrictions.eq("serviceType", EServiceType.REPOSFEE)));
		restrictions.addCriterion(Restrictions.eq("serviceType", EServiceType.COLFEE));
		return restrictions;
	}
	
	/**
	 * get service that servicetype = reposession fee
	 * @return
	 */
	private BaseRestrictions<FinService> getFinserviceReposessionFee() {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		restrictions.addCriterion(Restrictions.eq("serviceType", EServiceType.REPOSFEE));
		return restrictions;
	}

}
