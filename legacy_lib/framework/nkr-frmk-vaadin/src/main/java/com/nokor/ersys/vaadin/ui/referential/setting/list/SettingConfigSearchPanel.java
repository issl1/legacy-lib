/*
 * Created on 29/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.setting.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EStatusRecordOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Setting Config search
 * @author phirun.kong
 *
 */
public class SettingConfigSearchPanel extends AbstractSearchPanel<SettingConfig> {

	/**	 */
	private static final long serialVersionUID = -297418699392107923L;

	private TextField txtCode;
	private TextField txtDesc;
	private EStatusRecordOptionGroup cbStatuses;
	
	public SettingConfigSearchPanel(SettingConfigTablePanel settingTablePanel) {
		super(I18N.message("search"), settingTablePanel);
	}
	
	@Override
	protected Component createForm() {		
		
		final GridLayout gridLayout = new GridLayout(4, 1);	
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		txtDesc = ComponentFactory.getTextField("desc.search", false, 60, 200);
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);
		cbStatuses = new EStatusRecordOptionGroup();

        gridLayout.setSpacing(true);
		gridLayout.addComponent(new FormLayout(txtCode));
        gridLayout.addComponent(new FormLayout(txtDesc));
        gridLayout.addComponent(new FormLayout(cbStatuses));  
		return gridLayout;
	}

	@Override
	public BaseRestrictions<SettingConfig> getRestrictions() {
		BaseRestrictions<SettingConfig> restrictions = new BaseRestrictions<SettingConfig>(SettingConfig.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtDesc.getValue())) { 
			criterions.add(Restrictions.like("descEn", txtDesc.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtCode.getValue())) {
			criterions.add(Restrictions.like("code", txtCode.getValue(), MatchMode.ANYWHERE));
		}
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		restrictions.setStatusRecordList(new ArrayList(colSta));
		
		criterions.add(Restrictions.or(Restrictions.eq("visible", Boolean.TRUE), Restrictions.isNull("visible")));
		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.desc("id"));
		return restrictions;
	}

	@Override
	protected void reset() {
		txtDesc.setValue("");
		txtCode.setValue("");
		cbStatuses.setDefaultValue();
	}	
	
}
