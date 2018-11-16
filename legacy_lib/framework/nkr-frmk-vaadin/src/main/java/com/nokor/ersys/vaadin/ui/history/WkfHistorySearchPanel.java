/*
 * Created on 30/06/2015.
 */
package com.nokor.ersys.vaadin.ui.history;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EMainEntity;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.workflow.model.WkfHistory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;

/**
 * History Search Panel.
 * 
 * @author pengleng.huot
 *
 */
public class WkfHistorySearchPanel extends AbstractSearchPanel<WkfHistory> implements ValueChangeListener, AppServicesHelper {
	/** */
	private static final long serialVersionUID = 7398321147989916576L;
	
	public final static String TYPE_DEFAULT = "history.type.default";
	public final static String TYPE_CUSTOM = "history.type.custom";

	private ComboBox typeField;
	private ERefDataComboBox<EMainEntity> enityField;
	private ComboBox customEnityField;
	
	public WkfHistorySearchPanel(WkfHistoryTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	@Override
	protected Component createForm() {
		typeField = new ComboBox(I18N.message("history.type"));
		typeField.setNullSelectionAllowed(false);
		typeField.setImmediate(true);
		typeField.setTextInputAllowed(false);
		typeField.setEnabled(false);
		
		typeField.addItem(TYPE_DEFAULT);
		typeField.setItemCaption(TYPE_DEFAULT, I18N.message(TYPE_DEFAULT));
		typeField.setValue(TYPE_DEFAULT);

		BaseRestrictions<EMainEntity> restrictions = new BaseRestrictions<EMainEntity>(EMainEntity.class);
		restrictions.addCriterion(Restrictions.or(
				Restrictions.eq(EMainEntity.ISCUSTOMHISTORY, false),
				Restrictions.isNull(EMainEntity.ISCUSTOMHISTORY)
								));
		enityField = new ERefDataComboBox<EMainEntity>(I18N.message("history.entity"), ENTITY_SRV.list(restrictions));
		enityField.setNullSelectionAllowed(false);
		

		customEnityField = new ComboBox(I18N.message("history.entity"));
		customEnityField.setNullSelectionAllowed(false);
		customEnityField.setVisible(false);

		final GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(typeField));
        gridLayout.addComponent(new FormLayout(enityField, customEnityField));
        
        return gridLayout;
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().getValue().equals(TYPE_DEFAULT)) {
			enityField.setVisible(true);
			customEnityField.setVisible(false);
			customEnityField.setValue(null);
		} else {
			enityField.setVisible(false);
			customEnityField.setVisible(true);
		}
	}
	
	public void assignCustomEntityValues(List<String> values) {
		// Assign Type Value - Custom
		typeField.addItem(TYPE_CUSTOM);
		typeField.setItemCaption(TYPE_CUSTOM, I18N.message(TYPE_CUSTOM));
		typeField.setValue(TYPE_DEFAULT);
		typeField.setEnabled(true);
		
		typeField.addValueChangeListener(this);
		
		// Assign Custom Entity Values
		for (String value : values) {
			customEnityField.addItem(value);
			customEnityField.setItemCaption(value, I18N.message(value));
		}
	}
	
	@Override
	protected void reset() {
		typeField.setValue(TYPE_DEFAULT);
		enityField.setSelectedEntity(null);
		customEnityField.setValue(null);
	}
	
	public String getType() {
		return (String) typeField.getValue();
	}
	
	public String getCustomValue() {
		return (String) customEnityField.getValue();
	}

	@Override
	public BaseRestrictions<WkfHistory> getRestrictions() {
		BaseRestrictions<WkfHistory> restrictions = new BaseRestrictions<WkfHistory>(WkfHistory.class);
		
		if (enityField.getSelectedEntity() != null) {
			restrictions.addCriterion("entity", enityField.getSelectedEntity());
		}
		
		restrictions.setOrder(Order.desc("createDate"));
		return restrictions;
	}

}
