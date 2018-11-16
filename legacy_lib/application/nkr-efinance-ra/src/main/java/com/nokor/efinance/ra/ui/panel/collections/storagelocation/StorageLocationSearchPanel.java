package com.nokor.efinance.ra.ui.panel.collections.storagelocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.StorageLocation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Search Groups
 * @author buntha.chea
 *
 */
public class StorageLocationSearchPanel extends AbstractSearchPanel<StorageLocation> implements FMEntityField {
	
	private static final long serialVersionUID = -7066072217405732128L;

	private TextField txtDescEn;

	/**
	 * @param storageLocationTablePanel The Groups Table panel
	 */
	public StorageLocationSearchPanel(StorageLocationTablePanel storageLocationTablePanel) {
		super(I18N.message("search"), storageLocationTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(new FormLayout(txtDescEn));
		
		return horizontalLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<StorageLocation> getRestrictions() {
		BaseRestrictions<StorageLocation> restrictions = new BaseRestrictions<>(StorageLocation.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}	

		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(ID));
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
	}
}
