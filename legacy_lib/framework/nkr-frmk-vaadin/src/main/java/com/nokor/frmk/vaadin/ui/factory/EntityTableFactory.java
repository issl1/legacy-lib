package com.nokor.frmk.vaadin.ui.factory;

import org.seuksa.frmk.model.entity.Entity;

import com.vaadin.ui.Component;

/**
 * Entity table factory
 * @author ly.youhort
 */
public interface EntityTableFactory {
    Component create(final Class <? extends Entity> entityClass, boolean firstLoad);
}
