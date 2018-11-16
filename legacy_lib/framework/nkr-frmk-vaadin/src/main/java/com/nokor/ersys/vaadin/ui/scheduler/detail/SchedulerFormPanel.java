package com.nokor.ersys.vaadin.ui.scheduler.detail;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.scheduler.model.ScFrequency;
import com.nokor.common.app.scheduler.model.ScTask;
import com.nokor.common.app.scheduler.model.ScTriggerTask;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SchedulerFormPanel extends AbstractFormPanel implements PrintClickListener, VaadinServicesHelper {

	/**	 */
	private static final long serialVersionUID = 1603588597111954725L;

	private ScTriggerTask scheduler;

	private TextField txtDesc;
	private EntityRefComboBox<ScTask> cboTask;
	private EntityRefComboBox<ScFrequency> cboFrequency;
	private TextField txtDay;
	private TextField txtHours;
    private TextField txtMinutes;
    private TextField txtExpression;
    private TextField txtComment;

	private static final ThemeResource errorIcon = new ThemeResource("../nkr-default/icons/16/close.png");
	
	
	
    @PostConstruct
	public void PostConstruct() {
    	super.init();
        setCaption(I18N.message("scheduler.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    


	@Override
	protected com.vaadin.ui.Component createForm() {
		txtDesc = ComponentFactory.getTextField("desc", true, 60, 400);
		
		cboTask = new EntityRefComboBox<ScTask>(I18N.message("task"));
		cboTask.setRestrictions(new BaseRestrictions<ScTask>(ScTask.class));
		cboTask.setRequired(true);
		cboTask.renderer();
		cboTask.setWidth(400, Unit.PIXELS);
		cboTask.setInputPrompt("--select--");
		
		cboFrequency = new EntityRefComboBox<ScFrequency>(I18N.message("frequency"));
		cboFrequency.setRestrictions(new BaseRestrictions<ScFrequency>(ScFrequency.class));
		cboFrequency.setRequired(true);
		cboFrequency.renderer();
		cboFrequency.setWidth(400, Unit.PIXELS);
		cboFrequency.setInputPrompt("--select--");
		
		txtDay = ComponentFactory.getTextField("day", false, 60, 400);
		txtHours = ComponentFactory.getTextField("hours", false, 60, 400);
		txtMinutes = ComponentFactory.getTextField("minutes", false, 60, 400);
		txtExpression = ComponentFactory.getTextField("expression", false, 60, 400);
		txtComment = ComponentFactory.getTextField("comment", false, 60, 400);
		
		Panel detailPanel = new Panel(I18N.message("detail"));
        detailPanel.setSizeFull();
        
        FormLayout formDetailPanel = new FormLayout();
        formDetailPanel.setMargin(true);
        formDetailPanel.setSizeFull();
        formDetailPanel.addComponents(txtDesc, cboTask, cboFrequency, txtDay, txtHours, txtMinutes, txtExpression, txtComment);
        
        detailPanel.setContent(formDetailPanel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(detailPanel);
        
		return verticalLayout;
	}

	@Override
	protected Entity getEntity() {
		scheduler.setDesc(txtDesc.getValue());
		scheduler.setTask(cboTask.getSelectedEntity());
		scheduler.setFrequency(cboFrequency.getSelectedEntity());
		scheduler.setDay(Integer.parseInt(txtDay.getValue()));
		scheduler.setHours(Integer.parseInt(txtHours.getValue()));
		scheduler.setMinutes(Integer.parseInt(txtMinutes.getValue()));
		scheduler.setExpression(txtExpression.getValue());
		scheduler.setComment(txtComment.getValue());
		return scheduler;
	}
	
    /**
	 * @param schedulerId
	 */
	public void assignValues(Long schedulerId) {
		super.reset();
		if (schedulerId == null) {
			reset();
			return;
		}
		scheduler = ENTITY_SRV.getById(ScTriggerTask.class, schedulerId);
		txtDesc.setValue(scheduler.getDesc());
		cboTask.setSelectedEntity(scheduler.getTask());
		cboFrequency.setSelectedEntity(scheduler.getFrequency());
		txtDay.setValue(scheduler.getDay() != null ? scheduler.getDay().toString() : "");
		txtHours.setValue(scheduler.getHours() != null ? scheduler.getHours().toString() : "");
		txtMinutes.setValue(scheduler.getMinutes() != null ? scheduler.getMinutes().toString() : "");
		txtExpression.setValue(scheduler.getExpression());
		txtComment.setValue(scheduler.getComment());
	}
	
	@Override
	public void saveEntity() {
		ENTITY_SRV.create(scheduler);
	}
	
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		scheduler = ScTriggerTask.createInstance();		
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		boolean isValid = true;
		if (StringUtils.isEmpty(txtDesc.getValue())) {
			errors.add(I18N.message("field.required.1", txtDesc.getCaption()));
			isValid = false;
		}
		return isValid;
	}

	@Override
	public void printButtonClick(ClickEvent event) {
		
	}
}
