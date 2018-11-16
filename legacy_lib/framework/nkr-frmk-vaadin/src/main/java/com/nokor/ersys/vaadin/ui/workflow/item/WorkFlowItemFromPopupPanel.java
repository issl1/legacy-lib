package com.nokor.ersys.vaadin.ui.workflow.item;

import com.nokor.common.app.action.model.EActionType;
import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import com.nokor.common.app.workflow.model.WkfFlowItem;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import java.util.ArrayList;

public class WorkFlowItemFromPopupPanel extends Window implements SeuksaServicesHelper {

    /** */
    private static final long serialVersionUID = -1763660808470239887L;

    private WkfFlowEntity wkfFlowEntity;
    private WkfFlowItem wkfFlowItem;

    private ERefDataComboBox<EWkfStatus> fromStatusField;
    private ERefDataComboBox<EWkfStatus> toStatusField;
    private ERefDataComboBox<EActionType> beforeActionTypeField;
    private ERefDataComboBox<EActionType> afterActionTypeField;
    private TextField beforeActionField;
    private TextField afterActionField;
    private TextField sortIndexField;

    public WorkFlowItemFromPopupPanel(final WorkFlowItemTablePanel tablePanel, String caption) {
        setModal(true);
        final Window window = new Window(caption);
        window.setModal(true);
        window.setResizable(false);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);

        contentLayout.addComponent(createNavigationPanel(window, tablePanel));
        contentLayout.addComponent(createFormPanel());

