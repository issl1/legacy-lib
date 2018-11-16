package com.nokor.efinance.gui.ui.panel.contract.summary.popup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Summary flag pop up panel
 * @author uhout.cheng
 */
public class SummaryFlagPopupPanel extends Window implements FinServicesHelper, ValueChangeListener, CloseListener {

	/** */
	private static final long serialVersionUID = 3058294346726318425L;

	private ERefDataComboBox<EFlag> cbxFlag;
	private ERefDataComboBox<ELocation> cbxLocation;
	private TextField txtOtherLocationValue;
	private AutoDateField dfDate;
	private ComboBox cbHour;
	private ComboBox cbMinute;
	private TextArea txtComment;
	private List<String> errors;
	private VerticalLayout messagePanel;
	private Contract contract;
	
	private VerticalLayout attributeLayout;
	private VerticalLayout keyInDetailLayout;
	private VerticalLayout contentLayout;
	
	private Listener listener = null;
	
	/**
	 * 
	 * @param contract
	 */
	public SummaryFlagPopupPanel(Contract contract) {
		this.contract = contract;
		setCaption(I18N.message("flag"));
		setModal(true);
		setResizable(false);
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		contentLayout = new VerticalLayout();
		cbxFlag = new ERefDataComboBox<>(I18N.message("flag"), EFlag.values());
		cbxFlag.setRequired(true);
		cbxFlag.setWidth(170, Unit.PIXELS);
		cbxFlag.addValueChangeListener(this);
		cbxLocation = new ERefDataComboBox<ELocation>(I18N.message("location"), ELocation.values());
		cbxLocation.setWidth(170, Unit.PIXELS);
		cbxLocation.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 6271351166170472122L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxLocation.getSelectedEntity() != null) {
					txtOtherLocationValue.setVisible(cbxLocation.getSelectedEntity().equals(ELocation.getByCode("OTHER")));
				} else {
					txtOtherLocationValue.setVisible(false);
				}
			}
		});
		txtOtherLocationValue = ComponentFactory.getTextField("other.location.value", false, 100, 170);
		txtOtherLocationValue.setVisible(false);
		dfDate = ComponentFactory.getAutoDateField("date", false);
		
		cbHour = new ComboBox(I18N.message("hour"));
		cbMinute = new ComboBox(I18N.message("minute"));
		cbHour.setWidth(50, Unit.PIXELS);
		cbMinute.setWidth(50, Unit.PIXELS);
		for (int i = 1; i < 25; i++) {
			cbHour.addItem(i);
		}
		for (int i = 0; i < 60; i++) {
			cbMinute.addItem(i);
		}
		
		txtComment = ComponentFactory.getTextArea("comment", false, 300, 80);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		Button btnFlag = new NativeButton(I18N.message("flag"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4805149203226797243L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					ContractFlag contractFlag = EntityFactory.createInstance(ContractFlag.class);
					contractFlag.setContract(contract);
					contractFlag.setFlag(cbxFlag.getSelectedEntity());
					if (cbxFlag.getSelectedEntity().equals(EFlag.getByCode("F001"))) {						
						contractFlag.setComment(txtComment.getValue());
					} else {
						contractFlag.setLocation(cbxLocation.getSelectedEntity());
						contractFlag.setOtherLocationValue(txtOtherLocationValue.getValue());
						contractFlag.setDate(getSelectedDate());
					}
					ENTITY_SRV.saveOrUpdate(contractFlag);
					close();
				} else {
					displayErrors();
				}
			}
		});
		btnFlag.setIcon(FontAwesome.FLAG);
		
		Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(SummaryFlagPopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(SummaryFlagPopupPanel.this);
              }
            }
        };
        
        btnFlag.addClickListener(cb);
	     
		Button btnClose = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 4071048316094547146L;

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnClose.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnFlag);
		navigationPanel.addButton(btnClose);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(getVerLayout());
	    
		setContent(contentLayout);
	}
	
	/**
	 * Get Selected Date
	 * @return
	 */
	private Date getSelectedDate() {
		if (dfDate.getValue() == null) {
			return null;
		}
		int hour = cbHour.getValue() != null ? (int) cbHour.getValue() : 0;
		int minute = cbMinute.getValue() != null ? (int) cbMinute.getValue() : 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dfDate.getValue());
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getVerLayout() {
		HorizontalLayout timeLayout = new HorizontalLayout();
		timeLayout.setSpacing(true);
		timeLayout.setCaption(I18N.message("time"));
		timeLayout.addComponent(cbHour);
		timeLayout.addComponent(cbMinute);
		
		attributeLayout = new VerticalLayout();
		attributeLayout.setCaptionAsHtml(true);
		attributeLayout.setCaption("<b>" + I18N.message("attribute") + "</b>");
		attributeLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(cbxLocation, false));
		attributeLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtOtherLocationValue, false));
		attributeLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(dfDate, false));
		attributeLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(timeLayout, false));
		
		keyInDetailLayout = new VerticalLayout();
		keyInDetailLayout.addComponent(txtComment);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(cbxFlag, false));
		verLayout.addComponent(contentLayout);
		return verLayout;
	}
	
	/**
	 * Validate the asset model form
	 * @return
	 */
	private boolean validate() {
		removeErrorComponent();
		if (cbxFlag.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("flag") }));
		} else {
			if (!cbxFlag.getSelectedEntity().equals(EFlag.getByCode("F001")) &&
					(cbHour.getValue() != null || cbMinute.getValue() != null) &&
					dfDate.getValue() == null) {
				errors.add(I18N.message("field.required.1", new String[] { I18N.message("date") }));
			}
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	private void removeErrorComponent() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}
	
	/**
	 * Display Errors
	 */
	private void displayErrors() {
		this.messagePanel.removeAllComponents();
		if (!(this.errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		contentLayout.removeAllComponents();
		if (cbxFlag.getSelectedEntity() != null) {
			if (cbxFlag.getSelectedEntity().equals(EFlag.getByCode("F001"))) {	// Lost Motorbike
				contentLayout.addComponent(keyInDetailLayout);
			} else {
				contentLayout.addComponent(attributeLayout);
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	public interface Listener extends Serializable {
        void onClose(SummaryFlagPopupPanel dialog);
    }
	
	/**
	 * 
	 * @param contract
	 * @param listener
	 * @return
	 */
	 public static SummaryFlagPopupPanel show(final Contract contract, final Listener listener) {    	
		 SummaryFlagPopupPanel summaryFlagPopupPanel = new SummaryFlagPopupPanel(contract);
		 summaryFlagPopupPanel.listener = listener;
	    	UI.getCurrent().addWindow(summaryFlagPopupPanel);
	        return summaryFlagPopupPanel;
	    }
	 
	 /**
	  * 
	  */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
}
