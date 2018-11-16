package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
/**
 * 
 * @author buntha.chea
 *
 */
public class ResultFormPopupPanel extends Window implements FinServicesHelper, ClickListener {

	private static final long serialVersionUID = -4042033968851135017L;
	
	private EColResult colResult;
    private TextField txtCode;
    private TextField txtDescEn;
    private TextField txtDesc;
    
    private Button btnSave;
    private Button btnCancel;
    
    private VerticalLayout messagePanel;
    private ResultsTablePanel resultsTablePanel;
    
    /**
     * 
     */
    public ResultFormPopupPanel(ResultsTablePanel resultsTablePanel) {
    	this.resultsTablePanel = resultsTablePanel;
    	setModal(true);
    	setCaption(I18N.message("result"));
    	setHeight("235px");
    	
    	init();
    	
    	FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(formLayout);
		
		setContent(mainLayout);
	}
    
    /**
     * init
     */
    private void init() {
    	txtCode = ComponentFactory.getTextField("code", true, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
    }
    /**
     * Result
     */
    public void reset() {
    	colResult = new EColResult();
    	txtCode.setValue("");
    	txtDescEn.setValue("");
    	txtDesc.setValue("");
    }
    
    /**
     * Assign Value
     * @param colResult
     */
    public void assignValue(Long colResultId) {
    	reset();
    	if (colResultId != null) {
    		this.colResult = ENTITY_SRV.getById(EColResult.class, colResultId);
    		txtCode.setValue(colResult.getCode());
        	txtDescEn.setValue(colResult.getDescEn());
        	txtDesc.setValue(colResult.getDesc());
    	}
    }
    
    /**
     * Validate
     * @return
     */
    private boolean validate() {
    	messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		List<String> errors = new ArrayList<String>();
		Label messageLabel;
		
		if (StringUtils.isEmpty(txtCode.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("code") }));
		}
		
		if (StringUtils.isEmpty(txtDescEn.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("desc.en") }));
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
     * save
     */
    private void save() {
    	if (validate()) {
    		colResult.setCode(txtCode.getValue());
    		colResult.setDescEn(txtDescEn.getValue());
    		colResult.setDesc(txtDesc.getValue());
    		
    		if (ProfileUtil.isColPhoneSupervisor()) {
    			colResult.setColTypes(EColType.PHONE);
    		} else if (ProfileUtil.isColFieldSupervisor()) {
    			colResult.setColTypes(EColType.FIELD);
    		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
    			colResult.setColTypes(EColType.INSIDE_REPO);
    		}
    		
    		try {
    			ENTITY_SRV.saveOrUpdate(colResult);
    			close();
    			ComponentLayoutFactory.displaySuccessfullyMsg();
    			resultsTablePanel.refresh();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

}
