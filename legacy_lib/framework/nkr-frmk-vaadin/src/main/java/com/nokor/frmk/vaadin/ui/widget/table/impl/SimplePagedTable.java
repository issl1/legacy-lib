package com.nokor.frmk.vaadin.ui.widget.table.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.PagedTableContainer;
import com.nokor.frmk.vaadin.ui.widget.table.event.PagedTableChangeEvent;
import com.nokor.frmk.vaadin.ui.widget.table.listener.PageChangeListener;
import com.nokor.frmk.vaadin.util.exporter.CSVExporter;
import com.nokor.frmk.vaadin.util.exporter.ExcelExporter;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * Paged table
 * @author ly.youhort
 *
 */
public class SimplePagedTable<T extends Entity> extends AbstractPagedTable<T> {
    private static final long serialVersionUID = 6881455780158545828L;

    private List<PageChangeListener<T>> listeners = null;
    private PagedTableContainer container;

    /**
     * @param columns
     */
    public SimplePagedTable(List<ColumnDefinition> columns) {
        this(null, columns);
    }
    
    /**
     * @param caption
     * @param columns
     */
    public SimplePagedTable(String caption, List<ColumnDefinition> columns) {
        super(caption);
        setPageLength(10);
        addStyleName("pagedtable");
        setWidth("100%");
        setSelectable(true);
        setColumnReorderingAllowed(true);
        setColumnCollapsingAllowed(true);
        setNullSelectionAllowed(false);
        setImmediate(true);
        setContainerDataSource(new IndexedContainer());
        List<Object> columnsVisible = new ArrayList<Object>();
        for (ColumnDefinition columnDefinition : columns) {
			addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
			if (columnDefinition.getColumnGenerator() != null) {
				addGeneratedColumn(columnDefinition.getPropertyId(), columnDefinition.getColumnGenerator());
			}
			setColumnHeader(columnDefinition.getPropertyId(), columnDefinition.getPropertyCaption());
			setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
			setColumnAlignment(columnDefinition.getPropertyId(), columnDefinition.getPropertyAlignment());
			if (columnDefinition.isVisible()) {
				columnsVisible.add(columnDefinition.getPropertyId());
			}
    	}
        setVisibleColumns(columnsVisible.toArray(new Object[columnsVisible.size()]));
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        Object value = property.getValue();
        if (value instanceof Date) {
            Date dateValue = (Date) value;
            return new SimpleDateFormat(DateUtils.FORMAT_DDMMYYYY_SLASH).format(dateValue);
        } else if (value instanceof Long || value instanceof Integer) {
        	return String.valueOf(value);
        } else if (value instanceof Amount) {
        	Amount amountValue = (Amount) value;
        	if (amountValue != null && amountValue.getTiAmount() != null) {
        		return AmountUtils.format(amountValue.getTiAmount(), amountValue.getNbDecimal());
        	}
        	return null;
        }
        return super.formatPropertyValue(rowId, colId, property);
    }
    
