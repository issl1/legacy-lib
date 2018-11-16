/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.refdata;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.BaseERefData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RefTableData Pop-up Panel.
 *
 * @author pengleng.huot
 */
public class RefTableDataPopupPanel extends Window implements AppServicesHelper {
    /**     */
    private static final long serialVersionUID = -8135717900914778306L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IRefTableDataForm refTableDataForm;

    private Long refTableId;

    /**
     * @param refTableDataTablePanel
     * @param caption
     * @param refDataForm
     */
    public RefTableDataPopupPanel(final RefTableDataTablePanel refTableDataTablePanel, String caption, IRefTableDataForm refDataForm) {
        super(caption);
        this.refTableDataForm = refDataForm;

        /**
         * On Save
         */
        Button btnSave = new NativeButton(I18N.message(I18N.message("save")),
                new Button.ClickListener() {

                    /** */
                    private static final long serialVersionUID = -8810879161108212590L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (refTableDataForm.validate()) {
                            RefTable table = ENTITY_SRV.getById(RefTable.class, refTableId);
                            if (table.isEntity()) {
                                BaseERefData baseERefData = ((RefTableDataForm) refTableDataForm).getRefEntity();
                                if (baseERefData != null) {
                                    ENTITY_SRV.saveOrUpdate(baseERefData);
                                    BaseERefData.addERefData(baseERefData);
                                }
                            } else {
                                RefData refData = refTableDataForm.getValue();
                                if (refData.getId() == null) {
                                    refData.setTable(table);
                                }
                                REFDATA_SRV.saveOrUpdate(refData);
                            }
                            /*
                             * Do not comment
							 */
//							RefDataToolsSrvRsc.callWsFlushCacheInAllApps();
                            refTableDataTablePanel.refreshTable(refTableId);
                            close();
                        } else {
                            refTableDataForm.displayErrors();
                        }
                    }

                });
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));

        /**
         * On cancel
         */
        Button btnCancel = new NativeButton(
                I18N.message(I18N.message("close")),
                new Button.ClickListener() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 780402777333190539L;

                    public void buttonClick(ClickEvent event) {
                        close();
                    }
                });

        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/close.png"));

        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addButton(btnSave);
        navigationPanel.addButton(btnCancel);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);
        contentLayout.addComponent(navigationPanel);
        contentLayout.addComponent(refTableDataForm.getForm());

        setModal(true);
        setContent(contentLayout);
        setWidth(500, Unit.PIXELS);
        setHeight(330, Unit.PIXELS);
        setResizable(true);
        UI.getCurrent().addWindow(this);
    }

    /**
     * Reset Panel
     */

    public void reset() {
        refTableDataForm.reset();

        markAsDirty();
    }

    /**
     * @param entityId
     */
    public void assignValues(Long entityId) {
        if (entityId != null) {
            RefData entity = ENTITY_SRV.getById(RefData.class, entityId);
            refTableDataForm.assignValues(entity);
        }
    }

    /**
     * @return the refTableId
     */
    public Long getRefTableId() {
        return refTableId;
    }

    /**
     * @param refTableId the refTableId to set
     */
    public void setRefTableId(Long refTableId) {
        this.refTableId = refTableId;
    }

    /**
     * @param field
     * @return
     */
    public Double getDouble(AbstractTextField field) {
        try {
            return Double.valueOf(Double.parseDouble((String) field.getValue()));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param field
     * @return
     */
    public Integer getInteger(AbstractTextField field) {
        try {
            return Integer.valueOf(Integer.parseInt((String) field.getValue()));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }
}
