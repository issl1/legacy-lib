package com.nokor.ersys.vaadin.ui.workflow.status;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import com.nokor.common.app.workflow.model.WkfFlowStatus;
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

public class WorkFlowStatusFromPopupPanel extends Window implements SeuksaServicesHelper {

    /** */
    private static final long serialVersionUID = -1763660808470239887L;

    private WkfFlowEntity wkfFlowEntity;
    private WkfFlowStatus wkfFlowStatus;

    private ERefDataComboBox<EWkfStatus> wkfFlowStatusField;
    private TextField sortIndexField;

    public WorkFlowStatusFromPopupPanel(final WorkFlowStatusTablePanel tablePanel, String caption) {
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
        window.setHeight(180, Unit.PIXELS);
        UI.getCurrent().addWindow(window);
    }

    private VerticalLayout createFormPanel() {
        wkfFlowStatusField = new ERefDataComboBox<EWkfStatus>(I18N.message("status"), EWkfStatus.class);
        wkfFlowStatusField.setRequired(true);
        wkfFlowStatusField.setTextInputAllowed(false);
        sortIndexField = ComponentFactory.getTextField("sortIndex", false, 10, 100);

        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(wkfFlowStatusField);
        formLayout.addComponent(sortIndexField);

        VerticalLayout mainFormPanel = new VerticalLayout(formLayout);
        mainFormPanel.setSpacing(true);
        mainFormPanel.setMargin(true);

        return mainFormPanel;
    }

    private NavigationPanel createNavigationPanel(final Window window, final WorkFlowStatusTablePanel tablePanel) {
        Button btnSave = new NativeButton(I18N.message(I18N.message("save")),
                new Button.ClickListener() {

                    private static final long serialVersionUID = 8088485001713740490L;

                    public void buttonClick(ClickEvent event) {
                        try {
                            if (wkfFlowEntity != null) {
                                if (validate()) {
                                    wkfFlowStatus.setFlow(EWkfFlow.getByClass(Class.forName(wkfFlowEntity.getEntity().getCode())));
                                    wkfFlowStatus.setWkfFlowEntity(wkfFlowEntity);
                                    wkfFlowStatus.setStatus(wkfFlowStatusField.getSelectedEntity());

                                    if (sortIndexField.getValue() != null) {
                                        wkfFlowStatus.setSortIndex(Integer.valueOf(sortIndexField.getValue()));
                                    } else {
                                        wkfFlowStatus.setSortIndex(null);
                                    }
                                    ENTITY_SRV.saveOrUpdate(wkfFlowStatus);

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
        wkfFlowStatusField.setSelectedEntity(null);
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
        isValid = isValid & ValidateUtil.checkMandatorySelectField(wkfFlowStatusField, "status");

        if (sortIndexField.isValid()) {
            isValid = isValid & ValidateUtil.checkIntegerField(sortIndexField, "sortIndex");
        }
        return isValid;
    }

    public void assignValues(WkfFlowEntity wkfFlowEntity) {
        assignValues(wkfFlowEntity, EntityFactory.createInstance(WkfFlowStatus.class));
    }

    /**
     * Assign values
     *
     * @param wkfFlowStatus : {@link WkfFlowStatus}
     */
    public void assignValues(WkfFlowEntity wkfFlowEntity, WkfFlowStatus wkfFlowStatus) {
        this.wkfFlowEntity = wkfFlowEntity;
        this.wkfFlowStatus = wkfFlowStatus;

        if (wkfFlowStatus != null) {
            wkfFlowStatusField.setSelectedEntity(wkfFlowStatus.getStatus());

            if (wkfFlowStatus.getSortIndex() != null) {
                sortIndexField.setValue(wkfFlowStatus.getSortIndex().toString());
            }
        }
    }

}
