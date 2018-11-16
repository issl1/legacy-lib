package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;


/**
 * @author uhout.cheng
 */
public class ResourcePanel extends Window implements CloseListener {
	
	/**
	 */
	private static final long serialVersionUID = -4043167189720678528L;
	
	private TabSheet tabResource;
	
	private ResourceDetailPanel detailPanel;
	private ResourceDebtLevelTablePanel debtLevelTablePanel;
	private StaffAreaPanel staffAreaPanel;
	
	private String profileCode;
	
	private Listener listener = null;
	
	public interface Listener extends Serializable {
        void onClose(ResourcePanel dialog);
    }
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static ResourcePanel show(final String profileCode, final Listener listener) {    	
    	ResourcePanel resourcePanel = new ResourcePanel(profileCode);
    	resourcePanel.listener = listener;
        return resourcePanel;
    }
	
	/**
	 * 
	 * @param profileCode
	 */
	public ResourcePanel(String profileCode) {
		this.profileCode = profileCode;
		setModal(true);
		setCaption(I18N.message("resource"));
		tabResource = new TabSheet();
		detailPanel = new ResourceDetailPanel();
		debtLevelTablePanel = new ResourceDebtLevelTablePanel(profileCode);
		staffAreaPanel = new StaffAreaPanel();
		
		tabResource.addTab(detailPanel, I18N.message("detail"));
		if (IProfileCode.COL_PHO_STAFF.equals(profileCode) || IProfileCode.COL_FIE_STAFF.equals(profileCode) 
				|| IProfileCode.COL_INS_STAFF.equals(profileCode) || IProfileCode.COL_OA_STAFF.equals(profileCode)) {
			tabResource.addTab(debtLevelTablePanel, I18N.message("debt.level"));
		}
		if (IProfileCode.COL_FIE_STAFF.equals(profileCode)
				|| IProfileCode.COL_INS_STAFF.equals(profileCode) || IProfileCode.COL_OA_STAFF.equals(profileCode)) {
			tabResource.addTab(staffAreaPanel, I18N.message("area"));
		}
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(tabResource);
		setWidth("905px");
		setHeight("520px");
		setContent(layout);
		center();
		
		addCloseListener(this);
	}
	
	/**
	 * @param secUser
	 */
	public void assignValues(SecUser secUser) {
		detailPanel.assignValues(secUser);
		if (IProfileCode.COL_PHO_STAFF.equals(profileCode) || IProfileCode.COL_FIE_STAFF.equals(profileCode)
				|| IProfileCode.COL_INS_STAFF.equals(profileCode) || IProfileCode.COL_OA_STAFF.equals(profileCode)) {
			debtLevelTablePanel.assignValues(secUser);
		}
		if (IProfileCode.COL_FIE_STAFF.equals(profileCode)
				|| IProfileCode.COL_INS_STAFF.equals(profileCode) || IProfileCode.COL_OA_STAFF.equals(profileCode)) {
			staffAreaPanel.assignValues(secUser);
		}
	}

	@Override
	public void windowClose(CloseEvent e) {
		if (listener != null) {
			listener.onClose(ResourcePanel.this);
		}
		UI.getCurrent().removeWindow(this);
	}
}
