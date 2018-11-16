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
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.event.PagedTableChangeEvent;
import com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener;
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
 */
public class EntityPagedTable<T extends Entity> extends AbstractPagedTable<T> {
    /** */
	private static final long serialVersionUID = -1575082192177419739L;
	
	private List<PageChangeListener<T>> listeners = null;
    private ItemPerPageChangeListener itemPerPageChangeListener;
    private int startIndex = 0;
    private long totalRecords = 0;
    private PagedDataProvider<T> dataProvider;
    private Container currentContainer;
    private List<Object> visibleCols;
    
    /**
     * @param columns
     */
    public EntityPagedTable(PagedDataProvider<T> dataProvider) {
    	this("", dataProvider);
    }
    
    	
    /**
     * 
     * @param caption
     * @param dataProvider
     */
    public EntityPagedTable(final String caption, PagedDataProvider<T> dataProvider) {
        super(caption);
        this.dataProvider = dataProvider;
        setPageLength(10); // take from ini file or appConf
        addStyleName("pagedtable");
        setWidth("100%");
        setSelectable(true);
        setColumnReorderingAllowed(true);
        setColumnCollapsingAllowed(true);
        setNullSelectionAllowed(false);
        setImmediate(true);
     }
    
    /**
     * 
     */
    public void buildGrid() {
        buildGridHeader();
        totalRecords = getTotalRecords();
        // setContainerDataSource(buildIndexedContainer());
    }
        
