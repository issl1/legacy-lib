package com.nokor.frmk.vaadin.ui.panel;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Field;

/**
 * Basic Vaadin form for all forms in the application. Contains basic functionality inherited by all forms
 *
 */
public abstract class AbstractFieldGroup<T extends AbstractLayout> extends FieldGroup {
  
	private static final long serialVersionUID = 3926520614842628246L;
	
	private T content;

    /**
     * Responsible for form initialization
     */
    public abstract void init();

    /**
     * Gets field group content.
     *
     * @return field group content
     */
    public T getContent() {
        return content;
    }

    /**
     * Sets field group content.
     *
     * @param content - field group content
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Sets component error to field group layout
     *
     * @param localizedMessage - localized message
     */
    public void setComponentError(String localizedMessage) {
        getContent().setComponentError(new UserError(localizedMessage));
    }

  
    /**
     * Discard changes and unbinds all bound fields
     */
    public void unbindAll() {
        final Collection<Field<?>> fields = new HashSet<Field<?>>(getFields());
        for (Field<?> field : fields) {
            unbind(field);
        }
    }
}
