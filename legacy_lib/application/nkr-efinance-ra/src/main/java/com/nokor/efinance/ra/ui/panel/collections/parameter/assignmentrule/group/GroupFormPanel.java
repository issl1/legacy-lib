package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.group;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Group form panel in collection 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GroupFormPanel extends AbstractFormPanel {
	
	/** */
	private static final long serialVersionUID = -7174464620079889226L;
	
	private EColGroup colGroup;
	private TextField txtCode;
	private TextField txtDescEn;
	private TextField txtDesc;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		colGroup.setCode(txtCode.getValue());
		colGroup.setDescEn(txtDescEn.getValue());
		colGroup.setDesc(txtDesc.getValue());
		return colGroup;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		VerticalLayout teamLayout = ComponentFactory.getVerticalLayout();
		teamLayout.setSpacing(true);
		teamLayout.addComponent(new FormLayout(txtCode));
		teamLayout.addComponent(new FormLayout(txtDescEn));
		teamLayout.addComponent(new FormLayout(txtDesc));
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setSizeFull();
		mainPanel.setContent(teamLayout);
		return mainPanel;
	}
	
	/**
	 * @param colGroupID
	 */
	public void assignValues(Long colGroupID) {
		super.reset();
		if (colGroupID != null) {
			colGroup = ENTITY_SRV.getById(EColGroup.class, colGroupID);
			txtCode.setValue(colGroup.getCode());
			txtDescEn.setValue(colGroup.getDescEn());
			txtDesc.setValue(colGroup.getDesc());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		colGroup = new EColGroup();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
}
