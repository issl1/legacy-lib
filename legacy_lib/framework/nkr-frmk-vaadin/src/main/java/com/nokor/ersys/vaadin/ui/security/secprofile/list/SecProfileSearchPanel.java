package com.nokor.ersys.vaadin.ui.security.secprofile.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * ProfileSearchPanel
 * @author phirun.kong
 *
 */
public class SecProfileSearchPanel extends AbstractSearchPanel<SecProfile> {
	/**	 */
	private static final long serialVersionUID = 4087939761886787504L;
	
	private StatusRecordField statusRecordField;
	private TextField txtCode;
	private TextField txtDescEn;
	
	/**
	 * 
	 * @param profileTablePanel
	 */
	public SecProfileSearchPanel(SecProfileTablePanel profileTablePanel) {
		super(I18N.message("search.criteria"), profileTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		
		statusRecordField.clearValues();
	}


	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		statusRecordField = new StatusRecordField();

        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode));
        gridLayout.addComponent(new FormLayout(txtDescEn));
        gridLayout.addComponent(new FormLayout(statusRecordField));
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<SecProfile> getRestrictions() {
		
		BaseRestrictions<SecProfile> restrictions = new BaseRestrictions<SecProfile>(SecProfile.class);
		
		SecUser user = UserSessionManager.getCurrentUser();
		if (!user.isSysAdmin()) {
			List<Criterion> criterions = new ArrayList<Criterion>();
			
			DetachedCriteria subCriteria = DetachedCriteria.forClass(SecManagedProfile.class);
			subCriteria.add(Restrictions.eq("parent.id", user.getDefaultProfile().getId()));
			subCriteria.setProjection(Projections.property("child.id") );
			
			Criterion criterionMySelf = Restrictions.eq("id", user.getDefaultProfile().getId()); 
			Criterion criterionManagedProfiles = Subqueries.propertyIn("id", subCriteria);
			criterions.add(Restrictions.or(criterionManagedProfiles, criterionMySelf));
			
			if (StringUtils.isNotEmpty(txtCode.getValue())) { 
				criterions.add(Restrictions.like("code", txtCode.getValue(), MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
				criterions.add(Restrictions.like("descEn", txtDescEn.getValue(), MatchMode.ANYWHERE));
			}
			
			if (!statusRecordField.getActiveValue() 
					&& !statusRecordField.getInactiveValue()) {
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				restrictions.getStatusRecordList().add(EStatusRecord.INACT);
			} 
			if (statusRecordField.getActiveValue()) {
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			}
			if (statusRecordField.getInactiveValue()) {
				restrictions.getStatusRecordList().add(EStatusRecord.INACT);
			}
			
			restrictions.setCriterions(criterions);
			restrictions.addOrder(Order.asc("descEn"));
		}
		
		return restrictions;
	}

}
