package com.nokor.frmk.vaadin.ui.panel;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.CloseClickButton;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.SaveClickButton;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.CloseClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Abstract Form Popup Panel
 * @author phirun.kong
 *
 */
public abstract class AbstractFormPopupPanel<T extends Entity> extends Window implements SaveClickListener, CloseClickListener, AppServicesHelper {

    /**	 */
	private static final long serialVersionUID = -8745210345960313267L;
	
	private Window winAddService;
    private AbstractFormTablePanel<T> panel;
    
    protected Long mainEntityId; // Ex. Employee ID
    protected Long entityId;	// Ex. EmpOvertime ID
    
    public void init(AbstractFormTablePanel<T> panel, String caption, int width, int height) {
    	this.panel = panel;
    	
    	setModal(true);
        winAddService = new Window(caption);
        winAddService.setModal(true);
        
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);

        contentLayout.addComponent(addNavigation());
        contentLayout.addComponent(createForm());
        
        winAddService.setContent(contentLayout);
        winAddService.setWidth(width, Unit.PIXELS);
        winAddService.setHeight(height, Unit.PIXELS);
        UI.getCurrent().addWindow(winAddService);
    }
    
	/**
     * 
     * @return
     */
    public Component createDefaultNavigation() {
    	Button btnSave = new SaveClickButton(this);
        Button btnCancel = new CloseClickButton(this);

        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addButton(btnSave);
        navigationPanel.addButton(btnCancel);
        return navigationPanel;
    }
    
    /**
     * 
     */
    public void reset() {
    	mainEntityId = null;
    	entityId = null;
    	clearValue();
    }
    
	@Override
	public void saveButtonClick(ClickEvent event) {
        if (validate()) {
        	saveButtonClick();
            panel.refreshTable();
            winAddService.close();
        } else {
            MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"), MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.show();
        }
	}
	
	@Override
	public void closeButtonClick(ClickEvent event) {
		winAddService.close();
	}
	
	/**
	 * @param mainEntityId the mainEntityId to set
	 */
	public void setMainEntityId(Long mainEntityId) {
		this.mainEntityId = mainEntityId;
	}

	protected abstract Component createForm();
	protected abstract void clearValue();
	protected abstract void assignValues(T entity);
    protected abstract T buildEntityFromControls();
    protected abstract boolean validate();
    protected abstract void saveButtonClick();
    protected abstract Component addNavigation();
	
}
