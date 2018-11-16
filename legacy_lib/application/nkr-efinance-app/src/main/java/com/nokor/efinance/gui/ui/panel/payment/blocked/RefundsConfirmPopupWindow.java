package com.nokor.efinance.gui.ui.panel.payment.blocked;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Refunds confirm window
 * @author uhout.cheng
 */
public class RefundsConfirmPopupWindow extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -1665801316106539714L;

	private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtBankName;
	private TextField txtBranch;
	private TextField txtAccountNO;
	private TextField txtAccountName;
	
	/**
	 * 
	 */
	public RefundsConfirmPopupWindow() {
		setModal(true);		
		setClosable(false);
		setResizable(false);
		setCaption(I18N.message("refunds.information"));
		
		cbxPaymentMethod = new ERefDataComboBox<EPaymentMethod>(I18N.message("payment.method"), EPaymentMethod.values());
		cbxPaymentMethod.setWidth(150, Unit.PIXELS);
		txtBankName = ComponentFactory.getTextField("bank.name", false, 150, 150);
		txtBranch = ComponentFactory.getTextField("branch", false, 150, 150);
		txtAccountNO = ComponentFactory.getTextField("account.no", false, 100, 150);
		txtAccountName = ComponentFactory.getTextField("account.name", false, 100, 150);
		
		Button btnconfirm = ComponentLayoutFactory.getDefaultButton("confirm", FontAwesome.CHECK, 70);
		Button btnCancel = ComponentLayoutFactory.getDefaultButton("cancel", FontAwesome.BAN, 70);
		
		btnconfirm.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 7739594794397710318L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -4352460646049685750L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		frmLayout.addComponent(cbxPaymentMethod);
		frmLayout.addComponent(txtBankName);
		frmLayout.addComponent(txtBranch);
		frmLayout.addComponent(txtAccountNO);
		frmLayout.addComponent(txtAccountName);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnconfirm);
		buttonLayout.addComponent(btnCancel);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, true, false), true);
		contentLayout.setSizeUndefined();
		contentLayout.addComponent(frmLayout);
		contentLayout.addComponent(buttonLayout);
		contentLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
        setContent(contentLayout);
	}

}
