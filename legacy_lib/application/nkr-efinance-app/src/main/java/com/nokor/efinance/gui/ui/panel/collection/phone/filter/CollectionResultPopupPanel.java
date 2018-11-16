package com.nokor.efinance.gui.ui.panel.collection.phone.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionResultPopupPanel extends Window implements CloseListener, ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051137865964409476L;
	
	private EntityRefComboBox<EColResult> cbxResult;
	private TextArea txtRemark;	
	private Button btnAdd;
	
	private Listener listener = null;
	private VerticalLayout messagePanel;
	private List<String> errors;
	
	private Contract contract;
	private Logger logger;
	
	/***
	 * 
	 * @author buntha.chea
	 *
	 */
	public interface Listener extends Serializable {
		void onClose(CollectionResultPopupPanel collectionResultPopupPanel);
    }
	
	public CollectionResultPopupPanel(Contract contract) {
		this.contract = contract;
		logger = LoggerFactory.getLogger(CollectionResultPopupPanel.class);
		
		setCaption(I18N.message("result"));
		setModal(true);
		setWidth("400px");
		setHeight("250px");
		
		cbxResult = new EntityRefComboBox<>(I18N.message("result"));
		cbxResult.setRestrictions(new BaseRestrictions<>(EColResult.class));
		cbxResult.getRestrictions().addOrder(Order.asc("descEn"));
		cbxResult.setCaptionRenderer(new Function<EColResult, String>() {
			/**
			 * @see java.util.function.Function#apply(java.lang.Object)
			 */
			@Override
			public String apply(EColResult t) {
				return t.getCode() + " - " + (I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc());
			}
		});
		cbxResult.renderer();
		cbxResult.setWidth(150, Unit.PIXELS);
		txtRemark = ComponentFactory.getTextArea("remark", false, 160, 60);
		btnAdd = new NativeButton(I18N.message("add"), this);
		btnAdd.setIcon(FontAwesome.PLUS);
		
		Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(CollectionResultPopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(CollectionResultPopupPanel.this);
              }
            }
        };
		
        btnAdd.addClickListener(cb);
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");

		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		formLayout.addComponent(cbxResult);
		formLayout.addComponent(txtRemark);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		
		VerticalLayout resultLayout = new VerticalLayout();
		resultLayout.setSpacing(true);
		resultLayout.addComponent(navigationPanel);
		resultLayout.addComponent(messagePanel);
		resultLayout.addComponent(formLayout);
		
		setContent(resultLayout);
	}
	
	/**
	 * Validate Form 
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;
		
		if (cbxResult.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("result") }));
		}
		
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
	 * 
	 * @param listener
	 * @return
	 */
	public static CollectionResultPopupPanel show(Contract contract, final Listener listener) {    	
		CollectionResultPopupPanel collectionResultPopupPanel = new CollectionResultPopupPanel(contract);
	    collectionResultPopupPanel.listener = listener;
	    return collectionResultPopupPanel;
	}

	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			if (validate()) {
				try {
					CollectionHistory colHistory = CollectionHistory.createInstance();
					colHistory.setContract(contract);
					colHistory.setResult(cbxResult.getSelectedEntity());
					colHistory.setComment(txtRemark.getValue());
					colHistory.setOrigin(EColType.PHONE);
					COL_SRV.saveOrUpdate(colHistory);
					
					Collection collection = contract.getCollection();
					collection.setLastCollectionHistory(colHistory);
					COL_SRV.saveOrUpdate(collection);
					close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
}
