package com.nokor.efinance.ra.ui.panel.dealer.group;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
public class DealerGroupGeneralPanel extends AbstractFormPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4685219681765939045L;
	
	private DealerGroupFormPanel dealerGroupFormPanel;
	private DealerGroup dealerGroup;
	
	private CheckBox cbActive;
    private TextField txtDescEn;
   // private EntityComboBox<LadderType> cbxLadderType;
    
    public DealerGroupGeneralPanel(DealerGroupFormPanel dealerGroupFormPanel) {
    	 super.init();
		 this.dealerGroupFormPanel = dealerGroupFormPanel;
		 NavigationPanel navigationPanel = addNavigationPanel();
		 navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected Entity getEntity() {
		dealerGroup.setDesc(txtDescEn.getValue());
		dealerGroup.setDescEn(txtDescEn.getValue());
		//dealerGroup.setLadderType(cbxLadderType.getSelectedEntity());
		dealerGroup.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return dealerGroup;
	}
	
	@Override
	public void saveEntity() {
		DealerGroup dealerGroup = (DealerGroup) getEntity();
		ENTITY_SRV.saveOrUpdate(dealerGroup);
		displaySuccess();
		dealerGroupFormPanel.assignValues(dealerGroup.getId());
		dealerGroupFormPanel.setNeedRefresh(true);
	};

	@Override
	protected Component createForm() {
		final FormLayout formPanel = new FormLayout();
		formPanel.setStyleName("myform-align-left");
		// txtCode = ComponentFactory.getTextField("code", true, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc", true, 60, 200);        
		// txtDesc = ComponentFactory.getTextField35("brand.name", false, 60, 200);
		//cbxLadderType =  new EntityComboBox<>(LadderType.class, I18N.message("ladder"), LadderType.DESCEN, "");
		//cbxLadderType.renderer();
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        // formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        //formPanel.addComponent(cbxLadderType);
        // formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * 
	 * @param dealerGroupid
	 */
	public void assignValues(Long dealerGroupid) {
		reset();
		if (dealerGroupid != null) {
			dealerGroup = ENTITY_SRV.getById(DealerGroup.class, dealerGroupid);
			txtDescEn.setValue(getDefaultString(dealerGroup.getDescEn()));
			//cbxLadderType.setSelectedEntity(dealerGroup.getLadderType());
			cbActive.setValue(dealerGroup.isActive());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void reset() {
		super.reset();
		dealerGroup = new DealerGroup();
		txtDescEn.setValue("");
		//cbxLadderType.setSelectedEntity(null);
		cbActive.setValue(true);
		markAsDirty();	
	}
	
	/**
	 * 
	 */
	@Override
	public boolean validate() {
		checkMandatoryField(txtDescEn, "desc");		
		return errors.isEmpty();
	}

}