    /**
     * Create controls
     * @return
     */
    public HorizontalLayout createControls() {
    	
    	final Label itemsTotalRecordsLabel = new Label("");    	
        Label itemsPerPageLabel = new Label(I18N.message("items.per.page") + ":");
        final ComboBox itemsPerPageSelect = new ComboBox();
        itemsPerPageSelect.addItem("5");
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.addItem("25");
        itemsPerPageSelect.addItem("50");
        itemsPerPageSelect.addItem("100");
        itemsPerPageSelect.addItem("600");
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("50px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                setPageLength(Integer.valueOf(String.valueOf(event .getProperty().getValue())));
            }
        });
        itemsPerPageSelect.select("10");
        Label pageLabel = new Label(I18N.message("page") + ":&nbsp;", ContentMode.HTML);
        final TextField currentPageTextField = new TextField();
        currentPageTextField.setValue(String.valueOf(getCurrentPage()));
        currentPageTextField.setConverter(Integer.class);
        final IntegerRangeValidator validator = new IntegerRangeValidator("Wrong page number", 1, getTotalAmountOfPages());
        currentPageTextField.addValidator(validator);
        Label separatorLabel = new Label("&nbsp;/&nbsp;", ContentMode.HTML);
        final Label totalPagesLabel = new Label(String.valueOf(getTotalAmountOfPages()), ContentMode.HTML);
        currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        currentPageTextField.setImmediate(true);
        currentPageTextField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid() && currentPageTextField.getValue() != null) {
                    int page = Integer.valueOf(String.valueOf(currentPageTextField.getValue()));
                    setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setWidth("20px");
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);

        HorizontalLayout controlBar = new HorizontalLayout();
        HorizontalLayout pageSize = new HorizontalLayout();
        HorizontalLayout exportBar = createExportBar();
        HorizontalLayout pageManagement = new HorizontalLayout();
        final Button first = new Button("", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;
            public void buttonClick(ClickEvent event) {
                setCurrentPage(0);
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	first.setIcon(FontAwesome.FAST_BACKWARD);
        }
        else {
        	first.setIcon(new ThemeResource("../nkr-default/icons/16/first.png"));
        }
        
        final Button previous = new Button("", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;
            public void buttonClick(ClickEvent event) {
                previousPage();
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	previous.setIcon(FontAwesome.BACKWARD);
        }
        else {
        	previous.setIcon(new ThemeResource("../nkr-default/icons/16/previous.png"));
        }
        
        final Button next = new Button("", new ClickListener() {
            private static final long serialVersionUID = -1927138212640638452L;
            public void buttonClick(ClickEvent event) {
                nextPage();
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	next.setIcon(FontAwesome.FORWARD);
        }
        else {
        	next.setIcon(new ThemeResource("../nkr-default/icons/16/next.png"));
        }
        
        final Button last = new Button("", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;
            public void buttonClick(ClickEvent event) {
                setCurrentPage(getTotalAmountOfPages());
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	last.setIcon(FontAwesome.FAST_FORWARD);
        }
        else {
        	last.setIcon(new ThemeResource("../nkr-default/icons/16/last.png"));
        }
        
        first.setStyleName(Reindeer.BUTTON_LINK);
        previous.setStyleName(Reindeer.BUTTON_LINK);
        next.setStyleName(Reindeer.BUTTON_LINK);
        last.setStyleName(Reindeer.BUTTON_LINK);

        itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
        itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
        pageLabel.addStyleName("pagedtable-pagecaption");
        currentPageTextField.addStyleName("pagedtable-pagefield");
        separatorLabel.addStyleName("pagedtable-separator");
        totalPagesLabel.addStyleName("pagedtable-total");
        first.addStyleName("pagedtable-first");
        previous.addStyleName("pagedtable-previous");
        next.addStyleName("pagedtable-next");
        last.addStyleName("pagedtable-last");

        itemsTotalRecordsLabel.addStyleName("pagedtable-label");
        itemsPerPageLabel.addStyleName("pagedtable-label");
        itemsPerPageSelect.addStyleName("pagedtable-combobox");
        pageLabel.addStyleName("pagedtable-label");
        currentPageTextField.addStyleName("pagedtable-label");
        separatorLabel.addStyleName("pagedtable-label");
        totalPagesLabel.addStyleName("pagedtable-label");
        first.addStyleName("pagedtable-button");
        previous.addStyleName("pagedtable-button");
        next.addStyleName("pagedtable-button");
        last.addStyleName("pagedtable-button");

        pageSize.addComponent(itemsTotalRecordsLabel);
        pageSize.addComponent(itemsPerPageLabel);
        pageSize.addComponent(itemsPerPageSelect);
        pageSize.addComponent(exportBar);
        pageSize.setComponentAlignment(itemsTotalRecordsLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageSelect, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(exportBar, Alignment.MIDDLE_LEFT);
        pageSize.setSpacing(true);        
        pageManagement.addComponent(first);
        pageManagement.addComponent(previous);
        pageManagement.addComponent(pageLabel);
        pageManagement.addComponent(currentPageTextField);
        pageManagement.addComponent(separatorLabel);
        pageManagement.addComponent(totalPagesLabel);
        pageManagement.addComponent(next);
        pageManagement.addComponent(last);
        pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(currentPageTextField, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(separatorLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalPagesLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageManagement.setWidth(null);
        pageManagement.setSpacing(true);
        controlBar.addComponent(pageSize);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement, Alignment.MIDDLE_LEFT);
        controlBar.setWidth("100%");
        controlBar.setExpandRatio(pageSize, 1);
        addListener(new PageChangeListener<T>() {
            public void pageChanged(PagedTableChangeEvent<T> event) {
            	itemsTotalRecordsLabel.setValue("(" + I18N.message("total") + " : " + String.valueOf(container.getRealSize()) + " " + I18N.message("items") + ")");
                first.setEnabled(container.getStartIndex() > 0);
                previous.setEnabled(container.getStartIndex() > 0);
                next.setEnabled(container.getStartIndex() < container.getRealSize() - getPageLength());
                last.setEnabled(container.getStartIndex() < container.getRealSize() - getPageLength());
                currentPageTextField.setValue(String.valueOf(getCurrentPage()));
                totalPagesLabel.setValue(String.valueOf(getTotalAmountOfPages()));
                itemsPerPageSelect.setValue(String.valueOf(getPageLength()));
                validator.setMaxValue(getTotalAmountOfPages());
            }
        });
        return controlBar;
    }
    
    public HorizontalLayout createExportBar() {
        HorizontalLayout exportBar = new HorizontalLayout();
        
        final Button excelExporter = new Button("", new ClickListener() {
			private static final long serialVersionUID = -7152634450251542091L;
			public void buttonClick(ClickEvent event) {
            	final ExcelExporter excelExp = new ExcelExporter();
                excelExp.setContainerToBeExported(container);
                excelExp.sendConvertedFileToUser(getUI());
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	excelExporter.setIcon(FontAwesome.FILE_EXCEL_O);
        }
        else {
        	excelExporter.setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
        }
        excelExporter.setStyleName(Reindeer.BUTTON_LINK);
        
        final Button csvExporter = new Button("", new ClickListener() {
			private static final long serialVersionUID = -1155884428681466397L;
			public void buttonClick(ClickEvent event) {
            	final CSVExporter csvlExp = new CSVExporter();
                csvlExp.setContainerToBeExported(container);
                csvlExp.sendConvertedFileToUser(getUI());
            }
        });
        if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	csvExporter.setIcon(FontAwesome.FILE_TEXT_O);
        }
        else {
        	csvExporter.setIcon(new ThemeResource("../nkr-default/icons/16/csv.png"));
        }
        csvExporter.setStyleName(Reindeer.BUTTON_LINK);
        
        exportBar.addComponent(excelExporter);
        exportBar.addComponent(csvExporter);
        exportBar.setSpacing(true);
        exportBar.setWidth(null);
       
        return exportBar;
    }

    @Override
    public Container.Indexed getContainerDataSource() {
        return container.getContainer();
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        if (!(newDataSource instanceof Container.Indexed)) {
            throw new IllegalArgumentException(
                    "PagedTable can only use containers that implement Container.Indexed");
        }
        PagedTableContainer pagedTableContainer = new PagedTableContainer((Container.Indexed) newDataSource);
        pagedTableContainer.setPageLength(getPageLength());
        super.setContainerDataSource(pagedTableContainer);
        this.container = pagedTableContainer;
        firePagedChangedEvent();
    }

    /**
     */
    public void refreshContainerDataSource() {
    	setPageFirstIndex(0);
    }
    
    /**
     * Set page first index
     * @param firstIndex
     */
    private void setPageFirstIndex(int firstIndex) {
        if (container != null) {
            if (firstIndex <= 0) {
                firstIndex = 0;
            }
            if (firstIndex > container.getRealSize() - 1) {
                int size = container.getRealSize() - 1;
                int pages = 0;
                if (getPageLength() != 0) {
                    pages = (int) Math.floor(0.0 + size / getPageLength());
                }
                firstIndex = pages * getPageLength();
            }
            container.setStartIndex(firstIndex);
            setCurrentPageFirstItemIndex(firstIndex);
            containerItemSetChange(new Container.ItemSetChangeEvent() {
                private static final long serialVersionUID = -5083660879306951876L;
                public Container getContainer() {
                    return container;
                }
            });
            if (alwaysRecalculateColumnWidths) {
                for (Object columnId : container.getContainerPropertyIds()) {
                    setColumnWidth(columnId, -1);
                }
            }
            firePagedChangedEvent();
        }
    }

    /**
     * Fire paged changed event
     */
    private void firePagedChangedEvent() {
        if (listeners != null) {
            PagedTableChangeEvent<T> event = new PagedTableChangeEvent<T>(this);
            for (PageChangeListener<T> listener : listeners) {
                listener.pageChanged(event);
            }
        }
    }

    @Override
    public void setPageLength(int pageLength) {
        if (pageLength >= 0 && getPageLength() != pageLength) {
            container.setPageLength(pageLength);
            super.setPageLength(pageLength);
            firePagedChangedEvent();
        }
    }

    /**
     * Next page
     */
    public void nextPage() {
        setPageFirstIndex(container.getStartIndex() + getPageLength());
    }

    /**
     * Previous page
     */
    public void previousPage() {
        setPageFirstIndex(container.getStartIndex() - getPageLength());
    }

    /**
     * Get current page index
     * @return
     */
    public int getCurrentPage() {
        double pageLength = getPageLength();
        int page = (int) Math.floor((double) container.getStartIndex() / pageLength) + 1;
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    /**
     * Set current page index
     * @param page
     */
    public void setCurrentPage(int page) {
        int newIndex = (page - 1) * getPageLength();
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex >= 0 && newIndex != container.getStartIndex()) {
            setPageFirstIndex(newIndex);
        }
    }

    /**
     * Get nb page
     * @return
     */
    public int getTotalAmountOfPages() {
        int size = container.getContainer().size();
        double pageLength = getPageLength();
        int pageCount = (int) Math.ceil(size / pageLength);
        if (pageCount < 1) {
            pageCount = 1;
        }
        return pageCount;
    }

    /**
     * Add new PageChangeListener
     * @param listener
     */
    public void addListener(PageChangeListener<T> listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener<T>>();
        }
        listeners.add(listener);
    }

    /**
     * Remove PageChangeListener
     * @param listener
     */
    public void removeListener(PageChangeListener<T> listener) {
        if (listeners == null) {
            listeners = new ArrayList<PageChangeListener<T>>();
        }
        listeners.remove(listener);
    }

    /**
     * @param alwaysRecalculateColumnWidths
     */
    public void setAlwaysRecalculateColumnWidths(boolean alwaysRecalculateColumnWidths) {
        this.alwaysRecalculateColumnWidths = alwaysRecalculateColumnWidths;
    }

}