    /**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}


	/**
     * 
     */
    public void buildGridHeader() {
    	visibleCols = new ArrayList<Object>();
    	
    	for (ColumnDefinition columnDefinition : dataProvider.getPagedDefinition().getColumnDefinitions()) {
			addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
			setColumnHeader(columnDefinition.getPropertyId(), columnDefinition.getPropertyCaption());
			setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
			setColumnAlignment(columnDefinition.getPropertyId(), columnDefinition.getPropertyAlignment());
			
			if (columnDefinition.isVisible()) {
				visibleCols.add(columnDefinition.getPropertyId());
			}
    	}
    	// setVisibleColumns(visibleCols.toArray(new Object[visibleCols.size()]));
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
        Label itemsPerPageLabel = new Label(I18N.message("items.per.page"));
        final ComboBox itemsPerPageSelect = new ComboBox();
        itemsPerPageSelect.addItem("5");
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.addItem("25");
        itemsPerPageSelect.addItem("50");
        itemsPerPageSelect.addItem("100");
        itemsPerPageSelect.addItem("200");
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("50px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
            	int itemPerPage = Integer.valueOf(String.valueOf(event.getProperty().getValue()));
                setPageLength(itemPerPage);
                setPageFirstIndex(getStartIndex());
                
                if (itemPerPageChangeListener != null) {
                	itemPerPageChangeListener.onItemPerPageChange(itemPerPage);
                }
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
            public void pageChanged(PagedTableChangeEvent<T>  event) {
            	itemsTotalRecordsLabel.setValue("(" + I18N.message("total") + " : " + String.valueOf(totalRecords) + " " + I18N.message("items") + ")");
                first.setEnabled(getStartIndex() > 0);
                previous.setEnabled(getStartIndex() > 0);
                next.setEnabled(getStartIndex() < totalRecords - getPageLength());
                last.setEnabled(getStartIndex() < totalRecords - getPageLength());
                currentPageTextField.setValue(String.valueOf(getCurrentPage()));
                totalPagesLabel.setValue(String.valueOf(getTotalAmountOfPages()));
                itemsPerPageSelect.setValue(String.valueOf(getPageLength()));
                validator.setMaxValue(getTotalAmountOfPages());
            }
        });
        firePagedChangedEvent();
        return controlBar;
    }

    public HorizontalLayout createExportBar() {
        HorizontalLayout exportBar = new HorizontalLayout();
        final Button excelExporter = new Button("", new ClickListener() {
			private static final long serialVersionUID = -345128294252780849L;
			public void buttonClick(ClickEvent event) {
            	final ExcelExporter excelExp = new ExcelExporter();
            	IndexedContainer tmpContainer = dataProvider.getIndexedContainer(0, (int) totalRecords);
                excelExp.setContainerToBeExported(tmpContainer);
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
			private static final long serialVersionUID = 1544211032887048179L;
			public void buttonClick(ClickEvent event) {
            	final CSVExporter csvlExp = new CSVExporter();
            	IndexedContainer tmpContainer = dataProvider.getIndexedContainer(0, (int) totalRecords);
                csvlExp.setContainerToBeExported(tmpContainer);
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
    public void setContainerDataSource(Container container) {
        if (!(container instanceof Container.Indexed)) {
            throw new IllegalArgumentException(
                    "PagedTable can only use containers that implement Container.Indexed");
        }
        super.setContainerDataSource(container); 
        firePagedChangedEvent();
    }
    
    /**
     * Get start page index
     * @return
     */
    public int getStartIndex() {
    	return startIndex;
    }
    
    /**
     * Set start page index
     * @param startIndex
     */
    public void setStartIndex(int startIndex) {
    	this.startIndex = startIndex;
    }

    /**
     * Set page first index
     * @param firstIndex
     */
    private void setPageFirstIndex(int firstIndex) {
    	setPageFirstIndex(firstIndex, false);
    }

    /**
     * Set page first index
     * @param firstIndex
     * @param forceReloadData
     */
    private void setPageFirstIndex(int firstIndex, boolean forceReloadData) {
    	if (forceReloadData || getStartIndex() != firstIndex || currentContainer == null) {
    		setStartIndex(firstIndex);
    		currentContainer = buildIndexedContainer();
    	}
    	
        if (currentContainer != null) {
            if (firstIndex <= 0) {
                firstIndex = 0;
            }
            if (firstIndex > totalRecords - 1) {
                long size = totalRecords - 1;
                int pages = 0;
                if (getPageLength() != 0) {
                    pages = (int) Math.floor(0.0 + size / getPageLength());
                }
                firstIndex = pages * getPageLength();
            }
            setCurrentPageFirstItemIndex(firstIndex);
            if (alwaysRecalculateColumnWidths) {
                for (Object columnId : currentContainer.getContainerPropertyIds()) {
                    setColumnWidth(columnId, -1);
                }
            }
            setContainerDataSource(currentContainer);
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
            super.setPageLength(pageLength);
            firePagedChangedEvent();
        }
    }

    /**
     * Next page
     */
    public void nextPage() {
        setPageFirstIndex(getStartIndex() + getPageLength());
    }

    /**
     * Previous page
     */
    public void previousPage() {
        setPageFirstIndex(getStartIndex() - getPageLength());
    }

    /**
     * Get current page index
     * @return
     */
    public int getCurrentPage() {
        double pageLength = getPageLength();
        int page = (int) Math.floor((double) getStartIndex() / pageLength) + 1;
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
        if (newIndex >= 0 && newIndex != getStartIndex()) {
            setPageFirstIndex(newIndex);
        }
    }

    /**
     * Get nb page
     * @return
     */
    public int getTotalAmountOfPages() {
        double pageLength = getPageLength();
        int pageCount = (int) Math.ceil(totalRecords / pageLength);
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
     * Set itemPerPageChangeListener.
     * @param listener
     */
    public void setItemPerPageChangeListener(ItemPerPageChangeListener listener) {
    	itemPerPageChangeListener = listener;
    }
    
    /**
     * Remove itemPerPageChangeListener.
     */
    public void removeItemPerPageChangeListener() {
    	itemPerPageChangeListener = null;
    }

    /**
     * @param alwaysRecalculateColumnWidths
     */
    public void setAlwaysRecalculateColumnWidths(boolean alwaysRecalculateColumnWidths) {
        this.alwaysRecalculateColumnWidths = alwaysRecalculateColumnWidths;
    }

    /**
     * Refresh page
     * @param forceReloadData
     */
    public void refresh() {
    	totalRecords = getTotalRecords();
    	setPageFirstIndex(0, true);
    }
        
    /**
     * Build indexed container
     * @return
     */
	private IndexedContainer buildIndexedContainer() {
		return dataProvider.getIndexedContainer(getStartIndex(), getPageLength());
    }
	
	/**
	 * @return
	 */
	private long getTotalRecords() {
		return dataProvider.getTotalRecords();
	}

	/**
	 * @return the pagedDefinition
	 */
	public PagedDefinition<T> getPagedDefinition() {
		return dataProvider.getPagedDefinition();
	}

	/**
	 * @return the dataProvider
	 */
	public PagedDataProvider<T> getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(PagedDataProvider<T> dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the currentContainer
	 */
	public Container getCurrentContainer() {
		return currentContainer;
	}

	/**
	 * @param currentContainer the currentContainer to set
	 */
	public void setCurrentContainer(Container currentContainer) {
		this.currentContainer = currentContainer;
	}
	
	
}