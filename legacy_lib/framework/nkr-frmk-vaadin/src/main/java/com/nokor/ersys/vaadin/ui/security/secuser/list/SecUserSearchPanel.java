package com.nokor.ersys.vaadin.ui.security.secuser.list;

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
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * UserSearchPanel
 * @author phirun.kong
 *
 */
public class SecUserSearchPanel extends AbstractSearchPanel<SecUser> {

	/**	 */
	private static final long serialVersionUID = 5169824768727403240L;
	
	private StatusRecordField statusRecordField;
	private TextField txtLogin;
	private TextField txtNameEn;
	private EntityRefComboBox<SecProfile> cbxProfile;
	
	public SecUserSearchPanel(SecUserTablePanel dealerTablePanel) {
		super(I18N.message("search.criteria"), dealerTablePanel);
	}
	
	@Override
	protected void reset() {
		txtLogin.setValue("");
		txtNameEn.setValue("");
		cbxProfile.setSelectedEntity(null);

		statusRecordField.clearValues();
	}
	
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(4, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
				
		txtLogin = ComponentFactory.getTextField(I18N.message("login"), false, 60, 150);         		
		txtNameEn = ComponentFactory.getTextField(I18N.message("name.en"), false, 60, 150);
		
		cbxProfile = new EntityRefComboBox<SecProfile>(I18N.message("profile"));
		BaseRestrictions<SecProfile> resProfile = new BaseRestrictions<SecProfile>(SecProfile.class);
		resProfile.addCriterion(Restrictions.ne("id", SecProfile.ADMIN.getId()));
        cbxProfile.setRestrictions(resProfile);
        cbxProfile.renderer();
        
        statusRecordField = new StatusRecordField();

        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtLogin));
        gridLayout.addComponent(new FormLayout(txtNameEn));
        gridLayout.addComponent(new FormLayout(cbxProfile));
        gridLayout.addComponent(new FormLayout(statusRecordField));
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<SecUser> getRestrictions() {
		
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<SecUser>(SecUser.class);
		List<Criterion> criterions = restrictions.getCriterions();
		criterions.add(Restrictions.ne("id", SecurityHelper.DEFAULT_ADMIN_ID));
		criterions.add(Restrictions.ne("id", SecurityHelper.DEFAULT_ANONYMOUS_ID));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(SecManagedProfile.class);
		subCriteria.add(Restrictions.eq("parent.id", UserSessionManager.getCurrentUser().getDefaultProfile().getId()));
		subCriteria.setProjection(Projections.property("child.id") );
		criterions.add(Subqueries.propertyIn("defaultProfile.id", subCriteria));
		
		if (StringUtils.isNotEmpty(txtLogin.getValue())) { 
			criterions.add(Restrictions.like("login", txtLogin.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtNameEn.getValue())) { 
			criterions.add(Restrictions.like("desc", txtNameEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxProfile.getSelectedEntity() != null) {
			criterions.add(Restrictions.eq("defaultProfile.id", cbxProfile.getSelectedEntity().getId()));
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
		
		restrictions.addOrder(Order.asc("desc"));
		
		return restrictions;
	}

}
