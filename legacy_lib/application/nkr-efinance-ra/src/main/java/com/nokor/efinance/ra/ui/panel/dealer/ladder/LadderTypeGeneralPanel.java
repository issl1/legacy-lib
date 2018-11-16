package com.nokor.efinance.ra.ui.panel.dealer.ladder;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
/**
 * @author buntha.chea
 *
 */
public class LadderTypeGeneralPanel extends AbstractTabPanel implements SaveClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 1540431489332098224L;
	
	private LadderType ladderType;
	private CheckBox cbActive;
    private TextField txtDescEn;
    
    private LadderTypeFormPanel ladderTypeFormPanel;
    
    public LadderTypeGeneralPanel(LadderTypeFormPanel ladderTypeFormPanel) {
    	super();
		this.ladderTypeFormPanel = ladderTypeFormPanel;
		NavigationPanel navigationPanel = new NavigationPanel();
	    navigationPanel.addSaveClickListener(this);
	    addComponent(navigationPanel, 0);
	}

	@Override
	protected Component createForm() {
		final FormLayout formPanel = new FormLayout();
		formPanel.setStyleName("myform-align-left");	
		txtDescEn = ComponentFactory.getTextField("desc", true, 60, 200);        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	@Override
	public void reset() {
		super.reset();
		removeErrorsPanel();
		ladderType = new LadderType();
		txtDescEn.setValue("");		
		cbActive.setValue(true);
		markAsDirty();
	}
	
	private LadderType getEntity() {
		ladderType.setDesc(txtDescEn.getValue());
		ladderType.setDescEn(txtDescEn.getValue());
		ladderType.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return ladderType;
	}
	
	/**
	 * 
	 * @param ladderType
	 */
	public void assignValue(LadderType ladderType) {
		super.reset();
		removeErrorsPanel();
		if (ladderType != null) {
			txtDescEn.setValue(ladderType.getDescEn());
			cbActive.setValue(ladderType.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		removeErrorsPanel();
		checkMandatoryField(txtDescEn, "desc");		
		return errors.isEmpty();
	}
	
	@Override
	public void saveButtonClick(ClickEvent arg0) {
		if (validate()) {
			ladderType = getEntity();
			ENTITY_SRV.saveOrUpdate(ladderType);
			ladderTypeFormPanel.assignValues(ladderType.getId());
			ladderTypeFormPanel.setNeedRefresh(true);
			displaySuccess();
		} else {
			displayErrorsPanel();
		}
		
	}
}
