package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.callcenterresult;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CallCenterResultsFormPanel extends AbstractFormPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9133739933497636236L;
	
	private ECallCenterResult callCenterResult;
	
    private TextField txtCode;
    private TextField txtDescEn;
    private TextField txtDesc;
    
	@javax.annotation.PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * 
	 * @return formLayout
	 */
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
	public void assignValue(Long callCenterResultId) {
    	reset();
    	if (callCenterResultId != null) {
    		this.callCenterResult = ENTITY_SRV.getById(ECallCenterResult.class, callCenterResultId);
    		txtCode.setValue(callCenterResult.getCode());
        	txtDescEn.setValue(callCenterResult.getDescEn());
        	txtDesc.setValue(callCenterResult.getDesc());
    	}
    }
	
	/**
	 * Reset
	 */
	public void reset() {
		callCenterResult = new ECallCenterResult();
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
		callCenterResult.setCode(txtCode.getValue());
		callCenterResult.setDescEn(txtDescEn.getValue());
		callCenterResult.setDesc(txtDesc.getValue());
		
		return callCenterResult;
	}

}
