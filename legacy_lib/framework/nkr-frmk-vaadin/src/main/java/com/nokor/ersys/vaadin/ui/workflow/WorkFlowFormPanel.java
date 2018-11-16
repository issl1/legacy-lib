/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.*;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author pengleng.huot
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorkFlowFormPanel extends AbstractFormPanel {
    /** */
    private static final long serialVersionUID = -3096847346299943785L;

    private EWkfFlow entity;

    private TextField txtCode;
    private TextField txtDesc;
    private TextField txtDescEn;
    private CheckBox cbActive;
    private ERefDataComboBox<EMainEntity> mainEntityField;

    @PostConstruct
    public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
        navigationPanel.addSaveClickListener(this);
    }

    @Override
    protected com.vaadin.ui.Component createForm() {
        mainEntityField = new ERefDataComboBox<EMainEntity>(I18N.message("entity"), EMainEntity.class);
        mainEntityField.setRequired(true);
        mainEntityField.setTextInputAllowed(false);
        txtCode = ComponentFactory.getTextField("code", true, 60, 200);
        txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
        txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);

        final FormLayout formPanel = new FormLayout();
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(mainEntityField);
        formPanel.addComponent(cbActive);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(formPanel);

        Panel mainPanel = ComponentFactory.getPanel();
        mainPanel.setContent(verticalLayout);
        return mainPanel;
    }

    @Override
    protected Entity getEntity() {
        entity.setCode(txtCode.getValue());
        entity.setDesc(txtDesc.getValue());
        entity.setDescEn(txtDescEn.getValue());
        entity.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
        entity.setEntity(mainEntityField.getSelectedEntity());
        return entity;
    }

    /**
     * @param asmakId
     */
    public void assignValues(Long id) {
        super.reset();
        if (id != null) {
            entity = ENTITY_SRV.getById(EWkfFlow.class, id);
            txtCode.setValue(entity.getCode());
            txtDescEn.setValue(entity.getDescEn());
            txtDesc.setValue(entity.getDesc());
            cbActive.setValue(entity.getStatusRecord().equals(EStatusRecord.ACTIV));
            mainEntityField.setSelectedEntity(entity.getEntity());
        }
    }

    /**
     * Reset
     */
    @Override
    public void reset() {
        super.reset();
        entity = EntityFactory.createInstance(EWkfFlow.class);
        txtCode.setValue("");
        txtDescEn.setValue("");
        txtDesc.setValue("");
        cbActive.setValue(true);
        mainEntityField.setSelectedEntity(null);
        markAsDirty();
    }

    /**
     * @return
     */
    @Override
    protected boolean validate() {
        checkMandatoryField(txtCode, "code");
        checkMandatoryField(txtDescEn, "desc.en");
        checkMandatorySelectField(mainEntityField, "entity");
        return errors.isEmpty();
    }
}
