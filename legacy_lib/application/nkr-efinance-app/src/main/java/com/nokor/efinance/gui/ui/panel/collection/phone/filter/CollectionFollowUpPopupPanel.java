package com.nokor.efinance.gui.ui.panel.collection.phone.filter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ColContactFollowUpHistoryPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
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
public class CollectionFollowUpPopupPanel extends Window implements ClickListener, ValueChangeListener, CloseListener, FinServicesHelper{

	private static final long serialVersionUID = 88175078327564740L;
	
	private static final String SCHEDULE = EColAction.SCHEDULE.getDescLocale();
	private static final String SEELATER = EColAction.SEELATER.getDescLocale();
	private static final String NOFURTHER = EColAction.NOFURTHER.getDescLocale();
	private static final String CANTPRO = EColAction.CANTPRO.getDescLocale();
	
	private OptionGroup optionGroupColAction;
	private TextArea txtRemark;
	private Button btnSave;
	
	private Listener listener = null;
	private Contract contract;

	private ColContactFollowUpHistoryPanel colPhoneContactHistoryPanel;
	
	/***
	 * 
	 * @author buntha.chea
	 *
	 */
	public interface Listener extends Serializable {
		void onClose(CollectionFollowUpPopupPanel dilog);
    }
	
	/**
	 * 
	 * @param contract
	 */
	public CollectionFollowUpPopupPanel(Contract contract) {
		this.contract = contract;
		
		setModal(true);
		setCaption(I18N.message("result"));
		init();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		colPhoneContactHistoryPanel = new ColContactFollowUpHistoryPanel(false);
		colPhoneContactHistoryPanel.assingValues(contract);
		colPhoneContactHistoryPanel.getBtnAdd().setVisible(false);
		
		HorizontalLayout horizontalLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		horizontalLayout.addComponent(colPhoneContactHistoryPanel);
		horizontalLayout.addComponent(getResultLayout());
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(horizontalLayout);
		
		setContent(mainLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getResultLayout() {
		Label lblRemarkTitle = ComponentFactory.getLabel(I18N.message("remark"));
		
		GridLayout gridLayoutTop = ComponentLayoutFactory.getGridLayoutDefaultMargin(3, 1);
		int iCol = 0;
		gridLayoutTop.addComponent(optionGroupColAction, iCol++, 0);
		
		GridLayout gridLayoutMiddle = ComponentLayoutFactory.getGridLayout(new MarginInfo(true, false, true, true), 5, 3);
		iCol = 0;
		gridLayoutMiddle.addComponent(lblRemarkTitle, iCol++, 0);
		gridLayoutMiddle.addComponent(txtRemark, iCol++, 0);
		
		gridLayoutMiddle.setComponentAlignment(lblRemarkTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout resultLayout = new VerticalLayout();
		resultLayout.setSpacing(true);
		resultLayout.setMargin(true);
		if (ProfileUtil.isCallCenter()) {
			resultLayout.addComponent(callCenterHistoryLayout());
		} else {
			
			resultLayout.addComponent(gridLayoutTop);
			resultLayout.addComponent(gridLayoutMiddle);
		}
		
		Panel mainPanel = new Panel(resultLayout);
		mainPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("result") + "</h3>");
		mainPanel.setCaptionAsHtml(true);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		mainLayout.addComponent(mainPanel);
		
		return mainLayout;
	}

	/**
	 * init
	 */
	private void init() {
		List<String> options = Arrays.asList(new String[] { SCHEDULE, SEELATER, NOFURTHER, CANTPRO });
		optionGroupColAction = ComponentLayoutFactory.getOptionGroup(options);
		optionGroupColAction.addValueChangeListener(this);
		txtRemark = ComponentFactory.getTextArea(false, 170, 50);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
            	 if (listener != null) {
                     listener.onClose(CollectionFollowUpPopupPanel.this);
                 }
                 UI.getCurrent().removeWindow(CollectionFollowUpPopupPanel.this);
            }
        };
        
        btnSave.addClickListener(cb);
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout callCenterHistoryLayout() {
		GridLayout callCenterHistoryLayout = new GridLayout(2, 3);
		callCenterHistoryLayout.setSpacing(true);
		callCenterHistoryLayout.addComponent(optionGroupColAction, 0, 0);
		callCenterHistoryLayout.addComponent(txtRemark, 0, 1);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(callCenterHistoryLayout);
		verticalLayout.setComponentAlignment(callCenterHistoryLayout, Alignment.BOTTOM_CENTER);
		return verticalLayout;
	}
	
	/**
	 * 
	 * @param listener
	 * @return
	 */
	public static CollectionFollowUpPopupPanel show(Contract contract, final Listener listener) {    	
		CollectionFollowUpPopupPanel collectionFollowUpPopupPanel = new CollectionFollowUpPopupPanel(contract);
	    collectionFollowUpPopupPanel.listener = listener;
	    return collectionFollowUpPopupPanel;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			SecUser secUser = UserSessionManager.getCurrentUser();
		    CollectionAction colAction = CollectionAction.createInstance();
			colAction.setContract(this.contract);
			if (optionGroupColAction.isSelected(SCHEDULE)) {
				colAction.setColAction(EColAction.SCHEDULE);
				colAction.setNextActionDate(DateUtils.today());
			} else if (optionGroupColAction.isSelected(SEELATER)) {
				colAction.setColAction(EColAction.SEELATER);
			} else if (optionGroupColAction.isSelected(NOFURTHER)) {
				colAction.setColAction(EColAction.NOFURTHER);
			} else if (optionGroupColAction.isSelected(CANTPRO)) {
				colAction.setColAction(EColAction.CANTPRO);
			} 
			colAction.setComment(txtRemark.getValue());
			colAction.setUserLogin(secUser.getLogin());
			COL_SRV.saveOrUpdateLatestColAction(colAction);
			colPhoneContactHistoryPanel.save();
			close();
		}
		
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(optionGroupColAction)) {
			
		}
	}

	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

}
