package com.nokor.frmk.vaadin.ui.widget.toolbar;

import java.util.Iterator;

import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.ExportXlsClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;

/**
 * Navigation panel
 * @author ly.youhort
 */
public class NavigationPanel extends HorizontalLayout {

	private static final long serialVersionUID = 4569579205201727904L;

	private HorizontalLayout navigationLayout = null;
	
	private AddClickButton addClickButton = null;
	private EditClickButton editClickButton = null;
	private DeleteClickButton deleteClickButton = null;
	private RefreshClickButton refreshClickButton = null;
	private PrintClickButton printClickButton = null;
	private ExportXlsClickButton exportXlsClickButton = null;
	private SaveClickButton saveClickButton = null;
		
	/**
	 * Navigation panel
	 */
	public NavigationPanel() {
		 addStyleName("sidebar");
         setWidth("100%");
         navigationLayout = new HorizontalLayout();
         navigationLayout.addStyleName("navigation");
         navigationLayout.addStyleName("no-vertical-drag-hints");
         navigationLayout.addStyleName("no-horizontal-drag-hints");
         addComponent(navigationLayout);
         setExpandRatio(navigationLayout, 1);
	}
	
	/**
	 * @param addClickListener
	 */
	public void addAddClickListener(AddClickListener addClickListener) {
		if (addClickButton == null) {
			addClickButton = new AddClickButton(addClickListener);
			addButton(addClickButton);
		}
	}
	
	/**
	 * @param editClickListener
	 */
	public void addEditClickListener(EditClickListener editClickListener) {
		if (editClickButton == null) {
			editClickButton = new EditClickButton(editClickListener);
			addButton(editClickButton);
		}
	}
	
	/**
	 * @param deleteClickListener
	 */
	public void addDeleteClickListener(DeleteClickListener deleteClickListener) {
		if (deleteClickButton == null) {
			deleteClickButton = new DeleteClickButton(deleteClickListener);
			addButton(deleteClickButton);
		}
	}
	
	/**
	 * @param refreshClickListener
	 */
	public void addRefreshClickListener(SearchClickListener refreshClickListener) {
		if (refreshClickButton == null) {
			refreshClickButton = new RefreshClickButton(refreshClickListener);
			addButton(refreshClickButton);
		}
	}
	
	/**
	 * @param printClickListener
	 */
	public void addPrintClickListener(PrintClickListener printClickListener) {
		if (printClickButton == null) {
			printClickButton = new PrintClickButton(printClickListener);
			addButton(printClickButton);
		}
	}
	
	/**
	 * @param exportXlsClickListener
	 */
	public void addExportXlsClickListener(ExportXlsClickListener exportXlsClickListener) {
		if (exportXlsClickButton == null) {
			exportXlsClickButton = new ExportXlsClickButton(exportXlsClickListener);
			addButton(exportXlsClickButton);
		}
	}
	
	/**
	 * @param saveClickListener
	 */
	public void addSaveClickListener(SaveClickListener saveClickListener) {
		if (saveClickButton == null) {
			saveClickButton = new SaveClickButton(saveClickListener);
			addButton(saveClickButton);
		}
	}
	
	
	/**
	 * Add a new navigation button
	 * @param button
	 * @return
	 */
	public void addButton(Component button) {
		navigationLayout.addComponent(button);
		
	}
			
	/**
	 * Clear style css
	 */
	protected void clearSelection() {
        for (Iterator<Component> it = navigationLayout.iterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                ((DragAndDropWrapper) next).iterator().next().removeStyleName("selected");
            }
        }
    }

	/**
	 * @return the navigationLayout
	 */
	public HorizontalLayout getNavigationLayout() {
		return navigationLayout;
	}

	/**
	 * @return the addClickButton
	 */
	public AddClickButton getAddClickButton() {
		return addClickButton;
	}

	/**
	 * @return the editClickButton
	 */
	public EditClickButton getEditClickButton() {
		return editClickButton;
	}

	/**
	 * @return the deleteClickButton
	 */
	public DeleteClickButton getDeleteClickButton() {
		return deleteClickButton;
	}

	/**
	 * @return the refreshClickButton
	 */
	public RefreshClickButton getRefreshClickButton() {
		return refreshClickButton;
	}

	/**
	 * @return the printClickButton
	 */
	public PrintClickButton getPrintClickButton() {
		return printClickButton;
	}

	/**
	 * @return the saveClickButton
	 */
	public SaveClickButton getSaveClickButton() {
		return saveClickButton;
	}
}
