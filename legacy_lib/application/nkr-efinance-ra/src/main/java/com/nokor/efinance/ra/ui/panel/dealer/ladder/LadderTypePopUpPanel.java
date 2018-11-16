package com.nokor.efinance.ra.ui.panel.dealer.ladder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.dealer.model.LadderTypeAttribute;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Notes pop up panel
 * @author uhout.cheng
 */
public class LadderTypePopUpPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/** */
	private static final long serialVersionUID = 1032050276064909393L;

	private TextField txtFrom;
	private TextField txtTo;
	private TextField txtAmount;
	
	private Button btnSave;
	private Button btnClose;
	
	private Listener listener = null;
	
	private VerticalLayout messagePanel;
	
	private LadderType ladderType;
	private LadderTypeAttribute ladderTypeAttribute;
	
	private List<String> errors;
	
	public interface Listener extends Serializable {
        void onClose(LadderTypePopUpPanel dialog);
    }
	
	/**
     * @param parentWindow
     * @param listener
     */
    public static LadderTypePopUpPanel show(final LadderType ladderType, final Listener listener) {    	
    	LadderTypePopUpPanel ladderTypePopUpPanel = new LadderTypePopUpPanel(ladderType);
    	ladderTypePopUpPanel.listener = listener;
    	UI.getCurrent().addWindow(ladderTypePopUpPanel);
        return ladderTypePopUpPanel;
    }
    
	/**
	 * 
	 * @param contract
	 * @param notesTablePanel
	 */
	private LadderTypePopUpPanel(LadderType ladderType) {
		this.ladderType = ladderType;
		setCaption(I18N.message("ladder.type.attribute"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		init();
	}
	
	/**
	 */
	private void init() {
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.setIcon(FontAwesome.SAVE);
	     
		btnClose = new NativeButton(I18N.message("close"));
		btnClose.addClickListener(this);
		btnClose.setIcon(FontAwesome.TIMES);
    
        messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
        
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnClose);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(createForm());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param contractNote
	 */
	public void assignValues(LadderTypeAttribute ladderTypeAttribute) {
		this.ladderTypeAttribute = ladderTypeAttribute;
		if (ladderTypeAttribute != null) {
			txtFrom.setValue(AmountUtils.format(ladderTypeAttribute.getFrom()));
			txtTo.setValue(AmountUtils.format(ladderTypeAttribute.getTo()));
			txtAmount.setValue(AmountUtils.format(ladderTypeAttribute.getAmount()));
		} else {
			reset();
		}
	}
	
	/**
	 * @return
	 */
	private VerticalLayout createForm() {
		FormLayout formPanel = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		txtFrom = ComponentFactory.getTextField("from", false, 30, 150);
		txtTo = ComponentFactory.getTextField("to", false, 30, 150);
		txtAmount = ComponentFactory.getTextField("amount", false, 30, 150);
		
		formPanel.addComponent(txtFrom);
		formPanel.addComponent(txtTo);
		formPanel.addComponent(txtAmount);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		contentLayout.addComponent(formPanel);
		return contentLayout;
	}
	
	/**
	 * Validate 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;

		if (StringUtils.isEmpty(txtFrom.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("from") }));
		}
		
		if (StringUtils.isEmpty(txtAmount.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("amount") }));
		}
		
		if (!StringUtils.isEmpty(txtTo.getValue())) {
			if (NumberUtils.toDouble(txtFrom.getValue()) >= NumberUtils.toDouble(txtTo.getValue())) {
				errors.add(I18N.message("from.shoud.not.grater.than.to"));
			}
		}
		
		checkDoubleField(txtFrom, "from");
		checkDoubleField(txtTo, "to");
		checkDoubleField(txtAmount, "amount");
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				save();
				if (listener != null) {
                    listener.onClose(LadderTypePopUpPanel.this);
                }
                UI.getCurrent().removeWindow(LadderTypePopUpPanel.this);
			}
		} else if (event.getButton() == btnClose) {
			close();
		}
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (validate()) {
			if (ladderTypeAttribute == null) {
				ladderTypeAttribute = new LadderTypeAttribute();
				ladderTypeAttribute.setLadderType(ladderType);
			}
			ladderTypeAttribute.setFrom(Double.valueOf(txtFrom.getValue()));
			ladderTypeAttribute.setTo(Double.valueOf(txtTo.getValue()));
			ladderTypeAttribute.setAmount(Double.valueOf(txtAmount.getValue()));
			
			DEA_SRV.saveOrUpdate(ladderTypeAttribute);
			close();
		}
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		txtFrom.setValue("");
		txtTo.setValue("");
		txtAmount.setValue("");
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private boolean checkDoubleField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty((String) field.getValue())) {
			try {
				Double.parseDouble((String) field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1",
						new String[] { I18N.message(messageKey) }));
			}
		}
		return isValid;
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
}
