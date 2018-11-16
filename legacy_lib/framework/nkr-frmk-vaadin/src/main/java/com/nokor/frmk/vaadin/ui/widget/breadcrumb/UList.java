package com.nokor.frmk.vaadin.ui.widget.breadcrumb;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;

/**
 * @author ly.youhort
 */
public class UList extends AbstractLayout {

	private static final long serialVersionUID = -8270323002362538783L;
	
	protected LinkedList<Component> components = new LinkedList<Component>();

	/**
	 * @param c
	 */
	public void addComponent(Component c) {
		ListItem li = (ListItem) c;

		components.add(li);
		try {
			super.addComponent(li);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(li);
			throw e;
		}
	}

	/**
	 * @param c
	 */
	public void addComponentAsFirst(Component c) {
		ListItem li = (ListItem) c;
		components.addFirst(li);
		try {
			super.addComponent(li);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(li);
			throw e;
		}
	}

	/**
	 * @param c
	 * @param index
	 */
	public void addComponent(Component c, int index) {
		ListItem li = (ListItem) c;
		components.add(index, li);
		try {
			super.addComponent(li);
			markAsDirty();
		} catch (IllegalArgumentException e) {
			components.remove(li);
			throw e;
		}
	}

	/**
	 * @param c
	 */
	public void removeComponent(Component c) {
		ListItem li = (ListItem) c;
		components.remove(li);
		super.removeComponent(li);
		markAsDirty();
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
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
		ListItem oldli = (ListItem) oldComponent;
		ListItem newli = (ListItem) newComponent;

		// Gets the locations
		int oldLocation = -1;
		int newLocation = -1;
		int location = 0;
		for (final Iterator<Component> i = components.iterator(); i.hasNext();) {
			final Component component = i.next();

			if (component == oldli) {
				oldLocation = location;
			}
			if (component == newli) {
				newLocation = location;
			}

			location++;
		}

		if (oldLocation == -1) {
			addComponent(newli);
		} else if (newLocation == -1) {
			removeComponent(oldli);
			addComponent(newli, oldLocation);
		} else {
			if (oldLocation > newLocation) {
				components.remove(oldli);
				components.add(newLocation, oldli);
				components.remove(newli);
				components.add(oldLocation, newli);
			} else {
				components.remove(newli);
				components.add(oldLocation, newli);
				components.remove(oldli);
				components.add(newLocation, oldli);
			}

			markAsDirty();
		}
	}

	/**
	 * @return
	 */
	public Component getFirstComponent() {
		return components.getFirst();
	}

	/**
	 * @return
	 */
	public Component getLastComponent() {
		return components.getLast();
	}

	

}
