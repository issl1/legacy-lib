package com.nokor.efinance.gui.ui.panel.dashboard.resources;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.vaadin.suggestfield.SuggestField.TokenHandler;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.widget.TokenField;
import com.nokor.ersys.collab.project.model.MTaskEmployeeConfig;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.MStaff;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class ResourceSearchPanel extends AbstractSearchPanel<Employee> implements FinServicesHelper, TokenHandler, MTaskEmployeeConfig {
	
	/** */
	private static final long serialVersionUID = 4660103265955627854L;

//	private ERefDataComboBox<SecProfile> cbxRole; 
	private EntityRefComboBox<SecProfile> cbxRole;
	private ERefDataComboBox<EJobPosition> cbxJobPosition; 
//	private ERefDataComboBox<EGender> cbxGroup; 
	private TextField txtFullName;
	private TokenField tokenField;
	
	private Map<String, String> groupByFields;
	private ResourceTablePanel resourceTablePanel;
	private HorizontalLayout contentLayout;

	/**
	 * 
	 * @param resourceTablePanel
	 */
	public ResourceSearchPanel(ResourceTablePanel resourceTablePanel) {
		super(I18N.message("search"), null);
		this.resourceTablePanel = resourceTablePanel;
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
		
		List<SecProfile> profiles = ENTITY_SRV.list(SecProfile.class);
		//cbxRole = resourceTablePanel.getERefDataComboBox("role", profiles);
		cbxRole = new EntityRefComboBox<>(I18N.message("rold"));
		cbxRole.setRestrictions(new BaseRestrictions<>(SecProfile.class));
		cbxRole.setImmediate(true);
		cbxRole.renderer();
		cbxRole.setWidth(200, Unit.PIXELS);
//		cbxGroup = resourceTablePanel.getERefDataComboBox("group", EGender.values());
		cbxJobPosition = resourceTablePanel.getERefDataComboBox("position", EJobPosition.values());
		txtFullName = ComponentFactory.getTextField("fullname", false, 60, 170);
		
		FormLayout formLayout = resourceTablePanel.getFormLayout("filter.by");
//		formLayout.addComponent(cbxGroup);
		formLayout.addComponent(cbxRole);
		formLayout.addComponent(cbxJobPosition);
		formLayout.addComponent(txtFullName);
		
		tokenField = new TokenField();
		tokenField.setTokenHandler(this);
		
		HorizontalLayout groupByButtonLayout = new HorizontalLayout();
		for (String caption : groupByFields.keySet()) {
			Button btn = new NativeButton(caption);
			btn.addStyleName("btn btn-default");
			btn.addClickListener(new ClickListener() {
				
				/** */
				private static final long serialVersionUID = -961370950208599091L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
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
	public BaseRestrictions<Employee> getRestrictions() {
		BaseRestrictions<Employee> restrictions = new BaseRestrictions<>(Employee.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		
		if (StringUtils.isNotEmpty(txtFullName.getValue())) { 
			restrictions.addCriterion(Restrictions.or(
					Restrictions.ilike(Employee.LASTNAMEEN, txtFullName.getValue(), MatchMode.ANYWHERE),
					Restrictions.ilike(Employee.FIRSTNAMEEN, txtFullName.getValue(), MatchMode.ANYWHERE)));
		}
		
		if (cbxRole.getSelectedEntity() != null) {
			restrictions.addAssociation(Employee.SECUSER, "sec", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("sec." + SecUser.DEFAULTPROFILE, cbxRole.getSelectedEntity()));
		}
	
		if (cbxJobPosition.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(Employee.JOBPOSITION, cbxJobPosition.getSelectedEntity()));
		}
		
		/*if (cbxGroup.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(Employee.GENDER, cbxGroup.getSelectedEntity()));
		}*/
		
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
//		cbxGroup.setSelectedEntity(null);
		cbxRole.setSelectedEntity(null);
		cbxJobPosition.setSelectedEntity(null);
		txtFullName.setValue("");
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
			private static final long serialVersionUID = 3094020855435651960L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				resourceTablePanel.buildTableData(getRestrictions());
			}
		});
	}
	
	/**
	 * Refresh table after saved 
	 */
	public void refreshTable() {
		resourceTablePanel.buildTableData(getRestrictions());
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
		fields.put(I18N.message("role"), MStaff.SECUSER + "." + SecUser.DEFAULTPROFILE + "." + SecProfile.DESCEN);
		fields.put(I18N.message("position"), Employee.JOBPOSITION + "." + EJobPosition.DESCEN);
		fields.put(I18N.message("group"), "");
		return fields;
	}
	
	/**
	 * Get Group By Fields
	 * @return
	 */
	public String[] getGroupByFields() {
		List<String> fields = tokenField.getValue();
		if (fields.isEmpty()) {
			return null;
		}
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
