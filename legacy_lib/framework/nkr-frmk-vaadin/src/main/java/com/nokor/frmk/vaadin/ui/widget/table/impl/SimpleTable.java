package com.nokor.frmk.vaadin.ui.widget.table.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;

/**
 * @author ly.youhort
 *
 */
public class SimpleTable<T extends Entity> extends Table {
    private static final long serialVersionUID = 6881455780158545828L;

    /**
     * @param columns
     */
    public SimpleTable(List<ColumnDefinition> columns) {
        this(null, columns);
    }
    
    /**
     * @param caption
     * @param columns
     */
    public SimpleTable(String caption, List<ColumnDefinition> columns) {
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
}