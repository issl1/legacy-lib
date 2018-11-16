package com.nokor.frmk.vaadin.ui.panel;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.factory.EntityTableFactory;
import com.vaadin.ui.TabSheet;


/**
 * Entity tab sheet
 * @author ly.youhort
 */
public class EntityTabSheet extends TabSheet {
	private static final long serialVersionUID = -5068755056780262041L;
	    
	/**
	 * 
	 * @param title
	 * @param entityClass
	 * @param entityTableFactory
	 * @param firstLoad
	 */
    public EntityTabSheet(final String title, final Class<? extends Entity> entityClass, final EntityTableFactory entityTableFactory, final boolean firstLoad) {
    	setSizeFull();
    	super.removeAllComponents();
    	addTab(entityTableFactory.create(entityClass, firstLoad), title);
    }
}
