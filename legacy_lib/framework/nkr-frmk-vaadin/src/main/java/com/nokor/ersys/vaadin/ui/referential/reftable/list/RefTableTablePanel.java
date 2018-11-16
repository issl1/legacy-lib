/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.list;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * RefTable Table Panel.
 *
 * @author pengleng.huot
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RefTableTablePanel extends AbstractTablePanel<RefTable> implements AppServicesHelper {

    /**     */
    private static final long serialVersionUID = 6186374575959809352L;
    public static final Map<String, String> packageName;

    static {
        packageName = new HashMap<>();
    }

    @PostConstruct
    public void PostConstruct() {
        setCaption(I18N.message("ref.table.data"));
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        super.init(getCaption());
        NavigationPanel navigationPanel = addNavigationPanel();
        navigationPanel.addEditClickListener(this);

        Button btnViewData = new NativeButton(I18N.message(I18N.message("view.data")),
                new Button.ClickListener() {

                    /**     */
                    private static final long serialVersionUID = 8037669867834969655L;

                    public void buttonClick(ClickEvent event) {
                        mainPanel.getTabSheet().setAdd(false);
                        ((BaseRefTableHolderPanel) mainPanel).onViewSubTab();
                    }
                }
        );
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
            btnViewData.setIcon(FontAwesome.TABLE);
        } else {
            btnViewData.setIcon(new ThemeResource("../nkr-default/icons/16/view.png"));
        }
        navigationPanel.addButton(btnViewData);

        navigationPanel.addRefreshClickListener(this);

        pagedTable.addItemClickListener(new ItemClickListener() {

            /**     */
            private static final long serialVersionUID = -6816111694043069470L;

            @Override
            public void itemClick(ItemClickEvent event) {
                selectedItem = event.getItem();
                boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
                if (isDoubleClick) {
                    mainPanel.getTabSheet().setAdd(false);
                    ((BaseRefTableHolderPanel) mainPanel).onViewSubTab();
                }
            }
        });
    }

    @Override
    protected PagedDataProvider<RefTable> createPagedDataProvider() {
        PagedDefinition<RefTable> pagedDefinition = new PagedDefinition<RefTable>(searchPanel.getRestrictions());
        pagedDefinition.addColumnDefinition(RefTable.ID, I18N.message("id"), Long.class, Align.LEFT, 70);
        pagedDefinition.addColumnDefinition(RefTable.CODE, I18N.message("code"), String.class, Align.LEFT, 100, new CodeColumnRenderer());
        pagedDefinition.addColumnDefinition(RefTable.DESC, I18N.message("desc"), String.class, Align.LEFT, 250);
        pagedDefinition.addColumnDefinition(RefTable.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 250);
        pagedDefinition.addColumnDefinition(RefTable.SHORT_NAME, I18N.message("short.name"), String.class, Align.LEFT, 150);
        pagedDefinition.addColumnDefinition(RefTable.STATUSRECORD + "." + RefTable.CODE, I18N.message("is.active"), Boolean.class, Align.LEFT, 70, new StatusRecordColumnRenderer());
        pagedDefinition.addColumnDefinition(RefTable.READONLY, I18N.message("read.only"), Boolean.class, Align.LEFT, 70);
        pagedDefinition.addColumnDefinition(RefTable.USESORTINDEX, I18N.message("use.sort.index"), Boolean.class, Align.LEFT, 70);
        pagedDefinition.addColumnDefinition(RefTable.GENERATECODE, I18N.message("generate.code"), Boolean.class, Align.LEFT, 70);
        pagedDefinition.addColumnDefinition(RefTable.FETCHVALUESFROMDB, I18N.message("fetch.values.from.db"), Boolean.class, Align.LEFT, 140);
        pagedDefinition.addColumnDefinition(RefTable.FETCHI18NFROMDB, I18N.message("fetch.i18n.from.db"), Boolean.class, Align.LEFT, 140);
        pagedDefinition.addColumnDefinition(RefTable.VISIBLE, I18N.message("visible"), Boolean.class, Align.LEFT, 70, new VisibleColumnRenderer());
        pagedDefinition.addColumnDefinition(RefTable.CACHED, I18N.message("cached"), Boolean.class, Align.LEFT, 70, new CachedColumnRenderer());

        EntityPagedDataProvider<RefTable> pagedDataProvider = new EntityPagedDataProvider<RefTable>();
        pagedDataProvider.setPagedDefinition(pagedDefinition);
        return pagedDataProvider;
    }

    /**
     * Collapsed RefTable Columns
     */
    private void collapsedRefTableColumns() {
        if (pagedTable != null) {
            pagedTable.setColumnCollapsed(RefTable.DESC, true);
            pagedTable.setColumnCollapsed(RefTable.STATUSRECORD + "." + RefTable.CODE, true);
            pagedTable.setColumnCollapsed(RefTable.GENERATECODE, true);
            pagedTable.setColumnCollapsed(RefTable.FETCHVALUESFROMDB, true);
            pagedTable.setColumnCollapsed(RefTable.FETCHI18NFROMDB, true);
            pagedTable.setColumnCollapsed(RefTable.VISIBLE, true);
            pagedTable.setColumnCollapsed(RefTable.CACHED, true);
        }
    }

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedTable(java.lang.String)
     */
    @Override
    protected EntityPagedTable<RefTable> createPagedTable(String caption) {
        EntityPagedTable<RefTable> pagedTable = new RefTablePagedTable(caption, createPagedDataProvider());
        pagedTable.buildGrid();
        return pagedTable;
    }

    /**
     * RefTablePagedTable
     *
     * @author bunlong.taing
     */
    private class RefTablePagedTable extends EntityPagedTable<RefTable> {

        /** */
        private static final long serialVersionUID = -8803656132159059565L;

        public RefTablePagedTable(String caption, PagedDataProvider<RefTable> dataProvider) {
            super(caption, dataProvider);
        }

        /**
         * @see com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable#setContainerDataSource(com.vaadin.data.Container)
         */
        @Override
        public void setContainerDataSource(Container container) {
            super.setContainerDataSource(container);
            collapsedRefTableColumns();
        }

    }

    /**
     * Column Renderer for Code Field
     * Show only the first package and Class name
     * eg. com.sample.ClassName => com...ClassName
     *
     * @author bunlong.taing
     */
    private class CodeColumnRenderer extends EntityColumnRenderer {
        /** */
        private static final long serialVersionUID = 2289696558577228491L;

        @Override
        public Object getValue() {
            String value = ((RefTable) getEntity()).getCode();
            String tmp = new String(value);
            String result = "";

            if (StringUtils.isNotEmpty(value)) {
                result = value.substring(0, value.indexOf("."));
//				result += "..";
                result += value.substring(value.lastIndexOf("."));
            }
            String clazzName = value.substring(value.lastIndexOf(".") + 1);
            packageName.put(clazzName, tmp);
            return clazzName;
        }

    }

    /**
     * StatusRecord Column Renderer
     * True when ACTIV otherwise False
     *
     * @author bunlong.taing
     */
    private class StatusRecordColumnRenderer extends EntityColumnRenderer {

        /** */
        private static final long serialVersionUID = -8832258906977403870L;

        @Override
        public Object getValue() {
            return ((RefTable) getEntity()).getStatusRecord() == EStatusRecord.ACTIV;
        }

    }

    /**
     * Column Renderer for Visible field
     *
     * @author bunlong.taing
     */
    private class VisibleColumnRenderer extends EntityColumnRenderer {
        /** */
        private static final long serialVersionUID = -274729439358340702L;

        /**
         * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
         */
        @Override
        public Object getValue() {
            return ((RefTable) getEntity()).isVisible();
        }
    }

    /**
     * Column Renderer for Cached field
     *
     * @author bunlong.taing
     */
    private class CachedColumnRenderer extends EntityColumnRenderer {
        /** */
        private static final long serialVersionUID = 6868570675658675630L;

        /**
         * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
         */
        @Override
        public Object getValue() {
            return ((RefTable) getEntity()).isCached();
        }
    }

    public Class<?> getERefDataClass() throws ClassNotFoundException {
        RefTable refTable = getEntity();
        if (refTable != null) {
            return Class.forName(refTable.getCode());
        }
        return null;
    }

    @Override
    protected RefTable getEntity() {
        final Long id = getItemSelectedId();
        if (id != null) {
            return ENTITY_SRV.getById(RefTable.class, id);
        }
        return null;
    }

    @Override
    protected RefTableSearchPanel createSearchPanel() {
        return new RefTableSearchPanel(this);
    }
}
