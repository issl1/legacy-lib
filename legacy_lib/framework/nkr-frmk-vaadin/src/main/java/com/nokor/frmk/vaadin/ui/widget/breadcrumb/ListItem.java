package com.nokor.frmk.vaadin.ui.widget.breadcrumb;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;

/**
 * @author ly.youhort
 */
public class ListItem extends AbstractLayout {

	private static final long serialVersionUID = -3012572637508258633L;
	
	protected LinkedList<Component> components = new LinkedList<Component>();

	/**
	 * Add a component
	 * @param c
	 */
	public void addComponent(Component c) {

		components.add(c);
		try {
			super.addComponent(c);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(c);
			throw e;
		}
	}

	/**
	 * Add component at first index
	 * @param c
	 */
	public void addComponentAsFirst(Component c) {
		components.addFirst(c);
		try {
			super.addComponent(c);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(c);
			throw e;
		}
	}

	/**
	 * @param c
	 * @param index
	 */
	public void addComponent(Component c, int index) {
		components.add(index, c);
		try {
			super.addComponent(c);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(c);
			throw e;
		}
	}

	/**
	 * @param c
	 */
	public void removeComponent(Component c) {
		components.remove(c);
		super.removeComponent(c);
		markAsDirty();
	}

	/**
	 * @return
	 */
	public int getComponentCount() {
		return components.size();
	}


	/**
	 * @param oldComponent
	 * @param newComponent
	 */
	public void replaceComponent(Component oldComponent, Component newComponent) {
		// Gets the locations
		int oldLocation = -1;
		int newLocation = -1;
		int location = 0;
		for (final Iterator<Component> i = components.iterator(); i.hasNext();) {
			final Component component = i.next();

			if (component == oldComponent) {
				oldLocation = location;
			}
			if (component == newComponent) {
				newLocation = location;
			}
			location++;
		}

		if (oldLocation == -1) {
			addComponent(newComponent);
		} else if (newLocation == -1) {
			removeComponent(oldComponent);
			addComponent(newComponent, oldLocation);
		} else {
			if (oldLocation > newLocation) {
				components.remove(oldComponent);
				components.add(newLocation, oldComponent);
				components.remove(newComponent);
				components.add(oldLocation, newComponent);
			} else {
				components.remove(newComponent);
				components.add(oldLocation, newComponent);
				components.remove(oldComponent);
				components.add(newLocation, oldComponent);
			}
			markAsDirty();
		}
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}
}
