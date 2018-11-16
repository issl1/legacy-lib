package com.nokor.ersys.vaadin.ui.scheduler.list;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.scheduler.model.ScTriggerTask;
import com.nokor.common.app.scheduler.service.SchedulerRestriction;
import com.nokor.ersys.vaadin.ui.scheduler.SchedulerConstants;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author phirun.kong
 *
 */
public class SchedulerSearchPanel extends AbstractSearchPanel<ScTriggerTask> implements SchedulerConstants {
	/** */
	private static final long serialVersionUID = -9210346245963638796L;
	private TextField txtDesc;
	
	/**
	 * 
	 * @param listPanel
	 */
	public SchedulerSearchPanel(SchedulerListPanel listPanel) {
		super(I18N.message("search"), listPanel);
	}
	
	@Override
	protected void reset() {
		txtDesc.setValue("");
	}


	@Override
	protected Component createForm() {
		
		final GridLayout gridLayout = new GridLayout(1, 1);
		
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);        
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtDesc), 0, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<ScTriggerTask> getRestrictions() {
		SchedulerRestriction restrictions = new SchedulerRestriction();
		List<Criterion> criterions = new ArrayList<Criterion>();
		restrictions.setCriterions(criterions);
		return restrictions;
	}

}
