package com.nokor.efinance.core.quotation.panel.comment;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Comment Dialog
 * @author ly.youhort
 */
public class CommentDialog extends Window {
	
    /** */
	private static final long serialVersionUID = 7837382885463743029L;
	
	private TextArea txtRemark;	
    private boolean confirmed = false;
		
	public interface Listener extends Serializable {
        void onClose(CommentDialog dialog);
    }
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static CommentDialog show(final Window parentWindow,
            final Listener listener) {
        return null; //show(parentWindow, null, null, null, null, listener);
    }
	
    /**
     * Did the user confirm the dialog.
     * 
     * @return
     */
    public final boolean isConfirmed() {
        return confirmed;
    }    
    
	/**
	 * @param confirmed the confirmed to set
	 */
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	/**
	 * @param quotation
	 * @param quotationStatus
	 * @param forManager
	 * @param onSaveListener
	 */
	public CommentDialog() {
		setModal(true);
		
		txtRemark = ComponentFactory.getTextArea("remark", false, 230, 100);
		
		Button btnNo = new Button(I18N.message("no"), new Button.ClickListener() {
			private static final long serialVersionUID = 6029290645077959168L;
			public void buttonClick(ClickEvent event) {
            }
        });
        btnNo.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));        
		Button btnYes = new Button(I18N.message("yes"), new Button.ClickListener() {		
			private static final long serialVersionUID = 29242940883886177L;
			public void buttonClick(ClickEvent event) {
            }
        });
		btnYes.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		horizontalLayout.addComponent(btnYes);
		horizontalLayout.addComponent(btnNo);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(new FormLayout(txtRemark));
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_RIGHT);
		setContent(verticalLayout);
	}
}
