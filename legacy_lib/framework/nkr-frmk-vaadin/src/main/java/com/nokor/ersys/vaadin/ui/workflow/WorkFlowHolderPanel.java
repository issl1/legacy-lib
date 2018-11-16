/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow;

import com.nokor.ersys.vaadin.ui.workflow.entity.WorkFlowEntityTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;

/**
 * @author pengleng.huot
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(WorkFlowHolderPanel.NAME)
public class WorkFlowHolderPanel extends AbstractTabsheetPanel implements View {
    /** */
    private static final long serialVersionUID = -535053372024287048L;

    public static final String NAME = "workflows";

    @Autowired
    private WorkFlowTablePanel tablePanel;
    @Autowired
    private WorkFlowFormPanel formPanel;
    @Autowired
    private WorkFlowEntityTablePanel entityTablePanel;

    @Override
    public void enter(ViewChangeEvent event) {
    }

    @PostConstruct
    public void PostConstruct() {
        super.init();
        tablePanel.setMainPanel(this);
        formPanel.setCaption(I18N.message("detail"));
        entityTablePanel.setCaption(I18N.message("workflow.entities"));
        entityTablePanel.setMainPanel(this);
        getTabSheet().setTablePanel(tablePanel);

        getTabSheet().addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            private static final long serialVersionUID = -5587887484824043025L;

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                if (event.getTabSheet().getSelectedTab().equals(entityTablePanel)) {
                    TabSheet tabSheet = event.getTabSheet();
                    TabSheet.Tab wkfStatus = tabSheet.getTab(3);
                    TabSheet.Tab wkfStatusItem = tabSheet.getTab(4);
                    if (wkfStatus != null && wkfStatus.isVisible())
                        tabSheet.removeTab(wkfStatus);
                    if (wkfStatusItem != null && wkfStatusItem.isVisible())
                        tabSheet.removeTab(wkfStatusItem);
                }
                if (event.getTabSheet().getSelectedTab().getCaption().equals(I18N.message("workflow.items"))) {
                        entityTablePanel.refreshStatus();
                }
            }
        });
    }

    @Override
    public void onAddEventClick() {
        formPanel.reset();
        getTabSheet().addFormPanel(formPanel);
        getTabSheet().setSelectedTab(formPanel);
    }

    @Override
    public void onEditEventClick() {
        entityTablePanel.assignValuesToControls(tablePanel.getItemSelectedId());
        getTabSheet().addFormPanel(formPanel);
        getTabSheet().addFormPanel(entityTablePanel);
        initSelectedTab(formPanel);
    }

    @Override
    public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
        if (selectedTab == formPanel) {
            formPanel.assignValues(tablePanel.getItemSelectedId());
        } else if (selectedTab == tablePanel && getTabSheet().isNeedRefresh()) {
            tablePanel.refresh();
        }
        getTabSheet().setSelectedTab(selectedTab);
    }

}
