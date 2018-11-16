package com.nokor.efinance.gui.ui.panel.dashboard.task;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.suggestfield.SuggestField.TokenHandler;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.widget.TokenField;
import com.nokor.ersys.collab.project.model.Task;
import com.nokor.ersys.collab.project.service.TaskRestriction;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.MStaff;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

/**
 * Task Search Panel
 * @author bunlong.taing
 */
public class TaskSearchPanel extends AbstractSearchPanel<Task> implements TokenHandler {
	/** */
	private static final long serialVersionUID = 6792584343776105487L;
	
	private AutoDateField dfDeadline;
	
	private ComboBox cbxDaysRemaining;
	private ERefDataComboBox<EWkfStatus> cbxStatus;
	//private ERefDataComboBox<SecProfile> cbxRole;
	private EntityRefComboBox<SecProfile> cbxRole;
	private ERefDataComboBox<EJobPosition> cbxPosition;
	private TokenField tokenField;
	
	private Map<String, String> groupByFields;
	private TaskFormPanel formPanel;
	private HorizontalLayout contentLayout;

	/**
	 */
	public TaskSearchPanel(TaskFormPanel formPanel) {
		super(I18N.message("search"), null);
		this.formPanel = formPanel;
		setUpSearchButton();
		addControlsToForm();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		contentLayout = new HorizontalLayout();
		contentLayout.setSpacing(true);
		return contentLayout;
	}
	
	/**
	 * Add Controls To Form
	 */
	private void addControlsToForm() {
		groupByFields = getButtonCaptionList();
		
		cbxDaysRemaining = new ComboBox(I18N.message("days.remaining"));
		cbxDaysRemaining.setWidth(200, Unit.PIXELS);
		
		cbxStatus = new ERefDataComboBox<EWkfStatus>(I18N.message("status"), EWkfStatus.class);
		cbxStatus.setWidth(200, Unit.PIXELS);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setCaption(I18N.message("filter.by"));
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		
		if (formPanel.isMyTask()) {
			dfDeadline = new AutoDateField(I18N.message("deadline"));
			dfDeadline.setValue(DateUtils.today());
			formLayout.addComponent(dfDeadline);
		}
		formLayout.addComponent(cbxDaysRemaining);
		formLayout.addComponent(cbxStatus);
		
		if (!formPanel.isMyTask()) {
			//cbxRole = new ERefDataComboBox<SecProfile>(I18N.message("role"), SecProfile.class);
			cbxRole = new EntityRefComboBox<>(I18N.message("rold"));
			cbxRole.setRestrictions(new BaseRestrictions<>(SecProfile.class));
			cbxRole.setImmediate(true);
			cbxRole.renderer();
			cbxRole.setWidth(200, Unit.PIXELS);
			
			cbxPosition = new ERefDataComboBox<EJobPosition>(I18N.message("position"), EJobPosition.class);
			cbxPosition.setWidth(200, Unit.PIXELS);

			formLayout.addComponent(cbxRole);
			formLayout.addComponent(cbxPosition);
		}
		
		tokenField = new TokenField();
		tokenField.setTokenHandler(this);
		
		HorizontalLayout groupByButtonLayout = new HorizontalLayout();
		for (String caption : groupByFields.keySet()) {
			Button btn = new NativeButton(caption);
			btn.addStyleName("btn btn-default");
			btn.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = 3811684762362430735L;
				@Override
				public void buttonClick(ClickEvent event) {
					if (!tokenField.getValue().contains(btn.getCaption())) {
						tokenField.addToken(btn.getCaption());
					}
				}
			});
			groupByButtonLayout.addComponent(btn);
		}
		
		VerticalLayout groupByLayout = new VerticalLayout();
		groupByLayout.setCaption(I18N.message("group.by"));
		groupByLayout.setMargin(true);
		groupByLayout.setSpacing(true);
		groupByLayout.addComponent(groupByButtonLayout);
		groupByLayout.addComponent(tokenField);
		
		contentLayout.addComponent(formLayout);
		contentLayout.addComponent(groupByLayout);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public TaskRestriction getRestrictions() {
		TaskRestriction restrictions = new TaskRestriction();
		restrictions.setTaskType(formPanel.getTaskType());
		if (formPanel.isMyTask()) {
			restrictions.setDeadline(dfDeadline.getValue());
		}
		if (!formPanel.isMyTask()) {
			restrictions.setRole(cbxRole.getSelectedEntity());
			restrictions.setJobPosition(cbxPosition.getSelectedEntity());
		}
		restrictions.setStatus(cbxStatus.getSelectedEntity());
		restrictions.addOrder(Order.asc(Task.ID));
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		if (formPanel.isMyTask()) {
			dfDeadline.setValue(DateUtils.today());
		} else {
			cbxRole.setSelectedEntity(null);
			cbxPosition.setSelectedEntity(null);
		}
		cbxDaysRemaining.setValue(null);
		cbxStatus.setSelectedEntity(null);
		tokenField.setTokens(null);
	}
	
	/**
	 * Set up Search Button
	 */
	private void setUpSearchButton() {
		Collection<?> listeners = btnSearch.getListeners(ClickEvent.class);
		for (Object object : listeners) {
			btnSearch.removeClickListener((ClickListener) object);
		}
		btnSearch.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 3811684762362430735L;
			@Override
			public void buttonClick(ClickEvent event) {
				formPanel.buildTableData(getRestrictions());
			}
		});
	}

	/**
	 * @see org.vaadin.suggestfield.SuggestField.TokenHandler#handleToken(java.lang.Object)
	 */
	@Override
	public void handleToken(Object token) {
		if (token != null) {
			final String strToken = (String) token;
			if (!tokenField.getValue().contains(strToken)
					&& groupByFields.keySet().contains(strToken)) {
				tokenField.addToken(strToken);
			}
		}
	}
	
	/**
	 * Get Button Caption List
	 * @return
	 */
	public Map<String, String> getButtonCaptionList() {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put(I18N.message("status"), Task.WKFSTATUS + "." + EWkfStatus.DESCEN);
		fields.put(I18N.message("deadline"), Task.DEADLINE);
		fields.put(I18N.message("days.remaining"), Task.DATEREMAINING);
		if (!formPanel.isMyTask()) {
			fields.put(I18N.message("role"), Task.ASSIGNEE + "." + MStaff.SECUSER + "." + SecUser.DEFAULTPROFILE + "." + SecProfile.DESCEN);
			fields.put(I18N.message("assignee"), Task.ASSIGNEE + ".fullName");
			fields.put(I18N.message("position"), Task.ASSIGNEE + "." + Employee.JOBPOSITION + "." + EJobPosition.DESCEN);
			fields.put(I18N.message("group"), "");
		}
		return fields;
	}
	
	/**
	 * Get Group By Fields
	 * @return
	 */
	public String[] getGroupByFields() {
		List<String> fields = tokenField.getValue();
		String[] groupFields = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			groupFields[i] = groupByFields.get(fields.get(i));
		}
		return groupFields;
	}
	
	/**
	 * Get Group By Fields Caption
	 * @return
	 */
	public String[] getGroupByFieldsCaption() {
		List<String> fields = tokenField.getValue();
		if (fields.isEmpty()) {
			return null;
		}
		return fields.toArray(new String[fields.size()]);
	}
}
