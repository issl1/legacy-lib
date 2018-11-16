package com.nokor.efinance.ra.ui.panel.dealer.employee;

import java.io.Serializable;

import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Dealer Employee Contact Info Form Panel
 * @author bunlong.taing
 */
public class DealerEmployeeContactInfoFormPanel extends AbstractTabPanel implements ClickListener {
	/** */
	private static final long serialVersionUID = -6203353342069074616L;
	
	private Button btnBack;
	private Button btnSave;
	
	private BackListener backListener;
	private SaveListener saveListener;
	
	public ERefDataComboBox<ETypeAddress> cbxContactAddressType;
	public ERefDataComboBox<ETypeContactInfo> cbxContactInfoType;
	public TextField txtValue;
	public CheckBox cbPrimary;
	
	private DealerEmployeeContactInfo contactInfo;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#init()
	 */
	@Override
	public void init() {
		super.init();
		setMargin(false);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		btnBack = new NativeButton(I18N.message("back"));
		btnSave = new NativeButton(I18N.message("save"));

		btnBack.addClickListener(this);
		btnSave.addClickListener(this);
		
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		btnSave.setIcon(FontAwesome.SAVE);
		
		cbxContactAddressType = new ERefDataComboBox<ETypeAddress>(I18N.message("address.type"), ETypeAddress.values());
		cbxContactAddressType.setRequired(true);
		cbxContactAddressType.setVisible(false);
		cbxContactInfoType = new ERefDataComboBox<ETypeContactInfo>(I18N.message("contact.type"), ETypeContactInfo.values());
		cbxContactInfoType.setRequired(true);
		cbxContactInfoType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4625300506749531350L;
			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (ETypeContactInfo.LANDLINE.equals(cbxContactInfoType.getSelectedEntity())) {
					cbxContactAddressType.setVisible(true);
				} else {
					cbxContactAddressType.setVisible(false);
				}
			}
		});
		txtValue = ComponentFactory.getTextField("value", true, 100, 200);
		cbPrimary = new CheckBox(I18N.message("primary"));
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxContactInfoType);
		formLayout.addComponent(cbxContactAddressType);
		formLayout.addComponent(txtValue);
		formLayout.addComponent(cbPrimary);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeUndefined();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(formLayout);
		
		return content;
	}
	
	/**
	 * @param contactInfo
	 */
	public void assignValues(DealerEmployeeContactInfo contactInfo) {
		reset();
		this.contactInfo = contactInfo;
		cbxContactAddressType.setSelectedEntity(contactInfo.getTypeAddress());
		cbxContactInfoType.setSelectedEntity(contactInfo.getTypeInfo());
		txtValue.setValue(getDefaultString(contactInfo.getValue()));
		cbPrimary.setValue(contactInfo.isPrimary());
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnBack) {
			if (backListener != null) {
				backListener.onBack();
			}
		} else if (event.getButton() == btnSave) {
			if (validate()) {
				if (saveListener != null) {
					saveListener.onSaveSuccess(getEntity());
				}
			} else {
				displayErrorsPanel();
			}
		}
	}
	
	/**
	 * Validate
	 * @return
	 */
	public boolean validate() {
		super.reset();
		checkMandatorySelectField(cbxContactInfoType, "contact.type");
		if (cbxContactAddressType.isVisible()) {
			checkMandatorySelectField(cbxContactAddressType, "address.type");
		}
		checkMandatoryField(txtValue, "value");
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		removeErrorsPanel();
		cbxContactInfoType.setSelectedEntity(null);
		cbxContactAddressType.setSelectedEntity(null);
		txtValue.setValue("");
		cbPrimary.setValue(false);
	}

	/**
	 * @return the contactInfo
	 */
	private DealerEmployeeContactInfo getEntity() {
		contactInfo.setTypeInfo(cbxContactInfoType.getSelectedEntity());
		if (ETypeContactInfo.LANDLINE.equals(cbxContactInfoType.getSelectedEntity())) {
			contactInfo.setTypeAddress(cbxContactAddressType.getSelectedEntity());
		}
		contactInfo.setValue(txtValue.getValue());
		contactInfo.setPrimary(cbPrimary.getValue());
		return contactInfo;
	}

	/**
	 * @param saveListener the saveListener to set
	 */
	public void setSaveListener(SaveListener saveListener) {
		this.saveListener = saveListener;
	}
	
	/**
	 * @param backListener the backListener to set
	 */
	public void setBackListener(BackListener backListener) {
		this.backListener = backListener;
	}

	/**
	 * @author bunlong.taing
	 */
	public interface BackListener extends Serializable {
		/** */
		void onBack();
	}
	
	/**
	 * @author bunlong.taing
	 */
	public interface SaveListener extends Serializable {
		void onSaveSuccess(DealerEmployeeContactInfo contactInfo);
	}

}
