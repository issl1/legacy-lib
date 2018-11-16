package com.nokor.efinance.gui.ui.panel.dashboard.task;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.collab.project.model.ETaskType;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Task Holder Panel
 * @author bunlong.taing
 */
public class TaskHolderPanel extends VerticalLayout {
	/** */
	private static final long serialVersionUID = -5923506026743954162L;
	
	private TabSheet tabSheet;
	private List<TaskFormPanel> formPanels;
	
	/**
	 * 
	 */
	public TaskHolderPanel(String caption, boolean isMyTask) {
		setSizeFull();
		setMargin(true);
		setCaption(I18N.message(caption));
		createForm(isMyTask);
	}
	
	/**
	 * 
	 */
	private void createForm(boolean isMyTask) {
		tabSheet = new TabSheet();
		formPanels = new ArrayList<TaskFormPanel>();
		
		for (ETaskType taskType : ETaskType.values()) {
			TaskFormPanel formPanel = new TaskFormPanel(taskType, isMyTask);
			tabSheet.addTab(formPanel);
			formPanels.add(formPanel);
		}
		
		addComponent(tabSheet);
	}

}
