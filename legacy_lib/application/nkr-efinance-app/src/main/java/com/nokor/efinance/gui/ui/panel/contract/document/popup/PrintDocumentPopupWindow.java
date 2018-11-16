package com.nokor.efinance.gui.ui.panel.contract.document.popup;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.EDocumentStatus;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * Order to print document pop up window
 * @author uhout.cheng
 */
public class PrintDocumentPopupWindow extends Window {

	/** */
	private static final long serialVersionUID = -5424051738567651775L;

	private ERefDataComboBox<EDocumentStatus> cbxDocumentType;
	private ERefDataComboBox<EApplicantType> cbxApplicantType;
	private ERefDataComboBox<ETypeAddress> cbxAddressType;
	private AutoDateField dfDate;
	private CheckBox cbToday;
	
	/**
	 * 
	 * @param loanSummaryPanel
	 * @param contract
	 */
	public PrintDocumentPopupWindow() {
		setCaption(I18N.message("order.print"));
		setModal(true);
		setResizable(false);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		VerticalLayout contentLayout = new VerticalLayout(); 
	        
		Button btnPrint = new NativeButton(I18N.message("print"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 3836630068026534596L;

			public void buttonClick(ClickEvent event) {
				
			}
		});
		btnPrint.setIcon(FontAwesome.PRINT);
	     
		Button btnClose = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 7283738959546669383L;

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnClose.setIcon(FontAwesome.BAN);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnPrint);
		navigationPanel.addButton(btnClose);

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(getPanel());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getPanel() {	
		cbxDocumentType = new ERefDataComboBox<>(I18N.message("type"), EDocumentStatus.values());
		cbxDocumentType.setWidth(150, Unit.PIXELS);
		cbxApplicantType = new ERefDataComboBox<>(I18N.message("receiver"), EApplicantType.values());
		cbxApplicantType.setWidth(150, Unit.PIXELS);
		cbxAddressType = new ERefDataComboBox<>(I18N.message("address"), ETypeAddress.values());
		cbxAddressType.setWidth(150, Unit.PIXELS);
		dfDate = ComponentFactory.getAutoDateField("date", false);
		cbToday = new CheckBox(I18N.message("today"));
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		frmLayout.addComponent(cbxDocumentType);
		frmLayout.addComponent(cbxApplicantType);
		frmLayout.addComponent(cbxAddressType);
		frmLayout.addComponent(dfDate);
		frmLayout.addComponent(cbToday);
		cbToday.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -1251460461496843056L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().equals(cbToday)) {
					if (Boolean.TRUE.equals(cbToday.getValue())) {
						dfDate.setValue(DateUtils.today());
					} else {
						dfDate.setValue(null);
					}
				}
			}
		});
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(false, false, true, false), false);
		horLayout.addComponent(frmLayout);
		
		Panel panel = new Panel(new VerticalLayout(horLayout));
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		return panel;
	}
	
}
