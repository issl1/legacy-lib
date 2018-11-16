package com.nokor.efinance.ra.ui.panel.document;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Document Group form panel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentGroupFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = 6440116626693434925L;

	private DocumentGroup documentGroup;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtSortIndex;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		documentGroup.setCode(txtCode.getValue());
		documentGroup.setDesc(txtDesc.getValue());
		documentGroup.setDescEn(txtDescEn.getValue());
		documentGroup.setSortIndex(getInteger(txtSortIndex));
		documentGroup.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return documentGroup;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);        
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		txtSortIndex = ComponentFactory.getTextField("sort.index", false, 60, 100);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtSortIndex);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long dogrpId) {
		super.reset();
		if (dogrpId != null) {
			documentGroup = ENTITY_SRV.getById(DocumentGroup.class, dogrpId);
			txtCode.setValue(documentGroup.getCode());
			txtDescEn.setValue(documentGroup.getDescEn());
			txtDesc.setValue(documentGroup.getDesc());
			txtSortIndex.setValue(getDefaultString(documentGroup.getSortIndex()));
			cbActive.setValue(documentGroup.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		documentGroup = new DocumentGroup();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtSortIndex.setValue("");
		cbActive.setValue(true);
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
}
