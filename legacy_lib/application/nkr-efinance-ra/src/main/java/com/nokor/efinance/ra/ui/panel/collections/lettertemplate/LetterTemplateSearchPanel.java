package com.nokor.efinance.ra.ui.panel.collections.lettertemplate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.ELetterTemplate;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
public class LetterTemplateSearchPanel extends AbstractSearchPanel<ELetterTemplate> implements AssetEntityField {

	private static final long serialVersionUID = -4478589367053917211L;
	
	private TextField txtCode;
	private TextField txtDescEn;
	
	
	public LetterTemplateSearchPanel(LetterTemplateTablePanel letterTamplateTablePanel) {
		super(I18N.message("search"), letterTamplateTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
	}


	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(5, 1);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
    
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<ELetterTemplate> getRestrictions() {
		
		BaseRestrictions<ELetterTemplate> restrictions = new BaseRestrictions<>(ELetterTemplate.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		
		return restrictions;		
		
	}

}
