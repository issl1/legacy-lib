package com.nokor.efinance.ra.ui.panel.dealer.ladder;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LadderTypeFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 3009097491814863392L;
	
	private LadderType ladderType;
	private LadderTypesPanel mainPanel;

    private TabSheet tabLadderType;
    private LadderTypeGeneralPanel ladderTypeGeneralPanel;
    private LadderTypeAttributePanel ladderTypeAttributePanel;
    
    /**
     * 
     */
    public LadderTypeFormPanel() {
		super.init();
    }
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() { 
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabLadderType = new TabSheet();		
		ladderTypeGeneralPanel = new LadderTypeGeneralPanel(this);
		ladderTypeAttributePanel = new LadderTypeAttributePanel();
		
		tabLadderType.addTab(ladderTypeGeneralPanel, I18N.message("general"));
		tabLadderType.setSelectedTab(ladderTypeGeneralPanel);

	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(tabLadderType);
		
		return contentLayout;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long ladTypId) {
		super.reset();
		if (ladTypId != null) {
			ladderType = ENTITY_SRV.getById(LadderType.class, ladTypId);
			ladderTypeGeneralPanel.assignValue(ladderType);
			ladderTypeAttributePanel.assignValue(ladderType);
			
			tabLadderType.setSelectedTab(ladderTypeGeneralPanel);
			tabLadderType.addTab(ladderTypeAttributePanel, I18N.message("attributes"));
		} else {
			tabLadderType.removeComponent(ladderTypeAttributePanel);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		tabLadderType.removeComponent(ladderTypeAttributePanel);
		ladderTypeGeneralPanel.reset();
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}
	

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(LadderTypesPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**
	 * @param needRefresh
	 */
	public void setNeedRefresh(boolean needRefresh) {
		mainPanel.getTabSheet().setNeedRefresh(needRefresh);
	}
}
