package com.nokor.ersys.vaadin.ui.workflow.entity;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.WkfFlowEntity;
import com.nokor.common.app.workflow.model.WkfFlowStatus;
import com.nokor.ersys.vaadin.ui.workflow.item.WorkFlowItemTablePanel;
import com.nokor.ersys.vaadin.ui.workflow.status.WorkFlowStatusTablePanel;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ki.kao on 6/20/2017.
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorkFlowEntityTablePanel extends AbstractTabPanel
        implements AddClickListener, EditClickListener, DeleteClickListener, SearchClickListener, SeuksaServicesHelper {
    /***/
    private static final long serialVersionUID = -8654161944292126312L;

    private WorkFlowEntityTablePanel currentPanel;
    private AbstractTabsheetPanel mainPanel;
    private EWkfFlow wkfFlow;

    private List<ColumnDefinition> columnDefinitions;
    private SimpleTable<Entity> simpleTable;
    private Item selectedItem = null;
    private NavigationPanel navigationPanel;
    @Autowired
    private WorkFlowStatusTablePanel statusTablePanel;
    @Autowired
    private WorkFlowItemTablePanel itemTablePanel;

    public WorkFlowEntityTablePanel() {
        super();
        setSizeFull();
        navigationPanel = new NavigationPanel();
        navigationPanel.addAddClickListener(this);
        navigationPanel.addEditClickListener(this);
        navigationPanel.addDeleteClickListener(this);
        navigationPanel.addRefreshClickListener(this);
        addComponent(navigationPanel, 0);
        currentPanel = this;
    }

    @PostConstruct
    public void PostConstruct() {
        statusTablePanel.setCaption(I18N.message("workflow.status"));
        itemTablePanel.setCaption(I18N.message("workflow.items"));
    }

    public void setMainPanel(final AbstractTabsheetPanel mainPanel) {
        this.mainPanel = mainPanel;
        Button btnViewData = new NativeButton(I18N.message(I18N.message("view.flow")),
                new Button.ClickListener() {

                    /**     */
                    private static final long serialVersionUID = 8037669867834969655L;

                    public void buttonClick(Button.ClickEvent event) {
                        mainPanel.getTabSheet().setAdd(false);
                        if (selectedItem == null) {
                            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                                    MessageBox.Icon.WARN, I18N.message("msg.info.view.item.not.selected"), Alignment.MIDDLE_RIGHT,
                                    new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")));
                            mb.show();
                        } else {
                            statusTablePanel.assignValuesToControls((Long) selectedItem.getItemProperty("id").getValue());
                            itemTablePanel.assignValuesToControls((Long) selectedItem.getItemProperty("id").getValue());
                            mainPanel.getTabSheet().addFormPanel(statusTablePanel);
                            mainPanel.getTabSheet().addFormPanel(itemTablePanel);
                            mainPanel.getTabSheet().setSelectedTab(statusTablePanel);
                        }
                    }
                }
        );
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
            btnViewData.setIcon(FontAwesome.TABLE);
        } else {
            btnViewData.setIcon(new ThemeResource("../nkr-default/icons/16/view.png"));
        }
        navigationPanel.addButton(btnViewData);
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void refreshStatus(){
        itemTablePanel.assignValuesToControls((Long) selectedItem.getItemProperty("id").getValue());
    }
    @Override
    public void addButtonClick(Button.ClickEvent event) {
        if (wkfFlow == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.ERROR, I18N.message("msg.info.identity.should.be.created.first"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
            WorkFlowEntityFromPopupPanel formPopupPanel = new WorkFlowEntityFromPopupPanel(currentPanel, I18N.message("workflow.status"));
            formPopupPanel.reset();
            formPopupPanel.assignValues(wkfFlow);
        }
    }

    @Override
    public void editButtonClick(Button.ClickEvent event) {
        if (selectedItem == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.INFO, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")));
            mb.show();
        } else {
            final Long selectedId = (Long) selectedItem.getItemProperty("id").getValue();

            WorkFlowEntityFromPopupPanel formPopupPanel = new WorkFlowEntityFromPopupPanel(currentPanel, I18N.message("workflow.status"));
            formPopupPanel.reset();
            formPopupPanel.assignValues(wkfFlow, ENTITY_SRV.getById(WkfFlowEntity.class, selectedId));
        }
    }

    @Override
    public void deleteButtonClick(Button.ClickEvent event) {
        if (selectedItem == null) {
            MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
                    MessageBox.Icon.INFO, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
                    new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")));
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
    public void searchButtonClick(Button.ClickEvent event) {
        refreshTable();
    }

    @Override
    protected com.vaadin.ui.Component createForm() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setStyleName("has-no-padding");
        contentLayout.setMargin(true);
        this.columnDefinitions = createColumnDefinitions();
        simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
        simpleTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
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
    private IndexedContainer getIndexedContainer(List<WkfFlowEntity> wkfFlowEntities) {
        IndexedContainer indexedContainer = new IndexedContainer();
        for (ColumnDefinition column : this.columnDefinitions) {
            indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
        }
        if (wkfFlowEntities != null && !wkfFlowEntities.isEmpty()) {
            for (WkfFlowEntity wkfFlowEntity : wkfFlowEntities) {
                Item item = indexedContainer.addItem(wkfFlowEntity.getId());
                item.getItemProperty("id").setValue(wkfFlowEntity.getId());
                item.getItemProperty("entity" + "." + "desc").setValue(wkfFlowEntity.getEntity() != null ? wkfFlowEntity.getEntity().getDesc() : "");
                item.getItemProperty("productCode" + "." + "descEn").setValue(wkfFlowEntity.getProductCode().getDescEn());
                item.getItemProperty("sortIndex").setValue(wkfFlowEntity.getSortIndex());
                item.getItemProperty("updateDate").setValue((DateUtils.date2StringDDMMYYYY_MINUS(wkfFlowEntity.getUpdateDate())));
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
        columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Table.Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("entity" + "." + "desc", I18N.message("entity"), String.class, Table.Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("productCode" + "." + "descEn", I18N.message("product.line"), String.class, Table.Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("sortIndex", I18N.message("sortIndex"), Integer.class, Table.Align.LEFT, 100));
        columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date.updated"), String.class, Table.Align.LEFT, 120));
        return columnDefinitions;
    }

    /**
     * Refresh table after save action
     */
    public void refreshTable() {
        assignValues(wkfFlow);
    }

    /**
     * Assign values to controls
     *
     * @param entityId : {@link Long}
     */
    public void assignValuesToControls(Long entityId) {
        if (entityId != null) {
            // Must to reload to refresh session
            wkfFlow = ENTITY_SRV.getById(EWkfFlow.class, entityId);
            assignValues(wkfFlow);
        }
    }

    /**
     * Assign values
     *
     * @param : {@link EWkfFlow}
     */
    private void assignValues(EWkfFlow wkfFlow) {
        selectedItem = null;
        reset();

        BaseRestrictions<WkfFlowEntity> restrictions = new BaseRestrictions<WkfFlowEntity>(WkfFlowEntity.class);
        restrictions.addCriterion("entity", wkfFlow.getEntity());
        restrictions.addOrder(Order.asc("sortIndex"));
        restrictions.addOrder(Order.asc("id"));
        List<WkfFlowEntity> wkfFlowEntities = ENTITY_SRV.list(restrictions);

        if (wkfFlowEntities != null && !wkfFlowEntities.isEmpty()) {
            simpleTable.setContainerDataSource(getIndexedContainer(wkfFlowEntities));
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
