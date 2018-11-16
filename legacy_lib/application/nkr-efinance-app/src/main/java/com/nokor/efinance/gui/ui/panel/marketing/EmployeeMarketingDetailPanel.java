package com.nokor.efinance.gui.ui.panel.marketing;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.organization.Employee;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;


/**
 * @author uhout.cheng
 */
public class EmployeeMarketingDetailPanel extends Window implements CloseListener, SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = -7291120430793527957L;

	private TabSheet empMktTab;
	
//	private ResourceDetailPanel detailPanel;
	private EmployeeMarketingTeamTablePanel debtLevelTablePanel;
	private EmployeeMarketingAreaPanel empAreaPanel;
	private Employee employee;
	
	private Listener listener = null;
	
	public interface Listener extends Serializable {
        void onClose(EmployeeMarketingDetailPanel dialog);
    }
	
	/**
	 * Show a modal ConfirmDialog in a window.
	 * @param listener
	 * @return
	 */
    public static EmployeeMarketingDetailPanel show(final Listener listener) {    	
    	EmployeeMarketingDetailPanel resourcePanel = new EmployeeMarketingDetailPanel();
    	resourcePanel.listener = listener;
        return resourcePanel;
    }
	
	/**
	 * 
	 */
	public EmployeeMarketingDetailPanel() {
		setModal(true);
		setCaption(I18N.message("employee"));
		empMktTab = new TabSheet();
		empMktTab.addSelectedTabChangeListener(this);
//		detailPanel = new ResourceDetailPanel();
		debtLevelTablePanel = new EmployeeMarketingTeamTablePanel();
		empAreaPanel = new EmployeeMarketingAreaPanel();
		
//		tabResource.addTab(detailPanel, I18N.message("detail"));
		/*if (IProfileCode.COL_PHO_STAFF.equals(profileCode) || IProfileCode.COL_FIE_STAFF.equals(profileCode)) {
			tabResource.addTab(debtLevelTablePanel, I18N.message("debt.level"));
		}
		if (IProfileCode.COL_FIE_STAFF.equals(profileCode)) {
			tabResource.addTab(staffAreaPanel, I18N.message("area"));
		}
		
		
		
		if (IProfileCode.COL_INS_STAFF.equals(profileCode)) {
			tabResource.addTab(debtLevelTablePanel, I18N.message("debt.level"));
			tabResource.addTab(staffAreaPanel, I18N.message("area"));
		}*/
		
		empMktTab.addTab(debtLevelTablePanel, I18N.message("team"));
		empMktTab.addTab(empAreaPanel, I18N.message("area"));
		
		empMktTab.setSelectedTab(debtLevelTablePanel);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(empMktTab);
		setWidth("905px");
		setHeight("520px");
		setContent(layout);
		center();
		
		addCloseListener(this);
	}
	
	/**
	 * 
	 * @param employee
	 */
	public void assignValues(Employee employee) {
		/*detailPanel.assignValues(secUser);
		if (IProfileCode.COL_PHO_STAFF.equals(profileCode) || IProfileCode.COL_FIE_STAFF.equals(profileCode)) {
			debtLevelTablePanel.assignValues(secUser);
		}
		if (IProfileCode.COL_FIE_STAFF.equals(profileCode)) {
			staffAreaPanel.assignValues(secUser);
		}
		
		if (IProfileCode.COL_INS_STAFF.equals(profileCode)) {
			debtLevelTablePanel.assignValues(secUser);
			staffAreaPanel.assignValues(secUser);
		}*/
		this.employee = employee;
		debtLevelTablePanel.assignValues(employee);
	}

	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		if (listener != null) {
			listener.onClose(EmployeeMarketingDetailPanel.this);
		}
		UI.getCurrent().removeWindow(this);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (empMktTab.getSelectedTab().equals(empAreaPanel)) {
			empAreaPanel.assignValues(this.employee);
		}
	}
}
