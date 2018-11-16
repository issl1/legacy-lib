package com.nokor.frmk.vaadin.ui.widget.table;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * @author ly.youhort
 *
 */
public class PagedTableContainer implements Container, Container.Indexed, Container.Sortable {
    private static final long serialVersionUID = -2134233618583099046L;

    private final Container.Indexed container;
    private int pageLength = 25;
    private int startIndex = 0;

    /**
     * @param container
     */
    public PagedTableContainer(Container.Indexed container) {
        this.container = container;
    }

    /**
     * Get container
     * @return
     */
    public Container.Indexed getContainer() {
        return container;
    }

    /**
     * Get page length
     * @return
     */
    public int getPageLength() {
        return pageLength;
    }

    /**
     * Set page length
     * @param pageLength
     */
    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    /**
     * Get start index
     * @return
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Set start index
     * @param startIndex
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

 

    @Override
    public int size() {
        int rowsLeft = container.size() - startIndex;
        if (rowsLeft > pageLength) {
            return pageLength;
        } else {
            return rowsLeft;
        }
    }

    /**
     * Get real size
     * @return
     */
    public int getRealSize() {
        return container.size();
    }

    /**
     * Get id by index
     */
    public Object getIdByIndex(int index) {
        return container.getIdByIndex(index + startIndex);
    }


    @Override
    public Item getItem(Object itemId) {
        return container.getItem(itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return container.getContainerPropertyIds();
    }

    @Override
    public Collection<?> getItemIds() {
        return container.getItemIds();
    }

	@Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return container.getContainerProperty(itemId, propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return container.getType(propertyId);
    }

    @Override
    public boolean containsId(Object itemId) {
        return container.containsId(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        return container.addItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        return container.addItem();
    }

    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        return container.removeItem(itemId);
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        return container.addContainerProperty(propertyId, type, defaultValue);
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        return container.removeContainerProperty(propertyId);
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        return container.removeAllItems();
    }

    /**
     * Next item id
     * @param itemId
     * @return
     */
    public Object nextItemId(Object itemId) {
        return container.nextItemId(itemId);
    }

    /**
     * Previous item id
     * @param itemId
     * @return
     */
    public Object prevItemId(Object itemId) {
        return container.prevItemId(itemId);
    }

    /**
     * Get first item id
     * @return
     */
    public Object firstItemId() {
        return container.firstItemId();
    }

    /**
     * Get last item id
     * @return
     */
    public Object lastItemId() {
        return container.lastItemId();
    }

    /**
     * @param itemId
     * @return true if first id
     */
    public boolean isFirstId(Object itemId) {
        return container.isFirstId(itemId);
    }

    /**
     * @param itemId
     * @return true is last id
     */
    public boolean isLastId(Object itemId) {
        return container.isLastId(itemId);
    }

    /**
     * Add item after
     * @param previousItemId
     * @return
     */
    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        return container.addItemAfter(previousItemId);
    }

    /**
     * Add item after
     * @param previousItemId
     * @param newItemId
     * @return
     */
    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        return container.addItemAfter(previousItemId, newItemId);
    }

    /**
     * Index of id
     * @param itemId
     * @return
     */
    public int indexOfId(Object itemId) {
        return container.indexOfId(itemId);
    }
    
    /**
     * Add item at
     * @param index
     * @return
     */
    public Object addItemAt(int index) throws UnsupportedOperationException {
        return container.addItemAt(index);
    }

    /**
     * Add item at
     * @param index
     * @param newItemId
     * @return
     */
    public Item addItemAt(int index, Object newItemId)
            throws UnsupportedOperationException {
        return container.addItemAt(index, newItemId);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        if (container instanceof Container.Sortable) {
            ((Container.Sortable) container).sort(propertyId, ascending);
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (container instanceof Container.Sortable) {
            return ((Container.Sortable) container)
                    .getSortableContainerPropertyIds();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<?> getItemIds(final int startIndex, final int numberOfItems) {
        return container.getItemIds(this.startIndex, numberOfItems);
    }
}