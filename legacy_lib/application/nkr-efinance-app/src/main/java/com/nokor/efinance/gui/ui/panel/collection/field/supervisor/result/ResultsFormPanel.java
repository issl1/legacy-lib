package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.result;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
/**
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ResultsFormPanel extends AbstractFormPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9133739933497636236L;
	
	private EColResult colResult;
    private TextField txtCode;
    private TextField txtDescEn;
    private TextField txtDesc;
    
	@javax.annotation.PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected Component createForm() {
		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		
		return formLayout;
	}
	
	/**
	 * AssignValue
	 * @param colResultId
	 */
	public void assignValue(Long colResultId) {
    	reset();
    	if (colResultId != null) {
    		this.colResult = ENTITY_SRV.getById(EColResult.class, colResultId);
    		txtCode.setValue(colResult.getCode());
        	txtDescEn.setValue(colResult.getDescEn());
        	txtDesc.setValue(colResult.getDesc());
    	}
    }
	
	/**
	 * Reset
	 */
	public void reset() {
		colResult = new EColResult();
    	txtCode.setValue("");
    	txtDescEn.setValue("");
    	txtDesc.setValue("");
	}
	
	/**
	 * Validate
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
	
	/**
	 * save
	 */
	@Override
	protected Entity getEntity() {
		colResult.setCode(txtCode.getValue());
		colResult.setDescEn(txtDescEn.getValue());
		colResult.setDesc(txtDesc.getValue());
		
		if (ProfileUtil.isColPhoneSupervisor()) {
			colResult.setColTypes(EColType.PHONE);
		} else if (ProfileUtil.isColFieldSupervisor()) {
			colResult.setColTypes(EColType.FIELD);
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			colResult.setColTypes(EColType.INSIDE_REPO);
		}
		
		return colResult;
	}

}