        window.setContent(contentLayout);
        window.setWidth(450, Unit.PIXELS);
        window.setHeight(335, Unit.PIXELS);
        UI.getCurrent().addWindow(window);
    }

    private VerticalLayout createFormPanel() {
        fromStatusField = new ERefDataComboBox<EWkfStatus>(I18N.message("workflow.item.fromstatus"), new ArrayList<>());
        fromStatusField.setWidth(200, Unit.PIXELS);
        fromStatusField.setTextInputAllowed(false);

        toStatusField = new ERefDataComboBox<EWkfStatus>(I18N.message("workflow.item.tostatus"), new ArrayList<>());
        toStatusField.setWidth(200, Unit.PIXELS);
        toStatusField.setTextInputAllowed(false);

        beforeActionTypeField = new ERefDataComboBox<EActionType>(I18N.message("workflow.item.beforeActionType"), EActionType.class);
        beforeActionTypeField.setWidth(200, Unit.PIXELS);
        beforeActionTypeField.setTextInputAllowed(false);

        afterActionTypeField = new ERefDataComboBox<EActionType>(I18N.message("workflow.item.afterActionType"), EActionType.class);
        afterActionTypeField.setWidth(200, Unit.PIXELS);
        afterActionTypeField.setTextInputAllowed(false);

        beforeActionField = ComponentFactory.getTextField("workflow.item.beforeAction", false, 45, 200);
        afterActionField = ComponentFactory.getTextField("workflow.item.afterAction", false, 45, 200);
        sortIndexField = ComponentFactory.getTextField("sortIndex", false, 2, 100);

        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(fromStatusField);
        formLayout.addComponent(toStatusField);
        formLayout.addComponent(beforeActionTypeField);
        formLayout.addComponent(beforeActionField);
        formLayout.addComponent(afterActionTypeField);
        formLayout.addComponent(afterActionField);
        formLayout.addComponent(sortIndexField);

        VerticalLayout mainFormPanel = new VerticalLayout(formLayout);
        mainFormPanel.setSpacing(true);
        mainFormPanel.setMargin(true);

        return mainFormPanel;
    }

    private NavigationPanel createNavigationPanel(final Window window, final WorkFlowItemTablePanel tablePanel) {
        Button btnSave = new NativeButton(I18N.message(I18N.message("save")),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 8088485001713740490L;

                    public void buttonClick(ClickEvent event) {
                        try {
                            if (wkfFlowEntity != null) {
                                if (validate()) {
                                    wkfFlowItem.setFlow(EWkfFlow.getByClass(Class.forName(wkfFlowEntity.getEntity().getCode())));
                                    wkfFlowItem.setWkfFlowEntity(wkfFlowEntity);
                                    wkfFlowItem.setFromStatus(fromStatusField.getSelectedEntity());
                                    wkfFlowItem.setToStatus(toStatusField.getSelectedEntity());
                                    //							wkfFlowItem.setBeforeAction(beforeActionField.getValue());
                                    //							wkfFlowItem.setAfterAction(afterActionField.getValue());

                                    if (sortIndexField.getValue() != null && !sortIndexField.getValue().isEmpty()) {
                                        wkfFlowItem.setSortIndex(Integer.valueOf(sortIndexField.getValue()));
                                    } else {
                                        wkfFlowItem.setSortIndex(null);
                                    }
                                    ENTITY_SRV.saveOrUpdate(wkfFlowItem);

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
                                            new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
                                    mb.show();
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));

        Button btnCancel = new NativeButton(I18N.message(I18N.message("close")),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 3975121141565713259L;

                    public void buttonClick(ClickEvent event) {
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
        fromStatusField.setSelectedEntity(null);
        toStatusField.setSelectedEntity(null);
        beforeActionTypeField.setSelectedEntity(null);
        afterActionTypeField.setSelectedEntity(null);
        beforeActionField.clear();
        afterActionField.clear();
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

        EWkfStatus fromStatus = fromStatusField.getSelectedEntity();
        EWkfStatus toStatus = toStatusField.getSelectedEntity();
        if (fromStatus != null && toStatus != null) {
            if (fromStatus.equals(toStatus)) {
                ValidateUtil.addError(I18N.message("workflow.error.massage1"));
                isValid = isValid & false;
            }
        }

        if (!beforeActionField.getValue().isEmpty() && beforeActionTypeField.getSelectedEntity() == null) {
            ValidateUtil.addError(I18N.message("workflow.error.massage2"));
            isValid = isValid & false;
        }

        if (!afterActionField.getValue().isEmpty() && afterActionTypeField.getSelectedEntity() == null) {
            ValidateUtil.addError(I18N.message("workflow.error.massage3"));
            isValid = isValid & false;
        }

        if (sortIndexField.isValid()) {
            isValid = isValid & ValidateUtil.checkIntegerField(sortIndexField, "sortIndex");
        }
        return isValid;
    }

    public void assignValues(WkfFlowEntity wkfFlowEntity) {
        assignValues(wkfFlowEntity, EntityFactory.createInstance(WkfFlowItem.class));
    }

    /**
     * Assign values
     *
     * @param wkfFlowItem : {@link WkfFlowItem}
     */
    public void assignValues(WkfFlowEntity wkfFlowEntity, WkfFlowItem wkfFlowItem) {
        this.wkfFlowEntity = wkfFlowEntity;
        this.wkfFlowItem = wkfFlowItem;

        fromStatusField.assignValueMap(wkfFlowEntity.getStatuses());
        toStatusField.assignValueMap(wkfFlowEntity.getStatuses());

        if (wkfFlowItem != null) {
            fromStatusField.setSelectedEntity(wkfFlowItem.getFromStatus());
            toStatusField.setSelectedEntity(wkfFlowItem.getToStatus());
            beforeActionTypeField.setSelectedEntity(wkfFlowItem.getBeforeAction() != null ? wkfFlowItem.getBeforeAction().getType() : null);
            afterActionTypeField.setSelectedEntity(wkfFlowItem.getAfterAction() != null ? wkfFlowItem.getAfterAction().getType() : null);

            if (wkfFlowItem.getBeforeAction() != null) {
                beforeActionField.setValue(wkfFlowItem.getBeforeAction().getExecValue());
            }
            if (wkfFlowItem.getAfterAction() != null) {
                afterActionField.setValue(wkfFlowItem.getAfterAction().getExecValue());
            }
            if (wkfFlowItem.getSortIndex() != null) {
                sortIndexField.setValue(wkfFlowItem.getSortIndex().toString());
            }
        }
    }

}
