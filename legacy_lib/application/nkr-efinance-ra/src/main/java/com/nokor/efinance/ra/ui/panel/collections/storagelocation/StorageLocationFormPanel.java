package com.nokor.efinance.ra.ui.panel.collections.storagelocation;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.StorageLocation;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Groups form panel
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StorageLocationFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = -3865152280745041135L;
	
	private TextField txtDesc;
	private TextField txtDescEn;
	
	private StorageLocation storageLocation;
	
	/** */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);		
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);		
		return formLayout;
	}
	
	/**
	 * Assign District groups value
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			storageLocation = ENTITY_SRV.getById(StorageLocation.class, id);
			txtDescEn.setValue(storageLocation.getDescEn());
			txtDesc.setValue(storageLocation.getDesc());
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		storageLocation.setDescEn(txtDescEn.getValue());
		storageLocation.setDesc(txtDesc.getValue());
		return storageLocation;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		storageLocation = new StorageLocation();
		storageLocation.setStatusRecord(EStatusRecord.ACTIV);
		txtDesc.setValue("");
		txtDescEn.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}

}
