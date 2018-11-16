package com.nokor.ersys.vaadin.ui.referential.reftable.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * RefTable Form Panel.
 * 
 * @author pengleng.huot
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RefTableFormPanel extends AbstractFormPanel implements AppServicesHelper {

	/**	 */
	private static final long serialVersionUID = 6328243908103681681L;
	
	private RefTable refTable;	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private TextField txtShortName;
	private CheckBox cbUseSortIndex;
	private CheckBox cbReadOnly;
    private CheckBox cbActive;
    private CheckBox cbVisible;
    private CheckBox cbGenerateCode;
    private CheckBox cbFetchValuesFromDB;
    private CheckBox cbFetchI18NFromDB;
    private CheckBox cbCached;
	private VerticalLayout navigationPanel;
	private NavigationPanel defaultNavigationPanel;	 
	
	
	@PostConstruct
   	public void PostConstruct() {
        super.init();        
        setSizeFull();        
   		navigationPanel = new VerticalLayout();
        addComponent(navigationPanel, 0);        
        defaultNavigationPanel = new NavigationPanel();        
        defaultNavigationPanel.addSaveClickListener(this);
        navigationPanel.addComponent(defaultNavigationPanel);        
   	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {		
		final FormLayout formPanel = new FormLayout();			
		txtCode = ComponentFactory.getTextField("code", true, 50, 150);
		txtCode.setEnabled(false);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		
		txtShortName = ComponentFactory.getTextField("short.name", false, 100, 200);
		txtShortName.setEnabled(false);
		
		cbUseSortIndex = new CheckBox(I18N.message("use.sort.index"));
		cbReadOnly = new CheckBox(I18N.message("read.only"));
		cbActive = new CheckBox(I18N.message("active"));
	    cbVisible = new CheckBox(I18N.message("visible"));
	    cbGenerateCode = new CheckBox(I18N.message("generate.code"));
	    cbFetchValuesFromDB = new CheckBox(I18N.message("fetch.values.from.db"));
	    cbFetchI18NFromDB = new CheckBox(I18N.message("fetch.i18n.from.db"));
	    cbCached = new CheckBox(I18N.message("cached"));
	    
		cbUseSortIndex.setValue(true);
		cbReadOnly.setValue(true);
	    cbActive.setValue(true);
	    cbVisible.setValue(true);
	    cbGenerateCode.setValue(true);
	    cbFetchValuesFromDB.setValue(true);
	    cbFetchI18NFromDB.setValue(true);
	    cbCached.setValue(true);
	    
	    cbReadOnly.setEnabled(false);
	    cbActive.setEnabled(false);
	    cbVisible.setEnabled(false);
	    cbGenerateCode.setEnabled(false);
	    cbFetchValuesFromDB.setEnabled(false);
	    cbFetchI18NFromDB.setEnabled(false);
	    cbCached.setEnabled(false);
	    
		formPanel.addComponent(txtCode);		
		formPanel.addComponent(txtDesc);
		formPanel.addComponent(txtDescEn);	
		formPanel.addComponent(txtShortName);
		formPanel.addComponent(cbUseSortIndex);
		formPanel.addComponent(cbReadOnly);
		formPanel.addComponent(cbActive);
		formPanel.addComponent(cbVisible);
		formPanel.addComponent(cbGenerateCode);
		formPanel.addComponent(cbFetchValuesFromDB);
		formPanel.addComponent(cbFetchI18NFromDB);
		formPanel.addComponent(cbCached);
		
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(formPanel);
        
        Panel mainPanel = ComponentFactory.getPanel();        
		mainPanel.setContent(verticalLayout);        
		return mainPanel;
	}
	
	/**
   	 * @param asmakId
   	 */
   	public void assignValues(Long id) {
   		super.reset();
		if (id != null) {
			refTable = ENTITY_SRV.getById(RefTable.class, id);
			txtDesc.setValue(refTable.getDesc());
   			txtDescEn.setValue(refTable.getDesc());
   			txtShortName.setValue(refTable.getShortName() != null ? refTable.getShortName() : "");
   			cbUseSortIndex.setValue(refTable.getUseSortIndex());
   			cbReadOnly.setValue(refTable.getReadOnly());
   			cbActive.setValue(refTable.getStatusRecord().equals(EStatusRecord.ACTIV));
   			cbVisible.setValue(refTable.isVisible());
   			cbGenerateCode.setValue(refTable.getGenerateCode());
   			cbFetchValuesFromDB.setValue(refTable.getFetchValuesFromDB());
   			cbFetchI18NFromDB.setValue(refTable.getFetchI18NFromDB());
   			cbCached.setValue(refTable.isCached());
		}
   	}

	@Override
	protected Entity getEntity() {
		refTable.setDesc(txtDesc.getValue());
		refTable.setDescEn(txtDescEn.getValue());
		refTable.setUseSortIndex(cbUseSortIndex.getValue());
   		return refTable;
	}

	/**
   	 * Reset
   	 */
   	@Override
   	public void reset() {
   		super.reset();
   		refTable = new RefTable();   		
   		txtDesc.setValue("");   		
   		txtDescEn.setValue("");
   		txtShortName.setValue("");
   		cbUseSortIndex.setValue(true);
   		cbReadOnly.setValue(true);
   		cbActive.setValue(true);
   		cbVisible.setValue(true);
   		cbGenerateCode.setValue(true);
   		cbFetchValuesFromDB.setValue(true);
   		cbFetchI18NFromDB.setValue(true);
   		cbCached.setValue(true);
   		markAsDirty();		
   	}
	/**
   	 * @return
   	 */
   	@Override
   	protected boolean validate() {   		
   		checkMandatoryField(txtCode, "code");
   		checkMandatoryField(txtDesc, "desc");		
   		checkMandatoryField(txtDescEn, "desc.en");   	
   		return errors.isEmpty();
   	}
}
