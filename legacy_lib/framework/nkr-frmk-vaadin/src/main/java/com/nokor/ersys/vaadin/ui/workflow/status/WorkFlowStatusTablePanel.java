/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow.status;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import com.nokor.common.app.workflow.model.WkfFlowStatus;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengleng.huot
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorkFlowStatusTablePanel extends AbstractTabPanel
        implements AddClickListener, EditClickListener, DeleteClickListener, SearchClickListener, SeuksaServicesHelper {
    /***/
    private static final long serialVersionUID = -8654161944292126312L;

    private WorkFlowStatusTablePanel currentPanel;
    private WkfFlowEntity wkfFlowEntity;

    private List<ColumnDefinition> columnDefinitions;
    private SimpleTable<Entity> simpleTable;
    private Item selectedItem = null;

    public WorkFlowStatusTablePanel() {
        super();
        setSizeFull();
        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addAddClickListener(this);
        navigationPanel.addEditClickListener(this);
        navigationPanel.addDeleteClickListener(this);
        navigationPanel.addRefreshClickListener(this);
        addComponent(navigationPanel, 0);

        currentPanel = this;
    }

    @Override
    public void addButtonClick(ClickEvent event) {
        if (wkfFlowEntity == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.ERROR, I18N.message("msg.info.identity.should.be.created.first"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
            WorkFlowStatusFromPopupPanel formPopupPanel = new WorkFlowStatusFromPopupPanel(currentPanel, I18N.message("workflow.status"));
            formPopupPanel.reset();
            formPopupPanel.assignValues(wkfFlowEntity);
        }
    }

    @Override
    public void editButtonClick(ClickEvent event) {
        if (selectedItem == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.INFO, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
            final Long selectedId = (Long) selectedItem.getItemProperty("id").getValue();

            WorkFlowStatusFromPopupPanel formPopupPanel = new WorkFlowStatusFromPopupPanel(currentPanel, I18N.message("workflow.status"));
            formPopupPanel.reset();
            formPopupPanel.assignValues(wkfFlowEntity, ENTITY_SRV.getById(WkfFlowStatus.class, selectedId));
        }
    }

    @Override
    public void deleteButtonClick(ClickEvent event) {
        if (selectedItem == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
            final Long selectedId = (Long) selectedItem.getItemProperty("id").getValue();
            ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedId)),
                    new ConfirmDialog.Listener() {
                        /** */
                        private static final long serialVersionUID = -2203757872162548422L;

                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                ENTITY_SRV.delete(WkfFlowStatus.class, selectedId);
                                refreshTable();
                            }
                        }
                    });
        }
    }

    @Override
    public void searchButtonClick(ClickEvent event) {
        refreshTable();
    }

    @Override
    protected com.vaadin.ui.Component createForm() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setStyleName("has-no-padding");
        contentLayout.setMargin(true);
        this.columnDefinitions = createColumnDefinitions();
        simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
        simpleTable.addItemClickListener(new ItemClickListener() {
            /**     */
            private static final long serialVersionUID = -6676228064499031341L;

            @Override
            public void itemClick(ItemClickEvent event) {
                selectedItem = event.getItem();

                boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
                if (isDoubleClick) {
                    editButtonClick(null);
                }
            }
        });

        contentLayout.addComponent(simpleTable);
        return contentLayout;
    }

    /**
     * Get indexed container
     *
     * @return {@link IndexedContainer}
     */
    @SuppressWarnings("unchecked")
    private IndexedContainer getIndexedContainer(List<WkfFlowStatus> wkfFlowStatuses) {
        IndexedContainer indexedContainer = new IndexedContainer();
        for (ColumnDefinition column : this.columnDefinitions) {
            indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
        }
        if (wkfFlowStatuses != null && !wkfFlowStatuses.isEmpty()) {
            for (WkfFlowStatus wkfFlowStatus : wkfFlowStatuses) {
                Item item = indexedContainer.addItem(wkfFlowStatus.getId());
                item.getItemProperty("id").setValue(wkfFlowStatus.getId());
                item.getItemProperty("status" + "." + "desc").setValue(wkfFlowStatus.getStatus().getDesc());
                item.getItemProperty("sortIndex").setValue(wkfFlowStatus.getSortIndex());
                item.getItemProperty("updateDate").setValue((DateUtils.date2StringDDMMYYYY_MINUS(wkfFlowStatus.getUpdateDate())));
            }
        }
        return indexedContainer;
    }

    /**
     * Get Column definitions
     *
     * @return {@link List<ColumnDefinition>}
     */
    protected List<ColumnDefinition> createColumnDefinitions() {
        List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
        columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("status" + "." + "desc", I18N.message("desc"), String.class, Align.LEFT, 250));
        columnDefinitions.add(new ColumnDefinition("sortIndex", I18N.message("sortIndex"), Integer.class, Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date.updated"), String.class, Align.LEFT, 120));
        return columnDefinitions;
    }

    /**
     * Refresh table after save action
     */
    public void refreshTable() {
        assignValues(wkfFlowEntity);
    }

    /**
     * Assign values to controls
     *
     * @param entityId : {@link Long}
     */
    public void assignValuesToControls(Long entityId) {
        if (entityId != null) {
            // Must to reload to refresh session
            //wkfFlow = ENTITY_SRV.getById(EWkfFlow.class, entityId);
            wkfFlowEntity = ENTITY_SRV.getById(WkfFlowEntity.class, entityId);
            assignValues(wkfFlowEntity);
        }
    }

    /**
     * Assign values
     *
     * @param secUser : {@link EWkfFlow}
     */
    private void assignValues(WkfFlowEntity wkfFlowEntity) {
        selectedItem = null;
        reset();

        BaseRestrictions<WkfFlowStatus> restrictions = new BaseRestrictions<WkfFlowStatus>(WkfFlowStatus.class);
        restrictions.addCriterion("wkfFlowEntity.id", wkfFlowEntity.getId());
        restrictions.addOrder(Order.asc("sortIndex"));
        restrictions.addOrder(Order.asc("id"));
        List<WkfFlowStatus> wkfFlowStatus = ENTITY_SRV.list(restrictions);

        if (wkfFlowStatus != null && !wkfFlowStatus.isEmpty()) {
            simpleTable.setContainerDataSource(getIndexedContainer(wkfFlowStatus));
        } else {
            simpleTable.removeAllItems();
            // Must use this code to clear table
            IndexedContainer indexedContainer = new IndexedContainer();
            for (ColumnDefinition column : this.columnDefinitions) {
                indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
            }
            simpleTable.setContainerDataSource(indexedContainer);
        }
    }

}
