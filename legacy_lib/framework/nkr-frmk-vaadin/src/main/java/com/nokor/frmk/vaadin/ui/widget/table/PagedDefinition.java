package com.nokor.frmk.vaadin.ui.widget.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.vaadin.ui.Table.Align;

/**
 * Default implementation of Query Definition. 
 * Stores the property information query to simple Map structure.
 * @author youhort.ly
 */
public class PagedDefinition<T extends Entity> implements Serializable {
    
	private static final long serialVersionUID = -8488417064819867821L;
	
	private RowRenderer rowRenderer;
	private List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
	    
    /** Base criteria */
	private BaseRestrictions<T> restrictions;
    
    
    /**
     * Constructor which sets the batch size.
     */
    public PagedDefinition(final BaseRestrictions<T> restrictions) {
        this.restrictions = restrictions;
    }

	/**
	 * @return the restrictions
	 */
	public BaseRestrictions<T> getRestrictions() {
		return restrictions;
	}

	/**
	 * @param restrictions the restrictions to set
	 */
	public void setRestrictions(BaseRestrictions<T> restrictions) {
		this.restrictions = restrictions;
	}
	
    /**
	 * @return the rowRenderer
	 */
	public RowRenderer getRowRenderer() {
		return rowRenderer;
	}

	/**
	 * @param rowRenderer the rowRenderer to set
	 */
	public void setRowRenderer(RowRenderer rowRenderer) {
		this.rowRenderer = rowRenderer;
	}

	/**
	 * @param columnDefinitions the columnDefinitions to set
	 */
	public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
	}

	/**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param propertyCaption caption of the property.
     * @param width width of the property.
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, int propertyWidth) {
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, null, true, true, Align.LEFT, propertyWidth, true, null);
    }
    
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param propertyCaption caption of the property.
     * @param width width of the property.
     * @param visible visible of the property
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, int propertyWidth, boolean visible) {
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, null, true, true, Align.LEFT, propertyWidth, visible, null);
    }
       
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param propertyCaption caption of the property.
     * @param width width of the property.
     * @param alignment
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, 
    		final Align propertyAlignment, int propertyWidth) {
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth, true, null);
    }
    
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param propertyCaption caption of the property.
     * @param width width of the property.
     * @param alignment
     * @param visible visible of the property
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, 
    		final Align propertyAlignment, int propertyWidth, boolean visible) {
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth, visible, null);
    }
    
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param propertyCaption caption of the property.
     * @param width width of the property.
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, final Align propertyAlignment, 
    		int propertyWidth, ColumnRenderer columnRenderer) {
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth, true, columnRenderer);
    }
    
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param defaultValue Default value of the property.
     * @param propertyCaption caption of the property.
     * @param readOnly True if property is read only.
     * @param sortable True if property is sortable.
     * @param width width of the property.
     * @param alignment
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, final Object defaultValue,
            final boolean readOnly, final boolean sortable, final Align propertyAlignment, int propertyWidth) {
       
    	return addColumnDefinition(propertyId, propertyCaption, propertyType, defaultValue, 
    			readOnly, sortable, propertyAlignment, propertyWidth, true, null);
    }
    
    /**
     * Adds property.
     * @param propertyId ID of the property.
     * @param propertyType Type of the property.
     * @param defaultValue Default value of the property.
     * @param propertyCaption caption of the property.
     * @param readOnly True if property is read only.
     * @param sortable True if property is sortable.
     * @param width width of the property.
     * @param alignment
     * @param columnRenderer Column Renderer
     */
    public final ColumnDefinition addColumnDefinition(final String propertyId, String propertyCaption, final Class<?> propertyType, final Object defaultValue,
            final boolean readOnly, final boolean sortable, final Align propertyAlignment, int propertyWidth, boolean visible, ColumnRenderer columnRenderer) {
       
    	ColumnDefinition columnDefinition = new ColumnDefinition(propertyId, propertyCaption, propertyType, defaultValue, 
    			readOnly, sortable, propertyAlignment, propertyWidth, columnRenderer, null);
    	columnDefinition.setVisible(visible);
    	columnDefinitions.add(columnDefinition);
    	return columnDefinition;
    }
    
    /**
     * @return the propertyIds
     */
    public final Collection<String> getPropertyIds() {
    	List<String> propertyIds = new ArrayList<String>();
    	for (ColumnDefinition columnDefinition : columnDefinitions) {
    		propertyIds.add(columnDefinition.getPropertyId());
    	}
        return Collections.unmodifiableCollection(propertyIds);
    }

    /**
     * List of sortable property IDs.
     * @return the sortablePropertyIds
     */
    public final Collection<?> getSortablePropertyIds() {
        final List<Object> sortablePropertyIds = new ArrayList<Object>();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
        	if (columnDefinition.isSortable()) {
        		sortablePropertyIds.add(columnDefinition.getPropertyId());
        	}
    	}
        return sortablePropertyIds;
    }
    
    /**
     * List of sortable property IDs.
     * @return the sortablePropertyIds
     */
    public final Collection<?> getVisiblePropertyIds() {
        final List<Object> visiblePropertyIds = new ArrayList<Object>();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
        	if (columnDefinition.isVisible()) {
        		visiblePropertyIds.add(columnDefinition.getPropertyId());
        	}
    	}
        return visiblePropertyIds;
    }

    /**
     * Removes property.
     * @param propertyId ID identifying the property.
     */
    public final void removeProperty(final Object propertyId) {
        columnDefinitions.remove(getColumnDefinition(propertyId));
    }
    
    /**
     * Get column definition form the propertyId
     * @param propertyId
     * @return
     */
    public ColumnDefinition getColumnDefinition(final Object propertyId) {
    	for (ColumnDefinition columnDefinition : columnDefinitions) {
    		if (columnDefinition.getPropertyId().equals(propertyId)) {
    			return columnDefinition;
    		}
    	}
    	return null;
    }
    
    /**
     * 
     * @param index
     * @return
     */
    public ColumnDefinition getColumnDefinition(final int index) {
    	return columnDefinitions.get(index);
    }

	/**
	 * @return the columnDefinitions
	 */
	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}
    
}
