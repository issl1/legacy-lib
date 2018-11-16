package com.nokor.efinance.ra.ui.panel.referential.supportdecision;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Relationship form panel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SupportDecisionFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = -2524401515664448402L;

	private SupportDecision supportdecision;
	private CheckBox cbMandatory;
	private CheckBox cbActive;
	private CheckBox cbCommentRequired;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<EWkfStatus> cbxQuotationState;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		supportdecision.setCode(txtCode.getValue());
		supportdecision.setDesc(txtDesc.getValue());
		supportdecision.setDescEn(txtDescEn.getValue());
		supportdecision.setWkfStatus(cbxQuotationState.getSelectedEntity());
		supportdecision.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		supportdecision.setMandatory(cbMandatory.getValue() ? true : false);
		supportdecision.setCommentRequired(cbCommentRequired.getValue() ? true : false);
		return supportdecision;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);
        cbxQuotationState = new ERefDataComboBox<EWkfStatus>(I18N.message("quotation.status"), QuotationWkfStatus.values());
        cbxQuotationState.setRequired(true);
        cbxQuotationState.setSelectedEntity(QuotationWkfStatus.QUO);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);        
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);	
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
    	cbMandatory = new CheckBox(I18N.message("mandatory"));
    	cbMandatory.setValue(false);
    	cbCommentRequired = new CheckBox(I18N.message("comment.required"));
    	cbCommentRequired.setValue(false);
        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbxQuotationState);
        formPanel.addComponent(cbMandatory);
        formPanel.addComponent(cbCommentRequired);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			supportdecision = ENTITY_SRV.getById(SupportDecision.class, id);
			txtCode.setValue(supportdecision.getCode());
			txtDescEn.setValue(supportdecision.getDescEn());
			txtDesc.setValue(supportdecision.getDesc());
			cbxQuotationState.setSelectedEntity(supportdecision.getWkfStatus());
			cbActive.setValue(supportdecision.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbMandatory.setValue(supportdecision.isMandatory());
			cbCommentRequired.setValue(supportdecision.isCommentRequired());
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		supportdecision = EntityFactory.createInstance(SupportDecision.class);
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbxQuotationState.setSelectedEntity(QuotationWkfStatus.QUO);
		cbActive.setValue(true);
		cbMandatory.setValue(false);
		cbCommentRequired.setValue(false);
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxQuotationState, "quotation.status");
		return errors.isEmpty();
	}
}
