package com.nokor.ersys.vaadin.ui.workflow.entity;

import com.nokor.common.app.eref.EProductLineCode;
import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

/**
 * Created by ki.kao on 6/20/2017.
 */
public class WorkFlowEntityFromPopupPanel extends Window implements SeuksaServicesHelper {

    /** */
    private static final long serialVersionUID = -1763660808470239887L;

    private EWkfFlow workflow;
    private WkfFlowEntity wkfFlowEntity;
    private TextField txtEntity;
    private ERefDataComboBox<EProductLineCode> productLineCodeFiled;
    private TextField sortIndexField;

    public WorkFlowEntityFromPopupPanel(final WorkFlowEntityTablePanel tablePanel, String caption) {
        setModal(true);
        final Window window = new Window(caption);
        window.setModal(true);
        window.setResizable(false);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);

        contentLayout.addComponent(createNavigationPanel(window, tablePanel));
        contentLayout.addComponent(createFormPanel());

        window.setContent(contentLayout);
        window.setWidth(400, Unit.PIXELS);
        window.setHeight(195, Unit.PIXELS);
        UI.getCurrent().addWindow(window);
    }

    private VerticalLayout createFormPanel() {

        txtEntity = ComponentFactory.getTextField("entity", false, 10, 110);
        txtEntity.setEnabled(false);
        productLineCodeFiled = new ERefDataComboBox<EProductLineCode>(I18N.message("product.line"), EProductLineCode.class);
        productLineCodeFiled.setRequired(true);
        productLineCodeFiled.setTextInputAllowed(false);
        sortIndexField = ComponentFactory.getTextField("sortIndex", false, 10, 100);

        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(txtEntity);
        formLayout.addComponent(productLineCodeFiled);
        formLayout.addComponent(sortIndexField);

        VerticalLayout mainFormPanel = new VerticalLayout(formLayout);
        mainFormPanel.setSpacing(true);
        mainFormPanel.setMargin(true);

        return mainFormPanel;
    }

    private NavigationPanel createNavigationPanel(final Window window, final WorkFlowEntityTablePanel tablePanel) {
        Button btnSave = new NativeButton(I18N.message(I18N.message("save")),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 8088485001713740490L;

                    public void buttonClick(Button.ClickEvent event) {
                        if (workflow != null) {
                            if (validate()) {
                                wkfFlowEntity.setEntity(workflow.getEntity());
                                if (productLineCodeFiled.getSelectedEntity() != null)
                                    wkfFlowEntity.setProductCode(productLineCodeFiled.getSelectedEntity());
                                if (sortIndexField.getValue() != null) {
                                    wkfFlowEntity.setSortIndex(Integer.valueOf(sortIndexField.getValue()));
                                } else {
                                    wkfFlowEntity.setSortIndex(null);
                                }
                                ENTITY_SRV.saveOrUpdate(wkfFlowEntity);

                                tablePanel.refreshTable();

                                try {
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                }
                                window.close();
                            } else {
                                String msg = "Please check error below: <br/>";
                                msg += ValidateUtil.getErrorMessages();

                                MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
                                        MessageBox.Icon.ERROR, msg, Alignment.MIDDLE_RIGHT,
                                        new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")));
                                mb.show();
                            }
                        }
                    }
                }
        );
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));

        Button btnCancel = new NativeButton(I18N.message(I18N.message("close")),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 3975121141565713259L;

                    public void buttonClick(Button.ClickEvent event) {
                        window.close();
                    }
                }
        );
        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/close.png"));

        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addButton(btnSave);
        navigationPanel.addButton(btnCancel);
        return navigationPanel;
    }

    /**
     * reset form popup panel
     */
    public void reset() {
        txtEntity.setValue("");
        productLineCodeFiled.setSelectedEntity(null);
        sortIndexField.clear();
    }

    /**
     * Validate
     *
     * @return {@link Boolean}
     */
    private boolean validate() {
        ValidateUtil.clearErrors();

        boolean isValid = true;
        isValid = isValid && ValidateUtil.checkMandatoryField(txtEntity, "entity");
        isValid = isValid & ValidateUtil.checkMandatorySelectField(productLineCodeFiled, "product.line");
        if (sortIndexField.isValid()) {
            isValid = isValid & ValidateUtil.checkIntegerField(sortIndexField, "sortIndex");
        }
        return isValid;
    }

    public void assignValues(EWkfFlow workflow) {
        assignValues(workflow, EntityFactory.createInstance(WkfFlowEntity.class));
    }

    /**
     * Assign values
     *
     * @param wkfFlowEntity : {@link WkfFlowEntity}
     */
    public void assignValues(EWkfFlow workflow, WkfFlowEntity wkfFlowEntity) {
        this.workflow = workflow;
        this.wkfFlowEntity = wkfFlowEntity;

        if (wkfFlowEntity != null) {
            wkfFlowEntity.setEntity(workflow.getEntity());
            txtEntity.setValue(wkfFlowEntity.getEntity() != null ? wkfFlowEntity.getEntity().getDescEn() : "");
            productLineCodeFiled.setSelectedEntity(wkfFlowEntity.getProductCode());
            if (wkfFlowEntity.getSortIndex() != null) {
                sortIndexField.setValue(wkfFlowEntity.getSortIndex().toString());
            }
        }
    }

}
